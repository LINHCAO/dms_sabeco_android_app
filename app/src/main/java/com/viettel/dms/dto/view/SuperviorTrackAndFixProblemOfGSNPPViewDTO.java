/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * list track and fix problems of gsnpp
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class SuperviorTrackAndFixProblemOfGSNPPViewDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// list problem of gsnpp
	public ArrayList<SupervisorProblemOfGSNPPDTO> listProblemsOfGSNPP = new ArrayList<SupervisorProblemOfGSNPPDTO>();
	// total item problem in db
	public int totalItem = 0;

	public SuperviorTrackAndFixProblemOfGSNPPViewDTO() {
		listProblemsOfGSNPP = new ArrayList<SupervisorProblemOfGSNPPDTO>();
		totalItem = 0;
	}

}
