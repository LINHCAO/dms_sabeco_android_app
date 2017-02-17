/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/**
 * DTO cua man hinh bao cao chuong trinh trung bay TBHV
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVDisProComProgReportDTO {
	public int totalCustomer;
	// danh sach item
	public ArrayList<TBHVDisProComProgReportItem> arrList;
	// danh sach level code
	public ArrayList<String> arrLevelCode;
	// tong ket qua
	public ArrayList<TBHVDisProComProgReportItemResult> arrResultTotal;
	// tong cua tung level
	public TBHVDisProComProgReportItemResult dtoResultTotal;
	// list display programe for use
	public ArrayList<DisplayPresentProductInfo> listDisplayProgrameInfo = new ArrayList<DisplayPresentProductInfo>();
	// total item need get
	public int totalItem = 0;
	// gioi han tien do chuan cho phep kh chua psds hop le
	public int progressStandarPercent = 0;

	public TBHVDisProComProgReportDTO() {
		arrList = new ArrayList<TBHVDisProComProgReportItem>();
		arrLevelCode = new ArrayList<String>();
		arrResultTotal = new ArrayList<TBHVDisProComProgReportItemResult>();
		dtoResultTotal = new TBHVDisProComProgReportItemResult();
		listDisplayProgrameInfo = new ArrayList<DisplayPresentProductInfo>();
		totalCustomer = 0;
		totalItem = 0;
		progressStandarPercent = 0;
	}

	/**
	 * new DisProComProgReportItemResult
	 * 
	 * @return
	 */
	public TBHVDisProComProgReportItemResult newDisProComProgReportItemResult() {
		return new TBHVDisProComProgReportItemResult();
	}

	/**
	 * add item
	 * 
	 * @param c
	 */
	public void addItem(Cursor c) {
		arrList.add(new TBHVDisProComProgReportItem(c));
	}

	/**
	 * new DisProComProgReportItem
	 * 
	 * @return
	 */
	public TBHVDisProComProgReportItem newDisProComProgReportItem() {
		return new TBHVDisProComProgReportItem();
	}
}
