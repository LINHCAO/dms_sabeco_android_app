/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Luu max id cua cac table
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class TableIdDTO extends AbstractTableDTO {
	// id bang
	public int id;
	// ten cac table
	public 	String tableName;
	// max Id
	public long maxId;
	// gia tri bo sung khi tao id moi
	public long factor;
	// last syn
	public long lastSynMaxId;
	// id nha pp
	public int shopId;
	// id nhan vien
	public int staffId;
	// trang thai syn
	public int synState;
	
	public TableIdDTO(){
		super(TableType.TABLE_ID_TABLE);
	}
}
