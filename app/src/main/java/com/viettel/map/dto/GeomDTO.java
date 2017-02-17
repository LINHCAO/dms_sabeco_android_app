/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.dto;

import java.io.Serializable;

import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * GeomDTO
 * GeomDTO.java
 * @version: 1.0 
 * @since:  08:33:43 20 Jan 2014
 */
@SuppressWarnings("serial")
public class GeomDTO implements Serializable {
	public double lng;// toa do long
	public double lat;// toa do lat

	public void parseFromJson(String data) {
		JSONObject jsonObject;
		try {
			jsonObject = new JSONObject(data);
			lng = jsonObject.getDouble("lng");
			lat = jsonObject.getDouble("lat");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}
}
