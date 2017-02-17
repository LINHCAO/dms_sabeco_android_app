/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

/**
 * Thong tin nhan vien
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class StaffDTO extends AbstractTableDTO {
	// id nhan vien
	public long staffId;
	// ma nhan vien
	public String staffCode;
	// ten
	public String name;
	// dia chi
	public String address;
	// duong
	public String street;
	// nuoc
	public String country;
	// sdt ban
	public String phone;
	// so di dong
	public String mobile;
	// email
	public String email;
	// gioi tinh: 1 nam, 0 nu
	public int sex;
	// ngay vao lam
	public String startDate;
	// trinh do
	public String educationId;
	// vi tri
	public String positionId;
	// truong nay chua dung
	public String catId;
	// id nhan vien quan ly
	public String staffOwnerId;
	// ngay sinh
	public String birthday;
	// id NPP
	public long shopId;
	// ma vung
	public String areaCode;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// ngay tao
	public String createTime;
	// ngay cap nhat
	public String updateTime;
	// loai kenh
	public String channelTypeId;
	// 1: hoat dong, 0: ngung hoat dong
	public int status;
	// mat khau
	public String password;
	// so lan dang nhap sai
	public int numLoginFail;
	// trang thai khoa: 0: binh thuong, 1: khoa
	public int lockStatus;
	// so tien ke hoach ngay
	public String plan;
	// ngay cap nhat ke hoach
	public String updatePlan;
	// do hang cuoi cung duoc duyet
	public String lastApproveOrder;
	// don hang cuoi cung da dat
	public String lastOrder;

	public StaffDTO() {
		super(TableType.STAFF_TABLE);
	} 
	
	/**
	 * Parse du lieu staff 
	 * 
	 * @author: QuangVT
	 * @since: 6:00:02 PM Dec 17, 2013
	 * @return: void
	 * @throws:  
	 * @param c
	 */
	public void parseDataFromCusor(Cursor c){
		 
	}
}
