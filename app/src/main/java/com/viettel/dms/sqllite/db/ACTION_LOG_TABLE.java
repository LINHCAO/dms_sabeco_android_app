/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.view.ReportNVBHVisitCustomerDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin action tai mot thoi diem nao do (Ghe tham, cham hien dien mat
 * hang, kiem hang ton, dat hang)
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class ACTION_LOG_TABLE extends ABSTRACT_TABLE {
	// id action log
	public static final String ACTION_LOG_ID = "ACTION_LOG_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// object id
	public static final String OBJECT_ID = "OBJECT_ID";
	// object type
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// toa do lat
	public static final String LAT = "LAT";
	// toa do lng
	public static final String LNG = "LNG";
	// thoi gian bat dau hanh dong
	public static final String START_TIME = "START_TIME";
	// thoi gian ket thuc mot hanh dong
	public static final String END_TIME = "END_TIME";
	// trong tuyen, ngoai tuyen
	public static final String IS_OR = "IS_OR";

	public static final String INTERVAL_TIME = "INTERVAL_TIME";

	public static final String DISTANCE = "DISTANCE";
	
	public static final String TABLE_NAME = "ACTION_LOG";

	public ACTION_LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { ACTION_LOG_ID, STAFF_ID, CUSTOMER_ID, STAFF_ID, OBJECT_ID, OBJECT_TYPE, LAT, LNG,
				START_TIME, END_TIME, IS_OR, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((ActionLogDTO) dto);
		return insert(null, value);
	}

	/**
	 * Cap nhat customer
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: int
	 * @throws:
	 */
	public int update(ActionLogDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.id };
		return update(value, ACTION_LOG_ID + " = ?", params);
	}

	public long update(AbstractTableDTO dto) {
		ActionLogDTO cusDTO = (ActionLogDTO) dto;
		ContentValues value = initDataRow(cusDTO);
		String[] params = { "" + cusDTO.id };
		return update(value, ACTION_LOG_ID + " = ?", params);
	}

	/**
	 * Cap nhat khi tao moi order
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long updateFromOrder(AbstractTableDTO dto) {
		ActionLogDTO cusDTO = (ActionLogDTO) dto;
		ContentValues value = initDataUpdateFromOrder(cusDTO);
		String[] params = { "" + cusDTO.id };
		return update(value, ACTION_LOG_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: BangHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(ACTION_LOG_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		ActionLogDTO cusDTO = (ActionLogDTO) dto;
		String[] params = { "" + cusDTO.id };
		return delete(ACTION_LOG_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @param id
	 * @return: CustomerDTO
	 * @throws:
	 */
	public ActionLogDTO getCustomerById(String id) {
		ActionLogDTO action = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(ACTION_LOG_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				action = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return action;
	}

	/**
	 * Thong tin chung
	 * 
	 * @author : BangHN since : 1.0
	 */
	private ActionLogDTO initLogDTOFromCursor(Cursor c) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * init dong data action log
	 * 
	 * @author : BangHN since : 1.0
	 */
	private ContentValues initDataRow(ActionLogDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ACTION_LOG_ID, dto.id);
		editedValues.put(CUSTOMER_ID, dto.aCustomer.customerId);
		editedValues.put(STAFF_ID, dto.staffId);
		if (!StringUtil.isNullOrEmpty(dto.objectId)) {
			editedValues.put(OBJECT_ID, dto.objectId);
		}
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(LAT, dto.lat);
		editedValues.put(LNG, dto.lng);
		editedValues.put(START_TIME, dto.startTime);
		if (!StringUtil.isNullOrEmpty(dto.endTime)) {
			editedValues.put(END_TIME, dto.endTime);
		}
		if (!StringUtil.isNullOrEmpty(dto.interval_time)) {
			editedValues.put(INTERVAL_TIME, dto.interval_time);
		}
		editedValues.put(IS_OR, dto.isOr);
		editedValues.put(DISTANCE, dto.distance);
		return editedValues;
	}

	/**
	 * Thong tin chung
	 * 
	 * @author : BangHN since : 1.0
	 */
	private ContentValues initDataUpdateFromOrder(ActionLogDTO cusDTO) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * TamPQ
	 * 
	 * @param dto
	 * @return
	 */
	public long updateVisited(ActionLogDTO dto) {
		ContentValues editedValues = new ContentValues();
		if (!StringUtil.isNullOrEmpty(dto.objectId)) {
			editedValues.put(OBJECT_ID, dto.objectId);
		}
		editedValues.put(OBJECT_TYPE, dto.objectType);
		// editedValues.put(LAT, dto.lat);
		// editedValues.put(LNG, dto.lng);
		editedValues.put(END_TIME, dto.endTime);
		editedValues.put(INTERVAL_TIME, dto.interval_time);
		String[] params = { "" + dto.id };
		return update(editedValues, ACTION_LOG_ID + " = ?", params);
	}

	public ActionLogDTO checkVisitFromActionLog(long staffId) {
		ActionLogDTO dto = null;
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		StringBuffer var1 = new StringBuffer();
		var1.append("		SELECT AL.ACTION_LOG_ID, ");
		var1.append("		       AL.STAFF_ID        AS STAFF_ID, ");
		var1.append("		       AL.CUSTOMER_ID     AS CUSTOMER_ID, ");
		var1.append("		       AL.OBJECT_ID       AS OBJECT_ID, ");
		var1.append("		       AL.OBJECT_TYPE     AS OBJECT_TYPE, ");
		var1.append("		       AL.LAT             AS LAT, ");
		var1.append("		       AL.LNG             AS LNG, ");
		var1.append("		       MAX(AL.START_TIME) AS START_TIME, ");
		var1.append("		       AL.END_TIME        AS END_TIME, ");
		var1.append("		       AL.IS_OR           AS IS_OR, ");
		var1.append("		       CT.CUSTOMER_NAME   AS CUSTOMER_NAME, ");
		var1.append("		       CT.CUSTOMER_CODE   AS CUSTOMER_CODE,");
		var1.append("		       CT.SHORT_CODE   AS SHORT_CODE,");
		var1.append("           (SELECT COUNT(*) FROM ACTION_LOG AL1 WHERE AL1.[OBJECT_TYPE] != 0 AND AL1.STAFF_ID=AL.[STAFF_ID] AND AL1.CUSTOMER_ID=AL.[CUSTOMER_ID]) AS HAVE_ACTION ");
		var1.append("		FROM   ACTION_LOG AL, ");
		var1.append("		       CUSTOMER AS CT ");
		var1.append("		WHERE  (SELECT DATE(?) = DATE(AL.START_TIME)) = 1 ");
		var1.append("  		     AND AL.OBJECT_TYPE = 0 ");
		var1.append("		       AND AL.END_TIME IS NULL ");
		var1.append("		       AND AL.STAFF_ID = ? ");
		var1.append("		       AND AL.CUSTOMER_ID = CT.CUSTOMER_ID ");
		var1.append("		");
		var1.append("		GROUP  BY AL.OBJECT_TYPE");

		Cursor c = null;
		try {
			c = rawQuery(var1.toString(), new String[] { date_now, "" + staffId });
			if (c != null) {
				if (c.moveToFirst()) {
					dto = new ActionLogDTO();
					dto.initDataFromCursor(c);
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

		return dto;
	}

	/**
	 * Delete action log cua 1 don hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param actionLogDTO
	 * @return: void
	 * @throws:
	 */

	public int deleteActionLogWhenDeleteOrder(ActionLogDTO actionLogDTO) {
		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(actionLogDTO.staffId));
		params.add(actionLogDTO.objectId);
		params.add(actionLogDTO.objectType);

		return delete("STAFF_ID = ? AND OBJECT_ID = ? AND OBJECT_TYPE = ?", params.toArray(new String[params.size()]));
	}

	/**
	 * delete action cho phep dat hang tu xa
	 * 
	 * @author: BangHN
	 * @param actionLogDTO
	 * @return: int
	 */
	public int deleteActionLogWhenRemoveExceptionOrderDate(ActionLogDTO actionLogDTO) {
		ArrayList<String> params = new ArrayList<String>();
		params.add(String.valueOf(actionLogDTO.staffId));
		params.add(String.valueOf(actionLogDTO.aCustomer.customerId));
		params.add(DateUtils.now());
		return delete(
				"STAFF_ID = ? AND CUSTOMER_ID = ? AND OBJECT_TYPE = 5 AND DATE(START_TIME)= DATE(?)",
				params.toArray(new String[params.size()]));
	}

	/**
	 * delete action log truoc 3 ngay
	 * 
	 * @author banghn
	 * @return
	 */
	public long deleteOldActionLog() {
		long success = -1;
		try {
			//mDB.beginTransaction();
			// Mac dinh xoa cua thang truoc
			// String sqlDelete =
			// "Delete from ACTION_LOG where date(START_TIME) < date('now','-1 month')";
			String startOfMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(
					DateUtils.DATE_FORMAT_SQL_DEFAULT, 0);
			StringBuffer sqlDel = new StringBuffer();
			// sqlDel.append("DELETE FROM action_log ");
			sqlDel.append("  substr(start_time,0,11) < ? ");

			String[] params = { startOfMonth };
			// params[0] =
			// GlobalInfo.getInstance().getProfile().getUserData().id;
			delete(sqlDel.toString(), params);
			// execSQL(sqlDel.toString(), params);
			// mac dinh la thanh cong
			success = 1;
			//mDB.setTransactionSuccessful();
		} catch (Throwable e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			success = -1;
		} finally {
//			if (mDB != null && mDB.inTransaction()) {
//				mDB.endTransaction();
//			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return success;
	}

	/**
	 * 
	 * get list report of NVBH visit customer in day
	 * 
	 * @param data
	 * @return
	 * @return: ArrayList<ReportNVBHVisitCustomerDTO>
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public ArrayList<ReportNVBHVisitCustomerDTO> getListReportNVBHInDay(Bundle data) {
		String day = DateUtils.getToday();
		ArrayList<ReportNVBHVisitCustomerDTO> listReportNVBH = new ArrayList<ReportNVBHVisitCustomerDTO>();
		String startTime = data.getString(IntentConstants.INTENT_START_TIME_COMPARE);
		String middleTime = data.getString(IntentConstants.INTENT_MIDDLE_TIME_COMPARE);
		//String endTime = data.getString(IntentConstants.INTENT_END_TIME_COMPARE);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String dateNow= DateUtils.now();
		List<String> params= new ArrayList<String>();

		StringBuffer sqlRequest = new StringBuffer();
		sqlRequest.append("SELECT staff.staff_id                                     STAFF_ID, ");
		sqlRequest.append("       staff.staff_code                                   STAFF_CODE, ");
		sqlRequest.append("       staff.shop_id                                   	SHOP_ID, ");
		sqlRequest.append("       staff.staff_name                                   STAFF_NAME, ");
		sqlRequest.append("       Ifnull(PLAN.TOTAL_CUS_PLAN, 0)                          TOTAL_CUSTOMER_VISIT, ");
		sqlRequest.append("       actionlog.customer_name                            CUSTOMER_NAME, ");
		sqlRequest.append("       actionlog.customer_code                            CUSTOMER_CODE, ");
		sqlRequest.append("       actionlog.start_time                               START_TIME, ");
		sqlRequest.append("       actionlog2.nummiddle                               NUMMIDDLE, ");
		sqlRequest.append("       actionlog2.numend                                  NUMEND, ");
		sqlRequest.append("       actionlog2.numcurrent                              NUMCURRENT, ");
		sqlRequest.append("       Ifnull(actionlog.is_or, 1)                         IS_OR, ");
		sqlRequest.append("       Ifnull(actionlog.lessmiddle, 0)                    LESSMIDDLE, ");
		sqlRequest.append("       Ifnull(actionlog.lessend, 0)                       LESSEND, ");
		sqlRequest.append("       Ifnull(actionlog.lessnow_total_visited, 0)         LESSNOW_TOTAL_VISITED, ");
		sqlRequest.append("       Ifnull(actionlog.lat, 0)                           LAT, ");
		sqlRequest.append("       Ifnull(actionlog.lng, 0)                           LNG, ");
		sqlRequest.append("       Ifnull(actionlog.lessnow_total_visited_in_plan, 0) ");
		sqlRequest.append("       LESSNOW_TOTAL_VISITED_IN_PLAN ");
		sqlRequest.append("FROM   (SELECT staff_id, ");
		sqlRequest.append("               staff_code, ");
		sqlRequest.append("               staff_name, ");
		sqlRequest.append("               shop_id ");
		sqlRequest.append("        FROM   staff ");
		sqlRequest.append("        WHERE  staff_owner_id = ? ");
		params.add(staffId);
		sqlRequest.append("               AND shop_id = ? ");
		params.add(shopId);
		sqlRequest.append("               AND status = 1) staff ");
		sqlRequest.append("       LEFT JOIN (SELECT vp.staff_id           AS STAFF_ID_1, ");
		sqlRequest.append("                         Count(RTC.customer_id) AS TOTAL_CUS_PLAN ");
		sqlRequest.append("                  FROM   visit_plan VP, ");
		sqlRequest.append("                         routing RT, ");
		sqlRequest.append("                         (SELECT routing_customer_id, ");
		sqlRequest.append("                                 routing_id, ");
		sqlRequest.append("                                 customer_id, "); 
		sqlRequest.append("                                 status, ");
		sqlRequest.append("                ( ");
		sqlRequest.append("                (WEEK1 IS NULL AND WEEK2 IS NULL AND WEEK3 IS NULL AND WEEK4 IS NULL) OR");
		sqlRequest.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		params.add(dateNow);
		sqlRequest.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		params.add(dateNow);
		sqlRequest.append("			and (");
		sqlRequest.append("		cast((case when strftime('%w', ?) = '0' ");
		params.add(dateNow);
		sqlRequest.append("									  then 7 ");
		sqlRequest.append("									  else strftime('%w', ?)");
		params.add(dateNow);
		sqlRequest.append("									  end ) as integer) < ");
		sqlRequest.append("		 cast((case when strftime('%w', start_date) = '0' ");
		sqlRequest.append("										  then 7 ");
		sqlRequest.append("										  else strftime('%w', start_date)                          ");
		sqlRequest.append("										  end ) as integer) ) ");
		sqlRequest.append("			then 1 else 0 end)) % 4 + 1)=1 and week1=1) or");

		sqlRequest.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		params.add(dateNow);
		sqlRequest.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		params.add(dateNow);
		sqlRequest.append("			and (");
		sqlRequest.append("		cast((case when strftime('%w', ?) = '0' ");
		params.add(dateNow);
		sqlRequest.append("									  then 7 ");
		sqlRequest.append("									  else strftime('%w', ?)");
		params.add(dateNow);
		sqlRequest.append("									  end ) as integer) < ");
		sqlRequest.append("		 cast((case when strftime('%w', start_date) = '0' ");
		sqlRequest.append("										  then 7 ");
		sqlRequest.append("										  else strftime('%w', start_date)                          ");
		sqlRequest.append("										  end ) as integer) ) ");
		sqlRequest.append("			then 1 else 0 end)) % 4 + 1)=2 and week2=1) or");

		sqlRequest.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		params.add(dateNow);
		sqlRequest.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		params.add(dateNow);
		sqlRequest.append("			and (");
		sqlRequest.append("		cast((case when strftime('%w', ?) = '0' ");
		params.add(dateNow);
		sqlRequest.append("									  then 7 ");
		sqlRequest.append("									  else strftime('%w', ?)                          ");
		params.add(dateNow);
		sqlRequest.append("									  end ) as integer) < ");
		sqlRequest.append("		 cast((case when strftime('%w', start_date) = '0' ");
		sqlRequest.append("										  then 7 ");
		sqlRequest.append("										  else strftime('%w', start_date)                          ");
		sqlRequest.append("										  end ) as integer) ) ");
		sqlRequest.append("			then 1 else 0 end)) % 4 + 1)=3 and week3=1) or");

		sqlRequest.append("	(((cast((julianday(Date(?)) - julianday(start_date)) / 7  as integer) +");
		params.add(dateNow);
		sqlRequest.append("		   (case when ((julianday(Date(?)) - julianday(start_date)) % 7 > 0)");
		params.add(dateNow);
		sqlRequest.append("			and (");
		sqlRequest.append("		cast((case when strftime('%w', ?) = '0' ");
		params.add(dateNow);
		sqlRequest.append("									  then 7 ");
		sqlRequest.append("									  else strftime('%w', ?)                          ");
		params.add(dateNow);
		sqlRequest.append("									  end ) as integer) < ");
		sqlRequest.append("		 cast((case when strftime('%w', start_date) = '0' ");
		sqlRequest.append("										  then 7 ");
		sqlRequest.append("										  else strftime('%w', start_date)                          ");
		sqlRequest.append("										  end ) as integer) ) ");
		sqlRequest.append("			then 1 else 0 end)) % 4 + 1)=4 and week4=1) ) AS INWEEK, ");
		sqlRequest.append("                                 monday, ");
		sqlRequest.append("                                 tuesday, ");
		sqlRequest.append("                                 wednesday, ");
		sqlRequest.append("                                 thursday, ");
		sqlRequest.append("                                 friday, ");
		sqlRequest.append("                                 saturday, ");
		sqlRequest.append("                                 sunday, ");
		sqlRequest.append("                                 seq2, ");
		sqlRequest.append("                                 seq3, ");
		sqlRequest.append("                                 seq4, ");
		sqlRequest.append("                                 seq5, ");
		sqlRequest.append("                                 seq6, ");
		sqlRequest.append("                                 seq7, ");
		sqlRequest.append("                                 seq8 ");
		sqlRequest.append("                          FROM   routing_customer ");
		sqlRequest.append(" where  (DATE(END_DATE) >= DATE(?) OR DATE(END_DATE) IS NULL)) RTC , customer ctm ");
		params.add(dateNow);
		sqlRequest.append("                  WHERE  VP.routing_id = RT.routing_id ");
		sqlRequest.append("                         AND RTC.customer_id = ctm.customer_id ");
		sqlRequest.append("                         AND ctm.status = 1 ");
		sqlRequest.append("                         AND RTC.routing_id = RT.routing_id ");
		sqlRequest.append("                         AND Date(VP.from_date) <= Date(?) ");
		params.add(dateNow);
		sqlRequest.append("                         AND Ifnull(Date(VP.to_date) >= Date(?) ");
		params.add(dateNow);
		sqlRequest.append("                             , 1) ");
		sqlRequest.append("                         AND VP.status = 1 ");
		sqlRequest.append("                         AND RT.status = 1 ");
		sqlRequest.append("                         AND RTC.status = 1 ");
		sqlRequest.append("                         AND RTC.inweek = 1 ");
		sqlRequest.append("                         AND RTC." + day + " = 1 ");
		sqlRequest.append("                  GROUP  BY vp.staff_id) PLAN ");
		sqlRequest.append("              ON staff.staff_id = PLAN.staff_id_1 ");
		sqlRequest.append("       LEFT JOIN (SELECT al.staff_id      AS staff_id_2, ");
		sqlRequest.append("                         ct.customer_name customer_name, ");
		sqlRequest.append("                         ct.customer_code customer_code, ");
		sqlRequest.append("                         CASE ");
		sqlRequest.append("                           WHEN Max(al.start_time) THEN al.[lat] ");
		sqlRequest.append("                           ELSE 0 ");
		sqlRequest.append("                         END              lat, ");
		sqlRequest.append("                         CASE ");
		sqlRequest.append("                           WHEN Max(al.start_time) THEN al.[lng] ");
		sqlRequest.append("                           ELSE 0 ");
		sqlRequest.append("                         END              lng, ");
		sqlRequest.append("                         ( CASE ");
		sqlRequest.append("                             WHEN Time(Strftime('%H:%M', Min(al.start_time))) <= ");
		sqlRequest.append("                                  Time( ");
		sqlRequest.append("                                  Strftime('%H:%M', ?)) THEN ");
		params.add(startTime);
		sqlRequest.append("                             Strftime('%H:%M', Min(al.start_time)) ");
		sqlRequest.append("                             ELSE \"\" ");
		sqlRequest.append("                           END )          AS start_time, ");
		sqlRequest.append("                         al.is_or, ");
		sqlRequest.append("                         Count(CASE ");
		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', al.start_time)) ");
		sqlRequest.append("                                        <= Time( ");
		sqlRequest.append("                                        Strftime('%H:%M', ?)) ");
		params.add(middleTime);
		sqlRequest.append("                                        AND al.is_or = 0 ) THEN 1 ");
		sqlRequest.append("                               END)       AS lessMiddle, ");
//		sqlRequest.append("                         Count(CASE ");
//		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', al.start_time)) ");
//		sqlRequest.append("                                        <= Time( ");
//		sqlRequest.append("                                        Strftime('%H:%M', ?)) ");
//		params.add(endTime);
//		sqlRequest.append("                                        AND al.is_or = 0 ) THEN 1 ");
//		sqlRequest.append("                               END)       AS lessEnd, ");
		sqlRequest.append("                         Count(CASE ");
		sqlRequest.append("                                 WHEN ( al.is_or = 0 ) THEN 1 ");
		sqlRequest.append("                               END)       AS lessEnd, ");
		sqlRequest.append("                         Count(CASE ");
		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', al.start_time)) ");
		sqlRequest.append("                                        <= Time( ");
		sqlRequest.append("                                                 Strftime('%H:%M', ?)) ");
		params.add(dateNow);
		sqlRequest.append("                                        AND al.is_or = 0 ) THEN 1 ");
		sqlRequest.append("                               END)       AS lessnow_total_visited, ");
		sqlRequest.append("                         Count(CASE ");
		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', al.start_time)) ");
		sqlRequest.append("                                        <= Time( ");
		sqlRequest.append("                                                 Strftime('%H:%M', ?)) ");
		params.add(dateNow);
		sqlRequest.append("                                        AND al.is_or = 0 ) THEN 1 ");
		sqlRequest.append("                               END)       AS lessnow_total_visited_in_plan ");
		sqlRequest.append("                  FROM   action_log al, ");
		sqlRequest.append("                         customer ct ");
		sqlRequest.append("                  WHERE  al.object_type IN ( 0, 1 ) ");
		sqlRequest.append("                         AND ct.status = 1 ");
		sqlRequest.append("                         AND al.is_or = 0 ");
		sqlRequest.append("                         AND Date(al.start_time) = Date(?) ");
		params.add(dateNow);
		sqlRequest.append("                         AND al.customer_id = ct.customer_id ");
		sqlRequest.append("                  GROUP  BY al.staff_id) actionlog ");
		sqlRequest.append("              ON staff.staff_id = actionLog.staff_id_2 ");
		sqlRequest.append("       LEFT JOIN (SELECT cus_order.staff_id staff_Id, ");
		sqlRequest.append("                         Count(CASE ");
		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', ");
		sqlRequest.append("                                             cus_order.start_time)) ");
		sqlRequest.append("                                        <= ");
		sqlRequest.append("                                               Time(Strftime('%H:%M', ?)) ");
		params.add(middleTime);
		sqlRequest.append("                                      ) THEN ");
		sqlRequest.append("                                 cus_order.customer_id ");
		sqlRequest.append("                               END)         numMiddle, ");
//		// [Quang] bo khong check end time
//		sqlRequest.append("                         Count(CASE ");
//		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', ");
//		sqlRequest.append("                                             cus_order.start_time)) ");
//		sqlRequest.append("                                        <= ");
//		sqlRequest.append("                                               Time(Strftime('%H:%M', ?)) ");
//		params.add(endTime);
//		sqlRequest.append("                                      ) THEN ");
//		sqlRequest.append("                                 cus_order.customer_id ");
//		sqlRequest.append("                               END)         AS numEnd, ");
//		// [Quang] bo khong check end time
		sqlRequest.append("                         Count(cus_order.customer_id) AS numEnd,"); 
		
		sqlRequest.append("                         Count(CASE ");
		sqlRequest.append("                                 WHEN ( Time(Strftime('%H:%M', ");
		sqlRequest.append("                                             cus_order.start_time)) ");
		sqlRequest.append("                                        <= Time( ");
		sqlRequest.append("                                               Strftime('%H:%M', ?)) ) ");
		params.add(dateNow);
		sqlRequest.append("                               THEN ");
		sqlRequest.append("                                 cus_order.customer_id ");
		sqlRequest.append("                               END)         numCurrent ");
		sqlRequest.append("                  FROM   (SELECT staff_id, ");
		sqlRequest.append("                                 customer_id, ");
		sqlRequest.append("                                 Min(start_time) AS start_Time ");
		sqlRequest.append("                          FROM   action_log ");
		sqlRequest.append("                          WHERE  object_type = 4 ");
		sqlRequest.append("                                 AND Date(start_time) = Date(?) ");
		params.add(dateNow);
		sqlRequest.append("                                 AND is_or = 0 ");
		sqlRequest.append("                          GROUP  BY customer_id, ");
		sqlRequest.append("                                    staff_id ");
		sqlRequest.append("                          ORDER  BY staff_id) cus_order ");
		sqlRequest.append("                  GROUP  BY cus_order.staff_id) actionlog2 ");
		sqlRequest.append("              ON staff.staff_id = actionlog2.staff_id ");
		sqlRequest.append("ORDER  BY staff_code, ");
		sqlRequest.append("          staff_name ");

		String[] param = new String[params.size()] ;
		param=params.toArray(param);
		Cursor c = null;
		try {
			c = rawQuery(sqlRequest.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						ReportNVBHVisitCustomerDTO object = new ReportNVBHVisitCustomerDTO();
						object.initObjectWithCursor(c);
						listReportNVBH.add(object);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
				}
			}
		}

		return listReportNVBH;
	}

	/**
	 * Lay action cho phep dat hang tu xa o ngay hien tai cau staff, customer
	 * tuong ung
	 * 
	 * @author: BangHN
	 * @param staffId
	 * @param customerId
	 * @return
	 * @return: ActionLogDTO
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */

	public ActionLogDTO getActionLogExceptionOrderDate(String staffId, String customerId) {
		String dateNow= DateUtils.now();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT * ");
		sql.append("FROM   action_log ");
		sql.append("WHERE  staff_id = ? ");
		sql.append("       AND customer_id = ? ");
		sql.append("       AND object_type = 5 ");
		sql.append("       AND Date([start_time]) = Date(?) limit 1");

		String[] params = new String[] { staffId, customerId, dateNow };
		ActionLogDTO actionLog = null;
		Cursor c = null;
		try {
			c = rawQuery(sql.toString(), params);
			if (c != null) {
				actionLog = new ActionLogDTO();
				actionLog.initDataFromCursor(c);
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			// TODO: handle exception
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return actionLog;
	}

	/**
	 * TamPQ
	 * 
	 * @param dto
	 * @return
	 */
	public long updateVisitEndtime(ActionLogDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(END_TIME, dto.endTime);
		editedValues.put(INTERVAL_TIME, dto.interval_time);
		String[] params = { "" + dto.id };
		return update(editedValues, ACTION_LOG_ID + " = ?", params);
	}

	/**
	 * Lay thong tin action log xem da ghe tham KH hay chua
	 * @author: dungnt19
	 * @since: 11:44:37 16-12-2013
	 * @return: ActionLogDTO
	 * @throws:  
	 * @param staffId
	 * @param customerId
	 * @return
	 */
	public ActionLogDTO checkTNPGHaveActionFromActionLog(String staffId, String customerId) {
		String date_now = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
		ActionLogDTO dto = null;
		StringBuffer var1 = new StringBuffer();
		ArrayList<String> param = new ArrayList<String>();
		var1.append("SELECT AL.ACTION_LOG_ID              AS ACTION_LOG_ID, ");
		var1.append("       AL.STAFF_ID        AS STAFF_ID, ");
		var1.append("       AL.CUSTOMER_ID     AS CUSTOMER_ID, ");
		var1.append("       AL.OBJECT_ID       AS OBJECT_ID, ");
		var1.append("       AL.OBJECT_TYPE     AS OBJECT_TYPE, ");
		var1.append("       AL.LAT             AS LAT, ");
		var1.append("       AL.LNG             AS LNG, ");
		var1.append("       MAX(AL.START_TIME) AS START_TIME, ");
		var1.append("       AL.END_TIME        AS END_TIME, ");
		var1.append("       AL.IS_OR           AS IS_OR ");
		var1.append("FROM   ACTION_LOG AL ");
		var1.append("WHERE  DATE(AL.START_TIME) = DATE(?) ");
		param.add(date_now);
		var1.append("       AND AL.OBJECT_TYPE in (6,7,8) ");
//		var1.append("       AND AL.END_TIME IS NULL ");
		var1.append("       AND AL.STAFF_ID = ? ");
		param.add(staffId);
		var1.append("       AND AL.CUSTOMER_ID = ? ");
		param.add(customerId);
		var1.append("GROUP  BY AL.OBJECT_TYPE");

		Cursor c = null;
		try {
			c = rawQueries(var1.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					dto = new ActionLogDTO();
					dto.initDataFromCursor(c);
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
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
}
