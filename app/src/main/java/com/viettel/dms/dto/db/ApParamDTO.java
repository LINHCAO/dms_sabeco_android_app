/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.AP_PARAM_TABLE;
import com.viettel.dms.util.StringUtil;

/**
 * Thong tin cau hinh
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class ApParamDTO extends AbstractTableDTO {
	// id
	public long apParamId;
	// ma tham so cau hinh
	public String apParamCode;
	// gia tri tham so cau hinh
	public String value;
	// trang thai 0: het hieu luc, 1: dang hieu luc
	public String status;
	// ghi chu
	public String description;
	// name
	public String apParamName;
	// TYPE
	public String type;

	public static String PRIORITY_NOW = "A";
	public static String PRIORITY_IN_DAY = "B";
	public static String PRIORITY_OUT_DAY = "C";

	public ApParamDTO() {
		super(TableType.AP_PARAM_TABLE);
	}

	/**
	 * 
	 * init object with cursor
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 * 
	 */
	public void initObjectWithCursor(Cursor c) {
		// id
		if (c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_ID) >= 0) {
			apParamId = c.getLong(c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_ID));
		} else {
			apParamId = 0;
		}
		// code
		if (c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_CODE) >= 0) {
			apParamCode = c.getString(c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_CODE));
		} else {
			apParamCode = Constants.STR_BLANK;
		}
		// value
		if (c.getColumnIndex(AP_PARAM_TABLE.VALUE) >= 0) {
			value = c.getString(c.getColumnIndex(AP_PARAM_TABLE.VALUE));
		} else {
			value = Constants.STR_BLANK;
		}
		// value
		if (c.getColumnIndex(AP_PARAM_TABLE.TYPE) >= 0) {
			type = c.getString(c.getColumnIndex(AP_PARAM_TABLE.TYPE));
		} else {
			type = Constants.STR_BLANK;
		}
		// status
		if (c.getColumnIndex(AP_PARAM_TABLE.STATUS) >= 0) {
			status = c.getString(c.getColumnIndex(AP_PARAM_TABLE.STATUS));
		} else {
			status = Constants.STR_BLANK;
		}

		// description
		if (c.getColumnIndex(AP_PARAM_TABLE.DESCRIPTION) >= 0) {
			description = c.getString(c.getColumnIndex(AP_PARAM_TABLE.DESCRIPTION));
			if(StringUtil.isNullOrEmpty(description)) {
				description = Constants.STR_BLANK;
			}
		} else {
			description = Constants.STR_BLANK;
		}
		// description
		if (c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_NAME) >= 0) {
			apParamName = c.getString(c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_NAME));
		} else {
			apParamName = Constants.STR_BLANK;
		}
	}
}
