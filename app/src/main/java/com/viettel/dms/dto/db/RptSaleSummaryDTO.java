/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.Constants;

/**
 * tong hop thong tin ban hang cua NVBH
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class RptSaleSummaryDTO extends AbstractTableDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// id of table
	public static long rptSaleSummaryId;
	// Tuy vao Object_type de link den bang tuong ung (1: SHOP_ID, 2: STAFF_ID)
	public static String objectId;
	// 1: NPP, 2: STAFF
	public static String objectType;
	// Du lieu cho ngay hoac thang, neu la thang thi chinh la ngay dau thang
	public static String saleDate;
	// 0: OFF, 1: ON (default: 1)
	public static String status;
	// code dung de truy van tuy nghiep vu
	public static String code;
	// ten dung de hien thi tren man hinh
	public static String name;
	// mo ta them, khong hien thi tren man hinh, dung de tham khao
	public static String description;
	// gia tri theo ke hoach
	public static String plan;
	// gia tri thuc te theo tung chi tieu
	public static String actually;
	// 1: theo ngay, 2: Luy ke theo thang
	public static String type;
	// 1: Gia tri luu tru cho tinh toan, 2: dung de hien thi tren tablet
	public static String viewType;
	// Thu tu sap xep cac item tren man hinh
	public static String ordinal;

	public RptSaleSummaryDTO() {
		rptSaleSummaryId = 0;
		objectId = Constants.STR_BLANK;
		objectType = Constants.STR_BLANK;
		saleDate = Constants.STR_BLANK;
		status = Constants.STR_BLANK;
		code = Constants.STR_BLANK;
		name = Constants.STR_BLANK;
		description = Constants.STR_BLANK;
		plan = Constants.STR_BLANK;
		actually = Constants.STR_BLANK;
		type = Constants.STR_BLANK;
		viewType = Constants.STR_BLANK;
		ordinal = Constants.STR_BLANK;
	}
}
