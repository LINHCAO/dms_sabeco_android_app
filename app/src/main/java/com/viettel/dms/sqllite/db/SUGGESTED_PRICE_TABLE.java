/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import java.util.Vector;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.SuggestedPriceDTO;
import com.viettel.utils.VTLog;

/**
 *  Luu gia khi tao don hang cua san pham
 *  @author: Nguyen Thanh Dung
 *  @version: 1.0
 *  @since: 1.0
 */
public class SUGGESTED_PRICE_TABLE extends ABSTRACT_TABLE {
	// ma gia
	public static final String SUGGESTED_PRICE_ID = "SUGGESTED_PRICE_ID";
	// ma san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// hieu luc tu ngay
	public static final String SHOP_ID = "SHOP_ID";
	// hieu luc den ngay
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	// id nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	// 1: hoat dong, 0: ngung
	public static final String STATUS = "STATUS";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// gia
	public static final String PRICE = "PRICE"; 
	
	public static final String TABLE_NAME = "SUGGESTED_PRICE";
	
	public SUGGESTED_PRICE_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_NAME;
		this.columns = new String[] {SUGGESTED_PRICE_ID, PRODUCT_ID ,STATUS,SHOP_ID,CUSTOMER_ID,STAFF_ID,CREATE_USER,UPDATE_USER,CREATE_DATE,
				UPDATE_DATE,PRICE, SYN_STATE};
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
		ContentValues value = initDataRow((SuggestedPriceDTO) dto);
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
	public long insert(SuggestedPriceDTO dto) {
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
		SuggestedPriceDTO disDTO = (SuggestedPriceDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.suggestedPriceId };
		return update(value, SUGGESTED_PRICE_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: TruongHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String code) {
		String[] params = { code };
		return delete(SUGGESTED_PRICE_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		SuggestedPriceDTO disDTO = (SuggestedPriceDTO)dto;
		String[] params = { "" + disDTO.suggestedPriceId };
		return delete(SUGGESTED_PRICE_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public SuggestedPriceDTO getRowById(String id) {
		SuggestedPriceDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					SUGGESTED_PRICE_ID + " = ?" , params, null, null, null);
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

	private SuggestedPriceDTO initLogDTOFromCursor(Cursor c) {
		SuggestedPriceDTO dpDetailDTO = new SuggestedPriceDTO();
		dpDetailDTO.suggestedPriceId = (c.getLong(c.getColumnIndex(SUGGESTED_PRICE_ID)));
		dpDetailDTO.productId = (c.getLong(c.getColumnIndex(PRODUCT_ID)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.shopId = (c.getLong(c.getColumnIndex(SHOP_ID)));
		dpDetailDTO.customerId = (c.getLong(c.getColumnIndex(CUSTOMER_ID)));
		dpDetailDTO.staffId = (c.getLong(c.getColumnIndex(STAFF_ID)));
		
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.price = (c.getDouble(c.getColumnIndex(PRICE)));
		
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
	public Vector<SuggestedPriceDTO> getAllRow() {
		Vector<SuggestedPriceDTO> v = new Vector<SuggestedPriceDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			SuggestedPriceDTO price;
			if (c.moveToFirst()) {
				do {
					price = initLogDTOFromCursor(c);
					v.addElement(price);
				} while (c.moveToNext());
			}
			c.close();
		}
		return v;

	}

	private ContentValues initDataRow(SuggestedPriceDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(SUGGESTED_PRICE_ID, dto.suggestedPriceId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(STAFF_ID, dto.staffId);
		editedValues.put(STATUS, dto.status);
		
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(PRICE, dto.price);

		return editedValues;
	}
	
	/**
	 * Lay 1 dong cua CSDL theo cac dieu kien
	 * @author: DoanDM replaced
	 * @param productId
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public SuggestedPriceDTO getRowByCondition(String productId, String shopId, String customerId) {
		SuggestedPriceDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {productId, shopId, customerId};
			c = query(" PRODUCT_ID = ? AND SHOP_ID = ? AND CUSTOMER_ID = ? and STATUS = 1 ", params, null, null, null);
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
	
	private ContentValues initUpdateRemoveRow(SuggestedPriceDTO dto) {
		ContentValues editValues = new ContentValues();
		editValues.put(SUGGESTED_PRICE_ID, dto.suggestedPriceId);
		editValues.put(STATUS, dto.status);
		editValues.put(UPDATE_USER, dto.updateUser);
		editValues.put(UPDATE_DATE, dto.updateDate);
		
		return editValues;
	}

	/**
	 * Update 1 dong xuong db
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long updateRemoveRow(AbstractTableDTO dto) {
		SuggestedPriceDTO disDTO = (SuggestedPriceDTO)dto;
		ContentValues value = initUpdateRemoveRow(disDTO);
		String[] params = { "" + disDTO.suggestedPriceId };
		return update(value, SUGGESTED_PRICE_ID + " = ?", params);
	}
}
