/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.CustomerCanLoseItemDto;
import com.viettel.dms.dto.view.SaleInMonthItemDto;
import com.viettel.dms.dto.view.StaffInfoDTO;
import com.viettel.dms.util.DateUtils;

/**
 * RPT SALE HISTORY
 * RPT_SALE_HISTORY.java
 * @version: 1.0 
 * @since:  08:27:04 20 Jan 2014
 */
public class RPT_SALE_HISTORY extends ABSTRACT_TABLE {
	public static final String ID = "ID";
	public static final String STAFF_ID = "STAFF_ID";
	public static final String MONTH = "MONTH";
	public static final String NUM_CUSTOMER = "NUM_CUSTOMER";
	public static final String NUM_CUST_NOT_ORDER = "NUM_CUST_NOT_ORDER";
	public static final String TOTAL_VALUE_OF_ORDER = "TOTAL_VALUE_OF_ORDER";
	public static final String NUM_SKU = "NUM_SKU";
	public static final String CREATE_DATE = "CREATE_DATE";

	public static final String RPT_SALE_HISTORY = "RPT_SALE_HISTORY";

	public RPT_SALE_HISTORY(SQLiteDatabase mDB) {
		this.tableName = RPT_SALE_HISTORY;
		this.columns = new String[] { ID, STAFF_ID, MONTH, NUM_CUSTOMER, NUM_CUST_NOT_ORDER, TOTAL_VALUE_OF_ORDER,
				NUM_SKU, CREATE_DATE, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}


	@Override
	protected long insert(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		return 0;
	}

	public StaffInfoDTO getStaffInformation(String staffId, int page, int isGetTotalPage, int isLoadSaleInMonth) throws Exception {
		StaffInfoDTO dto = new StaffInfoDTO();
		String dateFirstLastOneMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT,-1);
		String dateFirstLastThreeMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -3);
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		ArrayList<String> params = new ArrayList<String>();
		StringBuffer var = new StringBuffer();
		var.append("SELECT RSH.rpt_sale_history_id   AS ID, ");
		var.append("       Strftime('%m', RSH.month) AS MONTH, ");
		var.append("       RSH.month 				 AS MONTH_2, ");
		var.append("       RSH.num_customer          AS NUM_CUSTOMER, ");
		var.append("       RSH.num_cust_not_order AS NUM_CUST_NOT_ORDER, ");
		var.append("       RSH.TOTAL_QUANTITY_OF_ORDER  AS TOTAL_VALUE_OF_ORDER, ");
		var.append("       RSH.create_date           AS CREATE_DATE ");
		var.append("FROM   rpt_sale_history RSH, staff ");
		var.append("WHERE  staff.staff_id = ? ");
		params.add(staffId);
		var.append("		and RSH.staff_id = staff.staff_id ");
		var.append("		and staff.status = 1 ");
		var.append("       AND Date(month) <= ? ");
		params.add(dateFirstLastOneMonth);
		var.append("       AND Date(month) >= ? ");
		params.add(dateFirstLastThreeMonth);
		var.append("ORDER  BY MONTH_2 ASC ");

		Cursor curList = null;
		try {
			curList = rawQueries(var.toString(), params);

			if (curList.moveToFirst()) {
				do {
					SaleInMonthItemDto item = new SaleInMonthItemDto();
					item.initDataFromCursor(curList);
					dto.arrSaleProgress.add(item);
				} while (curList.moveToNext());
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (curList != null) {
					curList.close();
				}
			} catch (Exception ex) {

			}
		}

