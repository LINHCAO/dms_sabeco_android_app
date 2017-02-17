/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.StaffDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.OP_SALE_VOLUME_TABLE;
import com.viettel.dms.sqllite.db.OP_STOCK_TOTAL_TABLE;
import com.viettel.dms.sqllite.db.STAFF_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.map.dto.LatLng;
import com.viettel.utils.VTLog;

/** 
 * Customer List Item class
 * CustomerListItem.java
 * @author dungdq3
 * @version: 1.0 
 * @since:  10:25:42 8 Jan 2014
 */
@SuppressWarnings("serial")
public class CustomerListItem implements Serializable {
	public String cusPlan; // tuyen ban hang
	public boolean isTodayOrdered = false;// dat hang 4 da dat hang
	public boolean isTodayVoted = false;// cham hien dien : 2 cham trong ngay
										// roi
	public boolean isTodayCheckedRemain = false;// kiem hang : 3 kiem roi
	public boolean isHaveDisplayProgramNotYetVote = false;
	public boolean isHaveSaleOrder = false; // neu co sale_order trong 2 thang
											// gan day thi se co Kiem hang ton
	public int isOr = 0; // 0: KH trong tuyen, 1: KH ngoai tuyen
	public int numUpdatePosition = 0; // so lan cap nhat vi tri cua khach hang
	public String lastUpdatePosition; // ngay cap nhat vi tri gan day
	public Double visitedLat;
	public Double visitedLng;
	public String visitStartTime;
	public String visitEndTime;
	public boolean isTooFarShop = false;
	public CustomerDTO aCustomer = new CustomerDTO();
	public StaffDTO staffSale;// nhan vien ban hang
	public String seqInDayPlan = "";
	public String currentPlan = "";// tuyen dang chon
	public String exceptionOrderDate;
	public int routingCustomerId;
	public double cusDistance = -1;
//	public int weekInterval;
//	public int startWeek;
	public String fromDate;
	public String toDate;
	public long staffCustomerId;
	public long visitActLogId;
	public boolean hasTodayTakeAttendance;
	public int totalPG;
	public long isTodayCheckedRemainCompetitor = -1;// kiem hang : 3 kiem roi
	public long isTodayCheckedSaleCompetitor = -1;
	public double shopDistance;
	public String shopName;

	// public int visitPlanId;

	public enum VISIT_STATUS {
		NONE_VISIT, VISITED_FINISHED, VISITING, VISITED_CLOSED
	}

	public VISIT_STATUS visitStatus = VISIT_STATUS.NONE_VISIT;

