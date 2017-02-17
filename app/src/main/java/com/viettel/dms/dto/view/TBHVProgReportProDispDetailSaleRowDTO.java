/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVProgReportProDispDetailSaleRowDTO {
	// ma khach hang
	public String customerCode;
	// teen khach hang
	public String customerName;
	// dia chi
	public String customerAddress;
	// doanh so con lai
	public long remainSale;
	// dinh muc
	public long amountPlan;
	//doanh so thuc hien
	public long amount;
	// muc
	public String displayProgLevel;
	// dat, ko dat
	public int result;

	public TBHVProgReportProDispDetailSaleRowDTO() {
		customerCode = "";
		customerName = "";
		customerAddress = "";
		remainSale = 0;
	}

	public TBHVProgReportProDispDetailSaleRowDTO(Cursor c) {
		customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
		customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		customerAddress = c.getString(c.getColumnIndex("CUSTOMER_ADD"));
		remainSale = c.getLong(c.getColumnIndex("REMAIN_SALE"));
	}
}
