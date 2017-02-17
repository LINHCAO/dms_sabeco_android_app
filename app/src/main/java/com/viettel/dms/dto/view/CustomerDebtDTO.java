/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.DebitDetailDTO;
import com.viettel.dms.util.StringUtil;


/**
 * DTO dinh nghia cong no khach hang
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class CustomerDebtDTO implements Serializable{
	//cong no khach hang
	public DebitDetailDTO debitDetail;
	//chi tiet khach hang
	public CustomerDTO customer;
	//so ngay cong no khach hang
	public int remainDay = 0;
	public CustomerDebtDTO(){
		customer = new CustomerDTO();
		debitDetail = new DebitDetailDTO();
	}

	/**
	* Parse du lieu sau khi query tu DB
	* @author: BangHN
	* @param cursor
	* @return
	* @return: CustomerDebtDTO
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	
	public CustomerDebtDTO initCustomerDebtFromCursor(Cursor c) {
		if(c != null){
			if (c.getColumnIndex("SHORT_CODE") >= 0) {
				this.customer.shortCode = c.getString(c.getColumnIndex("SHORT_CODE"));
			} 
			if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
				this.customer.customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
			} 
			if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
				this.customer.customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			} 
			if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
				this.customer.customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			} 
			if(c.getColumnIndex("ADDRESS") >= 0) {
				this.customer.address = c.getString(c.getColumnIndex("ADDRESS"));
			}
			//street = housenumber + street
			if (StringUtil.isNullOrEmpty(this.customer.address)) {
				this.customer.address = c.getString(c.getColumnIndex("STREET_HOUSE"));
			}
			if (c.getColumnIndex("TOTAL") >= 0) {
				this.debitDetail.total = c.getLong(c.getColumnIndex("TOTAL"));
			} 
			if (c.getColumnIndex("AMOUNT") >= 0) {
				this.debitDetail.amount = c.getLong(c.getColumnIndex("AMOUNT"));
			} 
			if (c.getColumnIndex("TOTAL_PAY") >= 0) {
				this.debitDetail.totalPay = c.getLong(c.getColumnIndex("TOTAL_PAY"));
			} 
			if (c.getColumnIndex("REMAIN") >= 0) {
				this.debitDetail.remain = c.getLong(c.getColumnIndex("REMAIN"));
			} 
			if (c.getColumnIndex("DEBIT_ID") >= 0) {
				this.debitDetail.debitID = c.getLong(c.getColumnIndex("DEBIT_ID"));
			} 
			if (c.getColumnIndex("DEBIT_DETAIL_ID") >= 0) {
				this.debitDetail.debitDetailID = c.getLong(c.getColumnIndex("DEBIT_DETAIL_ID"));
			} 
			if (c.getColumnIndex("REMAIN_DAY") >= 0) {
				this.remainDay = c.getInt(c.getColumnIndex("REMAIN_DAY"));
			} 		
		}
		return null;
	}
	
	
	
}
