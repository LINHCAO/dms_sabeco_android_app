/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;
 
/**
 * Thong tin cham trung bay cua 1 san pham trong chuong trinh
 * 
 * VoteDisplayProductDTO.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  14:08:23 09-05-2014
 */
public class VoteDisplayProductDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ma san pham
	public int productID;
	// ma san pham
	public String productCode;
	// ten san pham
	public String productName;
	// so luong
	public String numberProduct;
	// so luong cham
	public int voteNumber;
	// ten dong tong
	public String totalName;
	
	public VoteDisplayProductDTO(){
		productCode = "";
		productName = "";
		numberProduct = "";
		voteNumber = -1; 
		productID = -1;
	}
	
	/**
	 * 
	*  init vote display product
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initVoteDisplayProduct(Cursor c){

		if (c.getColumnIndex("PRODUCT_CODE") >= 0) {
			productCode = c.getString(c.getColumnIndex("PRODUCT_CODE"));
		} else {
			productCode = "";
		}

		if (c.getColumnIndex("PRODUCT_NAME") >= 0) {
			productName = c.getString(c.getColumnIndex("PRODUCT_NAME"));
			if(productName.equals("TOTAL_PRODUCT")){
				totalName = StringUtil.getString(R.string.TEXT_TOTAL_PRODUCT);
			}else if (productName.equals("TOTAL_NOT")) {
				totalName = StringUtil.getString(R.string.TEXT_TOTAL_PRODUCT_NOT_YET);
			}
		} else {
			productName = "";
		}

		if (c.getColumnIndex("QUANTITY") >= 0 && !StringUtil.isNullOrEmpty(c.getString(c.getColumnIndex("QUANTITY")))) {
			numberProduct = StringUtil.parseAmountMoney(String.valueOf(c.getInt(c.getColumnIndex("QUANTITY"))));
		} else {
			numberProduct = "";
		}

		if (c.getColumnIndex("VOTENUMBER") >= 0) {
			voteNumber = c.getInt(c.getColumnIndex("VOTENUMBER"));
		} else {
			voteNumber = 0;
		}
		if (c.getColumnIndex("PRODUCT_ID") >= 0) {
			productID = c.getInt(c.getColumnIndex("PRODUCT_ID"));
		} else {
			productID = -1;
		}

	}
}
