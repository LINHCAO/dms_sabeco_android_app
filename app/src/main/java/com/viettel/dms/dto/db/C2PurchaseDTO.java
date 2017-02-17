/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.C2_PURCHASE_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/** 
 * C2 Purchase DTO class
 * C2PurchaseDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:12:44 8 Jan 2014
 */
@SuppressWarnings("serial")
public class C2PurchaseDTO extends AbstractTableDTO {

	public long c2PurchaseId;
	public String fromShopName;
	public long customerId;
	public long staffId;
	public String c2PurchaseDate;
	public int approved;
	public int status;
	public String description;
	public String createUser;
	public String updateUser;
	public String createDate;
	public String updateDate;
	public int synState;
	public C2PurchaseDTO() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	*  Generate cau lenh insert
	*  @author: dungnt19
	*  @param seqOrderName
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateCreateSql(){
		JSONObject orderJson = new JSONObject();
		try{
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, C2_PURCHASE_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.C2_PURCHASE_ID, c2PurchaseId, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.FROM_SHOP_NAME, fromShopName, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.STAFF_ID, staffId, null));
			
			if(!StringUtil.isNullOrEmpty(c2PurchaseDate)) {
				params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.C2_PURCHASE_DATE, c2PurchaseDate, null));
			}
			
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.CREATE_USER, createUser, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.CREATE_DATE,createDate, null));
			
//			if(!StringUtil.isNullOrEmpty(updateDate)) {
//				params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.UPDATE_DATE,updateDate, null));
//			}
//			
//			if(!StringUtil.isNullOrEmpty(updateUser)) {
//				params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.UPDATE_USER,updateUser, null));
//			}
			
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.APPROVED, approved, null));
			params.put(GlobalUtil.getJsonColumn(C2_PURCHASE_TABLE.STATUS, status, null));
			
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

}
