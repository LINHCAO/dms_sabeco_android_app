/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/** 
 * Manager Equipment DTO
 * ManagerEquipmentDTO.java
 * @version: 1.0 
 * @since:  10:34:44 8 Jan 2014
 */
public class ManagerEquipmentDTO {
	public int totalList;	
	public String totalofTotal;
	
	public ArrayList<ManagerEquipmentItem> arrList;
	
	public ManagerEquipmentDTO(){
		arrList = new ArrayList<ManagerEquipmentDTO.ManagerEquipmentItem>();		
	}
	
	public void addItem(Cursor c){
		ManagerEquipmentItem item = new ManagerEquipmentItem();	
		item.id = c.getString(c.getColumnIndex("id"));
		item.maNVBH = c.getString(c.getColumnIndex("maNVBH"));
		item.nvbh = c.getString(c.getColumnIndex("nvbh"));
		item.soThietBi = c.getInt(c.getColumnIndex("soThietBi"));
		item.khongDat = item.soThietBi - c.getInt(c.getColumnIndex("dat"));		
		arrList.add(item);
	}
	
	public class ManagerEquipmentItem{
		public String id;
		public String maNVBH;
		public String nvbh;	
		public int soThietBi;
		public int khongDat;		
		
		public ManagerEquipmentItem(){
			id = "1";
			maNVBH = "maNVBH";
			nvbh = "maNVBH";
			soThietBi = 1;
			khongDat = 1;					
		}
	}
}
