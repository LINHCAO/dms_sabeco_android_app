/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.SalesPlanDTO;
import com.viettel.utils.VTLog;

/**
 *  Luu thong tin ke hoach ban hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class SALES_PLAN_TABLE extends ABSTRACT_TABLE {
	// id ke hoach ban hang
	public static final String SALE_PLAN_ID = "SALE_PLAN_ID";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// mo ta
	public static final String DESCRIPTION = "DESCRIPTION";
	// 1: trong tam, 0: khong phai trong tam
	public static final String IS_FOCUS_ITEM = "IS_FOCUS_ITEM";
	// ma nganh hang
	public static final String CATEGORY_CODE = "CATEGORY_CODE";
	// so luong
	public static final String QUANTITY = "QUANTITY";
	// so tien
	public static final String AMOUNT = "AMOUNT";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_TIME = "CREATE_TIME";
	// ngay cap nhat
	public static final String UPDATE_TIME = "UPDATE_TIME";
	
	private static final String SALES_PLAN_TABLE = "SALES_PLAN";
	
	public SALES_PLAN_TABLE(SQLiteDatabase mDB) {
		this.tableName = SALES_PLAN_TABLE;
		this.columns = new String[] {SALE_PLAN_ID, SHOP_ID ,STAFF_ID, PRODUCT_ID,DESCRIPTION,IS_FOCUS_ITEM,CATEGORY_CODE,
				QUANTITY,AMOUNT, FROM_DATE,TO_DATE,CREATE_USER, UPDATE_USER, CREATE_TIME, UPDATE_TIME, SYN_STATE};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((SalesPlanDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(SalesPlanDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}
	
	/**
	 * Update 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		SalesPlanDTO dtoSalePlan = (SalesPlanDTO)dto;
		ContentValues value = initDataRow(dtoSalePlan);
		String[] params = { "" + dtoSalePlan.salePlanId };
		return update(value, SALE_PLAN_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(SALE_PLAN_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		SalesPlanDTO dtoSale = (SalesPlanDTO)dto;
		String[] params = { "" + dtoSale.salePlanId };
		return delete(SALE_PLAN_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: TruongHN
	 * @param id
	 * @return: RoleDTO
	 * @throws:
	 */
	public SalesPlanDTO getRowById(String id) {
		SalesPlanDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					SALE_PLAN_ID + " = ?" , params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private SalesPlanDTO initDTOFromCursor(Cursor c) {
		SalesPlanDTO dto = new SalesPlanDTO();
		dto.salePlanId = (c.getInt(c.getColumnIndex(SALE_PLAN_ID)));
		dto.shopId = (c.getInt(c.getColumnIndex(SHOP_ID)));
		dto.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		dto.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		dto.description = (c.getString(c.getColumnIndex(DESCRIPTION)));
		dto.isfocusItem = (c.getInt(c.getColumnIndex(IS_FOCUS_ITEM)));
		dto.categoryCode = (c.getString(c.getColumnIndex(CATEGORY_CODE)));
		dto.quantity = (c.getString(c.getColumnIndex(QUANTITY)));
		dto.amount = (c.getString(c.getColumnIndex(AMOUNT)));
		dto.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dto.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		dto.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dto.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dto.createTime = (c.getString(c.getColumnIndex(CREATE_TIME)));
		dto.updateTime = (c.getString(c.getColumnIndex(UPDATE_TIME)));
		
		return dto;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * @author: TruongHN
	 * @return: Vector<FeedBackDTO>
	 * @throws:
	 */
	public Vector<SalesPlanDTO> getAllRow() {
		Vector<SalesPlanDTO> v = new Vector<SalesPlanDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			SalesPlanDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(SalesPlanDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(SALE_PLAN_ID, dto.salePlanId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(DESCRIPTION, dto.description);
		editedValues.put(QUANTITY, dto.quantity);
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_TIME, dto.createTime);
		editedValues.put(UPDATE_TIME, dto.updateTime);
		editedValues.put(UPDATE_USER, dto.updateUser);

		return editedValues;
	}
}
