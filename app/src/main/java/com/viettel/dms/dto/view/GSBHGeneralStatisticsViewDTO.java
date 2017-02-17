/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;

import com.viettel.dms.util.StringUtil;
/**
 * Chua thong tin cho report ngay & luy ke thang GSBH
 * GSBHGeneralStatisticsViewDTO.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  16:47:02 29 Nov 2013
 */
public class GSBHGeneralStatisticsViewDTO extends GeneralStatisticsViewDTO implements Serializable {
	private static final long serialVersionUID = 7209637694454732500L;
	
	//dung cho truong hop GST xem thong tin chi tiet cua GSBH
	public List<StaffsOfShopInfo> listStaffInShop;
	
	public GSBHGeneralStatisticsViewDTO(){
		super();	
		listStaffInShop = new ArrayList<StaffsOfShopInfo>();
	}	
	
	/**
	 * Chua thong tin 1 shop + danh sach cac staff GSBH trong shop do
	 * GSBHGeneralStatisticsViewDTO.java
	 * @author: duongdt3
	 * @version: 1.0 
	 * @since:  16:49:17 29 Nov 2013
	 */
	public static class StaffsOfShopInfo implements Serializable {
		private static final long serialVersionUID = 303811789390111230L;
		//info of SHOP
		ShopItemDTO shop;
		List<StaffItemDTO> listStaff;
		public StaffsOfShopInfo(){
			shop = new ShopItemDTO();
			listStaff = new ArrayList<StaffItemDTO>();
		}
		
		public void initCursor(Cursor c){
			
			//Shop info 
			 this.shop.initCursor(c);
			 
			 //staff of shop
			 StaffItemDTO staffInfo = new StaffItemDTO();
			 staffInfo.initCursor(c);
			 //set shop of staff
			 staffInfo.shopId = this.shop.shopId;
			 
			 //add to list 
			 this.listStaff.add(staffInfo);
		}
		
		public static void addItemToList(List<StaffsOfShopInfo> listInfo, StaffsOfShopInfo info){
			StaffsOfShopInfo infoExists = null;
			for (StaffsOfShopInfo staffsOfShopInfo : listInfo) {
				if(staffsOfShopInfo.shop.equals(info.shop)){
					infoExists = staffsOfShopInfo;
					break;
				}
			}
			
			//neu da co shop nay, thi them staff vao listStaff
			if (infoExists != null) {
				//set lai shop cho staff
				for (StaffItemDTO staffInfo : info.listStaff) {
					staffInfo.shopId = infoExists.shop.shopId;
				}
				
				infoExists.listStaff.addAll(info.listStaff);
			}else{
				//chua co thi them luon shop vao
				listInfo.add(info);
			}
		}

		public static List<ShopItemDTO> getShopArray(List<StaffsOfShopInfo> listStaffInShopInfo) {
			return getShopArray(listStaffInShopInfo, "");
		}
		
		public static List<ShopItemDTO> getShopArray(List<StaffsOfShopInfo> listStaffInShopInfo, String staffId) {
			List<ShopItemDTO> listShop = new ArrayList<ShopItemDTO>();
			
			//neu chon tat ca se hien tat ca shop
			if (StringUtil.isNullOrEmpty(staffId)) {
				//lay danh sach tat ca shop
				for (StaffsOfShopInfo staffsOfShopInfo : listStaffInShopInfo) {		
					listShop.add(staffsOfShopInfo.shop); 
				}
			}else{
				//tim danh sach shop co chua staff
				for (StaffsOfShopInfo staffsOfShopInfo : listStaffInShopInfo) {
					boolean isExists = false;
					for (StaffItemDTO gsbhHienTai : staffsOfShopInfo.listStaff) {
						if (gsbhHienTai.staffId.equals(staffId)) {
							isExists = true;
							break;
						}
					}
					//neu Shop nay co chua Staff thi them vao mang
					if (isExists) {
						listShop.add(staffsOfShopInfo.shop);	
					}
				}
			}
			
			//add All to info shops
			if (listShop.size() > 0) {
				ShopItemDTO info = new ShopItemDTO();
				info.shopName = "Tất cả";
				listShop.add(0, info);
			}	
			
			return listShop;
		}
		
