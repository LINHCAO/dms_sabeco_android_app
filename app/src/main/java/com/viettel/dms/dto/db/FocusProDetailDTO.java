/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Chi tiet chuong trinh trong tam
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class FocusProDetailDTO  extends AbstractTableDTO{
	// id bang
	public int focusProgrameDetailId ; 
	// id cua CTTT
	public int focusProgrameId;
	// id cua san pham
	public int productId ;
	// nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ; 
	// ngay cap nhat
	public String updateDate ;
	
	public FocusProDetailDTO(){
		super(TableType.FOCUS_PROGRAME_DETAIL_TABLE);
	}
}
