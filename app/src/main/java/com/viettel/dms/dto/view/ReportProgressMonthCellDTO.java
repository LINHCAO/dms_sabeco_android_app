/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.RPT_STAFF_SALE_TABLE;

/**
 * report month info cell
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class ReportProgressMonthCellDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// NPP id
	public long staffID; // idGSNPP , idNVBH
	// staff code
	public String staffCode; // code gsnpp, nvbh
	// name NPP
	public String staffName; // NAME_GSNPP, NAME_NVBH
	// GSNPP id
	public long staffOwnerID; // idNPP, idGSNPP
	// staff owner code
	public String staffOwnerCode; // code npp, gsnpp
	// name GSNPP
	public String staffOwnerName; // NAME_NPP, NAME_GSNPP
	// amount plan
	public long amountPlan;
	// amount sale
	public long amountDone;
	// progress done
	public int progressAmountDone;
	// amount remain
	public long amountRemain;
	// sku plan
	public float numSKUPlan;
	// sku done
	public float numSKUDone;

	public ReportProgressMonthCellDTO() {
		staffID = -1;
		staffName = "";
		staffOwnerName = "";
		amountDone = 0;
		amountPlan = 0;
		amountRemain = 0;
		progressAmountDone = 0;
		numSKUDone = 0;
		numSKUPlan = 0;
		staffCode = "";
		staffOwnerCode = "";
	}

	/**
	 * 
	 * init object report progress month
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initReportProgressMonthObject(Cursor c) {

		if (c.getColumnIndex("STAFF_ID") >= 0) {
			staffID = c.getLong(c.getColumnIndex("STAFF_ID"));
		} else {
			staffID = 0;
		}

		if (c.getColumnIndex("STAFF_CODE") >= 0) {
			staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
		} else {
			staffCode = "";
		}

		if (c.getColumnIndex("STAFF_NAME") >= 0) {
			staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
		} else {
			staffName = "";
		}

		if (c.getColumnIndex("STAFF_OWNER_ID") >= 0) {
			staffOwnerID = c.getLong(c.getColumnIndex("STAFF_OWNER_ID"));
		} else {
			staffOwnerID = 0;
		}

		if (c.getColumnIndex("STAFF_OWNER_CODE") >= 0) {
			staffOwnerCode = c.getString(c.getColumnIndex("STAFF_OWNER_CODE"));
		} else {
			staffOwnerCode = "0";
		}

		if (c.getColumnIndex("STAFF_OWNER_NAME") >= 0) {
			staffOwnerName = c.getString(c.getColumnIndex("STAFF_OWNER_NAME"));
		} else {
			staffOwnerName = "";
		}

		if (c.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_AMOUNT_PLAN) >= 0) {
			amountPlan = Math
					.round(c.getLong(c
							.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_AMOUNT_PLAN)) / 1000.0);
		} else {
			amountPlan = 0;
		}

		if (c.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_AMOUNT) >= 0) {
			amountDone = Math
					.round(c.getLong(c
							.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_AMOUNT)) / 1000.0);
		} else {
			amountDone = 0;
		}
		// remain (may be < 0)
		amountRemain = (amountPlan - amountDone) >= 0 ? (amountPlan - amountDone)
				: 0;
		// progress
		if (amountDone <= 0) {
			progressAmountDone = 0;
		} else {
			progressAmountDone = (int) (amountDone * 100 / (amountPlan <= 0 ? amountDone
					: amountPlan));
		}

		if (c.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_SKU) >= 0) {
			numSKUDone = c.getFloat(c
					.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_SKU));
		} else {
			numSKUDone = 0;
		}
		if (c.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_SKU_PLAN) >= 0) {
			numSKUPlan = c.getFloat(c
					.getColumnIndex(RPT_STAFF_SALE_TABLE.MONTH_SKU_PLAN));
		} else {
			numSKUPlan = 0;
		}

	}

}
