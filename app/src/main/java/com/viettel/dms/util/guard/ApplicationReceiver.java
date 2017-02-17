/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util.guard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Nhan su kien cai dat ung dung sau khi mot ung dung duoc cai dat
 * @author: banghn
 * @version: 1.0
 * @since: 18:53:36 27-01-2015
 */
public class ApplicationReceiver extends BroadcastReceiver {
	// dinh nghia truoc mot so ung dung fake gps de kiem tra nhanh
	String[] badApp = { "com.fakegps.mock", "com.lexa.fakegps",
			"com.blogspot.newapphorizons.fakegps", "ait.com.locationfaker",
			"com.lexa.fakegpsdonate", "com.kristo.fakegpspro",
			"com.incorporateapps.fakegps.fre", "com.my.fake.location",
			"com.polliapps.fakelocation", "com.Laurentapps.fakegps",
			"org.ajeje.fakelocation"};

	Context context;

	@Override
	public void onReceive(Context context, Intent intent) {
		List<String> blackList = Arrays.asList(badApp);
		String action = intent.getAction();
		try{
			String pkg = getPackageName(intent);
			if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
				VTLog.d("ApplicationReceiver", "App install: " + pkg);
				// int uid = intent.getIntExtra(Intent.EXTRA_UID, 0);
				
				if(GlobalInfo.getInstance().isVaildateMockLocation() && (blackList.contains(pkg) || isHasPermissionFakeLocation(pkg))){
					GlobalInfo.getInstance().isHasAppFakeLocation = true;
					((GlobalBaseActivity) GlobalInfo.getInstance()
							.getActivityContext()).showToastMessageLoop(
									StringUtil.getString(R.string.TEXT_APP_MOCK_LOCATION), 30000);
					VTLog.d("ApplicationReceiver", "Used permission mock location");
				}
			} else if(Intent.ACTION_PACKAGE_REMOVED.equals(action)){
				VTLog.d("ApplicationReceiver", "App remove: " + pkg);
				if(isHasAppFakeLocation()){
					GlobalInfo.getInstance().isHasAppFakeLocation = true;
				}else{
					GlobalInfo.getInstance().isHasAppFakeLocation = false;
				}
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}

	/**
	 * lay ten package dung dung tu intent tra ve
	 * @author: banghn
	 * @param intent
	 * @return
	 */
	private String getPackageName(Intent intent) {
		Uri uri = intent.getData();
		String pkg = uri != null ? uri.getSchemeSpecificPart() : null;
		return pkg;
	}
	
	
	/**
	 * Kiem tra ung dung co quyen fake location
	 * @author: banghn
	 * @param packageName
	 */
	public static boolean isHasPermissionFakeLocation(String packageName){
		boolean isHasPermission = false;
		String permission = "android.permission.ACCESS_MOCK_LOCATION";
		Context context = GlobalInfo.getInstance();
		ArrayList<String> whiteList = GlobalInfo.getInstance().getWhiteList();
		
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1){
				VTLog.d("ApplicationReceiver", "App: " + applicationInfo.name + " Package: "
						+ applicationInfo.packageName);
				try {
					PackageInfo packageInfo = pm.getPackageInfo(
							applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
					// Get Permissions
					String[] requestedPermissions = packageInfo.requestedPermissions;
					// check permission
					if (requestedPermissions != null) {
						for (String per : requestedPermissions) {
							if (per.equals(permission)
									&& applicationInfo.packageName.contains(packageName)
									&&!whiteList.contains(packageName)) {
								isHasPermission = true;
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return GlobalInfo.getInstance().isVaildateMockLocation() && isHasPermission;
	}
	
	
	
	/**
	 * Kiem tra co app voi quyen mock location
	 * @author: banghn
	 * @param packageName
	 */
	public static boolean isHasAppFakeLocation(){
		boolean isHasPermission = false;
		String permission = "android.permission.ACCESS_MOCK_LOCATION";
		Context context = GlobalInfo.getInstance();
		ArrayList<String> whiteList = GlobalInfo.getInstance().getWhiteList();
		
		PackageManager pm = context.getPackageManager();
		List<ApplicationInfo> packages = pm
				.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo applicationInfo : packages) {
			if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 1){
				VTLog.d("ApplicationReceiver", "App: " + applicationInfo.name + " Package: "
						+ applicationInfo.packageName);
				try {
					PackageInfo packageInfo = pm.getPackageInfo(
							applicationInfo.packageName, PackageManager.GET_PERMISSIONS);
					// Get Permissions
					String[] requestedPermissions = packageInfo.requestedPermissions;
					// check permission
					if (requestedPermissions != null) {
						for (String per : requestedPermissions) {
							if (per.equals(permission)
									&& !applicationInfo.packageName.equals(context.getPackageName())
									&& !whiteList.contains(applicationInfo.packageName)) {
								isHasPermission = true;
								break;
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return GlobalInfo.getInstance().isVaildateMockLocation() && isHasPermission;
	}
}