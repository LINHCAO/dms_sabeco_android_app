/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;

import com.viettel.dms.dto.db.AbstractTableDTO;

/**
 * TRAINING PLAN TABLE
 * TRAINING_PLAN_TABLE.java
 * @version: 1.0 
 * @since:  08:27:14 20 Jan 2014
 */
public class TRAINING_PLAN_TABLE extends ABSTRACT_TABLE {

	private static final String TRAINING_PLAN_ID = "TRAINING_PLAN_ID";
	private static final String STAFF_ID = "STAFF_ID";
	private static final String MONTH = "MONTH";
	// 1: ON, 0: OFF
	private static final String STATUS = "STATUS";
	private static final String NOTE = "NOTE";
	private static final String APPROVE_DATE = "APPROVE_DATE";
	private static final String APPROVE_USER = "APPROVE_USER";
	private static final String CREATE_DATE = "CREATE_DATE";
	private static final String UPDATE_DATE = "UPDATE_DATE";
	private static final String SHOP_ID = "SHOP_ID";
	private static final String SYN_STATE = "SYN_STATE";
	private static final String TRAINING_PLAN_TABLE = "TRAINING_PLAN";

	public TRAINING_PLAN_TABLE(SQLiteDatabase mDB) {
		this.tableName = TRAINING_PLAN_TABLE;
		this.columns = new String[] { TRAINING_PLAN_ID, STAFF_ID, MONTH, STATUS, NOTE,
				APPROVE_DATE, APPROVE_USER, CREATE_DATE, UPDATE_DATE, SHOP_ID,
				SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

}
