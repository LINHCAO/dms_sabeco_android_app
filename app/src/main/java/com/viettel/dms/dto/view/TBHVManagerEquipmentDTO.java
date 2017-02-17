/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.util.ArrayList;

import android.database.Cursor;

/**
 * 
 *  Chua du lieu man hinh quan ly thiet bi cua TBHV
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVManagerEquipmentDTO {
	public int totalList;	
	public String totalofTotal;
	
	public ArrayList<TBHVManagerEquipmentItem> arrList;
	
	public TBHVManagerEquipmentDTO(){
		arrList = new ArrayList<TBHVManagerEquipmentDTO.TBHVManagerEquipmentItem>();		
	}
	
	public void addItem(Cursor c){
		TBHVManagerEquipmentItem item = new TBHVManagerEquipmentItem();	
		item.id = c.getString(c.getColumnIndex("id"));
		item.maNVBH = c.getString(c.getColumnIndex("maNVBH"));
		item.nvbh = c.getString(c.getColumnIndex("nvbh"));
		item.soThietBi = c.getInt(c.getColumnIndex("soThietBi"));
		item.khongDat = c.getInt(c.getColumnIndex("khongDat"));		
		arrList.add(item);
	}
	
	public class TBHVManagerEquipmentItem{
		public String id;
		public String maNVBH;
		public String nvbh;	
		public int soThietBi;
		public int khongDat;		
		
		public TBHVManagerEquipmentItem(){
			id = "1";
			maNVBH = "maNVBH";
			nvbh = "maNVBH";
			soThietBi = 1;
			khongDat = 1;					
		}
	}
}
