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
 *  Luu tru thong tin version app
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class AppVersionDTO implements Serializable{	
	public String id = Constants.STR_BLANK;
	public String downloadLink = Constants.STR_BLANK;
	//bat buoc force download ?
	public boolean forceDownload = true;
	//can thiet resetDB hay khong
	public boolean needResetDB = false;
	public String version = Constants.STR_BLANK;
	public String content = Constants.STR_BLANK;
	public String getDate = Constants.STR_BLANK;
	
	public void parseFromJson(JSONObject jsonObject){		
		try {					
			id = jsonObject.getString("id");
			downloadLink = jsonObject.getString("downloadLink");
			int force = jsonObject.getInt("forceDownload");
			if(force == 1){
				forceDownload = true;
			}else{
				forceDownload = false;
			}
			int resetDB = jsonObject.getInt("needResetDB");
			if(resetDB == 1){
				needResetDB = true;
			}else{
				needResetDB = false;
			}
			getDate = jsonObject.getString("getDate");	
			version = jsonObject.getString("version");	
			content = jsonObject.getString("content"); 
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}		
	}
}
