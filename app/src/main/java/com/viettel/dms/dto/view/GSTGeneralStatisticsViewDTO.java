/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;

/** 
 * GST General Statistics View DTO class
 * GSTGeneralStatisticsViewDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:29:06 8 Jan 2014
 */
public class GSTGeneralStatisticsViewDTO implements Serializable {

	private static final long serialVersionUID = -257713147532499248L;

	//info numberDayPlan, numberDaySold, progressSold
	public ProgressSoldViewDTO progressSoldInfo; 
	public List<GeneralStatisticsInfo> listTNPGInfo;
	public List<GeneralStatisticsInfo> listGsbhInfo;
	
	public GSTGeneralStatisticsViewDTO(){
		progressSoldInfo = new ProgressSoldViewDTO();
		listGsbhInfo = new ArrayList<GeneralStatisticsInfo>();
		listTNPGInfo = new ArrayList<GeneralStatisticsInfo>();
	}
	
}
