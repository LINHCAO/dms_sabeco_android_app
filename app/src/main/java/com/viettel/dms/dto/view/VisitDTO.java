/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/** 
 * Visit DTO class
 * VisitDTO.java
 * @version: 1.0 
 * @since:  10:43:42 8 Jan 2014
 */
public class VisitDTO {
	public int totalList;	
	public String totalofTotal;
	
	public ArrayList<VisitItem> arrList;
	
	public VisitDTO(){
		arrList = new ArrayList<VisitDTO.VisitItem>();		
	}
	
	public void addItem(Cursor c){
		VisitItem item = new VisitItem();		
		try {
//			item.thoiGian = StringUtil.dateToString(StringUtil.stringToDate(c.getString(c.getColumnIndex("THOI_GIAN")), "yyyy-MM-dd HH:mm:ss"), "dd/MM/yyyy HH:mm");
			item.thoiGian = c.getString(c.getColumnIndex("THOI_GIAN"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		item.trangThai = c.getString(c.getColumnIndex("TRANG_THAI"));	
		if("0".equals(item.trangThai)){
			item.trangThai = "Không lấy hàng.";
		}else if("1".equals(item.trangThai)){
			item.trangThai = "Đóng cửa.";
		}else if("2".equals(item.trangThai)){
			//item.trangThai = "Đơn hàng chưa duyệt.";
			//duongdt xem nhu ko lay hang
			item.trangThai = "Không lấy hàng.";
		}
		arrList.add(item);
	}
	
	/**
	 * Thong tin ghe them khach hang cua NV
	 * Danh cho man hinh DS KH khong PSDS
	 * 
	 * @author quangvt1
	 *
	 */
	public class VisitItem{
		public String stt;
		public String thoiGian;	
		public String trangThai;		
		
		public VisitItem(){
			stt = "0/0";
			thoiGian = "3/7";
			trangThai = "0/0";					
		}
	}
}
