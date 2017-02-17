/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import com.viettel.dms.dto.db.DisplayProgrameLvDTO;

/**
 * doi tuong chua tat ca cac level cho mot chuong trinh trung bay
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class DisplayProgramLevelForProgramDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// d/s display program level
	public ArrayList<DisplayProgrameLvDTO> listDisProLevel = new ArrayList<DisplayProgrameLvDTO>();

	// display program id
	public long displayProgramId = 0;

	// max number display program level for display program id
	public int maxDisProLevel = 0;

	public DisplayProgramLevelForProgramDTO() {
		listDisProLevel = new ArrayList<DisplayProgrameLvDTO>();
		displayProgramId = 0;
		maxDisProLevel = 0;
	}
}
