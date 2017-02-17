/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.TRAINING_RESULT_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * luu tru mot thong tin danh gia
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class TrainingResultDTO extends AbstractTableDTO {

	private static final long serialVersionUID = 1L;
	public static final int OBJECT_TYPE_IMPROVE_DISPLAY = 0;
	public static final int OBJECT_TYPE_FEEDBACK = 1;
	public static final int OBJECT_TYPE_REVIEWS_SKILL_SALE = 2;
	public static final int OBJECT_TYPE_SKU = 3;
	public static final int STATE_NEW_INSERT = 0;
	public static final int STATE_NEW_UPDATE = 1;
	public static final int STATE_DELETED = 2;
	public static final int STATE_NO_UPDATE = 3;
	// id cua this table
	public long ID;
	// id cua bang training plan detail
	public long trainingPlanDetailID;
	// ma khach hang
	public long customerID;
	// gia tri phu thuoc vao object type
	public long objectID;
	// chi su dung truong hop object_type = 3. Khi do day chinh la SKU (objectID
	// = product_id, objectCode = productCode)
	public String objectCode;
	// xac dinh loai du lieu cua dong tuong ung
	public int objectType;
	// chi co y nghia voi object_type = 3. (0: OFF
	// "SKU nay khong can phai xet goi y", 1: ON
	// "SKU dang dinh nghia de goi y ban cho lan sau")
	public int status;
	// ngay tao
	public String createDate;
	// state insert or update
	public int currentState;
	// state for object in DB
	public int synState;

	public TrainingResultDTO() {
		super(TableType.TRAINING_RESULT_TABLE);
	}

	/**
	 * 
	 * generate SQL to update to server
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateCreateSql() {
		JSONObject orderJson = new JSONObject();
		try {
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME,
					TRAINING_RESULT_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(TRAINING_RESULT_TABLE.ID, ID,
					null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_RESULT_TABLE.TRAINING_PLAN_DETAIL_ID,
					trainingPlanDetailID, null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_RESULT_TABLE.CUSTOMER_ID, customerID, null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_RESULT_TABLE.OBJECT_ID, objectID, null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_RESULT_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumn(TRAINING_RESULT_TABLE.STATUS,
					status, null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_RESULT_TABLE.CREATE_DATE, createDate, null));

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
	
	/**
	 * 
	*  general sql to delete one training resultdo
	*  @author: HaiTC3
	*  @return
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateDeleteSql(){
		JSONObject trainingJson = new JSONObject();
		try {
			trainingJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			trainingJson.put(IntentConstants.INTENT_TABLE_NAME,
					TRAINING_RESULT_TABLE.TABLE_NAME);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(TRAINING_RESULT_TABLE.ID,
					String.valueOf(this.ID), null));
			trainingJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
			
		} catch (Exception e) {
		}
		return trainingJson;
	}

	/**
	 * 
	 * parse data from cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void parserDataFromCursor(Cursor c) {
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.ID) >= 0) {
			this.ID = c.getLong(c.getColumnIndex(TRAINING_RESULT_TABLE.ID));
		} else {
			this.ID = 0;
		}
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.TRAINING_PLAN_DETAIL_ID) >= 0) {
			this.trainingPlanDetailID = c
					.getLong(c
							.getColumnIndex(TRAINING_RESULT_TABLE.TRAINING_PLAN_DETAIL_ID));
		} else {
			this.trainingPlanDetailID = 0;
		}
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.CUSTOMER_ID) >= 0) {
			this.customerID = c.getLong(c
					.getColumnIndex(TRAINING_RESULT_TABLE.CUSTOMER_ID));
		} else {
			this.customerID = 0;
		}
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.OBJECT_ID) >= 0) {
			this.objectID = c.getLong(c
					.getColumnIndex(TRAINING_RESULT_TABLE.OBJECT_ID));
		} else {
			this.objectID = 0;
		}
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.OBJECT_TYPE) >= 0) {
			this.objectType = c.getInt(c
					.getColumnIndex(TRAINING_RESULT_TABLE.OBJECT_TYPE));
		} else {
			this.objectType = 0;
		}

		if (objectType == OBJECT_TYPE_SKU) {
			if (c.getColumnIndex("OBJECT_CODE") >= 0) {
				this.objectCode = c.getString(c.getColumnIndex("OBJECT_CODE"));
			} else {
				this.objectCode = "";
			}
		}

		if (c.getColumnIndex(TRAINING_RESULT_TABLE.STATUS) >= 0) {
			this.status = c.getInt(c
					.getColumnIndex(TRAINING_RESULT_TABLE.STATUS));
		} else {
			this.status = 0;
		}
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.CREATE_DATE) >= 0) {
			this.createDate = c.getString(c
					.getColumnIndex(TRAINING_RESULT_TABLE.CREATE_DATE));
		} else {
			this.createDate = "";
		}
		if (c.getColumnIndex(TRAINING_RESULT_TABLE.SYN_STATE) >= 0) {
			this.synState = c.getInt(c
					.getColumnIndex(TRAINING_RESULT_TABLE.SYN_STATE));
		} else {
			this.synState = 0;
		}
		this.currentState = STATE_NO_UPDATE;
	}
	
	/**
	 * 
	*  khoi tao doi tuong voi cac tham so
	*  @author: HaiTC3
	*  @param id
	*  @param tpDetailId
	*  @param customerId
	*  @param objectId
	*  @param objectType
	*  @param status
	*  @param createDate
	*  @param synState
	*  @return: void
	*  @throws:
	 */
	public void initDateForReviewsNVBH(long id, long tpDetailId, long customerId, long objectId, int objectType, int status, String createDate, int synState){
		this.ID = id;
		this.trainingPlanDetailID = tpDetailId;
		this.customerID = customerId;
		this.objectID = objectId;
		this.objectType = objectType;
		this.status = status;
		this.createDate = createDate;
		this.synState = synState;
		this.currentState = STATE_NEW_INSERT;
	}
}
