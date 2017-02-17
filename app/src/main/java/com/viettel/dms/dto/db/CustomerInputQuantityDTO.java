/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.OP_SALE_VOLUME_TABLE;
import com.viettel.dms.sqllite.db.OP_STOCK_TOTAL_TABLE;
import com.viettel.dms.sqllite.db.PRO_CUS_PROCESS_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;
  
/**
 * Thong tin nhap san luong ban cua khach hang
 * CustomerInputQuantity.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:15:42 13-06-2014
 */
@SuppressWarnings("serial")
public class CustomerInputQuantityDTO extends AbstractTableDTO{ 
	// id khach hang
	public long OBJECT_ID;
	// id
	public long PRO_CUS_PROCESS_ID;
	// id chuong trinh
	public long PRO_INFO_ID;
	// id pro cus map
	public long PRO_CUS_MAP_ID;
	// id chu trinh
	public long PRO_CUS_PERIOD;
	// ma khach hang
	public String CUSTOMER_CODE;
	// ten khach hang
	public String CUSTOMER_NAME;
	// Dia chi khach hang
	public String CUSTOMER_ADDRESS;
	// So nha cua khach hang
	public String CUSTOMER_HOUSENUMBER;
	// Dia chi duong cua Khach hang
	public String CUSTOMER_STREET;
	// Quan
	public String CUSTOMER_AREA;
	// Muc tham gia
	public int JOIN_LEVEL;
	// Muc tham gia
	public String LEVEL_NAME;
	// Nguoi cap nhat
	public boolean ALLOW_EDIT;
	// Thoi gian ban dau chu ki
	public String PERIOD_FROM_DATE;
	// Thoi gian ket thuc chu ki
	public String PERIOD_TO_DATE;
	// San luong mua tu NPP
	public long QUANTITY_SHOP;
	// San luong cap nhat tu SMS PG
	public long QUANTITY_PG;
	// San luong thuc hien
	public long QUANTITY;
	// Nguoi cap nhat
	public String UPDATE_USER;
	
	// Danh sach thong tin chi tiet
	public List<CustomerInputQuantityDetailDTO> details;
	public int totalProduct;
	// Lay so lieu mua tu 1 - npp, 2 - pg
	public int type = 0;
	// Da tung nhap san luong ban chua
	public boolean hasInput = false;
	// Chu ki co tu ngay bat dau den ket thuc cua thang khong
	public boolean isFullMonth = false;
	// Danh sach id cua op_sale_volume
	public List<OpProductDTO> listOpSale;
	// Danh sach cac san pham cung chu ki can cap nhat
	public CustomerInputQuantityDTO cusSamePeriod;
	
	public CustomerInputQuantityDTO(){ 
		details = new ArrayList<CustomerInputQuantityDetailDTO>();
	}

	/**
	 * Tao chuoi json update so luong ban gui len server
	 * @author: quangvt1
	 * @since: 15:30:15 19-06-2014
	 * @return: JSONArray
	 * @throws:  
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	public JSONArray getJsonUpdate(String staffId, String staffCode) {
		JSONArray jarray = new JSONArray();
		for (CustomerInputQuantityDetailDTO detail : details) {
			JSONObject jUpdate = getJonUpdateDetail(detail, staffId, staffCode);
			jarray.put(jUpdate);
			
			if(isFullMonth){
				if(isHaveOpSaleVolume(detail.OBJECT_ID, detail.PRODUCT_ID)){
					JSONObject jUpdateOp = genJsonUpdateOpVolumeFromHTBH(detail, staffId, staffCode);
					jarray.put(jUpdateOp);
				}else{
					JSONObject jInserOrUpdate = genJsonInserOrUpdateOpVolumeFromHTBH(detail, staffId, staffCode);
					jarray.put(jInserOrUpdate);
				}
				
			}
			
		}
		if(cusSamePeriod != null && cusSamePeriod.details.size() > 0){
			for (CustomerInputQuantityDetailDTO detail : cusSamePeriod.details) {
				JSONObject jUpdateSamePeriod = getJonUpdateDetail(detail, staffId, staffCode);
				jarray.put(jUpdateSamePeriod); 
			}
		}
		
		return jarray;
	} 
	
	/**
	 * Kiem tra op_sale_volume ton tai chua
	 * @author: quangvt1
	 * @since: 15:55:56 20-06-2014
	 * @return: boolean
	 * @throws:  
	 * @param customerId
	 * @param productId
	 * @return
	 */
	public boolean isHaveOpSaleVolume(long customerId, long productId){
		 return (getOpSale(customerId, productId) != null);
	}
	
