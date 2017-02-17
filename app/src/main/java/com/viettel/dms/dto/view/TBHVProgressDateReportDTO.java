/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/**
 * 
 *  DTO cua man hinh bao cao ngay cua TBHV
 *  @author: Nguyen Huu Hieu
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVProgressDateReportDTO {
	public int totalList;
	public String totalofTotal;

	public ArrayList<TBHVProgressDateReportItem> arrList;

	public TBHVProgressDateReportDTO() {
		arrList = new ArrayList<TBHVProgressDateReportDTO.TBHVProgressDateReportItem>();
	}

	public void addItem(Cursor c) {
		TBHVProgressDateReportItem item = new TBHVProgressDateReportItem();
		if (c.getColumnIndex("TEN_NPP") > -1) {
			item.tenNPP = c.getString(c.getColumnIndex("TEN_NPP"));
		}
		if (c.getColumnIndex("TEN_GSNPP") > -1) {
			item.tenGSNPP = c.getString(c.getColumnIndex("TEN_GSNPP"));
		}
		if (c.getColumnIndex("MA_NPP") > -1) {
			item.maNPP = c.getString(c.getColumnIndex("MA_NPP"));
		}
		if (c.getColumnIndex("MA_GSNPP") > -1) {
			item.maGSNPP = c.getString(c.getColumnIndex("MA_GSNPP"));
		}
		item.keHoach = (double) Math.round(c.getDouble(c.getColumnIndex("DOANH_SO_KH")) / 1000);
		item.thucHien = (double) Math.round(c.getDouble(c.getColumnIndex("DOANH_SO_TH")) / 1000);
		item.conLai = item.keHoach - item.thucHien;
		if (item.conLai < 0) {
			item.conLai = 0.0;
		}
		if (c.getColumnIndex("SKU_THUC_HIEN_AMOUNT") > -1) {
			float skuSum = c.getFloat(c.getColumnIndex("SKU_THUC_HIEN_AMOUNT"));
			float skuCount = c
					.getFloat(c.getColumnIndex("SKU_THUC_HIEN_COUNT"));
			item.skuThucHien = skuSum / skuCount;
			skuSum = c.getFloat(c.getColumnIndex("SKU_KE_HOACH_SUM"));
			skuCount = c.getFloat(c.getColumnIndex("SKU_KE_HOACH_COUNT"));
			item.skuKeHoach = skuSum / skuCount;
		}else{
			item.skuKeHoach = c.getFloat(c.getColumnIndex("SKU_KE_HOACH"));
			item.skuThucHien = c.getFloat(c.getColumnIndex("SKU_THUC_HIEN"));
		}		
		arrList.add(item);
	}

	public class TBHVProgressDateReportItem {
		public String tenNPP;
		public String maNPP;
		public String tenGSNPP;
		public String maGSNPP;
		public float skuKeHoach;
		public float skuThucHien;
		public Double keHoach;
		public Double thucHien;
		public Double conLai;
		public String id;

		public TBHVProgressDateReportItem() {
			tenNPP = "";
			tenGSNPP = "";
			skuKeHoach = 0;
			skuThucHien = 0;
			keHoach = 0.0;
			thucHien = 0.0;
			conLai = 0.0;
		}
	}
}
