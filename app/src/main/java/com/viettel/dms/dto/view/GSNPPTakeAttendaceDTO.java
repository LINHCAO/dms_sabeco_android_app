/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.map.dto.LatLng;

/**
 *  Thong tin ds cham cong cua GSNPP
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */

public class GSNPPTakeAttendaceDTO {
	public List<ShopParamDTO> listInfo;
	public List<AttendanceDTO> listStaff;
	public List<ShopSupervisorDTO> listCombox;
	public LatLng shopPosition;
	
	/**
	 * 
	 */
	public GSNPPTakeAttendaceDTO() {
		listInfo = new ArrayList<ShopParamDTO>();
		listStaff = new ArrayList<AttendanceDTO>();
		listCombox = new ArrayList<ShopSupervisorDTO>();
		shopPosition = new LatLng();
	}
}
