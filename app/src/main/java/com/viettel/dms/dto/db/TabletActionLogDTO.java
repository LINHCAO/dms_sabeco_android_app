/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.TABLET_ACTION_LOG_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin loi can log len server
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class TabletActionLogDTO extends AbstractTableDTO {
	// loi do vang exception
	public static final int LOG_EXCEPTION = 0;
	// loi do server tra ve
	public static final int LOG_SERVER = 1;
	// loi do xu ly duoi client
	public static final int LOG_CLIENT = 2;
	// loi do force close
	public static final int LOG_FORCE_CLOSE = 3;
	// log login dang nhap tai file
	public static final int LOG_LOGIN = 4;
	// log dinh vi lưu staff_position_log
	public static final int LOG_LOCATION = 5;
	// log dinh vi: có vị trí nhưng không thỏa điều kiện nên không thể lưu staff_position_log
	public static final int LOG_LOCATION_FAIL = 6;
	// id
	public long id;
	// staff id
	public long staffId;
	// shop id
	public String shopId;
	// device imei
	public String deviceImei = "";
	// version app
	public String appVersion = "";
	// noi dung log
	public String content;
	// dien giai
	public String description;
	// type: tam thoi chua dung
	public int type;
	// ngay tao
	public String createDate;

	public TabletActionLogDTO() {
		super(TableType.TABLET_ACTION_LOG);
	}
	
	/**
	*  Generate cau lenh insert
	*  @author: TruongHN
	*  @param 
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateCreateLogSql(){
		JSONObject logJson = new JSONObject();
		try{
			
			this.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			this.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
			
			logJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			logJson.put(IntentConstants.INTENT_TABLE_NAME, TABLET_ACTION_LOG_TABLE.TABLE_NAME);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.TABLET_ACTION_LOG_ID,"TABLET_ACTION_LOG_SEQ", DATA_TYPE.SEQUENCE.toString()));
			if(!StringUtil.isNullOrEmpty(this.shopId)) {
				params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.SHOP_ID, this.shopId, null));
			}
			if(this.staffId > 0) {
				params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.STAFF_ID, this.staffId, null));
			}
			params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.DEVICE_IMEI,GlobalInfo.getInstance().getDeviceIMEI(), null));
			params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.APP_VERSION,GlobalInfo.getInstance().getProfile().getVersionApp(), null));
			params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.CONTENT,content, null));
			params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.DESCRIPTION,description, null));
			params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.CREATE_DATE,DateUtils.now(), null));
			if (type >= 0){
				params.put(GlobalUtil.getJsonColumn(TABLET_ACTION_LOG_TABLE.TYPE,type, null));
			}
			
			logJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return logJson;
	}

	

}
