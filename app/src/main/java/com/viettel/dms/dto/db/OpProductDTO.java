/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.Date;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.AP_PARAM_TABLE;
import com.viettel.dms.sqllite.db.OP_PRICE_TABLE;
import com.viettel.dms.sqllite.db.OP_PRODUCT_TABLE;
import com.viettel.dms.sqllite.db.OP_SALE_VOLUME_TABLE;
import com.viettel.dms.sqllite.db.OP_STOCK_TOTAL_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * @author dungdq3
 * @version: 1.0
 * @since: 1.0
 */
public class OpProductDTO extends AbstractTableDTO {

	private static final long serialVersionUID = 2482091137472427680L;
	
	// id doi thu
	private long opID;
	public long getOpID() {
		return opID;
	}
	public void setOpID(long opID) {
		this.opID = opID;
	}
	
	// id san pham doi thu
	private long opProductID;
	public long getOpProductID() {
		return opProductID;
	}
	public void setOpProductID(long opProductID) {
		this.opProductID = opProductID;
	}
	
	// quy cach
	private String convfact;
	public String getConvfact() {
		return convfact;
	}
	public void setConvfact(String convfact) {
		this.convfact = convfact;
	}
	
	// ma san pham doi thu
	private String productCode;
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	
	// ten san pham doi thu
	private String productName;
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	private String UOM1;
	public String getUOM1() {
		return UOM1;
	}
	public void setUOM1(String uOM1) {
		UOM1 = uOM1;
	}
	
	private String UOM2;
	public String getUOM2() {
		return UOM2;
	}
	public void setUOM2(String uOM2) {
		UOM2 = uOM2;
	}
	
	// so luong
	private long quantity;
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	
	// so luong
	private long oldQuantity; 
	public long getOldQuantity() {
		return oldQuantity;
	}
	public void setOldQuantity(long quantity) {
		this.oldQuantity = quantity;
	}

	// gia
	private long price;
	public long getPrice() {
		return price;
	}
	public void setPrice(long price) {
		this.price = price;
	}

	// so luong
	private long oldPrice;
	public long getOldPrice() {
		return oldPrice;
	}
	public void setOldPrice(long oldPrice) {
		this.oldPrice = oldPrice;
	}
	
	// op_sale_volume_id
	private long op_sale_volume_id; 
	public long getOp_sale_volume_id() {
		return op_sale_volume_id;
	}
	public void setOp_sale_volume_id(long op_sale_volume_id) {
		this.op_sale_volume_id = op_sale_volume_id;
	}

	// op_price_id
	private long op_price_id;
	public long getOp_price_id() {
		return op_price_id;
	}
	public void setOp_price_id(long op_price_id) {
		this.op_price_id = op_price_id;
	}

	// so luong luu tam
	private long newQuantity;
	public long getNewQuantity() {
		return newQuantity;
	}
	public void setNewQuantity(long temp) {
		this.newQuantity = temp;
	}

	// gia luu tam
	private long newPrice;
	public long getNewPrice() {
		return newPrice;
	}
	public void setNewPrice(long temp) {
		this.newPrice = temp;
	}
	
	public OpProductDTO(){
		quantity=-1;
	}
	
	//id key
	private long maxID;
	public long getMaxID() {
		return maxID;
	}
	public void setMaxID(long maxID) {
		this.maxID = maxID;
	}
	
	// id key
	private boolean isInserted;  
	public boolean isInserted() {
		return isInserted;
	}
	public void setInserted(boolean isInserted) {
		this.isInserted = isInserted;
	}

	private boolean isInsertedPrice;
	public boolean isInsertedPrice() {
		return isInsertedPrice;
	}
	public void setInsertedPrice(boolean isInsertedPrice) {
		this.isInsertedPrice = isInsertedPrice;
	}
	
	// nguoi cap nhat
	public String updateUser;
	// ngay cap nhat
	public String updateDate;
	// xem du lieu duoc thu thap tu thu thap du lieu thi truong hay la thu thap tu chuong trinh HTBH
	public int actionType;
	
