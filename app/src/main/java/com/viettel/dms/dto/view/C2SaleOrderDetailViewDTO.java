/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.C2SaleOrderDetailDTO;
import com.viettel.dms.util.StringUtil;

/** 
 * C2 Sale Order Detail View DTO class
 * C2SaleOrderDetailViewDTO.java
 * @version: 1.0 
 * @since:  10:21:54 8 Jan 2014
 */
public class C2SaleOrderDetailViewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public C2SaleOrderDetailDTO orderDetail;
	public String productCode;
	public String productName;
	public C2SaleOrderDetailViewDTO() {
		// TODO Auto-generated constructor stub
		orderDetail = new C2SaleOrderDetailDTO();
	}

	public void initFromCursor(Cursor c) {
		productCode = StringUtil.getStringFromSQliteCursor(c, "PRODUCT_CODE");
		productName = StringUtil.getStringFromSQliteCursor(c, "PRODUCT_NAME");
		orderDetail.productId = StringUtil.getLongFromSQliteCursor(c, "PRODUCT_ID");
	}
}
