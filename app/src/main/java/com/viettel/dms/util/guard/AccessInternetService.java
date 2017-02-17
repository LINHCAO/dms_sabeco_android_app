/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util.guard;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.AppOpsManager;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.util.guard.models.AndroidAppProcess;
import com.viettel.dms.view.main.AccessInternetDialog;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

/**
 * Services chan su dung cac ung dung su dung internet 3g
 * 
 * @author: Tuanlt11
 * @version: 1.0
 * @since: 1.0
 */
public class AccessInternetService extends Service {
	// timer dinh thoi l
	private Timer locTimer;
	private TimerTask locTask;
	private Handler mHandler = new Handler();
	private static final String TAG = "AccessInternetService";
	static final String HOME = "com.sec.android.app.launcher";
	ActivityManager am = null;
	// ds cac ung dung can phai xoa
	// bien de kiem tra cac ung dung xoa co thay doi hay ko, neu ko thi luon
	// hien thi xoa
	public int sizeBlackApps = 0;
	private static boolean isUnlock = false;
	AlertDialog alertDialog = null;
	// ds ung dung chan su dung
	private static ArrayList<String> lstBlackAppPrevent = new ArrayList<String>();
	// view chan network
	AccessInternetDialog preventNetworkView;
	// sau thoi gian 5p thi khoi dong lock lai, thoi gian tinh bang giay
	public static long TIME_RESTART_LOCK = 300;
	// bien kiem tra xem co can checknetwork hay ko
	private static boolean isCheckNetwork = true;
	private static int countTime = 0;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		VTLog.d(TAG, "onCreate");
		// khoi tao truoc cac ds mac dinh
		am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
		TIME_RESTART_LOCK = GlobalInfo.getInstance().getTimeLockAppBlocked() / 1000;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		VTLog.d(TAG, "onStart");
		locTimer = new Timer();
		locTask = new TimerTask() {
			@Override
			public void run() {
				mHandler.post(new Runnable() {
					@Override
					public void run() {
						int checkAccessApp = GlobalInfo.getInstance()
								.getProfile().getUserData().checkAccessApp;
						if (!GlobalInfo.getInstance().isAppActive()
								&& checkAccessApp != UserDTO.CHECK_INSTALL
								&& checkAccessApp != 0) {
							showAppLock();
						}
						else{
							if (alertDialog != null && alertDialog.isShowing()) {
								alertDialog.dismiss();
							}
						}
					}
				});
				countTime++;
				if (countTime >= TIME_RESTART_LOCK) {
					isUnlock = false;
					isCheckNetwork = true;
					countTime = 0;
				}

			}
		};
		locTimer.schedule(locTask, 0, 1500);
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		VTLog.d(TAG, "onDestroy");
		// xu li ko cho destroy services
//		int checkAccessApp = GlobalInfo.getInstance().getProfile()
//				.getUserData().checkAccessApp;
//		if (checkAccessApp == UserDTO.CHECK_NETWORK
//				|| checkAccessApp == UserDTO.CHECK_ALL_ACCESS
//				|| checkAccessApp == -1) {
//		ServiceUtil.startServiceIfNotRunning(AccessInternetService.class);
//		}
		super.onDestroy();
	}
	/**
	 * hien thi xoa ung dung cai dat
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	public void showAppLock() {
		List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		ComponentName componentInfo = taskInfo.get(0).topActivity;
		String currentPackage = componentInfo.getPackageName();
		// bien kiem tra ton tai app bi chan network hay khong
		boolean isExistAppPrevent = false;
		// kiem tra net work su dung
		//Log.d("Password", "showAppLock ");
		if (isCheckNetwork)
			checkNetworkUsing();
		if (!isUnlock) {
			//if(lstBlackAppPrevent.size() <= 0)
			lstBlackAppPrevent = GlobalInfo.getInstance()
					.getBlackListAppBlocked();
			
			// kiem tra activity dang o top
			for (int i = 0, size = lstBlackAppPrevent.size(); i < size; i++) {
				if (!StringUtil.isNullOrEmpty(lstBlackAppPrevent.get(i))
						&& currentPackage.contains(lstBlackAppPrevent.get(i))) {
					// Log.e("WhiteList", currentRunningActivityName);
					isExistAppPrevent = true;
					try {
						if (alertDialog == null) {
							Builder build = new AlertDialog.Builder(this,
									R.style.CustomDialogTheme);
							preventNetworkView = new AccessInternetDialog(this);
							build.setView(preventNetworkView.viewLayout);
							alertDialog = build.create();
							alertDialog.getWindow().setType(
									WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
							alertDialog.setCancelable(false);
							// alertDialog.setOnKeyListener(this);
							Window window = alertDialog.getWindow();
							window.setBackgroundDrawable(new ColorDrawable(
									Color.argb(0, 255, 255, 255)));
							window.setLayout(LayoutParams.WRAP_CONTENT,
									LayoutParams.WRAP_CONTENT);
							window.setGravity(Gravity.CENTER);
						}
						//preventNetworkView.showWrongPassword(wrongPass);
						VTLog.d("Password", "show password : " + lstBlackAppPrevent.get(i));
						if (!alertDialog.isShowing()) {
							VTLog.d("Password", "!showing password ");
							alertDialog.show();
							preventNetworkView.showUserName();
						}
					} catch (Exception e) {
						VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog
								.getReportFromThrowable(e));
					}
					break;
				}
			}
			//break;
		}
		if (isUnlock || !isExistAppPrevent) {
			if (alertDialog != null && alertDialog.isShowing()) {
				alertDialog.dismiss();
//				alertDialog = null;
			}
		}
	}

	/**
	 * Kiem tra mang su dung la wifi hay 3g Neu la wifi thi ko chan ung dung su
	 * dung
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	private void checkNetworkUsing() {
		ConnectivityManager connMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo wifi = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		NetworkInfo mobile = connMgr
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		// truong hop co mang wifi thi ko can lock cac ung dung he thong
		if (wifi != null && wifi.isAvailable() && wifi.isConnected()) {
			// chinh bang false de wifi van chan ung dung he thong
			isUnlock = true; 
		} else if (mobile != null && mobile.isAvailable()
				&& mobile.isConnected()) {
			isUnlock = false;
		} else {
			// nhung truong hop con lai ko can chan
			isUnlock = true;
		}
		//no_log
//		isUnlock = false;
	}

	/**
	 * Khong hien thi chan ung dung nua
	 * @author: Tuanlt11
	 * @return: void
	 * @throws:
	 */
	public static void unlockAppPrevent(boolean isUnlockApp) {
		isUnlock = isUnlockApp;
		// neu di tu ung dung dang su dung thi ko checknetwork nua(isUnlock = true)
		isCheckNetwork = !isUnlockApp;
		// reset bien dem de 5 phut sau khoa lai
		countTime = 0;
	}

	/**
	 * Khoi tao danh sach mac dinh khi chua dang nhap, clear data
	 * @author: banghn
	 */
	@SuppressWarnings("unused")
	private void initBlockListDefault(){
		lstBlackAppPrevent.add("com.android.settings");
		lstBlackAppPrevent.add("com.android.browser");
		lstBlackAppPrevent.add("com.android.chrome");
		lstBlackAppPrevent.add("org.mozilla.firefox");
		lstBlackAppPrevent.add("com.android.vending");
		lstBlackAppPrevent.add("com.google.android.youtube");
		lstBlackAppPrevent.add("com.google.android.apps.plus");
	}
	
	/**
	 * Cap nhat danh sach ung dung
	 * @author: banghn
	 */
	public static void updateApplication(){
		if (GlobalInfo.getInstance().getBlackListAppBlocked() != null
				&& GlobalInfo.getInstance().getBlackListAppBlocked().size() > 0) {
			lstBlackAppPrevent = GlobalInfo.getInstance()
					.getBlackListAppBlocked();
		}
	}
}