/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.sync;

import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.sqllite.db.ABSTRACT_TABLE;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Syn Data Row DAO
 * SynDataRowDAO.java
 * @version: 1.0 
 * @since:  08:27:31 20 Jan 2014
 */
public class SynDataRowDAO {

	// private final static String CONDITION_SYN_STATE="& " +
	// ABSTRACT_TABLE.SYN_STATE + "=2";
	//private SQLiteDatabase db;
	private String tableName;
	private String[] arrColumnName;
	private String query;
	//private String[] arg;
	private String primaryKey;
	//thoi gian gui log khi dong bo loi
	public static long timeSendLogErrorSynData = 0;
	//co loi trong qua trinh cap nhat
	public boolean haveErrorSynData = false; 
	public SynDataRowDAO(SQLiteDatabase db, String tableName, String query)
			throws Exception {
		//this.db = db;
		this.tableName = tableName;
		this.query = query;
	}

	public SynDataRowDAO(SQLiteDatabase db, String tableName,
			String[] arrColumnName, String query, String[] arg, String key) {
		// TODO Auto-generated constructor stub
		//this.db = db;
		this.tableName = tableName;
		this.arrColumnName = arrColumnName;
		this.query = query;
		//this.arg = arg;
		this.primaryKey = key;
	}

	private boolean isExist(SQLiteDatabase db, String tableName, String query,
			String[] arg) {
		boolean isExist = false;
		Cursor c = null;
		try {
			//tat ca cac bang deu co truong: SYN_STATE
			query = "Select SYN_STATE from " + tableName + " where " + query;
			//query = "Select * from " + tableName + " where " + query;
			c = db.rawQuery(query, arg);
			
			if (c.getCount() <= 0) {
				isExist = false;
			} else {
				isExist = true;
			}
		} catch (Exception ex) {
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception ex) {
				}
			}
		}
		return isExist;
	}

	public void executeSyncRow(SQLiteDatabase db, List<String> arrValues)
			throws Exception {

		ContentValues value = initDataRow(arrValues);
		String[] idValue = { arrValues.get(arrValues.size() - 1) };
		int error = 0;
		long result = -1;
		try {
			if (getValuePrimaryKey(arrValues).equals("null")) {
				error = 1;
				result = this.deleteRow(db, tableName, query, idValue);
			} else if (this.isExist(db, tableName, query, idValue)) {
				error = 2;
				result = this.updateRow(db, tableName, value, query, idValue);
			} else {
				error = 3;
				result = db.insert(tableName, null, value);
			}
			//chi log loi len truong hop update || insert
			if((error == 2 ||error == 3) && result <=0){
				haveErrorSynData = true;
				if((System.currentTimeMillis() - timeSendLogErrorSynData) >= 10000){
					timeSendLogErrorSynData = System.currentTimeMillis();
					ServerLogger.sendLog("SYNDATALOGIC",
							"Loi hanh dong :  " + error +
							"  -  columns  :  " + arrColumnName +
							"  -  Data: " + arrValues +
							"  -  TableName :  " + tableName +
							"  -  Key Value :  " + arrValues.get(arrValues.size() - 1) +
							"  -  Key :  " + primaryKey , false, TabletActionLogDTO.LOG_CLIENT);
				}
//				VTLog.logToFile(
//						"SYNDATALOGIC",
//						"Loi trang thai " + error + " columns "
//								+ arrColumnName.toString() + "; dong du lieu "
//								+ arrValues.toString());
//				VTLog.logToFile("SYNDATALOGIC", "Loi row du lieu: TableName "
//						+ tableName + " Key " + primaryKey + " Key Value "
//						+ arrValues.get(arrValues.size() - 1));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.logToFile(
					"SYN DATA",
					"Loi trang thai " + error + " columns "
							+ arrColumnName + "; dong du lieu "
							+ arrValues);
			VTLog.logToFile("SYN DATA", "Loi row du lieu: TableName "
					+ tableName + " Key " + primaryKey + " Key Value "
					+ arrValues.get(arrValues.size() - 1));
			if((System.currentTimeMillis() - timeSendLogErrorSynData) >= 15000){
				timeSendLogErrorSynData = System.currentTimeMillis();
				ServerLogger.sendLog("SYNDATA",
						"Exception :  " + e.getMessage() +
						"  -  Loi hanh dong :  " + error +
						"  -  columns  :  " + arrColumnName +
						"  -  Data: " + arrValues +
						"  -  TableName :  " + tableName +
						"  -  Key Value :  " + arrValues.get(arrValues.size() - 1) +
						"  -  Key :  " + primaryKey, false, TabletActionLogDTO.LOG_CLIENT);
			}
			throw e;
		}
	}

	private String getValuePrimaryKey(List<String> arrValues) {
		for (int i = 0; i < arrColumnName.length; i++) {
			if (primaryKey != null && primaryKey.equals(arrColumnName[i])) {
				return arrValues.get(i);
			}
		}
		return "";
	}



	private long updateRow(SQLiteDatabase db, String tableName,
			ContentValues value, String query, String[] arg) {
		return db.update(tableName, value, query, arg);
	}

	private long deleteRow(SQLiteDatabase db, String tableName, String query,
			String[] arg) {
		return db.delete(tableName, query, arg);
	}

	private ContentValues initDataRow(List<String> dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(primaryKey, dto.get(dto.size() - 1));
		try {
			for (int i = 0; i < arrColumnName.length; i++) {

				if (!arrColumnName[i].equals(primaryKey)) {
					if (!dto.get(i).equals("null"))
						editedValues.put(arrColumnName[i], dto.get(i));
					else {
						String s = null;
						editedValues.put(arrColumnName[i], s);
					}
				}

			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		// Problem : con van de mot so bang ko co truong du lieu nay
		editedValues.put(ABSTRACT_TABLE.SYN_STATE,
				ABSTRACT_TABLE.SYNCHRONIZED_STATUS);
		return editedValues;
	}

}
