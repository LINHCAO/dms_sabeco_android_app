/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

/**
 *  thong tin mot dong bao cao mat hang trong tam cua 1 NPP va 1 GSNPP
 *  @author: HaiTC3
 *  @version: 1.1
 *  @since: 1.0
 */
public class ReportSalesFocusEmployeeInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// children object id
	public long staffId;
	// staff code
	public String staffCode;
	// children object name
	public String staffName;
	// parent object id
	public long staffOwnerId;
	// staff owner code
	public String staffOwnerCode;
	// parent object name
	public String staffOwnerName;
	// list report focus item
	public List<ReportFocusProductItem> listFocusProductItem = new ArrayList<ReportFocusProductItem>();

	public ReportSalesFocusEmployeeInfo() {
		staffId = 0;
		staffName = "";
		staffOwnerId = 0;
		staffOwnerName = "";
		staffCode = "";
		staffOwnerCode = "";
		listFocusProductItem = new ArrayList<ReportFocusProductItem>();
	}
	
	/**
	 * parse data from cursor after query sql success
	*  Mo ta chuc nang cua ham
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void parseDataFromCursor(Cursor c){
		if (c.getColumnIndex("STAFF_ID") >= 0) {
			staffId = c.getLong(c.getColumnIndex("STAFF_ID"));
		} else {
			staffId = 0;
		}

		if (c.getColumnIndex("STAFF_CODE") >= 0) {
			staffCode = c.getString(c
					.getColumnIndex("STAFF_CODE"));
		} else {
			staffCode = "";
		}

		if (c.getColumnIndex("STAFF_NAME") >= 0) {
			staffName = c.getString(c
					.getColumnIndex("STAFF_NAME"));
		} else {
			staffName = "";
		}

		if (c.getColumnIndex("STAFF_OWNER_ID") >= 0) {
			staffOwnerId = c.getLong(c
					.getColumnIndex("STAFF_OWNER_ID"));
		} else {
			staffOwnerId = 0;
		}

		if (c.getColumnIndex("STAFF_OWNER_CODE") >= 0) {
			staffOwnerCode = c.getString(c
					.getColumnIndex("STAFF_OWNER_CODE"));
		} else {
			staffOwnerCode = "";
		}

		if (c.getColumnIndex("STAFF_OWNER_NAME") >= 0) {
			staffOwnerName = c.getString(c
					.getColumnIndex("STAFF_OWNER_NAME"));
		} else {
			staffOwnerName = "";
		}
	}
}
