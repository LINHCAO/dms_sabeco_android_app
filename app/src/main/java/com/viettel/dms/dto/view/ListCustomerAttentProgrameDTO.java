/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Danh sach khach hang tham gia chuong trinh trung bay
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class ListCustomerAttentProgrameDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CustomerAttentProgrameDTO> listCustomer = new ArrayList<CustomerAttentProgrameDTO>();
	private int totalSize;
	/**
	 * @return the listCustomer
	 */
	public List<CustomerAttentProgrameDTO> getListCustomer() {
		return listCustomer;
	}
	/**
	 * @param listCustomer the listCustomer to set
	 */
	public void setListCustomer(List<CustomerAttentProgrameDTO> listCustomer) {
		this.listCustomer = listCustomer;
	}
	/**
	 * @return the totalSize
	 */
	public int getTotalSize() {
		return totalSize;
	}
	/**
	 * @param total the totalSize to set
	 */
	public void setTotalSize(int total) {
		this.totalSize = total;
	}
}
