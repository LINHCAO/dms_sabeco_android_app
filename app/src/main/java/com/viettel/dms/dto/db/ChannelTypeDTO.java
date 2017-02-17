/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

/**
 *  Thong tin loai khach hang, nhan vien
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class ChannelTypeDTO extends AbstractTableDTO {
	// ma loai channel
	public String channelTypeCode ;
	// ten kenh nhan vien, khach hang,..
	public String channelTypeName ; 
	// ?
	public int parentChannelTypeId ;
	// trang thai loai DV, NV, KH
	public int status ; 
	//phan loai theo tung doi tuong: voi doi tuong NV 1:preSale, 
	//2: VanSale,3:NVTT,4: NVGH,5: NVGS;6:TBHM;7TBHV
	public int objectType ; 
	//1: dung cho loai don vi, 2: dung cho loai NV, 3: dung cho loai KH, 
	//6: kieu nhan vien ban hang, 7: Hieu xe
	public int type ; 
	//sku
	public int SKU ; 
	// id channel
	public int channelTypeId ; 
	
	public ChannelTypeDTO(){
		super(TableType.CHANNEL_TYPE_TABLE);
	}
	
	
	/**
	 * Khoi tao thong tin sau khi query database 
	 * @author : BangHN
	 * since : 1.0
	 */
	public void initCustomerTypeDTOFromCursor(Cursor c) {
		channelTypeCode = (c.getString(c.getColumnIndex("ctc")));
		channelTypeName = (c.getString(c.getColumnIndex("ctn")));
		channelTypeId = (c.getInt(c.getColumnIndex("cti")));
	}
}
