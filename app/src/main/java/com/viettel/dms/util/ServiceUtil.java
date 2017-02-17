/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.viettel.dms.global.GlobalInfo;

/**
 * Cac util lien quan toi service
 * @author banghn
 * @version 1.0
 */
public class ServiceUtil {

	
	/**
	 * Kiem tra mot service co hoat dong hay khong
	 * @author: banghn
	 * @param serviceClass : lop cai dat service
	 * @return (true|false)
	 */
	public static boolean isServiceRunning(Class<?> serviceClass) {
		ActivityManager manager = (ActivityManager) GlobalInfo.getInstance()
				.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Khoi dong mot service 
	 * @author: banghn
	 * @param serviceClass
	 */
	public static void startServiceIfNotRunning(Class<?> serviceClass){
		if(!isServiceRunning(serviceClass)){
			Log.d("Password", "isServiceRunning false");
			GlobalInfo.getInstance().getApplicationContext().startService(
					new Intent(GlobalInfo.getInstance()
							.getApplicationContext(), serviceClass));
		}
	}
	
	/**
	 * Restart serivce
	 * @author: banghn
	 */
	public static void reStartService(Class<?> serviceClass){
		stopService(serviceClass);
		startService(serviceClass);
	}
	
	/**
	 * Khoi dong mot service 
	 * @author: banghn
	 * @param serviceClass
	 */
	public static void startService(Class<?> serviceClass){
		GlobalInfo.getInstance().getApplicationContext().startService(
			new Intent(GlobalInfo.getInstance()
					.getApplicationContext(), serviceClass));
	}
	
	/**
	 * Dung mot service
	 * @author: banghn
	 * @param serviceClass
	 */
	public static void stopService(Class<?> serviceClass){
		GlobalInfo.getInstance().getApplicationContext().stopService(
				new Intent(GlobalInfo.getInstance()
						.getApplicationContext(), serviceClass));
	}
	
	
	
	public static void printApplicationPackage() {
		StringBuilder listApp = new StringBuilder();
		PackageManager pm = GlobalInfo.getInstance()
				.getAppContext().getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo packageInfo : packages) {
			listApp.append(packageInfo.loadLabel(pm) + ":" + packageInfo.packageName);
			listApp.append("\n");
		}
		LogFile.logToFile(listApp.toString());
	}
}
