/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.C2_SALE_ORDER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/** 
 * C2 Sale Order DTO class 
 * C2SaleOrderDTO.java
 * @version: 1.0 
 * @since:  10:13:29 8 Jan 2014
 */
@SuppressWarnings("serial")
public class C2SaleOrderDTO extends AbstractTableDTO {

	public long c2SaleOrderId;
	public long shopId;
	public long c2Id;
	public long customerId;
	public long staffId;
	public String orderDate;
	public int approved;
	public int status;
	public String description;
	public String createUser;
	public String updateUser;
	public String createDate;
	public String updateDate;
	public int synState;
	public C2SaleOrderDTO() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	*  Generate cau lenh insert
	*  @author: TruongHN
	*  @param seqOrderName
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateCreateSql(){
		JSONObject orderJson = new JSONObject();
		try{
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, C2_SALE_ORDER_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.C2_SALE_ORDER_ID, c2SaleOrderId, null));
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.SHOP_ID, shopId, null));
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.C2_ID, c2Id , null));
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.STAFF_ID, staffId, null));
			
			if(!StringUtil.isNullOrEmpty(orderDate)) {
				params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.ORDER_DATE, orderDate, null));
			}
			
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.CREATE_USER, createUser, null));
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.CREATE_DATE,createDate, null));
			
//			if(!StringUtil.isNullOrEmpty(updateDate)) {
//				params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.UPDATE_DATE,updateDate, null));
//			}
//			
//			if(!StringUtil.isNullOrEmpty(updateUser)) {
//				params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.UPDATE_USER,updateUser, null));
//			}
			
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.APPROVED, approved, null));
			params.put(GlobalUtil.getJsonColumn(C2_SALE_ORDER_TABLE.STATUS, status, null));
			
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

}
