/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

/** 
 * Total Statistics Dto class
 * TotalStatisticsDto.java
 * @version: 1.0 
 * @since:  10:43:03 8 Jan 2014
 */
public class TotalStatisticsDto {
	public String productName;
	public String productCode;
	public String[] productType;
	public String[] productSubType;
	public int quantity;
	public int available_quantity;
	public ArrayList<TotalStatisticsItem> arrList = new ArrayList<TotalStatisticsItem>();
	
	public TotalStatisticsDto() {
	}
}
