/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * 
 * Du lieu MH Quan ly thiet bi cua tung NVBH
 * 
 * @author: Nguyen Thanh Dung
 * @version: 1.1
 * @since: 1.0
 */
public class CabinetStaffDTO {
	public int totalList;
	public String totalofTotal;

	public ArrayList<CabinetStaffItem> arrList;

	public CabinetStaffDTO() {
		arrList = new ArrayList<CabinetStaffDTO.CabinetStaffItem>();
	}

	public void addItem(Cursor c) {
		CabinetStaffItem item = new CabinetStaffItem();
		item.maKh = c.getString(c.getColumnIndex("MA_KH")).substring(0, 3);
		item.tenKh = c.getString(c.getColumnIndex("TEN_KH"));
		item.diaChi = c.getString(c.getColumnIndex("SO_NHA")) + " " + c.getString(c.getColumnIndex("DUONG"));
		item.keHoach = c.getInt(c.getColumnIndex("KE_HOACH"));// (double)
																// Math.round(c.getDouble(c.getColumnIndex("KE_HOACH"))
																// / 1000);
		item.thucHien = c.getInt(c.getColumnIndex("THUC_HIEN"));// (double)Math.round(c.getDouble(c.getColumnIndex("THUC_HIEN"))
																// / 1000);
		item.ketqua = c.getInt(c.getColumnIndex("DAT"));// (double)Math.round(c.getDouble(c.getColumnIndex("THUC_HIEN"))
														// / 1000);
		item.conLai = item.keHoach - item.thucHien;
		if (item.conLai < 0)
			item.conLai = 0;
		if (item.ketqua == 1) {
			item.danhGia = StringUtil.getString(R.string.ATTAIN);
		} else {
			item.danhGia = StringUtil.getString(R.string.UN_ATTAIN);
		}
		item.tenThietbi = c.getString(c.getColumnIndex("EQUIP_NAME"));
		arrList.add(item);
	}

	public class CabinetStaffItem {
		public String stt;// So thu tu
		public String maKh;// Ma khach hang
		public String tenKh;// Ten khach hang
		public String diaChi;// Dia chi
		public int keHoach;// Doanh so ke hoach
		public int thucHien;// Doanh so thuc hien
		public int conLai;// Doanh so con lai
		public int ketqua;// truong result
		public String danhGia;// Chuoi danh gia
		public String tenThietbi;

		public CabinetStaffItem() {
			stt = "0";
			maKh = "maKh";
			tenKh = "tenKh";
			diaChi = "diaChi";
			keHoach = 0;
			thucHien = 0;
			conLai = 0;
			danhGia = "dat";
		}
	}
}
