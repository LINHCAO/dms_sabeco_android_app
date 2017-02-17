/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.db;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;

/**
 * Thong tin quyen cua ung dung duoc su dung duoi tablet
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class AppRuleDTO implements Serializable{
	public int ruleType;
	public String appCode;

	public AppRuleDTO() {
	}

	public void initObjectWithCursor(Cursor c) {
		// id
		if (c.getColumnIndex("rule_type") >= 0) {
			ruleType = c.getInt(c.getColumnIndex("rule_type"));
		} else {
			ruleType = 0;
		}
		// code
		if (c.getColumnIndex("app_code") >= 0) {
			appCode = c.getString(c.getColumnIndex("app_code"));
		} else {
			appCode = Constants.STR_BLANK;
		}
	}
}
