/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.STAFF_POSITION_LOG_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 *  Bang ghi vi tri cua NVBH
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class StaffPositionLogDTO extends AbstractTableDTO{
	// id
	public long id;
	// staff id
	public long staffId;
	// lat
	public double lat;
	// lng
	public double lng;
	//createDate
	public String createDate;
	// do chinh xac khi lay vi tri
	public float accuracy = -1;
	// status
	public int status;
	//createDate su dung luu vao bang work log
	public String createDateTemp;
	// bang cham cong
	public WorkLogDTO workLogDTO = null;
	// workLogId
	public long workLogId;
	
	public StaffPositionLogDTO() {
		super(TableType.STAFF_POSITION_LOG);
	}
	
	/**
	*  Generate cau lenh insert
	*  @author: TruongHN
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateCreateSql(){
		JSONObject orderJson = new JSONObject();
		try{
			orderJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			orderJson.put(IntentConstants.INTENT_TABLE_NAME, STAFF_POSITION_LOG_TABLE.STAFF_POSITION_LOG_TABLE);
			
			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.STAFF_POSITION_ID,"STAFF_POSITION_LOG_SEQ", DATA_TYPE.SEQUENCE.toString()));
			params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.STAFF_ID, this.staffId, null));
			params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.LAT, this.lat , null));
			params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.LNG,this.lng, null));
			 
			if (!GlobalUtil.isSettingAutoTimeUpdate()) {
				// //thoi gian server
				params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.CREATE_DATE, this.createDate,
						DATA_TYPE.SYSDATE.toString()));
			} else {
				// //thoi gian client
				params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.CREATE_DATE, this.createDate, null));
			}
			if (this.accuracy >= 0){
				//params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.CREATE_DATE,this.createDate, null));
				params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.ACCURACY,this.accuracy, null));
			}
			params.put(GlobalUtil.getJsonColumn(STAFF_POSITION_LOG_TABLE.STATUS, this.status, null));
			orderJson.put(IntentConstants.INTENT_LIST_PARAM, params);
			
		}catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return orderJson;
	}
	/**
	 * initFromCursor
	 * @author: 
	 * @since: 19:07:05 01-04-2015
	 * @return: StaffPositionLogDTO
	 * @throws:  
	 * @param c
	 * @return
	 */
	public StaffPositionLogDTO initFromCursor(Cursor c) {
		StaffPositionLogDTO temp = new StaffPositionLogDTO();
		temp.id = c.getLong(c.getColumnIndex("STAFF_POSITION_LOG_ID"));
		temp.staffId = c.getLong(c.getColumnIndex("STAFF_ID"));
		temp.lat = c.getDouble(c.getColumnIndex("LAT"));
		temp.lng = c.getDouble(c.getColumnIndex("LNG"));
		temp.createDate = c.getString(c.getColumnIndex("CREATE_DATE"));
		if(c.getColumnIndex("CREATE_DATE_TEMP") > 0) {
			temp.createDateTemp = c.getString(c.getColumnIndex("CREATE_DATE_TEMP"));
		}
		return temp;
	}
}
