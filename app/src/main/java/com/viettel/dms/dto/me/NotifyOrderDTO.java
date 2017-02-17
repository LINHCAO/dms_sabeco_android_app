/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.me;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.LogDTO;

/**
 *  DTO luu tru thogn tin don hang trong log, va so don hang tra ve tu NPP
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class NotifyOrderDTO implements Serializable {
	// ds don hang co trong log_table
	public ArrayList<LogDTO> listOrderInLog = new ArrayList<LogDTO>();
	// so luong don hang trong ngay tra ve tu NPP
	public int numOrderReturnNPP;
}
