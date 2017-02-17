/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.CustomerDTO;

/**
 * 
 * dto thong tin kh
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVCustomerListDTO {
	public int totalList;
	//ma khach hang
	public String codeCus;
	//ten hoac dia chi khach hang
	public String nameCus;

	public List<CustomerDTO> listCustomer; 
	public int totalCustomer;

	public TBHVCustomerListDTO() {
		listCustomer = new ArrayList<CustomerDTO>();
	}

}
