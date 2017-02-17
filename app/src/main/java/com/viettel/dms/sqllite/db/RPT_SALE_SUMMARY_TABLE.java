/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;

import com.viettel.dms.dto.db.AbstractTableDTO;

/**
 * tong ket bao cao ban hang
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class RPT_SALE_SUMMARY_TABLE extends ABSTRACT_TABLE {
	public static final String RPT_SALE_SUMMARY_ID = "RPT_SALE_SUMMARY_ID";
	public static final String OBJECT_ID = "OBJECT_ID";
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	public static final String SALE_DATE = "SALE_DATE";
	public static final String STATUS = "STATUS";
	public static final String CODE = "CODE";
	public static final String NAME = "NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String PLAN = "PLAN";
	public static final String ACTUALLY = "ACTUALLY";
	public static final String TYPE = "TYPE";
	public static final String VIEW_TYPE = "VIEW_TYPE";
	public static final String ORDINAL = "ORDINAL";
	public static final String RPT_SALE_SUMMARY_NAME_TABLE = "RPT_SALE_SUMMARY";

	public RPT_SALE_SUMMARY_TABLE(SQLiteDatabase mDB) {
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
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#update(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.lib.sqllite.db.ABSTRACT_TABLE#delete(com.viettel
	 * .vinamilk.dto.db.AbstractTableDTO)
	 */
	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

}
