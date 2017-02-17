/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/**
 *  Thong tin dia ban
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class CustomerDisplayProgrameDTO extends AbstractTableDTO {
	// id bang
	public int customerDisplayProgrameId ;
	// ma khach hang
	public String customerId ; 
	// ma CTTB
	public String displayProgrameCode ;
	// muc CTTB
	public String levelCode ; 
	// tu ngay
	public String fromDate ; 
	// den ngay
	public String toDate ; 
	// trang thai
	public int status;
	// nguoi tao
	public String createUser ; 
	// nguoi cap nhat
	public String updateUser ; 
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;

	
	public CustomerDisplayProgrameDTO(){
		super(TableType.CUSTOMER_DISPLAY_PROGRAME);
	}
	
	public void initDataFromCursor(Cursor c) throws Exception {
		try {
			if (c == null) {
				throw new Exception("Cursor is empty");
			}

			customerId = StringUtil.isNullOrEmpty(c
					.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"))) ? ""
					: c.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"));
		} catch (Exception ex) {
			throw ex;
		}
	}
}
