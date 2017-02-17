/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.StringUtil;

/**
 * Them Khach Hang item
 * NewCustomerItem.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:14:05 3 Jan 2014
 */
public class NewCustomerItem implements Serializable{
	private static final long serialVersionUID = 3510370484856641238L;
	public long customerId;
	public int tvSTT;
	public String tvTKH;
	public String tvDiaChi;
	public String tvDienThoai;
	public String tvTrangThai;
	public boolean isEdit;
	public int trangThai;
	public static final String[] ARRAY_CUSTOMER_STATE = new String [] {"Tất cả","Chưa gửi", "Chưa duyệt", "Từ chối", "Lỗi"};
	public NewCustomerItem() {
		tvSTT = 0;
		tvTKH = "";
		tvDiaChi = "";
		tvDienThoai = "";
		tvTrangThai = "";
		isEdit = false;
	}
	
	public void initFromCursor(Cursor c){
		customerId = StringUtil.getLongFromSQliteCursor(c, "CUSTOMER_ID");
		tvTKH = StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_NAME");
		tvDiaChi = StringUtil.getStringFromSQliteCursor(c, "ADDRESS");
		tvDienThoai = StringUtil.getStringFromSQliteCursor(c, "PHONE");
		trangThai = (int)StringUtil.getLongFromSQliteCursor(c, "STATE");
		
		//0 -> tat ca, lay nhung trang thai > 1
		if (trangThai  > 0 && trangThai < NewCustomerItem.ARRAY_CUSTOMER_STATE.length) {
			tvTrangThai = NewCustomerItem.ARRAY_CUSTOMER_STATE[trangThai];
		}
		
		//neu nam trong 3 trang thai duoc phep edit
		if (trangThai == CUSTOMER_TABLE.STATE_CUSTOMER_NOT_SEND || trangThai == CUSTOMER_TABLE.STATE_CUSTOMER_NOT_APPROVED || trangThai == CUSTOMER_TABLE.STATE_CUSTOMER_ERROR) {
			isEdit = true;
		}else{
			isEdit = false;
		}
	}
}
