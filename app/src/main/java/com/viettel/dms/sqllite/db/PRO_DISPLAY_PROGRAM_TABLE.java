/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDetailDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
 
/**
 * 
 * PRO_DISPLAY_PROGRAM_TABLE.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:46:55 12-05-2014
 */
public class PRO_DISPLAY_PROGRAM_TABLE extends ABSTRACT_TABLE {

	// id cua bang
	public static final String PRO_DISPLAY_PROGRAM_ID = "PRO_DISPLAY_PROGRAM_ID";
	// id  chuong trinh HTBH
	public static final String PRO_INFO_ID = "PRO_INFO_ID";
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// 0 - khong dat, 1 - dat
	public static final String IMAGE_DISPLAY = "IMAGE_DISPLAY";
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
	
	public static final String PRO_DISPLAY_PROGRAM_TABLE = "PRO_DISPLAY_PROGRAM";
	 
	public PRO_DISPLAY_PROGRAM_TABLE(SQLiteDatabase mDB){
		this.tableName = PRO_DISPLAY_PROGRAM_TABLE;

		this.columns = new String[] {PRO_DISPLAY_PROGRAM_ID, PRO_INFO_ID, CUSTOMER_ID, IMAGE_DISPLAY, CREATE_DATE, CREATE_USER, UPDATE_DATE,
				UPDATE_USER, STAFF_ID};
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

	/**
	 * them 1 dong xuong CSDL
	 * @author: hoanpd1
	 * @since: 13:45:07 09-09-2014
	 * @return: long
	 * @throws:  
	 * @param dto
	 * @return
	 */
	public long insert(ProDisplayProgrameDTO dto) {
		ContentValues value = initDataRow(dto);
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
	 * Insert thong tin chuong trinh duoc cham
	 * @author: quangvt1
	 * @since: 16:19:09 12-05-2014
	 * @return: long
	 * @throws:  
	 * @param programe
	 * @return
	 */
	public boolean insertVotePrograme(ProDisplayProgrameDTO programe) {
		boolean success = false;
		mDB.beginTransaction();
		try{
			success = (insert(programe) > -1);
			
			if(success == false){
				mDB.setTransactionSuccessful();
			}else{
				PRO_DISPLAY_PROGRAM_DETAIL_TABLE detailTable = new PRO_DISPLAY_PROGRAM_DETAIL_TABLE(mDB);
				for (ProDisplayProgrameDetailDTO detail : programe.listDetail) {
					success = (detailTable.insert(detail) > -1);
					if(success == false){
						break;
					}
				}
				
				mDB.setTransactionSuccessful();
			}
		}catch(Exception ex){
			success = false;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
 		}finally{
 			if (mDB != null && mDB.inTransaction()) {
				mDB.endTransaction();
			}
			SQLUtils.getInstance().isProcessingTrans = false;
		}
		
		return success;
	}   
	
	/**
	 * @author: hoanpd1
	 * @since: 13:45:17 09-09-2014
	 * @return: ContentValues
	 * @throws:  
	 * @param dto
	 * @return
	 */
	private ContentValues initDataRow(ProDisplayProgrameDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PRO_DISPLAY_PROGRAM_ID, dto.displayProgrameId + "");
		editedValues.put(PRO_INFO_ID, dto.proInfoId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(IMAGE_DISPLAY, dto.imageDisplay + "");
		editedValues.put(CREATE_DATE, DateUtils.now());
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(CREATE_USER, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		
		return editedValues;
	}

}
