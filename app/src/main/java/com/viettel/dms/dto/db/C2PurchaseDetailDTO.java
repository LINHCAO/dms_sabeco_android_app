/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.C2_PURCHASE_DETAIL_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/** 
 * C2 Purchase Detail DTO class
 * C2PurchaseDetailDTO.java
 * @version: 1.0 
 * @since:  10:12:23 8 Jan 2014
 */
@SuppressWarnings("serial")
public class C2PurchaseDetailDTO extends AbstractTableDTO {
	public long c2PurchaseDetailId;
	public long shopId;
	public long staffId;
	public long productId;
	public long quantity;
	public String purchaseDate;
	public long amount;
	public long price;
	public int status;
	public long c2PurchaseId;
	public String createUser;
	public String updateUser;
	public String createDate;
	public String updateDate;
	public int synState;
	
	public C2PurchaseDetailDTO() {
		// TODO Auto-generated constructor stub
	}
	
	public JSONObject generateCreateSql(){
		JSONObject orderJson = new JSONObject();
		try{
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, C2_PURCHASE_DETAIL_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.C2_PURCHASE_DETAIL_ID, c2PurchaseDetailId, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.C2_PURCHASE_ID, c2PurchaseId, null));
//			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.SHOP_ID, shopId, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.PRODUCT_ID, productId, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.QUANTITY, quantity, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.PRICE, price, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.AMOUNT, amount, null));
			
			if(!StringUtil.isNullOrEmpty(purchaseDate)) {
				params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.PURCHASE_DATE, purchaseDate, null));
			}
			
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.CREATE_USER, createUser, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.CREATE_DATE,createDate, null));
			
//			if(!StringUtil.isNullOrEmpty(updateDate)) {
//				params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.UPDATE_DATE,updateDate, null));
//			}
//			
//			if(!StringUtil.isNullOrEmpty(updateUser)) {
//				params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.UPDATE_USER,updateUser, null));
//			}
			
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_DETAIL_TABLE.STATUS, status, null));
			
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

}
