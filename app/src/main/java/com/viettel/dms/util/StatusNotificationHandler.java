/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import java.io.Serializable;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.WindowManager;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;

/**
 *  xu ly cac su kien khi chuong trinh inactive (interface)
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class StatusNotificationHandler implements Serializable{
	private static final long serialVersionUID = 1L;
	// id cua notification post len status notification
	private static final int NOTIFICATION_MESSAGE_ID = 1;
	// so luong don hang can cap nhat
	private int numOrder = 0;
	
	
	/**
	*  Xu ly cac loai thong bao ngoai chuong trinh
	*  @author: TruongHN
	*  @param action
	*  @param bundle
	*  @param appContext
	*  @param activity
	*  @return: void
	*  @throws:
	 */
	public void handleAction(int action, Bundle bundle, GlobalBaseActivity activity){
		if (bundle != null){
			try{
				numOrder = bundle.getInt(IntentConstants.INTENT_NUM_RETURN_ORDER, 0);
				postNotificationMessage(activity);
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	
	/**
	*  Get chi tiet thong bao/tin nhan
	*  @author: AnhND
	*  @param context
	*  @return: String
	*  @throws:
	 */
	public String getMessageNotify() {
		String result = "Có " + numOrder+ " đơn hàng lỗi";
		return result;
	}
	
	/**
	 * 
	*  tao notification len status notification
	*  @author: AnhND
	*  @param appContext
	*  @param activity
	*  @return: void
	*  @throws:
	 */
	private void postNotificationMessage(GlobalBaseActivity activity){
		NotificationManager notificationManager = (NotificationManager) GlobalInfo.getInstance().getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
		int icon = R.drawable.icon_app_small;
		BitmapDrawable bd=(BitmapDrawable) GlobalInfo.getInstance().getAppContext().getResources().getDrawable(icon);		
		int iconWidth=bd.getBitmap().getWidth();
		CharSequence tickerText = getMessageNotify();
    	long when = System.currentTimeMillis();
    	// noi dung trong status bar khi keo xuong xem thong bao
    	CharSequence contentText = Constants.STR_BLANK;
    	contentText = getMessageNotify();
    	
    	int screenWidth = ((WindowManager)GlobalInfo.getInstance().getAppContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();
    	screenWidth -= iconWidth;//for icon
    	TextView textView = new TextView(GlobalInfo.getInstance().getAppContext());
    	int count = tickerText.length();
    	StringBuilder wrapTickerText = new StringBuilder();
    	wrapTickerText.append(tickerText);
    	while (count > 0) {
    		if (count < tickerText.length()) {
    			wrapTickerText.append("...");
    		}
    		float measuredWidth = textView.getPaint().measureText(wrapTickerText.toString());
    		if (measuredWidth < screenWidth) {
    			break;
    		}
    		count--;
    		wrapTickerText.setLength(count);
    	}
		Intent notificationIntent = initIntentMessage();
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
		//TamQP: fix bug _ phai truyen requestCode de phan biet PendingIntent lien tiep giong nhau
		int requestCode = (int) System.currentTimeMillis();
		PendingIntent contentIntent = PendingIntent.getActivity(GlobalInfo.getInstance().getAppContext(), requestCode, notificationIntent, 0);

//    	Notification notification = new Notification(icon, wrapTickerText, when);
//    	Intent notificationIntent = initIntentMessage();
//    	notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//    	//TamQP: fix bug _ phai truyen requestCode de phan biet PendingIntent lien tiep giong nhau
//    	int requestCode = (int) System.currentTimeMillis();
//    	PendingIntent contentIntent = PendingIntent.getActivity(GlobalInfo.getInstance().getAppContext(), requestCode, notificationIntent, 0);
//
//    	notification.setLatestEventInfo(GlobalInfo.getInstance().getAppContext(), StringUtil.getString(R.string.app_name), contentText, contentIntent);
//    	notification.flags |= Notification.FLAG_AUTO_CANCEL;
//    	notification.defaults = Notification.DEFAULT_ALL;
//
//    	notificationManager.cancel(NOTIFICATION_MESSAGE_ID);
//    	notificationManager.notify(NOTIFICATION_MESSAGE_ID, notification);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(GlobalInfo.getInstance().getAppContext());
		Notification notification = builder.setContentIntent(contentIntent)
				.setSmallIcon(icon).setTicker(contentText).setWhen(when)
				.setAutoCancel(true).setContentTitle(StringUtil.getString(R.string.app_name))
				.setContentText(contentText).build();
		notificationManager.cancel(NOTIFICATION_MESSAGE_ID);
		notificationManager.notify(NOTIFICATION_MESSAGE_ID, notification);

	}
	
	
	/**
	*  Khoi tao view khi nhan vao xem thong bao tu status bar
	*  @author: TruongHN
	*  @return: Intent
	*  @throws:
	 */
	private Intent initIntentMessage(){
		Intent notificationIntent = null;
		Bundle bundle = new Bundle();
		bundle.putBoolean(IntentConstants.INTENT_NOTIFY_INACTIVE, true);
		notificationIntent = new Intent(GlobalInfo.getInstance().getAppContext(), SalesPersonActivity.class);
		notificationIntent.putExtras(bundle);
		return notificationIntent;
	}
	
	/**
	*  reset cac gia tri
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	public void reset(){
		numOrder = 0;
	}

	/**
	*  cancel notifications
	*  @author: TruongHN
	*  @param appContext
	*  @return: void
	*  @throws:
	 */
	public void cancelNotifications() {
		reset();
		NotificationManager notificationManager = (NotificationManager) GlobalInfo.getInstance().getAppContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}
	
}