	public void initDataFromCursor(Cursor c, double shopDistance, String currentPlan) throws Exception {
		this.shopDistance = shopDistance;
		try {
			this.currentPlan = currentPlan;
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			if (c.getColumnIndex("DISTANCE_ORDER") > -1) {
				double disCus = c.getDouble(c.getColumnIndex("DISTANCE_ORDER"));
				if (disCus > 0){
					this.shopDistance = disCus;
				}
			}
			if (c.getColumnIndex("CUSTOMER_CODE") > -1) {
				aCustomer.customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			}
			if (c.getColumnIndex("SHORT_CODE") > -1) {
				aCustomer.shortCode = c.getString(c.getColumnIndex("SHORT_CODE"));
				if(StringUtil.isNullOrEmpty(aCustomer.shortCode)){
					aCustomer.shortCode = "";
				}
			}
			if (c.getColumnIndex("CUSTOMER_ID") > -1) {
				aCustomer.customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
			}
			if (c.getColumnIndex("CUSTOMER_NAME") > -1) {
				aCustomer.customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			}
			if (c.getColumnIndex("MOBIPHONE") > -1) {
				aCustomer.mobilePhone = c.getString(c.getColumnIndex("MOBIPHONE"));
				if (aCustomer.mobilePhone == null) {
					aCustomer.mobilePhone = "";
				}
			}
			if (c.getColumnIndex("PHONE") > -1) {
				aCustomer.phone = c.getString(c.getColumnIndex("PHONE"));
				if (aCustomer.phone == null) {
					aCustomer.phone = "";
				}
			}
			if (c.getColumnIndex("ADDRESS") > -1) {
				aCustomer.address = c.getString(c.getColumnIndex("ADDRESS"));
			}
			if (!StringUtil.isNullOrEmpty(aCustomer.address)) {
				aCustomer.street = aCustomer.address;
			} else {
				if (c.getColumnIndex("STREET") > -1) {
					aCustomer.street = c.getString(c.getColumnIndex("STREET"));
				}

				if (StringUtil.isNullOrEmpty(aCustomer.street)) {
					aCustomer.street = "";
				}
			}
			if (c.getColumnIndex("LAT") > -1) {
				aCustomer.lat = c.getDouble(c.getColumnIndex("LAT"));
			}
			if (c.getColumnIndex("LNG") > -1) {
				aCustomer.lng = c.getDouble(c.getColumnIndex("LNG"));
			}
			if (c.getColumnIndex("VISITED_LAT") > -1) {
				visitedLat = c.getDouble(c.getColumnIndex("VISITED_LAT"));
			}
			if (c.getColumnIndex("VISITED_LNG") > -1) {
				visitedLng = c.getDouble(c.getColumnIndex("VISITED_LNG"));
			}
			if (c.getColumnIndex("CUS_PLAN") > -1) {
				cusPlan = c.getString(c.getColumnIndex("CUS_PLAN"));
				if (!StringUtil.isNullOrEmpty(cusPlan) && cusPlan.substring(0, 1).equals(",")) {
					cusPlan = cusPlan.substring(1, cusPlan.length());
				}
			}
			if (c.getColumnIndex("START_TIME") > -1) {
				visitStartTime = c.getString(c.getColumnIndex("START_TIME"));
			}
			if (c.getColumnIndex("END_TIME") > -1) {
				visitEndTime = c.getString(c.getColumnIndex("END_TIME"));
			}
			String visited = null;
			if (c.getColumnIndex("VISIT") > -1) {
				visited = c.getString(c.getColumnIndex("VISIT")) == null ? "" : c.getString(c.getColumnIndex("VISIT"));
			}
			if (visited.equals("0") && !StringUtil.isNullOrEmpty(visitEndTime)) {
				visitStatus = VISIT_STATUS.VISITED_FINISHED;
			} else if (visited.equals("0") && StringUtil.isNullOrEmpty(visitEndTime)) {
				visitStatus = VISIT_STATUS.VISITING;
			} else if (visited.equals("1")) {
				visitStatus = VISIT_STATUS.VISITED_CLOSED;
			}
			int numVoteDisPro = 0;
			if (c.getColumnIndex("ACTION") > -1) {
				String action = c.getString(c.getColumnIndex("ACTION")) == null ? "" : c.getString(c
						.getColumnIndex("ACTION"));
				if (action.indexOf('2') > -1) {
					isTodayVoted = true;
				}
				numVoteDisPro = StringUtil.countCharInString('2', action);
				if (action.indexOf('3') > -1) {
					isTodayCheckedRemain = true;
				}
				if (action.indexOf('4') > -1) {
					isTodayOrdered = true;
				}
			}
			if (c.getColumnIndex("NUM_DISPLAY_PROGRAM_ID") > -1) {
				int numDisProId = c.getInt(c.getColumnIndex("NUM_DISPLAY_PROGRAM_ID"));
				if (numVoteDisPro < numDisProId) {
					isHaveDisplayProgramNotYetVote = true;
				}
			}
			if (c.getColumnIndex("SALE_ORDER_ID") > -1) {
				String saleOrderId = c.getString(c.getColumnIndex("SALE_ORDER_ID"));
				if (!StringUtil.isNullOrEmpty(saleOrderId)) {
					isHaveSaleOrder = true;
				}
			}
			isOr = checkInVisitPlan();
			if (isOr == 0 && c.getColumnIndex("DAY_SEQ") >= 0) {
				seqInDayPlan = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("DAY_SEQ"))) ? "0"
						: c.getString(c.getColumnIndexOrThrow("DAY_SEQ"));
			}else{
				seqInDayPlan="0";
			}
			updateCustomerDistance();
			if (c.getColumnIndex("EXCEPTION_ORDER_DATE") > -1) {
				exceptionOrderDate = c.getString(c.getColumnIndex("EXCEPTION_ORDER_DATE"));
			}
			if (c.getColumnIndex("VISIT_PLAN_ID") > -1) {
				routingCustomerId = c.getInt(c.getColumnIndex("VISIT_PLAN_ID"));
			}
//			if (c.getColumnIndex("WEEK_INTERVAL") > -1) {
//				weekInterval = c.getInt(c.getColumnIndex("WEEK_INTERVAL"));
//			}
//			if (c.getColumnIndex("START_WEEK") > -1) {
//				startWeek = c.getInt(c.getColumnIndex("START_WEEK"));
//			}
			if (c.getColumnIndex("FROM_DATE") > -1) {
				fromDate = c.getString(c.getColumnIndex("FROM_DATE"));
			}
			if (c.getColumnIndex("TO_DATE") > -1) {
				toDate = c.getString(c.getColumnIndex("TO_DATE"));
			}
			if (c.getColumnIndex("STAFF_CUSTOMER_ID") > -1) {
				staffCustomerId = c.getInt(c.getColumnIndex("STAFF_CUSTOMER_ID"));
			}
			if (c.getColumnIndex("CHANNEL_TYPE_ID") > -1) {
				aCustomer.channelTypeId = c.getInt(c.getColumnIndex("CHANNEL_TYPE_ID"));
			}
			if (c.getColumnIndex(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID) >= 0) {
				isTodayCheckedSaleCompetitor= c.getLong(c.getColumnIndex(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID));
			} else {
				isTodayCheckedSaleCompetitor = -1;
			}
			if (c.getColumnIndex(OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_ID) >= 0) {
				isTodayCheckedRemainCompetitor=c.getLong(c.getColumnIndex(OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_ID));
			} else {
				isTodayCheckedRemainCompetitor = -1;
			}

