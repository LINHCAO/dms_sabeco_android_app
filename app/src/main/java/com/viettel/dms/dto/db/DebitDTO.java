/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.view.CusDebitDetailDTO;
import com.viettel.dms.sqllite.db.DEBIT_TABLE;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Thong tin cong no
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class DebitDTO extends AbstractTableDTO {
	// id
	public long id;
	//
	public String objectID;
	//
	public String objectType;
	//
	public long totalAmount;
	//
	public long totalPay;
	public long totalDebit;
	public long maxDebitAmount;
	public String maxDebitDate;

	public DebitDTO() {
		super(TableType.DEBIT_TABLE);
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
			json.put(IntentConstants.INTENT_TABLE_NAME, DEBIT_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			// staff_customer_id: id tu tang tren server, va khong cap nhat khi
			// update
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.DEBIT_ID, id, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.OBJECT_ID, objectID, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.OBJECT_TYPE, objectType, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.TOTAL_AMOUNT, totalAmount, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.TOTAL_PAY, totalPay, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.TOTAL_DEBIT, totalDebit, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.MAX_DEBIT_AMOUNT, maxDebitAmount, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.MAX_DEBIT_DATE, maxDebitDate, null));

			json.put(IntentConstants.INTENT_LIST_PARAM, params);

		} catch (JSONException e) {
		}
		return json;
	}

	/**
	 * Generate cau lenh insertOrupdate
	 * 
	 * @author: TruongHN
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateInsertOrUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.INSERTORUPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, DEBIT_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.DEBIT_ID, id, null));

			// Update thi ko cap nhat cac truong nay
			if (!StringUtil.isNullOrEmpty(objectID)) {
				params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.OBJECT_ID, objectID, null));
			}
			if (!StringUtil.isNullOrEmpty(objectType)) {
				params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.OBJECT_TYPE, objectType, null));
			}
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.TOTAL_PAY, totalPay, null));
			params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.MAX_DEBIT_AMOUNT, maxDebitAmount, null));
			if (!StringUtil.isNullOrEmpty(maxDebitDate)) {
				params.put(GlobalUtil.getJsonColumnWithKey(DEBIT_TABLE.MAX_DEBIT_DATE, maxDebitDate, null));
			}

			// Insert thi cap nhat vao, update thi tang
			params.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.TOTAL_AMOUNT, "*+" + totalAmount,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.TOTAL_DEBIT, "*+" + totalDebit,
					DATA_TYPE.OPERATION.toString()));

			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.OBJECT_ID, objectID, null));
			wheres.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.OBJECT_TYPE, objectType, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
		}
		return json;
	}

	/**
	 * 
	 * Tang quantity cua stock total
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateIncreaseSql() {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, DEBIT_TABLE.TABLE_NAME);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.TOTAL_AMOUNT, "*+" + totalAmount,
					DATA_TYPE.OPERATION.toString()));
			params.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.TOTAL_DEBIT, "*+" + totalDebit,
					DATA_TYPE.OPERATION.toString()));
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.OBJECT_ID, objectID, null));
			wheres.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.OBJECT_TYPE, objectType, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
			// TODO: handle exception
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * Update khi thanh toan cong no
	 * 
	 * @author: TamPQ
	 * @param debitDto
	 * @return
	 * @return: JSONObjectvoid
	 * @throws:
	 */
	public JSONObject generateUpdateForPayDebt(CusDebitDetailDTO debitDto) {
		JSONObject debit = new JSONObject();
		try {
			debit.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			debit.put(IntentConstants.INTENT_TABLE_NAME, DEBIT_TABLE.TABLE_NAME);
			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.TOTAL_PAY, debitDto.totalPay, null));
			detailPara.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.TOTAL_DEBIT, debitDto.totalDebit, null));
			debit.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(DEBIT_TABLE.DEBIT_ID, debitDto.debitId, null));
			debit.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (Exception e) {
		}
		return debit;
	}
}
