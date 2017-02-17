/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 *  Cac sub-cat can co trong CTTB
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class DisplayProgrameSubDTO extends AbstractTableDTO {
	// id cua bang
	public int displayProgrameSubCatId ;
	// id CTTB
	public int displayProgrameId ;
	// ma sub_cat cua san pham
	public String subCat ; 
	// trang thai
	public int status ;
	
	public DisplayProgrameSubDTO(){
		super(TableType.DISPLAY_PROGRAME_SUB_CAT_TABLE);
	}
}
