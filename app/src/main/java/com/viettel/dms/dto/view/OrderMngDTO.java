/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.SaleOrderDTO;

/** 
 * Order Mng DTO class
 * OrderMngDTO.java
 * @version: 1.0 
 * @since:  10:35:25 8 Jan 2014
 */
public class OrderMngDTO {
	public CustomerDTO customer = new CustomerDTO();
	public SaleOrderDTO saleOrder = new SaleOrderDTO();
}
