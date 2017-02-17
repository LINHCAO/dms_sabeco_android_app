/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import android.database.Cursor;

import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/** 
 * GSNPP Training Plan DTO class
 * GSNPPTrainingPlanDTO.java
 * @version: 1.0 
 * @since:  10:27:56 8 Jan 2014
 */
@SuppressWarnings("serial")
public class GSNPPTrainingPlanDTO implements Serializable {

	public class GSNPPTrainingPlanIem implements Serializable {
		public Date date;
		public String dateString;
		public long staffId;
		public String staffName;
		public long trainDetailId;
		public double score;
		public long shopId;
		public String shopCode;
		public String shopName;
		public String saleTypeCode;

		/**
		 * Khởi tạo từ con trỏ
		 * 
		 * @author: TamPQ
		 * @param: Cursor c
		 * @return: void
		 * @throws:
		 */
		public void initFromCursor(Cursor c) {
			SimpleDateFormat sfs = DateUtils.defaultSqlDateFormat;
			SimpleDateFormat sfd = DateUtils.defaultDateFormat;
			if (c.getColumnIndex("TRAINING_PLAN_DETAIL_ID") > -1) {
				trainDetailId = c.getInt(c.getColumnIndex("TRAINING_PLAN_DETAIL_ID"));
			}
			if (c.getColumnIndex("STAFF_ID") > -1) {
				staffId = c.getInt(c.getColumnIndex("STAFF_ID"));
			}
			if (c.getColumnIndex("STAFF_NAME") > -1) {
				staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
			}
			if (c.getColumnIndex("SCORE") > -1) {
				score = c.getDouble(c.getColumnIndex("SCORE"));
			}
			if (c.getColumnIndex("SHOP_ID") > -1) {
				shopId = c.getInt(c.getColumnIndex("SHOP_ID"));
			}
			if (c.getColumnIndex("SHOP_CODE") > -1) {
				shopCode = c.getString(c.getColumnIndex("SHOP_CODE"));
			}
			if (c.getColumnIndex("SHOP_NAME") > -1) {

				shopName = c.getString(c.getColumnIndex("SHOP_NAME"));
			}
			if (c.getColumnIndex("SALE_TYPE_CODE") > -1) {
				saleTypeCode = c.getString(c.getColumnIndex("SALE_TYPE_CODE"));
			}

			if (c.getColumnIndex("TRAINING_DATE") > -1) {
				try {
					date = sfs.parse(c.getString(c.getColumnIndex("TRAINING_DATE")));
					dateString = sfd.format(date);
				} catch (ParseException e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				}
			}

		}

	}

	public Hashtable<String, GSNPPTrainingPlanIem> listResult = new Hashtable<String, GSNPPTrainingPlanIem>();

	public GSNPPTrainingPlanIem newPlanTrainResultReportItem() {
		return new GSNPPTrainingPlanIem();
	}
}
