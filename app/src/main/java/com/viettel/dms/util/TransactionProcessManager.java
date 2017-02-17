/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.KPILogDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.StaffPositionLogDTO;
import com.viettel.dms.dto.me.NotifyOrderDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.guard.AccessInternetService;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.LoginView;
import com.viettel.dms.view.pg.PGOrderView;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.NetworkUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

/**
 * Xu ly dong bo len server
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class TransactionProcessManager extends GlobalBaseActivity  {
	
	private Timer locTimer;//timer dinh thoi l
	private TimerTask locTask;
	//thoi gian dinh thoi de thuc hien check 3phut
	private final long TIME_LOC_PERIOD = GlobalInfo.getInstance().getTimeSyncToServer();
	// chua kiem tra duoc thoi gian
	private final static int TIME_NOT_CHECK = -1;
	// thoi gian khong hop le
	private final static int TIME_INVALID = 0;
	// thoi gian hop le
	private final static int TIME_VALID = 1;
	// sync binh thuong
	public static final int SYNC_NORMAL = 2;
	// sync tu login
	public static final int SYNC_FROM_LOGIN = 3;
	// syn khi nhan cap nhat
	public static final int SYNC_FROM_UPDATE = 4;
	// so luong generate log vi tri
	private final int NUM_LOG_GENERATE = 20;
		
	private static TransactionProcessManager instance;
	boolean isStarted = false;
	boolean isRunning = false;
	// luu tru cac ds log can goi len server
	ArrayList<LogDTO> listLog = new ArrayList<LogDTO>();
	// luu tru ds cac log vi tri
	ArrayList<LogDTO> listPosition = new ArrayList<LogDTO>();
	// thoi gian hien tai tren server
	private String currentDateServer = null;
	// vi tri hien tai
	//private GPSInfo myPosition = new GPSInfo();
	// ds don hang tra ve khi goi request lay thoi gian server
	private ArrayList<String> listOrderIdGetTime = new ArrayList<String>();
	// bien dung de kiem tra truoc khi day du lieu len server
	private int synType  = SYNC_NORMAL;
	// bien dung de kiem tra retry request
	private String timeRequest = "";
	//finish luong dong bo truoc do hay chua?
	boolean isFinishSynDataBefore = true;

	private static final Object lockObject = new Object();
	public static TransactionProcessManager getInstance() {
		if (instance == null) {
			synchronized (lockObject) {
				if (instance == null) {
					instance = new TransactionProcessManager();
				}
			}			
		}
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//super.onCreate(savedInstanceState);
	}
	
	/**
	 * Kiem tra & start check request
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void startChecking(int synType) {
		this.synType = synType;
		if (locTimer == null || !isStarted){
			isRunning = true;
			displayTimer();
		}
	}
	
	/**
	 * Kick lai luong checking
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void resetChecking(int synType){
		cancelTimer();
		listLog.clear();
		startChecking(synType);
	}
	
	/**
	 * Start timer
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void displayTimer(){
		isFinishSynDataBefore = true;
		locTimer = new Timer();
		locTask = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (isRunning){
					(GlobalInfo.getInstance().getAppHandler()).post(new Runnable() {
						@Override
						public void run() {
							executeProcessTransaction();
						}
					});
				}
			}
		};
		locTimer.schedule(locTask, 0, TIME_LOC_PERIOD);		
		isStarted = true;
	}
	
	

	/**
	* Thuc thi tien trinh chay dinh ky
	* 	- Kiem tra thoi gian idle qua time-out hay khong
	* 	- Thuc hien dong bo
	* @author: BangHN
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	private void executeProcessTransaction(){
		//kiem tra gioi han luong xu ly dong thoi
		int numAsyncTaskActive = AsyncTaskUtil.getNumAsyncTaskActive();
		//goi du lieu thi day du lieu len truoc khi lay ve
		if (GlobalInfo.IS_SEND_DATA_VERSION) {
			if (numAsyncTaskActive <= 3 && isFinishSynDataBefore
					&& GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_ONLINE) {
				isFinishSynDataBefore = false;
				synData();// comment dòng nay lai va mở dong duoi de khong day du lieu ( No log)
			}
		}else {
			// Hard code no_log
			if (synType == SYNC_FROM_UPDATE || synType == SYNC_FROM_LOGIN) {
				gotoSynFromServer();
			}
		}
	}
	
	
	/**
	 * Noi dung can mo ta : dong bo hao du lieu
	 * @author: DucHHA
	 * @return: void
	 * @throws:
	 */
	private void synData(){
		if (synType == SYNC_NORMAL){
			//check thoi gian idle
			if(!validateTimeIdleLargerOn5Minute(System.currentTimeMillis())){
				((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext())
				.sendBroadcast(ActionEventConstant.ACTION_STOP_GOOGLE_PLAY_SERVICE,
					new Bundle());
			}
			if(!validateTimeIdleLargerOnHours(System.currentTimeMillis())){
				((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext())
				.sendBroadcast(ActionEventConstant.ACTION_FINISH_AND_SHOW_LOGIN,
					new Bundle());
			}else{
				// check cac request chua hoan thanh
				int currentHour = DateUtils.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
				//cho phep request tu beginTime - > endTime h
				if (currentHour >= GlobalInfo.getInstance().getBeginTimeAllowSynData() && 
						currentHour < GlobalInfo.getInstance().getEndTimeAllowSynData()) { 
					// tao log position offline insert vao sqlLite
					if (GlobalUtil.checkNetworkAccess()){
						generateJsonListPosition(GlobalInfo.getInstance().getListPositionOffline());
						generateJsonListLogKPI(GlobalInfo.getInstance().getListKPILogOffline());
					}
					checkProcessRequest();
				} 
			}
		}else{
			// check cac request chua hoan thanh
			int currentHour = DateUtils.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
			//cho phep request tu beginTime - > endTime h
			if (currentHour >= GlobalInfo.getInstance().getBeginTimeAllowSynData() && 
					currentHour < GlobalInfo.getInstance().getEndTimeAllowSynData()) { 
				// tao log position offline insert vao sqlLite
				if (GlobalUtil.checkNetworkAccess()){
					generateJsonListPosition(GlobalInfo.getInstance().getListPositionOffline());
				}
				
				if (synType == SYNC_FROM_LOGIN ){
					checkProcessRequestForLogin();
				}else if (synType ==  SYNC_FROM_UPDATE){
					checkProcessRequest();
				}
			}else{
				gotoSynFromServer();
			}
		}
	}
	
	/**
	* Kiem tra thoi gian idle khong su dung, 
	* neu qua thoi gian gui broadcase show login
	* @author: BangHN
	* @param currentTime: thoi gian hien tai
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	private boolean validateTimeIdleLargerOnHours(long currentTime) {
		boolean isOK = true;
		long timeIdle = currentTime
				- GlobalInfo.getInstance().getTimeStartIdleStatus();
		if (timeIdle >= GlobalInfo.getInstance().getTimeAllowIdleStatus()
				&& !GlobalInfo.getInstance().isAppActive()) {
			isOK = false;
		}
		return isOK;
	}
	
	/**
	 * Kiem tra thoi gian idle khong su dung qua 5 phut, 
	 * dung truong hop tat service google
	 * @author: BangHN
	 * @param currentTime: thoi gian hien tai
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private boolean validateTimeIdleLargerOn5Minute(long currentTime) {
		boolean isOK = true;
		long timeIdle = currentTime
				- GlobalInfo.getInstance().getTimeStartIdleStatus();
		if (timeIdle >= 240000
				&& !GlobalInfo.getInstance().isAppActive()) {
			isOK = false;
		}
		return isOK;
	}
	
	/**
	 * Kiem tra cac request chua thuc hien dong bo
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private synchronized void checkProcessRequest(){
		ArrayList<LogDTO> list = new ArrayList<LogDTO>();
		if (synType == SYNC_NORMAL ){
			list = SQLUtils.getInstance().getLogNeedToCheck();
		}else{
			list = SQLUtils.getInstance().getLogNeedToCheckBeforeSync();
		}
		listLog.clear();
		listLog = list;
		this.timeRequest = System.currentTimeMillis()+ "_" + GlobalUtil.rand(0, 1000);
		if (GlobalInfo.getInstance().getProfile().getUserData().getLoginState() == UserDTO.LOGIN_SUCCESS){
			if (listLog != null && listLog.size() > 0){
				currentDateServer = null;
				getCurrentDateTimeServer();
			}else if (synType == SYNC_FROM_UPDATE){
				gotoSynFromServer();
			}
//			if (synType == SYNC_NORMAL){
//				updatePositionToServer();
//			}
		}else if (GlobalInfo.getInstance().getProfile().getUserData().getLoginState() == UserDTO.NOT_LOGIN){
			if (GlobalUtil.checkNetworkAccess()){
				requestRelogin();
			}else{
				// kiem tra cap nhat lai cac trang thai don hang
				if (synType == SYNC_NORMAL){
					getOrderInLog();
				}else if (synType == SYNC_FROM_UPDATE){
					gotoSynFromServer();
				}
			}
		}
		
	}

	/**
	 * Relogin
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void requestRelogin() {
		SharedPreferences sharedPref = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		String pass = sharedPref.getString(LoginView.DMS_PASSWORD, "");
		Vector<String> vt = new Vector<String>();
		vt.add(IntentConstants.INTENT_USER_NAME);
		vt.add(GlobalInfo.getInstance().getProfile().getUserData().userName);

		vt.add(IntentConstants.INTENT_LOGIN_PASSWORD);
//		vt.add(GlobalInfo.getInstance().getProfile().getUserData().pass);
		vt.add(pass);
		vt.add(IntentConstants.INTENT_LOGIN_IS_REMEMBER);
		vt.add("true");

		vt.add(IntentConstants.INTENT_IMEI);
		vt.add(GlobalInfo.getInstance().getDeviceIMEI());
		
		if(!StringUtil.isNullOrEmpty(StringUtil.getSimSerialNumber())){
			vt.add(IntentConstants.INTENT_SIM_SERIAL);
			vt.add(StringUtil.getSimSerialNumber());
		}

		vt.add(IntentConstants.INTENT_LOGIN_PHONE_MODEL);
		vt.add(GlobalInfo.getInstance().PHONE_MODEL);

		vt.add(IntentConstants.INTENT_VERSION_APP);
		vt.add(GlobalInfo.getInstance().getProfile().getVersionApp());

		vt.add(IntentConstants.INTENT_VERSION_DB);
		vt.add(GlobalInfo.getInstance().getProfile().getVersionDB());

		vt.add(IntentConstants.INTENT_LOGIN_PLATFORM);
		vt.add(GlobalInfo.getInstance().PLATFORM_SDK_STRING);

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.RE_LOGIN;
		e.viewData = vt;
		e.sender = this;
		UserController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * Kiem tra thuc hien request len server truoc khi syn ve
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private synchronized void checkProcessRequestForLogin(){
		ArrayList<LogDTO> list = SQLUtils.getInstance().getLogNeedToCheckForLogin();
		listLog.clear();
		listLog = list;
		this.timeRequest = System.currentTimeMillis()+ "_" + GlobalUtil.rand(0, 1000);
		GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_EXECUTING;
		if (GlobalInfo.getInstance().getProfile().getUserData().getLoginState() == UserDTO.LOGIN_SUCCESS){
			// goi log tiep theo neu co
			if (listLog != null && listLog.size() > 0){
				this.currentDateServer = GlobalInfo.getInstance().getProfile().getUserData().serverDate;
				checkAndRequestLog();
			}else{
				// tiep tuc thuc hien
				gotoSynFromServer();
			}
		}else if (GlobalInfo.getInstance().getProfile().getUserData().getLoginState() == UserDTO.NOT_LOGIN){
			if (GlobalUtil.checkNetworkAccess()){
				requestRelogin();
			}else{
				// tiep tuc thuc hien
				gotoSynFromServer();
			}
		}
		
	}
	
	/**
	 * Lay thoi gian hien tai tren server
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void getCurrentDateTimeServer() {
		if (GlobalUtil.checkNetworkAccess()){
			ActionEvent e = new ActionEvent();
			Bundle bundle = new Bundle();
			bundle.putBoolean(IntentConstants.INTENT_IS_SYNC, true);
			e.viewData = bundle;
			e.sender = this;
			e.action = ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER;
			UserController.getInstance().handleViewEvent(e);
		}else{
			 //neu offline ma ko co mang thi ko thuc hien request len server
			 //kiem tra cap nhat lai cac trang thai don hang
			getOrderInLog();
		}
	}
	

	/**
	 * Thuc hien retry lai request goi that bai
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void requestUpdatePosition(LogDTO log){
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.REQUEST_UPDATE_POSITION;
		e.sender = this;
		e.viewData = log.value;
		e.logData = log;
		UserController.getInstance().handleViewEvent(e);
	}

	
	/**
	 * Cancel timer
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void cancelTimer(){
		if (locTimer != null) {
			locTimer.cancel();
			locTimer = null;
			isRunning = false;
		}
		if (locTask != null){
			locTask.cancel();
			locTask = null;
			isRunning = false;
		}
		isStarted = false;
	}
	
	/**
	 * Kiem tra luong dong bo co dang thuc hien hay ko
	 * @author: TruongHN
	 * @return: boolean
	 * @throws:
	 */
	public boolean isStarting(){
		boolean isStarting = false;
		if (this.isRunning && this.isStarted){
			isStarting = true; 
		}else{
			isStarting = false;
		}
		return isStarting;
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
			case ActionEventConstant.RETRY_UPLOAD_PHOTO:
			case ActionEventConstant.ACTION_RETRY_REQUEST:
				// request da dc gui thanh cong
				// kiem tra thuc hien request tiep theo
				if (e.userData != null &&  this.timeRequest.equals(e.userData.toString())){
					LogDTO logData = (LogDTO)e.logData;
					if (logData != null && listLog != null){
						for (int i = 0, size = listLog.size(); i < size; i ++){
							if (logData.logId.equals(listLog.get(i).logId)){
								// remove log
								listLog.remove(i);
								break;
							}
						}
						// goi log tiep theo neu co  & loi phai != mat session
						if (listLog.size() > 0){
							checkAndRequestLog();
						}else{
							// kiem tra cap nhat lai cac trang thai don hang
							getOrderInLog();
						}
					}else{
						// kiem tra cap nhat lai cac trang thai don hang
						getOrderInLog();
					}
				}else {
					//finish luong hien tai
					isFinishSynDataBefore = true;
				}
				break;
			case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:
				if (modelEvent.getModelCode() == ErrorConstants.ERROR_CODE_SUCCESS){
					Bundle dataResult = (Bundle)modelEvent.getModelData();
					processGetServerTimeResult(dataResult);
				}else{
					if (synType == SYNC_NORMAL){
						getOrderInLog();
					}else if (synType == SYNC_FROM_UPDATE || synType == SYNC_FROM_LOGIN){
						gotoSynFromServer();
					}
				}
				break;
			case ActionEventConstant.RE_LOGIN:
				UserDTO userDTO = (UserDTO) modelEvent.getModelData();
				if (userDTO != null
						&& (userDTO.appVersion != null || userDTO.dbVersion != null)) {
					((GlobalBaseActivity) GlobalInfo.getInstance()
							.getActivityContext()).sendBroadcast(
									ActionEventConstant.UPGRADE_NOTIFY, new Bundle());
				}else if (synType == SYNC_NORMAL || synType == SYNC_FROM_UPDATE){
//					UserDTO userDTO = (UserDTO) modelEvent.getModelData();
					GlobalInfo.getInstance().getProfile().setUserData(userDTO);
					currentDateServer = null;
					getCurrentDateTimeServer();
//					if (synType == SYNC_NORMAL){
//						updatePositionToServer();
//					}
				}else if (synType == SYNC_FROM_LOGIN){
					this.currentDateServer = GlobalInfo.getInstance().getProfile().getUserData().serverDate;
					checkAndRequestLog();
				}
				
				break;
			case ActionEventConstant.REQUEST_UPDATE_POSITION:
				// goi tiep log
				// thuc hien request tiep theo 
				LogDTO logPos = (LogDTO)e.logData;
				if (logPos != null){
					for (int i = 0, size = listPosition.size(); i < size; i ++){
						if (logPos.logId.equals(listPosition.get(i).logId)){
							// remove log
							listPosition.remove(i);
							break;
						}
					}
					// goi log tiep theo neu co
					if (listPosition.size() > 0){
						requestUpdatePosition(listPosition.get(0));
					}
				}
				break;
			default:
				super.handleModelViewEvent(modelEvent);
				break;
		}
	}
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
			case ActionEventConstant.ACTION_RETRY_REQUEST:
			case ActionEventConstant.RETRY_UPLOAD_PHOTO:
				// cancel request
				// thuc hien request tiep theo 
				if (e.userData != null &&  this.timeRequest.equals(e.userData.toString())){
					LogDTO logData = (LogDTO)e.logData;
					if (logData != null && listLog != null){
						for (int i = 0, size = listLog.size(); i < size; i ++){
							if (logData.logId.equals(listLog.get(i).logId)){
								// remove log
								listLog.remove(i);
								break;
							}
						}
						// goi log tiep theo neu co  & loi phai != mat session
						if (listLog.size() > 0 && modelEvent.getModelCode() != ErrorConstants.ERROR_SESSION_RESET){
							checkAndRequestLog();
						}else{
							// kiem tra cap nhat lai cac trang thai don hang
							getOrderInLog();
						}
					}else{
						//finish luong hien tai
						isFinishSynDataBefore = true;
					}
				}else{
					//finish luong hien tai
					isFinishSynDataBefore = true;
				}
				break;
			case ActionEventConstant.RE_LOGIN:// relogin that bai
				GlobalInfo.getInstance().getProfile().getUserData().setLoginState(UserDTO.NOT_LOGIN);
				if (synType == SYNC_NORMAL){
					getOrderInLog();
				}else if (synType == SYNC_FROM_UPDATE || synType == SYNC_FROM_LOGIN){
					gotoSynFromServer();
				}
				break;
			case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:
				// TH not login thi relogin lai
				if (modelEvent.getModelCode() == ErrorConstants.ERROR_SESSION_RESET){
					// relogin
					requestRelogin();
				}else{
					// TH lay thoi gian server khong dc, nhung request loi hoac request ko can kiem tra thoi gian thi van day len server
					if (listLog != null && listLog.size() > 0){
						LogDTO l = null;
						ArrayList<LogDTO> list = new ArrayList<LogDTO>();
						for (int i = 0, size = listLog.size(); i < size; i ++){
							l = listLog.get(i);
							if (l.tableType == LogDTO.TYPE_LOG ||
									l.needCheckDate == LogDTO.NO_NEED_CHECK_TIME ){
									// neu la request log loi, hoac la request khong can kiem tra thoi gian, hoac request binh thuong ma thoi gian hop le
									// goi request dong bo len server
								list.add(l);
							}
						}
						listLog.clear();
						listLog = list;
						if (listLog != null && listLog.size() > 0){
							retryRequest(listLog.get(0));
						}else{
							// kiem tra cap nhat lai cac trang thai don hang
							getOrderInLog();
						}
					}
				}
				break;
			case ActionEventConstant.REQUEST_UPDATE_POSITION:
				// goi tiep log
				// thuc hien request tiep theo 
				LogDTO logPos = (LogDTO)e.logData;
				if (logPos != null){
					for (int i = 0, size = listPosition.size(); i < size; i ++){
						if (logPos.logId.equals(listPosition.get(i).logId)){
							// remove log
							listPosition.remove(i);
							break;
						}
					}
					// goi log tiep theo neu co
					if (listPosition.size() > 0){
						requestUpdatePosition(listPosition.get(0));
					}
				}
				break;
			default:
				super.handleErrorModelViewEvent(modelEvent);
				break;
		}

	}

	/**
	 * Xu ly sau khi lay thoi gian tu server ve
	 * @author: TruongHN
	 * @param dataResult
	 * @return: void
	 * @throws:
	 */
	private void processGetServerTimeResult(Bundle dataResult){
		String data = "";
		if (dataResult != null ){
			data = dataResult.getString(IntentConstants.INTENT_SERVER_DATE);
			if (!String.valueOf(ErrorConstants.ERROR_NO_CONNECTION).equals(data)){
				listOrderIdGetTime.clear();
				listOrderIdGetTime = dataResult.getStringArrayList(IntentConstants.INTENT_HAVE_RETURN_ORDER);
				boolean haveReturnOrder = false;
				if (listOrderIdGetTime != null && listOrderIdGetTime.size() > 0){
					haveReturnOrder = true;
					// kiem tra co can notify ra ngoai khi chuong trinh dang inactive hay ko
					boolean isIdNew = false;
					for (int i = 0, size = listOrderIdGetTime.size(); i < size; i++){
						String id = listOrderIdGetTime.get(i);
						if (!GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderId.contains(id)){
							GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderId.add(id);
							isIdNew = true;
						}
					}
					
					if (isIdNew){
						// ngoai chuong trinh thi notify thong bao
						// neu ds don hang da co truoc do, hoac da duoc update thi ko can notify
						if (!GlobalInfo.getInstance().isAppActive()){
							Bundle bundle = new Bundle();
							bundle.putInt(IntentConstants.INTENT_NUM_RETURN_ORDER, listOrderIdGetTime.size());
							handleNotifyInActiveView(1, bundle);
						}
					}
				}
				GlobalInfo.getInstance().notifyOrderReturnInfo.isSyncDataFromServer = haveReturnOrder;
			}
		}
		// TH online
		if (!String.valueOf(ErrorConstants.ERROR_NO_CONNECTION).equals(data)){
			try {
				currentDateServer = data;
				// goi log tiep theo neu co
				if (listLog != null && listLog.size() > 0){
					checkAndRequestLog();
				}else{
					// kiem tra cap nhat lai cac trang thai don hang
					getOrderInLog();
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
				// kiem tra cap nhat lai cac trang thai don hang
				getOrderInLog();
			}
		}else{
			// neu offline ma ko co mang thi ko thuc hien request len server
			// kiem tra cap nhat lai cac trang thai don hang
			getOrderInLog();
		}
	}

	/**
	 * Cap nhat ds log co thoi gian ko hop le
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void updateLogWithInvalidTime(){
		ArrayList<LogDTO> result = new ArrayList<LogDTO>();
		if (listLog != null){
			LogDTO l;
			for (int i = 0, size = listLog.size(); i < size; i ++){
				l = listLog.get(i);
				if (l.needCheckDate == LogDTO.NEED_CHECK_TIME && checkValidTime(l.createDate) == TIME_INVALID
						&& l.isCheckTime == 1){
					l.updateDate = DateUtils.now();
					result.add(l);
				}
				
			}
		}
		if (result.size() > 0){
			// thuc hien cancel cac log co cung ngay tao khong hop le
			// cap nhat cac don hang ma co ngay tao ko hop le
			SQLUtils.getInstance().updateLogWithStateClose(result);
		}
		
	}
	
	/**
	*  Kiem tra thoi gian request hop le khong?
	*  @author: TruongHN
	*  @return: int - 0: thoi gian khong hop le, 1: thoi gian hop le, -1: chua kiem tra duoc do loi,...
	*  @throws:
	 */
	private int checkValidTime(String createTime){
		// kiem tra thoi gian hop le khong
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateCreate;
		Date dateServer;
		int validTime = TIME_NOT_CHECK; // mac dinh la chua check duoc
		
		try {
			if (!StringUtil.isNullOrEmpty(createTime) && !StringUtil.isNullOrEmpty(currentDateServer) ){
				// thoi gian tao/ thoi gian dat hang
				dateCreate = formatter.parse(createTime);
				dateServer = formatter.parse(currentDateServer);
				// thoi gian hien tai
//				String currentDateClient = DateUtils.now();
				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
				Date dateCreate1 = formatter1.parse(createTime);
				Date dateServer1 = formatter1.parse(currentDateServer);
				
				long secs = (dateServer.getTime() - dateCreate
						.getTime()) / 1000;
				int hours = (int) secs / 3600;
				
				// neu thoi gian tao lon hon thoi gian server, hoac ngay tao < ngay hien tai --> fail
				// neu ngay dat hang khac ngay server, hoac thoi gian dat hang > thoi gian server 1h --> fail
				if ( hours <= -GlobalInfo.getInstance().getTimeTestOrder() || dateServer1.compareTo(dateCreate1) != 0){
					// thoi gian khong hop le, thong bao va hien thi setting time
					validTime = TIME_INVALID;
						
				}else{
					// thoi gian hop le, goi request dong bo len server
					validTime = TIME_VALID;
				}
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}
		return validTime;
		
	}
	
	/**
	 * Hien thi man hinh setting thoi gian
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void displaySettingTime(){
		AlertDialog alertDialog = null;
		try {
			alertDialog = new AlertDialog.Builder(GlobalInfo.getInstance().getActivityContext()).create();
			alertDialog.setMessage(StringUtil.getString(R.string.ERROR_TIME_ONLINE_INVALID));
			alertDialog.setButton(StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							// hien thi man hinh setting 
							GlobalInfo.getInstance().getActivityContext().startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
							//khong cam setting neu bi chan
							AccessInternetService.unlockAppPrevent(true);
							return;

						}
					});
			alertDialog.show();
		} catch (Throwable ex) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
	}
	
	
	/**
	 * Thuc hien retry lai request goi that bai
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void retryRequest(LogDTO log){
		// update log retry
		if (log.tableType == LogDTO.TYPE_IMAGE){
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.RETRY_UPLOAD_PHOTO;
			e.sender = this;
			e.viewData = log.value;
			e.logData = log;
			e.userData = timeRequest;
			UserController.getInstance().handleViewEvent(e);
		}else{
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_RETRY_REQUEST;
			e.sender = this;
			e.viewData = log.value;
			e.logData = log;
			e.userData = timeRequest;
			UserController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * Lay cac don hang trong log
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void getOrderInLog(){
		// neu la dong bo tu login hoac nhan update thi broadcast ve xu ly tiep
		if (this.synType == SYNC_FROM_LOGIN || this.synType == SYNC_FROM_UPDATE){
			gotoSynFromServer();
		} else if (this.synType == SYNC_NORMAL){
			((GlobalBaseActivity)GlobalInfo.getInstance().getActivityContext()).sendBroadcast(ActionEventConstant.ACTION_FINISH_SYN_SYNDATA_NORMAL, new Bundle());
		}
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				NotifyOrderDTO notifyDTO = SQLUtils.getInstance().getOrderNeedNotify();
				Bundle extras = new Bundle();
				extras.putInt(IntentConstants.INTENT_DATA, 0);
				((GlobalBaseActivity) GlobalInfo.getInstance().getActivityContext()).sendBroadcast(
						ActionEventConstant.NOTIFY_DATA_NOT_SYN, extras);
				if (notifyDTO != null){
					boolean hasOrderFail = false;
					// cap nhat ds don hang neu man hinh ds don hang dang active
					if (notifyDTO.listOrderInLog.size() > 0){
						Bundle bundle = new Bundle();
						//b.putInt(IntentConstants.INTENT_DATA, count);
						bundle.putSerializable(IntentConstants.INTENT_DATA, notifyDTO.listOrderInLog);
						((GlobalBaseActivity)GlobalInfo.getInstance().getActivityContext()).
							sendBroadcast(ActionEventConstant.NOTIFY_LIST_ORDER_STATE, bundle);
					}
					// hien thi icon don hang chua goi
					ArrayList<String> listOrderId = new ArrayList<String>();
					// luu ds don hang da xu ly ok
					for (int i = 0, size = notifyDTO.listOrderInLog.size(); i < size; i++){
						if (LogDTO.STATE_NEW.equals(notifyDTO.listOrderInLog.get(i).state)|| 
								LogDTO.STATE_FAIL.equals(notifyDTO.listOrderInLog.get(i).state) ||
								LogDTO.STATE_INVALID_TIME.equals(notifyDTO.listOrderInLog.get(i).state) ||
								LogDTO.STATE_UNIQUE_CONTRAINTS.equals(notifyDTO.listOrderInLog.get(i).state)){
							listOrderId.add(notifyDTO.listOrderInLog.get(i).tableId);
						}
					}
					
					hasOrderFail = listOrderId.size() > 0 ? true : false;
					if (!hasOrderFail){
						hasOrderFail = notifyDTO.numOrderReturnNPP  > 0 ? true : false;
					}
					if (!hasOrderFail){
						if (GlobalInfo.getInstance().notifyOrderReturnInfo.isSyncDataFromServer){
							for (int i = 0, size = listOrderIdGetTime.size(); i < size; i++){
								if (!GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderProcessedId.contains(listOrderIdGetTime.get(i))){
									// neu don hang tra ve nhung da xu ly roi
									hasOrderFail = true;
								}else{
									if (GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderId.contains(listOrderIdGetTime.get(i))){
										GlobalInfo.getInstance().notifyOrderReturnInfo.listOrderId.remove(listOrderIdGetTime.get(i));
									}
								}
							}
						}
						GlobalInfo.getInstance().notifyOrderReturnInfo.isSyncDataFromServer  = hasOrderFail; 
					}
					GlobalInfo.getInstance().notifyOrderReturnInfo.hasOrderFail = hasOrderFail;
					((GlobalBaseActivity)GlobalInfo.getInstance().getActivityContext()).sendBroadcast(ActionEventConstant.NOTIFY_ORDER_STATE, new Bundle());
				}
				return null;
			}
		};
		task.execute();
		//finish luong hien tai
		isFinishSynDataBefore = true;
	}

	/**
	 * Tiep tuc thu hien cap nhat du lieu tu server ve
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void gotoSynFromServer() {
		GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
		((GlobalBaseActivity)GlobalInfo.getInstance().getActivityContext()).
		sendBroadcast(ActionEventConstant.REQUEST_TO_SERVER_SUCCESS, new Bundle());
		this.cancelTimer();
		this.listLog.clear();
		this.synType = SYNC_NORMAL;
		isFinishSynDataBefore = true;
	}
	
	/**
	 * Kiem tra log request va thuc hien retry request
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void checkAndRequestLog(){
		while (listLog != null && listLog.size() > 0){
			LogDTO log = listLog.get(0);
			if (log.tableType == LogDTO.TYPE_LOG ||
				log.needCheckDate == LogDTO.NO_NEED_CHECK_TIME){
				// neu la request log loi, hoac la request khong can kiem tra thoi gian
				// goi request dong bo len server
				retryRequest(log);
				break;
			}else if (log.tableType != LogDTO.TYPE_LOG && log.needCheckDate == LogDTO.NEED_CHECK_TIME){
				// hoac request can kiem tra ma thoi gian hop le
				int validTime = checkValidTime(log.createDate);
				if (validTime == TIME_VALID || log.isCheckTime == 0){
					// hop le
					retryRequest(log);
					break;
				}else if (validTime == TIME_INVALID){
					// khong hop le
					if (this.synType == SYNC_FROM_LOGIN || this.synType == SYNC_FROM_UPDATE){
						// neu la login, hoac cap nhat du lieu thi bo qua di tiep --> request nay xu ly sau
						removeFirstLog();
					}else{
						// --> hien thi setting
						displaySettingTime();
						// --> cap nhat log ve loi
						updateLogWithInvalidTime();
						// kiem tra cap nhat lai cac trang thai don hang
						getOrderInLog();
						break;
					}
				}else if (validTime == TIME_NOT_CHECK){
					// chua kiem tra duoc, thuc hien request nay lai sau
					removeFirstLog();
				}
			}else{
				// se khong co TH nay dau :D
				removeFirstLog();
			}
		}
	}
	
	/**
	 * Remove log - su dung cho ham checkAndRequestLog
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void removeFirstLog(){
		if (listLog.size() > 0){
			listLog.remove(0);
		}
		if (listLog.size() == 0){
			// kiem tra cap nhat lai cac trang thai don hang
			getOrderInLog();
		}
	}
	
	private void generateJsonListPosition(ArrayList<StaffPositionLogDTO> listPos){
		if (listPos != null && listPos.size() > 0){
			ArrayList<AbstractTableDTO> listDTO = new ArrayList<AbstractTableDTO>();
			int length = 0;
			do{
				// moi lan tao chi tao 20 record cho 1 json thoi
				JSONArray listSql = new JSONArray();
				for (int  i = 0, size = listPos.size(); (i + length) < size && i < NUM_LOG_GENERATE; i++){
					StaffPositionLogDTO pos = listPos.get(i + length);
					JSONObject json = pos.generateCreateSql();
					if (json != null){
						listSql.put(json);
					}
				}
				
				// khoi tao sql
				String id =  GlobalUtil.generateLogId();
				
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(listSql);
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				
				// tao log
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(NetworkUtil.getJSONObject(para));
				
				LogDTO log = new LogDTO();
				log.setType(AbstractTableDTO.TableType.LOG);
				log.logId = id;
				log.value = strBuffer.toString();
				log.state = LogDTO.STATE_NONE;
				log.userId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
				log.createDate = DateUtils.now();
				log.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
				log.needCheckDate =  LogDTO.NO_NEED_CHECK_TIME;
				log.tableType = LogDTO.TYPE_POSITION;
				log.state = LogDTO.STATE_NEW;
				log.setAction(AbstractTableDTO.TableAction.INSERT);
				listDTO.add(log);
				
				length += listSql.length();
				
			}while(length < listPos.size());
			if (listDTO.size() > 0){
				GlobalInfo.getInstance().clearListPosition();
				SQLUtils.getInstance().executeListDTO(listDTO);
			}
			
		}
	}
	
	/**
	 * tao danh sach json cho log KPI
	 * @author: trungnt56
	 * @param: @param kpiLogList
	 * @return: void
	 * @throws:
	 */
	private void generateJsonListLogKPI(ArrayList<KPILogDTO> kpiLogList){
		if (kpiLogList != null && kpiLogList.size() > 0){
			ArrayList<AbstractTableDTO> listDTO = new ArrayList<AbstractTableDTO>();
			int length = 0;
			do{
				// moi lan tao chi tao 20 record cho 1 json thoi
				JSONArray listSql = new JSONArray();
				for (int  i = 0, size = kpiLogList.size(); (i + length) < size && i < NUM_LOG_GENERATE; i++){
					KPILogDTO kpiLog = kpiLogList.get(i + length);
					JSONObject json = kpiLog.generateCreateKpiLogSql();
					if (json != null){
						listSql.put(json);
					}
				}
				
				// khoi tao sql
				String id =  GlobalUtil.generateLogId();
				
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(listSql);
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(listSql.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				
				// tao log
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(NetworkUtil.getJSONObject(para));
				
				LogDTO log = new LogDTO();
				log.setType(AbstractTableDTO.TableType.LOG);
				log.logId = id;
				log.value = strBuffer.toString();
				log.state = LogDTO.STATE_NONE;
				log.userId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
				log.createDate = DateUtils.now();
				log.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
				log.needCheckDate =  LogDTO.NO_NEED_CHECK_TIME;
				log.tableType = LogDTO.TYPE_NORMAL;
				log.state = LogDTO.STATE_NEW;
				log.setAction(AbstractTableDTO.TableAction.INSERT);
				listDTO.add(log);
				
				length += listSql.length();
				
			}while(length < kpiLogList.size());
			if (listDTO.size() > 0){
				GlobalInfo.getInstance().clearLogKPI();
				SQLUtils.getInstance().executeListDTO(listDTO);
			}
			
		}
	}
}
