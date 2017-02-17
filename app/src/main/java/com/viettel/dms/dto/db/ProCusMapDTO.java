/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.PRO_CUS_MAP_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * ProCusMapDTO.java
 * @author: hoanpd1
 * @version: 1.0 
 * @since:  17:24:05 20-05-2014
 */
public class ProCusMapDTO extends AbstractTableDTO{
	private static final long serialVersionUID = 1L;
	public long proCusMapId;
	public long proInfoId;
	public long objectId;
	public int objectType;
	public int levelNumber;
	public String JoiningDate;
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
	public JSONObject generateCusMapSql() {
		JSONObject proCusMapJson = new JSONObject();

		try {
			proCusMapJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			proCusMapJson.put(IntentConstants.INTENT_TABLE_NAME, PRO_CUS_MAP_TABLE.PRO_CUS_MAP_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.PRO_CUS_MAP_ID, proCusMapId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.PRO_INFO_ID, proInfoId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.OBJECT_ID, objectId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.OBJECT_TYPE, objectType, null));
			
			if (!StringUtil.isNullOrEmpty(JoiningDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.JOINING_DATE, JoiningDate, null));
			}

			if (!StringUtil.isNullOrEmpty(createDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.CREATE_DATE, createDate, null));
			}
			if (!StringUtil.isNullOrEmpty(createUser)) {
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.CREATE_USER, createUser, null));
			}
			
			if(levelNumber > 0){
				detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.LEVEL_NUMBER, levelNumber, null));
			}
			
			detailPara.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.STAFF_ID, staffId, null));
			proCusMapJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(PRO_CUS_MAP_TABLE.PRO_CUS_MAP_ID, proCusMapId, null));
			proCusMapJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return proCusMapJson;
	}
}
