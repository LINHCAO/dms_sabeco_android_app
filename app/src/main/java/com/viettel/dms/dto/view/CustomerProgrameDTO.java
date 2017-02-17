/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

/**
 * Thong tin chuong trinh khach hang dang tham gia
 * 
 * @author : BangHN since : 1.0 version : 1.0
 */
@SuppressWarnings("serial")
public class CustomerProgrameDTO implements Serializable {
	public int customerId;// id khach hang
	public int staffId;// id nhan vien
	public int displayProgrameId;// id chuong trinh khuyen mai
	public String displayProgrameName;// name chuong trinh khuyen mai
	public String displayProgrameCode;// ma code chuong trinh
	public String levelCode;// muc tham gia
	public String status;// trang thai chuong tring
	public String fromDate; // ngay bat dau chuong trinh
	public String toDate;// ngay ket thuc chuong trinh
	public String cat;// nganh hang chuong trinh
	public long amount = 0;// doanh so dat duoc
	public long amountPlan = 0;// doanh so chi tieu
	public long result = 0;// con lai
	public long amountRemain = 0; // doanh so con lai

	public CustomerProgrameDTO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Parse mot cursor sau khi query db -> dto
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void initCustomerProgrameDTO(Cursor c) {
		displayProgrameId = (c.getInt(c.getColumnIndex("display_programe_id")));
		displayProgrameCode = (c.getString(c
				.getColumnIndex("display_programe_code")));
		displayProgrameName = (c.getString(c
				.getColumnIndex("display_programe_name")));
		cat = (c.getString(c.getColumnIndex("cat")));
		levelCode = (c.getString(c.getColumnIndex("display_programe_level")));
		amount = (c.getInt(c.getColumnIndex("amount")));
		amountPlan = (c.getInt(c.getColumnIndex("amount_plan")));
		result = (c.getInt(c.getColumnIndex("result")));
		amountRemain = (c.getLong(c.getColumnIndex("AMOUNT_REMAIN")));
		// staffId = (c.getInt(c.getColumnIndex("display_staff_map_id")));
		fromDate = (c.getString(c.getColumnIndex("from_date")));
		toDate = (c.getString(c.getColumnIndex("to_date")));
	}

}
