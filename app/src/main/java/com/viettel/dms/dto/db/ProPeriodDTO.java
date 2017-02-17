/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.util.DateUtils;
 
/**
 * Chu ki tinh thuong cua CT HTBH
 * ProPeriodDTO.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  10:30:03 12-06-2014
 */
@SuppressWarnings("serial")
public class ProPeriodDTO extends AbstractTableDTO{
	// id 
	public long PRO_PERIOD_ID ; 
	// id chuong trinh
	public long PRO_INFO_ID ;  
	// ten chu ki
	public String PRO_PERIOD_NAME ;  
	// tu ngay
	public String FROM_DATE ;
	// den ngay
	public String TO_DATE ;  
	// nguoi tao
	public String CREATE_USER ;
	// nguoi cap nhat
	public String UPDATE_USER ;
	// ngay tao
	public String CREATE_DATE ;
	// ngay cap nhat
	public String UPDATE_DATE ;  
	
	public ProPeriodDTO(){
		super(TableType.PRO_INFO_TABLE);
	}
	
	/**
	 * 
	*  Khoi tao du lieu tu cursor
	*  @author: dungnt19
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initFromCursor(Cursor c){
		if (c.getColumnIndex("PRO_PERIOD_ID") >= 0) {
			PRO_PERIOD_ID = c.getLong(c.getColumnIndex("PRO_PERIOD_ID"));
		} else {
			PRO_PERIOD_ID = 0;
		}
		
		if (c.getColumnIndex("PRO_INFO_ID") >= 0) {
			PRO_INFO_ID = c.getLong(c.getColumnIndex("PRO_INFO_ID"));
		} else {
			PRO_INFO_ID = 0;
		} 
		 
		if (c.getColumnIndex("PRO_PERIOD_NAME") >= 0) {
			PRO_PERIOD_NAME = c.getString(c.getColumnIndex("PRO_PERIOD_NAME"));
		} else {
			PRO_PERIOD_NAME = "";
		} 
		
		if (c.getColumnIndex("FROM_DATE") >= 0) {
			FROM_DATE = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("FROM_DATE")));
		} else {
			FROM_DATE = "";
		}
		if (c.getColumnIndex("TO_DATE") >= 0) {
			TO_DATE = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex("TO_DATE")));
		} else {
			TO_DATE = "";
		} 
	}
	
	@Override
	protected ProPeriodDTO clone() {
		ProPeriodDTO dto = new ProPeriodDTO();
		dto.CREATE_DATE = CREATE_DATE;
		dto.CREATE_USER = CREATE_USER;
		dto.FROM_DATE 	= FROM_DATE;
		dto.PRO_INFO_ID = PRO_INFO_ID;
		dto.TO_DATE 	= TO_DATE;
		dto.PRO_PERIOD_ID 	= PRO_PERIOD_ID;
		dto.PRO_PERIOD_NAME = PRO_PERIOD_NAME;
		dto.UPDATE_DATE = UPDATE_DATE;
		dto.UPDATE_USER = UPDATE_USER;
		 
		 return dto;
	}
}
