/**
 * Copyright 2015 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.util.guard;

import java.lang.reflect.Method;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;

/**
 * Tao vi tri gia cho provider
 * Muc dich khi phat hien phan mem fake toa do thi tao vi gia chep de
 * @author banghn
 * @version 1.0
 */
public class MockLocationProvider {
	public static String VNM_GPS = "VNMGPS";
	String providerName;
	Context ctx;

	public MockLocationProvider(String name, Context ctx) {
		this.providerName = name;
		this.ctx = ctx;

		try {
			LocationManager lm = (LocationManager) ctx
					.getSystemService(Context.LOCATION_SERVICE);
			if (lm.getProvider(providerName) != null) {
				lm.removeTestProvider(providerName);
			}
			lm.addTestProvider(providerName, false, false, false, false, false,
					true, true, 0, 0);
			lm.setTestProviderEnabled(providerName, true);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Set toa do gia vao provider
	 * @author: banghn
	 * @param lat
	 * @param lon
	 */
	public void pushLocation(double lat, double lon) {
		try {
			LocationManager lm = (LocationManager) ctx
					.getSystemService(Context.LOCATION_SERVICE);

			Location mockLocation = new Location(LocationManager.GPS_PROVIDER);
			mockLocation.setLatitude(lat);
			mockLocation.setLongitude(lon);
			mockLocation.setAltitude(0);
			mockLocation.setTime(System.currentTimeMillis());
			mockLocation.setAccuracy(5);
			
			Method locationJellyBeanFixMethod = Location.class.getMethod("makeComplete");
			if (locationJellyBeanFixMethod != null) {
			   locationJellyBeanFixMethod.invoke(mockLocation);
			}
			
			lm.setTestProviderLocation(providerName, mockLocation);
		} catch (Throwable ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Remove provider test
	 * @author: banghn
	 */
	public void shutdown() {
		LocationManager lm = (LocationManager) ctx
				.getSystemService(Context.LOCATION_SERVICE);
		lm.removeTestProvider(providerName);
	}
	
	public void makeComplete() {}
}