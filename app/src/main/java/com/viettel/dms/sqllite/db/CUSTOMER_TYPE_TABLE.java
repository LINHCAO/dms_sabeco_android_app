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
import com.viettel.dms.dto.db.CustomerTypeDTO;
import com.viettel.utils.VTLog;

/**
 *  Loai khach hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class CUSTOMER_TYPE_TABLE extends ABSTRACT_TABLE {
	// id cua bang
	public static final String CUSTOMER_TYPE_ID = "CUSTOMER_TYPE_ID";
	// ma loai khach hang
	public static final String CUSTOMER_TYPE_CODE = "CUSTOMER_TYPE_CODE";
	// ten loai khach hang
	public static final String CUSTOMER_TYPE_NAME = "CUSTOMER_TYPE_NAME";
	// trang thai
	public static final String STATUS = "STATUS";
	// ghi chu
	public static final String NOTE = "NOTE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	
	public static final String CUSTOMER_TYPE_TABLE = "CUSTOMER_TYPE";
	
	
	public CUSTOMER_TYPE_TABLE(SQLiteDatabase mDB) {
		this.tableName = CUSTOMER_TYPE_TABLE;
		this.columns = new String[] {CUSTOMER_TYPE_ID, CUSTOMER_TYPE_CODE ,CUSTOMER_TYPE_NAME,STATUS,NOTE,CREATE_USER,CREATE_DATE, SYN_STATE};
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
		ContentValues value = initDataRow((CustomerTypeDTO) dto);
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
	public long insert(CustomerTypeDTO dto) {
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
		CustomerTypeDTO disDTO = (CustomerTypeDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.customerTypeId };
		return update(value, CUSTOMER_TYPE_ID + " = ?", params);
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
		return delete(CUSTOMER_TYPE_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		CustomerTypeDTO disDTO = (CustomerTypeDTO)dto;
		String[] params = { "" + disDTO.customerTypeId };
		return delete(CUSTOMER_TYPE_ID + " = ?", params);
	}


	private CustomerTypeDTO initCustomerTypeDTOFromCursor(Cursor c) {
		CustomerTypeDTO cusDTO = new CustomerTypeDTO();
		cusDTO.customerTypeId = (c.getInt(c.getColumnIndex(CUSTOMER_TYPE_ID)));
		cusDTO.customerTypeCode = (c.getString(c.getColumnIndex(CUSTOMER_TYPE_CODE)));
		cusDTO.customerTypeName = (c.getString(c.getColumnIndex(CUSTOMER_TYPE_NAME)));
		cusDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		cusDTO.note = (c.getString(c.getColumnIndex(NOTE)));
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
	public Vector<CustomerTypeDTO> getAllRow() {
		Vector<CustomerTypeDTO> v = new Vector<CustomerTypeDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			CustomerTypeDTO dto;
			if (c.moveToFirst()) {
				do {
					dto = initCustomerTypeDTOFromCursor(c);
					v.addElement(dto);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(CustomerTypeDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_TYPE_ID, dto.customerTypeId);
		editedValues.put(CUSTOMER_TYPE_CODE, dto.customerTypeCode);
		editedValues.put(CUSTOMER_TYPE_NAME, dto.customerTypeName);
		editedValues.put(STATUS, dto.status);
		editedValues.put(NOTE, dto.note);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_DATE, dto.createDate);

		return editedValues;
	}
}
