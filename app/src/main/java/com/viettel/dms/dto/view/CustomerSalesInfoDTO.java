/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.CustomerCatLevelDTO;

/**
 * thong tin data cho man hinh doanh so khach hang trong xem chi tiet
 * @author : BangHN
 * since : 10:51:52 AM
 * version :
 */
@SuppressWarnings("serial")
public class CustomerSalesInfoDTO implements Serializable{
	//tong so don hang gan day
	int numLastOrderCustomer = 0;
	//danh sach don hang gan day
	ArrayList<SaleOrderCustomerDTO> lastOrderCustomer = new ArrayList<SaleOrderCustomerDTO>();
	ArrayList<CustomerCatLevelDTO> listCustomerCatLevel = new ArrayList<CustomerCatLevelDTO>();
	
	
	public CustomerSalesInfoDTO() {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<CustomerCatLevelDTO> getListCustomerCatLevel() {
		return listCustomerCatLevel;
	}

	public void setListCustomerCatLevel(
			ArrayList<CustomerCatLevelDTO> listCustomerCatLevel) {
		this.listCustomerCatLevel = listCustomerCatLevel;
	}
	
	public int getNumLastOrderCustomer() {
		return numLastOrderCustomer;
	}

	public void setNumLastOrderCustomer(int numLastOrderCustomer) {
		this.numLastOrderCustomer = numLastOrderCustomer;
	}

	public ArrayList<SaleOrderCustomerDTO> getLastOrderCustomer() {
		return lastOrderCustomer;
	}

	public void setLastOrderCustomer(
			ArrayList<SaleOrderCustomerDTO> lastOrderCustomer) {
		this.lastOrderCustomer = lastOrderCustomer;
	}
	
	
}
