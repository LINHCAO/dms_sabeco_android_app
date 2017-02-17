/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteStatement;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * ABSTRACT TABLE
 * ABSTRACT_TABLE.java
 * @version: 1.0 
 * @since:  08:23:33 20 Jan 2014
 */
public abstract class ABSTRACT_TABLE {
	public static final int CREATED_STATUS = 0; // trang thai khi row duoc tao ra
	public static final int TRANSFERED_STATUS = 1; // trang thai khi row duoc chuyen thanh cong len server
	public static final int SYNCHRONIZED_STATUS = 2; // trang thai khi dong bo thanh cong
	
	// colum phuc vu syn
	public static final String SYN_STATE = "SYN_STATE";
	
	protected String[] columns;
	public String sqlGetCountQuerry="SELECT COUNT(*) FROM ";
	public String sqlDelete = "DELETE_FROM ";
	protected String sqlInsert="Insert into ";
	protected StringBuffer sbInsert;
	protected ArrayList<String> arrParams;
	protected StringBuffer sbParams;
	public String tableName="";
	public SQLiteDatabase mDB;
	
	abstract protected long insert(AbstractTableDTO dto) ;
	abstract protected long update(AbstractTableDTO dto) ;
	abstract protected long delete(AbstractTableDTO dto) ;
	
	
	/**
	*  Lay so luong record cua table
	*  @author: TruongHN
	*  @return: long
	*  @throws:
	 */
	public long getCount() {
		SQLiteStatement statement = compileStatement(
				sqlGetCountQuerry);
		long count = statement.simpleQueryForLong();
		return count;
	}
	
//	/**
//	*   Get MaxId trong table khi truyen vao tableName & ten column
//	*  @author: TruongHN
//	*  @param tableName
//	*  @param columnIdName
//	*  @throws Exception
//	*  @return: int
//	*  @throws:
//	 */
//	public int getMaxIdInTable(String columnIdName) throws Exception {
//		int maxId = -1;
//		Cursor cursor = null;
//		try {
//			StringBuffer sqlState = new StringBuffer();
//			sqlState.append("select ifnull(max(" + columnIdName + "), 0) as " + columnIdName + " from " + tableName);
//			cursor = rawQuery(sqlState.toString(), null);
//
//			if (cursor != null) {
//				if (cursor.moveToFirst()) {
//					maxId = cursor.getInt(cursor
//							.getColumnIndex(columnIdName));
//				}
//			}
//		} catch (Exception ex) {
//			throw ex;
//		} finally {
//			if (cursor != null) {
//				cursor.close();
//			}
//		}
//		return maxId;
//	}
	
//	public static boolean isSyncTable(String tableName){
//		for (int i = 0; i < ARRAY_SYNC_TABLES.length; i++) {
//			if (ARRAY_SYNC_TABLES[i].equals(tableName))
//				return true;
//		}
//		return false;
//	}
	
	public static void addNewColumn(String[] columns,String columnName){
		String[] newColumn = new String[columns.length+1];	
		for (int index = 0; index < newColumn.length+1; index++){
			if (index == columns.length){
				newColumn[index] = columnName;
			}else{
				newColumn[index] = columns[index];
			}
		}
		columns = newColumn;
	}
	
	public int update(ContentValues values, String whereClause, String[] whereArgs){
		int upda = mDB.update(tableName, values, whereClause, whereArgs);
		VTLog.v("value = ", values.toString());
		VTLog.i("Truong, update - ", String.valueOf(upda));
		return upda;
	}
	
	public Cursor rawQuery(String sqlQuery,String[] params){
		return mDB.rawQuery(sqlQuery, params);
	}
	
	public Cursor rawQueries(String sqlQuery, ArrayList<String> params){
		String[] strParams =  null;
		if(params != null){
			strParams = new String[params.size()];
//			for(int i=0,s=params.size();i<s;i++){
//				strParams[i] = params.get(i);
//			}
			strParams=params.toArray(strParams);
		}
		return mDB.rawQuery(sqlQuery, strParams);
	}
	
	public void execSQL(String sql){
		mDB.execSQL(sql);
	}
	
	
	public void execSQL(String sql, Object[] bindArgs){
		mDB.execSQL(sql, bindArgs);
	}
	
	
	public SQLiteStatement compileStatement(String sqlQuery){
		return mDB.compileStatement(sqlQuery);
	}

	/**
	* insert dữ liệu bằng việc sử dụng sqliteStatement
	* @author: dungdq3
	* @return: long
	*/
	protected long insertBySQLiteStatement() {
		// TODO Auto-generated method stub
		SQLiteStatement state= compileStatement(sqlInsert);
		String[] params= new String[arrParams.size()];
		params=arrParams.toArray(params);
//		state.bindAllArgsAsStrings(params);
		this.bindAllArgsAsStrings(state, params);
		return state.executeInsert();
	}
	
	private void bindAllArgsAsStrings(SQLiteStatement state, String[] params) {
		for (int i = 0; i < params.length; i++) {
			state.bindString(i, params[i]);
		}
	}
	
	public long insert(String nullColumnHack, ContentValues values){
		long insert=0;
		try{
			insert=mDB.insert(tableName, nullColumnHack, values);
			VTLog.i("Truong, insert - ", String.valueOf(insert));
		}catch(Exception ex){
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return insert;
	}

	public long insertOrUpdate(String nullColumnHack, ContentValues values){
		long insert=0;
		try{
			insert=mDB.replace(tableName, nullColumnHack, values);
			VTLog.i("Truong, insert - ", String.valueOf(insert));
		}catch(Exception ex){
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return insert;
	}
	
	public long insert(String nameTable, String nullColumnHack, ContentValues values){
		long insert = mDB.insert(nameTable, nullColumnHack, values);
		VTLog.i("Truong, insert - ", String.valueOf(insert));
		return insert;
	}
	
	public int delete(String whereClause, String[] whereArgs){
		int delete = mDB.delete(tableName, whereClause, whereArgs);
		VTLog.i("Truong, delete - ", String.valueOf(delete));
		return delete;
	}
	
	public Cursor query(String selection, String[] selectionArgs, String groupBy, String having, String orderBy){
		return mDB.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy);
	}
	
	public void clearTable() {
		mDB.execSQL(this.sqlDelete);
	}
	
}
