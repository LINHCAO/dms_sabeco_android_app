/**
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.utils.VTLog;

/**
 * services dinh vi
 * com.viettel.dms.util.LocationService
 * @author banghn
 * @version 1.0
 */
public class LocationService extends Service implements
		ConnectionCallbacks, OnConnectionFailedListener, LocationListener {

	private static final String TAG = LocationService.class.getSimpleName();
	// dinh thoi goi dinh vi mat dinh
	public final static long TIME_SEND_LOG_LOCATING = 600000;
	
	private GoogleApiClient mGoogleApiClient;
	private LocationRequest mLocationRequest;
	
	long radius = 500; //mét
	long interval = 120; //s
	long fastInterval = 10; //s
	int priority = LocationRequest.PRIORITY_HIGH_ACCURACY;
	long timeSendLogFusedLocating = 0;

	@Override
	public void onCreate() {
		VTLog.d(TAG, "Creating..");
		buildGoogleApiClient();
	}
	
	private synchronized void buildGoogleApiClient() {
		VTLog.d(TAG, "Building GoogleApiClient");
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(LocationServices.API).build();
	}
	
	private void updateLocationRequest() {
		if (mLocationRequest == null) {
			mLocationRequest = new LocationRequest();
		}
		mLocationRequest.setInterval(interval * 1000);
		mLocationRequest.setFastestInterval(fastInterval * 1000);
		mLocationRequest.setPriority(priority);
	}

	/**
	 * Requests location updates from the FusedLocationApi.
	 */
	private void startLocationUpdates() {
		VTLog.d(TAG, "call startLocationUpdates");
		if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
			LocationServices.FusedLocationApi.requestLocationUpdates(
					mGoogleApiClient, mLocationRequest, this);
			VTLog.d(TAG, "startLocationUpdates success");
		} else {
			VTLog.d(TAG, "startLocationUpdates fail, not connected");
		}
	}

	/**
	 * Removes location updates from the FusedLocationApi.
	 */
	private void stopLocationUpdates() {
		VTLog.d(TAG, "call stopLocationUpdates");
		try {
			if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
				LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
				VTLog.d(TAG, "stopLocationUpdates success");
			} else {
				VTLog.d(TAG, "stopLocationUpdates fail, not connected");
			}
		} catch (Exception e){
			VTLog.e("stopLocationUpdates", "fail", e);
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		VTLog.d(TAG, "Starting.. intent: " + intent + " flags: " + flags);
		boolean isConnect = isGoogleClientConnected();
		boolean isReRequest = false;
		if (intent != null) {
			//trường hợp có data
			long tempRadius = intent.getLongExtra(IntentConstants.RADIUS_OF_POSITION_FUSED, GlobalInfo.getInstance().getRadiusFusedPosition());
			long tempInterval = intent.getLongExtra(IntentConstants.INTERVAL_FUSED_POSITION, GlobalInfo.getInstance().getIntervalFusedPosition());
			long tempFastInterval = intent.getLongExtra(IntentConstants.FAST_INTERVAL_FUSED_POSITION, GlobalInfo.getInstance().getFastIntervalFusedPosition());
			long tempPriorityId = intent.getLongExtra(IntentConstants.FUSED_POSTION_PRIORITY, GlobalInfo.getInstance().getFusedPositionPriority());
			boolean isRestart = intent.getBooleanExtra(IntentConstants.FUSED_POSTION_RESTART, false);
			int tempPriority = tempPriorityId == 1 ? LocationRequest.PRIORITY_HIGH_ACCURACY : LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
			//check need reconnect when config request change
			if (isRestart || (tempInterval != this.interval || tempFastInterval != this.fastInterval || tempPriority != this.priority)) {
				isReRequest = true;
			}
			//save config
			setConfig(tempRadius, tempInterval, tempFastInterval, tempPriority);
			updateLocationRequest();
		} else{
			//không có data, có thể do retry service, nên lấy lại cấu hình lưu file
			long tempRadius = GlobalInfo.getInstance().getRadiusFusedPosition();
			long tempInterval = GlobalInfo.getInstance().getIntervalFusedPosition();
			long tempFastInterval = GlobalInfo.getInstance().getFastIntervalFusedPosition();
			long tempPriorityId = GlobalInfo.getInstance().getFusedPositionPriority();
			int tempPriority = tempPriorityId == 1 ? LocationRequest.PRIORITY_HIGH_ACCURACY : LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;
			//save config
			setConfig(tempRadius, tempInterval, tempFastInterval, tempPriority);
			updateLocationRequest();
		}
		
		//chưa kết nối thì tiến hành kết nối
		if (!isConnect) {
			VTLog.d(TAG, "Connecting location client..");
			mGoogleApiClient.connect();
		} else if (isReRequest) {
			//nếu cần request lại
			VTLog.d(TAG, "Re request location client..");
			stopLocationUpdates();
			//restart location update
			startLocationUpdates();
		}
		
		VTLog.d(TAG, "onStartCommand" + " radius: " + radius + " interval: " + interval + " fastInterval: " + fastInterval + " priority: " + priority);
		return START_STICKY;
	}

	private boolean isGoogleClientConnected() {
		return (mGoogleApiClient != null && (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()));
	}
	
	/**
	 * @author: duongdt3
	 * @since: 11:58:57 6 Nov 2014
	 * @return: void
	 * @throws:  
	 * @param tempRadius
	 * @param tempInterval
	 * @param tempFastInterval
	 * @param tempPriority
	 */
	private void setConfig(long tempRadius, long tempInterval, long tempFastInterval, int tempPriority) {
		//lưu lại
		this.radius = tempRadius;
		this.interval = tempInterval;
		this.fastInterval = tempFastInterval;
		this.priority = tempPriority;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		VTLog.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
		stopSelf();
	}

	@Override
	public void onConnected(Bundle bundle) {
		VTLog.d(TAG, "onConnected" + " radius: " + radius + " interval: " + interval * 1000 + " fastInterval: " + fastInterval * 1000 + " priority: " + priority);
		startLocationUpdates();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		throw new UnsupportedOperationException("Not yet implemented");
	}

	@Override
	public void onLocationChanged(Location location) {
		if (location != null) {
			String log = "Provider :  "
					+ location.getProvider().toString()
					+ "  -  [lat,lng]: (" + location.getLatitude() + ","
					+ location.getLongitude() + ")" + "  -  Accuracy :  "
					+ location.getAccuracy() + " Radius : " + radius;
			//sai số quá cho phép, ghi log
			if (location.getAccuracy() > radius) {
				if (Math.abs(System.currentTimeMillis() - this.timeSendLogFusedLocating) >= TIME_SEND_LOG_LOCATING && GlobalUtil.checkNetworkAccess()) {
					this.timeSendLogFusedLocating = System.currentTimeMillis();
					ServerLogger.sendLog("Locating", log, false, TabletActionLogDTO.LOG_CLIENT);
				}
				VTLog.d("LocationService onLocationChanged FAIL", log);
			} else{
				Bundle bd = new Bundle();
				bd.putParcelable(IntentConstants.INTENT_DATA, location);
				sendBroadcast(ActionEventConstant.ACTION_UPDATE_POSITION_SERVICE, bd);
				VTLog.d("LocationService onLocationChanged PASS", log);
			}
		}
	}

	@Override
	public void onDestroy() {
		VTLog.d(TAG, "Destroying..");
		try {
			stopLocationUpdates();
			mGoogleApiClient.disconnect();
		} catch (Exception e) {
			VTLog.e(TAG, VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}
	
	/**
	 * Send broadcast
	 * @author: banghn
	 * @param action
	 * @param bundle
	 */
	public void sendBroadcast(int action, Bundle bundle) {
		Intent intent = new Intent(GlobalBaseActivity.DMS_ACTION);
		bundle.putInt(Constants.ACTION_BROADCAST, action);
		bundle.putInt(Constants.HASHCODE_BROADCAST, intent.getClass()
				.hashCode());
		intent.putExtras(bundle);
		sendBroadcast(intent, "com.viettel.sabeco.permission.BROADCAST");
	}

	@Override
	public void onConnectionSuspended(int cause) {
		switch (cause) {
		case GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST:
			VTLog.d(TAG, "Connection suspended cause: CAUSE_NETWORK_LOST" );
			break;
		case GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED:
			VTLog.d(TAG, "Connection suspended cause: CAUSE_SERVICE_DISCONNECTED" );
			break;

		default:
			VTLog.d(TAG, "Connection suspended cause: " + cause);
			break;
		}
	}
}
