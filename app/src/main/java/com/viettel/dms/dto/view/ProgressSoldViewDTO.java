/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

/** 
 * Progress Sold View DTO class
 * ProgressSoldViewDTO.java
 * @version: 1.0 
 * @since:  10:38:48 8 Jan 2014
 */
public class ProgressSoldViewDTO implements Serializable {

	private static final long serialVersionUID = 3741876226278390260L;
	
	public int numberDayPlan;
	// so ngay ban hang da qua
	public int numberDaySold;
	// tien do chuan
	public float progressSold;

	public ProgressSoldViewDTO() {
		numberDayPlan = 0;
		numberDaySold = 0;
		progressSold = 0;
	}
}
