/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.PromotionShopMapDTO;

/**
 *  Thong tin khuyen mai cua SHOP
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 2.1
 */
public class PROMOTION_SHOP_MAP extends ABSTRACT_TABLE {
	// id promotion shop
	public static final String PROMOTION_SHOP_MAP_ID = "PROMOTION_SHOP_MAP_ID";
	// id CTKM
	public static final String PROMOTION_PROGRAM_ID = "PROMOTION_PROGRAM_ID";
	// id shop
	public static final String SHOP_ID = "SHOP_ID";
	// so luong toi da
	public static final String QUANTITY_MAX = "QUANTITY_MAX";
	// so luong thuc nhan
	public static final String QUANTITY_RECEIVED = "QUANTITY_RECEIVED";
	// ?
	public static final String STATUS = "STATUS";
	// so xuat ap dung cho KH voi TH CTKM chi den NPP
	public static final String QUANTITY_CUSTOMER = "QUANTITY_CUSTOMER";
	// doi tuong ap dung 1: chi ap dung den NPP, 2: ap dung den NPP, loai KH; 3: ap dung den tan KH
	public static final String OBJECT_APPLY = "OBJECT_APPLY";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	private static final String TABLE_PROMOTION_SHOP_MAP = "PROMOTION_SHOP_MAP";
	
	public PROMOTION_SHOP_MAP(SQLiteDatabase mDB) {
		this.tableName = TABLE_PROMOTION_SHOP_MAP;
		this.columns = new String[] { PROMOTION_SHOP_MAP_ID,
				PROMOTION_PROGRAM_ID, SHOP_ID, QUANTITY_MAX, QUANTITY_RECEIVED, STATUS,
				QUANTITY_CUSTOMER, OBJECT_APPLY, CREATE_USER, UPDATE_USER, CREATE_DATE, CREATE_USER, UPDATE_USER};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		this.mDB = mDB;

	}

	/**
	 * Them 1 dong xuong db
	 * @author: BangHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long insert(AbstractTableDTO dto) {
		ContentValues value = initDataRow((PromotionShopMapDTO) dto);
		return insert(null, value);
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * @author: BangHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(PromotionShopMapDTO dto) {
		ContentValues value = initDataRow(dto);
		return insert(null, value);
	}
	
	/**
	 * Update 1 dong xuong db
	 * @author: BangHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	@Override
	public long update(AbstractTableDTO dto) {
		PromotionShopMapDTO disDTO = (PromotionShopMapDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.promotionShopMapId };
		return update(value, PROMOTION_SHOP_MAP_ID + " = ?", params);
	}

	/**
	 * Xoa 1 dong cua CSDL
	 * @author: BangHN
	 * @param id
	 * @return: int
	 * @throws:
	 */
	public int delete(String id) {
		String[] params = { id };
		return delete(PROMOTION_SHOP_MAP_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		PromotionShopMapDTO proDetail = (PromotionShopMapDTO)dto;
		String[] params = { "" + proDetail.promotionShopMapId };
		return delete(PROMOTION_SHOP_MAP_ID + " = ?", params);
	}


	
	/**
	* intit du lieu update vao bang
	* @author: BangHN
	* @return
	* @return: ContentValues
	*/
	private ContentValues initDataRow(PromotionShopMapDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PROMOTION_SHOP_MAP_ID, dto.promotionShopMapId);
		editedValues.put(PROMOTION_PROGRAM_ID, dto.promotionProgrameId);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(QUANTITY_MAX, dto.quantityMax);
		editedValues.put(QUANTITY_RECEIVED, dto.quantityReceived);
		editedValues.put(STATUS, dto.status);
		
		editedValues.put(QUANTITY_CUSTOMER, dto.quantityCustomer);
		editedValues.put(OBJECT_APPLY, dto.objectApply);
		editedValues.put(CREATE_USER, dto.createUser);
		
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);

		return editedValues;
	}
}
