/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

/**
 * Doanh so cua khach hang trong 1 thang
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 */
@SuppressWarnings("serial")
public class SaleInMonthDTO implements Serializable{
	public int month;//thang
	public long amount;//doanh so cua thang: month
	public long quantity;// san luong
	
	public SaleInMonthDTO(){
	}
	
	/**
	 * init thong tin doanh so trong mot thang sau khi query
	 * @author : BangHN
	 * since : 1.0
	 */
	public void parseSaleInMonth(Cursor c){
		month = Integer.parseInt(c.getString(c.getColumnIndex("month")));
		quantity = c.getInt(c.getColumnIndex("quantity"));
	}
}
