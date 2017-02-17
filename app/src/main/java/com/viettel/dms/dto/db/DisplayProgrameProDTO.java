/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Thong tin san pham cua chuong trinh trung bay
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class DisplayProgrameProDTO extends AbstractTableDTO {
	// ma CTTB
	public String displayProgrameCode;
	// ma san pham
	public String productCode;
	// ma nganh san pham
	public String category;
	// tu ngay
	public String fromeDate;
	// den ngay
	public String toDate;
	// trang thai
	public int status;
	
	public DisplayProgrameProDTO(){
		super(TableType.DISPLAY_PROGRAME_PRODUCT_TABLE);
	}
}
