/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * @author dungdq3
 *
 */
public class OP_STOCK_TOTAL_TABLE extends ABSTRACT_TABLE {
	
	// id kho
	public static final String OP_STOCK_TOTAL_ID = "OP_STOCK_TOTAL_ID";
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
	public static final String INVENTORY_DATE = "INVENTORY_DATE";
	// ngay tao don
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao don
	public static final String CREATE_USER = "CREATE_USER";
	public static final String OP_STOCK_TOTAL_TABLE = "OP_STOCK_TOTAL";
	

	public OP_STOCK_TOTAL_TABLE(SQLiteDatabase mDB) {
		this.tableName = OP_STOCK_TOTAL_TABLE;

		this.columns = new String[] {OP_STOCK_TOTAL_ID, OPPONENT_ID, OP_PRODUCT_ID, QUANTITY, CUSTOMER_ID, STAFF_ID, STATUS,
				INVENTORY_DATE, CREATE_DATE};
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
	* Cham kiem ton
	* @author: dungdq3
	* @param: ProductCompetitorDTO product, int staffID,
			int cusID
	* @return: long
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	public long insertStockTotal(ProductCompetitorDTO product, long staffID,
								 long cusID, String staffCode, long idMax) {
		// TODO Auto-generated method stub
		long checkInsert =-1;
		ContentValues value = null;
		try{
			for(OpProductDTO opProduct: product.getArrProduct()){
				opProduct.setMaxID(idMax);
				value =initDataRow(opProduct, staffID, cusID, staffCode);
				checkInsert = insert(null, value);
				if(checkInsert > 0){
					idMax += 1;
					checkInsert = idMax;
				}
			}
		}catch(Exception ex){

			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return checkInsert;
	}

	/**
	 * @author: hoanpd1
	 * @since: 11:21:39 09-09-2014
	 * @return: ContentValues
	 * @throws:  
	 * @return
	 */
	private ContentValues initDataRow(OpProductDTO opProduct, long staffID, long cusID, String staffCode) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(OP_STOCK_TOTAL_ID, String.valueOf(opProduct.getMaxID()));
		editedValues.put(OPPONENT_ID, String.valueOf(opProduct.getOpID()));
		editedValues.put(OP_PRODUCT_ID, String.valueOf(opProduct.getOpProductID()));
		editedValues.put(QUANTITY, String.valueOf(opProduct.getQuantity()));
		editedValues.put(CUSTOMER_ID, String.valueOf(cusID));
		editedValues.put(STATUS, 1);
		editedValues.put(STAFF_ID, String.valueOf(staffID));
		editedValues.put(INVENTORY_DATE, DateUtils.now());
		editedValues.put(CREATE_DATE, DateUtils.now());
		editedValues.put(CREATE_USER, staffCode);

		return editedValues;
	}

	/**
	* Kiem tra xem co can kiem hang ton ko!?
	* @author: dungdq3
	* @param data 
	* @return: int
	* @throws: Exception ex
	*/
	public long checkRemained(Bundle data) {
		// TODO Auto-generated method stub
		String cusID=data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		long staffID=data.getLong(IntentConstants.INTENT_STAFF_ID);
		String sql="select OP_STOCK_TOTAL_ID from OP_STOCK_TOTAL where (Date(INVENTORY_DATE))=Date(?) and customer_id=? and staff_id=?;";
		String[] params={DateUtils.now(), cusID, String.valueOf(staffID)};
		long check=-1;
		Cursor cursor=null;
		try {
			cursor = rawQuery(sql.toString(), params);

			if (cursor != null) {
				if (cursor.moveToFirst()) {
					if (cursor.getColumnIndex(OP_STOCK_TOTAL_ID) >= 0) {
						check=cursor.getLong(cursor.getColumnIndex(OP_STOCK_TOTAL_ID));
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
}
