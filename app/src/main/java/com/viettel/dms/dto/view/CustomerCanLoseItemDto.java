/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/**
 * Customer Can Lose Item Dto class
 * CustomerCanLoseItemDto.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:23:52 8 Jan 2014
 */
public class CustomerCanLoseItemDto {
	public String cusCode;
	public String cusName;
	public String cusAdd;
	public String cusLastOrder;

	public CustomerCanLoseItemDto() {
	}

	public void initDataFromCursor(Cursor c) throws Exception {
		
			if (c == null) {
				throw new Exception("CustomerCanLoseItemDto Cursor is empty");
			}

			cusCode = StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_CODE");			
			cusName = StringUtil.getStringFromSQliteCursor(c, "CUSTOMER_NAME");
			cusAdd = StringUtil.getStringFromSQliteCursor(c, "ADDRESS");
			if (StringUtil.isNullOrEmpty(cusAdd)) {
				cusAdd = StringUtil.getStringFromSQliteCursor(c, "STREET");
			}
			
			cusLastOrder = StringUtil.getStringFromSQliteCursor(c, "LAST_APPROVE_ORDER");
	}

}
