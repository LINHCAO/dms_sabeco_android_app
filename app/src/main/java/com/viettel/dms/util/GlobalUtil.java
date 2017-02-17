/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.download.Compress;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.KPILogDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.me.NotifyInfoOrder;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.GlobalInfo.RightTimeInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.SecurePreferences;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.sqllite.db.DEBIT_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.DEBIT_TABLE;
import com.viettel.dms.sqllite.db.LOG_TABLE;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.guard.AccessInternetService;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.LoginView;
import com.viettel.dms.view.main.SuperToast;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.utils.VTStringUtils;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Pattern;

//import com.joanzapata.android.iconify.Iconify;

/**
 * Chua cac ham util chung trong chuong trinh
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint("SimpleDateFormat")
public class GlobalUtil {

	public static String pathSynDataFileName = "";
	static boolean keyboardVisible = false;
	public static final String LARGE = "large";
	public static final String NORMAL = "normal";
	public static final String XLARGE = "xlarge";
	public static final String UNDEFINED = "undefined";
	public static final String SMALL = "small";

	public static interface IDataValidate<T>{
		boolean valid(T ob);
	}

	public static int dip2Pixel(int dips) {
		int ret = (int) (GlobalInfo.getInstance().getAppContext()
				.getResources().getDisplayMetrics().density
				* dips + 0.5f);
		return ret;
	}

	/**
	 * Kiem tra dang ket noi mang theo gi (wifi, 2g, 3g..)
	 * 
	 * @author: BANGHN
	 * @return
	 */
	public static int getTypeNetwork() {
		int type = 0;
		try {
			if (checkNetworkAccess()) {
				ConnectivityManager cm = (ConnectivityManager) GlobalInfo.getInstance().getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);

				if (cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting()) {
					type = 1;// wifi
				} else {
					TelephonyManager tm = (TelephonyManager) GlobalInfo.getInstance().getAppContext().getSystemService(Context.TELEPHONY_SERVICE);
					int typeNetwork = tm.getNetworkType();
					if ((typeNetwork >= TelephonyManager.NETWORK_TYPE_HSDPA)) {
						type = 2;// 3g -> 4g
					} else if ((typeNetwork == TelephonyManager.NETWORK_TYPE_GPRS)) {
						type = 3;// GPRS" 2g
					} else if ((typeNetwork == TelephonyManager.NETWORK_TYPE_EDGE)) {
						type = 4;// EDGE" 2g
					}
				}
			}
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
		return type;
	}

	/**
	 * save object vao file
	 * 
	 * @author: DoanDM
	 * @param object
	 * @param fileName
	 * @return: void
	 * @throws:
	 */
	public static void saveObject(Object object, String fileName) {
		try {
			FileOutputStream fos = GlobalInfo.getInstance().getAppContext()
					.openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(object);
			oos.close();
			fos.close();
		} catch (Throwable e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	/**
	 * Server dang su dung
	 * 
	 * @author banghn
	 * @return
	 */
	public static String getServerType() {
		String linkServer = GlobalInfo.IS_SEND_DATA_VERSION ? "" : "NO_LOG_";
		if (ServerPath.SERVER_PATH.contains("biasaigon.dmsone.vn:8580")
				|| ServerPath.SERVER_PATH.contains("biasaigon.dmsone.vn:8080/rpc")
				|| ServerPath.SERVER_PATH.contains("biasaigon.dmsone.vn:8583")
				|| ServerPath.SERVER_PATH.contains("203.190.173.195:8580")
				|| ServerPath.SERVER_PATH.contains("203.190.173.195:8583")) {
			linkServer = "RE"; // "RELEASE";
		} else if (ServerPath.SERVER_PATH.contains("103.1.210.150:9190")
				|| ServerPath.SERVER_PATH.contains("103.1.210.150:9143")) {
			linkServer = "DE_PUBLIC";
		} else if (ServerPath.SERVER_PATH.contains("10.30.174.211:14443")
				|| ServerPath.SERVER_PATH.contains("10.30.174.211:14080")) {
			linkServer = "DE"; // "TEST";
		} else {
			linkServer = "DE_LOCAL";// "DEV";
		}
		if(!GlobalInfo.getInstance().IS_SEND_DATA_VERSION || GlobalInfo.getInstance().IS_EMULATOR){
			linkServer = "NO_LOG_" + linkServer;
		}
		return linkServer;
	}

	/**
	 * Backup filde DB VNM to ZIP
	 * 
	 * @author banghn
	 */
	public static void backupDatabaseToZIP() {
		try {
			File zipDir = ExternalStorage.getFileDBPath(GlobalInfo
					.getInstance().getAppContext());
			File zipDirCheck = new File( ExternalStorage.getFileDBPath(
					GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
					+ "/" + Constants.DATABASE_NAME);
			if(zipDirCheck.exists()){
				// File path to store .zip file before unzipping
				String nameZip = "/"
						+ DateUtils
						.getCurrentDateTimeWithFormat("yyyy_MM_dd_HH_mm_SS")
						+ ".zip";
				File zipFile = new File(zipDir.getPath() + nameZip);
				String dbFilePath = ExternalStorage.getFileDBPath(
						GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
						+ "/" + Constants.DATABASE_NAME;

				Compress compress = new Compress(new String[] { dbFilePath },
						zipFile.getPath());
				compress.zip();
			}
		} catch (Throwable e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	
	/**
	 * Delete nhung file anh chup truoc ngay hien tai
	 * @author: BANGHN
	 */
	public static void deleteTempTakenPhoto(){
		try{
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"dd_MM_yyyy_HH_mm_SS");
			Date d2 = dateFormat.parse(DateUtils
					.getCurrentDateTimeWithFormat("dd_MM_yyyy_HH_mm_SS"));
			File yourDir = new File(ExternalStorage.getTakenPhotoPath(
					GlobalInfo.getInstance().getAppContext()).getAbsolutePath());
			String name;
			for (File f : yourDir.listFiles()) {
				if (f.isFile() && f.getName().length() > 20 && f.getName().contains("_")) {
					try {// co the co file ko phai dinh dang ngay thang
						name = f.getName();
						name = name.substring(11, 30);
						Date d1 = dateFormat.parse(name);
						int days = DateUtils.daysBetween(d1, d2);
						if (days > 10) {
							f.delete();
						}
					} catch (Exception e) {
					}
				}
			}
		} catch (Exception e) {

		}
	}
	
	/**
	 * Xoa file DB
	 * 
	 * @author banghn
	 */
	public static void deleteDatabaseVNM() {
		try {
			File file = new File(
					ExternalStorage.getFileDBPath(
							GlobalInfo.getInstance().getAppContext())
							.getAbsolutePath(), Constants.DATABASE_NAME);
			file.delete();
			if (!SQLUtils.getInstance().isProcessingTrans) {
				// cancel sqlLite
				SQLUtils.getInstance().closeDB();
			}
		} catch (Exception e) {
			VTLog.i("error", e.toString());
			ServerLogger.sendLog("DeleteDatabase",
					e.getMessage() + "\n" + e.toString(), false,
					TabletActionLogDTO.LOG_EXCEPTION);
		}
	}
	
	/**
	 * Xoa file DB
	 * 
	 * @author banghn
	 */
	/**
	 * clear SharedPreferences khi cap nhat version moi mà bat reset DB
	 * 
	 * @author: hoanpd1
	 * @since: 17:12:03 05-08-2014
	 * @return: void
	 * @throws:
	 */
	public void clearSharedPreferences() {
		try {
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			sharedPreferences.edit().clear().commit();
		} catch (Exception e) {
			VTLog.i("error", e.toString());
			ServerLogger.sendLog("Clear SharedPreferences: ",
					e.getMessage() + "\n" + e.toString(), false,
					TabletActionLogDTO.LOG_EXCEPTION);
		}
	}

	/**
	 * Xoa nhung file backup database VNM
	 * 
	 * @author banghn
	 */
	public static void deleteBackupDatabaseVNM() {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy_MM_dd_HH_mm_SS");
			Date d2 = dateFormat.parse(DateUtils
					.getCurrentDateTimeWithFormat("yyyy_MM_dd_HH_mm_SS"));
			File yourDir = new File(ExternalStorage.getFileDBPath(
					GlobalInfo.getInstance().getAppContext()).getAbsolutePath());
			String name;
			for (File f : yourDir.listFiles()) {
				if (f.isFile() && f.getName().length() > 20
						&& f.getName().contains("_")) {
					try {// co file ko phai dinh dang ngay thang
						name = f.getName();
						name = name.substring(0, 20);
						Date d1 = dateFormat.parse(name);
						int days = DateUtils.daysBetween(d1, d2);
						if (days > 10) {
							f.delete();
						}
					} catch (Exception e) {
						// ServerLogger.sendLog("DeleteDatabaseBackup1",
						// e.getMessage() + "\n" + e.toString(),
						// false, TabletActionLogDTO.LOG_EXCEPTION);
					}
				}
			}
		} catch (Exception e) {
			ServerLogger.sendLog("DeleteDatabaseBackup", e.getMessage() + "\n"
					+ e.toString(), false, TabletActionLogDTO.LOG_EXCEPTION);
		}
	}

	/**
	 * Kiem tra ton tai database hay khong
	 * 
	 * @author: BangHN
	 * @return: boolean
	 * @throws:
	 */
	public static int checkExistsDataBase() {
		// chua co DB
		int isExistData = 0;

		File dbFile = new File(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath() + "/" + Constants.DATABASE_NAME);

		if (!dbFile.exists()) {
			isExistData = 0;
			return isExistData;
		}

		SQLiteDatabase checkDB = null;
		// String verDB = GlobalInfo.getInstance().getProfile().getVersionDB();
//		SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {
//			@Override
//			public void preKey(SQLiteDatabase db) {
//				db.rawExecSQL("PRAGMA cipher = 'rc4'");
//			}
//
//			@Override
//			public void postKey(SQLiteDatabase db) {
//				db.rawExecSQL("PRAGMA cipher = 'rc4'");
//			}
//		};

		// kiem tra xem co ton tai DB Cipher hay ko
		try {
//			checkDB = SQLiteDatabase.openDatabase(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
//					+ "/" + Constants.DATABASE_NAME, Constants.CIPHER_KEY, null, SQLiteDatabase.OPEN_READWRITE, hook);
			checkDB = SQLiteDatabase.openDatabase(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
					+ "/" + Constants.DATABASE_NAME, Constants.CIPHER_KEY, null, SQLiteDatabase.OPEN_READWRITE, null);
			if (checkDB != null) {
				// da ton tai DB Cipher
				isExistData = 2;
				return isExistData;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (checkDB != null) {
					checkDB.close();
					checkDB = null;
				}
			} catch (Exception e) {
			}
		}

		// kiem tra xem co ton tai DB Not_Cipher hay ko
		try {
			checkDB = SQLiteDatabase.openDatabase(ExternalStorage.getFileDBPath(GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
					+ "/" + Constants.DATABASE_NAME, "", null, SQLiteDatabase.OPEN_READONLY);
			if (checkDB != null) {
				isExistData = 1; // da ton tai DB chua Cipher
				return isExistData;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			try {
				if (checkDB != null) {
					checkDB.close();
					checkDB = null;
				}
			} catch (Exception e) {
			}
		}

		return isExistData;
	}

	/**
	 * Doc file
	 * 
	 * @author: DoanDM
	 * @param fileName
	 * @return: void
	 * @throws:
	 */
	public static Object readObject(String fileName) {
		Object object = null;
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		try {
			if (isExistFile(fileName)) {
				fis = GlobalInfo.getInstance().getAppContext()
						.openFileInput(fileName);
				if (fis != null) {// ton tai file
					ois = new ObjectInputStream(fis);
					object = ois.readObject();

				}
			}
		} catch (Throwable e) {
			object = null;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} finally {
			try {
				ois.close();
				fis.close();
			} catch (Exception e) {
			}
		}
		return object;
	}

	/**
	 * Kiem tra file ton tai hay khong
	 * 
	 * @author DoanDM
	 * @param fileName
	 * @return
	 */
	public static boolean isExistFile(String fileName) {
		try {
			if (!StringUtil.isNullOrEmpty(fileName)) {
				String[] s = GlobalInfo.getInstance().getAppContext()
						.fileList();
				for (int i = 0, size = s.length; i < size; i++) {
					if (fileName.equals(s[i].toString())) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return false;
	}

	/*
	 * delete mot thu muc
	 */
	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				new CacheCleanTask().execute(new File(dir, children[i]));
				(new File(dir, children[i])).delete();
			}
		}
	}


	/**
	 * Hien thi man hinh setting thoi gian
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public static void showDialogSettingTime() {
		AlertDialog alertDialog = null;
		try {
			alertDialog = new AlertDialog.Builder(GlobalInfo.getInstance()
					.getActivityContext()).create();
			alertDialog.setCanceledOnTouchOutside(false);
			alertDialog.setCancelable(false);
			alertDialog.setTitle(StringUtil.getString(R.string.TEXT_WARNING));
			alertDialog.setMessage(StringUtil
					.getString(R.string.ERROR_TIME_ONLINE_INVALID));
			alertDialog.setButton(
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// hien thi man hinh setting
							GlobalInfo
									.getInstance()
									.getActivityContext()
									.startActivity(
											new Intent(
													android.provider.Settings.ACTION_DATE_SETTINGS));
							//khong cam setting neu bi chan
							AccessInternetService.unlockAppPrevent(true);
							return;

						}
					});
			alertDialog.show();
		} catch (Throwable ex) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	
	public static void showDialogNotAllowOfflineLogin() {
		try {
			SpannableObject strNotice = new SpannableObject();
			String dateOfWeekRight = "";
			String dateOfWeekNow = DateUtils.getDayOfWeek(new Date());
			
			String additionString = "\n";
			strNotice.addSpan(StringUtil.getString(R.string.ERROR_NOT_ALLOW_OFFLINE), ImageUtil.getColor(R.color.WHITE),
					Typeface.NORMAL);
			
			RightTimeInfo rightTimeInfo = GlobalInfo.getInstance().getRightTimeInfo();
			try {
				String lastTimeOnline = rightTimeInfo.lastTimeOnlineLogin;
    			
				if (!StringUtil.isNullOrEmpty(lastTimeOnline) && !"null".equals(lastTimeOnline)) {
					Date dFromLastLogIn = DateUtils.parseDateFromString(lastTimeOnline, DateUtils.DATE_FORMAT_NOW);
					Date rightTime = new Date(dFromLastLogIn.getTime());
					dateOfWeekRight = DateUtils.getDayOfWeek(rightTime);
	    			String dateRight = dateOfWeekRight + (dateOfWeekRight.length() < dateOfWeekNow.length() ? "\t\t\t" : "\t\t") + DateUtils.formatDate(rightTime, DateUtils.DATE_TIME_FORMAT_VN);
	    			
					strNotice.addSpan(additionString + StringUtil.getString(R.string.LAST_TIME_ONLINE) + ":\t\t", ImageUtil.getColor(R.color.WHITE),
							Typeface.NORMAL);
					strNotice.addSpan(dateRight, ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
				}
			} catch (Exception e) {}
			
			strNotice.addSpan(additionString + StringUtil.getString(R.string.CURRENT_TIME) + ":\t\t\t\t\t", ImageUtil.getColor(R.color.WHITE),
					Typeface.NORMAL);
			String dateNow = dateOfWeekNow + (dateOfWeekNow.length() < dateOfWeekRight.length() ? "\t\t\t" : "\t\t") + DateUtils.nowVN();
			strNotice.addSpan(dateNow, ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
			
			AlertDialog alertDialogNotAllowOfflineLogin = null;
			alertDialogNotAllowOfflineLogin = new AlertDialog.Builder(GlobalInfo.getInstance().getActivityContext()).create();
			alertDialogNotAllowOfflineLogin.setCanceledOnTouchOutside(false);
			alertDialogNotAllowOfflineLogin.setCancelable(false);
			alertDialogNotAllowOfflineLogin.setTitle(StringUtil.getString(R.string.TEXT_WARNING));
			alertDialogNotAllowOfflineLogin.setMessage(strNotice.getSpan());
			alertDialogNotAllowOfflineLogin.setButton(StringUtil.getString(R.string.TEXT_BUTTON_RELOGIN), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					return;
				}
			});
			alertDialogNotAllowOfflineLogin.show();
		} catch (Throwable ex) {
			VTLog.e("showDialogNotAllowOfflineLogin", ex.getMessage());
		}
	}
	
	static AlertDialog alertDialogCheckOnlineInDate = null;
	/**
	 * Hien thi dialog kiem tra thoi gian online hien tai
	 * @author: duongdt3
	 * @param wrongTimeType { {@link DateUtils#RIGHT_TIME DateUtils.RIGHT_TIME}, DateUtils.WRONG_DATE, DateUtils.WRONG_TIME, DateUtils.WRONG_TIME_BOOT_REASON}
	 * @since: 08:31:29 19 Mar 2014
	 * @return: void
	 * @throws:
	 */
	public static void showDialogCheckWrongTime(int wrongTimeType) {
		
		if (alertDialogAutoTime == null || !alertDialogAutoTime.isShowing()) {
    		try {
    			//nếu tồn tại 1 dialog đang show, ẩn nó đi, hiện cái mới lên
        		if(alertDialogCheckOnlineInDate != null && alertDialogCheckOnlineInDate.isShowing()){
        			alertDialogCheckOnlineInDate.dismiss();
        		}
        		RightTimeInfo rightTimeInfo = GlobalInfo.getInstance().getRightTimeInfo();
    			String lastRightTime = rightTimeInfo.lastRightTime; 
        		SpannableObject strNotice = new SpannableObject();
        		
        		strNotice.addSpan(StringUtil.getString(R.string.ERROR_TIME_TABLET_INVALID), ImageUtil.getColor(R.color.WHITE), Typeface.NORMAL);
        		//neu co thoi gian cuoi online, thi hien them vao thong bao
        		if (!StringUtil.isNullOrEmpty(lastRightTime) && !"null".equals(lastRightTime)) {
        			long lastTimeOnlineFromBoot = rightTimeInfo.lastRightTimeSinceBoot;
    				Date dLastRightTime = DateUtils.parseDateFromString(lastRightTime, DateUtils.DATE_FORMAT_NOW);
    				long timeSinceBootNow = SystemClock.elapsedRealtime();	
    				long dis = (timeSinceBootNow - lastTimeOnlineFromBoot);
    				if (dis < 0) {
    					dis = 0;
    				}
    				String dateOfWeekRight = "";
        			String dateOfWeekNow = DateUtils.getDayOfWeek(new Date());
    				Date rightTime = new Date(dLastRightTime.getTime() + dis);
    				dateOfWeekRight = DateUtils.getDayOfWeek(rightTime);
        			String dateRight = dateOfWeekRight + (dateOfWeekRight.length() < dateOfWeekNow.length() ? ",\t\t\t" : "\t\t") + DateUtils.formatDate(rightTime, DateUtils.DATE_TIME_FORMAT_VN);
        			String dateNow = dateOfWeekNow + (dateOfWeekNow.length() < dateOfWeekRight.length() ? ",\t\t\t" : "\t\t") + DateUtils.nowVN();
        			
        			String additionString = "\n";
        			strNotice.addSpan(additionString + StringUtil.getString(R.string.RIGHT_TIME) + ":\t\t", ImageUtil.getColor(R.color.WHITE),
        					Typeface.NORMAL);
        			strNotice.addSpan(dateRight, ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
        			strNotice.addSpan(additionString + StringUtil.getString(R.string.CURRENT_TIME) + ":\t\t\t\t\t", ImageUtil.getColor(R.color.WHITE),
        					Typeface.NORMAL);
        			strNotice.addSpan(dateNow, ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
        		}        		
				alertDialogCheckOnlineInDate = new AlertDialog.Builder(GlobalInfo.getInstance().getActivityContext()).create();
				alertDialogCheckOnlineInDate.setCanceledOnTouchOutside(false);
				alertDialogCheckOnlineInDate.setCancelable(false);
				alertDialogCheckOnlineInDate.setTitle(StringUtil.getString(R.string.TEXT_WARNING));
				alertDialogCheckOnlineInDate.setButton(StringUtil.getString(R.string.TEXT_BUTTON_TIME_SETTING), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						// hien thi man hinh setting
						GlobalInfo.getInstance().getActivityContext().startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
						return;
					}
				});
				alertDialogCheckOnlineInDate.setButton2(StringUtil.getString(R.string.TEXT_BUTTON_RELOGIN), new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						((GlobalBaseActivity)GlobalInfo.getInstance().getActivityContext()).sendBroadcast(ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN,
								new Bundle());
						//khong cam setting neu bi chan
						AccessInternetService.unlockAppPrevent(true);
						return;
					}
				});

				alertDialogCheckOnlineInDate.setMessage(strNotice.getSpan());
				alertDialogCheckOnlineInDate.show();
    			
    		} catch (Throwable ex) {
    			VTLog.e("showDialogCheckWrongTime", ex.getMessage());
    		}
		}
	}
	

	public static AlertDialog alertDialogAutoTime = null;
	/**
	 * Hien thi man hinh setting thoi gian bat buoc chon auto setting time
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public static void showDialogSettingTimeAutomatic() {
		String language = Locale.getDefault().getDisplayLanguage();
		SpannableObject strNotice = new SpannableObject();
		String additionString = "";
		if(!GlobalUtil.isSettingAutoTimeUpdate()){
    		if (language.equals("Tiếng Việt")) {
    			strNotice.addSpan(StringUtil.getString(R.string.TEXT_MSG_CHOOSE_OPTION), ImageUtil.getColor(R.color.WHITE),
    					Typeface.NORMAL);
    			strNotice.addSpan("[Thời gian tự động] ", ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
    			strNotice.addSpan(StringUtil.getString(R.string.TEXT_AND), ImageUtil.getColor(R.color.WHITE),
    					Typeface.NORMAL);
    			strNotice.addSpan("[Múi giờ tự động]", ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
    			strNotice.addSpan(StringUtil.getString(R.string.TEXT_FOR_TABLET), ImageUtil.getColor(R.color.WHITE),
    					Typeface.NORMAL);
    		} else {
    			strNotice.addSpan(StringUtil.getString(R.string.TEXT_MSG_CHOOSE_OPTION), ImageUtil.getColor(R.color.WHITE),
    					Typeface.NORMAL);
    			strNotice.addSpan("[Automatic date and time] ", ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
    			strNotice.addSpan(StringUtil.getString(R.string.TEXT_AND), ImageUtil.getColor(R.color.WHITE),
    					Typeface.NORMAL);
    			strNotice.addSpan("[Automatic time zone]", ImageUtil.getColor(R.color.COLOR_LINK), Typeface.NORMAL);
    			strNotice.addSpan(StringUtil.getString(R.string.TEXT_FOR_TABLET), ImageUtil.getColor(R.color.WHITE),
    					Typeface.NORMAL);
    		}
    		additionString = "\n";
		}
		
		if (!GlobalUtil.isSettingRightTimeZone()) {
			strNotice.addSpan( additionString + StringUtil.getString(R.string.TEXT_MSG_CHOOSE_GMT7), ImageUtil.getColor(R.color.WHITE),
					Typeface.NORMAL);
		}
		
		try {
			if (alertDialogAutoTime != null && alertDialogAutoTime.isShowing()) {
				alertDialogAutoTime.dismiss();
			}
			
			alertDialogAutoTime = new AlertDialog.Builder(GlobalInfo.getInstance().getActivityContext()).create();
			alertDialogAutoTime.setIcon(R.drawable.icon_warning);
			alertDialogAutoTime.setCanceledOnTouchOutside(false);
			alertDialogAutoTime.setCancelable(false);
			alertDialogAutoTime.setTitle(StringUtil.getString(R.string.TEXT_WARNING));
			alertDialogAutoTime.setButton(StringUtil.getString(R.string.TEXT_BUTTON_TIME_SETTING), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					// hien thi man hinh setting
					GlobalInfo.getInstance().getActivityContext().startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
					//khong cam setting neu bi chan
					AccessInternetService.unlockAppPrevent(true);
					return;

				}
			});
			//thay đổi thông tin
			alertDialogAutoTime.setMessage(strNotice.getSpan());
			//hiện lại
			alertDialogAutoTime.show();
		} catch (Throwable ex) {
			VTLog.e("showDialogSettingTimeAutomatic", ex.getMessage());
		}
	}

	/**
	 * show dialog confirm setting gps
	 * 
	 * @author banghn
	 */
	public static void showDialogSettingGPS() {
		AlertDialog alertDialog = null;
		try {
			alertDialog = new AlertDialog.Builder(GlobalInfo.getInstance()
					.getActivityContext()).create();
			alertDialog.setMessage(StringUtil
					.getString(R.string.NOTIFY_SETTING_GPS));
			alertDialog.setButton(
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// hien thi man hinh setting
							GlobalInfo
									.getInstance()
									.getActivityContext()
									.startActivity(
											new Intent(
													android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
							//khong cam setting neu bi chan
							AccessInternetService.unlockAppPrevent(true);
							return;

						}
					});
			alertDialog.show();
		} catch (Throwable ex) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}

	/**
	 * show dialog confirm 2 button
	 * 
	 * @author: HieuNH
	 * @param view
	 * @param notice
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @return: void
	 * @throws:
	 */
	public static void showDialogConfirm(final GlobalBaseActivity view,
			CharSequence notice, String ok, final int actionOk, String cancel,
			final int actionCancel, final Object data) {
		if (view != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(view).create();
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	/**
	 * Show dialog confrimed with action user define
	 * 
	 * @param view
	 *            : activity
	 * @param title
	 *            : Title caption thong bao
	 * @param notice
	 *            : Noi dung thong bao
	 * @param ok
	 *            : Text dong y
	 * @param actionOk
	 *            : action dong y
	 * @param cancel
	 *            : Text cancel
	 * @param actionCancel
	 *            : Action cancel
	 * @param data
	 *            : user data define
	 */
	public static void showDialogConfirm(final GlobalBaseActivity view,
			String title, CharSequence notice, String ok, final int actionOk,
			String cancel, final int actionCancel, final Object data) {
		if (view != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(view).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	/**
	 * Show dialog confrimed with action user define
	 * 
	 * @param view
	 *            : fragment dang dung
	 * @param title
	 *            : Title caption thong bao
	 * @param notice
	 *            : Noi dung thong bao
	 * @param ok
	 *            : Text dong y
	 * @param actionOk
	 *            : action dong y
	 * @param cancel
	 *            : Text cancel
	 * @param actionCancel
	 *            : Action cancel
	 * @param data
	 *            : user data define
	 */
	public static void showDialogConfirm(final BaseFragment view, CharSequence title,
			CharSequence notice, String ok, final int actionOk, String cancel,
			final int actionCancel, final Object data) {
		if (view != null && view.getActivity() != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(
					view.getActivity()).create();
			alertDialog.setTitle(title);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	public static void showDialogConfirm(final BaseFragment fragment,
			View view, String title, CharSequence notice, String ok,
			final int actionOk, String cancel, final int actionCancel,
			final Object data) {
		if (view != null && fragment.getActivity() != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(
					fragment.getActivity()).create();
			alertDialog.setTitle(title);
			alertDialog.setView(view);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	/**
	 * Show dialog ho tro add view nguoi dung (checkbox, textbox,...)
	 * 
	 * @author: BANGHN
	 * @param fragment
	 *            : fragment dang dung
	 * @param view
	 *            : view nguoi dung add them vao
	 * @param title
	 *            : tile dialog
	 * @param notice
	 *            : noi dung thong bao
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @param isCanBack
	 * @param isTouchOutSide
	 */
	public static void showDialogConfirmCanBackAndTouchOutSide(
			final BaseFragment fragment, View view, String title,
			CharSequence notice, String ok, final int actionOk, String cancel,
			final int actionCancel, final Object data, boolean isCanBack,
			boolean isTouchOutSide) {
		if (view != null && fragment.getActivity() != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(
					fragment.getActivity()).create();
			alertDialog.setTitle(title);
			alertDialog.setView(view);
			alertDialog.setCancelable(isCanBack);
			alertDialog.setCanceledOnTouchOutside(isTouchOutSide);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	/**
	 * Show dialog voi 1 action
	 * 
	 * @author: TruongHN
	 * @param view
	 * @param message
	 * @param ok
	 * @param actionOk
	 * @param data
	 * @return: void
	 * @throws:
	 */
	public static void showDialogConfirm(final BaseFragment fragment,
			GlobalBaseActivity view, CharSequence message, String ok,
			final int actionOk, final Object data, boolean isCanBack) {
		if (view != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(view).create();
			alertDialog.setCancelable(isCanBack);
			alertDialog.setMessage(message);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	/**
	 * 
	 * Show dialog cho phep back or touch outside
	 * 
	 * @author:
	 * @param fragment
	 * @param view
	 * @param notice
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @return: void
	 * @throws:
	 */
	public static void showDialogConfirm(final BaseFragment fragment,
			GlobalBaseActivity view, CharSequence notice, String ok,
			final int actionOk, String cancel, final int actionCancel,
			final Object data) {
		showDialogConfirmCanBack(fragment, view, notice, ok, actionOk, cancel,
				actionCancel, data, true);
	}

	/**
	 * 
	 * Show dialog confirm with allow can back or not
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param fragment
	 * @param view
	 * @param notice
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @param isCanBack
	 * @return: void
	 * @throws:
	 */
	public static void showDialogConfirmCanBack(final BaseFragment fragment,
			final GlobalBaseActivity view, CharSequence notice, String ok,
			final int actionOk, String cancel, final int actionCancel,
			final Object data, boolean isCanBack) {
		if (view != null) {

			AlertDialog alertDialog = GlobalInfo.getInstance().getAlertDialog();
			alertDialog.setCancelable(isCanBack);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									int which) {
								fragment.onEvent(actionOk, null, data);	
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									int which) {
								fragment.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}

			if (!alertDialog.isShowing())
				alertDialog.show();
		}
	}

	/**
	 * 
	 * Show dialog confirm with allow can back or not
	 * 
	 * @author: TamPQ
	 * @param fragment
	 * @param view
	 * @param notice
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @param isCanBack
	 * @return: void
	 * @throws:
	 */
	public static void showDialogConfirmCanBackAndTouchOutSide(
			final BaseFragment fragment, GlobalBaseActivity view,
			CharSequence notice, String ok, final int actionOk, String cancel,
			final int actionCancel, final Object data, boolean isCanBack,
			boolean isTouchOutSide) {
		if (view != null) {
			AlertDialog alertDialog = GlobalInfo.getInstance().getAlertDialog();
			alertDialog.setCancelable(isCanBack);
			alertDialog.setCanceledOnTouchOutside(isTouchOutSide);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								fragment.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}

			if (!alertDialog.isShowing())
				alertDialog.show();
		}
	}

	/**
	 * 
	 * Show dialog confirm with allow can back or not
	 * 
	 * @author: TamPQ
	 * @param activity
	 * @param notice
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @param isCanBack
	 * @return: void
	 * @throws:
	 */
	public static void showDialogConfirmCanBackAndTouchOutSide(
			final GlobalBaseActivity activity, CharSequence notice, String ok,
			final int actionOk, String cancel, final int actionCancel,
			final Object data, boolean isCanBack, boolean isTouchOutSide) {
		if (activity != null) {
			AlertDialog alertDialog = GlobalInfo.getInstance().getAlertDialog();
			alertDialog.setCancelable(isCanBack);
			alertDialog.setCanceledOnTouchOutside(isTouchOutSide);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								activity.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								activity.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}

			if (!alertDialog.isShowing())
				alertDialog.show();
		}
	}

	/**
	 * Dialog confirm co them icon title
	 * 
	 * @param view
	 * @param icon
	 * @param title
	 * @param notice
	 * @param ok
	 * @param actionOk
	 * @param cancel
	 * @param actionCancel
	 * @param data
	 * @param isCanBack
	 * @param isTouchOutSide
	 */
	public static void showDialogConfirm(final GlobalBaseActivity view,
			int icon, String title, CharSequence notice, String ok,
			final int actionOk, String cancel, final int actionCancel,
			final Object data, boolean isCanBack, boolean isTouchOutSide) {
		if (view != null) {
			AlertDialog alertDialog = new AlertDialog.Builder(view).create();
			alertDialog.setIcon(icon);
			alertDialog.setTitle(title);
			alertDialog.setCancelable(isCanBack);
			alertDialog.setCanceledOnTouchOutside(isTouchOutSide);
			alertDialog.setMessage(notice);
			if (ok != null && !Constants.STR_BLANK.equals(ok)) {
				alertDialog.setButton(ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionOk, null, data);
								dialog.dismiss();
							}
						});
			}
			if (cancel != null && !Constants.STR_BLANK.equals(cancel)) {
				alertDialog.setButton2(cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								view.onEvent(actionCancel, null, data);
								dialog.dismiss();
							}
						});
			}
			alertDialog.show();
		}
	}

	/**
	 * Removes the reference to the activity from every view in a view hierarchy
	 * (listeners, images etc.). This method should be called in the onDestroy()
	 * method of each activity
	 * 
	 * @author: PhucNT
	 * @param view
	 *            normally the id of the root layout
	 * @return
	 * @throws:
	 */

	public static void nullViewDrawablesRecursive(View view) {
		if (view != null) {
			try {
				ViewGroup viewGroup = (ViewGroup) view;

				int childCount = viewGroup.getChildCount();
				for (int index = 0; index < childCount; index++) {
					View child = viewGroup.getChildAt(index);
					nullViewDrawablesRecursive(child);
				}
			} catch (Exception e) {
			}

			unbindViewReferences(view);
		}
	}

	/**
	 * Remove view reference
	 * 
	 * @author: TruongHN
	 * @param view
	 * @return: void
	 * @throws:
	 */
	private static void unbindViewReferences(View view) {
		// set all listeners to null (not every view and not every API level
		// supports the methods)
		try {
			view.setOnClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnCreateContextMenuListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnFocusChangeListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnKeyListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnLongClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;
		try {
			view.setOnClickListener(null);
		} catch (Throwable mayHappen) {
		}
		;

		// set background to null
		Drawable d = view.getBackground();
		if (d != null)
			d.setCallback(null);
		if (view instanceof ImageView) {
			ImageView imageView = (ImageView) view;
			d = imageView.getDrawable();
			if (d != null)
				d.setCallback(null);
			imageView.setImageDrawable(null);
			imageView.setBackgroundDrawable(null);
			imageView = null;
		}

		// destroy webview
		if (view instanceof WebView) {
			((WebView) view).destroyDrawingCache();
			((WebView) view).destroy();
		}
		view = null;
	}

	/**
	 * Kiem tra request co can luu ko
	 * 
	 * @author : DoanDM since : 11:10:57 AM
	 */
	public static boolean checkActionSave(int action) {
		for (int i = 0, size = ActionEventConstant.LIST_ACTION_NOT_SAVE.length; i < size; i++) {
			if (action == ActionEventConstant.LIST_ACTION_NOT_SAVE[i]) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Tao log_id
	 * 
	 * @author: TruongHN
	 * @return: String
	 * @throws:
	 */
	public static String generateLogId() {
		StringBuffer logId = new StringBuffer();
		String id = String.valueOf(System.currentTimeMillis());
		logId.append(id);
		if (id.length() < Constants.MAX_LENGTH_TRANSACTION_ID) {
			logId.append("_");
			logId.append(rand(0, 99));
		}
		logId.append("_");
		logId.append(GlobalInfo.getInstance().getProfile().getUserData().id);
		return logId.toString();
	}

	/**
	 * 
	 * Generate id dua vao object id va timestamp
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param tableName
	 * @param objectId
	 * @return
	 * @return: String
	 * @throws:
	 */
	public static String generateRandomId(String tableName, String objectId) {
		StringBuffer logId = new StringBuffer();
		String id = String.valueOf(System.currentTimeMillis());
		if (LOG_TABLE.TABLE_NAME.equals(tableName)) {
			logId.append(id);
			if (id.length() < Constants.MAX_LENGTH_TRANSACTION_ID) {
				logId.append("_");
				logId.append(rand(0, 99));
			}
			logId.append("_");
			logId.append(objectId);
		} else if (DEBIT_TABLE.TABLE_NAME.equals(tableName)) {
			int remainLength = Constants.MAX_LENGTH_RANDOM_ID
					- objectId.length();
			if (id.length() > remainLength) {
				id = id.substring(id.length() - remainLength, id.length());
			}
			logId.append(objectId);
			logId.append(id);
		} else if (DEBIT_DETAIL_TABLE.TABLE_NAME.equals(tableName)) {
			int remainLength = Constants.MAX_LENGTH_RANDOM_ID
					- objectId.length();
			if (id.length() > remainLength) {
				id = id.substring(id.length() - remainLength, id.length());
			}

			logId.append(objectId);
			logId.append(id);
		}

		return logId.toString();
	}

	/**
	 * tao so ngau nhien trong pham vi
	 * 
	 * @param lo
	 *            : so ngau nhien thap nhat
	 * @param hi
	 *            : so ngau nhien cao nhat
	 * @return
	 */
	public static int rand(int lo, int hi) {
		Random rand = new Random();

		int n = hi - lo + 1;
		int i = rand.nextInt() % n;
		if (i < 0)
			i = -i;
		return lo + i;
	}

	/**
	 * Tao json name, value
	 * 
	 * @author: TruongHN
	 * @param name
	 * @param value
	 * @return: JSONObject
	 * @throws:
	 */
	public static JSONObject getJsonColumn(String name, Object value,
			String type) {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_COLUMN, name);
			json.put(IntentConstants.INTENT_VALUE, value);
			if (!StringUtil.isNullOrEmpty(type)) {
				json.put(IntentConstants.INTENT_TYPE, type);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * Tao json name, value, key
	 * 
	 * @author: TruongHN
	 * @param name
	 * @param value
	 * @return: JSONObject
	 * @throws:
	 */
	public static JSONObject getJsonColumnWithKey(String name, Object value,
			String type) {
		JSONObject json = new JSONObject();
		try { 
			json.put(IntentConstants.INTENT_KEY, "ISPK");
			json.put(IntentConstants.INTENT_COLUMN, name);
			json.put(IntentConstants.INTENT_VALUE, value);
			if (!StringUtil.isNullOrEmpty(type)) {
				json.put(IntentConstants.INTENT_TYPE, type);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	} 
	
	/**
	 * Tao json name, value, key
	 * Cau nay dung cho truong hop InsertOrUpdate, neu muon chi thuc hien luc update, insert thi bo qua
	 * Vd: updateUser, updateDate chi muon thuc hien luc update.
	 * @author: quangvt1
	 * @since: 16:06:08 27-06-2014
	 * @return: JSONObject
	 * @throws:  
	 * @param name
	 * @param value
	 * @param type
	 * @return
	 */
	public static JSONObject getJsonColumnIgnoreInsert(String name, Object value,
			String type) {
		JSONObject json = new JSONObject();
		try { 
			json.put(IntentConstants.INTENT_KEY, "IGNORE_INSERT");
			json.put(IntentConstants.INTENT_COLUMN, name);
			json.put(IntentConstants.INTENT_VALUE, value);
			if (!StringUtil.isNullOrEmpty(type)) {
				json.put(IntentConstants.INTENT_TYPE, type);
			}
		} catch (JSONException e) { 
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * Tao json cho menh de where
	 * 
	 * @author: TruongHN
	 * @param name
	 * @param value
	 * @param operator
	 * @return
	 * @return: JSONObject
	 * @throws:
	 */
	public static JSONObject getJsonColumnWhere(String name, Object value,
			String operator) {
		JSONObject json = new JSONObject();
		try {
			json.put(IntentConstants.INTENT_COLUMN, name);
			json.put(IntentConstants.INTENT_VALUE, value);
			if (!StringUtil.isNullOrEmpty(operator)) {
				json.put(IntentConstants.INTENT_OPERATOR, operator);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return json;
	}

	/**
	 * khoang cach giua 2 diem
	 * 
	 * @author: BangHN
	 * @param sourcePoint
	 *            : dau
	 * @param destinationPoint
	 *            : cuoi
	 * @return khoang cach (m)
	 * @return: double
	 * @throws:
	 */
	public static double getDistanceBetween(LatLng sourcePoint,
			LatLng destinationPoint) {
		int EARTH_RADIUS = 6378137; // ban kinh trai dat theo don vi m
		if (sourcePoint == null || destinationPoint == null) {
			return 0;
		}
		double R = EARTH_RADIUS / 1000; // Radius of the earth in km
		double dLat = Math.toRadians(destinationPoint.lat - sourcePoint.lat);
		double dLon = Math.toRadians(destinationPoint.lng - sourcePoint.lng);

		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(sourcePoint.lat))
				* Math.cos(Math.toRadians(destinationPoint.lat))
				* Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = (R * c) * 1000; // Distance in m
		return Math.round(distance);
	}

//	/**
//	 * Tinh goc giua 2 diem tren ban do tao nen
//	 * 
//	 * @author banghn
//	 * @param sourcePoint
//	 *            : Diem dau
//	 * @param destinationPoint
//	 *            : Diem cuoi
//	 * @since 1.0
//	 * @return
//	 */
//	public static double getAngle(GEPoint2D sourcePoint,
//			GEPoint2D destinationPoint) {
//		double dx = destinationPoint.x - sourcePoint.x;
//		double dy = -(destinationPoint.y - sourcePoint.y);
//		double inRads = Math.atan2(dy, dx);
//		if (inRads < 0) {
//			inRads = Math.abs(inRads);
//		} else {
//			inRads = 2 * Math.PI - inRads;
//		}
//		return Math.toDegrees(inRads);
//	}

	/**
	 * Set chieu dai toi da cua edit otp bang voi chuoi otp
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public static void setFilterInputConvfact(EditText edEditText, int length) {
		// TODO Auto-generated method stub
		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				String result = "";
				for (int i = start; i < end; i++) {
					if (Character.isDigit(source.charAt(i))
							|| source.charAt(i) == '/') {
						result = result + source.charAt(i);
					}
				}
				return result;
			}
		};

		filterArray[1] = new InputFilter.LengthFilter(length);
		if (edEditText != null) {
			edEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
			edEditText.setFilters(filterArray);
		}

	}
	
	/**
	 * 
	*  thiet lap validate du lieu nhap vao cho textbox dang nhap tien
	*  @author: HaiTC3
	*  @param edEditText
	*  @param length
	*  @return: void
	*  @throws:
	*  @since: May 9, 2013
	 */
	public static void setFilterInputMoney(EditText edEditText, int length) {
		// TODO Auto-generated method stub
		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				String result = "";
				for (int i = start; i < end; i++) {
					if (Character.isDigit(source.charAt(i))
							|| source.charAt(i) == ',') {
						result = result + source.charAt(i);
					}
				}
				return result;
			}
		};
		
		filterArray[1] = new InputFilter.LengthFilter(length);
		if (edEditText != null) {
			edEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
			edEditText.setFilters(filterArray);
		}
		
	}
	
	/**
	 * 
	*  thiet lap validate du lieu nhap vao cho textbox dang nhap tien
	*  @author: Nguyen Thanh Dung
	*  @param edEditText
	*  @param length
	*  @return: void
	*  @throws:
	*  @since: May 9, 2013
	 */
	public static void setFilterInputPrice(EditText edEditText, int length) {
		// TODO Auto-generated method stub
		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				String result = "";
				for (int i = start; i < end; i++) {
					if (Character.isDigit(source.charAt(i))
							|| source.charAt(i) == ',') {
						result = result + source.charAt(i);
					}
				}
				return result;
			}
		};
		
		filterArray[1] = new InputFilter.LengthFilter(length);
		if (edEditText != null) {
//			edEditText.setInputType(InputType.TYPE_);
			edEditText.setFilters(filterArray);
		}
		
	}

	/**
	 * 
	 * Filter de remove cac ky tu dac biet cua sql
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param edEditText
	 * @param length
	 * @return: void
	 * @throws:
	 */
	public static void setFilterInputForSearchLike(EditText edEditText,
			int length) {
		// TODO Auto-generated method stub
		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
					Spanned dest, int dstart, int dend) {
				// TODO Auto-generated method stub
				StringBuilder text = new StringBuilder();
				String OpPat = ",;&"; // search operator pattern
				String SpPat = "<>./!@#$%^*'\"-_():|[]~+{}?\\\n"; // special
																	// char
				boolean preCheck = true;
				for (int i = start; i < end; i++) {
					boolean canAdd = true;

					if (SpPat.indexOf(source.charAt(i)) >= 0) {
						canAdd = false;
					} else if (OpPat.indexOf(source.charAt(i)) >= 0) {
						if (preCheck) {
							canAdd = false;
						}
						preCheck = true;
					} else
						preCheck = false;

					if (canAdd) {
						text.append(source.charAt(i));
					}
				}
				return text.toString();
			}
		};

		filterArray[1] = new InputFilter.LengthFilter(length);
		if (edEditText != null) {
			// edEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
			edEditText.setFilters(filterArray);
		}

	}

	/**
	 * Tinh gia tri thuc dat de luu DB
	 * 
	 * @author: TruongHN
	 * @param value
	 * @param convfact
	 * @return: int
	 * @throws:
	 */
	public static int calRealOrder(String value, int convfact) {
		int realOrder = 0;
		if (isValidQuantity(value)) {
			// chi duoc phep 1 ky tu '/'
			String[] arr = value.split("/");
			if (arr.length <= 2 && arr.length > 0) {
				if (arr.length == 1) {
					if (value.contains("/")) {
						// thung: vd 9/
						realOrder = Integer.valueOf(arr[0]) * convfact;
					} else {
						// hop
						realOrder = Integer.valueOf(arr[0]);
					}

				} else {
					int n1 = 0;
					int n2 = 0;
					if (!StringUtil.isNullOrEmpty(arr[0])) {
						n1 = Integer.valueOf(arr[0]);
					}
					if (!StringUtil.isNullOrEmpty(arr[1])) {
						n2 = Integer.valueOf(arr[1]);
					}
					realOrder = n1 * convfact + n2;
				}
			}
		}
		return realOrder;
	}

	/**
	 * 
	 * dinh dang so luong san pham theo dang thung / hop
	 * 
	 * @author: HaiTC3
	 * @param numberProduct
	 * @param convfact
	 * @return
	 * @return: String
	 * @throws:
	 * @since: May 6, 2013
	 */
	public static String formatNumberProductFlowConvfact(long numberProduct,
			int convfact) {
		StringBuilder availableProductFormat = new StringBuilder();
		availableProductFormat.append("0/0");
		
		if (numberProduct != 0 && convfact != 0) {
			availableProductFormat.setLength(0);
			if (numberProduct < 0) {
				availableProductFormat.append("-");
			}
			availableProductFormat
					.append(StringUtil.parseAmountMoney(Math.abs(numberProduct
							/ convfact)))
					.append("/")
					.append(StringUtil.parseAmountMoney(Math.abs(numberProduct
							% convfact)));
			//		} else {
//			availableProductFormat = "0/0";
		}
		return availableProductFormat .toString();
	}

	/**
	 * Kiem tra so luong nhap co hop le hay khong
	 * 
	 * @author: TruongHN
	 * @param name
	 * @return: boolean
	 * @throws:
	 */
	public static boolean isValidQuantity(String name) {
		// Thuc dat chi chua cac 0-9 va ky tu /
		Boolean isValid = false;
		int length = name.length();
		for (int i = 0; i < length; i++) {
			char ch = name.charAt(i);
			if (((ch >= '0' && ch <= '9') || (ch == '/'))) {
				isValid = true;
			} else {
				isValid = false;
				break;
			}
		}
		return isValid;
	}

	/**
	 * set enable button send in ugc
	 * 
	 * @author: BangHN
	 * @param enable
	 * @return: void
	 * @throws:
	 */
	public static void setEnableButton(Button button, boolean enable) {
		if (button == null) {
			return;
		}
		if (enable) {
			button.setEnabled(true);
			button.setBackgroundResource(R.drawable.vnm_button_selector);
		} else {
			button.setEnabled(false);
			button.setBackgroundResource(R.drawable.bg_vnm_button_invisible);
		}

	}

	/**
	 * set enable button send in ugc
	 * 
	 * @author: BangHN
	 * @param enable
	 * @return: void
	 * @throws:
	 */
	public static void setEnableEditTextClear(VNMEditTextClearable button,
			boolean enable) {
		if (button == null) {
			return;
		}
		if (enable) {
			button.setEnabled(true);
			button.setBackgroundResource(R.drawable.bg_white_rounded);
		} else {
			button.setEnabled(false);
			button.setBackgroundResource(R.drawable.bg_white_rounded_disable);
		}
	}

	/**
	 * set max lenght cho edit text
	 * 
	 * @author: AnhND
	 * @param edText
	 * @param maxLength
	 * @return: void
	 * @throws:
	 */
	public static void setEditTextMaxLength(EditText edText, int maxLength) {
		InputFilter[] inputFilters = new InputFilter[1];
		inputFilters[0] = new InputFilter.LengthFilter(maxLength);
		edText.setFilters(inputFilters);
	}

	/**
	 * Kiem tra mang
	 * 
	 * @author: TruongHN
	 * @return: boolean
	 * @throws:
	 */
	public static boolean checkNetworkAccess() {
		boolean res = false;
		ConnectivityManager cm = (ConnectivityManager) GlobalInfo.getInstance()
				.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mobileInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if ((mobileInfo != null && mobileInfo.isConnectedOrConnecting()) || (wifiInfo!= null && wifiInfo.isConnectedOrConnecting())) {
			VTLog.i("Test Network Access", "Network available!");
			res = true;
		} else {
			VTLog.i("Test Network Access", "No network available!");
		}
//		return false;
		//no_log
		if(GlobalInfo.getInstance().IS_EMULATOR) {
			return true;
		}else{
			return res;
		}
	}

	/**
	 * Set mobile data enable || disable
	 * 
	 * @author banghn
	 * @param context
	 * @param enabled
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void setMobileDataEnabled(Context context, boolean enabled) {
		try {
			final ConnectivityManager conman = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			final Class conmanClass = Class
					.forName(conman.getClass().getName());
			final Field iConnectivityManagerField = conmanClass
					.getDeclaredField("mService");
			iConnectivityManagerField.setAccessible(true);
			final Object iConnectivityManager = iConnectivityManagerField
					.get(conman);
			final Class iConnectivityManagerClass = Class
					.forName(iConnectivityManager.getClass().getName());
			final Method setMobileDataEnabledMethod = iConnectivityManagerClass
					.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
			setMobileDataEnabledMethod.setAccessible(true);

			setMobileDataEnabledMethod.invoke(iConnectivityManager, enabled);
		} catch (Throwable e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	/**
	 * Back lai man hinh truoc khi khong nhan phim back mac dinh
	 * 
	 * @author: TruongHN
	 * @param activity
	 * @return: void
	 * @throws:
	 */
	public static void popBackStack(Activity activity) {
		if (activity != null) {
			GlobalInfo.getInstance().popCurrentTag();
			activity.getFragmentManager().popBackStack();
		}
	}

	/**
	 * FOrce hide Keyboard
	 * 
	 * @author: VietHQ
	 * @param activity
	 * @return: void
	 * @throws:
	 */
	public static void forceHideKeyboard(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			InputMethodManager inputManager = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
					.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	/**
	 * Hide keyboard with input, use in Popup
	 * * @author: DuongDT
	 * @param context
	 * @param input
	 */
	public static void forceHideKeyboardInput(Context context, View input) {
			InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
	}

	/**
	 * 
	 * hide keyboard when before user show keyboard use Toggle.
	 * 
	 * @param activity
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Dec 25, 2012
	 */
	public static void forceHideKeyboardUseToggle(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			if (keyboardVisible) {
				InputMethodManager imm = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.toggleSoftInput(0, 0);
				}
				keyboardVisible = false;
			} else {
				InputMethodManager inputManager = (InputMethodManager) activity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 
	 * show keyboard use ToogleSoftInput
	 * 
	 * @param activity
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Dec 25, 2012
	 */
	public static void showKeyboardUseToggle(Activity activity) {
		if (activity != null && activity.getCurrentFocus() != null) {
			InputMethodManager imm = (InputMethodManager) activity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					InputMethodManager.HIDE_IMPLICIT_ONLY);
			keyboardVisible = true;
		}
	}

	/**
	 * Lay method hien tai dang thuc thi
	 * 
	 * @author: TruongHN
	 * @return: String
	 * @throws:
	 */
	public static String getCurrentMethod() {
		String method = "";
		try {
			StackTraceElement[] stacktrace = Thread.currentThread()
					.getStackTrace();
			if (stacktrace.length > 1) {
				StackTraceElement e = stacktrace[1];
				method = e.getMethodName();
			}

		} catch (SecurityException e) {
			// TODO: handle exception
		}
		return method;
	}

	/**
	 * chup hinh
	 * 
	 * @author: AnhND
	 * @param sender
	 * @param requestCode
	 * @return
	 * @return: File
	 * @throws:
	 */
	public static File takePhoto(GlobalBaseActivity sender, int requestCode) {
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		String fileName = Constants.TEMP_IMG + "_" + DateUtils.getCurrentDateTimeWithFormat(null) + ".jpg";
		File retFile = new File(ExternalStorage.getTakenPhotoPath(sender), fileName);
		if (!retFile.exists())
			try {
				retFile.createNewFile();
			} catch (IOException e) { 
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(retFile));
		sender.startActivityForResult(intent, requestCode);
		return retFile;
	}

	public static File takePhoto(BaseFragment sender, int requestCode) {
		final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); 
		String fileName = Constants.TEMP_IMG + "_" + DateUtils.getCurrentDateTimeWithFormat(null) + ".jpg";
		File retFile = new File(ExternalStorage.getTakenPhotoPath(sender.getActivity()), fileName);
		if (!retFile.exists())
			try {
				retFile.createNewFile();
			} catch (IOException e) { 
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(retFile));
		sender.startActivityForResult(intent, requestCode);
		return retFile;
	}

	/**
	 * Chuyen doi so luong sang quy cach
	 * 
	 * @author: TruongHN
	 * @return: String
	 * @throws:
	 */
	public static String convertQuantityToRealOrderOnView(int quantity,
			int convfact) {
		String res = "";
		if (quantity > 0 && convfact > 0) {
			res = (quantity / convfact) + "/" + (quantity % convfact);
			if (res.length() > Constants.MAX_LENGHT_QUANTITY) {
				int index = res.indexOf("/");
				String hop = res.substring(index + 1);
				if(Integer.parseInt(hop) == 0) {
					res = res.substring(0, index + 1);
				} else {
					res = String.valueOf(quantity);
				}
			}
		}
		return res;
	}

	/**
	 * Doc file sang mang byte
	 * 
	 * @author TamPQ
	 * @param filePath
	 * @return
	 */
	public static byte[] readFileInByteArray(String filePath) {
		// create file object
		File file = new File(filePath);
		byte fileContent[] = null;
		try {
			// create FileInputStream object
			@SuppressWarnings("resource")
			FileInputStream fin = new FileInputStream(file);

			/*
			 * Create byte array large enough to hold the content of the file.
			 * Use File.length to determine size of the file in bytes.
			 */

			fileContent = new byte[(int) file.length()];

			/*
			 * To read content of the file in byte array, use int read(byte[]
			 * byteArray) method of java FileInputStream class.
			 */
			fin.read(fileContent);

			// create string from byte array
			String strFileContent = new String(fileContent);

			VTLog.i("File content : ", strFileContent);
		} catch (FileNotFoundException e) {
			VTLog.i("File content : ", e.toString());
		} catch (IOException ioe) {
			VTLog.i("Exception while reading the file ", ioe.toString());
		}
		return fileContent;
	}

	/**
	 * clear cache (xoa) hinh
	 * 
	 * @author phucnt
	 */
	public static class CacheCleanTask extends AsyncTask<File, Void, Void> {
		@Override
		protected Void doInBackground(File... files) {
			try {
				walkDir(files[0]);
			} catch (Throwable t) {
				VTLog.e("PhucNT4", "Exception cleaning cache", t);
			}
			return null;
		}

		void walkDir(File dir) {
			if (dir.isDirectory()) {
				String[] children = dir.list();

				for (int i = 0; i < children.length; i++) {
					walkDir(new File(dir, children[i]));
				}
			} else {
				dir.delete();
			}
		}
	}

	/**
	 * Clear data khi thoat chuong trinh
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public static void clearAllData() {
		// cancel position
		PositionManager.getInstance().stop();
		// cancel thong bao ngoai chuong trinh
		GlobalInfo.getInstance().getStatusNotifier().cancelNotifications();
		GlobalInfo.getInstance().setTimeSyncToServer(0);
		GlobalInfo.getInstance().setAllowEditPromotion(-1);
		// cancel luong dong bo
		TransactionProcessManager.getInstance().cancelTimer();
		// xoa thong tin ca nhan
		GlobalInfo.getInstance().getProfile().clearProfile();
		GlobalInfo.getInstance().notifyOrderReturnInfo = new NotifyInfoOrder();
		// cap nhat thoat ung dung
		GlobalInfo.getInstance().isExitApp = true;
		// kiem tra dong database
		if (!SQLUtils.getInstance().isProcessingTrans) {
			// cancel sqlLite
			SQLUtils.getInstance().closeDB();
		}
	}

	/**
	 * 
	 * Noi dung can mo ta : kiem tra thiet bi co cho phep xu dung vi tri ao
	 * khong (root location)
	 * 
	 * @author: DucHHA
	 * @return: boolean: true neu root, nguoc lai false
	 * @throws:
	 */
	public static boolean isMockLocation() {
		if (Settings.Secure.getString(
				GlobalInfo.getInstance().getActivityContext()
						.getContentResolver(),
				Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")) {
			return false;
		} else {
			return true;

		}
	}

	/**
	 * 
	 * Noi dung can mo ta: Kiem tra va xu ly thiet bi xem gia lap vi tri ao
	 * (root location) hay khong ,neu co hien thi thong bao cho nguoi dung biet
	 * de tat tool cho phep su dung vi tri ao hoac thoat khoi chuong trinh neu
	 * khong thi thoat khoi chuong trinh. nguoc lai chuong trinh chay binh
	 * thuong
	 * 
	 * @author: DucHHA
	 * @param: language: ngon ngu hien thi hien tai cua thiet bi
	 * @return: void
	 * @throws:
	 */
	public static void checkAllowMockLocation() {
		String language = Locale.getDefault().getDisplayLanguage();
		// code here
		if (language.equals("Tiếng Việt")) {
			GlobalUtil.showDialogConfirm((GlobalBaseActivity) GlobalInfo
					.getInstance().getActivityContext(),
					R.drawable.icon_warning, StringUtil
							.getString(R.string.TEXT_WARNING),
					"Vui lòng bỏ tùy chọn [Cho Phép Vị Trí Giả] ", StringUtil
							.getString(R.string.TEXT_AGREE),
					GlobalBaseActivity.ACTION_ALLOW_MOCK_LOCATION_OK,
					StringUtil.getString(R.string.TEXT_DENY),
					GlobalBaseActivity.ACTION_ALLOW_MOCK_LOCATION_CANCEL, null,
					false, false);
		} else {
			GlobalUtil.showDialogConfirm((GlobalBaseActivity) GlobalInfo
					.getInstance().getActivityContext(),
					R.drawable.icon_warning, StringUtil
							.getString(R.string.TEXT_WARNING),
					"Vui lòng bỏ tùy chọn [ALLOW MOCK LOCATION] ", StringUtil
							.getString(R.string.TEXT_AGREE),
					GlobalBaseActivity.ACTION_ALLOW_MOCK_LOCATION_OK,
					StringUtil.getString(R.string.TEXT_DENY),
					GlobalBaseActivity.ACTION_ALLOW_MOCK_LOCATION_CANCEL, null,
					false, false);
		}
	}

	/**
	 * Get mang byte de tao chuoi MD5 checksum
	 * 
	 * @author banghn
	 * @param filename
	 * @return
	 * @throws
	 */
	public static byte[] createChecksum(String filename) {
		InputStream fis = null;
		byte[] result = null;
		try {
			fis = new FileInputStream(filename);
			byte[] buffer = new byte[1024];
			MessageDigest complete = MessageDigest.getInstance("MD5");
			int numRead;
			do {
				numRead = fis.read(buffer);
				if (numRead > 0) {
					complete.update(buffer, 0, numRead);
				}
			} while (numRead != -1);
			result = complete.digest();
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} catch (Throwable th) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(th));
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				}
			}
		}
		return result;
	}

	/**
	 * Tao chuoi check sum MD5
	 * 
	 * @author banghn
	 * @param filename
	 * @return
	 * @throws Exception
	 */
	public static String getMD5Checksum(String filename) {
		byte[] b = createChecksum(filename);
		String result = "";
		if (b != null) {
			try {
				result = StringUtil.getHexStringFaster(b);
			} catch (UnsupportedEncodingException u) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(u));
			} catch (Exception e) {
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
			}
		}
		return result;
	}

	/**
	 * Tao chuoi check sum MD5 cho file DB VINMAILK
	 * 
	 * @author banghn
	 * @return
	 * @throws Exception
	 */
	public static String getMD5DBChecksum() {
		String md5Checksum = "";
		try {
			String dbFilePath = ExternalStorage.getFileDBPath(
					GlobalInfo.getInstance().getAppContext()).getAbsolutePath()
					+ "/" + Constants.DATABASE_NAME;
			md5Checksum = getMD5Checksum(dbFilePath);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		} catch (Throwable th) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(th));
		}
		VTLog.i("MD5Checksum", ".......chuoi MD5 checksum: " + md5Checksum);
		return md5Checksum;
	}

	/**
	 * Checksum md5 file database truoc khi thoat app
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public static void checksumMD5Database() {
		try {
			String md5Checksum = GlobalUtil.getMD5DBChecksum();
			SharedPreferences sharedPreferences = new SecurePreferences(
							GlobalInfo.getInstance().getActivityContext()
									.getPackageName(), Context.MODE_PRIVATE);
			Editor prefsPrivateEditor = sharedPreferences.edit();
			prefsPrivateEditor.putString(LoginView.DMS_MD5_CHECKSUM_DB,
					md5Checksum);
			prefsPrivateEditor.commit();
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		} catch (Throwable th) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(th));
		}
	}

	static Object obLock = new Object();
	/**
	 * Kiem tra co dang bat co don't keep activities hay khong
	 * 
	 * @author banghn
	 * @return
	 */
	public static void checkStatusAlwaysFinishActivities() {
		synchronized (obLock) {
			if (Settings.System.getInt(GlobalInfo.getInstance()
					.getActivityContext().getContentResolver(),
					Settings.System.ALWAYS_FINISH_ACTIVITIES, 0) > 0) {
				// Settings.System.putInt(GlobalInfo.getInstance().getActivityContext().getContentResolver(),
				// Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
				writeFinishOptions();
				// int pid = android.os.Process.myPid();
				// android.os.Process.killProcess(pid);
			}
		}
	}

	/**
	 * update setting: setAlwaysFinish
	 * 
	 * @author banghn
	 */
	private static void writeFinishOptions() {
		try {
			// Due to restrictions related to hidden APIs, need to emulate the
			// line below
			// using reflection:
			// ActivityManagerNative.getDefault().setAlwaysFinish(mAlwaysFinish);
			final Class<?> classActivityManagerNative = Class
					.forName("android.app.ActivityManagerNative");
			final Method methodGetDefault = classActivityManagerNative
					.getMethod("getDefault");
			final Method methodSetAlwaysFinish = classActivityManagerNative
					.getMethod("setAlwaysFinish", new Class[] { boolean.class });
			final Object objectInstance = methodGetDefault.invoke(null);
			boolean mAlwaysFinish = false;
			methodSetAlwaysFinish.invoke(objectInstance,
					new Object[] { mAlwaysFinish });
		} catch (Exception ex) {
			VTLog.i("GlobalUtil", ex.toString());
		}
	}

	/**
	 * Kiem tra co setting thoi gian theo che do auto hay khong
	 * 
	 * @author banghn
	 * @return
	 */
	public static boolean isSettingAutoTimeUpdate() {
		boolean isAutoTimeUpdate = true;
		if (Settings.System.getInt(GlobalInfo.getInstance()
				.getAppContext().getContentResolver(),
				Settings.System.AUTO_TIME, 0) <= 0
				|| Settings.System.getInt(GlobalInfo.getInstance()
						.getAppContext().getContentResolver(),
						Settings.System.AUTO_TIME_ZONE, 0) <= 0) {
			isAutoTimeUpdate = false;
		}
		return isAutoTimeUpdate;
	}
	
	/**
	 * Kiem tra file ton tai trong thu muc hay khong
	 * @author: BANGHN
	 * @param dir : Thu muc
	 * @param fileName : ten file 
	 * @return true: co - false:khong
	 */
	public static boolean isFileExistsInDirectory(File dir, String fileName){
		boolean isExists = false;
		File myFile = new File(dir.getAbsolutePath() + "/" +  fileName);
		if(myFile.exists()){
			isExists = true;
		}
		return isExists;
	}
	
	/**
	 * Kiem tra mot toa do co nam gan toa do NPP
	 * @author: BANGHN
	 * @param location
	 * @return
	 */
	public static boolean isNearNPPPosition(Location location){
		boolean isNear = false;
		double shopLat = GlobalInfo.getInstance().getProfile().getUserData().shopLat;
		double shopLng = GlobalInfo.getInstance().getProfile().getUserData().shopLng;
		if(location != null && shopLat > 0 && shopLng > 0){
			if (getDistanceBetween(new LatLng(location.getLatitude(), location.getLongitude()), 
					new LatLng(shopLat, shopLng)) <= GlobalInfo.getInstance().getCcDistance()){
				isNear = true;
			}
		}
		return isNear;
	}
	
	/**
	 * Kiem tra mot toa do co nam gan toa do khach hang dang ghe tham
	 * @author: BANGHN
	 * @param location
	 * @return
	 */
	public static boolean isNearCustomerVisitingPosition(Location location){
		boolean isNear = false;
		if(GlobalInfo.getInstance().getPositionCustomerVisiting() != null && location != null){
			if (getDistanceBetween(new LatLng(location.getLatitude(), location.getLongitude()),
					GlobalInfo.getInstance().getPositionCustomerVisiting()) <= 300) {
				isNear = true;
			}
		}
		return isNear;
	}
	
	/**
	 * set array view is BOLD
	 * @author: duongdt3
	 * @since: 11:51:08 2 Dec 2013
	 * @return: void
	 * @throws:
	 */
	public static void setBoldArrayViewInRow(View [] arrView){
		for (int i = 0; i < arrView.length; i++) {
			View view  = arrView[i];
			setBoldView(view);
		}
	}
	
	/**
	 * Tô đậm 1 view, nếu là text view
	 * @author: duongdt3
	 * @since: 09:23:05 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param view
	 */
	public static void setBoldView(View view){
		if (view instanceof TextView) {
			((TextView)view).setTypeface(null, Typeface.BOLD);
		}
	}
	
	/**
	 * Set text color for row
	 * @author: duongdt3
	 * @since: 11:08:30 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param arrView
	 * @param color Color int -> get by ImageUtils.getColor(idResource)
	 */
	public static void setTextColorViewInRow(View [] arrView, int color){
		for (int i = 0; i < arrView.length; i++) {
			View view  = arrView[i];
			setTextColorView(view, color);
		}
	}
	
	/**
	 * Hiện màu cho 1 view, nếu là TextView
	 * @author: duongdt3
	 * @since: 09:23:47 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param view
	 * @param color
	 */
	public static void setTextColorView(View view, int color){
		if (view instanceof TextView) {
			((TextView)view).setTextColor(color);
		}
	}
	
	/**
	 * Tong so dong trong file
	 * @author: banghn
	 * @param file
	 * @return
	 */
	public static long countLineOfFile(File file) {
		int lines = 0;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.readLine() != null)
				lines++;
			reader.close();
		} catch (Throwable e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return lines;
	} 
	
	/**
	 * Request ghi log KPI
	 * 
	 * @param type
	 * @param startFromBoot
	 */
	public static void requestInsertLogKPI(HashMapKPI type, long startFromBoot) {
		// nếu thời gian start từ lúc boot hợp lệ, tránh trùng
		if (startFromBoot > 0) {
			long endFromBoot = SystemClock.elapsedRealtime();
			Calendar endTimeKPI = DateUtils.getRightCalTimeNow();
			long totalExecutationTime = endFromBoot - startFromBoot;
			// tính start từ end
			Calendar startTimeKPI = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"));
			startTimeKPI.setTime(endTimeKPI.getTime());
			startTimeKPI.add(Calendar.MILLISECOND, (-(int) totalExecutationTime));

			SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATE_FORMAT_NOW);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
			// MyLog.i("KPI: " + type.getNote(), totalExecutationTime + "");
			requestInsertLogKPI(type, sdf.format(startTimeKPI.getTime()), sdf.format(endTimeKPI.getTime()), totalExecutationTime);
		} else {
			ServerLogger.sendLog("KPI START 0: " + type.getNote() + " TIME: "
					+ DateUtils.getRightTimeNow(), TabletActionLogDTO.LOG_CLIENT);
		}
	}

	/**
	 * Request ghi log KPI
	 * 
	 * @param type
	 * @param startTimeKPI
	 */
	public static void requestInsertLogKPI(HashMapKPI type, String startTimeKPI, String endTimeKPI, long totalExecutationTime) {
		try {
			if (startTimeKPI != null && endTimeKPI != null) {
				// tính toán thời gian thực thi
				int key = type.ordinal();
				int valueKey = GlobalInfo.getInstance().getHashMapKPI().get(key);
				// kiểm tra và ghi vào HashMap
				// nếu nhân viên hiện tại đang cho phép ghi log + ( loại LOGIN,
				// SYN_DATA luôn cho ghi || những loại khác chưa vượt quá qui
				// định số lượng log )
				if (GlobalInfo.getInstance().getProfile().getUserData().enableClientLog == 1
						&& (key == HashMapKPI.GLOBAL_SYN_DATA.ordinal()
								|| key == HashMapKPI.GLOBAL_LOGIN.ordinal() || valueKey < GlobalInfo.getInstance().getAllowRequestLogKPINumber())) {
					KPILogDTO kpiLogDTO = new KPILogDTO();
					kpiLogDTO.setClientLat(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude());
					kpiLogDTO.setClientLng(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());
					kpiLogDTO.setCreateDate(endTimeKPI);
					kpiLogDTO.setMobileData(GlobalUtil.getTypeNetwork());
					kpiLogDTO.setServiceBeginTime(startTimeKPI);
					kpiLogDTO.setServiceEndTime(endTimeKPI);
					kpiLogDTO.setServiceCode(type.getNote());
					if(!VTStringUtils.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
						kpiLogDTO.setShopId(Long.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
					}
					kpiLogDTO.setStaffId(GlobalInfo.getInstance().getProfile().getUserData().id);
					kpiLogDTO.setTotalExecutationTime(totalExecutationTime);

					// thêm những thuộc tính khác
					// nếu là đăng nhập thì sẽ có thêm IMEI + SERIAL
					if (key == HashMapKPI.GLOBAL_LOGIN.ordinal()) {
						kpiLogDTO.setIMEI(GlobalInfo.getInstance().getDeviceIMEI());
						kpiLogDTO.setSerialSim(StringUtil.getSimSerialNumber());
					}

					String addNote = "";
					if (key == HashMapKPI.GLOBAL_LOGIN.ordinal()) {
						//add thêm thông tin thiết bị khi đăng nhập
						addNote += getSystemInfoStr();
						addNote += " " + getHeapInfo();
					}

					// thêm ghi chú cho KPI
					String note = getKPINote() + " " + addNote;
					kpiLogDTO.setNote(note);

					GlobalInfo.getInstance().addKPILog(kpiLogDTO);
					GlobalInfo.getInstance().putHashMapKPI(key, valueKey + 1);
					// MyLog.i("KPI:", "Save to HashMap");
				}

			}
		} catch (Throwable exception) {
			VTLog.e("requestInsertLogKPI:", VNMTraceUnexceptionLog.getReportFromThrowable(exception));
			ServerLogger.sendLog("requestInsertLogKPI:"
					+ VNMTraceUnexceptionLog.getReportFromThrowable(exception), TabletActionLogDTO.LOG_CLIENT);
		}
	}

	/**
	 * Lấy KPI NOTE
	 * 
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 13:52:03 26 Jun 2014
	 * @return: String
	 * @throws:
	 * @return
	 */
	public static String getKPINote() {
		StringBuffer buffer = new StringBuffer();

		// xem phần trăm pin
		buffer.append("PIN: " + getBatteryPercent() + "% ");

		// xem lượng RAM còn lại
		buffer.append("RAM: " + getMemoryAv() + "MB ");

		// giờ hệ thống của máy tablet
		buffer.append("TIME: " + DateUtils.now() + " ");

		// xem số lượng ứng dụng đang chạy hiện tại, tối đa 20 ứng dụng
		List<RunningTaskInfo> lTask = getListTaskRunning(20);
		if (lTask != null && !lTask.isEmpty()) {
			String apps = "APP: ";
			for (RunningTaskInfo runningTaskInfo : lTask) {
				if (runningTaskInfo != null
						&& runningTaskInfo.topActivity != null) {
					String appName = runningTaskInfo.topActivity.getPackageName();
					apps += appName + ";";
				}
			}

			// kiểm tra danh sách ứng dụng hiện tại có thay đổi hay không.
			if (!apps.equals(GlobalInfo.getInstance().lastAppsList)) {
				buffer.append(apps);
				// set lại danh sách ứng dụng mới
				GlobalInfo.getInstance().lastAppsList = apps;
			}
		}

		//add thêm thông tin GPS provider hiện tại
		String gpsProviderListStr = getGpsProviderListStr();
		buffer.append(" " + gpsProviderListStr);

		return buffer.toString();
	}

	/**
	 * Get % pin
	 * 
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 09:59:37 26 Jun 2014
	 * @return: float
	 * @throws:
	 * @return
	 */
	public static int getBatteryPercent() {
		Intent batteryIntent = GlobalInfo.getInstance().getAppContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		float level = (float) batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
		float scale = (float) batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

		// Error checking that probably isn't needed but I added just in case.
		if (level < 0 || scale < 0) {
			return -1;
		}
		return (int) ((level / scale) * 100.0f);
	}

	/**
	 * Lấy lượng RAM còn lại
	 * 
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 11:23:05 26 Jun 2014
	 * @return: long
	 * @throws:
	 * @return
	 */
	public static long getMemoryAv() {
		MemoryInfo mi = new MemoryInfo();
		ActivityManager activityManager = (ActivityManager) GlobalInfo.getInstance().getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
		activityManager.getMemoryInfo(mi);
		long availableMegs = mi.availMem / 1048576L;
		return availableMegs;
	}

	/**
	 * Danh sách ứng dụng đang chạy foreground
	 * 
	 * @author: duongdt3
	 * @since: 1.0
	 * @time: 13:51:50 26 Jun 2014
	 * @return: List<RunningTaskInfo>
	 * @throws:
	 * @param maxList
	 * @return
	 */
	public static List<RunningTaskInfo> getListTaskRunning(int maxList) {
		ActivityManager am = (ActivityManager) GlobalInfo.getInstance().getAppContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> procInfos = am.getRunningTasks(maxList);
		return procInfos;
	}
	
	/**
	 * Check TimeZone 
	 * @author: duongdt3
	 * @since: 10:49:34 7 Mar 2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	public static boolean isSettingRightTimeZone() {
		return DateUtils.isGMT7TimeZone();
	}

	public static boolean isRightSettingTime() {
		boolean isRightSettingTime = true;
		if (!isSettingAutoTimeUpdate() || !isSettingRightTimeZone()) {
			isRightSettingTime = false;
		}
		return isRightSettingTime;
	}
	/**
	 * get soft app version
	 * @author: duongdt3
	 * @since: 18:08:02 26 Nov 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	public static String getAppVersion(){
		String result = "";
		try {
			result = GlobalInfo.getInstance().getPackageManager()
					.getPackageInfo(GlobalInfo.getInstance().getPackageName(), 0).versionName;
		} catch (Exception e) {
		}
		return result;
	}
	
	/**
	 * get intent send mail
	 * @author: duongdt3
	 * @since: 09:34:08 29 Jan 2015
	 * @return: Intent
	 * @throws:  
	 * @param subject
	 * @param sendToList
	 * @param content
	 * @param filePath
	 * @return
	 */
	public static Intent getIntentSendMail(String subject, String[] sendToList, String content, String filePath) {
	    Intent emailIntent = new Intent(Intent.ACTION_SEND);
	    emailIntent.putExtra(Intent.EXTRA_EMAIL, sendToList);
	    emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
	    emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
	    emailIntent.setType("text/plain");
		// convert from paths to Android friendly Parcelable Uri's
		File fileIn = new File(filePath);
		Uri u = Uri.fromFile(fileIn);
		emailIntent.putExtra(Intent.EXTRA_STREAM, u);
		
		return emailIntent;
	}

	/**
	 * Lay dpi man hinh
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	public static int getDPIScreen() {
		DisplayMetrics dm = GlobalInfo.getInstance().getAppContext()
				.getResources().getDisplayMetrics();
		int width = dm.widthPixels;
		int height = dm.heightPixels;

		int deviceWith = Math.max(width, height);
		//margin left + right in FragmentView
		int totalMarginTable = dip2Pixel(Constants.LEFT_MARGIN_TABLE_DIP) + dip2Pixel(Constants.RIGHT_MARGIN_TABLE_DIP);
		//padding left + right in DMSTableView
		int totalPaddingTable = dip2Pixel(1) + dip2Pixel(1);

		// tru margin left right 10dp
		int result = (int) (deviceWith) - totalMarginTable - totalPaddingTable;
		//Log.e("getDPIScreen DMSTableView", String.format("deviceWith: %d, result: %d", deviceWith, result));
		return result;
	}

	static final int TYPE_BACKGROUND_DRAWABLE = 1;
	static final int TYPE_BACKGROUND_COLOR = 2;
	/**
	 * Set background for View, color get by ImageUtil.getColor(idResourceColor)
	 * 
	 * @param v
	 * @param color
	 */
	public static void setBackgroundByColor(View v, int color) {
		setBackgroundView(v, color, TYPE_BACKGROUND_COLOR);
	}
	/**
	 * Set background for View, by drawable selector
	 * 
	 * @param v
	 * @param idResourceBackground
	 */
	public static void setBackgroundByDrawable(View v, int idResourceBackground) {
		setBackgroundView(v, idResourceBackground, TYPE_BACKGROUND_DRAWABLE);
	}
	/**
	 * setBackgroundView
	 * @author: 
	 * @since: 14:59:21 21-04-2015
	 * @return: void
	 * @throws:  
	 * @param v
	 * @param resource
	 * @param typeBackground
	 */
	static void setBackgroundView(View v, int resource, int typeBackground) {
		if (v != null) {
			// store layout param
			int paddingLeft = v.getPaddingLeft();
			int paddingRight = v.getPaddingRight();
			int paddingTop = v.getPaddingTop();
			int paddingBottom = v.getPaddingBottom();
			ViewGroup.LayoutParams viewParam = v.getLayoutParams();
			
			switch (typeBackground) {
			case TYPE_BACKGROUND_DRAWABLE:
				// set background
				v.setBackgroundResource(resource);
				break;
			case TYPE_BACKGROUND_COLOR:
				// set background
				v.setBackgroundColor(resource);
				break;
				
			default:
				break;
			}
			
			// re layout
			if (viewParam != null) {
				v.setLayoutParams(viewParam);
			}
			
			v.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
		}
	}
	/**
	 * set textview color for multi textview
	 * @author: duongdt3
	 * @since: 09:52:37 13 Apr 2015
	 * @return: void
	 * @throws:
	 * @param color
	 * @param views
	 */
	public static void setTextColor(int color, TextView ... views){
		for (int i = 0; i < views.length; i++) {
			TextView v = views[i];
			if (v != null) {
				v.setTextColor(color);
			}
		}
	}
	/**
	 * set background view
	 * @author: duongdt3
	 * @since: 14:04:55 17 Apr 2015
	 * @return: void
	 * @throws:  
	 * @param color
	 * @param views
	 */
	public static void setBackgroundColor(int color, View ... views){
		for (int i = 0; i < views.length; i++) {
			View v = views[i];
			if (v != null) {
				setBackgroundByColor(v, color);
			}
		}
	}
	 /**
	 * set type face
	 * @author: Tuanlt11
	 * @param type
	 * @param views
	 * @return: void
	 * @throws:
	*/
	public static void setTypeFace(int type, TextView... views) {
		for (int i = 0; i < views.length; i++) {
			TextView v = views[i];
			if (v != null) {
				v.setTypeface(null, type);
			}
		}
	}

	/**
	 * show toast with custom time
	 * @author: duongdt3
	 * @since: 15:19:51 27 Jan 2015
	 * @return: void
	 * @throws:
	 * @param message
	 */
	public static void showToastMessage(Context ctx, String message, int time) {
		SuperToast.makeText(ctx, message, time).show();
	}
//
//	public static void addIconsBegin(Iconify.IconValue icoVal, TextView ... tvs){
//		for (TextView tv : tvs) {
//			if (tv != null) {
//				tv.setTextSize(Constants.ICON_SIZE);
//				Iconify.addIcons(tv);
//			}
//		}
//	}
//
//	static final int DIRECTION_LEFT = 1;
//	static final int DIRECTION_RIGHT = 2;
//
//	public static void addIconRight(Iconify.IconValue iconVal, TextView ... tvs){
//		addIcon(iconVal, DIRECTION_RIGHT, tvs);
//	}
//
//	public static void addIconLeft(Iconify.IconValue iconVal, TextView ... tvs){
//		addIcon(iconVal, DIRECTION_LEFT, tvs);
//	}
//
//	private static void addIcon(Iconify.IconValue iconVal, int direction, TextView ... tvs){
//		addIcon(iconVal.formattedName(), direction, tvs);
//	}
//
//	private static void addIcon(String iconVal, int direction, TextView ... tvs){
//		for (TextView tv : tvs) {
//			if (tv != null) {
//				CharSequence val = tv.getText();
//				if (val == null){
//					val = "";
//				}
//				switch (direction){
//					case DIRECTION_LEFT:
//						val = iconVal + val;
//						break;
//
//					case DIRECTION_RIGHT:
//						val = val + iconVal;
//						break;
//				}
//
//				tv.setText(val);
//				tv.setTextSize(Constants.ICON_SIZE);
//				Iconify.addIcons(tv);
//			}
//		}
//	}

	/**
	 * viewAppInPlayStore
	 * @author: duongdt3
	 * @since: 15:19:51 27 Jan 2015
	 * @return: void
	 * @param con
	 * @param appPackageName
	 */
	public static void viewAppInPlayStore(Activity con, String appPackageName){
		try {
			con.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
		} catch (ActivityNotFoundException e) {
			Toast.makeText(con, StringUtil.getString(R.string.TEXT_NEED_INSTALL_GOOGLE_PLAY), Toast.LENGTH_LONG).show();
		} catch (Exception e) {

		}
	}

	/**
	 * launchGoogleMapApp
	 * @author: duongdt3
	 * @since: 15:19:51 27 Jan 2015
	 * @return: void
	 * @param con
	 * @param lat
	 * @param lng
	 * @param zoom
	 */
	public static void launchGoogleMapApp(Activity con, double lat, double lng, int zoom){
		//nếu không có vị trí thì show Google Map hiển thị cả VietNam
		if (lat <= 0) {
			lat = 16.271358;
			lng = 107.558876;
			zoom = 12;
		}
		String uri = String.format("geo:%f,%f?z=%d", lat, lng, zoom);
		Uri gmmIntentUri = Uri.parse(uri);
		Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
		String appGoogleMaps = "com.google.android.apps.maps";
		mapIntent.setPackage(appGoogleMaps);

		try {
			if (mapIntent.resolveActivity(con.getPackageManager()) != null) {
				con.startActivity(mapIntent);
			}
		} catch (ActivityNotFoundException e) {
			Toast.makeText(con, StringUtil.getString(R.string.TEXT_NEED_INSTALL_GOOGLE_MAP), Toast.LENGTH_LONG).show();
			viewAppInPlayStore(con, appGoogleMaps);
		} catch (Exception e) {

		}
	}

	/**
	 * get system info
	 * @author: duongdt3
	 * @since: 17:40:33 26 Nov 2014
	 * @return: String
	 * @throws:
	 * @return
	 */
	public static String getSystemInfoStr(){
		String result = "Device: " + android.os.Build.MANUFACTURER + ", "
				+ android.os.Build.MODEL + ", "
				+ android.os.Build.VERSION.SDK_INT + ", " + getAppVersion()
				+ ", " + Build.CPU_ABI + ", " + Build.CPU_ABI2;
		return result;
	}

	/**
	 * get list gps provider
	 * @author: duongdt3
	 * @since: 17:54:58 26 Nov 2014
	 * @return: String
	 * @throws:
	 * @return
	 */
	public static String getGpsProviderListStr(){
		String result = "GPS list: ";
		try {
			LocationManager locManager = (LocationManager) GlobalInfo
					.getInstance().getAppContext()
					.getSystemService(Context.LOCATION_SERVICE);

			List<String> listProvider = locManager.getAllProviders();
			for (String provider : listProvider) {
				result += provider + ": " + (locManager.isProviderEnabled(provider) ? "on" : "off") + " ";
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * get heap info
	 * @author: duongdt3
	 * @since: 10:58:38 9 Mar 2015
	 * @return: String
	 * @throws:
	 * @return
	 */
	public static String getHeapInfo() {
		String info  = "";
		try {
			Runtime rt = Runtime.getRuntime();
			long maxHeapAllow = rt.maxMemory() / (1024 * 1024);
			long maxHeap = rt.totalMemory() / (1024 * 1024);
			long freeHeap = rt.freeMemory() / (1024 * 1024);
			info  = String.format("HeapMaxAllow: %s Max: %s Free: %s", maxHeapAllow, maxHeap, freeHeap);
		} catch(Exception e){}

		return info;
	}

	/**
	 * Tinh khoang cach hop le
	 * @author: yennth16
	 */
	public static int locationCorrect(String timeNew, double latNew, double lngNew){
		int correct = 0;
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		double latOld = Double.parseDouble(sharedPreferences.getString(LoginView.DMS_POSITION_LAT, "0"));
		double lngOld = Double.parseDouble(sharedPreferences.getString(LoginView.DMS_POSITION_LNG, "0"));
		if(!GlobalUtil.isMockLocation()) {
			//		Tính toán: khoangcach=(thoigian_new -thoigian_old)*v .
			//		Nếu khoangcach< (Vitri_new-Vitri_old) thì vị trí mới không hợp lệ, ngược lại là hợp lệ
			long locationSpeed = GlobalInfo.getInstance().getLocationSpeed() * 1000 / 60;
			int status = sharedPreferences.getInt(LoginView.DMS_POSITION_STATUS, 0);
			String timeOld = sharedPreferences.getString(LoginView.DMS_POSITION_DATE, "0");
			double distanceCorrect = DateUtils.getDistanceSecondFrom2DateDouble(timeOld, timeNew) * locationSpeed;
			LatLng oldLatLng = new LatLng(latOld, lngOld);
			LatLng newLatLng = new LatLng(latNew, lngNew);
			double locationCorrect = GlobalUtil.getDistanceBetween(oldLatLng, newLatLng);
			if (status == 1 || distanceCorrect >= locationCorrect) {
				correct = 1;
				GlobalInfo.getInstance().saveStatusPosition(latNew, lngNew, timeNew);
				GlobalInfo.getInstance().saveStatusPosition(0);
			} else {
				correct = 0;
				if(GlobalInfo.getInstance().isSendLogPosition()) {
					ServerLogger.sendLogLogin("Log vị trí không hợp lệ",
							"Ngày:" + DateUtils.now() +
									" Lat,lng cũ:" + latOld + "," + lngOld +
									" Lat,lng mới:" + latNew + "," + lngNew +
									" timeOld, timeNew:" + timeOld + "," + timeNew +
									" status:" + status +
									" locationSpeed:" + locationSpeed +
									" kc:(new-old)*locationSpeed:" + distanceCorrect +
									" kc:newLatLng->oldLatLng:" + locationCorrect +
									" app:" + getPackageName(),
							TabletActionLogDTO.LOG_LOCATION_FAIL);
				}
			}
		}else{
			correct = 0;
			ServerLogger.sendLogLogin("Locating - vị trí giả",
					"Ngày:" + DateUtils.now() +
							" Lat,lng cũ:" + latOld + "," + lngOld +
							" Lat,lng mới:" + latNew + "," + lngNew,
					TabletActionLogDTO.LOG_CLIENT);
		}
		return correct;
	}

	/**
	 * Danh sach package name
	 * @return
     */
	public static String getPackageName() {
		StringBuffer buffer = new StringBuffer();
		// xem số lượng ứng dụng đang chạy hiện tại, tối đa 20 ứng dụng
		List<RunningTaskInfo> lTask = getListTaskRunning(20);
		if (lTask != null && !lTask.isEmpty()) {
			String apps = "APP: ";
			for (RunningTaskInfo runningTaskInfo : lTask) {
				if (runningTaskInfo != null
						&& runningTaskInfo.topActivity != null) {
					String appName = runningTaskInfo.topActivity.getPackageName();
					apps += appName + ";";
				}
			}
			buffer.append(apps);
		}
		return buffer.toString();
	}

	/**
	 * Kiem tra dang su dung tablet hay dien thoai?
	 * @param context
	 * @return
     */
	public static boolean checkIsTablet(Context context) {
		String string = getSizeName(context);
		if (string.equals(LARGE) || string.equals(XLARGE)) {
			return true;
		}
		return false;
	}
	/**
	 * getSizeName
	 * @param context
	 * @return
     */
	private static String getSizeName(Context context) {
		int screenLayout = context.getResources().getConfiguration().screenLayout;
		screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;

		switch (screenLayout) {
			case Configuration.SCREENLAYOUT_SIZE_SMALL:
				return SMALL;
			case Configuration.SCREENLAYOUT_SIZE_NORMAL:
				return NORMAL;
			case Configuration.SCREENLAYOUT_SIZE_LARGE:
				return LARGE;
			case Configuration.SCREENLAYOUT_SIZE_XLARGE:
				return XLARGE;
			default:
				return UNDEFINED;
		}
	}

	/**
	 * loc cac gia tri hop le khi nhap gia tri co phan le
	 * @return: void
	 * @throws:
	 * @param edEditText
	 * @param length
	 */
	public static void setFilterInputMoneyFloat(EditText edEditText,
												int length) {
		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
									   Spanned dest, int dstart, int dend) {
				String result = "";
				String strInput = dest.toString();
				for (int i = start; i < end; i++) {
					char currentChar = source.charAt(i);
					if (Character.isDigit(currentChar) || currentChar == '.') {
						if (currentChar == '.') {
							if (!strInput.contains(".")
									&& strInput.length() > 0
									&& dstart > 0
									&& Character.isDigit(strInput
									.charAt(dstart - 1))) {
								result = result + source.charAt(i);
							}
						} else if (Character.isDigit(currentChar)) {
							if (!strInput.contains(".")) {
								result = result + source.charAt(i);
							} else {
								String[] arrTemp = strInput.split(Pattern
										.quote("."));
								if (dstart <= arrTemp[0].length()) {
									result = result + source.charAt(i);
								} else if (arrTemp.length == 1
										|| arrTemp[1].length() < GlobalInfo.getInstance().getCfNumFloat()) {
									result = result + source.charAt(i);
								}
							}
						}
					}
				}
				;
				return result;
			}
		};

		filterArray[1] = new InputFilter.LengthFilter(length);
		if (edEditText != null) {
			edEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
					| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			edEditText.setFilters(filterArray);
		}
	}
	/**
	 * loc cac gia tri hop le khi nhap gia tri co phan le
	 * @return: void
	 * @throws:
	 * @param edEditText
	 * @param length
	 */
	public static void setFilterInputPercentFloat(EditText edEditText,
												  int length) {
		InputFilter[] filterArray = new InputFilter[2];
		filterArray[0] = new InputFilter.LengthFilter(length) {
			@Override
			public CharSequence filter(CharSequence source, int start, int end,
									   Spanned dest, int dstart, int dend) {
				String result = "";
				String strInput = dest.toString();
				for (int i = start; i < end; i++) {
					char currentChar = source.charAt(i);
					if (Character.isDigit(currentChar) || currentChar == '.') {
						if (currentChar == '.') {
							if (!strInput.contains(".")
									&& strInput.length() > 0
									&& dstart > 0
									&& Character.isDigit(strInput
									.charAt(dstart - 1))) {
								result = result + source.charAt(i);
							}
						} else if (Character.isDigit(currentChar)) {
							if (!strInput.contains(".")) {
								if (strInput.length() < Constants.MAX_LENGHT_PERCENT) {
									result = result + source.charAt(i);
								}
							} else {
								String[] arrTemp = strInput.split(Pattern
										.quote("."));
								if (dstart <= arrTemp[0].length()) {
									if (arrTemp[0].length() < Constants.MAX_LENGHT_PERCENT) {
										result = result + source.charAt(i);
									}
								} else if (arrTemp.length == 1
										|| arrTemp[1].length() < Constants.MAX_LENGHT_PERCENT) {
									result = result + source.charAt(i);
								}
							}
						}
					}
				}
				;
				return result;
			}
		};

		filterArray[1] = new InputFilter.LengthFilter(length);
		if (edEditText != null) {
			edEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL
					| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
					| InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
			edEditText.setFilters(filterArray);
		}
	}

	/**
	 * setEnableEditText
	 * @param button
	 * @param enable
     */
	public static void setEnableEditText(EditText button, boolean enable) {
		if (button == null) {
			return;
		}
		if (enable) {
			button.setEnabled(true);
			button.setBackgroundResource(R.drawable.bg_white_rounded);
		} else {
			button.setEnabled(false);
			button.setBackgroundResource(R.drawable.bg_white_rounded_disable);
		}
	}

	/**
	 * Tinh san luong thuc
	 * @param quantity
	 * @param convfact
     * @return
     */
	public static long realQuantity(String quantity, int convfact){
		long value = 0;
		String text = "";
		String [] arrayQuantity = quantity.split("/");
		if(StringUtil.isStringContainValidChars(quantity.toString(), ',')){
			text = arrayQuantity[0].replaceAll(",","");
		}else{
			text = arrayQuantity[0];
		}
		long quantiy1= Long.parseLong(text);
		if(arrayQuantity.length > 1){
			if(StringUtil.isNullOrEmpty(quantity) || quantity.equals("0")){
				value = 0;
			}else {
				long quantiy2 = Long.parseLong(arrayQuantity[1]);
				value = quantiy1 * convfact;
				value = value + quantiy2;
			}
		}else{
			value = quantiy1 * convfact;
		}
		return value;
	}
	/**
	 * Tinh san luong thuc
	 * @param quantity
	 * @return
	 */
	public static String formatQuantity(String quantity){
		String value = "";
		String [] arrayQuantity = quantity.split("/");
		if(arrayQuantity.length > 1){
			value = quantity;
		}else{
			if(StringUtil.isNullOrEmpty(quantity.toString()) || quantity.toString().equals("0")){
				value = "";
			}else {
				value = quantity + "0";
			}
		}
		return value;
	}
	/**
	 * Clear data app giong thao tac clear data trong setting
	 */
	public static void clearApplicationData() {
		try {
			// clearing app data
			Runtime runtime = Runtime.getRuntime();
			runtime.exec("pm clear com.viettel.sabeco");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void startApplicationData() {
		try {
			// clearing app data
			Runtime runtime = Runtime.getRuntime();
			VTLog.i("Start app", "Start app");
//			runtime.exec(" adb shell am start -n com.viettel.sabeco/com.viettel.dms.view.main.LoginView ");
			runtime.exec("adb shell monkey -p com.viettel.sabeco -c android.intent.category.LAUNCHER 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Xoa du lieu truoc khi dang nhap tai khoan khac
	 */
	public static void clearAllDataLoginOtherUser() {
		// cancel position
		PositionManager.getInstance().stop();
		// cancel thong bao ngoai chuong trinh
		GlobalInfo.getInstance().getStatusNotifier().cancelNotifications();
		GlobalInfo.getInstance().setTimeSyncToServer(0);
		GlobalInfo.getInstance().setAllowEditPromotion(-1);
		// cancel luong dong bo
		TransactionProcessManager.getInstance().cancelTimer();
		// xoa thong tin ca nhan
		GlobalInfo.getInstance().getProfile().clearProfileLoginOtherUser();
		GlobalInfo.getInstance().notifyOrderReturnInfo = new NotifyInfoOrder();
		// cap nhat thoat ung dung
		GlobalInfo.getInstance().isExitApp = true;
		// kiem tra dong database
		if (!SQLUtils.getInstance().isProcessingTrans) {
			// cancel sqlLite
			SQLUtils.getInstance().closeDB();
		}
	}
}
