/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

/** 
 * Show Room Tool Dto class
 * ShowRoomToolDto.java
 * @version: 1.0 
 * @since:  10:41:00 8 Jan 2014
 */
public class ShowRoomToolDto {
	public String toolCode;
	public String toolName;
	public boolean isPassed;

	public ShowRoomToolDto() {
		toolCode = "A01";
		toolName = "Tu lanh";
		isPassed = false;
	}
}
