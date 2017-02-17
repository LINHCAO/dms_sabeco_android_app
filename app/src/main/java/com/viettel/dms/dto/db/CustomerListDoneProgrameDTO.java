/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.ArrayList;
import java.util.List;

  
/**
 * Danh sach khach hang tham gia xong 1 chu ki tinh thuong
 * CustomerListDoneProgrameDTO.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:14:41 13-06-2014
 */
@SuppressWarnings("serial")
public class CustomerListDoneProgrameDTO extends AbstractTableDTO{
	public int totalRow;
	// Danh sach khach hang
	public List<CustomerInputQuantityDTO> listCustomer = new ArrayList<CustomerInputQuantityDTO>();
}
