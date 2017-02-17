/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.Vector;

/** 
 * Category Code DTO class
 * CategoryCodeDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:22:57 8 Jan 2014
 */
public class CategoryCodeDTO {
	public Vector<String> categoryCode;
	public Vector<String> subCategoryCode;
	
	public CategoryCodeDTO(){
		categoryCode = new Vector<String>();
		subCategoryCode = new Vector<String>();
	}
}
