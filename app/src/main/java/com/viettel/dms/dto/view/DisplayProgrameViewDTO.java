/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.List;

import com.viettel.dms.dto.db.DisplayProgrameLvDTO;

/**
 *  Thong tin hien thi chuong trinh khuyen mai
 *  @author: SoaN
 *  @version: 1.0
 *  @since: Jun 21, 2012
 */

public class DisplayProgrameViewDTO {
	private String name;
	private String code;
	private String startDate;
	private String endDate;
	List<DisplayProgrameLvDTO> displayProgLevel;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}
	/**
	 * @return the startDate
	 */
	public String getStartDate() {
		return startDate;
	}
	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * @return the endDate
	 */
	public String getEndDate() {
		return endDate;
	}
	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * @return the displayProgLevel
	 */
	public List<DisplayProgrameLvDTO> getDisplayProgLevel() {
		return displayProgLevel;
	}
	/**
	 * @param displayProgLevel the displayProgLevel to set
	 */
	public void setDisplayProgLevel(List<DisplayProgrameLvDTO> displayProgLevel) {
		this.displayProgLevel = displayProgLevel;
	}
	
	
}
