/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.C2PurchaseDTO;
import com.viettel.viettellib.json.me.JSONArray;

/** 
 * C2 Purchasel View DTO class
 * C2PurchaselViewDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:21:40 8 Jan 2014
 */
public class C2PurchaselViewDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	public C2PurchaseDTO orderInfo;
	public ArrayList<C2PurchaseDetailViewDTO> listProduct = new ArrayList<C2PurchaseDetailViewDTO>();
	public int c2ReportTime = 0;
	public long totalQuantity = 0;
	public C2PurchaselViewDTO() {
		orderInfo = new C2PurchaseDTO();
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

		for (C2PurchaseDetailViewDTO detail: listProduct) {
			if (detail.orderDetail.quantity > 0){
				// insert sales order detail
				listSql.put(detail.orderDetail.generateCreateSql());
			}
		}
			
		return listSql;
	}

}
