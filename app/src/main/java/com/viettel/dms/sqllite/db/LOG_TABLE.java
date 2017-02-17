/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Bang luu log request
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class LOG_TABLE extends ABSTRACT_TABLE{
	// id cua bang
	public static final String LOG_ID = "LOG_ID";
	// gia tri (cau lenh excute)
	public static final String VALUE = "VALUE";
	// userId
	public static final String USER_ID = "USER_ID";
	// trang thai log
	public static final String STATE = "STATE";
	// trang thai syn
	public static final String SYN_STATE = "SYN_STATE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay update
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi update
	public static final String UPDATE_USER = "UPDATE_USER";
	// table name - tam thoi chua dung
	public static final String TABLE_NAME = "TABLE_NAME";
	// table id - tam thoi chua dung
	public static final String TABLE_ID = "TABLE_ID";
	// table type
	public static final String TABLE_TYPE = "TABLE_TYPE";
	// co can kiem tra thoi gian truoc khi thuc hien request hay khong? : 0: khong can, 1: can kiem tra
	public static final String NEED_CHECK_DATE = "NEED_CHECK_DATE";
	
	private static final String TABLE_LOG = "LOG_TABLE";
	
	/**
	 * tao va mo mot CSDL
	 * @author: BangHN
	 * @return: SQLiteUtil
	 * @throws:
	 */
	public LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_LOG;
		this.columns = new String[] {LOG_ID,VALUE,USER_ID,STATE, SYN_STATE, 
				CREATE_DATE, UPDATE_DATE, CREATE_USER, UPDATE_USER, TABLE_NAME, TABLE_TYPE, TABLE_ID, NEED_CHECK_DATE};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	
	
	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((LogDTO) dto);
		return insert(null, value);
	}
	
	@Override
	protected long update(AbstractTableDTO dto) {
		ContentValues editedValues = new ContentValues();
		LogDTO log = (LogDTO)dto;
		editedValues.put(STATE, log.state);
		editedValues.put(UPDATE_DATE, log.updateDate);
		editedValues.put(UPDATE_USER, log.updateUser);
		String[] params = { "" + log.logId };
		return update(editedValues, LOG_ID + " = ?", params);
	}
	
	
	@Override
	protected long delete(AbstractTableDTO dto) {
		LogDTO log = (LogDTO)dto;
		String[] params = { log.logId};
		return delete(LOG_ID + " = ?", params);
	}
	
	/**
	 * init du lieu cho mot row (log)
	 * @author : BangHN
	 * since : 9:50:41 AM
	 */
	private ContentValues initDataRow(LogDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(LOG_ID, dto.logId);
		editedValues.put(VALUE, dto.value);
		editedValues.put(USER_ID, dto.userId);
		editedValues.put(STATE, dto.state);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(NEED_CHECK_DATE, dto.needCheckDate);
		editedValues.put(TABLE_TYPE, dto.tableType);
		if (!StringUtil.isNullOrEmpty(dto.tableId)){
			editedValues.put(TABLE_ID, dto.tableId);
		}
		editedValues.put(TABLE_NAME, dto.tableName);
		return editedValues;
	}
	

	
	private LogDTO initLogDTOFromCursor(Cursor c) {
		LogDTO logDTO = new LogDTO();
		logDTO.logId = c.getString(c.getColumnIndex(LOG_ID));
		logDTO.value = c.getString(c.getColumnIndex(VALUE));
		logDTO.userId = c.getString(c.getColumnIndex(USER_ID));
		logDTO.state = c.getString(c.getColumnIndex(STATE));
		logDTO.createDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndex(CREATE_DATE)));
		logDTO.createUser = c.getString(c.getColumnIndex(CREATE_USER));
		logDTO.tableType = c.getInt(c.getColumnIndex(TABLE_TYPE));
		logDTO.tableId = c.getString(c.getColumnIndex(TABLE_ID));
		logDTO.tableName= c.getString(c.getColumnIndex(TABLE_NAME));
		logDTO.needCheckDate = c.getInt(c.getColumnIndex(NEED_CHECK_DATE));
		if(c.getColumnIndex("IS_CHECK_TIME")>0){
			logDTO.isCheckTime = c.getInt(c.getColumnIndex("IS_CHECK_TIME"));
		}
		return logDTO;
	}
	
	/**
	*  Lay ds log tao moi hoac xu ly that bai
	*  @author: TruongHN
	*  @return: ArrayList<LogDTO>
	*  @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToCheck() {
		ArrayList<LogDTO> v = new ArrayList<LogDTO>();
		String datePrevious = DateUtils.getDateOfNumberPreviousDateWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -1);
		Cursor c = null;
		try {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(" STATE != ? and STATE != ? and STATE != ? and STATE != ? and STATE != ? and ") ;
			strBuffer.append(" substr(CREATE_DATE,0,11) >= ? ");
			
			String[] params = { LogDTO.STATE_SUCCESS, LogDTO.STATE_INVALID_TIME, 
					LogDTO.STATE_UNIQUE_CONTRAINTS, LogDTO.STATE_ORDER_DELETED, LogDTO.STATE_IMAGE_DELETED, datePrevious};
			c = query(strBuffer.toString(), params, null, null, CREATE_DATE + " asc");
			
			if (c != null) {
				LogDTO LogDTO;
				if (c.moveToFirst()) {
					do {
						LogDTO = initLogDTOFromCursor(c);
						v.add(LogDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getLogNeedToCheck", e.getMessage());
			}
		}
		
		return v;
	}
	/**
	 *  Lay ds log tao moi hoac xu ly that bai
	 *  Chi lay nhung request can thiet, sau do thuc hien dong bo ve cho nhanh
	 *  @author: TruongHN
	 *  @return: ArrayList<LogDTO>
	 *  @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToCheckBeforeSync() {
		ArrayList<LogDTO> v = new ArrayList<LogDTO>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		Cursor c = null;
		try {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(" STATE != ? and STATE != ? and STATE != ? and STATE != ? and ") ;
			strBuffer.append(" (TABLE_TYPE = ? or TABLE_TYPE = ? or TABLE_TYPE = ? ) and substr(CREATE_DATE,0,11) >= ? ");
			
			String[] params = { LogDTO.STATE_SUCCESS,
					LogDTO.STATE_INVALID_TIME, LogDTO.STATE_UNIQUE_CONTRAINTS,
					LogDTO.STATE_ORDER_DELETED,
					String.valueOf(LogDTO.TYPE_NORMAL),
					String.valueOf(LogDTO.TYPE_ORDER),
					String.valueOf(LogDTO.TYPE_POSITION), dateNow };
			c = query(strBuffer.toString(), params, null, null, CREATE_DATE + " asc");
			
			if (c != null) {
				LogDTO LogDTO;
				if (c.moveToFirst()) {
					do {
						LogDTO = initLogDTOFromCursor(c);
						v.add(LogDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getLogNeedToCheck", e.getMessage());
			}
		}
		
		return v;
	}
	
	/**
	 *  Lay ds log tao moi hoac xu ly that bai
	 *  Chi lay nhung request can thiet, sau do thuc hien dong bo ve cho nhanh
	 *  @author: TruongHN
	 *  @return: ArrayList<LogDTO>
	 *  @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToCheckForLogin() {
		ArrayList<LogDTO> v = new ArrayList<LogDTO>();
		String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
		String dateBefore = DateUtils.getDateOfNumberPreviousDateWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -GlobalInfo.getInstance().getTimeCheckToServer());
		//String timeCheckToServer = "-" + GlobalInfo.getInstance().getTimeCheckToServer() + " day";
		String pgNumberSendOrder = "-" + GlobalInfo.getInstance().getPGNumberSendOrder() + " day";
		Cursor c = null;
		try {
			StringBuffer  strBuffer = new StringBuffer();
			ArrayList<String> params = new ArrayList<String>();
			strBuffer.append("SELECT * ");
			strBuffer.append("		,'0' AS  IS_CHECK_TIME ");
			strBuffer.append("FROM   [log_table] ");
			strBuffer.append("WHERE  (state = ? ");
			params.add(LogDTO.STATE_NEW);
			strBuffer.append("       AND table_type = ? ");
			params.add(String.valueOf(LogDTO.TYPE_NORMAL));
			strBuffer.append("       AND substr(create_date,0,11) < ?) ");
			params.add(dateNow);
			strBuffer.append("		OR (state = ? ");
			params.add(LogDTO.STATE_NEW);
			strBuffer.append("       AND table_type = ? ");
			params.add(String.valueOf(LogDTO.TYPE_ORDER));
			strBuffer.append("       AND substr(create_date,0,11) < ?) ");
			params.add(dateBefore);
			
			strBuffer.append("UNION ");
			strBuffer.append("SELECT * ");
			strBuffer.append("		,'1' AS  IS_CHECK_TIME ");
			strBuffer.append("FROM   [log_table] ");
			strBuffer.append("WHERE  state != ? ");
			strBuffer.append("       AND state != ? ");
			strBuffer.append("       AND state != ? ");
			strBuffer.append("       AND state != ? ");
			params.add(LogDTO.STATE_SUCCESS);
			params.add(LogDTO.STATE_INVALID_TIME);
			params.add(LogDTO.STATE_UNIQUE_CONTRAINTS);
			params.add(LogDTO.STATE_ORDER_DELETED);
			strBuffer.append("       AND ( table_type = ? ");
			strBuffer.append("              OR table_type = ?  or table_type = ? ) ");
			params.add(String.valueOf(LogDTO.TYPE_NORMAL));
			params.add(String.valueOf(LogDTO.TYPE_ORDER));
			params.add(String.valueOf(LogDTO.TYPE_POSITION));
			strBuffer.append("       AND substr(create_date,0,11) >= ? ");
			params.add(dateNow);
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PG) {
				strBuffer.append("UNION ");
				strBuffer.append("SELECT * ");
				strBuffer.append("		,'0' AS  IS_CHECK_TIME ");
				strBuffer.append("FROM   [log_table] ");
				strBuffer.append("WHERE  (state = ? ");
				params.add(LogDTO.STATE_NEW);
				strBuffer.append("		 OR state = ?) ");
				params.add(LogDTO.STATE_INVALID_TIME);
				strBuffer.append("       AND (table_type = ? ");
				params.add(String.valueOf(LogDTO.TYPE_ORDER));
				strBuffer.append(" 		 AND date(create_date) >= date('now', 'localtime', ? )) ");
				params.add(pgNumberSendOrder);
			}
			
//			strBuffer.append("SELECT * ");
//			strBuffer.append("FROM   [log_table] ");
//			strBuffer.append("WHERE  state = ? ");
//			params.add(LogDTO.STATE_NEW);
//			strBuffer.append("       AND ( table_type = ? ");
//			strBuffer.append("              OR table_type = ? ) ");
//			params.add(String.valueOf(LogDTO.TYPE_NORMAL));
//			params.add(String.valueOf(LogDTO.TYPE_ORDER));
//			strBuffer.append("       AND Date(create_date) < Date(?) ");
//			params.add(dateNow);
//			strBuffer.append("UNION ");
//			strBuffer.append("SELECT * ");
//			strBuffer.append("FROM   [log_table] ");
//			strBuffer.append("WHERE  state != ? ");
//			strBuffer.append("       AND state != ? ");
//			strBuffer.append("       AND state != ? ");
//			strBuffer.append("       AND state != ? ");
//			params.add(LogDTO.STATE_SUCCESS);
//			params.add(LogDTO.STATE_INVALID_TIME);
//			params.add(LogDTO.STATE_UNIQUE_CONTRAINTS);
//			params.add(LogDTO.STATE_ORDER_DELETED);
//			strBuffer.append("       AND ( table_type = ? ");
//			strBuffer.append("              OR table_type = ?  or table_type = ? ) ");
//			params.add(String.valueOf(LogDTO.TYPE_NORMAL));
//			params.add(String.valueOf(LogDTO.TYPE_ORDER));
//			params.add(String.valueOf(LogDTO.TYPE_POSITION));
//			strBuffer.append("       AND Date(create_date) >= Date(?) ");
//			params.add(dateNow);
			
			c = rawQuery(strBuffer.toString(), params.toArray(new String[params.size()]));
			
			if (c != null) {
				LogDTO LogDTO;
				if (c.moveToFirst()) {
					do {
						LogDTO = initLogDTOFromCursor(c);
						v.add(LogDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getLogNeedToCheck", e.getMessage());
			}
		}
		
		return v;
	}
	
	
	/**
	 *  Lay ds log chua duoc xu ly trong vong 10 ngay
	 *  @author: BangHN
	 *  @return: ArrayList<LogDTO>
	 *  @throws:
	 */
	public ArrayList<LogDTO> getLogNeedToSynBefore10Days() {
		ArrayList<LogDTO> v = new ArrayList<LogDTO>();
		Cursor c = null;
		try {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append(" STATE != ? and STATE != ? and STATE != ? and STATE != ? and ") ;
			strBuffer.append(" TABLE_TYPE != 1 AND ");
			strBuffer.append(" date(CREATE_DATE) >= date('now', 'localtime', '-10 day') ");
			
			String[] params = { LogDTO.STATE_SUCCESS, LogDTO.STATE_INVALID_TIME, LogDTO.STATE_UNIQUE_CONTRAINTS,
					LogDTO.STATE_ORDER_DELETED};
			c = query(strBuffer.toString(), params, null, null, CREATE_DATE + " asc");
			if (c != null) {
				LogDTO LogDTO;
				if (c.moveToFirst()) {
					do {
						LogDTO = initLogDTOFromCursor(c);
						v.add(LogDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getLogNeedToCheck", e.getMessage());
			}
		}
		return v;
	}
	
	
	/**
	 *  Lay so luong request anh chua gui hoac loi
	 *  @author: BangHN
	 *  @return: ArrayList<LogDTO>
	 *  @throws:
	 */
	public ArrayList<String> getNumLogImageNotSynOrError() {
		ArrayList<String> v = new ArrayList<String>();
		Cursor c = null;
		try {
			StringBuffer strBuffer = new StringBuffer();
			strBuffer.append("SELECT count(1) as NUM, STATE ");
			strBuffer.append("FROM   LOG_TABLE ");
			strBuffer.append("WHERE  TABLE_TYPE = 2 ");
			strBuffer.append("       AND ( STATE = 0 ");
			strBuffer.append("              OR STATE = 6 ) ");
			strBuffer.append("       AND DATE(CREATE_DATE) > DATE('NOW', 'LOCALTIME', '-1 DAY') ");
			strBuffer.append("group by STATE ");
			strBuffer.append("order by STATE ");
			
			String[] params = {};
			c=  rawQuery(strBuffer.toString(), params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						String temp = "State " + c.getString(c.getColumnIndex("STATE"));
						temp += ": " + c.getString(c.getColumnIndex("NUM"));
						v.add(temp);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				VTLog.i("getNumLogImageNotSynOrError", e.getMessage());
			}
		}
		return v;
	}
	
	/**
	*  Lay tat ca log thuoc ve position
	*  @author: TruongHN
	*  @return: ArrayList<LogDTO>
	*  @throws:
	 */
	public ArrayList<LogDTO> getLogPosition() {
		ArrayList<LogDTO> v = new ArrayList<LogDTO>();
		Cursor c = null;
		try {
			c = query(STATE + " != " + LogDTO.STATE_SUCCESS + " and "
							+ STATE + " != " + LogDTO.STATE_INVALID_TIME + " and "
							+ STATE + " != " + LogDTO.STATE_UNIQUE_CONTRAINTS + " and "
							+ STATE + " != " + LogDTO.STATE_ORDER_DELETED + " and "
							+ TABLE_TYPE + " = " + LogDTO.TYPE_POSITION,
					null, null, null, CREATE_DATE + " asc");
			if (c != null) {
				LogDTO LogDTO;
				if (c.moveToFirst()) {
					do {
						LogDTO = initLogDTOFromCursor(c);
						v.add(LogDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
				VTLog.i("getLogPosition", e.getMessage());
			}
		}
		
		return v;
	}
	
	
	/**
	*  Cap nhat cac log cung ngay ve trang thai close
	*  @author: TruongHN
	*  @param dto
	*  @return: long
	*  @throws:
	 */
	public long updateLogsWithStateClose(AbstractTableDTO dto) {
		ContentValues editedValues = new ContentValues();
		LogDTO log = (LogDTO)dto;
		editedValues.put(STATE, LogDTO.STATE_INVALID_TIME);
		editedValues.put(UPDATE_DATE, log.updateDate);
		editedValues.put(UPDATE_USER, log.createUser);
		
		String date = "";
		int index = log.createDate.indexOf(" ");
		if (index > -1){
			date = log.createDate.substring(0, index);
		}else{
			date = log.createDate;
		}
		
		String[] params = { "%" + date + "%" };
		return update(editedValues, CREATE_DATE + " like ? and ( STATE = 0 or STATE  = 1)" , params);
	}
	
	/**
	*  Cap nhat log trang thai close
	*  @author: TruongHN
	*  @param dto
	*  @return: long
	*  @throws:
	 */
	public long updateLogWithStateClose(AbstractTableDTO dto) {
		ContentValues editedValues = new ContentValues();
		LogDTO log = (LogDTO)dto;
		editedValues.put(STATE, LogDTO.STATE_INVALID_TIME);
		editedValues.put(UPDATE_DATE, log.updateDate);
		editedValues.put(UPDATE_USER, log.updateUser);
		String[] params = { "" + log.logId };
		return update(editedValues, LOG_ID + " = ?", params);
	}



	/**
	 * delete log truoc 15 ngay
	 * @author banghn
	 * @return
	 */
	public long deleteOldLog(){
		long success  = -1;
		try{
			//mDB.beginTransaction();
			//String sqlDelete = "Delete from LOG_TABLE where date(CREATE_DATE) < date('now','-15 day','localtime')";
			//xoa du lieu truoc do toi da 2 thang hoac truoc 15 ngay nhung state = 2 
			//(giu lai record nao chua thanh cong de dieu tra trong 2 thang)
			String startOfPreviousMonth = DateUtils.getFirstDateOfNumberPreviousMonthWithFormat(
					DateUtils.DATE_FORMAT_SQL_DEFAULT, -1);
			String dayBefore = DateUtils.getDateOfNumberPreviousDateWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT, -15);
			StringBuffer  sqlDelete = new StringBuffer();
			sqlDelete.append("DELETE FROM log_table ");
			sqlDelete.append("WHERE  1 = 1 ");
			sqlDelete.append("       AND ( substr(create_date,0,11) < ? ");
			sqlDelete.append("              OR ( substr(create_date,0,11) < ? ");
			sqlDelete.append("                   AND state = 2 ) ) ");
			String[] params = {startOfPreviousMonth,dayBefore};
			execSQL(sqlDelete.toString(),params);
			success = 1;
			//mDB.setTransactionSuccessful();
		}catch (Throwable e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			success = -1;
		}finally{
//			if (mDB != null && mDB.inTransaction()) {
//				mDB.endTransaction();
//			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return success;
	}
	
	/**
	*  Lay ds don hang co trong log
	*  @author: TruongHN
	*  @return: ArrayList<String>
	*  @throws:
	 */
	public ArrayList<LogDTO> getOrderInLog() {
		ArrayList<LogDTO> v = new ArrayList<LogDTO>();
		Cursor c = null;
		try {
			String []param = {DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW)};
			// co TH xoa don hang khoi ds, nhung trong log_table van con, nen phai loai ra
			c = query(TABLE_TYPE + " = "  + LogDTO.TYPE_ORDER + " AND STATE != " + LogDTO.STATE_ORDER_DELETED +
					" AND date("+ CREATE_DATE + ") >= date(?)",
					param, null, null, CREATE_DATE + " asc");
			if (c != null) {
				LogDTO LogDTO;
				if (c.moveToFirst()) {
					do {
						LogDTO = initLogDTOFromCursor(c);
						v.add(LogDTO);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally{
			try{
				if (c != null) {
					c.close();
				}
			}catch (Exception e) {
				VTLog.i("getOrderInLog", e.getMessage());
			}
		}
		
		return v;
	}
	
	/**
	 * Cap nhat trang thai dong bo don hang
	 * @author: TruongHN
	 * @param id
	 * @param dtoFollowProblemView
	 * @return: int
	 * @throws:
	 */
	public long updateState(String saleOrderId, int synState) {
		ContentValues value = new ContentValues();
		value.put(STATE, synState);
		String[] params = { "" + saleOrderId };
		return update(value, TABLE_ID + " = ? ", params);
	}
}
