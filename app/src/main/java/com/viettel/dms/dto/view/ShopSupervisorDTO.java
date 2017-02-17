/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

import com.viettel.dms.constants.Constants;

/**
 *  Mo ta muc dich cua lop (interface)
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */

public class ShopSupervisorDTO {
	public int shopId;
	public String shopCode;
	public int staffId;
	public String staffName;
	
	/**
	 * 
	 */
	public ShopSupervisorDTO() {
		shopCode = "";
		staffName = "";
	}
	
	/**
	*  Khoi tao gia tri
	*  @author: Nguyen Thanh Dung
	*  @param c
	*  @return: void
	*  @throws: 
	*/
	
	public void initWithCursor(Cursor c) {
		// id
		if (c.getColumnIndex("GS_STAFF_ID") >= 0) {
			staffId = c.getInt(c.getColumnIndex("GS_STAFF_ID"));
		} else {
			staffId = 0;
		}
		if (c.getColumnIndex("GS_STAFF_NAME") >= 0) {
			staffName = c.getString(c.getColumnIndex("GS_STAFF_NAME"));
		} else {
			staffName = Constants.STR_BLANK;
		}
		
		if (c.getColumnIndex("NVBH_SHOP_ID") >= 0) {
			shopId = c.getInt(c.getColumnIndex("NVBH_SHOP_ID"));
		} else {
			shopId = 0;
		}
		
		// code
		if (c.getColumnIndex("NVBH_SHOP_CODE") >= 0) {
			shopCode = c.getString(c.getColumnIndex("NVBH_SHOP_CODE"));
		} else {
			shopCode = Constants.STR_BLANK;
		}
	}
}
