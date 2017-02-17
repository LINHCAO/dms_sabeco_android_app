/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.sqllite.db.STOCK_TOTAL_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin ton kho
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class StockTotalDTO extends AbstractTableDTO {
	// loai doi tuong: 1 doi tuong trong SHOP, 2 doi tuong trong STAFF, 3 CUSTOMER
	public static final int TYPE_SHOP = 1;
	public static final int TYPE_VANSALE = 2;
	public static final int TYPE_CUSTOMER = 3;
	
	private static final long serialVersionUID = 1L;
	// id ton kho
	public long stockTotalId;
	// id doi tuong ton kho
	public long objectId;
	// loai doi tuong: 1 doi tuong trong SHOP, 2 doi tuong trong STAFF, 3 doi
	// tuong CUSTOMER
	public int objectType;
	// id san pham
	public int productId;
	// mo ta
	public String descr;
	// so luong ton kho
	public int quantity;
	// so luong co the dat hang
	public int availableQuantity;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String updateDate;

	public StockTotalDTO() {
		super(TableType.STOCK_TOTAL_TABLE);
	}
	
	/**
	 * Tao doi tuong ton kho
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @param orderDetailDTO
	 * @return
	 * @return: StockTotalDTO
	 * @throws:
	 */

	public static StockTotalDTO createStockTotalInfo(OrderViewDTO dto, SaleOrderDetailDTO orderDetailDTO) {
		long shopId = dto.orderInfo.shopId;
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;

		int objectType = 1;
		long objectId = 0;

		StockTotalDTO stockTotal = new StockTotalDTO();

		if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_PRESALE)) {// presale
			objectType = StockTotalDTO.TYPE_SHOP;
			objectId = shopId;
		} else if (dto.orderInfo.orderType.equals(SALE_ORDER_TABLE.ORDER_TYPE_VANSALE)) {// vansale
			objectType = StockTotalDTO.TYPE_VANSALE;
			objectId = staffId;
		} else {
			objectType = StockTotalDTO.TYPE_CUSTOMER;
		}
		stockTotal.objectId = objectId;
		stockTotal.objectType = objectType;

		// stockTotal.objectType = 1;
		stockTotal.availableQuantity = orderDetailDTO.quantity;
		stockTotal.quantity = orderDetailDTO.quantity;
		stockTotal.productId = orderDetailDTO.productId;
		return stockTotal;
	}

	/**
	 * Generate cau lenh update
	 * 
	 * @author: TruongHN
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			// UPDATE STOCK_TOTAL SET STOCK_TOTAL.QUANTITY =
			// STOCK_TOTAL.QUANTITY - :11,STOCK_TOTAL.AVAILABLE_QUANTITY =
			// STOCK_TOTAL.AVAILABLE_QUANTITY -
			// :12 WHERE STOCK_TOTAL.OWNER_ID =:2 and STOCK_TOTAL.OWNER_TYPE =:
			// 3 and STOCK_TOTAL.PRODUCT_ID =: 4
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STOCK_TOTAL_TABLE.TABLE_STOCK_TOTAL);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.QUANTITY, "*-" + this.quantity,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY, "*-" + this.quantity,
					DATA_TYPE.OPERATION.toString()));
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_ID, this.objectId, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_TYPE, this.objectType, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.PRODUCT_ID, this.productId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * 
	 * Giam available_quantity cua stock total Presale
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateDescreaseSqlPresale() {
		String staffCode = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		String dateNow = DateUtils.now();
		JSONObject json = new JSONObject();
		try {
			// UPDATE STOCK_TOTAL SET STOCK_TOTAL.QUANTITY =
			// STOCK_TOTAL.QUANTITY - :11,STOCK_TOTAL.AVAILABLE_QUANTITY =
			// STOCK_TOTAL.AVAILABLE_QUANTITY -
			// :12 WHERE STOCK_TOTAL.OWNER_ID =:2 and STOCK_TOTAL.OWNER_TYPE =:
			// 3 and STOCK_TOTAL.PRODUCT_ID =: 4
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STOCK_TOTAL_TABLE.TABLE_STOCK_TOTAL);

			// ds params
			JSONArray params = new JSONArray();
//			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.QUANTITY, "*-" + this.quantity,
//					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY, "*-" + this.availableQuantity,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.UPDATE_USER, staffCode, null));  
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.UPDATE_DATE, dateNow, null));   
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_ID, this.objectId, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_TYPE, this.objectType, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.PRODUCT_ID, this.productId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}
	
	/**
	 * 
	 * Giam quantity & available_quantity cua stock total Vansale
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateDescreaseSqlVansale() { 
		JSONObject json = new JSONObject();
		try {
			// UPDATE STOCK_TOTAL SET STOCK_TOTAL.QUANTITY =
			// STOCK_TOTAL.QUANTITY - :11,STOCK_TOTAL.AVAILABLE_QUANTITY =
			// STOCK_TOTAL.AVAILABLE_QUANTITY -
			// :12 WHERE STOCK_TOTAL.OWNER_ID =:2 and STOCK_TOTAL.OWNER_TYPE =:
			// 3 and STOCK_TOTAL.PRODUCT_ID =: 4
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STOCK_TOTAL_TABLE.TABLE_STOCK_TOTAL);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.QUANTITY, "*-" + this.quantity,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY, "*-" + this.availableQuantity,
					DATA_TYPE.OPERATION.toString()));
			json.put(IntentConstants.INTENT_LIST_PARAM, params);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_ID, this.objectId, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_TYPE, this.objectType, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.PRODUCT_ID, this.productId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
			
		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * 
	 * Tang quantity cua stock total
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateIncreaseSql() {
		JSONObject json = new JSONObject();
		try {
			// UPDATE STOCK_TOTAL SET STOCK_TOTAL.QUANTITY =
			// STOCK_TOTAL.QUANTITY - :11,STOCK_TOTAL.AVAILABLE_QUANTITY =
			// STOCK_TOTAL.AVAILABLE_QUANTITY -
			// :12 WHERE STOCK_TOTAL.OWNER_ID =:2 and STOCK_TOTAL.OWNER_TYPE =:
			// 3 and STOCK_TOTAL.PRODUCT_ID =: 4
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STOCK_TOTAL_TABLE.TABLE_STOCK_TOTAL);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.QUANTITY, "*+" + this.quantity,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY, "*+" + this.availableQuantity,
					DATA_TYPE.OPERATION.toString()));
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_ID, this.objectId, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_TYPE, this.objectType, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.PRODUCT_ID, this.productId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * Gen cau insert or update stock total
	 * @author: quangvt1
	 * @since: 17:42:11 03-06-2014
	 * @return: JSONObject
	 * @throws:  
	 * @return
	 */
	public JSONObject generateInsertOrUpdateSqlPresale() { 
		String staffCode = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		String dateNow = DateUtils.now();
		
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, STOCK_TOTAL_TABLE.TABLE_STOCK_TOTAL);

			// ds params
			JSONArray params = new JSONArray();
			// insert
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.STOCK_TOTAL_ID,"STOCK_TOTAL_SEQ", DATA_TYPE.SEQUENCE.toString()));
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.OBJECT_ID, objectId, null));
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.PRODUCT_ID, productId, null));
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.QUANTITY, 0, null)); 
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.CREATE_USER, staffCode, null)); 
			params.put(GlobalUtil.getJsonColumnWithKey(STOCK_TOTAL_TABLE.CREATE_DATE, dateNow, null));  
			// update 
			params.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.AVAILABLE_QUANTITY, "*-" + this.availableQuantity,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumnIgnoreInsert(STOCK_TOTAL_TABLE.UPDATE_USER, staffCode, null));  
			params.put(GlobalUtil.getJsonColumnIgnoreInsert(STOCK_TOTAL_TABLE.UPDATE_DATE, dateNow, null));   
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_ID, objectId, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.OBJECT_TYPE, objectType, null));
			wheres.put(GlobalUtil.getJsonColumn(STOCK_TOTAL_TABLE.PRODUCT_ID, this.productId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}
}
