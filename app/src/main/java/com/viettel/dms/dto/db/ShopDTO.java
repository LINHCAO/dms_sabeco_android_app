/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

/**
 *  Luu thong tin NPP
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class ShopDTO extends AbstractTableDTO {
	// id npp
	public long shopId ;
	// ma NPP
	public String shopCode ;
	// ten NPP
	public String shopName ; 
	// id NPP cha
	public int parentShopId ; 
	// duong
	public String street ; 
	// phuong
	public String ward ;
	// quan/huyen
	public String district ;
	// tinh
	public String province ;
	// ma vung
	public String areaCode ;
	// nuoc
	public String country ;
	// sdt ban
	public String phone ;
	// di dong
	public String telephone ;
	// loai NPP, 1: NPP(default) 2: Vinamil
	public int shopType ; 
	// nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ;
	// loai kenh
	public int channelTypeId ;
	// email giam sat NPP
	public String email ;
	// duong dan den NPP
	public String shopPath ; 
	
	public ShopDTO(){
		super(TableType.SHOP_TABLE);
	}
	
	/**
	* Parse du lieu shop 
	* @author: BangHN
	* @param c
	* @return: void
	*/
	public void parseDataFromCusor(Cursor c){
		if (c.getColumnIndex("SHOP_ID") > -1) {
			shopId = c.getInt(c.getColumnIndex("SHOP_ID"));
		}
		if (c.getColumnIndex("SHOP_CODE") > -1) {
			shopCode = c.getString(c.getColumnIndex("SHOP_CODE"));
		}
		if (c.getColumnIndex("SHOP_NAME") > -1) {
			shopName = c.getString(c.getColumnIndex("SHOP_NAME"));
		}
	}
}
