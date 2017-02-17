/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.PGTakeAttendanceDTO;
import com.viettel.dms.dto.view.PGTakeAttendanceViewDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Table tuyen cua PG
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class PG_VISIT_PLAN_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String PG_VISIT_PLAN_ID = "PG_VISIT_PLAN_ID";
	// staff_id
	public static final String STAFF_ID = "STAFF_ID";
	// shop id
	public static final String SHOP_ID = "SHOP_ID";
	// id truong nhom tiep thi
	public static final String PARENT_STAFF_ID = "PARENT_STAFF_ID";
	// id KH
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	public static final String FROM_DATE = "FROM_DATE";
	public static final String TO_DATE = "TO_DATE";
	public static final String STATUS = "STATUS";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String UPDATE_USER = "UPDATE_USER";

	public static final String PG_VISIT_PLAN_TABLE = "PG_VISIT_PLAN";

	public PG_VISIT_PLAN_TABLE(SQLiteDatabase mDB) {
		this.tableName = PG_VISIT_PLAN_TABLE;
		this.columns = new String[] { PG_VISIT_PLAN_ID, STAFF_ID, SHOP_ID, PARENT_STAFF_ID, CUSTOMER_ID, FROM_DATE, TO_DATE, STATUS, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER };
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
	 * Lay ds PG de cham cong
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @throws:
	 */
	public PGTakeAttendanceViewDTO getListPGForTakeAttendance(Bundle data) {
		PGTakeAttendanceViewDTO result = new PGTakeAttendanceViewDTO();
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);

		StringBuffer sqlQuery = new StringBuffer();
		ArrayList<String> params = new ArrayList<String>();
		sqlQuery.append(" select st.*, tk.is_absent IS_ABSENT, tk.dresses DRESSES, tk.rule RULE, tk.start_time START_TIME, tk.time_keeper_id TIME_KEEPER_ID, tk.create_date CREATE_DATE, tk.create_user CREATE_USER from (SELECT ST.STAFF_ID STAFF_ID, ST.STAFF_CODE STAFF_CODE, ST.STAFF_NAME STAFF_NAME ");
		sqlQuery.append(" FROM PG_VISIT_PLAN PGVP, STAFF ST "); 
		sqlQuery.append(" WHERE 1 = 1 ");
		sqlQuery.append(" 	AND PGVP.STAFF_ID = ST.STAFF_ID ");
		sqlQuery.append(" 	AND PGVP.PARENT_STAFF_ID = ? ");
		sqlQuery.append(" 	AND PGVP.STATUS = 1 AND ST.STATUS=1 ");
		params.add(staffId);
		sqlQuery.append(" 	AND PGVP.SHOP_ID = ? ");
		params.add(shopId);
		sqlQuery.append(" 	AND PGVP.CUSTOMER_ID = ? ");
		params.add(customerId);
		sqlQuery.append(" 	AND DATE(PGVP.FROM_DATE) <= DATE('now', 'localtime') ");
		sqlQuery.append(" 	AND IFNULL(DATE(PGVP.TO_DATE) >= DATE('now', 'localtime'), 1)) st ");
		sqlQuery.append(" 	left join (select is_absent, dresses, rule, start_time,max(create_date) create_date, create_user, staff_id, time_keeper_id from time_keeper where staff_owner_id = ? ");
		params.add(staffId);
		sqlQuery.append(" 				and shop_id = ? ");
		params.add(shopId);
		sqlQuery.append(" 				and customer_id = ? ");
		params.add(customerId);
		sqlQuery.append(" 				and date(create_date) = substr(?,0,11) ");
		params.add(dateNow);
		sqlQuery.append(" 				group by staff_id ");
		sqlQuery.append(" 	) tk on st.staff_id = tk.staff_id ");

		Cursor c = null;
		int hourOfDay = DateUtils.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
		int minute = DateUtils.getCurrentTimeByTimeType(Calendar.MINUTE);
		try {
			c = rawQueries(sqlQuery.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						PGTakeAttendanceDTO item = new PGTakeAttendanceDTO();
						item.hourOfDay = hourOfDay;
						item.minute = minute;
						item.timeOnWork = DateUtils.getDateString(DateUtils.DATE_FORMAT_NOW, hourOfDay, minute);
						item.staffId = StringUtil.getStringFromSQliteCursor(c, STAFF_TABLE.STAFF_ID);
						item.staffCode = StringUtil.getStringFromSQliteCursor(c, STAFF_TABLE.STAFF_CODE);
						item.staffName = StringUtil.getStringFromSQliteCursor(c, STAFF_TABLE.STAFF_NAME);

						//item.isDress = StringUtil.getIntFromSQliteCursor(c, TIME_KEEPER_TABLE.DRESSES) == 1 ? true : false;
						if(!StringUtil.isNullOrEmpty(c.getString(c.getColumnIndex(TIME_KEEPER_TABLE.IS_ABSENT)))) {
							item.isOnWork = StringUtil.getIntFromSQliteCursor(c, TIME_KEEPER_TABLE.IS_ABSENT) == 0 ? true : false;
						} else {
							item.isOnWork = false;
						}
						item.id = StringUtil.getLongFromSQliteCursor(c, TIME_KEEPER_TABLE.TIME_KEEPER_ID);
						Date date = null;
						if(item.isOnWork) {
							item.isDress = StringUtil.getIntFromSQliteCursor(c, TIME_KEEPER_TABLE.DRESSES) == 1 ? true : false;
							item.isFollowRule = StringUtil.getIntFromSQliteCursor(c, TIME_KEEPER_TABLE.RULE) == 1 ? true : false;
							date = DateUtils.parseDateTypeFromSqlLite(StringUtil.getStringFromSQliteCursor(c, "START_TIME"));
							if(date != null) {
								item.timeOnWork = DateUtils.parseStringFromDate(date);
								item.hourOfDay = date.getHours();
								item.minute = date.getMinutes();
							}
						}
						item.createDate = StringUtil.getStringFromSQliteCursor(c, TIME_KEEPER_TABLE.CREATE_DATE);
						item.createUser = StringUtil.getStringFromSQliteCursor(c, TIME_KEEPER_TABLE.CREATE_USER);
						result.listPG.add(item);
					} while (c.moveToNext());
				}
				
//				for(int i = 0; i < 5; i++) {
//					PGTakeAttendanceDTO item = new PGTakeAttendanceDTO();
//					item.hourOfDay = hourOfDay;
//					item.minute = minute;
//					item.timeOnWork = DateUtils.getDateString(DateUtils.DATE_FORMAT_NOW, hourOfDay, minute);
//					item.staffId = "" + i;
//					item.staffCode = "staffCode" + i;
//					item.staffName = "staffName " + i;
////					item.staffId = StringUtil.getStringFromSQliteCursor(c, STAFF_TABLE.STAFF_ID);
////					item.staffCode = StringUtil.getStringFromSQliteCursor(c, STAFF_TABLE.STAFF_CODE);
////					item.staffName = StringUtil.getStringFromSQliteCursor(c, STAFF_TABLE.STAFF_NAME);
//					
//					result.listPG.add(item);
//				}
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
		return result;
	}
}
