/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Thong tin luu tru cua shop promotion
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 2.1
 */
@SuppressWarnings("serial")
public class PromotionShopMapDTO extends AbstractTableDTO{
	// id shop promotin
	public long promotionShopMapId ; 
	// id CTKM
	public long promotionProgrameId ; 
	// id shop
	public int shopId ;
	// so luong toi da
	public int quantityMax ; 
	// so luong thuc nhan
	public int quantityReceived; 
	// status
	public int status ; 
	// so xuat ap dung cho KH voi TH CTKM chi den NPP
	public int quantityCustomer ; 
	// doi tuong ap dung 1: chi ap dung den NPP, 2: ap dung den NPP, loai KH; 3: ap dung den tan KH
	public String objectApply ; 
	//nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;
	
	public PromotionShopMapDTO(){
		super(TableType.PROMOTION_SHOP_MAP_TABLE);
	}
}
