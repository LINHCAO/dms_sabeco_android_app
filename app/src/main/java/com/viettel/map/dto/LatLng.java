/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.dto;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 *  Lop co so the hien toa do cua vi tri tren ban do
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class LatLng implements Serializable{
	public double lat = -1; //toa do lat (vi do)
	public double lng = -1; //toa do long (kinh do)
	
	public LatLng(){
		
	}
	
	public LatLng(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	public void setLatLng(double lat, double lng){
		this.lat = lat;
		this.lng = lng;
	}
	
	public void setLat(double lat){
		this.lat = lat;
	}
	
	public void setLng(double lng){
		this.lng = lng;
	}
	
	public double lat(){
		return this.lat;
	}
	
	public double lng(){
		return this.lng;
	}
	
	
	/**
	 * Tra ve xau bieu dien diem : lat,lng
	 * @author BangHN
	 */
	public String toString(){
		DecimalFormat df = new DecimalFormat("0.000000");				
		String latString = df.format(lat);
		String lngString = df.format(lng);
		return latString + "," + lngString;
	}
	
	
	/**
	 * Tra ve mot doi tuong duoc moi clone tu doi tuong ban dau]
	 * @author BangHN
	 */
	public LatLng clone(){
		LatLng clone = new LatLng(this.lat, this.lng);
		return clone;
	}
	
	
	/**
	*  so sanh voi mot toa do khac
	*  @author: BangHN
	*  @param other : toa do diem khac truyen vao
	*  @return: true neu bang nhau
	*  @throws:
	*/
	public boolean equals(LatLng other){
		if(other == null){
			return false;
		}else if(this.lat != other.lat || this.lng != other.lng){
			return false;
		}else{
			return true;
		}
	}
	
	
	/**
	*  Chua the hien duoc
	*  @author: BangHN
	*  @return
	*  @return: Object
	*  @throws:
	 */
	public Object getOriginalObj(){
		return null;
	}
}
