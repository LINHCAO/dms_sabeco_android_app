/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/** 
 * Progress Date Report DTO class
 * ProgressDateReportDTO.java
 * @version: 1.0 
 * @since:  10:38:05 8 Jan 2014
 */
public class ProgressDateReportDTO {
	public int totalList;	
	public String totalofTotal;
	public double minScoreNotification;
//	public int remainDayInMonth;
	
	public ArrayList<ProgressDateReportItem> arrList;
	
	public ProgressDateReportDTO(){
		arrList = new ArrayList<ProgressDateReportDTO.ProgressDateReportItem>();		
	}
	
	public void addItem(Cursor c){
		ProgressDateReportItem item = new ProgressDateReportItem();		
		item.maNvbh = c.getString(c.getColumnIndex("MA_NVBH"));
		item.nvbh = c.getString(c.getColumnIndex("NVBH"));
		item.kh = c.getInt(c.getColumnIndex("DON_HANG_KH"));
		item.th = c.getInt(c.getColumnIndex("DON_HANG_TH"));
		item.keHoachTuyen = c.getDouble(c.getColumnIndex("DOANH_SO_KH_TUYEN"));
//		double monthAmountPlan = c.getDouble(c.getColumnIndex("MONTH_AMOUNT_PLAN"));
//		double monthAmount = c.getDouble(c.getColumnIndex("MONTH_AMOUNT"));
		item.keHoach = c.getDouble(c.getColumnIndex("DAY_PLAN"));
//		if (monthAmountPlan > monthAmount) {			
//			if(remainDayInMonth > 0)
//				item.keHoach = (monthAmountPlan - monthAmount) / remainDayInMonth;
//		}
		item.thucHien = c.getDouble(c.getColumnIndex("DOANH_SO_TH"));
		if(item.keHoach > item.thucHien){
			item.conLai = item.keHoach - item.thucHien;
		}		
		item.diem = c.getString(c.getColumnIndex("DIEM"));
		item.mobile = c.getString(c.getColumnIndex("MOBILE"));
		item.id = c.getString(c.getColumnIndex("ID"));
		arrList.add(item);
	}
	
	public class ProgressDateReportItem{		
		public String maNvbh;
		public String nvbh;		
		public int kh;
		public int th;
		public Double keHoach;
		public Double thucHien;
		public Double conLai;
		public String diem;
		public String mobile;
		public String id;
		public Double keHoachTuyen;
		
		public ProgressDateReportItem(){			
			maNvbh = "";
			nvbh = "";
			kh = 0;
			th = 0;
			keHoach = 0.0;
			thucHien = 0.0;
			conLai = 0.0;
			diem = "";
			keHoachTuyen = 0.0;
		}
	}
}
