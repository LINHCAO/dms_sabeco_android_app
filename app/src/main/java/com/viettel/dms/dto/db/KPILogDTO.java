/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.KPI_LOG_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Doi tuong luu tru du lieu log kpi
 * 
 * @author banghn
 * @version 1.0
 */
public class KPILogDTO extends AbstractTableDTO {
	private static final long serialVersionUID = 1L;
	private long KPILogId;
	private int mobileData;
	private long shopId;
	private long staffId;
	private String serviceCode;
	private String serviceBeginTime;
	private String serviceEndTime;
	private double clientLng;
	private double clientLat;
	private String createDate;
	private long clientTotalTime;
	private String IMEI;
	private String serialSim;
	private String note;

	public String getIMEI() {
		return IMEI;
	}

	public void setIMEI(String iMEI) {
		IMEI = iMEI;
	}

	public String getSerialSim() {
		return serialSim;
	}

	public void setSerialSim(String serialSim) {
		this.serialSim = serialSim;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public KPILogDTO() {
		super(TableType.KPI_LOG_TABLE);
	}

	public long getKPILogId() {
		return KPILogId;
	}

	public void setKPILogId(long kPILogId) {
		KPILogId = kPILogId;
	}

	public int getMobileData() {
		return mobileData;
	}

	public void setMobileData(int mobileData) {
		this.mobileData = mobileData;
	}

	public long getShopId() {
		return shopId;
	}

	public void setShopId(long shopId) {
		this.shopId = shopId;
	}

	public long getStaffId() {
		return staffId;
	}

	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public String getServiceCode() {
		return serviceCode;
	}

	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}

	public String getServiceBeginTime() {
		return serviceBeginTime;
	}

	public void setServiceBeginTime(String serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	public String getServiceEndTime() {
		return serviceEndTime;
	}

	public void setServiceEndTime(String serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	public double getClientLng() {
		return clientLng;
	}

	public void setClientLng(double clientLng) {
		this.clientLng = clientLng;
	}

	public double getClientLat() {
		return clientLat;
	}

	public void setClientLat(double clientLat) {
		this.clientLat = clientLat;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public JSONObject generateCreateKpiLogSql() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			jsonObject.put(IntentConstants.INTENT_TABLE_NAME, KPI_LOG_TABLE.KPI_LOG_TABLE);

			JSONArray array = new JSONArray();
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.KPI_LOG_ID, "KPI_LOG_SEQ", DATA_TYPE.SEQUENCE.toString()));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.MOBILE_DATA, this.mobileData, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.SHOP_ID, this.shopId, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.STAFF_ID, this.staffId, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.SERVICE_CODE, this.serviceCode, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.SERVICE_BEGIN_TIME, this.serviceBeginTime, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.SERVICE_END_TIME, this.serviceEndTime, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.CLIENT_LAT, this.clientLat, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.CLIENT_LNG, this.clientLng, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.CREATE_DATE, this.createDate, null));
			array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.CLIENT_TOTAL_TIME, this.clientTotalTime, null));
			if (!StringUtil.isNullOrEmpty(IMEI)) {
				array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.IMEI, this.IMEI, null));
			}
			if (!StringUtil.isNullOrEmpty(serialSim)) {
				array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.SERIAL, this.serialSim, null));
			}
			if (!StringUtil.isNullOrEmpty(note)) {
				array.put(GlobalUtil.getJsonColumn(KPI_LOG_TABLE.NOTE, this.note, null));
			}

			jsonObject.put(IntentConstants.INTENT_LIST_PARAM, array);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObject;
	}

	public long getTotalExecutationTime() {
		return clientTotalTime;
	}

	public void setTotalExecutationTime(long totalExecutationTime) {
		this.clientTotalTime = totalExecutationTime;
	}
}
