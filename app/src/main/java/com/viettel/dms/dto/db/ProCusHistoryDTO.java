/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.PRO_CUS_HISTORY_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * ProCusHistory.java
 * @author: hoanpd1
 * @version: 1.0 
 * @since:  17:52:55 20-05-2014
 */
public class ProCusHistoryDTO extends AbstractTableDTO{
	private static final long serialVersionUID = 1L;
	
	public long proCusHistoryId;
	public long proCusMapId;
	public String fromDate;
	public String toDate;
	public int type;
	public String createDate;
	public String createUser;
	public String updateDate;
	public String updateUser;
	public long staffId;
	

	/**
	 * generate sql insert to server
	 * @author: hoanpd1
	 * @since: 1.0
	 * @return: JSONObject
	 * @throws:  
	 * @return
	 */
	public JSONObject generateProCusHistorySql() {
		JSONObject proCusMapJson = new JSONObject();

		try {
			proCusMapJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			proCusMapJson.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_ID, proCusMapId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.PRO_CUS_MAP_ID, proCusMapId, null));
			if (!StringUtil.isNullOrEmpty(fromDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.FROM_DATE, fromDate, null));
			}
			if (!StringUtil.isNullOrEmpty(toDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.TO_DATE, toDate, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.TYPE, type, null));
			if (!StringUtil.isNullOrEmpty(createDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.CREATE_DATE, createDate, null));
			}
			if (!StringUtil.isNullOrEmpty(createUser)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.CREATE_USER, createUser, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.STAFF_ID, staffId, null));
			proCusMapJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_HISTORY_TABLE.PRO_CUS_HISTORY_ID, proCusMapId, null));
			proCusMapJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return proCusMapJson;
	}
}
