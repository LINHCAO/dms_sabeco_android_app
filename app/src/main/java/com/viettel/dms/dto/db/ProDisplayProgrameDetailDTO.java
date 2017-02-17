/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.PRO_DISPLAY_PROGRAM_DETAIL_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;


/**
 * Thong tin chi tiet 1 trung bay cua 1 san pham cua 1 chuong trinh
 * ProDisplayProgrameDTO.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:22:52 12-05-2014
 */
@SuppressWarnings("serial")
public class ProDisplayProgrameDetailDTO extends AbstractTableDTO {
	// Id cua bang
	public long displayProgrameDetailId;
	// id chuong trinh
	public long displayProgrameId;
	// id khach hang duoc cham
	public String customerId;
	// ma san pham
	public String productId;
	// So luong
	public int quantity;
	// ngay tao
	public String createDate;
	// ngay update
	public String updateDate;
	// nguoi tao
	public String createUser;
	// nguoi update
	public String updateUser; 
	// id nhan vien cham tb
	public String staffId; 
	
	public ProDisplayProgrameDetailDTO() {
		super(TableType.PRO_DISPLAY_PROGRAM_TABLE_DETAIL);
	}

	public JSONObject generateJsonSendServer() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			json.put(IntentConstants.INTENT_TABLE_NAME, PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRO_DISPLAY_PROGRAM_DETAIL_TABLE);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRO_DISPLAY_PROGRAM_DETAIL_ID, displayProgrameDetailId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRO_DISPLAY_PROGRAM_ID, displayProgrameId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.PRODUCT_ID, productId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.QUANTITY, quantity, null));  
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.STAFF_ID, staffId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_DETAIL_TABLE.CREATE_USER, createUser, null)); 

			json.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) { 
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	} 
}
