/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.CustomerPositionLogDTO;
import com.viettel.dms.dto.view.CustomerUpdateLocationDTO;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;

/**
 * Luu thong tin khach hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class CUSTOMER_POSITION_LOG_TABLE extends ABSTRACT_TABLE {
	// id row
	public static final String CUSTOMER_POSITION_LOG_ID = "CUSTOMER_POSITION_LOG_ID";
	// ma nhan vien ban hang
	public static final String STAFF_ID = "STAFF_ID";
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// id NPP
	public static final String LAT = "LAT";
	// truong nay chua dung
	public static final String LNG = "LNG";
	// ma vung
	public static final String STATUS = "STATUS";
	// DELETED_DATE
	public static final String DELETED_DATE = "DELETED_DATE";
	// DELETED_USER
	public static final String DELETED_USER = "DELETED_USER";
	// so nha
	public static final String CREATE_DATE = "CREATE_DATE";
	// dien thoai
	public static final String SYN_STATE = "SYN_STATE";

	public static final String CUSTOMER_POSITION_LOG_TABLE = "CUSTOMER_POSITION_LOG";

	public CUSTOMER_POSITION_LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = CUSTOMER_POSITION_LOG_TABLE;
		this.columns = new String[] { CUSTOMER_POSITION_LOG_ID, STAFF_ID,
				CUSTOMER_ID, LAT, LNG, STATUS, CREATE_DATE,DELETED_DATE, DELETED_USER, SYN_STATE};
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
		ContentValues value = initDataRow((CustomerPositionLogDTO) dto);
		return insert(null, value);
	}

	/**
	 * Cap nhat customer
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: int
	 * @throws:
	 */
	public int update(CustomerPositionLogDTO dto) {
		ContentValues value = initDataRow(dto);
		String[] params = { "" + dto.getCustomerId() };
		return update(value, CUSTOMER_POSITION_LOG_ID + " = ?", params);
	}

	public long update(AbstractTableDTO dto) {
		CustomerPositionLogDTO cusDTO = (CustomerPositionLogDTO) dto;
		ContentValues value = initDataRow(cusDTO);
		String[] params = { "" + cusDTO.getCustomerId() };
		return update(value, CUSTOMER_POSITION_LOG_ID + " = ?", params);
	}

	/**
	 * update vi tri cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public long updateLocation(CustomerPositionLogDTO dto) {
		ContentValues value = initCustomerLocationData(dto);
		String[] params = { "" + dto.getId() };
		return update(value, CUSTOMER_POSITION_LOG_ID + " = ?", params);
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
		return delete(CUSTOMER_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		CustomerDTO cusDTO = (CustomerDTO) dto;
		String[] params = { cusDTO.getCustomerId() };
		return delete(CUSTOMER_ID + " = ?", params);
	}



	public ContentValues initCustomerLocationData(CustomerPositionLogDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(LAT, dto.getLat());
		editedValues.put(LNG, dto.getLng());
		return editedValues;
	}

	public ContentValues initDataRow(CustomerPositionLogDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(CUSTOMER_POSITION_LOG_ID, dto.getId());
		editedValues.put(STAFF_ID, dto.getStaffId());
		editedValues.put(CUSTOMER_ID, dto.getCustomerId());
		editedValues.put(LAT, dto.getLat());
		editedValues.put(LNG, dto.getLng());
		editedValues.put(STATUS, dto.getStatus());
		editedValues.put(CREATE_DATE, dto.getCreateDate());
		return editedValues;
	}

	
	/**
	 * Lay danh sach cap nhat vi tri khach hang
	 * @author banghn
	 * @param customerId
	 * @return
	 */
	public ArrayList<CustomerUpdateLocationDTO> getCustomerHistoryUpdateLocation(
			String customerId) {
		Cursor c = null;
		ArrayList<CustomerUpdateLocationDTO> listHistory = new ArrayList<CustomerUpdateLocationDTO>();
		ArrayList<String> param = new ArrayList<String>();
		StringBuffer  sqlString = new StringBuffer();
		sqlString.append("SELECT cpl.CUSTOMER_POSITION_LOG_ID , cpl.[customer_id], ");
		sqlString.append("       cpl.[lat], ");
		sqlString.append("       cpl.lng, ");
		sqlString.append("       cpl.[status], ");
		sqlString.append("       Strftime('%d/%m/%Y', cpl.[create_date]) AS CREATE_DATE, ");
		sqlString.append("       st.[staff_id], ");
		sqlString.append("       st.[staff_code], ");
		sqlString.append("       st.[staff_name] ");
		sqlString.append("FROM   [customer_position_log] cpl ");
		sqlString.append("       LEFT JOIN [staff] st ");
		sqlString.append("              ON st.[staff_id] = cpl.staff_id ");
		sqlString.append("WHERE  cpl.[customer_id] = ? ");
		sqlString.append("	AND  cpl.[LAT] >0 ");
		sqlString.append("	AND  cpl.[LNG] >0 ");
		sqlString.append("order by CPL.CREATE_DATE desc");
		
		param.add(customerId);
		
		try {
			c = rawQueries(sqlString.toString(), param);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						CustomerUpdateLocationDTO item = new CustomerUpdateLocationDTO();
						item.initFromCursor(c);
						listHistory.add(item);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			if (c != null) {
				try {
					c.close();
				} catch (Exception ex) {
				}
			}
		}
		return listHistory;
	}

}
