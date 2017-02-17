/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.math.BigDecimal;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.RPT_SALE_SUMMARY_TABLE;
import com.viettel.dms.util.StringUtil;

/**
 * report infomation
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ReportInfoDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int REPORT_DETAIL = 0;
	public static int REPORT_TOTAL = 1;
	// report content
	public String content;
	// report quota
	public String quota;
	// report attain
	public String attain;
	// report pregress
	public String progress;
	// report remain
	public String remain;
	// number customer do not have amount in month
	public int numCustomerDoNotAmount;
	// number customer visit in month
	public int numCustomerVisitPlanInMonth;
	// type report
	public int reportType;
	// diem ban hang
	public String startSale;

	public ReportInfoDTO() {
		reportType = REPORT_DETAIL;
		content = "";
		quota = "0";
		attain = "0";
		progress = "0";
		remain = "0";
		startSale = "0";
	}

	/**
	 * 
	 * khoi tao doi tuong vơi cursor cho bao cao ngay
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 15, 2013
	 */
	public void initWithCursorReportDate(Cursor c) {
		// title name
		if (c.getString(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.NAME)) != null) {
			this.content = c.getString(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.NAME));
		} else {
			this.content = Constants.STR_BLANK;
		}

		// plan
		if (c.getString(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.PLAN)) != null) {
			this.quota = String.valueOf(c.getDouble(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.PLAN)));
		} else {
			this.quota = "0";
		}

		// attain
		if (c.getString(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.ACTUALLY))!= null) {
			this.attain = String.valueOf(c.getDouble(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.ACTUALLY)));
		} else {
			this.attain = "0";
		}

		// set type object (total or detail)
		String codeDBH = Constants.STR_BLANK;
		if (c.getString(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.CODE)) != null) {
			codeDBH = c
					.getString(c.getColumnIndex(RPT_SALE_SUMMARY_TABLE.CODE));
		} else {
			codeDBH = Constants.STR_BLANK;
		}
		// tinh thong tin diem ban hang
		if ("DBH".contentEquals(codeDBH)) {
			this.startSale = this.attain;
			this.reportType = REPORT_TOTAL;
			// this.quota = this.attain;
			this.attain = "0";
			this.remain = "0";
			this.progress = "0";
		} else {
			// remain
			double quota = Double.parseDouble(this.quota);
			double attain = Double.parseDouble(this.attain);
			BigDecimal quotaBig = BigDecimal.valueOf(quota);
			BigDecimal attainBig = BigDecimal.valueOf(attain);
			this.remain = String
					.valueOf((quotaBig.subtract(attainBig)).doubleValue() >= 0 ? (quotaBig.subtract(attainBig)) : 0);

			// progress
			if (quota == 0 && attain == 0) {
				this.progress = "0";
			} else {
				int pc = (int) (attain * 100 / (quota == 0 ? attain : quota));

				this.progress = String.valueOf(pc);
			}
		}
		//set lai string
		this.quota = StringUtil.formatDoubleString(this.quota);
		this.attain = StringUtil.formatDoubleString(this.attain);
		this.remain = StringUtil.formatDoubleString(this.remain);
	}
}
