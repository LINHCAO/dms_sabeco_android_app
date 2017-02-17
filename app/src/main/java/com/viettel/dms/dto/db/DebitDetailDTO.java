/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.view.CusDebitDetailDTO.CusDebitDetailItem;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.sqllite.db.DEBIT_DETAIL_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin chi tiet cong no
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class DebitDetailDTO extends AbstractTableDTO {
	// id
	public long debitID;
	public long debitDetailID;
	public long fromObjectID;
	public long amount;
	public long discount;
	public long total;
	public long totalPay;
	public long remain;
	public long fromObjectNumber;
	public long invoiceNumber;
	public String createDate;
	public String createUser;

	public DebitDetailDTO() {
		super(TableType.DEBIT_DETAIL_TABLE);
	}

	/**
	 * Generate cau lenh insertOrupdate
	 * 
	 * @author: TruongHN
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateInsertFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			json.put(IntentConstants.INTENT_TABLE_NAME, DEBIT_DETAIL_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			// staff_customer_id: id tu tang tren server, va khong cap nhat khi
			// update
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_DETAIL_TABLE.DEBIT_DETAIL_ID, debitDetailID, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.DEBIT_ID, debitID, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.FROM_OBJECT_ID, fromObjectID, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.FROM_OBJECT_NUMBER, fromObjectNumber, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.AMOUNT, amount, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.DISCOUNT, discount, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.TOTAL, total, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.TOTAL_PAY, totalPay, null));
			params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.REMAIN, remain, null));
			
			if (!StringUtil.isNullOrEmpty(createUser)) {
				params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.CREATE_USER, createUser, null));
			}
			if (!StringUtil.isNullOrEmpty(createDate)) {
				params.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.CREATE_DATE, createDate, null));	
			}
			

			json.put(IntentConstants.INTENT_LIST_PARAM, params);

		} catch (JSONException e) {
		}
		return json;
	}

	/**
	 * Tao chuoi JSON để cập nhật
	 * 
	 * @author: TamPQ
	 * @param cusDebitDetailItem
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateForPayDebt(CusDebitDetailItem cusDebitDetailItem) {
		JSONObject debit = new JSONObject();
		try {
			debit.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			debit.put(IntentConstants.INTENT_TABLE_NAME, DEBIT_DETAIL_TABLE.TABLE_NAME);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.TOTAL_PAY, cusDebitDetailItem.paidAmount, null));
			detailPara.put(GlobalUtil
					.getJsonColumn(DEBIT_DETAIL_TABLE.REMAIN, cusDebitDetailItem.remainingAmount, null));
			detailPara.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.UPDATE_USER, ""
					+ GlobalInfo.getInstance().getProfile().getUserData().userName, null));
			debit.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(DEBIT_DETAIL_TABLE.DEBIT_DETAIL_ID, cusDebitDetailItem.debitDetailId,
					null));
			debit.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return debit;
	}
}
