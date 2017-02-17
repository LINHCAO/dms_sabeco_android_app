/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

/**
 * @author: AnhND
 * @since : May 18, 2011
 * 
 */

public class LogMsg {
	private String errName = "";
	private StringBuffer description = new StringBuffer();

	public LogMsg() {
	}

	public LogMsg(String name) {
		this.errName = name;
	}

	/**
	 * 
	 * set name of log message
	 * 
	 * @author: AnhND
	 * @since: May 18, 2011
	 * @Param: @param name
	 * @Return: void
	 * @Modified by:
	 * @Modified date:
	 * @Description:
	 */
	public void setName(String name) {
		this.errName = name;
	}

	/**
	 * 
	 * get name of log message
	 * @author: AnhND
	 * @since: May 18, 2011
	 * @Param: @return
	 * @Return: String
	 * @Modified by:
	 * @Modified date: 
	 * @Description:
	 */
	public String getName(){
		return this.errName;
	}
	
	/**
	 * 
	 * get des of log message
	 * @author: AnhND
	 * @since: May 18, 2011
	 * @Param: @return
	 * @Return: String
	 * @Modified by:
	 * @Modified date: 
	 * @Description:
	 */
	public String getDes() {
		return this.description.toString();
	}
	
	/**
	 * 
	 * clear des
	 * 
	 * @author: AnhND
	 * @since: May 18, 2011
	 * @Param:
	 * @Return: void
	 * @Modified by:
	 * @Modified date:
	 * @Description:
	 */
	public void resetDes() {
		description.setLength(0);
	}

	/**
	 * 
	 * append des
	 * 
	 * @author: AnhND
	 * @since: May 18, 2011
	 * @Param: @param des
	 * @Return: void
	 * @Modified by:
	 * @Modified date:
	 * @Description:
	 */
	public void appendDes(String des) {
		description.append(des);
	}
}
