/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.C2SaleOrderDTO;
import com.viettel.viettellib.json.me.JSONArray;

/** 
 * C2 Sale Orderl View DTO class
 * C2SaleOrderlViewDTO.java
 * @version: 1.0 
 * @since:  10:22:18 8 Jan 2014
 */
public class C2SaleOrderlViewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public C2SaleOrderDTO orderInfo;
	public ArrayList<C2SaleOrderDetailViewDTO> listProduct = new ArrayList<C2SaleOrderDetailViewDTO>();
	public int c2ReportTime = 0;
	public long totalQuantity = 0;
	public C2SaleOrderlViewDTO() {
		orderInfo = new C2SaleOrderDTO();
	}
	
	/**
	*  Phat sinh ra cau lenh sql chuyen len server
	*  @author: dungnt19
	*  @return: JSONArray
	*  @throws:
	 */
	public JSONArray generateNewOrderSql() {
		JSONArray listSql = new JSONArray();
		
		// insert into SalesOrder
		listSql.put(orderInfo.generateCreateSql());

		for (C2SaleOrderDetailViewDTO detail: listProduct) {
			if (detail.orderDetail.amount > 0){
				// insert sales order detail
				listSql.put(detail.orderDetail.generateCreateSql());
			}
		}
			
		return listSql;
	}

}
