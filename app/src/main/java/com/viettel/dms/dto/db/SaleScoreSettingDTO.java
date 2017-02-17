/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.sqllite.db.SALE_SCORE_SETTING;

/**
 * thong tin diem cua NVBH
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class SaleScoreSettingDTO extends AbstractTableDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// id bang
	public long ID;
	// ma khach hang
	public int fromPercentage;
	// ID CTTB
	public int toPercentage;
	// id cua NVBH
	public int score;
	// ngay tao
	public String updateDate;
	// state for object in DB
	public int synState;

	public SaleScoreSettingDTO() {
		super(TableType.SALE_SCORE_SETTING);
	}

	/**
	 * 
	*  init data from cursor
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initDataFromCursor(Cursor c) {
		if (c.getColumnIndex(SALE_SCORE_SETTING.ID) >= 0) {
			ID = c.getLong(c.getColumnIndex(SALE_SCORE_SETTING.ID));
		}
		if (c.getColumnIndex(SALE_SCORE_SETTING.FROM_PERCENTAGE) >= 0) {
			fromPercentage = c.getInt(c
					.getColumnIndex(SALE_SCORE_SETTING.FROM_PERCENTAGE));
		}
		if (c.getColumnIndex(SALE_SCORE_SETTING.TO_PERCENTAGE) >= 0) {
			toPercentage = c.getInt(c
					.getColumnIndex(SALE_SCORE_SETTING.TO_PERCENTAGE));
		}
		if (c.getColumnIndex(SALE_SCORE_SETTING.SCORE) >= 0) {
			score = c.getInt(c.getColumnIndex(SALE_SCORE_SETTING.SCORE));
		}
		if (c.getColumnIndex(SALE_SCORE_SETTING.UPDATE_DATE) >= 0) {
			updateDate = c.getString(c
					.getColumnIndex(SALE_SCORE_SETTING.UPDATE_DATE));
		}
	}

}
