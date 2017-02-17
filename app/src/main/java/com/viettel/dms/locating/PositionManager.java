/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.locating;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.LogFile;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.util.guard.AccessInternetService;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Xu ly luong dinh vi
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 * com.viettel.dms.locating.PositionManager
 */
public class PositionManager extends BroadcastReceiver implements
		LocatingListener {
	final public static String ONE_TIME = "onetime";
	final String LOG_TAG = "PositionManager";
	// 5 phut
	public final static long TIME_SEND_LOG_LOCATING = 300000;
	// 1 phut
	public final static int TIME_OUT_SHOW_LOCATING = 60000;
	final String LBS = "NETWORK";
	// thoi gian time out lay gps
	final int GPS_TIME_OUT = 90000;
	// 10s thoi gian time out lay lbs
	final int LBS_TIME_OUT = 10000;
	// dinh thoi goi dinh vi mat dinh
//	public static final long TIME_LOC_PERIOD = 300000;
	// thoi gian dinh vi luc cham cong
	// public static final long TIME_LOC_PERIOD_ATTENDACE = 120000;
	// thoi gian time period dang dung
	public static long currentTimePeriod = 0;
	// trang thai none
	public static final int LOCATING_NONE = 0;
	// trang thai start gps
	static final int LOCATING_GPS_START = 1;
	// trang thai dang lay gps
	static final int LOCATING_GPS_ON_PROGRESS = 2;
	// trang thai lay gps that bai
	static final int LOCATING_GPS_FAILED = 3;
	// trang thai start lbs
	static final int LOCATING_LBS_START = 4;
	// trang thai dang lay lbs
	static final int LOCATING_LBS_ON_PROGRESS = 5;
	// trang thai lay lbs that bai
	static final int LOCATING_LBS_FAILED = 6;
	// trang thai dinh vi viettel
	static final int LOCATING_VIETTEL = 7;
	// id action dinh vi 1 lan thanh cong
	public int actionSuccess;
	// id action dinh vi 1 lan that bai
	public int actionFail;

	private boolean isStart = false;
	// co su dung lbs cua Google khong
	public boolean isUsingGoogleLBS = true;
	Locating gpsLocating;// dinh vi gps
	Locating lbsLocating;// dinh vi lbs
	// trang thai dinh vi
	int locatingState = LOCATING_NONE;
	// bat luong LBS truoc
	private boolean isFirstLBS;
	private BaseFragment listener;

	private static PositionManager instance;
	private static final Object lockObject = new Object();

	public static PositionManager getInstance() {
		if (instance == null) {
			synchronized (lockObject) {
				if (instance == null) {
					instance = new PositionManager();
				}
			}
		}
		return instance;
	}

	public PositionManager() {
		instance = this;
	}

	/**
	 * Thuc hien restart lai luong dinh vi stop() -> start()
	 * @author: BANGHN
	 */
	public void reStart() {
		stop();
		start();
	}

	public void startRepeatingTimer(long timePeriod) {
		Context context = GlobalInfo.getInstance().getAppContext();
		setAlarm(context, timePeriod);
	}

	public void cancelRepeatingTimer() {
		Context context = GlobalInfo.getInstance().getAppContext();
		cancelAlarm(context);
	}

	/**
	 * khoi dong lay toa do
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public void start() {
		// co the thoi gian dinh vi thoi gian cham cong nho hon
		if (DateUtils.isInAttendaceTime()) {
			start(GlobalInfo.getInstance().getTimeTrigPositionAttendance());
		} else {
			start(GlobalInfo.getInstance().getTimeTrigPosition());
		}
	}

	/**
	 * khoi dong lay toa do
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public void start(long timePeriod) {
		GlobalInfo.getInstance().getAppContext()
				.getSystemService(Context.LOCATION_SERVICE);
		isStart = true;
		isFirstLBS = true;
		isUsingGoogleLBS = isEnableGoogleLBS();
		// run 1 luong lbs song song voi luong dinh vi
		if (isUsingGoogleLBS) {
			getLBSGoogle();// lay lbs google
		} else {
			onLocationChanged(null);
		}
		startRepeatingTimer(timePeriod);
	}

	/**
	 * stop luong dinh vi (Dung lay toa do)
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws:
	 */
	public void stop() {
		isStart = false;
		if (GlobalInfo.getInstance().getLoc() != null
				&& GlobalInfo.getInstance().getLoc().getTime() < System
						.currentTimeMillis() - Locating.MAX_TIME_RESET) {
			GlobalInfo.getInstance().setLoc(null);
			GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.setLongtitude(-1);
			GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.setLattitude(-1);
			GlobalInfo.getInstance().getProfile().save();
		}
		if (gpsLocating != null) {
			gpsLocating.resetTimer();
		}
		if (lbsLocating != null) {
			lbsLocating.resetTimer();
		}
	}

	@SuppressLint("Wakelock") @Override
	public void onReceive(Context context, Intent arg1) {
		// TODO Auto-generated method stub
		PowerManager pm = (PowerManager) context
				.getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
				PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
		// Acquire the lock
		wl.acquire();
		(GlobalInfo.getInstance().getAppHandler()).post(new Runnable() {
			@Override
			public void run() {
				// start lbg google neu su dung
				if (isEnableGoogleLBS()) {
					getLBSGoogle();
				}
				if (GlobalInfo.getInstance().isAppActive()) {
					locatingState = LOCATING_NONE;
					if (isEnableGoogleLBS() || isEnableGPS()) {
						requestLocationUpdates();
					}
				} else {
					stop();
				}
			}
		});
		// Release the lock
		wl.release();
	}

	/**
	 * 
	 * quan ly trang thai va goi lay toa do
	 * 
	 * @author: AnhND
	 * @return: void
	 * @throws:
	 */
	private void requestLocationUpdates() {
		synchronized (this) {
			switch (locatingState) {
			case LOCATING_NONE:
				VTLog.i(LOG_TAG, "requestLocationUpdates() - LOCATING_NONE");
				locatingState = LOCATING_GPS_START;
				requestLocationUpdates();
				break;

			case LOCATING_GPS_START:
				VTLog.i(LOG_TAG,
						"requestLocationUpdates() - LOCATING_GPS_START");
				if (gpsLocating == null) {
					gpsLocating = new Locating(LocationManager.GPS_PROVIDER,
							this);
				}
				if (!gpsLocating.requestLocating(GPS_TIME_OUT)) {
					locatingState = LOCATING_GPS_FAILED;
					requestLocationUpdates();
				} else {
					locatingState = LOCATING_GPS_ON_PROGRESS;
				}
				break;

			case LOCATING_GPS_ON_PROGRESS:
				VTLog.i(LOG_TAG,
						"requestLocationUpdates() - LOCATING_GPS_ON_PROGRESS");
				break;

			case LOCATING_GPS_FAILED:
				VTLog.i(LOG_TAG,
						"requestLocationUpdates() - LOCATING_GPS_FAILED");
				if (isUsingGoogleLBS) {
					locatingState = LOCATING_LBS_START;
				} else {
					locatingState = LOCATING_VIETTEL;
				}
				LogFile.logToFile(DateUtils.now() + " LOCATING_GPS_FAILED");
				if (GlobalInfo.getInstance().isAppActive()
						&& (System.currentTimeMillis() - GlobalInfo
								.getInstance().timeSendLogLocating) >= TIME_SEND_LOG_LOCATING
						&& GlobalUtil.checkNetworkAccess()) {
					GlobalInfo.getInstance().timeSendLogLocating = System
							.currentTimeMillis();
					ServerLogger.sendLog("Locating",
							"Time : " + DateUtils.now()
									+ " .... LOCATING_GPS_FAILED", false,
							TabletActionLogDTO.LOG_CLIENT);
				}
				requestLocationUpdates();
				break;

			case LOCATING_LBS_START:
				VTLog.i(LOG_TAG,
						"requestLocationUpdates() - LOCATING_LBS_START");
				if (lbsLocating == null) {
					lbsLocating = new Locating(LocationManager.NETWORK_PROVIDER,
							this);
				}
				if (!lbsLocating.requestLocating(LBS_TIME_OUT)) {// dinh vi lbs
					locatingState = LOCATING_LBS_FAILED;
					requestLocationUpdates();
				} else {
					locatingState = LOCATING_LBS_ON_PROGRESS;
				}
				break;

			case LOCATING_LBS_ON_PROGRESS:
				VTLog.i(LOG_TAG,
						"requestLocationUpdates() - LOCATING_LBS_ON_PROGRESS");
				break;

			case LOCATING_LBS_FAILED:
				VTLog.i(LOG_TAG,
						"requestLocationUpdates() - LOCATING_LBS_FAILED");
				// VTLog.logToFile(LOG_TAG,
				// "requestLocationUpdates() - LOCATING_LBS_FAILED");
				LogFile.logToFile(DateUtils.now() + " LOCATING_LBS_FAILED");
				if (GlobalInfo.getInstance().isAppActive()
						&& (System.currentTimeMillis() - GlobalInfo
								.getInstance().timeSendLogLocating) >= TIME_SEND_LOG_LOCATING
						&& GlobalUtil.checkNetworkAccess()) {
					GlobalInfo.getInstance().timeSendLogLocating = System
							.currentTimeMillis();

					ServerLogger.sendLog("Locating",
							"Time : " + DateUtils.now()
									+ " .... LOCATING_LBS_FAILED", false,
							TabletActionLogDTO.LOG_CLIENT);
				}
				locatingState = LOCATING_NONE;
				break;

			case LOCATING_VIETTEL:
				VTLog.i(LOG_TAG, "requestLocationUpdates() - LOCATING_VIETTEL");
				onLocationChanged(null);
				break;
			}
		}
	}

	private void getLBSGoogle() {
		// isFirstLBS = false;
		(GlobalInfo.getInstance().getAppHandler()).post(new Runnable() {
			@Override
			public void run() {
				Locating lbs = new Locating(LocationManager.NETWORK_PROVIDER,
						PositionManager.this);
				lbs.requestLocating(LBS_TIME_OUT);
			}
		});
	}

	/**
	 * 
	 * chuyen trang thai khi dinh vi that bai
	 * 
	 * @author: AnhND
	 * @return: void
	 * @throws:
	 */
	private void handleLocatingFailed() {
		synchronized (this) {
			switch (locatingState) {
			case LOCATING_GPS_ON_PROGRESS:
				VTLog.i(LOG_TAG, "handleLocatingFailed() - GPS TIME OUT");
				locatingState = LOCATING_GPS_FAILED;
				requestLocationUpdates();
				break;
			case LOCATING_LBS_ON_PROGRESS:
				VTLog.i(LOG_TAG, "handleLocatingFailed() - LBS TIME OUT");
				locatingState = LOCATING_LBS_FAILED;
				requestLocationUpdates();
				break;
			case LOCATING_VIETTEL:// lay lbs Viettel error thi lay lbs Google
				VTLog.i(LOG_TAG, "handleLocatingFailed() - LBS TIME OUT");
				locatingState = LOCATING_LBS_START;
				(GlobalInfo.getInstance().getAppHandler()).post(new Runnable() {
					@Override
					public void run() {
						requestLocationUpdates();
					}
				});
				break;
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			if (isFirstLBS && LBS.toLowerCase().equals(location.getProvider().toString().toLowerCase())) {
				isFirstLBS = false;
			}
			long radius = GlobalInfo.getInstance().getRadiusOfPosition();
			if (location.getAccuracy() <= radius) {
				// xu ly vi tri sau khi nhan ket qua dinh vi
				updatePosition(location.getLongitude(), location.getLatitude(),
						location);
			} else if (DateUtils.isInAttendaceTime()
					&& GlobalUtil.isNearNPPPosition(location)) {
				// phuc vu cho cham cong
				updatePosition(location.getLongitude(), location.getLatitude(),
						location);
			} else if (location != null
					&& GlobalUtil.isNearCustomerVisitingPosition(location)) {
				//vi tri gan vi tri khach hang dang ghe tham
				updatePosition(location.getLongitude(), location.getLatitude(),
						location);
			} else {
				// gui log sai so, check ko gui lien tuc log
				if (GlobalInfo.getInstance().getActivityContext() != null
						&& GlobalUtil.checkNetworkAccess()
						&& System.currentTimeMillis()
								- GlobalInfo.getInstance().timeSendLogLocating >= TIME_SEND_LOG_LOCATING) {
					GlobalInfo.getInstance().timeSendLogLocating = System
							.currentTimeMillis();
					ServerLogger.sendLog(
							"Locating",
							"Dinh vi sai so Accuracy = "
									+ location.getAccuracy() + " Radius : " + radius
									+ " (lat, lng) = ("
									+ location.getLatitude() + ", "
									+ location.getLongitude() + ")", true,
							TabletActionLogDTO.LOG_CLIENT);
				}
			}
			// neu co 1 truong hop gps success thi chay xong gps se dung tien
			// trinh kg lam gi
			if (locatingState == LOCATING_GPS_ON_PROGRESS) {
				locatingState = LOCATING_NONE;
			}
		} else {
			// requestActionUpdatePosition();
		}
	}

	private void updatePosition(double lng, double lat, Location loc) {
		// kiem tra toa do nam trong lanh tho VN
		if (lat < 8.45 || lng < 102.0 || lat > 23.5 || lng > 110) {
			return;
		}

		Bundle bd = new Bundle();
		bd.putParcelable(IntentConstants.INTENT_DATA, loc);
		if (GlobalInfo.getInstance().getActivityContext() instanceof GlobalBaseActivity) {
			((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext())
					.sendBroadcast(ActionEventConstant.ACTION_UPDATE_POSITION_SERVICE,
							bd);
			if (GlobalInfo.getInstance().isSendLogPosition()) {
				ServerLogger.sendLogLogin("Log ghi thời gian định vị - PositionManager - updatePosition", "Vị trí hợp lệ, sendBroadcast cập nhật vị trí - ngày: "
						+ DateUtils.now() + "-lat,lng: " + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
						+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude(), TabletActionLogDTO.LOG_LOCATION);
			}
		}
	}
	
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		handleLocatingFailed();
		if (provider.toUpperCase().contains("GPS")) {
			try {
				((GlobalBaseActivity) GlobalInfo.getInstance()
						.getActivityContext())
						.showToastMessage("Ứng dụng cần mở chức năng định vị vệ tinh để xác định vị trí");
			} catch (Exception e) {
				VTLog.e("PositionManager", e.getMessage());
			}
		} else if (provider.toUpperCase().contains("NETWORK")) {
			try {
				((GlobalBaseActivity) GlobalInfo.getInstance()
						.getActivityContext())
						.showToastMessage("Ứng dụng cần mở chức năng định vị qua mạng di động để xác định vị trí");
			} catch (Exception e) {
				VTLog.e("PositionManager", e.getMessage());
			}
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTimeout(Locating locating) {
		// TODO Auto-generated method stub
		if (!isFirstLBS) {
			handleLocatingFailed();
		}
		isFirstLBS = false;// LBS google luon timeout truoc GPS
		LogFile.logToFile(DateUtils.now() + " onTimeout" + locating.getProviderName());
		if (GlobalInfo.getInstance().isAppActive()
				&& GlobalUtil.checkNetworkAccess()
				&& System.currentTimeMillis()
						- GlobalInfo.getInstance().timeSendLogLocating >= TIME_SEND_LOG_LOCATING) {
			GlobalInfo.getInstance().timeSendLogLocating = System
					.currentTimeMillis();
			ServerLogger.sendLog("Locating", "Time : " + DateUtils.now()
					+ "  -  Provider time-out: " + locating.getProviderName(),
					false, TabletActionLogDTO.LOG_CLIENT);
		}
	}

	

	public boolean getIsStart() {
		return isStart;
	}

	public int getLocatingState() {
		return locatingState;
	}

	public boolean getIsFirstLBS() {
		return isFirstLBS;
	}

	/**
	 * kiem tra xem co dang bat GPS khong
	 * @return the enableGPS
	 */
	public boolean isEnableGPS() {
		return new Locating(LocationManager.GPS_PROVIDER, this)
				.isEnableProvider();
	}

	/**
	 * kiem tra xem co dang bat network khong
	 * @return the enableGoogleLBS
	 */
	public boolean isEnableGoogleLBS() {
		return new Locating(LocationManager.NETWORK_PROVIDER, this)
				.isEnableProvider();
	}

	/**
	 * Kiem tra enable GPS va show setting thiet lap GPS
	 * 
	 * @author banghn
	 */
	public void checkAndShowSettingGPS() {
		if (!isEnableGPS()) {
			showDialogSettingGPS();
		}
	}

	/**
	 * show dialog confirm setting gps
	 * 
	 * @author banghn
	 */
	private void showDialogSettingGPS() {
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
	 * Set alarm thong bao thoi gian dinh vi
	 * 
	 * @author: BangHN
	 * @param context
	 * @param timePeriod
	 *            : Thoi gian dinh ki
	 * @throws:
	 */
	public void setAlarm(Context context, long timePeriod) {
		AlarmManager am = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, PositionManager.class);
		intent.putExtra(ONE_TIME, Boolean.FALSE);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
		// After after 30 seconds
		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				timePeriod, pi);
	}

	/**
	 * Goi huy bo alarm
	 * 
	 * @author: BangHN
	 * @param context
	 */
	public void cancelAlarm(Context context) {
		Intent intent = new Intent(context, PositionManager.class);
		PendingIntent sender = PendingIntent
				.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
	}

}
