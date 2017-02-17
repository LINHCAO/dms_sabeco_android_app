/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import android.database.Cursor;

import com.viettel.dms.util.DateUtils;

/**
 * Giam sat huan luyen ngay DTO
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
@SuppressWarnings("serial")
public class TBHVTrainingPlanDTO implements Serializable {
	// public ArrayList<TBHVGsnppTrainingPlanItem> spinerGsnppList = new
	// ArrayList<TBHVGsnppTrainingPlanDTO.TBHVGsnppTrainingPlanItem>(); // ds
	// gsnpp thuoc quyen quan ly
	public GSNPPTrainingPlanDTO trainingPlanOfGsnppDto = new GSNPPTrainingPlanDTO();
	public ListStaffDTO spinnerStaffList = new ListStaffDTO();
	public int spinnerItemSelected = -1;
	public Hashtable<String, TBHVTrainingPlanItem> tbhvTrainingPlan = new Hashtable<String, TBHVTrainingPlanItem>();

	public class TBHVTrainingPlanItem implements Serializable {
		public Date date;// date
		public String dateString;// date String
		public int staffId;// staff Id
		public String staffName;// staff Name
		public long trainDetailId;// train Detail Id
		public double score;// score
		public int shopId;// shopId
		public String shopCode;// shopCode
		public String shopName;// shopName
		public int nvbh_staff_id;// nvbh_staff_id
		public String nvbh_staff_name;// nvbh_staff_name
		public int nvbh_trainig_plan_detail_id;// nvbh_trainig_plan_detail_id
		public String staffCode;// staffCode

		/**
		 * init data
		 * 
		 * @author: TamPQ
		 * @return: void
		 * @throws:
		 */
		public void initFromCursor(Cursor c) {
			SimpleDateFormat sfs = DateUtils.defaultSqlDateFormat;
			SimpleDateFormat sfd = DateUtils.defaultDateFormat;
			trainDetailId = c.getInt(c.getColumnIndex("TRAINING_PLAN_DETAIL_ID"));
			staffId = c.getInt(c.getColumnIndex("GSNPP_STAFF_ID"));
			staffName = c.getString(c.getColumnIndex("GSNPP_STAFF_NAME"));
			staffCode = c.getString(c.getColumnIndex("GSNPP_STAFF_CODE"));
//			nvbhStaffId = c.getString(c.getColumnIndex("NVBH_STAFF_ID"));
			try {
				date = sfs.parse(c.getString(c.getColumnIndex("TRAINING_DATE")));
				dateString = sfd.format(date);
			} catch (ParseException e) {
			}

		}

	}

	public TBHVTrainingPlanDTO() {
	}

	/**
	 * init TBHVGsnppTrainingPlanItem dto
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public TBHVTrainingPlanItem newTBHVGsnppTrainingPlanItem() {
		return new TBHVTrainingPlanItem();
	}

	/**
	 * is Existed In List
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public boolean isExistedInList(int staffId, ArrayList<TBHVTrainingPlanItem> list) {
		boolean isExisted = false;
		for (int i = 0; i < list.size(); i++) {
			if (staffId == list.get(i).staffId) {
				isExisted = true;
				break;
			}
		}
		return isExisted;
	}
}
