/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.DISPLAY_PROGRAME_LEVEL_TABLE;

/**
 * Muc cua chuong trinh trung bay
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class DisplayProgrameLvDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ma CTTB
	public String displayProgrameCode;
	// muc CTTB
	public String levelCode;
	// so tien cua muc
	public int amount;
	// tu ngay
	public String fromDate;
	// den ngay
	public String toDate;
	// trang thai
	public int status;
	// long diplay program id
	public long dislayProgramId;

	public DisplayProgrameLvDTO() {
		super(TableType.DISPLAY_PROGRAME_LEVEL_TABLE);
	}

	/**
	 * 
	 * parse data with cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 * @since: Feb 19, 2013
	 */
	public void parseWithCursor(Cursor c) {
		if (c.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.DISPLAY_PROGRAM_ID) >= 0) {
			this.dislayProgramId = c
					.getLong(c
							.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.DISPLAY_PROGRAM_ID));
		} else {
			this.dislayProgramId = 0;
		}
		if (c.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.LEVEL_CODE) >= 0) {
			this.levelCode = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.LEVEL_CODE));
		} else {
			this.levelCode = "";
		}
		if (c.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.FROM_DATE) >= 0) {
			this.fromDate = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.FROM_DATE));
		} else {
			this.fromDate = "";
		}
		if (c.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.TO_DATE) >= 0) {
			this.toDate = c.getString(c
					.getColumnIndex(DISPLAY_PROGRAME_LEVEL_TABLE.TO_DATE));
		} else {
			this.toDate = "";
		}
	}
}
