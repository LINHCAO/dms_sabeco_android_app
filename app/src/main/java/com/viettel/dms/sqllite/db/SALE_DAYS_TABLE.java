/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.Date;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.util.DateUtils;

/**
 * 
 *  table sale day
 *  @author: HaiTC3
 *  @version: 1.1
 *  @since: 1.0
 */
public class SALE_DAYS_TABLE extends ABSTRACT_TABLE {

	public static final String SALE_DAYS_ID = "SALE_DAYS_ID";
	public static final String YEAR = "YEAR";
	public static final String T1 = "T1";
	public static final String T2 = "T2";
	public static final String T3 = "T3";
	public static final String T4 = "T4";
	public static final String T5 = "T5";
	public static final String T6 = "T6";
	public static final String T7 = "T7";
	public static final String T8 = "T8";
	public static final String T9 = "T9";
	public static final String T10 = "T10";
	public static final String T11 = "T11";
	public static final String T12 = "T12";
	public static final String STATUS = "STATUS";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String RPT_STAFF_SALE_TABLE = "RPT_STAFF_SALE";
	
	public SALE_DAYS_TABLE() {
		this.tableName = RPT_STAFF_SALE_TABLE;
		this.columns = new String[] { SALE_DAYS_ID, YEAR, T1, T2, T3, T4, T5,
				T6, T7, T8, T9, T10, T11, T12, STATUS, CREATE_DATE, CREATE_USER };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = SQLUtils.getInstance().getmDB();
	}

	public SALE_DAYS_TABLE(SQLiteDatabase mDB) {
		this.tableName = RPT_STAFF_SALE_TABLE;
		this.columns = new String[] { SALE_DAYS_ID, YEAR, T1, T2, T3, T4, T5,
				T6, T7, T8, T9, T10, T11, T12, STATUS, CREATE_DATE, CREATE_USER };
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

	/**
	 * 
	*  get number day working in month plan
	*  @author: HaiTC3
	*  @param date
	*  @return
	*  @return: int
	*  @throws:
	 */
	public int getPlanWorkingDaysOfMonth(Date date) throws Exception{
		Cursor c = null;
		int result = 0;
		try {
			int year = DateUtils.getYear(date);
			int month = DateUtils.getMonth(date) + 1;
			String sqlQuery = "SELECT * FROM sale_day WHERE year = ?";
			c = this.rawQuery(sqlQuery, new String[]{String.valueOf(year)});
			if (c.moveToFirst()) {
				result = c.getInt(c.getColumnIndex("T" + month));
			}
		} catch (Exception e) {
			throw e;
		} finally{
			if(c!=null){
				try {
					c.close();
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		}
		return result;
	}
}
