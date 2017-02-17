/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.STAFF_POSITION_LOG_TABLE;
import com.viettel.dms.sqllite.db.STAFF_TABLE;
import com.viettel.map.dto.LatLng;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: Nguyen Thanh Dung
 * @version: 1.1
 * @since: 1.0
 */

public class AttendanceDTO {
	// id nhan vien
	public long staffId;
	// ma nhan vien
	public String staffCode;
	// ten
	public String name;
	public String time1;
	public String time2;
	public LatLng position1;
	public LatLng position2;
	public double distance1 = -1;
	public double distance2 = -1;
	public String shopId;

	public boolean onTime;

	/**
	 * 
	 */
	public AttendanceDTO() {
		position1 = new LatLng();
		position2 = new LatLng();
		time1 = "";
		time2 = "";
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param c
	 * @return: void
	 * @throws:
	 */

	public void initWithCursor(Cursor c) {
		// id
		if (c.getColumnIndex(STAFF_TABLE.STAFF_ID) >= 0) {
			staffId = c.getLong(c.getColumnIndex(STAFF_TABLE.STAFF_ID));
		} else {
			staffId = 0;
		}
		// code
		if (c.getColumnIndex(STAFF_TABLE.STAFF_CODE) >= 0) {
			staffCode = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_CODE));
		} else {
			staffCode = Constants.STR_BLANK;
		}
		// value
		if (c.getColumnIndex(STAFF_TABLE.STAFF_NAME) >= 0) {
			name = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_NAME));
		} else {
			name = Constants.STR_BLANK;
		}

		// status
		if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.CREATE_DATE) >= 0) {
			time1 = c.getString(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.CREATE_DATE));
		} else {
			time1 = Constants.STR_BLANK;
		}

		// description
		if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LAT) >= 0) {
			position1.lat = c.getDouble(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LAT));
		} else {
			position1.lat = -1;
		}
		if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LNG) >= 0) {
			position1.lng = c.getDouble(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LNG));
		} else {
			position1.lng = -1;
		}
	}
}
