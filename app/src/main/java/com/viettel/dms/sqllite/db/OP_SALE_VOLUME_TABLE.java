/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDetailDTO;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * @author dungdq3
 *
 */
public class OP_SALE_VOLUME_TABLE extends ABSTRACT_TABLE {

	// id kho
	public static final String OP_SALE_VOLUME_ID = "OP_SALE_VOLUME_ID";
	// id doi thu
	public static final String OPPONENT_ID = "OPPONENT_ID";
	// id san pham doi thu
	public static final String OP_PRODUCT_ID = "OP_PRODUCT_ID";
	// so luong
	public static final String QUANTITY = "QUANTITY";
	// ma khach hang kiem kho
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// ma nhan vien kiem kho
	public static final String STAFF_ID = "STAFF_ID";
	//trang thai
	public static final String STATUS = "STATUS";
	// ngay kiem kho
	public static final String SALE_DATE = "SALE_DATE";
	// ngay tao don
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String OP_SALE_VOLUME_TABLE = "OP_SALE_VOLUME";
	// Type: 1 - BSG, 0- doi thu
	public static final String TYPE = "TYPE";
	// Type: 0 - Thu thap du lieu thi truong, duoc edit, 1- lay tu HTBH , khong duoc edit
	public static final String ACTION_TYPE = "ACTION_TYPE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	
	public OP_SALE_VOLUME_TABLE(SQLiteDatabase mDB){
		this.tableName = OP_SALE_VOLUME_TABLE;

		this.columns = new String[] {OP_SALE_VOLUME_ID, OPPONENT_ID, OP_PRODUCT_ID, QUANTITY, CUSTOMER_ID, STAFF_ID, ACTION_TYPE, STATUS,
				SALE_DATE, CREATE_DATE};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		sqlInsert+=tableName+" ( ";
		this.mDB = mDB;
	}
	
	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#insert(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected long insertDataFromHTBH(CustomerInputQuantityDetailDTO detail, String staffId, String staffCode) {
		ContentValues values = initDataFromHTBH(detail, staffId, staffCode);
		return insert(null, values);
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#update(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.sqllite.db.ABSTRACT_TABLE#delete(com.viettel.dms.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	* Kiem tra xem co can kiem ban hang ko!?
	* @author: dungdq3
	* @param: Bundle data
	* @return: int
	*/
	public long checkSaled(Bundle data) {
		// TODO Auto-generated method stub
		String cusID=data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		long staffID=data.getLong(IntentConstants.INTENT_STAFF_ID);
		String sql="select OP_SALE_VOLUME_ID from OP_SALE_VOLUME where Date(SALE_DATE)=Date(?) and customer_id=? and staff_id=?;";
		String[] params={DateUtils.now() ,cusID, String.valueOf(staffID)};
		long check=-1;
		Cursor cursor=null;
		try {
			cursor = rawQuery(sql.toString(), params);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					if (cursor.getColumnIndex(OP_SALE_VOLUME_ID) >= 0) {
						check= cursor.getLong(cursor.getColumnIndex(OP_SALE_VOLUME_ID));
					} else {
						check = -1;
					}
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {
			}
		}
		return check;
	}

