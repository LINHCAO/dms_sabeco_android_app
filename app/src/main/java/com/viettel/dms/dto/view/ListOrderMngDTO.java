/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.me.NotifyOrderDTO;

/** 
 * List Order Mng DTO class
 * ListOrderMngDTO.java
 * @version: 1.0 
 * @since:  10:31:22 8 Jan 2014
 */
public class ListOrderMngDTO {
	public int total=0;
	public int totalIsSend=0;
	public List<SaleOrderViewDTO> listDTO;
	// don hang loi can thong bao
	public NotifyOrderDTO notifyDTO;

	public ListOrderMngDTO() {
		listDTO = new ArrayList<SaleOrderViewDTO>();
		notifyDTO = new NotifyOrderDTO();
	}

	public List<SaleOrderViewDTO> getListDTO() {
		return listDTO;
	}

}
