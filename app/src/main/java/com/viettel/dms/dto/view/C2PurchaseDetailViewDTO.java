/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.C2PurchaseDetailDTO;
import com.viettel.dms.util.StringUtil;

/** 
 * C2 Purchase Detail View DTO class
 * C2PurchaseDetailViewDTO.java
 * @version: 1.0 
 * @since:  10:21:26 8 Jan 2014
 */
public class C2PurchaseDetailViewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public C2PurchaseDetailDTO orderDetail;
	public String productCode;
	public String productName;
	public C2PurchaseDetailViewDTO() {
		orderDetail = new C2PurchaseDetailDTO();
	}

	public void initFromCursor(Cursor c) {
		productCode = StringUtil.getStringFromSQliteCursor(c, "PRODUCT_CODE");
		productName = StringUtil.getStringFromSQliteCursor(c, "PRODUCT_NAME");
		orderDetail.productId = StringUtil.getLongFromSQliteCursor(c, "PRODUCT_ID");
	}
}
