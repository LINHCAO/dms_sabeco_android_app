/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.global;

import java.io.Serializable;

import android.os.SystemClock;

import com.viettel.dms.controller.AbstractController;
import com.viettel.viettellib.network.http.HTTPRequest;


/**
 *  Tao ra cac action event giao tiep
 *  @author: DoanDM
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public class ActionEvent implements Serializable{
	 public int action;
	 public Object modelData;
	 public Object viewData;
	 public Object userData;
	 public Object logData;
	 public Object sender;
	 public int tag = 0;
	 public long startTimeFromBoot;
	 
	public ActionEvent() {
		startTimeFromBoot = SystemClock.elapsedRealtime();
	}
	 private int isController = -1;
	 //AnhND: xu ly relogin do mat session
	 public AbstractController controller;
	 //AnhND: xuy ly cancel request
	 public HTTPRequest request;
	 //AnhND: request co block (show progressDialog)
	 public boolean isBlockRequest;
//	 private HttpUpdateListener updateListener;
	 public boolean isNeedCheckTimeServer = false;
//	 public boolean isCheckTimeSuccess = false;
	 public void reset(){
		 action = 0;
		 modelData = null;
		 viewData = null;
		 userData = null;
		 sender = null;
		 setIsController(-1);
	 }
	 
//	 public void setUpdateListener(HttpUpdateListener lis) {
//		 updateListener = lis;
//	 }

//	public HttpUpdateListener getUpdateListener() {
//		return updateListener;
//	}

	public void setIsController(int isController) {
		this.isController = isController;
	}

	public int getIsController() {
		return isController;
	}
}
