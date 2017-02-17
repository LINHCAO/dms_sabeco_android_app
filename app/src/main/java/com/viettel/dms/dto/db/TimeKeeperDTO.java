/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.TIME_KEEPER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin loai customer stock history
 * 
 * @author: PhucNT
 * @version: 1.0
 * @since: 1.0
 */
public class TimeKeeperDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id cua bang
	public long timeKeeperId;
	// id cua khach hang
	public long staffOwnerId;
	// id cua nhan vien ban hang
	public long staffId;
	// id cua san pham
	public long shopId;
	public long customerId;
	// object type (1: CTTB)
	public int type;
	// object id (tuong ung voi object type (1: DISPLAY_PROGRAM_ID))
	public long isAbsent;
	// result: so luong ton kho cua san pham luc cham
	public int dresses;
	public int rule;
	public String startTime; 
	// ngay tao
	public String createDate;
	public String createUser;
	public String updateDate;
	public String updateUser;

	public boolean isEdit;
	
	public TimeKeeperDTO() {
		super(TableType.TIME_KEEPER_TABLE);
	}

	public JSONObject generateInsertSql() {
		// TODO Auto-generated method stub
		JSONObject orderJson = new JSONObject();
		try {
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, TIME_KEEPER_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.TIME_KEEPER_ID,timeKeeperId, null));
			// state -- don hang do minh tao nen state = 1
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.STAFF_OWNER_ID, staffOwnerId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.SHOP_ID, shopId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.TYPE, type, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.IS_ABSENT, isAbsent, null));
			
			if(isAbsent == 0) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.DRESSES, dresses, null));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.RULE, rule, null));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.START_TIME, startTime, null));
			}
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.CREATE_DATE, createDate, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.CREATE_USER, createUser, null));
			
			if(!StringUtil.isNullOrEmpty(updateDate)) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.UPDATE_DATE, updateDate, null));
			}
			
			if(!StringUtil.isNullOrEmpty(updateUser)) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.UPDATE_USER, updateUser, null));
			}

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

	public JSONObject generateInsertOrUpdateSql() {
		// TODO Auto-generated method stub
		JSONObject orderJson = new JSONObject();
		try {
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, TIME_KEEPER_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			if(!isEdit) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.TIME_KEEPER_ID, timeKeeperId, null));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.CREATE_DATE, createDate, null));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.CREATE_USER, createUser, null));
			}
			// state -- don hang do minh tao nen state = 1
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.STAFF_OWNER_ID, staffOwnerId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.SHOP_ID, shopId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.TYPE, type, null));
			params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.IS_ABSENT, isAbsent, null));

			if(isAbsent == 0) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.DRESSES, dresses, null));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.RULE, rule, null));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.START_TIME, startTime, null));
			} else {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.DRESSES, "", DATA_TYPE.NULL.toString()));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.RULE, "", DATA_TYPE.NULL.toString()));
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.START_TIME, "", DATA_TYPE.NULL.toString()));
			}

			if(!StringUtil.isNullOrEmpty(updateDate)) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.UPDATE_DATE, updateDate, null));
			}

			if(!StringUtil.isNullOrEmpty(updateUser)) {
				params.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.UPDATE_USER, updateUser, null));
			}

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(TIME_KEEPER_TABLE.TIME_KEEPER_ID, timeKeeperId, null));
			orderJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
}
