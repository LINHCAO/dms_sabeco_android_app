/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.ProductDTO;

/**
 * 
 * dto danh sach san pham
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class ListProductDTO {
	// total object after research
	public int total;
	// list object in page
	public List<ProductDTO> producList;
	
	public ListProductDTO(){
		total = 0;
		producList = new ArrayList<ProductDTO>();
	}
}
