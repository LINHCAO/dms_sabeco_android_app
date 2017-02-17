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
import com.viettel.dms.dto.db.DisplayProgrameDetailDTO;
import com.viettel.utils.VTLog;

/**
 *  Chi tiet chuong trinh trung bay
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class DISPLAY_PROGRAME_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id cua bang
	public static final String DISPLAY_PROGRAM_DETAIL_ID = "DISPLAY_PROGRAM_DETAIL_ID";
	// id cua CTTB
	public static final String DISPLAY_PROGRAM_ID = "DISPLAY_PROGRAM_ID";
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// so luong can TB
	public static final String QUALTITY = "QUALTITY";
	// truong nay chua dung
	public static final String BRAND_GROUP = "BRAND_GROUP";
	// ma nganh hang
	public static final String CATEGORY_CODE = "CATEGORY_CODE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	private static final String TABLE_DISPLAY_PROGRAME_DETAIL = "DISPLAY_PROGRAM_DETAIL";

	public DISPLAY_PROGRAME_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_PROGRAME_DETAIL;
		this.columns = new String[] { DISPLAY_PROGRAM_DETAIL_ID,
				DISPLAY_PROGRAM_ID, PRODUCT_ID, QUALTITY, BRAND_GROUP,
				CATEGORY_CODE, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE, SYN_STATE};
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
		ContentValues value = initDataRow((DisplayProgrameDetailDTO) dto);
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
	public long insert(DisplayProgrameDetailDTO dto) {
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
		DisplayProgrameDetailDTO disDTO = (DisplayProgrameDetailDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayProgrameDetailId };
		return update(value, DISPLAY_PROGRAM_DETAIL_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(DISPLAY_PROGRAM_DETAIL_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		DisplayProgrameDetailDTO cusDTO = (DisplayProgrameDetailDTO)dto;
		String[] params = { String.valueOf(cusDTO.displayProgrameDetailId)};
		return delete(DISPLAY_PROGRAM_DETAIL_ID + " = ?", params);
	}


	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayProgrameDetailDTO
	 * @throws:
	 */
	public DisplayProgrameDetailDTO getRowById(String id) {
		DisplayProgrameDetailDTO DisplayProgrameDetailDTO = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(DISPLAY_PROGRAM_DETAIL_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				DisplayProgrameDetailDTO = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return DisplayProgrameDetailDTO;
	}

	private DisplayProgrameDetailDTO initLogDTOFromCursor(Cursor c) {
		DisplayProgrameDetailDTO dpDetailDTO = new DisplayProgrameDetailDTO();

		dpDetailDTO.displayProgrameDetailId = (c.getInt(c.getColumnIndex(DISPLAY_PROGRAM_DETAIL_ID)));
		dpDetailDTO.displayProgrameId = (c.getInt(c.getColumnIndex(DISPLAY_PROGRAM_ID)));
		dpDetailDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		dpDetailDTO.qualtity = (c.getInt(c.getColumnIndex(QUALTITY)));
		dpDetailDTO.brandGroup = (c.getString(c.getColumnIndex(BRAND_GROUP)));
		dpDetailDTO.categoryCode = (c.getString(c.getColumnIndex(CATEGORY_CODE)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));

		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		
		return dpDetailDTO;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * @author: HieuNH
	 * @return: Vector<DisplayProgrameDetailDTO>
	 * @throws:
	 */
	public Vector<DisplayProgrameDetailDTO> getAllRow() {
		Vector<DisplayProgrameDetailDTO> v = new Vector<DisplayProgrameDetailDTO>();
		Cursor c = null;
		try {
			
			c = query( null,null, null, null, null);
			
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			DisplayProgrameDetailDTO DisplayProgrameDetailDTO;
			if (c.moveToFirst()) {
				do {
					DisplayProgrameDetailDTO = initLogDTOFromCursor(c);
					v.addElement(DisplayProgrameDetailDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(DisplayProgrameDetailDTO dto) {
		ContentValues editedValues = new ContentValues();
		
		editedValues.put(DISPLAY_PROGRAM_DETAIL_ID, dto.displayProgrameDetailId);
		editedValues.put(DISPLAY_PROGRAM_ID, dto.displayProgrameId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(QUALTITY, dto.qualtity);
		editedValues.put(BRAND_GROUP, dto.brandGroup);
		editedValues.put(CATEGORY_CODE, dto.categoryCode);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		

		return editedValues;
	}
}
