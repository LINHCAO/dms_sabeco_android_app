/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * 
 * dto danh sach nhan vien
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class ListStaffDTO implements Serializable {
	public int totalList;
	public String totalofTotal;

	public ArrayList<StaffItem> arrList;

	public ListStaffDTO() {
		arrList = new ArrayList<ListStaffDTO.StaffItem>();
	}

	public void addItemAll() {
		arrList.add(0, new StaffItem("", StringUtil.getString(R.string.ALL), ""));
	}
		
	public void addItem(Cursor c) {
		StaffItem item = new StaffItem();
		if (c.getColumnIndex("STAFF_ID") > -1) {
			item.id = c.getString(c.getColumnIndex("STAFF_ID"));
		}
		if (c.getColumnIndex("STAFF_NAME") > -1) {
			item.name = c.getString(c.getColumnIndex("STAFF_NAME"));
		}
		if (c.getColumnIndex("STAFF_CODE") > -1) {
			item.code = c.getString(c.getColumnIndex("STAFF_CODE"));
		}
		if (c.getColumnIndex("LAT") > -1) {
			item.lat = c.getDouble(c.getColumnIndex("LAT"));
		}
		if (c.getColumnIndex("LNG") > -1) {
			item.lng = c.getDouble(c.getColumnIndex("LNG"));
		}
		if (c.getColumnIndex("NVBH_STAFF_ID") > -1) {
			item.nvbhId = c.getString(c.getColumnIndex("NVBH_STAFF_ID"));
		}
		if (c.getColumnIndex("NVBH_SHOP_CODE") > -1) {
			item.nvbhShopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
		}
		if (c.getColumnIndex("NVBH_SHOP_ID") > -1) {
			item.nvbhShopId = c.getString(c.getColumnIndex("NVBH_SHOP_ID"));
		}
		if (c.getColumnIndex("SHOP_ID") > -1) {
			item.shopId = c.getString(c.getColumnIndex("SHOP_ID"));
		}
		if (c.getColumnIndex("SHOP_ID") > -1) {
			item.shopId = c.getString(c.getColumnIndex("SHOP_ID"));
		}
		if (c.getColumnIndex("STAFF_OWNER_ID") > -1) {
			item.staffOwnerId = c.getString(c.getColumnIndex("STAFF_OWNER_ID"));
		}
		arrList.add(item);
	}
	
	public void parrseStaffFromCursor(Cursor c) {
		StaffItem item = new StaffItem();
		if (c.getColumnIndex("STAFF_ID") > -1) {
			item.id = c.getString(c.getColumnIndex("STAFF_ID"));
		}
		if (c.getColumnIndex("STAFF_NAME") > -1) {
			item.name = c.getString(c.getColumnIndex("STAFF_NAME"));
		}
		if (c.getColumnIndex("STAFF_CODE") > -1) {
			item.code = c.getString(c.getColumnIndex("STAFF_CODE"));
		}
		if (c.getColumnIndex("SHOP_ID") > -1) {
			item.nvbhShopId = c.getString(c.getColumnIndex("SHOP_ID"));
		}
		if (c.getColumnIndex("STAFF_OWNER_ID") > -1) {
			item.staffOwnerId = c.getString(c.getColumnIndex("STAFF_OWNER_ID"));
		}
		if (c.getColumnIndex("NVBH_SHOP_CODE") > -1) {
			item.nvbhShopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
		}
		arrList.add(item);
	}
	
	
	
	
	

	public StaffItem newStaffItem() {
		return new StaffItem();
	}

	public class StaffItem implements Serializable {
		public String id;
		public String name;
		public String code;
		public double lat;
		public double lng;
		public String nvbhId;
		public String nvbhShopCode = "";
		public String nvbhShopId;
		public String staffOwnerId;
		public String shopId;

		public StaffItem() {
			id = "0";
			name = "";
		}

		public StaffItem(String id, String name, String code) {
			this.id = id;
			this.name = name;
			this.code = code;
		}
		
		public String getCodeNameString() {
			//Hien thi [Code - Name]
			String result = !StringUtil.isNullOrEmpty(code) ? (!StringUtil.isNullOrEmpty(name) ? code + " - " + name : code) : name ;
			return result;
		}
	}
}
