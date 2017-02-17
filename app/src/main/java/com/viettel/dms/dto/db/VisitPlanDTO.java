/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.VISIT_PLAN_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin lo trinh ban hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class VisitPlanDTO extends AbstractTableDTO {
	// id lo trinh ban hang
	public int visitPlanId;
	// id NPP
	public int shopId;
	// id nhan vien
	public int staffId;
	// id khach hang
	public int customerId;
	// ngay bat dau
	public String startDate;
	// ngay ket thuc
	public String endDate;
	// tan suat trong 1 thang
	public String freq;
	// tuan di T1, T2, T3, T4, T13, T24, MD (ca 4 tuan)
	public String weekSale;
	// 0: khong di, 1: di
	public int monday;
	// 0: khong di, 1: di
	public int tuesday;
	// 0: khong di, 1: di
	public int wednesday;
	// 0: khong di, 1: di
	public int thursday;
	// 0: khong di, 1: di
	public int friday;
	// 0: khong di, 1: di
	public int saturday;
	// 0: khong di, 1: di
	public int sunday;
	// 1: hieu luc, 0: het hieu luc
	public int active;
	public int seq2;
	public int seq3;
	public int seq4;
	public int seq5;
	public int seq6;
	public int seq7;
	public int seq8;
	public int isnewoutlet;
	// ngay cap nhat
	public String updateDate;
	// ngay tao
	public String createDate;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String updateUser;
	// ngay dat hang cuoi cung
	public String lastOrder;
	// ngay duyet don hang cuoi cung
	public String lastApproveOrder;
	// co the dat hang xa khoang cach
	public String exceptionOrderDate;

	public VisitPlanDTO() {
		super(TableType.VISIT_PLAN_TABLE);
	}

	public void initWithCursor(Cursor c) {
		if (c.getColumnIndex(VISIT_PLAN_TABLE.VISIT_PLAN_ID) >= 0) {
			visitPlanId = c.getInt(c
					.getColumnIndex(VISIT_PLAN_TABLE.VISIT_PLAN_ID));
		} else {
			visitPlanId = 0;
		}
		if (c.getColumnIndex(VISIT_PLAN_TABLE.SHOP_ID) >= 0) {
			shopId = c.getInt(c.getColumnIndex(VISIT_PLAN_TABLE.SHOP_ID));
		} else {
			shopId = 0;
		}
		if (c.getColumnIndex(VISIT_PLAN_TABLE.STAFF_ID) >= 0) {
			staffId = c.getInt(c.getColumnIndex(VISIT_PLAN_TABLE.STAFF_ID));
		} else {
			staffId = 0;
		}
	}

	/**
	 * Cap nhat khi tao don hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: Vector
	 * @throws:
	 */

	public JSONObject generateUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME,
					VISIT_PLAN_TABLE.TABLE_VISIT_PLAN);

			// ds params
			JSONArray params = new JSONArray();
//			params.put(GlobalUtil.getJsonColumn(VISIT_PLAN_TABLE.LAST_ORDER,
//					lastOrder, null));
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
//			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.ACTIVE,
//					active, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.SHOP_ID,
					shopId, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.STAFF_ID,
					staffId, null));
//			wheres.put(GlobalUtil.getJsonColumnWhere(
//					VISIT_PLAN_TABLE.CUSTOMER_ID, customerId, null));
//			wheres.put(GlobalUtil.getJsonColumnWhere(
//					VISIT_PLAN_TABLE.START_DATE, lastOrder, "<="));
//			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.END_DATE,
//					lastOrder, ">="));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
		}
		return json;
	}

	/**
	 * Update khi xoa don hang cuoi
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param lastOrder2
	 * @return
	 * @return: Vector
	 * @throws:
	 */

	public JSONObject generateUpdateLastOrder(String lastOrder2) {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME,
					VISIT_PLAN_TABLE.TABLE_VISIT_PLAN);

			// ds params
			JSONArray params = new JSONArray();
			if (StringUtil.isNullOrEmpty(lastOrder2)) {
//				params.put(GlobalUtil.getJsonColumn(
//						VISIT_PLAN_TABLE.LAST_ORDER, "",
//						DATA_TYPE.NULL.toString()));
			} else {
//				params.put(GlobalUtil.getJsonColumn(
//						VISIT_PLAN_TABLE.LAST_ORDER, lastOrder2, null));
			}
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
//			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.ACTIVE,
//					active, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.SHOP_ID,
					shopId, null));
			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.STAFF_ID,
					staffId, null));
//			wheres.put(GlobalUtil.getJsonColumnWhere(
//					VISIT_PLAN_TABLE.CUSTOMER_ID, customerId, null));
//			wheres.put(GlobalUtil.getJsonColumnWhere(
//					VISIT_PLAN_TABLE.START_DATE, lastOrder, "<="));
//			wheres.put(GlobalUtil.getJsonColumnWhere(VISIT_PLAN_TABLE.END_DATE,
//					lastOrder, ">="));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
		}
		return json;
	}
}
