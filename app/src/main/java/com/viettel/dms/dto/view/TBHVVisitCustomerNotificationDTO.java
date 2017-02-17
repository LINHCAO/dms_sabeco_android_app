/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;

import android.database.Cursor;

import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVVisitCustomerNotificationDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public ArrayList<TBHVVisitCustomerNotificationItem> arrList = new ArrayList<TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem>();
	public Hashtable<String, ShopParamDTO> listParam = new Hashtable<String, ShopParamDTO>();
	public String lessColumn1;
	public String lessColumn2;
	public String lessColumn3;
	public String lessColumn4;

	public TBHVVisitCustomerNotificationDTO() {
	}

	public TBHVVisitCustomerNotificationItem newTBHVVisitCustomerNotificationItem() {
		return new TBHVVisitCustomerNotificationItem();
	}

	public class TBHVVisitCustomerNotificationItem implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String nvbhShopCode;
		public long nvbhShopId;
		public long gsnppStaffId;
		public String gsnppStaffCode;
		public String gsnppStaffName;
		public int numNvbh;
		// public int numNvbh_1200_plan;
		// public int numNvbh_1600_plan;
		// public int numNvbh_now_plan;
		public int numNvbh_right_930;
		public int numNvbh_right_1200;
		public int numNvbh_right_1600;
		public int numNvbh_right_now;
		public String less9h30;
		public String less12h00;
		public String less16h00;
		public String lessnow;
//		public String time9h30;
//		public String time1200;
//		public String time1600;
		public ArrayList<NVBH> arrNVBH = new ArrayList<TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem.NVBH>();
//		public String time9h30_1;
//		public String time1200_1;
//		public String time1600_1;
//		public String time9h30_desc;
//		public String time1200_desc;
//		public String time1600_desc;

		public TBHVVisitCustomerNotificationItem() {

		}

		public class NVBH implements Serializable {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public long staffId;
			public int num930;
			public int num1200;
			public int num1600;
			public int numNow;
			public int numCusPlan;
			public int num_1200_plan;
			public int num_1600_plan;
			public int num_now_plan;
		}

		/**
		 * Mo ta muc dich cua ham
		 * 
		 * @author: TamPQ
		 * @param c
		 * @return: voidvoid
		 * @throws:
		 */
		public void initData(Cursor c) {
			try {
				Calendar cal = Calendar.getInstance();
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				String timeNow = sdf.format(cal.getTime());

				if (c == null) {
					throw new Exception("Cursor is empty");
				}
				if (c.getColumnIndex("NVBH_SHOP_ID") > -1) {
					nvbhShopId = c.getLong(c.getColumnIndex("NVBH_SHOP_ID"));
				}
				if (c.getColumnIndex("GS_STAFF_ID") > -1) {
					gsnppStaffId = c.getInt(c.getColumnIndex("GS_STAFF_ID"));
				}
				if (c.getColumnIndex("GS_STAFF_CODE") > -1) {
					gsnppStaffCode = c.getString(c.getColumnIndex("GS_STAFF_CODE"));
				}
				if (c.getColumnIndex("GS_STAFF_NAME") > -1) {
					gsnppStaffName = c.getString(c.getColumnIndex("GS_STAFF_NAME"));
				}
				if (c.getColumnIndex("NVBH_SHOP_CODE") > -1) {
					nvbhShopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
				}
				if (c.getColumnIndex("NUM_NVBH") > -1) {
					numNvbh = c.getInt(c.getColumnIndex("NUM_NVBH"));
				}

				if (c.getColumnIndex("NUM930NUM") > -1) {
					numNvbh_right_930 = c.getInt(c.getColumnIndex("NUM930NUM"));
				}
				String nvbhListStr = null;
				String num1200ListStr = null;
				String num1600ListStr = null;
				String numNowListStr = null;
				String numCusPlanStr = null;
				if (c.getColumnIndex("STAFF_LIST") > -1) {
					nvbhListStr = c.getString(c.getColumnIndex("STAFF_LIST"));
					if (nvbhListStr == null) {
						nvbhListStr = "";
					}
				}
				if (c.getColumnIndex("NUM1200_LIST") > -1) {
					num1200ListStr = c.getString(c.getColumnIndex("NUM1200_LIST"));
					if (num1200ListStr == null) {
						num1200ListStr = "";
					}
				}
				if (c.getColumnIndex("NUM1600_LIST") > -1) {
					num1600ListStr = c.getString(c.getColumnIndex("NUM1600_LIST"));
					if (num1600ListStr == null) {
						num1600ListStr = "";
					}
				}
				if (c.getColumnIndex("NUM_NOW_LIST") > -1) {
					numNowListStr = c.getString(c.getColumnIndex("NUM_NOW_LIST"));
					if (numNowListStr == null) {
						numNowListStr = "";
					}
				}
				if (c.getColumnIndex("TOTAL_CUS_PLAN_LIST") > -1) {
					numCusPlanStr = c.getString(c.getColumnIndex("TOTAL_CUS_PLAN_LIST"));
					if (numCusPlanStr == null) {
						numCusPlanStr = "";
					}
				}

				String[] nvbhList = nvbhListStr.split(",");
				String[] num1200List = num1200ListStr.split(",");
				String[] num1600List = num1600ListStr.split(",");
				String[] numNowList = numNowListStr.split(",");
				String[] numCusPlanList = numCusPlanStr.split(",");

				for (int i = 0; i < nvbhList.length; i++) {
					NVBH nvbh = new NVBH();
					if (!StringUtil.isNullOrEmpty(nvbhList[i])) {
						nvbh.staffId = Long.parseLong(nvbhList[i]);
					}
					if (!StringUtil.isNullOrEmpty(num1200List[i])) {
						nvbh.num1200 = Integer.parseInt(num1200List[i]);
					}
					if (!StringUtil.isNullOrEmpty(num1600List[i])) {
						nvbh.num1600 = Integer.parseInt(num1600List[i]);
					}
					if (!StringUtil.isNullOrEmpty(numNowList[i])) {
						nvbh.numNow = Integer.parseInt(numNowList[i]);
					}
					if (!StringUtil.isNullOrEmpty(numCusPlanList[i])) {
						nvbh.numCusPlan = Integer.parseInt(numCusPlanList[i]);
					}
					nvbh.num_1200_plan = (int) Math.ceil((double) nvbh.numCusPlan / (double) 2);
					
					long temp2 = DateUtils.getDistanceMinutesFrom2Hours("08:00", timeNow);
					long temp3 = DateUtils.getDistanceMinutesFrom2Hours("08:00", listParam.get("DT_END").code);
					nvbh.num_now_plan = StringUtil.calcularPercent((double) temp2, (double) temp3);
					nvbh.num_1600_plan = nvbh.numCusPlan;
					arrNVBH.add(nvbh);
				}

				for (NVBH nvbh : arrNVBH) {
					if (nvbh.numCusPlan <= 0 || nvbh.num1200 >= nvbh.num_1200_plan) {
						numNvbh_right_1200++;
					}
					if (nvbh.numCusPlan <= 0 || nvbh.num1600 >= nvbh.num_1600_plan) {
						numNvbh_right_1600++;
					}
					int temp = (int) (((double) nvbh.numNow / (double) nvbh.numCusPlan) * 100);
					if (nvbh.numCusPlan <= 0 || temp >= nvbh.num_now_plan) {
						numNvbh_right_now++;
					}
				}

			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
	}
}
