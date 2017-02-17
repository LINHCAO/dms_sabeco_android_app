/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.syndata;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Respon Object class
 * ResponObject.java
 * @version: 1.0 
 * @since:  10:19:48 8 Jan 2014
 */
public class ResponObject {
	@JsonProperty("response")
	private SynDataDTO response;
	
	@JsonProperty("errorCode")
	private Integer errorCode;
	
	@JsonProperty("errorMessage")
	private String errorMessage;

	public SynDataDTO getResponse() {
		return response;
	}

	public void setSynDataDTO(SynDataDTO response) {
		this.response = response;
	}

	public Integer getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(Integer errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
