/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.db.ProInfoDTO;

/**
 * Du lieu MH DS CT HTBH SaleSupportProgrameModel.java
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 19:46:30 07-05-2014
 */
public class SaleSupportProgramModel {
	public List<ProInfoDTO> listPrograme = new ArrayList<ProInfoDTO>();
	public int total;
}
