/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.PAY_RECEIVED_TABLE;
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
public class PayReceivedDTO extends AbstractTableDTO {
	public static final String PAYMENT_TYPE_CASH = "0";
	// id
	public long payReceivedID;
	public String payReceivedNumber;
	public long amount;
	public String paymentType;
	public String shopId;
	public long customerId;
	public long receiptType;
	public long staffId;
	public String createUser;
	public String updateUser;
	public String updateDate;
	public String createDate;

	public PayReceivedDTO() {
		super(TableType.PAY_DECEIVED_TABLE);
	}

	/**
	 * tao row insert gach no
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: JSONObjectvoid
	 * @throws:
	 */
	public JSONObject generateInsertForPayDebt() {
		JSONObject payreceived = new JSONObject();
		try {
			payreceived.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			payreceived.put(IntentConstants.INTENT_TABLE_NAME, PAY_RECEIVED_TABLE.TABLE_PAY_RECEIVED);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.PAY_RECEIVED_ID, payReceivedID, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.PAY_RECEIVED_NUMBER, payReceivedNumber, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.AMOUNT, amount, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.PAYMENT_TYPE, paymentType, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.CUSTOMER_ID, customerId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.RECEIPT_TYPE, receiptType, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.SHOP_ID, shopId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.STAFF_ID, staffId, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.CREATE_USER, createUser, null));
			detailPara.put(GlobalUtil.getJsonColumn(PAY_RECEIVED_TABLE.CREATE_DATE, createDate, null));
			payreceived.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

		} catch (JSONException e) {
		}
		return payreceived;
	}

}
