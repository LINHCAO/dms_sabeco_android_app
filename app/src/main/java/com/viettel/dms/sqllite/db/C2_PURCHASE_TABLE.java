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
import com.viettel.dms.dto.db.C2PurchaseDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Thong tin don mua cua C2 
 * C2_PURCHASE_TABLE.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:51:55 12-12-2013
 */
public class C2_PURCHASE_TABLE extends ABSTRACT_TABLE {
	public static final String C2_PURCHASE_ID = "C2_PURCHASE_ID";
	public static final String FROM_SHOP_NAME = "FROM_SHOP_NAME";
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	public static final String STAFF_ID = "STAFF_ID";
	public static final String C2_PURCHASE_DATE = "C2_PURCHASE_DATE";
	public static final String APPROVED = "APPROVED";
	public static final String STATUS = "STATUS";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String UPDATE_USER = "UPDATE_USER";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String TABLE_NAME = "C2_PURCHASE";
	
	public C2_PURCHASE_TABLE(SQLiteDatabase mDB) {
		tableName = TABLE_NAME;
		this.columns = new String[] { C2_PURCHASE_ID, FROM_SHOP_NAME, 
				APPROVED, C2_PURCHASE_DATE, CUSTOMER_ID,
				DESCRIPTION, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE,
				STATUS, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	 * xoa data
	 * 
	 * @author: dunnt19
	 * @return: void
	 * @throws:
	 */
	public void clearTable() {
		execSQL(sqlDelete);
	}

	/**
	 * drop database trong data
	 * 
	 * @author: dungnt19
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
		ContentValues value = initDataRow((C2PurchaseDTO) dto);
		return insert(null, value);
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * thay doi 1 dong cua CSDL
	 * 
	 * @author: dungnt19
	 * @param id
	 * @param dto
	 * @return: int
	 * @throws:
	 */
	public long update(C2PurchaseDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.c2PurchaseId };
		return update(value, C2_PURCHASE_ID + " = ? ", params);
	}

	public long delete(AbstractTableDTO dto) {
		C2PurchaseDTO saleDTO = (C2PurchaseDTO) dto;
		String[] params = { "" + saleDTO.c2PurchaseId, "" + saleDTO.synState };
		return delete(C2_PURCHASE_ID + " = ? AND " + SYN_STATE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: dungnt19 replaced
	 * @param id
	 * @return: SaleOrderDTO
	 * @throws:
	 */
	public C2PurchaseDTO getSaleById(long id) {
		C2PurchaseDTO saleOrderDTO = null;
		Cursor c = null;
		try {
			String[] params = { "" + id };
			c = query(C2_PURCHASE_ID + " = ?", params, null, null, null);

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
	 * Khoi tao 1 row sau khi select
	 * @author: dungnt19
	 * @since: 10:52:36 12-12-2013
	 * @return: C2PurchaseDTO
	 * @throws:  
	 * @param c
	 * @return
	 */
	private C2PurchaseDTO initSaleOrderDTOFromCursor(Cursor c) {
		C2PurchaseDTO saleOrderDTO = new C2PurchaseDTO();

		saleOrderDTO.createUser = c.getString(c.getColumnIndex(CREATE_USER));
		saleOrderDTO.customerId = c.getLong(c.getColumnIndex(CUSTOMER_ID));
		saleOrderDTO.c2PurchaseDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(C2_PURCHASE_DATE)));
		saleOrderDTO.c2PurchaseId = c.getLong(c.getColumnIndex(C2_PURCHASE_ID));
		saleOrderDTO.fromShopName = c.getString(c.getColumnIndex(FROM_SHOP_NAME));
		saleOrderDTO.approved = c.getInt(c.getColumnIndex(APPROVED));
		saleOrderDTO.createDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(CREATE_DATE)));
		saleOrderDTO.updateDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(UPDATE_DATE)));
		saleOrderDTO.updateUser = c.getString(c.getColumnIndex(UPDATE_USER));
		saleOrderDTO.synState = c.getInt(c.getColumnIndex(SYN_STATE));
		saleOrderDTO.status =  c.getInt(c.getColumnIndex(STATUS));
		
		return saleOrderDTO;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: dungnt19
	 * @return
	 * @return: Vector<C2PurchaseDTO>
	 * @throws:
	 */
	public Vector<C2PurchaseDTO> getAllRow() {
		Vector<C2PurchaseDTO> v = new Vector<C2PurchaseDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			C2PurchaseDTO saleOrderDTO;
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
	 * Khoi tao du lieu de insert, update
	 * @author: dungnt19
	 * @since: 10:52:54 12-12-2013
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDataRow(C2PurchaseDTO dto) {
		ContentValues editedValues = new ContentValues();

		if (dto.c2PurchaseId > 0) {
			editedValues.put(C2_PURCHASE_ID, dto.c2PurchaseId);
		}
		if (!StringUtil.isNullOrEmpty(dto.fromShopName)) {
			editedValues.put(FROM_SHOP_NAME, dto.fromShopName);
		}
		if (dto.staffId > 0) {
			editedValues.put(STAFF_ID, dto.staffId);
		}

		if (!StringUtil.isNullOrEmpty(dto.c2PurchaseDate)) {
			editedValues.put(C2_PURCHASE_DATE, dto.c2PurchaseDate);
		}
		
		if (dto.customerId > 0) {
			editedValues.put(CUSTOMER_ID, dto.customerId);
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
