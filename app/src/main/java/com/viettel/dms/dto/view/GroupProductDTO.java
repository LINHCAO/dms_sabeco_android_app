/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

/**
 * dto nhom san pham
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 */
@SuppressWarnings("serial")
public class GroupProductDTO implements Serializable{

	public String groupName;//ten nhom (fino, khac)
	public float target;//chi tieu
	public float achieved;//da dat

	public float rest;// con lai

	public GroupProductDTO(){
		
	}
}
