/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/** 
 * Total Statistics Item class
 * TotalStatisticsItem.java
 * @version: 1.0 
 * @since:  10:43:22 8 Jan 2014
 */
public class TotalStatisticsItem {
	public String productId;
	public String productCode;
	public String productName;
	public String productPromotion;
	public String quantity;
	public String available_quantity;

	public TotalStatisticsItem() {
	}

	public void initDataFromCursor(Cursor c) {
		try {
			if (c == null) {
				throw new Exception("Cursor is empty");
			}
			productCode = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow("PRODUCT_CODE"))) ? "" : c
					.getString(c.getColumnIndexOrThrow("PRODUCT_CODE"));
			productName = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow("PRODUCT_NAME"))) ? "" : c
					.getString(c.getColumnIndexOrThrow("PRODUCT_NAME"));
//			productPromotion = StringUtil.isNullOrEmpty(c.getString(c
//					.getColumnIndexOrThrow("PROMOTION"))) ? "" : c
//					.getString(c.getColumnIndexOrThrow("PROMOTION"));
			quantity = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow("QUANTITY"))) ? "0" : c
					.getString(c.getColumnIndexOrThrow("QUANTITY"));
			available_quantity = StringUtil.isNullOrEmpty(c.getString(c
					.getColumnIndexOrThrow("AVAILABLE_QUANTITY"))) ? "0" : c
					.getString(c.getColumnIndexOrThrow("AVAILABLE_QUANTITY"));
		} catch (Exception e) {
		}
	}
}
