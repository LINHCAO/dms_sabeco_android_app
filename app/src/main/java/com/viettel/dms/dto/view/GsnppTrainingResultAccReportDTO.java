/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.DateUtils;

/** 
 * Gsnpp Training Result Acc Report DTO class
 * GsnppTrainingResultAccReportDTO.java
 * @version: 1.0 
 * @since:  10:28:11 8 Jan 2014
 */
public class GsnppTrainingResultAccReportDTO {

	public ArrayList<GsnppTrainingResultAccReportItem> listResult = new ArrayList<GsnppTrainingResultAccReportItem>();
	public double amountMonth;
	public double amount;
	public int numCustomerPlan;
	public int numCustomerOrder;
	public int numCustomerNew;
	public int numCustomerOn;
	public int numCustomerOr;
	public double score;
	public int shopId;

	public GsnppTrainingResultAccReportItem newAccTrainResultReportItem() {
		return new GsnppTrainingResultAccReportItem();
	}

	public class GsnppTrainingResultAccReportItem {
		public long trainDetailId;
		public long staffId;
		public String staffCode;
		public String staffName;
		public String date;
		public double amountMonth;
		public double amount;
		public int numCustomerPlan;
		public int numCustomerOrder;
		public int numCustomerNew;
		public int numCustomerIr;
		public int numCustomerOr;
		public double score;
		public long shopId;
		public String saleTypeCode;

		/**
		 * Khởi tạo dữ liệu từ con trỏ
		 * 
		 * @author: TamPQ
		 * @param: Cursor c
		 * @return: void
		 * @throws:
		 */
		public void initData(Cursor c) {
			if (c == null) {
				return;
			}
			SimpleDateFormat sfs = DateUtils.defaultSqlDateFormat;
			SimpleDateFormat sfd = DateUtils.defaultDateFormat;
			trainDetailId = c.getInt(c.getColumnIndex("TRAINING_PLAN_DETAIL_ID"));
			staffId = c.getInt(c.getColumnIndex("STAFF_ID"));
			staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
			staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
			shopId = c.getInt(c.getColumnIndex("SHOP_ID"));
			saleTypeCode = c.getString(c.getColumnIndex("SALE_TYPE_CODE"));
			amountMonth = c.getDouble(c.getColumnIndex("AMOUNT_PLAN"));
			amount = c.getDouble(c.getColumnIndex("AMOUNT"));
			numCustomerPlan = c.getInt(c.getColumnIndex("NUM_CUSTOMER_PLAN"));
			numCustomerOrder = c.getInt(c.getColumnIndex("NUM_CUSTOMER_ORDER"));
			numCustomerNew = c.getInt(c.getColumnIndex("NUM_CUSTOMER_NEW"));
			numCustomerIr = c.getInt(c.getColumnIndex("NUM_CUSTOMER_ON"));
			numCustomerOr = c.getInt(c.getColumnIndex("NUM_CUSTOMER_OR"));
			score = c.getDouble(c.getColumnIndex("SCORE"));
			try {
				date = sfd.format(sfs.parse(c.getString(c.getColumnIndex("TRAINING_DATE"))));
			} catch (ParseException e) {
			}
		}
	}
}
