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
import com.viettel.dms.dto.db.FocusProDetailDTO;
import com.viettel.utils.VTLog;

/**
 *  Chi tiet chuong trinh trong tam
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class FOCUS_PROGRAME_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String FOCUS_PROGRAME_DETAIL_ID = "FOCUS_PROGRAME_DETAIL_ID";
	// id cua CTTT
	public static final String FOCUS_PROGRAME_ID= "FOCUS_PROGRAME_ID";
	// id cua san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	
	private static final String TABLE_FOCUS_PROGRAME_DETAIL = "FOCUS_PROGRAME_DETAIL";
	
	public FOCUS_PROGRAME_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_FOCUS_PROGRAME_DETAIL;
		this.columns = new String[] {FOCUS_PROGRAME_DETAIL_ID, FOCUS_PROGRAME_ID ,PRODUCT_ID,CREATE_USER,UPDATE_USER,CREATE_DATE,UPDATE_DATE, SYN_STATE};
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
		ContentValues value = initDataRow((FocusProDetailDTO) dto);
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
	public long insert(FocusProDetailDTO dto) {
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
		FocusProDetailDTO disDTO = (FocusProDetailDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.focusProgrameDetailId };
		return update(value, FOCUS_PROGRAME_DETAIL_ID + " = ?", params);
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
		return delete(FOCUS_PROGRAME_DETAIL_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		FocusProDetailDTO cusDTO = (FocusProDetailDTO)dto;
		String[] params = { "" + cusDTO.focusProgrameDetailId };
		return delete(FOCUS_PROGRAME_DETAIL_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public FocusProDetailDTO getRowById(String id) {
		FocusProDetailDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					FOCUS_PROGRAME_DETAIL_ID + " = ?" , params, null, null, null);
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

	private FocusProDetailDTO initLogDTOFromCursor(Cursor c) {
		FocusProDetailDTO dpDetailDTO = new FocusProDetailDTO();

		dpDetailDTO.focusProgrameDetailId = (c.getInt(c.getColumnIndex(FOCUS_PROGRAME_DETAIL_ID)));
		dpDetailDTO.focusProgrameId = (c.getInt(c.getColumnIndex(FOCUS_PROGRAME_ID)));
		dpDetailDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
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
	public Vector<FocusProDetailDTO> getAllRow() {
		Vector<FocusProDetailDTO> v = new Vector<FocusProDetailDTO>();
		Cursor c = null;
		try {
			c = query(null,null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			FocusProDetailDTO DisplayPrdogrameLvDTO;
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

	private ContentValues initDataRow(FocusProDetailDTO dto) {
		ContentValues editedValues = new ContentValues();
		
		editedValues.put(FOCUS_PROGRAME_DETAIL_ID, dto.focusProgrameDetailId);
		editedValues.put(FOCUS_PROGRAME_ID, dto.focusProgrameId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);

		return editedValues;
	}
}
