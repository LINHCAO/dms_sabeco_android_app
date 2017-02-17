/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.db;

import java.util.ArrayList;

/**
 * ProCusAttendDTO.java
 * @author: hoanpd1
 * @version: 1.0 
 * @since:  18:28:11 20-05-2014
 */
public class ProCusAttendDTO {
	public ArrayList<ProCusMapDTO> lisCusMapDto;
	public ArrayList<ProCusHistoryDTO> listCusHisDto;
	
	public ProCusAttendDTO(){
		lisCusMapDto = new ArrayList<ProCusMapDTO>();
		listCusHisDto = new ArrayList<ProCusHistoryDTO>();
	}
}
