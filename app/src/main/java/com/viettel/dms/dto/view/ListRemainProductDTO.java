/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * List Remain Product DTO class
 * ListRemainProductDTO.java
 * @version: 1.0 
 * @since:  10:32:49 8 Jan 2014
 */
@SuppressWarnings("serial")
public class ListRemainProductDTO implements Serializable{
	public int total;
	public ArrayList<RemainProductViewDTO> listDTO = new ArrayList<RemainProductViewDTO>();
	
	public ListRemainProductDTO(){
		
	}
}
