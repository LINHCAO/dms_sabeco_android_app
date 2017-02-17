/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;

/**
 * DTO cho man hinh bao cao DS khach hang chua PSDS - TBHV
 * 
 * TBHVCustomerNotPSDSReportDTO.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  4:36:22 PM Dec 16, 2013
 */
public class TBHVCustomerNotPSDSReportDTO implements Serializable{ 
	private static final long serialVersionUID = 1L;
	public int totalList;	
	public String totalofTotal;
	
	public ArrayList<TBHVCustomerNotPSDSReportItem> arrList;
	
	public TBHVCustomerNotPSDSReportDTO(){
		arrList = new ArrayList<TBHVCustomerNotPSDSReportDTO.TBHVCustomerNotPSDSReportItem>();		
	}
	
	/**
	 * Parse tu cursor sau do add vao list
	 * 
	 * @author: QuangVT
	 * @since: 4:07:06 PM Dec 19, 2013
	 * @return: void
	 * @throws:  
	 * @param c
	 */
	public void addItem(Cursor c){
		TBHVCustomerNotPSDSReportItem item = new TBHVCustomerNotPSDSReportItem();		
		if(c.getColumnIndex("SHOP_CODE") > -1){
			item.NPPCode = c.getString(c.getColumnIndex("SHOP_CODE"));
		}
		if(c.getColumnIndex("SHOP_ID") > -1){
			item.NPPId = c.getString(c.getColumnIndex("SHOP_ID"));
		}
		if(c.getColumnIndex("STAFF_NAME") > -1){
			item.GSBHName = c.getString(c.getColumnIndex("STAFF_NAME"));
		}
		if(c.getColumnIndex("STAFF_ID") > -1){
			item.GSBHId = c.getString(c.getColumnIndex("STAFF_ID"));
		}
		if(c.getColumnIndex("VISIT_NUMBER_IN_MONTH") > -1){
			item.numCusVisit = c.getInt(c.getColumnIndex("VISIT_NUMBER_IN_MONTH"));
		} 
		if(c.getColumnIndex("NUM_CUS_NOT_PSDS") > -1){
			item.numCusNotPSDS = c.getInt(c.getColumnIndex("NUM_CUS_NOT_PSDS"));
		} 
		
		item.numCusPSDS = item.numCusVisit - item.numCusNotPSDS;
		
		arrList.add(item);
	}
	
	/**
	 * Item 1 dong trong bang cua man hinh bao cao danh sach khách hàng chua PSDS
	 * 
	 * TBHVCustomerNotPSDSReportDTO.java
	 * @author: QuangVT
	 * @version: 1.0 
	 * @since:  4:37:23 PM Dec 16, 2013
	 */
	public class TBHVCustomerNotPSDSReportItem implements Serializable{		
		private static final long serialVersionUID = 1L;
		public String NPPCode;
		public String NPPId;
		public String GSBHName;
		public String GSBHId;
		public int numCusVisit;
		public int numCusPSDS;
		public int numCusNotPSDS; 
		
		/**
		 * Ham khoi tao
		 */
		public TBHVCustomerNotPSDSReportItem(){			
			 NPPCode  = Constants.STR_BLANK;
			 NPPId    = Constants.STR_BLANK;
			 GSBHName = Constants.STR_BLANK;
			 GSBHId   = Constants.STR_BLANK;
			 numCusVisit   = 0;
			 numCusPSDS    = 0;
			 numCusNotPSDS = 0;
		}
	}
}
