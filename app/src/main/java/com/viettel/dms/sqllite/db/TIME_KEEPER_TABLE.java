/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.TimeKeeperDTO;

/**
 * Thong tin cham cong pg
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class TIME_KEEPER_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String TIME_KEEPER_ID = "TIME_KEEPER_ID";
	// id customer
	public static final String STAFF_OWNER_ID = "STAFF_OWNER_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// id product
	public static final String SHOP_ID = "SHOP_ID";
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// OBJECT TYPE
	public static final String TYPE = "TYPE";
	// OBJECT ID
	public static final String IS_ABSENT = "IS_ABSENT";
	// RESULT
	public static final String DRESSES = "DRESSES";
	//Theo quy trinh
	public static final String RULE = "RULE";
	//Gio di lam
	public static final String START_TIME = "START_TIME";
	// CREATE DATE
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String UPDATE_USER = "UPDATE_USER";

	// // so luon
	// public static final String QUANTITY = "QUANTITY";
	// // ordinal
	// public static final String ORDINAL = "ORDINAL";
	// // check date
	// public static final String CHECK_DATE = "CHECK_DATE";
	// ten bang
	public static final String TABLE_NAME = "TIME_KEEPER";

	public TIME_KEEPER_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { TIME_KEEPER_ID, STAFF_OWNER_ID, STAFF_ID, SHOP_ID, CUSTOMER_ID, TYPE,
				IS_ABSENT, DRESSES, RULE, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#insert(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		TimeKeeperDTO disDTO = (TimeKeeperDTO) dto;
		ContentValues value = initDataRow(disDTO);
		return insert(null, value);

	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		TimeKeeperDTO disDTO = (TimeKeeperDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = {  };
		String where = "";
		return update(value, where, params);
	}

	public long insertOrUpdate(AbstractTableDTO dto) {
		TimeKeeperDTO disDTO = (TimeKeeperDTO) dto;
		ContentValues value = initDataRow(disDTO);
		return insertOrUpdate(null, value);
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	private ContentValues initDataRow(TimeKeeperDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(TIME_KEEPER_ID, dto.timeKeeperId);
		editedValues.put(STAFF_OWNER_ID, dto.staffOwnerId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(TYPE, dto.type);
		editedValues.put(IS_ABSENT, dto.isAbsent);
		if(dto.isAbsent == 0) {
			editedValues.put(DRESSES, dto.dresses);
			editedValues.put(RULE, dto.rule);
			editedValues.put(START_TIME, dto.startTime);
		}
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		if(dto.isEdit) {
			editedValues.put(UPDATE_DATE, dto.updateDate);
			editedValues.put(UPDATE_USER, dto.updateUser);
		}

		return editedValues;
	}
}
