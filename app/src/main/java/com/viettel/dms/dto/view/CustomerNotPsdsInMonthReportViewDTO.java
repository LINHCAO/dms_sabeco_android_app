/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;

/**
 * 
 * dto dung cho man hinh Khach hang chua PSDS cua TBHV
 * 
 * @author: Nguyen Huu Hieu
 * @version: 1.1
 * @since: 1.0
 */
@SuppressLint("SimpleDateFormat")
public class CustomerNotPsdsInMonthReportViewDTO {
	public int totalList  = -1;
	public String totalofTotal;
	

	private List<Calendar> listExcepTionDay;
	public ArrayList<CustomerNotPsdsInMonthReportItem> arrList;

	/**
	 * Set list String ExcepTion Day -> to list Calendar ExcepTion Day 
	 * @author: duongdt3
	 * @since: 13:39:41 23 Nov 2013
	 * @return: void
	 * @throws:  
	 * @param listExcepTionDayString
	 */
	public void setListExcepTionDay(List<String> listExcepTionDayString) {

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

		for (int i = 0; i < listExcepTionDayString.size(); i++) {
			try {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(format.parse(listExcepTionDayString.get(i)));
				listExcepTionDay.add(calendar);
			} catch (Exception ex) {

			}
		}
	}
	
	public CustomerNotPsdsInMonthReportViewDTO() {
		listExcepTionDay = new ArrayList<Calendar>();
		arrList = new ArrayList<CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem>();
	}

	public void addItem(Cursor c) {
		CustomerNotPsdsInMonthReportItem item = new CustomerNotPsdsInMonthReportItem();
		item.initCursor(c, listExcepTionDay);
		arrList.add(item);
	}

	public static class CustomerNotPsdsInMonthReportItem {
		public String address;
		public String customerId;
		public String customerCode;
		public String customerName;
		public String rtCustomerEndDate;
		public String rtCustomerStartDate;
		public String lastOrderDate;
		public String staffId;
		public String staffName;
		public String staffCode;
		public int visitNumberInMonth;
		public long amountInLastMonth;
		public String listRoutingDate;
		public int isLossDistribution;
		public int currentWeek;
		//week
		public int week1;
		public int week2;
		public int week3;
		public int week4;
		
		//auto cal from currentWeek
		public int visitNumberInMonthPlan;
				
		public CustomerNotPsdsInMonthReportItem() {
			address = "";
			customerId= "";
			customerCode= "";
			customerName= "";
			rtCustomerEndDate = "";
			rtCustomerStartDate = "";
			lastOrderDate= "";
			staffId= "";
			staffName= "";
			staffCode= "";
			visitNumberInMonth= 0;
			amountInLastMonth= 0;
			listRoutingDate= "";
			isLossDistribution= 0;
			currentWeek= 0;
			
			//cal
			visitNumberInMonthPlan = 0;
		}
		
		public void initCursor(Cursor c, List<Calendar> listExcepTionDay){	
			address = StringUtil.getStringFromSQliteCursor(c, "ADDRESS");
			customerId= StringUtil.getLongFromSQliteCursor(c, "CUSTOMER_ID") + "";
			customerCode= StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_CODE");			
			customerName= StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_NAME");
			
			rtCustomerEndDate = StringUtil.getStringFromSQliteCursor(c, "RT_CUS_END_DATE");
			rtCustomerStartDate = StringUtil.getStringFromSQliteCursor(c, "RT_CUS_START_DATE");
			
			lastOrderDate= StringUtil.getStringFromSQliteCursor(c, "LAST_ORDER_DATE");
			staffId= StringUtil.getStringFromSQliteCursor(c, "STAFF_ID");
			staffName= StringUtil.getStringFromSQliteCursor(c, "STAFF_NAME");
			staffCode= StringUtil.getStringFromSQliteCursor(c, "STAFF_CODE");
			visitNumberInMonth = (int) StringUtil.getLongFromSQliteCursor(c, "VISIT_NUMBER_IN_MONTH");
			amountInLastMonth = StringUtil.getLongFromSQliteCursor(c, "AMOUNT_IN_LAST_MONTH");
			listRoutingDate= StringUtil.getStringFromSQliteCursor(c, "LIST_ROUTING_DATE");
			//bo dau phay dau tien
			listRoutingDate = StringUtil.removeFirstComma(listRoutingDate);
			
			isLossDistribution = (int) StringUtil.getLongFromSQliteCursor(c, "IS_LOSS_DISTRIBUTION");
			currentWeek = (int) StringUtil.getLongFromSQliteCursor(c, "CURRENT_WEEK");
			week1 = (int) StringUtil.getLongFromSQliteCursor(c, "WEEK1");
			week2 = (int) StringUtil.getLongFromSQliteCursor(c, "WEEK2");
			week3 = (int) StringUtil.getLongFromSQliteCursor(c, "WEEK3");
			week4 = (int) StringUtil.getLongFromSQliteCursor(c, "WEEK4");
			
			//cal visitNumberInMonthPlan
			visitNumberInMonthPlan = calNumberTimesVisitPlanInMonth(listExcepTionDay, listRoutingDate, currentWeek);
			
		}
		
