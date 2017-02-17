/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.Vector;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.FEED_BACK_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * 
 * dto theo doi khac phuc van de
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class FollowProblemItemDTO extends AbstractTableDTO {
	public long feedBackId;
	public String staffId;// ma nhan vien
	public String customerId;// ma khach hang
	public String productId;// ma san pham
	public String staffCode;// ma nhan vien
	public String staffName;// ten nhan vien
	public String customerCode;// ma khach hang
	public String customerName;// ten khach hang
	public String type;// loai van de
	public String content;// noi dung
	public int status;// trang thai
	public String createDate;// ngay tao
	public String remindDate;// ngay nhac nho
	public String doneDate;// ngay tao
	public int ischeck;// kiem tra da duyet
	public int numReturn;// so lan yeu cau lam lai
	public Vector<HistoryItemDTO> vHistory = new Vector<HistoryItemDTO>();
	public String houseNumber;// so nha
	public String street;// duong
	public String address;// dia chi
	public String updateUser; // ma so nguoi update
	public String updateDate;// ngay cap nhat

	/**
	 * khoi tao feedback
	 * 
	 * @author: YenNTH
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initDateWithCursor(Cursor c) {
		// TODO Auto-generated method stub
		if (c.getColumnIndex("FEEDBACK_ID") >= 0) {
			feedBackId = c.getLong(c.getColumnIndex("FEEDBACK_ID"));
		} else {
			feedBackId = 0;
		}

		if (c.getColumnIndex("STAFF_ID") >= 0) {
			staffId = c.getString(c.getColumnIndex("STAFF_ID"));
			if (staffId == null) {
				staffId = Constants.STR_BLANK;
			}
		} else {
			staffId = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
			customerId = c.getString(c.getColumnIndex("CUSTOMER_ID"));
		} else {
			customerId = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("PRODUCT_ID") >= 0) {
			productId = c.getString(c.getColumnIndex("PRODUCT_ID"));
		} else {
			productId = Constants.STR_BLANK;
		}

		if (c.getColumnIndex("STAFF_CODE") >= 0) {
			staffCode = c.getString(c.getColumnIndex("STAFF_CODE"));
			if (staffCode == null) {
				staffCode = Constants.STR_BLANK;
			}
		} else {
			staffCode = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("STAFF_NAME") >= 0) {
			staffName = c.getString(c.getColumnIndex("STAFF_NAME"));
		} else {
			staffName = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
		} else {
			customerCode = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		} else {
			customerName = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("ADDRESS") >= 0) {
			address = c.getString(c.getColumnIndex("ADDRESS"));
		} else {
			address = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("HOUSENUMBER") >= 0) {
			houseNumber = c.getString(c.getColumnIndex("HOUSENUMBER"));
		} else {
			houseNumber = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("STREET") >= 0) {
			street = c.getString(c.getColumnIndex("STREET"));
		} else {
			street = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CONTENT") >= 0) {
			content = c.getString(c.getColumnIndex("CONTENT"));
		} else {
			content = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("AP_PARAM_NAME") >= 0) {
			type = c.getString(c.getColumnIndex("AP_PARAM_NAME"));
		} else {
			type = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CREATE_DATE") >= 0) {
			createDate = c.getString(c.getColumnIndex("CREATE_DATE"));
		} else {
			createDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("DONE_DATE") >= 0) {
			doneDate = c.getString(c.getColumnIndex("DONE_DATE"));
		} else {
			doneDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("REMIND_DATE") >= 0) {
			remindDate = c.getString(c.getColumnIndex("REMIND_DATE"));
		} else {
			remindDate = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("STATUS") >= 0) {
			status = c.getInt(c.getColumnIndex("STATUS"));
		} else {
			status = 1;
		}
		if (c.getColumnIndex("NUM_RETURN") >= 0) {
			numReturn = c.getInt(c.getColumnIndex("NUM_RETURN"));
		} else {
			numReturn = 0;
		}
		updateUser = String.valueOf(GlobalInfo.getInstance().getProfile()
				.getUserData().id);
		updateDate = DateUtils.now();
	}

	/**
	 * gen json gui server
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateFollowProblemSql(FollowProblemItemDTO dto) {
		// TODO Auto-generated method stub
		JSONObject feedbackJson = new JSONObject();
		try {
			feedbackJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			feedbackJson.put(IntentConstants.INTENT_TABLE_NAME,FEED_BACK_TABLE.FEED_BACK_TABLE);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.STATUS,dto.status, null));
			if (dto.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.DONE_DATE, Constants.STR_BLANK,DATA_TYPE.NULL.toString()));
				detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.NUM_RETURN, dto.numReturn, null));
			}
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_DATE,dto.updateDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.UPDATE_USER,dto.updateUser, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(FEED_BACK_TABLE.FEEDBACK_ID,
					dto.feedBackId, null));
			feedbackJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (Exception e) {
		}
		return feedbackJson;
	}
}
