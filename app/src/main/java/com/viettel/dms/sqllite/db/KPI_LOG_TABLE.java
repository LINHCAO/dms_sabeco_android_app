/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.KPILogDTO;

/**
 * 
 * @author trungnt56
 * @version:
 * @since: 09-01-2014
 */
public class KPI_LOG_TABLE extends ABSTRACT_TABLE {
	public static final String KPI_LOG_ID = "KPI_LOG_ID";
	public static final String MOBILE_DATA = "MOBILE_DATA";
	public static final String SHOP_ID = "SHOP_ID";
	public static final String STAFF_ID="STAFF_ID";
	public static final String SERVICE_CODE = "SERVICE_CODE";
	public static final String SERVICE_BEGIN_TIME = "SERVICE_BEGIN_TIME";
	public static final String SERVICE_END_TIME = "SERVICE_END_TIME";
	public static final String CLIENT_TOTAL_TIME = "CLIENT_TOTAL_TIME";
	public static final String CLIENT_LNG = "CLIENT_LNG";
	public static final String CLIENT_LAT = "CLIENT_LAT";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String IMEI = "IMEI";
	public static final String SERIAL = "SERIAL";
	public static final String NOTE = "NOTE";

	public static final String KPI_LOG_TABLE = "KPI_LOG";

	public KPI_LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = KPI_LOG_TABLE;

		this.columns = new String[] { KPI_LOG_ID, MOBILE_DATA, SHOP_ID,
				SERVICE_CODE, SERVICE_BEGIN_TIME, SERVICE_END_TIME,
				CLIENT_TOTAL_TIME, CLIENT_LNG, CLIENT_LAT, CREATE_DATE, IMEI, SERIAL, NOTE };
		this.sqlGetCountQuerry+=this.tableName+";";
		this.sqlDelete+=this.tableName+";";
		this.mDB=mDB;
	}

	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		ContentValues contentValues=initDataRow((KPILogDTO) dto);
		return insert(null, contentValues);
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		KPILogDTO kpiLogDTO = (KPILogDTO) dto;
		ContentValues value = initDataRow(kpiLogDTO);
		String[] params = { "" + kpiLogDTO.getKPILogId() };
		return update(value, KPI_LOG_ID + " = ?", params);
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		KPILogDTO kpiLogDTO = (KPILogDTO) dto;
		String[] params = { String.valueOf(kpiLogDTO.getKPILogId()) };
		return delete(KPI_LOG_ID + " = ?", params);
	}

	/**
	 * tao content values cho doi tuong kpi log dto
	* 
	* @author: trungnt56
	* @param: @param kpiLogDTO
	* @param: @return
	* @return: ContentValues
	* @throws:
	 */
	public ContentValues initDataRow(KPILogDTO kpiLogDTO){
		ContentValues contentValues=new ContentValues();
		contentValues.put(KPI_LOG_ID, kpiLogDTO.getKPILogId());
		contentValues.put(MOBILE_DATA, kpiLogDTO.getMobileData());
		contentValues.put(SHOP_ID, kpiLogDTO.getShopId());
		contentValues.put(STAFF_ID, kpiLogDTO.getStaffId());
		contentValues.put(SERVICE_CODE, kpiLogDTO.getServiceCode());
		contentValues.put(SERVICE_BEGIN_TIME, kpiLogDTO.getServiceBeginTime());
		contentValues.put(SERVICE_END_TIME, kpiLogDTO.getServiceEndTime());
		contentValues.put(CLIENT_LAT, kpiLogDTO.getClientLat());
		contentValues.put(CLIENT_LNG, kpiLogDTO.getClientLng());
		contentValues.put(CREATE_DATE, kpiLogDTO.getCreateDate());
		contentValues.put(IMEI, kpiLogDTO.getIMEI());
		contentValues.put(SERIAL, kpiLogDTO.getSerialSim());
		contentValues.put(NOTE, kpiLogDTO.getNote());

		return contentValues;
	}
}
