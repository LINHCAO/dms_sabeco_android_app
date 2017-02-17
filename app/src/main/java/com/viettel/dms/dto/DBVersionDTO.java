/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto;

import java.io.Serializable;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 *  Luu tru thong tin version db
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class DBVersionDTO implements Serializable{	
	public static String UPDATE = "UPDATEDB";
	public static String RESET  = "RESETDB";
	
	//action update DB (update or reset)
	public String action;
	//script
	public String script = Constants.STR_BLANK;;
	//version db
	public String version = Constants.STR_BLANK;;
	
	// last log id luc tao file
	public String lastLogId = "";
	// log db hien tai tren server
	public String maxDBLogId = "";
	// url tai file db
	public String urlDB = "";
	
	public String keyZip = "";
		
	/**
	 * parse thong tin db tu login
	 * @author: BANGHN
	 * @param jsonObject
	 */
	public void parseFromJson(JSONObject jsonObject){		
		try {					
			action = jsonObject.getString("action");
			script = jsonObject.getString("script");
			version = jsonObject.getString("version");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}		
	}
	
	/**
	 * Parse luu du lieu khi login ve: luu thong tin version db, last_log_udate
	 * @author : BangHN
	 * since : 2:08:43 PM
	 */
	public void parseGetLinkDB(JSONObject jsonObject){		
		try {					
			lastLogId = jsonObject.getString("lastLogId_update");
			maxDBLogId = jsonObject.getString("maxDBLogId");
			version = jsonObject.getString("currentDBVersion");
			urlDB = jsonObject.getString("URL");
			keyZip = jsonObject.getString("keyZip");
		} catch (JSONException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}		
	}
}
