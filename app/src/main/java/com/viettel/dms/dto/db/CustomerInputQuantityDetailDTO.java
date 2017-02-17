/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;
  
/**
 * Thong tin nhap san luong ban cua khach hang
 * CustomerInputQuantity.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:15:42 13-06-2014
 */
@SuppressWarnings("serial")
public class CustomerInputQuantityDetailDTO extends AbstractTableDTO{
		// id 
		public long PRO_CUS_PROCESS_ID ; 
		// id chuong trinh
		public long PRO_INFO_ID ;  
		// id pro cus map
		public long PRO_CUS_MAP_ID ;  
		// id chu trinh
		public long PRO_CUS_PERIOD ;
		// id khach hang
		public long OBJECT_ID; 
		// ma khach hang
		public String CUSTOMER_CODE;
		//ten khach hang
		public String CUSTOMER_NAME;
		// Dia chi khach hang
		public String CUSTOMER_ADDRESS;
		// So nha cua khach hang
		public String CUSTOMER_HOUSENUMBER;
		// Dia chi duong cua Khach hang
		public String CUSTOMER_STREET;
		// Quan
		public String CUSTOMER_AREA; 
		// Muc tham gia
		public int JOIN_LEVEL;
		// Muc tham gia
		public String LEVEL_NAME;
		// San luong thuc hien
		public long OLDQUANTITY;
		// San luong thuc hien moi
		public long NEWQUANTITY;
		// Thoi gian ban dau chu ki
		public String PERIOD_FROM_DATE;
		// Thoi gian ket thuc chu ki
		public String PERIOD_TO_DATE;
		// Thoi gian ban dau tham gia
		public String JOIN_FROM_DATE;
		// Thoi gian ket thuc tham gia
		public String JOIN_TO_DATE;
		// Ma san pham
		public String PRODUCT_CODE;
		// id san pham
		public long PRODUCT_ID;
		// Ten san pham
		public String PRODUCT_NAME;
		// San luong mua tu NPP
		public long QUANTITY_SHOP;
		// San luong cap nhat tu SMS PG
		public long QUANTITY_PG;
		// Nguoi cap nhat
		public String UPDATE_USER; 
		// Nguoi cap nhat
		public boolean ALLOW_EDIT; 
		// id op_sale_volume
		public String opId;
		// Cho biet nguoi dung co nhap san luong tren dialog khong?
		public boolean isInputNewQuantity;
		
		/**
		 * 
		*  Khoi tao du lieu tu cursor
		*  @author: dungnt19
		*  @param c
		*  @return: void
		*  @throws:
		 */
		public void initFromCursor(Cursor c){
			if (c.getColumnIndex("PRO_CUS_PROCESS_ID") >= 0) {
				PRO_CUS_PROCESS_ID = c.getLong(c.getColumnIndex("PRO_CUS_PROCESS_ID"));
			} else {
				PRO_CUS_PROCESS_ID = 0;
			}
			
			if (c.getColumnIndex("PRO_INFO_ID") >= 0) {
				PRO_INFO_ID = c.getLong(c.getColumnIndex("PRO_INFO_ID"));
			} else {
				PRO_INFO_ID = 0;
			} 

			if (c.getColumnIndex("PRO_CUS_MAP_ID") >= 0) {
				PRO_CUS_MAP_ID = c.getLong(c.getColumnIndex("PRO_CUS_MAP_ID"));
			} else {
				PRO_CUS_MAP_ID = 0;
			} 

			if (c.getColumnIndex("PRO_PERIOD_ID") >= 0) {
				PRO_CUS_PERIOD = c.getLong(c.getColumnIndex("PRO_PERIOD_ID"));
			} else {
				PRO_CUS_PERIOD = 0;
			} 

			if (c.getColumnIndex("OBJECT_ID") >= 0) {
				OBJECT_ID = c.getLong(c.getColumnIndex("OBJECT_ID"));
			} else {
				OBJECT_ID = 0;
			} 
			 
			if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
				CUSTOMER_CODE = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
			} else {
				CUSTOMER_CODE = "";
			} 

