/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ProductCategoryDTO;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class PRODUCT_CATEGORY_TABLE extends ABSTRACT_TABLE {
	// id
	public static final String ID = "ID";
	// ma category
	public static final String CATEGORY_CODE = "CATEGORY_CODE";
	// ten category
	public static final String CATEGORY_NAME = "CATEGORY_NAME";
	// trang thai: 0: category
	public static final String TYPE = "TYPE";
	// id cua parent
	public static final String PARENT_CATEGORY_ID = "PARENT_CATEGORY_ID";
	
	private static final String TABLE_PRODUCT_CATEGORY = "PRODUCT_CATEGORY";


	public PRODUCT_CATEGORY_TABLE(SQLiteDatabase mDB) {
		this.tableName = TABLE_PRODUCT_CATEGORY;
		this.columns = new String[] { ID, CATEGORY_CODE, CATEGORY_NAME, TYPE, PARENT_CATEGORY_ID };
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
		ContentValues value = initDataRow((ProductCategoryDTO) dto);
		return insert(null, value);
	}

	/**
	*  Mo ta chuc nang cua ham
	*  @author: ThanhNN8
	*  @param dto
	*  @return
	*  @return: ContentValues
	*  @throws: 
	*/
	private ContentValues initDataRow(ProductCategoryDTO dto) {
		// TODO Auto-generated method stub
		ContentValues editedValues = new ContentValues();
		editedValues.put(ID, dto.id);
		editedValues.put(CATEGORY_CODE, dto.categoryCode);
		editedValues.put(CATEGORY_NAME, dto.categoryName);
		editedValues.put(TYPE, dto.type);
		editedValues.put(PARENT_CATEGORY_ID, dto.parentCategoryId);

		return editedValues;
	}

	/**
	 * 
	 * them 1 dong xuong CSDL
	 * @author: TruongHN
	 * @param dto
	 * @return: long
	 * @throws:
	 */
	public long insert(ProductCategoryDTO dto) {
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
		ProductCategoryDTO disDTO = (ProductCategoryDTO)dto;
		ContentValues value = initDataRow(disDTO);
		String[] params = { "" + disDTO.categoryCode };
		return update(value, CATEGORY_CODE + " = ?", params);
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
		return delete(CATEGORY_CODE + " = ?", params);
	}
	
	public long delete(AbstractTableDTO dto) {
		ProductCategoryDTO paramDTO = (ProductCategoryDTO) dto;
		String[] params = { String.valueOf(paramDTO.categoryCode)};
		return delete(CATEGORY_CODE + " = ?", params);
	}

	/**
	 * Lay 1 dong cua CSDL theo id
	 * @author: TruongHN
	 * @param id
	 * @return: ApParamDTO
	 * @throws:
	 */
	public ProductCategoryDTO getRowById(String id) {
		ProductCategoryDTO dto = null;
		Cursor c = null;
		try {
			String[]params = {id};
			c = query(CATEGORY_CODE + " = ?" , params, null, null, null);
		} catch (Exception ex) {
			c = null;
		}
		if (c != null) {
			if (c.moveToFirst()) {
				dto = initProductCategoryDTOFromCursor(c);
			}
		}
		if (c != null) {
			c.close();
		}
		return dto;
	}

	private ProductCategoryDTO initProductCategoryDTOFromCursor(Cursor c) {
		ProductCategoryDTO dto = new ProductCategoryDTO();
		dto.id = (c.getInt(c.getColumnIndex(ID)));
		dto.categoryCode = (c.getString(c.getColumnIndex(CATEGORY_CODE)));
		dto.categoryName = (c.getString(c.getColumnIndex(CATEGORY_NAME)));
		dto.type = (c.getInt(c.getColumnIndex(TYPE)));
		dto.parentCategoryId = (c.getInt(c.getColumnIndex(PARENT_CATEGORY_ID)));
		return dto;
	}
}
