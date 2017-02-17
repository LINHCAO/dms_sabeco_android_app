/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

/**
 *  Thong tin loai khach hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class CustomerTypeDTO extends AbstractTableDTO {
	// id cua bang
	public int customerTypeId ;
	// ma loai khach hang
	public String customerTypeName ; 
	// ten loai khach hang
	public String customerTypeCode ;
	// trang thai
	public int status ; 
	// ghi chu
	public String note ; 
	// nguoi tao
	public String createUser ; 
	// ngay tao
	public String createDate ; 
	
	public CustomerTypeDTO(){
		super(TableType.CUSTOMER_TYPE_TABLE);
	}
	
	
	/**
	 * Khoi tao thong tin sau khi query database 
	 * @author : BangHN
	 * since : 1.0
	 */
	public void initCustomerTypeDTOFromCursor(Cursor c) {
		customerTypeId = (c.getInt(c.getColumnIndex("CUSTOMER_TYPE_ID")));
		customerTypeCode = (c.getString(c.getColumnIndex("CUSTOMER_TYPE_CODE")));
		customerTypeName = (c.getString(c.getColumnIndex("CUSTOMER_TYPE_NAME")));
		//status = (c.getInt(c.getColumnIndex("STATUS")));
		//note = (c.getString(c.getColumnIndex("NOTE")));
		//createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		//createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
	}
}
