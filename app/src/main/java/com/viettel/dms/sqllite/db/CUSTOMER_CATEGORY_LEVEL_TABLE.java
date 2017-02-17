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

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerCatLevelDTO;
import com.viettel.utils.VTLog;

/**
 *  Muc doanh so khach hang
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class CUSTOMER_CATEGORY_LEVEL_TABLE extends ABSTRACT_TABLE {
	// id cua bang
	public static final String CUSTOMER_CAT_LEVEL_ID = "CUSTOMER_CAT_LEVEL_ID";
	// ma khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// ma code level
	public static final String CATEGORY_CODE = "CATEGORY_CODE";
	// level
	public static final String LEVEL_ID = "LEVEL_ID";
	// nam
	public static final String YEAR = "YEAR";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	
	public static final String CUSTOMER_CATEGORY_LEVEL_TABLE = "CUSTOMER_CATEGORY_LEVEL";
	
	
	public CUSTOMER_CATEGORY_LEVEL_TABLE(SQLiteDatabase mDB) {
		this.tableName = CUSTOMER_CATEGORY_LEVEL_TABLE;
		this.columns = new String[] {CUSTOMER_CAT_LEVEL_ID, CUSTOMER_ID,CATEGORY_CODE, LEVEL_ID, YEAR, 
				CREATE_DATE, UPDATE_DATE, CREATE_USER, UPDATE_USER, SYN_STATE};
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
		ContentValues value = initDataRow((CustomerCatLevelDTO) dto);
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
	public long insert(CustomerCatLevelDTO dto) {
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
		CustomerCatLevelDTO disDTO = (CustomerCatLevelDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.customerCatLevelId };
		return update(value, CUSTOMER_CAT_LEVEL_ID + " = ?", params);
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
		return delete(CUSTOMER_CAT_LEVEL_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		CustomerCatLevelDTO disDTO = (CustomerCatLevelDTO)dto;
		String[] params = { "" + disDTO.customerCatLevelId };
		return delete(CUSTOMER_CAT_LEVEL_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public CustomerCatLevelDTO getRowById(String id) {
		CustomerCatLevelDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					CUSTOMER_CAT_LEVEL_ID + " = ?" , params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initCustomerCatLevelDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private CustomerCatLevelDTO initCustomerCatLevelDTOFromCursor(Cursor c) {
		CustomerCatLevelDTO cusDTO = new CustomerCatLevelDTO();
		cusDTO.customerCatLevelId = (c.getInt(c.getColumnIndex(CUSTOMER_CAT_LEVEL_ID)));
		cusDTO.customerId = (c.getString(c.getColumnIndex(CUSTOMER_ID)));
		cusDTO.categoryCode = (c.getString(c.getColumnIndex(CATEGORY_CODE)));
		cusDTO.levelId = (c.getInt(c.getColumnIndex(LEVEL_ID)));
		cusDTO.year = (c.getString(c.getColumnIndex(YEAR)));
		cusDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		cusDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		return cusDTO;
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
	public Vector<CustomerCatLevelDTO> getAllRow() {
		Vector<CustomerCatLevelDTO> v = new Vector<CustomerCatLevelDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			CustomerCatLevelDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initCustomerCatLevelDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(CustomerCatLevelDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_CAT_LEVEL_ID, dto.customerCatLevelId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(CATEGORY_CODE, dto.categoryCode);
		editedValues.put(LEVEL_ID, dto.levelId);
		editedValues.put(YEAR, dto.year);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_DATE, dto.createDate);

		return editedValues;
	}
	
	
	/**
	 * Lay danh sach muc doanh so khach hang
	 * @author : BangHN
	 * since : 1.0
	 * @throws Exception 
	 */
	public ArrayList<CustomerCatLevelDTO> getListCustomerCatLevel(String customerId) throws Exception{
		ArrayList<CustomerCatLevelDTO> result = new ArrayList<CustomerCatLevelDTO>();
		Cursor cursor = null;
		StringBuilder sql = new StringBuilder();
		List<String> params = new ArrayList<String>();
		sql.append(" select CUSTOMER_CAT_LEVEL_ID, CATEGORY_CODE, CUSTOMER_ID,LEVEL_ID");
		sql.append(" from CUSTOMER_CATEGORY_LEVEL");
		sql.append(" WHERE   CUSTOMER_ID = ?");
		params.add(customerId);
		try {
			String[] arrParam = new String[params.size()];
			for (int i = 0; i < params.size(); i++) {
				arrParam[i] = params.get(i);
			}
			cursor = rawQuery(sql.toString(), arrParam);
			CustomerCatLevelDTO cusLevel;
			if (cursor != null) {
				if (cursor.moveToFirst()) {
					do{
					cusLevel = new CustomerCatLevelDTO();
					cusLevel.customerCatLevelId = cursor.getInt(cursor.getColumnIndex("CUSTOMER_CAT_LEVEL_ID"));
					cusLevel.customerId = cursor.getString(cursor.getColumnIndex("CUSTOMER_ID"));
					cusLevel.categoryCode = cursor.getString(cursor.getColumnIndex("CATEGORY_CODE"));
					cusLevel.levelId = cursor.getInt(cursor.getColumnIndex("LEVEL_ID"));
					result.add(cusLevel);
					}while(cursor.moveToNext());
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			if (cursor != null) {
				try {
					cursor.close();
				} catch (Exception ex) {
				}
			} else {
			}
		}
		return result;
	}
	
}
