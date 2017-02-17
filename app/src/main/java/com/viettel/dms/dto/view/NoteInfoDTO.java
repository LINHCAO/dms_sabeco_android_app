/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.viettellib.json.me.JSONObject;


/**
 *  note information
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class NoteInfoDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// feed back dto
	public FeedBackDTO feedBack;
	// customer DTO
	public CustomerDTO customerDTO;
	
	public NoteInfoDTO(){
		feedBack = new FeedBackDTO();
		customerDTO = new CustomerDTO();
	}
	
	/**
	 * 
	*  init data with cursor
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initDateWithCursor(Cursor c){
		feedBack.initDataWithCursor(c);
		customerDTO.setCustomerId(Long.parseLong(feedBack.customerId));
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			customerDTO.setCustomerCode(c.getString(c.getColumnIndex("CUSTOMER_CODE")));
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			customerDTO.setCustomerName(c.getString(c.getColumnIndex("CUSTOMER_NAME")));
		}
	}
	
	/**
	 * 
	*  general json object update feed back
	*  @author: HaiTC3
	*  @return
	*  @return: JSONObject
	*  @throws:
	 */
	public JSONObject generateJsonUpdateFeedBack(){
		JSONObject result = feedBack.generateUpdateFeedbackSql();
		return result;
	}
}
