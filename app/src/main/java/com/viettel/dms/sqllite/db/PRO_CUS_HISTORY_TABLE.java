/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProCusHistoryDTO;
 
/**
 * 
 * PRO_DISPLAY_PROGRAM_TABLE.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:46:55 12-05-2014
 */
public class PRO_CUS_HISTORY_TABLE extends ABSTRACT_TABLE {

	// id cua bang
	public static final String PRO_CUS_HISTORY_ID = "PRO_CUS_HISTORY_ID";
	// ID PRO_CUS_MAP
	public static final String PRO_CUS_MAP_ID = "PRO_CUS_MAP_ID";
	// Tu ngay
	public static final String FROM_DATE = "FROM_DATE"; 
	// Den ngay
	public static final String TO_DATE = "TO_DATE"; 
	//Loai trang thai: 0: cho duyet, 1: tham gia, 2: tam ngung
	public static final String TYPE = "TYPE"; 
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
	
	public static final String PRO_CUS_HISTORY_TABLE = "PRO_CUS_HISTORY";
	 
	public PRO_CUS_HISTORY_TABLE(SQLiteDatabase mDB){
		this.tableName = PRO_CUS_HISTORY_TABLE;

		this.columns = new String[] {PRO_CUS_MAP_ID, PRO_CUS_MAP_ID, FROM_DATE, TO_DATE, TYPE, CREATE_DATE, CREATE_USER, UPDATE_DATE,
				UPDATE_USER, STAFF_ID};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		sqlInsert+=tableName+" ( ";
		this.mDB = mDB;
	}
	
	@Override
	protected long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((ProCusHistoryDTO) dto);
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
	 * Xoa cac lich su cua KH tham gia chuong trinh HTBH
	 * @author: quangvt1
	 * @since: 15:37:22 19-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param pro_cus_map_id
	 * @return
	 */
	public boolean deleteProCusHistory(String pro_cus_map_id) { 
		String where = PRO_CUS_MAP_ID + " = ?";
		String[] args = {pro_cus_map_id};
		return delete(where, args) > -1; 
	}

	/**
	 * 
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: long
	 * @throws:  
	 * @param staffId
	 * @param staffCode
	 * @param day
	 * @return
	 */
	public ProCusHistoryDTO saveCustomerAttendHistory(long proHistoryId, long proCusMapId, long staffId, String staffCode, String day, int type) {
		long success = -1;
		ProCusHistoryDTO dto = new ProCusHistoryDTO();
		dto.proCusHistoryId = proHistoryId;
		dto.proCusMapId = proCusMapId;
		dto.type = type;
		dto.fromDate = day;
		dto.createDate = day;
		dto.createUser = staffCode;
		dto.staffId = staffId;
		success = insert(dto);
		if(success != -1){
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
	private ContentValues initDataRow(ProCusHistoryDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PRO_CUS_HISTORY_ID, dto.proCusMapId);
		editedValues.put(PRO_CUS_MAP_ID, dto.proCusMapId);
		editedValues.put(TYPE, dto.type);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(STAFF_ID, dto.staffId);
		return editedValues;
	}
}
