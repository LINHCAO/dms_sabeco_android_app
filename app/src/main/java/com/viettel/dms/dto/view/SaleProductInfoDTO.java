/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.dto.db.ProductDTO;

/**
 * thong tin mot sku khi duoc ban, su dung cho man hinh thong ke don tong
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class SaleProductInfoDTO {
	// thong tin cua san pham
	public ProductDTO productInfo;
	// so luong san pham
	public int numberProduct;
	// number product format
	public String numberProductFormat;
	// tong tien da ban
	public long totalAmountSold;
	// so luong san pham ton kho dau ngay
	public int numProductRemainFirstDay;
	// so luong san pham ton kho dau ngay format
	public String numProductRemainFirstDayFormat;
	// so luong san pham con lai
	public int numProductRemain;
	// so luong san pham con lai format
	public String numProductRemainFormat;
	// so luong san pham dat them
	public int numProductAddOrder;
	// so luong san pham dat them format
	public String numProductAddOrderFormat;

	/**
	 * struct
	 */
	public SaleProductInfoDTO() {
		productInfo = new ProductDTO();
		numberProduct = 0;
		totalAmountSold = 0;
		numberProductFormat = "0/0";
		numProductRemainFirstDay = 0;
		numProductRemainFirstDayFormat = "0/0";
		numProductRemain = 0;
		numProductRemainFormat = "0/0";
		numProductAddOrder = 0;
		numProductAddOrderFormat = "0/0";
	}

	/**
	 * 
	 * init data for pre sale from cursor
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 19, 2012
	 */
	public void initDataPreSaleFromCursor(Cursor c) {
		productInfo.initDataFromCursor(c);
		if (c.getColumnIndex("PRODUCT_NUMBER") >= 0) {
			numberProduct = c.getInt(c.getColumnIndex("PRODUCT_NUMBER"));
		} else {
			numberProduct = 0;
		}

		int a = numberProduct / productInfo.convfact;
		int b = numberProduct % productInfo.convfact;
		numberProductFormat = Integer.toString(a) + "/" + Integer.toString(b);

		if (c.getColumnIndex("AMOUNT_TOTAL") >= 0) {
			totalAmountSold = c.getLong(c.getColumnIndex("AMOUNT_TOTAL"));
		} else {
			totalAmountSold = 0;
		}
	}

	/**
	 * 
	 * init data for van sale from cursor
	 * 
	 * @param c
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 17, 2013
	 */
	public void initDataVanSaleFromCursor(Cursor c) {
		productInfo.initDataFromCursor(c);
		int a = 0;
		int b = 0;
		// // tong so luong san pham va tong tien da ban
		// if (c.getColumnIndex("TOTAL_PRODUCT") >= 0) {
		// numberProduct = c.getInt(c.getColumnIndex("TOTAL_PRODUCT"));
		// } else {
		// numberProduct = 0;
		// }
		//
		// int a = numberProduct / productInfo.convfact;
		// int b = numberProduct % productInfo.convfact;
		// numberProductFormat = Integer.toString(a) + "/" +
		// Integer.toString(b);
		//
		// if (c.getColumnIndex("TOTAL_AMOUNT") >= 0) {
		// totalAmountSold = c.getLong(c.getColumnIndex("TOTAL_AMOUNT"));
		// } else {
		// totalAmountSold = 0;
		// }

		// so luong ton kho / con lai
		if (c.getColumnIndex("TOTAL_PRODUCT_REMAIN") >= 0) {
			numProductRemain = c.getInt(c.getColumnIndex("TOTAL_PRODUCT_REMAIN"));
		} else {
			numProductRemain = 0;
		}
		a = numProductRemain / productInfo.convfact;
		b = numProductRemain % productInfo.convfact;
		numProductRemainFormat = Integer.toString(a) + "/" + Integer.toString(b);

		// so luong da ban
		if (c.getColumnIndex("TOTAL_PRODUCT_SOLD") >= 0) {
			numberProduct = c.getInt(c.getColumnIndex("TOTAL_PRODUCT_SOLD"));
		} else {
			numberProduct = 0;
		}
		a = numberProduct / productInfo.convfact;
		b = numberProduct % productInfo.convfact;
		numberProductFormat = Integer.toString(a) + "/" + Integer.toString(b);

		// so luong ton kho dau ki
		numProductRemainFirstDay = numProductRemain + numberProduct;
		a = numProductRemainFirstDay / productInfo.convfact;
		b = numProductRemainFirstDay % productInfo.convfact;
		numProductRemainFirstDayFormat = Integer.toString(a) + "/" + Integer.toString(b);

		// so luong dat them
		if (c.getColumnIndex("TOTAL_PRODUCT_ADD_ORDER") >= 0) {
			numProductAddOrder = c.getInt(c.getColumnIndex("TOTAL_PRODUCT_ADD_ORDER"));
		} else {
			numProductAddOrder = 0;
		}
		a = numProductAddOrder / productInfo.convfact;
		b = numProductAddOrder % productInfo.convfact;
		numProductAddOrderFormat = Integer.toString(a) + "/" + Integer.toString(b);

		// tong tien da ban
		if (c.getColumnIndex("TOTAL_AMOUNT") >= 0) {
			totalAmountSold = c.getLong(c.getColumnIndex("TOTAL_AMOUNT"));
		} else {
			totalAmountSold = 0;
		}

	}

}
