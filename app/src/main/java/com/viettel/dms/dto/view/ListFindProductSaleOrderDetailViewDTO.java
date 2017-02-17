/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *  list find product sale order detail view and total object
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class ListFindProductSaleOrderDetailViewDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// total object after research
	public int totalObject;
	// list object in page
	public ArrayList<FindProductSaleOrderDetailViewDTO> listObject;
	
	public ListFindProductSaleOrderDetailViewDTO(){
		totalObject = 0;
		listObject = new ArrayList<FindProductSaleOrderDetailViewDTO>();
	}
}
