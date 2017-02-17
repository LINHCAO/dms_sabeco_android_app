/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

/** 
 * List Customer DTO class
 * ListCustomerDTO.java
 * @version: 1.0 
 * @since:  10:31:00 8 Jan 2014
 */
public class ListCustomerDTO {
	private int totalItem;
	private List<CustomerListDTO> listCusDTO = null;

	public ListCustomerDTO() {
		this.totalItem = 0;
		listCusDTO = new ArrayList<CustomerListDTO>();
	}

	public int getTotalItem() {
		return totalItem;
	}

	public void setTotalItem(int totalItem) {
		this.totalItem = totalItem;
	}

	public List<CustomerListDTO> getListCusDTO() {
		return listCusDTO;
	}

	public void setListCusDTO(List<CustomerListDTO> listCusDTO) {
		this.listCusDTO = listCusDTO;
	}
}
