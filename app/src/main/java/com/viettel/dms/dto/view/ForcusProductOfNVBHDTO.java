/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.PRODUCT_TABLE;

/**
 * thong tin san pham mat hang trong tam cua nhan vien ban hang
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class ForcusProductOfNVBHDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id of product
	public long productId;
	// ma mat hang
	public String productCode;
	// ten mat hang
	public String productName;
	// nganh hang
	public String productIndustry;
	// loai mat hang trong tam
	public String typeProductFocus;
	// doanh so ke hoach
	public long planAmount;
	// doanh so thuc hien
	public long doneAmount;
	// doanh so con lai
	public long remainAmount;
	// tien do
	public int progress;

	/**
	 * init object
	 */
	public ForcusProductOfNVBHDTO() {
		productCode = Constants.STR_BLANK;
		productName = Constants.STR_BLANK;
		productIndustry = Constants.STR_BLANK;
		typeProductFocus = Constants.STR_BLANK;
		planAmount = 0;
		doneAmount = 0;
		remainAmount = 0;
		progress = 0;
		productId = 0;
	}

	/**
	 * 
	 * khoi tao doi tuong
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void initDateWithCursor(Cursor c) {
		// id san pham
		if (c.getColumnIndex(PRODUCT_TABLE.PRODUCT_ID) >= 0) {
			productId = c.getLong(c.getColumnIndex(PRODUCT_TABLE.PRODUCT_ID));
		} else {
			productId = 0;
		}
		// code san pham
		if (c.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE) >= 0) {
			productCode = c.getString(c
					.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE));
		} else {
			productCode = "";
		}
		// ten san pham
		if (c.getColumnIndex(PRODUCT_TABLE.PRODUCT_NAME) >= 0) {
			productName = c.getString(c
					.getColumnIndex(PRODUCT_TABLE.PRODUCT_NAME));
		} else {
			productName = "";
		}
		// nganh hang
		if (c.getColumnIndex("PRODUCT_INFO_NAME") >= 0) {
			productIndustry = c
					.getString(c.getColumnIndex("PRODUCT_INFO_NAME"));
		} else {
			productIndustry = "";
		}
		// loai mat hang trong tam
		if (c.getColumnIndex("TYPE_PRODUCT_FORCUS") >= 0) {
			typeProductFocus = c.getString(c
					.getColumnIndex("TYPE_PRODUCT_FORCUS"));
		} else {
			typeProductFocus = "";
		}
		// doanh so ke hoach
		if (c.getColumnIndex("PLAN_AMOUNT") >= 0) {
			planAmount = c.getLong(c.getColumnIndex("PLAN_AMOUNT"));
			planAmount = Math.round((double)planAmount / 1000);
		} else {
			planAmount = 0;
		}
		// doanh so thuc hien
		if (c.getColumnIndex("AMOUNT") >= 0) {
			doneAmount = c.getLong(c.getColumnIndex("AMOUNT"));
			doneAmount = Math.round((double)doneAmount / 1000);
		} else {
			doneAmount = 0;
		}
		// doanh so con lai
		remainAmount = (planAmount - doneAmount) >= 0 ? (planAmount - doneAmount)
				: 0;

		// tien do
		if (doneAmount > 0) {
			progress = (int) (doneAmount * 100 / (planAmount <= 0 ? doneAmount
					: planAmount));
		} else {
			if (planAmount == 0) {
				progress = 100;
			} else {
				progress = 0;
			}
		}
	}
}
