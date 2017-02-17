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
import com.viettel.dms.dto.db.C2PurchaseDetailDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Chi tiet don mua cua C2
 * C2_PURCHASE_DETAIL_TABLE.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:49:57 12-12-2013
 */
public class C2_PURCHASE_DETAIL_TABLE extends ABSTRACT_TABLE {
	public static final String C2_PURCHASE_DETAIL_ID = "C2_PURCHASE_DETAIL_ID";
	public static final String C2_PURCHASE_ID = "C2_PURCHASE_ID";
	public static final String SHOP_ID = "SHOP_ID";
	public static final String STAFF_ID = "STAFF_ID";
	public static final String PRODUCT_ID = "PRODUCT_ID";
	public static final String QUANTITY = "QUANTITY";
	public static final String PURCHASE_DATE = "PURCHASE_DATE";
	public static final String AMOUNT = "AMOUNT";
	public static final String PRICE = "PRICE";
	public static final String STATUS = "STATUS";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String UPDATE_USER = "UPDATE_USER";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String TABLE_NAME = "C2_PURCHASE_DETAIL";
	
	public C2_PURCHASE_DETAIL_TABLE(SQLiteDatabase mDB) {
		tableName = TABLE_NAME;
		this.columns = new String[] { C2_PURCHASE_DETAIL_ID, C2_PURCHASE_ID, SHOP_ID, 
				PURCHASE_DATE, PRODUCT_ID, QUANTITY, AMOUNT, PRICE,
				 DESCRIPTION, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE,
				STATUS, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/**
	 * Xoa du lieu bang
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
		ContentValues value = initDataRow((C2PurchaseDetailDTO) dto);
		return insert(null, value);
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	/**
	 * Update du lieu 1 row
	 * @author: dungnt19
	 * @since: 10:50:37 12-12-2013
	 * @return: long
	 * @throws:  
	 * @param dto
	 * @return
	 */
	public long update(C2PurchaseDetailDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.c2PurchaseId };
		return update(value, C2_PURCHASE_ID + " = ? ", params);
	}

	public long delete(AbstractTableDTO dto) {
		C2PurchaseDetailDTO saleDTO = (C2PurchaseDetailDTO) dto;
		String[] params = { "" + saleDTO.c2PurchaseId, "" + saleDTO.synState };
		return delete(C2_PURCHASE_ID + " = ? AND " + SYN_STATE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: dungnt19 replaced
	 * @param id
	 * @return: C2PurchaseDetailDTO
	 * @throws:
	 */
	public C2PurchaseDetailDTO getSaleById(long id) {
		C2PurchaseDetailDTO saleOrderDTO = null;
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
	 * Khoi tao du lieu khi select
	 * @author: dungnt19
	 * @since: 10:51:11 12-12-2013
	 * @return: C2PurchaseDetailDTO
	 * @throws:  
	 * @param c
	 * @return
	 */
	private C2PurchaseDetailDTO initSaleOrderDTOFromCursor(Cursor c) {
		C2PurchaseDetailDTO saleOrderDTO = new C2PurchaseDetailDTO();

		saleOrderDTO.createUser = c.getString(c.getColumnIndex(CREATE_USER));
		saleOrderDTO.productId = c.getInt(c.getColumnIndex(PRODUCT_ID));
		saleOrderDTO.purchaseDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(PURCHASE_DATE)));
		saleOrderDTO.c2PurchaseId = c.getLong(c.getColumnIndex(C2_PURCHASE_ID));
		saleOrderDTO.shopId = c.getInt(c.getColumnIndex(SHOP_ID));
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
	 * @author: dunnt19
	 * @return
	 * @return: Vector<SaleOrderDTO>
	 * @throws:
	 */
	public Vector<C2PurchaseDetailDTO> getAllRow() {
		Vector<C2PurchaseDetailDTO> v = new Vector<C2PurchaseDetailDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			C2PurchaseDetailDTO saleOrderDTO;
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
	 * Khoi tao du lieu cho insert, update vao bang
	 * @author: dungnt19
	 * @since: 10:51:29 12-12-2013
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDataRow(C2PurchaseDetailDTO dto) {
		ContentValues editedValues = new ContentValues();

		if (dto.c2PurchaseDetailId > 0) {
			editedValues.put(C2_PURCHASE_DETAIL_ID, dto.c2PurchaseDetailId);
		}
		if (dto.c2PurchaseId > 0) {
			editedValues.put(C2_PURCHASE_ID, dto.c2PurchaseId);
		}
		if (dto.shopId > 0) {
			editedValues.put(SHOP_ID, dto.shopId);
		}
		
		if (dto.staffId > 0) {
			editedValues.put(STAFF_ID, dto.staffId);
		}

		if (!StringUtil.isNullOrEmpty(dto.purchaseDate)) {
			editedValues.put(PURCHASE_DATE, dto.purchaseDate);
		}
		
		if (dto.productId > 0) {
			editedValues.put(PRODUCT_ID, dto.productId);
		}
		
		if (dto.price > 0) {
			editedValues.put(PRICE, dto.price);
		}
		
		if (dto.quantity > 0) {
			editedValues.put(QUANTITY, dto.quantity);
		}
		
		if (dto.amount > 0) {
			editedValues.put(AMOUNT, dto.amount);
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

		editedValues.put(SYN_STATE, dto.synState);
		editedValues.put(STATUS, dto.status);
		return editedValues;
	}
}
