/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.commonsware.cwac.cache.HashMapManager;
import com.commonsware.cwac.cache.MemoryUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SynDataController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.DownloadFile;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.DBVersionDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.syndata.SynDataDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.GlobalInfo.RightTimeInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.SqlCipherUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.TransactionProcessManager;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.maps.util.AppInfo;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.utils.VTStringUtils;
import com.viettel.viettellib.network.http.HTTPClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

/**
 * Hien thi man hinh login
 *
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class LoginView extends GlobalBaseActivity implements OnClickListener {
	public static final String PREFS_PRIVATE = "com.viettel.sabeco";
	public static final String DMS_USER_NAME = "com.viettel.dms.username";
	public static final String DMS_USER_AUTH_DATA = "com.viettel.dms.auth";
	public static final String DMS_PASSWORD = "com.viettel.dms.password";
	public static final String DMS_ID = "com.viettel.dms.id";
	public static final String DMS_SHOP_ID = "com.viettel.dms.shopId";
	public static final String DMS_SHOP_ID_PROFILE = "com.viettel.dms.shopIdProfile";
	public static final String DMS_LAST_SHOP_LOGIN = "com.viettel.dms.lastShopLogin";
	public static final String DMS_VER_DB = "com.viettel.dms.verdb";
	public static final String DMS_SV_DATE = "com.viettel.dms.getdate";
	private static final String DMS_STATUS_DOWNLOAD_DB = "com.viettel.dms.statusdb";
	public static final String MAX_LAST_LOG_ID = "com.viettel.dms.maxDBLogId";
	public static final String DMS_SHOP_LAT = "com.viettel.dms.shopLat";
	public static final String DMS_SHOP_LNG = "com.viettel.dms.shopLng";
	public static final String DMS_SHOP_CODE = "com.viettel.dms.shopCode";
	public static final String DMS_LIST_SHOP = "com.viettel.dms.listShop";
	public static final String DMS_SHOP_MANAGED = "com.viettel.dms.shopManaged";

	// public static final String VNM_ROLE_TYPE =
	// "com.viettel.vinamilk.roleType";
	public static final String DMS_CHANEL_TYPE = "com.viettel.dms.chanelType";
	public static final String DMS_CHANEL_OBJECT_TYPE = "com.viettel.dms.chanelObjectType";
	public static final String DMS_STAFF_OWNER_ID = "com.viettel.dms.staffOwnerId";
	public static final String DMS_STAFF_CODE = "com.viettel.dms.staffCode";
	public static final String DMS_SALE_TYPE_CODE = "com.viettel.dms.saleTypeCode";
	public static final String DMS_SERVER_DATE_SYNDATA = "com.viettel.dms.serverdateSynData";
	public static final String DMS_MD5_CHECKSUM_DB = "com.viettel.dms.md5ChecksumDB";
	public static final String DMS_IGNORE_MSG_TAKE_PICTURE_DP = "com.viettel.dms.ignoreTakePicture";
	public static final String DMS_VER_AP = "com.viettel.dms.verAp";
	public static final String DMS_OLD_VERSION = "com.viettel.sabeco.oldVersion";
	//phục vụ việc kiểm tra thời gian
	public static final String DMS_LAST_LOGIN_ONLINE = "com.viettel.sabeco.lastLoginOnline";
	public static final String DMS_LAST_LOGIN_ONLINE_FROM_BOOT = "com.viettel.sabeco.lastLoginOnlineSinceBoot";
	public static final String DMS_LAST_RIGHT_TIME = "com.viettel.sabeco.lastRightTime";
	public static final String DMS_RIGHT_TIME_INFO = "com.viettel.sabeco.right_time_info";
	public static final String DMS_ALLOW_OFFLINE_MODE = "com.viettel.sabeco.allowConnectionOffline";
	public static final String DMS_ACCESS_APP = "com.viettel.sabeco.checkAccessApp";
	// TINH VI TRI HOP LE
	public static final String DMS_POSITION_STATUS = "com.viettel.sabeco.status";
	public static final String DMS_POSITION_LAT = "com.viettel.sabeco.lat";
	public static final String DMS_POSITION_LNG = "com.viettel.sabeco.lng";
	public static final String DMS_POSITION_DATE = "com.viettel.sabeco.date";
	public static final String DMS_MOBILEPHONE = "com.viettel.sabeco.mobilePhone";
	public static final String DMS_SEND_LOG = "com.viettel.sabeco.sendLog";
	public static final String DMS_DISPLAY_NAME = "com.viettel.sabeco.displayName";
	public static final String DMS_SALT = "com.viettel.dms.salt";

	private final int ACTION_UPDATE_VERSION_SUCCESS = 1;
	private final int ACTION_UPDATE_VERSION_FAIL = 2;
	// download db
	private final int ACTION_DOWNLOAD_DB_OK = 3;
	private final int ACTION_DOWNLOAD_DB_CANCEL = 4;
	// update DB
	private final int ACTION_UPDATE_DB_OK = 101;
	private final int ACTION_UPDATE_DB_CANCEL = 102;
	// tai lieu du lieu moi
	private final int ACTION_RE_DOWNLOAD_DB_OK = 103;
	private final int ACTION_RE_DOWNLOAD_DB_CANCEL = 104;
	// cancle login
	private final int ACTION_CANCEL_LOGIN_OK = 105;
	// action sau khi chon NPP quan ly
	private final int ACTION_SELECTED_SHOP_MANAGE = 106;
	//proccess type when start download native lib
	private static final int PROCCESS_LOGIN_ONLINE = 107;
	private static final int PROCCESS_UPDATE_VERSION_FAIL = 108;
	private static final int PROCCESS_LOGIN_OFFLINE = 109;
	private static final int PROCCESS_NEED_UPDATE_VERSION = 110;
	private static final int PROCCESS_LOGIN_OFFLINE_WRONG_TIME = 111;
	// xoa du lieu ung dung
	private final int ACTION_CLEAR_DATA_OK = 112;
	private final int ACTION_CLEAR_DATA_CANCEL = 113;

	RelativeLayout rlMain;// view root
	EditText edUserName;// control nhap ten
	EditText edPassword;// control nhap pass
	Button btLogin; // button login
	private int stateExistsDB = 0;// ton tai file DB?
	LinearLayout llMenu;// layout header icon
	private UserDTO userDTO;// staff login
	String passMD5;// pass luu tru sau khi login thanh cong
	// dang dong bo du lieu
	private boolean isRequestSynData = false;
	// dang login
	private boolean isRequestLogin = false;
	// dialog hien thi danh sach NPP
	private AlertDialog alertListShopDialog;

	// dung tinh % hoan thanh dong bo
	int percentSynData = 0;
	//check số lần lỗi download native
	private int numErrorNativeLib = 0;
	//dang download native libs
	public boolean isDownloadNativeLibs = false;
	//dang download new version
	public boolean isDownloadNewVersion = false;
	//dang download db
	public boolean isDownloadDB = false;
	// download link db
	boolean isRequestDBLink = false;
	// syndata truoc khi update version
	private boolean isRequestSynDataBeforeUpVersion = false;
	// kiem tra update version
	private boolean isNeedUpdateVersionGlobal = false;
	CheckBox cbAllowOffline;// cho phep dang nhap offline
	TextView tvLoginOtherUser; // dang nhap tai khoan khac
	public long startTimeFromBoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (!GlobalUtil.checkIsTablet(this)) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
			// Activity was brought to front and not created,
			// Thus finishing this will get us to the last viewed activity
			VTLog.i("Login", "loginview onCreate:" + getIntent().getFlags() + this.getClass().getName());
			finish();
			return;
		}
		setContentView(R.layout.layout_login);
		setTitleName("Bia SG");
		llMenu = (LinearLayout) findViewById(R.id.llMenu);
		rlMain = (RelativeLayout) findViewById(R.id.rlMain);
		btLogin = (Button) findViewById(R.id.btLogin);
		btLogin.setOnClickListener(this);
		edUserName = (EditText) findViewById(R.id.edUserName);
		edPassword = (EditText) findViewById(R.id.edPassword);
		cbAllowOffline = (CheckBox) findViewById(R.id.cbExportOrder);
		// edPassword.setImeOptions(EditorInfo.IME_ACTION_DONE);
		// edPassword.setImeActionLabel("Login", EditorInfo.IME_ACTION_DONE);

		GlobalUtil.setEditTextMaxLength(edUserName, 10);
		GlobalUtil.setEditTextMaxLength(edPassword, 30);

		edUserName.setText("0000011717");// 0000011717
		edPassword.setText("123456");// 123456

		// set text menu & icon left gone
		TextView tvTitleMenu = (TextView) findViewById(R.id.tvTitleMenu);
		ImageView iconLeft = (ImageView) llShowHideMenu.findViewById(R.id.ivLeftIcon);
		iconLeft.setVisibility(View.GONE);
		tvTitleMenu.setVisibility(View.GONE);
		tvLoginOtherUser = (TextView) findViewById(R.id.tvLoginOtherUser);
		tvLoginOtherUser.setOnClickListener(this);

		init();

		rlMain.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				GlobalUtil.forceHideKeyboard(LoginView.this);
				return false;
			}
		});

		// set hash map kpi
		GlobalInfo.getInstance().resetHashMapKPI();

		// showToastMessage("Dung luong bo nho ngoai con trong : " +
		// ExternalStorage.megabytesAvailableOnDisk());
		// dung luong nho hon 500MB canh bao
		if (ExternalStorage.megabytesAvailableOnDisk() < 500) {
			showDialog(StringUtil.getString(R.string.ERR_FULL_DISK));
		}
	}

	String a = "";

	private void init() {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		String verDB = sharedPreferences.getString(DMS_VER_DB, "0.0.0");
		String userName = sharedPreferences.getString(DMS_USER_NAME, "");
		passMD5 = sharedPreferences.getString(DMS_PASSWORD, "");

		beginLogId = Long.parseLong(sharedPreferences.getString(LAST_LOG_ID, "0"));
		if (!StringUtil.isNullOrEmpty(userName)) {
//			ServerLogger.sendLogLogin("init"
//					, "NV da dang nhap get name tu sharepref: " + sharedPreferences.getString(DMS_USER_NAME, "")
//					, TabletActionLogDTO.LOG_LOGIN);
			// khong cho edit ten dang nhap khi da co thong tin user dang nhap
			edUserName.setText(userName);
			edUserName.setEnabled(false);
			edUserName.setBackgroundResource(R.drawable.bg_edittext_login_disable);
			edUserName.setPadding(GlobalUtil.dip2Pixel(10), GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5));
			edPassword.requestFocus();
		} else {
			//ServerLogger.sendLogLogin("init", "Ko co userName trong sharepref", TabletActionLogDTO.LOG_LOGIN);
			edUserName.setEnabled(true);
			edUserName.requestFocus();
		}

		VTLog.i("Login", "Init get ver DB: " + verDB);
		GlobalInfo.getInstance().getProfile().setVersionDB(verDB);

		// header
		RelativeLayout.LayoutParams llMenuParam = new RelativeLayout.LayoutParams(GlobalUtil.dip2Pixel(50), LayoutParams.WRAP_CONTENT);
		llMenu.setLayoutParams(llMenuParam);

		// lay thong tin version
		try {
			GlobalInfo.getInstance().getProfile().setVersionApp(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
		} catch (NameNotFoundException e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		String verInfo = "";

		Date date = apkUpdateTime();
		String dateApp = DateUtils.convertDateTimeWithFormat(date, "dd/MM/yyy HH:mm");

		if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getVersionApp())) {
			verInfo = StringUtil.getString(R.string.TEXT_VERSION_INFO_1)
					+ GlobalInfo.getInstance().getProfile().getVersionApp()
					+ "." + GlobalUtil.getServerType();
		}
		if (!StringUtil.isNullOrEmpty(dateApp)) {
			verInfo += StringUtil.getString(R.string.TEXT_VERSION_INFO_2)
					+ dateApp;
		}
		setStatusVisible(verInfo, View.VISIBLE);

		// ghi log server deploy
		try {
			VTLog.logToFileInReleaseMode("ModeServer", DateUtils.now()
					+ " - Build Server : " + GlobalUtil.getServerType());
		} catch (Exception e) {
		}
		//mac dinh trang thai la login co ket noi
		GlobalInfo.getInstance().setStateConnectionMode(Constants.CONNECTION_ONLINE);
	}

	/**
	 * Lay ngay cap nhat app
	 *
	 * @author : BangHN since : 1.0
	 */
	private Date apkUpdateTime() {
		PackageManager packageManager = getPackageManager();
		String packageName = getPackageName();
		Date date = null;
		try {
			ApplicationInfo appInfo = packageManager.getApplicationInfo(packageName, 0);
			String appFile = appInfo.sourceDir;
			Long installed = new File(appFile).lastModified();
			date = new Date(installed);
		} catch (NameNotFoundException e) {
			return null; // package not found
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		}
		// field wasn't found
		return date;
	}

	/**
	 * validate thong tin dang nhap
	 *
	 * @author : BangHN since : 1.0
	 */
	private boolean isValidateInput(String userName, String pass) {
		if (StringUtil.isNullOrEmpty(userName)) {
			showDialog(StringUtil.getString(R.string.PLS_INPUT_US_NAME));
			return false;
		} else if (StringUtil.isNullOrEmpty(pass)) {
			showDialog(StringUtil.getString(R.string.PLS_INPUT_PASSWORD));
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btLogin) {
			startTimeFromBoot = SystemClock.elapsedRealtime();

			HTTPClient.sessionID = null;
			// cancel luồng đồng bộ, để login có thể đồng bộ được bình thường
			TransactionProcessManager.getInstance().cancelTimer();

			percentSynData = 0;
			isRequestSynData = false;
			GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
			String userName = edUserName.getText().toString().trim();
			String pass = edPassword.getText().toString().trim();
			String fullName = StringUtil.generateFullUserName(userName);
			edUserName.setText(fullName);
			stateExistsDB = GlobalUtil.checkExistsDataBase();
			if(stateExistsDB == 0){
				GlobalInfo.getInstance().getProfile().setUserData(new UserDTO());
			}
//			try {
//				pass = StringUtil.generateHashSHA256(pass, GlobalInfo.getInstance().getProfile().getUserData().salt.toLowerCase(),
//						GlobalInfo.getInstance().getTypeHashPass());
//			} catch (NoSuchAlgorithmException e) {
////				// TODO Auto-generated catch block
////				VTLog.e("LoginView-onClick: ", e.getMessage());
//			} catch (UnsupportedEncodingException e) {
////				// TODO Auto-generated catch block
////				VTLog.e("LoginView-onClick: ", e.getMessage());
//			}
			if (isValidateInput(userName, pass)) {//
				// Khong bat GPS
				if (!PositionManager.getInstance().isEnableGPS()) {
					GlobalUtil.showDialogSettingGPS();
				} else {
					showProgressDialog(StringUtil.getString(R.string.loading));
//					ServerLogger.sendLogLogin("onClick", "Dang nhap", TabletActionLogDTO.LOG_LOGIN);
					requestLogin(fullName, pass);
				}
			}
			// kiem tra start lai dinh vi khi thoat ra man hinh login
			if (!PositionManager.getInstance().getIsStart()) {
				PositionManager.getInstance().start();
			}
			GlobalUtil.forceHideKeyboard(this);
		} else if (v == tvLoginOtherUser) {
			try {
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				String userName = sharedPreferences.getString(DMS_USER_NAME, "");
				if (!StringUtil.isNullOrEmpty(userName)) {
					clearData();
				}
			} catch (Exception e) {
				closeProgressDialog();
				if (!isFinishing()) {
					showDialog(VTStringUtils.getString(getApplicationContext(), R.string.MESSAGE_ERROR_DELETE_DB));
				}
			}
		}
	}

	@Override
	protected void onResume() {
		// check sum md5
		// new VNMDBChecksum().execute("");
		showWarning(false);
		resetSavedDataForLogout(false);
		super.onResume();
	}

	/**
	 * Reset du lieu truoc khi log out
	 *
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void resetSavedDataForLogout(boolean isRememberAfterLogin) {
		MemoryUtils.setNullInstance();
		HashMapManager.setNullInstance();
	}

	/**
	 * Luu thong tin dang nhap
	 *
	 * @author : BangHN since : 9:01:36 AM
	 */
	private void saveUserInfo(UserDTO userDTO) {
		GlobalInfo.getInstance().getProfile().setUserData(userDTO);
		// luu lai profile de auto login lan sau
		// SharedPreferences sharedPreferences =
		// getSharedPreferences(LoginView.PREFS_PRIVATE, Context.MODE_PRIVATE);
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		String userName = edUserName.getText().toString().trim();
		String pass = edPassword.getText().toString().trim();
		if (!StringUtil.isNullOrEmpty(userName))
			prefsPrivateEditor.putString(DMS_USER_NAME, userName);
		if (!StringUtil.isNullOrEmpty(pass))
			prefsPrivateEditor.putString(DMS_PASSWORD, userDTO.pass);
		if (userDTO.id > 0)
			prefsPrivateEditor.putLong(DMS_ID, userDTO.id);
		if (!StringUtil.isNullOrEmpty(userDTO.shopId)) {
			prefsPrivateEditor.putString(DMS_SHOP_ID, userDTO.shopId);
		}
		if (!StringUtil.isNullOrEmpty(userDTO.shopIdProfile)) {
			prefsPrivateEditor.putString(DMS_SHOP_ID_PROFILE, userDTO.shopIdProfile);
		}
		prefsPrivateEditor.putString(DMS_STAFF_CODE, userDTO.userCode);
		prefsPrivateEditor.putString(DMS_LIST_SHOP, new Gson().toJson(userDTO.listShop));
		prefsPrivateEditor.putString(DMS_SHOP_MANAGED, new Gson().toJson(userDTO.shopManaged));
//		Type type = new TypeToken<ArrayList<ShopDTO>>() {}.getType();
//		new Gson().fromJson("", type);
		prefsPrivateEditor.putInt(DMS_CHANEL_TYPE, userDTO.chanelType);
		prefsPrivateEditor.putInt(DMS_CHANEL_OBJECT_TYPE, userDTO.chanelObjectType);
		prefsPrivateEditor.putLong(DMS_STAFF_OWNER_ID, userDTO.staffOwnerId);
		prefsPrivateEditor.putString(DMS_SALE_TYPE_CODE, userDTO.saleTypeCode);
		prefsPrivateEditor.putString(DMS_SHOP_LAT, Double.toString(userDTO.shopLat));
		prefsPrivateEditor.putString(DMS_SHOP_LNG, Double.toString(userDTO.shopLng));
		prefsPrivateEditor.putString(DMS_SV_DATE, DateUtils.now());
		prefsPrivateEditor.putString(DMS_SHOP_CODE, userDTO.shopCode);
		//prefsPrivateEditor.putString(DMS_SV_DATE, DateUtils.now());
		prefsPrivateEditor.putInt(DMS_ACCESS_APP, userDTO.checkAccessApp);
		prefsPrivateEditor.putString(DMS_MOBILEPHONE, userDTO.mobilePhone);
		prefsPrivateEditor.putInt(DMS_SEND_LOG, userDTO.sendLog);
		prefsPrivateEditor.putString(DMS_DISPLAY_NAME, userDTO.displayName);
		if (!StringUtil.isNullOrEmpty(userDTO.salt)) {
			prefsPrivateEditor.putString(DMS_SALT, userDTO.salt);
		}
		prefsPrivateEditor.commit();
		// disable user name sau khi dang nhap thanh cong
		edUserName.setEnabled(false);
		edUserName.setBackgroundResource(R.drawable.bg_edittext_login_disable);
		edUserName.setPadding(GlobalUtil.dip2Pixel(10), GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5));
	}

	/**
	 * Luu thong tin version db, sau khi download db thanh cong
	 *
	 * @author : BangHN since : 1.0
	 */
	private void saveDBVersionInfo() {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		if (StringUtil.isNullOrEmpty(userDTO.dbVersion.version)) {
			userDTO.dbVersion.version = "0.0.0";
		}
		// file db tai ve co lastlogid = maxdblog hien thoi
		if (userDTO.dbVersion.lastLogId.equals(userDTO.dbVersion.maxDBLogId)) {
			prefsPrivateEditor.putString(DMS_STATUS_DOWNLOAD_DB, SynDataDTO.UPDATE_TO_DATE);
		} else {
			// nguoc lai file db nay tao ra truoc do 12h, chua up_to_date
			prefsPrivateEditor.putString(DMS_STATUS_DOWNLOAD_DB, SynDataDTO.CONTINUE);
		}
		prefsPrivateEditor.putString(DMS_VER_DB, userDTO.dbVersion.version.trim());
		prefsPrivateEditor.putString(LAST_LOG_ID, userDTO.dbVersion.lastLogId);
		prefsPrivateEditor.putString(MAX_LAST_LOG_ID, userDTO.dbVersion.maxDBLogId);
		prefsPrivateEditor.commit();
	}

	/**
	 * Luu thong tin sau khi update DB thanh cong
	 *
	 * @author: BANGHN
	 */
	private void saveDBInfoAfterUpdateDB() {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		prefsPrivateEditor.putString(DMS_VER_DB, userDTO.dbVersion.version.trim());
		GlobalInfo.getInstance().getProfile().setVersionDB(userDTO.dbVersion.version.trim());
		prefsPrivateEditor.commit();
	}

	/**
	 * Luu thong tin trang thai update db la UP_TO_DATE
	 *
	 * @param synDTO
	 * @author: BANGHN
	 */
	private void saveDBInfoAfterUpToDate(SynDataDTO synDTO) {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		// trang thai hoan thanh cap nhat db
		prefsPrivateEditor.putString(DMS_STATUS_DOWNLOAD_DB, SynDataDTO.UPDATE_TO_DATE);
		if (synDTO.getLastLogId_update() > 0 && synDTO.getMaxDBLogId() > 0) {
			prefsPrivateEditor.putString(LAST_LOG_ID, "" + synDTO.getLastLogId_update());
			prefsPrivateEditor.putString(MAX_LAST_LOG_ID, "" + synDTO.getMaxDBLogId());
			VTLog.d("saveDBInfoAfterUpToDate", "update synDTO.getLastLogId_update: " + synDTO.getLastLogId_update() + " synDTO.getMaxDBLogId: " + synDTO.getMaxDBLogId());
		} else {
			VTLog.d("saveDBInfoAfterUpToDate", "not update synDTO.getLastLogId_update: " + synDTO.getLastLogId_update() + " synDTO.getMaxDBLogId: " + synDTO.getMaxDBLogId());
		}

		prefsPrivateEditor.commit();
	}

	/**
	 * Kiem tra trang thai update du lieu up_to_date?
	 *
	 * @return
	 * @author: BANGHN
	 */
	private boolean checkDBUpdateToDate() {
		boolean isUpToDate = true;
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		String state = sharedPreferences.getString(DMS_STATUS_DOWNLOAD_DB, SynDataDTO.UPDATE_TO_DATE);
		if (SynDataDTO.CONTINUE.equals(state)) {
			isUpToDate = false;
		}
		return isUpToDate;
	}

	/**
	 * Request login to server
	 *
	 * @param
	 * @param pass (neu la autologin thi truyen oauth data)
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	private void requestLogin(String username, String pass) {
		isRequestLogin = true;
		GlobalInfo.getInstance().saveStatusPosition(1);
		Vector<String> vt = new Vector<String>();

		vt.add(IntentConstants.INTENT_USER_NAME);
		vt.add(username);
		vt.add(IntentConstants.INTENT_LOGIN_PASSWORD);
		vt.add(pass);
		vt.add(IntentConstants.INTENT_LOGIN_IS_REMEMBER);
		vt.add("true");

		vt.add(IntentConstants.INTENT_LOGIN_PHONE_MODEL);
		vt.add(GlobalInfo.getInstance().PHONE_MODEL);


		vt.add(IntentConstants.INTENT_IMEI);
		vt.add(GlobalInfo.getInstance().getDeviceIMEI());

		if (!StringUtil.isNullOrEmpty(StringUtil.getSimSerialNumber())) {
			vt.add(IntentConstants.INTENT_SIM_SERIAL);
			vt.add(StringUtil.getSimSerialNumber());
		}

		vt.add(IntentConstants.INTENT_VERSION_APP);
		vt.add(GlobalInfo.getInstance().getProfile().getVersionApp());

		vt.add(IntentConstants.INTENT_VERSION_DB);
		vt.add(GlobalInfo.getInstance().getProfile().getVersionDB());

		vt.add(IntentConstants.INTENT_LOGIN_PLATFORM);
		vt.add(GlobalInfo.getInstance().PLATFORM_SDK_STRING);

		vt.add(IntentConstants.INTENT_DEVICE_ID);
		vt.add(String.valueOf(GlobalInfo.getInstance().getDeviceId()));

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.ACTION_LOGIN;
		// e.action = ActionEventConstant.ACTION_LOGIN_OFFLINE;
		e.viewData = vt;
		e.isBlockRequest = true;
		e.sender = LoginView.this;
		UserController.getInstance().handleViewEvent(e);
	}

	/**
	 * Login offline khi khong co mang
	 *
	 * @author : BangHN since : 1.0
	 */
	private void loginOffline(ModelEvent modelEvent) {

		userDTO = new UserDTO();
		// kiem tra da dang nhap truoc do chua
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		String userName = sharedPreferences.getString(DMS_USER_NAME, "");
		String pass = sharedPreferences.getString(DMS_PASSWORD, "");

		userDTO.userName = userName;
		userDTO.pass = pass;
		userDTO.userCode = sharedPreferences.getString(DMS_STAFF_CODE, "");
		userDTO.chanelObjectType = sharedPreferences.getInt(DMS_CHANEL_OBJECT_TYPE, 1);
		userDTO.chanelType = sharedPreferences.getInt(DMS_CHANEL_TYPE, 0);
		userDTO.staffOwnerId = sharedPreferences.getLong(DMS_STAFF_OWNER_ID, 0);
		userDTO.shopId = sharedPreferences.getString(DMS_SHOP_ID, "");
		userDTO.shopIdProfile = sharedPreferences.getString(DMS_SHOP_ID_PROFILE, "");
		userDTO.shopCode = sharedPreferences.getString(DMS_SHOP_CODE, "");
		userDTO.saleTypeCode = sharedPreferences.getString(DMS_SALE_TYPE_CODE, "");
		userDTO.id = sharedPreferences.getInt(DMS_ID, 0);
		userDTO.checkAccessApp = sharedPreferences.getInt(DMS_ACCESS_APP, -1);
		userDTO.mobilePhone = sharedPreferences.getString(DMS_MOBILEPHONE, "");
		userDTO.sendLog = sharedPreferences.getInt(DMS_SEND_LOG, -1);
		userDTO.displayName = sharedPreferences.getString(DMS_DISPLAY_NAME, "");
		Type type = new TypeToken<ArrayList<ShopDTO>>() {
		}.getType();
		userDTO.listShop = new Gson().fromJson(sharedPreferences.getString(DMS_LIST_SHOP, ""), type);
		userDTO.shopManaged = new Gson().fromJson(sharedPreferences.getString(DMS_SHOP_MANAGED, ""), ShopDTO.class);
		userDTO.salt = sharedPreferences.getString(DMS_SALT, "");
		try {
			String edPass = StringUtil.generateHashSHA256(edPassword.getText().toString().trim(), userDTO.salt.toLowerCase(),
					GlobalInfo.getInstance().getTypeHashPass());
			if (userName.equals(edUserName.getText().toString().trim())
					&& pass.equals(edPass)) {
				//check trường hợp nâng cấp phiên bản mà cancel, sau đó login offline
				String oldVersion = sharedPreferences.getString(DMS_OLD_VERSION, "");
				String currentVersion = GlobalUtil.getAppVersion();
				boolean isNeedUpdateVersion = currentVersion != null && currentVersion.equals(oldVersion);
				// nếu chưa từng đăng nhập online trong ngày
				int wrongTime = DateUtils.checkTabletRightTimeLoginOffline();
				if (isNeedUpdateVersion) {
					showDialog(StringUtil.getString(R.string.ERROR_NEED_UPDATE_APP));
				} else if (wrongTime != DateUtils.RIGHT_TIME) {
//					GlobalUtil.showDialogNotAllowOfflineLogin();
					closeProgressDialog();
					processAfterLoginOfflineWrongTime();
				} else {
					GlobalInfo.getInstance().getProfile().setUserData(userDTO);
					// save data
					saveUserInfo(userDTO);
					closeProgressDialog();
					// check va gui toan bo log dang con ton dong
					// TransactionProcessManager.getInstance().startChecking();
					proccessAfterLoginOffline();
//					stateExistsDB = GlobalUtil.checkExistsDataBase();
//					closeProgressDialog();
//
//					if (stateExistsDB == 1) {
//						// thuc hien Cipher DB neu chua Cipher, tham so 1 login
//						// offline
//						showProgressDialog(StringUtil.getString(R.string.PROCESS_CIPHER_DB), false);
//						new CipherTask().execute(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath(), "1");
//					} else {
//						this.selectShopAfterLogin();
//					}
				}
			} else if (StringUtil.isNullOrEmpty(pass) && edUserName.isEnabled()) {
				closeProgressDialog();
				// neu chua login lan dau tien, ma ket noi mang loi
				showDialog(modelEvent.getModelMessage());
			} else {
				closeProgressDialog();
				showDialog(StringUtil.getString(R.string.ERR_PASSWORD_WRONG));
			}
		} catch (Exception e) {
			closeProgressDialog();
			showDialog(StringUtil.getString(R.string.ERR_LOGIN_OFFLINE));
		}
	}

	/**
	 * Request sync data
	 *
	 * @author : BangHN since : 1:49:12 PM
	 */
	private void requestSynData() {
		try {
			isRequestSynData = true;
			Vector<String> vt = new Vector<String>();
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			String lastLogId = sharedPreferences.getString(LAST_LOG_ID, "0");
			VTLog.logToFile("SYNDATA", "Login: requestSynData : " + lastLogId);
			VTLog.i("SYNDATA", "Login: requestSynData : " + lastLogId);
			// vt.add(IntentConstants.INTENT_SYN_STAFFID);
			// vt.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
			// vt.add(IntentConstants.INTENT_SHOP_ID);
			// vt.add(GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile);
			vt.add(IntentConstants.INTENT_LAST_LOG_ID);
			vt.add(lastLogId);// "2489904"

			ActionEvent e = new ActionEvent();
			// e.action = ActionEventConstant.ACTION_SYN_SYNDATA;
			e.action = ActionEventConstant.ACTION_SYN_SYNDATA;
			e.isBlockRequest = true;
			e.viewData = vt;
			e.userData = lastLogId;
			e.sender = LoginView.this;

			SynDataController.getInstance().handleViewEvent(e);
			VTLog.d("requestSynData", "requestSynData Login lastLogId: " + lastLogId);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	/**
	 * Request sync data max id row
	 *
	 * @author : BangHN since : 1:49:12 PM
	 */
	private void requestSynDataMax() {
		try {
			isRequestSynData = true;
			Vector<String> vt = new Vector<String>();
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			String lastLogId = sharedPreferences.getString(LAST_LOG_ID, "0");
			VTLog.i("SYNDATA", "Login: requestSynData (getMaxData) : "
					+ lastLogId);
			vt.add(IntentConstants.INTENT_SYN_STAFFID);
			vt.add(String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
			vt.add(IntentConstants.INTENT_SHOP_ID);
			vt.add(GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile);
			vt.add(IntentConstants.INTENT_LAST_LOG_ID);
			vt.add(lastLogId);
			vt.add(IntentConstants.INTENT_GET_MAX_ID_DATA);
			vt.add("1");

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_SYN_SYNDATA_MAX;
			e.isBlockRequest = true;
			e.viewData = vt;
			e.userData = lastLogId;
			e.sender = LoginView.this;

			SynDataController.getInstance().handleViewEvent(e);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	/**
	 * Request xoa bo nhung record cu trong bang log_table, action log
	 *
	 * @author banghn
	 */
	// private void requestDeleteOldLogTable() {
	// ActionEvent e = new ActionEvent();
	// e.action = ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE;
	// e.viewData = new Bundle();
	// e.sender = LoginView.this;
	// UserController.getInstance().handleViewEvent(e);
	// }
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent act = modelEvent.getActionEvent();
		switch (act.action) {
			case ActionEventConstant.ACTION_LOGIN: {
				isRequestLogin = false;
				userDTO = (UserDTO) modelEvent.getModelData();
				// //////////////Check thời gian\\\\\\\\\\\\\\\\\\\\\
				if (!StringUtil.isNullOrEmpty(userDTO.serverDate)) {
					RightTimeInfo rightInfo = new RightTimeInfo();
//				// luu lai thoi gian cuoi cung login online server
//				rightInfo.lastTimeOnlineLogin = userDTO.serverDate;
//				// cap nhat thoi gian dung cuoi cung
//				rightInfo.lastRightTime = userDTO.serverDate;
					// Check thoi gian
					if (!StringUtil.isNullOrEmpty(userDTO.serverDate)) {
						// luu lai thoi gian cuoi cung login online server
						rightInfo.lastTimeOnlineLogin = userDTO.serverDate;
					} else {
						// luu lai thoi gian cuoi cung login online local
						rightInfo.lastTimeOnlineLogin = DateUtils.now();
					}
					//cap nhat thoi gian dung cuoi cung
					rightInfo.lastRightTime = rightInfo.lastTimeOnlineLogin;
					// cap nhat thoi gian tu khi boot
					rightInfo.lastRightTimeSinceBoot = SystemClock.elapsedRealtime();
					// lưu thời gian đúng mới nhất
					GlobalInfo.getInstance().setRightTimeInfo(rightInfo);
				}
				//Id thiet bi
				if (userDTO.deviceId > 0) {
					GlobalInfo.getInstance().setDeviceId(userDTO.deviceId);
				}
				// //////////////Check thời gian\\\\\\\\\\\\\\\\\\\\\

				// saveServerDateAfterLogin();
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				// thêm KPI
				requestInsertLogKPI(HashMapKPI.GLOBAL_LOGIN, act.startTimeFromBoot);
				if (userDTO.appVersion != null && !StringUtil.isNullOrEmpty(userDTO.appVersion.content)) {
					ServerLogger.sendLogLogin("handleModelViewEvent - ACTION_LOGIN"
							, "Cap nhat phien ban moi"
							, TabletActionLogDTO.LOG_LOGIN);
//				String strCancel = "";
					isNeedUpdateVersionGlobal = true;
					if (userDTO.appVersion.forceDownload) {
//					strCancel = StringUtil.getString(R.string.TEXT_CANCEL);
						//lưu version hiện tại để khi login offline + cancel check lại
						sharedPreferences.edit().putString(DMS_OLD_VERSION, GlobalUtil.getAppVersion()).commit();
					} else {
//					strCancel = StringUtil.getString(R.string.TEXT_UPDATE_LATER);
						//bỏ check cần cập nhật phiên bản mới
						sharedPreferences.edit().putString(DMS_OLD_VERSION, "").commit();
					}
//				GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.NOTICE_UPDATE_VERSION), userDTO.appVersion.content, StringUtil.getString(R.string.UPDATE), ACTION_UPDATE_VERSION_SUCCESS, strCancel, ACTION_UPDATE_VERSION_FAIL, userDTO);
					proccessNeedUpdateVersion();
				}
//			else if (userDTO.dbVersion != null) {
//				// reset db
//				if (DBVersionDTO.RESET.equals(userDTO.dbVersion.action)) {
//					closeProgressDialog();
//					saveUserInfo(userDTO);
//					GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_RESET_DB), StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK_RESET_DB, StringUtil.getString(R.string.TEXT_CANCEL), ACTION_CANCEL_RESET_DB, null);
//				} else {
//					// updatedb
//					executeSQLUpdateDB(userDTO.dbVersion.script);
////					saveDBInfoAfterUpdateDB();
//					// ket thuc login - ghi log neu co
//					// tiep tuc login
//					continueAfterLoginSuccess();
//				}
//			} 
				else {
					ServerLogger.sendLogLogin("handleModelViewEvent - ACTION_LOGIN"
							, "continueAfterLoginSuccess"
							, TabletActionLogDTO.LOG_LOGIN);
					// tiep tuc login
//				continueAfterLoginSuccess();
					//bỏ check cần cập nhật phiên bản mới
					isNeedUpdateVersionGlobal = false;
					sharedPreferences.edit().putString(DMS_OLD_VERSION, "").commit();
					proccessAtferLoginOnline();
				}
				break;

			}
			case ActionEventConstant.ACTION_GET_LINK_SQL_FILE: {
				isRequestDBLink = false;
				VTLog.logToFile("Login", "Finish request get link DB: "
						+ DateUtils.now());
				userDTO.dbVersion = (DBVersionDTO) modelEvent.getModelData();
				beginLogId = Long.parseLong(userDTO.dbVersion.lastLogId);
				new DownloadTask().execute(userDTO.dbVersion.urlDB, userDTO.dbVersion.keyZip);
				ServerLogger.sendLogLogin("handleModelViewEvent - ACTION_GET_LINK_SQL_FILE"
						, "link: " + userDTO.dbVersion.urlDB +
								" -fileName: " + ExternalStorage.getListFileName(ExternalStorage.getFileDBPath(getBaseContext()))
						, TabletActionLogDTO.LOG_LOGIN);
				break;
			}

			// case ActionEventConstant.ACTION_SYN_SYNDATA:
			case ActionEventConstant.ACTION_SYN_SYNDATA: {
				// switch (modelEvent.getModelCode()) {
				// case 0:
				// super.handleModelViewEvent(modelEvent);
				// break;
				// case 1:
				// // xoa nhung bang log khong dung toi/
				// this.requestDeleteOldLogTable();
				// break;
				// case 200:
				// chuyen den trang home
				SynDataDTO synDataDTO = (SynDataDTO) modelEvent.getModelData();
				if (SynDataDTO.UPDATE_TO_DATE.equals(synDataDTO.getState())) {
					new VNMDeleteFileTmp().execute("");
					VTLog.i("SYNDATA", "Login: UPDATE_TO_DATE");
					// luu trang thai hoan thanh cap nhat
					saveDBInfoAfterUpToDate(synDataDTO);
					VTLog.i("SYNDATA", "Login: UPDATE_TO_DATE");
					// get dong max_id data
					//requestSynDataMax();
					updateProgressPercentDialog(99);
					//end syn data
					// thêm KPI
					requestInsertLogKPI(HashMapKPI.GLOBAL_LOGIN_SYN_DATA, startTimeLoginSynDataFromBoot);
					// tránh trùng
					startTimeLoginSynDataFromBoot = 0;
					selectShopAfterLogin();
				} else if (SynDataDTO.CONTINUE.equals(synDataDTO.getState())) {
					if (GlobalInfo.getInstance().stateSynData != GlobalInfo.SYNDATA_CANCELED) {
						requestSynData();
						percentSynData = (int) (100 - ((float) (synDataDTO.getMaxDBLogId() - synDataDTO.getLastLogId_update()) / (float) (synDataDTO.getMaxDBLogId() - beginLogId)) * 100);
						updateProgressPercentDialog(percentSynData);
					}
				} else if (SynDataDTO.RESET.equals(synDataDTO.getState())) {
					closeProgressDialog();
					GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_RESET_DB), StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK_RESET_DB, StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_RESET_DB, null);
				}
				// isRequestSynData = false;
				// break;
				// }
				break;
			}
			// finish dong bo du lieu
			case ActionEventConstant.ACTION_SYN_SYNDATA_MAX: {
				// thêm KPI
				requestInsertLogKPI(HashMapKPI.GLOBAL_LOGIN_SYN_DATA, startTimeLoginSynDataFromBoot);
				// tránh trùng
				startTimeLoginSynDataFromBoot = 0;
				selectShopAfterLogin();
				// requestDeleteOldLogTable();
				break;
			}

			// case ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE:
			// selectShopAfterLogin();
			// break;
			case ActionEventConstant.ACTION_UPDATE_DELETED_DB:
				userDTO.isDeleteData = false;
				GlobalInfo.getInstance().getProfile().getUserData().isDeleteData = false;
				GlobalInfo.getInstance().getProfile().save();
				break;
			default:
				super.handleModelViewEvent(modelEvent);
				break;
		}

	}

	/**
	 * Hien thi danh sach NPP can quan ly
	 *
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void showDialogListNPP() {
		Builder build = new Builder(this, R.style.CustomDialogTheme);
		ListShopManagedView view = new ListShopManagedView(this, userDTO.listShop, ACTION_SELECTED_SHOP_MANAGE);
		build.setView(view.viewLayout);
		alertListShopDialog = build.create();
		Window window = alertListShopDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		if (!alertListShopDialog.isShowing() && !isFinishing()) {
			alertListShopDialog.show();
		}
		closeProgressDialog();
	}

	/**
	 * Tiep tuc luong save data, update data sau khi login thanh cong
	 *
	 * @author : BangHN since : 1.0
	 */
	private void continueAfterLoginSuccess() {
		// file DB co ton tai hay available hay khong
		stateExistsDB = GlobalUtil.checkExistsDataBase();

		ServerLogger.sendLogLogin("continueAfterLoginSuccess- begin"
				, "stateExistsDB: " + stateExistsDB +
						" -VerDB: " + GlobalInfo.getInstance().getProfile().getVersionDB() +
						" -isDeleteData: " + userDTO.isDeleteData +
						" -fileName: " + ExternalStorage.getListFileName(ExternalStorage.getFileDBPath(getBaseContext()))
				, TabletActionLogDTO.LOG_LOGIN);

		// luu thong tin dang nhap
		this.saveUserInfo(userDTO);
		VTLog.logToFile("Login", DateUtils.now() + " - Login success ");

		// kiem tra download file DB hay sync data
//		closeProgressDialog();
		// neu lan dau login hoac ko ton tai file db
		VTLog.logToFile("SQLCheck", DateUtils.now() + "Login: requestData : "
				+ GlobalInfo.getInstance().getProfile().getVersionDB() + " : "
				+ stateExistsDB);

		// Download du lieu khi cap nhat truong isDeleteData
		if (userDTO.isDeleteData) {
			ServerLogger.sendLogLogin("continueAfterLoginSuccess - isDeleteData"
					, "isDeleteData:" + userDTO.isDeleteData +
							"-stateExistsDB:" + stateExistsDB
					, TabletActionLogDTO.LOG_LOGIN);
			GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_RESET_DB), StringUtil.getString(R.string.TEXT_AGREE), ACTION_RE_DOWNLOAD_DB_OK, StringUtil.getString(R.string.TEXT_DENY), ACTION_RE_DOWNLOAD_DB_CANCEL, null);
		} else if (GlobalInfo.getInstance().getProfile().getVersionDB().equals("0.0.0")
				|| stateExistsDB == 0) {
			if (!edUserName.isEnabled()) {
				ServerLogger.sendLogLogin("continueAfterLoginSuccess - !edUserName"
						, "!edUserName,stateExistsDB : " + stateExistsDB +
								" -fileName: " + ExternalStorage.getListFileName(ExternalStorage.getFileDBPath(getBaseContext()))
						, TabletActionLogDTO.LOG_LOGIN);
				new VNMDBBackup().execute("");
			} else {
				ServerLogger.sendLogLogin("continueAfterLoginSuccess - requestGetLinkFileDB"
						, "requestGetLinkFileDB, stateExistsDB: " + stateExistsDB +
								" -fileName: " + ExternalStorage.getListFileName(ExternalStorage.getFileDBPath(getBaseContext()))
						, TabletActionLogDTO.LOG_LOGIN);
				showProgressDialog(StringUtil.getString(R.string.downloading));
				requestGetLinkFileDB();
			}
		} else if (stateExistsDB == 1) { // thuc hien Cipher DB neu chua Cipher
			ServerLogger.sendLogLogin("continueAfterLoginSuccess - stateExistsDB == 1"
					, "Thuc hien Cipher DB, stateExistsDB:" + stateExistsDB
					, TabletActionLogDTO.LOG_LOGIN);
			showProgressDialog(StringUtil.getString(R.string.PROCESS_CIPHER_DB), false);
			new CipherTask().execute(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath(), "0");
		} else {
			ServerLogger.sendLogLogin("continueAfterLoginSuccess - startUpSynDataFlow"
					, "startUpSynDataFlow, stateExistsDB: " + stateExistsDB
					, TabletActionLogDTO.LOG_LOGIN);
			this.startUpSynDataFlow();
		}
	}

	private long startTimeLoginSynDataFromBoot = 0;

	/**
	 * Khoi dong luong Syndata
	 *
	 * @author: ThangNV31
	 * @return: void
	 */
	private void startUpSynDataFlow() {
		startTimeLoginSynDataFromBoot = SystemClock.elapsedRealtime();
		// checkUserDB();
		// showProgressDialog(StringUtil.getString(R.string.updating));
		showProgressPercentDialog(StringUtil.getString(R.string.updating), true);
		// thuc hien day het request len server truoc khi update ve
		TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_FROM_LOGIN);
		// requestSynData();
	}

	/**
	 * Update DB: Them cot trong sales_order
	 *
	 * @author: BANGHN
	 */
	private void executeSQLUpdateDB(String sql) {
		try {
			if (!StringUtil.isNullOrEmpty(sql)) {
				if (sql.contains("\r\n")) {
					sql = sql.replaceAll("\r\n", "");
				} else if (sql.contains("\n")) {
					sql = sql.replaceAll("\n", "");
				}
				String[] script = sql.split(";");
				for (int i = 0; i < script.length; i++) {
					SQLUtils.getInstance().getmDB().execSQL(script[i]);
				}
			}
			saveDBInfoAfterUpdateDB();
		} catch (Exception e) {
			ServerLogger.sendLog("UPDATE SQLITE", e.getMessage() + sql,
					TabletActionLogDTO.LOG_EXCEPTION);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	/**
	 * Request get link DB de tai file DB
	 *
	 * @author : BangHN since : 11:53:27 AM
	 */
	private void requestGetLinkFileDB() {
		isRequestDBLink = true;
		// TODO Auto-generated method stub
		startTimeDownloadDBFromBoot = SystemClock.elapsedRealtime();
		try {
			VTLog.logToFile("Login", "Start request get link DB: "
					+ DateUtils.now());
			Vector<String> vt = new Vector<String>();
			// vt.add(IntentConstants.INTENT_SYN_STAFFID);
			// vt.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
			// vt.add(IntentConstants.INTENT_SHOP_ID);
			// vt.add(GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile);
			vt.add(IntentConstants.INTENT_IMEI);
			vt.add(GlobalInfo.getInstance().getDeviceIMEI());

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_GET_LINK_SQL_FILE;
			e.isBlockRequest = true;
			e.viewData = vt;
			e.sender = LoginView.this;

			SynDataController.getInstance().handleViewEvent(e);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	/**
	 * gui request thong bao da xoa DB thanh cong
	 *
	 * @author banghn
	 */
	private void requestUpdateDeletedDB() {
		if (userDTO != null && userDTO.isDeleteData) {
			Vector<String> vt = new Vector<String>();
			// vt.add(IntentConstants.INTENT_STAFF_ID);
			// vt.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
			// vt.add(IntentConstants.INTENT_SHOP_ID);
			// vt.add(GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_UPDATE_DELETED_DB;
			e.viewData = vt;
			e.sender = LoginView.this;
			UserController.getInstance().handleViewEvent(e);
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		VTLog.logToFile("Login", DateUtils.now() + "Action		: "
				+ modelEvent.getActionEvent().action);
		VTLog.logToFile("Login", DateUtils.now() + "Error code	: "
				+ modelEvent.getModelCode());
		VTLog.logToFile("Login", DateUtils.now() + "Message		: "
				+ modelEvent.getModelMessage());
		GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
		closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
			case ActionEventConstant.ACTION_GET_LINK_SQL_FILE:
				isRequestDBLink = false;
				switch (modelEvent.getModelCode()) {
					case ErrorConstants.ERROR_REST_DB_OUT_MAXIMUM:
						showDialog(modelEvent.getModelMessage());
						break;
					default:
						showDialog(StringUtil.getString(R.string.ERR_CANT_CREATE_DB));
						ServerLogger.sendLogLogin("LOGIN ACTION_SYN_SYNDATA"
								, "modelEvent 2: " + modelEvent.getModelMessage()
										+ "modelCode: " + modelEvent.getModelCode()
								, TabletActionLogDTO.LOG_CLIENT);
						break;
				}
				break;
			// truong hop dong bo update du lieu bi loi
			// tiep tuc toi trang home
			case ActionEventConstant.ACTION_SYN_SYNDATA_MAX:
			case ActionEventConstant.ACTION_SYN_SYNDATA:
				if (modelEvent != null && modelEvent.getModelMessage() != null
						&& modelEvent.getModelMessage().contains("ENOSPC")) {
					showDialog(StringUtil.getString(R.string.ERR_FULL_DISK));
				} else {
					showDialog(Constants.MESSAGE_ERROR_COMMON);
					ServerLogger.sendLogLogin("LOGIN ACTION_SYN_SYNDATA"
							, "modelEvent: " + modelEvent.getModelMessage()
									+ " code: " + modelEvent.getModelCode()
									+ " action: " + modelEvent.getActionEvent().action
							, TabletActionLogDTO.LOG_CLIENT);
				}
				//selectShopAfterLogin();
				break;
			case ActionEventConstant.ACTION_DELETE_OLD_LOG_TABLE:
				selectShopAfterLogin();
				break;
			case ActionEventConstant.ACTION_LOGIN:
				isRequestLogin = false;
				switch (modelEvent.getModelCode()) {
					case ErrorConstants.ERROR_DB_OUT_OF_DATE:
						GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.ERR_DB_STRUCTURE), StringUtil.getString(R.string.TEXT_AGREE), ACTION_UPDATE_DB_OK, StringUtil.getString(R.string.TEXT_DENY), ACTION_UPDATE_DB_CANCEL, null);
						break;
					case ErrorConstants.ERROR_INVALID_ACCOUNT:
						showDialog(modelEvent.getModelMessage());
						edUserName.requestFocus();
						break;
					case ErrorConstants.ERROR_INVALID_PASSWORD:
						showDialog(StringUtil.getString(R.string.ERR_PASSWORD_WRONG));
						edPassword.setText("");
						edPassword.requestFocus();
						break;
					case ErrorConstants.ERROR_ACCOUNT_LOCKED:
						showDialog(StringUtil.getString(R.string.ERROR_ACCOUNT_LOCKED));
						// showDialog(modelEvent.getModelMessage());
						edUserName.requestFocus();
						break;
					case ErrorConstants.ERROR_CONNET_SERVER_DB:
					case ErrorConstants.ERROR_NO_CONNECTION:
						loginOffline(modelEvent);
						break;
					case ErrorConstants.ERROR_EXPIRED_TIMESTAMP:
						GlobalUtil.showDialogSettingTime();
						break;
					case ErrorConstants.ERROR_ACCOUNT_MAXIMUM_LOCK:
						showDialog(StringUtil.getString(R.string.ERROR_ACCOUNT_MAXIMUM_LOCK));
						// showDialog(modelEvent.getModelMessage());
						edPassword.requestFocus();
						break;
					case ErrorConstants.ERROR_INVALID_IMEI:
						showDialog(StringUtil.getString(R.string.ERROR_INVALID_IMEI));
						// showDialog(modelEvent.getModelMessage());
						break;
					case ErrorConstants.ERROR_INVALID_SERIAL:
						showDialog(StringUtil.getString(R.string.ERROR_INVALID_SERIAL));
						// showDialog(modelEvent.getModelMessage());
						break;
					case ErrorConstants.ERROR_INVALID_IMEI_OR_SERIAL:
						showDialog(StringUtil.getString(R.string.ERROR_INVALID_IMEI_OR_SERIAL));
						// showDialog(modelEvent.getModelMessage());
						break;
					default:
						super.handleErrorModelViewEvent(modelEvent);
						break;
				}
				break;

			default:
				super.handleErrorModelViewEvent(modelEvent);
				break;
		}
	}

	/**
	 * Chon NPP sau khi login thanh cong de tiep tuc vao main view
	 *
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void selectShopAfterLogin() {
		// hien thi popup NPP giam sat can chon de vao chuong trinh
		if (userDTO.listShop != null && userDTO.listShop.size() > 1) {
			showDialogListNPP();
		} else if (userDTO.listShop != null && userDTO.listShop.size() == 1) {
			// TamPQ: truong hop GS chi quan ly 1 shop ma shop nay khac shop cua
			// gs
			userDTO.shopManaged = userDTO.listShop.get(0);
			userDTO.shopId = "" + userDTO.listShop.get(0).shopId;
			saveUserInfo(userDTO);
			goToMainView();
		} else {
			goToMainView();
		}
	}

	/**
	 * Di toi man hinh nhan vien ban hang
	 *
	 * @author : BangHN since : 1.0
	 */
	private void goToMainView() {
		if (GlobalInfo.getInstance().stateSynData == GlobalInfo.SYNDATA_CANCELED) {
			return;
		}
		GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
		isRequestSynData = false;
		GlobalInfo.getInstance().isExitApp = false;

		// neu update du lieu chua toi trang thai up_to_date thi
		// khong tiep tuc thuc hien
		if (!checkDBUpdateToDate()) {
			closeProgressDialog();
			if (!isFinishing()) {
				showDialog(StringUtil.getString(R.string.ERR_UPDATE_NOT_SUCCESS));
			}
			ServerLogger.sendLog("SYNDATA", "Update du lieu loi chua toi UP_TO_DATE", TabletActionLogDTO.LOG_CLIENT);
			return;
		}

		if (userDTO == null) {
			userDTO = GlobalInfo.getInstance().getProfile().getUserData();
		}
		// request update da xoa DB thanh cong neu co
		requestUpdateDeletedDB();
		// constant define app
		setAppDefineConstant();

		if (GlobalInfo.getInstance().isVtMapSetServer()) {
			// AnhND modify dns time out cache
			AppInfo.setServerAddress(GlobalInfo.getInstance().getVtMapProtocol(), GlobalInfo.getInstance().getVtMapIP(), GlobalInfo.getInstance().getVtMapPort());
			System.setProperty("networkaddress.cache.ttl", "0");
			System.setProperty("networkaddress.cache.negative.ttl", "0");
		}
		Bundle b = new Bundle();
		if (userDTO != null && !StringUtil.isNullOrEmpty(userDTO.serverDate)) {
			b.putString(IntentConstants.INTENT_TIME, userDTO.serverDate);
		} else {
			b.putString(IntentConstants.INTENT_TIME, "");
		}
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b;
		if (userDTO.chanelObjectType == UserDTO.TYPE_GSNPP) {
			// NVGS
			e.action = ActionEventConstant.GO_TO_SUPERVISOR_VIEW;
		} else if (userDTO.chanelObjectType == UserDTO.TYPE_GSTINH) {
			// Giam sat tinh (~ TBHV)
			e.action = ActionEventConstant.GO_TO_TBHV_VIEW;
		} else if (userDTO.chanelObjectType == UserDTO.TYPE_TNPG) {
			// Truong nhom tiep thi
			e.action = ActionEventConstant.GO_TO_TNPG;
		} else if (userDTO.chanelObjectType == UserDTO.TYPE_PG) {
			// nhan vien tiep thi
			e.action = ActionEventConstant.GO_TO_PG;
		} else {
			// NVBH
			e.action = ActionEventConstant.GO_TO_SALE_PERSON_VIEW;
		}

		// thêm KPI
		requestInsertLogKPI(HashMapKPI.GLOBAL_FULL_LOGIN, startTimeFromBoot);

		UserController.getInstance().handleSwitchActivity(e);
		closeProgressDialog();
	}

	@Override


	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
			case ACTION_UPDATE_VERSION_SUCCESS:
				// kiem tra co reset DB hay khong
				if (userDTO.appVersion.needResetDB) {
					GlobalUtil.deleteDatabaseVNM();
				}
				// HieuNH: xoa file apk trong thu muc app truoc khi download
				GlobalUtil.deleteDir(new File(ExternalStorage.DMS_SABECO_FOLDER
						+ "APP"));
				// thuc hien download update ver moi
				new DownloadTaskInstallApp().execute(((UserDTO) data).appVersion.downloadLink);
				break;
			case ACTION_UPDATE_VERSION_FAIL:
				// tiep tuc login vao he thong
				if (userDTO.appVersion != null
						&& userDTO.appVersion.forceDownload == false) {
//				continueAfterLoginSuccess();
					// xu li truong hop ko update version de vao ung dung
					proccessAtferUpdateVersionFail();
				} else {
					finish();
				}
				break;
			case ACTION_RE_DOWNLOAD_DB_OK:
				// requestGetLinkFileDB();
				TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_NORMAL);
				ServerLogger.sendLogLogin("ACTION_RE_DOWNLOAD_DB_OK"
						, "Dong y down lai file"
						, TabletActionLogDTO.LOG_LOGIN);
				new VNMDBBackup().execute("");
				break;
			case ACTION_UPDATE_DB_OK:
				// do something here to update db
				break;
			case ACTION_UPDATE_DB_CANCEL:
				ServerLogger.sendLogLogin("ACTION_UPDATE_DB_CANCEL"
						, "Huy bo down lai file"
						, TabletActionLogDTO.LOG_LOGIN);
				finish();
				break;
			case ACTION_DOWNLOAD_DB_OK:
				// do something here to update db
				new VNMDBBackup().execute("");
				break;
			case ACTION_DOWNLOAD_DB_CANCEL:
				finish();
				break;
			case ACTION_OK_RESET_DB:
				// do something here to update db
				ServerLogger.sendLogLogin("ACTION_OK_RESET_DB"
						, "Dong y tai lai file"
						, TabletActionLogDTO.LOG_LOGIN);
				new VNMDBBackup().execute("");
				break;
			case ACTION_CANCEL_LOGIN_OK:
				sendBroadcast(ActionEventConstant.ACTION_FINISH_APP, new Bundle());
				break;
			case ACTION_SELECTED_SHOP_MANAGE:
				showProgressDialog(StringUtil.getString(R.string.loading));
				// /ShopDTO shop = (ShopDTO) data;
				userDTO.shopManaged = (ShopDTO) data;
				userDTO.shopId = "" + userDTO.shopManaged.shopId;
				userDTO.shopCode = userDTO.shopManaged.shopCode;
				alertListShopDialog.cancel();
				saveUserInfo(userDTO);
				goToMainView();
				break;
			case ACTION_CLEAR_DATA_OK:
				setReGPSRecentInfo();
				TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_NORMAL);
				new BackupDeleteFilrLoginOtherUser().execute("");
				GlobalInfo.getInstance().getProfile().clearProfileLoginOtherUser();
				break;
			default:
				super.onEvent(eventType, control, data);
				break;
		}
	}

	@Override
	public void receiveBroadcast(int action, Bundle bundle) {
		switch (action) {
			case ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN:
				edPassword.setText("");
				break;
			case ActionEventConstant.REQUEST_TO_SERVER_SUCCESS:
				if (GlobalInfo.getInstance().lastActivity != null
						&& GlobalInfo.getInstance().lastActivity.equals(this)) {
					if (GlobalInfo.getInstance().getProfile().getUserData().getLoginState() == UserDTO.LOGIN_SUCCESS) {
						// SharedPreferences sharedPreferences =
						// GlobalInfo.getInstance().getDmsPrivateSharePreference();
						// beginDBLogId =
						// Long.parseLong(sharedPreferences.getString(LAST_LOG_ID,
						// "0"));
						startTimeLoginSynDataFromBoot = SystemClock.elapsedRealtime();
						requestSynData();
					} else {
						closeProgressDialog();
						ServerLogger.sendLogLogin("LOGIN"
								, "login view state: " + GlobalInfo.getInstance().getProfile().getUserData().getLoginState()
								, TabletActionLogDTO.LOG_CLIENT);
						showDialog(Constants.MESSAGE_ERROR_COMMON);
					}
				}
				break;
			case ActionEventConstant.ACTION_SYN_PERCENT: {
				long lastLogId = bundle.getLong(IntentConstants.INTENT_LAST_LOG_ID);
				long maxLogId = bundle.getLong(IntentConstants.INTENT_MAX_LOG_ID);

				percentSynData = (int) (100 - ((float) (maxLogId - lastLogId) / (float) (maxLogId - beginLogId)) * 100);
				updateProgressPercentDialog(percentSynData);
				break;
			}
			case ActionEventConstant.ACTION_FINISH_SYN_SYNDATA_NORMAL: {
//				closeProgressDialog();
				isRequestSynDataBeforeUpVersion = false;
				//check need update version
				if (isNeedUpdateVersionGlobal) {
					isNeedUpdateVersionGlobal = false;
					proccessUpdateVersion();
				}
				break;
			}
			default:
				super.receiveBroadcast(action, bundle);
				break;
		}
	}

	// cai dat ung dung moi
	private void InstallNewApp(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(new File(url)), "application/vnd.android.package-archive");
		startActivity(intent);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		VTLog.i("Login", "onCancel.... ");
		if (GlobalInfo.getInstance().stateSynData == GlobalInfo.SYNDATA_EXECUTING) {
			// showDialog(StringUtil.getString(R.string.MSG_EXECUTING_SYNDATA));
			showToastMessage(StringUtil.getString(R.string.MSG_EXECUTING_SYNDATA), Toast.LENGTH_LONG);
			// showProgressDialog(StringUtil.getString(R.string.updating));
			showProgressPercentDialog(StringUtil.getString(R.string.updating), true);
			updateProgressPercentDialog(percentSynData);
		} else if (isRequestSynData) {
			GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_CANCELED;
		} else if (isRequestLogin) {
			showToastMessage(StringUtil.getString(R.string.MSG_EXECUTING_LOGIN), Toast.LENGTH_LONG);
			showProgressDialog(StringUtil.getString(R.string.loading));
		} else if (isDownloadNativeLibs) {
			LoginView.this.showProgressPercentDialog(StringUtil.getString(R.string.TEXT_DOWNLOADING_NATIVE));
		} else if (isDownloadNewVersion) {
			LoginView.this.showProgressPercentDialog(StringUtil.getString(R.string.APP_DOWNLOADING));
		} else if (isDownloadDB) {
			LoginView.this.showProgressPercentDialog(StringUtil.getString(R.string.downloading));
		} else if (isRequestDBLink) {
			LoginView.this.showProgressDialog(StringUtil.getString(R.string.requestingDB));
		} else if (isRequestSynDataBeforeUpVersion) {
			showProgressDialog(StringUtil.getString(R.string.updating_before_up_version));
		} else {
			super.onCancel(dialog);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_BACK: {
				VTLog.i("Login", "onKeyDown.... KEYCODE_BACK");
				TransactionProcessManager.getInstance().cancelTimer();
				PositionManager.getInstance().stop();
				// SQLUtils.getInstance().closeDB();
				// cap nhat thoat ung dung
				GlobalInfo.getInstance().isExitApp = true;
				// kiem tra dong database
				if (!SQLUtils.getInstance().isProcessingTrans) {
					// cancel sqlLite
					SQLUtils.getInstance().closeDB();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	// ////////////////////////////////////////////////////////////////////////
	// Background Task: backup DB
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Sao luu du lieu luu tru truoc khi download moi
	 *
	 * @author banghn
	 */
	private class VNMDBBackup extends AsyncTask<String, Void, Exception> {
		@Override
		protected void onPreExecute() {
			if (!isFinishing()) {
				showProgressDialog(StringUtil.getString(R.string.backucking));
			}
		}

		@Override
		protected Exception doInBackground(String... params) {
			try {
				GlobalUtil.backupDatabaseToZIP();
				GlobalUtil.deleteBackupDatabaseVNM();
			} catch (Exception e) {
				onPostExecute(e);
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			if (!isFinishing()) {
				showProgressDialog(StringUtil.getString(R.string.downloading));
			}
			if (result != null) {
				// zip file loi tiep tuc get file
				requestGetLinkFileDB();
			} else {
				// tiep tuc get file
				requestGetLinkFileDB();
			}
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// Background Task: Download file DB
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Xoa bo cac file tmp trong qua trinh su dung chuong trinh
	 *
	 * @author banghn
	 */
	private class VNMDeleteFileTmp extends AsyncTask<String, Void, Exception> {
		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Exception doInBackground(String... params) {
			try {
				// xoa bo file anh chup truoc do mot ngay ve truoc
				GlobalUtil.deleteTempTakenPhoto();
			} catch (Exception e) {
				onPostExecute(e);
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			// xu ly sau khi hoan thanh hay loi
		}

	}

	/**
	 * Background task to download and unpack .zip file in background.
	 */
	private class DownloadTask extends AsyncTask<String, Void, Exception> {

		@Override
		protected void onPreExecute() {
			isDownloadDB = true;
			LoginView.this.showBeginProgressPercentDialog(StringUtil.getString(R.string.downloading));
		}

		@Override
		protected Exception doInBackground(String... params) {
			String url = (String) params[0];
			String keyZip = (String) params[1];
			try {
				downloadAllAssets(url, keyZip);
			} catch (Exception e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			isDownloadDB = false;
			if (result != null) {
				// download loi
				closeProgressDialog();
				if (!isFinishing()) {
					if (result.getMessage().contains("ENOSPC")) {
						LoginView.this.showDialog(StringUtil.getString(R.string.ERR_FULL_DISK));
					} else {
						LoginView.this.showDialog(StringUtil.getString(R.string.ERR_DOWNLOAD_DB));
					}
				}
				ServerLogger.sendLog("Download DB", result.getMessage(), false, TabletActionLogDTO.LOG_EXCEPTION);
			} else {
				//update finish download db
				LoginView.this.updateProgressPercentDialog(98);
				LoginView.this.updateProgressPercentDialog(99);
				LoginView.this.updateProgressPercentDialog(100);

				endDownloadDB();
				saveDBVersionInfo();
				// dong bo du lieu thieu (vi co the lay file db da ton tai truoc
				// do)
				closeProgressDialog();
				if (!isFinishing()) {
					LoginView.this.showProgressPercentDialog(StringUtil.getString(R.string.updating), true);
				}
				startTimeLoginSynDataFromBoot = SystemClock.elapsedRealtime();
				requestSynData();
			}
			ServerLogger.sendLogLogin("Finished DownloadDB", "Finished DownloadDB", TabletActionLogDTO.LOG_LOGIN);
			VTLog.logToFile("DownloadDB", "Finished Download DB :" + DateUtils.now());
		}
	}

	// ////////////////////////////////////////////////////////////////////////
	// File Download: Download file cai dat ung dung
	// ////////////////////////////////////////////////////////////////////////

	/**
	 * Download .zip file specified by url, then unzip it to a folder in
	 * external storage.
	 *
	 * @param url
	 */
	private void downloadAllAssets(String url, String keyZip) {
		ServerLogger.sendLogLogin("downloadAllAssets begin", "downloadAllAssets begin", TabletActionLogDTO.LOG_LOGIN);
		// xoa file db hien tai
		GlobalUtil.deleteDatabaseVNM();
		File zipDir = ExternalStorage.getFileDBPath(getBaseContext());
		// File path to store .zip file before unzipping
		File zipFile = new File(zipDir.getPath() + "/temp.zip");
		File outputDir = ExternalStorage.getFileDBPath(getBaseContext());
		File plaintextDBFile = new File(outputDir.getPath() + "/PlaintDMSDatabase");
		File sqlCipherDBFile = new File(outputDir.getPath() + "/DMSDatabase");
		try {
			DownloadFile.downloadWithURLConnection(url, zipFile, zipDir);
			DownloadFile.unzipFileWithPass(zipFile.getPath(), outputDir.getPath(), keyZip);
			GlobalInfo.inProcessCipher = true;
			sqlCipherDBFile.renameTo(new File(outputDir.getPath() + "/PlaintDMSDatabase"));
			SqlCipherUtil.encryptPlaintextDB(plaintextDBFile.getAbsolutePath(), getBaseContext());
//			SQLUtils.getInstance().getmDB().execSQL("analyze;");
			GlobalInfo.inProcessCipher = false;
		} catch (Exception e) {
			ServerLogger.sendLog("downloadAllAssets error" + DateUtils.getRightTimeNow(), TabletActionLogDTO.LOG_EXCEPTION);
			throw new RuntimeException(e);
		} finally {
			zipFile.delete();
		}

	}

	// update application automatic

	/**
	 * Background task to download file in background.
	 */
	private class DownloadTaskInstallApp extends
			AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			if (!isFinishing()) {
				isDownloadNewVersion = true;
				LoginView.this.showBeginProgressPercentDialog(StringUtil.getString(R.string.APP_DOWNLOADING));
			}
		}

		@Override
		protected String doInBackground(String... params) {
			String url = (String) params[0];
			try {
				String path = ExternalStorage.downloadApk(url);
				publishProgress(path);
			} catch (Exception e) {
				return null;
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
			if (values != null) {
				String sdCard = values[0];
				closeProgressDialog();
				InstallNewApp(sdCard);
			} else {
				LoginView.this.updateProgressPercentDialog(98);
				LoginView.this.updateProgressPercentDialog(99);
				LoginView.this.updateProgressPercentDialog(100);
				closeProgressDialog();
				if (!isFinishing()) {
					LoginView.this.showDialog(StringUtil.getString(R.string.ERR_APP_DOWNLOAD));
				}
			}
			isDownloadNewVersion = false;
		}

		@Override
		protected void onPostExecute(String result) {
		}
	}

	/**
	 * Get file apk sau khi download
	 *
	 * @param path
	 * @throws IOException
	 * @author ThanhNN
	 */
	private String getDataSource(String path) throws IOException {
		if (!URLUtil.isNetworkUrl(path)) {
			return path;
		} else {
			URL url = null;
			URLConnection cn = null;
			InputStream stream = null;
			FileOutputStream out = null;
			String tempPath = null;
			try {
				url = new URL(path);
				cn = url.openConnection();
				cn.connect();
				cn.setConnectTimeout(DownloadFile.CONNECT_TIMEOUT);
				cn.setReadTimeout(DownloadFile.READ_TIMEOUT);
				stream = cn.getInputStream();
				if (stream == null)
					throw new RuntimeException("stream is null");
				File temp1 = new File(ExternalStorage.DMS_SABECO_FOLDER
						+ "APP/");
				temp1.mkdirs();
				File temp = File.createTempFile("sabeco_new", ".apk", temp1);

				temp.deleteOnExit();
				tempPath = temp.getAbsolutePath();
				out = new FileOutputStream(temp);
				byte buf[] = new byte[128];
				do {
					int numread = stream.read(buf);
					if (numread <= 0)
						break;
					out.write(buf, 0, numread);
				} while (true);

			} catch (Exception e) {
			} finally {
				try {
					if (out != null) {
						out.close();
						out = null;
					}
					if (stream != null) {
						stream.close();
						stream = null;
					}
					url = null;
					cn = null;
				} catch (IOException ex) {
					VTLog.i("error: ", ex.getMessage());
				}
			}
			return tempPath;
		}
	}

	long startTimeDownloadDBFromBoot = 0;

	/**
	 * Ghi log tai file DB
	 *
	 * @author: banghn
	 */
	private void endDownloadDB() {
		requestInsertLogKPI(HashMapKPI.GLOBAL_GET_FILE_DB, startTimeDownloadDBFromBoot);
		// tránh trùng
		startTimeDownloadDBFromBoot = 0;
	}

	/**
	 * Thread thuc hien chuyen doi DB No_Cipher -> DB_Cipher
	 *
	 * @author: ThangNV31
	 */
	private class CipherTask extends AsyncTask<String, Void, Exception> {
		int actionOnPost = 0;

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected Exception doInBackground(String... params) {
			try {
				String dbDirectoryPath = params[0];
				File sqlCipherDBFile = new File(dbDirectoryPath + "/DMSDatabase");
				GlobalInfo.inProcessCipher = true;
				sqlCipherDBFile.renameTo(new File(dbDirectoryPath + "/DMSDatabasePlain"));
				SqlCipherUtil.encryptPlaintextDB(dbDirectoryPath + "/DMSDatabasePlain", getBaseContext());
				SQLUtils.getInstance().getmDB().execSQL("analyze;");
			} catch (Exception e) {
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			closeProgressDialog();
			if (result != null) {
				if (!isFinishing()) {
					showDialog(StringUtil.getString(R.string.ERR_CIPHER_DB_FAIL));
				}
				VTLog.d("CipherDB", "Error Cipher DB Fail :" + DateUtils.now());
				return;
			}
			if (userDTO.dbVersion != null) {
				executeSQLUpdateDB(userDTO.dbVersion.script);
			}
			GlobalInfo.inProcessCipher = false;
			switch (this.actionOnPost) {
				case 1:// login onffline
					LoginView.this.selectShopAfterLogin();
					break;
				default:
					LoginView.this.startUpSynDataFlow();
			}
		}
	}

	/**
	 * continue atfer login offline success
	 *
	 * @author: duongdt3
	 * @since: 16:05:00 5 Dec 2014
	 * @return: void
	 * @throws:
	 */
	private void proccessAfterLoginOffline() {
		boolean isHaveNativeLib = loadNativeLib();
		// nếu đã có thư viện trong Internal Storage thì cho phép login
		if (isHaveNativeLib) {
			LoginView.this.numErrorNativeLib = 0;
			// cho phep nhap ma login offline xuat don hang
			boolean isAllow = cbAllowOffline.isChecked();
			if (isAllow) {
				showPopupConfirmedCode();
			} else {
				// check va gui toan bo log dang con ton dong
				// TransactionProcessManager.getInstance().startChecking();
				stateExistsDB = GlobalUtil.checkExistsDataBase();
				closeProgressDialog();

				if (stateExistsDB == 1) {
					// thuc hien Cipher DB neu chua Cipher, tham so 1 login
					// offline
					showProgressDialog(StringUtil.getString(R.string.PROCESS_CIPHER_DB), false);
					new CipherTask().execute(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath(), "1");
				} else if (stateExistsDB == 2) {
					//tồn tại db đã cipher
					this.selectShopAfterLogin();
				} else {
					//không có db
					showDialog(StringUtil.getString(R.string.ERR_NOT_YET_DOWNLOAD_DB));
				}
			}

		} else {
			proccessLoadNativeLibFail(PROCCESS_LOGIN_OFFLINE);
		}
	}

	/**
	 * continue atfer login online success
	 *
	 * @author: duongdt3
	 * @since: 15:14:09 5 Dec 2014
	 * @return: void
	 * @throws:
	 */
	private void proccessAtferLoginOnline() {
		boolean isHaveNativeLib = loadNativeLib();
		// nếu đã có thư viện trong Internal Storage thì cho phép login
		if (isHaveNativeLib) {
			LoginView.this.numErrorNativeLib = 0;
			if (userDTO.dbVersion != null) {
				ServerLogger.sendLogLogin("handleModelViewEvent - ACTION_LOGIN"
						, "userDTO.dbVersion != null"
						, TabletActionLogDTO.LOG_LOGIN);
				// reset db
				if (DBVersionDTO.RESET.equals(userDTO.dbVersion.action)) {
					closeProgressDialog();
					saveUserInfo(userDTO);
					ServerLogger.sendLogLogin("handleModelViewEvent - ACTION_LOGIN"
							, "Tai lai du lieu RESET = action: " + userDTO.dbVersion.action
							, TabletActionLogDTO.LOG_LOGIN);
					GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_RESET_DB), StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK_RESET_DB, StringUtil.getString(R.string.TEXT_CANCEL), ACTION_CANCEL_RESET_DB, null);
				} else {
					// updatedb
					ServerLogger.sendLogLogin("handleModelViewEvent - ACTION_LOGIN"
							, "userDTO.dbVersion != null - RESET != action - executeSQLUpdateDB - continueAfterLoginSuccess"
							, TabletActionLogDTO.LOG_LOGIN);
					executeSQLUpdateDB(userDTO.dbVersion.script);
//					saveDBInfoAfterUpdateDB();
					// ket thuc login - ghi log neu co
					// tiep tuc login
					continueAfterLoginSuccess();
				}
			} else {
				continueAfterLoginSuccess();
				// goToMainView();
			}

		} else {
			proccessLoadNativeLibFail(PROCCESS_LOGIN_ONLINE);
		}
	}

	/**
	 * continue atfer update version without require
	 *
	 * @author: duongdt3
	 * @since: 15:13:50 5 Dec 2014
	 * @return: void
	 * @throws:
	 */
	private void proccessAtferUpdateVersionFail() {
		//check native lib
		boolean isHaveNativeLib = loadNativeLib();
		// nếu đã có thư viện trong Internal Storage thì cho phép login
		if (isHaveNativeLib) {
			LoginView.this.numErrorNativeLib = 0;

			continueAfterLoginSuccess();

		} else {
			proccessLoadNativeLibFail(PROCCESS_UPDATE_VERSION_FAIL);
		}
	}

	/**
	 * Xu li cho truong hop load lib tu thu muc ung dung
	 * Goi ham tai lib
	 *
	 * @param typeProccess
	 * @author: Duongdt3
	 * @return: void
	 * @throws:
	 */
	private void proccessLoadNativeLibFail(final int typeProccess) {
		//count num error native lib
		this.numErrorNativeLib++;
		String log = DateUtils.now() + " numFail: " + numErrorNativeLib + " " + Build.CPU_ABI + ", " + Build.CPU_ABI2;
		//delete libs
		ExternalStorage.deleteNativeLibPath(LoginView.this);
		if (this.numErrorNativeLib > 1) {
			if (this.numErrorNativeLib <= 3) {
				AlertDialog alertDialog = new Builder(this).create();
				alertDialog.setMessage(StringUtil.getString(R.string.ERR_LOAD_NATIVE_LIB));
				alertDialog.setButton(StringUtil.getString(R.string.TEXT_FOLLOW_PROBLEM_OK), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						if (LoginView.this != null) {
							//xử lý tải, unzip thư viện
							new DownloadNativeLibTask(typeProccess).execute(Build.CPU_ABI, Build.CPU_ABI2);
						}
					}
				});
				alertDialog.show();
			} else {
				//nếu bị lỗi quá nhiều lần thì yêu cầu khởi động lại máy
				this.showDialog(StringUtil.getString(R.string.ERR_LOAD_NATIVE_LIB_OFTEN));
			}
			ServerLogger.sendLog("Check native lib fail, redownload", log, false, TabletActionLogDTO.LOG_EXCEPTION);
		} else {
			if (!isDownloadNativeLibs) {
				//xử lý tải, unzip thư viện
				new DownloadNativeLibTask(typeProccess).execute(Build.CPU_ABI, Build.CPU_ABI2);
			}
		}
	}

	/**
	 * download lib dong
	 *
	 * @author cuonglt3
	 */
	private class DownloadNativeLibTask extends AsyncTask<String, Void, Exception> {
		int typeProccess = 0;

		public DownloadNativeLibTask(int typeProccess) {
			this.typeProccess = typeProccess;
		}

		@Override
		protected void onPreExecute() {
			if (LoginView.this != null) {
				isDownloadNativeLibs = true;
				LoginView.this.showProgressPercentDialog(StringUtil.getString(R.string.TEXT_DOWNLOADING_NATIVE));
			}
		}

		@Override
		protected Exception doInBackground(String... params) {
			Exception result = new Exception("Not params cpu type");
			if (params != null) {
				for (int i = 0, size = params.length; i < size; i++) {
					String cpu = params[i];
					if (!StringUtil.isNullOrEmpty(cpu)) {
						String urlLib = ServerPath.SERVER_PATH_NATIVE_LIB + cpu + ".zip";
						try {
							ExternalStorage.downloadNativeLib(urlLib, LoginView.this);
							result = null;
							break;
						} catch (Exception e) {
							result = e;
							VTLog.e("Download Native Lib error", urlLib + " " + e.getMessage());
						}
					}
				}
				if (result != null) {
					String urlLib = ServerPath.SERVER_PATH_NATIVE_LIB  + "armeabi-v7a.zip";
					try {
						ExternalStorage.downloadNativeLib(urlLib, LoginView.this);
						result = null;
					} catch (Exception e) {
						result = e;
						VTLog.e("Download Native Lib error", urlLib + " " + e.getMessage());
					}
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(Exception result) {
			if (LoginView.this != null) {
				if (result != null) {
					//delete libs
					ExternalStorage.deleteNativeLibPath(LoginView.this);
					// download loi
					LoginView.this.closeProgressDialog();
					LoginView.this.showDialog(StringUtil.getString(R.string.ERR_DOWNLOAD_NATIVE_LIB));
					VTLog.d("Download Native Lib error", DateUtils.now() + " " + result.getMessage());
					ServerLogger.sendLog("Download Native Lib", result.getMessage(), false, TabletActionLogDTO.LOG_EXCEPTION);
				} else {
					//update 100% finish download native libs
					LoginView.this.updateProgressPercentDialog(100);
					VTLog.d("Download Native Lib finish", DateUtils.now());
					LoginView.this.closeProgressDialog();
					switch (typeProccess) {
						case PROCCESS_LOGIN_ONLINE:
							proccessAtferLoginOnline();
							break;
						case PROCCESS_UPDATE_VERSION_FAIL:
							proccessAtferUpdateVersionFail();
							break;
						case PROCCESS_LOGIN_OFFLINE:
							proccessAfterLoginOffline();
							break;
						case PROCCESS_LOGIN_OFFLINE_WRONG_TIME:
							processAfterLoginOfflineWrongTime();
							break;
						default:
							break;
					}
				}
				LoginView.this.isDownloadNativeLibs = false;
			}

		}
	}

	/**
	 * proccessNeedUpdateVersion
	 *
	 * @author:
	 * @since: 08:57:53 10-04-2015
	 * @return: void
	 * @throws:
	 */
	private void proccessNeedUpdateVersion() {
		VTLog.d("proccessNeedUpdateVersion", "start");
		//check native libs
		boolean isHaveNativeLib = loadNativeLib();
		// nếu đã có thư viện trong Internal Storage
		if (isHaveNativeLib) {
			LoginView.this.numErrorNativeLib = 0;
			int stateDB = GlobalUtil.checkExistsDataBase();
//			tồn tại db đã cipher
			if (stateDB == 2) {
				isRequestSynDataBeforeUpVersion = true;
				showProgressDialog(StringUtil.getString(R.string.updating_before_up_version));
				//đẩy hết dữ liệu hiện tại lên, đảm bảo đẩy hết lên mới cập nhật phiên bản
				TransactionProcessManager.getInstance().startChecking(TransactionProcessManager.SYNC_NORMAL);
			} else {
				isNeedUpdateVersionGlobal = false;
				proccessUpdateVersion();
			}
		} else {
			proccessLoadNativeLibFail(PROCCESS_NEED_UPDATE_VERSION);
		}
		VTLog.d("proccessNeedUpdateVersion", "end");
	}

	/**
	 * proccessUpdateVersion
	 *
	 * @author:
	 * @since: 08:57:43 10-04-2015
	 * @return: void
	 * @throws:
	 */
	private void proccessUpdateVersion() {
		VTLog.d("proccessUpdateVersion", "start");
		closeProgressDialog();
		if (userDTO.appVersion != null && !StringUtil.isNullOrEmpty(userDTO.appVersion.content)) {
			VTLog.d("proccessUpdateVersion", "proccess");
			String strCancel = "";
			if (userDTO.appVersion.forceDownload) {
				strCancel = StringUtil.getString(R.string.TEXT_CANCEL);
			} else {
				strCancel = StringUtil.getString(R.string.TEXT_UPDATE_LATER);
			}
//			GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.NOTICE_UPDATE_VERSION),
//					userDTO.appVersion.content, StringUtil.getString(R.string.UPDATE), 
//					ACTION_UPDATE_VERSION_SUCCESS,strCancel, ACTION_UPDATE_VERSION_FAIL, userDTO);
			GlobalUtil.showDialogConfirm(this, StringUtil.getString(
					R.string.NOTICE_UPDATE_VERSION),
					StringUtil.getString(R.string.NOTICE_UPDATE_VERSION) +
							" (" + userDTO.appVersion.version + ") " +
							StringUtil.getString(R.string.TEXT_NOTIFY_UPDATE_VERSION_LAST)
					, StringUtil.getString(R.string.UPDATE),
					ACTION_UPDATE_VERSION_SUCCESS, strCancel,
					ACTION_UPDATE_VERSION_FAIL, userDTO);
		}
		VTLog.d("proccessUpdateVersion", "end");
	}

	/**
	 * Show popup confirmed ma code dang nhap offline
	 *
	 * @author: banghn
	 */
	private void showPopupConfirmedCode() {
		Builder alert = new Builder(this);
		alert.setTitle(StringUtil.getString(R.string.TEXT_CONFIRM_LOGIN_OFFLINE));
		alert.setMessage(StringUtil.getString(R.string.TEXT_REQUIRE_INPUT_CONFIRM_CODE));

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		input.setHint(StringUtil.getString(R.string.TEXT_INPUT_CONFIRM_CODE));
		input.setTextColor(ImageUtil.getColor(R.color.WHITE));
		alert.setView(input);

		alert.setPositiveButton(StringUtil.getString(R.string.TEXT_CONFIMRED), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if (checkCodeAllowLoginOffline(value.trim().toLowerCase())) {
					//cap nhat thoi gian, xem nhu thoi gian confirmed voi CSKH la thoi giandung
					//cap nhat luu tru trang thai
					updateTimeAfterRelogin(DateUtils.now());
					GlobalInfo.getInstance().setStateConnectionMode(Constants.CONNECTION_OFFLINE);
					//thanh cong vao ben trong
					goToMainView();
				} else {
					LoginView.this.showDialog(StringUtil.getString(R.string.TEXT_INPUT_WRONG_CONFIRM_CODE_PLEASE_REINPUT));
				}
			}
		});
		alert.show();
	}

	/**
	 * Code cho phep đăng nhập offline
	 *
	 * @return
	 * @author: banghn
	 */
	private boolean checkCodeAllowLoginOffline(String code) {
		boolean isRightCode = false;
		try {
			String userName = edUserName.getText().toString().trim().toLowerCase();
			String dateNow = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_SQL_DEFAULT);
			if (StringUtil.isNullOrEmpty(code) || StringUtil.isNullOrEmpty(userName)) {
				isRightCode = false;
			} else if (code.equals(StringUtil.generateHash(dateNow, userName).substring(0, 3))
					|| code.equals(StringUtil.generateHash(dateNow, "vtvnm").substring(0, 3))) {
				isRightCode = true;
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
			ServerLogger.sendLog("Login", "check code dang nhap offline :\n"
							+ VNMTraceUnexceptionLog.getReportFromThrowable(ex),
					false, TabletActionLogDTO.LOG_EXCEPTION);
		}
		return isRightCode;
	}

	/**
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 */
	public void processAfterLoginOfflineWrongTime() {
		boolean isHaveNativeLib = loadNativeLib();
		// nếu đã có thư viện trong Internal Storage thì cho phép login
		if (isHaveNativeLib) {
			LoginView.this.numErrorNativeLib = 0;

			// cho phep nhap ma login offline xuat don hang
			boolean isAllow = cbAllowOffline.isChecked();
			if (isAllow) {
				showPopupConfirmedCode();
			} else {
				GlobalUtil.showDialogNotAllowOfflineLogin();
			}
		} else {
			proccessLoadNativeLibFail(PROCCESS_LOGIN_OFFLINE_WRONG_TIME);
		}
	}

	/**
	 * Xoa du lieu ung dung
	 */
	public void clearData() {
		GlobalUtil.showDialogConfirm(this, StringUtil.getString(R.string.TEXT_CONFIRM_CLEAR_DATA),
				StringUtil.getString(R.string.TEXT_AGREE), ACTION_CLEAR_DATA_OK,
				StringUtil.getString(R.string.TEXT_DENY), ACTION_CLEAR_DATA_CANCEL, null);
	}

	/**
	 * Backup file, xoa file, đang nhap tai khoan khac
	 */
	private class BackupDeleteFilrLoginOtherUser extends AsyncTask<String, Void, Exception> {
		@Override
		protected void onPreExecute() {
			if (!isFinishing()) {
				showProgressDialog(StringUtil.getString(R.string.TEXT_SYNC_BACKUP_DELETE_DATA));
			}
		}

		@Override
		protected Exception doInBackground(String... params) {
			try {
				GlobalUtil.backupDatabaseToZIP();
				GlobalUtil.deleteBackupDatabaseVNM();
				GlobalUtil.deleteDatabaseVNM();
				// xoa thong tin ca nhan
				GlobalUtil.clearAllDataLoginOtherUser();
			} catch (Exception e) {
				onPostExecute(e);
				closeProgressDialog();
				return e;
			}
			return null;
		}

		@Override
		protected void onPostExecute(Exception result) {
			// xu ly sau khi hoan thanh hay loi
			if (result == null) {
				VTLog.i("Start app", "onPostExecute");
				SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
				Editor prefsPrivateEditor = sharedPreferences.edit();
				prefsPrivateEditor.clear();
				prefsPrivateEditor.commit();
				edUserName.setText("");
				edPassword.setText("");
				edUserName.setEnabled(true);
				edUserName.setBackgroundResource(R.drawable.bg_edittext_login_focus);
				edUserName.setPadding(GlobalUtil.dip2Pixel(10), GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5));
				edUserName.requestFocus();
				closeProgressDialog();
			}
		}

	}
}
