/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.sqllite.db;

import net.sqlcipher.database.SQLiteDatabase;
import android.content.ContentValues;

import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.view.ListProductQuantityJoin;
import com.viettel.dms.dto.view.ListProductQuantityJoin.ProductQuantityJoin;
import com.viettel.dms.util.DateUtils;
 
/**
 * 
 * PRO_DISPLAY_PROGRAM_TABLE.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:46:55 12-05-2014
 */
public class PRO_CUS_MAP_DETAIL_TABLE extends ABSTRACT_TABLE {

	// id cua bang
	public static final String PRO_CUS_MAP_DETAIL_ID = "PRO_CUS_MAP_DETAIL_ID";
	// ID PRO_CUS_MAP
	public static final String PRO_CUS_MAP_ID = "PRO_CUS_MAP_ID";
	// id san pham
	public static final String PRODUCT_ID = "PRODUCT_ID"; 
	// So luong dang ki
	public static final String QUANTITY = "QUANTITY"; 
	// ngay tao
	public static final String CREATE_DATE = "CREATE_DATE";
	// nguoi tao
	public static final String CREATE_USER = "CREATE_USER";
	// ngay cap nhat
	public static final String UPDATE_DATE = "UPDATE_DATE";
	// nguoi cap nhat
	public static final String UPDATE_USER = "UPDATE_USER";
	// ma nhan vien
	public static final String STAFF_ID = "STAFF_ID";
	
	public static final String PRO_CUS_MAP_DETAIL_TABLE = "PRO_CUS_MAP_DETAIL";
	//private TABLE_ID table;
	 
	public PRO_CUS_MAP_DETAIL_TABLE(SQLiteDatabase mDB){
		this.tableName = PRO_CUS_MAP_DETAIL_TABLE;

		this.columns = new String[] {PRO_CUS_MAP_ID, PRO_CUS_MAP_ID, PRODUCT_ID, QUANTITY, CREATE_DATE, CREATE_USER, UPDATE_DATE,
				UPDATE_USER, STAFF_ID};
		this.sqlGetCountQuerry += this.tableName + ";";
		this.sqlDelete += this.tableName + ";";
		sqlInsert+=tableName+" ( ";
		this.mDB = mDB;
	}
	
	@Override
	protected long insert(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long update(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected long delete(AbstractTableDTO dto) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Xoa cac chi tiet tham gia chuong trinh ho tro ban hang cua HH
	 * @author: quangvt1
	 * @since: 15:37:22 19-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param pro_cus_map_id
	 * @return
	 */
	public boolean deleteDetailProCusMap(String pro_cus_map_id) { 
		String where = PRO_CUS_MAP_ID + " = ?";
		String[] args = {pro_cus_map_id};
		return delete(where, args) > -1; 
	}

	/**
	 * Luu thong tin thay doi
	 * @author: quangvt1
	 * @since: 17:05:56 21-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param pro_cus_map_id2
	 * @param productList
	 * @return
	 */
	public boolean saveChangeDetail(String pro_cus_map_id,
			ListProductQuantityJoin productList, String staffId, String staffCode) {
		TABLE_ID table = new TABLE_ID(mDB);
		boolean isSuccess = true;
		long id = table.getMaxId(PRO_CUS_MAP_DETAIL_TABLE);
		for (ProductQuantityJoin p : productList.listProduct) {
			if(p.newQuantity > 0){
				if(p.oldQuantity == 0){
					// Insert dong moi
					p.maxId = id + "";
					isSuccess = insertNewDetail(pro_cus_map_id, p, staffId, staffCode);
					if(!isSuccess){
						break;
					}
				}else{
					isSuccess = updateQuantity(pro_cus_map_id, p, staffId, staffCode);
					
					// Cap nhat
					if(!isSuccess){
						break;
					}
				}
			}
			id++;
		}
		return isSuccess;
	}

	/**
	 * Update so luong
	 * @author: quangvt1
	 * @since: 17:38:07 21-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param pro_cus_map_id2
	 * @param p
	 * @param staffId
	 * @param staffCode
	 * @return
	 */
	private boolean updateQuantity(String pro_cus_map_id,
			ProductQuantityJoin p, String staffId, String staffCode) {
		ContentValues values = new ContentValues();
		values.put(QUANTITY, p.newQuantity);
		values.put(UPDATE_DATE, DateUtils.now());
		values.put(UPDATE_USER, staffCode);
		
		String where = PRO_CUS_MAP_DETAIL_ID + " = ?";
		String[] args = {p.pro_cus_map_detail_id};
		return update(values, where, args) > -1;  
	}

	/**
	 * Insert san luong tham gia
	 * @author: quangvt1
	 * @since: 17:21:33 21-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param pro_cus_map_id2
	 * @param p
	 * @return
	 */
	private boolean insertNewDetail(String pro_cus_map_id,
			ProductQuantityJoin p, String staffId, String staffCode) {
		ContentValues values = new ContentValues();
		values.put(PRO_CUS_MAP_DETAIL_ID, p.maxId);
		values.put(PRO_CUS_MAP_ID, pro_cus_map_id);
		values.put(PRODUCT_ID, p.product_id);
		values.put(QUANTITY, p.newQuantity);
		values.put(CREATE_DATE, DateUtils.now());
		values.put(CREATE_USER, staffCode);
		values.put(STAFF_ID, staffId);
		
		return this.insert(null, values) > -1;
	} 
}
