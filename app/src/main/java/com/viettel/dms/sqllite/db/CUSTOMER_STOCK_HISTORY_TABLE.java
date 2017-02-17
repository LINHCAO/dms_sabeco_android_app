/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerStockHistoryDTO;

/**
 * Bang luu thong tin dong bo
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class CUSTOMER_STOCK_HISTORY_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String CUSTOMER_STOCK_HISTORY_ID = "CUSTOMER_STOCK_HISTORY_ID";
	// id customer
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// // id sale order
	// public static final String SALE_ORDER_ID = "SALE_ORDER_ID";
	// id product
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// OBJECT TYPE
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// OBJECT ID
	public static final String OBJECT_ID = "OBJECT_ID";
	// RESULT
	public static final String RESULT = "RESULT";
	// CREATE DATE
	public static final String CREATE_DATE = "CREATE_DATE";

	// // so luon
	// public static final String QUANTITY = "QUANTITY";
	// // ordinal
	// public static final String ORDINAL = "ORDINAL";
	// // check date
	// public static final String CHECK_DATE = "CHECK_DATE";
	// ten bang
	public static final String TABLE_NAME = "CUSTOMER_STOCK_HISTORY";

	public CUSTOMER_STOCK_HISTORY_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { CUSTOMER_STOCK_HISTORY_ID, CUSTOMER_ID, STAFF_ID, PRODUCT_ID, OBJECT_TYPE,
				OBJECT_ID, RESULT, CREATE_DATE, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#insert(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		CustomerStockHistoryDTO disDTO = (CustomerStockHistoryDTO) dto;
		ContentValues value = initDataRow(disDTO);
		return insert(null, value);

	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		CustomerStockHistoryDTO disDTO = (CustomerStockHistoryDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.customerId, "" + disDTO.productId, "" + disDTO.staffId };
		String where = CUSTOMER_ID + " = ?  AND " + PRODUCT_ID + " = ?  AND " + STAFF_ID + " = ?  ";
		return update(value, where, params);
	}

	public long insertOrUpdate(AbstractTableDTO dto) {
		CustomerStockHistoryDTO disDTO = (CustomerStockHistoryDTO) dto;
		ContentValues value = initDataRow(disDTO);
		return insert(null, value);
	}

	@SuppressWarnings("unused")
	private boolean isExist(SQLiteDatabase db, String tableName, String query, String[] arg) {
		query = "Select * from " + tableName + " where " + query;
		Cursor c = db.rawQuery(query, arg);
		if (c.getCount() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	private ContentValues initDataRow(CustomerStockHistoryDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_STOCK_HISTORY_ID, dto.customerStockHistoryId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(OBJECT_ID, dto.objectId);
		editedValues.put(RESULT, dto.result);
		editedValues.put(CREATE_DATE, dto.createDate);
		// editedValues.put(SALE_ORDER_ID, dto.saleOrderId);
		// editedValues.put(QUANTITY, dto.quanlity);
		// editedValues.put(CHECK_DATE, DateUtils.now());
		// editedValues.put(CREATE_DATE, dto.createDate);

		return editedValues;
	}
}
