/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.db;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.sqllite.db.CUSTOMER_AVATAR_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

/**
 * DTO hinh anh dai dien khach hang
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerAvatarDTO extends AbstractTableDTO {

	private static final long serialVersionUID = -2726330122678992277L;
	// id key
	private long maxID;

	public long getMaxID() {
		return maxID;
	}

	public void setMaxID(long maxID) {
		this.maxID = maxID;
	}

	// id npp
	private String shopID;
	// ma NPP
	private String customerAvatarID;
	// ten NPP
	private String customerID;
	// id hinh anh
	private String mediaItemID;
	// link anh
	private String URL;
	// trang thai
	private String status;
	// ngay tao
	private String createDate;
	// nguoi tao
	private String createUser;
	// nguoi cap nhat
	private String updateUser;
	// ngay cap nhat
	private String updateDate;
	// id nhan vien
	private String staffID;

	public CustomerAvatarDTO() {
		super(TableType.CUSTOMER_AVATAR_TABLE);
	}

	public CustomerAvatarDTO(String shopID, String customerAvatarID,
			String customerID, String mediaItemID, String URL, String status,
			String createUser, String staffID) {
		this();
		this.shopID = shopID;
		this.customerAvatarID = customerAvatarID;
		this.customerID = customerID;
		this.mediaItemID = mediaItemID;
		this.URL = URL;
		this.status = status;
		this.createUser = createUser;
		this.staffID=staffID;
	}

	/**
	 * tao ra chuoi JSON de gui len server delete hinh
	 * 
	 * @author: dungdq3
	 * @param customerAvatarID
	 * @param staffCode
	 * @return: JSONObject
	 * @date: Jan 6, 2014
	 */
	public JSONObject generateJSONDeleteAvatar() {
		// TODO Auto-generated method stub
		JSONObject customerAvatar = new JSONObject();
		try {
			customerAvatar.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			customerAvatar.put(IntentConstants.INTENT_TABLE_NAME,
					CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_ID, customerAvatarID,
					null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.STATUS, status, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.UPDATE_USER, createUser, null));
			customerAvatar.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_ID, customerAvatarID, null));
			customerAvatar.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return customerAvatar;
	}

	/**
	 * tao ra chuoi JSON de gui len server update hinh
	 * 
	 * @author: dungdq3
	 * @return: JSONObject
	 * @date: Jan 6, 2014
	 */
	public JSONObject generateJSONUpdateAvatar() {
		// TODO Auto-generated method stub
		JSONObject customerAvatar = new JSONObject();
		try {
			customerAvatar.put(IntentConstants.INTENT_TYPE, TableAction.UPDATE);
			customerAvatar.put(IntentConstants.INTENT_TABLE_NAME,
					CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_ID, customerAvatarID,
					null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.UPDATE_DATE, DateUtils.now(), null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.UPDATE_USER, createUser, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.MEDIA_ITEM_ID, mediaItemID, null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_AVATAR_TABLE.URL,
					URL, null));
			customerAvatar.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
			
			JSONArray wheres = new JSONArray();
			wheres.put(GlobalUtil.getJsonColumn(CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_ID, customerAvatarID, null));
			customerAvatar.put(IntentConstants.INTENT_LIST_WHERE_PARAM, wheres);
		} catch (JSONException e) {
		}

		return customerAvatar;
	}

	/**
	 * tao ra chuoi JSON de gui len server insert hinh
	 * 
	 * @author: dungdq3
	 * @return: JSONObject
	 * @date: Jan 6, 2014
	 */
	public JSONObject generateJSONInsertAvatar() {
		// TODO Auto-generated method stub
		JSONObject customerAvatar = new JSONObject();
		try {
			customerAvatar.put(IntentConstants.INTENT_TYPE, TableAction.INSERT);
			customerAvatar.put(IntentConstants.INTENT_TABLE_NAME,
					CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_TABLE);

			// ds params
			JSONArray detailPara = new JSONArray();
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.CUSTOMER_AVATAR_ID, maxID, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.CREATE_USER, createUser, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.MEDIA_ITEM_ID, mediaItemID, null));
			detailPara.put(GlobalUtil.getJsonColumn(CUSTOMER_AVATAR_TABLE.URL,
					URL, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.SHOP_ID, shopID, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.CUSTOMER_ID, customerID, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.STATUS, status, null));
			detailPara.put(GlobalUtil.getJsonColumn(
					CUSTOMER_AVATAR_TABLE.STAFFID, staffID, null));
			customerAvatar.put(IntentConstants.INTENT_LIST_PARAM, detailPara);
		} catch (JSONException e) {
		}

		return customerAvatar;
	}

	/**
	 * @return the shopID
	 */
	public String getShopID() {
		return shopID;
	}

	/**
	 * @param shopID
	 *            the shopID to set
	 */
	public void setShopID(String shopID) {
		this.shopID = shopID;
	}

	/**
	 * @return the customerAvatarID
	 */
	public String getCustomerAvatarID() {
		return customerAvatarID;
	}

	/**
	 * @param customerAvatarID
	 *            the customerAvatarID to set
	 */
	public void setCustomerAvatarID(String customerAvatarID) {
		this.customerAvatarID = customerAvatarID;
	}

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * @param customerID
	 *            the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * @return the mediaItemID
	 */
	public String getMediaItemID() {
		return mediaItemID;
	}

	/**
	 * @param mediaItemID
	 *            the mediaItemID to set
	 */
	public void setMediaItemID(String mediaItemID) {
		this.mediaItemID = mediaItemID;
	}

	/**
	 * @return the uRL
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @param uRL
	 *            the uRL to set
	 */
	public void setURL(String uRL) {
		URL = uRL;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the createDate
	 */
	public String getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the createUser
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * @param createUser
	 *            the createUser to set
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * @param updateUser
	 *            the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * @return the updateDate
	 */
	public String getUpdateDate() {
		return updateDate;
	}

	/**
	 * @param updateDate
	 *            the updateDate to set
	 */
	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * @return the staffID
	 */
	public String getStaffID() {
		return staffID;
	}

	/**
	 * @param staffID the staffID to set
	 */
	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
}
