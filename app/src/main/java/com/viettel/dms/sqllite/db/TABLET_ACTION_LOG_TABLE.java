/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;

/**
 * Luu thong tin log loi client
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class TABLET_ACTION_LOG_TABLE extends ABSTRACT_TABLE {
	// id 
	public static final String TABLET_ACTION_LOG_ID = "TABLET_ACTION_LOG_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// id npp
	public static final String SHOP_ID = "SHOP_ID";
	// so imei may client
	public static final String DEVICE_IMEI = "DEVICE_IMEI";
	// version cua app dang su dung
	public static final String APP_VERSION = "APP_VERSION";
	// noi dung log loi goi len server
	public static final String CONTENT = "CONTENT";
	// dien giai
	public static final String DESCRIPTION = "DESCRIPTION";
	// type: tam thoi chua dung
	public static final String TYPE = "TYPE";
	// thoi gian tao log
	public static final String CREATE_DATE = "CREATE_DATE";

	public static final String TABLE_NAME = "TABLET_ACTION_LOG";

	public TABLET_ACTION_LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { TABLET_ACTION_LOG_ID, STAFF_ID, SHOP_ID, DEVICE_IMEI,
				APP_VERSION, CONTENT, DESCRIPTION, TYPE, CREATE_DATE};
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
		ContentValues value = initDataRow((TabletActionLogDTO) dto);
		return insert(null, value);
	}


	public long update(AbstractTableDTO dto) {
		TabletActionLogDTO tActionLog = (TabletActionLogDTO) dto;
		ContentValues value = initDataRow(tActionLog);
		String[] params = { "" + tActionLog.id };
		return update(value, TABLET_ACTION_LOG_ID + " = ?", params);
	}


	/**
	 * Xoa 1 dong cua CSDL
	 * @author: BangHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(TABLET_ACTION_LOG_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		ActionLogDTO cusDTO = (ActionLogDTO) dto;
		String[] params = { "" + cusDTO.id };
		return delete(TABLET_ACTION_LOG_ID + " = ?", params);
	}


	private ContentValues initDataRow(TabletActionLogDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(TABLET_ACTION_LOG_ID, dto.id);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(DEVICE_IMEI, dto.deviceImei);
		editedValues.put(APP_VERSION, dto.appVersion);
		editedValues.put(CONTENT, dto.content);
		editedValues.put(DESCRIPTION, dto.description);
		editedValues.put(TYPE, dto.type);
		editedValues.put(CREATE_DATE, dto.createDate);
		return editedValues;
	}
	
	/**
	 * Xoa log vi tri cu hon 1 ngay
	 * @author: banghn
	 * @return
	 */
	public long deleteOldTabletActionLog(){
		long result = -1;
		try {
			mDB.beginTransaction();
			StringBuffer sqlDel = new StringBuffer();
			sqlDel.append("  substr(create_date,0,11) < date('now', '-1 day') ");

			delete(sqlDel.toString(), null);
			result = 1;
			mDB.setTransactionSuccessful();
		} catch (Throwable e) {
			e.printStackTrace();
			result = -1;
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return result;
	}

}
