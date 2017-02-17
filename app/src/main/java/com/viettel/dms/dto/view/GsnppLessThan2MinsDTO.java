/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class GsnppLessThan2MinsDTO {
	public boolean isLessThan2Min;
	public ArrayList<LessThan2MinsItem> arrList;

	public GsnppLessThan2MinsDTO() {
		arrList = new ArrayList<GsnppLessThan2MinsDTO.LessThan2MinsItem>();
	}

	public LessThan2MinsItem newLessThan2MinsItem() {
		return new LessThan2MinsItem();
	}

	public class LessThan2MinsItem {
		public String customerName;
		public String customerId;
		public String customerAddress;
		public String startTime;
		public String endTime;
		public long sales;

		public LessThan2MinsItem() {

		}

		/**
		 * Khoi tạo đối tượng
		 * 
		 * @author: TamPQ
		 * @param cursor
		 * @return: void
		 * @throws:
		 */
		public void initItem(Cursor c) {
			if (c == null) {
				return;
			}
			customerName = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CUS_CODE_NAME"))) ? "" : c
					.getString(c.getColumnIndexOrThrow("CUS_CODE_NAME"));
			customerId = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"))) ? "" : c
					.getString(c.getColumnIndexOrThrow("CUSTOMER_ID"));
			customerAddress = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("STREET"))) ? "" : c
					.getString(c.getColumnIndexOrThrow("STREET"));
			startTime = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("START_TIME"))) ? "" : c
					.getString(c.getColumnIndexOrThrow("START_TIME"));
			endTime = StringUtil.isNullOrEmpty(c.getString(c.getColumnIndexOrThrow("END_TIME"))) ? "" : c.getString(c
					.getColumnIndexOrThrow("END_TIME"));
			sales = c.getLong(c.getColumnIndexOrThrow("QUANTITY_IN_DATE"));
		}
	}

}
