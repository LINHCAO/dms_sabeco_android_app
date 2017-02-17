/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.STAFF_TABLE;

/**
 * GSNPP info , use for function CTTB of NPP (module TBHV)
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class GSNPPInfoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public long staffId;
	public String staffCode;
	public String staffName;

	public GSNPPInfoDTO() {
		staffId = 0;
		staffCode = Constants.STR_BLANK;
		staffName = Constants.STR_BLANK;
	}

	public void initObjectWithCursor(Cursor c) {
		if (c.getColumnIndex(STAFF_TABLE.STAFF_ID) >= 0) {
			staffId = c.getLong(c.getColumnIndex(STAFF_TABLE.STAFF_ID));
		} else {
			staffId = 0;
		}

		if (c.getColumnIndex(STAFF_TABLE.STAFF_CODE) >= 0) {
			staffCode = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_CODE));
		} else {
			staffCode = "";
		}

		if (c.getColumnIndex(STAFF_TABLE.STAFF_NAME) >= 0) {
			staffName = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_NAME));
		} else {
			staffName = "";
		}
	}

}
