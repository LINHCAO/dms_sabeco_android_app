/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;

import android.database.Cursor;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.util.StringUtil;
/**
 * Thông tin màn hình thêm mới Khách hàng
 * NewCustomerListDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:13:41 3 Jan 2014
 */
public class CreateCustomerInfoDTO implements Serializable {
	private static final long serialVersionUID = -4309260884881517698L;
	public ArrayList<AreaItem> listProvine;
	public ArrayList<AreaItem> listDistrict;
	public ArrayList<AreaItem> listPrecinct;
	public CustomerListDTO listC2;	
	public ArrayList<CustomerType> listCusType;
	public CustomerDTO cusInfo;
	public long curentIdProvine;
	public long curentIdDistrict;
	public long curentIdPrecinct;
	public long curentIdC2;
	public long curentIdType;
	

	public CreateCustomerInfoDTO() {
		 curentIdProvine = 0;
		 curentIdDistrict = 0;
		 curentIdPrecinct = 0;
		 curentIdC2 = 0;
		 curentIdType = 0;
		 
		 currentIndexC2 = -1;
		 currentIndexDistrict = -1;
		 currentIndexPrecinct = -1;
		 currentIndexProvince = -1;
		 currentIndexType = -1;
	}
	
	public int currentIndexProvince;
	public void setCurrentProvince(int index){
		int size = listProvine.size();
		if (index >= 0 && index < size) {
			currentIndexProvince = index;
			curentIdProvine = listProvine.get(index).areaId;
		}else{
			currentIndexProvince = index;
			curentIdProvine = 0;
		}
	}
	
	public int currentIndexDistrict;
	public void setCurrentDistrict(int index){
		int size = listDistrict.size();
		if (index >= 0 && index < size) {
			currentIndexDistrict = index;
			curentIdDistrict = listDistrict.get(index).areaId;
		}else{
			currentIndexDistrict = -1;
			curentIdDistrict = 0;
		}
    }
	
	public int currentIndexPrecinct;
	public void setCurrentPrecinct(int index){
		int size = listPrecinct.size();
		if (index >= 0 && index < size) {
			currentIndexPrecinct = index;
			curentIdPrecinct = listPrecinct.get(index).areaId;
		} else {
			currentIndexPrecinct = -1;
			curentIdPrecinct = 0;
		}
    }
    
	public int currentIndexType;
	public void setCurrentType(int index, long typeId){
		if (index >= 0 ) {
			currentIndexType = index;
			curentIdType = typeId;
		} else {
			currentIndexType = -1;
			curentIdType = 0;
		}
    }
	
	public int currentIndexC2;
	public void setCurrentC2(int index){
		int size = listC2.cusList.size();
		if (index >= 0 && index < size) {
			currentIndexC2 = index;
			curentIdC2 = listC2.cusList.get(index).aCustomer.customerId;
		}else{
			currentIndexC2 = -1;
			curentIdC2 = 0;
		}
    }
	
	/**
	 * Thông tin tỉnh, quận/huyện, phường/xã
	 * CreateCustomerInfoDTO.java
	 * @author: duongdt3
	 * @version: 1.0 
	 * @since:  09:32:02 6 Jan 2014
	 */
	public static class AreaItem{
		public String areaName;
		public long areaId;
		public long parrentId;
		
		public AreaItem() {
			areaName = "";
			areaId = 0;
			parrentId = 0;
		}

		/**
		 * @author: duongdt3
		 * @since: 11:55:13 6 Jan 2014
		 * @return: void
		 * @throws:  
		 * @param c
		 */
		public void initFromCursor(Cursor c) {
			areaId = StringUtil.getLongFromSQliteCursor(c, "AREA_ID");
			areaName = StringUtil.getStringFromSQliteCursor(c, "AREA_NAME");
			parrentId = StringUtil.getLongFromSQliteCursor(c, "PARENT_AREA_ID");
		}
	}
	
	public static class CustomerType {
		public String typeName;
		public long typeId;
		public int objectType;

		public CustomerType() {
			typeName = "";
			typeId = 0;
			objectType = 0;
		}

		/**
		 * init type customer from cursor
		 * @author: duongdt3
		 * @since: 10:19:10 6 Jan 2014
		 * @return: void
		 * @throws:  
		 * @param c
		 */
		public void initFromCursor(Cursor c) {
			this.typeId = StringUtil.getLongFromSQliteCursor(c, "CHANNEL_TYPE_ID");
			this.typeName = StringUtil.getStringFromSQliteCursor(c, "CHANNEL_TYPE_NAME");
			this.objectType = (int)StringUtil.getLongFromSQliteCursor(c, "OBJECT_TYPE");
		}
	}
}
