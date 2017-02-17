/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayProgrameSubDTO;

/**
 *  Cac sub-cat can co trong CTTB
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class DISPLAY_PROGRAME_SUB_CAT_TABLE extends ABSTRACT_TABLE {
	// id cua bang
	public static final String DISPLAY_PROGRAME_SUB_CAT_ID = "DISPLAY_PROGRAME_SUB_CAT_ID";
	// id CTTB
	public static final String DISPLAY_PROGRAME_ID = "DISPLAY_PROGRAME_ID";
	 // ma sub_cat cua san pham
	public static final String SUB_CAT = "SUB_CAT";
	// trang thai
	public static final String STATUS = "STATUS";
	
	private static final String TABLE_DISPLAY_PROGRAME_SUB_CAT_TABLE = "DISPLAY_PROGRAME_SUB_CAT";
	
	public DISPLAY_PROGRAME_SUB_CAT_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_PROGRAME_SUB_CAT_TABLE;
		this.columns = new String[] {DISPLAY_PROGRAME_SUB_CAT_ID, DISPLAY_PROGRAME_ID ,SUB_CAT,STATUS, SYN_STATE};
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
		ContentValues value = initDataRow((DisplayProgrameSubDTO) dto);
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
	public long insert(DisplayProgrameSubDTO dto) {
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
		DisplayProgrameSubDTO disDTO = (DisplayProgrameSubDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayProgrameSubCatId };
		return update(value, DISPLAY_PROGRAME_SUB_CAT_ID + " = ?", params);
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
		return delete(DISPLAY_PROGRAME_SUB_CAT_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		DisplayProgrameSubDTO disDTO = (DisplayProgrameSubDTO)dto;
		String[] params = { "" + disDTO.displayProgrameSubCatId };
		return delete(DISPLAY_PROGRAME_SUB_CAT_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayProgrameSubDTO
	 * @throws:
	 */
	public DisplayProgrameSubDTO getRowById(String id) {
		DisplayProgrameSubDTO DisplayProgrameSubDTO = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(DISPLAY_PROGRAME_SUB_CAT_ID + " = ?" , params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				DisplayProgrameSubDTO = initDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return DisplayProgrameSubDTO;
	}

	private DisplayProgrameSubDTO initDTOFromCursor(Cursor c) {
		DisplayProgrameSubDTO dpDetailDTO = new DisplayProgrameSubDTO();

		dpDetailDTO.displayProgrameId = (c.getInt(c.getColumnIndex(DISPLAY_PROGRAME_ID)));
		dpDetailDTO.displayProgrameSubCatId = (c.getInt(c.getColumnIndex(DISPLAY_PROGRAME_SUB_CAT_ID)));
		dpDetailDTO.subCat = (c.getString(c.getColumnIndex(SUB_CAT)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		
		return dpDetailDTO;
	}

	private ContentValues initDataRow(DisplayProgrameSubDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(DISPLAY_PROGRAME_ID, dto.displayProgrameId);
		editedValues.put(DISPLAY_PROGRAME_SUB_CAT_ID, dto.displayProgrameSubCatId);
		editedValues.put(SUB_CAT, dto.subCat);
		editedValues.put(STATUS, dto.status);

		return editedValues;
	}
}
