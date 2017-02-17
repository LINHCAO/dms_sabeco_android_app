/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;


/**
 * 
 *  Du lieu MH HTTM
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVBusinessSupportProgrameDTO {
	private ArrayList<TBHVBusinessSupportPrograme> listPrograme;

	public TBHVBusinessSupportProgrameDTO() {
		listPrograme = new ArrayList<TBHVBusinessSupportPrograme>();
	}
	
	/**
	 * @return the listPrograme
	 */
	public ArrayList<TBHVBusinessSupportPrograme> getListPrograme() {
		return listPrograme;
	}

	/**
	 * @param listPrograme the listPrograme to set
	 */
	public void setListPrograme(ArrayList<TBHVBusinessSupportPrograme> listPrograme) {
		this.listPrograme = listPrograme;
	}
	
}
