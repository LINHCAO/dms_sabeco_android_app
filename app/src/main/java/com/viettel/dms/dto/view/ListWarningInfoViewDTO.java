/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;
import java.util.List;

/**
 *  list warning info view
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class ListWarningInfoViewDTO {
	private int numberWarningInfo;
	private List<WarningInfoDTO> listWarning = new ArrayList<WarningInfoDTO>();
	
	public ListWarningInfoViewDTO(){
		setNumberWarningInfo(0);
		setListWarning(new ArrayList<WarningInfoDTO>());
	}

	/**
	 * @return the numberWarningInfo
	 */
	public int getNumberWarningInfo() {
		return numberWarningInfo;
	}

	/**
	 * @param numberWarningInfo the numberWarningInfo to set
	 */
	public void setNumberWarningInfo(int numberWarningInfo) {
		this.numberWarningInfo = numberWarningInfo;
	}

	/**
	 * @return the listWarning
	 */
	public List<WarningInfoDTO> getListWarning() {
		return listWarning;
	}

	/**
	 * @param listWarning the listWarning to set
	 */
	public void setListWarning(List<WarningInfoDTO> listWarning) {
		this.listWarning = listWarning;
	}
}
