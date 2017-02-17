/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/** 
 * No Success Sale Order Dto class
 * NoSuccessSaleOrderDto.java
 * @version: 1.0 
 * @since:  10:35:03 8 Jan 2014
 */
@SuppressWarnings("serial")
public class NoSuccessSaleOrderDto implements Serializable {
	public ArrayList<NoSuccessSaleOrderItem> itemList;

	public NoSuccessSaleOrderDto() {
		itemList = new ArrayList<NoSuccessSaleOrderItem>();
	}

	public NoSuccessSaleOrderItem newNoSuccessSaleOrderItem() {
		return new NoSuccessSaleOrderItem();
	}

	public class NoSuccessSaleOrderItem {
		public SaleOrderDTO saleOrder;
		public CustomerDTO customer;

		public NoSuccessSaleOrderItem() {
			saleOrder = new SaleOrderDTO();
			customer = new CustomerDTO();
		}

		public void initNoSuccessSaleOrderItem(Cursor c) {
			try {
				if (c == null) {
					throw new Exception("Cursor is empty");
				}
			} catch (Exception ex) {
			}
			
			this.customer.customerCode = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE))) ? "" : c
					.getString(c.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_CODE));
			this.customer.customerName = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME))) ? "" : c
					.getString(c.getColumnIndexOrThrow(CUSTOMER_TABLE.CUSTOMER_NAME));
			this.saleOrder.orderNumber = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow(SALE_ORDER_TABLE.ORDER_NUMBER))) ? "" : c
					.getString(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.ORDER_NUMBER));
			this.saleOrder.orderDate = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow(SALE_ORDER_TABLE.ORDER_DATE))) ? "" : c
					.getString(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.ORDER_DATE));
			this.saleOrder.total = c.getLong(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.TOTAL));
			this.saleOrder.saleOrderId = c.getLong(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.SALE_ORDER_ID));
			this.saleOrder.synState = c.getInt(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.SYN_STATE));
			this.saleOrder.description = c.getString(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.DESCRIPTION));
			this.saleOrder.approved = c.getInt(c.getColumnIndexOrThrow(SALE_ORDER_TABLE.APPROVED));
			
			String synState = String.valueOf(this.saleOrder.synState);
			if (this.saleOrder.approved == 2){
				// lay luon thong tin description hien thi len view
			}else if (this.saleOrder.approved == 0 ||
					this.saleOrder.approved == 1){//Vansale		
				if(synState.equals(LogDTO.STATE_NEW)) {// Chua gui
					this.saleOrder.description = StringUtil.getString(R.string.TEXT_ORDER_NOT_SEND);
				} else if(synState.equals(LogDTO.STATE_FAIL)) {// Dang gui
					this.saleOrder.description = StringUtil.getString(R.string.TEXT_ORDER_NOT_SEND);
				} else if(synState.equals(LogDTO.STATE_SUCCESS)) {// Da gui
					this.saleOrder.description = StringUtil.getString(R.string.TEXT_WAITING_PROCESS);
				} else if(synState.equals(LogDTO.STATE_INVALID_TIME)) {//Sai thoi gian
					this.saleOrder.description = StringUtil.getString(R.string.TEXT_ORDER_WRONG);				
				} else if(synState.equals(LogDTO.STATE_UNIQUE_CONTRAINTS)) {//Duplicate don hang
					this.saleOrder.description = StringUtil.getString(R.string.TEXT_ORDER_WRONG);				
				}
			}
				
		}
	}

}
