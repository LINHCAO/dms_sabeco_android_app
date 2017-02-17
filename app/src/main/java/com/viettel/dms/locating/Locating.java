/**
 * Copyright 2011 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.locating;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.utils.VTLog;

/**
 *  Lay toa do hien hanh, ho tro timeout
 *  @author: AnhND
 *  @version: 1.0
 *  @since: 1.0
 */
public class Locating implements LocationListener{
	
	class TimeOutTask extends TimerTask {
		public boolean isCancel = false;

		@Override
		public void run() {
			(GlobalInfo.getInstance().getAppHandler()).post(new Runnable() {
				@Override
				public void run() {
					if (!isCancel) {
						LocationManager locManager = (LocationManager) GlobalInfo
								.getInstance().getAppContext()
								.getSystemService(Context.LOCATION_SERVICE);
						locManager.removeUpdates(Locating.this);
						Locating.this.resetTimer();
						listener.onTimeout(Locating.this);
					}
				}
			});
		}
	}
	Location lastKnownloc = null;
	String providerName = "";//ten provider dinh vi(gps, lbs)
	Timer timeOutTimer = null;//quan ly timeout	
	LocatingListener listener;//listener
	TimeOutTask timeOutTask;
	boolean isRuning = false;
	Location location = null;
	
	// The maximum time that should pass before the user gets a location update.
	public static long MAX_TIME = GlobalInfo.getInstance().getTimeTrigPosition();
	public static long MAX_TIME_RESET = GlobalInfo.getInstance().getTimeTrigPosition();//60000; (kenh sieu thi ghe tham lau tang len 30 phut)

	
	public Locating(String locationProviderName, LocatingListener listener) {
		providerName = locationProviderName;
		this.listener = listener;
	}

	public String getProviderName(){
		return providerName;
	}
	
	public void resetTimer() {
		// TODO Auto-generated method stub
		// MyLog.logToFile(Constants.LOG_LBS,"ResetTimer : provider = "+
		// providerName);
		if (timeOutTimer != null) {
			timeOutTimer.cancel();
			timeOutTimer = null;

			timeOutTask.isCancel = true;
			timeOutTask.cancel();
			timeOutTask = null;

			LocationManager locManager = (LocationManager) GlobalInfo
					.getInstance().getAppContext()
					.getSystemService(Context.LOCATION_SERVICE);
			locManager.removeUpdates(Locating.this);
		}
	}

	public boolean isEnableProvider() {
		if (GlobalInfo.getInstance().getAppContext() != null) {
			LocationManager locManager = (LocationManager) GlobalInfo
					.getInstance().getAppContext()
					.getSystemService(Context.LOCATION_SERVICE);
			if (locManager.isProviderEnabled(providerName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * thuc hien lay toa do
	 * @author: AnhND
	 * @param timeout
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean requestLocating(int timeout) {
		// TODO Auto-generated method stub
		boolean result = false;
		// resetTimer();
		// if(isRuning) return false;
		LocationManager locManager = (LocationManager) GlobalInfo.getInstance()
				.getAppContext().getSystemService(Context.LOCATION_SERVICE);
		if (locManager.isProviderEnabled(providerName)) {
			locManager.requestLocationUpdates(providerName, 0, 0.0f,
					Locating.this);
			timeOutTimer = new Timer();
			timeOutTask = new TimeOutTask();
			timeOutTimer.schedule(timeOutTask, timeout);
			//goi de lay toa do cuoi cung gan day
			onLocationChanged(null);
			result = true;
		} else {
			result = false;
		}
		isRuning = result;
		return result;
	}

	@Override
	public void onLocationChanged(Location loc) {
		// vinamilk bussiness 3 minus ago
		long minTime = System.currentTimeMillis() - MAX_TIME; 
		
		Location oldLoc = GlobalInfo.getInstance().getLoc();
		Location bestResult = null;
		float bestAccuracy = Float.MAX_VALUE;
		long bestTime = Long.MIN_VALUE;

		if (oldLoc != null && oldLoc.getTime() > minTime) {
			bestResult = oldLoc;
			bestTime = oldLoc.getTime();
			bestAccuracy = oldLoc.getAccuracy();
		}
		LocationManager locationManager = (LocationManager) GlobalInfo
				.getInstance().getAppContext()
				.getSystemService(Context.LOCATION_SERVICE);
			List<String> matchingProviders = locationManager.getAllProviders();
			for (String provider : matchingProviders) {
				Location location = locationManager.getLastKnownLocation(provider);
				if (location != null) {
					float accuracy = location.getAccuracy();
					long time = location.getTime();
					if ((time > minTime && accuracy < bestAccuracy)) {
						bestResult = location;
						bestAccuracy = accuracy;
						bestTime = time;
					} else if (time < minTime && bestAccuracy == Float.MAX_VALUE
							&& time > bestTime) {
						bestResult = location;
						bestTime = time;
					}
				}
			}
		if(bestResult != null){
			bestAccuracy = bestResult.getAccuracy();
			if((System.currentTimeMillis() - GlobalInfo.getInstance().timeSendLogLocating) >= PositionManager.TIME_SEND_LOG_LOCATING 
					&& bestAccuracy > GlobalInfo.getInstance().getRadiusOfPosition() && GlobalUtil.checkNetworkAccess()){
				GlobalInfo.getInstance().timeSendLogLocating = System.currentTimeMillis();
				ServerLogger.sendLog("Locating",
						"Provider :  " + bestResult.getProvider().toString() +
						"  -  [lat,lng]: (" + bestResult.getLatitude() + "," + bestResult.getLongitude() +")" +
						"  -  Accuracy :  " + bestAccuracy , false, TabletActionLogDTO.LOG_CLIENT);
			}
		}
		
		if(bestResult != null && loc == null){
			//lay toa do cu
			if (listener != null) {
				bestResult.setTime(System.currentTimeMillis());
				listener.onLocationChanged(bestResult);
			}
		}else if (bestResult != null
				&& (bestTime > minTime && bestAccuracy <= GlobalInfo.getInstance().getRadiusOfPosition())) {
			if (bestAccuracy < 50) {
				resetTimer();
			}
			if (listener != null) {
				listener.onLocationChanged(bestResult);
			}
		}
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		resetTimer();
		if (listener != null) {
			listener.onProviderDisabled(provider);
		}
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.onProviderEnabled(provider);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.onStatusChanged(provider, status, extras);
		}
	}
}
