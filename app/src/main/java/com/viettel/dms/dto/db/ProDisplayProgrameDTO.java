/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.PRO_DISPLAY_PROGRAM_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;


/**
 *  DTO khi cham trung bay
 * ProDisplayProgrameDTO.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:22:52 12-05-2014
 */
@SuppressWarnings("serial")
public class ProDisplayProgrameDTO extends AbstractTableDTO {
	// Id cua bang
	public long displayProgrameId;
	// id chuong trinh ho tro ban hang
	public String proInfoId;
	// id khach hang duoc cham
	public String customerId;
	// cham 0 : khong dat, 1 : dat
	public int imageDisplay;
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
	public ArrayList<ProDisplayProgrameDetailDTO> listDetail;
	
	public ProDisplayProgrameDTO() {
		super(TableType.PRO_DISPLAY_PROGRAM_TABLE);
		listDetail = new ArrayList<ProDisplayProgrameDetailDTO>();
	}

	public JSONArray generateNewVoteDisplayJson() {
		JSONArray result = new JSONArray();
		
		// Json cho display programe
		JSONObject jsonPrograme = generateSqlInsertVoteDisplayProgram();
		result.put(jsonPrograme);
		
		// Json cho display programe detail
		for (ProDisplayProgrameDetailDTO detail : listDetail) {
			JSONObject jsonDetail = detail.generateJsonSendServer();
			result.put(jsonDetail);
		}
		 
		return result;
	} 
	
	/**
	 * 
	 * @author: quangvt1
	 * @since: 18:27:06 12-05-2014
	 * @return: JSONObject
	 * @throws:  
	 * @return
	 */
	public JSONObject generateSqlInsertVoteDisplayProgram() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			json.put(IntentConstants.INTENT_TABLE_NAME, PRO_DISPLAY_PROGRAM_TABLE.PRO_DISPLAY_PROGRAM_TABLE);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_TABLE.PRO_DISPLAY_PROGRAM_ID, displayProgrameId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_TABLE.PRO_INFO_ID, proInfoId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_TABLE.CUSTOMER_ID, customerId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_TABLE.IMAGE_DISPLAY, imageDisplay, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_TABLE.STAFF_ID, staffId, null)); 
			params.put(GlobalUtil.getJsonColumn(PRO_DISPLAY_PROGRAM_TABLE.CREATE_USER, createUser, null)); 

			json.put(IntentConstants.INTENT_LIST_PARAM, params);
		} catch (JSONException e) { 
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}
}
