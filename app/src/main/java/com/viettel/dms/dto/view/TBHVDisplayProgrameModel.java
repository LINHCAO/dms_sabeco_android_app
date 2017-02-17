/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.List;

import com.viettel.dms.dto.db.DisplayProgrameDTO;

/**
 * 
 *  Ds chuong trinh trung bay cua TBHV
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */

public class TBHVDisplayProgrameModel {
	List<DisplayProgrameDTO> modelData;
	ComboboxDisplayProgrameDTO comboboxDTO;
	/**
	 * @return the comboboxDTO
	 */
	public ComboboxDisplayProgrameDTO getComboboxDTO() {
		return comboboxDTO;
	}
	/**
	 * @param comboboxDTO the comboboxDTO to set
	 */
	public void setComboboxDTO(ComboboxDisplayProgrameDTO comboboxDTO) {
		this.comboboxDTO = comboboxDTO;
	}
	int total;
	/**
	 * @return the modelData
	 */
	public List<DisplayProgrameDTO> getModelData() {
		return modelData;
	}
	/**
	 * @param modelData the modelData to set
	 */
	public void setModelData(List<DisplayProgrameDTO> modelData) {
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
