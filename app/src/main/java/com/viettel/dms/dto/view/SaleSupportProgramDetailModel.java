/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.util.DateUtils;


/**
 * Du lieu MH Chi tiet CT HTBH
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 19:46:30 07-05-2014
 */
public class SaleSupportProgramDetailModel {
	public long programeId;
	public String programeCode;
	public String programeName;
	public String fromDate;
	public String toDate;
	public String customerType;
	public String customerTypeName;
	public String category;
	public String budget;
	public String content;  
	// Co nhap so luong dang ki tham gia khong?
	public boolean isHaveQuantityJoin;
	
	public void initDataFromCursor(Cursor c, String nameCategory) {
		//Loai khach hang: 1: NPP C1, 2: C2, 3: C3, 4: Quan, 5: Tiem tap hoa, 6: Khac
		customerType = c.getString(c.getColumnIndex("CUSTOMER_TYPE"));
		customerTypeName = c.getString(c.getColumnIndex("CUSTOMER_TYPE_NAME")); 
		
		if(c.getColumnIndex("PRO_INFO_ID") > -1){
			this.programeId = Long.parseLong(c.getString(c.getColumnIndex("PRO_INFO_ID")));
		}
		
		if(c.getColumnIndex("PRO_INFO_CODE") > -1){
			this.programeCode = c.getString(c.getColumnIndex("PRO_INFO_CODE"));
		}
		
		if(c.getColumnIndex("PRO_INFO_NAME") > -1){
			this.programeName = c.getString(c.getColumnIndex("PRO_INFO_NAME"));
		}
		
		if(c.getColumnIndex("FROM_DATE") > -1){
			this.fromDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("FROM_DATE")));
		}

		if(c.getColumnIndex("TO_DATE") > -1){
			this.toDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("TO_DATE")));
		}
		
		//Loai nguon ngan sach: 1: Tong cong ty, 2: Thuong mai
		int budget = c.getInt(c.getColumnIndex("BUDGET_TYPE"));
		switch (budget) {
		case 1:
			this.budget = "Tổng công ty";					
			break;
		case 2:
			this.budget = "Thương mại";					
			break;
		default:
			break;
		}
		
		//Noi dung
		content = c.getString(c.getColumnIndex("CONTENT"));
		
		//Phan loai CT
//		String category3 = c.getString(c.getColumnIndex("CATEGORY_NAME_3"));
//		String category2 = c.getString(c.getColumnIndex("CATEGORY_NAME_2"));
//		String category1 = c.getString(c.getColumnIndex("CATEGORY_NAME_1"));
		
//		category = category1 + " - " + category2 + " - " + category3; 
		category =nameCategory ;
	}
	
}
