/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * Customer List DTO class
 * CustomerListDTO.java
 * @version: 1.0 
 * @since:  10:25:20 8 Jan 2014
 */
@SuppressWarnings("serial")
public class CustomerListDTO implements Serializable {
	public int totalCustomer;
	public ArrayList<CustomerListItem> cusList;
	//public double distance;
	public int curPage = -1;
	
	public CustomerListDTO() {
		cusList = new ArrayList<CustomerListItem>();
	}
}