		ArrayList<String> param1 = new ArrayList<String>();
		ArrayList<String> param2 = new ArrayList<String>();
		StringBuffer var1 = new StringBuffer();
		var1.append("SELECT CUS.*, ");
		var1.append("       STC.LAST_ORDER, ");
		var1.append("       Strftime('%d/%m/%Y', STC.LAST_APPROVE_ORDER) AS LAST_APPROVE_ORDER ");
		var1.append("FROM   (SELECT VP.STAFF_ID, ");
		var1.append("               CT.CUSTOMER_ID, ");
		//var1.append("               SUBSTR(CT.CUSTOMER_CODE, 1, 3) AS CUSTOMER_CODE, ");
		var1.append("               CT.SHORT_CODE AS CUSTOMER_CODE, ");
		var1.append("               CT.CUSTOMER_NAME, ");
		var1.append("               CT.ADDRESS 					AS ADDRESS, ");
		var1.append("               ( CT.HOUSENUMBER ");
		var1.append("                 || ' ' ");
		var1.append("                 || CT.STREET )               AS STREET ");
		var1.append("        FROM   VISIT_PLAN VP, ");
		var1.append("               ROUTING RT, ");
		var1.append("               ROUTING_CUSTOMER RTC, ");
		var1.append("               CUSTOMER CT ");
		var1.append("        WHERE  substr(VP.FROM_DATE,0,11) <= ? ");
		param1.add(dateNow);
		param2.add(dateNow);
		var1.append("               AND IFNULL(substr(VP.TO_DATE,0,11) >= ?, 1) ");
		param1.add(dateNow);
		param2.add(dateNow);
		var1.append("        		AND substr(RTC.START_DATE,0,11) <= ? ");
		param1.add(dateNow);
		param2.add(dateNow);
		var1.append("               AND IFNULL(substr(RTC.END_DATE,0,11) >= ?, 1) ");
		param1.add(dateNow);
		param2.add(dateNow);
		var1.append("               AND VP.ROUTING_ID = RT.ROUTING_ID ");
		var1.append("               AND RT.ROUTING_ID = RTC.ROUTING_ID ");
		var1.append("               AND RTC.CUSTOMER_ID = CT.CUSTOMER_ID ");
		var1.append("               AND VP.STAFF_ID = ? ");
		param1.add(staffId);
		param2.add(staffId);
		var1.append("               AND VP.STATUS = 1 ");
		var1.append("               AND RTC.STATUS = 1 ");
		var1.append("               AND CT.STATUS = 1 ");
		var1.append("               AND RT.STATUS = 1 ");
		//var1.append("               AND RTC.START_WEEK <= ( ROUND(STRFTIME('%W', '" + date_now + "')) + 1 ) ");
		var1.append("        GROUP  BY CT.CUSTOMER_ID) CUS ");
		var1.append("       LEFT JOIN STAFF_CUSTOMER STC ");
		var1.append("              ON ( CUS.STAFF_ID = STC.STAFF_ID ");
		var1.append("                   AND CUS.CUSTOMER_ID = STC.CUSTOMER_ID ) ");
		var1.append("WHERE  ( STC.LAST_APPROVE_ORDER IS NULL ");
		var1.append("          OR substr(STC.LAST_APPROVE_ORDER,0,11) < ?) ");
		param1.add(dateFirstLastOneMonth);
		param2.add(dateFirstLastOneMonth);
		var1.append("       AND ( STC.LAST_ORDER IS NULL ");
		var1.append("              OR substr(STC.LAST_ORDER,0,11) < ?) ");
		param1.add(dateNow);
		param2.add(dateNow);

		// get count
		StringBuilder getTotalCusCanLose = new StringBuilder();
		getTotalCusCanLose.append("select count(*) as TOTAL_ROW from (" + var1.toString() + ")");

		var1.append(" limit ? offset ?");
		param1.add("" + Constants.NUM_ITEM_PER_PAGE);
		param1.add("" + (page - 1) * Constants.NUM_ITEM_PER_PAGE);

		Cursor curCusCanLose = null;

		try {
			curCusCanLose = rawQueries(var1.toString(), param1);

			if (curCusCanLose.moveToFirst()) {
				do {
					CustomerCanLoseItemDto item = new CustomerCanLoseItemDto();
					item.initDataFromCursor(curCusCanLose);
					dto.arrCusCanLose.add(item);
				} while (curCusCanLose.moveToNext());
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (curCusCanLose != null) {
					curCusCanLose.close();
				}
			} catch (Exception ex) {

			}
		}
		Cursor curTotalCusCanLose =null;
		try{
			curTotalCusCanLose = rawQueries(getTotalCusCanLose.toString(), param2);
			if (curTotalCusCanLose != null) {
				if (curTotalCusCanLose.moveToFirst()) {
					dto.totalCusCanLose = curTotalCusCanLose.getInt(0);
				}
			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			try {
				if (curTotalCusCanLose != null) {
					curTotalCusCanLose.close();
				}
			} catch (Exception ex) {

			}
		}

		return dto;
	}

}
