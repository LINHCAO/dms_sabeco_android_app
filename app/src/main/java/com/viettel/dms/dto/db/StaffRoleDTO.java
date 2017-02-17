/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Luu thong tin role cua nhan vien
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class StaffRoleDTO extends AbstractTableDTO {
	// id quyen
	public int staffId ;
	// ma quyen
	public int roleId ; 
	// tu ngay
	public String fromDate;
	// den ngay
	public String toDate;
	// trang thai 1: hieu luc 0: khong hieu luc
	public int status ;
	
	public StaffRoleDTO(){
		super(TableType.STAFF_ROLE_TABLE);
	}
}
