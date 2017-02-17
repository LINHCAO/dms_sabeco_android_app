/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.STAFF_TABLE;

 
/**
 * Thong tin bao cao cua NVBH ghe tham khach hang trong ngay
 * 
 * ReportNVBHVisitCustomerDTO.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  3:24:51 PM Dec 5, 2013
 */
public class ReportNVBHVisitCustomerDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ghe tham khach hang dau tien trong tuyen
	public static final int CUSTOMER_IN_PLAN = 0;
	// ghe tham khach hang dau tien ngoai tuyen
	public static final int CUSTOMER_OUT_PLAN = 1;

	// id NVBH
	public String staffId;
	// ma NVBH
	public String staffCode;
	//ma cua hang
	public String shopID;
	// ten NVBH
	public String staffName;
	// ma khach hang ghe tham dau tien trong ngay
	public String customerCodeStartVisit;
	// ten khach hang ghe tham dau tien trong ngay
	public String customerNameStartVisit;
	// thoi gian ghe tham khach hang dau tien trong ngay
	public String timeStartVisit;
	// so khach hang da ghe tham middle
	public int numCustomerVisitedMiddle;
	// so khach hang max can ghe tham middle
	public int maxNumCustomerVisitMiddle;
	// so khach hang da ghe tham end time
	public int numCustomerVisitedEnd;
	// so khach hang max can ghe tham end
	public int maxNumCustomerVisitEnd;
	// so khach hang da ghe tham hien tai
	public int numCustomerCurrentVisited;
	// so khach hang da ghe tham hien tai trong tuyen
	public int numCustomerCurrentVisitedInPlan;
	// tong so khach hang can phai ghe tham trong tuyen - trong ngay
	public int numTotalCustomerVisit;
	// hight light middle infor
	public boolean isHighLightMiddle = false;
	// hight light end info
	public boolean isHighLightEnd = false;
	// high light current info
	public boolean isHighLightCurrent = false;
	// high light NVBH info
	public boolean isHighLightNVBH = false;
	// vi tri hien tai cua NVBH
	public double latPosition;
	public double lngPosition;
	// number customer order start
	public int numCusOrderStart = 0;
	// number customer order middle
	public int numCusOrderMiddle = 0;
	// number customer order end day
	public int numCusOrderEnd = 0;
	// number customer order current
	public int numCusOrderCurrent = 0;
	// ghe tham khach hang dau tien trong ngay (0: trong tuyen / 1: ngoai tuyen)
	public int isOr; 
	// hien thi theo middle
	public static final int FOLLOW_MIDDLE = 3;
	// hien thi theo end
	public static final int FOLLOW_END	   = 4;	
	// Hien thi cot hien tai theo middle hay end
	public int viewCurrenFollow = FOLLOW_MIDDLE;

	public ReportNVBHVisitCustomerDTO() {
		staffId = "0";
		staffCode = Constants.STR_BLANK;
		staffName = Constants.STR_BLANK;
		customerCodeStartVisit = Constants.STR_BLANK;
		customerNameStartVisit = Constants.STR_BLANK;
		timeStartVisit = Constants.STR_BLANK;
		numCustomerVisitedMiddle = 0;
		maxNumCustomerVisitMiddle = 0;
		numCustomerVisitedEnd = 0;
		maxNumCustomerVisitEnd = 0;
		numCustomerCurrentVisited = 0;
		numCustomerCurrentVisitedInPlan = 0;
		numTotalCustomerVisit = 0;
	} 
	
	/**
	 * init object with cursor data
	 * 
	 * @author: QuangVT
	 * @since: 3:25:18 PM Dec 5, 2013
	 * @return: void
	 * @throws:  
	 * @param c
	 */
	public void initObjectWithCursor(Cursor c) {
		if (c.getColumnIndex(STAFF_TABLE.STAFF_ID) >= 0) {
			staffId = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_ID));
		} else {
			staffId = "0";
		}
		if (c.getColumnIndex(STAFF_TABLE.STAFF_CODE) >= 0) {
			staffCode = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_CODE));
		} else {
			staffCode = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(STAFF_TABLE.SHOP_ID) >= 0) {
			shopID = c.getString(c.getColumnIndex(STAFF_TABLE.SHOP_ID));
		} else {
			shopID = Constants.STR_BLANK;
		}
		
		if (c.getColumnIndex(STAFF_TABLE.STAFF_NAME) >= 0) {
			staffName = c.getString(c.getColumnIndex(STAFF_TABLE.STAFF_NAME));
		} else {
			staffName = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_CODE) >= 0) {
			customerCodeStartVisit = c.getString(c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_CODE));
		} else {
			customerCodeStartVisit = Constants.STR_BLANK;
		}
		if (c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_NAME) >= 0) {
			customerNameStartVisit = c.getString(c.getColumnIndex(CUSTOMER_TABLE.CUSTOMER_NAME));
		} else {
			customerNameStartVisit = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("TOTAL_CUSTOMER_VISIT") >= 0) {
			numTotalCustomerVisit = c.getInt(c.getColumnIndex("TOTAL_CUSTOMER_VISIT"));
		} else {
			numTotalCustomerVisit = 0;
		}

		if (c.getColumnIndex("START_TIME") >= 0) {
			timeStartVisit = c.getString(c.getColumnIndex("START_TIME"));
		} else {
			timeStartVisit = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("IS_OR") >= 0) {
			isOr = c.getInt(c.getColumnIndex("IS_OR"));
		} else {
			isOr = 1;
		}
		if (c.getColumnIndex("LESSMIDDLE") >= 0) {
			numCustomerVisitedMiddle = c.getInt(c.getColumnIndex("LESSMIDDLE"));
		} else {
			numCustomerVisitedMiddle = 0;
		}
		if (c.getColumnIndex("LESSEND") >= 0) {
			numCustomerVisitedEnd = c.getInt(c.getColumnIndex("LESSEND"));
		} else {
			numCustomerVisitedEnd = 0;
		}
		if (c.getColumnIndex("LESSNOW_TOTAL_VISITED") >= 0) {
			numCustomerCurrentVisited = c.getInt(c.getColumnIndex("LESSNOW_TOTAL_VISITED"));
		} else {
			numCustomerCurrentVisited = 0;
		}
		if (c.getColumnIndex("LESSNOW_TOTAL_VISITED_IN_PLAN") >= 0) {
			numCustomerCurrentVisitedInPlan = c.getInt(c.getColumnIndex("LESSNOW_TOTAL_VISITED_IN_PLAN"));
		} else {
			numCustomerCurrentVisitedInPlan = 0;
		}

		if (c.getColumnIndex("LAT") >= 0) {
			latPosition = c.getDouble(c.getColumnIndex("LAT"));
		} else {
			latPosition = 0;
		}
		if (c.getColumnIndex("LNG") >= 0) {
			lngPosition = c.getDouble(c.getColumnIndex("LNG"));
		} else {
			lngPosition = 0;
		}

		if (c.getColumnIndex("NUMMIDDLE") >= 0) {
			numCusOrderMiddle = c.getInt(c.getColumnIndex("NUMMIDDLE"));
		} else {
			numCusOrderMiddle = 0;
		}
		if (c.getColumnIndex("NUMEND") >= 0) {
			numCusOrderEnd = c.getInt(c.getColumnIndex("NUMEND"));
		} else {
			numCusOrderEnd = 0;
		}
		if (c.getColumnIndex("NUMCURRENT") >= 0) {
			numCusOrderCurrent = c.getInt(c.getColumnIndex("NUMCURRENT"));
		} else {
			numCusOrderCurrent = 0;
		}
	}
}
