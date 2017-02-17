/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.DISPLAY_PROGRAME_TABLE;
import com.viettel.dms.util.DateUtils;

/**
 * DTO chuong trinh trung bay
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class DisplayProgrameDTO extends AbstractTableDTO {
	// Id cua bang
	public long displayProgrameId;
	// ma CTTB
	public String displayProgrameCode;
	// ten CTTB
	public String displayProgrameName;
	// loai CTTB
	public String displayProgrameType;
	// trang thai
	public int status;
	// tu ngay
	public String fromDate;
	// den ngay
	public String toDate;
	// truong nay chua dung
	public float objTarget;
	// quan he: 1: OR, 0 : AND
	public int relation;
	// ngay tao
	public String createDate;
	// ngay update
	public String updateDate;
	// nguoi tao
	public String createUser;
	// nguoi update
	public String updateUser;
	// ma nganh hang
	public String cat;
	// % ti le fino
	public float percentFino;

	public int quantity;

	public int countCustomerNotComplete; // so luong khach hang chua dat - dung
											// cho nv giam sat

	public DisplayProgrameDTO() {
		super(TableType.DISPLAY_PROGRAME_TABLE);
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: SoaN
	 * @param c
	 * @return
	 * @return: DisplayProgrameDTO
	 * @throws:
	 */

	public void initDisplayProgrameObject(Cursor c) {

		if (c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_ID) >= 0) {
			displayProgrameId = c.getLong(c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_ID));
		} else {
			displayProgrameId = 0;
		}

		if (c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_CODE) >= 0) {
			displayProgrameCode = c.getString(c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_CODE));
		} else {
			displayProgrameCode = "";
		}

		if (c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_NAME) >= 0) {
			displayProgrameName = c.getString(c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DISPLAY_PROGRAM_NAME));
		} else {
			displayProgrameName = "";
		}

		if (c.getColumnIndex(DISPLAY_PROGRAME_TABLE.FROM_DATE) >= 0) {
			fromDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(DISPLAY_PROGRAME_TABLE.FROM_DATE)));
		} else {
			fromDate = "";
		}

		if (c.getColumnIndex(DISPLAY_PROGRAME_TABLE.TO_DATE) >= 0) {
			toDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(DISPLAY_PROGRAME_TABLE.TO_DATE)));
		} else {
			toDate = "";
		}
		if (c.getColumnIndex("SOSUAT") >= 0) {
			quantity = c.getInt(c.getColumnIndex("SOSUAT"));
		} else {
			quantity = 0;
		}
		if (c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DESCRIPTION) >= 0) {
			displayProgrameType = c.getString(c.getColumnIndex(DISPLAY_PROGRAME_TABLE.DESCRIPTION));
		} else {
			displayProgrameType = "";
		}

	}
}
