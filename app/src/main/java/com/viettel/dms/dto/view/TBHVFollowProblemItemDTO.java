/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.Vector;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO.DATA_TYPE;
import com.viettel.dms.dto.db.AbstractTableDTO.TableAction;
import com.viettel.dms.dto.db.ChannelTypeDTO;
import com.viettel.dms.sqllite.db.FEED_BACK_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * 
 * dto theo doi khac phuc van de GST
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVFollowProblemItemDTO {
	public long id = 0;// id
	public String staffId = Constants.STR_BLANK;// ma nhan vien
	public String staffCode = Constants.STR_BLANK;// ma nhan vien
	public String staffName = Constants.STR_BLANK;// ten nhan vien
	public String type = Constants.STR_BLANK;// loai van de
	public String content = Constants.STR_BLANK;// noi dung
	public int status = 0;// trang thai
	public String createDate = Constants.STR_BLANK;// ngay tao
	public String remindDate = DateUtils.getCurrentDateTimeWithFormat("dd/MM/yyyy");// ngay nhac nho
	public String doneDate = Constants.STR_BLANK;// ngay thuc hien
	public int numReturn=0;// so lan yeu cau lam lai
	public String customer_code;// ma kh
	public String customer_name;// ten kh
	public String housenumber;// sdt
	public String street;// dia chi
	public int ischeck = 0;// kiem tra da duyet
	public Vector<HistoryItemDTO> vHistory = new Vector<HistoryItemDTO>();
	public String updateDate;// ngay cap nhat
	public String updateUser;// nguoi cap nhat
	public String address;// dia chi

	public static int STATUS_DELETED = 0;// xoa
	public static int STATUS_NEW = 1;// tao moi
	public static int STATUS_DONE = 2;// da thuc hien
	public static int STATUS_APPROVE = 3;// da duyet
	public ChannelTypeDTO chanelTypeDTO= new ChannelTypeDTO();// dto chaneltype

	/**
	 * init data feedback role tbhv
	 * 
	 * @author: YenNTH
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initDateWithCursor(Cursor c) {
		// TODO Auto-generated method stub
		if (c.getColumnIndex("ID") >= 0) {
			id = c.getLong(c.getColumnIndex("ID"));
		} else {
			id = 0;
		}
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			customer_code = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			if (customer_code == null) {
				customer_code = Constants.STR_BLANK;
			}
		} else {
			customer_code = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			customer_name = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			if (customer_name == null) {
				customer_name = Constants.STR_BLANK;
			}
		} else {
			customer_name = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("HOUSENUMBER") >= 0) {
			housenumber = c.getString(c.getColumnIndex("HOUSENUMBER"));
			if (housenumber == null) {
				housenumber = Constants.STR_BLANK;
			}
		} else {
			housenumber = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("STREET") >= 0) {
			street = c.getString(c.getColumnIndex("STREET"));
			if (street == null) {
				street = Constants.STR_BLANK;
			}
		} else {
			street = Constants.STR_BLANK;
		}

		if (c.getColumnIndex("STAFF_ID") >= 0) {
			staffId = c.getString(c.getColumnIndex("STAFF_ID"));
			if (staffId == null) {
				staffId = Constants.STR_BLANK;
			}
		} else {
			staffId = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("STAFF_CODE") >= 0) {
			staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
			if (staffCode == null) {
				staffCode = Constants.STR_BLANK;
			}
		} else {
			staffCode = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("NAME") >= 0) {
			staffName = c.getString(c.getColumnIndex("NAME"));
		} else {
			if (c.getColumnIndex("STAFF_NAME") >= 0) {
				staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
			} else {
				staffName = Constants.STR_BLANK;
			}
		}
		if (c.getColumnIndex("CONTENT") >= 0) {
			content = c.getString(c.getColumnIndex("CONTENT"));
		} else {
			content = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("DESCRIPTION") >= 0) {
			type = c.getString(c.getColumnIndex("DESCRIPTION"));
		} else {
			type = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CREATE_DATE") >= 0) {
			createDate = c.getString(c.getColumnIndex("CREATE_DATE"));
		} else {
			createDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("REMIND_DATE") >= 0) {
			remindDate = c.getString(c.getColumnIndex("REMIND_DATE"));
		} else {
			remindDate = DateUtils.getCurrentDateTimeWithFormat("dd/MM/yyyy");// ngay
		}
		if (c.getColumnIndex("DONE_DATE") >= 0) {
			doneDate = c.getString(c.getColumnIndex("DONE_DATE"));
		} else {
			doneDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("NUM_RETURN") >= 0) {
			numReturn = c.getInt(c.getColumnIndex("NUM_RETURN"));
		} else {
			numReturn = 0;
		}
		if (c.getColumnIndex("STATUS") >= 0) {
			status = c.getInt(c.getColumnIndex("STATUS"));
		} else {
			status = STATUS_NEW;
		}
		if(c.getColumnIndex("OBJECT_TYPE")>=0){
			chanelTypeDTO.objectType= c.getInt(c.getColumnIndex("OBJECT_TYPE"));
		}else {
			chanelTypeDTO.objectType = 0;
		}
		if(c.getColumnIndex("ADDRESS") >= 0){
			address = c.getString(c.getColumnIndex("ADDRESS"));
		}else {
			address = Constants.STR_BLANK;
		}
	}
	/**
	 * cap nhat theo doi khac phuc van de TBHV
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateFollowProblemSql(TBHVFollowProblemItemDTO dto) {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, dto.status, null));
			if (!StringUtil.isNullOrEmpty(dto.updateDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE, dto.updateDate, null));
			}
			if (!StringUtil.isNullOrEmpty(dto.doneDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.DONE_DATE, dto.doneDate, null));
			} else {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.DONE_DATE, Constants.STR_BLANK, DATA_TYPE.NULL.toString()));
			}
			if (!StringUtil.isNullOrEmpty(dto.updateUser)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER, dto.updateUser, null));
			}
			if (dto.numReturn>0) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.NUM_RETURN, dto.numReturn, null));
			}
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, dto.id, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}

	/**
	 * gui yeu cau delete request len server
	 * 
	 * @author YenNTH
	 * @param dto
	 * @return
	 */
	public JSONObject generateDeleteFollowProblemSql(TBHVFollowProblemItemDTO dto) {
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME, FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS, 0, null));
			if (!StringUtil.isNullOrEmpty(dto.updateDate)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE, dto.updateDate, null));
			}
			if (!StringUtil.isNullOrEmpty(dto.updateUser)) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER, dto.updateUser, null));
			}
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID, dto.id, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}
}
