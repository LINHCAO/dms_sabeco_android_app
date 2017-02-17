/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 *  Thong tin don hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class SaleOrderDTO extends AbstractTableDTO {
	// id don dat hang
	public long saleOrderId = 0;
	// mo ta
	public String description = "";
	// ma NPP
	public long shopId = 0;
	// loai don dat hang (IN, OFF)
	public String orderType = "";
	// ma don hang
	public String orderNumber = "";
	// so hoa don
	public String invoiceNumber = "";
	//ngay lap don hang
	public String orderDate = "";
	// id khach hang
	public long customerId = 0;
	// ma nhan vien
	public long staffId = 0;
	// phuong tien van chuyen
	public String deliveryId = "";
	// so tien don hang chua tinh khuyen mai
	public long amount = 0;
	// so tien khuyen mai
	public long discount = 0;
	// so tien don hang sau khi tinh khuyen mai
	public long total = 0;
	// nguoi tao
	public String createUser = "";
	// nguoi cap nhat
	public String updateUser = "";
	// ngay tao
	public String createDate = "";
	// ngay cap nhat
	public String updateDate = "";
	// 1: trong tuyen, 0: ngoai tuyen
	public int isVisitPlan = 0;
	// muc do khan
	public long priority ;
	// ngay dat hang mong muon
	public String deliveryDate;
	// 1: don hang ban nhung chua tra, 0: don hang ban da thuc hien tra lai, 2 don tra hàng
	public int type;
	// 2: don hang tao tren tablet chua yeu cau xac nhan, 3: don hang tao tren tablet yeu cau xac nhan, 
	// 0: don hang chua duyet,1: don hang da duyet, 4 huy do qua ngay khong phe duyet
	public int approved;
	// xe giao hang
	public String carId;
	// thue suat
	public double vat;
	// id nhan vien thu tien
	public long cashierId;
	// 1: don hang tren web; 2: don hang tao ra tren table;
	public int orderSource;
	// id don hang bi tra
	public long fromSaleOrderId;
	// tong trong luong don hang
	public double totalWeight;
	// ma CTKM dang Docmt
	public String programeCode;
	// % chiet khau
	public float discountPercent;
	// so tien KM
	public long discountAmount;
	// so tien chiet khau toi da
	public long maxAmountFree;
	// tong so luong sku cua don hang
	public int totalDetail;
	// MA NPP
	public String shopCode;
	// Tong san luong ban
	public long totalQuantity;
	public int synState;
	public String destroyCode;
	public boolean isNotUpdateStockTotal;
	
	public SaleOrderDTO(){
		super(TableType.SALE_ORDER);
	}


	/**
	*  Generate cau lenh insert
	*  @author: TruongHN
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
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, SALE_ORDER_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SALE_ORDER_ID,saleOrderId, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SHOP_ID, shopId, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.ORDER_TYPE, orderType , null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.ORDER_NUMBER,orderNumber, null));
//			if (!StringUtil.isNullOrEmpty(invoiceNumber)){
//				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.INVOICE_NUMBER,invoiceNumber, null));
//			}
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.ORDER_DATE, orderDate, null));
			
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.CUSTOMER_ID,customerId, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.STAFF_ID, staffId, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DELIVERY_ID,deliveryId, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.AMOUNT, amount, null));
			// discount - so tien khuyen mai
			if (discount > 0){
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DISCOUNT, discount, null));	
			}
			// promo code - ma chuong trinh khuyen mai
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL,total, null));
			// order source
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.ORDER_SOURCE,orderSource, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.CREATE_USER, createUser, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.CREATE_DATE,createDate, null));
			if (!StringUtil.isNullOrEmpty(deliveryDate)){
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DELIVERY_DATE,deliveryDate, null));
			}else{
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DELIVERY_DATE,"", DATA_TYPE.NULL.toString()));
			}
			
			if (priority > 0){
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.PRIORITY,priority, null));
			}else{
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.PRIORITY,0, DATA_TYPE.NULL.toString()));
			}
			
			if(!StringUtil.isNullOrEmpty(updateDate)) {
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.UPDATE_DATE,updateDate, null));
			}
			
			if(!StringUtil.isNullOrEmpty(updateUser)) {
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.UPDATE_USER,updateUser, null));
			}
			
			// is_visit_plan
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IS_VISIT_PLAN,isVisitPlan, null));
			// is send
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IS_SEND,isSend, null));
			// syn statuso
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TYPE,type, null));
			// total weght
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL_WEIGHT,totalWeight, null));
			// approve
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.APPROVED,approved, null));
			
			// total detail
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL_DETAIL,totalDetail, null));
			// shop code
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SHOP_CODE, shopCode, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL_QUANTITY, totalQuantity, null));
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
			// ds where params --> insert khong co menh de where
			// sqlInsertOrder.put(IntentConstants.INTENT_LIST_WHERE_PARAM, value);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}

	/**
	 * 
	*  Generate ra cau sql sua don hang
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateEditOrderSql() {
		JSONObject orderJson = new JSONObject();
		try{			
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, SALE_ORDER_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SALE_ORDER_ID,saleOrderId, null));
			// state -- don hang do minh tao nen state = 1
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.AMOUNT, String.valueOf(amount), null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.AMOUNT, amount, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.CUSTOMER_ID, String.valueOf(customerId), null));//tam
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.CUSTOMER_ID, customerId, null));//tam
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DELIVERY_ID, "GN01", null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DISCOUNT, String.valueOf(discount), null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DISCOUNT, discount, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IS_SEND,isSend, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IS_VISIT_PLAN, String.valueOf(isVisitPlan), null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IS_VISIT_PLAN, isVisitPlan, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.INVOICE_NUMBER, "Default", null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.STATE, "1", null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL, String.valueOf(total), null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL, total, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.ORDER_DATE, DATA_VALUE.sysdate.toString(), DATA_TYPE.SYSDATE.toString()));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.UPDATE_DATE, updateDate, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.UPDATE_USER, updateUser, null));//Tam
			if (!StringUtil.isNullOrEmpty(deliveryDate)){
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DELIVERY_DATE,deliveryDate, null));
			}else{
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DELIVERY_DATE,"", DATA_TYPE.NULL.toString()));
			}
			if (priority > 0){
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.PRIORITY,priority, null));
			}else{
				params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.PRIORITY,0, DATA_TYPE.NULL.toString()));
			}
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.DESCRIPTION, "", DATA_TYPE.NULL.toString()));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TYPE,type, null));
			// total weght
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL_WEIGHT,totalWeight, null));
			// order source
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.ORDER_SOURCE,orderSource, null));
			// approve
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.APPROVED,approved, null));
			// total detail
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL_DETAIL,totalDetail, null));
			// total quantity
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.TOTAL_QUANTITY, totalQuantity, null));
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
			// ds where params --> insert khong co menh de where
			JSONArray whereParams = new JSONArray();
			whereParams.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SALE_ORDER_ID, String.valueOf(saleOrderId), null));
			orderJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, whereParams);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
	
//	public JSONObject generateDeleteSaleOrderSql(String id) {
//		JSONObject saleJson = new JSONObject();
//		try {
//			saleJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
//			saleJson.put(IntentConstants.INTENT_TABLE_NAME,
//					SALE_ORDER_TABLE.TABLE_NAME);
//			// ds params
//			JSONArray detailPara = new JSONArray();
//			detailPara.put(GlobalUtil.getJsonColumn(
//					SALE_ORDER_TABLE.SALE_ORDER_ID, id, null));
//			saleJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
//		} catch (Exception e) {
//		}
//		return saleJson;
//	}
	
	public JSONObject generateUpdateSentOrderSql() {
		// TODO Auto-generated method stub
		JSONObject orderJson = new JSONObject();
		try{
//			UPDATE don hang thanh trang thai da chuyen
			
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, SALE_ORDER_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			//params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SALE_ORDER_ID,saleOrderId, null));
			// state -- don hang do minh tao nen state = 1
			//params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.STATE,state, null));
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IMPORT_CODE, "", DATA_TYPE.NULL.toString()));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.UPDATE_USER, updateUser, null));
			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.UPDATE_DATE,updateDate, null));
			// is_visit_plan
			// is send
//			params.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.IS_SEND,isSend, null));
			// syn status
			
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
			// ds where params --> insert khong co menh de where
			// sqlInsertOrder.put(IntentConstants.INTENT_LIST_WHERE_PARAM, value);
			// ds where params --> insert khong co menh de where
			JSONArray whereParams = new JSONArray();
			whereParams.put(GlobalUtil.getJsonColumn(SALE_ORDER_TABLE.SALE_ORDER_ID, String.valueOf(saleOrderId), null));
			orderJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, whereParams);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
	
}
