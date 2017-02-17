/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.SparseIntArray;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.StockTotalDTO;
import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin ton kho
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class STOCK_TOTAL_TABLE extends ABSTRACT_TABLE {
	// id ton kho
	public static final String STOCK_TOTAL_ID = "STOCK_TOTAL_ID";
	// id doi tuong ton kho
	public static final String OBJECT_ID = "OBJECT_ID";
	// loai doi tuong: 1 doi tuong trong SHOP, 2 doi tuong trong STAFF, 3
	// CUSTOMER
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// mo ta
	public static final String DESCR = "DESCR";
	// so luong ton kho
	public static final String QUANTITY = "QUANTITY";
	// so luong co the dat hang
	public static final String AVAILABLE_QUANTITY = "AVAILABLE_QUANTITY";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	public static final String TABLE_STOCK_TOTAL = "STOCK_TOTAL";

	public STOCK_TOTAL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_STOCK_TOTAL;
		this.columns = new String[] { STOCK_TOTAL_ID, OBJECT_ID, OBJECT_TYPE, PRODUCT_ID, DESCR, QUANTITY,
				AVAILABLE_QUANTITY, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((StockTotalDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(StockTotalDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}

	/**
	 * Update 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		StockTotalDTO disDTO = (StockTotalDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.stockTotalId };
		return update(value, STOCK_TOTAL_ID + " = ?", params);
	}

	public long updateStockTotalFromOrder(AbstractTableDTO dto) {
		StockTotalDTO disDTO = (StockTotalDTO) dto;
		ContentValues value = initDataRowUpderOrder(disDTO);
		String[] params = { "" + disDTO.objectId, "" + disDTO.objectType, "" + disDTO.productId };
		return update(value, OBJECT_ID + " = ? and " + OBJECT_TYPE + " =? and " + PRODUCT_ID + " =? ", params);
	}

	public boolean increaseStockTotal2(AbstractTableDTO dto) {
		StockTotalDTO temp = (StockTotalDTO) dto;

		StockTotalDTO disDTO = new StockTotalDTO();
		disDTO.objectId = temp.objectId;
		disDTO.objectType = temp.objectType;
		disDTO.productId = temp.productId;
		disDTO.quantity = temp.quantity;
		disDTO.availableQuantity = temp.availableQuantity;

		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(disDTO.objectId));
		params.add(String.valueOf(disDTO.objectType));
		params.add(String.valueOf(disDTO.productId));

		StringBuilder sqlObject = new StringBuilder();
		sqlObject.append("UPDATE STOCK_TOTAL SET ");
		sqlObject.append("QUANTITY = QUANTITY + ");
		sqlObject.append(disDTO.quantity);

		sqlObject.append(",AVAILABLE_QUANTITY = AVAILABLE_QUANTITY + ");
		sqlObject.append(disDTO.availableQuantity);
		sqlObject.append(" WHERE ");
		sqlObject.append(" OBJECT_ID = ");
		sqlObject.append(disDTO.objectId);
		sqlObject.append(" AND OBJECT_TYPE = ");
		sqlObject.append(disDTO.objectType);
		sqlObject.append(" AND PRODUCT_ID = ");
		sqlObject.append(disDTO.productId);

		try {
			execSQL(sqlObject.toString());
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
		}

		return true;
	}

	/**
	 * 
	*  Giam ton kho tren available_quantity
	*  @author: Nguyen Thanh Dung
	*  @param dto
	*  @return
	*  @return: boolean
	*  @throws:
	 */
	public boolean descreaseStockTotalPresale(AbstractTableDTO dto) {
		StockTotalDTO temp = (StockTotalDTO) dto;

		StockTotalDTO disDTO = new StockTotalDTO();
		disDTO.objectId = temp.objectId;
		disDTO.objectType = temp.objectType;
		disDTO.productId = temp.productId;
		disDTO.quantity = temp.quantity;
		disDTO.availableQuantity = temp.availableQuantity;

		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(disDTO.objectId));
		params.add(String.valueOf(disDTO.objectType));
		params.add(String.valueOf(disDTO.productId));

		StringBuilder sqlObject = new StringBuilder();
		sqlObject.append("UPDATE STOCK_TOTAL SET ");
//		sqlObject.append("QUANTITY = QUANTITY - ");
//		sqlObject.append(disDTO.quantity);

		sqlObject.append("AVAILABLE_QUANTITY = AVAILABLE_QUANTITY - ");
		sqlObject.append(disDTO.availableQuantity);
		sqlObject.append(" WHERE ");
		sqlObject.append(" OBJECT_ID = ");
		sqlObject.append(disDTO.objectId);
		sqlObject.append(" AND OBJECT_TYPE = ");
		sqlObject.append(disDTO.objectType);
		sqlObject.append(" AND PRODUCT_ID = ");
		sqlObject.append(disDTO.productId);

		try {
			execSQL(sqlObject.toString());
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
		}

		return true;
	}
	
	/**
	 * 
	*  Giam ton kho tren available_quantity & quantity
	*  @author: Nguyen Thanh Dung
	*  @param dto
	*  @return
	*  @return: boolean
	*  @throws:
	 */
	public boolean descreaseStockTotalVansale(AbstractTableDTO dto) {
		StockTotalDTO temp = (StockTotalDTO) dto;
		
		StockTotalDTO disDTO = new StockTotalDTO();
		disDTO.objectId = temp.objectId;
		disDTO.objectType = temp.objectType;
		disDTO.productId = temp.productId;
		disDTO.quantity = temp.quantity;
		disDTO.availableQuantity = temp.availableQuantity;
		
		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(disDTO.objectId));
		params.add(String.valueOf(disDTO.objectType));
		params.add(String.valueOf(disDTO.productId));
		
		StringBuilder sqlObject = new StringBuilder();
		sqlObject.append("UPDATE STOCK_TOTAL SET ");
		sqlObject.append("QUANTITY = QUANTITY - ");
		sqlObject.append(disDTO.quantity);
		
		sqlObject.append("AVAILABLE_QUANTITY = AVAILABLE_QUANTITY - ");
		sqlObject.append(disDTO.availableQuantity);
		sqlObject.append(" WHERE ");
		sqlObject.append(" OBJECT_ID = ");
		sqlObject.append(disDTO.objectId);
		sqlObject.append(" AND OBJECT_TYPE = ");
		sqlObject.append(disDTO.objectType);
		sqlObject.append(" AND PRODUCT_ID = ");
		sqlObject.append(disDTO.productId);
		
		try {
			execSQL(sqlObject.toString());
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
		}
		
		return true;
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(STOCK_TOTAL_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		StockTotalDTO cusDTO = (StockTotalDTO) dto;
		String[] params = { String.valueOf(cusDTO.stockTotalId) };
		return delete(STOCK_TOTAL_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public StockTotalDTO getRowById(String id) {
		StockTotalDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(STOCK_TOTAL_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private StockTotalDTO initLogDTOFromCursor(Cursor c) {
		StockTotalDTO dpDetailDTO = new StockTotalDTO();

		dpDetailDTO.stockTotalId = (c.getInt(c.getColumnIndex(STOCK_TOTAL_ID)));
		dpDetailDTO.objectId = (c.getInt(c.getColumnIndex(OBJECT_ID)));
		dpDetailDTO.objectType = (c.getInt(c.getColumnIndex(OBJECT_TYPE)));
		dpDetailDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		dpDetailDTO.descr = (c.getString(c.getColumnIndex(DESCR)));
		dpDetailDTO.quantity = (c.getInt(c.getColumnIndex(QUANTITY)));
		dpDetailDTO.availableQuantity = (c.getInt(c.getColumnIndex(AVAILABLE_QUANTITY)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));

		return dpDetailDTO;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: Vector<DisplayPrdogrameLvDTO>
	 * @throws:
	 */
	public Vector<StockTotalDTO> getAllRow() {
		Vector<StockTotalDTO> v = new Vector<StockTotalDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			StockTotalDTO DisplayPrdogrameLvDTO;
			if (c.moveToFirst()) {
				do {
					DisplayPrdogrameLvDTO = initLogDTOFromCursor(c);
					v.addElement(DisplayPrdogrameLvDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(StockTotalDTO dto) {
		ContentValues editedValues = new ContentValues();

		editedValues.put(STOCK_TOTAL_ID, dto.stockTotalId);
		editedValues.put(OBJECT_ID, dto.objectId);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(DESCR, dto.descr);
		editedValues.put(QUANTITY, dto.quantity);
		editedValues.put(AVAILABLE_QUANTITY, dto.availableQuantity);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);

		return editedValues;
	}

	/**
	 * Update stock total khi order
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDataRowUpderOrder(StockTotalDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(QUANTITY, dto.quantity);
		editedValues.put(AVAILABLE_QUANTITY, dto.availableQuantity);

		return editedValues;
	}

	/**
	 * Lay danh sach id san pham co trong order nhung chua co stock_total
	 * @author: quangvt1
	 * @since: 16:50:54 03-06-2014
	 * @return: List<Integer>
	 * @throws:  
	 * @param dto
	 * @return
	 */
	public SparseIntArray getListProductIdNotInStockTotal(OrderViewDTO dto) {
		long shopId = dto.orderInfo.shopId;
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		int objectType = 1;
		long objectId = 0;

		if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = shopId;
		} else if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
			objectType = StockTotalDTO.TYPE_VANSALE;
			objectId = staffId;
		} else {
			objectType = StockTotalDTO.TYPE_CUSTOMER;
		}
		
		SparseIntArray result = new SparseIntArray();//<Integer, Integer>();
		List<Integer> listProductId = dto.getListIdProduct();
		String productIdList = TextUtils.join(",", listProductId);
		ArrayList<String> params = new ArrayList<String>(); 
		
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT P.PRODUCT_ID AS PRODUCT_ID, ");
		var1.append("       CASE ");
		var1.append("         WHEN S.PRODUCT_ID IS NULL THEN 0 ");
		var1.append("         ELSE 1 ");
		var1.append("       END          AS HAVE ");
		var1.append("FROM   (SELECT PRODUCT_ID ");
		var1.append("        FROM   PRODUCT P ");
		var1.append("        WHERE  P.PRODUCT_ID IN ( " + productIdList + " )) AS P "); 
		var1.append("       LEFT JOIN STOCK_TOTAL AS S ");
		var1.append("              ON ( P.PRODUCT_ID = S.PRODUCT_ID ");
		var1.append("              		AND S.OBJECT_ID = ? ");
		params.add(objectId + "");
		var1.append("              		AND S.OBJECT_TYPE = ?) ");
		params.add(objectType + "");
		var1.append("WHERE  HAVE = 0 ");
		
		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), params);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						 if(c.getColumnIndex("PRODUCT_ID") > -1){
							 int id = c.getInt(c.getColumnIndex("PRODUCT_ID"));
							 result.put(id, id); 
						 }
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				if (c != null) {
					c.close();
				} 
			} catch (Exception ex) {
			}
		}
		return result;
	}
}
