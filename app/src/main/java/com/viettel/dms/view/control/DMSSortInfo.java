/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.control;

import java.io.Serializable;

/**
 * DMSSortInfo.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  09:34:12 26 Mar 2015
 */
public class DMSSortInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	public static final int ASC_TYPE = 1;
	public static final int DESC_TYPE = 2;
	public static final int NO_SORT_TYPE = 3;
	
	private int sortAction;
	private int sortType;
	
	public DMSSortInfo(int sortAction, int sortType) {
		this.sortAction = sortAction;
		this.sortType = sortType;
	}
	
	/**
	 * @param sortType the sortType to set
	 */
	protected void setSortType(int sortType) {
		this.sortType = sortType;
	}
	/**
	 * @return the sortType
	 */
	public int getSortType() {
		return sortType;
	}
	
	/**
	 * @return the sortAction
	 */
	public int getSortAction() {
		return sortAction;
	}
}
