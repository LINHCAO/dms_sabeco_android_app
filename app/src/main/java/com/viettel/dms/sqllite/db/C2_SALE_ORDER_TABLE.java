/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.C2SaleOrderDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Thong tin don ban cua C2
 * C2_SALE_ORDER_TABLE.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:47:33 12-12-2013
 */
public class C2_SALE_ORDER_TABLE extends ABSTRACT_TABLE {
	public static final String C2_SALE_ORDER_ID = "C2_SALE_ORDER_ID";
	public static final String SHOP_ID = "SHOP_ID";
	public static final String C2_ID = "C2_ID";
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	public static final String STAFF_ID = "STAFF_ID";
	public static final String ORDER_DATE = "ORDER_DATE";
	public static final String APPROVED = "APPROVED";
	public static final String STATUS = "STATUS";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String UPDATE_USER = "UPDATE_USER";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String TABLE_NAME = "C2_SALE_ORDER";
	
	public C2_SALE_ORDER_TABLE(SQLiteDatabase mDB) {
		tableName = TABLE_NAME;
		this.columns = new String[] { C2_SALE_ORDER_ID, SHOP_ID, 
				APPROVED, ORDER_DATE, CUSTOMER_ID,
				C2_ID, DESCRIPTION, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE,
				STATUS, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	 * Xoa data
	 */
	public void clearTable() {
		execSQL(sqlDelete);
	}

	/**
	 * drop database trong data
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public void dropDatabase() {
		try {
			GlobalInfo.getInstance().getAppContext()
					.deleteDatabase(Constants.DATABASE_NAME);
			VTLog.i("DBAdapter", "Drop database success.......");
		} catch (Exception e) {
			VTLog.i("DBAdapter", "Error drop database : " + e.toString());
		}
	}

	public long getCount() {
		SQLiteStatement statement = compileStatement(sqlGetCountQuerry);
		long count = statement.simpleQueryForLong();
		return count;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((C2SaleOrderDTO) dto);
		return insert(null, value);
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public long update(C2SaleOrderDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.c2SaleOrderId };
		return update(value, C2_SALE_ORDER_ID + " = ? ", params);
	}

	public long delete(AbstractTableDTO dto) {
		C2SaleOrderDTO saleDTO = (C2SaleOrderDTO) dto;
		String[] params = { "" + saleDTO.c2SaleOrderId, "" + saleDTO.synState };
		return delete(C2_SALE_ORDER_ID + " = ? AND " + SYN_STATE + " = ?", params);
	}

	public C2SaleOrderDTO getSaleById(long id) {
		C2SaleOrderDTO saleOrderDTO = null;
		Cursor c = null;
		try {
			String[] params = { "" + id };
			c = query(C2_SALE_ORDER_ID + " = ?", params, null, null, null);

			if (c != null) {
				if (c.moveToFirst()) {
					saleOrderDTO = initSaleOrderDTOFromCursor(c);
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			}
		}
		return saleOrderDTO;
	}

	/**
	 * Khoi tao du lieu khi select
	 * @author: dungnt19
	 * @since: 10:49:09 12-12-2013
	 * @return: C2SaleOrderDTO
	 * @throws:  
	 * @param c
	 * @return
	 */
	private C2SaleOrderDTO initSaleOrderDTOFromCursor(Cursor c) {
		C2SaleOrderDTO saleOrderDTO = new C2SaleOrderDTO();

		saleOrderDTO.createUser = c.getString(c.getColumnIndex(CREATE_USER));
		saleOrderDTO.customerId = c.getLong(c.getColumnIndex(CUSTOMER_ID));
		saleOrderDTO.orderDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(ORDER_DATE)));
		saleOrderDTO.c2SaleOrderId = c.getLong(c.getColumnIndex(C2_SALE_ORDER_ID));
		saleOrderDTO.shopId = c.getInt(c.getColumnIndex(SHOP_ID));
		saleOrderDTO.c2Id = c.getInt(c.getColumnIndex(C2_ID));
		saleOrderDTO.approved = c.getInt(c.getColumnIndex(APPROVED));
		saleOrderDTO.createDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(CREATE_DATE)));
		saleOrderDTO.updateDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(UPDATE_DATE)));
		saleOrderDTO.updateUser = c.getString(c.getColumnIndex(UPDATE_USER));
		saleOrderDTO.synState = c.getInt(c.getColumnIndex(SYN_STATE));
		saleOrderDTO.status =  c.getInt(c.getColumnIndex(STATUS));
		
		return saleOrderDTO;
	}

	/**
	 * Lay tat ca cac row
	 * @author: dungnt19
	 * @since: 10:49:21 12-12-2013
	 * @return: Vector<C2SaleOrderDTO>
	 * @throws:  
	 * @return
	 */
	public Vector<C2SaleOrderDTO> getAllRow() {
		Vector<C2SaleOrderDTO> v = new Vector<C2SaleOrderDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			C2SaleOrderDTO saleOrderDTO;
			if (c.moveToFirst()) {
				do {
					saleOrderDTO = initSaleOrderDTOFromCursor(c);
					v.addElement(saleOrderDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	/**
	 * Khoi tao du lieu khi insert, update
	 * @author: dungnt19
	 * @since: 10:49:30 12-12-2013
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDataRow(C2SaleOrderDTO dto) {
		ContentValues editedValues = new ContentValues();

		if (dto.c2SaleOrderId > 0) {
			editedValues.put(C2_SALE_ORDER_ID, dto.c2SaleOrderId);
		}
		if (dto.shopId > 0) {
			editedValues.put(SHOP_ID, dto.shopId);
		}
		if (dto.staffId > 0) {
			editedValues.put(STAFF_ID, dto.staffId);
		}

		if (!StringUtil.isNullOrEmpty(dto.orderDate)) {
			editedValues.put(ORDER_DATE, dto.orderDate);
		}
		
		if (dto.customerId > 0) {
			editedValues.put(CUSTOMER_ID, dto.customerId);
		}

		if (dto.c2Id > 0) {
			editedValues.put(C2_ID, dto.c2Id);
		}
		
		if (!StringUtil.isNullOrEmpty(dto.createUser)) {
			editedValues.put(CREATE_USER, dto.createUser);
		}

		if (!StringUtil.isNullOrEmpty(dto.createDate))
			editedValues.put(CREATE_DATE, dto.createDate);

		if (!StringUtil.isNullOrEmpty(dto.updateUser)) {
			editedValues.put(UPDATE_USER, dto.updateUser);
		}

		if (!StringUtil.isNullOrEmpty(dto.updateDate)) {
			editedValues.put(UPDATE_DATE, dto.updateDate);
		}
		
		if (!StringUtil.isNullOrEmpty(dto.description)) {
			editedValues.put(DESCRIPTION, dto.description);
		}

		editedValues.put(APPROVED, dto.approved);
		editedValues.put(SYN_STATE, dto.synState);
		editedValues.put(STATUS, dto.status);
		return editedValues;
	}
}
