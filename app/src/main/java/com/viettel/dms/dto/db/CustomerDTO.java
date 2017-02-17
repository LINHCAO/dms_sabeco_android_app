/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import java.util.Date;

import android.database.Cursor;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_POSITION_LOG_TABLE;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * Luu thong tin khach hang
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerDTO extends AbstractTableDTO {
	private static final long serialVersionUID = -5225453952621913199L;
	// id khach hang
	public long customerId;
	// ma khach hang
	public String customerCode;
	// ma rut gon 3 ky tu
	public String shortCode;
	// ten khach hang
	public String customerName;
	//full address: dia chi day du
	public String address;
	// id NPP
	private String shopId;
	// truong nay chua dung
	private String country;
	// ma vung
	private String areaCode;
	// area id
	private int areaId;
	// duong
	public String street;
	// so nha
	public String houseNumber;
	// dien thoai
	public String phone;
	// ngay tao
	private String createDate;
	// ngay cap nhat
	private String updateDate;
	// truong nay chua dung
	private String postalCode;
	// nguoi lien he
	private String contactPerson;
	// truong nay chua dung
	private String taxCode;
	// di dong
	public String mobilePhone;
	// khu vuc 1: thanh thi, 2: nong thon
	private int location;
	// loai kenh 
	public int channelTypeId;
	// do trung thanh
	private int loyalty;
	// nguoi tao
	private String createUser;
	// nguoi cap nhat
	private String updateUser;
	// loai chuong trinh tham gia
	private String dislay;
	// private String tuyen;
	// private String trang_thai;
	// don hang cuoi cung duoc duyet
	private String lastApproveOrder;
	// don hang cuoi cung tao
	private String lastOrder;
	// status
	private int status;
	// lat
	public double lat = -1;
	// lng
	public double lng = -1;
	// cong no cho phep toi da
	private long maxDebitAmount;
	// so ngay toi da cho phep no
	private String maxDebitDate;
	// URL avatar
	private String URL;
	private long parentCustomerId;
	//nhân viên tạo KH này (tablet)
	private long staffId;
	private String customerAvatarID;
	public int shopDistance;	
	private String nameText;
	
	public CustomerDTO() {
		super(TableType.CUSTOMER);
	}

	public String getCustomerId() {
		return Long.toString(customerId);
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public int getAreaId() {
		return areaId;
	}

	public void setAreaId(int areaId) {
		this.areaId = areaId;
	}
	
	public String getAreaCde() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getAddress() {
		return this.address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getTaxCode() {
		return taxCode;
	}

	public void setTaxCode(String taxCode) {
		this.taxCode = taxCode;
	}

	public String getMobilephone() {
		return mobilePhone;
	}

	public void setMobilephone(String mobilephone) {
		this.mobilePhone = mobilephone;
	}

	public int getLocation() {
		return location;
	}

	public void setLocation(int location) {
		this.location = location;
	}

	public int getCustomerTypeId() {
		return channelTypeId;
	}

	public void setCustomerTypeId(int customerTypeId) {
		this.channelTypeId = customerTypeId;
	}

	public int getLoyalty() {
		return loyalty;
	}

	public void setLoyalty(int loyalty) {
		this.loyalty = loyalty;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public String getDislay() {
		return dislay;
	}

	public void setDislay(String dislay) {
		this.dislay = dislay;
	}

	public String getLastApproverder() {
		return this.lastApproveOrder;
	}

	public void setLastApproverder(String lastApproveOrder) {
		this.lastApproveOrder = lastApproveOrder;
	}

	public String getLastOrder() {
		return lastOrder;
	}

	public void setLastOrder(String lastOrder) {
		this.lastOrder = lastOrder;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * @return the maxDebitAmount
	 */
	public long getMaxDebitAmount() {
		return maxDebitAmount;
	}

	/**
	 * @param maxDebitAmount the maxDebitAmount to set
	 */
	public void setMaxDebitAmount(long maxDebitAmount) {
		this.maxDebitAmount = maxDebitAmount;
	}

	/**
	 * @return the maxDebitDate
	 */
	public String getMaxDebitDate() {
		return maxDebitDate;
	}

	/**
	 * @param maxDebitDate the maxDebitDate to set
	 */
	public void setMaxDebitDate(String maxDebitDate) {
		this.maxDebitDate = maxDebitDate;
	}
	
	/**
	 * duongdt
	 * @return the parentCustomerId
	 */
	public long getParentCustomerId() {
		return parentCustomerId;
	}

	/**
	 * duongdt
	 * @param parentCustomerId the parentCustomerId to set
	 */
	public void setParentCustomerId(long parentCustomerId) {
		this.parentCustomerId = parentCustomerId;
	}
	
	/*
	 * duongdt get staffId
	 */
	public long getStaffId() {
		return staffId;
	}
	
	/*
	 * duongdt set staffId
	 */
	public void setStaffId(long staffId) {
		this.staffId = staffId;
	}
	
	
	
	/**
	 * Khoi tao thong tin sau khi query database
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void initLogDTOFromCursor(Cursor c) {
		// setCustomerTypeId(c.getInt(c
		// .getColumnIndex("CUSTOMER_TYPE_ID")));
		// setLoyalty(c.getInt(c.getColumnIndex("LOYALTY")));
		// setDislay(c.getString(c.getColumnIndex("DISLAY")));
		// setLastOrder(c.getString(c.getColumnIndex("LAST_ORDER")));
		setCustomerId(StringUtil.getLongFromSQliteCursor(c,"CUSTOMER_ID"));
		setCustomerCode(c.getString(c.getColumnIndex("CUSTOMER_CODE")));
		setCustomerName(c.getString(c.getColumnIndex("CUSTOMER_NAME")));
		setShopId(c.getString(c.getColumnIndex("SHOP_ID")));
		address = c.getString(c.getColumnIndex("ADDRESS"));
		setStreet(c.getString(c.getColumnIndex("STREET")));
		setHouseNumber(c.getString(c.getColumnIndex("HOUSENUMBER")));
		setPhone(c.getString(c.getColumnIndex("PHONE")));
		setContactPerson(c.getString(c.getColumnIndex("CONTACT_NAME")));
		setMobilephone(c.getString(c.getColumnIndex("MOBIPHONE")));
		setLat(c.getFloat(c.getColumnIndex("LAT")));
		setLng(c.getFloat(c.getColumnIndex("LNG")));
		setURL(c.getString(c.getColumnIndex("URL")));
		shopDistance=Integer.parseInt(c.getString(c.getColumnIndex("DISTANCE")));
		setCustomerAvatarID(c.getString(c.getColumnIndex("CUSTOMER_AVATAR_ID")));
		if(StringUtil.isNullOrEmpty(address)){
			address = houseNumber + " - " + street;
		}
	}

	/**
	 * init customer for create customer
	 * @author: duongdt3
	 * @since: 10:31:18 6 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param c
	 */
	public void initCreateCustomerDTOFromCursor(Cursor c) {
		setCustomerId(StringUtil.getLongFromSQliteCursor(c, "CUSTOMER_ID"));
		setCustomerName(StringUtil.getStringFromSQliteCursor(c,"CUSTOMER_NAME"));
		setAreaId((int) StringUtil.getLongFromSQliteCursor(c, "AREA_ID"));
		setCustomerTypeId((int) StringUtil.getLongFromSQliteCursor(c, "CHANNEL_TYPE_ID"));
		setParentCustomerId(StringUtil.getLongFromSQliteCursor(c, "PARENT_CUSTOMER_ID"));
		setHouseNumber(StringUtil.getStringFromSQliteCursor(c, "HOUSENUMBER"));
		setStreet(StringUtil.getStringFromSQliteCursor(c, "STREET"));
		setPhone(StringUtil.getStringFromSQliteCursor(c, "PHONE"));
		setContactPerson(StringUtil.getStringFromSQliteCursor(c, "CONTACT_NAME"));
		setMobilephone(StringUtil.getStringFromSQliteCursor(c, "MOBIPHONE"));
		setLat(StringUtil.getDoubleFromSQliteCursor(c,"LAT"));
		setLng(StringUtil.getDoubleFromSQliteCursor(c, "LNG"));
	}
	
	/**
	 * Generate cau lenh update
	 * @author: TruongHN
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateFromOrderSql() {
		JSONObject json = new JSONObject();
		try {
			// update customer set LAST_ORDER = sysdate where customer_id =
			// 87484

			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

			// ds params
			JSONArray params = new JSONArray();
			params.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAST_ORDER, lastOrder, null));
			
			if (!StringUtil.isNullOrEmpty(lastApproveOrder)) {
				params.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAST_APPROVE_ORDER, lastApproveOrder, null));
			}
			
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
		}
		return json;
	}

	/**
	 * Generate cau lenh update last order after delete
	 * 
	 * @author: PhucNT
	 * @param lastOrder2
	 * @return: JSONObject
	 * @throws:
	 */
	public JSONObject generateUpdateLastOrder(String lastOrder2) {
		JSONObject json = new JSONObject();
		try {
			// update customer set LAST_ORDER = sysdate where customer_id =
			// 87484

			json.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			json.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

			// ds params
			JSONArray params = new JSONArray();
			if (StringUtil.isNullOrEmpty(lastOrder2)) {
				params.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAST_ORDER, "", DATA_TYPE.NULL.toString()));
			} else {
				Date d= DateUtils.parseDateFromString(lastOrder2, DateUtils.DATE_FORMAT_SQL);
				String lastOrder= DateUtils.convertDateTimeWithFormat(d, DateUtils.DATE_FORMAT_NOW);
				params.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAST_ORDER, lastOrder, null));
			}
			json.put(IntentConstants.INTENT_LIST_PARAM, params);

			// ds where params --> insert khong co menh de where
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			json.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);

		} catch (JSONException e) {
		}
		return json;
	}
	
	/**
	 * tạo json create Customer
	 * @author: duongdt3
	 * @since: 09:50:21 7 Jan 2014
	 * @return: JSONObject
	 * @throws:  
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject generateCreateCustomerSql() throws JSONException {
		JSONObject insertCustomerJson = new JSONObject();
		insertCustomerJson.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
		insertCustomerJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

		// ds params
		JSONArray detailPara = new JSONArray();
		// ...thêm thuộc tính		
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, getCustomerId(), null));
//		if (getParentCustomerId() > 0) {
//			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.PARENT_CUSTOMER_ID , getParentCustomerId() , null));
//		}
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_CODE , getCustomerCode(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_NAME , getCustomerName(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.SHOP_ID , getShopId(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.AREA_ID , getAreaId(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.HOUSENUMBER ,getHouseNumber() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.STREET ,getStreet() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.PHONE ,getPhone() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CREATE_DATE , getCreateDate() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CONTACT_NAME ,getContactPerson() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.MOBIPHONE ,getMobilephone() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CHANNEL_TYPE_ID ,getCustomerTypeId() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CREATE_USER ,getCreateUser() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.STATUS ,getStatus() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.STAFF_ID ,getStaffId() , null));
		if (getParentCustomerId() > 0) {
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.PARENT_CUSTOMER_ID , getParentCustomerId() , null));
		}
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.ADDRESS ,getAddress() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.NAME_TEXT ,getNameText() , null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAT, lat, null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LNG, lng, null));
		insertCustomerJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		return insertCustomerJson;
	}
	
	/**
	 * tạo json update Customer
	 * @author: duongdt3
	 * @since: 09:50:21 7 Jan 2014
	 * @return: JSONObject
	 * @throws:  
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject generateUpdateCustomerSql() throws JSONException {
		JSONObject updateCustomerJson = new JSONObject();
		updateCustomerJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
		updateCustomerJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

		// ds params
		JSONArray detailPara = new JSONArray();
		// ...thêm thuộc tính		
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, getCustomerId(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_NAME, getCustomerName(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.AREA_ID, getAreaId(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.HOUSENUMBER, getHouseNumber(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.STREET, getStreet(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.PHONE, getPhone(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.UPDATE_DATE, getUpdateDate(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CONTACT_NAME, getContactPerson(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.MOBIPHONE, getMobilephone(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CHANNEL_TYPE_ID, getCustomerTypeId(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.UPDATE_USER, getUpdateUser(), null));
		if (getParentCustomerId() > 0) {
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.PARENT_CUSTOMER_ID, getParentCustomerId(), null));
		}else{
			//không có thì phải set lại là null
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.PARENT_CUSTOMER_ID, "" , DATA_TYPE.NULL.toString()));
		}
		
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.ADDRESS, getAddress(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.NAME_TEXT, getNameText(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.STATUS, getStatus(), null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAT, lat, null));
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LNG, lng, null));
		updateCustomerJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		
		// ds where params --> insert khong co menh de where
		JSONArray wheres = new JSONArray();
		wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, getCustomerId(), null));
		updateCustomerJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
					
		return updateCustomerJson;
	}


	/**
	 * tạo câu json xóa
	 * @author: duongdt3
	 * @since: 15:55:11 7 Jan 2014
	 * @return: JSONObject
	 * @throws:  
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject generateDeleteCustomerSql() throws JSONException {
		JSONObject deleteCustomerJson = new JSONObject();
		deleteCustomerJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
		deleteCustomerJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

		// ds params
		JSONArray detailPara = new JSONArray();
		// ...thêm thuộc tính		
		detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.STATUS, getStatus(), null));
		deleteCustomerJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		
		// ds where params --> insert khong co menh de where
		JSONArray wheres = new JSONArray();
		wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, getCustomerId(), null));
		deleteCustomerJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
					
		return deleteCustomerJson;
	}
	
	/**
	 * generate cau lenh update location trong customerinfo
	 * 
	 * @author : BangHN since : 1.0
	 */
	public JSONObject generateUpdateLocationSql() {
		JSONObject updateLocationJson = new JSONObject();
		try {
			updateLocationJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			updateLocationJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LAT, lat, null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.LNG, lng, null));
			if(!StringUtil.isNullOrEmpty(updateDate)){
				detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.UPDATE_DATE, updateDate, null));
			}
			if(!StringUtil.isNullOrEmpty(updateUser)){
				detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.UPDATE_USER, updateUser, null));
			}
			updateLocationJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			updateLocationJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return updateLocationJson;
	}

	/**
	 * 
	 * parse thong tin khach hang
	 * 
	 * @author: YenNTH
	 * @param c
	 * @return: void
	 * @throws:
	 */
	public void parseCustomerInfo(Cursor c) {
		setCustomerId(c.getLong(c.getColumnIndex("CUSTOMER_ID")));
		setCustomerCode(c.getString(c.getColumnIndex("CUSTOMER_CODE")));
		setCustomerName(c.getString(c.getColumnIndex("CUSTOMER_NAME")));
		if(c.getColumnIndex("ADDRESS_") > -1){
			setAddress(c.getString(c.getColumnIndex("ADDRESS_")));
		}
		if(c.getColumnIndex("STREET") > -1){
			setStreet(c.getString(c.getColumnIndex("STREET")));
		}
		if(c.getColumnIndex("HOUSENUMBER") > -1){
			setHouseNumber(c.getString(c.getColumnIndex("HOUSENUMBER")));
		}

	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the customerAvatarID
	 */
	public String getCustomerAvatarID() {
		return customerAvatarID;
	}

	/**
	 * @param customerAvatarID the customerAvatarID to set
	 */
	public void setCustomerAvatarID(String customerAvatarID) {
		this.customerAvatarID = customerAvatarID;
	}

	/**
	 * @return the nameText
	 */
	public String getNameText() {
		return nameText;
	}

	/**
	 * @param nameText the nameText to set
	 */
	public void setNameText(String nameText) {
		this.nameText = nameText;
	}


	public JSONObject generateUpdateCustomerAreaSql() {
		JSONObject updateAreaJson = new JSONObject();
		try {
			updateAreaJson.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			updateAreaJson.put(IntentConstants.INTENT_TABLE_NAME, CUSTOMER_TABLE.CUSTOMER_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.AREA_ID, areaId, null));
			updateAreaJson.put(IntentConstants.INTENT_LIST_PARAM, detailPara);

			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_TABLE.CUSTOMER_ID, customerId, null));
			updateAreaJson.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return updateAreaJson;
	}
	/**
	 * initView
	 * @param c
	 */
	public void initView(Cursor c) {
		if (c.getColumnIndex("CUSTOMER_ID") >= 0) {
			customerId = c.getLong(c.getColumnIndex("CUSTOMER_ID"));
		} else {
			customerId = 0;
		}
		if (c.getColumnIndex("CUSTOMER_CODE") >= 0) {
			customerCode = c.getString(c.getColumnIndex("CUSTOMER_CODE"));
		} else {
			customerCode = "";
		}
		if (c.getColumnIndex("CUSTOMER_NAME") >= 0) {
			customerName = c.getString(c.getColumnIndex("CUSTOMER_NAME"));
		} else {
			customerName = "";
		}
	}
}
