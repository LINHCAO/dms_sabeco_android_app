/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.SUGGESTED_PRICE_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Luu gia khi tao don hang cua san pham
 * 
 * @author: Nguyen Thanh Dung
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class SuggestedPriceDTO extends AbstractTableDTO {
	// ma gia
	public long suggestedPriceId;
	// ma san pham
	public long productId;
	// hieu luc tu ngay
	public long shopId;
	// hieu luc den ngay
	public long customerId;
	// 1: hoat dong, 0: ngung
	public int status;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;
	// gia
	public double price;
	//staff id
	public long staffId;

	public SuggestedPriceDTO() {
		super(TableType.SUGGESTED_PRICE_TABLE);
	}

	public JSONObject generateInsertOrUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			json.put(IntentConstants.INTENT_TABLE_NAME, SUGGESTED_PRICE_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			// staff_customer_id: id tu tang tren server, va khong cap nhat khi update
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.SUGGESTED_PRICE_ID, suggestedPriceId, null));
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.PRODUCT_ID, productId, null));
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.SHOP_ID, shopId, null));
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.STATUS, status, null));
			params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.PRICE, price, null));
			
			if (!StringUtil.isNullOrEmpty(createDate)) {
				params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.CREATE_DATE, createDate, null));
			}
			
			if (!StringUtil.isNullOrEmpty(createUser)) {
				params.put(GlobalUtil.getJsonColumn(SUGGESTED_PRICE_TABLE.CREATE_USER, createUser, null));
			}
			
			json.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) {
		}
		return json;
	}
}
