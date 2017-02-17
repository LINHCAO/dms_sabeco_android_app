/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

/**
 * TBHVDayTrainingSupervisionDTO
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVTrainingPlanDayResultReportDTO implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public double amountMonth;
	public double amount;
	public int isNew;
	public int isOn;
	public int isOr;
	public double score;
	public double distance;
	public ArrayList<TBHVTrainingPlanDayResultReportItem> listResult = new ArrayList<TBHVTrainingPlanDayResultReportDTO.TBHVTrainingPlanDayResultReportItem>();

	public class TBHVTrainingPlanDayResultReportItem implements Serializable {
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
		
		public TBHVTrainingPlanDayResultReportItem(){
			
		}

		public void initFromCursor(Cursor c, TBHVTrainingPlanDayResultReportDTO dto, int stt2) {
			stt = stt2;
			custCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			custId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
			custName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			custAddr = c.getString(c.getColumnIndex("HOUSENUMBER"));
			custAddr += " " + c.getString(c.getColumnIndex("STREET"));
			amountMonth = c
					.getDouble(c.getColumnIndex("AMOUNT_PLAN"));
			amount = c.getDouble(c.getColumnIndex("AMOUNT"));
			isNew = c.getInt(c.getColumnIndex("IS_NEW"));
			isOn = c.getInt(c.getColumnIndex("IS_ON"));
			isOr = c.getInt(c.getColumnIndex("IS_OR"));
			score = c.getDouble(c.getColumnIndex("SCORE"));
			lat = c.getDouble(c.getColumnIndex("LAT"));
			lng = c.getDouble(c.getColumnIndex("LNG"));
			dto.listResult.add(this);
			dto.amountMonth += amountMonth;
			dto.amount += amount;
			dto.isNew += isNew;
			dto.isOr += isOr;
			dto.isOn += isOn;
//			dto.score += score;
			
		}
		

	}
	
	public TBHVTrainingPlanDayResultReportDTO(){
		listResult = new ArrayList<TBHVTrainingPlanDayResultReportItem>();
	}
	
	public TBHVTrainingPlanDayResultReportItem newStaffTrainResultReportItem() {
		return new TBHVTrainingPlanDayResultReportItem();
	}

}
