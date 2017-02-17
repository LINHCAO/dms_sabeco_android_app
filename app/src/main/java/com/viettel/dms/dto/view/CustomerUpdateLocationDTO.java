/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerPositionLogDTO;
import com.viettel.dms.dto.db.StaffDTO;
import com.viettel.dms.sqllite.db.CUSTOMER_POSITION_LOG_TABLE;
import com.viettel.dms.sqllite.db.STAFF_TABLE;
import com.viettel.dms.util.StringUtil;
/**
 * DTO thong tin item mot lan cap nhat vi tri khach hang
 * @author banghn
 * @since 1.1
 */
@SuppressWarnings("serial")
public class CustomerUpdateLocationDTO implements Serializable{
	//thong tin lich su
	public CustomerPositionLogDTO positionLog;
	
	//thong tin nhan vien cap nhat
	public StaffDTO staffSale;
	
	public CustomerUpdateLocationDTO(){
		positionLog = new CustomerPositionLogDTO();
		staffSale = new StaffDTO();
	}

	
	/**
	 * parse du lieu sau khi truy van tu DB
	 * @author banghn
	 * @param c
	 */
	public void initFromCursor(Cursor c) throws Exception {
		if (c == null) {
			throw new Exception("Cursor is empty");
		}
		this.staffSale.staffId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
						.getColumnIndexOrThrow(STAFF_TABLE.STAFF_ID)))) ? "0"
						: c.getString(c
								.getColumnIndexOrThrow(STAFF_TABLE.STAFF_ID)));
		this.staffSale.staffCode = StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_CODE))) ? "" : c
				.getString(c.getColumnIndexOrThrow(STAFF_TABLE.STAFF_CODE));
		this.staffSale.name = StringUtil.isNullOrEmpty(c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_NAME))) ? "" : c.getString(c
				.getColumnIndexOrThrow(STAFF_TABLE.STAFF_NAME));

		this.positionLog.customerId = Long.parseLong((StringUtil.isNullOrEmpty(c.getString(c
						.getColumnIndexOrThrow(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_ID)))) ? "0"
						: c.getString(c
								.getColumnIndexOrThrow(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_ID)));
		this.positionLog.setLat(c.getDouble(c.getColumnIndexOrThrow("LAT")));
		this.positionLog.setLng(c.getDouble(c.getColumnIndexOrThrow("LNG")));

		this.positionLog.createDate = StringUtil
				.isNullOrEmpty(c.getString(c
						.getColumnIndexOrThrow(CUSTOMER_POSITION_LOG_TABLE.CREATE_DATE))) ? ""
				: c.getString(c
						.getColumnIndexOrThrow(CUSTOMER_POSITION_LOG_TABLE.CREATE_DATE));

		this.positionLog.id = Integer
				.parseInt((StringUtil.isNullOrEmpty(c.getString(c
						.getColumnIndexOrThrow(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_POSITION_LOG_ID)))) ? "0"
						: c.getString(c
						.getColumnIndexOrThrow(CUSTOMER_POSITION_LOG_TABLE.CUSTOMER_POSITION_LOG_ID)));
	}
}
