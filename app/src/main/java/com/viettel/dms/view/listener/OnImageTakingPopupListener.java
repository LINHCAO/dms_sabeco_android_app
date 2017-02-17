/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.listener;


/**
 * Listener su kien khi chon chup anh tren popup
 * @author BANGHN
 * @version 1.0
 */
public interface OnImageTakingPopupListener {
	//type, action 0: hinh dong cua
	//action chup anh diem ban
	public static int ACTION_TAKING_IMAGE_LOCATION = 1;
	//action chup anh trung bay
	public static int ACTION_TAKING_IMAGE_CTTB = 2;
	//chup anh doi thu canh trang
	public static int ACTION_TAKING_IMAGE_RIVAl = 3;
	
	public void onImageTakingPopupEvent(int eventType, Object data);
}
