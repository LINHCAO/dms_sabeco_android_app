/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.view.RemainProductViewDTO;
import com.viettel.dms.sqllite.db.CUSTOMER_STOCK_HISTORY_TABLE;
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
public class CustomerStockHistoryDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int TYPE_CTTB = 1;

	// id cua bang
	public long customerStockHistoryId;
	// id cua khach hang
	public long customerId;
	// id cua nhan vien ban hang
	public long staffId;
	// id cua san pham
	public long productId;
	// object type (1: CTTB)
	public int objectType;
	// object id (tuong ung voi object type (1: DISPLAY_PROGRAM_ID))
	public long objectId;
	// result: so luong ton kho cua san pham luc cham
	public int result;
	// ngay tao
	public String createDate;

	// // id sale order
	// public int saleOrderId ;
	// so luong
	// public int quanlity ;
	public String checkDate;

	public CustomerStockHistoryDTO() {
		super(TableType.CUSTOMER_STOCK_HISTORY_TABLE);
	}

	public void convertFromRemainProductDTO(RemainProductViewDTO s) {
		// TODO Auto-generated method stub
		customerId = s.CUSTOMER_ID;
		staffId = s.STAFF_ID;
		productId = s.productId;
		if(!StringUtil.isNullOrEmpty(s.remainNumber)){
			result = Integer.parseInt(s.remainNumber);
		}
	}

	public JSONObject generateUpdateRemainProductSql() {
		// TODO Auto-generated method stub
		JSONObject orderJson = new JSONObject();
		try {
			// UPDATE don hang thanh trang thai da chuyen

			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_STOCK_HISTORY_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CUSTOMER_STOCK_HISTORY_ID,
					customerStockHistoryId, null));
			// state -- don hang do minh tao nen state = 1
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.PRODUCT_ID, productId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.OBJECT_ID, objectId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.RESULT, result, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CREATE_DATE, createDate, null));
			// params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CHECK_DATE,
			// checkDate, null));
			// params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.QUANTITY,
			// quanlity, null));
			// syn status

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

	/**
	 * general sql to insert to server when vote display program
	 * 
	 * @return
	 * @return: Vector
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 16, 2013
	 */
	public JSONObject generateCreateSqlInsertVoteDisplayProgram() {
		JSONObject orderJson = new JSONObject();
		try {
			// INSERT INTO
			// SALES_ORDER(SALE_ORDER_ID,SHOP_ID,ORDER_TYPE,ORDER_NUMBER,INVOICE_NUMBER,ORDER_DATE,CUSTOMER_ID,STAFF_ID,
			// DELIVERY_CODE,AMOUNT,DISCOUNT,PROMO_CODE,TOTAL,STATE,CREATE_USER,UPDATE_USER,CREATE_DATE,UPDATE_DATE,IS_VISIT_PLAN,
			// IS_SEND,SYN_STATUS) VALUES (...)

			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_STOCK_HISTORY_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CUSTOMER_STOCK_HISTORY_ID,
					customerStockHistoryId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.PRODUCT_ID, productId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.OBJECT_ID, objectId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.RESULT, result, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_STOCK_HISTORY_TABLE.CREATE_DATE, createDate, null));

			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) { 
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
}
