/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto;

import com.viettel.dms.global.GlobalInfo;
import com.viettel.maps.base.LatLng;

import java.io.Serializable;

/**
 * GPS info class
 * GPSInfo.java
 * @version: 1.0 
 * @since:  10:10:44 8 Jan 2014
 */
@SuppressWarnings("serial")
public class GPSInfo implements Serializable{
	// lat, long
	private double longitude = -1;
	private double latitude = -1;
	//toa do lat, lng khong bi reset 10 phut
	private double lastLongitude = -1;
	private double lastLatitude = -1;
	// luu toa do fake
	private double fakeLongitude = -1;
	private double fakeLatitude = -1;
	// info cell
	public String cellId;
	public String MNC;
	public String LAC;
	public String cellType;
	public double accuracy;

	public GPSInfo(){
	}
	
	//10.784892,106.68302
	public double getLongtitude(){
		//no_log
		if(GlobalInfo.getInstance().IS_EMULATOR){
			return 106.68302;
		}else{
			return longitude;
		}
//		return 107.16990334913135;
//		return 106.70175;//vincom
	}
	public double getLatitude(){
		//no_log
		if(GlobalInfo.getInstance().IS_EMULATOR){
			return 10.784892;
		}else{
			return latitude;
		}
//		return 10.663712527602911;
//		return 10.776617; //vincom
	}
	
	public void setLongtitude(double longtitude){
		this.longitude = longtitude;
		if(longtitude > 0){
			lastLongitude = longtitude;
		}
	}
	public void setLattitude(double lattitude){
		this.latitude = lattitude;
		if(lattitude > 0){
			lastLatitude = lattitude;
		}
	}

	public double getAccuracy() {
		return accuracy;
	}

	public void setAccuracy(double accuracy) {
		this.accuracy = accuracy;
	}

	//vi tri khong bi reset ve -1,-1 de insert actionlog
	public double getLastLongtitude(){
		return lastLongitude;
	}
	//vi tri khong bi reset ve -1,-1 de insert actionlog
	public double getLastLatitude(){
		return lastLatitude;
	}
	
	public double getFakeLongitude() {
		return fakeLongitude;
	}

	public void setFakeLongitude(double fakeLongitude) {
		this.fakeLongitude = fakeLongitude;
	}

	public double getFakeLatitude() {
		return fakeLatitude;
	}

	public void setFakeLatitude(double fakeLatitude) {
		this.fakeLatitude = fakeLatitude;
	}

	public boolean isVaildLocation(){
		return (getLatitude() > 0 && getLongtitude() > 0);
	}

	public LatLng getLocationOb(){
		return new LatLng(getLatitude(), getLongtitude());
	}
}
