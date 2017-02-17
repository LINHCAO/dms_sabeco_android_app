/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.global;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;

import com.google.gson.Gson;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.KPILogDTO;
import com.viettel.dms.dto.db.StaffPositionLogDTO;
import com.viettel.dms.dto.me.NotifyInfoOrder;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.PGOrderViewDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.GlobalUtil.IDataValidate;
import com.viettel.dms.util.SqlCipherUtil;
import com.viettel.dms.util.StatusNotificationHandler;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.LoginView;
import com.viettel.dms.view.pg.PGOrderView;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTGlobalUtils;
import com.viettel.utils.VTLog;
import com.viettel.utils.VTStringUtils;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 *  Luu tru cac bien su dung chung trong chuong trinh
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class GlobalInfo extends Application {
	public static final boolean IS_SEND_DATA_VERSION = true;//phien ban goi du lieu
	public static final boolean IS_EMULATOR = false;//MAY AO
	public static final int SYNDATA_NONE = 1;//khong dong bo
	public static final int SYNDATA_EXECUTING = 2;//dang xy ly
	public static final int SYNDATA_CANCELED = 3;//user cancel
	public static final String DMS_CURRENT_TAG = "sabecoCurrentTag";
	public static final String DMS_TIME_TEST_ORDER = "sabecoTimeTestOrder";
	public static final String DMS_RADIUS_OF_POSITION = "sabecoRadiusOfPosition";
	public static final String DMS_TIME_TRIG_POSITION = "sabecoTimeTrigPosition";
	public static final String DMS_TIME_SYNC_TO_SERVER = "sabecoTimeSyncServer";
	public static final String DMS_TIME_START_IDLE_STATUS = "sabecoTimeStartIdleStatus";
	public static final String DMS_TIME_ALLOW_IDLE_STATUS = "sabecoTimeAllowIdleStatus";
	public static final String DMS_BEGIN_TIME_ALLOW_SYN = "sabecoBeginTimeAllowSyn";
	public static final String DMS_END_TIME_ALLOW_SYN = "sabecoEndTimeAllowSyn";
	public static final String DMS_ALLOW_EDIT_PROMOTION = "sabecoAllowEditPromotion";
	public static final String DMS_SERVER_IMAGE_PRODUCT = "sabecoServerImageProduct";
	public static final String DMS_CC_START_TIME = "sabecoCCStartTime";
	public static final String DMS_CC_END_TIME = "sabecoCCEndTime";
	public static final String DMS_CC_DISTANCE = "sabecoCCDistance";
	public static final String DMS_LIST_POSITION = "sabecoListPosition";
	public static final String DMS_IS_SHOW_PRICE = "dmsIsShowPrice";
	
	public static final String VNM_TIME_CREATE_HASHMAP_KPI = "sabecoTimeCreateHashmapKPI";
	public static final String VNM_HASHMAP_KPI = "sabecoHashMapKPI";
	public static final String VNM_LIST_KPI_LOG = "sabecoListKPILog";
	// chan 3G
	public static final String DMS_WHITE_LIST = "sabecoWhiteList";
	public static final String DMS_BLACK_LIST_APP_BLOCKED = "sabecoBlackListAppBlocked";
	public static final String DMS_TIME_LOCK_APP_BLOCKED = "sabecoTimeLockApp";
	private static final String DMS_DEVICE_ID = "sabecoDeviceId";
	//gps google service cofig
	private static final String VNM_INTERVAL_FUSED_POSITION = "intervalFusedPosition";
	private static final String VNM_FAST_INTERVAL_FUSED_POSITION = "fastIntervalFusedPosition";
	private static final String VNM_RADIUS_FUSED_POSITION = "radiusFusedPosition";
	private static final String VNM_FUSED_POSITION_PRIORITY = "fusedPositionPriority";
	// thoi gian day du lieu qua khu len server
	public static final String DMS_TIME_CHECK_TO_SERVER = "timeCheckToServer";
	//Sử dụng dấu phẩy (,) hay dấu chấm (.) phân cách thập phân? 0: dấu phẩy; 1: dấu chấm
	public static final String VNM_SYS_DECIMAL_POINT = "sabecoSysDecimalPoint";
	private static final String VNM_FLAG_VALIDATE_LOCATION = "flagLocation";
	public static final String CF_NUM_FLOAT = "CF_NUM_FLOAT";
	// Vận tốc di chuyển chuẩn của xe máy (Km/h)
	public static final String DMS_LOCATION_SPEED = "locationSpeed";
	// Số ngày cho phép đẩy đơn hàng PG
	public static final String PG_SEND_ORDER = "pgSendOrder";
	// danh sach san pham pg
	public static final String PG_LIST_PRODUCT = "pgListProduct";
	// Email send data
	public static final String EMAIL_SEND_DATA = "emailSendData";
	// danh sach san pham pg
	public static final String PG_LIST_ALL_PRODUCT = "pgListAllProduct";
	// Loai ma hoa pass
	public static final String TYPE_HASH_PASS = "typeHashPass";
	//KEY BAN DO SU DUNG
	public static String VIETTEL_MAP_KEY = Constants.DMS_VIETTEL_MAP_KEY_RELEASE;
		
	// phan khong luu xuong file
	private static GlobalInfo instance = null;
//	Context appContext;// application context
	Context activityContext;// activity context
	public boolean isDebugMode = true;
	private boolean isAppActive;
	public final String VERSION = "1.0";
	public final String PLATFORM_SDK_STRING = android.os.Build.VERSION.SDK;
	public final String PHONE_MODEL = "Android_" + android.os.Build.MODEL;
	private String DEVICE_IMEI;
	// phan luu xuong file
	private Profile profile = new Profile();
	private Handler mHandler = new Handler();
	private Bitmap bmpData = null;// luu giu data bitmap khi dinh kem hinh anh
	// du lieu dung de test request
	public ArrayList<HTTPRequest> arrayRequest = new ArrayList<HTTPRequest>();
	//thoi gian hop le khi tao don hang trong ngay (gio:hour)
	private long timeTestOrder = 0;
	//gioi han ban kinh cho phep lay dinh vi, (do chinh xac):met
	private long radiusOfPosition = 300;
	//thoi gian triger luong dinh vi (phut)
	private long timeTrigPosition = 0;
	// thoi gian dong bo client len server
	private long timeSyncToServer = 0; 
	//thoi gian bat dau trang thai idle, pause
	private long timeStartIdleStatus = 0;
	//thoi gian cho phep o trang thai IDLE
	private long timeAllowIdleStatus = 0;
	//thoi gian bat dau cho phep dong bo du lieu
	private int beginTimeAllowSynData = 0;
	//thoi gian ket thuc cho phep dong bo du lieu
	private int endTimeAllowSynData = 0;
	// bien dung de kiem tra co cho phep chinh sua khuyen mai tu dong hay khong
	private int allowEditPromotion = -1;
	//thoi gian luong dinh vi trong qua trinh cham cong
	private long timeTrigPositionAttendance = 120000;
	//Thoi gian bat dau cham cong
	private String ccStartTime = "";
	// Thoi gian ket thuc cham cong
	private String ccEndTime = "";
	// khoang cach cham cong
	private int ccDistance = 0;
	// thoi gian sai lenh giua client va server
	private long maxIimeWrong = 0;
	
	private ArrayList<String> listStackTag = new ArrayList<String>();
	//private AlertDialog alertDialog;

	// bien dung de kiem tra ghi log - ko can luu khi bi reset
	public boolean isSendLogException = false;
	
	//locatin, count: su dung de kiem tra locating
	private Location location;
	private int count = 0;
	
	// xu ly realtime ngoai chuong trinh
	private StatusNotificationHandler statusNotifier;
	// bien luu tru thong tin don hang tra ve realtịme
	public NotifyInfoOrder notifyOrderReturnInfo = new NotifyInfoOrder(); // khong can luu khi reset
	// luu activity dang dung -- ko can luu khi reset
	public GlobalBaseActivity lastActivity = null;
	
	// kiem tra ung dung da thoat hay chua
	public boolean isExitApp = false;
	
	//time send location khi connection changed
	public long timeSendLogPosition = 0;
	//time send location khi dinh vi
	public long timeSendLogLocating = 0;
	//trang thai dong bo du lieu
	public int stateSynData = SYNDATA_NONE; 
	//link hinh anh server product
	private String serverImageProductVNM = "";
	//luu vi tri day offline
	private ArrayList<StaffPositionLogDTO> listPosition = new ArrayList<StaffPositionLogDTO>();
	//Hien thi gia khi tao, xem don hang hay khong?
	private int isShowPrice = -1;
	//list position su dung dong bo len server trong thoi gian offline
	private ArrayList<KPILogDTO> listKPILogOffline = new ArrayList<KPILogDTO>();
	// hashmap cac chuc nang kpi
	public HashMap<Integer, Integer> hashMapKPI;
	// so lan toi da request ghi log cua 1 chuc nang
	public int allowRequestLogKPINumber = 10;
	//kiem tra tranh truong hop dang cipher co phat sinh insert vao log-table (dinh vi...)
	public static boolean inProcessCipher = false;
	//toa do khach hang dang ghe tham, muc dich de kiem tra dinh vi 
	private LatLng positionCustomerVisiting;

	// danh sách ứng dụng hiện tại
	public String lastAppsList;
	// phục vụ việc kiểm tra thời gian
	public String lastTimeOnlineLogin;
	//kiem tra trang thai khong cho phep ket noi (1:online; -1 offline)
	private int stateConnectionMode = 0;
	// bien kiem tra co app fake locaiton
	public boolean isHasAppFakeLocation = false;
	// mang ds dc phep cai dat vao
	private ArrayList<String> whiteListAppInstall = new ArrayList<String>();
	// mang ds cac dung chan ko cho du sung
	private ArrayList<String> blackListAppBlocked = new ArrayList<String>();
	// thoi gian dong bo client len server
	private long timeLockAppBlocked = 0;
	// bien kiem tra de chi send log cac package ung dung mot lan khi dang nhap
	public boolean isFirstTimeSendApp = false;
	//Id thiet bi tuong ung voi thiet bi (EMEI)
	public int deviceId;
	//chu kỳ request fused position
	private long intervalFusedPosition = 0;
	// chu kỳ ngắn nhất request fused position
	private long fastIntervalFusedPosition = 0;
	// bán kính cho phép của vị trí Fused
	private long radiusFusedPosition = 0;
	// độ chính xác của request GPS Fused (1 chính xác cao, 2 cân bằng)
	private long fusedPositionPriority = 0;
	// thoi gian day du lieu qua khu len server
	private int timeCheckToServer = 0;
	//Sử dụng dấu phẩy (,) hay dấu chấm (.) phân cách thập phân? 1: dấu phẩy; 2: dấu chấm
	//mặc định là 2 dấu chấm
	private int sysDecimalPoint = 0;
	//Số chữ số phần thập phân của tiền tệ
	//mặc định là 2 số
	private int sysDigitDecimal = 0;
	//cách validate vị trí (1 không validate, 2 validate)
	private long flagValidateLocation = 0;

	private boolean vtMapSetServer = false;
	private String vtMapProtocol = "";
	private String vtMapIP = "";
	private int vtMapPort = 0;
	// Vận tốc di chuyển chuẩn của xe máy (Km/h)
	private int locationSpeed = 0;
	// Cấu hình hiển thị bao nhiêu số thập phân trong shop hiện tại
	public int cfNumFloat = 2;
	// Số ngày cho phép đẩy đơn hàng PG
	public int pgNumSendOrder = 0;
	private String emailSendData = "dmsviettel@gmail.com";
	// danh sach san pham
	private ArrayList<PGOrderViewDTO> listProduct = new ArrayList<PGOrderViewDTO>();
	// danh sach tat ca san pham
	private ArrayList<PGOrderViewDTO> listAllProduct = new ArrayList<PGOrderViewDTO>();
	private String typeHashPass = "MD5";
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		DEVICE_IMEI = telephonyManager.getDeviceId();
		// xu ly clear file sharedpreferences chua ma hoa, khi nang cap tu phien ban cu len
//		SharedPreferences sharedpreferences = getSharedPreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
//		if(sharedpreferences.getInt(LoginView.DMS_ID, 0) > 0){
//			sharedpreferences.edit().clear().commit();
//		}
//		GlobalInfo.getInstance().getDmsPrivateSharePreference();
//		GlobalInfo.getInstance().getTimeSharePreference();
		SqlCipherUtil.ensureSecurePreference(this);
		
		try {
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
			GlobalInfo.getInstance().isDebugMode = ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
		} catch (Exception ex) {
			GlobalInfo.getInstance().isDebugMode = false;
		}
		//lấy thông tin là tablet hay không
//		if (GlobalUtil.checkIsTablet(this)){
//			Constants.IS_TABLET = true;
//		} else{
//			Constants.IS_TABLET = false;
//		}
		// set gia tri debug hay khong de build release ko ghi log
		VTLog.setIsDebugMode(GlobalInfo.getInstance().isDebugMode);
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		MultiDex.install(this);
	}


	public static GlobalInfo getInstance() {
		return instance;
	}

	/**
	 * Get id may
	 * @author : BangHN
	 * since : 1.0
	 */
	public String getDeviceIMEI(){
		if(StringUtil.isNullOrEmpty(DEVICE_IMEI)){
			TelephonyManager telephonyManager = (TelephonyManager)getActivityContext().getSystemService(Context.TELEPHONY_SERVICE);
			DEVICE_IMEI = telephonyManager.getDeviceId();
		}
		if(StringUtil.isNullOrEmpty(DEVICE_IMEI)){
			DEVICE_IMEI = "deviceImei";
		}
		return DEVICE_IMEI;
	}
	

	public Context getAppContext() {
		return instance;
	}

	public void setActivityContext(Context context) {
		this.activityContext = context;
		//alertDialog =  new AlertDialog.Builder(context).create();
	}

	public Context getActivityContext() {
		return activityContext;
	}

	public Handler getAppHandler() {
		return mHandler;
	}
	public AlertDialog getAlertDialog() {
		//return alertDialog;
		return new AlertDialog.Builder(activityContext).create();
	}

	/**
	 * set bitmap data
	 * 
	 * @author: BangHN
	 * @return: Bitmap
	 */
	public void setBitmapData(Bitmap data) {
		bmpData = data;
	}

	/**
	 * get bitmap data
	 * @author: BangHN
	 * @return: Bitmap
	 */
	public Bitmap getBitmapData() {
		return bmpData;
	}

	public long getTimeTestOrder() {
		Object temp;
		try{
			if (this.timeTestOrder <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_TIME_TEST_ORDER)) != null) {
					this.timeTestOrder = (Long) temp;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (this.timeTestOrder <= 0){
			this.timeTestOrder = 1;
		}
		return this.timeTestOrder;
	}

	public void setTimeTestOrder(long timeTestOrder) {
		this.timeTestOrder = timeTestOrder;
		GlobalUtil.saveObject(this.timeTestOrder,DMS_TIME_TEST_ORDER);
	}

	public long getRadiusOfPosition() {
		Object temp;
		try{
			if (this.radiusOfPosition <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_RADIUS_OF_POSITION)) != null) {
					this.radiusOfPosition = (Long) temp;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (this.radiusOfPosition <= 0){
			this.radiusOfPosition = 300;
		}
		return this.radiusOfPosition;
	}

	public void setRadiusOfPosition(long radiusOfPosition) {
		this.radiusOfPosition = radiusOfPosition;
		GlobalUtil.saveObject(this.radiusOfPosition,DMS_RADIUS_OF_POSITION);
	}
	
	public String getServerImageProductVNM() {
		Object temp;
		try{
			if (StringUtil.isNullOrEmpty(this.serverImageProductVNM)) {
				if ((temp = GlobalUtil.readObject(
						DMS_SERVER_IMAGE_PRODUCT)) != null) {
					this.serverImageProductVNM = (String) temp;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (StringUtil.isNullOrEmpty(this.serverImageProductVNM)){
			this.serverImageProductVNM = ServerPath.IMAGE_PRODUCT_VNM;
		}
		return this.serverImageProductVNM;
	}
	
	public void setServerImageProductVNM(String serverImageProductVNM) {
		this.serverImageProductVNM = serverImageProductVNM;
		GlobalUtil.saveObject(this.serverImageProductVNM, DMS_SERVER_IMAGE_PRODUCT);
	}

	public long getTimeTrigPosition() {
		Object temp;
		try{
			if (this.timeTrigPosition <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_TIME_TRIG_POSITION)) != null) {
					this.timeTrigPosition = (Long) temp;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (this.timeTrigPosition <= 0){
			this.timeTrigPosition = 5;
		}
		return this.timeTrigPosition*60*1000;
	}

	public void setTimeTrigPosition(long timeTrigPosition) {
		this.timeTrigPosition = timeTrigPosition;
		GlobalUtil.saveObject(this.timeTrigPosition,DMS_TIME_TRIG_POSITION);
	}
	
	
	
	public long getTimeTrigPositionAttendance() {
		if(timeTrigPositionAttendance < 120000)
			timeTrigPositionAttendance = 120000; 
		return timeTrigPositionAttendance;
	}

	public void setTimeTrigPositionAttendance(long timeTrigPositionAttendance) {
		this.timeTrigPositionAttendance = timeTrigPositionAttendance;
	}

	public void setTimeSyncToServer(long timeSyncToServer) {
		this.timeSyncToServer = timeSyncToServer;
		GlobalUtil.saveObject(this.timeSyncToServer,DMS_TIME_SYNC_TO_SERVER);
	}
	
	public long getTimeSyncToServer() {
		Object temp;
		try{
			if (this.timeSyncToServer <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_TIME_SYNC_TO_SERVER)) != null) {
					this.timeSyncToServer = (Long) temp;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		if (this.timeSyncToServer <= 0){
			this.timeSyncToServer = 3;
		}
		return this.timeSyncToServer*60*1000;
	}

	
	
	public long getTimeStartIdleStatus() {
		Object temp;
		try{
			if (this.timeStartIdleStatus <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_TIME_START_IDLE_STATUS)) != null) {
					this.timeStartIdleStatus = (Long) temp;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception
		}
		return timeStartIdleStatus;
	}

	public void setTimeStartIdleStatus(long timeStartIdleStatus) {
		this.timeStartIdleStatus = timeStartIdleStatus;
		GlobalUtil.saveObject(this.timeStartIdleStatus,DMS_TIME_START_IDLE_STATUS);
	}


	public long getTimeAllowIdleStatus() {
		Object temp;
		try{
			if (this.timeAllowIdleStatus <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_TIME_ALLOW_IDLE_STATUS)) != null) {
					this.timeAllowIdleStatus = (Long) temp;
				}
			}
		}catch (Exception e) {
		}
		
		if (this.timeAllowIdleStatus <= 0){
			this.timeAllowIdleStatus = 180;//3 hours
		}
		return timeAllowIdleStatus*60*1000;
	}

	public void setTimeAllowIdleStatus(long timeAllowIdleStatus) {
		this.timeAllowIdleStatus = timeAllowIdleStatus;
		GlobalUtil.saveObject(this.timeAllowIdleStatus,DMS_TIME_ALLOW_IDLE_STATUS);
	}

	

	public int getBeginTimeAllowSynData() {
		Object temp;
		try{
			if (this.beginTimeAllowSynData <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_BEGIN_TIME_ALLOW_SYN)) != null) {
					this.beginTimeAllowSynData = (Integer) temp;
				}
			}
		}catch (Exception e) {
		}
		
		if (this.beginTimeAllowSynData < 0){
			this.beginTimeAllowSynData = 5;//5 oclok's
		}
		return beginTimeAllowSynData;
	}

	public void setBeginTimeAllowSynData(int beginTimeAllowSynData) {
		this.beginTimeAllowSynData = beginTimeAllowSynData;
		GlobalUtil.saveObject(this.beginTimeAllowSynData,DMS_BEGIN_TIME_ALLOW_SYN);
	}

	public int getEndTimeAllowSynData() {
		Object temp;
		try{
			if (this.endTimeAllowSynData <= 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_END_TIME_ALLOW_SYN)) != null) {
					this.endTimeAllowSynData = (Integer) temp;
				}
			}
		}catch (Exception e) {
		}
		
		if (this.endTimeAllowSynData <= 0){
			this.endTimeAllowSynData = 22;//20 oclok's
		}
		return endTimeAllowSynData;
	}

	public void setEndTimeAllowSynData(int endTimeAllowSynData) {
		this.endTimeAllowSynData = endTimeAllowSynData;
		GlobalUtil.saveObject(this.endTimeAllowSynData,DMS_END_TIME_ALLOW_SYN);
	}
	
	public int getAllowEditPromotion() {
		Object temp;
		try{
			if (this.allowEditPromotion < 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_ALLOW_EDIT_PROMOTION)) != null) {
					this.allowEditPromotion = (Integer) temp;
				}
			}
		}catch (Exception e) {
		}
		
		if (this.allowEditPromotion < 0){
			this.allowEditPromotion = 0; // mac dinh la khong cho chinh sua
		}
		return allowEditPromotion;
	}
	
	public void setAllowEditPromotion(int allowEditPromotion) {
		this.allowEditPromotion = allowEditPromotion;
		GlobalUtil.saveObject(this.allowEditPromotion,DMS_ALLOW_EDIT_PROMOTION);
	}

	
	
	public String getCcStartTime() {
		Object temp;
		try {
			if (StringUtil.isNullOrEmpty(ccStartTime)) {
				if ((temp = GlobalUtil.readObject(DMS_CC_START_TIME)) != null) {
					this.ccStartTime = (String) temp;
				}
			}
		} catch (Exception e) {
		}

		if (StringUtil.isNullOrEmpty(ccStartTime)) {
			// mac dinh 7h30 bat dau cham cong
			this.ccStartTime = "7:30";
		}
		return ccStartTime;
	}

	public void setCcStartTime(String ccStartTime) {
		this.ccStartTime = ccStartTime;
		GlobalUtil.saveObject(this.ccStartTime,DMS_CC_START_TIME);
	}

	public String getCcEndTime() {
		Object temp;
		try {
			if (StringUtil.isNullOrEmpty(ccEndTime)) {
				if ((temp = GlobalUtil.readObject(DMS_CC_END_TIME)) != null) {
					this.ccEndTime = (String) temp;
				}
			}
		} catch (Exception e) {
		}

		if (StringUtil.isNullOrEmpty(ccEndTime)) {
			// mac dinh 8h15 ket thuc cham cong
			this.ccEndTime = "8:15";
		}
		return ccEndTime;
	}

	public void setCcEndTime(String ccEndTime) {
		this.ccEndTime = ccEndTime;
		GlobalUtil.saveObject(this.ccEndTime,DMS_CC_END_TIME);
	}

	public int getCcDistance() {
		Object temp;
		try {
			if (ccDistance <= 0) {
				if ((temp = GlobalUtil.readObject(DMS_CC_DISTANCE)) != null) {
					this.ccDistance = (Integer) temp;
				}
			}
		} catch (Exception e) {
		}

		if (ccDistance <= 0) {
			// mac dinh la 300m
			this.ccDistance = 300;
		}
		return ccDistance;
	}
	
	public void setCcDistance(int distance){
		this.ccDistance = distance;
		GlobalUtil.saveObject(this.ccDistance,DMS_CC_DISTANCE);
	}
	
	public int getIsShowPrice() {
		Object temp;
		try{
			if (this.isShowPrice < 0 ) {
				if ((temp = GlobalUtil.readObject(
						DMS_IS_SHOW_PRICE)) != null) {
					this.isShowPrice = (Integer) temp;
				}
			}
		}catch (Exception e) {
		}
		
		if (this.isShowPrice < 0){
			this.isShowPrice = 1; // mac dinh la cho phep xem gia
		}
		return isShowPrice;
	}

	public void setIsShowPrice(int isShowPrice) {
		this.isShowPrice = isShowPrice;
		GlobalUtil.saveObject(this.isShowPrice, DMS_IS_SHOW_PRICE);
	}
	
	/**
	 * huy bo doi tuong bitmap dta
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public void recycleBitmapData() {
		if (bmpData != null) {
			bmpData.recycle();
			bmpData = null;
		}
	}

	/**
	 * Lay thong tin profile
	 * 
	 * @author: DoanDM
	 * @return
	 * @return: KunKunProfile
	 * @throws:
	 */
	public Profile getProfile() {
		Object temp;
		if (profile == null || profile.getUserData() == null || profile.getUserData().id <= 0) {// nghia la bien
			// thai ban dau hoac da bi reset
			if ((temp = GlobalUtil.readObject(Profile.DMS_PROFILE)) != null) {
				profile = (Profile) temp;// bi out memory
			}
			if (profile == null){
				profile = new Profile();
				profile.setUserData(tryGetUserData());
				profile.save();
			}else if(profile != null && (profile.getUserData() == null || profile.getUserData().id <= 0)){
				profile.setUserData(tryGetUserData());
				profile.save();
			}
						
		}
		return profile;
	}
	
	
	/**
	 * Doc lai thong tin userData khi get profile bi null, read lai bi null
	 * @author banghn
	 * @return
	 */
	private UserDTO tryGetUserData(){
		UserDTO userDTO = new UserDTO();
		try{
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			userDTO.userCode = sharedPreferences.getString(LoginView.DMS_STAFF_CODE, "");
			userDTO.shopId = sharedPreferences.getString(LoginView.DMS_SHOP_ID, "");
			userDTO.shopIdProfile = sharedPreferences.getString(LoginView.DMS_SHOP_ID_PROFILE, "");
			userDTO.id = sharedPreferences.getInt(LoginView.DMS_ID, 0);
			userDTO.pass = sharedPreferences.getString(LoginView.DMS_PASSWORD, "");
			userDTO.chanelType = sharedPreferences.getInt(LoginView.DMS_CHANEL_TYPE, 0);
			userDTO.chanelObjectType = sharedPreferences.getInt(LoginView.DMS_CHANEL_OBJECT_TYPE, -1);
			userDTO.staffOwnerId = sharedPreferences.getLong(LoginView.DMS_STAFF_OWNER_ID, 0);
			userDTO.userName = sharedPreferences.getString(LoginView.DMS_USER_NAME, "");
			userDTO.saleTypeCode = sharedPreferences.getString(LoginView.DMS_SALE_TYPE_CODE, "");
			userDTO.shopLat = Double.parseDouble(sharedPreferences.getString(LoginView.DMS_SHOP_LAT, "0"));
			userDTO.shopLng = Double.parseDouble(sharedPreferences.getString(LoginView.DMS_SHOP_LNG, "0"));
			userDTO.shopCode = sharedPreferences.getString(LoginView.DMS_SHOP_CODE, "");
			userDTO.checkAccessApp = sharedPreferences.getInt(LoginView.DMS_ACCESS_APP, -1);
			userDTO.mobilePhone = sharedPreferences.getString(LoginView.DMS_MOBILEPHONE, "");
			userDTO.sendLog = sharedPreferences.getInt(LoginView.DMS_SEND_LOG, -1);
			userDTO.displayName = sharedPreferences.getString(LoginView.DMS_DISPLAY_NAME, "");
			userDTO.salt = sharedPreferences.getString(LoginView.DMS_SALT, "");
		}catch (Exception e) {
			//start chuong trinh lan dau tien bi vang vi khong co thong tin user
			//VTLog.logToFile("GlobalInfo", DateUtils.now() + " tryGetUserData : " + e.toString());
			//VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return userDTO;
	}

	/**
	 * 
	 * set app co dang active hay ko
	 * 
	 * @author : DoanDM since : 10:12:47 AM
	 */
	public void setAppActive(boolean isActive) {

		this.isAppActive = isActive;
	}

	/**
	 * 
	 * get thong tin activte cua app
	 * 
	 * @author : DoanDM since : 10:12:58 AM
	 */
	public boolean isAppActive() {

		return isAppActive;
	}

	/**
	*  Cap nhat current tag
	*  @author: TruongHN
	*  @param tag
	*  @return: void
	*  @throws:
	 */
	public void setCurrentTag(String tag){
		this.listStackTag.add(tag);
		GlobalUtil.saveObject(this.listStackTag,DMS_CURRENT_TAG);
	}
	
	/**
	*  Lay tag hien tai 
	*  @author: TruongHN
	*  @return
	*  @return: String
	*  @throws:
	 */
	@SuppressWarnings("unchecked")
	public String getCurrentTag(){
		String currentTag = "";
		Object temp;
		if (this.listStackTag == null ) {
			if ((temp = GlobalUtil.readObject(
					DMS_CURRENT_TAG)) != null) {
				this.listStackTag = (ArrayList<String>) temp;
			}
		}
		if (this.listStackTag.size() > 0){
			currentTag = this.listStackTag.get(this.listStackTag.size() -1);
		}
		return currentTag;
	}
	
	/**
	*  Pop stack
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	public void popCurrentTag(){
		if (this.listStackTag.size() > 0){
			this.listStackTag.remove(this.listStackTag.size() -1);
			GlobalUtil.saveObject(this.listStackTag,DMS_CURRENT_TAG);
		}
	}
	
	/**
	*  Pop all stack
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	public void popAllTag(){
		this.listStackTag.clear();
		GlobalUtil.saveObject(this.listStackTag,DMS_CURRENT_TAG);
	}

	
	public Location getLoc() {
		return location;
	}

	public void setLoc(Location loc) {
		this.location = loc;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}	
	public void increaseCount() {
		this.count++;
	}
	
	public StatusNotificationHandler getStatusNotifier() {
		if (statusNotifier == null) {
			statusNotifier = new StatusNotificationHandler();
		}
		return statusNotifier;
	}

	public void setStatusNotification(StatusNotificationHandler vl) {
		statusNotifier = vl;
	}
	
	/**
	*  Add vi tri vao danh sach luu tru
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	@SuppressWarnings("unchecked")
	public void addPosition(StaffPositionLogDTO pos){
		Object temp;
		if (this.listPosition == null || listPosition.isEmpty()) {
			if ((temp = GlobalUtil.readObject(DMS_LIST_POSITION)) != null) {
				this.listPosition = (ArrayList<StaffPositionLogDTO>) temp;
			}
			
			if(listPosition == null) {
				this.listPosition = new ArrayList<StaffPositionLogDTO>();
			}
		}
		
		this.listPosition.add(pos);
		GlobalUtil.saveObject(this.listPosition,DMS_LIST_POSITION);
	}
	
	/**
	*  Lay ds vi tri luu tru offline
	*  @author: TruongHN
	*  @return: ArrayList<LatLng>
	*  @throws:
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<StaffPositionLogDTO> getListPositionOffline(){
		ArrayList<StaffPositionLogDTO> listPos = new ArrayList<StaffPositionLogDTO>();
		Object temp;
		if (this.listPosition == null || listPosition.isEmpty()) {
			if ((temp = GlobalUtil.readObject(DMS_LIST_POSITION)) != null) {
				this.listPosition = (ArrayList<StaffPositionLogDTO>) temp;
			}
			
			if(listPosition == null) {
				this.listPosition = new ArrayList<StaffPositionLogDTO>();
			}
		}
		if (this.listPosition.size() > 0){
			listPos = (ArrayList<StaffPositionLogDTO>)this.listPosition.clone();
		}
		return listPos;
	}
	
	/**
	* Reset lai list vi tri
	* @author: TruongHN
	* @return: void
	* @throws:
	 */
	public void clearListPosition(){
		if (this.listPosition != null){
			this.listPosition.clear();
			GlobalUtil.saveObject(this.listPosition,DMS_LIST_POSITION);
		}
	}
	
	/**
	 * set thoi gian tao hash map KPI
	 * @author: trungnt56
	 * @param: @param timeCreateHashMapKPI
	 * @return: void
	 * @throws:
	 */
	public void setTimeCreateHashMapKPI(Calendar timeCreateHashMapKPI) {
		GlobalUtil
				.saveObject(timeCreateHashMapKPI, VNM_TIME_CREATE_HASHMAP_KPI);
	}
	
	
	/**
	 * lay thoi gian tao hash map kpi lon nhat
	 * @author: trungnt56
	 * @param: @return
	 * @return: Calendar
	 * @throws:
	 */
	public Calendar getTimeCreateHashMapKPI() {
		Object temp;
		try {
			if ((temp = GlobalUtil.readObject(VNM_TIME_CREATE_HASHMAP_KPI)) != null) {
				return (Calendar) temp;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * lay hashmap Kpi
	 * @author: trungnt56
	 * @param: @return
	 * @return: SparseIntArray
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	public HashMap<Integer, Integer> getHashMapKPI() {
		Object temp;
		if (this.hashMapKPI == null) {
			try {
				if ((temp = GlobalUtil.readObject(VNM_HASHMAP_KPI)) != null) {
					this.hashMapKPI = (HashMap<Integer, Integer>) temp;
				} else {
					this.hashMapKPI = createHashMapKPI();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return this.hashMapKPI;
	}
	
	
	/**
	 * cap nhat hash map kpi key
	 * @author: trungnt56
	 * @param: @param key
	 * @param: @param value
	 * @return: void
	 * @throws:
	 */
	public void putHashMapKPI(int key, int value) {
		if (this.hashMapKPI != null) {
			this.hashMapKPI.put(key, value);
			GlobalUtil.saveObject(this.hashMapKPI, VNM_HASHMAP_KPI);
		}
	}

	/**
	 * reset hashmap kpi
	 * @author: trungnt56
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void resetHashMapKPI() {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			Calendar timeCreateHashMapKPI = getTimeCreateHashMapKPI();
			if (getHashMapKPI() == null || timeCreateHashMapKPI == null
					|| timeCreateHashMapKPI.compareTo(calendar) < 0) {
				setTimeCreateHashMapKPI(calendar);

				// tao hashmap
				this.hashMapKPI = createHashMapKPI();
				GlobalUtil.saveObject(this.hashMapKPI, VNM_HASHMAP_KPI);
			}
		} catch (Exception exception) {
			VTLog.e("trungnt", exception.getMessage());
		}
	}

	/**
	 * tao hashmapKPI
	 * @author: trungnt56
	 * @param: @return
	 * @return: HashMap<Integer,Integer>
	 * @throws:
	 */
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer, Integer> createHashMapKPI() {
		HashMap<Integer, Integer> hashMap = new HashMap<Integer, Integer>();
		for (HashMapKPI hashMapKPIEnum : HashMapKPI.values()) {
			hashMap.put(hashMapKPIEnum.ordinal(), 0);
		}
		return hashMap;
	}
	
	
	/**
	 * set so lan cho phep request ghi log KPI
	 * @author: trungnt56
	 * @param: @param allowRequestLogKPINumber
	 * @return: void
	 * @throws:
	 */
	public void setAllowRequestLogKPINumber(int allowRequestLogKPINumber) {
		this.allowRequestLogKPINumber = allowRequestLogKPINumber;
	}
	
	/**
	 * lay so lan cho phep request ghi log KPI
	 * @author: trungnt56
	 * @param: @return
	 * @return: int
	 * @throws:
	 */
	public int getAllowRequestLogKPINumber() {
		if (this.allowRequestLogKPINumber <= 0) {
			return 10;
		}
		return this.allowRequestLogKPINumber;
	}
	
	/**
	 * Add kpi log vao danh sach luu tru
	 * @author: trungnt56
	 * @param: @param kpiLog
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	public void addKPILog(KPILogDTO kpiLog) {
		Object temp;
		if (this.listKPILogOffline == null || listKPILogOffline.isEmpty()) {
			if ((temp = GlobalUtil.readObject(VNM_LIST_KPI_LOG)) != null) {
				this.listKPILogOffline = (ArrayList<KPILogDTO>) temp;
			}
			
			if(listKPILogOffline == null) {
				listKPILogOffline = new ArrayList<KPILogDTO>();
			}
		}
		
		this.listKPILogOffline.add(kpiLog);
		GlobalUtil.saveObject(this.listKPILogOffline, VNM_LIST_KPI_LOG);
	}
	
	/**
	 * lay danh sach luu tru log KPI offline
	 * @author: trungnt56
	 * @param: @return
	 * @return: ArrayList<KPILogDTO>
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<KPILogDTO> getListKPILogOffline() {
		ArrayList<KPILogDTO> listKPILog = new ArrayList<KPILogDTO>();
		Object temp;
		if (this.listKPILogOffline == null || listKPILogOffline.isEmpty()) {
			if ((temp = GlobalUtil.readObject(VNM_LIST_KPI_LOG)) != null) {
				this.listKPILogOffline = (ArrayList<KPILogDTO>) temp;
			}
			
			if(listKPILogOffline == null) {
				listKPILogOffline = new ArrayList<KPILogDTO>();
			}
		}
		if (this.listKPILogOffline.size() > 0) {
			listKPILog = (ArrayList<KPILogDTO>) this.listKPILogOffline.clone();
		}
		return listKPILog;
	}
	
	/**
	 * reset lai kpi log list offline
	 * @author: trungnt56
	 * @param:
	 * @return: void
	 * @throws:
	 */
	public void clearLogKPI(){
		if (this.listKPILogOffline != null){
			this.listKPILogOffline.clear();
			GlobalUtil.saveObject(this.listKPILogOffline,VNM_LIST_KPI_LOG);
		}
	}
	
		private RightTimeInfo rightTimeInfo = null;
		/**
		 * Lấy thông tin thời gian đúng gần nhất
		 * @author: duongdt3
		 * @since: 09:52:36 14 Jul 2014
		 * @return: RightTimeInfo
		 * @throws:  
		 * @return
		 */
		public RightTimeInfo getRightTimeInfo(){
			if (rightTimeInfo == null || StringUtil.isNullOrEmpty(rightTimeInfo.lastRightTime)) {
				rightTimeInfo = new RightTimeInfo();
				//get info from share
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getTimeSharePreference();
				rightTimeInfo.lastTimeOnlineLogin = sharedPreferences.getString(LoginView.DMS_LAST_LOGIN_ONLINE, "");
				rightTimeInfo.lastRightTime = sharedPreferences.getString(LoginView.DMS_LAST_RIGHT_TIME, "");
				rightTimeInfo.lastRightTimeSinceBoot = sharedPreferences.getLong(LoginView.DMS_LAST_LOGIN_ONLINE_FROM_BOOT, 0);
			}
			return rightTimeInfo;
		}
		
		/**
		 * Lưu thông tin thời gian đúng gần nhất
		 * @author: duongdt3
		 * @since: 09:52:49 14 Jul 2014
		 * @return: void
		 * @throws:  
		 * @param info
		 */
		public void setRightTimeInfo(RightTimeInfo info){
			if (info != null) {
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getTimeSharePreference();
				Editor prefsPrivateEditor = sharedPreferences.edit();
				prefsPrivateEditor.putString(LoginView.DMS_LAST_LOGIN_ONLINE, info.lastTimeOnlineLogin);
				prefsPrivateEditor.putString(LoginView.DMS_LAST_RIGHT_TIME, info.lastRightTime);
				prefsPrivateEditor.putLong(LoginView.DMS_LAST_LOGIN_ONLINE_FROM_BOOT, info.lastRightTimeSinceBoot);
				if(!prefsPrivateEditor.commit()){
					prefsPrivateEditor.apply();
				}
				
				//set lại rightTimeInfo = null, để reset lại dữ liệu mới
				rightTimeInfo = null;
			}
		}

	public String getCurrentStaffId() {
		return String.valueOf(getProfile().getUserData().id);
	}

	public static class RightTimeInfo {
			public String lastRightTime = "";
			public long lastRightTimeSinceBoot = 0;
			public String lastTimeOnlineLogin = "";
		}
		
		private static SharedPreferences sharedPreferencesPrivateDms = null;
		/**
		 * khoi tao mot lan cho file SharedPreferences
		 * @author: hoanpd1
		 * @since: 08:28:13 05-09-2014
		 * @return: SharedPreferences
		 * @throws:  
		 * @return
		 */
		public SharedPreferences getDmsPrivateSharePreference(){
			if (sharedPreferencesPrivateDms == null) {
				sharedPreferencesPrivateDms = new SecurePreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
			}
			return sharedPreferencesPrivateDms;
		}
		
		private static SharedPreferences sharedPreferencesTime = null;
		/**
		 * khoi tao mot lan cho file SharedPreferences
		 * @author: hoanpd1
		 * @since: 08:28:13 05-09-2014
		 * @return: SharedPreferences
		 * @throws:  
		 * @return
		 */
		public SharedPreferences getTimeSharePreference(){
			if (sharedPreferencesTime == null) {
				sharedPreferencesTime = new SecurePreferences(LoginView.DMS_RIGHT_TIME_INFO, Context.MODE_PRIVATE);
			}
			return sharedPreferencesTime;
		}
		
		public void setPositionCustomerVisiting(LatLng position){
			this.positionCustomerVisiting = position;
		}
		
		public LatLng getPositionCustomerVisiting(){
			//co vi tri tri thi tra ve vi tri ko thi tra null
			if (positionCustomerVisiting != null
					&& positionCustomerVisiting.lat > 0
					&& positionCustomerVisiting.lng > 0) {
				return positionCustomerVisiting;
			} else {
				return null;
			}
		}
		
		/**
		 * getStateConnectionMode
		 * @author: 
		 * @since: 16:57:16 10-04-2015
		 * @return: int
		 * @throws:  
		 * @return
		 */
		public int getStateConnectionMode() {
			if(stateConnectionMode == 0){
				SharedPreferences sharedPreferences = getDmsPrivateSharePreference();
				stateConnectionMode = sharedPreferences.getInt(
						LoginView.DMS_ALLOW_OFFLINE_MODE,
						Constants.CONNECTION_ONLINE);
			}
			return stateConnectionMode;
		}
		/**
		 * getStateConnectionMode
		 * @author: 
		 * @since: 16:57:25 10-04-2015
		 * @return: void
		 * @throws:  
		 * @param stateConnectionMode
		 */
		public void setStateConnectionMode(int stateConnectionMode) {
			this.stateConnectionMode = stateConnectionMode;
			// luu lai profile de auto login lan sau
			SharedPreferences sharedPreferences = getDmsPrivateSharePreference();
			Editor prefsPrivateEditor = sharedPreferences.edit();
			prefsPrivateEditor.putInt(LoginView.DMS_ALLOW_OFFLINE_MODE, stateConnectionMode);
			prefsPrivateEditor.commit();
		}
		
		/**
		 * set mang ung dung dc cai dat
		 * @author: Tuanlt11
		 * @param whiteList
		 * @return: void
		 * @throws:
		 */
		public void setWhiteList(ArrayList<String> whiteList) {
			this.whiteListAppInstall = whiteList;
			GlobalUtil.saveObject(this.whiteListAppInstall, DMS_WHITE_LIST);
		}

		/**
		 * Lay ds cac ung dung dc cai dat vao
		 * 
		 * @author: Tuanlt11
		 * @return
		 * @return: ArrayList<String>
		 * @throws:
		 */
		@SuppressWarnings("unchecked")
		public ArrayList<String> getWhiteList() {
			Object temp;
			if (this.whiteListAppInstall == null || whiteListAppInstall.isEmpty()) {
				if ((temp = GlobalUtil.readObject(DMS_WHITE_LIST)) != null) {
					this.whiteListAppInstall = (ArrayList<String>) temp;
				}

				if (whiteListAppInstall == null) {
					whiteListAppInstall = new ArrayList<String>();
				}
			}
			return this.whiteListAppInstall;
		}
		
		/**
		 * isHasAppFakeLocation
		 * @author: 
		 * @since: 14:29:43 15-04-2015
		 * @return: boolean
		 * @throws:  
		 * @return
		 */
		public boolean isHasAppFakeLocation(){
			return (isHasAppFakeLocation || GlobalUtil.isMockLocation());
		}
		/**
		 * set ds cac ung dung bi chan
		 * @author: Tuanlt11
		 * @return: void
		 * @throws:
		 */
		public void setBlackListAppBlocked(ArrayList<String> lstBlackAppPrevent) {
			this.blackListAppBlocked = lstBlackAppPrevent;
			GlobalUtil.saveObject(this.blackListAppBlocked,
					DMS_BLACK_LIST_APP_BLOCKED);
		}

		/**
		 * Lay ds cac ung dung bi chan
		 * @author: Tuanlt11
		 * @return
		 * @return: ArrayList<String>
		 * @throws:
		 */
		@SuppressWarnings("unchecked")
		public ArrayList<String> getBlackListAppBlocked() {
			Object temp;
			if (this.blackListAppBlocked == null || blackListAppBlocked.isEmpty()) {
				if ((temp = GlobalUtil.readObject(DMS_BLACK_LIST_APP_BLOCKED)) != null) {
					this.blackListAppBlocked = (ArrayList<String>) temp;
				}

				if (blackListAppBlocked == null) {
					blackListAppBlocked = new ArrayList<String>();
				}
			}
			return this.blackListAppBlocked;
		}
		/**
		 * set thoi gian lock lai ung dung
		 * @author: Tuanlt11
		 * @param timeLockAppPrevent
		 * @return: void
		 * @throws:
		 */
		public void setTimeLockAppBlocked(long timeLockAppPrevent) {
			this.timeLockAppBlocked = timeLockAppPrevent;
			GlobalUtil.saveObject(this.timeLockAppBlocked,
					DMS_TIME_LOCK_APP_BLOCKED);
		}

		/**
		 * lay thoi gian lock ung dung
		 * @author: Tuanlt11
		 * @return
		 * @return: long
		 * @throws:
		 */
		public long getTimeLockAppBlocked() {
			Object temp;
			try {
				if (this.timeLockAppBlocked <= 0) {
					if ((temp = GlobalUtil.readObject(DMS_TIME_LOCK_APP_BLOCKED)) != null) {
						this.timeLockAppBlocked = (Long) temp;
					}
				}
			} catch (Exception e) {}
			if (this.timeLockAppBlocked <= 0) {
				// mac dinh 5phut se khoa lai
				this.timeLockAppBlocked = 5;
			}
			return this.timeLockAppBlocked * 60 * 1000;
		}
		/**
		 * Id cua thiet bi
		 * @author: banghn
		 * @return
		 */
		public int getDeviceId() {
			Object temp;
			try {
				if (this.deviceId <= 0) {
					if ((temp = GlobalUtil.readObject(DMS_DEVICE_ID)) != null) {
						this.deviceId = (Integer) temp;
					}
				}
			} catch (Exception e) {
			}
			return this.deviceId;
		}

		public void setDeviceId(int deviceId) {
			this.deviceId = deviceId;
			GlobalUtil.saveObject(this.deviceId, DMS_DEVICE_ID);
		}
		
		/**
		 * Thoi gian sai lenh giua client va server
		 * @author: yennth16
		 * @since: 14:04:31 17-04-2015
		 * @return: void
		 * @throws:  
		 */
		public void setMaxIimeWrong(long maxIimeWrong) {
			this.maxIimeWrong = maxIimeWrong;
			GlobalUtil.saveObject(this.maxIimeWrong,DMS_TIME_SYNC_TO_SERVER);
		}
		
		/**
		 * Thoi gian sai lenh giua client va server
		 * @author: yennth16
		 * @since: 14:04:54 17-04-2015
		 * @return: long
		 * @throws:  
		 * @return
		 */
		public long getMaxIimeWrong() {
			Object temp;
			try{
				if (this.maxIimeWrong <= 0 ) {
					if ((temp = GlobalUtil.readObject(
							DMS_TIME_SYNC_TO_SERVER)) != null) {
						this.maxIimeWrong = (Long) temp;
					}
				}
			}catch (Exception e) {
				// TODO: handle exception
			}
			if (this.maxIimeWrong <= 0){
				this.maxIimeWrong = 50 * 60 * 1000;
			}
			return this.maxIimeWrong*60*1000;
		}
		/**
		 * get Interval Fused Position
		 * @author: duongdt3
		 * @since: 1.0
		 * @time: 11:01:44 18 Oct 2014
		 * @return: int Cấu hình chu kỳ gởi vị trí fused (phút)
		 * @throws:  
		 * @return
		 */
		public long getIntervalFusedPosition() {
			Object temp;
			try{
				if (this.intervalFusedPosition  <= 0 ) {
					if ((temp = GlobalUtil.readObject(
							VNM_INTERVAL_FUSED_POSITION)) != null) {
						this.intervalFusedPosition = (Long) temp;
					}
				}
			}catch (Exception e) {
			}
			if (this.intervalFusedPosition <= 0){
				this.intervalFusedPosition = 60; //mặc định là 60s
			}
			return this.intervalFusedPosition;
		}
		
		/**
		 * set Interval Fused Position
		 * @author: duongdt3
		 * @since: 10:42:21 12 Nov 2014
		 * @return: void
		 * @throws:  
		 * @param intervalFusedPosition
		 */
		public void setIntervalFusedPosition(long intervalFusedPosition) {
			this.intervalFusedPosition = intervalFusedPosition;
			GlobalUtil.saveObject(this.intervalFusedPosition, VNM_INTERVAL_FUSED_POSITION);
		}
		
		/**
		 * get Fast Interval Fused Position
		 * @author: duongdt3
		 * @since: 1.0
		 * @time: 11:01:51 18 Oct 2014
		 * @return: int Cấu hình chu kỳ ngắn nhất gởi vị trí fused (phút)
		 * @throws:  
		 * @return
		 */
		public long getFastIntervalFusedPosition() {
			Object temp;
			try{
				if (this.fastIntervalFusedPosition  <= 0 ) {
					if ((temp = GlobalUtil.readObject(
							VNM_FAST_INTERVAL_FUSED_POSITION)) != null) {
						this.fastIntervalFusedPosition = (Long) temp;
					}
				}
			}catch (Exception e) {
			}
			
			if (this.fastIntervalFusedPosition <= 0){
				this.fastIntervalFusedPosition = 10; //mặc định là 10s
			}
			return this.fastIntervalFusedPosition;
		}

		/**
		 * set Fast Interval Fused Position
		 * @author: duongdt3
		 * @since: 10:34:51 12 Nov 2014
		 * @return: void
		 * @throws:  
		 * @param fastIntervalFusedPosition
		 */
		public void setFastIntervalFusedPosition(long fastIntervalFusedPosition) {
			this.fastIntervalFusedPosition = fastIntervalFusedPosition;
			GlobalUtil.saveObject(this.fastIntervalFusedPosition, VNM_FAST_INTERVAL_FUSED_POSITION);
		}
		
		/**
		 * get Radius Fused Position
		 * @author: duongdt3
		 * @since: 10:34:18 12 Nov 2014
		 * @return: long bán kính sai số tối đa cho phép của fused provider
		 * @throws:  
		 * @return
		 */
		public long getRadiusFusedPosition() {
			Object temp;
			try{
				if (this.radiusFusedPosition  <= 0 ) {
					if ((temp = GlobalUtil.readObject(
							VNM_RADIUS_FUSED_POSITION)) != null) {
						this.radiusFusedPosition = (Long) temp;
					}
				}
			}catch (Exception e) {
			}
			
			if (this.radiusFusedPosition <= 0){
				this.radiusFusedPosition = 500; //mặc định là 500 mét 
			}
			return this.radiusFusedPosition;
		}
		
		/**
		 * set Radius Fused Position
		 * @author: duongdt3
		 * @since: 10:34:05 12 Nov 2014
		 * @return: void
		 * @throws:  
		 * @param radiusFusedPosition
		 */
		public void setRadiusFusedPosition(long radiusFusedPosition) {
			this.radiusFusedPosition = radiusFusedPosition;
			GlobalUtil.saveObject(this.radiusFusedPosition, VNM_RADIUS_FUSED_POSITION);
		}
		
		/**
		 * get fused Position Priority
		 * @author: duongdt3
		 * @since: 10:33:43 12 Nov 2014
		 * @return: long 1 là độ chính xác cao, 2 là độ chính xác cân bằng với pin
		 * @throws:  
		 * @return
		 */
		public long getFusedPositionPriority() {
			Object temp;
			try{
				if (this.fusedPositionPriority  <= 0 ) {
					if ((temp = GlobalUtil.readObject(
							VNM_FUSED_POSITION_PRIORITY)) != null) {
						this.fusedPositionPriority = (Long) temp;
					}
				}
			}catch (Exception e) {
			}
			
			if (this.fusedPositionPriority <= 0){
				this.fusedPositionPriority = 1; //mặc định là độ chính xác cao
			}
			return this.fusedPositionPriority;
		}
		
		/**
		 * set fused Position Priority
		 * @author: duongdt3
		 * @since: 10:33:27 12 Nov 2014
		 * @return: void
		 * @throws:  
		 * @param fusedPositionPriority
		 */
		public void setFusedPositionPriority(long fusedPositionPriority) {
			this.fusedPositionPriority = fusedPositionPriority;
			GlobalUtil.saveObject(this.fusedPositionPriority, VNM_FUSED_POSITION_PRIORITY);
		}
		/**
		 * Thoi gian day du lieu qua khu len server
		 * getTimeCheckToServer
		 * @author: yennth16
		 * @since: 13:54:40 02-04-2015
		 * @return: int
		 * @throws:  
		 * @return
		 */
		public int getTimeCheckToServer() {
			Object temp;
			try {
				if (this.timeCheckToServer <= 0) {
					if ((temp = GlobalUtil.readObject(DMS_TIME_CHECK_TO_SERVER)) != null) {
						this.timeCheckToServer = (Integer) temp;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (this.timeCheckToServer <= 0) {
				this.timeCheckToServer = 0;
			}
			return this.timeCheckToServer;
		}

		/**
		 * Thoi gian day du lieu qua khu len server
		 * @author: yennth16
		 * @since: 13:55:02 02-04-2015
		 * @return: void
		 * @throws:  
		 * @param timeCheckToServer
		 */
		public void setTimeCheckToServer(int timeCheckToServer) {
			this.timeCheckToServer = timeCheckToServer;
			GlobalUtil.saveObject(this.timeCheckToServer, DMS_TIME_CHECK_TO_SERVER);
		}
		
		IDataValidate<Integer> intConfigValidation = new IDataValidate<Integer>() {
			@Override
			public boolean valid(Integer ob) {
				boolean isValid = (ob != null && ob > 0);
				return isValid;
			}
		};
		/**
		 * @return the sysDecimalPoint
		 */
		public int getSysDecimalPoint() {
			if (!intConfigValidation.valid(this.sysDecimalPoint)) {
				//mặc định là 2 dấu chấm
				this.sysDecimalPoint = 2;
			}
			return sysDecimalPoint;
		}

		/**
		 * @param sysDecimalPoint the sysDecimalPoint to set
		 * @param status 
		 */
		public void setSysDecimalPoint(int sysDecimalPoint, int status) {
			this.sysDecimalPoint = status != 0 ? sysDecimalPoint : 2;
			GlobalUtil.saveObject(this.sysDecimalPoint, VNM_SYS_DECIMAL_POINT);
		}
		IDataValidate<Integer> intNotNegativeConfigValidation = new IDataValidate<Integer>() {
			@Override
			public boolean valid(Integer ob) {
				boolean isValid = (ob != null && ob >= 0);
				return isValid;
			}
		};
		/**
		 * @return the sysDigitDecimal
		 */
		public int getSysDigitDecimal() {
			if (!intNotNegativeConfigValidation.valid(this.sysDigitDecimal)) {
				//mặc định là 2 số
				this.sysDigitDecimal = 2;
			}
			return sysDigitDecimal;
		}

	/**
	 * set validate mock location
	 * @author: duongdt3
	 * @since: 13:49:45 11 Feb 2015
	 * @return: void
	 * @throws:
	 * @param flagValidateLocation 1 not validate, 2 validate is mock location
	 */
	public void setFlagValidateMockLocation(long flagValidateLocation) {
		this.flagValidateLocation = flagValidateLocation;
		GlobalUtil.saveObject(this.flagValidateLocation, VNM_FLAG_VALIDATE_LOCATION);
	}

	/**
	 * Vận tốc di chuyển chuẩn của xe máy (Km/h)
	 * getTimeCheckToServer
	 * @author: yennth16
	 * @return: int
	 * @throws:
	 * @return
	 */
	public int getLocationSpeed() {
		Object temp;
		try {
			if (this.locationSpeed <= 0) {
				if ((temp = GlobalUtil.readObject(DMS_LOCATION_SPEED)) != null) {
					this.locationSpeed = (Integer) temp;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		if (this.locationSpeed <= 0) {
			this.locationSpeed = 40;
		}
		return this.locationSpeed;
	}

	/**
	 * Vận tốc di chuyển chuẩn của xe máy (Km/h)
	 * @author: yennth16
	 * @return: void
	 * @throws:
	 * @param locationSpeed
	 */
	public void setLocationSpeed(int locationSpeed) {
		this.locationSpeed = locationSpeed;
		GlobalUtil.saveObject(this.locationSpeed, DMS_LOCATION_SPEED);
	}

	/**
	 * Luu thong tin status trang thai vi tri hop le
	 *
	 * @author : yennth16
	 */
	public static void saveStatusPosition(int status) {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		prefsPrivateEditor.putInt(LoginView.DMS_POSITION_STATUS, status);
		prefsPrivateEditor.commit();
	}

	/**
	 * Luu thong tin lat, lng, date - tinh vi tri hop le
	 *
	 * @author : yennth16
	 */
	public static void saveStatusPosition(double lat, double lng, String date) {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		prefsPrivateEditor.putString(LoginView.DMS_POSITION_LAT, Constants.STR_BLANK + lat);
		prefsPrivateEditor.putString(LoginView.DMS_POSITION_LNG, Constants.STR_BLANK + lng);
		prefsPrivateEditor.putString(LoginView.DMS_POSITION_DATE, date);
		prefsPrivateEditor.commit();
	}


	/**
	 * is Vaildate Location Mocklocation
	 * @author: duongdt3
	 * @since: 08:56:46 11 Feb 2015
	 * @return: boolean
	 * @throws:
	 * @return
	 */
	public boolean isVaildateMockLocation() {
		try{
			if (this.flagValidateLocation <= 0 ) {
				Object temp;
				if ((temp = GlobalUtil.readObject(
						VNM_FLAG_VALIDATE_LOCATION)) != null) {
					this.flagValidateLocation = (Long) temp;
				}
			}
		}catch (Exception e) {
		}

		if (this.flagValidateLocation <= 0){
			this.flagValidateLocation = 1; //mặc định là không validate
		}
		return (this.flagValidateLocation == 2);
	}

	public boolean isVtMapSetServer() {
		return vtMapSetServer;
	}

	public String getVtMapProtocol() {
		return vtMapProtocol;
	}

	public String getVtMapIP() {
		return vtMapIP;
	}

	public int getVtMapPort() {
		return vtMapPort;
	}

	public void setVtMapPort(int vtMapPort) {
		this.vtMapPort = vtMapPort;
	}

	public void setVtMapIP(String vtMapIP) {
		this.vtMapIP = vtMapIP;
	}

	public void setVtMapProtocol(String vtMapProtocol) {
		this.vtMapProtocol = vtMapProtocol;
	}

	public void setVtMapSetServer(boolean vtMapSetServer) {
		this.vtMapSetServer = vtMapSetServer;
	}
	/**
	 * lay hien thi bao nhieu chu so phan thap phan
	 * @return
	 */
	public int getCfNumFloat() {
		Object temp;
		try {
			if ((temp = VTGlobalUtils.readObject(instance
					.getAppContext(), CF_NUM_FLOAT)) != null) {
				this.cfNumFloat = Integer.parseInt(temp.toString());
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return this.cfNumFloat;
	}

	/**
	 * gan hien thi bao nhieu chu so phan thap phan
	 * @param cfNumFloat
	 */
	public void setCfNumFloat(String cfNumFloat) {
		if (this.cfNumFloat != getIntegerValue(cfNumFloat, 2)) {
			this.cfNumFloat = getIntegerValue(cfNumFloat, 2);
			VTGlobalUtils.saveObject(instance.getAppContext(),
					this.cfNumFloat, CF_NUM_FLOAT);
		}
	}
	/**
	 * parse int tu string, neu ko tra ve gia tri mac dinh
	 * @return: int
	 * @throws:
	 * @param value
	 * @param valueDefault
	 * @return
	 */
	private int getIntegerValue(String value, int valueDefault) {
		try {
			if (!VTStringUtils.isNullOrEmpty(value)) {
				return Integer.parseInt(value);
			}
		} catch (Exception ex) {
			// truong hop ko phai gia tri kieu int
		}
		return valueDefault;
	}
	/**
	 * Số ngày cho phép đẩy đơn hàng PG
	 * @return
	 */
	public int getPGNumberSendOrder() {
		Object temp;
		try {
			if (this.pgNumSendOrder <= 0) {
				if ((temp = GlobalUtil.readObject(PG_SEND_ORDER)) != null) {
					this.pgNumSendOrder = (Integer) temp;
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return this.pgNumSendOrder;
	}

	/**
	 * Số ngày cho phép đẩy đơn hàng PG
	 */
	public void setPGNumberSendOrder(int pgNumSendOrder) {
		this.pgNumSendOrder = pgNumSendOrder;
		GlobalUtil.saveObject(this.pgNumSendOrder, PG_SEND_ORDER);
	}

	/**
	 * add danh sach san pham pg
     */
	public void setListProduct(ArrayList<PGOrderViewDTO> listProduct, boolean isReset){
		if (this.listProduct == null || this.listProduct.isEmpty()) {
			Object temp;
			if ((temp = GlobalUtil.readObject(PG_LIST_PRODUCT)) != null) {
				this.listProduct = (ArrayList<PGOrderViewDTO>) temp;
			}
		}
		if(isReset || listProduct.size() == 0){
			this.listProduct = new ArrayList<>();
		}
		if(listProduct.size() > 0) {
			this.listProduct = listProduct;
		}
		GlobalUtil.saveObject(this.listProduct,PG_LIST_PRODUCT);
	}

	/**
	 * Lay danh san pham pg
	 * @return
     */
	public ArrayList<PGOrderViewDTO> getListProduct(){
		ArrayList<PGOrderViewDTO> listPos = new ArrayList<PGOrderViewDTO>();
		Object temp;
		if (this.listProduct == null || this.listProduct.isEmpty()) {
			if ((temp = GlobalUtil.readObject(PG_LIST_PRODUCT)) != null) {
				this.listProduct = (ArrayList<PGOrderViewDTO>) temp;
			}
		}
		if (this.listProduct.size() > 0){
			listPos = (ArrayList<PGOrderViewDTO>)this.listProduct.clone();
		}
		return listPos;
	}

	/**
	 * Email send data
	 * @param data
     */
	public void setEmailSendData(String data) {
		this.emailSendData = data;
		GlobalUtil.saveObject(this.emailSendData, EMAIL_SEND_DATA);
	}
	/**
	 * Email send data
	 */
	public String getEmailSendData() {
		Object temp;
		try {
			if (!StringUtil.isNullOrEmpty(this.emailSendData)) {
				if ((temp = GlobalUtil.readObject(EMAIL_SEND_DATA)) != null) {
					this.emailSendData = (String) temp;
				}
			}else{
				this.emailSendData = "dmsviettel@gmail.com";
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return this.emailSendData;
	}

	/**
	 * add danh sach tat ca san pham pg
	 */
	public void setListAllProduct(ArrayList<PGOrderViewDTO> listProduct){
		if (this.listAllProduct == null || this.listAllProduct.isEmpty()) {
			Object temp;
			if ((temp = GlobalUtil.readObject(PG_LIST_ALL_PRODUCT)) != null) {
				this.listAllProduct = (ArrayList<PGOrderViewDTO>) temp;
			}
		}
		if(listProduct.size() == 0){
			this.listAllProduct = new ArrayList<>();
		}
		if(listProduct.size() > 0) {
			this.listAllProduct = listProduct;
		}
		GlobalUtil.saveObject(this.listAllProduct,PG_LIST_ALL_PRODUCT);
	}

	/**
	 * Lay danh tat ca san pham pg
	 * @return
	 */
	public ArrayList<PGOrderViewDTO> getListAllProduct(){
		ArrayList<PGOrderViewDTO> listPos = new ArrayList<PGOrderViewDTO>();
		Object temp;
		if (this.listAllProduct == null || this.listAllProduct.isEmpty()) {
			if ((temp = GlobalUtil.readObject(PG_LIST_ALL_PRODUCT)) != null) {
				this.listAllProduct = (ArrayList<PGOrderViewDTO>) temp;
			}
		}
		if (this.listAllProduct.size() > 0){
			listPos = (ArrayList<PGOrderViewDTO>)this.listAllProduct.clone();
		}
		return listPos;
	}

	/**
	 * Loại ma hoa password
	 * yennth16
	 * @param data
	 */
	public void setTypeHashPass(String data) {
		this.typeHashPass = data;
		GlobalUtil.saveObject(this.typeHashPass, TYPE_HASH_PASS);
	}
	/**
	 * Loại ma hoa password
	 * yennth16
	 */
	public String getTypeHashPass() {
		Object temp;
		try {
			if (!StringUtil.isNullOrEmpty(this.typeHashPass)) {
				if ((temp = GlobalUtil.readObject(TYPE_HASH_PASS)) != null) {
					this.typeHashPass = (String) temp;
				}
			}else{
				this.typeHashPass = "MD5";
			}
		} catch (Exception e) {
			this.typeHashPass = "MD5";
		}
		return this.typeHashPass;
	}
	/**
	 * Thuc hien day log vi tri vao tablet action log
	 * yennth16
	 */
	public boolean isSendLogPosition() {
		boolean result = false;
		if(GlobalInfo.getInstance().getProfile().getUserData().sendLog == UserDTO.SEND_LOG_ALL
				|| GlobalInfo.getInstance().getProfile().getUserData().sendLog == UserDTO.SEND_LOG_POSITION){
			result = true;
		}
		return result;
	}
}
