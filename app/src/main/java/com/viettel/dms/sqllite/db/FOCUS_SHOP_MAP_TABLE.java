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
import com.viettel.dms.dto.db.FocusShopMapDTO;
import com.viettel.utils.VTLog;

/**
 *  Luu CTTT ap dung cho NPP nao
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class FOCUS_SHOP_MAP_TABLE extends ABSTRACT_TABLE{
	// id CTTT
	public static final String FOCUS_PROGRAM_ID = "FOCUS_PROGRAM_ID";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// trang thai
	public static final String STATUS = "STATUS";
	// id bang
	public static final String FOCUS_SHOP_MAP_ID = "FOCUS_SHOP_MAP_ID";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	
	private static final String TABLE_FOCUS_SHOP_MAP = "FOCUS_SHOP_MAP";
	
	public FOCUS_SHOP_MAP_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_FOCUS_SHOP_MAP;
		this.columns = new String[] {FOCUS_PROGRAM_ID, SHOP_ID ,STATUS,FOCUS_SHOP_MAP_ID,CREATE_DATE,CREATE_USER,UPDATE_DATE,UPDATE_USER, SYN_STATE};
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
		ContentValues value = initDataRow((FocusShopMapDTO) dto);
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
	public long insert(FocusShopMapDTO dto) {
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
		FocusShopMapDTO disDTO = (FocusShopMapDTO)dto;
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
		FocusShopMapDTO cusDTO = (FocusShopMapDTO)dto;
		String[] params = { "" + cusDTO.focusProgrameId };
		return delete(FOCUS_PROGRAM_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public FocusShopMapDTO getRowById(String id) {
		FocusShopMapDTO dto = null;
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

	private FocusShopMapDTO initLogDTOFromCursor(Cursor c) {
		FocusShopMapDTO dpDetailDTO = new FocusShopMapDTO();
		
		dpDetailDTO.focusProgrameId = (c.getInt(c.getColumnIndex(FOCUS_PROGRAM_ID)));
		dpDetailDTO.shopId = (c.getInt(c.getColumnIndex(SHOP_ID)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.focusShopMapId = (c.getInt(c.getColumnIndex(FOCUS_SHOP_MAP_ID)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		
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
	public Vector<FocusShopMapDTO> getAllRow() {
		Vector<FocusShopMapDTO> v = new Vector<FocusShopMapDTO>();
		Cursor c = null;
		try {
			c = query( null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			FocusShopMapDTO DisplayPrdogrameLvDTO;
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

	private ContentValues initDataRow(FocusShopMapDTO dto) {
		ContentValues editedValues = new ContentValues();
		
		editedValues.put(FOCUS_PROGRAM_ID, dto.focusProgrameId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(STATUS, dto.status);
		editedValues.put(FOCUS_SHOP_MAP_ID, dto.focusShopMapId);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(UPDATE_USER, dto.updateUser);

		return editedValues;
	}
	
}
