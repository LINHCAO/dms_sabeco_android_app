/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.util.StringUtil;

/** 
 * Wrong Plan Customer DTO class
 * WrongPlanCustomerDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:44:10 8 Jan 2014
 */
public class WrongPlanCustomerDTO {
	public ArrayList<WrongPlanCustomerItem> arrWrong = new ArrayList<WrongPlanCustomerDTO.WrongPlanCustomerItem>();

	public WrongPlanCustomerItem newWrongPlanCustomerItem() {
		return new WrongPlanCustomerItem();
	}

	public class WrongPlanCustomerItem {
		public CustomerDTO aCustomer = new CustomerDTO();
		public String plan;
		public String visitedTime;
		public long quantityInDay;
		public String cusCodeName;

		public WrongPlanCustomerItem() {
		}

		public void initWrongPlanCusItem(Cursor c) {
			try {
				if (c == null) {
					throw new Exception("Cursor is empty");
				}
			} catch (Exception ex) {
			}
			if (c.getColumnIndex("CUSTOMER_ID") > -1) {
				aCustomer.customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
			}
			if (c.getColumnIndex("CUSTOMER_CODE_NAME") > -1) {
				cusCodeName = c.getString(c.getColumnIndex("CUSTOMER_CODE_NAME"));
			}
			if (c.getColumnIndex("ADDRESS") > -1) {
				aCustomer.address = c.getString(c.getColumnIndex("ADDRESS"));
			}
			if (!StringUtil.isNullOrEmpty(aCustomer.address)) {
				aCustomer.street = aCustomer.address;
			} else {
				if (c.getColumnIndex("STREET") > -1) {
					aCustomer.street = c.getString(c.getColumnIndex("STREET"));
				}
			}
			if (c.getColumnIndex("WEEK_PLAN") > -1) {
				plan = c.getString(c.getColumnIndex("WEEK_PLAN"));
				if (!StringUtil.isNullOrEmpty(plan) && plan.substring(0, 1).equals(",")) {
					plan = plan.substring(1, plan.length());
				}
			}
			if (c.getColumnIndex("QUANTITY_IN_DATE") > -1) {
				quantityInDay = c.getLong(c.getColumnIndex("QUANTITY_IN_DATE"));
			}
			if (c.getColumnIndex("START_TIME") > -1) {
				visitedTime = c.getString(c.getColumnIndex("START_TIME"));
			}
		}
	}
}
