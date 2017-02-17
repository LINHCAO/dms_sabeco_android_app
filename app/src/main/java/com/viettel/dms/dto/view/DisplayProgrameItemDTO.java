/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.dto.view;

import java.io.Serializable;

/**
 * 
 * DTO chuong trinh
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class DisplayProgrameItemDTO implements Serializable{
	// trang thai 
	public static final String VALUE_PROBLEM_CREATE_NEW = "1";// tao moi
	public static final String VALUE_NVBH_IMPLEMENTED = "2";// da thuc hien
	public static final String VALUE_FEEDBACK_TYPE_TBHV_DONE = "3";// da duyet
	public static final String TYPE_GSNPP= "5";// gsnpp
	public String id;// id
	public String value;// value
	public String name;// ten hien thi
	public String type;// loai van de
}
