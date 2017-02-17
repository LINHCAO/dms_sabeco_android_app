/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/** 
 * Progress Date Detail Report DTO class
 * ProgressDateDetailReportDTO.java
 * @version: 1.0 
 * @since:  10:37:41 8 Jan 2014
 */
public class ProgressDateDetailReportDTO {
	public int totalList;	
	public String totalofTotal;
	
	public ArrayList<ProgressDateDetailReportItem> arrList;
	
	public ProgressDateDetailReportDTO(){
		arrList = new ArrayList<ProgressDateDetailReportDTO.ProgressDateDetailReportItem>();	
	}
	
	public void addItem(Cursor c){
		ProgressDateDetailReportItem item = new ProgressDateDetailReportItem();		
		item.maKh = c.getString(c.getColumnIndex("MA_KH"));
		item.kh = c.getString(c.getColumnIndex("KH"));
		item.diaChi = c.getString(c.getColumnIndex("address"));
		
		if(StringUtil.isNullOrEmpty(item.diaChi)){
			StringBuilder address = new StringBuilder();
			if(!StringUtil.isNullOrEmpty(c.getString(c.getColumnIndex("SoNha")))) {
				address.append(c.getString(c.getColumnIndex("SoNha")));
			}
			if(!StringUtil.isNullOrEmpty(c.getString(c.getColumnIndex("Duong")))) {
				address.append(c.getString(c.getColumnIndex("Duong")));
			}
			item.diaChi = address.toString().trim();
		}
		item.mucTieu = c.getDouble(c.getColumnIndex("MUC_TIEU"));
		item.thucHien = c.getDouble(c.getColumnIndex("THUC_HIEN"));		
		item.diem = c.getDouble(c.getColumnIndex("DIEM"));
		item.moi = c.getInt(c.getColumnIndex("IS_MOI"));
		item.on = c.getInt(c.getColumnIndex("IS_ON"));
		item.nt = c.getInt(c.getColumnIndex("IS_NT"));		
		arrList.add(item);
	}
	
	public class ProgressDateDetailReportItem{		
		public String stt;
		public String maKh;
		public String kh;		
		public String diaChi;	
		public Double mucTieu;
		public Double thucHien;	
		public Double diem;
		public int moi;
		public int on;
		public int nt;	
		
		public ProgressDateDetailReportItem(){		
			stt = "";
			maKh = "";
			kh = "";		
			diaChi = "";	
			mucTieu = 0.0;
			thucHien = 0.0;	
			diem = 0.0;
			moi = 0;
			on = 0;
			nt = 0;	
		}
	}
}
