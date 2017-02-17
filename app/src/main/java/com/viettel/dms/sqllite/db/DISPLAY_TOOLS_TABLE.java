/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.DisplayToolDTO;

/**
 *  Ds cong cu trung bay
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class DISPLAY_TOOLS_TABLE extends ABSTRACT_TABLE{
	// id cua bang
	public static final String DISPLAY_TOOLS_ID = "DISPLAY_TOOLS_ID";
	// id NPP
	public static final String SHOP_ID = "SHOP_ID";
	// id khach hang
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// so luong
	public static final String QUANTITY = "QUANTITY";
	// truong nay chua dung
	public static final String RESULT = "RESULT";
	// ngay chuyen
	public static final String TRANSFER_DATE = "TRANSFER_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// id NVBH
	public static final String STAFF_ID = "STAFF_ID";

	private static final String TABLE_DISPLAY_TOOLS = "DISPLAY_TOOLS";
	
	public DISPLAY_TOOLS_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_DISPLAY_TOOLS;
		this.columns = new String[] { DISPLAY_TOOLS_ID, SHOP_ID,
				CUSTOMER_ID, PRODUCT_ID, QUANTITY, RESULT, TRANSFER_DATE,
				CREATE_USER, UPDATE_USER, CREATE_DATE, UPDATE_DATE, STAFF_ID, SYN_STATE };
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((DisplayToolDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(DisplayToolDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}
	
	/**
	 * Update 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		DisplayToolDTO disDTO = (DisplayToolDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.displayToolsId };
		return update(value, DISPLAY_TOOLS_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @param ap_param_id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(DISPLAY_TOOLS_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		DisplayToolDTO disDTO = (DisplayToolDTO)dto;
		String[] params = { "" + disDTO.displayToolsId };
		return delete(DISPLAY_TOOLS_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * 
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayToolDTO
	 * @throws:
	 */
	public DisplayToolDTO getRowById(String id) {
		DisplayToolDTO DisplayToolDTO = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(DISPLAY_TOOLS_ID + " = ?", params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				DisplayToolDTO = initDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return DisplayToolDTO;
	}

	private DisplayToolDTO initDTOFromCursor(Cursor c) {
		DisplayToolDTO dpDetailDTO = new DisplayToolDTO();

		dpDetailDTO.displayToolsId = (c.getInt(c.getColumnIndex(DISPLAY_TOOLS_ID)));
		dpDetailDTO.shopId = (c.getInt(c.getColumnIndex(SHOP_ID)));
		dpDetailDTO.customerId = (c.getLong(c.getColumnIndex(CUSTOMER_ID)));
		dpDetailDTO.productId = (c.getString(c.getColumnIndex(PRODUCT_ID)));
		
		dpDetailDTO.quantity = (c.getInt(c.getColumnIndex(QUANTITY)));
		dpDetailDTO.result = (c.getInt(c.getColumnIndex(RESULT)));
		dpDetailDTO.transferDate = (c.getString(c.getColumnIndex(TRANSFER_DATE)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.staffId = (c.getInt(c.getColumnIndex(STAFF_ID)));
		
		return dpDetailDTO;
	}

	private ContentValues initDataRow(DisplayToolDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(DISPLAY_TOOLS_ID, dto.displayToolsId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(PRODUCT_ID, dto.productId);
		
		editedValues.put(QUANTITY, dto.quantity);
		editedValues.put(RESULT, dto.result);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(TRANSFER_DATE, dto.transferDate);
		
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createUser);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(STAFF_ID, dto.staffId);

		return editedValues;
	}
}
