/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.STAFF_CUSTOMER_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class StaffCustomerDTO extends AbstractTableDTO {
	public long staffCustomerId;
	public long staffId;
	public long customerId;
	public String lastOrder;
	public String lastApproveOrder;
	public String dayPlan;
	public String dayPlanDate;
	public String dayPlanAvg;
	public String dayPlanAvgDate;
	public String exceptionOrderDate;
	public int synState;
	public StaffCustomerDetailDTO staffCustomerDetailDTO = new StaffCustomerDetailDTO();

	public StaffCustomerDTO() {
		super(TableType.STAFF_CUSTOMER_TABLE);
	}

	/**
	 * generate Update ExceptionOrderDate
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateExceptionOrderDate() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STAFF_CUSTOMER_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			if (StringUtil.isNullOrEmpty(exceptionOrderDate)) {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.EXCEPTION_ORDER_DATE, "",
						DATA_TYPE.NULL.toString()));
			} else {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.EXCEPTION_ORDER_DATE, DateUtils.now(),
						DATA_TYPE.SYSDATE.toString()));
			}

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumnWhere(STAFF_CUSTOMER_TABLE.STAFF_CUSTOMER_ID, staffCustomerId, null));
			json.put(IntentConstants.INTENT_LIST_PARAM, params);
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return json;
	}
	
	/**
	* Tao cau truy van insert or update ngay cho phep dat hang tu xa du lieu len server
	* @author: BangHN
	* @return
	* @return: JSONObject
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public JSONObject generateInserOrUpdateExceptionOrderDate() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STAFF_CUSTOMER_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			// staff_customer_id: id tu tang tren server, va khong cap nhat khi update
			params.put(GlobalUtil.getJsonColumnWithKey(STAFF_CUSTOMER_TABLE.STAFF_CUSTOMER_ID, staffCustomerId,null));
			
			if (StringUtil.isNullOrEmpty(exceptionOrderDate)) {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.EXCEPTION_ORDER_DATE, "", DATA_TYPE.NULL.toString()));
			} else {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.EXCEPTION_ORDER_DATE, exceptionOrderDate, null));
				
			}
			// customer id
			params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			// staff id
			params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.STAFF_ID, staffId, null));
			
			json.put(IntentConstants.INTENT_LIST_PARAM, params);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.STAFF_ID, staffId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
			
		} catch (JSONException e) {
		}
		return json;
	}
	
	/**
	 * Generate cau lenh update
	 * @author: TruongHN
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STAFF_CUSTOMER_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			if (StringUtil.isNullOrEmpty(lastOrder)) {
				params.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAST_ORDER, "", DATA_TYPE.NULL.toString()));
			} else {
				params.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAST_ORDER, lastOrder, null));
				
			}
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.STAFF_ID, staffId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
		}
		return json;
	}
	/**
	 * Generate cau lenh insertOrupdate
	 * @author: TruongHN
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateInsertOrUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STAFF_CUSTOMER_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			// staff_customer_id: id tu tang tren server, va khong cap nhat khi update
			params.put(GlobalUtil.getJsonColumnWithKey(STAFF_CUSTOMER_TABLE.STAFF_CUSTOMER_ID, staffCustomerId,null));
			
			if (StringUtil.isNullOrEmpty(lastOrder)) {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.LAST_ORDER, "", DATA_TYPE.NULL.toString()));
			} else {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.LAST_ORDER, lastOrder, null));
			}
			
			if (!StringUtil.isNullOrEmpty(lastApproveOrder)) {
				params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.LAST_APPROVE_ORDER, lastApproveOrder, null));
			}
			
			// customer id
			params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			// staff id
			params.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.STAFF_ID, staffId, null));
			
			json.put(IntentConstants.INTENT_LIST_PARAM, params);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			wheres.put(GlobalUtil.getJsonColumn(STAFF_CUSTOMER_TABLE.STAFF_ID, staffId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
			
		} catch (JSONException e) {
		}
		return json;
	}
}
