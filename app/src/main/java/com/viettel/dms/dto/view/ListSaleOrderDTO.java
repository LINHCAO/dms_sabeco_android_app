/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.SaleOrderDTO;

/**
 * List Sale Order DTO class
 * ListSaleOrderDTO.java
 * @version: 1.0 
 * @since:  10:33:22 8 Jan 2014
 */
public class ListSaleOrderDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	public ArrayList<OrderDetailViewDTO> listData;
	public SaleOrderDTO saleOrderDTO;
	public int numPage = 0;

	public ListSaleOrderDTO() {
		this.listData = new ArrayList<OrderDetailViewDTO>();
		this.saleOrderDTO = new SaleOrderDTO();
		this.numPage = 0;
	}
}
