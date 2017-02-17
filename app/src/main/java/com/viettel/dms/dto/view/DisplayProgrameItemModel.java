/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.List;

/**
 * Model du lieu cho trang chuong trinh khuyen mai
 * 
 * @author: SoaN
 * @version: 1.0
 * @since: Jun 19, 2012
 */

public class DisplayProgrameItemModel {
	List<DisplayProgrameItemViewDTO> modelData;
	int total;
	/**
	 * @return the modelData
	 */
	public List<DisplayProgrameItemViewDTO> getModelData() {
		return modelData;
	}
	/**
	 * @param modelData the modelData to set
	 */
	public void setModelData(List<DisplayProgrameItemViewDTO> modelData) {
		this.modelData = modelData;
	}
	/**
	 * @return the total
	 */
	public int getTotal() {
		return total;
	}
	/**
	 * @param total the total to set
	 */
	public void setTotal(int total) {
		this.total = total;
	}
	
}
