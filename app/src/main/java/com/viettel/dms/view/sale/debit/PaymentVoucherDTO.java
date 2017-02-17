/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.debit;

import java.util.ArrayList;

import android.database.Cursor;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class PaymentVoucherDTO {
	/**
	 * Mo ta muc dich cua class
	 * 
	 * @author: TamPQ
	 * @version: 1.0
	 * @since: 1.0
	 */
	public class PaymentVoucherItem {
		public long debitDetailId;
		public String orderCode;
		public String orderNumber;
		public String orderDate;
		public String orderReturnDate;
		public long totalReturnAmount;
		public long cusPaidAmount;
		public long returnedAmount;
		public String returnDate;
		public boolean isCheck;

		public PaymentVoucherItem() {
		}

		/**
		 * Mo ta muc dich cua ham
		 * 
		 * @author: TamPQ
		 * @param c
		 * @return: voidvoid
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
					totalReturnAmount = c.getLong(c.getColumnIndex("TOTAL"));
				}
				if (c.getColumnIndex("TOTAL_PAY") > -1) {
					cusPaidAmount = c.getLong(c.getColumnIndex("TOTAL_PAY"));
				}
				// if (c.getColumnIndex("REMAIN") > -1) {
				// remainingAmount = c.getLong(c.getColumnIndex("REMAIN"));
				// }
				// if (c.getColumnIndex("PAY_RECEIVED_NUMBER") > -1) {
				// receipt =
				// c.getString(c.getColumnIndex("PAY_RECEIVED_NUMBER"));
				// }
				// if (c.getColumnIndex("PAY_DATE") > -1) {
				// paidDate = c.getString(c.getColumnIndex("PAY_DATE"));
				// }
			} catch (Exception e) {
			}
		}
	}

	public PaymentVoucherItem newPaymentVoucherItem() {
		return new PaymentVoucherItem();
	}

	public ArrayList<PaymentVoucherItem> arrList = new ArrayList<PaymentVoucherDTO.PaymentVoucherItem>();
}
