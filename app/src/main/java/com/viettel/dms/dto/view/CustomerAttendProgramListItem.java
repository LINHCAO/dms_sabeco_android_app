/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO.TableAction;
import com.viettel.dms.sqllite.db.PRO_CUS_HISTORY_TABLE;
import com.viettel.dms.sqllite.db.PRO_CUS_MAP_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.PRO_CUS_MAP_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin KH Tham gia CT HTBH CustomerAttendProgramListItem.java
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 15:19:24 11-05-2014
 */
@SuppressWarnings("serial")
public class CustomerAttendProgramListItem implements Serializable {
	public long customerId;
	public String customerCode;
	public String customerName;
	public String houseNumber;
	public String street;
	public String area;
	public String address;
	public String address1;
	public int level; 
	public String levelName; 
	public String attendDate;
	//Loai trang thai: 0: cho duyet, 1: tham gia, 2: tam ngung
	public int status;
	public String statusName;
	public boolean isCheck;
	// Co nhap san luong dang ki tham gia khong?
	public boolean isHaveJoin;
	// pro_cus_map_id : dung de xoa
	public String pro_cus_map_id;
	
	public void initDataFromCursor(Cursor c) {
		if (c.getColumnIndex("CUSTOMER_ID") > -1) {
			customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
		}
		
		if (c.getColumnIndex("SHORT_CODE") > -1) {
			customerCode = c.getString(c.getColumnIndex("SHORT_CODE"));
		}
		
		if (c.getColumnIndex("CUSTOMER_NAME") > -1) {
			customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		}
		
		if (c.getColumnIndex("STREET") > -1) {
			street = c.getString(c.getColumnIndex("STREET"));
			
			if(street == null || "null".equalsIgnoreCase(street)){
				street = "";
			}
		} 
		
		if (c.getColumnIndex("HOUSENUMBER") > -1) {
			houseNumber = c.getString(c.getColumnIndex("HOUSENUMBER"));
			
			if(houseNumber == null || "null".equalsIgnoreCase(houseNumber)){
				houseNumber = "";
			}
		}
		
		if (c.getColumnIndex("AREA_NAME") > -1) {
			area = c.getString(c.getColumnIndex("AREA_NAME"));
			
			if(area == null || "null".equalsIgnoreCase(area)){
				area = "";
			}
		}
		
		if (c.getColumnIndex("CUSTOMER_NAME") > -1) {
			customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		}
		
		if (c.getColumnIndex("LEVEL_NUMBER") > -1) {
			level = c.getInt(c.getColumnIndex("LEVEL_NUMBER"));
		}
		
		if (c.getColumnIndex("LEVEL_NAME") > -1) {
			levelName = c.getString(c.getColumnIndex("LEVEL_NAME"));
		}
		
		if (c.getColumnIndex("TYPE") > -1) {
			status = c.getInt(c.getColumnIndex("TYPE"));
		}

		if (c.getColumnIndex("NUM_JOIN") > -1) {
			int numJoin = c.getInt(c.getColumnIndex("NUM_JOIN"));
			this.isHaveJoin = (numJoin > 0);
		}
		
		if (c.getColumnIndex("PRO_CUS_MAP_ID") > -1) { 
			this.pro_cus_map_id = c.getString(c.getColumnIndex("PRO_CUS_MAP_ID"));
		}
		
		if (c.getColumnIndex("JOINING_DATE") > -1) {
			attendDate = c.getString(c.getColumnIndex("JOINING_DATE"));
			
			if(attendDate == null || "null".equalsIgnoreCase(attendDate)){
				attendDate = "";
			}else{
				attendDate = DateUtils.convertDateOneFromFormatToAnotherFormat(attendDate, DateUtils.DATE_FORMAT_NOW, DateUtils.DATE_FORMAT_DEFAULT);
			}
		}
		if (c.getColumnIndex("ADDRESS1") > -1) {
			address1 = c.getString(c.getColumnIndex("ADDRESS1"));
		}
		
		
		// Dia chi hoan chinh
		// Dia chi dang : housenumber + " " + street + ", " + area
		address = "";
		if(!StringUtil.isNullOrEmpty(houseNumber)){
			address += houseNumber;
		}
		
		if(!StringUtil.isNullOrEmpty(address)){
			address +=  " ";
		}
		
		if(!StringUtil.isNullOrEmpty(street)){
			address += street;
		}
		
		if(!StringUtil.isNullOrEmpty(address)){
			address +=  ", ";
		}
		
		if(!StringUtil.isNullOrEmpty(area)){
			address +=  area;
		}
		 
		
		// Status Name
		String sttTemp = "";
		switch (status) {
		case 0: {
			sttTemp = StringUtil.getString(R.string.TEXT_STATUS_WAIT_FOR_APPROVE);
			break;
		}
		case 1: {
			sttTemp = StringUtil.getString(R.string.TEXT_STATUS_ATTEND);
			break;
		}
		case 2: {
			sttTemp = StringUtil.getString(R.string.TEXT_STATUS_PAUSE_ATTEND);
			break;
		}
		default:
			sttTemp = "";
			break;
		}
		this.statusName = sttTemp;
		
	}

	/**
	 * Gen cau sql detele pro_cus_map
	 * @author: quangvt1
	 * @since: 16:30:04 19-05-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param pro_cus_map_id
	 * @return
	 */
	public JSONObject generateDeleteProCusMapSql(String pro_cus_map_id) {
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_TABLE.PRO_CUS_MAP_TABLE); 

			// where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.PRO_CUS_MAP_ID, pro_cus_map_id, null));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}

	/**
	 * Gen cau sql detele pro_cus_map_detail
	 * @author: quangvt1
	 * @since: 16:30:20 19-05-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param pro_cus_map_id
	 * @return
	 */
	public JSONObject generateDeleteProCusMapDetailSql(String pro_cus_map_id) {
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_DETAIL_TABLE.PRO_CUS_MAP_DETAIL_TABLE); 

			// where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.PRO_CUS_MAP_ID, pro_cus_map_id, null));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}

	/**
	 * Gen cau sql detele pro_cus_history
	 * @author: quangvt1
	 * @since: 16:30:27 19-05-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param pro_cus_map_id
	 * @return
	 */
	public JSONObject generateDeleteHistorySql(String pro_cus_map_id) {
		JSONObject actionJson = new JSONObject();
		try {
			actionJson.put(IntentConstants.INTENT_TYPE, TableAction.DELETE);
			actionJson.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_TABLE); 

			// where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.PRO_CUS_MAP_ID, pro_cus_map_id, null));
			actionJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return actionJson;
	}
}
