/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO.TableAction;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.sqllite.db.ABSTRACT_TABLE;
import com.viettel.dms.sqllite.db.AP_PARAM_TABLE;
import com.viettel.dms.sqllite.db.SALES_ORDER_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;

/** 
 * Sale Order View DTO class
 * SaleOrderViewDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:40:08 8 Jan 2014
 */
@SuppressWarnings("serial")
public class SaleOrderViewDTO implements Serializable{
	public SaleOrderDTO saleOrder = new SaleOrderDTO();
	public CustomerDTO customer = new CustomerDTO();
	//private String customerCode;
	private String description;
	public boolean isChecked;
	//Ngay/gio dat hang cuoi cung
	public String lastOrder;
	// co phai don hang cuoi cung cua khach hang 
	public int isFinalOrder;
//	public String customerStreet = "";
	public String priorityCode;
	public String staffCode;
	public String staffName;
	
	public String getDESCRIPTION() {
		if (!StringUtil.isNullOrEmpty(description))
			description=description.trim();
		return description;
	}

	public void setDESCRIPTION(String descrip) {
		description = descrip;
	}
	public static List<SaleOrderViewDTO> initListDataFromCursor(Cursor cursor)
			throws Exception {

		List<SaleOrderViewDTO> listData = new ArrayList<SaleOrderViewDTO>();
		if (cursor == null) {
			throw new Exception("Cursor is empty");
		}

		if (cursor.moveToFirst()) {
			do {
				listData.add(SaleOrderViewDTO.initDataFromCursor(cursor));

			} while (cursor.moveToNext());
		}

		return listData;
	}

	public static SaleOrderViewDTO initDataFromCursor(Cursor c)
			throws Exception {
		SaleOrderViewDTO data = new SaleOrderViewDTO();

//		data.saleOrder.orderDate = DateUtils.parseDateFromSqlLite(c.getString(c.getColumnIndexOrThrow("ORDER_DATE")));
		String code = c.getString(c.getColumnIndexOrThrow("SHORT_CODE"));
//		if (code.length() > 3){
//			data.customer.customerCode = String.valueOf(code.subSequence(0, 3));
//		}else{
//			data.customer.customerCode = code;
//		}
		data.customer.customerCode = code;
		data.customer.setStreet( c.getString(c
				.getColumnIndexOrThrow("STREET")));
		data.customer.setCustomerName(c.getString(c.getColumnIndexOrThrow("CUSTOMER_NAME")));
		data.saleOrder.customerId = c.getLong(c.getColumnIndexOrThrow("CUSTOMER_ID"));
		data.customer.customerId = data.saleOrder.customerId;//for edit order -> get product list 
		data.customer.setCustomerTypeId(c.getInt(c.getColumnIndexOrThrow("CUSTOMER_TYPE_ID")));//for edit order -> get product list
		data.saleOrder.shopId = c.getInt(c.getColumnIndexOrThrow("SHOP_ID"));
		data.saleOrder.staffId = c.getInt(c.getColumnIndexOrThrow("STAFF_ID"));
		data.saleOrder.orderDate = c.getString(c.getColumnIndexOrThrow("ORDER_DATE"));
		data.saleOrder.createDate = c.getString(c.getColumnIndexOrThrow("CREATE_DATE"));
		data.saleOrder.createUser = c.getString(c.getColumnIndexOrThrow("CREATE_USER"));
		data.saleOrder.total = c.getLong(c.getColumnIndexOrThrow("TOTAL"));
		data.saleOrder.approved = c.getInt(c.getColumnIndexOrThrow("APPROVED"));
		data.saleOrder.isVisitPlan = c.getInt(c
				.getColumnIndexOrThrow("IS_VISIT_PLAN"));
		data.saleOrder.orderNumber = c
				.getString(c.getColumnIndexOrThrow("ORDER_NUMBER"));
		data.saleOrder.saleOrderId = c.getLong(c
				.getColumnIndexOrThrow("SALE_ORDER_ID"));
//		data.saleOrder.state = c.getInt(c.getColumnIndexOrThrow("STATE"));
		data.saleOrder.orderType = c.getString(c.getColumnIndexOrThrow("ORDER_TYPE"));
		data.saleOrder.synState = c.getInt(c.getColumnIndexOrThrow(ABSTRACT_TABLE.SYN_STATE));
		data.saleOrder.priority = c.getLong(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.PRIORITY));
		data.saleOrder.destroyCode = c.getString(c.getColumnIndexOrThrow("DESTROY_CODE"));
		data.priorityCode = c.getString(c.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_CODE));
		data.description = c.getString(c.getColumnIndexOrThrow("DESCRIPTION"));
		data.staffCode = c.getString(c.getColumnIndexOrThrow("STAFF_CODE"));
		data.staffName = c.getString(c.getColumnIndexOrThrow("STAFF_NAME"));
		
		return data;

	}

	public String getOrderDate() {
		return saleOrder.orderDate ;
	}

	public void setOrderDate(String ord_date) {
		saleOrder.orderDate  = ord_date;
	}

//	public String getCustomerCode() {
//		return customerCode;
//	}
//
//	public void setCustomerCode(String customerCode) {
//		this.customerCode = customerCode;
//	}

	public long getCustomerId() {
		return saleOrder.customerId;
	}

	public void setCusomerId(long customerId) {
		saleOrder.customerId = customerId;
	}



	public long getTotal() {
		return saleOrder.total;
	}

	public void setTotal(long total) {
		this.saleOrder.total = total;
	}


	public int getIsVisitPlan() {
		return saleOrder.isVisitPlan;
	}

	public void setIsVisitPlan(int IsVisitPlan) {
		saleOrder.isVisitPlan = IsVisitPlan;
	}

	public String getOrderNumber() {
		return saleOrder.orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		saleOrder.orderNumber = orderNumber;
	}

	public long getSaleOrderId() {
		return saleOrder.saleOrderId;
	}

	public void setSaleOrderId(int saleOrderId) {
		saleOrder.saleOrderId = saleOrderId;
	}

	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (saleOrder.saleOrderId == ((SaleOrderViewDTO)o).saleOrder.saleOrderId)
			return true;
		return false;
	}

	public JSONArray generateDeleteSaleOrderSql() {
		JSONArray listSql = new JSONArray();
		
		try {
			// xoa nhung sale order detail 
			JSONObject sdJson = new JSONObject();
			sdJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			sdJson.put(IntentConstants.INTENT_TABLE_NAME,
					SALES_ORDER_DETAIL_TABLE.TABLE_NAME);
			// ds params
			JSONArray sdArr = new JSONArray();
			sdArr.put(GlobalUtil.getJsonColumn(
					SALES_ORDER_DETAIL_TABLE.SALE_ORDER_ID, saleOrder.saleOrderId, null));
//			sdJson.put(IntentConstants.INTENT_LIST_PARAM, "");
			sdJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, sdArr);
			listSql.put(sdJson);
			
			// xoa sale order 
			JSONObject saleJson = new JSONObject();
			saleJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			saleJson.put(IntentConstants.INTENT_TABLE_NAME,
					SALE_ORDER_TABLE.TABLE_NAME);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(
					SALE_ORDER_TABLE.SALE_ORDER_ID, saleOrder.saleOrderId, null));
//			saleJson.put(IntentConstants.INTENT_LIST_PARAM, "");
			saleJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, detailPara);
			listSql.put(saleJson);
			
			
		} catch (Exception e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return listSql;
	}
}
