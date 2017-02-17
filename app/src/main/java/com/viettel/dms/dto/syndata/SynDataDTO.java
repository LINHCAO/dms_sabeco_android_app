/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.syndata;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Syn Data DTO class
 * SynDataDTO.java
 * @version: 1.0 
 * @since:  10:20:29 8 Jan 2014
 */
public class SynDataDTO {
	public final static String UPDATE_TO_DATE = "UPDATE_TO_DATE";
	public final static String CONTINUE = "CONTINUE";
	public final static String RESET = "RESET";
	//dong bo bang cach parse  json
	public final static int TYPE_JSON = 1;
	//dong bo bang cach download file
	public final static int TYPE_FILE = 2;
	
	@JsonProperty("state")
	private String state;
	
	//lastLogId update
	@JsonProperty("lastLogId_update")
	private long lastLogId_update;
	@JsonProperty("maxDBLogId")
	private long maxDBLogId;
	
	@JsonProperty("rowData")
	private List<SynDataTableDTO> rowData = new ArrayList<SynDataTableDTO>();

	public SynDataDTO() {
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public long getLastLogId_update() {
		return lastLogId_update;
	}

	public void setLastLogId_update(long lastLogId_update) {
		this.lastLogId_update = lastLogId_update;
	}

	public List<SynDataTableDTO> getRowData() {
		return rowData;
	}

	public void setRowData(List<SynDataTableDTO> rowData) {
		this.rowData = rowData;
	}

	public long getMaxDBLogId() {
		return maxDBLogId;
	}

	public void setMaxDBLogId(long maxDBLogId) {
		this.maxDBLogId = maxDBLogId;
	}
}