	/**
	* insert danh sach cac san pham sau khi kiem ban
	* @author: dungdq3
	 * @param maxID 
	* @param: Tham số của hàm
	* @return: Kết qủa trả về
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	public long saveSaleVolume(ProductCompetitorDTO product, long staffID, String staffCode,
			long cusID, String date, int type, long maxID) { 
		long success=-2;
		try{
			for(OpProductDTO opProduct: product.getArrProduct()){ 
				opProduct.setMaxID(maxID);
				
				// Lay loai request db
				int typeRequest = opProduct.getTypeRequestDB();
				
				// Insert
				if(typeRequest == 1){
					success = initDataRow(opProduct, staffID, cusID, type, date); 
					if (success > 0) {
						maxID +=1;
						success = maxID;
					}
					VTLog.i("MAXID","maxID"+ maxID);
				}
				// Update
				else if(typeRequest == 2){
					opProduct.updateDate = DateUtils.now();
					opProduct.updateUser = staffCode;
					success = updateData(opProduct); 
				}
				// Delele
				else if(typeRequest == 3){
					success = deleteData(opProduct); 
				} 
				
				if(success == -1){
					break;
				}
			}
		}catch(Exception ex){
			success=-1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return success;
	}
	
	/**
	* insert danh sach cac san pham sau khi kiem ban
	* @author: dungdq3
	 * @param maxID 
	* @param: Tham số của hàm
	* @return: Kết qủa trả về
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	public long insertSaleVolumePG(ProductCompetitorDTO product, long staffID,
			long cusID, int type, String date, long maxID) {
		// TODO Auto-generated method stub
		long success = -1;
		try{
			for(OpProductDTO opProduct: product.getArrProduct()){
				opProduct.setMaxID(maxID);
				success=initDataRow(opProduct, staffID, cusID, type, date);
				if (success > 0) {
					maxID +=1;
					success = maxID;
				}
			}
		}catch(Exception ex){
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return success;
	}

	/**
	 * Luu  du lieu thu thap du lieu thi truong role NVBH/ luu kiem ton, ban cua bia sai gon , bia doi thu 
	 * @author: hoanpd1
	 * @since: 11:55:57 09-09-2014
	 * @return: ContentValues
	 * @throws:  
	 * @param opProduct
	 * @param staffID
	 * @param cusID
	 * @return
	 */
	private long initDataRow(OpProductDTO opProduct, long staffID, long cusID, int type, String date) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(OP_SALE_VOLUME_ID, String.valueOf(opProduct.getMaxID()));
		editedValues.put(OPPONENT_ID, String.valueOf(opProduct.getOpID()));
		editedValues.put(OP_PRODUCT_ID, String.valueOf(opProduct.getOpProductID()));
		
		editedValues.put(CUSTOMER_ID, String.valueOf(cusID));
		editedValues.put(STATUS, 1);
		editedValues.put(STAFF_ID, String.valueOf(staffID));
		editedValues.put(TYPE, type + "");
		
		editedValues.put(CREATE_DATE, DateUtils.now());
		editedValues.put(CREATE_USER, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_TNPG){
			editedValues.put(SALE_DATE, DateUtils.now());
			editedValues.put(QUANTITY, String.valueOf(opProduct.getQuantity()));
		}else{
			editedValues.put(SALE_DATE, date);
			editedValues.put(QUANTITY, String.valueOf(opProduct.getNewQuantity()));
			editedValues.put(ACTION_TYPE, 0);
		}

