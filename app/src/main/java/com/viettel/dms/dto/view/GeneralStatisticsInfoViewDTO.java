/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

/**
 * general statistics info view
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class GeneralStatisticsInfoViewDTO {
	// so ngay ban hang theo ke hoach
	public int numberDayPlan;
	// so ngay ban hang da qua
	public int numberDaySold;
	// tien do chuan
	public float progressSold;
	// list report follow date
	public List<ReportInfoDTO> listReportDate = new ArrayList<ReportInfoDTO>();

	// list report follow month
	public List<ReportInfoDTO> listReportMonth = new ArrayList<ReportInfoDTO>();

	public GeneralStatisticsInfoViewDTO() {
		numberDayPlan = 0;
		numberDaySold = 0;
		progressSold = 0;
		listReportDate = new ArrayList<ReportInfoDTO>();
		listReportMonth = new ArrayList<ReportInfoDTO>();
	}
}