		public boolean checkDayOff(List<Calendar> listExcepTionDay, int day, int month, int year) {
			boolean result = false;
			for (int i = 0; i < listExcepTionDay.size(); i++) {
				Calendar calendar = listExcepTionDay.get(i);
				int dayOff = calendar.get(Calendar.DAY_OF_MONTH);
				int monthOff = calendar.get(Calendar.MONTH);
				int yearOff = calendar.get(Calendar.YEAR);
				
				if (dayOff == day && monthOff == month && yearOff == year) {
					result = true;
				}
			}
			return result;
		}

		/**
		 * Tính toán kế hoạch ghé thăm KH
		 * @author: duongdt3
		 * @param listExcepTionDay 
		 * @since: 14:48:32 23 Nov 2013
		 * @return: int
		 * @throws:  
		 * @param listRoutingDate
		 * @param currentWeek
		 * @return
		 */
		private int calNumberTimesVisitPlanInMonth(List<Calendar> listExcepTionDay, String listRoutingDate, int currentWeek) {
			
			// lay danh sach cac thu can di trong tuyen
			int T2 = listRoutingDate.contains("T2") ? 1 : 0;
			int T3 = listRoutingDate.contains("T3") ? 1 : 0;
			int T4 = listRoutingDate.contains("T4") ? 1 : 0;
			int T5 = listRoutingDate.contains("T5") ? 1 : 0;
			int T6 = listRoutingDate.contains("T6") ? 1 : 0;
			int T7 = listRoutingDate.contains("T7") ? 1 : 0;
			int CN = listRoutingDate.contains("CN") ? 1 : 0;

			//1. Tính số lượng ngày phải viếng thăm
 			// tinh so ngay trong thang hien tai
			Calendar calendarNow = Calendar.getInstance();
			int month = calendarNow.get(Calendar.MONTH);
			int year = calendarNow.get(Calendar.YEAR);
			//truong hop du ca thang
			int beginDay = 1;
			int endDay = calendarNow.getActualMaximum(Calendar.DATE);
			
			//truong hop rt_customer_start_date nam trong thang hien tai, tuc la ko lay het so ngay trong thang duoc
			if (!StringUtil.isNullOrEmpty(rtCustomerStartDate)) {
				Calendar calendarFirstDateCurrentMonth = DateUtils.getCalendarNotTime(null);
				//ve dau thang
				calendarFirstDateCurrentMonth.set(Calendar.DAY_OF_MONTH, 1);
				
				//ngay RT CUS START
				Date dateStart = DateUtils.parseDateFromString(rtCustomerStartDate, DateUtils.DATE_FORMAT_DEFAULT);
				Calendar calendarStartDate = DateUtils.getCalendarNotTime(dateStart);
				
				//neu calendarStartDate nam trong thang hien tai, tuc la ko lay het so ngay trong thang duoc
				if(calendarFirstDateCurrentMonth.before(calendarStartDate)){
					//thay doi ngay bat dau
					beginDay =  calendarStartDate.get(Calendar.DAY_OF_MONTH);
				}
			}
			
 			//truong hop rt_customer_end_date nam trong thang hien tai, tuc la ko lay het so ngay trong thang duoc
			if (!StringUtil.isNullOrEmpty(rtCustomerEndDate)) {
				
				Calendar calendarFirstDateNextMonth = DateUtils.getCalendarNotTime(null);
				//ve dau thang
				calendarFirstDateNextMonth.set(Calendar.DAY_OF_MONTH, 1);
				//them 1 thang
				calendarFirstDateNextMonth.add(Calendar.MONTH, 1);
				
				//ngay RT CUS END
				Date dateEnd = DateUtils.parseDateFromString(rtCustomerEndDate, DateUtils.DATE_FORMAT_DEFAULT);
				Calendar calendarEndDate = DateUtils.getCalendarNotTime(dateEnd);
				
				//neu calendarEndDate nam trong thang hien tai, tuc la ko lay het so ngay trong thang duoc
				if(calendarFirstDateNextMonth.after(calendarEndDate)){
					//int days = DateUtils.daysBetween(calendarNextMonth.getTime(), calendarEndDate.getTime());
					//tinh lai num day
					//=> so ngay phai di = ngay End date, (iBeginDay - EndDate)
					endDay = calendarEndDate.get(Calendar.DAY_OF_MONTH);
				}
			}
			
			//2. Tìm tuần của ngay begin (dau thang) thang hien tai 
			//tinh khoang cach ngay cua ngay hien tai va ngay begin (dau thang)
			int dayNow = calendarNow.get(Calendar.DAY_OF_MONTH);
			int kcTuan = 0;
 			Calendar iCalendar = Calendar.getInstance();
 			for (int iDay = beginDay + 1; iDay <= dayNow; iDay++) {
 				//set ngay dang xet vao Calender
				iCalendar.set(year, month, iDay);
				int thuHienTai = iCalendar.get(Calendar.DAY_OF_WEEK);
				
				//Sang tuan moi
				if (thuHienTai ==  Calendar.MONDAY) {
					//qua tuan moi
					kcTuan++; 
				}
			}
 			
 			//tim khoang cach tuan
 			//currentWeek hien tai la 1 -> 4
 			int tuanCuaNgayDauThang = currentWeek;
 			for (int iTuan = 1; iTuan <= kcTuan; iTuan++) {
 				tuanCuaNgayDauThang--;
 				if (tuanCuaNgayDauThang == 0) {
 					tuanCuaNgayDauThang = 4;
				}
			}

 			//Tuần của ngày bắt đầu là tuần của ngày đầu tháng
			int tuanHienTai = tuanCuaNgayDauThang;
			int soLuongNgayPhaiGheTham = 0;
			
			for (int iBeginDay = beginDay; iBeginDay <= endDay; iBeginDay++) {
				//set ngay dang xet vao Calender
				iCalendar.set(year, month, iBeginDay);
				//neu khong phai ngay nghi le
				int thuHienTai = iCalendar.get(Calendar.DAY_OF_WEEK);
				
				//kiem tra tuan nay co can di KH nay hay khong, Tuan 4 = Tuan 0
				if ((tuanHienTai == 1 && week1 == 1)
						|| (tuanHienTai == 2 && week2 == 1)
						|| (tuanHienTai == 3 && week3 == 1)
						|| (tuanHienTai == 4 && week4 == 1) ) {
					//neu co di trong tuan nay
					//kiem tra co phai ngay nghi le hay khong
					if (checkDayOff(listExcepTionDay,iBeginDay,month,year) == false) {						
						//kiem tra Thu co phai di hay khong
						if (thuHienTai == Calendar.SUNDAY && CN == 1) {
							soLuongNgayPhaiGheTham++;
						}else if (thuHienTai == Calendar.MONDAY && T2 == 1) {
							soLuongNgayPhaiGheTham++;
						}else if (thuHienTai == Calendar.TUESDAY && T3 == 1) {
							soLuongNgayPhaiGheTham++;
						}else if (thuHienTai == Calendar.WEDNESDAY && T4 == 1) {
							soLuongNgayPhaiGheTham++;
						}else if (thuHienTai == Calendar.THURSDAY && T5 == 1) {
							soLuongNgayPhaiGheTham++;
						}else if (thuHienTai == Calendar.FRIDAY && T6 == 1) {
							soLuongNgayPhaiGheTham++;
						}else if (thuHienTai == Calendar.SATURDAY && T7 == 1) {
							soLuongNgayPhaiGheTham++;
						}
					}
				}
				
				//Cuoi tuan, sap sang tuan moi
				if (thuHienTai ==  Calendar.SUNDAY) {
					//qua tuan moi
					tuanHienTai++;
					//1->4
					if (tuanHienTai == 5) {
						tuanHienTai = 1;
					} 
				}
			}
			
			return soLuongNgayPhaiGheTham;
		}
	}
}
