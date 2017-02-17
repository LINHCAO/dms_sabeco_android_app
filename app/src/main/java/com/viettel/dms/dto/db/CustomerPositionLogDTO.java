/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_POSITION_LOG_TABLE;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;


/**
 *  Luu thong tin lich su cap nhat vi tri khach hang
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class CustomerPositionLogDTO extends AbstractTableDTO{
	// id row
	public long id;
	// staffId
	public long staffId;
	// ten khach hang
	public long customerId;
	// lat
	public double lat;
	//lng
	public double lng;
	//status
	public int status;
	//createdate
	public String createDate;
	//syndate
	public int synState;
	//deleteDate
	public String deleteDate;
	//deleteUser
	public String deleteUser;

	public CustomerPositionLogDTO() {
		super(TableType.CUSTOMER_POSITION_LOG);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStaffId() {
		return staffId;
	}
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}

	public int getSynState() {
		return synState;
	}

	public void setSynState(int synState) {
		this.synState = synState;
	}

	public String getCustomerId() {
		return Long.toString(customerId);
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	
	/**
	 * generate cau lenh update location trong customerinfo
	 * @author : BangHN
	 * since : 1.0
	 */
	public JSONObject generateInsertCustomerPositionLogSql() {
		JSONObject updateLocationJson = new JSONObject();
		try {
			updateLocationJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			updateLocationJson.put(IntentConstants.INTENT_TABLE_NAME,
					CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_POSITION_LOG_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_POSITION_LOG_ID,"CUSTOMER_POSITION_LOG_SEQ", 
					DATA_TYPE.SEQUENCE.toString()));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.STAFF_ID, staffId,
					null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_ID, customerId,
					null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.LAT, lat,
					null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.LNG, lng,
					null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.CREATE_DATE, createDate,
					null));
			updateLocationJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
		}
		return updateLocationJson;
	}

	/**
	 * generate cau lenh update location trong CUSTOMER_POSITION_LOG_TABLE
	 *
	 * @author : yennth16 since : 1.0
	 */
	public JSONObject generateUpdateLocationSql() {
		JSONObject updateLocationJson = new JSONObject();
		try {
			updateLocationJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			updateLocationJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_POSITION_LOG_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			if(!StringUtil.isNullOrEmpty(deleteDate)){
				detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.DELETED_DATE, deleteDate, null));
			}
			if(!StringUtil.isNullOrEmpty(deleteUser)){
				detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.DELETED_USER, deleteUser, null));
			}
			updateLocationJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_POSITION_LOG_ID, id, null));
			updateLocationJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return updateLocationJson;
	}
}
