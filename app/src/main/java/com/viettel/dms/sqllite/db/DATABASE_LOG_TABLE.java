/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;

import com.viettel.dms.dto.db.AbstractTableDTO;

/**
 *  Bang luu thong tin dong bo
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class DATABASE_LOG_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String ID = "ID";
	// ten table
	public static final String TABLE_NAME = "TABLE_NAME";
	// id table
	public static final String TABLE_ID = "TABLE_ID";
	// action: 1: insert, 2:update, 3: delete
	public static final String ACTION = "ACTION";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// nha phan phoi
	public static final String SHOP_ID = "SHOP_ID";
	
	public static final String DATABASE_LOG_TABLE = "DATABASE_LOG";
	
	public DATABASE_LOG_TABLE(SQLiteDatabase mDB) {
		this.tableName = DATABASE_LOG_TABLE;
		this.columns = new String[] {ID, TABLE_NAME ,TABLE_ID,ACTION,STAFF_ID,SHOP_ID};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;
	}

	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#insert(com.viettel.vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#update(com.viettel.vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#delete(com.viettel.vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}
}
