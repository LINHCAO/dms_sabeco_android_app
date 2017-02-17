/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

/**
 * History Item DTO class
 * HistoryItemDTO.java
 * @version: 1.0 
 * @since:  10:29:25 8 Jan 2014
 */
public class HistoryItemDTO {
	public String STT;
	public String ngayBan;
	public String doanhSo;
	public String sku;
	public void initDateWithCursor(Cursor c) {
		// TODO Auto-generated method stub
		if (c.getColumnIndex("SKU") >= 0) {
			sku = c.getString(c.getColumnIndex("SKU"));
		} else {
			sku = "";
		}
//		if (c.getColumnIndex("NGAY_BAN") >= 0) {
//			ngayBan = c.getString(c.getColumnIndex("NGAY_BAN"));
//		} else {
//			ngayBan = "";
//		}
		if (c.getColumnIndex("DOANH_SO") >= 0) {
			doanhSo = c.getString(c.getColumnIndex("DOANH_SO"));
		} else {
			doanhSo = "";
		}		
	}
}
