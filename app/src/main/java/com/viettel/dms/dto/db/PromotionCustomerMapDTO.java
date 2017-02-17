/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Thong tin luu tru cua customer promotion map
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 2.1
 */
@SuppressWarnings("serial")
public class PromotionCustomerMapDTO extends AbstractTableDTO{
	// id shop promotin
	public long promotionCustomerMapId ; 
	// id CTKM
	public long promotionShopMapd ; 
	// id shop
	public int shopId ;
	//customer id
	public int customerId;
	//customer type id
	public int customerTypeId;
	// so luong toi da
	public int quantityMax ; 
	// so luong thuc nhan
	public int quantityReceived; 
	// status
	public int status ; 
	//type
	public int type;
	//nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;
	
	public PromotionCustomerMapDTO(){
		super(TableType.PROMOTION_CUSTOMER_MAP_TABLE);
	}
}
