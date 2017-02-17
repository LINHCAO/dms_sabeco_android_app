/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.util.DateUtils;

/**
 * 
 * table exception day define from VNM
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class EXCEPTION_DAY_TABLE extends ABSTRACT_TABLE {

	public static final String ID = "ID";
	public static final String DAY_OF = "DAY_OF";
	public static final String TYPE = "TYPE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String EXCEPTION_DAY_TABLE = "EXCEPTION_DAY";

	public EXCEPTION_DAY_TABLE(SQLiteDatabase mDB) {
		this.tableName = EXCEPTION_DAY_TABLE;
		this.columns = new String[] { ID, DAY_OF, TYPE, UPDATE_DATE };
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
	 * get number day working in month
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getWorkingDaysOfMonth() throws Exception{
		Date dateE = new Date();
		return getWorkingDaysOfMonth(dateE);
	}
	
	/**
	 * 
	 * get number day working in month
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getWorkingDaysOfMonth(Date dateE) throws Exception {
		Date dateS = DateUtils.getStartTimeOfMonth(dateE);
		int workDays = DateUtils.getWorkingDaysBetweenTwoDates(dateS, dateE);
		Cursor c = null;
		try {
			SimpleDateFormat sfd = DateUtils.defaultSqlDateFormat;
			String sDateE = sfd.format(dateE);
			String sDateS = sfd.format(dateS);
			String sqlQuery = "SELECT distinct * FROM EXCEPTION_DAY"
					+ " WHERE date(EXCEPTION_DAY.DAY_OFF, 'start of day') <= ?"
					+ " and date(EXCEPTION_DAY.DAY_OFF, 'start of day') >= ?";
			c = this.rawQuery(sqlQuery, new String[] { sDateE, sDateS });
			if (c.moveToFirst()) {
				do {
					String ds = c.getString(c.getColumnIndex("DAY_OFF"));
					Date dd = sfd.parse(ds);
					if (!DateUtils.isSunday(dd)) {
						workDays--;
					}
				} while (c.moveToNext());
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
		return workDays >= 0 ? workDays : 0;
	}

	/**
	 * 
	 * lay d/s so ngay nghi trong nam
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: ArrayList<String>
	 * @throws:
	 * @since: Feb 28, 2013
	 */
	public ArrayList<String> getListExceptionDay() {
		ArrayList<String> listExcepTionDay = new ArrayList<String>();
		String sqlQuery = "SELECT distinct date(DAY_OFF) DAY_OFF FROM EXCEPTION_DAY";
		Cursor c = null;
		try {
			c = this.rawQuery(sqlQuery, null);
			if (c.moveToFirst()) {
				do {
					String ds = c.getString(c.getColumnIndex("DAY_OFF"));
					listExcepTionDay.add(ds);
				} while (c.moveToNext());
			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return listExcepTionDay;
	}
	
	/**
	 * 
	*  Check exception day
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: int
	*  @throws:
	 */
	public boolean checkExceptionDay(String date) {
		boolean result = false;
		Cursor c = null;
		try {
			String sqlQuery = "SELECT * FROM EXCEPTION_DAY WHERE date(EXCEPTION_DAY.DAY_OFF, 'start of day') = date(?, 'start of day')";
			c = this.rawQuery(sqlQuery, new String[] { date });
			if (c.moveToFirst()) {
				result = true;
			}
		} catch (Exception e) {
		}finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception ex) {
				}
			} else {

			}
		}
		return result;
	}
}
