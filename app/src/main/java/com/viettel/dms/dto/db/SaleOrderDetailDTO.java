/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.SALES_ORDER_DETAIL_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 *  Thong tin chi tiet don hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class SaleOrderDetailDTO extends AbstractTableDTO implements
		Serializable {
	// id chi tiet don hang
	public long salesOrderDetailId;
	// ma san pham
	public int productId;
	// so luong
	public int quantity;
	// gia lich su
	public long priceId;
	// % khuyen mai
	public float discountPercentage;
	// so tien khuyen mai
	public long discountAmount;
	// 0: hang ban, 1: hang KM
	public int isFreeItem;
	// so tien
	public long amount;
	// id don dat hang
	public long salesOrderId;
	// gia ban
	public long price;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;
	// ngay dat
	public String orderDate;
	// so luong KM max cua mat hang
	public int maxQuantityFree;
	// loai khuyen mai hay chuong trinh trung bay: 0 - CTKM tinh tu dong, 1 - CTKM tinh manual, 2 - CTTB
	public int programeType = -1; // mac dinh la -1 de kiem tra khi luu
	// ma chuong trinh khuyen mai - ma chuong trinh trung bay
	public String programeCode;
	// ten chuong trinh khuyen mai
	public String programeName;
	// id cua nv
	public long staffId;
	// id nha phan phoi
	public long shopId;
	
	// thue VAT
	public double vat;
	// gia chua VAT
	public long priceNotVat;
	// trong luong
	public double totalWeight;
	//max tien KM
	public long maxAmountFree;
	//lưu loại KM (ví dụ ZV01, 02…)
	public String programeTypeCode;
	
	// trang thai da syn hay chua?
	public int synState;
	
	
	public SaleOrderDetailDTO(){
		super(TableType.SALE_ORDER_DETAIL);
	}

	public String insertSql() {
		String sql = "insert into SALES_ORDER (SALE_ORDER_ID,SHOP_ID, ORDER_NUMBER, ORDER_DATE, CUSTOMER_ID, STAFF_ID,"
				+ "DELIVERY_ID, AMOUNT, TOTAL, CREATE_USER, CREATE_DATE) values (SALES_ORDER_SEQ.nextval,?,?,sysdate,?,?,?,?,?,?,sysdate)";
		return sql;
	}

	public ArrayList<String> generateInsertOrderSql() {
		ArrayList<String> result = new ArrayList<String>();
		String sql = "insert into SALES_ORDER (SALE_ORDER_ID,SHOP_ID, ORDER_NUMBER, ORDER_DATE, CUSTOMER_ID, STAFF_ID,"
				+ "DELIVERY_ID, AMOUNT, TOTAL, CREATE_USER, CREATE_DATE) values (SALES_ORDER_SEQ.nextval,?,?,sysdate,?,?,?,?,?,?,sysdate)";

		StringBuffer paraBuff = new StringBuffer();
//		paraBuff.append(this.shopId);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.orderNumber);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.customerId);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.staffId);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.DELIVERY_CODE);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.amount);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.TOTAL);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.createUser);

		result.add(sql);
		result.add(paraBuff.toString());
		return result;
	}

	
	
	/**
	*  Generate cau lenh insert
	*  @author: TruongHN
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateCreateSql(){
		JSONObject orderDetailJson = new JSONObject();
		try{
//			INSERT INTO
//			SALES_ORDER_DETAIL(SALES_ORDER_DETAIL_ID,PRODUCT_ID,QUANTITY,PRICE_ID,DISCOUNT_PERCENTAGE,DISCOUNT_AMOUNT,IS_FREE_ITEM,
//			PROMOTION_CODE,AMOUNT,REASON_CODE,SALE_ORDER_ID,PRICE,CREATE_USER,UPDATE_USER,CREATE_DATE,UPDATE_DATE,
//			ORDER_DATE,MAX_QUANTITY_FREE) VALUES()

			orderDetailJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderDetailJson.put(IntentConstants.INTENT_TABLE_NAME, SALES_ORDER_DETAIL_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALE_ORDER_DETAIL_ID,salesOrderDetailId, null));
//			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALES_ORDER_DETAIL_ID,"SALES_ORDER_DETAIL_SEQ", DATA_TYPE.SEQUENCE.toString()));
			
			if(productId > 0) {
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRODUCT_ID, productId, null));
			} else {
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRODUCT_ID,"", DATA_TYPE.NULL.toString()));
			}
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.QUANTITY,quantity, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.MAX_QUANTITY_FREE,maxQuantityFree, null));
			// thong tin khuyen mai
			if (discountPercentage > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.DISCOUNT_PERCENT,discountPercentage, null));
			}
			if (discountAmount > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.DISCOUNT_AMOUNT,discountAmount, null));
			}
			if (maxAmountFree > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.MAX_AMOUNT_FREE, maxAmountFree, null));
			}
			
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.IS_FREE_ITEM,isFreeItem, null));
			if (!StringUtil.isNullOrEmpty(programeCode)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PROGRAM_CODE,programeCode, null));
			}
			// them vao programeType neu la mat hang khuyen mai
			if (isFreeItem == 1 && programeType >= 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PROGRAM_TYPE,programeType, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.AMOUNT,amount, null));
			// reasonCode -- chua dung nen ko can insert
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALE_ORDER_ID,salesOrderId, null));
			
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRICE,price, null));
			if (priceId > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRICE_ID,priceId, null));
			}
			if (!StringUtil.isNullOrEmpty(createUser)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.CREATE_USER,createUser, null));
			}
			if (!StringUtil.isNullOrEmpty(updateUser)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.UPDATE_USER,updateUser, null));
			}
			if (shopId > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SHOP_ID,shopId, null));
			}
			if (staffId > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.STAFF_ID,staffId, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.CREATE_DATE,createDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.ORDER_DATE,orderDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRICE_NOT_VAT,priceNotVat, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.TOTAL_WEIGHT,totalWeight, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.VAT,vat, null));
			if (!StringUtil.isNullOrEmpty(programeTypeCode)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PROGRAME_TYPE_CODE, programeTypeCode, null));
			}
			orderDetailJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			// sqlInsertOrder.put(IntentConstants.INTENT_LIST_WHERE_PARAM, value);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderDetailJson;
	}
	
	public JSONObject generateCreateSqlForUpdate(){
		JSONObject orderDetailJson = new JSONObject();
		try{
//			INSERT INTO
//			SALES_ORDER_DETAIL(SALES_ORDER_DETAIL_ID,PRODUCT_ID,QUANTITY,PRICE_ID,DISCOUNT_PERCENTAGE,DISCOUNT_AMOUNT,IS_FREE_ITEM,
//			PROMOTION_CODE,AMOUNT,REASON_CODE,SALE_ORDER_ID,PRICE,CREATE_USER,UPDATE_USER,CREATE_DATE,UPDATE_DATE,
//			ORDER_DATE,MAX_QUANTITY_FREE) VALUES()
			
			orderDetailJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderDetailJson.put(IntentConstants.INTENT_TABLE_NAME, SALES_ORDER_DETAIL_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALE_ORDER_DETAIL_ID,salesOrderDetailId, null));
//			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALES_ORDER_DETAIL_ID,"SALES_ORDER_DETAIL_SEQ", DATA_TYPE.SEQUENCE.toString()));
			
			if(productId > 0) {
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRODUCT_ID, productId, null));
			} else {
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRODUCT_ID,"", DATA_TYPE.NULL.toString()));
			}
			
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.QUANTITY,quantity, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.MAX_QUANTITY_FREE,maxQuantityFree, null));
			// thong tin khuyen mai
			if (discountPercentage > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.DISCOUNT_PERCENT,discountPercentage, null));
			}
			if (discountAmount > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.DISCOUNT_AMOUNT,discountAmount, null));
			}
			if (maxAmountFree > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.MAX_AMOUNT_FREE,maxAmountFree, null));
			}
			
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.IS_FREE_ITEM,isFreeItem, null));
			if (!StringUtil.isNullOrEmpty(programeCode)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PROGRAM_CODE,programeCode, null));
			}
			// them vao programeType neu la mat hang khuyen mai
			if (isFreeItem == 1 && programeType >= 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PROGRAM_TYPE,programeType, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.AMOUNT,amount, null));
			// reasonCode -- chua dung nen ko can insert
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALE_ORDER_ID,salesOrderId, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRICE,price, null));
			if (priceId > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRICE_ID,priceId, null));
			}
			if (!StringUtil.isNullOrEmpty(createUser)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.CREATE_USER,createUser, null));
			}
			if (!StringUtil.isNullOrEmpty(updateUser)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.UPDATE_USER,updateUser, null));
			}
			if (shopId > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SHOP_ID,shopId, null));
			}
			if (staffId > 0){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.STAFF_ID,staffId, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.CREATE_DATE,createDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.ORDER_DATE,orderDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.UPDATE_DATE,updateDate, null));
			
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PRICE_NOT_VAT,priceNotVat, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.TOTAL_WEIGHT,totalWeight, null));
			detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.VAT,vat, null));
			//Loai KM
			if (!StringUtil.isNullOrEmpty(programeTypeCode)){
				detailPara.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.PROGRAME_TYPE_CODE, programeTypeCode, null));
			}
			
			// max quantity free
			orderDetailJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			// sqlInsertOrder.put(IntentConstants.INTENT_LIST_WHERE_PARAM, value);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderDetailJson;
	}
	
	/**
	 * 
	*  Genereate delete sale order detail
	*  @author: Nguyen Thanh Dung
	*  @return
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateDeleteOrderSql(long saleOrderId) {
		JSONObject orderJson = new JSONObject();
		try{			
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, SALES_ORDER_DETAIL_TABLE.TABLE_NAME);
			
			// ds where params --> insert khong co menh de where
			JSONArray whereParams = new JSONArray();
			whereParams.put(GlobalUtil.getJsonColumn(SALES_ORDER_DETAIL_TABLE.SALE_ORDER_ID, String.valueOf(saleOrderId), null));
			orderJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, whereParams);
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
}
