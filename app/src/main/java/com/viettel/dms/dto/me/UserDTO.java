/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.me;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.SharedPreferences;

import com.viettel.dms.dto.AppVersionDTO;
import com.viettel.dms.dto.DBVersionDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.LoginView;
import com.viettel.map.dto.GeomDTO;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPClient;

/**
 * Chua thong tin cua 1 user
 * @author: DoanDM
 * @version: 1.1
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class UserDTO implements Serializable {
	public static final int NOT_LOGIN = 0;
	public static final int LOGIN_SUCCESS = 1;
	public static final int PROCESSING_LOGIN = 2;
	//DINH NGHIA LOAI NHAN VIEN
	public static final int TYPE_PRESALES = 1;//presales
	public static final int TYPE_VALSALES = 2;//valsales
	public static final int TYPE_GSNPP = 5;//giam sat
	public static final int TYPE_TNPG  = 11;//tn tiep thi
	public static final int TYPE_PG  = 12;//NV tiep thi
	public static final int TYPE_GSTINH  = 13;//gaim sat tinh
	public static final int TYPE_ADMIN  = 14;//Giam doc

	//la so cua viettel
	public boolean isVT = false;
	
	public long id = -1;//id nhan vien
	public int staffTypeId = -1;//id loai nhan vien
	public String userName;//ten dang nhap
	public String userCode;//ma code
	// ten hien thi
	public String birthday;//ngay sinh
	public String displayName;//ten
	public String street;//dia chi duong
	public String address;//dia chi duong
	public String plan;//plan
	// thong tin version app
	public AppVersionDTO appVersion;
	// thong tin version db
	public DBVersionDTO dbVersion;
	//co can reset clear DB
	public boolean isDeleteData  = false;
	
	private GeomDTO geom = new GeomDTO();
	public int loginState = 0;
	public String pass;
	//id nguoi quan ly
	public long staffOwnerId;
	//ma loai nhan vien ban hang
	public String channelTypeCode;
	public String saleTypeCode;
	//loai nhan vien
	public int chanelType;
	//loai NV: Presales, Valsales, GSNPP, TBHV
	public int chanelObjectType = -1;
	//ten loai nhan vien
	public String channelTypeName;

	// phục vụ việc kiểm tra thời gian
	public String lastTimeOnlineLogin;
	public String lastRightTime;
	public long lastRightTimeSinceBoot;
	
	public String serverDate;//thoi gian server tra ve
	public boolean isUserDB = false;
	//shop chon de quan ly ( cua GSNPP)
	public ShopDTO shopManaged;
	//Id shop chon de quan ly
	public String shopId;
	//shopId de dong bo du lieu, ....
	public String shopIdProfile;
	//toa do lat,lng NPP
	public double shopLat = 0;
	public double shopLng = 0;
	//list shop quan ly cua GSNPP
	public ArrayList<ShopDTO> listShop;
	public String shopCode;
	// cho phep ghi log kpi
	public int enableClientLog = 0;
	// kiem tra cho phep cai dat ung dung, cho phep truy cap network
	public int checkAccessApp = -1;
	//trang thai check install, truy cap network
	public static final int CHECK_INSTALL = 1;
	public static final int CHECK_NETWORK = 2;
	public static final int CHECK_ALL_ACCESS = 3;
	//Id thiet bi ung voi EMEI
	public int deviceId = 0;
	// Cho phep send log hay ko?
	public int sendLog = -1;
	//so dien thoai PG login
	public String mobilePhone;
	//salt hash pass
	public String salt;
	public String saltChangePass = "";
	public static final int SEND_LOG_ALL = 1;
	public static final int SEND_LOG_POSITION = 2;
	public void parseFromJSONLogin(JSONObject entry) {
		if (entry == null)
			return;
		try {
			//apversion
			if (entry.has("appversion")
					&& entry.getJSONObject("appversion").length() != 0) {
				appVersion = new AppVersionDTO();
				appVersion.parseFromJson(entry.getJSONObject("appversion"));
			}
			//dbversion
			if (entry.has("dbversion")) {
				dbVersion = new DBVersionDTO();
				dbVersion.parseFromJson(entry.getJSONObject("dbversion"));
			}
			
			JSONObject channelType = entry.getJSONObject("channelType");
			if(channelType != null){
				channelTypeName = channelType.getString("channelTypeName");
				channelTypeCode = channelType.getString("channelTypeCode");
				chanelObjectType = channelType.getInt("objectType");
				chanelType = channelType.getInt("type");
			}
			serverDate = entry.getString("serverDate");
			HTTPClient.sessionID = "JSESSIONID=" + entry.getString("sessionId");

			// isVT = entry.getBoolean("isVT");
			GlobalInfo.getInstance().getProfile()
					.setAuthData(entry.getString("authData"));

			// parse profile user login
			JSONObject profile = entry.getJSONObject("profile");
			if(profile != null){
				if (!profile.isNull("staffId")) {
					this.id = profile.getLong("staffId");
				}
				if (!profile.isNull("staffCode")) {
					userName = profile.getString("staffCode");
				}
				if (!profile.isNull("password")) {
					pass = profile.getString("password");
				}
				if (!profile.isNull("staffCode")) {
					userCode = profile.getString("staffCode");
				}
				if (!profile.isNull("birthday")) {
					birthday = profile.getString("birthday");
				}
				if (!profile.isNull("street")) {
					street = profile.getString("street");
				}
				if (!profile.isNull("address")) {
					address = profile.getString("address");
				}
				if (!profile.isNull("plan")) {
					plan = profile.getString("plan");
				}
				if (!profile.isNull("staffName")) {
					displayName = profile.getString("staffName");
				}
				if (!profile.isNull("staffOwnerId")) {
					staffOwnerId = profile.getLong("staffOwnerId");
				}
				if (!profile.isNull("staffTypeId")) {
					staffTypeId = profile.getInt("staffTypeId");
				}
				if (!profile.isNull("saleTypeCode")) {
					saleTypeCode = profile.getString("saleTypeCode");
				}
				int isDelete = profile.getInt("isDeleteData");
				if (isDelete == 1) {
					isDeleteData = true;
				} else {
					isDeleteData = false;
				}
				if (StringUtil.isNullOrEmpty(displayName)) {
					displayName = userName;
				}
				if (!profile.isNull("shopId")) {
					shopId = profile.getString("shopId");
				}
				shopIdProfile = shopId;
				if (!profile.isNull("shopLat")) {
					shopLat = profile.getDouble("shopLat");
				}
				if (!profile.isNull("shopLng")) {
					shopLng = profile.getDouble("shopLng");
				}
				if (!profile.isNull("shopCode")) {
					shopCode = profile.getString("shopCode");
				}
				if (!profile.isNull("enableClientLog")) {
					if (profile.has("enableClientLog")) {
						enableClientLog = profile.getInt("enableClientLog");
					}
				}
				if (!profile.isNull("checkInstallApp")) {
					if (profile.has("checkInstallApp")) {
						checkAccessApp = profile.getInt("checkInstallApp");
					}
				}
				if (entry.has("deviceId")) {
					deviceId  = entry.getInt("deviceId");
				}
				if (!profile.isNull("mobilePhone")) {
					if (profile.has("mobilePhone")) {
						mobilePhone = profile.getString("mobilePhone");
					}
				}
				if (!profile.isNull("sendLog")) {
					if (profile.has("sendLog")) {
						sendLog = profile.getInt("sendLog");
					}
				}
				if (!profile.isNull("salt")) {
					if (profile.has("salt")) {
						salt = profile.getString("salt");
					}else{
						salt = userName;
					}
				}else{
					salt = userName;
				}
			}
			JSONArray listSubShop = entry.getJSONArray("listSubShop");
			if(listSubShop != null){
				//Fixed truong hop gsnpp login offline,
				//sau do relogin trong ct, can lay lai shopnpp chon truoc do
				
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				
				String shopIdNPP = sharedPreferences.getString(LoginView.DMS_SHOP_ID, "");
				
				listShop = new ArrayList<ShopDTO>();
				int size = listSubShop.length();
				for(int i = 0; i < size; i++){
					JSONObject item = listSubShop.getJSONObject(i);
					if (item != null) {
						ShopDTO shop = new ShopDTO();
						if (!item.isNull("shopId")) {
							shop.shopId = item.getInt("shopId");
						}
						if (!item.isNull("shopName")) {
							shop.shopName = item.getString("shopName");
						}
						if (!item.isNull("shopCode")) {
							shop.shopCode = item.getString("shopCode");
						}
						if (!item.isNull("address")) {
							shop.street = item.getString("address");
						}
						listShop.add(shop);
						
						//lay lai id npp chon truoc do
						if(shopIdNPP != null && shopIdNPP.equals("" + shop.shopId)){
							this.shopId = "" + shop.shopId;
						}
					}
				}
			}

			if(GlobalInfo.getInstance().getProfile().getUserData() != null && GlobalInfo.getInstance().getProfile().getUserData().shopManaged != null) {
				this.shopManaged = GlobalInfo.getInstance().getProfile().getUserData().shopManaged;
			}
			loginState = LOGIN_SUCCESS;
			GlobalInfo.getInstance().getProfile().getUserData().setLoginState(LOGIN_SUCCESS);

		} catch (JSONException ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	public void setGeom(GeomDTO geom) {
		this.geom = geom;
	}

	public GeomDTO getGeom() {
		return geom;
	}

	public void setLoginState(int state) {
		this.loginState = state;
	}

	public int getLoginState() {
		return this.loginState;
	}
}
