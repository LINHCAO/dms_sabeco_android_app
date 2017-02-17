/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

/**
 *  thong tin mot mat hang trong tam
 *  @author: HaiTC3
 *  @version: 1.1
 *  @since: 1.0
 */
public class ReportFocusProductItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// d/s ke hoach
	public long amountPlan;
	// d/s da thuc hien
	public long amount;
	// tien do
	public int progress;
	// con lai
	public long remain;
	// forcus item name
	public String focusItemName;

	public ReportFocusProductItem() {
		amountPlan = 0;
		amount = 0;
		progress = 0;
		remain = 0;
		focusItemName = "";
	}
	
	/**
	 * 
	*  parse data from cursor with number display program forcus
	*  @author: HaiTC3
	*  @param c
	*  @param numForcus
	*  @return: void
	*  @throws:
	 */
	public void parseDataFromCursor(Cursor c, String numForcus){
		amountPlan = Math.round(c.getLong(c
				.getColumnIndex("FOCUS" + numForcus
						+ "_AMOUNT_PLAN")) / 1000.0);
		amount = Math
				.round(c.getLong(c.getColumnIndex("FOCUS"
						+ numForcus + "_AMOUNT")) / 1000.0);
	}
}