			if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
				CUSTOMER_NAME = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
			} else {
				CUSTOMER_NAME = "";
			} 

			if (c.getColumnIndex("CUSTOMER_ADDRESS") >= 0) {
				CUSTOMER_ADDRESS = c.getString(c.getColumnIndex("CUSTOMER_ADDRESS"));
			} else {
				CUSTOMER_ADDRESS = "";
			} 

			if (c.getColumnIndex("JOIN_LEVEL") >= 0) {
				String level = c.getString(c.getColumnIndex("JOIN_LEVEL"));
				if(level != null && !"null".equals(level.toLowerCase())){
					JOIN_LEVEL = Integer.parseInt(level);
				} 
			} else {
				JOIN_LEVEL = 0;
			} 
			if (c.getColumnIndex("LEVEL_NAME") >= 0) {
				LEVEL_NAME = c.getString(c.getColumnIndex("LEVEL_NAME"));
			} else {
				LEVEL_NAME = "";
			} 
			if (c.getColumnIndex("QUANTITY") >= 0) {
				String quantity = c.getString(c.getColumnIndex("QUANTITY"));
				if(quantity != null && !"null".equals(quantity.toLowerCase())){
					OLDQUANTITY = Long.parseLong((quantity)); 
				}
			} else {
				OLDQUANTITY = 0;
			} 
			NEWQUANTITY = OLDQUANTITY;

			if (c.getColumnIndex("PERIOD_FROM_DATE") >= 0) {
				PERIOD_FROM_DATE = c.getString(c.getColumnIndex("PERIOD_FROM_DATE"));
			} else {
				PERIOD_FROM_DATE = "";
			} 
			
			if (c.getColumnIndex("PERIOD_TO_DATE") >= 0) {
				PERIOD_TO_DATE = c.getString(c.getColumnIndex("PERIOD_TO_DATE"));
			} else {
				PERIOD_TO_DATE = "";
			} 

			if (c.getColumnIndex("JOIN_FROM_DATE") >= 0) {
				JOIN_FROM_DATE = c.getString(c.getColumnIndex("JOIN_FROM_DATE"));
			} else {
				JOIN_FROM_DATE = "";
			} 

			if (c.getColumnIndex("JOIN_TO_DATE") >= 0) {
				JOIN_TO_DATE = c.getString(c.getColumnIndex("JOIN_TO_DATE"));
				if(JOIN_TO_DATE != null && !"null".equals(JOIN_TO_DATE.toLowerCase())){
					JOIN_TO_DATE = "";
				}
			} else {
				JOIN_TO_DATE = "";
			}  
			
			if (c.getColumnIndex("PRODUCT_CODE") >= 0) {
				PRODUCT_CODE = c.getString(c.getColumnIndex("PRODUCT_CODE"));
			} else {
				PRODUCT_CODE = "";
			}
			
			if (c.getColumnIndex("PRODUCT_ID") >= 0) {
				PRODUCT_ID = c.getLong(c.getColumnIndex("PRODUCT_ID"));
			} else {
				PRODUCT_ID = 0;
			} 
			
			if (c.getColumnIndex("PRODUCT_NAME") >= 0) {
				PRODUCT_NAME = c.getString(c.getColumnIndex("PRODUCT_NAME"));
			} else {
				PRODUCT_NAME = "";
			}
			
			if (c.getColumnIndex("QUANTITY_SHOP") >= 0) {
				String quantity = c.getString(c.getColumnIndex("QUANTITY_SHOP"));
				if(quantity != null && !"null".equals(quantity.toLowerCase())){
					QUANTITY_SHOP = Long.parseLong(quantity);
				}
			} else {
				QUANTITY_SHOP = 0;
			} 

			if (c.getColumnIndex("QUANTITY_PG") >= 0) {
				String quantity = c.getString(c.getColumnIndex("QUANTITY_PG"));
				if(quantity != null && !"null".equals(quantity.toLowerCase())){
					QUANTITY_PG = Long.parseLong(quantity);
				} 
			} else {
				QUANTITY_PG = 0;
			} 

			if (c.getColumnIndex("UPDATE_USER") >= 0) {
				UPDATE_USER = c.getString(c.getColumnIndex("UPDATE_USER"));
				if(UPDATE_USER == null || "null".equals(UPDATE_USER.toLowerCase())){
					UPDATE_USER = "";
				}
			} else {
				UPDATE_USER = "";
			}  

			if (c.getColumnIndex("ALLOW_EDIT") >= 0) {
				ALLOW_EDIT = Integer.parseInt(c.getString(c.getColumnIndex("ALLOW_EDIT"))) == 1; 
			} else {
				ALLOW_EDIT = false;
			}  
			
			if (c.getColumnIndex("STREET") > -1) {
				CUSTOMER_STREET = c.getString(c.getColumnIndex("STREET"));
				
				if(CUSTOMER_STREET == null || "null".equalsIgnoreCase(CUSTOMER_STREET)){
					CUSTOMER_STREET = "";
				}
			} 
			
			if (c.getColumnIndex("HOUSENUMBER") > -1) {
				CUSTOMER_HOUSENUMBER = c.getString(c.getColumnIndex("HOUSENUMBER"));
				
				if(CUSTOMER_HOUSENUMBER == null || "null".equalsIgnoreCase(CUSTOMER_HOUSENUMBER)){
					CUSTOMER_HOUSENUMBER = "";
				}
			}
			
			if (c.getColumnIndex("AREA_NAME") > -1) {
				CUSTOMER_AREA = c.getString(c.getColumnIndex("AREA_NAME"));
				
				if(CUSTOMER_AREA == null || "null".equalsIgnoreCase(CUSTOMER_AREA)){
					CUSTOMER_AREA = "";
				}
			}
			
			// Dia chi hoan chinh
			// Dia chi dang : housenumber + " " + street + ", " + area
			CUSTOMER_ADDRESS = "";
			if(!StringUtil.isNullOrEmpty(CUSTOMER_HOUSENUMBER)){
				CUSTOMER_ADDRESS += CUSTOMER_HOUSENUMBER;
			}
			
			if(!StringUtil.isNullOrEmpty(CUSTOMER_ADDRESS)){
				CUSTOMER_ADDRESS +=  " ";
			}
			
			if(!StringUtil.isNullOrEmpty(CUSTOMER_STREET)){
				CUSTOMER_ADDRESS += CUSTOMER_STREET;
			}
			
			if(!StringUtil.isNullOrEmpty(CUSTOMER_ADDRESS)){
				CUSTOMER_ADDRESS +=  ", ";
			}
			
			if(!StringUtil.isNullOrEmpty(CUSTOMER_AREA)){
				CUSTOMER_ADDRESS +=  CUSTOMER_AREA;
			}
		}

		/**
		 * Khoi tao du lieu lay cac san pham cung chu ki can cap nhat
		 * @author: quangvt1
		 * @since: 10:21:39 25-06-2014
		 * @return: void
		 * @throws:  
		 * @param c
		 */
		public void initFromCursorForListSamePreriod(Cursor c) {
			if (c.getColumnIndex("PRO_CUS_PROCESS_ID") >= 0) {
				PRO_CUS_PROCESS_ID = c.getLong(c.getColumnIndex("PRO_CUS_PROCESS_ID"));
			} else {
				PRO_CUS_PROCESS_ID = 0;
			}
			
			if (c.getColumnIndex("PRODUCT_ID") >= 0) {
				PRODUCT_ID = c.getLong(c.getColumnIndex("PRODUCT_ID"));
			} else {
				PRODUCT_ID = 0;
			} 
		}
} 