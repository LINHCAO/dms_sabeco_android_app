/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Du lieu MH KH tham gia CT HTBH CustomerAttendProgramListDTO.java
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 11:06:45 11-05-2014
 */
@SuppressWarnings("serial")
public class CustomerAttendProgramListDTO implements Serializable {
	public long staffId;
	public long programID;
	public String staffCode;
	public String day;
	public int type;
	public int totalCustomer;
	// ma khach hang
	public String codeCus;
	// ten hoac dia chi khach hang
	public String nameCus;
	public ArrayList<CustomerAttendProgramListItem> cusList;
	public int curPage = -1;
	// Muc ma nguoi dung tham gia
	public String level;

	public CustomerAttendProgramListDTO() {
		cusList = new ArrayList<CustomerAttendProgramListItem>();
	}
}
