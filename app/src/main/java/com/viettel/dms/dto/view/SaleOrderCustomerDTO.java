/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.SaleOrderDTO;

/**
 * Thong tin cua mot don hang
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 */
@SuppressWarnings("serial")
public class SaleOrderCustomerDTO implements Serializable{
	SaleOrderDTO saleOder;//thong tin don hang trong bang: SALE_ORDER
	int SKU = 0;//so luong product trong mot don hang
	
	public SaleOrderCustomerDTO() {
		saleOder = new SaleOrderDTO();
	}

	public SaleOrderDTO getSaleOder() {
		return saleOder;
	}

	public void setSaleOder(SaleOrderDTO saleOder) {
		this.saleOder = saleOder;
	}

	public int getSKU() {
		return SKU;
	}

	public void setSKU(int sKU) {
		SKU = sKU;
	}

	/**
	 * Thong tin chung
	 * @author : BangHN
	 * since : 2:36:49 PM
	 */
	public static SaleOrderCustomerDTO initOrderFromCursor(Cursor cursor) {
		if(cursor != null){
			SaleOrderCustomerDTO dto = new SaleOrderCustomerDTO();
			dto.saleOder.saleOrderId = cursor.getLong(cursor.getColumnIndex("SALE_ORDER_ID"));
			dto.saleOder.orderDate = cursor.getString(cursor.getColumnIndex("ORDER_DATE"));
			dto.saleOder.orderNumber = cursor.getString(cursor.getColumnIndex("ORDER_NUMBER"));
			dto.saleOder.total = cursor.getLong(cursor.getColumnIndex("TOTAL"));
//			dto.saleOder.isSend = cursor.getInt(cursor.getColumnIndex("IS_SEND"));
			dto.SKU = cursor.getInt(cursor.getColumnIndex("SKU"));
			return dto;
		}
		return null;
	}
}
