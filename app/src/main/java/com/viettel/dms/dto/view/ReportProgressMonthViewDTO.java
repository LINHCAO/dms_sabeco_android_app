/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *  report progress month info view dto
 *  @author: HaiTC3
 *  @version: 1.1
 *  @since: 1.0
 */
public class ReportProgressMonthViewDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	// number day sale plan
	public int numDaySalePlan;
	// number day sold plan
	public int numDaySoldPlan;
	// progress sale
	public int progessSold;
	// list report progess month dto
	public List<ReportProgressMonthCellDTO> listReportProgessMonthDTO = new ArrayList<ReportProgressMonthCellDTO>();
	// total report progess month object
	public ReportProgressMonthCellDTO totalReportObject = new ReportProgressMonthCellDTO();
	
	public ReportProgressMonthViewDTO(){
		numDaySalePlan = 0;
		numDaySoldPlan = 0;
		progessSold = 0;
		listReportProgessMonthDTO = new ArrayList<ReportProgressMonthCellDTO>();
		totalReportObject = new ReportProgressMonthCellDTO();
	}
	
	

}
