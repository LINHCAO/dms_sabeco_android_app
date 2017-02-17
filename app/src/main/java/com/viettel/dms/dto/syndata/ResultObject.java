/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.syndata;

import com.fasterxml.jackson.annotation.JsonProperty;

/** 
 * Result Object class
 * ResultObject.java
 * @version: 1.0 
 * @since:  10:20:12 8 Jan 2014
 */
public class ResultObject {
	@JsonProperty("result")
	private ResponObject result;

	public ResponObject getResult() {
		return result;
	}

	public void setResult(ResponObject result) {
		this.result = result;
	}

}