		return insert(null, editedValues);
	}

	/**
	 * Update du lieu bang
	 * @author: quangvt1
	 * @since: 15:27:35 07-05-2014
	 * @return: long
	 * @throws:  
	 * @param opProduct
	 * @return
	 */
	private long updateData(OpProductDTO opProduct) {  
		// Du lieu can update
		ContentValues values = new ContentValues();
		values.put(QUANTITY, opProduct.getNewQuantity());
		values.put(UPDATE_DATE, opProduct.updateDate);
		values.put(UPDATE_USER, opProduct.updateUser);
		
		// where
		String where = OP_SALE_VOLUME_ID + " = ?";
		String[] args = {opProduct.getOp_sale_volume_id() + ""};
		return update(values, where, args);
	}

	/**
	 * Delete 1 dong trong bang op_sale_volume
	 * @author: quangvt1
	 * @since: 15:27:01 07-05-2014
	 * @return: long
	 * @throws:  
	 * @param opProduct
	 * @return
	 */
	private long deleteData(OpProductDTO opProduct) { 
		String where = OP_SALE_VOLUME_ID + " = ?";
		String[] args = {opProduct.getOp_sale_volume_id() + ""};
		return delete(where, args);
	}



	/**
	 * Luu thong tin khao sat thi truong khi nhap san luong ban co
	 * chu ki dung 1 thang
	 * @author: quangvt1
	 * @since: 15:54:21 20-06-2014
	 * @return: boolean
	 * @throws:  
	 * @param customer
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	public boolean saveSaleVolumeFromHTBH(CustomerInputQuantityDTO customer,
			String staffId, String staffCode, int actionType) {
		// Lay danh sach op_sale_volume cua khach hang trong thang
		// de biet ma insert hay update
		customer.listOpSale = getListOpSaleVolumeInMonth(customer);
		TABLE_ID tableId = new TABLE_ID(mDB);
		long id = tableId.getMaxId(OP_SALE_VOLUME_TABLE);
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			if(!customer.isHaveOpSaleVolume(detail.OBJECT_ID, detail.PRODUCT_ID)){
				detail.opId = id + "";
				id++;
				long success = insertDataFromHTBH(detail, staffId, staffCode);
				if(success <=0){
					return false;
				} 
			}else{
				detail.opId = customer.getOpSale(detail.OBJECT_ID, detail.PRODUCT_ID).getOp_sale_volume_id() + "";
				boolean success = updateDataFromHTBH(detail, staffId, staffCode,actionType);
				if(!success){
					return false;
				} 
			}
		}
		return true;
	}
	
	/**
	 * luu san luong thuc hien CTHTBH OP_SALE_VOLUME
	 * 
	 * @author: hoanpd1
	 * @since: 19:29:31 09-09-2014
	 * @return: ContentValues
	 * @throws:  
	 * @param detail
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	private ContentValues initDataFromHTBH(CustomerInputQuantityDetailDTO detail,
			String staffId, String staffCode) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(OP_SALE_VOLUME_ID, detail.opId);
		editedValues.put(OP_PRODUCT_ID, detail.PRODUCT_ID + "");
		editedValues.put(QUANTITY, detail.NEWQUANTITY + "");
		editedValues.put(CUSTOMER_ID, detail.OBJECT_ID + "");
		editedValues.put(STATUS, 1);
		editedValues.put(STAFF_ID, staffId);
		editedValues.put(TYPE, 1);
		editedValues.put(ACTION_TYPE, 1);
		editedValues.put(CREATE_USER, staffCode);
		editedValues.put(CREATE_DATE, DateUtils.now());
		editedValues.put(SALE_DATE, detail.PERIOD_FROM_DATE);
		return editedValues;
	}

	/**
	 * Update data local bang op_sale_volume
	 * @author: quangvt1
	 * @since: 15:58:59 20-06-2014
	 * @return: boolean
	 * @throws:  
	 * @param detail
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	private boolean updateDataFromHTBH(CustomerInputQuantityDetailDTO detail,
			String staffId, String staffCode,int actionType) {
		// Du lieu can update
		ContentValues values = new ContentValues();
		values.put(QUANTITY, detail.NEWQUANTITY);
		values.put(ACTION_TYPE, actionType);
		values.put(UPDATE_DATE, DateUtils.now());
		values.put(UPDATE_USER, staffCode);

		// where
		String where = OP_SALE_VOLUME_ID + " = ?";
		String[] args = { detail.opId };
		return (update(values, where, args) != -1);
	}

	/**
	 * Lay danh sach cac op_sale_volume trong thang cua khach hang
	 * @author: quangvt1
	 * @since: 17:23:36 19-06-2014
	 * @return: Map<String,String>
	 * @throws:  
	 * @param customer
	 * @return
	 */
	private List<OpProductDTO> getListOpSaleVolumeInMonth(
			CustomerInputQuantityDTO customer) { 
		List<OpProductDTO> list = new ArrayList<OpProductDTO>();
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT OP.OP_SALE_VOLUME_ID, ");
		var1.append("       OP.OP_PRODUCT_ID, ");
		var1.append("       OP.CUSTOMER_ID ");
		var1.append("FROM   OP_SALE_VOLUME OP ");
		var1.append("WHERE  OP.CUSTOMER_ID = ? ");
		params.add(customer.OBJECT_ID + "");
		var1.append("       AND DATE(OP.SALE_DATE) = DATE(?) ");
		params.add(customer.details.get(0).PERIOD_FROM_DATE);
		
		Cursor cursor = null;
		try {
			cursor = rawQueries(var1.toString(), params);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do{
						String id = "";
						String productId = "";
						String customerId = "";
						if (cursor.getColumnIndex(OP_SALE_VOLUME_ID) >= 0) {
							id = cursor.getString(cursor.getColumnIndex(OP_SALE_VOLUME_ID));
						}  

						if (cursor.getColumnIndex(OP_PRODUCT_ID) >= 0) {
							productId = cursor.getString(cursor.getColumnIndex(OP_PRODUCT_ID));
						}  

						if (cursor.getColumnIndex(CUSTOMER_ID) >= 0) {
							customerId = cursor.getString(cursor.getColumnIndex(CUSTOMER_ID));
						}  
						
						OpProductDTO dto = new OpProductDTO();
						dto.setOpProductID(Long.parseLong(productId));
						dto.customerId = Long.parseLong(customerId);
						dto.setOp_sale_volume_id(Long.parseLong(id));
						list.add(dto);
					}while(cursor.moveToNext()); 
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
				}
			} else {
			}
		}
		
		return list;
	}
}
