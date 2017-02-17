/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

/** 
 * Customer Indebtedness Dto class
 * CustomerIndebtednessDto.java
 * @version: 1.0 
 * @since:  10:24:52 8 Jan 2014
 */
public class CustomerIndebtednessDto {
	public String cusCode;
	public String cusName;
	public String ticketCode;
	public String paidMoney;
	public String debtMoney;
	public CustomerIndebtednessItem item = new CustomerIndebtednessItem();
	
	public class CustomerIndebtednessItem{
		public String number;
		public String invoiceCode;
		public String date;
		public String money;
		public String debtRemain;
		public String status;
		
		public CustomerIndebtednessItem() {
			invoiceCode = "P235456";
			date = "23/4/2012";
			money = "150,000,000";
			debtRemain = "150,000,000";
			status = "Chưa thanh toán";
		}
	}
	
}
