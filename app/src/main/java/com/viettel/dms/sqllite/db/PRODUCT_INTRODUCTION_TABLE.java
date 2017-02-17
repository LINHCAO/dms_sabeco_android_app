/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.utils.VTLog;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class PRODUCT_INTRODUCTION_TABLE extends ABSTRACT_TABLE{
	
	public static final String ID = "ID";
	public static final String PRODUCT_ID = "PRODUCT_ID";
	public static final String CONTENT = "CONTENT";
	public static final String CREATE_DATE = "CREATE_DATE";
	public static final String UPDATE_DATE = "UPDATE_DATE";
	public static final String CREATE_USER = "CREATE_USER";
	public static final String UPDATE_USER = "UPDATE_USER";
	// 1: active, 0 : inactive
	public static final String STATUS = "STATUS";
	
private static final String TABLE_PRODUCT_INTRODUCTION = "PRODUCT_INTRODUCTION";
	
	public PRODUCT_INTRODUCTION_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PRODUCT_INTRODUCTION;
		this.columns = new String[] {ID, PRODUCT_ID ,CONTENT,CREATE_DATE,CREATE_USER,UPDATE_USER,STATUS};
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
	
	/**
	 * 
	*  bo xung comment cho ThanhNN : lay thong tin gioi thieu ve san pham
	*  @author: HaiTC3
	*  @param productId
	*  @return
	*  @return: String
	*  @throws:
	 */
	public String getContentIntroduction (String productId) {
		String result = "";
		String queryGetContentIntroduction = "select * from product_introduction where product_id = ? AND STATUS = 1";
		String[]params = {productId};
		Cursor c = null;
		try {
			c = rawQuery(queryGetContentIntroduction, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						if (c.getColumnIndex("CONTENT") >= 0) {
							result = c.getString(c.getColumnIndex("CONTENT"));
						}
					} while (c.moveToNext());
				}
			}

		} catch (Exception e) {
			VTLog.i("error", e.toString());
			return null;
		} finally {
			try{
				if (c != null){
					c.close();
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return result;
	}

}
