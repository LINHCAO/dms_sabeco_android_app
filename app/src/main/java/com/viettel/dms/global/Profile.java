/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.global;

import java.io.Serializable;

import android.content.SharedPreferences;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.GPSInfo;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.LoginView;

/**
 *  Luu tru thong tin user dang nhap
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class Profile implements Serializable {
	public static final String NONE = "NONE";
	public static final String UPDATEDB = "UPDATEDB";
	public static final String RESETDB = "RESETDB";
	private String actionLogin = NONE;
	
	public static final String DMS_PROFILE = "sabecoProfile";
	private String softVersion = Constants.STR_BLANK;
	private String dbVersion = Constants.STR_BLANK;
	// revision cua thu muc cache hinh anh
	private String server_revision = "";
	// Luu chung thuc dang nhap
	private String authData = "";
	private UserDTO userData = new UserDTO();
	
	private GPSInfo myGPSInfo = new GPSInfo();
	//is mode debug or not
	private boolean isDebugMode = true;

	//luu cac gia tri ghe tham (chi dung cho chuc nang ket thuc || dong cua)
	private ActionLogDTO actionLogVisitCustomer;
	//dang ghe tham khach hang
	private boolean isVisitingCustomer = false;
	
	
	public void setUserData(UserDTO userData) {
		this.userData = userData;
		save();
	}

	public UserDTO getUserData() {
		return userData;
	}
	
	/**
	 * set action sau khi login
	 * @author : BangHN
	 * since : 10:17:33 AM
	 */
	public void setActionLogin(String action){
		this.actionLogin = action;
		save();
	}
	
	/**
	 * get action sau khi login: action voi db
	 * @author : BangHN
	 * since : 10:17:20 AM
	 */
	public String getActionLogin(){
		return actionLogin;
	}
	
	/**
	 * @return the isDebugMode
	 */
	public boolean isDebugMode() {
		return isDebugMode;
	}
	
	
	
	public boolean isVisitingCustomer() {
		return isVisitingCustomer;
	}

	public void setVisitingCustomer(boolean isVisitingCustomer) {
		this.isVisitingCustomer = isVisitingCustomer;
		save();
	}

	public void setActionLogVisitCustomer(ActionLogDTO action){
		this.actionLogVisitCustomer = action;
		save();
	}
	
	public ActionLogDTO getActionLogVisitCustomer(){
		return actionLogVisitCustomer;
	}

	/**
	 * @param isDebugMode the isDebugMode to set
	 */
	public void setDebugMode(boolean isDebugMode) {
		this.isDebugMode = isDebugMode;
		save();
	}

	/**
	 * set chuoi chung thuc de dang nhap lan sau
	 * 
	 * @author: BangHN
	 */
	public void setAuthData(String auth) {
		this.authData = auth;
		save();
	}

	/**
	 * Tra ve chuoi chung thuc dang nhap
	 * 
	 * @author: BangHN
	 */
	public String getAuthData() {
		return authData;
	}

	/**
	 * set chuoi server revision
	 * 
	 * @author: BangHN
	 */
	public void setServerRevision(String revision) {
		this.server_revision = revision;
		save();
	}

	/**
	 * Tra ve chuoi server revision
	 * 
	 * @author: BangHN
	 */
	public String getServerRevision() {
		return server_revision;
	}

	/**
	 * @return the versionName
	 */
	public String getVersionApp() {
		if(StringUtil.isNullOrEmpty(softVersion) || "1.0.0".equals(softVersion)){
			try {
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				softVersion = sharedPreferences.getString(LoginView.DMS_VER_AP, "1.0.0");
			} catch (Throwable e) {
			}
		}
		if(StringUtil.isNullOrEmpty(softVersion)){
			softVersion = "1.0.0";
		}
		return softVersion;
	}
	
	
	/**
	 * tra ve thong tin version db
	 * @author : BangHN
	 * since : 10:07:10 AM
	 */
	public String getVersionDB(){
		if(StringUtil.isNullOrEmpty(dbVersion) || "0.0.0".equals(dbVersion)){
			try {
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				dbVersion = sharedPreferences.getString(LoginView.DMS_VER_DB, "0.0.0");
			} catch (Throwable e) {
			}
		}
		if(StringUtil.isNullOrEmpty(dbVersion)){
			dbVersion = "0.0.0";
		}
		return dbVersion;
	}
	
	

	/**
	 * @param versionName
	 *            the versionName to set
	 */
	public void setVersionApp(String versionName) {
		if (!StringUtil.isNullOrEmpty(versionName)) {
			int length = versionName.length();
			if (length == 3) {
				// 2.1 --> 2.10
				versionName += ".0";
			}
			// else if (length == 5){
			// // 2.1.1 --> 2.11
			// String lastChar = versionName.substring(length - 1, length);
			// versionName = versionName.substring(0,3) + lastChar;
			// }
		}
		this.softVersion = versionName;
		save();
	}

	
	/**
	 * set dbversion
	 * @author : BangHN
	 * since : 10:06:33 AM
	 */
	public void setVersionDB(String dbVersion){
		if(StringUtil.isNullOrEmpty(dbVersion)){
			dbVersion = "0.0.0";
		}
		this.dbVersion = dbVersion;
		save();
	}
	
	
	public void setMyCell(String MNC, String LAC, String cellId, String cellType) {
		this.myGPSInfo.MNC = MNC;
		this.myGPSInfo.LAC = LAC;
		this.myGPSInfo.cellId = cellId;
		this.myGPSInfo.cellType = cellType;
		save();
	}

	
	
	/**
	 * @return the myGPSInfo
	 */
	public GPSInfo getMyGPSInfo() {
		return myGPSInfo;
	}

	public void setMyGPSInfo(GPSInfo myGPSInfo) {
		this.myGPSInfo = myGPSInfo;
		save();
	}
	

	/**
	 * 
	 * Luu thong in profile
	 * @author : DoanDM since : 9:15:31 AM
	 */
	public void save() {
		GlobalUtil.saveObject(this, DMS_PROFILE);
	}
	
	
	/**
	 * Clear profile
	 * @author : BangHN
	 * since : 1.0
	 */
	public void clearProfile(){
		actionLogVisitCustomer = null;
		isVisitingCustomer = false;
		userData.loginState = 0;
		save();
	}

	/**
	 * Clear profile dang nhap tai khoan khac
	 */
	public void clearProfileLoginOtherUser(){
		actionLogVisitCustomer = null;
		isVisitingCustomer = false;
		userData.loginState = 0;
		userData.enableClientLog = 0;
		GlobalInfo.getInstance().getProfile().setUserData(new UserDTO());
	}
}
