/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Luu ke hoach ban hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class SalesPlanDTO extends AbstractTableDTO {
	// id bang
	public int salePlanId ;
	// id NPP
	public int shopId ; 
	// id nhan vien
	public int staffId ;
	// id san pham
	public int productId ;
	// mo ta
	public String description;
	// 1: trong tam, 0: khong phai trong tam
	public int isfocusItem;
	// ma nganh hang
	public String categoryCode;
	// so luong
	public String quantity;
	// so tien
	public String amount;
	// tu ngay
	public String fromDate;
	// den ngay
	public String toDate;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// thoi gian tao
	public String createTime;
	// thoi gian cap nhat
	public String updateTime;
	
	public SalesPlanDTO(){
		super(TableType.SALES_PLAN_TABLE);
	}
}
