/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

/** 
 * Prog Report Pro Disp Detail Sale DTO class
 * ProgReportProDispDetailSaleDTO.java
 * @version: 1.0 
 * @since:  10:36:56 8 Jan 2014
 */
public class ProgReportProDispDetailSaleDTO {
	
	public long RemainSaleTotal;

	public ArrayList<ProgReportProDispDetailSaleRowDTO> listItem;
	
	// tong so item
	public int totalItem = 0;

	public  ProgReportProDispDetailSaleDTO(){
		listItem = new ArrayList<ProgReportProDispDetailSaleRowDTO>();
	}
	public void addItem(ProgReportProDispDetailSaleRowDTO c) {
		listItem.add(c);
		RemainSaleTotal +=c.RemainSale;
	}
}
