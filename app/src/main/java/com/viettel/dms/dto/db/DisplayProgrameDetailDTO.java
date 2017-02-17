/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Chi tiet chuong trinh trung bay
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class DisplayProgrameDetailDTO extends AbstractTableDTO {
	// id cua bang
	public int displayProgrameDetailId ; 
	// id cua CTTB
	public int displayProgrameId ;
	// id san pham
	public int productId ;
	// so luong can TB
	public int qualtity ;
	// truong nay chua dung
	public String brandGroup ;
	// ma nganh hang
	public String categoryCode ; 
	// nguoi tao
	public String createUser ;
	// nguoi cap nhat
	public String updateUser ;
	// ngay tao
	public String createDate ;
	// ngay cap nhat
	public String updateDate ; 
	
	public DisplayProgrameDetailDTO() {
		super(TableType.DISPLAY_PROGRAME_DETAIL_TABLE);
	}
}
