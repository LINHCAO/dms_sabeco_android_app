/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

/** 
 * Prog Report Pro Disp Detail Sale Row DTO class
 * ProgReportProDispDetailSaleRowDTO.java
 * @version: 1.0 
 * @since:  10:37:16 8 Jan 2014
 */
public class ProgReportProDispDetailSaleRowDTO {
		public String customerCode;
		public String customerName;
		public String CustomerAddress;
		public long amountPlan;
		public String level;
		public long RemainSale;
		public int result;
		
		public ProgReportProDispDetailSaleRowDTO(){
			customerCode= "";
			customerName= "";
			CustomerAddress= "";
			RemainSale= 0;
			result = 0;
		}
		public ProgReportProDispDetailSaleRowDTO(Cursor c){
			customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			CustomerAddress = c.getString(c.getColumnIndex("CUSTOMER_ADD"));
			RemainSale = c.getLong(c.getColumnIndex("REMAIN_SALE"));
			amountPlan = c.getLong(c.getColumnIndex("AMOUNT_PLAN"));
			level = c.getString(c.getColumnIndex("DISPLAY_PROGRAME_LEVEL"));
			result = c.getInt(c.getColumnIndex("RESULT"));
		}
	
}
