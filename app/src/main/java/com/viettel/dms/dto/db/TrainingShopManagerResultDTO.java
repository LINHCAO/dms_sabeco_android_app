/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.TRAINING_SHOP_MANAGER_RESULT_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * 
 * DTO cua bang traning_shop_manager_result
 * 
 * @author: Nguyen Thanh Dung
 * @version: 1.1
 * @since: 1.0
 */
public class TrainingShopManagerResultDTO extends AbstractTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// nhan xet chung
	public static final int OBJECT_TYPE_GENERAL = 0;
	// phan phoi va trung bay
	public static final int OBJECT_TYPE_DISTRIBUTION_DISPLAY = 1;
	// dich vu khach hang
	public static final int OBJECT_TYPE_SERVICE_CUSTOMER = 2;
	// quan he khach hang
	public static final int OBJECT_TYPE_RELATIONSHIP_CUSTOMER = 3;
	// ky nang NVBH
	public static final int OBJECT_TYPE_SKILL_NVBH = 4;
	// ky nang GSNPP
	public static final int OBJECT_TYPE_SKILL_GSNPP = 5;
	// van de khac
	public static final int OBJECT_TYPE_OTHER_PROBLEM = 6;
	// Da xoa
	public static final int STATUS_DELETE = 0;
	// tao moi
	public static final int STATUS_NEW_CREATE = 1;
	// gsnpp da thuc hien
	public static final int STATUS_GSNPP_DONE = 2;
	// da duyet
	public static final int STATUS_DONE_CONFIRM = 3;

	public static final int STATE_NEW_INSERT = 0;
	public static final int STATE_NEW_UPDATE = 1;
	public static final int STATE_DELETED = 2;
	public static final int STATE_NO_UPDATE = 3;
	// id cua this table
	public long ID;
	// id cua bang training plan detail
	public long trainingPlanDetailID;
	// xac dinh loai du lieu cua dong tuong ung
	public int objectType;
	// trang thai
	public int status;
	// chi co y nghia voi object_type = 3. (0: OFF
	// "SKU nay khong can phai xet goi y", 1: ON
	// "SKU dang dinh nghia de goi y ban cho lan sau")
	// ngay nhac nho
	public String remindDate;
	// ngay thuc hien
	public String doneDate;

	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;
	// noi dung
	public String content;
	// state insert or update
	public int currentState;
	// state for object in DB
	public int synState;

	public TrainingShopManagerResultDTO() {
		super(TableType.TRAINING_SHOP_MANAGER_RESULT_TABLE);
	}

	public TrainingShopManagerResultDTO(long tpDetailId, int type,
			int newStatus, String newRemainDate, String newDoneDate,
			String newCreateDate, String newUpdateDate, String newContent,
			int newCurrentState, int newsynState) {
		super(TableType.TRAINING_SHOP_MANAGER_RESULT_TABLE);
		this.trainingPlanDetailID = tpDetailId;
		this.objectType = type;
		this.status = newStatus;
		this.remindDate = newRemainDate;
		this.doneDate = newDoneDate;
		this.createDate = newCreateDate;
		this.updateDate = newUpdateDate;
		this.content = newContent;
		this.currentState = newCurrentState;
		this.synState = newsynState;
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
					TRAINING_SHOP_MANAGER_RESULT_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(TRAINING_SHOP_MANAGER_RESULT_TABLE.ID, ID,
					null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_SHOP_MANAGER_RESULT_TABLE.TRAINING_PLAN_DETAIL_ID,
					trainingPlanDetailID, null));
			if (!StringUtil.isNullOrEmpty(remindDate)) {
				params.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.REMIND_DATE,
						remindDate, null));
			}
			if (!StringUtil.isNullOrEmpty(doneDate)) {
				params.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.DONE_DATE, doneDate,
						null));
			}
			if (!StringUtil.isNullOrEmpty(createDate)) {
				params.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.CREATE_DATE,
						createDate, null));
			}
			if (!StringUtil.isNullOrEmpty(updateDate)) {
				params.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.UPDATE_DATE,
						updateDate, null));
			}
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_SHOP_MANAGER_RESULT_TABLE.OBJECT_TYPE, objectType,
					null));
			params.put(GlobalUtil.getJsonColumn(
					TRAINING_SHOP_MANAGER_RESULT_TABLE.STATUS, status, null));
			if (!StringUtil.isNullOrEmpty(content)) {
				params.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.CONTENT, content,
						null));
			}

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

	/**
	 * 
	 * general sql to delete one training resultdo
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateDeleteSql() {
		JSONObject trainingJson = new JSONObject();
		try {
			trainingJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			trainingJson.put(IntentConstants.INTENT_TABLE_NAME,
					TRAINING_SHOP_MANAGER_RESULT_TABLE.TABLE_NAME);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(
					TRAINING_SHOP_MANAGER_RESULT_TABLE.ID,
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
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.ID) >= 0) {
			this.ID = c.getLong(c
					.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.ID));
		} else {
			this.ID = 0;
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.TRAINING_PLAN_DETAIL_ID) >= 0) {
			this.trainingPlanDetailID = c
					.getLong(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.TRAINING_PLAN_DETAIL_ID));
		} else {
			this.trainingPlanDetailID = 0;
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.UPDATE_DATE) >= 0) {
			this.updateDate = c
					.getString(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.UPDATE_DATE));
		} else {
			this.updateDate = "";
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.REMIND_DATE) >= 0) {
			this.remindDate = c
					.getString(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.REMIND_DATE));
		} else {
			this.remindDate = "";
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.DONE_DATE) >= 0) {
			this.doneDate = c
					.getString(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.DONE_DATE));
		} else {
			this.doneDate = "";
		}

		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.OBJECT_TYPE) >= 0) {
			this.objectType = c
					.getInt(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.OBJECT_TYPE));
		} else {
			this.objectType = 0;
		}

		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.STATUS) >= 0) {
			this.status = c.getInt(c
					.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.STATUS));
		} else {
			this.status = 0;
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.CREATE_DATE) >= 0) {
			this.createDate = c
					.getString(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.CREATE_DATE));
		} else {
			this.createDate = "";
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.CONTENT) >= 0) {
			this.content = c
					.getString(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.CONTENT));
		} else {
			this.content = "";
		}
		if (c.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.SYN_STATE) >= 0) {
			this.synState = c
					.getInt(c
							.getColumnIndex(TRAINING_SHOP_MANAGER_RESULT_TABLE.SYN_STATE));
		} else {
			this.synState = 0;
		}
		this.currentState = STATE_NO_UPDATE;
	}

	/**
	 * 
	 * general sql update content for table
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateContentSql() {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME,
					TRAINING_SHOP_MANAGER_RESULT_TABLE.TABLE_NAME);
			// ds params
			JSONArray detailPara = new JSONArray();
			if (!StringUtil.isNullOrEmpty(updateDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.UPDATE_DATE,
						updateDate, null));
			}
			if (!StringUtil.isNullOrEmpty(content)) {
				detailPara.put(GlobalUtil.getJsonColumn(
						TRAINING_SHOP_MANAGER_RESULT_TABLE.CONTENT, content,
						null));
			}
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(
					TRAINING_SHOP_MANAGER_RESULT_TABLE.ID, this.ID, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}
}
