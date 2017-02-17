/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.sync;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.syndata.SynDataTableDTO;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.ServerLogger;
import com.viettel.utils.VTLog;

/**
 * Syn Data Table DAO
 * SynDataTableDAO.java
 * @version: 1.0 
 * @since:  08:27:23 20 Jan 2014
 */
public class SynDataTableDAO {
	//thoi gian gui log khi dong bo loi
	public static long timeSendLogErrorSynData = 0;
	
	List<SynDataTableDTO> listTables;
	SQLiteDatabase db;
	//so table, row du lieu update de kiem tra log len server.
	public int numTableUpdate = 0;
	public long numRowUpdate = 0;
	//trang thai db de gui log
	String stateDBConnection = "";
	public SynDataTableDAO(List<SynDataTableDTO> listTables) {
		this.listTables = listTables;
	}

	private boolean openDBForWrite() throws Exception {
		boolean result = false;

		try {
			db = SQLUtils.getInstance().getmDB();
			if (!db.isOpen()) {
				stateDBConnection = "db.isOpen: " + db.isOpen();
				return result;
			}

			if (db.isReadOnly()) {
				stateDBConnection = "db.isReadOnly: " + db.isReadOnly();
				return result;
			}
			//wait 3 second, avoid lock db, after exit after
			int waitCount = 4;
			while (true) {
				boolean lockDB = db.isDbLockedByCurrentThread() || db.isDbLockedByOtherThreads();
				if (!lockDB) {
					break;
				}
				
				if (waitCount <= 0) {
					stateDBConnection = "db.isDbLockedByCurrentThread: " + db.isDbLockedByCurrentThread()
							+ "db.isDbLockedByOtherThreads: " + db.isDbLockedByOtherThreads();
					return result;
				}
				
				try {
					VTLog.d("openDBForWrite", "wait " + waitCount);
					Thread.sleep(1000);
				} catch (Exception e) {
				}
				waitCount --;
			}
			result = true;
		} catch (Exception ex) {
			stateDBConnection = "" + ex.getMessage() ;
			throw ex;
		}


		return result;
	}

	private boolean openDBForRead() throws Exception {
		boolean result = false;

		try {
			if (db == null) {
				db = SQLUtils.getInstance().getmDB();;//db = DBHelper.getInstance().getReadableDatabase();
			}

			if (!db.isOpen()) {
				return result;
			}

			result = true;
		} catch (Exception ex) {
			throw ex;
		}

		return result;
	}

	public int synData(long lastLogId, long newLogId) throws Exception {
		int rows = 0;
		boolean haveErrorSynData = false; 
		if (this.listTables == null || this.listTables.size() <= 0) {
			throw new Exception("No data need to syn.");
		} else {
			if (openDBForWrite()) {
				try {
					// Begin transaction
					this.db.beginTransaction();
					numTableUpdate = this.listTables.size();
					for (SynDataTableDTO item : this.listTables) {
						String tableName = item.getTableName();
						String query = item.getPkField() +" = ? ";

						String[] arrColumnName =item.getTableColumns();
						List<List<String>> listRowData = item.getData();

						SynDataRowDAO rowDTO = new SynDataRowDAO(this.db,
								tableName, arrColumnName, query, null , item.getPkField());
						// xoa nhung record co trang thai la 1 (trang thai
						// chuyen len server roi)
						//rowDTO.deleteTranseferedRows(tableName);
						// insert du lieu tu server ve database
						numRowUpdate += listRowData.size();
						for (List<String> row : listRowData) {
							//String[] arrValues = getArgForQuery(row, ",");
							rowDTO.executeSyncRow(db, row);
							if(rowDTO.haveErrorSynData) 
								haveErrorSynData = rowDTO.haveErrorSynData;
						}
					}
					// Commit transaction
					this.db.setTransactionSuccessful();
					//gui log khi qua trinh dong bo xay ra loi
					if(haveErrorSynData){
						ServerLogger.sendLog("SYNDATA",
								"LastLogId :  " + lastLogId +
								"  -  ResponeLogId: " + newLogId +
								"  -  NumTable, NumRow update :  " + numTableUpdate + " - "  
								+  numRowUpdate, false, TabletActionLogDTO.LOG_CLIENT);
					}
				}catch (Exception ex) {
					//VTLog.logToFile("SYN", DateUtils.now() + "- Exception : " + ex.toString());
					if((System.currentTimeMillis() - timeSendLogErrorSynData) >= 30000){
						timeSendLogErrorSynData = System.currentTimeMillis();
						ServerLogger.sendLog("SYNDATA",
							ex.toString(), false, TabletActionLogDTO.LOG_EXCEPTION);
					}
					throw ex;
				} finally {
					// Rollback transaction
					this.db.endTransaction();
					// this.db.close();
				}
			} else {
				ServerLogger.sendLog("SYNDATA","StateConnection = " + stateDBConnection, false, TabletActionLogDTO.LOG_CLIENT);
				throw new Exception(
						"Can't open connection to SQLite. Please check it again.");
			}
		}
		return rows;
	}

