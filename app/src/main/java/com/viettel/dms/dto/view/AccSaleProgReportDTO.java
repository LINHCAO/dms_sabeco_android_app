/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/**
 * DTO cua man hinh bao cao luy ke den ngay
 * 
 * @author hieunq1
 * 
 */
public class AccSaleProgReportDTO {
	public int totalList;
	// ngay lam viec trong thang
	public int monthSalePlan;
	// ngay da lam viec trong thang
	public int soldSalePlan;
	// percent ngay lam viec trong thang
	public double perSalePlan;
	// ke hoach ban hang trong thang
	public long planTotal;
	// doanh so da ban trong thang
	public long soldTotal;
	// doanh so con lai
	public long remainTotal;
	// percent so tien da ban
	public double perSoldTotal;

	public ArrayList<AccSaleProgReportItem> arrList;

	public AccSaleProgReportDTO() {
		arrList = new ArrayList<AccSaleProgReportItem>();
	}

	/**
	 * add item
	 * 
	 * @param c
	 */
	public void addItem(AccSaleProgReportItem c) {
		arrList.add(c);
		planTotal += c.moneyPlan;
		soldTotal += c.moneySold;
		remainTotal += c.moneyRemain;
	}

	public AccSaleProgReportItem newAccSaleProgReportItem() {
		return new AccSaleProgReportItem();
	}

	/**
	 * DTO row
	 * 
	 * @author hieunq1
	 * 
	 */
	public class AccSaleProgReportItem {
		public String staffCode;
		public String staffName;
		// doanh so ke hoach
		public double moneyPlan;
		// doanh so da ban
		public double moneySold;
		// doanh so con lai
		public double moneyRemain;
		// phan tram da ban / ke hoach thang
		public double percent;
		// sku khach hang
		public double sku_kh;
		// sku chi tieu
		public double sku_target;
		// diem thang
		public double score;
		public String mobile;
		public int staffId;

		public AccSaleProgReportItem() {
			staffCode = "";
			staffName = "";
			moneyPlan = 0;
			moneySold = 0;
			percent = 0;
			moneyRemain = 0;
			sku_kh = 0;
			sku_target = 0;
			score = 0;
		}

		public AccSaleProgReportItem(Cursor c) {
			staffCode = c.getString(c.getColumnIndex("staff_code"));
			staffName = c.getString(c.getColumnIndex("name"));
			moneyPlan = c.getLong(c.getColumnIndex("plan_amount"));
			moneySold = c.getLong(c.getColumnIndex("sold_amount"));
			if (moneyPlan > 0) {
				percent = (double) moneySold / (double) moneyPlan;
				percent *= 100;
			}
			moneyRemain = moneyPlan - moneySold;
			sku_kh = 11;
			sku_target = 14;
			score = 2;
		}

		/**
		 * 
		 * parse data with cursor
		 * 
		 * @author: HaiTC3
		 * @param c
		 * @return: void
		 * @throws:
		 * @since: Jan 29, 2013
		 */
		public void parseDataFromCursor(Cursor c) {
			staffId = c.getInt(c.getColumnIndex("STAFF_ID"));
			staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
			staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
			mobile = c.getString(c.getColumnIndex("MOBILE"));
			moneyPlan = c.getDouble(c.getColumnIndex("MONTH_AMOUNT_PLAN"));
			moneySold = c.getDouble(c.getColumnIndex("MONTH_AMOUNT"));
			percent = (int) (moneyPlan > 0 ? moneySold * 100 / moneyPlan
					: (moneySold > 0 ? 100 : 0));
			moneyRemain = moneyPlan >= moneySold ? moneyPlan - moneySold : 0;
			sku_target = c.getDouble(c.getColumnIndex("MONTH_SKU_PLAN"));
			sku_kh = c.getDouble(c.getColumnIndex("MONTH_SKU"));
			score = c.getDouble(c.getColumnIndex("MONTH_SCORE"));
		}
	}

}
