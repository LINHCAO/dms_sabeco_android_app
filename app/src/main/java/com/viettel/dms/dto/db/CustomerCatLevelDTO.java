/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Thong tin muc doanh so khach hang
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class CustomerCatLevelDTO extends AbstractTableDTO {
	// id cua bang
	public int customerCatLevelId ;
	// ma khach hang
	public String customerId ; 
	// ma loai muc doanh so
	public String categoryCode ;
	// muc doanh so
	public int levelId ; 
	// nam
	public String year ; 
	// nguoi tao
	public String createUser ; 
	// ngay tao
	public String createDate ; 
	
	public CustomerCatLevelDTO(){
		super(TableType.CUSTOMER_CATEGORY_LEVEL_TABLE);
	}
}
