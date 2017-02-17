/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;


/**
 * 
 *  Lop chua thong tin cua 1 chuong ho tro thuong mai
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVBusinessSupportPrograme {
	private String name;
	private int count;
	private int action;
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the count
	 */
	public int getCount() {
		return count;
	}
	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}
	/**
	 * @return the action
	 */
	public int getAction() {
		return action;
	}
	/**
	 * @param action the action to set
	 */
	public void setAction(int action) {
		this.action = action;
	}
}
