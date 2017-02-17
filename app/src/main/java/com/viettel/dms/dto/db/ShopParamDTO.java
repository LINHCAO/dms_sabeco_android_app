/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.SHOP_PARAM_TABLE;

/**
 * thong tin cua shop param
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class ShopParamDTO extends AbstractTableDTO {

	private static final long serialVersionUID = 1L;
	// id cua bang
	public int shopPramId;
	// id cua shop tuong ung
	public int shopId;
	// type cua param
	public String type;
	// code cua param
	public String code;
	// name cua param
	public String name;
	// description cua param
	public String description;
	// status cua param (1: active, 0: off)
	public int status;
	// create date
	public String createDate;
	// create use
	public String createUser;
	// update date
	public String updateDate;
	// update user
	public String updateUser;

	public ShopParamDTO() {
		super(TableType.SHOP_PARAM_TABLE);
	}

	/**
	 * 
	 * init object with cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return: void
	 * @throws:
	 * @since: Mar 8, 2013
	 */
	public void initObjectWithCursor(Cursor c) {
		if (c.getColumnIndex(SHOP_PARAM_TABLE.SHOP_PARAM_ID) >= 0) {
			shopPramId = c.getInt(c
					.getColumnIndex(SHOP_PARAM_TABLE.SHOP_PARAM_ID));
		} else {
			shopPramId = 0;
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.SHOP_ID) >= 0) {
			shopId = c.getInt(c.getColumnIndex(SHOP_PARAM_TABLE.SHOP_ID));
		} else {
			shopId = 0;
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.TYPE) >= 0) {
			type = c.getString(c.getColumnIndex(SHOP_PARAM_TABLE.TYPE));
		} else {
			type = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.CODE) >= 0) {
			code = c.getString(c.getColumnIndex(SHOP_PARAM_TABLE.CODE));
		} else {
			code = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.NAME) >= 0) {
			name = c.getString(c.getColumnIndex(SHOP_PARAM_TABLE.NAME));
		} else {
			name = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.DESCRIPTION) >= 0) {
			description = c.getString(c
					.getColumnIndex(SHOP_PARAM_TABLE.DESCRIPTION));
		} else {
			description = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.STATUS) >= 0) {
			status = c.getInt(c.getColumnIndex(SHOP_PARAM_TABLE.STATUS));
		} else {
			status = 0;
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.CREATE_DATE) >= 0) {
			createDate = c.getString(c
					.getColumnIndex(SHOP_PARAM_TABLE.CREATE_DATE));
		} else {
			createDate = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.CREATE_USER) >= 0) {
			createUser = c.getString(c
					.getColumnIndex(SHOP_PARAM_TABLE.CREATE_USER));
		} else {
			createUser = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.UPDATE_DATE) >= 0) {
			updateDate = c.getString(c
					.getColumnIndex(SHOP_PARAM_TABLE.UPDATE_DATE));
		} else {
			updateDate = "";
		}
		if (c.getColumnIndex(SHOP_PARAM_TABLE.UPDATE_USER) >= 0) {
			updateUser = c.getString(c
					.getColumnIndex(SHOP_PARAM_TABLE.UPDATE_USER));
		} else {
			updateUser = "";
		}
	}

}
