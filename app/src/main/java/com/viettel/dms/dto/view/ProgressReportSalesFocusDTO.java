/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/** 
 * Progress Report Sales Focus DTO class
 * ProgressReportSalesFocusDTO.java
 * @version: 1.0 
 * @since:  10:38:25 8 Jan 2014
 */
public class ProgressReportSalesFocusDTO {
	public int PlanDay = 0;// so ngay ban hang ke hoach
	public int PastDay = 0;// so ngay ban hang da qua
	public double Progress = 0;// tien do
	public ArrayList<String> arrMMTTText;

	// bang bao cao tien do ban hang trong tam
	public ArrayList<InfoProgressEmployeeDTO> listProgressSalesStaff;
	public ArrayList<ReportProductFocusItem> arrRptFocusItemTotal;

	public ProgressReportSalesFocusDTO() {
		listProgressSalesStaff = new ArrayList<InfoProgressEmployeeDTO>();
		arrMMTTText = new ArrayList<String>();
		arrRptFocusItemTotal = new ArrayList<ReportProductFocusItem>();
	}

	public void addItem(InfoProgressEmployeeDTO c) {
		listProgressSalesStaff.add(c);
	}

	public class InfoProgressEmployeeDTO {
		
		// id nhan vien
		public int staffId ;
		public String staffCode;// ma nhan vien
		public String staffName;// ten nhan vien
		public String staffPhone;// sdt nhan vien
		public String staffMobile;// di dong nhan vien
		// MHTT1
		public double moneyPlan1;// so tien theo ke hoach
		public double moneySold1;// so tien ban duoc
		public double moneyRemain1;// so tien con lai
		public double progress1;// % tien do
		// MHTT2
		public double moneyPlan2;// so tien theo ke hoach
		public double moneySold2;// so tien ban duoc
		public double moneyRemain2;// so tien con lai
		public double progress2;// % tien do

		public ArrayList<ReportProductFocusItem> arrRptFocusItem;

		public InfoProgressEmployeeDTO() {
			staffCode = "";
			staffName = "";
			moneyPlan1 = 0;
			moneySold1 = 0;
			progress1 = 0;
			moneyRemain1 = 0;

			moneyPlan2 = 0;
			moneySold2 = 0;
			progress2 = 0;
			moneyRemain2 = 0;

			arrRptFocusItem = new ArrayList<ReportProductFocusItem>();

		}

		public InfoProgressEmployeeDTO(Cursor c) {
			staffCode = c.getString(c.getColumnIndex("staff_code"));
			staffName = c.getString(c.getColumnIndex("name"));
			moneyPlan1 = c.getLong(c.getColumnIndex("plan_amount1"));
			moneySold1 = c.getLong(c.getColumnIndex("sold_amount1"));
			if (moneyPlan1 > 0) {
				progress1 = (double) moneySold1 / (double) moneyPlan1;
				progress1 *= 100;
			}
			moneyRemain1 = moneyPlan1 - moneySold1;

			moneyPlan2 = c.getLong(c.getColumnIndex("plan_amount2"));
			moneySold2 = c.getLong(c.getColumnIndex("sold_amount2"));
			if (moneyPlan2 > 0) {
				progress2 = (double) moneySold2 / (double) moneyPlan2;
				progress2 *= 100;
			}
			moneyRemain2 = moneyPlan2 - moneySold2;

		}

	}
	
	public class ReportProductFocusItem {
		public double planMoney;// so tien theo ke hoach
		public double soldMoney;// so tien ban duoc
		public double leftMoney;// so tien con lai
		public double soldPercent;// % tien do
	}

	public InfoProgressEmployeeDTO newInfoProgressEmployeeDTO() {
		return new InfoProgressEmployeeDTO();
	}
	
	public ReportProductFocusItem newReportProductFocusItem() {
		return new ReportProductFocusItem();
	}

}
