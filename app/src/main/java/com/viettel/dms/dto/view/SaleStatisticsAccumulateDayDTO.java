/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;
/**
 * 
 *  dto dung cho man hinh don tong luy ke ngay cua NVBH
 *  @author: Nguyen Huu Hieu
 *  @version: 1.1
 *  @since: 1.0
 */
public class SaleStatisticsAccumulateDayDTO {
	public int totalList;	
	public String totalofTotal;
	
	public ArrayList<SaleStatisticsAccumulateDayItem> arrList;
	public ArrayList<String> listIndustry = new ArrayList<String>();
	
	public SaleStatisticsAccumulateDayDTO(){
		arrList = new ArrayList<SaleStatisticsAccumulateDayDTO.SaleStatisticsAccumulateDayItem>();		
	}
	
	public void addItem(Cursor c){
		SaleStatisticsAccumulateDayItem item = new SaleStatisticsAccumulateDayItem();	
		item.maHang = c.getString(c.getColumnIndex("maHang"));
		item.tenMatHang = c.getString(c.getColumnIndex("tenMatHang"));
		item.nh = c.getString(c.getColumnIndex("nh"));		
		item.keHoach = (int) Math.round(c.getDouble(c.getColumnIndex("keHoach")) / 1000);
		item.thucHien = (int) Math.round(c.getDouble(c.getColumnIndex("thucHien")) / 1000);		
		if((item.keHoach - item.thucHien) > 0){
			item.conLai = item.keHoach - item.thucHien;
		}
		if(item.keHoach == 0){
			item.tienDo = "100%";
		}else{
			item.tienDo = Math.round((item.thucHien * 100 / item.keHoach)) + "%";
		}
		arrList.add(item);
	}
	
	public class SaleStatisticsAccumulateDayItem{
		public String stt;
		public String maHang;
		public String tenMatHang;		
		public String nh;
		public int keHoach;
		public int thucHien;
		public int conLai;
		public String tienDo;
		
		public SaleStatisticsAccumulateDayItem(){
			stt = "";
			maHang = "";
			tenMatHang = "";
			nh = "";
			keHoach = 0;
			thucHien = 0;
			conLai = 0;
			tienDo = "0";				
		}
	}
}
