/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.ACTION_LOG_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin cau hinh
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class ActionLogDTO extends AbstractTableDTO {
	public static final String TYPE_VISIT = "0";
	public static final String TYPE_VISIT_CLOSING = "1";
	public static final String TYPE_VOTE_DISPLAY = "2";
	public static final String TYPE_REMAIN_PRODUCT = "3";
	public static final String TYPE_ORDER = "4";
	public static final String TYPE_EXCEPTION_ORDER = "5";
	public static final String TYPE_REMAIN_COMPETITOR = "6";
	public static final String TYPE_SALE_COMPETITOR = "7";
	public static final String TYPE_PG_TAKE_ATTENDANCE = "8";
	public static final String TYPE_END_VISIT = "9";
	
	// id
	public long id;
	// customer
	public CustomerDTO aCustomer = new CustomerDTO();
	// staff id
	public long staffId;
	// object id
	public String objectId = "";
	// object type
	public String objectType = "";
	// lat
	public double lat;
	// lng
	public double lng;
	// action type
	public String actionType;
	// ngay gio bat dau action
	public String startTime;
	public String endTime;
	// ngay cap nhat
	public String updateDate;
	// nguoi tao
	public String createUser;
	// bien cho biet 
	public int haveAction=0;
	// nguoi cap nhat
	public String updateUser;
	// 0:trong tuyen, 1:ngoai tuyen
	public int isOr = 0; //
	
	public String interval_time;

	public Double distance;

	public ActionLogDTO() {
		super(TableType.ACTION_LOG);
	}

	/**
	 * tao ra mot cau truc sql de gui len server insert action log
	 * 
	 * @author : BangHN since : 1.0
	 */
	public JSONObject generateActionLogSql() {
		JSONObject actionJson = new JSONObject();

		// inster into ACTION_LOG
		// (ID, CUSTOMER_ID, STAFF_ID, OBJECT_ID, OBJECT_TYPE, LAT, LNG,
		// ACTION_TYPE, CREATE_DATE)

		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, ACTION_LOG_TABLE.TABLE_NAME);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.ACTION_LOG_ID, id, null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.CUSTOMER_ID, aCustomer.getCustomerId(), null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.STAFF_ID, staffId, null));
			if (!StringUtil.isNullOrEmpty(objectId)) {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_ID, objectId, null));
			} else {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_ID, "", DATA_TYPE.NULL.toString()));
			}
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_TYPE, objectType, null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.LAT, lat, null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.LNG, lng, null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.START_TIME, startTime, null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.IS_OR, isOr, null));
			if (!StringUtil.isNullOrEmpty(endTime)) {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.END_TIME, endTime, null));
			}
			if (!StringUtil.isNullOrEmpty(interval_time)) {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.INTERVAL_TIME, interval_time, null));
			}
			if (distance != null && distance >= 0) {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.DISTANCE, distance, null));
			}
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
		}

		return actionJson;
	}

	public JSONObject generateUpdateActionLogSql() {
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, ACTION_LOG_TABLE.TABLE_NAME);

			// ds params
			JSONArray detailPara = new JSONArray();

			if (!StringUtil.isNullOrEmpty(objectId)) {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_ID, objectId, null));
			} else {
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_ID, "", DATA_TYPE.NULL.toString()));
			}
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_TYPE, objectType, null));
			// detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.LAT,
			// lat,
			// null));
			// detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.LNG,
			// lng,
			// null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.END_TIME, endTime, null));
			detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.INTERVAL_TIME, interval_time, null));
			
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.ACTION_LOG_ID, this.id, null));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}

	/**
	 * tao cau lenh delete action_log len server Xoa nhung action_log cua staff,
	 * customer voi object tuong ung trong ngay hien tai
	 * 
	 * @author: BangHN
	 * @return
	 * @return: JSONObject
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public JSONObject generateDeleteActionLogSql() {
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, ACTION_LOG_TABLE.TABLE_NAME);

			// ds params
			JSONArray detailPara = new JSONArray();
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.STAFF_ID, String.valueOf(staffId), null));
			wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.CUSTOMER_ID, String.valueOf(aCustomer.customerId),
					null));
			wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_TYPE, objectType, null));
			wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.START_TIME, startTime, DATA_TYPE.TRUNC.toString()));

			// wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.ACTION_LOG_ID,
			// this.id, null));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}

	public void initDataFromCursor(Cursor c) {
		if (c.getColumnIndex("ACTION_LOG_ID") >= 0) {
			id = c.getLong(c.getColumnIndex("ACTION_LOG_ID"));
		} else {
			id = -1;
		}
		if (c.getColumnIndex("STAFF_ID") >= 0) {
			staffId = c.getInt(c.getColumnIndex("STAFF_ID"));
		} else {
			staffId = -1;
		}
		if (c.getColumnIndex("HAVE_ACTION") >= 0) {
			haveAction = c.getInt(c.getColumnIndex("HAVE_ACTION"));
		} else {
			haveAction = 0;
		}
		if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
			aCustomer.customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
		} else {
			aCustomer.customerId = -1;
		}
		if (c.getColumnIndex("OBJECT_ID") >= 0) {
			objectId = c.getString(c.getColumnIndex("OBJECT_ID"));
		} else {
			objectId = "";
		}
		if (c.getColumnIndex("OBJECT_TYPE") >= 0) {
			objectType = c.getString(c.getColumnIndex("OBJECT_TYPE"));
		} else {
			objectType = "";
		}
		if (c.getColumnIndex("LAT") >= 0) {
			lat = c.getDouble(c.getColumnIndex("LAT"));
		} else {
			lat = 0;
		}
		if (c.getColumnIndex("LNG") >= 0) {
			lng = c.getDouble(c.getColumnIndex("LNG"));
		} else {
			lng = 0;
		}
		if (c.getColumnIndex("START_TIME") >= 0) {
			startTime = c.getString(c.getColumnIndex("START_TIME"));
		} else {
			startTime = "";
		}
		if (c.getColumnIndex("END_TIME") >= 0) {
			endTime = c.getString(c.getColumnIndex("END_TIME"));
		} else {
			endTime = "";
		}
		if (c.getColumnIndex("IS_OR") >= 0) {
			isOr = c.getInt(c.getColumnIndex("IS_OR"));
		} else {
			isOr = 0;
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			aCustomer.customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		} else {
			aCustomer.customerName = "";
		}
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			aCustomer.customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
		} else {
			aCustomer.customerCode = "";
		}
		if (c.getColumnIndex("SHORT_CODE") >= 0) {
			aCustomer.shortCode = c.getString(c.getColumnIndex("SHORT_CODE"));
		} else {
			aCustomer.shortCode = "";
		}
	}

	public boolean isVisited() {
		boolean isVisited = false;
		if (("0".equals(objectType) && !StringUtil.isNullOrEmpty(endTime)) || "1".equals(objectType)) {
			isVisited = true;
		}
		return isVisited;
	}
	
	public boolean isCheckRemainAndSale(){
		boolean isVisited = false;
		if ((ActionLogDTO.TYPE_REMAIN_COMPETITOR.equals(objectType) && !StringUtil.isNullOrEmpty(endTime)) || 
				ActionLogDTO.TYPE_SALE_COMPETITOR.equals(objectType)|| 
				ActionLogDTO.TYPE_PG_TAKE_ATTENDANCE.equals(objectType)) {
			isVisited = true;
		}
		return isVisited;
	}

	/**
	 * Generate code delete action when delete order
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: Vector
	 * @throws:
	 */

	public JSONObject generateDeleteActionWhenDeleteOrder() {
		JSONObject deleteJson = new JSONObject();
		try {
			deleteJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			deleteJson.put(IntentConstants.INTENT_TABLE_NAME, ACTION_LOG_TABLE.TABLE_NAME);

			// ds where params --> insert khong co menh de where
			JSONArray whereParams = new JSONArray();
			whereParams.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.STAFF_ID, String.valueOf(staffId), null));
			whereParams.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_ID, objectId, null));
			whereParams.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.OBJECT_TYPE, objectType, null));

			deleteJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, whereParams);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return deleteJson;
	}

	public ActionLogDTO copyActionLog() {
		ActionLogDTO al = new ActionLogDTO();
		al.actionType = this.actionType;
		al.aCustomer.customerId = this.aCustomer.customerId;
		al.aCustomer.customerName = this.aCustomer.customerName;
		al.aCustomer.customerCode = this.aCustomer.customerCode;
		al.createUser = this.createUser;
		al.endTime = this.endTime;
		al.id = this.id;
		al.isOr = this.isOr;
		al.lat = this.lat;
		al.lng = this.lng;
		al.objectId = this.objectId;
		al.objectType = this.objectType;
		al.staffId = this.staffId;
		al.startTime = this.startTime;
		al.updateDate = this.updateDate;
		al.updateUser = this.updateUser;
		return al;
	}

	/**
	 * tao chuoi JSON de cap nhat lại actionLog
	 * 
	 * @author: TamPQ
	 * @param listActLog
	 * @return: JSONArray
	 * @throws:
	 */
	public JSONArray generateUpdateEntimeListActionLogSql(ArrayList<ActionLogDTO> listActLog) {
		JSONArray jsonArr = new JSONArray();
		for (int i = 0; i < listActLog.size(); i++) {
			ActionLogDTO item = listActLog.get(i);
			JSONObject actionJson = new JSONObject();
			try {
				actionJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
				actionJson.put(IntentConstants.INTENT_TABLE_NAME, ACTION_LOG_TABLE.TABLE_NAME);
				// ds params
				JSONArray detailPara = new JSONArray();
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.END_TIME, item.endTime, null));
				detailPara.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.INTERVAL_TIME, item.interval_time, null));
				actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

				// ds where params --> insert khong co menh de where
				JSONArray wheres = new JSONArray();
				wheres.put(GlobalUtil.getJsonColumn(ACTION_LOG_TABLE.ACTION_LOG_ID, item.id, null));
				actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
			} catch (Exception e) {
			}

			jsonArr.put(actionJson);
		}

		return jsonArr;
	}
}
