/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import android.database.Cursor;

/**
 * Thong tin hien thi san pham trung bay
 * 
 * @author: SoaN
 * @version: 1.0
 * @since: Jun 20, 2012
 */

public class DisplayProgrameItemViewDTO {

	// ma CTTB
	public static final String PRODUCT_CODE = "PRODUCT_CODE";
	// ten CTTB
	public static final String PRODUCT_NAME = "PRODUCT_NAME";

	private String displayProductCode;
	private String displayProductName;

	/**
	 * @return the displayProductCode
	 */
	public String getDisplayProductCode() {
		return displayProductCode;
	}

	/**
	 * @param displayProductCode
	 *            the displayProductCode to set
	 */
	public void setDisplayProductCode(String displayProductCode) {
		this.displayProductCode = displayProductCode;
	}

	/**
	 * @return the displayProductName
	 */
	public String getDisplayProductName() {
		return displayProductName;
	}

	/**
	 * @param displayProductName
	 *            the displayProductName to set
	 */
	public void setDisplayProductName(String displayProductName) {
		this.displayProductName = displayProductName;
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: SoaN
	 * @param c
	 * @return
	 * @return: DisplayProgrameDTO
	 * @throws:
	 */

	public void initDisplayProgrameObjectFromGetStatement(
			Cursor c) {
		
		if (c.getColumnIndex(PRODUCT_CODE) >= 0) {
			displayProductCode = c.getString(c
					.getColumnIndex(PRODUCT_CODE));
		} else {
			displayProductCode = "";
		}

		if (c.getColumnIndex(PRODUCT_NAME) >= 0) {
			displayProductName = c.getString(c
					.getColumnIndex(PRODUCT_NAME));
		} else {
			displayProductName = "";
		}
	}
}
