/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.StaffDTO;
import com.viettel.dms.sqllite.db.STAFF_POSITION_LOG_TABLE;
import com.viettel.dms.sqllite.db.STAFF_TABLE;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/** 
 * Gsnpp Route Supervision Item class
 * GsnppRouteSupervisionItem.java
 * @version: 1.0 
 * @since:  10:27:30 8 Jan 2014
 */
@SuppressWarnings("serial")
public class GsnppRouteSupervisionItem implements Serializable {
	public StaffDTO aStaff = new StaffDTO();
	public int numTotalCus;
	public int numVisited;
	public int numRightPlan;
	public int numWrongPlan;
	public int numLessThan2Min;
	public int numOnTime;
	public int numMoreThan30Min;
	public double lat;
	public double lng;
	public String updateTime;
	public String visitingCusName;
	public String lessThan2MinList;
	public String trainingDate;
	public String moreThan30MinList;

	public GsnppRouteSupervisionItem() {
	}

	public void initDataFromCursor(Cursor c) {
		try {
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			aStaff.staffId = (int) StringUtil.getLongFromSQliteCursor(c, "STAFF_ID");
			aStaff.staffCode = StringUtil.getStringFromSQliteCursor(c, "STAFF_CODE");
			aStaff.name = StringUtil.getStringFromSQliteCursor(c, "NAME");
			aStaff.shopId = (int) StringUtil.getLongFromSQliteCursor(c, "SHOP_ID");
			aStaff.mobile = StringUtil.getStringFromSQliteCursor(c, "MOBILE");
			lat = StringUtil.getDoubleFromSQliteCursor(c, "LAT");
			lng = StringUtil.getDoubleFromSQliteCursor(c, "LNG");
			numTotalCus = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_TOTAL_CUS");
			numVisited = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_VISITED");
			numRightPlan = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_RIGHT_PLAN");
			numWrongPlan = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_WRONG_PLAN");
			numLessThan2Min = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_LESTTHAN_2MIN");
			numMoreThan30Min = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_MORETHAN_30MIN");
			numOnTime = (int) StringUtil.getLongFromSQliteCursor(c, "NUM_ON_TIME");
			lessThan2MinList = StringUtil.getStringFromSQliteCursor(c, "LESSTHAN_2_MIN_LIST");
			moreThan30MinList = StringUtil.getStringFromSQliteCursor(c, "MORETHAN_30_MIN_LIST");
			
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	/**
	 * Lay thong tin ds vi tri NVBH trong 2 ngay
	 * 
	 * @author: TruongHN
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initListPostSalePersons(Cursor c) {
		try {
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			if (c.getColumnIndex(STAFF_TABLE.STAFF_ID) >= 0) {
				aStaff.staffId = c.getLong(c.getColumnIndex(STAFF_TABLE.STAFF_ID));
			}
			if (c.getColumnIndex(STAFF_TABLE.STAFF_CODE) >= 0) {
				aStaff.staffCode = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_CODE));
			}
			if (c.getColumnIndex(STAFF_TABLE.STAFF_NAME) >= 0) {
				aStaff.name = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_NAME));
			}
			if (c.getColumnIndex(STAFF_TABLE.SHOP_ID) >= 0) {
				aStaff.shopId = c.getInt(c.getColumnIndex(STAFF_TABLE.SHOP_ID));
			}
			if (c.getColumnIndex(STAFF_TABLE.MOBILE_PHONE) >= 0) {
				aStaff.mobile = c.getString(c.getColumnIndex(STAFF_TABLE.MOBILE_PHONE));
			}
			if (c.getColumnIndex(STAFF_TABLE.PHONE) >= 0) {
				aStaff.phone = c.getString(c.getColumnIndex(STAFF_TABLE.PHONE));
			}
			if (StringUtil.isNullOrEmpty(aStaff.mobile)) {
				aStaff.mobile = aStaff.phone;
			}
			if (StringUtil.isNullOrEmpty(aStaff.mobile)) {
				aStaff.mobile = Constants.STR_BLANK;
			}
			if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LAT) >= 0) {
				lat = c.getFloat(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LAT));
			}
			if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LNG) >= 0) {
				lng = c.getFloat(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LNG));
			}
			if (c.getColumnIndex("DATE_TIME") >= 0) {
				updateTime = c.getString(c.getColumnIndex("DATE_TIME"));
			}
			String start_time = null;
			String end_time = null;
			if (c.getColumnIndex("START_TIME") >= 0) {
				start_time = c.getString(c.getColumnIndex("START_TIME"));
			}
			if (c.getColumnIndex("END_TIME") >= 0) {
				end_time = c.getString(c.getColumnIndex("END_TIME"));
			}
			if (!StringUtil.isNullOrEmpty(start_time) && StringUtil.isNullOrEmpty(end_time)) {
				if (c.getColumnIndex("CUS_NAME") >= 0) {
					visitingCusName = c.getString(c.getColumnIndex("CUS_NAME"));
				}
			}
			if (c.getColumnIndex("TRAINING_DATE") >= 0) {
				trainingDate = c.getString(c.getColumnIndex("TRAINING_DATE"));
			}

		} catch (Exception e) {
		}
	}
}
