/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * thong tin man hinh bao cao thong ke don tong trong ngay
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class SaleStatisticsProductInDayInfoViewDTO {
	// list product info
	public ArrayList<SaleProductInfoDTO> listProduct = new ArrayList<SaleProductInfoDTO>();
	// list industry product
	public ArrayList<String> listIndustry = new ArrayList<String>();
	
	/**
	 * construct object
	 */
	public SaleStatisticsProductInDayInfoViewDTO(){
		listIndustry = new ArrayList<String>();
		listIndustry.add(StringUtil.getString(R.string.ALL));
		listProduct = new ArrayList<SaleProductInfoDTO>();
	}
}
