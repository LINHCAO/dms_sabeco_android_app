/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.Constants;

/**
 * thong tin nganh hang cua san pham
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class ProductInfoDTO extends AbstractTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id of product info
	public static long productInfoId;
	// code of product info
	public static String productInfoCode;
	// name of product info
	public static String productInfoName;
	// description of product info
	public static String description;
	// status of product info (0: OFF, 1: ON)
	public static int status;
	// type of product info (4:flavour, 5: packing, 1: CAT, 2: SUB CAT, 3:
	// BRAND)
	public static String type;

	public ProductInfoDTO() {
		productInfoId = 0;
		productInfoCode = Constants.STR_BLANK;
		productInfoName = Constants.STR_BLANK;
		description = Constants.STR_BLANK;
		status = 0;
		type = "0";
	}
}
