/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.dto.view;

/** 
 * Commodity Focus DTO class
 * CommodityFocusDTO.java
 * @version: 1.0 
 * @since:  10:23:17 8 Jan 2014
 */
//mat hang trong tam
public class CommodityFocusDTO {
	public int Plan =0;//ke hoach
	public int Execute = 0;//thuc hien
    public int Progress = 0;//tien do
    public int Rest = 0;//con lai
	
    
    public CommodityFocusDTO() {
		// TODO Auto-generated constructor stub
	 	
	}
    
	public String getPlan() {
		return Integer.toString(Plan);
	}

	public void setCustomerId(int Plan) {
		this.Plan = Plan;
	}
	
	public String getExecute(){
		return Integer.toString(Execute);
	}
	public void setExecute(int Execute){
		this.Execute = Execute;
	}
	
	public String getProgress(){
		return Integer.toString(Progress);
	}
	public void setProgress(int Progress){
		this.Progress = Progress;
	}
	
	public String getRest(){
		return Integer.toString(Rest);
	}
	public void setRest(int Rest){
		this.Rest = Rest;
	}
}
