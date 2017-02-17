/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import java.util.ArrayList;

import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;

/**
 * Customer feedback dto
 * CustomerFeedBackDto.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:08:08 14 Jan 2014
 */
public class CustomerFeedBackDto {
	public int totalFeedBack;
	public int currentPage = 0;
	public CustomerDTO customer;
	public ArrayList<ApParamDTO> typeList;
//	public ArrayList<ApParamDTO> gsnppTypeList;

	public CustomerDTO getCustomer() {
		return customer;
	}

	public void setCustomer(CustomerDTO customer) {
		this.customer = customer;
	}

	public ArrayList<FeedBackDTO> arrItem = new ArrayList<FeedBackDTO>();

	public CustomerFeedBackDto() {
	}

//	public void getGsnppTypeList() {
//		gsnppTypeList = new ArrayList<ApParamDTO>();
//		for (ApParamDTO element : typeList) {
//			if (element.type.equals("FEEDBACK_TYPE_GSNPP")) {
//				gsnppTypeList.add(element);
//			}
//		}
//	}
}
