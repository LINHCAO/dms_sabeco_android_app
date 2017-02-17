/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;

/**
 *  Thong tin cham khong nhan vien
 *  @author: Nguyen Thanh Dung
 *  @version: 1.0
 *  @since: 1.0
 */
public class PGTakeAttendanceDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String staffId;
	public String staffCode;
	public String staffName;
	public String timeOnWork;
	public boolean isOnWork;
	public boolean isDress;
	public boolean isFollowRule;
	public int hourOfDay;
	public int minute;
	public long id;
	public String createDate;
	public String createUser;
	
	public PGTakeAttendanceDTO(){
		staffId = "";
		staffCode = "";
		staffName="";
		timeOnWork="";
	}
	
}
