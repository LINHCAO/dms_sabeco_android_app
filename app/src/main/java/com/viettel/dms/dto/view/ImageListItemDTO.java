/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.StringUtil;

/** 
 * Image List Item DTO class
 * ImageListItemDTO.java
 * @version: 1.0 
 * @since:  10:30:07 8 Jan 2014
 */
@SuppressWarnings("serial")
public class ImageListItemDTO implements Serializable {
	// id khach hang
	public long customerId;
	// ma khach hang
	public String customerCode;
	// ten khach hang
	public String customerName;
	// dia chi khach hang
	public String street;
	// so luong anh
	public int imageNumber;
	// dia chi khach hang
	public String address;
	// thong tin khach hang
	public CustomerDTO aCustomer = new CustomerDTO();
	// Lat
	public double lat;
	// lng
	public double lng;
	// So nha KH
	public String houseNumber;
	// Tuyen cua KH
	public String visitPlan;
	// StaffId of NVBH
	public int nvbhStaffId;
	//StaffName of VBH
	public String nvbhStaffName;

	/**
	 * Tao du lieu tu cursor
	 * 
	 * @author: QuangVT
	 * @since: 2:44:27 PM Dec 12, 2013
	 * @return: void
	 * @throws:  
	 * @param c
	 * @throws Exception
	 */
	public void imageListItemDTO(Cursor c) throws Exception {

		if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
			customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
		} else {
			customerId = 1;
		}
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
		} else {
			customerCode = "";
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		} else {
			customerName = "";
		}
		if (c.getColumnIndex("STREET") >= 0) {
			street = c.getString(c.getColumnIndex("STREET"));
		} else {
			street = "";
		}
		if (c.getColumnIndex("NUM_ITEM") >= 0) {
			imageNumber = c.getInt(c.getColumnIndex("NUM_ITEM"));
		} else {
			imageNumber = 0;
		}

		if (c.getColumnIndex(CUSTOMER_TABLE.LAT) >= 0) {
			lat = c.getDouble(c.getColumnIndex(CUSTOMER_TABLE.LAT));
		} else {
			lat = 0;
		}

		if (c.getColumnIndex(CUSTOMER_TABLE.LNG) >= 0) {
			lng = c.getDouble(c.getColumnIndex(CUSTOMER_TABLE.LNG));
		} else {
			lng = 0;
		}

		if (c.getColumnIndex(CUSTOMER_TABLE.HOUSENUMBER) >= 0) {
			houseNumber = c.getString(c.getColumnIndex(CUSTOMER_TABLE.HOUSENUMBER));
		} else {
			houseNumber = "";
		}
		
		if (c.getColumnIndex(CUSTOMER_TABLE.ADDRESS) >= 0) {
			address = c.getString(c.getColumnIndex(CUSTOMER_TABLE.ADDRESS));
		} else {
			address = "";
		}

		if (c.getColumnIndex("VISIT_PLAN") >= 0) {
			visitPlan = c.getString(c.getColumnIndex("VISIT_PLAN"));
			if (!StringUtil.isNullOrEmpty(visitPlan) && visitPlan.substring(0, 1).equals(",")) {
				visitPlan = visitPlan.substring(1, visitPlan.length());
			}
		} else {
			visitPlan = "";
		}

		if (c.getColumnIndex("STAFF_ID") >= 0) {
			nvbhStaffId = c.getInt(c.getColumnIndex("STAFF_ID"));
		} else {
			nvbhStaffId = 0;
		}

		if (c.getColumnIndex("NVBH_STAFF_NAME") >= 0) {
			nvbhStaffName = c.getString(c.getColumnIndex("NVBH_STAFF_NAME"));
		} else {
			nvbhStaffName = "";
		}
	}

	/**
	 * Ham khoi tao
	 */
	public ImageListItemDTO() {
		customerCode = "";
		customerName = "";
		street = "";
		imageNumber = 0;
	}
}
