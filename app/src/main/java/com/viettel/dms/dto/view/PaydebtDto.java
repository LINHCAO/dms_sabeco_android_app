/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

/** 
 * Paydebt Dto class
 * PaydebtDto.java
 * @version: 1.0 
 * @since:  10:36:13 8 Jan 2014
 */
public class PaydebtDto {
	public String cusCode;
	public String cusName;
	public String ticketCode;
	public String paidMoney;
	public String debtMoney;
	public String contractNum;
	public String date;
	public String invoiceValue;
	public PaydebtItem item = new PaydebtItem();
	
	public class PaydebtItem{
		public String number;
		public String invoiceCode;
		public String date;
		public String money;
		
		public PaydebtItem() {
			invoiceCode = "P235456";
			date = "23/4/2012";
			money = "150,000,000";
		}
	}
}
