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
 * TBHVHistoryPlanTrainingDTO
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVTrainingPlanHistoryAccDTO {
	public ArrayList<TBHVHistoryPlanTrainingItem> listResult = new ArrayList<TBHVHistoryPlanTrainingItem>();
	public double amountMonth; // amount Month
	public double amount;// amount
	public int numCustomerPlan;// tong so customer
	public int numCustomerOrder;// so customer order
	public int numCustomerNew;// so customer moi
	public int numCustomerOn;// so customer trong
	public int numCustomerOr;// so Customer ngoai tuyen
	public double score;// diem
	public int shopId;// shop id

	/**
	 * new TBHVHistoryPlanTrainingItem dto
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public TBHVHistoryPlanTrainingItem newTBHVHistoryPlanTrainingItem() {
		return new TBHVHistoryPlanTrainingItem();
	}

	public class TBHVHistoryPlanTrainingItem {
		public int trainDetailId;// training Detail Id
		public int staffId;// npp staff Id
		public String staffCode;// npp staff Code
		public String staffName;// npp staff Name
		public String date;// date
		public double amountMonth;// amount Month
		public double amount;// amount
		public int numCustomerPlan;// num Customer Plan
		public int numCustomerOrder;// num Customer Ordered
		public int numCustomerNew;// num new Customer
		public int numCustomerIr;// num in plan Customer
		public int numCustomerOr;// num out plan Customer
		public double score;// score
		public int shopId;// shop Id
		public String shop_name;// shop_name
		public String shop_code;// shop_code
		public int nvbhStaffId;// nvbh Staff Id
		public String nvbhStaffCode;// nvbh Staff Code
		public String nvbhStaffName;// nvbh Staff Name
		public int nvbh_trainDetailId;// nvbh trainDetail Id
		public String nvbhShopCode;
		public String nvbhShopName;
		public int nvbhShopId;
		public String nvbhTrainingDate;

		/**
		 * init data
		 * 
		 * @author: TamPQ
		 * @return: void
		 * @throws:
		 */
		public void initFromCursor(Cursor c, TBHVTrainingPlanHistoryAccDTO dto) {
			SimpleDateFormat sfs = DateUtils.defaultSqlDateFormat;
			SimpleDateFormat sfd = DateUtils.defaultDateFormat;

			nvbh_trainDetailId = c.getInt(c.getColumnIndex("TRAINING_PLAN_DETAIL_ID"));
			staffId = c.getInt(c.getColumnIndex("GSNPP_STAFF_ID"));
			staffCode = c.getString(c.getColumnIndex("GSNPP_STAFF_CODE"));
			staffName = c.getString(c.getColumnIndex("GSNPP_STAFF_NAME"));
			nvbhStaffId = c.getInt(c.getColumnIndex("STAFF_ID"));
			nvbhStaffCode = c.getString(c.getColumnIndex("NVBH_STAFF_CODE"));
			nvbhStaffName = c.getString(c.getColumnIndex("NVBH_STAFF_NAME"));
			nvbhShopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
			nvbhShopName = c.getString(c.getColumnIndex("NVBH_SHOP_NAME"));
			nvbhShopId = c.getInt(c.getColumnIndex("SHOP_ID"));
			// nvbhTrainingDate =
			// c.getString(c.getColumnIndex("NVBH_TRAINING_DATE"));
			amountMonth = c.getDouble(c.getColumnIndex("AMOUNT_PLAN"));
			amount = c.getDouble(c.getColumnIndex("AMOUNT"));
			numCustomerPlan = c.getInt(c.getColumnIndex("NUM_CUSTOMER_PLAN"));
			numCustomerOrder = c.getInt(c.getColumnIndex("NUM_CUSTOMER_ORDER"));
			numCustomerNew = c.getInt(c.getColumnIndex("NUM_CUSTOMER_NEW"));
			numCustomerIr = c.getInt(c.getColumnIndex("NUM_CUSTOMER_IR"));
			numCustomerOr = c.getInt(c.getColumnIndex("NUM_CUSTOMER_OR"));
			numCustomerOn = c.getInt(c.getColumnIndex("NUM_CUSTOMER_ON"));
			try {
				score = c.getDouble(c.getColumnIndex("SCORE"));
			} catch (Exception e) {
				score = 0;
			}

			try {
				date = sfd.format(sfs.parse(c.getString(c.getColumnIndex("TRAINING_DATE"))));
			} catch (ParseException e) {

			}
			dto.listResult.add(this);
			dto.amountMonth += amountMonth;
			dto.amount += amount;
			dto.numCustomerPlan += numCustomerPlan;
			dto.numCustomerOrder += numCustomerOrder;
			dto.numCustomerNew += numCustomerNew;
			dto.numCustomerOn += numCustomerIr;
			dto.numCustomerOr += numCustomerOr;
			dto.score += score;

		}

		/**
		 * Mo ta muc dich cua ham
		 * 
		 * @author: TamPQ
		 * @param trainedDto
		 * @return
		 * @return: booleanvoid
		 * @throws:
		 */
		public boolean isTrainedByTBHV(TBHVTrainingPlanHistoryAccDTO trainedDto) {
			for (int i = 0; i < trainedDto.listResult.size(); i++) {
				if (date.equals(trainedDto.listResult.get(i).date)) {
					return true;
				}
			}
			return false;
		}

		public boolean isTrainedTodayByTbvh(TBHVTrainingPlanHistoryAccDTO trainedDto) {
			boolean isTrainedTodayByTbvh = false;
			if (isTrainedByTBHV(trainedDto)) {
				isTrainedTodayByTbvh = date.equals(DateUtils.getCurrentDate());
			}
			return isTrainedTodayByTbvh;
		}
	}
}