	// lay gia tri cac arg trong doi tuong row.
	// strArg : chuoi arg name can lay, cac column ngan cach nhau boi dau ";"
	// row : row can lay ra chuoi gia tri.
	public String[] getArgForQuery(String strArg,String split) throws Exception {
		String[] result = null;

		try {
			if (strArg == null || "".equals(strArg)) {
				return result;
			} else {
				result = strArg.split(split);
				if (result == null || result.length <= 0) {
					throw new Exception(
							"Can't get list name of argument query.");
				}

			}
		} catch (Exception ex) {
			throw ex;
		} finally {
			// this.db.close();
		}

		return result;
	}


	public void readDataFromTable() throws Exception {
		if (this.openDBForRead()) {
			try {
				List<String> listTableName = this.getDBNames();
				// // S O S ADD THEM PRODUCT
				listTableName.add("PRODUCT");

				if (listTableName == null || listTableName.size() > 0) {
					VTLog.e("Total of table : ",
							Integer.toString(listTableName.size()));

					VTLog.e("List table name :", "");

					for (String tableName : listTableName) {

						VTLog.e("tableName : ", tableName);

						String selectQuery = "SELECT  * FROM " + tableName;

						Cursor cursor = this.db.rawQuery(selectQuery, null);

						if (cursor != null) {
							VTLog.e("Number row in table : ",
									Integer.toString(cursor.getCount()));
						}

						int countTemp = 0;
						if (cursor.moveToFirst()) {
							do {
								int colCount = cursor.getColumnCount();
								StringBuilder log = new StringBuilder();

								for (int i = 0; i < colCount; i++) {
									log.append(cursor.getColumnName(i) + " : "
											+ cursor.getString(i) + " ||| ");
								}

								VTLog.e("Rows " + ++countTemp, log.toString());

							} while (cursor.moveToNext());
						}

						cursor.close();
					}
				}

			} catch (Exception ex) {

			} finally {
				// this.db.close();
			}
		} else {
			throw new Exception(
					"Can't open connection to SQLite. Please check it again.");
		}
	}

	public List<String> getDBNames() throws Exception {
		List<String> result = new ArrayList<String>();

		if (this.openDBForRead()) {
			try {
				StringBuilder sb = new StringBuilder();
				sb.append("SELECT name FROM sqlite_master ");
				sb.append("WHERE type IN ('table','view') AND name NOT LIKE 'sqlite_%' AND name NOT LIKE 'android%'");
				sb.append("UNION ALL ");
				sb.append("SELECT name FROM sqlite_temp_master ");
				sb.append("WHERE type IN ('table','view') AND name NOT LIKE 'android%'");
				sb.append("ORDER BY 1");

				// sb.append("SELECT name FROM sqlite_master ");
				// sb.append("WHERE type IN ('table','view')");

				Cursor c = this.db.rawQuery(sb.toString(), null);
				c.moveToFirst();

				// result = new String[c.getCount()];
				while (c.moveToNext()) {
					result.add(c.getString(c.getColumnIndex("name")));
				}
				c.close();
			} catch (SQLiteException e) {
				VTLog.e("Exception : ", e.getMessage());
			}

			return result;
		} else {
			throw new Exception(
					"Can't open connection to SQLite. Please check it again.");
		}

	}

	public boolean insertTempProduct() throws Exception {
		try {
			ContentValues editedValues = new ContentValues();
			editedValues.put("PRODUCT_ID", 1);
			editedValues.put("PRODUCT_NAME", "thuattq");
			editedValues.put("STATUS", 1);
			editedValues.put("UOM1", "xxxx");
			editedValues.put("UOM2", "yy");

			StringBuilder sb = new StringBuilder();
			sb.append("SELECT * FROM PRODUCT where PRODUCT_ID = 1");
			Cursor c = this.db.rawQuery(sb.toString(), null);

			if (c.getCount() > 0) {
				if (db.update("PRODUCT", editedValues, "PRODUCT_ID = ?",
						new String[] { "1" }) > 0) {
					return true;
				} else {
					return false;
				}
			} else {
				if (db.insert("PRODUCT", null, editedValues) > 0) {
					return true;
				} else {
					return false;
				}
			}

		} catch (Exception ex) {
			throw ex;
		}
	}
}
