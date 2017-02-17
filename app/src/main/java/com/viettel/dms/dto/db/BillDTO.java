/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;
/**
 * Bill dto class
 * BillDTO.java
 * @version: 1.0 
 * @since:  10:12:08 8 Jan 2014
 */
public class BillDTO {
	public String PRO_CODE = "";//ma sp
	public String PRO_NAME = "";//ten sp
	public String REMAINING = "";//ton kho
	public long PRICE = 0;//gia
	public boolean isChose = false;
	public String PROMOTION_DETAIL = "";//CTKM
	
	public String insertSql(){
		String sql = "insert into SALES_ORDER (SALE_ORDER_ID,SHOP_ID, ORDER_NUMBER, ORDER_DATE, CUSTOMER_ID, STAFF_ID," +
				"DELIVERY_CODE, AMOUNT, TOTAL, CREATE_USER, CREATE_DATE) values (SALES_ORDER_SEQ.nextval,?,?,sysdate,?,?,?,?,?,?,sysdate)";
		return sql;
	}
	
	public ArrayList<String> generageInsertBillSql(){
		ArrayList<String> result = new ArrayList<String>();
		String sql = "insert into SALES_ORDER (SALE_ORDER_ID,SHOP_ID, ORDER_NUMBER, ORDER_DATE, CUSTOMER_ID, STAFF_ID," +
				"DELIVERY_CODE, AMOUNT, TOTAL, CREATE_USER, CREATE_DATE) values (SALES_ORDER_SEQ.nextval,?,?,sysdate,?,?,?,?,?,?,sysdate)";
		
		StringBuffer paraBuff = new StringBuffer();
//		paraBuff.append(this.SHOP_ID);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.ORDER_NUMBER);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.CUSTOMER_ID);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.STAFF_ID);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.DELIVERY_CODE);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.AMOUNT);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.TOTAL);
//		paraBuff.append(com.viettel.vinamilk.constants.Constants.STR_SPLIT_SQL);
//		paraBuff.append(this.CREATE_USER);
//		
		result.add(sql);
		result.add(paraBuff.toString());
		return result;
	}
}
