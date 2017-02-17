/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.controller;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.OAuthRequestManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *  Lop base controller
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public abstract class AbstractController {
	//private int isController = -1;
	// trace log xuong server
	abstract public void handleViewEvent(ActionEvent e);
    abstract public void handleModelEvent(ModelEvent modelEvent);
    abstract public void handleSwitchActivity(ActionEvent e);
    abstract public void handleSwitchFragment(ActionEvent e);
    
    /**
	 * switch Fragment with option isRemoveInBackStack
	 * @author: duongdt3
	 * @since: 16:57:14 17 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param fm
	 * @param ft
	 * @param e
	 * @param frag
	 * @param TAG
	 */
	void switchFragment(FragmentManager fm, Fragment frag, String TAG, boolean isRemoveInBackStack){
		FragmentTransaction ft = fm.beginTransaction();
		if (isRemoveInBackStack) {
			removeAllInBackStack(fm);
		}
		Fragment existsFrag = fm.findFragmentByTag(TAG);
		if (existsFrag != null) {
			FragmentTransaction ftRemove = fm.beginTransaction();
			ftRemove.remove(existsFrag);
			ftRemove.commit();
		}
		ft.replace(R.id.fragDetail, frag, TAG);
		ft.addToBackStack(TAG);
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		GlobalInfo.getInstance().setCurrentTag(TAG);
		VTLog.i("Fragment", "Num fragment in stack : " + fm.getBackStackEntryCount());
	}
	
    public void handleErrorModelEvent(final ModelEvent modelEvent) {

		VTLog.e("AbstractController handleErrorModelEvent", DateUtils.now() + "Action: "
				+ modelEvent.getActionEvent().action + " Code: " + modelEvent.getModelCode() + " Message: " + modelEvent.getModelMessage());
		VTLog.logToFile("AbstractController handleErrorModelEvent", DateUtils.now() + "Action: "
				+ modelEvent.getActionEvent().action + " Code: " + modelEvent.getModelCode() + " Message: " + modelEvent.getModelMessage());

    	final ActionEvent e = modelEvent.getActionEvent();
    	HTTPRequest request = e.request;
    	String msg = modelEvent.getDataText();
    	if(msg != null && msg.contains(Constants.APACHE_TOMCAT)){
    		modelEvent.setIsSendLog(false);
    	}
    	if (modelEvent.getModelCode() == ErrorConstants.ERROR_EXPIRED_TIMESTAMP){
    		//truong hop timestamp co oauth la time-out thi van set lai thoi gian
    	}else if (modelEvent.getModelCode() == ErrorConstants.ERROR_SESSION_RESET||
    			modelEvent.getCode() == OAuthRequestManager.TIMED_OUT) {
    		modelEvent.setModelCode(ErrorConstants.ERROR_SESSION_RESET);
			GlobalInfo.getInstance().getProfile().getUserData().setLoginState(UserDTO.NOT_LOGIN);
		}
    	
		// check: chi ghi log khi loi server
		if (modelEvent.getIsSendLog() && modelEvent.getModelCode() !=  ErrorConstants.ERROR_NO_CONNECTION 
				&& modelEvent.getModelCode() !=  ErrorConstants.ERROR_SESSION_RESET
				&&  (modelEvent.getModelCode() == ErrorConstants.ERROR_COMMON ||
				   (modelEvent.getModelCode() != ErrorConstants.ERROR_COMMON  && e.action != ActionEventConstant.ACTION_LOGIN ))){
			boolean isInsertTableLog = true;
			if (e.logData != null && e.logData instanceof LogDTO){
				LogDTO logDTO = (LogDTO) e.logData;
				if (logDTO != null && 
						(logDTO.tableType == LogDTO.TYPE_LOG || 
								(LogDTO.STATE_FAIL.equals(logDTO.state)))){
					// TH log loi thi chi can goi len server, ko insert lai table_log
					isInsertTableLog = false;
				}
			}

			String param = modelEvent.getParams();
			try {
				JSONObject json = new JSONObject(param);
				json.remove(IntentConstants.INTENT_USER_NAME);
				json.remove(IntentConstants.INTENT_LOGIN_PASSWORD);

				param = json.toString();
			} catch (JSONException e1) {

			}
			ServerLogger.sendLog(modelEvent.getParams(), modelEvent.getDataText(), 
					isInsertTableLog, TabletActionLogDTO.LOG_SERVER);
		}
		
		// end 
		handleCommonError(modelEvent);
		if (e.sender != null && (request == null || (request != null && request.isAlive()))) {
			if(e.sender instanceof GlobalBaseActivity){
				final GlobalBaseActivity sender = (GlobalBaseActivity) e.sender;
				if(sender.isFinished){
					return;
				}
				sender.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sender.handleErrorModelViewEvent(modelEvent);
					}
				});
			}else if(e.sender instanceof BaseFragment){
				final BaseFragment sender = (BaseFragment) e.sender;
				if(sender.getActivity() == null || sender.isFinished){
					return;
				}
				sender.getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {
						sender.handleErrorModelViewEvent(modelEvent);
					}
				});
			}
		}
	}
    
    /**
     * 
    *  Xu ly cac loi chung
    *  @author: AnhND
    *  @param modelEvent
    *  @return: void
    *  @throws:
     */
    public void handleCommonError(ModelEvent modelEvent){    	
    	ActionEvent actionEvent = modelEvent.getActionEvent();
    	switch(modelEvent.getModelCode()){
    	case ErrorConstants.ERROR_SESSION_RESET:
    		actionEvent.controller = this;
    		break;
    	}
    }
    /**
    * remove all fragment in back stack Muc dich: cac trang o menu chinh khong
	* can luu trong stack khi chuyen cac trang trong menu.
    *  @author: BangHn
    *  @param fm
    *  @return: void
    *  @throws:
     */
	public void removeAllInBackStack(FragmentManager fm) {
		for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
			fm.popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		GlobalInfo.getInstance().popAllTag();

		try {
			fm.executePendingTransactions();
		} catch (Exception e) {
			ServerLogger.sendLog("removeAllInBackStack", VNMTraceUnexceptionLog.getReportFromThrowable(e), TabletActionLogDTO.LOG_EXCEPTION);
		}
	}
	
}
