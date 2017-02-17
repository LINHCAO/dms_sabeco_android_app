/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDetailDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
 
/**
 * 
 * PRO_DISPLAY_PROGRAME_DETAIL_TABLE.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:47:18 12-05-2014
 */
public class PRO_DISPLAY_PROGRAM_DETAIL_TABLE extends ABSTRACT_TABLE {

	// id cua bang
	public static final String PRO_DISPLAY_PROGRAM_DETAIL_ID = "PRO_DISPLAY_PROGRAM_DETAIL_ID";
	// id cua chuong trinh trung bay
	public static final String PRO_DISPLAY_PROGRAM_ID = "PRO_DISPLAY_PROGRAM_ID";
	// ma san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// so luong cham
	public static final String QUANTITY = "QUANTITY";
	/// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ma nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	
	public static final String PRO_DISPLAY_PROGRAM_DETAIL_TABLE = "PRO_DISPLAY_PROGRAM_DETAIL"; 
	
	public PRO_DISPLAY_PROGRAM_DETAIL_TABLE(SQLiteDatabase mDB){
		this.tableName = PRO_DISPLAY_PROGRAM_DETAIL_TABLE;

		this.columns = new String[] {PRO_DISPLAY_PROGRAM_DETAIL_ID, PRO_DISPLAY_PROGRAM_ID, PRODUCT_ID, QUANTITY, CREATE_DATE, CREATE_USER, UPDATE_DATE,
				UPDATE_USER, STAFF_ID, SYN_STATE};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		sqlInsert+=tableName+" ( ";
		this.mDB = mDB;
	}
	
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	protected long insert(ProDisplayProgrameDetailDTO detail) {
		ContentValues value = initDataRow(detail);
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
	 * @author: hoanpd1
	 * @since: 13:45:17 09-09-2014
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDataRow(ProDisplayProgrameDetailDTO detail) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PRO_DISPLAY_PROGRAM_DETAIL_ID, detail.displayProgrameDetailId + "");
		editedValues.put(PRO_DISPLAY_PROGRAM_ID, detail.displayProgrameId + "");
		editedValues.put(PRODUCT_ID, detail.productId);
		editedValues.put(QUANTITY, detail.quantity + "");
		editedValues.put(CREATE_DATE, DateUtils.now());
		editedValues.put(STAFF_ID,detail.staffId);
		editedValues.put(CREATE_USER, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		
		return editedValues;
	}
}
