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
import com.viettel.dms.dto.db.FocusProgrameDTO;
import com.viettel.utils.VTLog;

/**
 *  Luu chuong trinh trong tam
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class FOCUS_PROGRAME_TABLE extends ABSTRACT_TABLE{
	// id bang
	public static final String FOCUS_PROGRAM_ID = "FOCUS_PROGRAM_ID";
	// ma CTTT
	public static final String FOCUS_PROGRAM_CODE = "FOCUS_PROGRAM_CODE";
	// ten CTTT
	public static final String FOCUS_PROGRAM_NAME = "FOCUS_PROGRAM_NAME";
	// trang thai
	public static final String STATUS = "STATUS";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	
	private static final String TABLE_FOCUS_PROGRAME = "FOCUS_PROGRAME";

	public FOCUS_PROGRAME_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_FOCUS_PROGRAME;
		this.columns = new String[] { FOCUS_PROGRAM_ID,
				FOCUS_PROGRAM_CODE, FOCUS_PROGRAM_NAME, STATUS, FROM_DATE,
				TO_DATE, CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE, SYN_STATE };
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
		ContentValues value = initDataRow((FocusProgrameDTO) dto);
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
	public long insert(FocusProgrameDTO dto) {
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
		FocusProgrameDTO disDTO = (FocusProgrameDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.focusProgrameId };
		return update(value, FOCUS_PROGRAM_ID + " = ?", params);
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
		return delete(FOCUS_PROGRAM_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		FocusProgrameDTO disDTO = (FocusProgrameDTO)dto;
		String[] params = { "" + disDTO.focusProgrameId };
		return delete(FOCUS_PROGRAM_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public FocusProgrameDTO getRowById(String id) {
		FocusProgrameDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					FOCUS_PROGRAM_ID + " = ?" , params, null, null, null);
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

	private FocusProgrameDTO initLogDTOFromCursor(Cursor c) {
		FocusProgrameDTO dpDetailDTO = new FocusProgrameDTO();

		dpDetailDTO.focusProgrameId = (c.getInt(c
				.getColumnIndex(FOCUS_PROGRAM_ID)));
		dpDetailDTO.focusProgameCode = (c.getString(c
				.getColumnIndex(FOCUS_PROGRAM_CODE)));
		dpDetailDTO.focusProgrameName = (c.getString(c
				.getColumnIndex(FOCUS_PROGRAM_NAME)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.toDate = (c.getString(c.getColumnIndex(TO_DATE)));

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
	public Vector<FocusProgrameDTO> getAllRow() {
		Vector<FocusProgrameDTO> v = new Vector<FocusProgrameDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			FocusProgrameDTO DisplayPrdogrameLvDTO;
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

	private ContentValues initDataRow(FocusProgrameDTO dto) {
		ContentValues editedValues = new ContentValues();
		
		editedValues.put(FOCUS_PROGRAM_ID, dto.focusProgrameId);
		editedValues.put(FOCUS_PROGRAM_CODE, dto.focusProgameCode);
		editedValues.put(FOCUS_PROGRAM_NAME, dto.focusProgrameName);
		editedValues.put(STATUS, dto.status);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);
		
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		
		return editedValues;
	}

}
