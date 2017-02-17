/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.StaffPositionLogDTO;

/** 
 * Staff Position DTO class
 * StaffPositionDTO.java
 * @version: 1.0 
 * @since:  10:41:34 8 Jan 2014
 */
@SuppressWarnings("serial")
public class StaffPositionDTO implements Serializable{
	ArrayList<StaffPositionLogDTO> itemList;
	
	public StaffPositionDTO(){
		itemList = new ArrayList<StaffPositionLogDTO>();
	}
}
