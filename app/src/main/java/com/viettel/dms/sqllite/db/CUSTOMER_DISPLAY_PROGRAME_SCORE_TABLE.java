/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.CustomerDisplayProgrameScoreDTO;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String ID = "ID";
	// ma khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// ma CTTB
	public static final String DISPLAY_PROGRAME_ID = "DISPLAY_PROGRAME_ID";

	// ma NVBH
	public static final String STAFF_ID = "STAFF_ID";
	// loai doi tuong cham trung bay
	public static final String OBJECT_TYPE = "OBJECT_TYPE";
	// ID cua doi tuong cham trung bay
	public static final String OBJECT_ID = "OBJECT_ID";
	// diem cham
	public static final String RESULT = "RESULT";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";

	public static final String TABLE_NAME = "CUSTOMER_DISPLAY_PROGR_SCORE";

	public CUSTOMER_DISPLAY_PROGRAME_SCORE_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] { ID, CUSTOMER_ID, DISPLAY_PROGRAME_ID,
				STAFF_ID, OBJECT_TYPE, OBJECT_ID, RESULT, CREATE_DATE,
				SYN_STATE };
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
		ContentValues value = initDataRow((CustomerDisplayProgrameScoreDTO) dto);
		return insert(null, value);
	}

	public long insert(CustomerDisplayProgrameScoreDTO dto) {
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
		CustomerDisplayProgrameScoreDTO dtoDisplay = (CustomerDisplayProgrameScoreDTO) dto;
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
	 * GET ROW BY ID
	 * 
	 * @author: HaiTC3
	 * @param id
	 * @return
	 * @return: CustomerDisplayProgrameScoreDTO
	 * @throws:
	 */
	public CustomerDisplayProgrameScoreDTO getRowById(String id) {
		CustomerDisplayProgrameScoreDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	/**
	 * 
	 * init object data with cursor
	 * 
	 * @author: HaiTC3
	 * @param c
	 * @return
	 * @return: CustomerDisplayProgrameScoreDTO
	 * @throws:
	 */
	private CustomerDisplayProgrameScoreDTO initDTOFromCursor(Cursor c) {
		CustomerDisplayProgrameScoreDTO dto = new CustomerDisplayProgrameScoreDTO();
		dto.ID = (c.getInt(c.getColumnIndex(ID)));
		dto.customerId = (c.getLong(c.getColumnIndex(CUSTOMER_ID)));
		dto.displayProgrameId = (c
				.getInt(c.getColumnIndex(DISPLAY_PROGRAME_ID)));
		dto.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		dto.objectType = (c.getInt(c.getColumnIndex(OBJECT_TYPE)));
		dto.objectId = (c.getInt(c.getColumnIndex(OBJECT_ID)));

		dto.result = (c.getInt(c.getColumnIndex(RESULT)));
		dto.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dto.synState = (c.getInt(c.getColumnIndex(SYN_STATE)));
		return dto;
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
	private ContentValues initDataRow(CustomerDisplayProgrameScoreDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(ID, dto.ID);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(DISPLAY_PROGRAME_ID, dto.displayProgrameId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(OBJECT_TYPE, dto.objectType);
		editedValues.put(OBJECT_ID, dto.objectId);
		editedValues.put(RESULT, dto.result);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(SYN_STATE, dto.synState);
		return editedValues;
	} 
}
