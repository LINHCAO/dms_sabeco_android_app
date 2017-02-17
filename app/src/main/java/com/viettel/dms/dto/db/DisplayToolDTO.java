/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Ds cong cu trung bay
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class DisplayToolDTO extends AbstractTableDTO{
	// id cua bang
	public int displayToolsId ;
	// id NPP
	public int shopId ; 
	// id khach hang
	public long customerId ;
	// id san pham
	public String productId ;
	// so luong
	public int quantity ;
	// truong nay chua dung
	public int result ; 
	// ngay chuyen
	public String transferDate ;
	// nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;
	// id NVBH
	public int staffId ;
	
	public DisplayToolDTO(){
		super(TableType.DISPLAY_TOOLS_TABLE);
	}
}
