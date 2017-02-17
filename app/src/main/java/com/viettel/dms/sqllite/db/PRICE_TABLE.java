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
import com.viettel.dms.dto.db.PriceDTO;
import com.viettel.utils.VTLog;

/**
 *  Luu gia ca san pham
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class PRICE_TABLE extends ABSTRACT_TABLE {
	// ma gia
	public static final String PRICE_ID = "PRICE_ID";
	// ma san pham
	public static final String PRODUCT_ID = "PRODUCT_ID";
	// hieu luc tu ngay
	public static final String FROM_DATE = "FROM_DATE";
	// hieu luc den ngay
	public static final String TO_DATE = "TO_DATE";
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
	// gia chua vat
	public static final String PRICE_NOT_VAT = "PRICE_NOT_VAT";
	// vat
	public static final String VAT = "VAT";
	
	private static final String TABLE_PRICE = "PRICE";
	
	public PRICE_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PRICE;
		this.columns = new String[] {PRICE_ID, PRODUCT_ID ,STATUS,FROM_DATE,TO_DATE,CREATE_USER,UPDATE_USER,CREATE_DATE,
				UPDATE_DATE,PRICE,PRICE_NOT_VAT,VAT, SYN_STATE};
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
		ContentValues value = initDataRow((PriceDTO) dto);
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
	public long insert(PriceDTO dto) {
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
		PriceDTO disDTO = (PriceDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.priceId };
		return update(value, PRICE_ID + " = ?", params);
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
		return delete(PRICE_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		PriceDTO disDTO = (PriceDTO)dto;
		String[] params = { "" + disDTO.priceId };
		return delete(PRICE_ID + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: DoanDM replaced
	 * @param id
	 * @return: DisplayPrdogrameLvDTO
	 * @throws:
	 */
	public PriceDTO getRowById(String id) {
		PriceDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(
					PRICE_ID + " = ?" , params, null, null, null);
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

	private PriceDTO initLogDTOFromCursor(Cursor c) {
		PriceDTO dpDetailDTO = new PriceDTO();
		dpDetailDTO.priceId = (c.getInt(c.getColumnIndex(PRICE_ID)));
		dpDetailDTO.productId = (c.getInt(c.getColumnIndex(PRODUCT_ID)));
		dpDetailDTO.status = (c.getInt(c.getColumnIndex(STATUS)));
		dpDetailDTO.fromDate = (c.getString(c.getColumnIndex(FROM_DATE)));
		dpDetailDTO.toDate = (c.getString(c.getColumnIndex(TO_DATE)));
		dpDetailDTO.createUser = (c.getString(c.getColumnIndex(CREATE_USER)));
		
		dpDetailDTO.updateUser = (c.getString(c.getColumnIndex(UPDATE_USER)));
		dpDetailDTO.createDate = (c.getString(c.getColumnIndex(CREATE_DATE)));
		dpDetailDTO.updateDate = (c.getString(c.getColumnIndex(UPDATE_DATE)));
		dpDetailDTO.price = (c.getFloat(c.getColumnIndex(PRICE)));
		
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
	public Vector<PriceDTO> getAllRow() {
		Vector<PriceDTO> v = new Vector<PriceDTO>();
		Cursor c = null;
		try {
			c = query(null,
					null, null, null, null);
		} catch (Exception e) {
			VTLog.i("error", e.toString());
		}
		if (c != null) {
			PriceDTO price;
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

	private ContentValues initDataRow(PriceDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PRICE_ID, dto.priceId);
		editedValues.put(PRODUCT_ID, dto.productId);
		editedValues.put(CREATE_USER, dto.createUser);
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(FROM_DATE, dto.fromDate);
		editedValues.put(TO_DATE, dto.toDate);
		editedValues.put(STATUS, dto.status);
		
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);
		editedValues.put(PRICE, dto.price);

		return editedValues;
	}
}
