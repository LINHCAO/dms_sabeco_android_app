/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.io.Serializable;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;


/**
 *  thong tin cham trung bay
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class CustomerDisplayProgrameScoreDTO extends AbstractTableDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id bang
	public long ID ;
	// ma khach hang
	public long customerId ; 
	// ID CTTB
	public int displayProgrameId ;
	// id cua NVBH
	public int staffId ; 
	// loai trung bay
	public int objectType ; 
	// id cua doi tuong trung bay
	public int objectId ; 
	// diem trung bay
	public int result;
	// ngay tao
	public String createDate ;
	// state for object in DB
	public int synState;

	
	public CustomerDisplayProgrameScoreDTO(){
		super(TableType.CUSTOMER_DISPLAY_PROGRAME_SCORE);
	}
	
	/**
	 * 
	*  general create sql insert customer display programe score
	*  @author: HaiTC3
	*  @return
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateCreateSql(){
		JSONObject orderJson = new JSONObject();
		try{
//			INSERT INTO SALES_ORDER(SALE_ORDER_ID,SHOP_ID,ORDER_TYPE,ORDER_NUMBER,INVOICE_NUMBER,ORDER_DATE,CUSTOMER_ID,STAFF_ID,
//					DELIVERY_CODE,AMOUNT,DISCOUNT,PROMO_CODE,TOTAL,STATE,CREATE_USER,UPDATE_USER,CREATE_DATE,UPDATE_DATE,IS_VISIT_PLAN,
//					IS_SEND,SYN_STATUS) VALUES (...)
			
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.ID,ID, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.CUSTOMER_ID, customerId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.DISPLAY_PROGRAME_ID, displayProgrameId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.STAFF_ID, staffId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.OBJECT_ID, objectId, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.RESULT, result, null));
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE.CREATE_DATE, createDate, null));
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
}
