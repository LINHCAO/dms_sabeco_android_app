/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDisplayProgrameScoreDTO;
import com.viettel.dms.dto.db.SaleScoreSettingDTO;

/**
 * BANG THONG TIN DIEM BAN HANG
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class SALE_SCORE_SETTING extends ABSTRACT_TABLE {
	public static final String ID = "ID";
	public static final String FROM_PERCENTAGE = "FROM_PERCENTAGE";
	public static final String TO_PERCENTAGE = "TO_PERCENTAGE";
	public static final String SCORE = "SCORE";
	public static final String UPDATE_DATE = "UPDATE_DATE";

	public static final String TABLE_NAME = "SALE_SCORE_SETTING";

	public SALE_SCORE_SETTING(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { ID, FROM_PERCENTAGE, FROM_PERCENTAGE,
				SCORE, UPDATE_DATE, SYN_STATE };
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
		ContentValues value = initDataRow((SaleScoreSettingDTO) dto);
		return insert(null, value);
	}

	public long insert(SaleScoreSettingDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
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
		SaleScoreSettingDTO dtoDisplay = (SaleScoreSettingDTO) dto;
		ContentValues value = initDataRow(dtoDisplay);
		String[] params = { String.valueOf(dtoDisplay.ID) };
		return update(value, ID + " = ?", params);
	}

	public int delete(String code) {
		String[] params = { code };
		return delete(ID + " = ?", params);
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
		CustomerDisplayProgrameScoreDTO dtoDisplay = (CustomerDisplayProgrameScoreDTO) dto;
		String[] params = { "" + dtoDisplay.ID };
		return delete(ID + " = ?", params);
	}

	/**
	 * 
	 * init content values with object dto
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDataRow(SaleScoreSettingDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ID, dto.ID);
		editedValues.put(FROM_PERCENTAGE, dto.fromPercentage);
		editedValues.put(TO_PERCENTAGE, dto.toPercentage);
		editedValues.put(SCORE, dto.score);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(SYN_STATE, dto.synState);
		return editedValues;
	}
}
