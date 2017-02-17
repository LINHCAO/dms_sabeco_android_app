/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.syndata;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viettel.utils.VTLog;

/** 
 * Syn Data Table DTO class
 * SynDataTableDTO.java
 * @version: 1.0 
 * @since:  10:20:51 8 Jan 2014
 */
public class SynDataTableDTO {
	@JsonProperty("tableName")
	private String tableName;
	
	@JsonProperty("pkField")
	private String pkField;
	
	@JsonProperty("tableColumns")
	private String[] tableColumns;
	
	@JsonProperty("data")
	private List<List<String>> data = new ArrayList<List<String>>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPkField() {
		return pkField;
	}

	public void setPkField(String pkField) {
		this.pkField = pkField;
	}

	public String[] getTableColumns() {
		return tableColumns;
	}

	public void setTableColumns(String[] tableColumns) {
		this.tableColumns = tableColumns;
	}

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}

	public void printLog() {
		VTLog.d("PhucNT4", "SynDataTableDTO -- table +" + tableName
				+ "--query +" + pkField);
		for (int i = 0; i < data.size(); i++) {
			VTLog.d("PhucNT4", "SynDataTableDTO -- dataRow + " + i);

		}
	}

}
