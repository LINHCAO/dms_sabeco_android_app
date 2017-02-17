/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.PAYMENT_DETAIL_TABLE;
import com.viettel.dms.util.GlobalUtil;
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
public class PaymentDetailDTO extends AbstractTableDTO {
	// id
	public long paymentDetailID;
	public long debitDetailID;
	public long payReceivedID;
	public long amount;
	public String paymentType;
	public int status;
	public long staffId;
	public String payDate;
	public String createUser;
	public String createDate;
	public String updateUser;
	public String updateDate;

	public PaymentDetailDTO() {
		super(TableType.PAYMENT_DETAIL_TABLE);
	}

	/**
	 * Tạo chuỗi JSON để thêm vào
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateInsertForPayDebt() {
		JSONObject payreceived = new JSONObject();
		try {
			payreceived.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			payreceived.put(IntentConstants.INTENT_TABLE_NAME, PAYMENT_DETAIL_TABLE.TABLE_PAYMENT_DETAIL);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.PAYMENT_DETAIL_ID, paymentDetailID, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.DEBIT_DETAIL_ID, debitDetailID, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.PAY_RECEIVED_ID, payReceivedID, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.AMOUNT, amount, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.PAYMENT_TYPE, paymentType, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.STATUS, status, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.STAFF_ID, staffId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.PAY_DATE, payDate, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.CREATE_USER, createUser, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAYMENT_DETAIL_TABLE.CREATE_DATE, createDate, null));
			payreceived.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

		} catch (JSONException e) {
		}
		return payreceived;
	}

}