			totalPG=c.getInt(c.getColumnIndex("SUM_PG"));
			if (c.getColumnIndex("TOTAL_PG") > -1) {
				hasTodayTakeAttendance = StringUtil.getLongFromSQliteCursor(c, "TOTAL_PG") == totalPG ? true : false;
			}
			this.visitActLogId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow("VISIT_ACT_LOG_ID")))) ? "0" : c.getString(c
					.getColumnIndexOrThrow("VISIT_ACT_LOG_ID")));

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}
	
	public void initDataCustomerOfC2FromCursor(Cursor c) throws Exception {
		try {
			//this.currentPlan = currentPlan;
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			if (c.getColumnIndex("CUSTOMER_CODE") > -1) {
				aCustomer.customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
				if (aCustomer.customerCode == null) {
					aCustomer.customerCode = "";
				}
			}
			if (c.getColumnIndex("CUSTOMER_ID") > -1) {
				aCustomer.customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
			}
			if (c.getColumnIndex("CUSTOMER_NAME") > -1) {
				aCustomer.customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
				if (aCustomer.customerName  == null) {
					aCustomer.customerName  = "";
				}
			}
			if (c.getColumnIndex("MOBIPHONE") > -1) {
				aCustomer.mobilePhone = c.getString(c.getColumnIndex("MOBIPHONE"));
				if (aCustomer.mobilePhone == null) {
					aCustomer.mobilePhone = "";
				}
			}
			if (c.getColumnIndex("PHONE") > -1) {
				aCustomer.phone = c.getString(c.getColumnIndex("PHONE"));
				if (aCustomer.phone == null) {
					aCustomer.phone = "";
				}
			}
			if (c.getColumnIndex("ADDRESS") > -1) {
				aCustomer.address = c.getString(c.getColumnIndex("ADDRESS"));
			}
			if (!StringUtil.isNullOrEmpty(aCustomer.address)) {
				aCustomer.street = aCustomer.address;
			} else {
				if (c.getColumnIndex("STREET") > -1) {
					aCustomer.street = c.getString(c.getColumnIndex("STREET"));
				}

				if (StringUtil.isNullOrEmpty(aCustomer.street)) {
					aCustomer.street = "";
				}
			}
			if (c.getColumnIndex("LAT") > -1) {
				aCustomer.lat = c.getDouble(c.getColumnIndex("LAT"));
			}
			if (c.getColumnIndex("LNG") > -1) {
				aCustomer.lng = c.getDouble(c.getColumnIndex("LNG"));
			}
			if (c.getColumnIndex("VISITED_LAT") > -1) {
				visitedLat = c.getDouble(c.getColumnIndex("VISITED_LAT"));
			}
			if (c.getColumnIndex("VISITED_LNG") > -1) {
				visitedLng = c.getDouble(c.getColumnIndex("VISITED_LNG"));
			}
			if (c.getColumnIndex("SALE_ORDER_ID") > -1) {
				String saleOrderId = c.getString(c.getColumnIndex("SALE_ORDER_ID"));
				if (!StringUtil.isNullOrEmpty(saleOrderId)) {
					isHaveSaleOrder = true;
				}
			}
			
			
			if(c.getColumnIndex("HAS_TODAY_ORDERED") > -1) {
				isTodayOrdered  = StringUtil.getLongFromSQliteCursor(c, "HAS_TODAY_ORDERED") > 0 ? true : false;
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	/**
	 * Cập nhật khoảng cách khách hàng
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @param: double shopDistance
	 * @throws:
	 */
	public void updateCustomerDistance() {
		//reset isTooFarShop before re-check distance
		isTooFarShop = false;

		LatLng myLatLng = new LatLng(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude(), GlobalInfo
				.getInstance().getProfile().getMyGPSInfo().getLongtitude());
		LatLng cusLatLng = new LatLng(aCustomer.getLat(), aCustomer.getLng());
		if (myLatLng.lat > 0 && myLatLng.lng > 0 && cusLatLng.lat > 0 && cusLatLng.lng > 0) {
			cusDistance = GlobalUtil.getDistanceBetween(myLatLng, cusLatLng);
			if (cusDistance > this.shopDistance) {
				isTooFarShop = true;
			}
		} else {
			cusDistance = -1;
			isTooFarShop = true;
		}
		//neu hoat dong che do cho phep ko ket noi thi ko check k.c ghe tham
		if (GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_OFFLINE) {
			isTooFarShop = false;
		}
		//no_log
		if(GlobalInfo.getInstance().IS_EMULATOR){
			isTooFarShop = false; //mở comment dong nay de cho ghe tham dat hang
		}
	}

	/**
	 * Kiem tra co duoc dat hang ngoai tuyen ko
	 * @author: dungdq3
	 * @return: boolean
	 */
	public boolean canOrderException() {
		boolean canOrderException = false;
		if(!StringUtil.isNullOrEmpty(exceptionOrderDate)){
			int compare=DateUtils.isCompareWithToDate(exceptionOrderDate);
			if(compare==0){
				canOrderException = true;
			}
		}
		return canOrderException;
	}

	private int checkInVisitPlan() {
		int isOr;
		if (cusPlan.indexOf(DateUtils.getCurrentLine()) != -1) {
			isOr = 0; // trong tuyen
		} else {
			isOr = 1; // ngoai tuyen
		}
		return isOr;
	}

	public boolean isVisit() {
		return visitStatus != VISIT_STATUS.NONE_VISIT;
	}

	/**
	 * Parse thong tin khach hang cua mot nhan vien ban hang trong tuyen Su dung
	 * cho NVGS NPP giam sat vi tri khach hang
	 * 
	 * @author banghn
	 * @throws Exception
	 */
	public void initDataCustomerSaleItem(Cursor c) throws Exception {
		if (c == null) {
			throw new Exception("Cursor is empty");
		}
		this.staffSale = new StaffDTO();
		this.aCustomer.setCustomerCode(StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE)));
		this.aCustomer.setCustomerName(StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME)));
		this.aCustomer.setCustomerId(Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_ID)))) ? "0" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_ID))));
		this.aCustomer.address = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("ADDRESS"))) ? "" : c
				.getString(c.getColumnIndexOrThrow("ADDRESS"));
		this.aCustomer.setStreet(StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("STREET"))) ? "" : c
				.getString(c.getColumnIndexOrThrow("STREET")));
		if (StringUtil.isNullOrEmpty(this.aCustomer.address)) {
			this.aCustomer.address = this.aCustomer.street;
		}
		this.staffSale.staffId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_ID)))) ? "0" : c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_ID)));
		this.staffSale.staffCode = StringUtil
				.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_CODE))) ? "" : c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_CODE));
		this.staffSale.name = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_NAME))) ? ""
				: c.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_NAME));
		this.aCustomer.setLat(c.getDouble(c.getColumnIndexOrThrow("LAT")));
		this.aCustomer.setLng(c.getDouble(c.getColumnIndexOrThrow("LNG")));
		this.numUpdatePosition = c.getInt(c.getColumnIndexOrThrow("NUM_UPDATE"));
		this.lastUpdatePosition = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("LAST_UPDATE"))) ? ""
				: c.getString(c.getColumnIndexOrThrow("LAST_UPDATE"));
		this.staffCustomerId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow("STAFF_CUSTOMER_ID")))) ? "0" : c.getString(c
				.getColumnIndexOrThrow("STAFF_CUSTOMER_ID")));

		this.exceptionOrderDate = StringUtil
				.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("EXCEPTION_ORDER_DATE"))) ? "" : c.getString(c
				.getColumnIndexOrThrow("EXCEPTION_ORDER_DATE"));
	}

	/**
	 * Khoi tạo dữ liệu từ khách hàng ko psds
	 * 
	 * @author: TamPQ
	 * @param: Cursor c
	 * @return: void
	 * @throws:
	 */
	public void initDataFromNoPSDS(Cursor c) {
		this.aCustomer.setCustomerCode(StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE)));
		this.aCustomer.setCustomerName(StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME)));
		this.aCustomer.setCustomerId(Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_ID)))) ? "0" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_ID))));
		this.aCustomer.setStreet(StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("ADDRESS"))) ? "" : c
				.getString(c.getColumnIndexOrThrow("ADDRESS")));

	}

	/**
	 * Khoi tao danh sách khách hàng màn hình huấn luyện
	 * @param c
	 * @throws Exception
     */
	public void initDataCustomerSaleItemTraining(Cursor c, double distance) throws Exception {
		if (c == null) {
			throw new Exception("Cursor is empty");
		}
		this.staffSale = new StaffDTO();
		this.shopDistance = distance;
		this.aCustomer.setCustomerCode(StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE)));
		this.aCustomer.shortCode= (StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.SHORT_CODE))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.SHORT_CODE)));
		this.aCustomer.setCustomerName(StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME))) ? "" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME)));
		this.aCustomer.setCustomerId(Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_ID)))) ? "0" : c.getString(c
				.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_ID))));
		this.aCustomer.address = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("ADDRESS"))) ? "" : c
				.getString(c.getColumnIndexOrThrow("ADDRESS"));
		if (!StringUtil.isNullOrEmpty(aCustomer.address)) {
			aCustomer.street = aCustomer.address;
		} else {
			if (c.getColumnIndex("STREET") > -1) {
				aCustomer.street = c.getString(c.getColumnIndex("STREET"));
			}

			if (StringUtil.isNullOrEmpty(aCustomer.street)) {
				aCustomer.street = "";
			}
		}
		this.staffSale.staffId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_ID)))) ? "0" : c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_ID)));
		this.staffSale.staffCode = StringUtil
				.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_CODE))) ? "" : c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_CODE));
		this.staffSale.name = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_NAME))) ? ""
				: c.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_NAME));
		this.aCustomer.setLat(c.getDouble(c.getColumnIndexOrThrow("LAT")));
		this.aCustomer.setLng(c.getDouble(c.getColumnIndexOrThrow("LNG")));
		this.numUpdatePosition = c.getInt(c.getColumnIndexOrThrow("NUM_UPDATE"));
		this.lastUpdatePosition = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("LAST_UPDATE"))) ? ""
				: c.getString(c.getColumnIndexOrThrow("LAST_UPDATE"));
		this.staffCustomerId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow("STAFF_CUSTOMER_ID")))) ? "0" : c.getString(c
				.getColumnIndexOrThrow("STAFF_CUSTOMER_ID")));

		this.exceptionOrderDate = StringUtil
				.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("EXCEPTION_ORDER_DATE"))) ? "" : c.getString(c
				.getColumnIndexOrThrow("EXCEPTION_ORDER_DATE"));

		if (c.getColumnIndex("TUYEN") > -1) {
			cusPlan = c.getString(c.getColumnIndex("TUYEN"));
			if (!StringUtil.isNullOrEmpty(cusPlan) && cusPlan.substring(0, 1).equals(",")) {
				cusPlan = cusPlan.substring(1, cusPlan.length());
			}
		}
		isOr = checkInVisitPlan();
		if (isOr == 0 && c.getColumnIndex("DAY_SEQ") >= 0) {
			seqInDayPlan = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("DAY_SEQ"))) ? "0"
					: c.getString(c.getColumnIndexOrThrow("DAY_SEQ"));
		}else{
			seqInDayPlan="0";
		}
		updateCustomerDistance();
		if (c.getColumnIndex("START_TIME") > -1) {
			visitStartTime = c.getString(c.getColumnIndex("START_TIME"));
		}
		if (c.getColumnIndex("END_TIME") > -1) {
			visitEndTime = c.getString(c.getColumnIndex("END_TIME"));
		}
		String visited = null;
		if (c.getColumnIndex("VISIT") > -1) {
			visited = c.getString(c.getColumnIndex("VISIT")) == null ? "" : c.getString(c.getColumnIndex("VISIT"));
		}
		if (visited.equals("0") && !StringUtil.isNullOrEmpty(visitEndTime)) {
			visitStatus = VISIT_STATUS.VISITED_FINISHED;
		} else if (visited.equals("0") && StringUtil.isNullOrEmpty(visitEndTime)) {
			visitStatus = VISIT_STATUS.VISITING;
		} else if (visited.equals("1")) {
			visitStatus = VISIT_STATUS.VISITED_CLOSED;
		}
		this.shopName = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("SHOP_NAME"))) ? "" : c
				.getString(c.getColumnIndexOrThrow("SHOP_NAME"));
		this.visitActLogId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow("VISIT_ACT_LOG_ID")))) ? "0" : c.getString(c
				.getColumnIndexOrThrow("VISIT_ACT_LOG_ID")));
	}
}
