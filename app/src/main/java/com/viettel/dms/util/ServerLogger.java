/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import java.util.Calendar;
import java.util.Vector;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPListenner;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;
import com.viettel.viettellib.network.http.HttpAsyncTask;
import com.viettel.viettellib.network.http.NetworkUtil;

/**
 *  Send log len server
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class ServerLogger implements HTTPListenner {

	private static ServerLogger logger;

	protected ServerLogger() {

	}

	/**
	* get instance of ServerLogger
	*  @author: TruongHN
	*  @return
	*  @return: ServerLogger
	*  @throws:
	 */
	private static ServerLogger getInstance() {
		if (logger == null) {
			logger = new ServerLogger();
		}
		return logger;
	}


	/**
	*  Send log len server
	*  @author: TruongHN
	*  @param content
	*  @param despcription
	*  @return: void
	*  @throws:
	 */
	public static void sendLog(String content, String despcription, int typeLog) {
		TabletActionLogDTO log = new TabletActionLogDTO();
		if (StringUtil.isNullOrEmpty(content)){
			log.content = "";
		}else{
			log.content = content;
		}
		if (StringUtil.isNullOrEmpty(despcription)){
			log.description = "";
		}else{
			log.description = despcription;
		}
		log.type = typeLog;
		
		sendLog(log, true);
		
	}
	
	/**
	*  Send log len server - kiem tra co can phai luu table log hay khong?
	*  @author: TruongHN
	*  @param content
	*  @param despcription
	*  @param isInsertTableLog
	*  @return: void
	*  @throws:
	 */
	public static void sendLog(String content, String despcription, boolean isInsertTableLog, int typeLog) {
		TabletActionLogDTO log = new TabletActionLogDTO();
		if (StringUtil.isNullOrEmpty(content)){
			log.content = "";
		}else{
			log.content = content;
		}
		if (StringUtil.isNullOrEmpty(despcription)){
			log.description = "";
		}else{
			log.description = despcription;
		}
		log.type = typeLog;
		sendLog(log, isInsertTableLog);
		
	}
	
	/**
	 *  Send log len server - mac dinh lay content la ten phuong thuc bi loi
	 *  @author: TruongHN
	 *  @param content
	 *  @param despcription
	 *  @return: void
	 *  @throws:
	 */
	public static void sendLog(String despcription, int typeLog) {
		//bo qua log loi tomcat
		if(despcription != null && !despcription.contains(Constants.APACHE_TOMCAT)){
			TabletActionLogDTO log = new TabletActionLogDTO();
			log.content = GlobalUtil.getCurrentMethod();
			log.description = despcription;
			log.type = typeLog;
			sendLog(log, true);
		}
	}
	
	/**
	*  Send log len server
	*  @author: TruongHN
	*  @param logMsg
	*  @return: void
	*  @throws:
	 */
	public static void sendLog(TabletActionLogDTO logMsg, boolean isInsertTableLog) {
		// h hien tai
		int currentHour = DateUtils.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
		// cho phep request day log loi tu beginTime - > endTime h
		if (currentHour >= GlobalInfo.getInstance().getBeginTimeAllowSynData()
				&& currentHour < GlobalInfo.getInstance().getEndTimeAllowSynData()) {
			long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			if (logMsg != null
					&& staffId > 0
					&& (!StringUtil.isNullOrEmpty(logMsg.content) 
							|| !StringUtil.isNullOrEmpty(logMsg.description))) {
				JSONArray jarr = new JSONArray();
				jarr.put(logMsg.generateCreateLogSql());
				String logId = GlobalUtil.generateLogId();

				// send to server
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(jarr);
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(jarr.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
				// para.add(IntentConstants.INTENT_STAFF_ID_PARA);
				// para.add(staffId);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				String strJson = NetworkUtil.getJSONObject(para).toString();

				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.ACTION_SEND_LOG_CLIENT;
				e.userData = logMsg;
				if (isInsertTableLog) {
					// ghi lai log
					LogDTO log = new LogDTO();
					log.setType(AbstractTableDTO.TableType.LOG);
					log.logId = logId;
					log.value = strJson;
					log.state = LogDTO.STATE_NONE;
					log.tableType = LogDTO.TYPE_LOG;
					log.userId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
					e.logData = log;
				}

				HTTPRequest httpReq = new HTTPRequest();
				httpReq.setObserver(getInstance());
				httpReq.setUrl(ServerPath.SERVER_PATH);
				httpReq.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
						Constants.CLIENT_ID, Constants.CLIENT_SECRET, 
						Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
				httpReq.setMethod(Constants.HTTPCONNECTION_POST);
				httpReq.setContentType(HTTPMessage.CONTENT_JSON);
				httpReq.setMethodName("queryController/executeSql");
				httpReq.setDataText(strJson);
				httpReq.setUserData(e);

				// sending to server
				new HttpAsyncTask(httpReq).execute();
			}
		}
	}

	@Override
	public void onReceiveData(HTTPMessage mes) {
		// TODO Auto-generated method stub
		ActionEvent actionEvent = (ActionEvent) mes.getUserData();
		ModelEvent model = new ModelEvent();
		model.setDataText(mes.getDataText());
		model.setCode(mes.getCode());
		model.setParams(((HTTPResponse) mes).getRequest().getDataText());
		model.setActionEvent(actionEvent);
		// check null or empty
		if (StringUtil.isNullOrEmpty((String) mes.getDataText())) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
			return;
		}
		
		boolean isDefaultSuccess = false;
		try {
			JSONObject json = new JSONObject((String) mes.getDataText());
			JSONObject result = json.getJSONObject("result");
			int errCode = result.getInt("errorCode");
			if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
				isDefaultSuccess = true;
			}

		} catch (JSONException e) {
			// loi
			VTLog.i("ServerLogger", "OnReceive: action:" + e.getMessage());
		} catch (Exception e) {
			// loi
			VTLog.i("ServerLogger", "OnReceive: action:" + e.getMessage());
		} finally {
			// thanh cong, that bai khong gui tra kq ve view nua
			if (isDefaultSuccess) {
				// TH mac dinh la request create/update/delete du lieu
				updateLog(actionEvent, LogDTO.STATE_SUCCESS);
				// model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				// UserController.getInstance().handleModelEvent(model);
			} else {
				// ghi log loi len server
				updateLog(actionEvent, LogDTO.STATE_FAIL);
				// UserController.getInstance().handleErrorModelEvent(model);
			}
		}

	}

	@Override
	public void onReceiveError(HTTPResponse response) {
		// TODO Auto-generated method stub
		ActionEvent actionEvent = (ActionEvent) response.getUserData();
		ModelEvent model = new ModelEvent();
		model.setDataText(response.getDataText());
		model.setParams(((HTTPResponse) response).getRequest().getDataText());
		model.setActionEvent(actionEvent);

		if (actionEvent.logData != null){
			LogDTO log = (LogDTO) actionEvent.logData;
			if (log != null && LogDTO.STATE_NONE.equals(log.state)) {
				updateLog(actionEvent, LogDTO.STATE_NEW);
			} else {
				updateLog(actionEvent, LogDTO.STATE_FAIL);
			}
			// neu la loi do mang thi ko thuc hien j tiep theo
		}
	}
	
	/**
	*  Cap nhat vao bang log
	*  @author: TruongHN
	*  @param actionEvent
	*  @param newState
	*  @return: void
	*  @throws:
	 */
	public void updateLog(ActionEvent actionEvent, String newState){
		try {
			if (actionEvent.userData != null){
				TabletActionLogDTO logMsg = (TabletActionLogDTO) actionEvent.userData;
				if (logMsg != null){
					if (logMsg.type != TabletActionLogDTO.LOG_FORCE_CLOSE){
						// khong can luu khi log force close
						// xu ly chung cho cac request
						if (actionEvent.logData != null){
							LogDTO logDTO = (LogDTO) actionEvent.logData;
							if (logDTO != null && !StringUtil.isNullOrEmpty(logDTO.logId) && logDTO.tableType == LogDTO.TYPE_LOG){
								logDTO.setType(AbstractTableDTO.TableType.LOG);
								if (LogDTO.STATE_NONE.equals(logDTO.state)){
									logDTO.state = newState;
									logDTO.createDate = DateUtils.now();
									logDTO.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
									logDTO.needCheckDate = actionEvent.isNeedCheckTimeServer ? LogDTO.NEED_CHECK_TIME: LogDTO.NO_NEED_CHECK_TIME;
									try {
										SQLUtils.getInstance().insert(logDTO);
									} catch (Exception e) {
										VTLog.e("updateLog", "insert fail", e);
									}
								}else{
									logDTO.state = newState;
									logDTO.updateDate = DateUtils.now();
									logDTO.updateUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
									if (logDTO.tableType == LogDTO.TYPE_ORDER && !StringUtil.isNullOrEmpty(logDTO.tableId)){
										try {
											SQLUtils.getInstance().updateLogWithOrder(logDTO, logDTO.tableId);
										} catch (Exception e) {
											VTLog.e("updateLog", "update order fail", e);
										}
									}else{
										try {
											SQLUtils.getInstance().update(logDTO);
										} catch (Exception e) {
											VTLog.e("updateLog", "update fail", e);
										}
									}
								}
							}
						}
					}else{
						// save md5
						//GlobalUtil.checksumMD5Database();
						//clear cac thong tin trong ctrinh
						GlobalUtil.clearAllData();
					}
					
				}
			}
		} catch (Exception e) {}
	}
	/**
	 * Ghi log dang nhap tai file 
	 * @author: yennth16
	 * @since: 10:18:52 04-02-2015
	 * @return: void
	 * @throws:  
	 * @param content
	 * @param despcription
	 * @param typeLog
	 */
	public static void sendLogLogin(String content, String despcription, int typeLog) {
		TabletActionLogDTO log = new TabletActionLogDTO();
		if (StringUtil.isNullOrEmpty(content)){
			log.content = "";
		}else{
			log.content = content;
		}
		if (StringUtil.isNullOrEmpty(despcription)){
			log.description = "";
		}else{
			log.description = despcription;
		}
		log.type = typeLog;
		if(GlobalInfo.getInstance().getProfile().getUserData().id > 0
				&& !StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile().getUserData().shopId)) {
			sendLogLogin(log, true);
		}
		
	}
	
	/**
	 * Ghi log dang nhap tai file 
	 * @author: yennth16
	 * @since: 10:16:59 04-02-2015
	 * @return: void
	 * @throws:  
	 * @param logMsg
	 * @param isInsertTableLog
	 */
	 public static void sendLogLogin(TabletActionLogDTO logMsg, boolean isInsertTableLog) {
		// h hien tai
		int currentHour = DateUtils
				.getCurrentTimeByTimeType(Calendar.HOUR_OF_DAY);
		// cho phep request day log loi tu beginTime - > endTime h
		if (currentHour >= GlobalInfo.getInstance().getBeginTimeAllowSynData()
				&& currentHour < GlobalInfo.getInstance()
						.getEndTimeAllowSynData()) {
			long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			if(staffId < 0){
				staffId = -1;
			}
			if (logMsg != null
					&& (!StringUtil.isNullOrEmpty(logMsg.content) || !StringUtil
							.isNullOrEmpty(logMsg.description))) {
				JSONArray jarr = new JSONArray();
				jarr.put(logMsg.generateCreateLogSql());
				String logId = GlobalUtil.generateLogId();

				// send to server
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(jarr);
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(jarr.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				String strJson = NetworkUtil.getJSONObject(para).toString();

				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.ACTION_SEND_LOG_CLIENT;
				e.userData = logMsg;
				if (isInsertTableLog) {
					// ghi lai log
					LogDTO log = new LogDTO();
					log.setType(AbstractTableDTO.TableType.LOG);
					log.logId = logId;
					log.value = strJson;
					log.state = LogDTO.STATE_NONE;
					log.tableType = LogDTO.TYPE_LOG;
					log.userId = String.valueOf(GlobalInfo.getInstance()
							.getProfile().getUserData().id);
					e.logData = log;
				}

				HTTPRequest httpReq = new HTTPRequest();
				httpReq.setObserver(getInstance());
				httpReq.setUrl(ServerPath.SERVER_PATH);
				httpReq.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
						Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
				httpReq.setMethod(Constants.HTTPCONNECTION_POST);
				httpReq.setContentType(HTTPMessage.CONTENT_JSON);
				httpReq.setMethodName(Constants.METHOD_QUERY_EXECUTE_SQL);
				httpReq.setDataText(strJson);
				httpReq.setUserData(e);

				// sending to server
				new HttpAsyncTask(httpReq).execute();
			}
		}
	}
}
