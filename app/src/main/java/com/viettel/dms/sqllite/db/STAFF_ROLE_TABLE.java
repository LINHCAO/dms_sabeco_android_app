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
import com.viettel.dms.dto.db.StaffRoleDTO;
import com.viettel.utils.VTLog;

/**
 *  Luu thong tin role cua nhan vien
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class STAFF_ROLE_TABLE extends ABSTRACT_TABLE {
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// id quyen
	public static final String ROLE_ID = "ROLE_ID";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// 1: hieu luc, 0: khong hieu luc
	public static final String STATUS = "STATUS";
	
	private static final String STAFF_ROLE_TABLE = "STAFF_ROLE";
	
	public STAFF_ROLE_TABLE(SQLiteDatabase mDB) {
		this.tableName = STAFF_ROLE_TABLE;
		this.columns = new String[] {STAFF_ID, ROLE_ID ,FROM_DATE, TO_DATE, STATUS, SYN_STATE};
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
		ContentValues value = initDataRow((StaffRoleDTO) dto);
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
	public long insert(StaffRoleDTO dto) {
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
		StaffRoleDTO dtoRole = (StaffRoleDTO)dto;
		ContentValues value = initDataRow(dtoRole);
		String[] params = { "" + dtoRole.roleId };
		return update(value, ROLE_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String roleId, String staffId) {
		String[] params = { roleId, staffId };
		return delete(ROLE_ID + " = ? and " + STAFF_ID + " =? ", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		StaffRoleDTO dtoRole = (StaffRoleDTO)dto;
		String[] params = { "" + dtoRole.roleId, "" + dtoRole.staffId };
		return delete(ROLE_ID + " = ? and " + STAFF_ID + " =? ", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: TruongHN
	 * @param id
	 * @return: StaffRoleDTO
	 * @throws:
	 */
	public StaffRoleDTO getRowById(String id) {
		StaffRoleDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					ROLE_ID + " = ?" , params, null, null, null);
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

	private StaffRoleDTO initDTOFromCursor(Cursor c) {
		StaffRoleDTO dto = new StaffRoleDTO();
		dto.roleId = (c.getInt(c.getColumnIndex(ROLE_ID)));
		dto.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		dto.status = (c.getInt(c.getColumnIndex(STATUS)));
		dto.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dto.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		return dto;
	}

	/**
	 * lay tat ca cac dong cua CSDL
	 * @author: TruongHN
	 * @return: Vector<StaffRoleDTO>
	 * @throws:
	 */
	public Vector<StaffRoleDTO> getAllRow() {
		Vector<StaffRoleDTO> v = new Vector<StaffRoleDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			StaffRoleDTO dto;
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

	private ContentValues initDataRow(StaffRoleDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ROLE_ID, dto.roleId);
		editedValues.put(STATUS, dto.status);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);

		return editedValues;
	}
}
