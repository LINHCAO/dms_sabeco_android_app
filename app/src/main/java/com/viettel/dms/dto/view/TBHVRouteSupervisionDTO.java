/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.STAFF_POSITION_LOG_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;

/**
 * Giam sat lo trinh DTO
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVRouteSupervisionDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int totalList;

	public enum STATUS {
		NONE_VISIT, VISITED, WRONG_PLAN, RIGHT_PLAN, NVBH_VISITED_BUT_GSNPP, GSNPP_VISITED_BUT_NVBH
	}

	public ArrayList<THBVRouteSupervisionItem> itemGSNPPList;
	public ArrayList<THBVRouteSupervisionItem> itemListTTTT;
	public ArrayList<THBVRouteSupervisionItem> itemListNVBH;

	public TBHVRouteSupervisionDTO() {
		itemGSNPPList = new ArrayList<THBVRouteSupervisionItem>();
		itemListTTTT = new ArrayList<THBVRouteSupervisionItem>();
		itemListNVBH = new ArrayList<THBVRouteSupervisionItem>();
	}

	public THBVRouteSupervisionItem newTHBVRouteSupervisionItem() {
		return new THBVRouteSupervisionItem();
	}

	public class THBVRouteSupervisionItem implements Serializable {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public String staffNameGS; // name Gsnpp
		public String staffCodeGS; // staffCode Gsnpp
		public String staffIdGS;// staffId
		public String staffNameNVBH; // name NVBH
		public String gsnppMobile;
		public String gsnppPhone;
		public int staffIdNVBH; // staffId NVBH
		public String staffCodeNVBH;// staffCode NVBH
		public int numSaleStore;// num Sale Store
		public int numRightPlan;// num Right Plan
		public int numWrongPlan;// num Wrong Plan
		public int numGsnppVisitedStore;// num Visited Store
		public double lat;// lat
		public double lng;// lng
		public String shopName;
		public int shopId;// shopId
		public String shopCode;
		public ArrayList<Customer> gsnppRealSeq;// gsnpp Real Seq
		public ArrayList<Customer> nvbhRealSeq;// nvbh Real Seq
		public String updateTime;
		public String visitingCusName;
		
		public int numNvbhVisitedStore;
		public int numGsVisitedButNvbh;

		public THBVRouteSupervisionItem() {

		}

		/**
		 * Customer Item
		 * 
		 * @author : TamPQ
		 */
		@SuppressWarnings("serial")
		public class Customer implements Serializable {
			public long cusId;
			public STATUS status;
			public double lat;
			public double lng;
			public int seqInPlan = -1;
			public String visit_start_time;
			public Date visit_start_time_2;
			public String visit_end_time;
			public Date visit_end_time_2;
			public String codeName;
			public String address;
			public boolean isVisting;
			public int realSeq;

			public Customer(STATUS st) {
				status = st;
			}

		}

		/**
		 * initDataFromCursor
		 * 
		 * @author: TamPQ
		 * @return: void
		 * @throws:
		 */
		public void initDataFromCursor(Cursor c) {
			if (c.getColumnIndex("GS_STAFF_ID") >= 0) {
				staffIdGS = c.getString(c.getColumnIndex("GS_STAFF_ID"));
			} else {
				staffIdGS = "";
			}
			if (c.getColumnIndex("GS_STAFF_CODE") >= 0) {
				staffCodeGS = c.getString(c.getColumnIndex("GS_STAFF_CODE"));
			} else {
				staffCodeGS = "";
			}
			if (c.getColumnIndex("GS_STAFF_NAME") >= 0) {
				staffNameGS = c.getString(c.getColumnIndex("GS_STAFF_NAME"));
			} else {
				staffNameGS = "";
			}
			if (c.getColumnIndex("NVBH_SHOP_ID") >= 0) {
				shopId = c.getInt(c.getColumnIndex("NVBH_SHOP_ID"));
			} else {
				shopId = 0;
			}
			if (c.getColumnIndex("NVBH_SHOP_CODE") >= 0) {
				shopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
			} else {
				shopCode = "";
			}
			if (c.getColumnIndex("NVBH_STAFF_ID") >= 0) {
				staffIdNVBH = c.getInt(c.getColumnIndex("NVBH_STAFF_ID"));
			} else {
				staffIdNVBH = 0;
			}
			if (c.getColumnIndex("NVBH_STAFF_CODE") >= 0) {
				staffCodeNVBH = c.getString(c.getColumnIndex("NVBH_STAFF_CODE"));
			} else {
				staffCodeNVBH = "";
			}
			if (c.getColumnIndex("NVBH_STAFF_NAME") >= 0) {
				staffNameNVBH = c.getString(c.getColumnIndex("NVBH_STAFF_NAME"));
			} else {
				staffNameNVBH = "";
			}
			if (c.getColumnIndex("NUM_CUS_PLAN") >= 0) {
				numSaleStore = c.getInt(c.getColumnIndex("NUM_CUS_PLAN"));
			}

			String strGSNPPRealSeq;
			String strGSNPPRealLat;
			String strGSNPPRealLng;
			String strGSNPPRealStartTime;
			String strGSNPPRealCustomerCodeName;
			String strGSNPPRealCustomerAddress;
			String strNvbhRealSeq;
			String strNvbhRealLat;
			String strNvbhRealLng;
			String strNvbhRealStartTime;
			String strNvbhRealEndTime;
			String strNVBHRealCustomerCodeName;
			String strNVBHRealCustomerAddress;

			if (c.getColumnIndex("NPP_VISITED_ORDER") >= 0) {
				strGSNPPRealSeq = c.getString(c.getColumnIndex("NPP_VISITED_ORDER"));
				if (strGSNPPRealSeq == null) {
					strGSNPPRealSeq = "";
				}
			} else {
				strGSNPPRealSeq = "";
			}
			if (c.getColumnIndex("NPP_VISITED_LAT") >= 0) {
				strGSNPPRealLat = c.getString(c.getColumnIndex("NPP_VISITED_LAT"));
				if (strGSNPPRealLat == null) {
					strGSNPPRealLat = "";
				}
			} else {
				strGSNPPRealLat = "";
			}
			if (c.getColumnIndex("NPP_VISITED_LNG") >= 0) {
				strGSNPPRealLng = c.getString(c.getColumnIndex("NPP_VISITED_LNG"));
				if (strGSNPPRealLng == null) {
					strGSNPPRealLng = "";
				}
			} else {
				strGSNPPRealLng = "";
			}
			if (c.getColumnIndex("NPP_VISITED_START_TIME") >= 0) {
				strGSNPPRealStartTime = c.getString(c.getColumnIndex("NPP_VISITED_START_TIME"));
				if (strGSNPPRealStartTime == null) {
					strGSNPPRealStartTime = "";
				}
			} else {
				strGSNPPRealStartTime = "";
			}
			if (c.getColumnIndex("NPP_VISITED_END_TIME") >= 0) {
				strNvbhRealEndTime = c.getString(c.getColumnIndex("NPP_VISITED_END_TIME"));
				if (strNvbhRealEndTime == null) {
					strNvbhRealEndTime = "";
				}
			} else {
				strNvbhRealEndTime = "";
			}
			if (c.getColumnIndex("NPP_VISITED_CUSTOMER_CODE_NAME") >= 0) {
				strGSNPPRealCustomerCodeName = c.getString(c.getColumnIndex("NPP_VISITED_CUSTOMER_CODE_NAME"));
				if (strGSNPPRealCustomerCodeName == null) {
					strGSNPPRealCustomerCodeName = "";
				}
			} else {
				strGSNPPRealCustomerCodeName = "";
			}
			if (c.getColumnIndex("NPP_VISITED_CUSTOMER_ADDRESS") >= 0) {
				strGSNPPRealCustomerAddress = c.getString(c.getColumnIndex("NPP_VISITED_CUSTOMER_ADDRESS"));
				if (strGSNPPRealCustomerAddress == null) {
					strGSNPPRealCustomerAddress = "";
				}
			} else {
				strGSNPPRealCustomerAddress = "";
			}
			if (c.getColumnIndex("NVBH_VISITED_ORDER") >= 0) {
				strNvbhRealSeq = c.getString(c.getColumnIndex("NVBH_VISITED_ORDER"));
				if (strNvbhRealSeq == null) {
					strNvbhRealSeq = "";
				}
			} else {
				strNvbhRealSeq = "";
			}
			if (c.getColumnIndex("NVBH_VISITED_LAT") >= 0) {
				strNvbhRealLat = c.getString(c.getColumnIndex("NVBH_VISITED_LAT"));
				if (strNvbhRealLat == null) {
					strNvbhRealLat = "";
				}
			} else {
				strNvbhRealLat = "";
			}
			if (c.getColumnIndex("NVBH_VISITED_LNG") >= 0) {
				strNvbhRealLng = c.getString(c.getColumnIndex("NVBH_VISITED_LNG"));
				if (strNvbhRealLng == null) {
					strNvbhRealLng = "";
				}
			} else {
				strNvbhRealLng = "";
			}
			if (c.getColumnIndex("NVBH_VISITED_START_TIME") >= 0) {
				strNvbhRealStartTime = c.getString(c.getColumnIndex("NVBH_VISITED_START_TIME"));
				if (strNvbhRealStartTime == null) {
					strNvbhRealStartTime = "";
				}
			} else {
				strNvbhRealStartTime = "";
			}
			if (c.getColumnIndex("NVBH_VISITED_END_TIME") >= 0) {
				strNvbhRealEndTime = c.getString(c.getColumnIndex("NVBH_VISITED_END_TIME"));
				if (strNvbhRealEndTime == null) {
					strNvbhRealEndTime = "";
				}
			} else {
				strNvbhRealEndTime = "";
			}

			if (c.getColumnIndex("NVBH_VISITED_CUSTOMER_CODE_NAME") >= 0) {
				strNVBHRealCustomerCodeName = c.getString(c.getColumnIndex("NVBH_VISITED_CUSTOMER_CODE_NAME"));
				if (strNVBHRealCustomerCodeName == null) {
					strNVBHRealCustomerCodeName = "";
				}
			} else {
				strNVBHRealCustomerCodeName = "";
			}
			if (c.getColumnIndex("NVBH_VISITED_CUSTOMER_ADDRESS") >= 0) {
				strNVBHRealCustomerAddress = c.getString(c.getColumnIndex("NVBH_VISITED_CUSTOMER_ADDRESS"));
				if (strNVBHRealCustomerAddress == null) {
					strNVBHRealCustomerAddress = "";
				}
			} else {
				strNVBHRealCustomerAddress = "";
			}

			String[] arrGsnppRealSeq = strGSNPPRealSeq.split(",");
			String[] arrGsnppRealLat = strGSNPPRealLat.split(",");
			String[] arrGsnppRealLng = strGSNPPRealLng.split(",");
			String[] arrGsnppRealStartTime = strGSNPPRealStartTime.split(",");
			String[] arrGsnppRealCustomerCodeName = strGSNPPRealCustomerCodeName.split(",");
			String[] arrGsnppRealCustomerAddress = strGSNPPRealCustomerAddress.split(",");
			String[] arrNvbhRealSeq = strNvbhRealSeq.split(",");
			String[] arrNvbhRealLat = strNvbhRealLat.split(",");
			String[] arrNvbhRealLng = strNvbhRealLng.split(",");
			String[] arrNvbhRealStartTime = strNvbhRealStartTime.split(",");
			String[] arrNvbhRealEndTime = strNvbhRealEndTime.split(",");
			String[] arrNvbhRealCustomerCodeName = strNVBHRealCustomerCodeName.split(",");
			String[] arrNvbhRealCustomerAddress = strNVBHRealCustomerAddress.split(",");

			gsnppRealSeq = initListCustomer(arrGsnppRealSeq, null, arrGsnppRealLat, arrGsnppRealLng,
					arrGsnppRealStartTime, null, arrGsnppRealCustomerCodeName, arrGsnppRealCustomerAddress, 1);

			nvbhRealSeq = initListCustomer(arrNvbhRealSeq, null, arrNvbhRealLat, arrNvbhRealLng, arrNvbhRealStartTime,
					arrNvbhRealEndTime, arrNvbhRealCustomerCodeName, arrNvbhRealCustomerAddress, 0);

			numGsnppVisitedStore = gsnppRealSeq.size();
			numNvbhVisitedStore = nvbhRealSeq.size();

			// check sai lo trinh:
			if (nvbhRealSeq.size() >= 2) {
				for (int i = 0; i < nvbhRealSeq.size(); i++) {
					Customer gsnppCus = getCusInList(nvbhRealSeq.get(i), gsnppRealSeq);
					if (i == 0) {
						if (gsnppCus != null
								&& gsnppCus.visit_start_time_2.before(nvbhRealSeq.get(i + 1).visit_start_time_2)) {
							gsnppCus.status = STATUS.RIGHT_PLAN;
							numRightPlan++;
						} else if (gsnppCus != null) {
							gsnppCus.status = STATUS.WRONG_PLAN;
						} else {
							nvbhRealSeq.get(i).status = STATUS.NVBH_VISITED_BUT_GSNPP;
						}
					} else if (i < nvbhRealSeq.size() - 1) {
						if (gsnppCus != null
								&& gsnppCus.visit_start_time_2.after(nvbhRealSeq.get(i - 1).visit_end_time_2)
								&& gsnppCus.visit_start_time_2.before(nvbhRealSeq.get(i + 1).visit_start_time_2)) {
							gsnppCus.status = STATUS.RIGHT_PLAN;
							numRightPlan++;
						} else if (gsnppCus != null) {
							gsnppCus.status = STATUS.WRONG_PLAN;
						} else {
							nvbhRealSeq.get(i).status = STATUS.NVBH_VISITED_BUT_GSNPP;
						}
					} else {
						if (gsnppCus != null
								&& gsnppCus.visit_start_time_2.after(nvbhRealSeq.get(i - 1).visit_end_time_2)) {
							gsnppCus.status = STATUS.RIGHT_PLAN;
							numRightPlan++;
						} else if (gsnppCus != null) {
							gsnppCus.status = STATUS.WRONG_PLAN;
						} else {
							nvbhRealSeq.get(i).status = STATUS.NVBH_VISITED_BUT_GSNPP;
						}
					}
				}
			} else if (nvbhRealSeq.size() == 1) {
				// neu nvbh moi di 1 KH thi GS luon dung
				Customer gsnppCus = getCusInList(nvbhRealSeq.get(0), gsnppRealSeq);
				if (gsnppCus != null) {
					gsnppCus.status = STATUS.RIGHT_PLAN;
				}
				numRightPlan++;
			}

			for (int i = 0; i < gsnppRealSeq.size(); i++) {
				Customer cus = gsnppRealSeq.get(i);
				if (getCusInList(cus, nvbhRealSeq) == null) {
					cus.status = STATUS.GSNPP_VISITED_BUT_NVBH;
					numGsVisitedButNvbh++;
				}
			}

			numWrongPlan = numNvbhVisitedStore + numGsVisitedButNvbh - numRightPlan;
		}

		/**
		 * lay cusId lien truoc trong ds
		 * 
		 * @author: TamPQ
		 * @return: ArrayList<String>
		 * @throws:
		 */
		public long previousCusOf(Customer cus, ArrayList<Customer> list) {
			long pre_Id = -1;
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i).cusId == cus.cusId) {
					pre_Id = list.get(i - 1).cusId;
					break;
				}
			}
			return pre_Id;
		}

		/**
		 * getCusInList
		 * 
		 * @author: TamPQ
		 * @return: ArrayList<String>
		 * @throws:
		 */
		public Customer getCusInList(Customer cus, ArrayList<Customer> list) {
			Customer cust = null;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).cusId == cus.cusId) {
					return list.get(i);
				}
			}
			return cust;
		}

		/**
		 * init List Customer In Plan
		 * 
		 * @author: TamPQ
		 * @return: ArrayList<Customer>
		 * @throws:
		 */
		public ArrayList<Customer> initListCustomer(String[] arrCus, String[] arrSeq, String[] lat, String[] lng,
				String[] arrStartTime, String[] arrEndTime, String[] arrCusCodeName, String[] arrCusAdd, int mode) {
			ArrayList<Customer> tempList = new ArrayList<Customer>();
			if (arrCus[0] != "") {
				for (int i = 0; i < arrCus.length; i++) {
					Customer cus = new Customer(STATUS.NONE_VISIT);
					cus.realSeq = i;
					cus.cusId = Long.parseLong(arrCus[i]);
					if (arrSeq != null && i < arrSeq.length) {
						cus.seqInPlan = Integer.parseInt(arrSeq[i]);
					}
					if (lat != null && i < lat.length) {
						cus.lat = Double.parseDouble(lat[i]);
					}
					if (lng != null && i < lng.length) {
						cus.lng = Double.parseDouble(lng[i]);
					}
					if (arrStartTime != null && i < arrStartTime.length) {
						cus.visit_start_time = arrStartTime[i];
						cus.visit_start_time_2 = DateUtils.parseDateFromString(cus.visit_start_time,
								DateUtils.defaultDateFormat_2);
						cus.visit_start_time = DateUtils.convertDateTimeWithFormat(cus.visit_start_time_2,
								DateUtils.DATE_FORMAT_HOUR_MINUTE);
					}
					if (arrEndTime != null && i < arrEndTime.length) {
						cus.visit_end_time = arrEndTime[i];
						cus.visit_end_time_2 = DateUtils.parseDateFromString(cus.visit_end_time,
								DateUtils.defaultDateFormat_2);
						cus.visit_end_time = DateUtils.convertDateTimeWithFormat(cus.visit_end_time_2,
								DateUtils.DATE_FORMAT_HOUR_MINUTE);
					}
					if (arrCusCodeName != null && i < arrCusCodeName.length) {
						cus.codeName = arrCusCodeName[i];
					}
					if (arrCusAdd != null && i < arrCusAdd.length) {
						cus.address = arrCusAdd[i];
					}
					tempList.add(cus);
				}
				try {	
					if (DateUtils.isIn30Min(tempList.get(tempList.size() - 1).visit_start_time_2)) {
						if(mode == 0){
							if(StringUtil.isNullOrEmpty(tempList.get(tempList.size() - 1).visit_end_time)){
								tempList.get(tempList.size() - 1).isVisting = true;
							}
						}else{
							tempList.get(tempList.size() - 1).isVisting = true;
						}

					}
				} catch (ParseException e) {
				}

			}
			return tempList;
		}

		/**
		 * Mo ta muc dich cua ham
		 * @author: TamPQ
		 * @return0.
		 * 
		 * @return: Stringvoid
		 * @throws:
		 */
		public int getRealSequence(long idVistingKH, ArrayList<Customer> list) {
			int seq = -1;
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).cusId == idVistingKH) {
					seq = list.get(i).realSeq + 1;
					break;
				}
			}
			return seq;
		}

		/**
		 * get NVBH Visit Time
		 * @author: TamPQ
		 * @param cusId
		 * @return
		 * @return: Stringvoid
		 * @throws:
		 */
		public String getNVBHVisitTime(int cusId, ArrayList<Customer> list) {
			String time = "";
			for (int i = 0; i < list.size(); i++) {
				if (cusId == list.get(i).cusId) {
					time = list.get(i).visit_start_time;
					break;
				}
			}
			return time;
		}

		public Customer getCusFromRealSequence(int index, ArrayList<Customer> list) {
			for (int i = 0; i < list.size(); i++) {
				if (index == list.get(i).realSeq) {
					return list.get(i);
				}
			}
			return null;
		}

		/**
		 * init danh sach GSBH - MH theo doi vi tri NVBH, GSBH, TTTT role GST
		 * 
		 * @author: YenNTH
		 * @param c
		 * @return: voidvoid
		 * @throws:
		 */
		public void initItem(Cursor c) {
			try {
				if (c == null) {
					throw new Exception("Cursor is empty");
				}
				if (c.getColumnIndex("GS_STAFF_ID") >= 0) {
					staffIdGS = c.getString(c.getColumnIndex("GS_STAFF_ID"));
				}
				if (c.getColumnIndex("GS_STAFF_CODE") >= 0) {
					staffCodeGS = c.getString(c.getColumnIndex("GS_STAFF_CODE"));
				}
				if (c.getColumnIndex("GS_STAFF_NAME") >= 0) {
					staffNameGS = c.getString(c.getColumnIndex("GS_STAFF_NAME"));
				}
				if (c.getColumnIndex("GS_MOBILEPHONE") >= 0) {
					gsnppMobile = c.getString(c.getColumnIndex("GS_MOBILEPHONE"));
				}
				if (c.getColumnIndex("GS_PHONE") >= 0) {
					gsnppPhone = c.getString(c.getColumnIndex("GS_PHONE"));
				}
				if (c.getColumnIndex("NVBH_SHOP_NAME") >= 0) {
					shopName = c.getString(c.getColumnIndex("NVBH_SHOP_NAME"));
				}
				if (c.getColumnIndex("NVBH_SHOP_CODE") >= 0) {
					shopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
				}
				if (c.getColumnIndex("NVBH_SHOP_ID") >= 0) {
					shopId = c.getInt(c.getColumnIndex("NVBH_SHOP_ID"));
				}
				if (StringUtil.isNullOrEmpty(gsnppMobile)) {
					gsnppMobile = gsnppPhone;
				}
				if (StringUtil.isNullOrEmpty(gsnppMobile)) {
					gsnppMobile = Constants.STR_BLANK;
				}
				if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LAT) >= 0) {
					lat = c.getDouble(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LAT));
				}
				if (c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LNG) >= 0) {
					lng = c.getDouble(c.getColumnIndex(STAFF_POSITION_LOG_TABLE.LNG));
				}
				if (c.getColumnIndex("DATE_TIME") >= 0) {
					updateTime = c.getString(c.getColumnIndex("DATE_TIME"));
				}
				if(c.getColumnIndex("SHOP_ID")>=0){
					shopId = c.getInt(c.getColumnIndex("SHOP_ID"));
				}
				if(c.getColumnIndex("NVBH_STAFF_NAME") >= 0){
					staffNameNVBH = c.getString(c.getColumnIndex("NVBH_STAFF_NAME"));
				}
				if(c.getColumnIndex("NVBH_STAFF_CODE") >= 0){
					staffCodeNVBH = c.getString(c.getColumnIndex("NVBH_STAFF_CODE"));
				}else {
					staffCodeNVBH = Constants.STR_BLANK;
				}
				String start_time = null;
				String end_time = null;
				if (c.getColumnIndex("START_TIME") >= 0) {
					start_time = c.getString(c.getColumnIndex("START_TIME"));
				}
				if (c.getColumnIndex("END_TIME") >= 0) {
					end_time = c.getString(c.getColumnIndex("END_TIME"));
				}
				if (!StringUtil.isNullOrEmpty(start_time) && StringUtil.isNullOrEmpty(end_time)) {
					if (c.getColumnIndex("CUS_NAME") >= 0) {
						visitingCusName = c.getString(c.getColumnIndex("CUS_NAME"));
					}
				}
			} catch (Exception e) {
			}
		}

	}
}
