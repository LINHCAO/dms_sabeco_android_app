/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 *  Mo ta muc dich cua lop (interface)
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 11:03:51 AM | Jun 11, 2012
 */
public class ListFindProductOrderListDTO {
	// number product find result
	private int totalProduct = 0;
	// list product we can find
	private List<ProductDTO> listProduct = new ArrayList<ProductDTO>();
	

	/**
	 * 
	*  parse data 
	*  @author: HaiTC3
	*  @since: 11:06:31 AM | Jun 11, 2012
	*  @param response
	*  @return: void
	*  @throws:
	 */
	public void parseFromJson(JSONObject response) {
		try {
			setTotalProduct(response.getInt("totalProduct"));
			JSONArray jArray = response.getJSONArray("list");
			if (jArray != null) {
				for (int i = 0, size = jArray.length(); i < size; i++) {
					ProductDTO cusDTO = new ProductDTO();
//					cusDTO.parseFromJson(jArray.getJSONObject(i));
					listProduct.add(cusDTO);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	/**
	 * @return the totalProduct
	 */
	public int getTotalProduct() {
		return totalProduct;
	}

	/**
	 * @param totalProduct the totalProduct to set
	 */
	public void setTotalProduct(int totalProduct) {
		this.totalProduct = totalProduct;
	}
	/**
	 * @return the listProduct
	 */
	public List<ProductDTO> getListProduct() {
		return listProduct;
	}
	
	/**
	 * @param listProduct the listProduct to set
	 */
	public void setListProduct(List<ProductDTO> listProduct) {
		this.listProduct = listProduct;
	}
}
