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
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayProgrameProDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemModel;
import com.viettel.dms.dto.view.DisplayProgrameItemViewDTO;
import com.viettel.utils.VTLog;

/**
 * Thong tin san pham cua CTTB
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class DISPLAY_PROGRAME_PRODUCT_TABLE extends ABSTRACT_TABLE {
	// ma CTTB
	public static final String DISPLAY_PROGRAME_CODE = "DISPLAY_PROGRAME_CODE";
	// ma san pham
	public static final String PRODUCT_CODE = "PRODUCT_CODE";
	// ma nganh san pham
	public static final String CATEGORY = "CATEGORY";
	// tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// den ngay
	public static final String TO_DATE = "TO_DATE";
	// trang thai
	public static final String STATUS = "STATUS";

	private static final String TABLE_DISPLAY_PROGRAME_PRODUCT = "DISPLAY_PROGRAME_PRODUCT";

	public DISPLAY_PROGRAME_PRODUCT_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_PROGRAME_PRODUCT;
		this.columns = new String[] { DISPLAY_PROGRAME_CODE, PRODUCT_CODE,
				CATEGORY, FROM_DATE, TO_DATE, STATUS, SYN_STATE};
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
		ContentValues value = initDataRow((DisplayProgrameProDTO) dto);
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
	public long insert(DisplayProgrameProDTO dto) {
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
		DisplayProgrameProDTO disDTO = (DisplayProgrameProDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayProgrameCode };
		return update(value, DISPLAY_PROGRAME_CODE + " = ?", params);
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
		return delete(DISPLAY_PROGRAME_CODE + " = ?", params);
	}

	@Override
	public long delete(AbstractTableDTO dto) {
		DisplayProgrameProDTO cusDTO = (DisplayProgrameProDTO) dto;
		String[] params = { cusDTO.displayProgrameCode };
		return delete(DISPLAY_PROGRAME_CODE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayProgrameProDTO
	 * @throws:
	 */
	public DisplayProgrameProDTO getRowById(String id) {
		DisplayProgrameProDTO DisplayProgrameProDTO = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(DISPLAY_PROGRAME_CODE + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				DisplayProgrameProDTO = initDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return DisplayProgrameProDTO;
	}

	private DisplayProgrameProDTO initDTOFromCursor(Cursor c) {
		DisplayProgrameProDTO dpDetailDTO = new DisplayProgrameProDTO();

		dpDetailDTO.displayProgrameCode = (c.getString(c
				.getColumnIndex(DISPLAY_PROGRAME_CODE)));
		dpDetailDTO.category = (c.getString(c.getColumnIndex(CATEGORY)));
		dpDetailDTO.productCode = (c.getString(c.getColumnIndex(PRODUCT_CODE)));
		dpDetailDTO.fromeDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));

		return dpDetailDTO;
	}


	private ContentValues initDataRow(DisplayProgrameProDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(DISPLAY_PROGRAME_CODE, dto.displayProgrameCode);
		editedValues.put(PRODUCT_CODE, dto.productCode);
		editedValues.put(CATEGORY, dto.category);
		editedValues.put(FROM_DATE, dto.fromeDate);
		editedValues.put(TO_DATE, dto.toDate);
		editedValues.put(STATUS, dto.status);

		return editedValues;
	}

	/**
	 * Lay ds item cua chuong trinh trung bay
	 * 
	 * @author: SoaN
	 * @param ext
	 * @return
	 * @return: DisplayProgrameItemModel
	 * @throws:
	 */

	public DisplayProgrameItemModel getListDisplayProgrameItem(Bundle ext) {

		String promotionCode = ext
				.getString(IntentConstants.INTENT_PROMOTION_CODE);

		String limit = ext.getString(IntentConstants.INTENT_PAGE);

		String getCountProductList = "SELECT COUNT(*) FROM DISPLAY_PROGRAME_PRODUCT as dis_p, PRODUCT as p where dis_p.DISPLAY_PROGRAME_CODE = ?";
		String queryGetListProductForOrder = "select dis_p.PRODUCT_CODE, p.PRODUCT_NAME from DISPLAY_PROGRAME_PRODUCT as dis_p, PRODUCT as p where dis_p.DISPLAY_PROGRAME_CODE = ?"
				+ limit;
		String[] params = new String[] { promotionCode };

		DisplayProgrameItemModel modelData = new DisplayProgrameItemModel();
		List<DisplayProgrameItemViewDTO> list = new ArrayList<DisplayProgrameItemViewDTO>();
		
		Cursor cTmp = null;
		int total = 0;
		try {
			// get total row first
			cTmp = rawQuery(getCountProductList, params);
			if (cTmp != null) {
				cTmp.moveToFirst();
				total = cTmp.getInt(0);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e) {
			}
		}
		// end
		Cursor c = null;
		try {
			c = rawQuery(queryGetListProductForOrder, params);

			if (c != null) {

				if (c.moveToFirst()) {
					do {
						DisplayProgrameItemViewDTO orderJoinTableDTO = new DisplayProgrameItemViewDTO();
						orderJoinTableDTO.initDisplayProgrameObjectFromGetStatement(c);
						list.add(orderJoinTableDTO);
					} while (c.moveToNext());
				}
			}
			modelData.setModelData(list);
			modelData.setTotal(total);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e) {
			}
		}
		return modelData;
	}
}