	public long staffId;
	public long customerId; 
	
	/**
	* Doc danh sach san pham tu cursor
	* @author: dungdq3
	* @param: Cursor cursor
	* @return: void
	*/
	public void initFromCursor(Cursor cursor) {
		// TODO Auto-generated method stub
		if (cursor.getColumnIndex("OPPID") >= 0) {
			opID= cursor.getLong(cursor.getColumnIndex("OPPID"));
		} else {
			opID = 0;
		}
		if (cursor.getColumnIndex(OP_PRODUCT_TABLE.OP_PRODUCT_ID) >= 0) {
			opProductID = cursor.getLong(cursor.getColumnIndex(OP_PRODUCT_TABLE.OP_PRODUCT_ID));
		} else {
			opProductID = 0;
		}
		if (cursor.getColumnIndex(OP_PRODUCT_TABLE.PRODUCT_CODE) >= 0) {
			productCode = cursor.getString(cursor
					.getColumnIndex(OP_PRODUCT_TABLE.PRODUCT_CODE));
		} else {
			productCode = "";
		}
		if (cursor.getColumnIndex(OP_PRODUCT_TABLE.PRODUCT_NAME) >= 0) {
			productName = cursor.getString(cursor
					.getColumnIndex(OP_PRODUCT_TABLE.PRODUCT_NAME));
		} else {
			productName = "";
		}
		if (cursor.getColumnIndex(OP_PRODUCT_TABLE.CONVFACT) >= 0) {
			convfact = cursor.getString(cursor
					.getColumnIndex(OP_PRODUCT_TABLE.CONVFACT));
		} else {
			convfact = "";
		}
		if (cursor.getColumnIndex(OP_PRODUCT_TABLE.UOM1) >= 0) {
			UOM1 = (cursor.getString(cursor.getColumnIndex(OP_PRODUCT_TABLE.UOM1)));
		} else {
			UOM1 = "";
		}
		if (cursor.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_NAME) >= 0) {
			UOM2 = (cursor.getString(cursor.getColumnIndex(AP_PARAM_TABLE.AP_PARAM_NAME)));
		} else {
			UOM2 = "";
		}
		if (cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID) >= 0) {
			op_sale_volume_id = (cursor.getLong(cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID)));
		} else {
			op_sale_volume_id = 0;
		}
		if (cursor.getColumnIndex(OP_PRICE_TABLE.OP_PRICE_ID) >= 0) {
			op_price_id = (cursor.getLong(cursor.getColumnIndex(OP_PRICE_TABLE.OP_PRICE_ID)));
		} else {
			op_price_id = 0;
		}
		if (cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.ACTION_TYPE) >= 0) {
			actionType = (cursor.getInt(cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.ACTION_TYPE)));
		} else {
			actionType = 0;
		}
		
		if (cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.STAFF_ID) >= 0) {
			staffId = (cursor.getLong(cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.STAFF_ID)));
		} else {
			staffId = 0;
		}
		if (cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.UPDATE_USER) >= 0
				&& !StringUtil.isNullOrEmpty(cursor.getString(cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.UPDATE_USER)))) {
			updateUser = (cursor.getString(cursor.getColumnIndex(OP_SALE_VOLUME_TABLE.UPDATE_USER)));
		} else {
			updateUser = "";
		}
		if (cursor.getColumnIndex("QUANTITY") >= 0) {
			String quan = (cursor.getString(cursor.getColumnIndex("QUANTITY")));
			if(null == quan || "null".equals(quan)){
				this.oldQuantity = 0;
				this.setInserted(false);
			}else{
				long quantity = Long.parseLong(quan);
				if(quantity > 0){
					this.setOldQuantity(quantity);
					this.setInserted(true);
				}else{
					this.setOldQuantity(0);
					this.setInserted(false);
				}
				 
			}
		} else {
			this.setInserted(false);
			oldQuantity = 0;
		}

		if (cursor.getColumnIndex("PRICE") >= 0) {
			String priceStr = (cursor.getString(cursor.getColumnIndex("PRICE")));
			if(null == priceStr || "null".equals(priceStr)){
				this.oldPrice = 0;
				this.setInsertedPrice(false);
			}else{
				long price = Long.parseLong(priceStr);
				if(price > 0){
					this.setOldPrice(price);
					this.setInsertedPrice(true);
				}else{
					this.setOldPrice(0);
					this.setInsertedPrice(false);
				}

			}
		} else {
			this.setInsertedPrice(false);
			oldPrice = 0;
		}
	} 
	
	/**
	 * 
	 * @author: quangvt1
	 * @since: 14:50:32 07-05-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param staffID
	 * @param cusID
	 * @param type
	 * @param staffCode
	 * @param  : 0 - thang truoc, 1 - thang hien tai
	 * @return
	 */
	public JSONObject generateListProductCompetitor(long staffID, long cusID, int type, String staffCode, String date) {
		int typeRequest = getTypeRequestDB();
		
		// Insert
		if(typeRequest == 1){
			return generateInsertOrUpdateJson(staffID, cusID, type, staffCode, date);
		}
		// Update
		else if(typeRequest == 2){
			return generateUpdate(cusID, type, staffCode, date);
		}
		// Delete
		if(typeRequest == 3){
			return generateDelete(cusID, type, staffCode, date);
		}
		
		return null; 
	}

	/**
	 *
	 * @author: quangvt1
	 * @since: 14:50:32 07-05-2014
	 * @return: JSONObject
	 * @throws:
	 * @param staffID
	 * @param cusID
	 * @param type
	 * @param staffCode
	 * @param  : 0 - thang truoc, 1 - thang hien tai
	 * @return
	 */
	public JSONObject generateListProductPriceCompetitor(long staffID, long cusID, int type, String staffCode, String date) {
		int typeRequest = getTypeRequestPriceDB();

		// Insert
		if(typeRequest == 1){
			return generateInsertOrUpdatePriceJson(staffID, cusID, type, staffCode, date);
		}
		// Update
		else if(typeRequest == 2){
			return generateUpdatePrice(cusID, type, staffCode, date);
		}
		// Delete
		if(typeRequest == 3){
			return generateDeletePrice(cusID, type, staffCode, date);
		}

		return null;
	}
	
	private JSONObject generateDelete(long cusID, int type, String staffCode, String strDate) {
		Date date = DateUtils.parseDateFromString(strDate, DateUtils.DATE_FORMAT_NOW);
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, cusID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, opProductID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.TYPE, type, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<")); 
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}

	private JSONObject generateDeletePrice(long cusID, int type, String staffCode, String strDate) {
		Date date = DateUtils.parseDateFromString(strDate, DateUtils.DATE_FORMAT_NOW);
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, OP_PRICE_TABLE.OP_PRICE_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.CUSTOMER_ID, cusID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.OP_PRODUCT_ID, opProductID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.TYPE, type, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_PRICE_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_PRICE_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<"));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}
	
	private JSONObject generateUpdate(long cusID, int type, String staffCode, String strDate) { 
		Date date = DateUtils.parseDateFromString(strDate, DateUtils.DATE_FORMAT_NOW);
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray(); 
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.QUANTITY, newQuantity, null)); 
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.UPDATE_DATE, DateUtils.now(), null)); 
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.UPDATE_USER, staffCode, null)); 
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, cusID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, opProductID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.TYPE, type, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<")); 
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson; 
	}

	private JSONObject generateUpdatePrice(long cusID, int type, String staffCode, String strDate) {
		Date date = DateUtils.parseDateFromString(strDate, DateUtils.DATE_FORMAT_NOW);
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, OP_PRICE_TABLE.OP_PRICE_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.PRICE, newPrice, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.UPDATE_USER, staffCode, null));
			actionJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.CUSTOMER_ID, cusID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.OP_PRODUCT_ID, opProductID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.TYPE, type, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_PRICE_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_PRICE_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<"));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}

	/**
	 * Tao cau insert gui len server
	 * @author: quangvt1
	 * @since: 14:31:02 07-05-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param staffID
	 * @param cusID
	 * @param type : 1 - BSG, 0- doi thu
	 * @param staffCode
	 * @return
	 */
	private JSONObject generateInsertOrUpdateJson(long staffID, long cusID, int type, String staffCode, String strDate) {
		Date date = DateUtils.parseDateFromString(strDate, DateUtils.DATE_FORMAT_NOW);
		JSONObject listProductCompetitorJson = new JSONObject();
		JSONArray detailPara = new JSONArray();
		try {
			// Insert
			listProductCompetitorJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE); 
			listProductCompetitorJson.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
			
			// ds params
			// Luc update thi khong update cac dong nay, chi thuc hien luc insert
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.SALE_DATE, strDate, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID, maxID, null)); 
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.OPPONENT_ID, opID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, opProductID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.STAFF_ID, staffID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.STATUS, 1, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, cusID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_STOCK_TOTAL_TABLE.CREATE_USER, staffCode, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.TYPE, type, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.ACTION_TYPE, 0, null)); // 0 : du lieu thi truong, 1 :HTBH
			
			// Insert hoac update deu cap nhat cac truong sau
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.QUANTITY, quantity, null));
			
			// Chi Update moi cap nhat cac dong
			detailPara.put(GlobalUtil.getJsonColumnIgnoreInsert(OP_SALE_VOLUME_TABLE.UPDATE_DATE, DateUtils.now(), null)); 
			detailPara.put(GlobalUtil.getJsonColumnIgnoreInsert(OP_SALE_VOLUME_TABLE.UPDATE_USER, staffCode, null)); 
			
			listProductCompetitorJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, cusID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, opProductID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.TYPE, type, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<")); 
			listProductCompetitorJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		
		return listProductCompetitorJson;
	}

	/**
	 * Tao cau insert gui len server
	 * @author: quangvt1
	 * @since: 14:31:02 07-05-2014
	 * @return: JSONObject
	 * @throws:
	 * @param staffID
	 * @param cusID
	 * @param type : 1 - BSG, 0- doi thu
	 * @param staffCode
	 * @return
	 */
	private JSONObject generateInsertOrUpdatePriceJson(long staffID, long cusID, int type, String staffCode, String strDate) {
		Date date = DateUtils.parseDateFromString(strDate, DateUtils.DATE_FORMAT_NOW);
		JSONObject listProductCompetitorJson = new JSONObject();
		JSONArray detailPara = new JSONArray();
		try {
			// Insert
			listProductCompetitorJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			listProductCompetitorJson.put(IntentConstants.INTENT_TABLE_NAME, OP_PRICE_TABLE.OP_PRICE_TABLE);

			// ds params
			// Luc update thi khong update cac dong nay, chi thuc hien luc insert
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.SALE_DATE, strDate, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.OP_PRICE_ID, maxID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.OPPONENT_ID, opID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.OP_PRODUCT_ID, opProductID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.STAFF_ID, staffID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.STATUS, 1, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.CUSTOMER_ID, cusID, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.CREATE_USER, staffCode, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.TYPE, type, null));
			detailPara.put(GlobalUtil.getJsonColumnWithKey(OP_PRICE_TABLE.ACTION_TYPE, 0, null)); // 0 : du lieu thi truong, 1 :HTBH

			// Insert hoac update deu cap nhat cac truong sau
			detailPara.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.PRICE, price, null));

			// Chi Update moi cap nhat cac dong
			detailPara.put(GlobalUtil.getJsonColumnIgnoreInsert(OP_PRICE_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumnIgnoreInsert(OP_PRICE_TABLE.UPDATE_USER, staffCode, null));

			listProductCompetitorJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.CUSTOMER_ID, cusID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.OP_PRODUCT_ID, opProductID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_PRICE_TABLE.TYPE, type, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_PRICE_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_PRICE_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<"));
			listProductCompetitorJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return listProductCompetitorJson;
	}
	
	/**
	 * Tra ve dang request db
	 * 0 : khong lam gi ca
	 * 1 : insert
	 * 2 : update
	 * 3 : delete
	 * @author: quangvt1
	 * @since: 14:22:03 07-05-2014
	 * @return: int
	 * @throws:  
	 * @return
	 */
	public int getTypeRequestDB() {
		int type = 0;
		
		// Truong hop insert
		if(isInserted == false && newQuantity > 0){
			type = 1;
		} 
		// Truong hop update
		else if(isInserted == true && newQuantity != oldQuantity && newQuantity != 0){
			type = 2;
		}
		// Truong hop delete
		else if(isInserted == true && newQuantity == 0 && oldQuantity > 0){
			type = 3;
		}
		 
		return type;
	}

	/**
	 * Tra ve dang request db
	 * 0 : khong lam gi ca
	 * 1 : insert
	 * 2 : update
	 * 3 : delete
	 * @author: quangvt1
	 * @since: 14:22:03 07-05-2014
	 * @return: int
	 * @throws:
	 * @return
	 */
	public int getTypeRequestPriceDB() {
		int type = 0;

		// Truong hop insert
		if(isInsertedPrice == false && newPrice > 0){
			type = 1;
		}
		// Truong hop update
		else if(isInsertedPrice == true && newPrice != oldPrice && newPrice != 0){
			type = 2;
		}
		// Truong hop delete
		else if(isInsertedPrice == true && newPrice == 0 && oldPrice > 0){
			type = 3;
		}

		return type;
	}
	
	/**
	* Tao chuoi JSON gui len server
	* @author: dungdq3
	* @param cusID 
	* @param staffID 
	* @return: JSONObject
	*/
	public JSONObject generateListProductCompetitor(long staffID, long cusID, int type, String staffCode) {
		// TODO Auto-generated method stub
		JSONObject listProductCompetitorJson = new JSONObject();
		JSONArray detailPara = new JSONArray();
		try {
			listProductCompetitorJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			if(type==0){
				listProductCompetitorJson.put(IntentConstants.INTENT_TABLE_NAME, OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_TABLE);
				detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.INVENTORY_DATE, DateUtils.now(), null));
				detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_ID, maxID, null));
			}else{
				listProductCompetitorJson.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
				detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.now(), null));
				detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID, maxID, null));
			}
			// ds params
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.OPPONENT_ID, opID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.OP_PRODUCT_ID, opProductID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.QUANTITY, quantity, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.STAFF_ID, staffID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.STATUS, 1, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.CUSTOMER_ID, cusID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.CREATE_USER, staffCode, null));
			listProductCompetitorJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return listProductCompetitorJson;
	}
	
	/**
	* Tao chuoi JSON gui len server
	* @author: dungdq3
	* @param cusID 
	* @param staffID 
	* @return: JSONObject
	*/
	public JSONObject generateListProductCompetitorPG(long staffID, long cusID, int typeTable, int type, String staffCode) {
		// TODO Auto-generated method stub
		JSONObject listProductCompetitorJson = new JSONObject();
		JSONArray detailPara = new JSONArray();
		try {
			listProductCompetitorJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			if(typeTable==0){
				listProductCompetitorJson.put(IntentConstants.INTENT_TABLE_NAME, OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_TABLE);
				detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.INVENTORY_DATE, DateUtils.now(), null));
				detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.OP_STOCK_TOTAL_ID, maxID, null));
			}else{
				listProductCompetitorJson.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
				detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.now(), null));
				detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID, maxID, null));
				detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.TYPE, type, null));
			}
			// ds params
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.OPPONENT_ID, opID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.OP_PRODUCT_ID, opProductID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.QUANTITY, quantity, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.STAFF_ID, staffID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.STATUS, 1, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.CUSTOMER_ID, cusID, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_STOCK_TOTAL_TABLE.CREATE_USER, staffCode, null));
			listProductCompetitorJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return listProductCompetitorJson;
	}
}
