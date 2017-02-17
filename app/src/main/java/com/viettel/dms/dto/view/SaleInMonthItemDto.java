/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/** 
 * Sale In Month Item Dto class
 * SaleInMonthItemDto.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:39:48 8 Jan 2014
 */
public class SaleInMonthItemDto {
	public String month;
	public String numCustomer;
	public int noPSDS;
	public long revenue;
	public float numSKU;
	public int rptSaleHisId;

	public SaleInMonthItemDto() {
	}

	public void initDataFromCursor(Cursor c) throws Exception {
		
			if (c == null) {
				throw new Exception("Cursor is empty");
			}

			month = StringUtil.getStringFromSQliteCursor(c, "MONTH");
			numCustomer = StringUtil.getStringFromSQliteCursor(c, "NUM_CUSTOMER");
			noPSDS = (int)StringUtil.getLongFromSQliteCursor(c, "NUM_CUST_NOT_ORDER");
			revenue = StringUtil.getLongFromSQliteCursor(c, "TOTAL_VALUE_OF_ORDER");
			numSKU = StringUtil.getFloatFromSQliteCursor(c, "NUM_SKU");
			rptSaleHisId = (int)StringUtil.getLongFromSQliteCursor(c, "ID");
	}
}
