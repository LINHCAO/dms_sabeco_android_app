/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.FeedBackDetailDTO;

/**
 * feedback detail table
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class FEEDBACK_DETAIL_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String FEEDBACK_DETAIL_ID = "FEEDBACK_DETAIL_ID";
	// id bang feedback tuong ung
	public static final String FEEDBACK_ID = "FEEDBACK_ID";
	// id bang product tuong ung
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// create user id
	public static final String CREATE_USER_ID = "CREATE_USER_ID";

	public static final String FEEDBACK_DETAIL_TABLE = "FEEDBACK_DETAIL";

	public FEEDBACK_DETAIL_TABLE(SQLiteDatabase mDB) {
		this.tableName = FEEDBACK_DETAIL_TABLE;
		this.columns = new String[] { FEEDBACK_DETAIL_ID, FEEDBACK_ID,
				PRODUCT_ID, CREATE_USER_ID, SYN_STATE };
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
		ContentValues value = initDataRow((FeedBackDetailDTO) dto);
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
		FeedBackDetailDTO dtoFeedBackDetail = (FeedBackDetailDTO) dto;
		String[] params = { String.valueOf(dtoFeedBackDetail.feedbackDetailId) };
		ContentValues value = initDataRow((FeedBackDetailDTO) dto);
		return update(value, FEEDBACK_DETAIL_ID + " = ?", params);
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
		FeedBackDetailDTO dtoFeedBackDetail = (FeedBackDetailDTO) dto;
		String[] params = { String.valueOf(dtoFeedBackDetail.feedbackDetailId) };
		return delete(FEEDBACK_DETAIL_ID + " = ?", params);
	}

	/**
	 * 
	 * init data
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	private ContentValues initDataRow(FeedBackDetailDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(FEEDBACK_DETAIL_ID, dto.feedbackDetailId);
		editedValues.put(FEEDBACK_ID, dto.feedbackId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(CREATE_USER_ID, dto.createUserId);
		return editedValues;
	}
}
