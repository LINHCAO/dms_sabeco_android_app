/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.sqllite.db.PRODUCT_TABLE;

/**
 * Thong tin san pham
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class ProductDTO extends AbstractTableDTO {
	public static final String PRODUCT_EMPTY = "0";

	// id san pham
	public int productId;
	// ten san pham
	public String productName;
	// trang thai 1: hoat dong, 0: ngung
	public int status;
	// don vi tinh nho nhat (hop)
	public String uom1;
	// package (thung)
	public String uom2;
	// gia tri quy doi tu UOM2 --> UOM1
	public int convfact;
	// ma nganh hang
	public String categoryCode;
	public String subCategoryId;
	// thuoc tinh cua mat hang
	public String brand;
	// thuoc tinh cua mat hang
	public String flavour;
	// ton kho an toan - chua dung
	public float safetyStock;
	// hoa hong - chua dung
	public String commission;
	// ngay tao
	public String createDate;
	// ngay cap nhat
	public String udpateDate;
	// thuoc tinh cua mat hang
	public float volumn;
	// thuoc tinh cua mat hang
	public float netweight;
	// thuoc tinh cua mat hang
	public float grossWeight;
	// thuoc tinh cua mat hang
	public String packing;
	public int productTypeId;
	// ma mat hang
	public String productCode;
	// nguoi tao
	public String createUser;
	// nguoi cap nhat
	public String udpateUser;
	// loai san pham
	public String productType;
	// sub cat cua sang pham
	public String subCat;
	// don gia
	public double unitPrice;
	// ton kho
	public int inventory;
	// tong hang
	public int totalProduct;

	// has promotion
	public boolean isHasPromotion;
	// has focus programe
	public boolean isHasFocus;
	public boolean isUOM2;

	public ProductDTO() {
		super(TableType.PRODUCT);
	}

	/**
	 * 
	 * init data product
	 * 
	 * @author: YenNTH
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void initDataFromCursor(Cursor c) {
		if (c.getColumnIndex("PRODUCT_ID") >= 0) {
			productId = c.getInt(c.getColumnIndex("PRODUCT_ID"));
		} else {
			productId = 0;
		}
		if (c.getColumnIndex("PRODUCT_CODE") >= 0) {
			productCode = c.getString(c.getColumnIndex("PRODUCT_CODE"));
		} else {
			productCode = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("PRODUCT_NAME") >= 0) {
			productName = c.getString(c.getColumnIndex("PRODUCT_NAME"));
		} else {
			productName = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("UOM1") >= 0) {
			uom1 = c.getString(c.getColumnIndex("UOM1"));
		} else {
			uom1 = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("UOM2") >= 0) {
			uom2 = c.getString(c.getColumnIndex("UOM2"));
		} else {
			uom2 = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("CATEGORY_CODE") >= 0) {
			categoryCode = c.getString(c.getColumnIndex("CATEGORY_CODE"));
		} else {
			categoryCode = Constants.STR_BLANK;
		}
		if (c.getColumnIndex("PRICE") >= 0) {
			unitPrice = c.getDouble(c.getColumnIndex("PRICE"));
		} else {
			unitPrice = 0;
		}
		if (c.getColumnIndex("CONVFACT") >= 0) {
			convfact = c.getInt(c.getColumnIndex("CONVFACT"));
		} else {
			convfact = 1;
		}
		if (c.getColumnIndex("AVAILABLE_QUANTITY") >= 0) {
			totalProduct = c.getInt(c.getColumnIndex("AVAILABLE_QUANTITY"));
			if (totalProduct == 0) {
				totalProduct = 0;
			}
			int b = (totalProduct) % convfact;
			inventory = totalProduct;
			if (b == 0) {
				isUOM2 = true;
			}
		} else {
			totalProduct = 0;
			inventory = 0;
		}
		if(c.getColumnIndex("TT") >= 0) {
			isHasFocus = c.getInt(c.getColumnIndex("TT")) == 1 ? true : false;
		}
		if(c.getColumnIndex("HAS_PROMOTION") >= 0) {
			isHasPromotion = c.getInt(c.getColumnIndex("HAS_PROMOTION")) == 1 ? true : false;
		}
	}

	/**
	 * 
	 * init data cho popup chi tiet san pham
	 * 
	 * @author: HaiTC3
	 * @param cursor
	 * @return: void
	 * @throws:
	 * @since: Feb 22, 2013
	 */
	public void initDataWithCursor(Cursor cursor) {
		if (cursor.getColumnIndex("PRODUCT_ID") >= 0) {
			productId = cursor.getInt(cursor.getColumnIndex("PRODUCT_ID"));
		} else {
			productId = 0;
		}
		if (cursor.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE) >= 0) {
			productCode = cursor.getString(cursor.getColumnIndex(PRODUCT_TABLE.PRODUCT_CODE));
		} else {
			productCode = Constants.STR_BLANK;
		}
		if (cursor.getColumnIndex(PRODUCT_TABLE.PRODUCT_NAME) >= 0) {
			productName = cursor.getString(cursor.getColumnIndex(PRODUCT_TABLE.PRODUCT_NAME));
		} else {
			productName = Constants.STR_BLANK;
		}
		if (cursor.getColumnIndex(PRODUCT_TABLE.CONVFACT) >= 0) {
			convfact = cursor.getInt(cursor.getColumnIndex(PRODUCT_TABLE.CONVFACT));
		} else {
			convfact = 0;
		}
		if (cursor.getColumnIndex(PRODUCT_TABLE.CATEGORY_CODE) >= 0) {
			categoryCode = cursor.getString(cursor.getColumnIndex(PRODUCT_TABLE.CATEGORY_CODE));
		} else {
			categoryCode = Constants.STR_BLANK;
		}
		if (cursor.getColumnIndex(PRODUCT_TABLE.UOM1) >= 0) {
			uom1 = (cursor.getString(cursor.getColumnIndex(PRODUCT_TABLE.UOM1)));
		} else {
			uom1 = Constants.STR_BLANK;
		}
		if (cursor.getColumnIndex(PRODUCT_TABLE.UOM2) >= 0) {
			uom2 = (cursor.getString(cursor.getColumnIndex(PRODUCT_TABLE.UOM2)));
		} else {
			uom2 = Constants.STR_BLANK;
		}
	}
}
