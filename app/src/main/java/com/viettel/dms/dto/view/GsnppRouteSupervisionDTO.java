/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/** 
 * Gsnpp Route Supervision DTO class
 * GsnppRouteSupervisionDTO.java
 * @version: 1.0 
 * @since:  10:26:59 8 Jan 2014
 */
@SuppressWarnings("serial")
public class GsnppRouteSupervisionDTO implements Serializable{
	public int totalList;
	public ArrayList<GsnppRouteSupervisionItem> itemList;

	public GsnppRouteSupervisionDTO(){
		itemList = new ArrayList<GsnppRouteSupervisionItem>();
	}
}
