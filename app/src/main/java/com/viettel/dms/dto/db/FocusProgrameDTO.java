/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Luu chuong trinh trong tam
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class FocusProgrameDTO extends AbstractTableDTO {
	// id bang
	public int focusProgrameId ; 
	// ma CTTT
	public String focusProgameCode ;
	// ten CTTT
	public String focusProgrameName ; 
	// trang thai
	public int status ; 
	// tu ngay
	public String fromDate ;
	// den ngay
	public String toDate ;
	// nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;
	
	public FocusProgrameDTO(){
		super(TableType.FOCUS_PROGRAME_TABLE);
	}
}
