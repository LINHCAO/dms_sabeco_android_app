/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.PromotionCustomerMapDTO;

/**
 *  Thong tin khuyen mai cua promotion customer
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 2.1
 */
public class PROMOTION_CUSTOMER_MAP extends ABSTRACT_TABLE {
	// id promotion map
	public static final String PROMOTION_CUSTOMER_MAP_ID = "PROMOTION_CUSTOMER_MAP_ID";
	// id shop max
	public static final String PROMOTION_SHOP_MAP_ID = "PROMOTION_SHOP_MAP_ID";
	// id shop
	public static final String SHOP_ID = "SHOP_ID";
	//customer id
	public static final String CUSTOMER_ID = "CUSTOMER_ID";
	//type customer
	public static final String CUSTOMER_TYPE_ID = "CUSTOMER_TYPE_ID";
	// so luong toi da
	public static final String QUANTITY_MAX = "QUANTITY_MAX";
	// so luong thuc nhan
	public static final String QUANTITY_RECEIVED = "QUANTITY_RECEIVED";
	// ?
	public static final String STATUS = "STATUS";
	//type
	public static final String TYPE = "TYPE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";

	private static final String TABLE_PROMOTION_CUSTOMER_MAP = "PROMOTION_CUSTOMER_MAP";
	
	public PROMOTION_CUSTOMER_MAP(SQLiteDatabase mDB) {
		this.tableName = TABLE_PROMOTION_CUSTOMER_MAP;
		this.columns = new String[] { PROMOTION_CUSTOMER_MAP_ID,
				PROMOTION_SHOP_MAP_ID, SHOP_ID, CUSTOMER_ID, QUANTITY_MAX, QUANTITY_RECEIVED, STATUS,
				TYPE, CREATE_USER, UPDATE_USER, CREATE_DATE, CREATE_USER, UPDATE_USER};
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
		ContentValues value = initDataRow((PromotionCustomerMapDTO) dto);
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
	public long insert(PromotionCustomerMapDTO dto) {
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
		PromotionCustomerMapDTO disDTO = (PromotionCustomerMapDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.promotionCustomerMapId };
		return update(value, PROMOTION_CUSTOMER_MAP_ID + " = ?", params);
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
		return delete(PROMOTION_CUSTOMER_MAP_ID + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		PromotionCustomerMapDTO proDetail = (PromotionCustomerMapDTO)dto;
		String[] params = { "" + proDetail.promotionCustomerMapId };
		return delete(PROMOTION_CUSTOMER_MAP_ID + " = ?", params);
	}


	
	/**
	* intit du lieu update vao bang
	* @author: BangHN
	* @return
	* @return: ContentValues
	*/
	private ContentValues initDataRow(PromotionCustomerMapDTO dto) {
		ContentValues editedValues = new ContentValues();
		editedValues.put(PROMOTION_CUSTOMER_MAP_ID, dto.promotionCustomerMapId);
		editedValues.put(PROMOTION_SHOP_MAP_ID, dto.promotionShopMapd);
		editedValues.put(SHOP_ID, dto.shopId);
		editedValues.put(QUANTITY_MAX, dto.quantityMax);
		editedValues.put(QUANTITY_RECEIVED, dto.quantityReceived);
		editedValues.put(STATUS, dto.status);
		editedValues.put(TYPE, dto.type);
		
		editedValues.put(CUSTOMER_ID, dto.customerId);
		editedValues.put(CUSTOMER_TYPE_ID, dto.customerTypeId);
		editedValues.put(CREATE_USER, dto.createUser);
		
		editedValues.put(UPDATE_USER, dto.updateUser);
		editedValues.put(CREATE_DATE, dto.createDate);
		editedValues.put(UPDATE_DATE, dto.updateDate);

		return editedValues;
	}
}
