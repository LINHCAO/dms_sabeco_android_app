/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

/**
 * 
 * dto cua 3 textview de thuc hien autoComplete
 * 
 * @author: HieuNH6
 * @version: 1.1
 * @since: 1.0
 */
public class AutoCompleteFindProductSaleOrderDetailViewDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ma san pham
	public ArrayList<String> strInputOrderCode = new ArrayList<String>();
	// ten san pham
	public ArrayList<String> strInputOrderName = new ArrayList<String>();
	// ma CTKM
	public ArrayList<String> strInputCTKMCode = new ArrayList<String>();
	// ten san pham khong dau
	public ArrayList<String> strInputOrderNameTextUnSigned = new ArrayList<String>();

	public AutoCompleteFindProductSaleOrderDetailViewDTO() {

	}

	/**
	 * 
	 * init with cursor
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 9, 2013
	 */
	public void init(Cursor c) {
		if (c.getColumnIndex("PRODUCT_CODE") >= 0) {
			strInputOrderCode.add(c.getString(c.getColumnIndex("PRODUCT_CODE")));
		}
		if (c.getColumnIndex("PRODUCT_NAME") >= 0) {
			strInputOrderName.add(c.getString(c.getColumnIndex("PRODUCT_NAME")));
		}
		if (c.getColumnIndex("PRODUCT_NAME_TEXT") >= 0) {
			strInputOrderNameTextUnSigned.add(c.getString(c.getColumnIndex("PRODUCT_NAME_TEXT")));
		}
		if (c.getColumnIndex("PROMOTION_CODE") >= 0) {
			strInputCTKMCode.add(c.getString(c.getColumnIndex("PROMOTION_CODE")));
		}
	}
}
