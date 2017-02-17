/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.List;

import com.viettel.dms.dto.db.PromotionProgrameDTO;

/**
 * 
 *  Mo ta muc dich cua lop (interface)
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVPromotionProgrameDTO {
	List<PromotionProgrameDTO> modelData;
	int total;
	/**
	 * @return the modelData
	 */
	public List<PromotionProgrameDTO> getModelData() {
		return modelData;
	}
	/**
	 * @param modelData the modelData to set
	 */
	public void setModelData(List<PromotionProgrameDTO> modelData) {
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
