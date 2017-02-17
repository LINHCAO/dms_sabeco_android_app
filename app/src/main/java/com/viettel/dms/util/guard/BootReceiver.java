/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util.guard;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.viettel.dms.global.GlobalInfo;

/**
 * Nhan su kien boot device de khoi dong service
 * @author: tuanlt
 * @version: 1.0 
 * @since:  18:53:36 25-12-2014
 */
public class BootReceiver extends BroadcastReceiver {

	Context context;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
		if (Intent.ACTION_BOOT_COMPLETED.equals(action)) {
			if (!isMyServiceRunning()) {
				Intent startServiceIntent = new Intent(context,
						AccessInternetService.class);
				context.startService(startServiceIntent);
			}
		} 
	}
	
	
	/**
	 * Check services co dang chay hay ko
	 * @author: Tuanlt11
	 * @param serviceClass
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isMyServiceRunning() {
		ActivityManager manager = (ActivityManager) GlobalInfo.getInstance()
				.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (AccessInternetService.class.getName().equals(
					service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}