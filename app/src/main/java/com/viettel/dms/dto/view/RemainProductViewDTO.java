/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/**
 * Thong tin kiem ton
 * RemainProductViewDTO.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  20:03:33 12-12-2013
 */
@SuppressWarnings("serial")
public class RemainProductViewDTO implements Serializable{

	public int stt;
	public int productId;
	public String productName;
	public long price;
	public String suggestedPrice = "";
	public long priceNotVat;
	public long vat;
	public float grossWeight;
	public long priceId;
	public int quantity;
	public String remainNumber="";
	public String hintNumber="0";
	public String focusProgrameDetailId;
	public String promotionProgrameDetailId;
	public boolean isCheck = false;
	public String productCode;
	public String promotionProgramCode;
	public boolean hasPromotionProgrameCode = false;
	public int quantityRemain;//so luong ton kho cua mat hang
	public int hasRemain;
	public long CUSTOMER_ID;
	public long STAFF_ID;
	public long saleOrderId;
	public int convfact;
	public String uom;
	public int sign;
	public int sale;

	public static List<RemainProductViewDTO> initListDataFromCursor(Cursor cursor)
			throws Exception {

		List<RemainProductViewDTO> listData = new ArrayList<RemainProductViewDTO>();
		if (cursor == null) {
			throw new Exception("Cursor is empty");
		}

		if (cursor.moveToFirst()) {
			do {
				RemainProductViewDTO rp = new RemainProductViewDTO();
				rp.initDataFromCursor(cursor);
				listData.add(rp);

			} while (cursor.moveToNext());
		}

		return listData;
	}
	
	public void initDataFromCursor(Cursor c)
			throws Exception {
		productId= c.getInt(c.getColumnIndexOrThrow("PRODUCT_ID"));
		productCode = c.getString(c.getColumnIndexOrThrow("PRODUCT_CODE"));
		productName = c.getString(c.getColumnIndexOrThrow("PRODUCT_NAME"));
		uom = StringUtil.getStringFromSQliteCursor(c, "UNIT");
		
		price = StringUtil.getLongFromSQliteCursor(c, "PRICE");
		suggestedPrice = String.valueOf(price);
		priceId = c.getInt(c.getColumnIndexOrThrow("PRICE_ID"));
		quantityRemain = c.getInt(c.getColumnIndexOrThrow("QUANTITY_REMAIN"));
		priceNotVat = c.getLong(c.getColumnIndexOrThrow("PRICE_NOT_VAT"));
		vat = c.getLong(c.getColumnIndexOrThrow("VAT"));
		grossWeight = c.getFloat(c.getColumnIndexOrThrow("GROSS_WEIGHT"));
	}


	@Override
	public boolean equals(Object o) {
		// TODO Auto-generated method stub
		if (productId == ((RemainProductViewDTO)o).productId)
			return true;
		return false;
	}

}
