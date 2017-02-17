/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Luu CTTT ap dung cho NPP nao
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class FocusShopMapDTO extends AbstractTableDTO {
	// id CTTT
	public int focusProgrameId ; 
	// id NPP
	public int shopId ; 
	// trang thai
	public int status ; 
	// id bang
	public int focusShopMapId ; 
	// ngay tao
	public String createDate ; 
	// nguoi tao
	public String createUser ; 
	// ngay cap nhat
	public String updateDate ; 
	// nguoi cap nhat
	public String updateUser ;
	
	public FocusShopMapDTO(){
		super(TableType.FOCUS_SHOP_MAP_TABLE);
	}
}
