/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.dto.db.DebitDTO;
import com.viettel.dms.dto.db.DebitDetailDTO;
import com.viettel.dms.dto.db.PayReceivedDTO;
import com.viettel.dms.dto.db.PaymentDetailDTO;
import com.viettel.viettellib.json.me.JSONArray;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class CusDebitDetailDTO {
	public ArrayList<CusDebitDetailItem> arrList = new ArrayList<CusDebitDetailDTO.CusDebitDetailItem>();
	public boolean isAllUncheck = true;
	public long payNowTotal;
	public long customerId;
	public long debitId;
	public long totalPay;
	public long totalDebit;

	public PayReceivedDTO payReceivedDto;
	public ArrayList<PaymentDetailDTO> arrPaymentDetailDto;

	public CusDebitDetailDTO() {

	}

	public CusDebitDetailItem newCusDebDetailItem() {
		return new CusDebitDetailItem();
	}

	public class CusDebitDetailItem {
		public long debitDetailId;
		public String orderCode;
		public String orderNumber;
		public String orderDate;
		public long totalDebit;
		public long paidAmount;
		public long remainingAmount;
		public String receipt;
		public String paidDate;
		public int index;
		public boolean isCheck;
		public boolean isWouldPay;

		public CusDebitDetailItem() {
		}

		/**
		 * Khoi tao du liệu
		 * 
		 * @author: TamPQ
		 * @param c
		 * @return: void
		 * @throws:
		 */
		public void initData(Cursor c) {
			try {
				if (c == null) {
					throw new Exception("Cursor is empty");
				}
				if (c.getColumnIndex("DEBIT_DETAIL_ID") > -1) {
					debitDetailId = c.getLong(c.getColumnIndex("DEBIT_DETAIL_ID"));
				}
				if (c.getColumnIndex("ORDER_NUMBER") > -1) {
					orderNumber = c.getString(c.getColumnIndex("ORDER_NUMBER"));
				}
				if (c.getColumnIndex("ORDER_DATE") > -1) {
					orderDate = c.getString(c.getColumnIndex("ORDER_DATE"));
				}
				if (c.getColumnIndex("TOTAL") > -1) {
					totalDebit = c.getLong(c.getColumnIndex("TOTAL"));
				}
				if (c.getColumnIndex("TOTAL_PAY") > -1) {
					paidAmount = c.getLong(c.getColumnIndex("TOTAL_PAY"));
				}
				if (c.getColumnIndex("REMAIN") > -1) {
					remainingAmount = c.getLong(c.getColumnIndex("REMAIN"));
				}
				if (c.getColumnIndex("PAY_RECEIVED_NUMBER") > -1) {
					receipt = c.getString(c.getColumnIndex("PAY_RECEIVED_NUMBER"));
				}
				if (c.getColumnIndex("PAY_DATE") > -1) {
					paidDate = c.getString(c.getColumnIndex("PAY_DATE"));
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * Tao chuoi JSON
	 * 
	 * @author: TamPQ
	 * @return
	 * @return: JSONArray
	 * @throws:
	 */
	public JSONArray generatePayDebtSql() {
		JSONArray listSql = new JSONArray();

		DebitDTO debit = new DebitDTO();
		//update DEBIT table
		listSql.put(debit.generateUpdateForPayDebt(this));
		
		//insert PAY_RECEIVED table
		listSql.put(payReceivedDto.generateInsertForPayDebt());
		for (int i = 0; i < arrList.size(); i++) {
			if (arrList.get(i).isWouldPay) {
				PaymentDetailDTO paymentDetail = arrPaymentDetailDto.get(i);
				DebitDetailDTO debitDetail = new DebitDetailDTO();
				//update DEBIT_DETAIL table
				listSql.put(debitDetail.generateUpdateForPayDebt(arrList.get(i)));
				
				//insert PAYMENT_DETAIL table
				listSql.put(paymentDetail.generateInsertForPayDebt());
			}			
		}

		return listSql;
	}
}
