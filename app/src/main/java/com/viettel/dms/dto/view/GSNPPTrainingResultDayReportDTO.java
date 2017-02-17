/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/**
 * GSNPP Training Result Day Report DTO class
 * GSNPPTrainingResultDayReportDTO.java
 * @version: 1.0 
 * @since:  10:28:31 8 Jan 2014
 */
public class GSNPPTrainingResultDayReportDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	public double amountMonth;
	public double amount;
	public int isNew;
	public int isOn;
	public int isOr;
	public double score;
	public double distance;
	public ArrayList<GSNPPTrainingResultReportDayItem> listResult = new ArrayList<GSNPPTrainingResultReportDayItem>();

	public enum VISIT_STATUS {
		NONE_VISIT, VISITED, VISITING
	}

	public class GSNPPTrainingResultReportDayItem implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public int stt;
		public String custCode;
		public long custId;
		public String custName;
		public String custAddr;
		public double amountMonth;
		public double amount;
		public double score;
		public int isNew;
		public int isOn;
		public int isOr;
		public double lat;
		public double lng;
		public int channelTypeId;
		public String startTime;
		public String endTime;
		public double tpdScore;
		public VISIT_STATUS visit = VISIT_STATUS.NONE_VISIT;
		public long visitActLogId;

		public void initFromCursor(Cursor c, GSNPPTrainingResultDayReportDTO dto, int stt2) {
			stt = stt2;
			custCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			custId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
			custName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			custAddr = c.getString(c.getColumnIndex("HOUSENUMBER"));
			custAddr += " " + c.getString(c.getColumnIndex("STREET"));
			amountMonth = c.getDouble(c.getColumnIndex("AMOUNT_PLAN"));
			amount = c.getDouble(c.getColumnIndex("AMOUNT"));
			isNew = c.getInt(c.getColumnIndex("IS_NEW"));
			isOn = c.getInt(c.getColumnIndex("IS_ON"));
			isOr = c.getInt(c.getColumnIndex("IS_OR"));
			score = c.getDouble(c.getColumnIndex("SCORE"));
			lat = c.getDouble(c.getColumnIndex("LAT"));
			lng = c.getDouble(c.getColumnIndex("LNG"));
			startTime = c.getString(c.getColumnIndex("AL_START_TIME"));
			endTime = c.getString(c.getColumnIndex("AL_END_TIME"));
			tpdScore = c.getDouble(c.getColumnIndex("TPD_SCORE"));
			visitActLogId = c.getLong(c.getColumnIndex("AL_ID"));
			if (!StringUtil.isNullOrEmpty(startTime)) {
				if (!StringUtil.isNullOrEmpty(endTime)) {
					visit = VISIT_STATUS.VISITED;
				} else {
					visit = VISIT_STATUS.VISITING;
				}
			}
			if (c.getColumnIndex("CHANNEL_TYPE_ID") > -1) {
				channelTypeId = c.getInt(c.getColumnIndex("CHANNEL_TYPE_ID"));
			}
			dto.listResult.add(this);
			dto.amountMonth += amountMonth;
			dto.amount += amount;
			dto.isNew += isNew;
			dto.isOr += isOr;
			dto.isOn += isOn;
			// dto.score += score;
			dto.score = tpdScore;

		}

	}

	public GSNPPTrainingResultReportDayItem newStaffTrainResultReportItem() {
		return new GSNPPTrainingResultReportDayItem();
	}
}
