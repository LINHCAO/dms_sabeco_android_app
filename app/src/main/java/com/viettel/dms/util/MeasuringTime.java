/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

/**
 * Do thoi gian 
 * @author: TruongHN3
 * @version: 1.0
 * @since: 1.0
 */
public class MeasuringTime {
	// thoi diem bat dau tinh thoi gian render
	private static long startTimeRender;
	// thoi diem can tinh thoi gian render
	private static long endTimeRender;
	// thoi diem bat dau tinh thoi gian parser
	private static long startTimeParser;
	// thoi diem can tinh thoi gian parser
	private static long endTimeParser;
	
	/**
	*  Lay thoi gian bat dau tinh render
	*  @author: TruongHN3
	*  @return: long
	*  @throws:
	 */
	public static long getStartTimeRender(){
		startTimeRender = System.currentTimeMillis();
		return startTimeRender;
	}
	
	/**
	*  Lay thoi gian render den thoi diem hien tai
	*  @author: TruongHN3
	*  @return: long
	*  @throws:
	 */
	public static long getTimeRender(){
		return endTimeRender - startTimeRender;
	}
	
	public static void getEndTimeRender(){
		endTimeRender = System.currentTimeMillis();
	}
	
	/**
	 *  Lay thoi gian bat dau tinh parser
	 *  @author: TruongHN3
	 *  @return: long
	 *  @throws:
	 */
	public static long getStartTimeParser(){
		startTimeParser = System.currentTimeMillis();
		return startTimeParser;
	}
	
	/**
	 *  Lay thoi gian parser den thoi diem hien tai
	 *  @author: TruongHN3
	 *  @return: long
	 *  @throws:
	 */
	public static long getTimeParser(){
		return endTimeParser - startTimeParser;
	}
	
	
	public static void getEndTimeParser(){
		endTimeParser = System.currentTimeMillis();
	}
}
