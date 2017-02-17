/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.dto.db.ChannelTypeDTO;
import com.viettel.dms.dto.db.CustomerDTO;

/**
 * dto thong tin cho chi tiet khach hang
 * @author : BangHN
 * since : 4:57:53 PM
 * version :
 */
public class CustomerInfoDTO {
	private CustomerDTO customer;//chi tiet khach hang
	private ChannelTypeDTO customerType;//
	public int sku = 0;//so mat hang khac nhau ban duoc trong ngay
	public int saleOrdersInMonth = 0;//so don hang trong thang
	public long amountInMonth = 0; //tong so tien cac don hang ban trong thang hien tai
	public long amountInOneMonthAgo = 0; //tong so tien cac don hang ban trong thang  - 1
	public long amountInTwoMonthAgo = 0; //tong so tien cac don hang ban trong thang - 2
	public long amountInThreeMonthAgo = 0; //tong so tien cac don hang ban trong thang  - 3

	//chuong trinh khach hang tham gia
	private ArrayList<CustomerProgrameDTO> listCustomerPrograme;
	
	public int numLastOrdersCustomer = 0;
	//danh sach don hang gan day
	public ArrayList<SaleOrderCustomerDTO> listOrderCustomer;
	

	public CustomerInfoDTO() {
		// TODO Auto-generated constructor stub
	}


	public CustomerDTO getCustomer() {
		return customer;
	}


	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}


	public ChannelTypeDTO getCustomerType() {
		return customerType;
	}


	public void setCustomerType(ChannelTypeDTO customerType) {
		this.customerType = customerType;
	}


	public ArrayList<CustomerProgrameDTO> getListCustomerPrograme() {
		return listCustomerPrograme;
	}

	public void setListCustomerPrograme(
			ArrayList<CustomerProgrameDTO> listCustomerPrograme) {
		this.listCustomerPrograme = listCustomerPrograme;
	}
	
	
	/**
	 * parse thong tin co ban cua khach hang
	 * @author : BangHN
	 * since : 1.0
	 */
	public void parseCustomerInfo(Cursor c){
		if(customer == null)
			customer = new CustomerDTO();
		if(customerType == null)
			customerType = new ChannelTypeDTO();
		
		customer.initLogDTOFromCursor(c);
		customerType.initCustomerTypeDTOFromCursor(c);
		
		sku = (c.getInt(c.getColumnIndex("NUM_SKU")));
		saleOrdersInMonth = (c.getInt(c.getColumnIndex("NUM_ORDER_IN_MONTH")));
		amountInMonth = (c.getLong(c.getColumnIndex("AVG_IN_MONTH")));
	}
}
