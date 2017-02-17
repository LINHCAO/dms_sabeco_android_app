/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.FEEDBACK_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.PRODUCT_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin san pham them vao danh gia cua GSNPP
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class FeedBackDetailDTO extends AbstractTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int OBJECT_TYPE_SKU = 3;
	public static final int STATE_NEW_INSERT = 0;
	public static final int STATE_NEW_UPDATE = 1;
	public static final int STATE_DELETED = 2;
	public static final int STATE_NO_UPDATE = 3;

	// feedback detail id
	public long feedbackDetailId = -1;
	// feedback id
	public long feedbackId = -1;
	// product id
	public long productId = -1;
	// create user id
	public long createUserId = -1;
	// product code
	public String productCode = Constants.STR_BLANK;

	// state insert or update
	public int currentState;

	public FeedBackDetailDTO() {
		super(TableType.FEEDBACK_DETAIL_TABLE);
		currentState = STATE_NEW_INSERT;
	}

	/**
	 * 
	 * init data with param
	 * 
	 * @author: HaiTC3
	 * @param feedbackDetailId
	 * @param feedbackId
	 * @param productId
	 * @return: void
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public void initData(long feedbackDetailId, long feedbackId,
			long productId, String productCode, long createUserId) {
		this.feedbackDetailId = feedbackDetailId;
		this.feedbackId = feedbackId;
		this.productCode = productCode;
		this.productId = productId;
		this.createUserId = createUserId;
	}

	/**
	 * 
	 * init data with cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public void initWithCursor(Cursor c) {
		if (c.getColumnIndex(FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_ID) >= 0) {
			this.feedbackDetailId = c.getLong(c
					.getColumnIndex(FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_ID));
		} else {
			this.feedbackDetailId = 0;
		}

		if (c.getColumnIndex(FEEDBACK_DETAIL_TABLE.FEEDBACK_ID) >= 0) {
			this.feedbackId = c.getLong(c
					.getColumnIndex(FEEDBACK_DETAIL_TABLE.FEEDBACK_ID));
		} else {
			this.feedbackId = 0;
		}

		if (c.getColumnIndex(FEEDBACK_DETAIL_TABLE.PRODUCT_ID) >= 0) {
			this.productId = c.getLong(c
					.getColumnIndex(FEEDBACK_DETAIL_TABLE.PRODUCT_ID));
		} else {
			this.productId = 0;
		}
		if (c.getColumnIndex(FEEDBACK_DETAIL_TABLE.CREATE_USER_ID) >= 0) {
			this.createUserId = c.getLong(c
					.getColumnIndex(FEEDBACK_DETAIL_TABLE.CREATE_USER_ID));
		} else {
			this.createUserId = 0;
		}
		if (c.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE) >= 0) {
			this.productCode = c.getString(c
					.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE));
		} else {
			this.productCode = Constants.STR_BLANK;
		}

		this.currentState = STATE_NO_UPDATE;
	}

	/**
	 * general sql to insert to server when reviews of GSNPP
	 * 
	 * @return
	 * @return: Vector
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 16, 2013
	 */
	public JSONObject generateCreateSqlInsertFeedbackDetail() {
		JSONObject orderJson = new JSONObject();
		try {
			// INSERT INTO
			// SALES_ORDER(SALE_ORDER_ID,SHOP_ID,ORDER_TYPE,ORDER_NUMBER,INVOICE_NUMBER,ORDER_DATE,CUSTOMER_ID,STAFF_ID,
			// DELIVERY_CODE,AMOUNT,DISCOUNT,PROMO_CODE,TOTAL,STATE,CREATE_USER,UPDATE_USER,CREATE_DATE,UPDATE_DATE,IS_VISIT_PLAN,
			// IS_SEND,SYN_STATUS) VALUES (...)

			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME,
					FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_TABLE);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(
					FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_ID, feedbackDetailId,
					null));
			params.put(GlobalUtil.getJsonColumn(
					FEEDBACK_DETAIL_TABLE.FEEDBACK_ID, feedbackId, null));
			params.put(GlobalUtil.getJsonColumn(
					FEEDBACK_DETAIL_TABLE.PRODUCT_ID, productId, null));

			params.put(GlobalUtil.getJsonColumn(
					FEEDBACK_DETAIL_TABLE.CREATE_USER_ID, createUserId, null));

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

	/**
	 * 
	 * general sql delete one feedback detail
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: JSONObject
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public JSONObject generateDeleteSql() {
		JSONObject trainingJson = new JSONObject();
		try {
			trainingJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			trainingJson.put(IntentConstants.INTENT_TABLE_NAME,
					FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_TABLE);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(
					FEEDBACK_DETAIL_TABLE.FEEDBACK_DETAIL_ID,
					String.valueOf(this.feedbackDetailId), null));
			trainingJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return trainingJson;
	}
}
