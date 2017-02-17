/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.db.DisplayShopMapDTO;
import com.viettel.dms.dto.view.DisplayProgrameModel;
import com.viettel.utils.VTLog;

/**
 * Luu CTTT ap dung cho NPP nao
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class DISPLAY_SHOP_MAP_TABLE extends ABSTRACT_TABLE {
	// id CTTB
	public static final String DISPLAY_PROGRAM_ID = "DISPLAY_PROGRAM_ID";
	// code CTTB
	public static final String DISPLAY_PROGRAM_CODE = "DISPLAY_PROGRAM_CODE";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// trang thai
	public static final String STATUS = "STATUS";
	// id bang
	public static final String DISPLAY_SHOP_MAP_ID = "DISPLAY_SHOP_MAP_ID";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay cap nhat
	public static final String FROM_DATE = "FROM_DATE";
	// nguoi cap nhat
	public static final String TO_DATE = "TO_DATE";

	private static final String TABLE_DISPLAY_SHOP_MAP = "TABLE_DISPLAY_SHOP_MAP";

	public DISPLAY_SHOP_MAP_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_SHOP_MAP;
		this.columns = new String[] { DISPLAY_PROGRAM_ID, DISPLAY_PROGRAM_CODE, SHOP_ID, STATUS,
				DISPLAY_SHOP_MAP_ID, CREATE_DATE, CREATE_USER, UPDATE_DATE, UPDATE_USER, SYN_STATE };
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
		ContentValues value = initDataRow((DisplayShopMapDTO) dto);
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
	public long insert(DisplayShopMapDTO dto) {
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
		DisplayShopMapDTO disDTO = (DisplayShopMapDTO) dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayProgramId };
		return update(value, DISPLAY_PROGRAM_ID + " = ?", params);
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
		return delete(DISPLAY_PROGRAM_ID + " = ?", params);
	}

	public long delete(AbstractTableDTO dto) {
		DisplayShopMapDTO cusDTO = (DisplayShopMapDTO) dto;
		String[] params = { "" + cusDTO.displayProgramId };
		return delete(DISPLAY_PROGRAM_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public DisplayShopMapDTO getRowById(String id) {
		DisplayShopMapDTO dto = null;
		Cursor c = null;
		try {
			String[] params = { id };
			c = query(DISPLAY_PROGRAM_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initLogDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private DisplayShopMapDTO initLogDTOFromCursor(Cursor c) {
		DisplayShopMapDTO dpDetailDTO = new DisplayShopMapDTO();

		dpDetailDTO.displayProgramId = (c.getInt(c.getColumnIndex(DISPLAY_PROGRAM_ID)));
		dpDetailDTO.displayProgrameCode = (c.getString(c.getColumnIndex(DISPLAY_PROGRAM_CODE)));
		dpDetailDTO.shopId = (c.getInt(c.getColumnIndex(SHOP_ID)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.displayShopMapId = (c.getInt(c.getColumnIndex(DISPLAY_SHOP_MAP_ID)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.toDate = (c.getString(c.getColumnIndex(TO_DATE)));

		return dpDetailDTO;
	}

	/**
	 * 
	 * lay tat ca cac dong cua CSDL
	 * 
	 * @author: HieuNH
	 * @return
	 * @return: Vector<DisplayPrdogrameLvDTO>
	 * @throws:
	 */
	public Vector<DisplayShopMapDTO> getAllRow() {
		Vector<DisplayShopMapDTO> v = new Vector<DisplayShopMapDTO>();
		Cursor c = null;
		try {
			c = query(null, null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			DisplayShopMapDTO DisplayPrdogrameLvDTO;
			if (c.moveToFirst()) {
				do {
					DisplayPrdogrameLvDTO = initLogDTOFromCursor(c);
					v.addElement(DisplayPrdogrameLvDTO);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(DisplayShopMapDTO dto) {
		ContentValues editedValues = new ContentValues();

		editedValues.put(DISPLAY_PROGRAM_ID, dto.displayProgramId);
		editedValues.put(DISPLAY_PROGRAM_CODE, dto.displayProgrameCode);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(STATUS, dto.status);
		editedValues.put(DISPLAY_SHOP_MAP_ID, dto.displayShopMapId);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);

		return editedValues;
	}

	/**
	 * 
	 * Lay ds CTTB cho chuc nang hinh cua NVBH & cua KH cua NV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param data
	 * @return
	 * @return: DisplayProgrameModel
	 * @throws:
	 */
	public DisplayProgrameModel getListDisplayProgrameImage(Bundle data) {
		boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<String> params = new ArrayList<String>();

		StringBuilder stringbuilder = new StringBuilder();
		stringbuilder
				.append("SELECT distinct DPSM.DISPLAY_PROGRAM_ID, DPSM.DISPLAY_PROGRAM_CODE FROM DISPLAY_SHOP_MAP DPSM ");
		stringbuilder.append(" WHERE DPSM.STATUS=1 ");
		// stringbuilder.append(" AND date(DPSM.FROM_DATE) <= date('now','localtime') ");
		// stringbuilder.append(" AND ifnull(date(DPSM.TO_DATE) >= date('now','localtime'),1) ");
		stringbuilder.append(" AND DPSM.SHOP_ID in (?) ");
		params.add(shopId);

		String getCountProductList = " SELECT COUNT(*) AS TOTAL_ROW FROM (" + stringbuilder.toString() + ") ";
		stringbuilder.append(" ORDER BY DPSM.DISPLAY_PROGRAM_CODE");

		DisplayProgrameModel modelData = new DisplayProgrameModel();
		List<DisplayProgrameDTO> list = new ArrayList<DisplayProgrameDTO>();

		Cursor cTmp = null;
		try {
			// get total row first
			if (!checkPagging) {
				cTmp = rawQuery(getCountProductList, params.toArray(new String[params.size()]));
				int total = 0;
				if (cTmp != null) {
					cTmp.moveToFirst();
					total = cTmp.getInt(0);
				}
				modelData.setTotal(total);
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (cTmp != null) {
					cTmp.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
			// end
		Cursor c = null;
		try{
			c = rawQuery(stringbuilder.toString(), params.toArray(new String[params.size()]));

			if (c != null) {

				if (c.moveToFirst()) {
					DisplayProgrameDTO orderJoinTableDTO = null;
					do {
						orderJoinTableDTO = new DisplayProgrameDTO();
						orderJoinTableDTO.initDisplayProgrameObject(c);
						list.add(orderJoinTableDTO);
					} while (c.moveToNext());
				}
			}
			modelData.setModelData(list);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		} finally {
			try {
				if (c != null) {
					c.close();
				}
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}

		return modelData;
	}
}
