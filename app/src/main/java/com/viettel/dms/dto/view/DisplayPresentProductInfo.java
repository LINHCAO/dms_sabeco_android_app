/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

 
/**
 * Thong tin chi tiet cua chuong trinh trung bay
 * - Chua danh sach san pham de cham trung bay
 * 
 * DisplayPresentProductInfo.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  14:05:56 09-05-2014
 */
public class DisplayPresentProductInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// ma chuong trinh
	public String displayProgramCode;
	// ten chuong trinh
	public String displayProgramName;
	//  muc (level)
	public String joinLevel; 
	// id chuong trinh
	public String displayProgrameID; 
	// status voted?
	public boolean isVoted;
	// danh sach san pham cua chuong trinh
	public List<VoteDisplayProductDTO> listProductDisplay = new ArrayList<VoteDisplayProductDTO>();
	
	public DisplayPresentProductInfo(){
		displayProgrameID 	= "";
		displayProgramCode 	= "";
		displayProgramName 	= "";
		joinLevel 			= "";
		isVoted 			= false; 
	}
	
	/**
	 * 
	*  init display present product info
	*  @author: HaiTC3
	*  @param c
	*  @return: void
	*  @throws:
	 */
	public void initDisplayPresentProductInfo(Cursor c){
		if (c.getColumnIndex("PRO_INFO_ID") >= 0) {
			displayProgrameID = c.getString(c.getColumnIndex("PRO_INFO_ID"));
		} else {
			displayProgrameID = "";
		} 
		
		if (c.getColumnIndex("PRO_INFO_CODE") >= 0) {
			displayProgramCode = c.getString(c.getColumnIndex("PRO_INFO_CODE"));
		} else {
			displayProgramCode = "";
		}

		if (c.getColumnIndex("PRO_INFO_NAME") >= 0) {
			displayProgramName = c.getString(c.getColumnIndex("PRO_INFO_NAME"));
		} else {
			displayProgramName = "";
		}

		if (c.getColumnIndex("LEVEL_NUMBER") >= 0) {
			joinLevel = c.getString(c.getColumnIndex("LEVEL_NUMBER"));
		} else {
			joinLevel = "";
		} 
	}
}
