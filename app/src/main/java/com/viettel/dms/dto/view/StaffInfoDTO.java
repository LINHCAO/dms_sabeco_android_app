/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * Staff Info DTO class
 * StaffInfoDTO.java
 * @version: 1.0 
 * @since:  10:41:18 8 Jan 2014
 */
@SuppressWarnings("serial")
public class StaffInfoDTO implements Serializable {
	public int totalCusCanLose;
	public ArrayList<SaleInMonthItemDto> arrSaleProgress;
	public ArrayList<CustomerCanLoseItemDto> arrCusCanLose;

	public StaffInfoDTO() {
		arrSaleProgress = new ArrayList<SaleInMonthItemDto>();
		arrCusCanLose = new ArrayList<CustomerCanLoseItemDto>();

	}
}
