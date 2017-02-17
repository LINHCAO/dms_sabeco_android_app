/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 * Thong tin nhom sp trung bay
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class DisplayCustomerMapDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id bang
	public long displayCustomerMapId;
	// id bang nv phat trien cttb
	public long displayStaffMapId;
	// id muc cua cttb
	public long displayProgramLevelId;
	// id kh tham gia cttb
	public long customerId;
	// tu ngay
	public String fromDate;
	// den ngay
	public String toDate;
	// trang thai
	public int status;
	// ngay tao
	public String createDate;
	// nguoi tao
	public String createUser;
	// ngay cap nhat
	public String updateDate;
	// nguoi cap nhat
	public String updateUser;

	public DisplayCustomerMapDTO() {
		super(TableType.DISPLAY_GROUP_TABLE);
	}
}
