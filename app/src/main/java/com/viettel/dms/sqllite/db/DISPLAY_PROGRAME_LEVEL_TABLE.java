/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayProgrameLvDTO;

/**
 * Muc cua chuong trinh trung bay
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class DISPLAY_PROGRAME_LEVEL_TABLE extends ABSTRACT_TABLE {
	// id bang
	public static final String DISPLAY_PROGRAM_LEVEL_ID = "DISPLAY_PROGRAM_LEVEL_ID";
	// id CTTB
	public static final String DISPLAY_PROGRAM_ID = "DISPLAY_PROGRAM_ID";
	// ma CTTB
	public static final String DISPLAY_PROGRAM_CODE = "DISPLAY_PROGRAM_CODE";
	// muc CTTB
	public static final String LEVEL_CODE = "LEVEL_CODE";
	// discount event failed
	public static final String DISCOUNT_EVEN_DISPLAY_FAILED = "DISCOUNT_EVEN_DISPLAY_FAILED";
	// DISPLAY_BONUS
	public static final String DISPLAY_BONUS = "DISPLAY_BONUS";
	// DISPLAY_BONUS_TYPE
	public static final String DISPLAY_BONUS_TYPE = "DISPLAY_BONUS_TYPE";
	// DISPLAY_POSTION_1
	public static final String DISPLAY_POSTION_1 = "DISPLAY_POSTION_1";
	// DISPLAY_POSTION_2
	public static final String DISPLAY_POSTION_2 = "DISPLAY_POSTION_2";
	// DISPLAY_POSTION_3
	public static final String DISPLAY_POSTION_3 = "DISPLAY_POSTION_3";
	// DISPLAY_POSTION_4
	public static final String DISPLAY_POSTION_4 = "DISPLAY_POSTION_4";
	// MAX_DISCOUNT
	public static final String MAX_DISCOUNT = "MAX_DISCOUNT";
	// MAX_SUPPORT_DISPLAY
	public static final String MAX_SUPPORT_DISPLAY = "MAX_SUPPORT_DISPLAY";
	// NUM_PRODUCT_DISPLAY
	public static final String NUM_PRODUCT_DISPLAY = "NUM_PRODUCT_DISPLAY";
	// NUM_SKU
	public static final String NUM_SKU = "NUM_SKU";
	// PAID_PRICE
	public static final String PAID_PRICE = "PAID_PRICE";
	// PAID_PRODUCT_ID
	public static final String PAID_PRODUCT_ID = "PAID_PRODUCT_ID";
	// PERCENT_DISCOUNT_OVER
	public static final String PERCENT_DISCOUNT_OVER = "PERCENT_DISCOUNT_OVER";
	// PERCENT_DISCOUNT_PASS
	public static final String PERCENT_DISCOUNT_PASS = "PERCENT_DISCOUNT_PASS";
	// PERCENT_MIN_SUPPORT_DISPLAY
	public static final String PERCENT_MIN_SUPPORT_DISPLAY = "PERCENT_MIN_SUPPORT_DISPLAY";
	// REVENUE_BONUS
	public static final String REVENUE_BONUS = "REVENUE_BONUS";
	// REVENUE_BONUS_TYPE
	public static final String REVENUE_BONUS_TYPE = "REVENUE_BONUS_TYPE";
	// so tien cua muc
	public static final String AMOUNT = "AMOUNT";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// trang thai
	public static final String STATUS = "STATUS";

	private static final String TABLE_DISPLAY_PROGRAME_LEVEL = "DISPLAY_PROGRAM_LEVEL";

	public DISPLAY_PROGRAME_LEVEL_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_PROGRAME_LEVEL;
		this.columns = new String[] { DISPLAY_PROGRAM_LEVEL_ID,
				DISPLAY_PROGRAM_ID, DISPLAY_PROGRAM_CODE, LEVEL_CODE,
				DISCOUNT_EVEN_DISPLAY_FAILED, DISPLAY_BONUS,
				DISPLAY_BONUS_TYPE, DISPLAY_POSTION_1, DISPLAY_POSTION_2,
				DISPLAY_POSTION_3, DISPLAY_POSTION_4, MAX_DISCOUNT,
				MAX_SUPPORT_DISPLAY, NUM_PRODUCT_DISPLAY, NUM_SKU, PAID_PRICE,
				PAID_PRODUCT_ID, PERCENT_DISCOUNT_OVER, PERCENT_DISCOUNT_PASS,
				PERCENT_MIN_SUPPORT_DISPLAY, REVENUE_BONUS, REVENUE_BONUS_TYPE,
				AMOUNT, FROM_DATE, TO_DATE, STATUS, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((DisplayProgrameLvDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(DisplayProgrameLvDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}

	/**
	 * Update 1 dong xuong db
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		DisplayProgrameLvDTO disDTO = (DisplayProgrameLvDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayProgrameCode };
		return update(value, DISPLAY_PROGRAM_CODE + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * 
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(DISPLAY_PROGRAM_CODE + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		DisplayProgrameLvDTO disDTO = (DisplayProgrameLvDTO) dto;
		String[] params = { "" + disDTO.displayProgrameCode };
		return delete(DISPLAY_PROGRAM_CODE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public List<DisplayProgrameLvDTO> getListRowById(String id) {
		List<DisplayProgrameLvDTO> list = new ArrayList<DisplayProgrameLvDTO>();
		Cursor c = null;
		try {
			String[] params = { id };
			String queryStr = "SELECT * FROM DISPLAY_PROGRAM_LEVEL WHERE DISPLAY_PROGRAM_CODE = ?";
			c = rawQuery(queryStr, params);
			if (c != null) {
				if (c.moveToFirst()) {
					DisplayProgrameLvDTO dto = null;
					do {
						dto = this.initLogDTOFromCursor(c);
						list.add(dto);
					} while (c.moveToNext());
				}
			}
		} catch (Exception ex) {
			c = null;
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		return list;
	}

	private DisplayProgrameLvDTO initLogDTOFromCursor(Cursor c) {
		DisplayProgrameLvDTO dpDetailDTO = new DisplayProgrameLvDTO();

		dpDetailDTO.displayProgrameCode = (c.getString(c
				.getColumnIndex(DISPLAY_PROGRAM_CODE)));
		dpDetailDTO.levelCode = (c.getString(c.getColumnIndex(LEVEL_CODE)));
		dpDetailDTO.amount = (c.getInt(c.getColumnIndex(AMOUNT)));
		dpDetailDTO.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));

		return dpDetailDTO;
	}

	private ContentValues initDataRow(DisplayProgrameLvDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(DISPLAY_PROGRAM_CODE, dto.displayProgrameCode);
		editedValues.put(LEVEL_CODE, dto.levelCode);
		editedValues.put(AMOUNT, dto.amount);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);
		editedValues.put(STATUS, dto.status);

		return editedValues;
	}

}