		public static List<StaffItemDTO> getStaffArray(List<StaffsOfShopInfo> listStaffInShopInfo, String shopId) {
			List<StaffItemDTO> listStaff = new ArrayList<StaffItemDTO>();
			
			StaffsOfShopInfo infoExists = null;
			//kiem tra null shopId => neu la nut tat ca, cung ko xet mang, ma lay tat ca staff
			if (!StringUtil.isNullOrEmpty(shopId)) {
				for (StaffsOfShopInfo staffsOfShopInfo : listStaffInShopInfo) {
					if(staffsOfShopInfo.shop.shopId.equals(shopId)){
						infoExists = staffsOfShopInfo;
						break;
					}
				}
			}
			
			//co ton tai 1 shop 
			if (infoExists != null) {
				//lay danh sach staff cua shop nay
				listStaff.addAll(infoExists.listStaff);
			}else{
				//lay danh sach staff tat ca shop
				for (StaffsOfShopInfo staffsOfShopInfo : listStaffInShopInfo) {					
					for (StaffItemDTO gsbhHienTai : staffsOfShopInfo.listStaff) {
						boolean isExists = false; 
						//tranh truong hop trung GSBH (vi 1 GSBH co the QL NPP) + (1 NPP co the duoc quan ly boi nhieu GSBH)
						for (StaffItemDTO staff : listStaff) {
							if(gsbhHienTai.staffId.compareTo(staff.staffId) == 0){
								isExists = true;
								break;
							}
						}
						//neu chua co trong mang staff thi them vao
						if (!isExists) {
							listStaff.add(gsbhHienTai);
						}
					}
				}
			}
			
			//add All to info staffs
			if (listStaff.size() > 0) {
				StaffItemDTO info = new StaffItemDTO();
				info.staffName = "Tất cả";
				listStaff.add(0, info);
			}	
			
			return listStaff;
		}
	}
	
	/**
	 * Thong tin 1 shop
	 * GSBHGeneralStatisticsViewDTO.java
	 * @author: duongdt3
	 * @version: 1.0 
	 * @since:  16:49:40 29 Nov 2013
	 */
	public static class ShopItemDTO implements Serializable {
		private static final long serialVersionUID = 3407665551084883350L;
		public String shopId;
		public String shopName;
		public String shopCode;
				
		public ShopItemDTO() {
			shopId = "";
			shopName = "";
			shopCode = "";
		}
		public void initCursor(Cursor c){
			//shop info
			this.shopId = String.valueOf(StringUtil.getLongFromSQliteCursor(c, "SHOP_ID"));
			this.shopCode = StringUtil.getStringFromSQliteCursor(c, "SHOP_CODE");
			this.shopName = StringUtil.getStringFromSQliteCursor(c, "SHOP_NAME");
		}
		
		@Override
		public boolean equals(Object o) {
			boolean result = false;
			if (o instanceof ShopItemDTO) {
				ShopItemDTO info = (ShopItemDTO) o;
				//neu giong shopid => == nhau
				if (info.shopId.compareTo(this.shopId) == 0) {
					result = true;
				}
			}
			return result;
		}
		
		@Override
		public String toString() {
			String result = !StringUtil.isNullOrEmpty(shopCode) ? (!StringUtil.
					isNullOrEmpty(shopName) ? shopCode + " - " + shopName: shopCode) : shopName; 
			return result;
		}
	}
	
	/**
	 * Thong tin 1 staff
	 * GSBHGeneralStatisticsViewDTO.java
	 * @author: duongdt3
	 * @version: 1.0 
	 * @since:  16:49:48 29 Nov 2013
	 */
	public static class StaffItemDTO implements Serializable {
		private static final long serialVersionUID = -2630585134060299226L;
		public String staffId;
		public String staffCode;
		public String staffName;
		
		public String shopId;
		public StaffItemDTO() {
			staffId = "";
			staffCode = "";
			staffName = "";
			shopId = "";
		}
		
		public void initCursor(Cursor c){
			this.staffId = String.valueOf(StringUtil.getLongFromSQliteCursor(c, "STAFF_ID"));
			this.staffCode = StringUtil.getStringFromSQliteCursor(c, "STAFF_CODE");
			this.staffName = StringUtil.getStringFromSQliteCursor(c, "STAFF_NAME");
		}
		
		@Override
		public String toString() {
			String result = !StringUtil.isNullOrEmpty(staffCode) ? (!StringUtil.isNullOrEmpty(staffName) ? staffCode + " - " + staffName: staffCode) : staffName; 
			return result;
		}
	}
}