	/**
	 * Tra ve thong tin op_sale_volume trong thang dua vao id khach hang, id san pham
	 * @author: quangvt1
	 * @since: 15:56:22 20-06-2014
	 * @return: OpProductDTO
	 * @throws:  
	 * @param customerId
	 * @param productId
	 * @return
	 */
	public OpProductDTO getOpSale(long customerId, long productId){
		OpProductDTO dto = null;
		for (int i = 0; i < listOpSale.size(); i++) {
			dto = listOpSale.get(i);
			if(dto.getOpProductID() == productId && dto.customerId == customerId){
				 return dto;
			}
		}
		
		return null;
	}
	
	/**
	 * Gen cau json insert or update vao bang Op_Sale_Volume
	 * co Action_type = 1 : du lieu tu CH HTBH
	 * @author: quangvt1
	 * @since: 15:57:13 20-06-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param detail
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	private JSONObject genJsonInserOrUpdateOpVolumeFromHTBH(CustomerInputQuantityDetailDTO detail, String staffId, String staffCode) { 
		JSONObject jsonInsert = new JSONObject();
		Date date = DateUtils.parseDateFromString(detail.PERIOD_FROM_DATE, DateUtils.DATE_FORMAT_NOW);
		try {
			// Insert
			jsonInsert.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE); 
			jsonInsert.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
			
			// ds params
			JSONArray jsonDetail = new JSONArray();
			// Khi insert thi insert cac truong sau
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.SALE_DATE, detail.PERIOD_FROM_DATE, null));
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_ID, detail.opId, null));  
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, detail.PRODUCT_ID, null));
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.STAFF_ID, staffId, null));
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.STATUS, 1, null));
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, detail.OBJECT_ID, null));
			jsonDetail.put(GlobalUtil.getJsonColumnWithKey(OP_STOCK_TOTAL_TABLE.CREATE_USER, staffCode, null));
			// Update hay insert deu thuc hien
			jsonDetail.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.QUANTITY, detail.NEWQUANTITY, null));
			jsonDetail.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.TYPE, 1, null)); // 1:BSG
			jsonDetail.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.ACTION_TYPE, 1, null)); // 1 ; HTBH
			// Chi thuc hien khi update
			jsonDetail.put(GlobalUtil.getJsonColumnIgnoreInsert(OP_SALE_VOLUME_TABLE.UPDATE_DATE, DateUtils.now(), null));
			jsonDetail.put(GlobalUtil.getJsonColumnIgnoreInsert(OP_SALE_VOLUME_TABLE.UPDATE_USER, staffCode, null));
			
			jsonInsert.put(IntentConstants.INTENT_LIST_PARAM, jsonDetail);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, detail.PRODUCT_ID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, detail.OBJECT_ID, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<"));
			jsonInsert.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonInsert;
	} 

	/**
	 * Gen cau json update vao bang Op_Sale_Volume
	 * co Action_type = 1 : du lieu tu CH HTBH
	 * @author: quangvt1
	 * @since: 15:58:00 20-06-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param detail
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	private JSONObject genJsonUpdateOpVolumeFromHTBH(CustomerInputQuantityDetailDTO detail, String staffId, String staffCode) {
		JSONObject jsonUpdate = new JSONObject(); 
		Date date = DateUtils.parseDateFromString(detail.PERIOD_FROM_DATE, DateUtils.DATE_FORMAT_NOW);
		try {
			// Update
			jsonUpdate.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE); 
			jsonUpdate.put(IntentConstants.INTENT_TABLE_NAME, OP_SALE_VOLUME_TABLE.OP_SALE_VOLUME_TABLE);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			// ...thêm thuộc tính		
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.QUANTITY, detail.NEWQUANTITY, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.UPDATE_USER, staffCode, null));
			detailPara.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.ACTION_TYPE, 1, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.OP_PRODUCT_ID, detail.PRODUCT_ID, null));
			wheres.put(GlobalUtil.getJsonColumn(OP_SALE_VOLUME_TABLE.CUSTOMER_ID, detail.OBJECT_ID, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getLastDayOfPreviousMonth(date), ">"));
			wheres.put(GlobalUtil.getJsonColumnWhere(OP_SALE_VOLUME_TABLE.SALE_DATE, DateUtils.getNextDay(date), "<"));
			jsonUpdate.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonUpdate; 
	}
	  

	/**
	 * Gen 1 cau json update cho 1 san pham
	 * @author: quangvt1
	 * @since: 15:30:34 19-06-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param detail
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	private JSONObject getJonUpdateDetail(CustomerInputQuantityDetailDTO detail,
			String staffId, String staffCode) { 
		JSONObject jsonUpdate = new JSONObject(); 
		try {
			// Update
			jsonUpdate.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE); 
			jsonUpdate.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_PROCESS_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray detailPara = new JSONArray();
			// ...thêm thuộc tính		
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_PROCESS_TABLE.QUANTITY, detail.NEWQUANTITY, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_PROCESS_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_PROCESS_TABLE.UPDATE_USER, staffCode, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_PROCESS_TABLE.PRO_CUS_PROCESS_ID, detail.PRO_CUS_PROCESS_ID, null));
			jsonUpdate.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog",
					VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		return jsonUpdate; 
	}
	
	
	/**
	 * parse du lieu danh sach khach hang thuc hien
	 * 
	 * @author: hoanpd1
	 * @since: 18:20:52 08-08-2014
	 * @return: void
	 * @throws:  
	 * @param c
	 */
	public void initFromCursor(Cursor c) {
		if (c.getColumnIndex("PRO_CUS_PROCESS_ID") >= 0) {
			PRO_CUS_PROCESS_ID = c.getLong(c.getColumnIndex("PRO_CUS_PROCESS_ID"));
		} else {
			PRO_CUS_PROCESS_ID = 0;
		}

		if (c.getColumnIndex("PRO_INFO_ID") >= 0) {
			PRO_INFO_ID = c.getLong(c.getColumnIndex("PRO_INFO_ID"));
		} else {
			PRO_INFO_ID = 0;
		}

		if (c.getColumnIndex("PRO_CUS_MAP_ID") >= 0) {
			PRO_CUS_MAP_ID = c.getLong(c.getColumnIndex("PRO_CUS_MAP_ID"));
		} else {
			PRO_CUS_MAP_ID = 0;
		}

		if (c.getColumnIndex("PRO_PERIOD_ID") >= 0) {
			PRO_CUS_PERIOD = c.getLong(c.getColumnIndex("PRO_PERIOD_ID"));
		} else {
			PRO_CUS_PERIOD = 0;
		}

		if (c.getColumnIndex("OBJECT_ID") >= 0) {
			OBJECT_ID = c.getLong(c.getColumnIndex("OBJECT_ID"));
		} else {
			OBJECT_ID = 0;
		}

		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			CUSTOMER_CODE = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
		} else {
			CUSTOMER_CODE = "";
		}

		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			CUSTOMER_NAME = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		} else {
			CUSTOMER_NAME = "";
		}

		if (c.getColumnIndex("CUSTOMER_ADDRESS") >= 0) {
			CUSTOMER_ADDRESS = c.getString(c.getColumnIndex("CUSTOMER_ADDRESS"));
		} else {
			CUSTOMER_ADDRESS = "";
		}

		if (c.getColumnIndex("JOIN_LEVEL") >= 0) {
			String level = c.getString(c.getColumnIndex("JOIN_LEVEL"));
			if (!StringUtil.isNullOrEmpty(level )) {
				JOIN_LEVEL = Integer.parseInt(level);
			}
		} else {
			JOIN_LEVEL = 0;
		}
		if (c.getColumnIndex("LEVEL_NAME") >= 0) {
			LEVEL_NAME = c.getString(c.getColumnIndex("LEVEL_NAME"));
		} else {
			LEVEL_NAME = "";
		}

		if (c.getColumnIndex("STREET") > -1) {
			CUSTOMER_STREET = c.getString(c.getColumnIndex("STREET"));

			if (StringUtil.isNullOrEmpty(CUSTOMER_STREET)) {
				CUSTOMER_STREET = "";
			}
		}

		if (c.getColumnIndex("HOUSENUMBER") > -1) {
			CUSTOMER_HOUSENUMBER = c.getString(c.getColumnIndex("HOUSENUMBER"));
			if (StringUtil.isNullOrEmpty(CUSTOMER_HOUSENUMBER)) {
				CUSTOMER_HOUSENUMBER = "";
			}
		}

		if (c.getColumnIndex("AREA_NAME") > -1) {
			CUSTOMER_AREA = c.getString(c.getColumnIndex("AREA_NAME"));

			if (StringUtil.isNullOrEmpty(CUSTOMER_AREA)) {
				CUSTOMER_AREA = "";
			}
		}
		if (c.getColumnIndex("ALLOW_EDIT") >= 0) {
			ALLOW_EDIT = Integer.parseInt(c.getString(c.getColumnIndex("ALLOW_EDIT"))) == 1; 
		} else {
			ALLOW_EDIT = false;
		} 

		// Dia chi hoan chinh
		// Dia chi dang : housenumber + " " + street + ", " + area
		CUSTOMER_ADDRESS = "";
		if (!StringUtil.isNullOrEmpty(CUSTOMER_HOUSENUMBER)) {
			CUSTOMER_ADDRESS += CUSTOMER_HOUSENUMBER;
		}

		if (!StringUtil.isNullOrEmpty(CUSTOMER_ADDRESS)) {
			CUSTOMER_ADDRESS += " ";
		}

		if (!StringUtil.isNullOrEmpty(CUSTOMER_STREET)) {
			CUSTOMER_ADDRESS += CUSTOMER_STREET;
		}

		if (!StringUtil.isNullOrEmpty(CUSTOMER_ADDRESS)) {
			CUSTOMER_ADDRESS += ", ";
		}

		if (!StringUtil.isNullOrEmpty(CUSTOMER_AREA)) {
			CUSTOMER_ADDRESS += CUSTOMER_AREA;
		}
		if (c.getColumnIndex("PERIOD_FROM_DATE") >= 0) {
			PERIOD_FROM_DATE = c.getString(c.getColumnIndex("PERIOD_FROM_DATE"));
		} else {
			PERIOD_FROM_DATE = "";
		}

		if (c.getColumnIndex("PERIOD_TO_DATE") >= 0) {
			PERIOD_TO_DATE = c.getString(c.getColumnIndex("PERIOD_TO_DATE"));
		} else {
			PERIOD_TO_DATE = "";
		}
		
		if (c.getColumnIndex("QUANTITY_SHOP") >= 0) {
			String quantity = c.getString(c.getColumnIndex("QUANTITY_SHOP"));
			if(!StringUtil.isNullOrEmpty(quantity) ){
				QUANTITY_SHOP = Long.parseLong(quantity);
			}
		} else {
			QUANTITY_SHOP = 0;
		} 

		if (c.getColumnIndex("QUANTITY_PG") >= 0) {
			String quantity = c.getString(c.getColumnIndex("QUANTITY_PG"));
			if(!StringUtil.isNullOrEmpty(quantity)){
				QUANTITY_PG = Long.parseLong(quantity);
			} 
		} else {
			QUANTITY_PG = 0;
		}
		 if(QUANTITY_PG > 0){
			 type =2;
		 }else {
			 type =1;
		}
		
		if (c.getColumnIndex("QUANTITY") >= 0) {
			String quantity = c.getString(c.getColumnIndex("QUANTITY"));
			if(!StringUtil.isNullOrEmpty(quantity)){
				QUANTITY = Long.parseLong(quantity);
			} 
		} else {
			QUANTITY = 0;
		}
		
		if (c.getColumnIndex("UPDATE_USER") >= 0) {
			UPDATE_USER = c.getString(c.getColumnIndex("UPDATE_USER"));
			
			if(StringUtil.isNullOrEmpty(UPDATE_USER)){
				UPDATE_USER = "";
				hasInput = false;
			}else {
				hasInput = true;
			}
		} else {
			UPDATE_USER = "";
			hasInput = false;
		}  
		
		isFullMonth = isFullMonth(PERIOD_FROM_DATE,PERIOD_TO_DATE);
	}
	
	/**
	 * Kiem tra chu ki chuong trinh co full 1 thang khong
	 * @author: hoanpd1
	 * @since: 18:30:34 08-08-2014
	 * @return: boolean
	 * @throws:  
	 * @param fromDatePrriod
	 * @param toDatePrriod
	 * @return
	 */
	private boolean isFullMonth(String fromDatePrriod, String toDatePrriod) {
		String strDateOfMonth = DateUtils.getDayOfMonth(DateUtils.parseDateFromString(fromDatePrriod,
						DateUtils.DATE_FORMAT_NOW));
		String strLastDateOfMonth = DateUtils.getLastDayOfMonth(DateUtils.parseDateFromString(fromDatePrriod,
						DateUtils.DATE_FORMAT_NOW));
		 
		int compare1 = DateUtils.compare(strDateOfMonth, fromDatePrriod, "dd-MM-yyyy");
		int compare2 = DateUtils.compare(strLastDateOfMonth, toDatePrriod, "dd-MM-yyyy"); 
		
		return (compare1 == 0 && compare2 == 0);
	} 
}
