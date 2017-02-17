/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * progress report sales focus view for tbhv
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVProgressReportSalesFocusViewDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// so ngay ke hoach trong thang
	public int numPlanDate;
	// so ngay da ban trong thang
	public int numPlanDateDone;
	// tien do ban hang
	public int progressSales;
	// list report info
	public List<ReportSalesFocusEmployeeInfo> listFocusInfoRow = new ArrayList<ReportSalesFocusEmployeeInfo>();
	// row info report total
	public ReportSalesFocusEmployeeInfo objectReportTotal = new ReportSalesFocusEmployeeInfo();
	public ArrayList<String> arrMMTTText;

	public TBHVProgressReportSalesFocusViewDTO() {
		numPlanDate = 0;
		numPlanDateDone = 0;
		progressSales = 0;
		listFocusInfoRow = new ArrayList<ReportSalesFocusEmployeeInfo>();
		objectReportTotal = new ReportSalesFocusEmployeeInfo();
		arrMMTTText = new ArrayList<String>();
	}
}
