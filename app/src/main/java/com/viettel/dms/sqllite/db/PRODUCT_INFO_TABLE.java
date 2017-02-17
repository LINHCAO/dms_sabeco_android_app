/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;

/**
 * product info (industry of product)
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class PRODUCT_INFO_TABLE extends ABSTRACT_TABLE {
	public static final String PRODUCT_INFO_ID = "PRODUCT_INFO_ID";
	public static final String PRODUCT_INFO_CODE = "PRODUCT_INFO_CODE";
	public static final String PRODUCT_INFO_NAME = "PRODUCT_INFO_NAME";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String STATUS = "STATUS";
	public static final String TYPE = "TYPE";
	public static final String PRODUCT_INFO_NAME_TABLE = "PRODUCT_INFO";

	public PRODUCT_INFO_TABLE(SQLiteDatabase mDB) {
		this.tableName = PRODUCT_INFO_NAME_TABLE;
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
	
	public List<DisplayProgrameItemDTO> getListDepartDisplayPrograme() {
		List<DisplayProgrameItemDTO> result = new ArrayList<DisplayProgrameItemDTO>();
		String queryGetListDepartDisplayPrograme = "SELECT PRODUCT_INFO_CODE FROM PRODUCT_INFO WHERE TYPE = ? AND STATUS = 1 ORDER BY PRODUCT_INFO_CODE";
		Cursor c = null;
		String params[] = {"1"}; //Nganh hang
		try {
			// get total row first
			c = rawQuery(queryGetListDepartDisplayPrograme, params);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemDTO dto = initComboBoxDisProItemFromCursor(c);
						result.add(dto);
					} while (c.moveToNext());
				}
			}
		} catch (Exception e) {
			result = null;
		} finally {
			if (c != null) {
				c.close();
			}
		}
		return result;
	}
	
	/**
	*  Tao du lieu combobox cho nganh hang
	*  @author: ThanhNN8
	*  @param c
	*  @return
	*  @return: ComboBoxDisplayProgrameItemDTO
	*  @throws: 
	*/
	private DisplayProgrameItemDTO initComboBoxDisProItemFromCursor(
			Cursor c) {
		// TODO Auto-generated method stub
		DisplayProgrameItemDTO result = new DisplayProgrameItemDTO();
		if (c.getColumnIndex(PRODUCT_INFO_CODE) >= 0) {
			result.value = (c.getString(c.getColumnIndex(PRODUCT_INFO_CODE)));
			result.name = (c.getString(c.getColumnIndex(PRODUCT_INFO_CODE)));
		} else {
			result.value = "";
			result.name = "";
		}
		return result;
	}

}
