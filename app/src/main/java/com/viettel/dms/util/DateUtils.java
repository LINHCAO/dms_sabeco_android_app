/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.SystemClock;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.GlobalInfo.RightTimeInfo;
import com.viettel.dms.view.main.LoginView;
import com.viettel.utils.VTLog;

/**
 * Chua cac ham util ve date
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint("SimpleDateFormat")
public class DateUtils {
	/** The Constant SECOND. */
	public static final long SECOND = 1000;

	/** The Constant MINUTE. */
	public static final long MINUTE = SECOND * 60;

	/** The Constant HOUR. */
	public static final long HOUR = MINUTE * 60;
	// chua kiem tra duoc thoi gian
	public final static int TIME_NOT_CHECK = -1;
	// thoi gian khong hop le
	public final static int TIME_INVALID = 0;
	// thoi gian hop le
	public final static int TIME_VALID = 1;
	
	public static final String DATE_FORMAT_DEFAULT = "dd/MM/yyyy";
	public static final String DATE_TIME_FORMAT_VN = "dd/MM/yyyy HH:mm:ss";
	public static final String DATE_FORMAT_SQL="dd/MM/yyyy HH:mm:ss";
	public static final SimpleDateFormat defaultDateFormat = new SimpleDateFormat(DATE_FORMAT_DEFAULT);
	public static final String DATE_FORMAT_NOW = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat defaultDateFormat_2 = new SimpleDateFormat(DATE_FORMAT_NOW);
	public static final String DATE_FORMAT_SQL_DEFAULT = "yyyy-MM-dd";
	public static final SimpleDateFormat defaultSqlDateFormat = new SimpleDateFormat(DATE_FORMAT_SQL_DEFAULT);
	public static final String DATE_FORMAT_ATTENDANCE = "yyyy-MM-dd HH:mm";
	public static final String DATE_FORMAT_MONTH = "yyyy-MM";
	public static final String DATE_FORMAT_HOUR_MINUTE = "HH:mm";
	public static final SimpleDateFormat defaultHourMinute = new SimpleDateFormat(DATE_FORMAT_HOUR_MINUTE);
	public static final String DATE_FORMAT_SQL_MONTH_DEFAULT = "yyyy-MM";
	public static final String DATE_FORMAT_DATE_TIME_FILE = "dd_MM_yyyy_HH_mm_ss";

	public static final long ONE_MINUTE_IN_MILLIS = 60000;// millisecs

	/**
	 * Lay thoi gian hien tai theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String now() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Lay ngay dau tien cua thang truoc
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getDayOfPreviousMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	/**
	 * Lay ngay dau tien cua thang truoc
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 *
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getDateOfPreviousMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);
		cal.set(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SQL_DEFAULT);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * lay ngay cuoi cung cua so thang truoc 
	* 
	* @author: trungnt56
	* @param: @return
	* @return: String
	* @throws:
	 */
	public static String getLastDateOfNumberPreviousMonthWithFormat(String strFormat,int numberPreviousMonth) {
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat format = null;
		if (StringUtil.isNullOrEmpty(strFormat)) {
			format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_SS");
		} else {
			format = new SimpleDateFormat(strFormat);
		}
		cal.add(Calendar.MONTH, numberPreviousMonth);
		cal.set(Calendar.DATE,Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH));
		//cal.add(Calendar.DATE, -1);
		return format.format(cal.getTime());
	}
	
	/**
	 * lay ngay dau tien cua so thang truoc 
	* 
	* @author: trungnt56
	* @param: @return
	* @return: String
	* @throws:
	 */
	public static String getFirstDateOfNumberPreviousMonthWithFormat(String strFormat,int numberPreviousMonth) {
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat format = null;
		if (StringUtil.isNullOrEmpty(strFormat)) {
			format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_SS");
		} else {
			format = new SimpleDateFormat(strFormat);
		}
		cal.add(Calendar.MONTH, numberPreviousMonth);
		cal.set(Calendar.DATE,Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
		return format.format(cal.getTime());
	}

	/**
	 * lay ngay truoc ngay hien tai
	 *
	 * @author: trungnt56
	 * @param: @return
	 * @return: String
	 * @throws:
	 */
	public static String getDateOfNumberPreviousDateWithFormat(String strFormat,int numberPreviousDay) {
		Calendar cal=Calendar.getInstance();
		SimpleDateFormat format = null;
		if (StringUtil.isNullOrEmpty(strFormat)) {
			format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_SS");
		} else {
			format = new SimpleDateFormat(strFormat);
		}
		cal.add(Calendar.DATE, numberPreviousDay);
		return format.format(cal.getTime());
	}
	
	/**
	 * Lay ngay dau tien cua thang
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getDayOfMonth() {
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	/**
	 * Lay ngay dau tien cua thang
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 *
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getDateOfMonth() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_SQL_DEFAULT);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Lay ngay dau tien cua thang
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getDayOfMonth(Date date) {
		SimpleDateFormat dfMonth = new SimpleDateFormat("MM"); 
		SimpleDateFormat dfYear = new SimpleDateFormat("yyyy");  
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1); 
		cal.set(Calendar.MONTH, Integer.parseInt(dfMonth.format(date)) - 1);
		cal.set(Calendar.YEAR, Integer.parseInt(dfYear.format(date)));
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}

	/**
	 * Lay ngay dau tien cua thang
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getLastDayOfMonth(Date date) {
		SimpleDateFormat dfMonth = new SimpleDateFormat("MM"); 
		SimpleDateFormat dfYear = new SimpleDateFormat("yyyy"); 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Integer.parseInt(dfMonth.format(date)) - 1);
		cal.set(Calendar.YEAR, Integer.parseInt(dfYear.format(date)));
		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Lay ngay cuoi cung cua thang truoc
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getLastDayOfPreviousMonth(Date date) {
		SimpleDateFormat dfMonth = new SimpleDateFormat("MM"); 
		SimpleDateFormat dfYear = new SimpleDateFormat("yyyy"); 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Integer.parseInt(dfMonth.format(date)) - 1);
		cal.set(Calendar.YEAR, Integer.parseInt(dfYear.format(date))); 
		cal.add(Calendar.DATE, -1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Lay ngay cuoi cung cua thang truoc
	 * Dinh dang theo: yyyy-MM-dd HH:mm:SS
	 * 
	 * @author : BangHN since : 12:00:32 PM
	 */
	public static String getNextDay(Date date) {
		SimpleDateFormat dfMonth = new SimpleDateFormat("MM"); 
		SimpleDateFormat dfYear = new SimpleDateFormat("yyyy"); 
		Calendar cal = Calendar.getInstance(); 
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.MONTH, Integer.parseInt(dfMonth.format(date)) - 1);
		cal.set(Calendar.YEAR, Integer.parseInt(dfYear.format(date))); 
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		return sdf.format(cal.getTime());
	}
	
	/**
	 * Convert date sang mot dinh dang truyen vao
	 * 
	 * @author : BangHN since : 1.0
	 */
	public static String convertDateTimeWithFormat(Date date, String format) {
		if(date != null){
			if (StringUtil.isNullOrEmpty(format)) {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
				return sdf.format(date);
			} else {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				return sdf.format(date);
			}
		}else{
			return null;
		}
	}

	/**
	 * Lay thoi gian theo format truyen vao
	 * 
	 * @author : BangHN since : 11:57:44 AM
	 */
	public static String getCurrentDateTimeWithFormat(String strFormat) {
		Date currentDateTime = new Date();
		SimpleDateFormat format = null;
		if (StringUtil.isNullOrEmpty(strFormat)) {
			format = new SimpleDateFormat("dd_MM_yyyy_HH_mm_SS");
		} else {
			format = new SimpleDateFormat(strFormat);
		}
		return format.format(currentDateTime);
	}

	/**
	 * getCurrentDate
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public static int getCurrentDay() {
		Date d = new Date(System.currentTimeMillis());
		Calendar.getInstance().setTime(d);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		int indexDay = -1;
		switch (day) {
		case Calendar.MONDAY:
			indexDay = 0;
			break;
		case Calendar.TUESDAY:
			indexDay = 1;
			break;
		case Calendar.WEDNESDAY:
			indexDay = 2;
			break;
		case Calendar.THURSDAY:
			indexDay = 3;
			break;
		case Calendar.FRIDAY:
			indexDay = 4;
			break;
		case Calendar.SATURDAY:
			indexDay = 5;
			break;
		case Calendar.SUNDAY:
			indexDay = 6;
			break;
		}
		return indexDay;
	}

	/**
	 * 
	 * Lay tuyen hien tai(T2-->T7)
	 * 
	 * @author : DoanDM since : 11:08:34 AM
	 */
	public static String getCurrentLine() {
		String rs = "";
		Date d = new Date(System.currentTimeMillis());
		Calendar.getInstance().setTime(d);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case Calendar.MONDAY:
			rs = Constants.DAY_LINE[0];
			break;
		case Calendar.TUESDAY:
			rs = Constants.DAY_LINE[1];
			break;
		case Calendar.WEDNESDAY:
			rs = Constants.DAY_LINE[2];
			break;
		case Calendar.THURSDAY:
			rs = Constants.DAY_LINE[3];
			break;
		case Calendar.FRIDAY:
			rs = Constants.DAY_LINE[4];
			break;
		case Calendar.SATURDAY:
			rs = Constants.DAY_LINE[5];
			break;
		case Calendar.SUNDAY:
			rs = Constants.DAY_LINE[6];
			break;
		}
		return rs;
	}

	/**
	 * getCurrentDate
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public static String getToday() {
		String today = "";
		Date d = new Date(System.currentTimeMillis());
		Calendar.getInstance().setTime(d);
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		switch (day) {
		case Calendar.MONDAY:
			today = Constants.TODAY[0];
			break;
		case Calendar.TUESDAY:
			today = Constants.TODAY[1];
			break;
		case Calendar.WEDNESDAY:
			today = Constants.TODAY[2];
			break;
		case Calendar.THURSDAY:
			today = Constants.TODAY[3];
			break;
		case Calendar.FRIDAY:
			today = Constants.TODAY[4];
			break;
		case Calendar.SATURDAY:
			today = Constants.TODAY[5];
			break;
		case Calendar.SUNDAY:
			today = Constants.TODAY[6];
			break;
		}
		return today;
	}

	/**
	 * 
	 * Lay chuoi ngay thang nam yyyy-MM-dd HH:mm:ss tu dd/MM/yyyy HH:mm
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param strDate
	 * @param strTime
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String getDateTimeStringFromDateAndTime(String strDate, String strTime) {
		String[] days = strDate.split("/");
		StringBuilder sbDateTime = new StringBuilder();

		if (days.length >= 3) {
			sbDateTime.append(days[2]);
			sbDateTime.append("-");
			sbDateTime.append(days[1]);
			sbDateTime.append("-");
			sbDateTime.append(days[0]);

			sbDateTime.append(" ");
		}

		sbDateTime.append(strTime);
		sbDateTime.append(":");
		sbDateTime.append("00");

		return sbDateTime.toString().trim();
	}

	/**
	 * Lay ngay lam viec giua 2 ngay
	 * 
	 * @author : ? since : 1.0
	 */
	public static int getWorkingDaysBetweenTwoDates(Date startDate, Date endDate) {
		SimpleDateFormat sfd = new SimpleDateFormat("yyyyMMdd");
		try {
			startDate = sfd.parse(sfd.format(startDate));
			endDate = sfd.parse(sfd.format(endDate));
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		Calendar startCal;
		Calendar endCal;
		startCal = Calendar.getInstance();
		startCal.setTime(startDate);
		endCal = Calendar.getInstance();
		endCal.setTime(endDate);
		int workDays = 1;
		// Return 0 if start and end are the same
		if (startCal.getTimeInMillis() > endCal.getTimeInMillis()) {
			startCal.setTime(endDate);
			endCal.setTime(startDate);
		}
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()
				&& startCal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
			return 0;
		}
		if (startCal.getTimeInMillis() == endCal.getTimeInMillis()
				&& startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
			return 1;
		}
		do {
			if (startCal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
				++workDays;
			}
			startCal.add(Calendar.DAY_OF_MONTH, 1);
		} while (startCal.getTimeInMillis() < endCal.getTimeInMillis());
		return workDays;
	}

	/**
	 * Lay ngay dau tien trong tahang
	 * 
	 * @author : ? since : 1.0
	 */
	public static Date getStartTimeOfMonth(Date date) {
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM");
		try {
			return sfd.parse(sfd.format(date));
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return null;
	}

	/**
	 * Lay ngay dau tien trong tahang
	 * 
	 * @author : ? since : 1.0
	 */
	public static Date getStartTimeOfDay(Date date) {
		SimpleDateFormat sfd = new SimpleDateFormat("yyyy/MM/dd");
		try {
			return sfd.parse(sfd.format(date));
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return null;
	}

	/**
	 * 
	 * compare one Date with today . -1 : strDay < today , 0: strDay = today, 1:
	 * strDay > today
	 * 
	 * @param strDay
	 * @return
	 * @return: int
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
	 */
	public static int isCompareWithToDate(String strDay) {
		int kq = 0;
		SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
		try {
			Date dayFormat = sfd.parse(strDay);
			Date toDay = sfd.parse(getCurrentDate());
			kq = dayFormat.compareTo(toDay);
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return kq;
	}

	/**
	 * kiem tra mot ngay phai la ngay chu nhat?
	 * 
	 * @author : ? since : 1.0
	 */
	public static boolean isSunday(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY;
	}

	/**
	 * Lay nam hien tai
	 * 
	 * @author : ? since : 1.0
	 */
	public static int getYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.YEAR);
	}

	/**
	 * Lay thang hien tai
	 * 
	 * @author : ? since : 1.0
	 */
	public static int getMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.MONTH);
	}
	
	/**
	 * Lay thang truoc cua thang hien tai
	 * Tra ve -1 neu thang < 0 hoac > 11
	 * @author: quangvt1
	 * @since: 15:07:13 06-05-2014
	 * @return: int
	 * @throws:  
	 * @param month
	 * @return
	 */
	public static int getMonthPrevious(Date date){
		 return getMonthPrevious(DateUtils.getMonth(date));
	}
	
	/**
	 * Lay thang truoc cua thang hien tai 
	 * @author: quangvt1
	 * @since: 15:07:13 06-05-2014
	 * @return: int
	 * @throws:  
	 * @param month = [0, 11]
	 * @return
	 */
	public static int getMonthPrevious(int month){
		month++;
		int monthPrevious = -1;
		if(month >= 1 && month <= 12){
			monthPrevious = ((month + 12) - 1) % 12;
		}
		return --monthPrevious;
	}

	/**
	 * Lay ngay hien tai
	 * 
	 * @author : ? since : 1.0
	 */
	public static int getDay(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.DAY_OF_MONTH);
	}

	public static String getDayOfWeek() {
		Calendar c = Calendar.getInstance();
		return getDayOfWeek(c.getTime());
	}
	
	public static String getDayOfWeek(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		switch (c.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.SUNDAY:
			return "Chủ nhật";
		case Calendar.MONDAY:
			return "Thứ 2";
		case Calendar.TUESDAY:
			return "Thứ 3";
		case Calendar.WEDNESDAY:
			return "Thứ 4";
		case Calendar.THURSDAY:
			return "Thứ 5";
		case Calendar.FRIDAY:
			return "Thứ 6";
		case Calendar.SATURDAY:
			return "Thứ 7";
		default:
			break;
		}
		return "";
	}

	/**
	 * Parse date tu chuoi date trong sqlLite
	 *
	 * @author: TruongHN
	 * @param dateSql
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String parseDateFromSqlLite(String dateSql) {
		String date = "";
		if (!StringUtil.isNullOrEmpty(dateSql)) {
			// 2012-08-01 14:09:21.0

			int index = dateSql.indexOf(".");
			if (index > -1) {
				date = dateSql.substring(0, index);
			} else {
				date = dateSql;
			}
		}
		return date;
	}

	/**
	 * Parse date tu chuoi date trong sqlLite
	 *
	 * @author: TruongHN
	 * @param dateSql
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static Date parseDateTypeFromSqlLite(String dateSql) {
		Date date = null;
		if (!StringUtil.isNullOrEmpty(dateSql)) {
			// 2012-08-01 14:09:21.0

			DateFormat format = new SimpleDateFormat(DATE_FORMAT_NOW, Locale.ENGLISH);
			try {
				date = format.parse(dateSql);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return date;
	}

	/**
	 * Parse date tu chuoi date trong sqlLite
	 *
	 * @author: TruongHN
	 * @param date
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String parseStringFromDate(Date date) {
		String s = null;
		DateFormat df = new SimpleDateFormat(DATE_FORMAT_NOW);
		s = df.format(date);
		return s;
	}

	/**
	 * getVisitPlan
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public static String getVisitPlan(String date) {
		String d = "";
		if (date.equals(Constants.ARRAY_LINE_CHOOSE[0])) {
			d = Constants.DAY_LINE[0];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[1])) {
			d = Constants.DAY_LINE[1];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[2])) {
			d = Constants.DAY_LINE[2];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[3])) {
			d = Constants.DAY_LINE[3];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[4])) {
			d = Constants.DAY_LINE[4];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[5])) {
			d = Constants.DAY_LINE[5];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[6])) {
			d = Constants.DAY_LINE[6];
		} else if (date.equals(Constants.ARRAY_LINE_CHOOSE[7])) {
			d = "";
		}
		return d;
	}

	public static String convertFormatDate(String strDate, String fromFormat, String toFormat) {
		String strToDate = strDate;
		SimpleDateFormat fromFM = new SimpleDateFormat(fromFormat);
		SimpleDateFormat toFM = new SimpleDateFormat(toFormat);
		try {
			Date dateFrom = fromFM.parse(strDate);
			strToDate = toFM.format(dateFrom);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return strToDate;
	}

	/**
	 * Kiem tra thoi gian hop le cua client voi server
	 * 
	 * @author BangHN
	 * @param createTime
	 * @return
	 */
	public static int checkTimeClientAndServer(String strServerDate) {
		// kiem tra thoi gian hop le khong
		SimpleDateFormat formatterDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");

		Date dateTimeServer;
		Date dateTimeClient;
		int validTime = TIME_NOT_CHECK; // mac dinh la chua check duoc

		try {
			if (!StringUtil.isNullOrEmpty(strServerDate)) {
				// thoi gian hien tai
				String currentDateClient = DateUtils.now();

				dateTimeServer = formatterDateTime.parse(strServerDate);
				dateTimeClient = formatterDateTime.parse(currentDateClient);

				Date dateServer = formatterDate.parse(strServerDate);
				Date dateClient = formatterDate.parse(currentDateClient);

				long secs = (dateTimeServer.getTime() - dateTimeClient.getTime()) / 1000;
				int hours = (int) secs / 3600;

				// neu gio cach biet nhau 1 h -> fail
				// neu ngay khac ngay hien tai -> fail
				if (Math.abs(hours) >= GlobalInfo.getInstance().getTimeTestOrder()
						|| dateClient.compareTo(dateServer) != 0) {
					// thoi gian khong hop le
					validTime = TIME_INVALID;
				} else {
					// thoi gian hop le
					validTime = TIME_VALID;
				}
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}
		return validTime;
	}

	/**
	 * Check thoi gian sau khi relogin hay nhan cap nhat du lieu Neu sau mot
	 * ngay login thi yeu cau ra man hinh login de cap nhat du lieu
	 * 
	 * @author banghn
	 * @param userDTO
	 */
	public static boolean checkTimeToShowLogin(String serverDate) {
		boolean isValid = true;
		SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		String dateBefore = sharedPreferences.getString(LoginView.DMS_SV_DATE, "");

		Date dateAfterRelogin = null;
		Date dateBeforeRelogin = null;
		try {
			if (!StringUtil.isNullOrEmpty(serverDate)) {
				dateAfterRelogin = formatterDate.parse(serverDate);
			}
			if (!StringUtil.isNullOrEmpty(dateBefore)) {
				dateBeforeRelogin = formatterDate.parse(dateBefore);
			}
			if (dateAfterRelogin != null && dateBeforeRelogin != null
					&& dateAfterRelogin.compareTo(dateBeforeRelogin) != 0) {
				isValid = false;
			}
		} catch (ParseException e1) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}
		return isValid;
	}

	public static String getCurrentDate() {
		Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		return defaultDateFormat.format(cal.getTime());
	}

	/**
	 * 
	 * Lay gia tri hien tai cua 1 loai thoi gian nao do
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param timeType
	 * @return
	 * @return: int
	 * @throws:
	 */
	public static int getCurrentTimeByTimeType(int timeType) {
		int currentTime = 0;
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);

		switch (timeType) {
		case Calendar.MONTH: {
			currentTime = calendar.get(Calendar.MONTH) + 1;
			break;
		}
		case Calendar.YEAR: {
			currentTime = calendar.get(Calendar.YEAR);
			break;
		}
		case Calendar.HOUR_OF_DAY: {
			currentTime = calendar.get(Calendar.HOUR_OF_DAY);
			break;
		}
		case Calendar.MINUTE: {
			currentTime = calendar.get(Calendar.MINUTE);
			break;
		}

		default:
			break;
		}

		return currentTime;
	}

	/**
	 * 
	 * Convert 1 chuoi ngay tu 1 format sang 1 format khac
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param date
	 * @param fromFormat
	 * @param toFormat
	 * @return
	 */
	public static String convertDateOneFromFormatToAnotherFormat(String date, String fromFormat, String toFormat) {
		Date tn;
		String result = "";
		try {
			tn = StringUtil.stringToDate(date, fromFormat);
			result = StringUtil.dateToString(tn, toFormat);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return result;
	}

	/**
	 * Lay so ngay giua 2 ngay bat ky
	 * 
	 * @param d1
	 *            : ngay dau
	 * @param d2
	 *            : ngay sau
	 * @author banghn
	 * @return
	 */
	public static int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * HieuNH Compare to.
	 * 
	 * @param date1
	 *            the date1
	 * @param date2
	 *            the date2
	 * @return the int
	 */
	public static int compare(Date date1, Date date2) {

		if ((date1 != null) && (date2 == null)) {
			return -1;
		} else if ((date1 == null) && (date2 != null)) {
			return 1;
		} else if ((date1 == null) && (date2 == null)) {
			return 0;
		}

		long time1 = date1.getTime() / HOUR;
		long time2 = date2.getTime() / HOUR;

		if (time1 == time2) {
			return 0;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 1;
		}
	}

	/**
	 * HieuNH
	 * 
	 * @param formatDate
	 *            the format date
	 * @return the date
	 */
	public static Date now(String formatDate) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
		try {
			return sdf.parse(sdf.format(cal.getTime()));
		} catch (ParseException e) {
			return new Date();
		}
	}

	/*
	 * HieuNH so sanh 2 ngay
	 */
	public static int compare(String sDate1, String sDate2, String sDateFormat) {
		if ((!StringUtil.isNullOrEmpty(sDate1)) && (StringUtil.isNullOrEmpty(sDate2))) {
			return -1;
		} else if ((StringUtil.isNullOrEmpty(sDate1)) && (!StringUtil.isNullOrEmpty(sDate2))) {
			return 1;
		} else if ((StringUtil.isNullOrEmpty(sDate1)) && (StringUtil.isNullOrEmpty(sDate2))) {
			return 0;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat);
		Date date1 = null;
		Date date2 = null;
		long time1 = 0, time2 = 0;
		try {
			date1 = sdf.parse(sDate1);
			date2 = sdf.parse(sDate2);
			time1 = date1.getTime() / HOUR;
			time2 = date2.getTime() / HOUR;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		if (time1 == time2) {
			return 0;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 1;
		}
	}

	/*
	 * HieuNH so sanh ngay voi ngay hien tai
	 */
	public static int compareWithNow(String sDate1, String sDateFormat) {
		if (StringUtil.isNullOrEmpty(sDateFormat))
			return -1;
		Date now = now(sDateFormat);
		if ((StringUtil.isNullOrEmpty(sDate1))) {
			return 1;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(sDateFormat);
		Date date1 = null;
		long time1 = 0, time2 = 0;
		try {
			date1 = sdf.parse(sDate1);
			time1 = date1.getTime() / HOUR;
			time2 = now.getTime() / HOUR;
		} catch (ParseException e) {
		}
		if (time1 == time2) {
			return 0;
		} else if (time1 < time2) {
			return -1;
		} else {
			return 1;
		}
	}
	
	/**
	 * So sanh ngay thang nam voi Now
	 * @author: duongdt3
	 * @since: 08:55:49 13 Dec 2013
	 * @return: int
	 * @throws:  
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return Neu ngay > NOW => return > 0, Neu ngay < NOW => return <0 , bang nhau return = 0 
	 */
	public static int compareDateWithNow(int dayOfMonth, int monthOfYear, int year) {
		Calendar calender = Calendar.getInstance();
		calender.set(year, monthOfYear, dayOfMonth);
		Calendar calenderNow = Calendar.getInstance();
		
		Calendar cal = getCalendarNotTime(calender.getTime());
		Calendar calNow = getCalendarNotTime(calenderNow.getTime());
		
		return cal.compareTo(calNow);
	}

	/**
	 * TamPQ
	 * 
	 * @param formatDate
	 *            the format date
	 * @return the date
	 */
	public static Date parseDateFromString(String st, SimpleDateFormat format) {
		Date date = null;
		SimpleDateFormat sfs = format;
		try {
			date = sfs.parse(st);
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return date;
	}
	
    /**
     * 
     * @author: duongdt3
     * @since: 11:22:17 4 Dec 2013
     * @return: Date
     * @throws:  
     * @param st
     * @param format
     * @return
     */
	public static Date parseDateFromString(String st, String format) {
		SimpleDateFormat sfs = new SimpleDateFormat(format);
		return parseDateFromString(st, sfs);
	}

	/**
	 * Lay thu tu tuan thu may trong nam cua 1 ngay
	 * 
	 * @author: TamPQ
	 * @param fDate
	 * @return
	 * @return: intvoid
	 * @throws:
	 */
	public static int getWeekFromDate(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * get num minutes two time in date theo temple HH:mm
	 * @param strTime1
	 * @param strTime2
	 * @return: long
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public static long getDistanceMinutesFrom2Hours(String strTime1, String strTime2) {
		long distance = 0;
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		try {
			Date date1 = formatter.parse(strTime1);
			Date date2 = formatter.parse(strTime2);

			int hours1 = date1.getHours();
			int hours2 = date2.getHours();

			long distanceHours = hours2 - hours1;
			long distanceMinutes = date2.getMinutes() - date1.getMinutes();
			if (distanceHours >= 0) {
				distance = distanceHours * 60 + distanceMinutes;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return distance;
	}
	
	/**
	 * get num minutes two time in date theo temple HH:mm with negative
	 * @param strTime1
	 * @param strTime2
	 * @return: long
	 * @throws:
	 * @author: dungdq3
	 * @date: Dec 31 2013
	 */
	public static long getDistanceMinutesFrom2HoursNegative(String strTime1, String strTime2) {
		long distance = 0;
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		try {
			Date date1 = formatter.parse(strTime1);
			Date date2 = formatter.parse(strTime2);

			int hours1 = date1.getHours();
			int hours2 = date2.getHours();

			long distanceHours = hours2 - hours1;
			long distanceMinutes = date2.getMinutes() - date1.getMinutes();
			distance = distanceHours * 60 + distanceMinutes;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return distance;
	}
	
	/**
	 * lay so giay giua thoi gian theo dang yyyy-MM-dd HH:mm:ss
	 * @author: dungdq3
	 * @param strDate1 : ngay gio bat dau
	 * @param strDate2 : ngay gio ket thuc
	 * @return : so giay (seconds)
	 */
	public static long getDistanceSecondsFrom2Date(String strDate1, String strDate2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
		Date startTimeDate = new Date();
		try {
			startTimeDate = sdf.parse(strDate1);
		} catch (ParseException e) {
		}
		Date endTimeDate = new Date();
		try {
			endTimeDate = sdf.parse(strDate2);
		} catch (ParseException e) {
		}
		long diff = endTimeDate.getTime() - startTimeDate.getTime();
		long seconds = diff / 1000;
		//long minutes = seconds / 60;

		//seconds = seconds % 60;
		//minutes = minutes % 60;
		
		return seconds;
	}

	/**
	 * lay so phut giua thoi gian theo dang yyyy-MM-dd HH:mm:ss
	 * @author: BANGHN
	 * @param strDate1 : ngay gio bat dau
	 * @param strDate2 : ngay gio ket thuc
	 * @return : so phut (minutes)
	 */
	public static long getDistanceMinutesFrom2Date(String strDate1, String strDate2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
		Date startTimeDate = new Date();
		try {
			startTimeDate = sdf.parse(strDate1);
		} catch (ParseException e) {
		}
		Date endTimeDate = new Date();
		try {
			endTimeDate = sdf.parse(strDate2);
		} catch (ParseException e) {
		}
		long diff = endTimeDate.getTime() - startTimeDate.getTime();
		long seconds = diff / 1000;
		long minutes = seconds / 60;

		//seconds = seconds % 60;
		minutes = minutes % 60;
		
		return minutes;
	}
	
	/**
	 * lay so giay giua thoi gian theo dang yyyy-MM-dd HH:mm:ss
	 * @author: BANGHN
	 * @param strDate1 : ngay gio bat dau
	 * @param strDate2 : ngay gio ket thuc
	 * @return : so phut (minutes)
	 */
	public static long getDistanceSecondFrom2Date(String strDate1, String strDate2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
		Date startTimeDate = new Date();
		try {
			startTimeDate = sdf.parse(strDate1);
		} catch (ParseException e) {
		}
		Date endTimeDate = new Date();
		try {
			endTimeDate = sdf.parse(strDate2);
		} catch (ParseException e) {
		}
		long diff = endTimeDate.getTime() - startTimeDate.getTime();
		long seconds = diff / 1000;
		
		return seconds;
	}

	/**
	 * lay so giay giua thoi gian theo dang yyyy-MM-dd HH:mm:ss
	 * @author: yennth16
	 * @param strDate1 : ngay gio bat dau
	 * @param strDate2 : ngay gio ket thuc
	 * @return : so phut (minutes)
	 */
	public static double getDistanceSecondFrom2DateDouble(String strDate1, String strDate2) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
		Date startTimeDate = new Date();
		try {
			startTimeDate = sdf.parse(strDate1);
		} catch (ParseException e) {
		}
		Date endTimeDate = new Date();
		try {
			endTimeDate = sdf.parse(strDate2);
		} catch (ParseException e) {
		}
		double diff = endTimeDate.getTime() - startTimeDate.getTime();
		double seconds = diff / 1000;
//		double minutes = seconds / 60;
//		minutes = minutes % 60;
		double hour = seconds / 60;
		hour = hour % 60;
		return hour;
	}

	/**
	 * Kiem tra co phai hien tai nam trong thoi gian cham cong hay khong
	 * 
	 * @author banghn
	 * @return: Co hay khong
	 */
	public static boolean isInAttendaceTime() {
		boolean isIn = false;
		String dateNow = DateUtils.now();
		SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_FORMAT_ATTENDANCE);
		Date currentDate = new Date();
		Date startDate = new Date();
		Date endDate = new Date();

		try {
			// starttime
			String startTime = dateNow.split(" ")[0] + " " + GlobalInfo.getInstance().getCcStartTime();

			// endtime
			String endTime = dateNow.split(" ")[0] + " " + GlobalInfo.getInstance().getCcEndTime();

			currentDate = formatter.parse(dateNow);
			startDate = formatter.parse(startTime);
			endDate = formatter.parse(endTime);
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		// Trong thoi gian cham cong
		if (currentDate.compareTo(startDate) >= 0 && currentDate.compareTo(endDate) <= 0) {
			isIn = true;
		}
		return isIn;
	}

	/**
	 * parse Hour Minute
	 * 
	 * @author: TamPQ
	 * @param visit_start_time
	 * @return
	 * @return: Datevoid
	 * @throws:
	 */
	public static Date parseHourMinute(String visit_start_time) {
		Date date = null;
		DateFormat formatter = new SimpleDateFormat("HH:mm");
		try {
			date = formatter.parse(visit_start_time);
		} catch (Exception e) {
		}
		return date;
	}

	/**
	 * So sanh ngay
	 * 
	 * @author: TamPQ
	 * @param remindDateTime : yyyy-MM-dd hoac yyyy-MM-dd HH:mm:ss 
	 * @param dateFormatNow  : yyyy-MM-dd hoac yyyy-MM-dd HH:mm:ss 
	 * @return
	 * @return: intvoid
	 * @throws:
	 */
	public static int compareDate(String date1, String date2) {
		int result = -2;
		try {
			date1 = date1.substring(0, 10);
			date2 = date2.substring(0, 10);

			Date date11 = defaultSqlDateFormat.parse(date1);
			Date date22 = defaultSqlDateFormat.parse(date2);

			if (date11.before(date22)) {
				result = -1; // ngay 1 < ngay 2
			} else if (date11.after(date22)) {
				result = 1; // ngay 1 > ngay 2
			} else {
				result = 0;
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Ktra thoi gian trong vong 30p so voi hien tai
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: booleanvoid
	 * @throws ParseException
	 * @throws:
	 */
	public static boolean isIn30Min(Date t) throws ParseException {
		boolean isValid = false;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, -30);
		Date time30 = calendar.getTime();

		if (time30.after(t)) {
			isValid = false;
		} else {
			isValid = true;
		}

		return isValid;
	}

	/**
	 * Cong thoi gian
	 * 
	 * @author: TamPQ
	 * @param startTime
	 * @param i
	 * @return
	 * @return: Stringvoid
	 * @throws:
	 */
	public static String plusTime(String time, int valueInMinute) {
		Date d = null;
		SimpleDateFormat format = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
		try {
			d = format.parse(time);
			d = new Date(d.getTime() + valueInMinute * ONE_MINUTE_IN_MILLIS);
		} catch (ParseException e) {
			// VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return format.format(d);
	}

	/**
	 * check ngay thang trong khoang may thang
	 * 
	 * @author: TamPQ
	 * @param sDay
	 * @param sMonth
	 * @param year
	 * @return: voidvoid
	 * @throws:
	 */
	public static boolean checkDateInOffsetMonth(String sDay, String sMonth, int year, int offsetMonth) {
		boolean isValid = false;
		try {
			String strDate = sDay + "/" + sMonth + "/" + year;
			Date date = DateUtils.defaultDateFormat.parse(strDate);
			Date firstDateOfPrevious2Month = getFirstDateOfOffsetMonth(offsetMonth);

			if (!date.before(firstDateOfPrevious2Month)) {
				isValid = true;
			}

		} catch (ParseException e) {
			isValid = false;
		}
		return isValid;
	}

	/**
	 * lay ngay dau tien cua 1 offset thang
	 * 
	 * @author: TamPQ
	 * @param sDay
	 * @param sMonth
	 * @param year
	 * @return: voidvoid
	 * @throws:
	 */
	public static Date getFirstDateOfOffsetMonth(int offsetMonth) {
		Date firstDateOfPreviousMonth = null;
		Calendar aCalendar = Calendar.getInstance();
		aCalendar.set(Calendar.DATE, 1);
		aCalendar.add(Calendar.MONTH, offsetMonth);
		try {
			firstDateOfPreviousMonth = DateUtils.defaultDateFormat.parse(DateUtils.defaultDateFormat.format(aCalendar
					.getTime()));
		} catch (ParseException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return firstDateOfPreviousMonth;
	}
	
	/**
	 * Lay chuoi ngay gio giua 2 ngay
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static String getVisitTime(String startTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
		Date startTimeDate = new Date();
		try {
			startTimeDate = sdf.parse(startTime);
		} catch (ParseException e) {
		}
		Date endTimeDate = DateUtils.now(DateUtils.DATE_FORMAT_NOW);
		StringBuilder time = new StringBuilder();
		long diff = endTimeDate.getTime() - startTimeDate.getTime();
		long seconds = diff / 1000;
		long minutes = seconds / 60;
		long hours = minutes / 60;
		
		seconds = seconds % 60;
		minutes = minutes % 60;

		if (hours > 0) {
			time.append(hours + " giờ ");
		}

		time.append(minutes + " phút ");
		time.append(seconds + " giây");

		return time.toString();
	}
	
	/**
	 * Lay so luong ngay trong 1 thang, 1 nam co dinh
	 * @author: duongdt3
	 * @since: 09:45:54 22 Nov 2013
	 * @return: int
	 * @throws:  
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getNumberDayInMonth(int month, int year) {
		Calendar calendar = Calendar.getInstance();
		// vì tháng trong calender sẽ zero-base
		month += 1;
		int date = 1;
		calendar.set(year, month, date);
		int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return days;
	}
	
	public static int getNumberDayInCurrentMonth() {
		Calendar calendar = Calendar.getInstance();
		int thang = calendar.get(Calendar.MONTH);
		int nam = calendar.get(Calendar.YEAR);
		return getNumberDayInMonth(thang, nam);
	}
	
	/**
	 * get string date with format dd/MM/yyyy 
	 * @author: duongdt3
	 * @since: 09:25:19 28 Nov 2013
	 * @return: String
	 * @throws:  
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return
	 */
	public static String getDateString(String formatString, int dayOfMonth, int monthOfYear, int year){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, monthOfYear);
		calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		String result = format.format(calendar.getTime());
		return result;
		
	}
	
	/**
	 * Lay format ngay hien tai voi tham so la gio & phut 
	 * @author: Nguyen Thanh Dung
	 * @return: String
	 * @throws:  
	 * @param formatString
	 * @param hourOfDay
	 * @param minute
	 * @return
	 */
	public static String getDateString(String formatString, int hourOfDay, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0 );
		
		SimpleDateFormat format = new SimpleDateFormat(formatString);
		String result = format.format(calendar.getTime());
		return result;
	}
	
	/**
	 * Get get Calender with date Not Time
	 * @author: duongdt3
	 * @since: 09:41:48 9 Dec 2013
	 * @return: Calendar
	 * @throws:  
	 * @return
	 */
	public static Calendar getCalendarNotTime(Date date) {
		Calendar calendar = Calendar.getInstance();
		if (date!= null) {
			calendar.setTime(date);
		}
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);

		return calendar;
	}
	
	public static String nowVN() {
		Calendar cal = Calendar.getInstance();
		String result = formatDate(cal.getTime(), DATE_TIME_FORMAT_VN);
		return result;
	}
	public static final int GMT7Offset = 60 * 60 * 1000 * 7;

	public static String formatDate(Date date, String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String result = sdf.format(date);
		return result;
	}
	
	/**
	 * Cập nhật thời gian dựa vào thời gian đúng, đồng thời hiện dialog thông báo nếu sai giờ
	 * @author: duongdt3
	 * @since: 09:17:07 8 Apr 2014
	 * @return: void
	 * @throws:  
	 * @param rightDate
	 */
	public static void updateRightTime(String rightDate) {
		/*long timeSinceBootNow = SystemClock.elapsedRealtime();
		long timeBootOld = GlobalInfo.getInstance().getProfile().getUserData().lastRightTimeSinceBoot;
		String rightTimeOld = GlobalInfo.getInstance().getProfile().getUserData().lastRightTime;
		
		//Trong vòng 5 phút mới cập nhật
		if (rightDate.compareTo(rightTimeOld) > 0 && Math.abs(timeSinceBootNow - timeBootOld) >  5 * 60 * 1000) {
			//cập nhật lại lastRightTime, lastTimeOnlineLogin
			GlobalInfo.getInstance().getProfile().getUserData().lastRightTime = rightDate;
			GlobalInfo.getInstance().getProfile().getUserData().lastRightTimeSinceBoot = timeSinceBootNow;
			
			GlobalInfo.getInstance().getProfile().saveRightTime();
			//khi upadate lại giờ, nếu hiện tại không ở LoginView thì check lại giờ đúng hay sai + show dialog cảnh báo người dùng
			if(GlobalInfo.getInstance().getActivityContext() != null && !(GlobalInfo.getInstance().getActivityContext() instanceof LoginView)){
				int wrongTimeType = DateUtils.checkTabletRightTimeWorking();
				if (wrongTimeType != RIGHT_TIME) {
					GlobalUtil.showDialogCheckWrongTime(wrongTimeType);
				}
			}
		}*/
	}
	/**
	 * Cập nhật lại thời gian đúng, có thể bằng GPS, NTP...
	 * @author: duongdt3
	 * @since: 11:45:35 24 Mar 2014
	 * @return: void
	 * @throws:  
	 * @param time
	 */
	public static void updateRightTime(long time) {
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time);
			SimpleDateFormat fm = new SimpleDateFormat(DATE_FORMAT_NOW);
			fm.setTimeZone(TimeZone.getTimeZone("GMT+7"));
			String rightDate = fm.format(cal.getTime());
			DateUtils.updateRightTime(rightDate);
		} catch (Exception e) {
		}
	}
	//thời gian sai lệch tối đa 50p
	public static final long MAX_TIME_WRONG = 50 * 60 * 1000;
	public static final int RIGHT_TIME = 0;
	public static final int WRONG_DATE = 1;
	public static final int WRONG_TIME = 2;
	public static final int WRONG_TIME_BOOT_REASON = 3;
	
	/**
	 * Kiểm tra thời gian hiện tại của tablet, có chênh lệch 1 ngày so với lần Online login cuối cùng 
	 * @author: duongdt3
	 * @since: 11:54:22 24 Mar 2014
	 * @return: int {DateUtils.RIGHT_TIME, DateUtils.WRONG_DATE}
	 * @throws:  
	 * @return
	 */
	public static int checkTabletRightTimeLoginOffline() {
		return checkTabletRightTime(false);
	}
	/**
	 * Kiểm tra thời gian thực tế của tablet, có chênh lệch quá 50 phút hay không 
	 * @author: duongdt3
	 * @since: 11:53:51 24 Mar 2014
	 * @return: int {DateUtils.RIGHT_TIME, DateUtils.WRONG_DATE, DateUtils.WRONG_TIME, DateUtils.WRONG_TIME_BOOT_REASON}
	 * @throws:  
	 * @return
	 */
	public static int checkTabletRightTimeWorking() {
		return checkTabletRightTime(true);
	}
	
	/**
	 * Lấy thời gian đúng tương đối, tính toán bằng lastRightTime
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 12:57:57 15 Jun 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	public static String getRightTimeNow(){
		Calendar cal = getRightCalTimeNow();
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
		String result = sdf.format(cal.getTime());
		return result;
	}
	
	/**
	 * Lấy thời gian đúng tương đối, tính toán bằng lastRightTime, trả ra Calendar
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 17:24:38 20 Jun 2014
	 * @return: Calendar
	 * @throws:  
	 * @return
	 */
	public static Calendar getRightCalTimeNow(){
		Calendar result = Calendar.getInstance();
		try {
			RightTimeInfo rightTimeInfo = GlobalInfo.getInstance().getRightTimeInfo();
			String lastRightTime = rightTimeInfo.lastRightTime;
			if (!StringUtil.isNullOrEmpty(lastRightTime) && !"null".equals(lastRightTime)) {
				long lastTimeOnlineFromBoot = rightTimeInfo.lastRightTimeSinceBoot;
				long timeSinceBootNow = SystemClock.elapsedRealtime();
				long dis = (timeSinceBootNow - lastTimeOnlineFromBoot);
				// bo qua thời gian tắt máy, khởi động lại máy
				if (dis < 0) {
					dis = 0;
				}
				SimpleDateFormat sfs = new SimpleDateFormat(DATE_FORMAT_NOW);
				sfs.setTimeZone(TimeZone.getTimeZone("GMT+7"));
				result = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
				Date rightTime = sfs.parse(lastRightTime.toString());
				result.setTimeInMillis(rightTime.getTime());
				result.add(Calendar.MILLISECOND, (int)dis);
			} else {
				ServerLogger.sendLog( "TIME getRightCalTimeNow lastRightTime: " + lastRightTime + " lastRightTimeSinceBoot " + rightTimeInfo.lastRightTimeSinceBoot, TabletActionLogDTO.LOG_CLIENT);
			}
		} catch (Exception e) {
			ServerLogger.sendLog("TIME getRightCalTimeNow " + e.getMessage(), TabletActionLogDTO.LOG_CLIENT);
			result = Calendar.getInstance();
		}
		return result;
	}
	/**
	 * Kiểm tra thời gian hiện tại của tablet, có chênh lệch quá 50 phút hay không 
	 * @author: duongdt3
	 * @since: 11:46:03 24 Mar 2014
	 * @return: int {DateUtils.RIGHT_TIME, DateUtils.WRONG_DATE, DateUtils.WRONG_TIME, DateUtils.WRONG_TIME_BOOT_REASON}
	 * @throws:  
	 * @param isCheckTimeFromBoot
	 * @return
	 */
	private static int checkTabletRightTime(boolean isCheckTimeFromBoot) {
		
		RightTimeInfo rightTimeInfo = GlobalInfo.getInstance().getRightTimeInfo();
		String lastRightTime = isCheckTimeFromBoot ? rightTimeInfo.lastRightTime : rightTimeInfo.lastTimeOnlineLogin;
		//mac dinh, danh cho truong hop ko co lastTime hoac co loi khi thao tac
		int isOnlineInDate = DateUtils.RIGHT_TIME;
		try {
			if (!StringUtil.isNullOrEmpty(lastRightTime) && !"null".equals(lastRightTime)) {
				String now = DateUtils.now();
				String dateLastRightTime = lastRightTime.substring(0, 10);
				String dateNow = now.substring(0, 10);
				long lastTimeOnlineFromBoot = rightTimeInfo.lastRightTimeSinceBoot;
				Date dFromLastLogIn = DateUtils.parseDateFromString(lastRightTime, DateUtils.DATE_FORMAT_NOW);
				Date dnow = new Date();
				long timeSinceBootNow = SystemClock.elapsedRealtime();
				//dung ngay
				if (dateLastRightTime.equals(dateNow)) {
					isOnlineInDate = DateUtils.RIGHT_TIME;
					
					//nếu cần check đúng giờ
					if (isCheckTimeFromBoot) {						
						long dis = (timeSinceBootNow - lastTimeOnlineFromBoot);
						boolean isWrongBootTime = false;
						//bo qua thời gian tắt máy, khởi động lại máy
						if (dis < 0) {
							dis = 0;
							isWrongBootTime = true;
						}
						//sai gio, qua 50 phut(mac dinh), co the cau hinh ap_param tham so MAX_TIME_WRONG
						if(Math.abs((dFromLastLogIn.getTime() + dis) - dnow.getTime()) > GlobalInfo.getInstance().getMaxIimeWrong()){
							if (isWrongBootTime) {
								isOnlineInDate = DateUtils.WRONG_TIME_BOOT_REASON;							
							}else{
								isOnlineInDate = DateUtils.WRONG_TIME;
							}
						}
					}				
				}else{
					//sai ngày
					isOnlineInDate = DateUtils.WRONG_DATE;
				}
			} else {
				isOnlineInDate = DateUtils.WRONG_DATE;
			}
		} catch (Exception e) {
			isOnlineInDate = DateUtils.WRONG_DATE;
			ServerLogger.sendLog("TIME checkTabletRightTime " + e.getMessage(), TabletActionLogDTO.LOG_CLIENT);
		}
		return isOnlineInDate;
	}
	
	
	/**
	 * check TimeZone hiện tại có phải là GMT+7 hay không
	 * 
	 * @author : duongdt
	 */
	public static boolean isGMT7TimeZone(){
		boolean isRight = false;
		if (GMT7Offset == TimeZone.getDefault().getRawOffset()) {
			isRight = true;
		}
		return isRight;
	}
	
	/**
	 * nowStr
	 * @author: 
	 * @since: 13:43:42 10-04-2015
	 * @return: String
	 * @throws:  
	 * @param format
	 * @return
	 */
	public static String nowStr(String format) {
		Calendar cal = Calendar.getInstance();
		String result = formatDate(cal.getTime(), format);
		return result;
	}

	/**
	 * Convert to string -> date
	 * @param dateString
	 * @return
     */
	public static Date convertToDate(String dateString, String format){
		SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (ParseException e) {
		}
		return convertedDate;
	}
}
