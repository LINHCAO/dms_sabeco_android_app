/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.TrainingResultDTO;

/**
 * luu tru cac danh gia ve NVBH, ghi nhan gop y cua khach hang, ghi nhan cac SKU
 * can ban lan sau. Cac thong tin duoc ghi nhan trong qua trinh huan luyen NVBH
 * tren mot khach hang cu the
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class TRAINING_RESULT_TABLE extends ABSTRACT_TABLE {

	// id bang
	public static final String ID = "ID";
	// ma training plan detail
	public static final String TRAINING_PLAN_DETAIL_ID = "TRAINING_PLAN_DETAIL_ID";
	// ma khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// gia tri phu thuoc vao object_type
	public static final String OBJECT_ID = "OBJECT_ID";
	// xac dinh loai du lieu cua dong tuong ung
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// chi co y nghia voi object type = 3 (1: ON, 0: OFF)
	public static final String STATUS = "STATUS";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ten table
	public static final String TABLE_NAME = "TRAINING_RESULT";

	public TRAINING_RESULT_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { ID, TRAINING_PLAN_DETAIL_ID, CUSTOMER_ID,
				OBJECT_ID, OBJECT_TYPE, STATUS, CREATE_DATE, SYN_STATE };
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
		ContentValues value = initDataRow((TrainingResultDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * insert with TrainingResultDTO
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: long
	 * @throws:
	 */
	public long insert(TrainingResultDTO dto) {
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
		TrainingResultDTO dtoDisplay = (TrainingResultDTO) dto;
		ContentValues value = initDataRow(dtoDisplay);
		String[] params = { String.valueOf(dtoDisplay.ID) };
		return update(value, ID + " = ?", params);
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
		TrainingResultDTO dtoDisplay = (TrainingResultDTO) dto;
		String[] params = { "" + dtoDisplay.ID };
		return delete(ID + " = ?", params);
	}

	/**
	 * 
	 * init data for row in DB
	 * 
	 * @author: HaiTC3
	 * @param dto
	 * @return
	 * @return: ContentValues
	 * @throws:
	 */
	private ContentValues initDataRow(TrainingResultDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ID, dto.ID);
		editedValues.put(TRAINING_PLAN_DETAIL_ID, dto.trainingPlanDetailID);
		editedValues.put(CUSTOMER_ID, dto.customerID);
		editedValues.put(OBJECT_ID, dto.objectID);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(STATUS, dto.status);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(SYN_STATE, dto.synState);
		return editedValues;
	}

}
