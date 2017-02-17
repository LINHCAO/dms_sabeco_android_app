/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import com.viettel.dms.dto.db.BillDTO;

/** 
 * List Bill DTO class
 * ListBillDTO.java
 * @version: 1.0 
 * @since:  10:30:30 8 Jan 2014
 */
public class ListBillDTO {
	public ArrayList<BillDTO> listDTO = new ArrayList<BillDTO>();
	int totalPage = 0;
}
