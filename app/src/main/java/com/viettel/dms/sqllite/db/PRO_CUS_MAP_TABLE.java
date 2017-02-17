/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProCusMapDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * 
 * PRO_DISPLAY_PROGRAM_TABLE.java
 * 
 * @author: quangvt1
 * @version: 1.0
 * @since: 15:46:55 12-05-2014
 */
public class PRO_CUS_MAP_TABLE extends ABSTRACT_TABLE {

	// id cua bang
	public static final String PRO_CUS_MAP_ID = "PRO_CUS_MAP_ID";
	// id chuong trinh HTBH
	public static final String PRO_INFO_ID = "PRO_INFO_ID";
	// doi thuong tham gia
	public static final String OBJECT_ID = "OBJECT_ID";
	// Loai doi tuong: 1: customer, 2: npp
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// so muc/ so xuat
	public static final String LEVEL_NUMBER = "LEVEL_NUMBER";
	// Ngay tham gia
	public static final String JOINING_DATE = "JOINING_DATE";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ma nhan vien
	public static final String STAFF_ID = "STAFF_ID";

	public static final String PRO_CUS_MAP_TABLE = "PRO_CUS_MAP";

	public PRO_CUS_MAP_TABLE(SQLiteDatabase mDB) {
		this.tableName = PRO_CUS_MAP_TABLE;

		this.columns = new String[] { PRO_CUS_MAP_ID, PRO_INFO_ID, OBJECT_ID,
				OBJECT_TYPE, LEVEL_NUMBER, JOINING_DATE, CREATE_DATE,
				CREATE_USER, UPDATE_DATE, UPDATE_USER, STAFF_ID };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		sqlInsert += tableName + " ( ";
		this.mDB = mDB;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((ProCusMapDTO) dto);
		return insert(null, value);
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
	 * Xoa thonh tin khach hang tham gia chuong trinh
	 * @author: quangvt1
	 * @since: 16:44:18 21-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param pro_cus_map_id
	 * @return
	 */
	public boolean deleteCustomerJoinHTBH(String pro_cus_map_id) {
		boolean success = false;
		PRO_CUS_MAP_DETAIL_TABLE tableDetail = new PRO_CUS_MAP_DETAIL_TABLE(mDB);
		PRO_CUS_HISTORY_TABLE tableHistory = new PRO_CUS_HISTORY_TABLE(mDB);
		mDB.beginTransaction();
		try {
			// Xoa cac chi tiet
			success = tableDetail.deleteDetailProCusMap(pro_cus_map_id);

			if (success) {
				// Xoa lich su
				success = tableHistory.deleteProCusHistory(pro_cus_map_id);
				if (!success) {
					return success;
				}
			}

			if (success) {
				// Xoa 1 dong pro_cus_map
				success = deleteProCusMap(pro_cus_map_id);
				if (success) {
					mDB.setTransactionSuccessful();
					return success;
				}
			}
		} catch (Exception ex) {
			success = false;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} finally {
			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		return success;
	}

	/**
	 * Xoa 1 dong pro_cus_map dua vao id
	 * 
	 * @author: quangvt1
	 * @since: 15:44:14 19-05-2014
	 * @return: boolean
	 * @throws:
	 * @param pro_cus_map_id
	 * @return
	 */
	private boolean deleteProCusMap(String pro_cus_map_id) {
		String where = PRO_CUS_MAP_ID + " = ? ";
		String[] args = { pro_cus_map_id };
		return delete(where, args) > -1;
	}

	/**
	 * luu khach hang tham gia HTBH
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: long
	 * @throws:  
	 * @param programID
	 * @param staffId
	 * @param staffCode
	 * @param day
	 * @param customerId
	 * @return
	 */
	public ProCusMapDTO saveCustomerAttend(long proCusMapId, long programID, long staffId, String staffCode, String day, long customerId, String level) {
		long success = -1;
		ProCusMapDTO dto = new ProCusMapDTO();
		dto.proCusMapId = proCusMapId;
		dto.proInfoId = programID;
		dto.objectId = customerId;
		dto.objectType = 1;
		dto.JoiningDate = day;
		dto.createDate = day;
		dto.createUser = staffCode;
		dto.staffId = staffId;
		dto.levelNumber = Integer.parseInt(level);
		success = insert(dto);
		if(success != -1) {
			return dto;
		} 
		return null;
	}
	/**
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDataRow(ProCusMapDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PRO_CUS_MAP_ID, dto.proCusMapId);
		editedValues.put(PRO_INFO_ID, dto.proInfoId);
		editedValues.put(OBJECT_ID, dto.objectId);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(JOINING_DATE, dto.JoiningDate);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(STAFF_ID, dto.staffId);
		
		if(dto.levelNumber > 0){
			editedValues.put(LEVEL_NUMBER, dto.levelNumber); 
		}
		return editedValues;
	}

	public boolean updateLevelOfCustomerJoinHTBH(String pro_cus_map_id,
			int newLevel, String staffCode) {
		// Du lieu can update
		ContentValues values = new ContentValues();
		values.put(LEVEL_NUMBER, newLevel); 
		values.put(UPDATE_USER, staffCode); 
		values.put(UPDATE_DATE, DateUtils.now()); 

		// where
		String where = PRO_CUS_MAP_ID + " = ?";
		String[] args = {pro_cus_map_id };
		return update(values, where, args) > 0; 
	}
}
