/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

/**
 * 
 * Mo ta cho class
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class ProductCategoryDTO extends AbstractTableDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		// id
		public int id;
		// ma category
		public String categoryCode;
		// ten category
		public String categoryName;
		// trang thai: 0: het hieu luc, 1: dang hieu luc
		public int type;
		// ghi chu
		public int parentCategoryId;
}
