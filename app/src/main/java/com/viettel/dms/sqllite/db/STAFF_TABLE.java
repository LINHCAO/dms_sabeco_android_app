/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.os.Bundle;
import android.text.TextUtils;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.AttendanceDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO.StaffsOfShopInfo;
import com.viettel.dms.dto.view.GSTGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsInfoViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsTNPGViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.dto.view.GsnppRouteSupervisionDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ProgressSoldViewDTO;
import com.viettel.dms.dto.view.ReportInfoDTO;
import com.viettel.dms.dto.view.ShopSupervisorDTO;
import com.viettel.dms.dto.view.TBHVCustomerListDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO;
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Thong tin nhan vien
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class STAFF_TABLE extends ABSTRACT_TABLE {
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// ma nhan vien
	public static final String STAFF_CODE = "STAFF_CODE";
	// ten
	public static final String STAFF_NAME = "STAFF_NAME";
	// dia chi
	public static final String ADDRESS = "ADDRESS";
	// duong
	public static final String STREET = "STREET";
	// nuoc
	public static final String COUNTRY = "COUNTRY";
	// sdt ban
	public static final String PHONE = "PHONE";
	// so di dong
	public static final String MOBILE_PHONE = "MOBILEPHONE";
	// email
	public static final String MAIL = "MAIL";
	// gioi tinh: 1 nam, 0 nu
	public static final String SEX = "SEX";
	// ngay vao lam
	public static final String START_DATE = "START_DATE";
	// trinh do
	public static final String EDUCATION_ID = "EDUCATION_ID";
	// vi tri
	public static final String POSITION_ID = "POSITION_ID";
	// truong nay chua dung
	public static final String CAT_ID = "CAT_ID";
	// id nhan vien quan ly
	public static final String STAFF_OWNER_ID = "STAFF_OWNER_ID";
	// ngay sinh
	public static final String BIRTHDAY = "BIRTHDAY";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// ma vung
	public static final String AREA_CODE = "AREA_CODE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_TIME = "CREATE_TIME";
	// ngay cap nhat
	public static final String UPDATE_TIME = "UPDATE_TIME";
	// loai kenh
	public static final String CHANNEL_TYPE_ID = "CHANNEL_TYPE_ID";
	// 1: hoat dong, 0: ngung hoat dong
	public static final String STATUS = "STATUS";
	// mat khau
	public static final String PASSWORD = "PASSWORD";
	// so lan dang nhap sai
	public static final String NUMBER_LOGIN_FAIL = "NUMBER_LOGIN_FAIL";
	// trang thai khoa: 0: binh thuong, 1: khoa
	public static final String LOCKSTATUS = "LOCKSTATUS";
	// so tien ke hoach ngay
	public static final String PLAN = "PLAN";
	// ngay cap nhat ke hoach
	public static final String UPDATE_PLAN = "UPDATE_PLAN";
	// do hang cuoi cung duoc duyet
	public static final String LAST_APPROVE_ORDER = "LAST_APPROVE_ORDER";
	// don hang cuoi cung da dat
	public static final String LAST_ORDER = "LAST_ORDER";

	public static final String STAFF_TABLE = "STAFF";

	public STAFF_TABLE() {
		this.tableName = STAFF_TABLE;
		this.columns = new String[] { STAFF_ID, STAFF_CODE, STAFF_NAME, ADDRESS, STREET, COUNTRY, PHONE, MOBILE_PHONE,
				MAIL, SEX, START_DATE, EDUCATION_ID, POSITION_ID, CAT_ID, STAFF_OWNER_ID, BIRTHDAY, SHOP_ID, AREA_CODE,
				CREATE_USER, UPDATE_USER, CREATE_TIME, UPDATE_TIME, CHANNEL_TYPE_ID, STATUS, PASSWORD,
				NUMBER_LOGIN_FAIL, LOCKSTATUS, PLAN, UPDATE_PLAN, LAST_APPROVE_ORDER, LAST_ORDER, SYN_STATE };

		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public STAFF_TABLE(SQLiteDatabase mDB) {
		this.tableName = STAFF_TABLE;
		this.columns = new String[] { STAFF_ID, STAFF_CODE, STAFF_NAME, ADDRESS, STREET, COUNTRY, PHONE, MOBILE_PHONE,
				MAIL, SEX, START_DATE, EDUCATION_ID, POSITION_ID, CAT_ID, STAFF_OWNER_ID, BIRTHDAY, SHOP_ID, AREA_CODE,
				CREATE_USER, UPDATE_USER, CREATE_TIME, UPDATE_TIME, CHANNEL_TYPE_ID, STATUS, PASSWORD,
				NUMBER_LOGIN_FAIL, LOCKSTATUS, PLAN, UPDATE_PLAN, LAST_APPROVE_ORDER, LAST_ORDER, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 
	 * get report info date
	 * 
	 * @param staffId
	 * @param shopId
	 * @return
	 * @return: GeneralStatisticsInfoViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 15, 2013
	 */
	public GeneralStatisticsInfoViewDTO getReportGeneralStatisticsDate(String staffId, String shopId) {
		GeneralStatisticsInfoViewDTO result = new GeneralStatisticsInfoViewDTO();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT NAME, ");
		sqlQuery.append("       PLAN, ");
		sqlQuery.append("       CODE, ");
		sqlQuery.append("       ACTUALLY ");
		sqlQuery.append("FROM   RPT_SALE_SUMMARY ");
		sqlQuery.append("WHERE  OBJECT_TYPE = 2 ");
		sqlQuery.append("       AND OBJECT_ID = ? ");
		sqlQuery.append("       AND STATUS = 1 ");
		sqlQuery.append("       AND TYPE = 1 ");
		sqlQuery.append("       AND VIEW_TYPE = 2 ");
		sqlQuery.append("       AND Date(SALE_DATE) = Date('NOW', 'LOCALTIME') ");
		sqlQuery.append("ORDER  BY ORDINAL");

		Cursor c = null;
		String[] paramsList = new String[] { staffId };
		try {
			c = rawQuery(sqlQuery.toString(), paramsList);
			if (c.moveToFirst()) {
				do {
					ReportInfoDTO reportDto = new ReportInfoDTO();
					reportDto.initWithCursorReportDate(c);
					result.listReportDate.add(reportDto);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
//			System.out.println(e.toString());
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return result;
	}

	/**
	 * 
	 * get report info flow month
	 * 
	 * @param staffId
	 * @param shopId
	 * @return
	 * @return: GeneralStatisticsInfoViewDTO
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 15, 2013
	 */
	public GeneralStatisticsInfoViewDTO getReportGeneralStatisticsMonth(String staffId, String shopId) {
		GeneralStatisticsInfoViewDTO result = new GeneralStatisticsInfoViewDTO();
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT NAME, ");
		sqlQuery.append("       PLAN, ");
		sqlQuery.append("       CODE, ");
		sqlQuery.append("       ACTUALLY ");
		sqlQuery.append("FROM   RPT_SALE_SUMMARY ");
		sqlQuery.append("WHERE  OBJECT_TYPE = 2 ");
		sqlQuery.append("       AND OBJECT_ID = ? ");
		sqlQuery.append("       AND STATUS = 1 ");
		sqlQuery.append("       AND TYPE = 2 ");
		sqlQuery.append("       AND VIEW_TYPE = 2 ");
		sqlQuery.append("       AND DATE(SALE_DATE) = DATE(?,'START OF MONTH') ");
		sqlQuery.append("ORDER  BY ORDINAL");

		Cursor c = null;
		String[] paramsList = new String[] { staffId, DateUtils.now() };
		try {
			c = rawQuery(sqlQuery.toString(), paramsList);
			if (c.moveToFirst()) {
				do {
					ReportInfoDTO reportDto = new ReportInfoDTO();
					reportDto.initWithCursorReportDate(c);
					// neu dong tong thi lay them thong tin khach hang chua psds
					// trong thang
					if (reportDto.reportType == ReportInfoDTO.REPORT_TOTAL) {
						reportDto.numCustomerVisitPlanInMonth = this.getNumCustomerVisitPlanInMonth(shopId, staffId);
						reportDto.numCustomerDoNotAmount = this.getNumCustomerDoNotAmountInMonth(shopId, staffId);
					}
					result.listReportMonth.add(reportDto);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
//			System.out.println(e.toString());
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				c.close();
			}
		}

		return result;
	}

	/**
	 * get progress sold infp
	 * @return
	 */
	public GeneralStatisticsTNPGViewDTO.CustomerInfo getCustomerInfoOfPG(String idPG, String date) throws Exception{
		GeneralStatisticsTNPGViewDTO.CustomerInfo result = new GeneralStatisticsTNPGViewDTO.CustomerInfo();
		//neu co ID PG thi lay thong tin KH cua PG dang hoat dong, nhay mua
		if (!StringUtil.isNullOrEmpty(idPG) && !StringUtil.isNullOrEmpty(date)) {
			StringBuffer sqlObject = new StringBuffer();
			ArrayList<String> paramsList = new ArrayList<String>();
			sqlObject.append("	SELECT cus.CUSTOMER_NAME CUSTOMER_NAME, cus.SHORT_CODE CUSTOMER_CODE, cus.ADDRESS ADDRESS");
			sqlObject.append("	FROM PG_VISIT_PLAN vp, CUSTOMER cus	");
			sqlObject.append("	WHERE 1=1	");
			sqlObject.append("	AND vp.CUSTOMER_ID = cus.CUSTOMER_ID 	");
			sqlObject.append("	AND vp.STAFF_ID = ?	");
			//ADD PARAM
			paramsList.add(idPG);
			sqlObject.append("	AND substr(vp.FROM_DATE,1,10) <= ?	");
			//ADD PARAM
			paramsList.add(date);
			sqlObject.append("	AND IFNULL(substr(vp.TO_DATE,1,10) >= ?, 1)	");
			//ADD PARAM
			paramsList.add(date);
			sqlObject.append("	AND vp.STATUS = 1	");
			sqlObject.append("	AND cus.STATUS = 1	");
			
			Cursor c = null;
			
			try {
				c = rawQueries(sqlObject.toString(), paramsList);
				if (c.moveToFirst()) {
					result.initForCursor(c);
				}
			} catch (Exception e) {
//				System.out.println(e.toString());
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			} finally {
				if (c != null) {
					c.close();
				}
			}
		}
		return result;
	}
	
	/**
	 * get progress sold info in NOW
	 * @return
	 */
	public ProgressSoldViewDTO getProgressInfo() throws Exception{
		return getProgressInfo(new Date());
	}
	
	/**
	 * get progress sold info of date
	 * @return
	 */
	public ProgressSoldViewDTO getProgressInfo(Date date) throws Exception{
		ProgressSoldViewDTO result = new ProgressSoldViewDTO();
		// number day plan sale in month
		result.numberDayPlan = new SALE_DAYS_TABLE(mDB)
				.getPlanWorkingDaysOfMonth(date);
		// number day sold in month
		result.numberDaySold = new EXCEPTION_DAY_TABLE(mDB)
				.getWorkingDaysOfMonth(date);

		result.progressSold = (int) StringUtil.calProgress(result.numberDayPlan, result.numberDaySold, true);
		
		return result;
	}
	/**
	 * 
	 * get general statistics info view
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: GeneralStatisticsInfoViewDTO
	 * @throws:
	 */
	public GeneralStatisticsInfoViewDTO getGeneralStatisticsInfo(String staffId, String shopId) throws Exception{

		GeneralStatisticsInfoViewDTO result = new GeneralStatisticsInfoViewDTO();

		/** -------- start report date -------- */
		/**
		 * lay thong tin bao cao doanh so theo ngay
		 */
		// cap nhat thong tin bao cao theo ngay - so
		result.listReportDate = this.getReportGeneralStatisticsDate(staffId, shopId).listReportDate;

		/** -------- start report month ------ */
		// cap nhat thong tin bao cao theo thang - so
		result.listReportMonth = this.getReportGeneralStatisticsMonth(staffId, shopId).listReportMonth;

		/** -------- end report month ------ */
		// number day plan sale in month
		result.numberDayPlan = new SALE_DAYS_TABLE(mDB).getPlanWorkingDaysOfMonth(new Date());
		// number day sold in month
		result.numberDaySold = new EXCEPTION_DAY_TABLE(mDB).getWorkingDaysOfMonth();
		// percent
		if (result.numberDaySold <= 0) {
			result.progressSold = 0;
		} else {
			result.progressSold = (int) ((float) result.numberDaySold * 100 / ((float) result.numberDayPlan <= 0 ? (float) result.numberDaySold
					: (float) result.numberDayPlan));
		}

		return result;
	}

	/**
	 * 
	 * giam sat lo trinh gsnpp tai
	 * 
	 * @author: duongdt
	 * @return
	 * @return: int
	 * @throws:
	 */
	public GsnppRouteSupervisionDTO getGsnppRouteSupervision(long staffId, String today, String shopId) {
		GsnppRouteSupervisionDTO dto = null;
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer sqlObject = new StringBuffer();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);

		sqlObject.append("	SELECT STAFFLIST.STAFF_ID AS STAFF_ID	");
		sqlObject.append("		, STAFFLIST.STAFF_CODE AS STAFF_CODE	");
		sqlObject.append("		, STAFFLIST.NAME AS NAME	");
		sqlObject.append("		, STAFFLIST.SHOP_ID AS SHOP_ID	");
		sqlObject.append("		, STAFFLIST.MOBILE AS MOBILE	");
		sqlObject.append("		, STAFFLIST.LAT AS LAT	");
		sqlObject.append("		, STAFFLIST.LNG AS LNG	");
		sqlObject.append("		, VP.NUM_TOTAL_CUS AS NUM_TOTAL_CUS	");
		sqlObject.append("		, VISITED.NUM_VISITED AS NUM_VISITED	");
		sqlObject.append("		, VISITED.NUM_LESTTHAN_2MIN AS NUM_LESTTHAN_2MIN	");
		sqlObject.append("		, VISITED.NUM_MORETHAN_30MIN AS NUM_MORETHAN_30MIN	");
		sqlObject.append("		, (NUM_VISITED - NUM_MORETHAN_30MIN - NUM_LESTTHAN_2MIN) AS NUM_ON_TIME	");
		sqlObject.append("		, VISITED.NUM_RIGHT_PLAN AS NUM_RIGHT_PLAN, VISITED.NUM_WRONG_PLAN AS NUM_WRONG_PLAN	");
		sqlObject.append("		, VISITED.LESSTHAN_2_MIN_LIST AS LESSTHAN_2_MIN_LIST	");
		sqlObject.append("		, VISITED.MORETHAN_30_MIN_LIST AS MORETHAN_30_MIN_LIST	");
		sqlObject.append("	FROM (	");
		sqlObject.append("		SELECT ST.STAFF_ID AS STAFF_ID	");
		sqlObject.append("			, ST.STAFF_CODE AS STAFF_CODE	");
		sqlObject.append("			, ST.STAFF_NAME AS NAME	");
		sqlObject.append("			, ST.SHOP_ID AS SHOP_ID	");
		sqlObject.append("			, ST.MOBILEPHONE AS MOBILE	");
		sqlObject.append("			, AAC.LAT AS LAT	");
		sqlObject.append("			, AAC.LNG AS LNG	");
		sqlObject.append("		FROM (	");
		sqlObject.append("			SELECT *	");
		sqlObject.append("			FROM STAFF S, CHANNEL_TYPE C	");
		sqlObject.append("			WHERE S.SHOP_ID = ?	");
		//ADD PARAM SHOP ID STAFF
		params.add(shopId);
		sqlObject.append("				AND S.STAFF_OWNER_ID = ?	");
		//ADD PARAM ID GS
		params.add(staffId + "");
		sqlObject.append("				AND S.STATUS = 1	");
		sqlObject.append("				AND C.STATUS = 1	");
		sqlObject.append("				AND S.STAFF_TYPE_ID = C.CHANNEL_TYPE_ID	");
		sqlObject.append("				AND C.TYPE = 2	");
		sqlObject.append("				AND C.OBJECT_TYPE IN (1)	");
		sqlObject.append("			) ST	");
		sqlObject.append("		LEFT JOIN (	");
		sqlObject.append("			SELECT AL.*, MAX(AL.END_TIME) AS LASTENDVISITTIME	");
		sqlObject.append("			FROM ACTION_LOG AL	");
		sqlObject.append("			WHERE substr(START_TIME,0,11) = ?	");
		params.add(dateNow);
		sqlObject.append("			GROUP BY STAFF_ID	");
		sqlObject.append("			) AAC	");
		sqlObject.append("			ON ST.STAFF_ID = AAC.STAFF_ID	");
		sqlObject.append("		) STAFFLIST	");
		sqlObject.append("	LEFT JOIN (	");
		sqlObject.append("		SELECT VP.STAFF_ID STAFF_ID, COUNT(RTC.ROUTING_CUSTOMER_ID) AS NUM_TOTAL_CUS	");
		sqlObject.append("		FROM VISIT_PLAN VP, ROUTING RT, CUSTOMER CT, (	");
		sqlObject.append("				SELECT ROUTING_CUSTOMER_ID	");
		sqlObject.append("					, ROUTING_ID	");
		sqlObject.append("					, CUSTOMER_ID	");
		sqlObject.append("					, STATUS	");
		sqlObject.append("					, MONDAY	");
		sqlObject.append("					, TUESDAY	");
		sqlObject.append("					, WEDNESDAY	");
		sqlObject.append("					, THURSDAY	");
		sqlObject.append("					, FRIDAY	");
		sqlObject.append("					, SATURDAY	");
		sqlObject.append("					, SUNDAY	");
		sqlObject.append("					, SEQ2	");
		sqlObject.append("					, SEQ3	");
		sqlObject.append("					, SEQ4	");
		sqlObject.append("					, SEQ5	");
		sqlObject.append("					, SEQ6	");
		sqlObject.append("					, SEQ7	");
		sqlObject.append("					, SEQ8	");
		sqlObject.append("				FROM ROUTING_CUSTOMER	");
		sqlObject.append("	      WHERE 1=1      	");
		sqlObject.append("			AND substr(START_DATE,0,11) <= ?	");
		params.add(dateNow);
		sqlObject.append("			AND IFNULL(substr(END_DATE,0,11) >= ?, 1)	");
		params.add(dateNow);
		//ADD PARAM TODAY
		sqlObject.append("	            AND " + today + " = 1            	");
		sqlObject.append("	            AND (((cast((julianday(?) - julianday(start_date)) / 7 AS INT) + (	");
		params.add(dateNow);
		sqlObject.append("	            									CASE WHEN ((julianday(?) - julianday(start_date)) % 7 > 0)	");
		params.add(dateNow);
		sqlObject.append("	            											AND (cast((CASE WHEN strftime('%w', ?) = '0' THEN 7 ELSE strftime('%w', ?) END) AS INT) < cast((CASE WHEN strftime('%w', start_date) = '0' THEN 7 ELSE strftime('%w', start_date) END) AS INT)) THEN 1 ELSE 0 END	");
		params.add(dateNow);
		params.add(dateNow);
		sqlObject.append("	            									)	");
		sqlObject.append("	            								) % 4 + 1) IN ( WEEK1 * 1, WEEK2 * 2, WEEK3 * 3, WEEK4 * 4) )	");
		sqlObject.append("				) RTC	");
		sqlObject.append("		WHERE VP.ROUTING_ID = RT.ROUTING_ID	");
		sqlObject.append("			AND RTC.ROUTING_ID = RT.ROUTING_ID	");
		sqlObject.append("			AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID	");
		sqlObject.append("			AND substr(VP.FROM_DATE,0,11) <= ?	");
		params.add(dateNow);
		sqlObject.append("			AND IFNULL(substr(VP.TO_DATE,0,11) >= ?, 1)	");
		params.add(dateNow);
		sqlObject.append("			AND VP.STATUS = 1	");
		sqlObject.append("			AND RT.STATUS = 1	");
		sqlObject.append("			AND RTC.STATUS = 1	");
		sqlObject.append("			AND CT.STATUS = 1	");
		sqlObject.append("		GROUP BY VP.STAFF_ID	");
		sqlObject.append("		) VP	");
		sqlObject.append("		ON STAFFLIST.STAFF_ID = VP.STAFF_ID	");
		sqlObject.append("	LEFT JOIN (	");
		sqlObject.append("		SELECT STAFF_ID, COUNT(CASE WHEN IS_OR = 0 THEN 1 END) AS NUM_VISITED	");
		sqlObject.append("			, COUNT(CASE WHEN (IS_OR = 0 AND VISIT_TIME < 120 ) THEN 1 END) AS NUM_LESTTHAN_2MIN	");
		sqlObject.append("			, COUNT(CASE WHEN (IS_OR = 0 AND VISIT_TIME > 1800) THEN 1 END) AS NUM_MORETHAN_30MIN	");
		sqlObject.append("			, COUNT(CASE WHEN IS_OR = 0 THEN 1 END) AS NUM_RIGHT_PLAN	");
		sqlObject.append("			, COUNT(CASE WHEN IS_OR = 1 THEN 1 END) AS NUM_WRONG_PLAN	");
		sqlObject.append("			, GROUP_CONCAT((CASE WHEN (IS_OR = 0 AND VISIT_TIME < 120) THEN CUSTOMER_ID END)) AS LESSTHAN_2_MIN_LIST	");
		sqlObject.append("			, GROUP_CONCAT((CASE WHEN (IS_OR = 0 AND VISIT_TIME > 1800 ) THEN CUSTOMER_ID END)) AS MORETHAN_30_MIN_LIST	");
		sqlObject.append("		FROM (	");
		sqlObject.append("			SELECT AL.STAFF_ID AS STAFF_ID	");
		sqlObject.append("				, AL.CUSTOMER_ID AS CUSTOMER_ID	");
		sqlObject.append("				, AL.OBJECT_ID AS OBJECT_ID	");
		sqlObject.append("				, AL.OBJECT_TYPE AS OBJECT_TYPE	");
		sqlObject.append("				, AL.IS_OR AS IS_OR	");
		sqlObject.append("				, AL.START_TIME AS START_TIME	");
		sqlObject.append("				, AL.END_TIME AS END_TIME	");
		sqlObject.append("				, (STRFTIME('%H', AL.END_TIME) * 3600) + ((STRFTIME('%M', AL.END_TIME) * 60) + STRFTIME('%S', AL.END_TIME) - (STRFTIME('%H', AL.START_TIME) * 3600) - (STRFTIME('%M', AL.START_TIME) * 60) - STRFTIME('%S', AL.START_TIME)) AS VISIT_TIME	");
		sqlObject.append("			FROM ACTION_LOG AL, CUSTOMER CT	");
		sqlObject.append("			WHERE 1 = 1	");
		sqlObject.append("				AND AL.CUSTOMER_ID = CT.CUSTOMER_ID	");
		sqlObject.append("				AND CT.STATUS = 1	");
		sqlObject.append("				AND ((AL.OBJECT_TYPE = 0 AND AL.END_TIME IS NOT NULL) OR AL.OBJECT_TYPE = 1)	");
		sqlObject.append("				AND substr(AL.START_TIME,0,11) = ?	");
		params.add(dateNow);
		sqlObject.append("				AND (AL.END_TIME IS NULL OR substr(AL.END_TIME,0,11) >= ?)	");
		params.add(dateNow);
		sqlObject.append("			)	");
		sqlObject.append("		GROUP BY STAFF_ID	");
		sqlObject.append("		) VISITED	");
		sqlObject.append("		ON STAFFLIST.STAFF_ID = VISITED.STAFF_ID	");
		sqlObject.append("	ORDER BY STAFF_CODE ASC, NAME ASC	");

		String sql = sqlObject.toString();
		Cursor c = rawQueries(sql, params);
		if (c != null) {
			try {
				dto = new GsnppRouteSupervisionDTO();
				if (c.moveToFirst()) {
					do {
						GsnppRouteSupervisionItem item = new GsnppRouteSupervisionItem();
						item.initDataFromCursor(c);
						dto.itemList.add(item);
					} while (c.moveToNext());
				}
			} catch (Exception ex) {
				VTLog.d("[Quang]", ex.getMessage());
			} finally {
				if (c != null) {
					c.close();
				}
			}
		}

		return dto;
	}

	/**
	 * 
	 * lay so luong khach hang can vieng tham trong thang hien tai
	 * 
	 * @author: HaiTC3
	 * @param shop_id
	 * @param staff_id
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getNumCustomerVisitPlanInMonth(String shop_id, String staff_id) {
		int numCustomer = 0;
		StringBuffer requestGetNumCustomer = new StringBuffer();
		requestGetNumCustomer.append("SELECT Count(DISTINCT RTC.CUSTOMER_ID) AS NUM_CUSTOMER ");
		requestGetNumCustomer.append("FROM   VISIT_PLAN VP, ");
		requestGetNumCustomer.append("       ROUTING RT, ");
		requestGetNumCustomer
				.append("       (select * from routing_customer where Round(Strftime('%W', 'now', 'localtime') + 1 - start_week) >= 0 ) RTC ");
		requestGetNumCustomer.append("WHERE  VP.STAFF_ID = ? ");
		requestGetNumCustomer.append("       AND VP.SHOP_ID = ? ");
		requestGetNumCustomer.append("       AND Date(VP.FROM_DATE) <= Date('NOW', 'LOCALTIME') ");
		requestGetNumCustomer.append("       AND Ifnull (Date(VP.TO_DATE) >= Date('NOW', 'LOCALTIME'), 1) ");
		requestGetNumCustomer.append("       AND VP.STATUS = 1 ");
		requestGetNumCustomer.append("       AND VP.ROUTING_ID = RT.ROUTING_ID ");
		requestGetNumCustomer.append("       AND RT.SHOP_ID = ? ");
		requestGetNumCustomer.append("       AND RT.STATUS = 1 ");
		requestGetNumCustomer.append("       AND RT.ROUTING_ID = RTC.ROUTING_ID ");
		requestGetNumCustomer.append("       AND RTC.STATUS = 1 ");

		// String[] param = { shop_id, staff_id };
		String[] param = { staff_id, shop_id, shop_id };
		Cursor c = null;
		try {
			c = rawQuery(requestGetNumCustomer.toString(), param);
			if (c != null) {

				if (c.moveToFirst()) {
					if (c.getColumnIndex("NUM_CUSTOMER") >= 0) {
						numCustomer = c.getInt(c.getColumnIndex("NUM_CUSTOMER"));
					}
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					VTLog.i("error", e2.toString());
				}
			}
		}
		return numCustomer;
	}

	/**
	 * 
	 * lay so khach hang chua phat sinh doanh so trong thang hien tai
	 * 
	 * @author: HaiTC3
	 * @param shop_id
	 * @param staff_id
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getNumCustomerDoNotAmountInMonth(String shop_id, String staff_id) {
		int numCustomer = 0;

		StringBuffer requestGetNumCustomer = new StringBuffer();
		requestGetNumCustomer.append("SELECT Count(DISTINCT RTC.customer_id) NUM_CUSTOMER ");
		requestGetNumCustomer.append("FROM   visit_plan VP, ");
		requestGetNumCustomer.append("       routing RT, ");
		requestGetNumCustomer
				.append("       (select * from routing_customer where Round(Strftime('%W', 'now', 'localtime') + 1 - start_week) >= 0 ) RTC ");
		requestGetNumCustomer.append("       LEFT JOIN (SELECT SC.customer_id        CUSTOMER_ID, ");
		requestGetNumCustomer.append("                         SC.last_order         LAST_ORDER, ");
		requestGetNumCustomer.append("                         SC.last_approve_order LAST_APPROVE_ORDER ");
		requestGetNumCustomer.append("                  FROM   staff_customer SC ");
		requestGetNumCustomer.append("                  WHERE  1 = 1 ");
		requestGetNumCustomer.append("                         AND SC.staff_id = ?) STC ");
		requestGetNumCustomer.append("              ON STC.customer_id = RTC.customer_id ");
		requestGetNumCustomer.append("WHERE  VP.staff_id = ? ");
		requestGetNumCustomer.append("       AND VP.shop_id = ? ");
		requestGetNumCustomer.append("       AND Date(VP.from_date) <= Date('NOW', 'LOCALTIME') ");
		requestGetNumCustomer.append("       AND Ifnull (Date(VP.to_date) >= Date('NOW', 'LOCALTIME'), 1) ");
		requestGetNumCustomer.append("       AND VP.status = 1 ");
		requestGetNumCustomer.append("       AND VP.routing_id = RT.routing_id ");
		requestGetNumCustomer.append("       AND RT.shop_id = ? ");
		requestGetNumCustomer.append("       AND RT.status = 1 ");
		requestGetNumCustomer.append("       AND RT.routing_id = RTC.routing_id ");
		requestGetNumCustomer.append("       AND RTC.status = 1 ");
		requestGetNumCustomer.append("       AND Ifnull (Date(STC.last_order) < Date('NOW', 'LOCALTIME'), 1) ");
		requestGetNumCustomer.append("       AND Ifnull (Date(STC.last_approve_order) < ");
		requestGetNumCustomer.append("                   Date('NOW', 'localtime','START OF MONTH' ");
		requestGetNumCustomer.append("                       ), 1) ");
		// requestGetNumCustomer.append("       AND Ifnull (Date(RTC.last_order) < Date('NOW', 'LOCALTIME'), 1) ");
		// requestGetNumCustomer.append("       AND Ifnull (Date(RTC.last_approve_order) < ");
		// requestGetNumCustomer.append("                   Date('NOW', 'START OF MONTH', 'LOCALTIME' ");
		// requestGetNumCustomer.append("                       ), 1) ");

		String[] param = { staff_id, staff_id, shop_id, shop_id };
		Cursor c = null;
		try {
			c = rawQuery(requestGetNumCustomer.toString(), param);
			if (c != null) {

				if (c.moveToFirst()) {
					if (c.getColumnIndex("NUM_CUSTOMER") >= 0) {
						numCustomer = c.getInt(c.getColumnIndex("NUM_CUSTOMER"));
					}
				}
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					VTLog.i("error", e2.toString());
				}
			}
		}
		return numCustomer;
	}

	/**
	 * 
	 * lay danh sach nvbh cho man hinh theo doi khac phuc GSBH
	 * 
	 * @author: YenNTH
	 * @param
	 * @return
	 * @return: ComboboxFollowProblemDTO
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getStaffCodeComboboxProblemNVBHOfSuperVisor() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT staff_name AS STAFF_NAME, ");
		var1.append("       staff_id   AS STAFF_ID ");
		var1.append("FROM   staff ");
		var1.append("WHERE  staff_owner_id = ? ");
		var1.append("       AND staff_type_id IN (SELECT channel_type_id ");
		var1.append("                             FROM   channel_type ");
		var1.append("                             WHERE  TYPE = 2 ");
		var1.append("                                    AND status = 1 ");
		var1.append("                                    AND object_type IN ( 1, 2 )) ");
		var1.append("       AND status = 1 ");
		var1.append("       AND shop_id = ? ");
		var1.append("ORDER  BY staff_code, staff_name ");
		String requestGetFollowProblemList = var1.toString();
		Cursor c = null;
		String[] params = new String[] { GlobalInfo.getInstance().getProfile().getUserData().id + Constants.STR_BLANK,GlobalInfo.getInstance().getProfile().getUserData().shopId + Constants.STR_BLANK };
		try {
			c = rawQuery(requestGetFollowProblemList, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemDTO comboboxNVBHDTO = new DisplayProgrameItemDTO();
						if (c.getColumnIndex("STAFF_NAME") >= 0) {
							comboboxNVBHDTO.value = c.getString(c.getColumnIndex("STAFF_ID"));
							comboboxNVBHDTO.name = c.getString(c.getColumnIndex("STAFF_NAME"));
						} else {
							comboboxNVBHDTO.value = Constants.STR_BLANK;
							comboboxNVBHDTO.name = Constants.STR_BLANK;
						}
						result.add(comboboxNVBHDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * Lay ds GSNPP/TTTT cua GST
	 * 
	 * @author: YenNTH
	 * @param shopId
	 * @return
	 * @return: List<ComboBoxDisplayProgrameItemDTO>
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getStaffCodeComboboxProblemSuperVisorOfTBHV(String shopId) {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		SHOP_TABLE shopTB = new SHOP_TABLE(this.mDB);
		ArrayList<String> listShopId = shopTB.getShopRecursiveReverse(shopId);
		//String strListShop = TextUtils.join(",", listShopId);
		StringBuffer stringbuilder = new StringBuffer();
		ArrayList<String> params = new ArrayList<String>();
		stringbuilder.append("SELECT DISTINCT st.staff_code ");
		stringbuilder.append("                || '-' ");
		stringbuilder.append("                || st.staff_name AS STAFF_CODE, st.staff_name as STAFF_NAME, ");
		stringbuilder.append("                st.staff_id AS STAFF_ID, ct.object_type AS OBJECT_TYPE ");
		stringbuilder.append("FROM   (SELECT st.*, ");
		stringbuilder.append("               LIST_GSNPP.shop_id NEW_SHOP_GS ");
		stringbuilder.append("        FROM   staff st ");
		stringbuilder.append("               INNER JOIN (SELECT DISTINCT s.staff_owner_id, ");
		stringbuilder.append("                                           s.shop_id shop_id ");
		stringbuilder.append("                           FROM   staff s, ");
		stringbuilder.append("                                  channel_type c ");
		stringbuilder.append("                           WHERE  s.staff_type_id = c.channel_type_id ");
		stringbuilder.append("                                  AND c.object_type IN ( 1, 2, 11 ) ");
		stringbuilder.append("                                  AND s.status = 1 ");
		stringbuilder.append("                                  AND c.type = 2 and c.status = 1 ");
		stringbuilder.append("                                  AND s.staff_owner_id <> 'null') LIST_GSNPP ");
		stringbuilder.append("                          ) st, ");
		stringbuilder.append("       shop sh, channel_type ct ");
		stringbuilder.append("	WHERE	");
		stringbuilder.append("	    (	");
		stringbuilder.append("	        (	");
		stringbuilder.append("	            st.staff_id IN (	");
		stringbuilder.append("	                SELECT	");
		stringbuilder.append("	                    s.staff_id	");
		stringbuilder.append("	                FROM	");
		stringbuilder.append("	                    staff s,	");
		stringbuilder.append("	                    channel_type c	");
		stringbuilder.append("	                WHERE	");
		stringbuilder.append("	                    s.staff_type_id = c.channel_type_id	");
		stringbuilder.append("	                    AND c.object_type = 11	");
		stringbuilder.append("	                    AND s.status = 1	");
		stringbuilder.append("	                    AND c.type = 2	");
		stringbuilder.append("	                    AND c.status = 1	");
		stringbuilder.append("	            )	");
		stringbuilder.append("	            AND st.STAFF_OWNER_ID= ?	");
		params.add(Constants.STR_BLANK + GlobalInfo.getInstance().getProfile().getUserData().id);
		stringbuilder.append("	        )	");
		stringbuilder.append("	        OR (	");
		stringbuilder.append("	            st.staff_id IN (	");
		stringbuilder.append("	                SELECT	");
		stringbuilder.append("	                    s.staff_id	");
		stringbuilder.append("	                FROM	");
		stringbuilder.append("	                    staff s,	");
		stringbuilder.append("	                    channel_type c	");
		stringbuilder.append("	                WHERE	");
		stringbuilder.append("	                    s.staff_type_id = c.channel_type_id	");
		stringbuilder.append("	                    AND c.object_type = 5	");
		stringbuilder.append("	                    AND s.status = 1	");
		stringbuilder.append("	                    AND c.type = 2	");
		stringbuilder.append("	                    AND c.status = 1	");
		stringbuilder.append("	                    AND s.STAFF_ID in(	");
		stringbuilder.append("	                        SELECT	");
		stringbuilder.append("	                            sgd.STAFF_ID	");
		stringbuilder.append("	                        FROM	");
		stringbuilder.append("	                            staff_group_detail sgd	");
		stringbuilder.append("	                        WHERE	");
		stringbuilder.append("	                            sgd.STAFF_GROUP_ID IN       (	");
		stringbuilder.append("	                                SELECT	");
		stringbuilder.append("	                                    sg1.staff_group_id	");
		stringbuilder.append("	                                FROM	");
		stringbuilder.append("	                                    staff_group sg1	");
		stringbuilder.append("	                                WHERE	");
		stringbuilder.append("	                                    sg1.STAFF_ID = ?	");
		params.add(Constants.STR_BLANK + GlobalInfo.getInstance().getProfile().getUserData().id);
		stringbuilder.append("	                                    AND sg1.status = 1	");
		stringbuilder.append("	                                    AND sg1.GROUP_LEVEL = 3	");
		stringbuilder.append("	                                    AND sg1.GROUP_TYPE = 4	");
		stringbuilder.append("	                            )	");
		stringbuilder.append("	                        )	");
		stringbuilder.append("	                )	");
		stringbuilder.append("	            )	");
		stringbuilder.append("	    )	");

//		stringbuilder.append("WHERE  st.staff_id IN (SELECT s.staff_id ");
//		stringbuilder.append("                       FROM   staff s, ");
//		stringbuilder.append("                              channel_type c ");
//		stringbuilder.append("                       WHERE  s.staff_type_id = c.channel_type_id ");
//		stringbuilder.append("                              AND c.object_type in (?,?) ");
//		params.add(Constants.STR_BLANK + UserDTO.TYPE_GSNPP);
//		params.add(Constants.STR_BLANK + UserDTO.TYPE_TNPG);
//		stringbuilder.append("                              AND s.status = 1 ");
//		stringbuilder.append("                              AND c.type = 2 and c.status = 1) ");
//		stringbuilder.append("       AND st.STAFF_OWNER_ID= ? ");
//		params.add(Constants.STR_BLANK + GlobalInfo.getInstance().getProfile().getUserData().id);

		stringbuilder.append("       AND st.status = 1 and sh.status = 1 ");
		stringbuilder.append("       AND st.new_shop_gs = sh.shop_id ");
		stringbuilder.append("       AND st.staff_type_id = ct.channel_type_id ");
		stringbuilder.append("       AND sh.shop_id IN ( ");
		for(int i = 0; i < listShopId.size(); i++) {
			if(i != 0) {
				stringbuilder.append(",");
			}
			stringbuilder.append("?");
			params.add(listShopId.get(i));
		}
		stringbuilder.append(" ) ");
		stringbuilder.append("ORDER  BY st.staff_code ");
		String requestGetFollowProblemList = stringbuilder.toString();
		Cursor c = null;
//		params.add(Constants.STR_BLANK + UserDTO.TYPE_GSNPP);
//		params.add(Constants.STR_BLANK + UserDTO.TYPE_TNPG);
//		params.add(Constants.STR_BLANK + GlobalInfo.getInstance().getProfile().getUserData().id);

		try {
			c = rawQuery(requestGetFollowProblemList, params.toArray(new String[params.size()]));
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemDTO comboboxNVBHDTO = new DisplayProgrameItemDTO();
						if (c.getColumnIndex("STAFF_CODE") >= 0) {
							comboboxNVBHDTO.value = c.getString(c.getColumnIndex("STAFF_ID"));
							comboboxNVBHDTO.name = c.getString(c.getColumnIndex("OBJECT_TYPE"));
							if(comboboxNVBHDTO.name.equalsIgnoreCase(DisplayProgrameItemDTO.TYPE_GSNPP)){
								comboboxNVBHDTO.name = StringUtil.getString(R.string.TEXT_GS)+"-"+c.getString(c.getColumnIndex("STAFF_NAME"));
								comboboxNVBHDTO.type = Constants.TYPE_GS;
							}else {
								comboboxNVBHDTO.name =StringUtil.getString(R.string.TEXT_TT)+"-"+ c.getString(c.getColumnIndex("STAFF_NAME"));
								comboboxNVBHDTO.type = Constants.TYPE_TT;
							}
						} else {
							comboboxNVBHDTO.value = Constants.STR_BLANK;
							comboboxNVBHDTO.name = Constants.STR_BLANK;
						}
						result.add(comboboxNVBHDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * lay danh sach loai van de cho man hinh theo doi khac phuc GSBH
	 * 
	 * @author: YenNTH
	 * @param
	 * @return
	 * @return: ComboboxFollowProblemDTO
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getComboboxProblemTypeProblemOfSuperVisor() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT AP.ap_param_code, ");
		var1.append("       AP.ap_param_name ");
		var1.append("FROM   ap_param AP ");
		var1.append("WHERE  ( AP.TYPE LIKE 'FEEDBACK_TYPE_NVBH' ");
		var1.append("          OR AP.TYPE LIKE 'FEEDBACK_TYPE_GSNPP' ) ");
		var1.append("       AND status = 1 ");
		var1.append("ORDER BY AP_PARAM_NAME ");
		String requestGetFollowProblemList = var1.toString();
		Cursor c = null;
		String[] params = new String[] {};

		try {
			c = rawQuery(requestGetFollowProblemList, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemDTO comboboxNVBHDTO = new DisplayProgrameItemDTO();
						if (c.getColumnIndex("AP_PARAM_CODE") >= 0) {
							comboboxNVBHDTO.value = c.getString(c.getColumnIndex("AP_PARAM_CODE"));
							comboboxNVBHDTO.name = c.getString(c.getColumnIndex("AP_PARAM_NAME"));
						} else {
							comboboxNVBHDTO.value = Constants.STR_BLANK;
							comboboxNVBHDTO.name = Constants.STR_BLANK;
						}
						result.add(comboboxNVBHDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * 
	 * check db co phai cua user dang dang nhap hay kg?
	 * 
	 * @author: HieuNH
	 * @param
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean checkUserDB(String staffCode, int roleType) {
		boolean result = false;
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("SELECT STAFF_CODE, ROLE_TYPE FROM STAFF");
		stringbuilder
				.append(" WHERE STAFF_CODE = ? AND ROLE_TYPE = ? AND ROLE_TYPE = (SELECT MAX(ROLE_TYPE) FROM STAFF)");
		String requestCheckUserDB = stringbuilder.toString();
		Cursor c = null;
		String[] params = new String[] { staffCode, "" + roleType };

		try {
			c = rawQuery(requestCheckUserDB, params);
			if (c != null) {
				if (c.moveToFirst()) {
					result = true;
				}
			}
		} catch (Exception e) {
			// return null;
			result = false;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		if (!result) {
			mDB.close();
		}
		return result;
	}

	/**
	 * 
	 * danh sach nhan vien giam sat sai tuyen
	 * 
	 * @author: TamPQ
	 * @param
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public TBHVRouteSupervisionDTO getTbhvRouteSupervision(long staff_id, String shopId, int day) {
		String today = DateUtils.getToday();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		TBHVRouteSupervisionDTO dto = null;

		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(shopId);
		//String shopStr = TextUtils.join(",", shopList);

		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (");
		var1.append("SELECT GS.STAFF_ID          AS GS_STAFF_ID, ");
		var1.append("       GS.STAFF_CODE        AS GS_STAFF_CODE, ");
		var1.append("       GS.STAFF_NAME        AS GS_STAFF_NAME, ");
		var1.append("       SH.SHOP_ID			 AS NVBH_SHOP_ID, ");
		var1.append("       SH.SHOP_CODE		 AS NVBH_SHOP_CODE, ");
		var1.append("       COUNT(NVBH.STAFF_ID) AS NUM_NVBH ");
		var1.append("FROM   STAFF NVBH, ");
		var1.append("       CHANNEL_TYPE CH, ");
		var1.append("       SHOP SH, ");
		var1.append("       STAFF GS, ");
		var1.append("       CHANNEL_TYPE GS_CH ");
		var1.append("WHERE  NVBH.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("       AND CH.TYPE = 2 ");
		var1.append("       AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		var1.append("       AND NVBH.SHOP_ID = SH.SHOP_ID ");
		var1.append("       AND NVBH.STATUS = 1 ");
		var1.append("       AND SH.STATUS = 1 ");
		var1.append("       AND NVBH.SHOP_ID IN ( ");
		for(int i = 0; i < shopList.size(); i++) {
			if(i != 0) {
				var1.append(",");
			}
			var1.append("?");
			param.add(shopList.get(i));
		}
		var1.append("       ) ");
		var1.append("       AND NVBH.STAFF_OWNER_ID IS NOT NULL ");
		var1.append("       AND NVBH.STAFF_OWNER_ID = GS.STAFF_ID ");
		var1.append("       AND GS.STAFF_TYPE_ID = GS_CH.CHANNEL_TYPE_ID ");
		var1.append("       AND GS_CH.TYPE = 2 ");
		var1.append("       AND GS_CH.OBJECT_TYPE = 5 ");
		var1.append("       AND GS.STATUS = 1 ");
		var1.append("GROUP  BY SH.SHOP_ID, ");
		var1.append("          NVBH.STAFF_OWNER_ID ");
		var1.append("ORDER  BY SH.SHOP_CODE, ");
		var1.append("          GS.STAFF_NAME ");
		var1.append("		) NPP_LIST ");
		var1.append("       JOIN (SELECT ST.STAFF_ID                 AS NVBH_STAFF_ID, ");
		var1.append("                    ST.STAFF_CODE               AS NVBH_STAFF_CODE, ");
		var1.append("                    ST.STAFF_NAME               AS NVBH_STAFF_NAME, ");
		var1.append("                    ST.STAFF_OWNER_ID           AS NVBH_STAFF_OWNER_ID, ");
		var1.append("                    ST.SHOP_ID           		 AS NVBH_SHOP_ID_1, ");
		var1.append("                    TPD.TRAINING_PLAN_DETAIL_ID AS NVBH_TPD_ID, ");
		var1.append("                    TPD.TRAINING_PLAN_ID        AS NVBH_TP_ID, ");
		var1.append("                    TPD.TRAINING_DATE           AS NVBH_TRAINING_DATE ");
		var1.append("             FROM   TRAINING_PLAN_DETAIL TPD, ");
		var1.append("                    TRAINING_PLAN TP, ");
		var1.append("                    STAFF ST, ");
		var1.append("                    CHANNEL_TYPE CH ");
		var1.append("             WHERE  DATE(TPD.TRAINING_DATE) = DATE (?) ");
		param.add(date_now);
		var1.append("                    AND ST.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("                    AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		var1.append("                    AND CH.TYPE = 2 ");
		var1.append("                    AND ST.STATUS = 1 ");
		var1.append("                    AND TP.TRAINING_PLAN_ID = TPD.TRAINING_PLAN_ID ");
		var1.append("                    AND TPD.STATUS IN ( 0, 1 ) ");
		var1.append("                    AND TP.STATUS = 1 ");
		var1.append("                    AND TPD.STAFF_ID = ST.STAFF_ID)NVBH_TPD ");
		var1.append("         ON (NPP_LIST.GS_STAFF_ID = NVBH_TPD.NVBH_STAFF_OWNER_ID AND  NPP_LIST.NVBH_SHOP_ID = NVBH_TPD.NVBH_SHOP_ID_1 ) ");
		var1.append("       LEFT JOIN (SELECT AL_STAFF.STAFF_ID                         AS NPP_STAFF_ID, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.CUSTOMER_ID)        AS NPP_VISITED_ORDER, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.LAT)                AS NPP_VISITED_LAT, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.LNG)                AS NPP_VISITED_LNG, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.START_TIME)         AS NPP_VISITED_START_TIME, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.CUSTOMER_CODE_NAME) AS NPP_VISITED_CUSTOMER_CODE_NAME, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.CUSTOMER_ADDRESS)   AS NPP_VISITED_CUSTOMER_ADDRESS ");
		var1.append("                  FROM   (SELECT ACTION_LOG.*, ");
		var1.append("                                 ( CUSTOMER.SHORT_CODE");
		var1.append("                                   || ' - ' ");
		var1.append("                                   || CUSTOMER.CUSTOMER_NAME ) AS CUSTOMER_CODE_NAME, ");
		var1.append("                                 (CASE WHEN CUSTOMER.ADDRESS IS NOT NULL THEN CUSTOMER.ADDRESS ELSE (CUSTOMER.HOUSENUMBER || ' - ' || CUSTOMER.STREET) END)  AS CUSTOMER_ADDRESS, ");
		var1.append("                                 MIN(START_TIME)               AS VISITED_TIME, ");
		var1.append("                                 MAX(START_TIME)               AS LAST_VISITED_TIME ");
		var1.append("                          FROM   ACTION_LOG, ");
		var1.append("                                 CUSTOMER ");
		var1.append("                          WHERE  DATE(START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("                                 AND CUSTOMER.CUSTOMER_ID = ACTION_LOG.CUSTOMER_ID ");
		var1.append("                                 AND ACTION_LOG.OBJECT_TYPE = 0 ");
		var1.append("                          GROUP  BY ACTION_LOG.STAFF_ID, ");
		var1.append("                                    ACTION_LOG.CUSTOMER_ID ");
		var1.append("                          ORDER  BY VISITED_TIME ASC) AL_STAFF, ");
		var1.append("                         STAFF, ");
		var1.append("                         CHANNEL_TYPE CH, ");
		var1.append("                         CUSTOMER ");
		var1.append("                  WHERE  AL_STAFF.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("                         AND STAFF.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("                         AND CH.OBJECT_TYPE = 5 ");
		var1.append("                         AND CH.TYPE = 2 ");
		var1.append("                         AND CUSTOMER.CUSTOMER_ID = AL_STAFF.CUSTOMER_ID ");
		var1.append("                         AND CUSTOMER.STATUS = 1 ");
		var1.append("                  GROUP  BY AL_STAFF.STAFF_ID)NPP_AL ");
		var1.append("              ON NPP_LIST.GS_STAFF_ID = NPP_AL.NPP_STAFF_ID ");
		var1.append("       LEFT JOIN (SELECT STAFF_ID               AS NVBH_STAFF_ID_1, ");
		var1.append("                         COUNT(RTC.CUSTOMER_ID) AS NUM_CUS_PLAN ");
		var1.append("                  FROM   VISIT_PLAN VP, ");
		var1.append("                         ROUTING RT, ");
		var1.append("                         CUSTOMER CT, ");
		var1.append("                         (SELECT ROUTING_CUSTOMER_ID, ");
		var1.append("                                 ROUTING_ID, ");
		var1.append("                                 CUSTOMER_ID, ");
		var1.append("                                 START_WEEK, ");
		var1.append("                                 WEEK_INTERVAL, ");
		var1.append("                                 STATUS, ");
		var1.append("                                 MONDAY, ");
		var1.append("                                 TUESDAY, ");
		var1.append("                                 WEDNESDAY, ");
		var1.append("                                 THURSDAY, ");
		var1.append("                                 FRIDAY, ");
		var1.append("                                 SATURDAY, ");
		var1.append("                                 SUNDAY, ");
		var1.append("                                 SEQ2, ");
		var1.append("                                 SEQ3, ");
		var1.append("                                 SEQ4, ");
		var1.append("                                 SEQ5, ");
		var1.append("                                 SEQ6, ");
		var1.append("                                 SEQ7, ");
		var1.append("                                 SEQ8 ");
		var1.append("                          FROM   ROUTING_CUSTOMER ");
		var1.append("                          WHERE 1=1 ");
		var1.append("	             		   AND (((cast((julianday(DATE ('now', 'localtime')) - julianday(start_date)) / 7 AS INT) + (	");
		var1.append("	            									CASE WHEN ((julianday(DATE ('now', 'localtime')) - julianday(start_date)) % 7 > 0)	");
		var1.append("	            											AND (cast((CASE WHEN strftime('%w', 'now', 'localtime') = '0' THEN 7 ELSE strftime('%w', 'now', 'localtime') END) AS INT) < cast((CASE WHEN strftime('%w', start_date) = '0' THEN 7 ELSE strftime('%w', start_date) END) AS INT)) THEN 1 ELSE 0 END	");
		var1.append("	            									)	");
		var1.append("	            								) % 4 + 1) IN ( WEEK1 * 1, WEEK2 * 2, WEEK3 * 3, WEEK4 * 4) )	");
		var1.append("                                                 ) RTC ");
		var1.append("                  WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("                         AND RTC.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("                         AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		var1.append("                         AND DATE(VP.FROM_DATE) <= DATE(?) ");
		param.add(date_now);
		var1.append("                         AND ( VP.TO_DATE IS NULL ");
		var1.append("                                OR DATE(VP.TO_DATE) >= DATE(?) ) ");
		param.add(date_now);
		var1.append("                         AND VP.STATUS = 1 ");
		var1.append("                         AND RT.STATUS = 1 ");
		var1.append("                         AND RTC.STATUS = 1 ");
		var1.append("                         AND RTC." + today + " = 1 ");
		var1.append("                         AND CT.STATUS = 1 ");
		var1.append("                  GROUP  BY STAFF_ID) PLAN ");
		var1.append("              ON PLAN.NVBH_STAFF_ID_1 = NVBH_TPD.NVBH_STAFF_ID ");
		var1.append("       LEFT JOIN (SELECT AL_STAFF.STAFF_ID                         AS NVBH_STAFF_IDS, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.CUSTOMER_ID)        AS NVBH_VISITED_ORDER, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.LAT)                AS NVBH_VISITED_LAT, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.LNG)                AS NVBH_VISITED_LNG, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.START_TIME)         AS NVBH_VISITED_START_TIME, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.END_TIME)           AS NVBH_VISITED_END_TIME, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.CUSTOMER_CODE_NAME) AS NVBH_VISITED_CUSTOMER_CODE_NAME, ");
		var1.append("                         GROUP_CONCAT(AL_STAFF.CUSTOMER_ADDRESS)   AS NVBH_VISITED_CUSTOMER_ADDRESS ");
		var1.append("                  FROM   (SELECT ACTION_LOG.*, ");
		var1.append("                                 ( CUSTOMER.SHORT_CODE ");
		var1.append("                                   || ' - ' ");
		var1.append("                                   || CUSTOMER.CUSTOMER_NAME ) AS CUSTOMER_CODE_NAME, ");
		var1.append("                                 CUSTOMER.ADDRESS  AS CUSTOMER_ADDRESS, ");
		var1.append("                                 MIN(START_TIME)               AS VISITED_TIME ");
		var1.append("                          FROM   ACTION_LOG, ");
		var1.append("                                 CUSTOMER ");
		var1.append("                          WHERE  DATE(START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("                                 AND ACTION_LOG.CUSTOMER_ID = CUSTOMER.CUSTOMER_ID ");
		var1.append("                                 AND OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("                                 AND IS_OR = 0 ");
		var1.append("                          GROUP  BY ACTION_LOG.STAFF_ID, ");
		var1.append("                                    ACTION_LOG.CUSTOMER_ID ");
		var1.append("                          ORDER  BY START_TIME ASC) AL_STAFF, ");
		var1.append("                         STAFF, ");
		var1.append("                         CHANNEL_TYPE CH ");
		var1.append("                  WHERE  AL_STAFF.STAFF_ID = STAFF.STAFF_ID ");
		var1.append("                         AND STAFF.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("                         AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		var1.append("                         AND CH.TYPE = 2 ");
		var1.append("                  GROUP  BY AL_STAFF.STAFF_ID) NVBH_AL ");
		var1.append("              ON NVBH_AL.NVBH_STAFF_IDS = NVBH_TPD.NVBH_STAFF_ID ");
		var1.append("ORDER  BY NVBH_SHOP_CODE ASC, ");
		var1.append("          GS_STAFF_NAME ASC ");

		Cursor c = rawQueries(var1.toString(), param);
		if (c != null) {
			try {
				dto = new TBHVRouteSupervisionDTO();
				if (c.moveToFirst()) {
					do {
						TBHVRouteSupervisionDTO.THBVRouteSupervisionItem item = dto.newTHBVRouteSupervisionItem();
						item.initDataFromCursor(c);
						dto.itemGSNPPList.add(item);
					} while (c.moveToNext());
				}
			} catch (Exception ex) {
			} finally {
				if (c != null) {
					c.close();
				}
			}
		}

		return dto;
	}

	/**
	 * Lay ds vi tri NVBH cua NVGS
	 * 
	 * @author: TruongHN
	 * @param staffId
	 * @return: SaleRoadMapSupervisorDTO
	 * @throws:
	 */
	public GsnppRouteSupervisionDTO getListPositionSalePersons(long staffId, String shopId) {
		GsnppRouteSupervisionDTO dto = null;
		String date_now = DateUtils
				.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT staff.staff_id                           AS STAFF_ID, ");
		var1.append("       staff.staff_code                         AS STAFF_CODE, ");
		var1.append("       staff.staff_name                         AS STAFF_NAME, ");
		var1.append("       staff.shop_id                            AS SHOP_ID, ");
		var1.append("       staff.mobilephone                        AS MOBILEPHONE, ");
		var1.append("       staff.phone                              AS PHONE, ");
		var1.append("       staffPosLog.lat                          AS LAT, ");
		var1.append("       staffPosLog.lng                          AS LNG, ");
		var1.append("       ACTIONLOGSTAFF.customer_id               AS CUSTOMER_ID, ");
		var1.append("       ACTIONLOGSTAFF.cus_name                  AS CUS_NAME, ");
		var1.append("       ACTIONLOGSTAFF.start_time                AS START_TIME, ");
		var1.append("       ACTIONLOGSTAFF.end_time                  AS END_TIME, ");
		var1.append("       Strftime('%H:%M', staffPosLog.date_time) AS DATE_TIME ");
		var1.append("FROM   (SELECT * ");
		var1.append("        FROM   staff ");
		var1.append("        WHERE  staff_owner_id = ? ");
		param.add("" + staffId);
		var1.append("               AND status = 1 ");
		var1.append("               AND shop_id = ? ");
		param.add(shopId);
		var1.append("        ORDER  BY staff_name) staff ");
		var1.append("       LEFT JOIN (SELECT staff_position_log_id, ");
		var1.append("                         staff_id, ");
		var1.append("                         Max(create_date) AS DATE_TIME, ");
		var1.append("                         lat, ");
		var1.append("                         lng ");
		var1.append("                  FROM   staff_position_log ");
		var1.append("                  WHERE  substr(create_date,1,10) = ? ");
		param.add(date_now);
		var1.append("                  		  AND STATUS = 1 ");
		var1.append("                  GROUP  BY staff_id) staffPosLog ");
		var1.append("              ON staff.staff_id = staffPosLog.staff_id ");
		var1.append("       LEFT JOIN (SELECT staff.staff_id                AS STAFF_ID_1, ");
		var1.append("                         customer.customer_id, ");
		var1.append("                         ( Substr(customer.customer_code, 1, 3) ");
		var1.append("                           || '-' ");
		var1.append("                           || customer.customer_name ) AS CUS_NAME, ");
		var1.append("                         Max(action_log.start_time)    AS START_TIME, ");
		var1.append("                         action_log.end_time           AS END_TIME ");
		var1.append("                  FROM   action_log, ");
		var1.append("                         staff, ");
		var1.append("                         customer ");
		var1.append("                  WHERE  action_log.staff_id = staff.staff_id ");
		var1.append("                         AND substr(action_log.start_time,1,10) = ? ");
		param.add(date_now);
		var1.append("                         AND staff.status = 1 ");
		var1.append("                         AND action_log.object_type IN ( 0, 1 ) ");
		var1.append("                         AND customer.customer_id = action_log.customer_id ");
		var1.append("                  GROUP  BY action_log.staff_id) ACTIONLOGSTAFF ");
		var1.append("              ON staff.staff_id = ACTIONLOGSTAFF.staff_id_1 ");

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				dto = new GsnppRouteSupervisionDTO();
				if (c.moveToFirst()) {
					do {
						GsnppRouteSupervisionItem item = new GsnppRouteSupervisionItem();
						item.initListPostSalePersons(c);
						dto.itemList.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.i("getListPositionSalePersons", ex.getMessage());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getListPositionSalePersons", e.getMessage());
			}
		}
		return dto;
	}

	/**
	 * HieuNH lay danh sach NVBH thuoc quyen quan ly cua NVGS trong chuc nang
	 * danh sach khach hang chua PSDS
	 * 
	 * @param shopId
	 * @param staffOwnerId
	 *            : id NVGS
	 * @return
	 */
	public ListStaffDTO getListNVBH(long shopId, String staffOwnerId) {
		ListStaffDTO dto = new ListStaffDTO();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT DISTINCT tb1.nvbh     STAFF_NAME, ");
		sqlQuery.append("                tb1.staff_id STAFF_ID, ");
		sqlQuery.append("                tb1.manv     STAFF_CODE ");
		sqlQuery.append("FROM   (SELECT s.staff_id, ");
		sqlQuery.append("               s.staff_code MaNV, ");
		sqlQuery.append("               s.staff_name NVBH, ");
		sqlQuery.append("               a.customer_id ");
		sqlQuery.append("        FROM   (SELECT * ");
		sqlQuery.append("                FROM   routing_customer ");
		sqlQuery.append("                WHERE  Round(Strftime('%W', ?) + 1 - start_week ");
		params.add(date_now);
		sqlQuery.append("                       ) >= 0 ");
		sqlQuery.append("                       AND status = 1) a, ");
		sqlQuery.append("               visit_plan b, ");
		sqlQuery.append("               routing c, ");
		sqlQuery.append("               staff s, ");
		sqlQuery.append("               shop sh, ");
		sqlQuery.append("               customer cu ");
		sqlQuery.append("        WHERE  1 = 1 ");
		sqlQuery.append("               AND b.routing_id = a.routing_id ");
		sqlQuery.append("               AND c.routing_id = a.routing_id ");
		sqlQuery.append("               AND b.staff_id = s.staff_id ");
		sqlQuery.append("               AND a.customer_id = cu.customer_id ");
		sqlQuery.append("               AND s.shop_id = sh.shop_id ");
		sqlQuery.append("               AND sh.status = 1 ");
		sqlQuery.append("               AND a.status = 1 ");
		sqlQuery.append("               AND b.status = 1 ");
		sqlQuery.append("               AND s.status = 1 ");
		sqlQuery.append("               AND c.status = 1 ");
		sqlQuery.append("               AND cu.status = 1 ");
		sqlQuery.append("               AND ( ? > substr(b.from_date,1,10) ");
		params.add(date_now);
		sqlQuery.append("                     AND ( b.to_date IS NULL ");
		sqlQuery.append("                            OR ? < ");
		params.add(date_now);
		sqlQuery.append("                               substr(b.to_date,1,10) ) ) ");
		if (!StringUtil.isNullOrEmpty("" + shopId)) {
			sqlQuery.append(" AND s.shop_id = ?");
			params.add("" + shopId);
		}

		if (!StringUtil.isNullOrEmpty("" + staffOwnerId)) {
			sqlQuery.append(" AND s.staff_owner_id = ?");
			params.add("" + staffOwnerId);
		}
		sqlQuery.append("   ) tb1    LEFT JOIN staff_customer tb2 ");
		sqlQuery.append("              ON ( tb1.staff_id = tb2.staff_id ");
		sqlQuery.append("                   AND tb1.customer_id = tb2.customer_id ) ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND ( substr(tb2.last_approve_order,1,10) IS NULL ");
		sqlQuery.append("              OR tb2.last_approve_order = '' ");
		sqlQuery.append("              OR Date(tb2.last_approve_order) < (SELECT ");
		sqlQuery.append("                 Date(?,'start of month' ");
		params.add(date_now);
		sqlQuery.append("                 )) ) ");
		sqlQuery.append("       AND ( tb2.last_order IS NULL ");
		sqlQuery.append("              OR tb2.last_order = '' ");
		sqlQuery.append("              OR substr(tb2.last_order,1,10) < ? ) ");
		params.add(date_now);
		sqlQuery.append("ORDER  BY staff_name ");

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				VTLog.i("error", e.toString());
			}
		}

		return dto;
	}

	/**
	 * 
	 * Lay danh sach nhan vien
	 * 
	 * @author: YenNTH
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ArrayList<String> getStaffRecursiveReverse(String parentStaffId) {
	
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT staff_name AS STAFF_NAME, ");
		var1.append("       staff_id   AS STAFF_ID ");
		var1.append("FROM   staff ");
		var1.append("WHERE  staff_owner_id = ? ");
		var1.append("       AND staff_type_id IN (SELECT channel_type_id ");
		var1.append("                             FROM   channel_type ");
		var1.append("                             WHERE  TYPE = 2 ");
		var1.append("                                    AND status = 1 ");
		var1.append("                                    AND object_type IN ( 1, 2 )) ");
		var1.append("       AND status = 1 ");
		var1.append("       AND shop_id = ? ");
		var1.append("ORDER  BY staff_code, staff_name ");
		String requestGetFollowProblemList = var1.toString();
		String[] params = { GlobalInfo.getInstance().getProfile().getUserData().id + Constants.STR_BLANK,GlobalInfo.getInstance().getProfile().getUserData().shopId + Constants.STR_BLANK };
		Cursor c = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			c = rawQuery(requestGetFollowProblemList, params);
			if (c != null) {
				while (c.moveToNext()) {
						String staff_id = c.getString(c.getColumnIndex("STAFF_ID"));
						result.add(staff_id);
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 *
	 * Lay danh sach nhan vien theo giam sat npp
	 *
	 * @author: TrungNT56
	 * @param parentStaffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ArrayList<String> getStaffRecursiveReverseByGSNPP(String parentStaffId) {
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT staff_name AS STAFF_NAME, ");
		var1.append("       staff_id   AS STAFF_ID ");
		var1.append("FROM   staff ");
		var1.append("WHERE  staff_owner_id = ? ");
		var1.append("       AND staff_type_id IN (SELECT channel_type_id ");
		var1.append("                             FROM   channel_type ");
		var1.append("                             WHERE  TYPE = 2 ");
		var1.append("                                    AND status = 1 ");
		var1.append("                                    AND object_type IN ( 1, 2 )) ");
		var1.append("       AND status = 1 ");
		var1.append("ORDER  BY staff_code, staff_name ");
		String requestGetFollowProblemList = var1.toString();
		String[] params = { parentStaffId + Constants.STR_BLANK};
		Cursor c = null;
		ArrayList<String> result = new ArrayList<String>();
		try {
			c = rawQuery(requestGetFollowProblemList, params);
			if (c != null) {
				while (c.moveToNext()) {
					String staff_id = c.getString(c.getColumnIndex("STAFF_ID"));
					result.add(staff_id);
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 *
	 * Lay danh sach GSNPP thuoc GSBH
	 *
	 * @author: TrungNT56
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ListStaffDTO getListGSNPPByGSBH(String staffId, boolean isHaveAll) {
		ListStaffDTO dto = new ListStaffDTO();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT STAFF_ID, STAFF_NAME, STAFF_CODE, SHOP_ID FROM   staff WHERE 1=1 ");
		sql.append("		AND STAFF_ID in(	");
		sql.append("	    	SELECT	");
		sql.append("	        	sgd.STAFF_ID	");
		sql.append("	    	FROM	");
		sql.append("	        	staff_group_detail sgd	");
		sql.append("	    	WHERE	");
		sql.append("	        	sgd.STAFF_GROUP_ID IN       (	");
		sql.append("	            	SELECT	");
		sql.append("	                	sg1.staff_group_id	");
		sql.append("	            	FROM	");
		sql.append("	                	staff_group sg1	");
		sql.append("	            	WHERE	");
		sql.append("	                	sg1.STAFF_ID = ?	");
		sql.append("	                	AND sg1.status = 1	");
		sql.append("	                	AND sg1.GROUP_LEVEL = 3	");
		sql.append("	                	AND sg1.GROUP_TYPE = 4	");
		sql.append("	        ))	");
//		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
//		}else {
//			sql.append("   AND staff_owner_id = ? ");
//		}
		sql.append("       AND STATUS = 1 ");
		sql.append("       AND staff_type_id IN (SELECT channel_type_id ");
		sql.append("                             FROM   channel_type ");
		sql.append("                             WHERE  TYPE = 2 ");
		sql.append("                                    AND status = 1 ");
		sql.append("                                    AND object_type IN (5)) ");
		sql.append(" order by STAFF_CODE");
		Cursor c = null;
		String[] params = { staffId };
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					if (isHaveAll && c.getCount() > 1) {
						dto.addItemAll();
					}
					do {
						dto.addItem(c);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return dto;
	}

	/**
	 *
	 * Lay danh sach nhan vien thuoc GSBH
	 *
	 * @author: TrungNT56
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ListStaffDTO getListNVBHByGSBH(String staffId, boolean isHaveAll) {
		ListStaffDTO dto = new ListStaffDTO();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT STAFF_ID, STAFF_NAME, STAFF_CODE FROM   staff WHERE  1=1  ");
		sql.append("		AND STAFF_ID in(	");
		sql.append("	    	SELECT	");
		sql.append("	        	sgd.STAFF_ID	");
		sql.append("	    	FROM	");
		sql.append("	        	staff_group_detail sgd	");
		sql.append("	    	WHERE	");
		sql.append("	        	sgd.STAFF_GROUP_ID IN       (	");
		sql.append("	            	SELECT	");
		sql.append("	                	sg1.staff_group_id	");
		sql.append("	            	FROM	");
		sql.append("	                	staff_group sg1	");
		sql.append("	            	WHERE	");
		sql.append("	                	sg1.STAFF_ID = ?	");
		sql.append("	                	AND sg1.status = 1	");
		sql.append("	                	AND sg1.GROUP_LEVEL = 3	");
		sql.append("	                	AND sg1.GROUP_TYPE = 4	");
		sql.append("	        ))	");
//		sql.append("       AND staff_owner_id = ? ");
		sql.append("       AND STATUS = 1 ");
		sql.append("       AND staff_type_id IN (SELECT channel_type_id ");
		sql.append("                             FROM   channel_type ");
		sql.append("                             WHERE  TYPE = 2 ");
		sql.append("                                    AND status = 1 ");
		sql.append("                                    AND object_type IN (5)) ");
		sql.append(" order by STAFF_CODE");
		Cursor c = null;
		String[] params = { staffId };
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					if (isHaveAll && c.getCount() > 1) {
						dto.addItemAll();
					}
					do {
						dto.arrList.addAll(getListNVBHByGSNPP(c.getString(c.getColumnIndex("STAFF_ID")), false, false).arrList);
					} while (c.moveToNext());

					// sap xep danh sach
					for(int i = 0; i <= dto.arrList.size() - 2; i++) {
						for(int j = i + 1; j <= dto.arrList.size() - 1; j++) {
							if(dto.arrList.get(i).code.compareTo(dto.arrList.get(j).code) > 0) {
								ListStaffDTO.StaffItem staffItem = dto.arrList.get(i);
								dto.arrList.set(i, dto.arrList.get(j));
								dto.arrList.set(j, staffItem);
							}
						}
					}
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return dto;
	}

	/**
	 *
	 * Lay danh sach nhan vien thuoc GSNPP
	 *
	 * @author: TrungNT56
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ListStaffDTO getListNVBHByGSNPP(String staffId, boolean isHaveAll, boolean isOrder) {
		ListStaffDTO dto = new ListStaffDTO();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT STAFF_ID, STAFF_NAME, STAFF_CODE, STAFF_OWNER_ID, SHOP_ID FROM   staff WHERE  staff_owner_id = ? and STATUS = 1 ");
		sql.append("       AND staff_type_id IN (SELECT channel_type_id ");
		sql.append("                             FROM   channel_type ");
		sql.append("                             WHERE  TYPE = 2 ");
		sql.append("                                    AND status = 1 ");
		sql.append("                                    AND object_type IN (1)) ");
		if(isOrder) {
			sql.append(" order by STAFF_CODE");
		}
		Cursor c = null;
		String[] params = { staffId };
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					if (isHaveAll && c.getCount() > 1) {
						dto.addItemAll();
					}
					do {
						dto.addItem(c);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return dto;
	}

	/**
	 * 
	 * Lay danh sach nhan vien thuoc gs
	 * 
	 * @author: YenNTH
	 * @param staffId
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 */
	public ArrayList<String> getStaffRecursiveReverseTBHV(String staffId) {
		String sql = "SELECT staff_id AS STAFF_ID FROM   staff WHERE  staff_owner_id = ? and STATUS <> 0 ";
		Cursor c = null;
		String[] params = { staffId };
		ArrayList<String> staffIdArray = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(staffId) && !"null".equals(staffId)) {
			staffIdArray.add(staffId);
		}
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String staff = c.getString(c.getColumnIndex("STAFF_ID"));
						ArrayList<String> tempArray = getStaffRecursiveReverseTBHV(staff);
						staffIdArray.addAll(tempArray);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		
		return staffIdArray;
	}

	
	/**
	 * Lay ds Kh cua 1 GSNPP, GST
	 * @author YenNTH
	 * @return
	 */
	public TBHVCustomerListDTO getCustomerListForPostFeedback(Bundle data) {
		int page = data.getInt(IntentConstants.INTENT_PAGE);
		String staff = Constants.STR_BLANK;
		if (data.containsKey(IntentConstants.INTENT_STAFF_ID)) {
			staff = data.getString(IntentConstants.INTENT_STAFF_ID);
		}
		String shopId = Constants.STR_BLANK;
		if (data.containsKey(IntentConstants.INTENT_SHOP_ID)) {
			shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		}
		String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		boolean isGetNumRow = data.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		String objectType = data.getString(IntentConstants.INTENT_OBJECT_TYPE);
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		StringBuffer totalPageSql = new StringBuffer();
		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = null;
		String shopStr = Constants.STR_BLANK;
		ArrayList<String> staffList = null;
		String staffStr = Constants.STR_BLANK;
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
			shopList = shopTable.getShopRecursiveReverseTBHV(staff);
			shopId = null;
			//shopStr = TextUtils.join(",", shopList);
			if (objectType.equalsIgnoreCase(Constants.TYPE_GS)) {
				staffList = getStaffRecursiveReverseTBHV(staff);
				staff = null;
				//staffStr = TextUtils.join(",", staffList);
			}
		}else {
			 shopList = shopTable.getShopRecursiveReverse(String.valueOf(shopId));
			 //shopStr = TextUtils.join(",", shopList);
			 staffList = getStaffRecursiveReverse(Constants.STR_BLANK+GlobalInfo.getInstance().getProfile().getUserData().id);
			 //staffStr = TextUtils.join(",", staffList);
		}
		
		var1.append("SELECT DISTINCT cus.customer_id   AS CUSTOMER_ID, ");
		var1.append("                cus.customer_code AS CUSTOMER_CODE, ");
		var1.append("                cus.customer_name AS CUSTOMER_NAME, ");
		var1.append("                cus.address_      AS ADDRESS_ ");
		var1.append("FROM   ( (SELECT DISTINCT vp.shop_id  AS VPSHOP_ID, ");
		var1.append("                        vp.staff_id AS VPSTAFF_ID, ");
		var1.append("                        rc.customer_id, ");
		var1.append("                        rc.routing_id ");
		var1.append("        FROM   visit_plan vp, ");
		var1.append("               routing_customer rc, ");
		var1.append("               routing r ");
		var1.append("        WHERE  rc.routing_id = r.routing_id ");
		var1.append("               AND vp.routing_id = r.routing_id ");
		var1.append("               AND Date(rc.start_date) <= Date('now', 'localtime') ");
		var1.append("               AND r.status <> 0 ");
		var1.append("               AND ( Date(rc.end_date) >= Date('now', 'localtime') ");
		var1.append("                      OR Date(rc.end_date) IS NULL ) ");
		var1.append("               AND ( Date(vp.from_date) <= Date('now', 'localtime') ) ");
		var1.append("               AND ( Date(vp.to_date) >= Date('now', 'localtime') ");
		var1.append("                      OR Date(vp.to_date) IS NULL ) ");
		if(!StringUtil.isNullOrEmpty(shopId)){
			var1.append("               AND vp.shop_id IN ( ");
			for(int i = 0; i < shopList.size(); i++) {
				if(i != 0) {
					var1.append(",");
				}
				var1.append("?");
				param.add(shopList.get(i));
			}
			var1.append("               ) ");
		}
		if(!StringUtil.isNullOrEmpty(staff)){
			var1.append("             AND vp.staff_id = (?) ");
			param.add(staff);
		}else {
			var1.append("             AND vp.staff_id IN ( ");
			for(int i = 0; i < staffList.size(); i++) {
				if(i != 0) {
					var1.append(",");
				}
				var1.append("?");
				param.add(staffList.get(i));
			}
			var1.append("             ) ");
		}
		var1.append("               AND r.shop_id = vp.shop_id) vp ");
		var1.append("         INNER JOIN (SELECT DISTINCT c.customer_id   AS CUSTOMER_ID, ");
		var1.append("                                     c.shop_id       AS CTSHOP_ID, ");
		var1.append("                                     c.short_code    AS CUSTOMER_CODE, ");
		var1.append("                                     c.customer_name AS CUSTOMER_NAME, ");
		var1.append("                                     c.name_text     AS CUSTOMER_NAME_ADDRESS_TEXT ");
		var1.append("                                     , ");
		var1.append("                    c.address       AS ADDRESS_ ");
		var1.append("                     FROM   customer c join shop s on c.shop_id = s.shop_id ");
		var1.append("                     WHERE  c.status = 1 and s.status = 1 ");
		if (!StringUtil.isNullOrEmpty(customerCode)) {
			customerCode = StringUtil.escapeSqlString(customerCode);
			var1.append("	and upper(c.SHORT_CODE) like upper(?) escape '^' ");
			param.add("%" + customerCode + "%");
		}
		if (!StringUtil.isNullOrEmpty(customerName)) {
			customerName = StringUtil.getEngStringFromUnicodeString(customerName);
			customerName = StringUtil.escapeSqlString(customerName);
			customerName = DatabaseUtils.sqlEscapeString("%" + customerName + "%");
			customerName = customerName.substring(1, customerName.length()-1);
			var1.append("	and upper(c.NAME_TEXT) like upper(?) escape '^' ");
			param.add(customerName);
		}
		if(!StringUtil.isNullOrEmpty(shopId)){
			var1.append("               AND c.shop_id IN ( ");
			for(int i = 0; i < shopList.size(); i++) {
				if(i != 0) {
					var1.append(",");
				}
				var1.append("?");
				param.add(shopList.get(i));
			}
			var1.append("               ) ");
		}
		var1.append("                    ) cus ");
		var1.append("                 ON vp.customer_id = cus.customer_id ) ");
		var1.append("ORDER  BY customer_code ");
		
		Cursor cTotalRow = null;
		TBHVCustomerListDTO dto = new TBHVCustomerListDTO();
		List<CustomerDTO> listCustomer = new ArrayList<CustomerDTO>();
		try {
			if (page > 0) {
				// get count
				if (isGetNumRow) {
					totalPageSql.append("select count(*) as TOTAL_ROW from (" + var1 + ")");
					cTotalRow = rawQueries(totalPageSql.toString(), param);
					if (cTotalRow != null) {
						if (cTotalRow.moveToFirst()) {
							dto.totalCustomer = cTotalRow.getInt(0);
						}
					}
				}
				var1.append(" limit " + Integer.toString(Constants.NUM_ITEM_PER_PAGE));
				var1.append(" offset " + Integer.toString((page - 1) * Constants.NUM_ITEM_PER_PAGE));
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTotalRow != null) {
					cTotalRow.close();
				}
			} catch (Exception ex) {
			}
		}
		Cursor c = null;
		try{
			c = rawQueries(var1.toString(), param);

			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerDTO item = new CustomerDTO();
						item.parseCustomerInfo(c);
						listCustomer.add(item);
					} while (c.moveToNext());
				}
				dto.listCustomer = listCustomer;
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception ex) {
			}
		}

		return dto;
	}

	/**
	 * 
	 * Lay danh sach GSBH, TTTT - MH theo doi vi tri NVBH, GSBH, TTTT role GST 
	 * 
	 * @author: YenNTH
	 * @param shopId
	 * @return
	 * @return: TBHVRouteSupervisionDTO
	 * @throws:
	 */
	public TBHVRouteSupervisionDTO getGsnppTtttPosition(String shopId) {
		TBHVRouteSupervisionDTO dto = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);

		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(shopId);
		//String shopStr = TextUtils.join(",", shopList);

		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append("SELECT DISTINCT s.staff_id                              AS GS_STAFF_ID, ");
		var1.append("                s.staff_code                            AS GS_STAFF_CODE, ");
		var1.append("                s.mobilephone                           AS GS_MOBILEPHONE, ");
		var1.append("                s.phone                                 AS GS_PHONE, ");
		var1.append("                s.staff_name                            AS GS_STAFF_NAME, ");
		var1.append("                sh.shop_id                              AS NVBH_SHOP_ID, ");
		var1.append("                sh.shop_code                            AS NVBH_SHOP_CODE, ");
		var1.append("                ct.object_type                          AS OBJECT_TYPE, ");
		var1.append("                STAFFPOSLOG.lat                         AS LAT, ");
		var1.append("                STAFFPOSLOG.lng                         AS LNG, ");
		var1.append("                Strftime('%H:%M', STAFFPOSLOG.datetime) AS DATE_TIME, ");
		var1.append("                ACTIONLOGSTAFF.CUS_NAME                 AS CUS_NAME, ");
		var1.append("                ACTIONLOGSTAFF.START_TIME               AS START_TIME, ");
		var1.append("                ACTIONLOGSTAFF.END_TIME                 AS END_TIME ");
		var1.append("FROM   staff s, ");
		var1.append("       channel_type ct, ");
		var1.append("       shop sh ");
		var1.append("       LEFT JOIN (SELECT staff_position_log_id, ");
		var1.append("                         staff_id, ");
		var1.append("                         Max(create_date) AS DATETIME, ");
		var1.append("                         lat, ");
		var1.append("                         lng ");
		var1.append("                  FROM   staff_position_log ");
		var1.append("                  WHERE  substr(create_date,1,10) >= ? ");
		params.add(date_now);
		var1.append("                         AND status = 1 ");
		var1.append("                  GROUP  BY staff_id) STAFFPOSLOG ");
		var1.append("              ON s.[staff_id] = STAFFPOSLOG.staff_id ");
		
		var1.append("       LEFT JOIN (SELECT staff.staff_id                AS STAFF_ID_1, ");
		var1.append("                         customer.customer_id, ");
		var1.append("                         ( Substr(customer.customer_code, 1, 3) ");
		var1.append("                           || '-' ");
		var1.append("                           || customer.customer_name ) AS CUS_NAME, ");
		var1.append("                         Max(action_log.start_time)    AS START_TIME, ");
		var1.append("                         action_log.end_time           AS END_TIME ");
		var1.append("                  FROM   action_log, ");
		var1.append("                         staff, ");
		var1.append("                         channel_type ct, ");
		var1.append("                         customer ");
		var1.append("                  WHERE  action_log.staff_id = staff.staff_id ");
		var1.append("                         AND substr(action_log.start_time,1,10) = ? ");
		params.add(date_now);
		var1.append("                         AND staff.status = 1 ");
		var1.append("                         AND staff.staff_type_id = ct.channel_type_id ");
		var1.append("                         AND ct.object_type IN ( 11, 5 ) ");
		var1.append("                         AND ct.type = 2 ");

		var1.append("                         AND action_log.object_type IN ( 0, 1 ) ");
		var1.append("                         AND customer.customer_id = action_log.customer_id ");
		var1.append("                  GROUP  BY action_log.staff_id) ACTIONLOGSTAFF ");
		var1.append("                 ON  s.staff_id = ACTIONLOGSTAFF.staff_id_1 ");
		
		var1.append("WHERE  ((s.staff_owner_id = ? ");
		params.add(Constants.STR_BLANK + GlobalInfo.getInstance().getProfile().getUserData().id);
		var1.append("       AND ct.object_type = 11 ) ");
		var1.append("		OR (s.STAFF_ID in(	");
		var1.append("	    	SELECT	");
		var1.append("	        	sgd.STAFF_ID	");
		var1.append("	    	FROM	");
		var1.append("	        	staff_group_detail sgd	");
		var1.append("	    	WHERE	");
		var1.append("	        	sgd.STAFF_GROUP_ID IN       (	");
		var1.append("	            	SELECT	");
		var1.append("	                	sg1.staff_group_id	");
		var1.append("	            	FROM	");
		var1.append("	                	staff_group sg1	");
		var1.append("	            	WHERE	");
		var1.append("	                	sg1.STAFF_ID = ?	");
		params.add(Constants.STR_BLANK + GlobalInfo.getInstance().getProfile().getUserData().id);
		var1.append("	                	AND sg1.status = 1	");
		var1.append("	                	AND sg1.GROUP_LEVEL = 3	");
		var1.append("	                	AND sg1.GROUP_TYPE = 4	");
		var1.append("	        ))	");
		var1.append("       AND ct.object_type = 5 )) ");
		var1.append("       AND s.staff_type_id = ct.channel_type_id ");
		var1.append("       AND s.shop_id IN ( ");
		for(int i = 0; i < shopList.size(); i++) {
			if(i != 0) {
				var1.append(",");
			}
			var1.append("?");
			params.add(shopList.get(i));
		}
		var1.append("               ) ");
		var1.append("       AND s.status = 1 ");
		var1.append("       AND ct.status = 1 ");
		var1.append("       AND s.shop_id = sh.shop_id ");
		var1.append(" ORDER BY s.staff_code, s.staff_name ");

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				dto = new TBHVRouteSupervisionDTO();
				if (c.moveToFirst()) {
					do {
						TBHVRouteSupervisionDTO.THBVRouteSupervisionItem item = dto.newTHBVRouteSupervisionItem();
						item.initItem(c);
						if(c.getString(c.getColumnIndex("OBJECT_TYPE")).equalsIgnoreCase(DisplayProgrameItemDTO.TYPE_GSNPP)){
							dto.itemGSNPPList.add(item);
						}else {
							dto.itemListTTTT.add(item);
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getListPositionSalePersons", e.getMessage());
			}
		}
		return dto;
	}

	/**
	 * Lay danh sach NVBH - MH theo doi vi tri NVBH, GSBH, TTTT role GST
	 * 
	 * @author: YenNTH
	 * @param staffId
	 * @return
	 * @return: SaleRoadMapSupervisorDTOvoid
	 * @throws:
	 */
	public TBHVRouteSupervisionDTO getNvbhPositionOfGsnpp(String staffId, String shopId) {
		TBHVRouteSupervisionDTO dto = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		ArrayList<String> param = new ArrayList<String>();

		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   (SELECT STAFF.staff_id                           AS GS_STAFF_ID, ");
		var1.append("               STAFF.staff_code                         AS GS_STAFF_CODE, ");
		var1.append("               STAFF.staff_name                         AS GS_STAFF_NAME, ");
		var1.append("               STAFF.shop_id                            AS SHOP_ID, ");
		var1.append("               STAFF.shop_code                          AS NVBH_SHOP_CODE, ");
		var1.append("               STAFF.shop_name                          AS NVBH_SHOP_NAME, ");
		var1.append("               STAFF.mobilephone                        AS GS_MOBILEPHONE, ");
		var1.append("               STAFF.phone                              AS GS_PHONE, ");
		var1.append("               STAFFPOSLOG.lat                          AS LAT, ");
		var1.append("               STAFFPOSLOG.lng                          AS LNG, ");
		var1.append("               Strftime('%H:%M', STAFFPOSLOG.date_time) AS DATE_TIME ");
		var1.append("        FROM   (SELECT * ");
		var1.append("                FROM   staff st, shop sh ");
		var1.append("                WHERE  st.staff_owner_id = ? ");
		param.add(Constants.STR_BLANK + staffId);
		var1.append("                       AND st.status = 1  ");
		var1.append("                       AND st.shop_id= sh.shop_id ");
		var1.append("                ORDER  BY staff_name) STAFF ");
		var1.append("               LEFT JOIN (SELECT staff_position_log_id, ");
		var1.append("                                 staff_id, ");
		var1.append("                                 Max(create_date) AS DATE_TIME, ");
		var1.append("                                 lat, ");
		var1.append("                                 lng ");
		var1.append("                          FROM   staff_position_log ");
		var1.append("                          WHERE  substr(create_date,1,10) >= ? ");
		param.add(date_now);
		var1.append("                         			AND status = 1 ");
		var1.append("                          GROUP  BY staff_id) STAFFPOSLOG ");
		var1.append("                      ON STAFF.staff_id = STAFFPOSLOG.staff_id) POSSTAFF ");
		var1.append("       LEFT JOIN (SELECT staff.staff_id                AS STAFF_ID_1, ");
		var1.append("                         customer.customer_id, ");
		var1.append("                         ( Substr(customer.customer_code, 1, 3) ");
		var1.append("                           || '-' ");
		var1.append("                           || customer.customer_name ) AS CUS_NAME, ");
		var1.append("                         Max(action_log.start_time)    AS START_TIME, ");
		var1.append("                         action_log.end_time           AS END_TIME ");
		var1.append("                  FROM   action_log, ");
		var1.append("                         staff, ");
		var1.append("                         channel_type ct, ");
		var1.append("                         customer ");
		var1.append("                  WHERE  action_log.staff_id = staff.staff_id ");
		var1.append("                         AND substr(action_log.start_time,1,10) = ? ");
		param.add(date_now);
		var1.append("                         AND staff.status = 1 ");
		// kiem tra loai nhan vien la presales || valsales
		var1.append("                         AND staff.staff_type_id = ct.channel_type_id ");
		var1.append("                         AND ct.object_type IN ( 1, 2 ) ");
		var1.append("                         AND ct.type = 2 ");

		var1.append("                         AND action_log.object_type IN ( 0, 1 ) ");
		var1.append("                         AND customer.customer_id = action_log.customer_id ");
		var1.append("                  GROUP  BY action_log.staff_id) ACTIONLOGSTAFF ");
		var1.append("              ON POSSTAFF.GS_STAFF_ID = ACTIONLOGSTAFF.staff_id_1 ");
		
		var1.append("LEFT JOIN (SELECT staff_name   AS NVBH_STAFF_NAME, ");
		var1.append("       		   staff_code   AS NVBH_STAFF_CODE ");
		var1.append("			FROM   staff ");
		var1.append("			WHERE  staff_id = ? ) ");
		param.add(Constants.STR_BLANK + staffId);

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				dto = new TBHVRouteSupervisionDTO();
				if (c.moveToFirst()) {
					do {
						TBHVRouteSupervisionDTO.THBVRouteSupervisionItem item = dto.newTHBVRouteSupervisionItem();
						item.initItem(c);
						dto.itemListNVBH.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getListPositionSalePersons", e.getMessage());
			}
		}
		return dto;
	}
	/**
	 * K tra da co log ghe tham trong ngay hay chua cua 1 KH
	 * 
	 * @author: TamPQ
	 * @param actionLog
	 * @return
	 * @return: booleanvoid
	 * @throws:
	 */
	public boolean alreadyHaveVisitLog(ActionLogDTO actionLog) {
		boolean alreadyHaveVisitLog = false;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT * ");
		var1.append("FROM   ACTION_LOG ");
		var1.append("WHERE  STAFF_ID = ? ");
		var1.append("       AND CUSTOMER_ID = ? ");
		var1.append("       AND OBJECT_TYPE IN ( 0, 1 ) ");
		var1.append("       AND DATE(START_TIME) = DATE(?) ");

		String[] params = { String.valueOf(actionLog.staffId), "" + actionLog.aCustomer.customerId, date_now };
		Cursor c = null;
		try {
			c = rawQuery(var1.toString(), params);
			if (c != null && c.getCount() > 0) {
				alreadyHaveVisitLog = true;
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		return alreadyHaveVisitLog;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param list
	 * @return
	 * @return: ArrayList<StaffItem>void
	 * @throws:
	 */
	public ListStaffDTO getPositionOfGsnppAndNvbh(String[] list) {
		ListStaffDTO staffList = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		StringBuffer var1 = new StringBuffer();
		ArrayList<String> params = new ArrayList<String>();
		var1.append("SELECT STAFF_ID, LAT, LNG, ");
		var1.append("       MAX(CREATE_DATE) ");
		var1.append("FROM   STAFF_POSITION_LOG ");
		if (list != null && list.length == 2) {
			var1.append("WHERE  (STAFF_ID = ? ");
			var1.append("        OR STAFF_ID = ?) ");
			params.add(list[0]);
			params.add(list[1]);
		} else if (list != null && list.length == 1) {
			var1.append("WHERE  STAFF_ID = ? ");
			params.add(list[0]);
		}
		var1.append("		AND DATE(CREATE_DATE) = DATE(?) ");
		params.add(date_now);
		var1.append("		AND STATUS = 1 ");
		var1.append("GROUP  BY STAFF_ID ");

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), params);
			if (c != null) {
				staffList = new ListStaffDTO();
				if (c.moveToFirst()) {
					do {
						staffList.addItem(c);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		return staffList;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param staffID 
	 * @return
	 * @return: TBHVVisitCustomerNotificationDTOvoid
	 * @throws Exception
	 * @throws:
	 */
	public TBHVVisitCustomerNotificationDTO getVisitCusNotification(String shopId, String staffID) throws Exception {
		String day = DateUtils.getToday();
		TBHVVisitCustomerNotificationDTO dto = new TBHVVisitCustomerNotificationDTO();
		String dateNow = DateUtils.now();
		Cursor c = null;

		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(shopId);
		//String shopStr = TextUtils.join(",", shopList);

		StringBuffer shopParamSql = new StringBuffer();
		shopParamSql.append("SELECT * FROM SHOP_PARAM WHERE SHOP_ID = ? AND TYPE IN ('DT_START', 'DT_MIDDLE', 'DT_END') AND STATUS = 1");
		Cursor cScore = null;
		try {
			cScore = rawQuery(shopParamSql.toString(), new String[] { String.valueOf(shopId) });
			if (cScore != null) {
				if (cScore.moveToFirst()) {
					do {
						if (dto != null) {
							ShopParamDTO item = new ShopParamDTO();
							item.initObjectWithCursor(cScore);
							dto.listParam.put(item.type, item);
						}
					} while (cScore.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			if (cScore != null) {
				cScore.close();
			}
		}

		String DT_START = "";
		String DT_MIDDLE = "";
		//String DT_END = "";
		if (dto.listParam.size() ==3) {
			DT_START = dto.listParam.get("DT_START").code;
			DT_MIDDLE = dto.listParam.get("DT_MIDDLE").code;
			//DT_END = dto.listParam.get("DT_END").code;
			ArrayList<String> param = new ArrayList<String>();
			StringBuffer var1 = new StringBuffer();
			var1.append("SELECT * ");
			var1.append("FROM   (SELECT GS.STAFF_ID          AS GS_STAFF_ID, ");
			var1.append("               GS.STAFF_CODE        AS GS_STAFF_CODE, ");
			var1.append("               GS.STAFF_NAME        AS GS_STAFF_NAME, ");
			var1.append("               SH.SHOP_ID           AS NVBH_SHOP_ID, ");
			var1.append("               SH.SHOP_CODE         AS NVBH_SHOP_CODE, ");
			var1.append("               COUNT(NVBH.STAFF_ID) AS NUM_NVBH ");
			var1.append("        FROM   STAFF as NVBH, ");
			var1.append("               CHANNEL_TYPE as CH, ");
			var1.append("               SHOP as SH, ");
			var1.append("               STAFF as GS, ");
			var1.append("               CHANNEL_TYPE as GS_CH ");
			var1.append("        WHERE  NVBH.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
			var1.append("               AND CH.TYPE = 2 ");
			var1.append("               AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
			var1.append("               AND NVBH.SHOP_ID = SH.SHOP_ID ");
			var1.append("               AND NVBH.STATUS = 1 ");
			var1.append("               AND SH.STATUS = 1 ");
			var1.append("               AND NVBH.SHOP_ID IN ( ");
			for(int i = 0; i < shopList.size(); i++) {
				if(i != 0) {
					var1.append(",");
				}
				var1.append("?");
				param.add(shopList.get(i));
			}
			var1.append("               ) ");
			var1.append("               AND NVBH.STAFF_OWNER_ID IS NOT NULL ");
//			var1.append("       		AND GS.STAFF_OWNER_ID = ? ");
//			param.add(staffID);
			var1.append("				AND GS.STAFF_ID in(	");
			var1.append("	    			SELECT	");
			var1.append("	        			sgd.STAFF_ID	");
			var1.append("	    			FROM	");
			var1.append("	        			staff_group_detail sgd	");
			var1.append("	    			WHERE	");
			var1.append("	        			sgd.STAFF_GROUP_ID IN       (	");
			var1.append("	            		SELECT	");
			var1.append("	                		sg1.staff_group_id	");
			var1.append("	            		FROM	");
			var1.append("	                		staff_group sg1	");
			var1.append("	            		WHERE	");
			var1.append("	                		sg1.STAFF_ID = ?	");
			param.add(staffID);
			var1.append("	                		AND sg1.status = 1	");
			var1.append("	                		AND sg1.GROUP_LEVEL = 3	");
			var1.append("	                		AND sg1.GROUP_TYPE = 4	");
			var1.append("	        ))	");
			var1.append("               AND NVBH.STAFF_OWNER_ID = GS.STAFF_ID ");
			var1.append("               AND GS.STAFF_TYPE_ID = GS_CH.CHANNEL_TYPE_ID ");
			var1.append("               AND GS_CH.TYPE = 2 ");
			var1.append("               AND GS_CH.OBJECT_TYPE = 5 ");
			var1.append("               AND GS.STATUS = 1 ");
			var1.append("        GROUP  BY SH.SHOP_ID, ");
			var1.append("                  NVBH.STAFF_OWNER_ID ");
			var1.append("        ORDER  BY SH.SHOP_CODE, ");
			var1.append("                  GS.STAFF_NAME) NPP_LIST ");
			var1.append("       LEFT JOIN (SELECT NPP_STAFF_ID                AS NPP_STAFF_ID_2, ");
			var1.append("                         NVBH_SHOP_CODE              AS NVBH_SHOP_CODE_1, ");
			var1.append("                         COUNT(CASE ");
			var1.append("                                 WHEN ( NUM0930 > 0 ");
			var1.append("                                         OR TOTAL_CUS_PLAN IS NULL ");
			var1.append("                                         OR TOTAL_CUS_PLAN = 0 ) THEN 1 ");
			var1.append("                               END)                  AS NUM930NUM, ");
			var1.append("                         GROUP_CONCAT(NVBH_STAFF_ID) AS STAFF_LIST, ");
			var1.append("                         GROUP_CONCAT(CASE ");
			var1.append("                                        WHEN TOTAL_CUS_PLAN IS NULL THEN 0 ");
			var1.append("                                        ELSE TOTAL_CUS_PLAN ");
			var1.append("                                      END)           AS TOTAL_CUS_PLAN_LIST, ");
			var1.append("                         GROUP_CONCAT(NUM1200)       AS NUM1200_LIST, ");
			var1.append("                         GROUP_CONCAT(NUM1600)       AS NUM1600_LIST, ");
			var1.append("                         GROUP_CONCAT(NUM_NOW)       AS NUM_NOW_LIST ");
			var1.append("                  FROM   (SELECT NPP_STAFF_ID, ");
			var1.append("                                 NVBH_SHOP_CODE, ");
			var1.append("                                 NVBH_STAFF_ID, ");
			var1.append("                                 TOTAL_CUS_PLAN, ");
			var1.append("                                 COUNT(CASE ");
			var1.append("                                         WHEN TIME(STRFTIME('%H:%M', START_TIME) ");
			var1.append("                                              ) <= ");
			var1.append("                                              TIME( ");
			var1.append("                                              STRFTIME('%H:%M', ?)) THEN 1 ");
			param.add(DT_START);
			var1.append("                                       END) AS NUM0930, ");
			var1.append("                                 COUNT(CASE ");
			var1.append("                                         WHEN TIME(STRFTIME('%H:%M', START_TIME) ");
			var1.append("                                              ) <= ");
			var1.append("                                              TIME( ");
			var1.append("                                              STRFTIME('%H:%M', ?)) THEN 1 ");
			param.add(DT_MIDDLE);
			var1.append("                                       END) AS NUM1200, ");
//			var1.append("                                 COUNT(CASE ");
//			var1.append("                                         WHEN TIME(STRFTIME('%H:%M', START_TIME) ");
//			var1.append("                                              ) <= ");
//			var1.append("                                              TIME( ");
//			var1.append("                                              STRFTIME('%H:%M', '" + DT_END + "')) ");
//			var1.append("                                       THEN 1 ");
//			var1.append("                                       END) AS NUM1600, ");
			var1.append("                                 COUNT(1) AS NUM1600, ");
			var1.append("                                 COUNT(CASE ");
			var1.append("                                         WHEN TIME(STRFTIME('%H:%M', START_TIME) ");
			var1.append("                                              ) <= ");
			var1.append("                                              TIME( ");
			var1.append("                                              STRFTIME('%H:%M', ?)) ");
			param.add(dateNow);
			var1.append("                                       THEN 1 ");
			var1.append("                                       END) AS NUM_NOW ");
			var1.append("                          FROM   (SELECT NVBH.*, ");
			var1.append("                                         NVBH_AL.START_TIME  AS START_TIME, ");
			var1.append("                                         PLAN.TOTAL_CUS_PLAN AS TOTAL_CUS_PLAN ");
			var1.append("                                  FROM   (SELECT ST.STAFF_ID       AS ");
			var1.append("                                                 NVBH_STAFF_ID, ");
			var1.append("                                                 ST.STAFF_OWNER_ID AS ");
			var1.append("                                                 NPP_STAFF_ID, ");
			var1.append("                                                 SHOP.SHOP_CODE    AS ");
			var1.append("                                                 NVBH_SHOP_CODE ");
			var1.append("                                          FROM   STAFF ST, ");
			var1.append("                                                 CHANNEL_TYPE CH, ");
			var1.append("                                                 SHOP ");
			var1.append("                                          WHERE  ST.STATUS = 1 ");
			var1.append("                                                 AND ST.STAFF_TYPE_ID = ");
			var1.append("                                                     CH.CHANNEL_TYPE_ID ");
			var1.append("                                                 AND CH.TYPE = 2 ");
			var1.append("                                                 AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
			var1.append("                                                 AND ST.SHOP_ID IN ( ");
			for(int i = 0; i < shopList.size(); i++) {
				if(i != 0) {
					var1.append(",");
				}
				var1.append("?");
				param.add(shopList.get(i));
			}
			var1.append("                                                     ) ");
			var1.append("                                                 AND SHOP.SHOP_ID = ST.SHOP_ID ");
			var1.append("                                                 AND SHOP.STATUS = 1 ");
			var1.append("                                                 AND ST.STAFF_OWNER_ID IS NOT ");
			var1.append("                                                     NULL) ");
			var1.append("                                         NVBH ");
			var1.append("                                         LEFT JOIN (SELECT ");
			var1.append("                                         STAFF_ID AS NVBH_AL_STAFF_ID ");
			var1.append("                                         , ");
			var1.append("       START_TIME ");
			var1.append("       FROM   ACTION_LOG ");
			var1.append("       WHERE  STAFF_ID = STAFF_ID ");
			var1.append("       AND OBJECT_TYPE IN ( 0, 1 ) ");
			var1.append("       AND IS_OR = 0 ");
			var1.append("       AND DATE(START_TIME) = DATE(?)) ");
			param.add(dateNow);
			var1.append("       NVBH_AL ");
			var1.append("       ON NVBH.NVBH_STAFF_ID = NVBH_AL.NVBH_AL_STAFF_ID ");
			var1.append("       LEFT JOIN (SELECT VP.STAFF_ID               AS STAFF_ID_1, ");
			var1.append("       COUNT(RTC.CUSTOMER_ID) AS ");
			var1.append("       TOTAL_CUS_PLAN ");
			var1.append("       FROM   VISIT_PLAN VP, ");
			var1.append("       ROUTING RT, ");
			var1.append("       (SELECT ROUTING_CUSTOMER_ID, ");
			var1.append("               ROUTING_ID, ");
			var1.append("               CUSTOMER_ID, ");
			var1.append("               STATUS, ");
	
			var1.append("               MONDAY, ");
			var1.append("               TUESDAY, ");
			var1.append("               WEDNESDAY, ");
			var1.append("               THURSDAY, ");
			var1.append("               FRIDAY, ");
			var1.append("               SATURDAY, ");
			var1.append("               SUNDAY, ");
			var1.append("               SEQ2, ");
			var1.append("               SEQ3, ");
			var1.append("               SEQ4, ");
			var1.append("               SEQ5, ");
			var1.append("               SEQ6, ");
			var1.append("               SEQ7, ");
			var1.append("               SEQ8 ");
			var1.append("        FROM   ROUTING_CUSTOMER ");
			var1.append("		WHERE (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL) AND ");
			param.add(dateNow);
			var1.append("                ( ");
			var1.append("                (WEEK1 IS NULL AND WEEK2 IS NULL AND WEEK3 IS NULL AND WEEK4 IS NULL) OR");
			var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
			param.add(dateNow);
			var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
			param.add(dateNow);
			var1.append("			and (");
			var1.append("		cast((case when strftime('%w', ?) = '0' ");
			param.add(dateNow);
			var1.append("									  then 7 ");
			var1.append("									  else strftime('%w', ?)                          ");
			param.add(dateNow);
			var1.append("									  end ) as integer) < ");
			var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
			var1.append("										  then 7 ");
			var1.append("										  else strftime('%w', start_date)                          ");
			var1.append("										  end ) as integer) ) ");
			var1.append("			then 1 else 0 end)) % 4 + 1)=1 and week1=1) or");
	
			var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
			param.add(dateNow);
			var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
			param.add(dateNow);
			var1.append("			and (");
			var1.append("		cast((case when strftime('%w', ?) = '0' ");
			param.add(dateNow);
			var1.append("									  then 7 ");
			var1.append("									  else strftime('%w', ?)                          ");
			param.add(dateNow);
			var1.append("									  end ) as integer) < ");
			var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
			var1.append("										  then 7 ");
			var1.append("										  else strftime('%w', start_date)                          ");
			var1.append("										  end ) as integer) ) ");
			var1.append("			then 1 else 0 end)) % 4 + 1)=2 and week2=1) or");
	
			var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
			param.add(dateNow);
			var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
			param.add(dateNow);
			var1.append("			and (");
			var1.append("		cast((case when strftime('%w', ?) = '0' ");
			param.add(dateNow);
			var1.append("									  then 7 ");
			var1.append("									  else strftime('%w', ?)                          ");
			param.add(dateNow);
			var1.append("									  end ) as integer) < ");
			var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
			var1.append("										  then 7 ");
			var1.append("										  else strftime('%w', start_date)                          ");
			var1.append("										  end ) as integer) ) ");
			var1.append("			then 1 else 0 end)) % 4 + 1)=3 and week3=1) or");
	
			var1.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
			param.add(dateNow);
			var1.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
			param.add(dateNow);
			var1.append("			and (");
			var1.append("		cast((case when strftime('%w', ?) = '0' ");
			param.add(dateNow);
			var1.append("									  then 7 ");
			var1.append("									  else strftime('%w', ?)                          ");
			param.add(dateNow);
			var1.append("									  end ) as integer) < ");
			var1.append("		 cast((case when strftime('%w', start_date) = '0' ");
			var1.append("										  then 7 ");
			var1.append("										  else strftime('%w', start_date)                          ");
			var1.append("										  end ) as integer) ) ");
			var1.append("			then 1 else 0 end)) % 4 + 1)=4 and week4=1) ) ");
			var1.append("                ) RTC, ");
			var1.append("       CUSTOMER CT ");
			var1.append("       WHERE  VP.ROUTING_ID = RT.ROUTING_ID ");
			var1.append("       AND RTC.ROUTING_ID = RT.ROUTING_ID ");
			var1.append("       AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
			var1.append("       AND DATE(VP.FROM_DATE) <= DATE(?) ");
			param.add(dateNow);
			var1.append("       AND ( VP.TO_DATE IS NULL ");
			var1.append("              OR DATE(VP.TO_DATE) >= DATE(?) ) ");
			param.add(dateNow);
			var1.append("       AND VP.STATUS = 1 ");
			var1.append("       AND RT.STATUS = 1 ");
			var1.append("       AND CT.STATUS = 1 ");
			var1.append("       AND RTC.STATUS = 1 ");
			var1.append("       AND RTC." + day + " = 1 ");
			var1.append("       GROUP  BY VP.STAFF_ID) PLAN ");
			var1.append("       ON NVBH.NVBH_STAFF_ID = PLAN.STAFF_ID_1) AL_NVBH ");
			var1.append("       GROUP  BY NVBH_STAFF_ID) ");
			var1.append("       GROUP  BY NPP_STAFF_ID, ");
			var1.append("       NVBH_SHOP_CODE) COUNT_NVBH ");
			var1.append("              ON ( NPP_LIST.GS_STAFF_ID = COUNT_NVBH.NPP_STAFF_ID_2 ");
			var1.append("                   AND NPP_LIST.NVBH_SHOP_CODE = COUNT_NVBH.NVBH_SHOP_CODE_1 ) ");
	
			try {
				c = rawQueries(var1.toString(), param);
				if (c != null) {
					if (c.moveToFirst()) {
						do {
							TBHVVisitCustomerNotificationItem item = dto.newTBHVVisitCustomerNotificationItem();
							item.initData(c);
							dto.arrList.add(item);
						} while (c.moveToNext());
					}
				}
			} catch (Exception ex) {
				throw ex;
			} finally {
				try {
					if (c != null) {
						c.close();
					}
				} catch (Exception e) {
				}
			}
		}

		return dto;
	}

	/**
	 * Lay ds cham cong cua gsnpp
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param ext
	 * @return
	 * @return: ListProductDTO
	 * @throws:
	 */

	public List<AttendanceDTO> getSupervisorAttendanceList(Bundle ext, LatLng shopPosition) {
		String shopId = ext.getString(IntentConstants.INTENT_SHOP_ID);
		String staffId = ext.getString(IntentConstants.INTENT_STAFF_ID);
		List<AttendanceDTO> list = new ArrayList<AttendanceDTO>();
//		List<AttendanceDTO> supList = new ArrayList<AttendanceDTO>();
		List<AttendanceDTO> resultList = new ArrayList<AttendanceDTO>();

		StringBuilder sql = new StringBuilder();

		sql.append(" SELECT ST.STAFF_ID, ST.STAFF_CODE, ");
		sql.append(" ST.STAFF_NAME, SPLTEMP.LAT, SPLTEMP.LNG, strftime('%Y-%m-%d %H:%M', SPLTEMP.CREATE_DATE) CREATE_DATE");
		sql.append(" FROM STAFF ST LEFT JOIN ");
		sql.append(" 	(SELECT LAT, LNG, CREATE_DATE, STAFF_ID  FROM STAFF_POSITION_LOG ");
		sql.append(" 	WHERE STAFF_ID IN (SELECT STAFF_ID FROM STAFF WHERE STATUS = 1 AND STAFF_OWNER_ID = ? AND SHOP_ID = ?) ");
		sql.append(" 	AND DATE(CREATE_DATE) = DATE(?) ) SPLTEMP ");
		sql.append(" 	ON ST.STAFF_ID = SPLTEMP.STAFF_ID ");
		sql.append(" 	WHERE ST.STAFF_ID IN (SELECT STAFF_ID FROM STAFF WHERE STATUS = 1 AND STAFF_OWNER_ID = ? AND SHOP_ID = ?) ");
		sql.append(" ORDER BY ST.STAFF_CODE, ST.STAFF_NAME, SPLTEMP.CREATE_DATE  ");

		String[] params = { staffId, shopId, DateUtils.now(), staffId, shopId};
		Cursor c = null;

		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						AttendanceDTO atDTO = new AttendanceDTO();
						atDTO.initWithCursor(c);
						atDTO.shopId = shopId;
						double cusDistance = -1;
						if(shopPosition.lat > 0 && shopPosition.lng > 0) {
							if (atDTO.position1.lat > 0 && atDTO.position1.lng > 0) {
								cusDistance = GlobalUtil.getDistanceBetween(atDTO.position1, shopPosition);
								atDTO.distance1 = cusDistance;
							}
							
							list.add(atDTO);
						}

						// add giam sat len dau tien
//						if (atDTO.staffId == Integer.parseInt(staffId)) {
//							supList.add(atDTO);
//						} else {
//						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		resultList.addAll(list);
		return resultList;
	}

	/**
	 * Lay ds gs cung voi shop tuong ung
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: TBHVVisitCustomerNotificationDTOvoid
	 * @throws:
	 */
	public List<ShopSupervisorDTO> getTBHListSupervisorShopManaged(String shopId) {
		List<ShopSupervisorDTO> list = new ArrayList<ShopSupervisorDTO>();
		Cursor c = null;

		SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
		ArrayList<String> shopList = shopTable.getShopRecursiveReverse(shopId);
		//String shopStr = TextUtils.join(",", shopList);

		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append("SELECT GS.STAFF_ID          AS GS_STAFF_ID, ");
		var1.append("       GS.STAFF_CODE        AS GS_STAFF_CODE, ");
		var1.append("       GS.STAFF_NAME        AS GS_STAFF_NAME, ");
		var1.append("       SH.SHOP_ID			 AS NVBH_SHOP_ID, ");
		var1.append("       SH.SHOP_CODE		 AS NVBH_SHOP_CODE, ");
		var1.append("       COUNT(NVBH.STAFF_ID) AS NUM_NVBH ");
		var1.append("FROM   STAFF NVBH, ");
		var1.append("       CHANNEL_TYPE CH, ");
		var1.append("       SHOP SH, ");
		var1.append("       STAFF GS, ");
		var1.append("       CHANNEL_TYPE GS_CH ");
		var1.append("WHERE  NVBH.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("       AND CH.TYPE = 2 ");
		var1.append("       AND CH.OBJECT_TYPE IN ( 1, 2 ) ");
		var1.append("       AND NVBH.SHOP_ID = SH.SHOP_ID ");
		var1.append("       AND NVBH.STATUS = 1 ");
		var1.append("       AND SH.STATUS = 1 ");
		var1.append("       AND NVBH.SHOP_ID IN ( ");
		for(int i = 0; i < shopList.size(); i++) {
			if(i != 0) {
				var1.append(",");
			}
			var1.append("?");
			param.add(shopList.get(i));
		}
		var1.append("       ) ");
		var1.append("       AND NVBH.STAFF_OWNER_ID IS NOT NULL ");
		var1.append("       AND NVBH.STAFF_OWNER_ID = GS.STAFF_ID ");
		var1.append("       AND GS.STAFF_TYPE_ID = GS_CH.CHANNEL_TYPE_ID ");
		var1.append("       AND GS_CH.TYPE = 2 ");
		var1.append("       AND GS_CH.OBJECT_TYPE = 5 ");
		var1.append("       AND GS.STATUS = 1 ");
//		var1.append("       AND GS.STAFF_OWNER_ID = ? ");
		var1.append("		AND GS.STAFF_ID in(	");
		var1.append("	    	SELECT	");
		var1.append("	        	sgd.STAFF_ID	");
		var1.append("	    	FROM	");
		var1.append("	        	staff_group_detail sgd	");
		var1.append("	    	WHERE	");
		var1.append("	        	sgd.STAFF_GROUP_ID IN       (	");
		var1.append("	            	SELECT	");
		var1.append("	                	sg1.staff_group_id	");
		var1.append("	            	FROM	");
		var1.append("	                	staff_group sg1	");
		var1.append("	            	WHERE	");
		var1.append("	                	sg1.STAFF_ID = ?	");
		param.add(Constants.STR_BLANK+GlobalInfo.getInstance().getProfile().getUserData().id);
		var1.append("	                	AND sg1.status = 1	");
		var1.append("	                	AND sg1.GROUP_LEVEL = 3	");
		var1.append("	                	AND sg1.GROUP_TYPE = 4	");
		var1.append("	        ))	");
		var1.append("GROUP  BY SH.SHOP_ID, ");
		var1.append("          NVBH.STAFF_OWNER_ID ");
		var1.append("ORDER  BY SH.SHOP_CODE, ");
		var1.append("          GS.STAFF_NAME ");

		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ShopSupervisorDTO item = new ShopSupervisorDTO();
						item.initWithCursor(c);
						list.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			try {
				throw ex;
			} catch (Exception e) {
			}
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return list;
	}

	/**
	 * Lay ds gs thuoc quyen quan ly cua 1 tbhv
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: LatLng
	 * @throws:
	 */

	public ArrayList<String> getShopRecursiveReverse(String parentShopId) {
		String sql = "SELECT SHOP_ID, PARENT_SHOP_ID FROM SHOP WHERE PARENT_SHOP_ID = ?";

		Cursor c = null;
		String[] params = { parentShopId };
		ArrayList<String> shopIdArray = new ArrayList<String>();
		if (!StringUtil.isNullOrEmpty(parentShopId) && !"null".equals(parentShopId)) {
			shopIdArray.add(parentShopId);
		}
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				while (c.moveToNext()) {
					// String id = c.getString(c.getColumnIndex(SHOP_ID));
					String shopId = c.getString(c.getColumnIndex(SHOP_ID));
					// shopIdArray.add(id);

					ArrayList<String> tempArray = getShopRecursiveReverse(shopId);
					shopIdArray.addAll(tempArray);
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return shopIdArray;
	}

	/**
	 * Lay action_log ghe tham trong ngay voi end_time = null
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: ArrayList<ActionLogDTO>void
	 * @throws:
	 */
	public ArrayList<ActionLogDTO> getVisitActionLogWithEndTimeIsNull(long staffId, long cusId) {
		ArrayList<ActionLogDTO> listActLog = new ArrayList<ActionLogDTO>();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append("SELECT * ");
		var1.append("FROM   ACTION_LOG ");
		var1.append("WHERE  STAFF_ID = ? ");
		param.add("" + staffId);
		var1.append("       AND CUSTOMER_ID != ? ");
		param.add("" + cusId);
		var1.append("       AND OBJECT_TYPE = 0 ");
		var1.append("       AND DATE(START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("       AND END_TIME IS NULL ");

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ActionLogDTO item = new ActionLogDTO();
						item.initDataFromCursor(c);
						listActLog.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		return listActLog;
	}

	/**
	 * 
	 * lay ds loai van de cho man hinh theo doi khac phuc
	 * 
	 * @author: YenNTH
	 * @param
	 * @return
	 * @return: ComboboxFollowProblemDTO
	 * @throws:
	 */
	public List<DisplayProgrameItemDTO> getComboboxProblemTypeProblem(boolean isGSNPP) {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder.append("SELECT AP.AP_PARAM_CODE, AP.AP_PARAM_NAME, AP.TYPE FROM AP_PARAM AP");
		if (isGSNPP) {
			stringbuilder.append(" WHERE (AP.TYPE LIKE 'FEEDBACK_TYPE_NVBH' OR AP.TYPE LIKE 'FEEDBACK_TYPE_GSNPP') AND STATUS = 1");
		} else {// tbhv
			stringbuilder.append(" WHERE (AP.TYPE LIKE 'FEEDBACK_TYPE_GSNPP' OR AP.TYPE LIKE 'FEEDBACK_TYPE_TTTT') AND STATUS = 1");
		}
		stringbuilder.append(" ORDER BY AP.AP_PARAM_NAME ");
		String requestGetFollowProblemList = stringbuilder.toString();
		Cursor c = null;
		String[] params = new String[] {};

		try {
			c = rawQuery(requestGetFollowProblemList, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						if (c.getColumnIndex("AP_PARAM_CODE") >= 0) {
							DisplayProgrameItemDTO comboboxNVBHDTO = new DisplayProgrameItemDTO();
							comboboxNVBHDTO.value = c.getString(c.getColumnIndex("AP_PARAM_CODE"));
							comboboxNVBHDTO.name = c.getString(c.getColumnIndex("AP_PARAM_NAME"));
							comboboxNVBHDTO.type = c.getString(c.getColumnIndex("TYPE"));
							result.add(comboboxNVBHDTO);
						}
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

	/**
	 * lay danh sach nvbh cua gsnpp
	 *
	 * @author thanhnguyen
	 * @param shopId
	 * @param staffOwnerId
	 * @return
	 */
	public ListStaffDTO getListNVBHForGSNPP(String shopId, String staffOwnerId, boolean orderByCode) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT DISTINCT s.staff_name     STAFF_NAME, ");
		sqlQuery.append("                s.staff_id 	  STAFF_ID, ");
		sqlQuery.append("                s.staff_code     STAFF_CODE ");
		sqlQuery.append("FROM   staff s ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append(" AND s.staff_type_id in (select channel_type_id from channel_type where type = 2 and status = 1 and object_type in (1,2)) ");
		sqlQuery.append(" AND s.status = 1");
		if (!StringUtil.isNullOrEmpty("" + shopId)) {
			sqlQuery.append(" AND s.shop_id = ? ");
			params.add("" + shopId);
		}

		if (!StringUtil.isNullOrEmpty("" + staffOwnerId)) {
			sqlQuery.append(" AND s.staff_owner_id in ( ");
			sqlQuery.append(staffOwnerId);
			sqlQuery.append(" )");
		}
		if (orderByCode) {
			sqlQuery.append(" ORDER  BY s.staff_code, s.staff_name ");
		} else {
			sqlQuery.append(" ORDER  BY s.staff_name ");
		}

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			dto = null;
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return dto;
	}
	
    /**
     * Lay danh sach PG cua TNPG
     * @author: duongdt3
     * @since: 09:14:08 13 Dec 2013
     * @return: ListStaffDTO
     * @throws:  
     * @param shopId
     * @param staffOwnerId
     * @param orderByCode
     * @return
     */
	public ListStaffDTO getListPGForTNPG(String shopId, String staffOwnerId, boolean orderByCode) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT DISTINCT s.staff_name     STAFF_NAME, ");
		sqlQuery.append("                s.staff_id 	  STAFF_ID, ");
		sqlQuery.append("                s.staff_code     STAFF_CODE ");
		sqlQuery.append("FROM   staff s ");
		sqlQuery.append("WHERE  1 = 1 ");
		//type PG = 12
		sqlQuery.append(" AND s.staff_type_id in (select channel_type_id from channel_type where type = 2 and status = 1 and object_type in (" + UserDTO.TYPE_PG + ")) ");
		//duongdt khong check status cua NV PG
		//sqlQuery.append(" AND s.status = 1");
		if (!StringUtil.isNullOrEmpty(shopId)) {
			sqlQuery.append(" AND s.shop_id = ? ");
			params.add(shopId);
		}

		if (!StringUtil.isNullOrEmpty(staffOwnerId)) {
			sqlQuery.append(" AND s.staff_owner_id in ( ");
			sqlQuery.append(staffOwnerId);
			sqlQuery.append(" )");
		}
		
		if (orderByCode) {
			sqlQuery.append(" ORDER  BY s.staff_code, s.staff_name ");
		} else {
			sqlQuery.append(" ORDER  BY s.staff_name ");
		}

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			dto = null;
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		return dto;
	}

	/**
	 * get report date & month for saleman
	 * @author: duongdt3
	 * @since: 15:02:08 21 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @return
	 */
	public GeneralStatisticsViewDTO getGeneralStatisticsForSaleman(
			String staffId, String shopId)throws Exception {
		
		GeneralStatisticsViewDTO result = new GeneralStatisticsViewDTO();
		try {
			//get progress info
			result.progressSoldInfo = getProgressInfo();
			
			//get report
			result.listInfo = getGeneralStatisticsInfoForSaleman(staffId, shopId);
			
		} catch (Exception e) {
			result = null;
			throw e;
		}
		return result;
	}
	
	/**
	 * get report date & month for GST
	 * @author: duongdt3
	 * @since: 10:03:24 29 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public GSTGeneralStatisticsViewDTO getGeneralStatisticsForGST(
			String staffId, String shopId)throws Exception {
		
			GSTGeneralStatisticsViewDTO result = new GSTGeneralStatisticsViewDTO();
			//get progress info
			result.progressSoldInfo = getProgressInfo();
			
			//SHOP_TABLE shopTable = new SHOP_TABLE(mDB);
			//ArrayList<String> arrShop = shopTable.getShopRecursiveReverse(shopId);
			//String listIDShop = TextUtils.join(",", arrShop); 
			
			//get report list GSBH
			result.listGsbhInfo = getGeneralStatisticsInfoListGSBHForGST(staffId);
			
			//get report TNPG
			result.listTNPGInfo = getGeneralStatisticsInfoListTNPGForGST(staffId);
		return result;
	}
	
	/**
	 * get report date & month for TTTT
	 * @author: duongdt3
	 * @since: 15:02:08 21 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param date 
	 * @param isLoadStaffList 
	 * @return
	 */
	public GeneralStatisticsTNPGViewDTO getGeneralStatisticsForTNPG(
			String staffId, String shopId, String idPG, String date, boolean isLoadStaffList)throws Exception {
		GeneralStatisticsTNPGViewDTO result = new GeneralStatisticsTNPGViewDTO();
		try {
			//get list PG hí hí
			if (isLoadStaffList) {
				result.listStaffInfo = getListPGForTNPG(shopId, staffId, false);
			}else{
				result.listStaffInfo = null;
			}
			result.reportInfo.customerInfo = getCustomerInfoOfPG(idPG, date);			
			//get progress info
			//mac dinh la ngay hien tai
			Date dateGetProgress = new Date();
			if (!StringUtil.isNullOrEmpty(date)) {
				dateGetProgress = DateUtils.parseDateFromString(date, DateUtils.defaultSqlDateFormat);
			}
			result.reportInfo.progressSoldInfo = getProgressInfo(dateGetProgress);
			
			//get report
			result.reportInfo.listInfo = getGeneralStatisticsInfoForTTTT(staffId, shopId, idPG, date);
			
		} catch (Exception e) {
			VTLog.d("getGeneralStatisticsForTNPG", e.getMessage());
			result = null;
		}
		return result;
	}
	
	/**
	 * get report date & month for Supervior (GSBH) 
	 * @author: duongdt3
	 * @since: 15:02:08 21 Nov 2013
	 * @return: GeneralStatisticsViewDTO
	 * @throws:  
	 * @param staffId
	 * @param shopId
	 * @param isLoadListStaff 
	 * @return
	 * @throws Exception 
	 */
	public GSBHGeneralStatisticsViewDTO getGeneralStatisticsForSupervior(
			String staffId, String shopId, String parrentStaffId, boolean isLoadListStaff) throws Exception {
		
		GSBHGeneralStatisticsViewDTO result = new GSBHGeneralStatisticsViewDTO();
		try {
			//default value
			result.progressSoldInfo = null;
			result.listStaffInShop  = null;
			
			//GST request
			if (!StringUtil.isNullOrEmpty(parrentStaffId) && isLoadListStaff) {
				result.listStaffInShop = getListStaffsOfShopReport_GST(parrentStaffId, UserDTO.TYPE_GSNPP);
				
			}
			result.progressSoldInfo = getProgressInfo();
			
			//get report
			result.listInfo = getGeneralStatisticsInfoForSupervior(staffId, shopId, parrentStaffId);
		} catch (Exception e) {
			result = null;
			throw e;
		}
		return result;
	}
	
	/**
	 * Lấy danh sách staff gs dưới quyền 1 staffOwnerId
	 * @author: duongdt3
	 * @since: 16:59:16 29 Nov 2013
	 * @return: List<StaffsOfShopInfo>
	 * @throws:  
	 * @param staffOwnerId
	 * @param typeGsStaff  
	 * @return
	 * @throws Exception 
	 */
	private List<StaffsOfShopInfo> getListStaffsOfShopReport_GST(String staffOwnerId, int typeGsStaff) throws Exception {	
		List<StaffsOfShopInfo> listResult = new ArrayList<StaffsOfShopInfo>();
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer stringBuilder = new StringBuffer();
		
		stringBuilder.append("		SELECT stList.STAFF_ID_GS STAFF_ID		");
		stringBuilder.append("			, stList.STAFF_CODE_GS STAFF_CODE		");
		stringBuilder.append("			, stList.STAFF_NAME_GS STAFF_NAME		");
		stringBuilder.append("			, sh.SHOP_ID SHOP_ID		");
		stringBuilder.append("			, sh.SHOP_CODE SHOP_CODE		");
		stringBuilder.append("			, sh.SHOP_NAME SHOP_NAME		");
		stringBuilder.append("		FROM (		");
		stringBuilder.append("			(SELECT * 	");
		stringBuilder.append("				FROM (SELECT S.STAFF_ID STAFF_ID_GS, S.STAFF_CODE STAFF_CODE_GS, S.STAFF_NAME STAFF_NAME_GS	");
		stringBuilder.append("						FROM STAFF S, CHANNEL_TYPE CH		");
		stringBuilder.append("						WHERE S.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID		");
		stringBuilder.append("							AND CH.TYPE = 2		");
		stringBuilder.append("							AND CH.OBJECT_TYPE IN (?)		");
		//ADD PARAM TYPE STAFF
		params.add(String.valueOf(typeGsStaff));
		stringBuilder.append("							AND CH.STATUS = 1		");
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
			stringBuilder.append("							AND S.STAFF_ID in(	");
			stringBuilder.append("	    						SELECT	");
			stringBuilder.append("	        						sgd.STAFF_ID	");
			stringBuilder.append("	    						FROM	");
			stringBuilder.append("	        						staff_group_detail sgd	");
			stringBuilder.append("	    						WHERE	");
			stringBuilder.append("	        						sgd.STAFF_GROUP_ID IN       (	");
			stringBuilder.append("	            					SELECT	");
			stringBuilder.append("	                					sg1.staff_group_id	");
			stringBuilder.append("	            					FROM	");
			stringBuilder.append("	                					staff_group sg1	");
			stringBuilder.append("	            					WHERE	");
			stringBuilder.append("	                					sg1.STAFF_ID = ?	");
			params.add(staffOwnerId);
			stringBuilder.append("	                					AND sg1.status = 1	");
			stringBuilder.append("	                					AND sg1.GROUP_LEVEL = 3	");
			stringBuilder.append("	                					AND sg1.GROUP_TYPE = 4	");
			stringBuilder.append("	        ))	");
		}else {
			stringBuilder.append("							AND S.STAFF_OWNER_ID = ?	");
			//ADD PARAM STAFF ID GST
			params.add(staffOwnerId);
		}
		stringBuilder.append("							AND S.STATUS = 1) stGSNPP, STAFF st	");
		stringBuilder.append("				WHERE 1 = 1	");
		stringBuilder.append("				AND stGSNPP.STAFF_ID_GS = st.STAFF_OWNER_ID	");
		stringBuilder.append("				AND EXISTS (	");
		stringBuilder.append("					SELECT *	");
		stringBuilder.append("					FROM STAFF stListType	");
		stringBuilder.append("						,CHANNEL_TYPE stype	");
		stringBuilder.append("					WHERE 1 = 1	");
		stringBuilder.append("						AND stListType.STAFF_ID = st.STAFF_ID	");
		stringBuilder.append("						AND stype.TYPE = 2	");
		stringBuilder.append("						AND stype.OBJECT_TYPE IN (1)	");
		stringBuilder.append("						AND stype.STATUS = 1	");
		stringBuilder.append("						AND stListType.STATUS = 1))	stList	");
		stringBuilder.append("			JOIN SHOP sh		");
		stringBuilder.append("				ON (stList.SHOP_ID = sh.SHOP_ID AND	sh.STATUS = 1)	");
		stringBuilder.append("		)	");
		stringBuilder.append("		WHERE 1 = 1		");
		stringBuilder.append("		GROUP BY stList.STAFF_ID_GS, sh.SHOP_ID 	");
		stringBuilder.append("		ORDER BY sh.SHOP_CODE ASC, sh.SHOP_NAME ASC, stList.STAFF_CODE ASC, stList.STAFF_NAME ASC		");

		Cursor c = null;
		try {
			String sql = stringBuilder.toString();
			c = this.rawQueries(sql, params);
			if (c != null && c.moveToFirst()) {
				do {
						StaffsOfShopInfo info = new StaffsOfShopInfo();
						info.initCursor(c);
						//add to list
						StaffsOfShopInfo.addItemToList(listResult, info);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			listResult = null;
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		
		return listResult;
	}

	/**
	 * get list report info for Saleman
	 * @author: duongdt3
	 * @since: 15:02:21 21 Nov 2013
	 * @return: List<GeneralStatisticsInfo>
	 * @throws:  
	 * @return
	 */
	private List<GeneralStatisticsInfo> getGeneralStatisticsInfoForSaleman(String staffId, String shopId) throws Exception{
		List<GeneralStatisticsInfo> listResult = new ArrayList<GeneralStatisticsInfo>();
		ArrayList<String> params = new ArrayList<String>();
		String dateNow= DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateFirstMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		String dateLastMonth = DateUtils.getLastDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,0);
		StringBuffer stringBuilder = new StringBuffer();
		stringBuilder.append("	SELECT rpt_sale.product_id OBJECT_ID	");
		stringBuilder.append("		,pd.PRODUCT_CODE OBJECT_CODE	");
		stringBuilder.append("		,pd.PRODUCT_NAME OBJECT_NAME	");
		stringBuilder.append("      ,( CASE ");
		stringBuilder.append("         WHEN pd.SORT_ORDER IS NULL THEN 'z' ");
		stringBuilder.append("         ELSE SORT_ORDER ");
		stringBuilder.append("         END )        AS SORT_ORDER ");
		stringBuilder.append("		,param.ap_param_name DVT	");
		stringBuilder.append("		,rpt_sale.CONVFACT CONVFACT	");
		stringBuilder.append("		,rpt_sale.SALE_IN_DATE SALE_IN_DATE	");
		stringBuilder.append("		,rpt_sale.DAY_PLAN DATE_PLAN	");
		stringBuilder.append("		,rpt_sale.SALE_IN_MONTH SALE_IN_MONTH	");
		stringBuilder.append("		,rpt_sale.MONTH_PLAN MONTH_PLAN	");
		stringBuilder.append("	FROM (	");
		stringBuilder.append("		SELECT rpt_sale.staff_id staff_id	");
		stringBuilder.append("			,rpt_sale.CONVFACT CONVFACT	");
		stringBuilder.append("			,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN rpt_sale.DAY_QUANTITY	");
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) SALE_IN_DATE	");
		stringBuilder.append("			,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN cast(cast(MONTH_PLAN as DOUBLE) / strftime('%d', ?) + 0.5 AS INTEGER)	");
		params.add(dateLastMonth);
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) DAY_PLAN	");
		stringBuilder.append("			,rpt_sale.MONTH_QUANTITY SALE_IN_MONTH	");
		stringBuilder.append("			,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN rpt_sale.MONTH_PLAN	");
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) MONTH_PLAN	");
		stringBuilder.append("			,rpt_sale.PRODUCT_ID PRODUCT_ID	");
		stringBuilder.append("		FROM RPT_SALE_STATISTIC rpt_sale	");
		stringBuilder.append("		WHERE 1 = 1	");
		
		if (!StringUtil.isNullOrEmpty(staffId)) {
			stringBuilder.append("			AND rpt_sale.STAFF_ID = ?	");
			params.add(staffId);
		}
		
		if (!StringUtil.isNullOrEmpty(shopId)) {
			stringBuilder.append("			AND rpt_sale.SHOP_ID = ?	");
			params.add(shopId);
		}
		//stringBuilder.append("			--kiem tra thoi gian trong thang    	");
		stringBuilder.append("			AND DATE (rpt_sale.RPT_IN_DATE,'start of month') = ?	");
		params.add(dateFirstMonth);
		//stringBuilder.append("			--kiem tra thoi gian nay phai la MAX trong thang {SP + NV} 	");
		stringBuilder.append("			AND rpt_sale.RPT_IN_DATE = (	");
		stringBuilder.append("				SELECT MAX(rpt_Max.RPT_IN_DATE)	");
		stringBuilder.append("				FROM RPT_SALE_STATISTIC rpt_Max	");
		stringBuilder.append("				WHERE rpt_Max.PRODUCT_ID = rpt_sale.PRODUCT_ID	");
		stringBuilder.append("					AND rpt_Max.STAFF_ID = rpt_sale.STAFF_ID	");
		stringBuilder.append("					AND DATE (rpt_Max.RPT_IN_DATE,'start of month') = ?)	");
		params.add(dateFirstMonth);
		stringBuilder.append("			AND (substr(rpt_sale.rpt_in_date,1,10) = ? OR rpt_sale.MONTH_QUANTITY > 0 )	");
		params.add(dateNow);
		stringBuilder.append("		) rpt_sale	");
		stringBuilder.append("	JOIN PRODUCT pd ON rpt_sale.product_id = pd.product_id	");
		stringBuilder.append("	JOIN AP_PARAM param ON (	");
		stringBuilder.append("			param.AP_PARAM_CODE = pd.UOM2	");
		stringBuilder.append("			AND param.TYPE = 'UOM2'	");
		stringBuilder.append("			AND param.STATUS = 1	");
		stringBuilder.append("			)	");
		stringBuilder.append("	ORDER BY SORT_ORDER, pd.PRODUCT_CODE ASC	");
		stringBuilder.append("		,pd.PRODUCT_NAME ASC	");
		
		Cursor c = null;
		try {
			String sql = stringBuilder.toString();
			c = this.rawQueries(sql, params);
			if (c != null && c.moveToFirst()) {
				do {
					GeneralStatisticsInfo info = new GeneralStatisticsInfo();
					info.initCursorForSaleman(c);
					listResult.add(info);
					
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			listResult = null;
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listResult;
	}
	
	/**
	 * get list report info of list GSBH for GST
	 * @author: duongdt3
	 * @since: 10:05:03 29 Nov 2013
	 * @return: List<GeneralStatisticsInfo>
	 * @throws:  
	 * @param staffId
	 * @return
	 * @throws Exception
	 */
	private List<GeneralStatisticsInfo> getGeneralStatisticsInfoListGSBHForGST(String staffId) throws Exception{
		List<GeneralStatisticsInfo> listResult = new ArrayList<GeneralStatisticsInfo>();
		ArrayList<String> params = new ArrayList<String>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		StringBuffer stringBuilder = new StringBuffer();
//		String startOfMonth = DateUtils
//				.getFirstDateOfNumberPreviousMonthWithFormat(
//						DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		String lastOfMonth = DateUtils
				.getLastDateOfNumberPreviousMonthWithFormat(
						DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		String month = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_MONTH);
		
		stringBuilder.append("	SELECT stList.STAFF_ID_GS OBJECT_ID		");
		stringBuilder.append("				, sh.SHOP_CODE OBJECT_CODE		");
		stringBuilder.append("				, stList.STAFF_NAME_GS OBJECT_NAME		");
		stringBuilder.append("				, SUM(IFNULL(rpt_sale.SALE_IN_DATE, 0)) SALE_IN_DATE		");
		stringBuilder.append("				, SUM(IFNULL(rpt_sale.DAY_PLAN, 0)) DATE_PLAN		");
		stringBuilder.append("				, SUM(IFNULL(rpt_sale.SALE_IN_MONTH, 0)) SALE_IN_MONTH		");
		stringBuilder.append("				, SUM(IFNULL(rpt_sale.MONTH_PLAN, 0)) MONTH_PLAN		");
		stringBuilder.append("				, sh.SHOP_ID SHOP_ID		");
		stringBuilder.append("				, sh.SHOP_CODE SHOP_CODE		");
		stringBuilder.append("				, sh.SHOP_NAME SHOP_NAME		");
		stringBuilder.append("		FROM (		");
		stringBuilder.append("			(SELECT stGSNPP.*,st.STAFF_ID STAFF_ID, st.SHOP_ID SHOP_ID 	");
		stringBuilder.append("				FROM (SELECT S.STAFF_ID STAFF_ID_GS, S.STAFF_CODE STAFF_CODE_GS, S.STAFF_NAME STAFF_NAME_GS	");
		stringBuilder.append("						FROM STAFF S, CHANNEL_TYPE CH		");
		stringBuilder.append("						WHERE S.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID		");
		stringBuilder.append("							AND CH.TYPE = 2		");
		stringBuilder.append("							AND CH.OBJECT_TYPE IN (5)		");
		stringBuilder.append("							AND CH.STATUS = 1		");
//		stringBuilder.append("							AND S.STAFF_OWNER_ID = ? 	");
//		params.add(staffId);
		stringBuilder.append("							AND S.STAFF_ID in(	");
		stringBuilder.append("	    						SELECT	");
		stringBuilder.append("	        						sgd.STAFF_ID	");
		stringBuilder.append("	    						FROM	");
		stringBuilder.append("	        						staff_group_detail sgd	");
		stringBuilder.append("	    						WHERE	");
		stringBuilder.append("	        						sgd.STAFF_GROUP_ID IN       (	");
		stringBuilder.append("	            					SELECT	");
		stringBuilder.append("	                					sg1.staff_group_id	");
		stringBuilder.append("	            					FROM	");
		stringBuilder.append("	                					staff_group sg1	");
		stringBuilder.append("	            					WHERE	");
		stringBuilder.append("	                					sg1.STAFF_ID = ?	");
		params.add(staffId);
		stringBuilder.append("	                					AND sg1.status = 1	");
		stringBuilder.append("	                					AND sg1.GROUP_LEVEL = 3	");
		stringBuilder.append("	                					AND sg1.GROUP_TYPE = 4	");
		stringBuilder.append("	        )	");
		stringBuilder.append("	        AND sgd.status = 1	");
		stringBuilder.append("	    )	");
		stringBuilder.append("							AND S.STATUS = 1) stGSNPP, STAFF st	");
		stringBuilder.append("				WHERE 1 = 1	");
		stringBuilder.append("				AND stGSNPP.STAFF_ID_GS = st.STAFF_OWNER_ID	");
		stringBuilder.append("				AND EXISTS (	");
		stringBuilder.append("					SELECT stListType.STAFF_ID	");
		stringBuilder.append("					FROM STAFF stListType	");
		stringBuilder.append("						,CHANNEL_TYPE stype	");
		stringBuilder.append("					WHERE 1 = 1	");
		stringBuilder.append("						AND stListType.STAFF_ID = st.STAFF_ID	");
		stringBuilder.append("						AND stype.TYPE = 2	");
		stringBuilder.append("						AND stype.OBJECT_TYPE IN (1)	");
		stringBuilder.append("						AND stype.STATUS = 1	");
		stringBuilder.append("						AND stListType.STATUS = 1))	stList	");
		stringBuilder.append("			LEFT JOIN 	");
		stringBuilder.append("			(SELECT STAFF_ID STAFF_ID_NV	");
		stringBuilder.append("				,SUM(SALE_IN_DATE) SALE_IN_DATE	");
		stringBuilder.append("				,SUM(DAY_PLAN) DAY_PLAN	");
		stringBuilder.append("				,SUM(SALE_IN_MONTH) SALE_IN_MONTH	");
		stringBuilder.append("				,SUM(MONTH_PLAN) MONTH_PLAN	");
		stringBuilder.append("			FROM (	");
		stringBuilder.append("				SELECT rpt_sale.staff_id staff_id	");
		stringBuilder.append("					,(CASE WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ? 	");
		params.add(dateNow);
		stringBuilder.append("						THEN rpt_sale.DAY_QUANTITY 	");
		stringBuilder.append("						ELSE 0 END) SALE_IN_DATE	");
		stringBuilder.append("				,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN cast(cast(MONTH_PLAN as DOUBLE) / strftime('%d', ?) + 0.5 AS INTEGER)	");
		params.add(lastOfMonth);
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) DAY_PLAN	");
		stringBuilder.append("				,rpt_sale.MONTH_QUANTITY SALE_IN_MONTH	");
		stringBuilder.append("				,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN rpt_sale.MONTH_PLAN	");
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) MONTH_PLAN	");
		stringBuilder.append("				,rpt_sale.PRODUCT_ID PRODUCT_ID	");
		stringBuilder.append("				FROM RPT_SALE_STATISTIC rpt_sale	");
		stringBuilder.append("				WHERE 1 = 1	");
		//stringBuilder.append("					--kiem tra thoi gian trong thang    	");
		stringBuilder.append("					AND SUBSTR(rpt_sale.rpt_in_date,1,7) = ?	");
		params.add(month);
		//stringBuilder.append("					--kiem tra thoi gian nay phai la MAX trong thang {SP + NV} 	");
		stringBuilder.append("					AND rpt_sale.rpt_in_date = (	");
		stringBuilder.append("						SELECT  MAX(rpt_Max.rpt_in_date)	");
		stringBuilder.append("						FROM RPT_SALE_STATISTIC rpt_Max	");
		stringBuilder.append("						WHERE rpt_Max.PRODUCT_ID = rpt_sale.PRODUCT_ID	");
		stringBuilder.append("						AND rpt_Max.STAFF_ID = rpt_sale.STAFF_ID 	");
		stringBuilder.append("						AND SUBSTR(rpt_Max.RPT_IN_DATE,1,7) = ?)	");
		params.add(month);
		stringBuilder.append("					AND (substr(rpt_sale.rpt_in_date,1,10) = ? OR rpt_sale.MONTH_QUANTITY > 0 )	");
		params.add(dateNow);
		stringBuilder.append("					AND EXISTS (	");
		stringBuilder.append("						SELECT pd.PRODUCT_ID 	");
		stringBuilder.append("						FROM PRODUCT pd ,AP_PARAM param	");
		stringBuilder.append("						WHERE 1=1	");
		stringBuilder.append("							AND pd.PRODUCT_ID = rpt_sale.PRODUCT_ID	");
		stringBuilder.append("							AND param.AP_PARAM_CODE = pd.UOM2	");
		stringBuilder.append("							AND param.STATUS = 1	");
		stringBuilder.append("							AND param.TYPE = 'UOM2')	");
		stringBuilder.append("				)	");
		stringBuilder.append("				GROUP BY staff_id	");
		stringBuilder.append("			) rpt_sale	");
		stringBuilder.append("			ON rpt_sale.STAFF_ID_NV = stList.STAFF_ID	");
		stringBuilder.append("			JOIN SHOP sh		");
		stringBuilder.append("				ON (stList.SHOP_ID = sh.SHOP_ID AND	sh.STATUS = 1)	");
		stringBuilder.append("		)	");
		stringBuilder.append("		WHERE 1 = 1	");
		stringBuilder.append("		GROUP BY stList.STAFF_ID_GS, sh.SHOP_ID 	");
		stringBuilder.append("		ORDER BY sh.SHOP_CODE ASC, stList.STAFF_NAME_GS ASC		");

		Cursor c = null;
		try {
			String sql = stringBuilder.toString();
			c = this.rawQueries(sql, params);
			if (c != null && c.moveToFirst()) {
				do {
					GeneralStatisticsInfo info = new GeneralStatisticsInfo();
					info.initCursorForSaleman(c);
					listResult.add(info);
					
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			listResult = null;
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listResult;
	}
	
	/**
	 * get list report info of list TNPG for GST
	 * @author: duongdt3/ HoanPD1
	 * @since: 10:05:03 29 Nov 2013
	 * @return: List<GeneralStatisticsInfo>
	 * @throws:  
	 * @param staffId
	 * @return
	 * @throws Exception
	 */
	private List<GeneralStatisticsInfo> getGeneralStatisticsInfoListTNPGForGST(String staffId) throws Exception{
		List<GeneralStatisticsInfo> listResult = new ArrayList<GeneralStatisticsInfo>();
		String dateNow=  DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		int month = DateUtils.getCurrentTimeByTimeType(Calendar.MONTH);
		int year = DateUtils.getCurrentTimeByTimeType(Calendar.YEAR);
		
		String lastOfMonth = DateUtils
				.getLastDateOfNumberPreviousMonthWithFormat(
						DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		String stringMonth = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_MONTH);
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer  var1 = new StringBuffer();
		var1.append(" select st.staff_id OBJECT_ID , ");
		var1.append("        st.STAFF_CODE OBJECT_CODE , ");
		var1.append("        st.STAFF_NAME OBJECT_NAME , ");
		var1.append("        rpt.SALE_IN_DATE SALE_IN_DATE , ");
		var1.append("        st.DATE_PLAN  DATE_PLAN, ");
		var1.append("        rpt.SALE_IN_MONTH SALE_IN_MONTH , ");
		var1.append("        st.MONTH_PLAN MONTH_PLAN ,        ");
		var1.append("        st.SHOP_ID SHOP_ID , ");
		var1.append("        st.SHOP_CODE SHOP_CODE , ");
		var1.append("        st.SHOP_NAME SHOP_NAME ");
		var1.append(" from ");
		var1.append("       (SELECT S.staff_id, ");
		var1.append("               s.staff_code, ");
		var1.append("               s.staff_name,  ");
		var1.append("       (select sum(DATE_PLAN)   ");
		var1.append("        from (SELECT cast(cast(QUANTITY AS DOUBLE) ");
		var1.append("						/strftime('%d', ?) ");
		var1.append("												+ 0.5 AS integer) DATE_PLAN    ");  
		params.add(lastOfMonth);
		var1.append("              FROM SALE_PLAN   ");
		var1.append("              WHERE TYPE = 2   ");
		var1.append("                    AND MONTH = ?   ");
		params.add(String.valueOf(month));
		var1.append("                    AND YEAR = ?   ");
		params.add(String.valueOf(year));
		var1.append("                    AND OBJECT_TYPE = 2   ");
		var1.append("                    AND OBJECT_ID = S.staff_id   ");         
		var1.append("              group by product_id)) DATE_PLAN,   ");
		var1.append("                 (SELECT SUM(QUANTITY) ");
		var1.append("                  FROM SALE_PLAN ");
		var1.append("                  WHERE TYPE = 2 ");
		var1.append("                        AND MONTH = ? ");
		params.add(String.valueOf(month));
		var1.append("                        AND YEAR = ? ");
		params.add(String.valueOf(year));
		var1.append("                        AND OBJECT_TYPE = 2 ");
		var1.append("                        AND OBJECT_ID = S.staff_id) MONTH_PLAN,  ");
		var1.append("               sh.shop_id as SHOP_ID,  ");              
		var1.append("               sh.shop_code as SHOP_CODE, ");               
		var1.append("               sh.shop_name as SHOP_NAME  ");               
		var1.append("        FROM STAFF S, ");
		var1.append("             CHANNEL_TYPE CH,   ");            
		var1.append("             shop sh ");
		var1.append("        WHERE S.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID    ");      
		var1.append("              and sh.shop_id = s.shop_id ");
		var1.append("              AND CH.TYPE = 2 ");
		var1.append("              AND CH.OBJECT_TYPE IN ( 11 ) ");
		var1.append("              AND CH.STATUS = 1 ");
		var1.append("              AND S.STATUS = 1 ");
		var1.append("              AND S.STAFF_OWNER_ID = ?) st  ");
		params.add(staffId); 
		var1.append(" left join  ");
		var1.append("         (select  PARENT_STAFF_ID,  ");
		var1.append("                 SUM((CASE WHEN ((DAY_QUANTITY % convfact) > CONVFACT2 AND DAY_QUANTITY >0) THEN (cast(DAY_QUANTITY AS INT) / convfact) + 1 ELSE (cast(DAY_QUANTITY AS INT) / convfact) END)) SALE_IN_DATE ,  ");
		var1.append("                 SUM((CASE WHEN ((MONTH_QUANTITY % convfact) > CONVFACT2 AND MONTH_QUANTITY > 0)  THEN (cast(MONTH_QUANTITY AS INT) / convfact) + 1 ELSE (cast(MONTH_QUANTITY AS INT) / convfact) END)) SALE_IN_MONTH , ");         
		var1.append("                 SHOP_ID ");
		var1.append("         from (select SUM(CASE WHEN substr(rss.rpt_in_date, 1, 10) = ? THEN rss.DAY_PG_QUANTITY ELSE 0 END) DAY_QUANTITY ,  ");   
		params.add(dateNow);     
		var1.append("                               sum(rss.month_pg_quantity) as MONTH_QUANTITY,        ");
		var1.append("                               rss.parent_staff_id as PARENT_STAFF_ID, ");
		var1.append("                               rss.SHOP_ID, ");
		var1.append("                               rss.PRODUCT_ID, ");    
		var1.append("                               cast(rss.convfact AS INT) CONVFACT, ");
		var1.append("                               (cast(rss.convfact AS INT)/2) CONVFACT2   ");            
		var1.append("               from rpt_sale_statistic rss       "); 
		var1.append("               where 1=1  ");
		var1.append("                     and rss.parent_staff_id in (SELECT S.staff_id ");
		var1.append("                                                 FROM STAFF S, ");
		var1.append("                                                      CHANNEL_TYPE CH ");
		var1.append("                                                 WHERE S.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID ");
		var1.append("                                                       AND CH.TYPE = 2 ");
		var1.append("                                                       AND CH.OBJECT_TYPE IN ( 11 ) ");
		var1.append("                                                       AND CH.STATUS = 1 ");
		var1.append("                                                       AND S.STATUS = 1 ");
		var1.append("                                                       AND S.STAFF_OWNER_ID = ? ) ");
		params.add(staffId);
		var1.append("                     and substr (rss.rpt_in_date,1,7) = ? ");
		params.add(stringMonth);
		var1.append("                     AND rss.rpt_in_date = ( SELECT MAX(rpt_Max.rpt_in_date) ");
		var1.append("                                             FROM RPT_SALE_STATISTIC rpt_Max ");
		var1.append("                                             WHERE rpt_Max.PRODUCT_ID = rss.PRODUCT_ID ");
		var1.append("                                                   AND rpt_Max.STAFF_ID = rss.STAFF_ID ");
		var1.append("                                                   AND substr (rpt_Max.rpt_in_date,1,7) = ?) ");   
		params.add(stringMonth);                                 
		var1.append("               GROUP BY PARENT_STAFF_ID, ");
		var1.append("                                 product_id)) RPT ON rpt.PARENT_STAFF_ID = st.staff_id and st.shop_id = rpt.shop_id    ");                             
		var1.append(" WHERE 1 = 1 ");
		var1.append(" ORDER BY st.STAFF_CODE ASC, ");
		var1.append("          st.STAFF_NAME ASC ");
		Cursor c = null;
		try {
			String sql = var1.toString();
			c = this.rawQueries(sql, params);
			if (c != null && c.moveToFirst()) {
				do {
					GeneralStatisticsInfo info = new GeneralStatisticsInfo();
					info.initCursorForSaleman(c);
					listResult.add(info);
					
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			listResult = null;
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listResult;
	}
	
	
	/**
	 * get list report info for TTTT (tổ trưởng tiếp thị)
	 * @author: duongdt3
	 * @since: 15:02:21 21 Nov 2013
	 * @return: List<GeneralStatisticsInfo>
	 * @throws:  
	 * @return
	 */
	private List<GeneralStatisticsViewDTO.GeneralStatisticsInfo> getGeneralStatisticsInfoForTTTT(String staffId, String shopId, String idPG, String date) throws Exception{
		List<GeneralStatisticsViewDTO.GeneralStatisticsInfo> listResult = new ArrayList<GeneralStatisticsViewDTO.GeneralStatisticsInfo>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		StringBuffer sqlObject = new StringBuffer();
		ArrayList<String> paramsObject = new ArrayList<String>();
		String stringMonth = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_MONTH);
		String lastOfMonth = DateUtils
				.getLastDateOfNumberPreviousMonthWithFormat(
						DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		
		Date parseDate = DateUtils.parseDateFromString(date, DateUtils.DATE_FORMAT_SQL_DEFAULT);
		Calendar calender = Calendar.getInstance();
		calender.setTime(parseDate);
		int month = calender.get(Calendar.MONTH) + 1;
		int year = calender.get(Calendar.YEAR);
		//nếu lấy tất cả PG, = "2", nếu lấy từng PG = "1"
		String sqlType = StringUtil.isNullOrEmpty(idPG) ? "2" : "1";
		
		sqlObject.append("	SELECT rpt_sale.OBJECT_ID OBJECT_ID	");
		sqlObject.append("		,rpt_sale.PG_SALE_IN_DATE PG_SALE_IN_DATE	");
		sqlObject.append("		,rpt_sale.PG_SALE_IN_MONTH PG_SALE_IN_MONTH	");
		sqlObject.append("		,rpt_sale.DATE_PLAN DATE_PLAN	");
		sqlObject.append("		,rpt_sale.DATE_PLAN_LAST DATE_PLAN_LAST	");
		sqlObject.append("		,rpt_sale.MONTH_PLAN MONTH_PLAN	");
		sqlObject.append("		,rpt_sale.MONTH_PLAN_LAST MONTH_PLAN_LAST	");
		sqlObject.append("		,rpt_sale.IS_DATE_REQUEST_IN_MONTH IS_DATE_REQUEST_IN_MONTH	");
		sqlObject.append("		,pd.PRODUCT_CODE OBJECT_CODE	");
		sqlObject.append("		,pd.PRODUCT_NAME OBJECT_NAME	");
		sqlObject.append("		,param.ap_param_name DVT	");
		sqlObject.append("      ,( CASE ");
		sqlObject.append("          WHEN pd.SORT_ORDER IS NULL THEN 'z' ");
		sqlObject.append("          ELSE SORT_ORDER ");
		sqlObject.append("          END )        AS SORT_ORDER ");
		sqlObject.append("		,pd.CONVFACT CONVFACT	");
		sqlObject.append("	FROM (	");
		sqlObject.append("		SELECT OBJECT_ID	");
		sqlObject.append("			,SUM(PG_SALE_IN_DATE) PG_SALE_IN_DATE	");
		sqlObject.append("			,SUM(PG_SALE_IN_MONTH) PG_SALE_IN_MONTH	");
		sqlObject.append("			,SUM(CASE	");
		sqlObject.append("					WHEN typeSql = ?	");
		paramsObject.add(sqlType);
		sqlObject.append("						THEN DATE_PLAN	");
		sqlObject.append("					ELSE 0	");
		sqlObject.append("					END) DATE_PLAN	");
		sqlObject.append("			,SUM(CASE	");
		sqlObject.append("					WHEN typeSql = ?	");
		paramsObject.add(sqlType);
		sqlObject.append("						THEN DATE_PLAN_LAST	");
		sqlObject.append("					ELSE 0	");
		sqlObject.append("					END) DATE_PLAN_LAST	");
		sqlObject.append("			,SUM(CASE	");
		sqlObject.append("					WHEN typeSql = ?	");
		paramsObject.add(sqlType);
		sqlObject.append("						THEN MONTH_PLAN	");
		sqlObject.append("					ELSE 0	");
		sqlObject.append("					END) MONTH_PLAN	");
		sqlObject.append("			,SUM(CASE	");
		sqlObject.append("					WHEN typeSql = ?	");
		paramsObject.add(sqlType);
		sqlObject.append("						THEN MONTH_PLAN_LAST	");
		sqlObject.append("					ELSE 0	");
		sqlObject.append("					END) MONTH_PLAN_LAST	");
		sqlObject.append("			,SUM(IS_DATE_REQUEST_IN_MONTH) IS_DATE_REQUEST_IN_MONTH	");
		sqlObject.append("		FROM (	");
		sqlObject.append("			SELECT '1' typeSql	");
		sqlObject.append("				,rpt_sale.product_id OBJECT_ID	");
		sqlObject.append("				,rpt_sale.SALE_IN_DATE PG_SALE_IN_DATE	");
		sqlObject.append("				,rpt_sale.SALE_IN_MONTH PG_SALE_IN_MONTH	");
		sqlObject.append("				,rpt_sale.DAY_PLAN DATE_PLAN	");
		sqlObject.append("				,rpt_sale.DAY_PLAN_LAST DATE_PLAN_LAST	");
		sqlObject.append("				,rpt_sale.MONTH_PLAN MONTH_PLAN	");
		sqlObject.append("				,rpt_sale.MONTH_PLAN_LAST MONTH_PLAN_LAST	");
		//"				--neu co PLAN ngay hom nay\n	"
		sqlObject.append("				,(	");
		sqlObject.append("					CASE	");
		sqlObject.append("						WHEN SUBSTR(?,1,7) = ?	");
		paramsObject.add(date);
		paramsObject.add(stringMonth);
		sqlObject.append("							THEN 1	");
		sqlObject.append("						ELSE 0	");
		sqlObject.append("						END	");
		sqlObject.append("					) IS_DATE_REQUEST_IN_MONTH	");
		sqlObject.append("			FROM (	");
		sqlObject.append("				SELECT rpt_sale.product_id product_id	");
		sqlObject.append("					,rpt_sale.CONVFACT CONVFACT	");
		sqlObject.append("					,SUM(CASE	");
		sqlObject.append("							WHEN substr(rpt_sale.rpt_in_date,1,10) = ?	");
		paramsObject.add(date);
		sqlObject.append("								THEN rpt_sale.DAY_PG_QUANTITY	");
		sqlObject.append("							ELSE 0	");
		sqlObject.append("							END) SALE_IN_DATE	");
		sqlObject.append("					,SUM(CASE	");
		sqlObject.append("							WHEN substr(rpt_sale.rpt_in_date,1,10) <= ?	");
		paramsObject.add(date);
		sqlObject.append("								THEN cast(cast(MONTH_PLAN as DOUBLE) / strftime('%d', ?)  + 0.5 AS INT)	");
		paramsObject.add(lastOfMonth);
		sqlObject.append("							ELSE 0	");
		sqlObject.append("							END) DAY_PLAN	");
		sqlObject.append("					,SUM(CASE	");
		sqlObject.append("							WHEN substr(rpt_sale.rpt_in_date,1,10) = ?	");
		paramsObject.add(dateNow);
		sqlObject.append("								THEN cast(cast(MONTH_PLAN as DOUBLE) / strftime('%d', ?) + 0.5 AS INT)	");
		paramsObject.add(lastOfMonth);
		sqlObject.append("							ELSE 0	");
		sqlObject.append("							END) DAY_PLAN_LAST	");
		sqlObject.append("					,SUM(CASE	");
		sqlObject.append("							WHEN substr(rpt_sale.rpt_in_date,1,10) <= ?	");
		paramsObject.add(date);
		sqlObject.append("								THEN rpt_sale.MONTH_PG_QUANTITY	");
		sqlObject.append("							ELSE 0	");
		sqlObject.append("							END) SALE_IN_MONTH	");
		sqlObject.append("					,SUM(CASE	");
		sqlObject.append("							WHEN substr(rpt_sale.rpt_in_date,1,10) <= ?	");
		paramsObject.add(date);
		sqlObject.append("								THEN rpt_sale.MONTH_PLAN	");
		sqlObject.append("							ELSE 0	");
		sqlObject.append("							END) MONTH_PLAN	");
		sqlObject.append("					,SUM(CASE	");
		sqlObject.append("							WHEN substr(rpt_sale.rpt_in_date,1,10) = ?	");
		paramsObject.add(dateNow);
		sqlObject.append("								THEN rpt_sale.MONTH_PLAN	");
		sqlObject.append("							ELSE 0	");
		sqlObject.append("							END) MONTH_PLAN_LAST	");
		sqlObject.append("				FROM RPT_SALE_STATISTIC rpt_sale	");
		sqlObject.append("				WHERE 1 = 1  ");
		//neu co lay theo tung PG
		if (!StringUtil.isNullOrEmpty(idPG)) {
			sqlObject.append("				AND rpt_sale.STAFF_ID = ?	");
			paramsObject.add(idPG);
		}
		sqlObject.append("					AND rpt_sale.PARENT_STAFF_ID = ? ");
		paramsObject.add(staffId);
		sqlObject.append("					AND ( (	substr(rpt_sale.rpt_in_date,1,10) <= ? AND rpt_sale.rpt_in_date = (	");
		paramsObject.add(date);
		sqlObject.append("								SELECT MAX(rpt_Max.rpt_in_date)	");
		sqlObject.append("								FROM RPT_SALE_STATISTIC rpt_Max	");
		sqlObject.append("								WHERE rpt_Max.PRODUCT_ID = rpt_sale.PRODUCT_ID	");
		sqlObject.append("									AND rpt_Max.STAFF_ID = rpt_sale.STAFF_ID	");
		sqlObject.append("									AND substr(rpt_Max.rpt_in_date,1,10) <= ?	");
		paramsObject.add(date);
		sqlObject.append("									AND substr(rpt_Max.rpt_in_date,1,7) = ?	");
		paramsObject.add(stringMonth);
		sqlObject.append("								)	");
		sqlObject.append("							) ");
		//--thêm dieu kien trong cung 1 thang hien tai moi lay PLAN moi nhat, thang cua qua khu thi lay dung thong tin\n
		sqlObject.append("						OR (	");
		sqlObject.append("							substr(rpt_sale.rpt_in_date,1,10) = ? AND SUBSTR(?,1,7) = ?	");
		paramsObject.add(dateNow);
		paramsObject.add(date);
		paramsObject.add(stringMonth);
		sqlObject.append("							)	");
		sqlObject.append("						)	");
		sqlObject.append("				GROUP BY rpt_sale.product_id	");
		sqlObject.append("				) rpt_sale	");
		sqlObject.append("			WHERE 1 = 1	");
		sqlObject.append("		");
		sqlObject.append("			UNION ALL	");
		sqlObject.append("		");
		sqlObject.append("			SELECT '2' typeSql	");
		sqlObject.append("				,spl.product_id OBJECT_ID	");
		sqlObject.append("				,0 PG_SALE_IN_DATE	");
		sqlObject.append("				,0 PG_SALE_IN_MONTH	");
		sqlObject.append("				,cast(cast(spl.QUANTITY as DOUBLE) / strftime('%d', ?) + 0.5 AS INT) DATE_PLAN	");
		paramsObject.add(lastOfMonth);
		sqlObject.append("				,cast(cast(spl.QUANTITY as DOUBLE) / strftime('%d', ?) + 0.5 AS INT) DATE_PLAN_LAST	");
		paramsObject.add(lastOfMonth);
		sqlObject.append("				,spl.QUANTITY MONTH_PLAN	");
		sqlObject.append("				,spl.QUANTITY MONTH_PLAN_LAST	");
		sqlObject.append("				,0 IS_DATE_REQUEST_IN_MONTH	");
		sqlObject.append("			FROM SALE_PLAN spl	");
		sqlObject.append("			WHERE 1 = 1 AND spl.TYPE = 2 AND spl.MONTH = ? AND spl.YEAR = ? AND spl.OBJECT_TYPE = 2 AND spl.OBJECT_ID = ?	");
		paramsObject.add(String.valueOf(month));
		paramsObject.add(String.valueOf(year));
		paramsObject.add(staffId);
		sqlObject.append("			)	");
		sqlObject.append("		GROUP BY OBJECT_ID	");
		sqlObject.append("		) rpt_sale	");
		sqlObject.append("	INNER JOIN PRODUCT pd	");
		sqlObject.append("		ON rpt_sale.OBJECT_ID = pd.product_id	");
		sqlObject.append("	INNER JOIN AP_PARAM param	");
		sqlObject.append("		ON (param.AP_PARAM_CODE = pd.UOM2 AND param.TYPE = 'UOM2' AND param.STATUS = 1)	");
		sqlObject.append("	ORDER BY SORT_ORDER, pd.PRODUCT_CODE ASC, pd.PRODUCT_NAME ASC	");
		
		Cursor c = null;
		try {
			String sql = sqlObject.toString();
			c = this.rawQueries(sql, paramsObject);
			if (c != null && c.moveToFirst()) {
				do {
					GeneralStatisticsInfo info = new GeneralStatisticsInfo();
					info.initCursorForPG(c);
					listResult.add(info);
					
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			listResult = null;
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listResult;
	}
	
	/**
	 * get list report info for Supervior (GSBH)
	 * @author: duongdt3
	 * @param staffIdGSBH 
	 * @since: 15:02:21 21 Nov 2013
	 * @return: List<GeneralStatisticsInfo>
	 * @throws:  
	 * @return
	 * @throws Exception 
	 */
	private List<GeneralStatisticsInfo> getGeneralStatisticsInfoForSupervior(String staffIdGS, String shopIdNV, String staffIdGSBH) throws Exception {
		List<GeneralStatisticsInfo> listResult = new ArrayList<GeneralStatisticsInfo>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		
		String lastOfMonth = DateUtils
				.getLastDateOfNumberPreviousMonthWithFormat(
						DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
		String stringMonth = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_MONTH);
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer stringBuilder = new StringBuffer();
		stringBuilder.append("	SELECT stList.staff_id OBJECT_ID	");
		stringBuilder.append("		,stList.STAFF_CODE OBJECT_CODE	");
		stringBuilder.append("		,stList.STAFF_NAME OBJECT_NAME	");
		stringBuilder.append("		,stList.MOBILEPHONE STAFF_MOBILE	");
		stringBuilder.append("		,stList.SHOP_ID SHOP_ID	");
		stringBuilder.append("		,IFNULL(rpt_sale.SALE_IN_DATE, 0) SALE_IN_DATE	");
		stringBuilder.append("		,IFNULL(rpt_sale.DAY_PLAN, 0) DATE_PLAN	");
		stringBuilder.append("		,IFNULL(rpt_sale.SALE_IN_MONTH, 0) SALE_IN_MONTH	");
		stringBuilder.append("		,IFNULL(rpt_sale.MONTH_PLAN, 0) MONTH_PLAN	");
		stringBuilder.append("	FROM (( SELECT st.staff_id, 	");
		stringBuilder.append("                 st.staff_code,      	");        
		stringBuilder.append("      		   st.staff_name,       	");       
		stringBuilder.append("      		   st.MOBILEPHONE,    	");          
		stringBuilder.append("       		   st.SHOP_ID            ");	
		stringBuilder.append("			FROM STAFF st 	");
		stringBuilder.append("			WHERE 1=1	");
		if (!StringUtil.isNullOrEmpty(staffIdGS)) {
			stringBuilder.append("			AND st.STAFF_OWNER_ID = ?	");
			params.add(staffIdGS);
		}
		
		if (!StringUtil.isNullOrEmpty(shopIdNV)) {
			stringBuilder.append("			AND st.SHOP_ID = ?	");
			params.add(shopIdNV);
		}
		//trường hợp của GSBH gọi
		if (!StringUtil.isNullOrEmpty(staffIdGSBH) && StringUtil.isNullOrEmpty(staffIdGS)) {
			//GS quản lý nhân viên, phải trong danh sách quản lý của GSBH
			stringBuilder.append("				AND st.STAFF_OWNER_ID IN	");
			stringBuilder.append("		(SELECT stGSList.STAFF_ID_GS STAFF_ID_GS	");
			stringBuilder.append("		FROM ((SELECT st.SHOP_ID SHOP_ID, ");
			stringBuilder.append("                    stGSNPP.STAFF_ID_GS ");
			stringBuilder.append("				FROM (SELECT S.STAFF_ID STAFF_ID_GS, S.STAFF_CODE STAFF_CODE_GS, S.STAFF_NAME STAFF_NAME_GS	");
			stringBuilder.append("						FROM STAFF S, CHANNEL_TYPE CH		");
			stringBuilder.append("						WHERE S.STAFF_TYPE_ID = CH.CHANNEL_TYPE_ID		");
			stringBuilder.append("							AND CH.TYPE = 2		");
			stringBuilder.append("							AND CH.OBJECT_TYPE IN (5)		");
			stringBuilder.append("							AND CH.STATUS = 1		");
			if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
				stringBuilder.append("							AND S.STAFF_ID in(	");
				stringBuilder.append("	    						SELECT	");
				stringBuilder.append("	        						sgd.STAFF_ID	");
				stringBuilder.append("	    						FROM	");
				stringBuilder.append("	        						staff_group_detail sgd	");
				stringBuilder.append("	    						WHERE	");
				stringBuilder.append("	        						sgd.STAFF_GROUP_ID IN       (	");
				stringBuilder.append("	            					SELECT	");
				stringBuilder.append("	                					sg1.staff_group_id	");
				stringBuilder.append("	            					FROM	");
				stringBuilder.append("	                					staff_group sg1	");
				stringBuilder.append("	            					WHERE	");
				stringBuilder.append("	                					sg1.STAFF_ID = ?	");
				params.add(staffIdGSBH);
				stringBuilder.append("	                					AND sg1.status = 1	");
				stringBuilder.append("	                					AND sg1.GROUP_LEVEL = 3	");
				stringBuilder.append("	                					AND sg1.GROUP_TYPE = 4	");
				stringBuilder.append("	        ))	");
			}else {
				stringBuilder.append("							AND S.STAFF_OWNER_ID = ?	");
				params.add(staffIdGSBH);
			}
			stringBuilder.append("							AND S.STATUS = 1) stGSNPP, STAFF st	");
			stringBuilder.append("				WHERE 1 = 1	");
			stringBuilder.append("				AND stGSNPP.STAFF_ID_GS = st.STAFF_OWNER_ID	");
			stringBuilder.append("				AND EXISTS (	");
			stringBuilder.append("					SELECT stGSListType.STAFF_ID STAFF_ID	");
			stringBuilder.append("					FROM STAFF stGSListType	");
			stringBuilder.append("						,CHANNEL_TYPE stype	");
			stringBuilder.append("					WHERE 1 = 1	");
			stringBuilder.append("						AND stGSListType.STAFF_ID = st.STAFF_ID	");
			stringBuilder.append("   					AND stGSListType.STAFF_TYPE_ID = STYPE.CHANNEL_TYPE_ID");
			stringBuilder.append("						AND stype.TYPE = 2	");
			stringBuilder.append("						AND stype.OBJECT_TYPE IN (1)	");
			stringBuilder.append("						AND stype.STATUS = 1	");
			stringBuilder.append("						AND stGSListType.STATUS = 1))	stGSList	");
			stringBuilder.append("			JOIN SHOP sh		");
			stringBuilder.append("				ON (stGSList.SHOP_ID = sh.SHOP_ID AND	sh.STATUS = 1)	");
			stringBuilder.append("		)	");
			stringBuilder.append("		WHERE 1 = 1		");
			stringBuilder.append("		GROUP BY stGSList.STAFF_ID_GS, sh.SHOP_ID) 	");
		}
		
		stringBuilder.append("			AND EXISTS (	");
		stringBuilder.append("						SELECT sh.SHOP_ID  SHOP_ID	");
		stringBuilder.append("						FROM SHOP sh	");
		stringBuilder.append("						WHERE 1=1 	");
		stringBuilder.append("							  AND sh.SHOP_ID = st.SHOP_ID	");
		stringBuilder.append("							  AND sh.STATUS = 1	");
		stringBuilder.append("			)	");		
		stringBuilder.append("			AND EXISTS (	");
		stringBuilder.append("				SELECT stListType.STAFF_ID STAFF_ID	");
		stringBuilder.append("				FROM STAFF stListType	");
		stringBuilder.append("					,CHANNEL_TYPE stype	");
		stringBuilder.append("				WHERE 1 = 1	");
		stringBuilder.append("					AND stListType.STAFF_ID = st.STAFF_ID	");
		stringBuilder.append("					AND stListType.STAFF_TYPE_ID = STYPE.CHANNEL_TYPE_ID	");
		stringBuilder.append("					AND stype.TYPE = 2	");
		stringBuilder.append("					AND stype.OBJECT_TYPE IN (1)	");
		stringBuilder.append("					AND stype.STATUS = 1	");
		stringBuilder.append("					AND stListType.STATUS = 1))	stList	");
		stringBuilder.append("		LEFT JOIN 	");
		stringBuilder.append("		(SELECT STAFF_ID STAFF_ID_NV	");
		stringBuilder.append("			,SUM(SALE_IN_DATE) SALE_IN_DATE	");
		stringBuilder.append("			,SUM(DAY_PLAN) DAY_PLAN	");
		stringBuilder.append("			,SUM(SALE_IN_MONTH) SALE_IN_MONTH	");
		stringBuilder.append("			,SUM(MONTH_PLAN) MONTH_PLAN	");
		stringBuilder.append("		FROM (	");
		stringBuilder.append("			SELECT rpt_sale.staff_id staff_id	");
		stringBuilder.append("				,(CASE WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ? 	");
		params.add(dateNow);
		stringBuilder.append("					THEN rpt_sale.DAY_QUANTITY 	");
		stringBuilder.append("					ELSE 0 END) SALE_IN_DATE	");
		stringBuilder.append("			,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN cast(cast(MONTH_PLAN as DOUBLE) / strftime('%d', ?) + 0.5 AS INTEGER)	");
		params.add(lastOfMonth);
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) DAY_PLAN	");
		stringBuilder.append("			,rpt_sale.MONTH_QUANTITY SALE_IN_MONTH	");
		stringBuilder.append("			,(	");
		stringBuilder.append("				CASE 	");
		stringBuilder.append("					WHEN substr(rpt_sale.RPT_IN_DATE,1,10) = ?	");
		params.add(dateNow);
		stringBuilder.append("						THEN rpt_sale.MONTH_PLAN	");
		stringBuilder.append("					ELSE 0	");
		stringBuilder.append("					END	");
		stringBuilder.append("				) MONTH_PLAN	");
		stringBuilder.append("			,rpt_sale.PRODUCT_ID PRODUCT_ID	");
		stringBuilder.append("			FROM RPT_SALE_STATISTIC rpt_sale	");
		stringBuilder.append("			WHERE 1 = 1	");
		//stringBuilder.append("				--kiem tra thoi gian trong thang    	");
		stringBuilder.append("				AND substr(rpt_sale.rpt_in_date,1,7) = ?	");
		params.add(stringMonth);
		//stringBuilder.append("				--kiem tra thoi gian nay phai la MAX trong thang {SP + NV} 	");
		stringBuilder.append("				AND rpt_sale.rpt_in_date = (	");
		stringBuilder.append("					SELECT  MAX(rpt_Max.rpt_in_date)	");
		stringBuilder.append("					FROM RPT_SALE_STATISTIC rpt_Max	");
		stringBuilder.append("					WHERE rpt_Max.PRODUCT_ID = rpt_sale.PRODUCT_ID	");
		stringBuilder.append("					AND rpt_Max.STAFF_ID = rpt_sale.STAFF_ID 	");
		stringBuilder.append("					AND substr(rpt_Max.RPT_IN_DATE,1,7) = ?)	");
		params.add(stringMonth);
		stringBuilder.append("				AND (substr(rpt_sale.rpt_in_date,1,10) = ? OR rpt_sale.MONTH_QUANTITY > 0 )	");
		params.add(dateNow);
		//stringBuilder.append("				--kiem tra sản phẩm + DVT có tồn tại 	");
        stringBuilder.append("				AND EXISTS (	");
        stringBuilder.append("					SELECT pd.PRODUCT_ID PRODUCT_ID 	");
        stringBuilder.append("					FROM PRODUCT pd ,AP_PARAM param	");
        stringBuilder.append("					WHERE 1=1	");
        stringBuilder.append("						AND pd.PRODUCT_ID = rpt_sale.PRODUCT_ID	");
        stringBuilder.append("						AND param.AP_PARAM_CODE = pd.UOM2	");
        stringBuilder.append("					  	AND param.STATUS = 1");
        stringBuilder.append("						AND param.TYPE LIKE 'UOM2')	");
        stringBuilder.append("			)	");
        stringBuilder.append("		GROUP BY staff_id	");
		stringBuilder.append("		) rpt_sale	");
		stringBuilder.append("		ON rpt_sale.STAFF_ID_NV = stList.STAFF_ID	");
		stringBuilder.append("		)	");
		stringBuilder.append("	ORDER BY stList.STAFF_CODE ASC	");
		
		Cursor c = null;
		try {
			String sql = stringBuilder.toString();
			c = this.rawQueries(sql, params);
			if (c != null && c.moveToFirst()) {
				do {
					GeneralStatisticsInfo info = new GeneralStatisticsInfo();
					info.initCursorForSaleman(c);
					listResult.add(info);
					
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			listResult = null;
			throw ex;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listResult;
	}
	
	/**
	 * Lay danh sach nhan vien ban hang cua GST
	 * 
	 * @author: QuangVT
	 * @since: 1:47:41 PM Dec 18, 2013
	 * @return: ListStaffDTO
	 * @throws:  
	 * @param staffOwnerId
	 * @param orderByCode
	 * @return
	 */
	public ListStaffDTO getListNVBHForTBHV(String staffOwnerId, boolean orderByCode) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>();
		StringBuffer  sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DISTINCT STAFF_NAME     STAFF_NAME, ");
		sqlQuery.append("                STAFF_ID 	    STAFF_ID, ");
		sqlQuery.append("                STAFF_CODE     STAFF_CODE, ");
		sqlQuery.append("                SHOP_ID        SHOP_ID, ");
		sqlQuery.append("                STAFF_OWNER_ID STAFF_OWNER_ID ");
		sqlQuery.append("FROM   STAFF ");
		sqlQuery.append("WHERE  STAFF_OWNER_ID IN (SELECT STAFF_ID AS ID_GSBH ");
		sqlQuery.append("                          FROM   STAFF ");
		sqlQuery.append("                          WHERE  1 = 1 ");
//		sqlQuery.append("                                 AND STAFF_OWNER_ID = ? ");
//		params.add(staffOwnerId);
		sqlQuery.append("							AND STAFF_ID in(	");
		sqlQuery.append("	    						SELECT	");
		sqlQuery.append("	        						sgd.STAFF_ID	");
		sqlQuery.append("	    						FROM	");
		sqlQuery.append("	        						staff_group_detail sgd	");
		sqlQuery.append("	    						WHERE	");
		sqlQuery.append("	        						sgd.STAFF_GROUP_ID IN       (	");
		sqlQuery.append("	            					SELECT	");
		sqlQuery.append("	                					sg1.staff_group_id	");
		sqlQuery.append("	            					FROM	");
		sqlQuery.append("	                					staff_group sg1	");
		sqlQuery.append("	            					WHERE	");
		sqlQuery.append("	                					sg1.STAFF_ID = ?	");
		params.add(staffOwnerId);
		sqlQuery.append("	                					AND sg1.status = 1	");
		sqlQuery.append("	                					AND sg1.GROUP_LEVEL = 3	");
		sqlQuery.append("	                					AND sg1.GROUP_TYPE = 4	");
		sqlQuery.append("	        ))	");
		sqlQuery.append("                                 AND STAFF_TYPE_ID IN (SELECT CHANNEL_TYPE_ID ");
		sqlQuery.append("                                                       FROM   CHANNEL_TYPE ");
		sqlQuery.append("                                                       WHERE  TYPE = 2 ");
		sqlQuery.append("                                                              AND STATUS = 1 ");
		sqlQuery.append("                                                              AND OBJECT_TYPE IN ");
		sqlQuery.append("                                                                  ( 5 )) ");
		sqlQuery.append("                                ) ");
		sqlQuery.append("       AND STAFF_TYPE_ID IN (SELECT CHANNEL_TYPE_ID ");
		sqlQuery.append("                             FROM   CHANNEL_TYPE ");
		sqlQuery.append("                             WHERE  TYPE = 2 ");
		sqlQuery.append("                                    AND OBJECT_TYPE IN ( 1 )) ");
		sqlQuery.append("       AND STATUS = 1 ");
 
		if (orderByCode) {
			sqlQuery.append(" ORDER  BY STAFF_CODE, STAFF_NAME ");
		} else {
			sqlQuery.append(" ORDER  BY STAFF_NAME ");
		}

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			dto = null;
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) { 
				VTLog.i("error", e.toString());
			}
		}

		return dto;
	}
	
	/**
	 * Lay danh sach giam sat ban hang cua GST
	 * 
	 * @author: QuangVT
	 * @since: 1:47:41 PM Dec 18, 2013
	 * @return: ListStaffDTO
	 * @throws:  
	 * @param staffOwnerId
	 * @param orderByCode
	 * @return
	 */
	public ListStaffDTO getListGSBHForTBHV(String staffOwnerId, boolean orderByCode) {
		ListStaffDTO dto = new ListStaffDTO();
		List<String> params = new ArrayList<String>(); 
		StringBuffer  sqlQuery = new StringBuffer();
		sqlQuery.append("SELECT DISTINCT STAFF_NAME     STAFF_NAME, ");
		sqlQuery.append("                STAFF_ID       STAFF_ID, ");
		sqlQuery.append("                STAFF_CODE     STAFF_CODE, ");
		sqlQuery.append("                SHOP_ID        SHOP_ID, ");
		sqlQuery.append("                STAFF_OWNER_ID STAFF_OWNER_ID ");
		sqlQuery.append("FROM   STAFF GS ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND GS.STATUS = 1 ");
//		sqlQuery.append("       AND GS.STAFF_OWNER_ID = ? ");
//		params.add(staffOwnerId);
		sqlQuery.append("							AND GS.STAFF_ID in(	");
		sqlQuery.append("	    						SELECT	");
		sqlQuery.append("	        						sgd.STAFF_ID	");
		sqlQuery.append("	    						FROM	");
		sqlQuery.append("	        						staff_group_detail sgd	");
		sqlQuery.append("	    						WHERE	");
		sqlQuery.append("	        						sgd.STAFF_GROUP_ID IN       (	");
		sqlQuery.append("	            					SELECT	");
		sqlQuery.append("	                					sg1.staff_group_id	");
		sqlQuery.append("	            					FROM	");
		sqlQuery.append("	                					staff_group sg1	");
		sqlQuery.append("	            					WHERE	");
		sqlQuery.append("	                					sg1.STAFF_ID = ?	");
		params.add(staffOwnerId);
		sqlQuery.append("	                					AND sg1.status = 1	");
		sqlQuery.append("	                					AND sg1.GROUP_LEVEL = 3	");
		sqlQuery.append("	                					AND sg1.GROUP_TYPE = 4	");
		sqlQuery.append("	        ))	");
		sqlQuery.append("       AND GS.STAFF_TYPE_ID IN (SELECT CHANNEL_TYPE_ID ");
		sqlQuery.append("                                FROM   CHANNEL_TYPE ");
		sqlQuery.append("                                WHERE  TYPE = 2 ");
		sqlQuery.append("                                       AND OBJECT_TYPE IN ( 5 )) ");
		sqlQuery.append("       AND EXISTS(SELECT NV.STAFF_ID ");
		sqlQuery.append("                  FROM   STAFF NV ");
		sqlQuery.append("                  WHERE  NV.STAFF_OWNER_ID = GS.STAFF_ID ");
		sqlQuery.append("                         AND NV.STATUS = 1) ");
		
		if (orderByCode) {
			sqlQuery.append(" ORDER  BY STAFF_CODE, STAFF_NAME ");
		} else {
			sqlQuery.append(" ORDER  BY STAFF_NAME ");
		}

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			dto = null;
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) { 
				VTLog.i("error", e.toString());
			}
		}

		return dto;
	}

	/**
	 * Lay cau hinh hack gps
	 *
	 * @author: yennth16
	 * @return
	 * @return: LatLng
	 * @throws:
	 */

	public int checkAccessApp() {
		int checkAccessApp = 0;
		String sql = "SELECT CHECK_INSTALL_APP FROM STAFF WHERE STAFF_ID = ?";

		Cursor c = null;
		String[] params = { "" + GlobalInfo.getInstance().getProfile().getUserData().id };
		try {
			c = rawQuery(sql, params);
			if (c != null) {
				while (c.moveToNext()) {
					checkAccessApp = c.getInt(c.getColumnIndex("CHECK_INSTALL_APP"));
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return checkAccessApp;
	}

	/**
	 * Lấy danh sách nhân viên bán hàng màn hình huấn luyện
	 * @param shopId
	 * @param staffOwnerId
     * @return
     */
	public ListStaffDTO getTrainingListNVBH(long shopId, String staffOwnerId) {
		ListStaffDTO dto = new ListStaffDTO();
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		List<String> params = new ArrayList<String>();
		StringBuffer sqlQuery = new StringBuffer();

		sqlQuery.append("SELECT DISTINCT tb1.nvbh     STAFF_NAME, ");
		sqlQuery.append("                tb1.staff_id STAFF_ID, ");
		sqlQuery.append("                tb1.manv     STAFF_CODE ");
		sqlQuery.append("FROM   (SELECT s.staff_id, ");
		sqlQuery.append("               s.staff_code MaNV, ");
		sqlQuery.append("               s.staff_name NVBH, ");
		sqlQuery.append("               a.customer_id ");
		sqlQuery.append("        FROM   (SELECT * ");
		sqlQuery.append("                FROM   routing_customer ");
		sqlQuery.append("                WHERE  Round(Strftime('%W', ?) + 1 - start_week ");
		params.add(date_now);
		sqlQuery.append("                       ) >= 0 ");
		sqlQuery.append("                       AND status = 1) a, ");
		sqlQuery.append("               visit_plan b, ");
		sqlQuery.append("               routing c, ");
		sqlQuery.append("               staff s, ");
		sqlQuery.append("               shop sh, ");
		sqlQuery.append("               customer cu ");
		sqlQuery.append("        WHERE  1 = 1 ");
		sqlQuery.append("               AND b.routing_id = a.routing_id ");
		sqlQuery.append("               AND c.routing_id = a.routing_id ");
		sqlQuery.append("               AND b.staff_id = s.staff_id ");
		sqlQuery.append("               AND a.customer_id = cu.customer_id ");
		sqlQuery.append("               AND s.shop_id = sh.shop_id ");
		sqlQuery.append("               AND sh.status = 1 ");
		sqlQuery.append("               AND a.status = 1 ");
		sqlQuery.append("               AND b.status = 1 ");
		sqlQuery.append("               AND s.status = 1 ");
		sqlQuery.append("               AND c.status = 1 ");
		sqlQuery.append("               AND cu.status = 1 ");
		sqlQuery.append("               AND ( ? > substr(b.from_date,1,10) ");
		params.add(date_now);
		sqlQuery.append("                     AND ( b.to_date IS NULL ");
		sqlQuery.append("                            OR ? < ");
		params.add(date_now);
		sqlQuery.append("                               substr(b.to_date,1,10) ) ) ");
		if (!StringUtil.isNullOrEmpty("" + shopId)) {
			sqlQuery.append(" AND s.shop_id in ");
			params.add("(" + shopId + ")");
		}

		if (!StringUtil.isNullOrEmpty("" + staffOwnerId)) {
			sqlQuery.append(" AND s.staff_owner_id in ");
			params.add("(" + staffOwnerId + ")");
		}
		sqlQuery.append("   ) tb1    LEFT JOIN staff_customer tb2 ");
		sqlQuery.append("              ON ( tb1.staff_id = tb2.staff_id ");
		sqlQuery.append("                   AND tb1.customer_id = tb2.customer_id ) ");
		sqlQuery.append("WHERE  1 = 1 ");
		sqlQuery.append("       AND ( substr(tb2.last_approve_order,1,10) IS NULL ");
		sqlQuery.append("              OR tb2.last_approve_order = '' ");
		sqlQuery.append("              OR Date(tb2.last_approve_order) < (SELECT ");
		sqlQuery.append("                 Date(?,'start of month' ");
		params.add(date_now);
		sqlQuery.append("                 )) ) ");
		sqlQuery.append("       AND ( tb2.last_order IS NULL ");
		sqlQuery.append("              OR tb2.last_order = '' ");
		sqlQuery.append("              OR substr(tb2.last_order,1,10) < ? ) ");
		params.add(date_now);
		sqlQuery.append("ORDER  BY staff_name ");

		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery.toString(), params.toArray(new String[params.size()]));
			if (c != null && c.moveToFirst()) {
				if (c.getCount() > 1) {
					dto.addItemAll();
				}
				do {
					dto.parrseStaffFromCursor(c);
				} while (c.moveToNext());
			}
		} catch (Exception ex) {
			VTLog.i("error", ex.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				VTLog.i("error", e.toString());
			}
		}

		return dto;
	}

	/**
	 * Lấy danh sách NV của GSBH theo staff_group
	 * @param staffId
	 * @return
     */
	public ArrayList<String> getStaffGroupTBHV(String staffId) {
		ArrayList<String> result = new ArrayList<>();
		StringBuffer sqlObject = new StringBuffer();
		ArrayList<String> paramsObject = new ArrayList<String>();
		sqlObject.append("	SELECT	");
		sqlObject.append("	    staff_id                            as  STAFF_ID	");
		sqlObject.append("	FROM	");
		sqlObject.append("	    STAFF	");
		sqlObject.append("	WHERE	");
		sqlObject.append("	    STAFF_ID IN (	");
		sqlObject.append("	        SELECT	");
		sqlObject.append("	            DISTINCT sgde.STAFF_ID	");
		sqlObject.append("	        FROM	");
		sqlObject.append("	            staff_group_detail sgde	");
		sqlObject.append("	        WHERE	");
		sqlObject.append("	            sgde.status = 1	");
		sqlObject.append("	            AND sgde.STAFF_GROUP_ID IN (	");
		sqlObject.append("	                SELECT	");
		sqlObject.append("	                    sgd.STAFF_GROUP_ID	");
		sqlObject.append("	                FROM	");
		sqlObject.append("	                    staff_group_detail sgd,	");
		sqlObject.append("	                    staff_group sg	");
		sqlObject.append("	                WHERE	");
		sqlObject.append("	                    sgd.STAFF_ID IN (	");
		sqlObject.append("	                        SELECT	");
		sqlObject.append("	                            sgd.STAFF_ID	");
		sqlObject.append("	                        FROM	");
		sqlObject.append("	                            staff_group_detail sgd	");
		sqlObject.append("	                        WHERE	");
		sqlObject.append("	                            sgd.STAFF_GROUP_ID IN (	");
		sqlObject.append("	                                SELECT	");
		sqlObject.append("	                                    sg1.staff_group_id	");
		sqlObject.append("	                                FROM	");
		sqlObject.append("	                                    staff_group sg1	");
		sqlObject.append("	                                WHERE	");
		sqlObject.append("	                                    sg1.STAFF_ID = ?	");
		paramsObject.add(staffId);
		sqlObject.append("	                                    AND status = 1	");
		sqlObject.append("	                            )	");
		sqlObject.append("	                            AND sgd.status = 1	");
		sqlObject.append("	                        )	");
		sqlObject.append("	                        AND sgd.STATUS = 1	");
		sqlObject.append("	                        AND sg.STAFF_GROUP_ID = sgd.STAFF_GROUP_ID	");
		sqlObject.append("	                )	");
		sqlObject.append("	            )	");
		Cursor c = null;
		try {
			c = this.rawQuery(sqlObject.toString(), paramsObject.toArray(new String[paramsObject.size()]));
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String staff = c.getString(c.getColumnIndex("STAFF_ID"));
						result.add(staff);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}

		return result;
	}

}
