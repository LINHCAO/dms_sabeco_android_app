/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import java.io.Serializable;
import java.util.Vector;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.AbstractController;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPListenner;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPMultiPartRequest;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;
import com.viettel.viettellib.network.http.HttpAsyncTask;
import com.viettel.viettellib.network.http.MultiPartInputStream;
import com.viettel.viettellib.network.http.NetworkUtil;

/**
 *  Lop Abstract model (interface)
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
@SuppressWarnings("serial")
public abstract class AbstractModelService implements HTTPListenner,
		Serializable {

	public static int TIME_OUT_GET_FILE_SQL = 3600000;
	public static int TIME_OUT_LOGIN = 30000;//30 s: 

	public void onReceiveError(HTTPResponse response) {

	}

	public void onReceiveData(HTTPMessage mes) {
		// TODO Auto-generated method stub

	}

	/**
	 * Cap nhat vao bang log
	 * 
	 * @author: TruongHN
	 * @param actionEvent
	 * @param newState
	 * @return: void
	 * @throws:
	 */
	public void updateLog(ActionEvent actionEvent, String newState) {
			if (GlobalUtil.checkActionSave(actionEvent.action)
					&& actionEvent.logData != null) {
				// xu ly chung cho cac request
				LogDTO logDTO = (LogDTO) actionEvent.logData;
				try {
					if (logDTO != null && !StringUtil.isNullOrEmpty(logDTO.logId)) {
						logDTO.setType(AbstractTableDTO.TableType.LOG);
						if (LogDTO.STATE_NONE.equals(logDTO.state)) {
							logDTO.state = newState;
							logDTO.createDate = DateUtils.now();
							logDTO.createUser = GlobalInfo.getInstance().getProfile()
									.getUserData().userCode;
							logDTO.needCheckDate = actionEvent.isNeedCheckTimeServer ? LogDTO.NEED_CHECK_TIME
									: LogDTO.NO_NEED_CHECK_TIME;
							SQLUtils.getInstance().insert(logDTO);
						} else {
							logDTO.state = newState;
							logDTO.updateDate = DateUtils.now();
							logDTO.updateUser = GlobalInfo.getInstance().getProfile()
									.getUserData().userCode;
							if (logDTO.tableType == LogDTO.TYPE_ORDER
									&& !StringUtil.isNullOrEmpty(logDTO.tableId)) {
								SQLUtils.getInstance().updateLogWithOrder(logDTO,
										logDTO.tableId);
							} else {
								SQLUtils.getInstance().update(logDTO);
							}
						}
					}
				}catch (Exception e){
					ServerLogger.sendLog("xoa db", e.getMessage()  + logDTO.tableName + "-" + logDTO.value , TabletActionLogDTO.LOG_EXCEPTION);
				}
			}
	}

	/**
	 * Request text
	 * 
	 * @author: HieuNH
	 * @param method
	 * @param data
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest sendHttpRequest(String method, Vector data,
			ActionEvent actionEvent) {
		HTTPRequest re = new HTTPRequest();
		StringBuffer strBuffer = new StringBuffer();
//		strBuffer.append(NetworkUtil.getJSONString(method, data));
		strBuffer.append(NetworkUtil.getJSONObject(data));

		if (GlobalUtil.checkActionSave(actionEvent.action)) {
			LogDTO log = new LogDTO();
			log.setType(AbstractTableDTO.TableType.LOG);
			log.logId = (String) actionEvent.logData;
			log.value = strBuffer.toString();
			log.state = LogDTO.STATE_NONE;
			log.userId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
			actionEvent.logData = log;
		}
		re.setUrl(ServerPath.SERVER_PATH);
//		re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//				Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
		re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
				Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
		re.setAction(actionEvent.action);
		re.setContentType(HTTPMessage.CONTENT_JSON);
		re.setMethod(Constants.HTTPCONNECTION_POST);
		re.setDataText(strBuffer.toString());
		re.setObserver(this);
		re.setUserData(actionEvent);
		re.setMethodName(method);
		new HttpAsyncTask(re).execute();
		return re;
	}
	

	/**
	 * Send request voi connect, read time out
	 * 
	 * @param method
	 * @param data
	 * @param actionEvent
	 * @param connectTimeOut
	 * @param readTimeOut
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest sendHttpRequest(String method, Vector data,
			ActionEvent actionEvent, int connectTimeOut, int readTimeOut, boolean checkLogin) {
		HTTPRequest re = new HTTPRequest();
		StringBuffer strBuffer = new StringBuffer();
//		strBuffer.append(NetworkUtil.getJSONString(method, data));
		strBuffer.append(NetworkUtil.getJSONObject(data));

		if (GlobalUtil.checkActionSave(actionEvent.action)) {
			LogDTO log = new LogDTO();
			log.setType(AbstractTableDTO.TableType.LOG);
			log.logId = (String) actionEvent.logData;
			log.value = strBuffer.toString();
			log.state = LogDTO.STATE_NONE;
			log.userId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
			actionEvent.logData = log;
		}
		if (checkLogin == true) {
			re.setUrl(ServerPath.SERVER_PATH_LOGIN);
		} else {
			re.setUrl(ServerPath.SERVER_PATH);
		}
//		re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//				Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
		re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
				Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
		re.setAction(actionEvent.action);
		re.setContentType(HTTPMessage.CONTENT_JSON);
		re.setMethod(Constants.HTTPCONNECTION_POST);
		re.setDataText(strBuffer.toString());
		re.setObserver(this);
		re.setUserData(actionEvent);
		re.setMethodName(method);
		new HttpAsyncTask(re, connectTimeOut, readTimeOut).execute();
		return re;
	}

	/**
	 * Request text
	 * 
	 * @author: HieuNH
	 * @param method
	 * @param data
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest sendHttpRequestOffline(String method, Vector data,
			ActionEvent actionEvent) {
		HTTPRequest re = new HTTPRequest();
		StringBuffer strBuffer = new StringBuffer();
//		strBuffer.append(NetworkUtil.getJSONString(method, data));
		strBuffer.append(NetworkUtil.getJSONObject(data));

		if (GlobalUtil.checkActionSave(actionEvent.action)) {
			LogDTO log = new LogDTO();
			log.setType(AbstractTableDTO.TableType.LOG);
			log.logId = (String) actionEvent.logData;
			log.value = strBuffer.toString();
			log.state = LogDTO.STATE_NEW;
			log.userId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
			log.createDate = DateUtils.now();
			log.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			log.needCheckDate = actionEvent.isNeedCheckTimeServer ? LogDTO.NEED_CHECK_TIME
					: LogDTO.NO_NEED_CHECK_TIME;
			actionEvent.logData = log;
			// luu log
			SQLUtils.getInstance().insert(log);
			// kick lai luong offline
			// TransactionProcessManager.getInstance().resetChecking();

		} else {

			re.setUrl(ServerPath.SERVER_PATH);
//			re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//					Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
			re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
					Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
			re.setAction(actionEvent.action);
			re.setContentType(HTTPMessage.CONTENT_JSON);
			re.setMethod(Constants.HTTPCONNECTION_POST);
			re.setDataText(strBuffer.toString());
			re.setObserver(this);
			re.setUserData(actionEvent);
			re.setMethodName(method);
			new HttpAsyncTask(re).execute();
		}

		return re;
	}

	/**
	 * Request len server, voi day du thong tin table
	 * 
	 * @author: TruongHN
	 * @param method
	 * @param data
	 * @param actionEvent
	 * @param tableType
	 * @param tableId
	 * @param tableName
	 * @return: HTTPRequest
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest sendHttpRequestOffline(String method, Vector data,
			ActionEvent actionEvent, int tableType, String tableId,
			String tableName) {
		HTTPRequest re = new HTTPRequest();
		StringBuffer strBuffer = new StringBuffer();
//		strBuffer.append(NetworkUtil.getJSONString(method, data));
		strBuffer.append(NetworkUtil.getJSONObject(data));

		if (GlobalUtil.checkActionSave(actionEvent.action)) {
			LogDTO log = new LogDTO();
			log.setType(AbstractTableDTO.TableType.LOG);
			log.logId = (String) actionEvent.logData;
			log.value = strBuffer.toString();
			log.state = LogDTO.STATE_NEW;
			log.userId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
			log.createDate = DateUtils.now();
			log.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			log.needCheckDate = actionEvent.isNeedCheckTimeServer ? LogDTO.NEED_CHECK_TIME
					: LogDTO.NO_NEED_CHECK_TIME;
			log.tableType = tableType;
			log.tableName = tableName;
			log.tableId = tableId;
			actionEvent.logData = log;
			// luu log
			SQLUtils.getInstance().insert(log);
			// kick lai luong offline
			// TransactionProcessManager.getInstance().resetChecking();

		} else {
			re.setUrl(ServerPath.SERVER_PATH);
//			re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//					Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
			re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
					Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
			re.setAction(actionEvent.action);
			re.setContentType(HTTPMessage.CONTENT_JSON);
			re.setMethod(Constants.HTTPCONNECTION_POST);
			re.setDataText(strBuffer.toString());
			re.setObserver(this);
			re.setUserData(actionEvent);
			re.setMethodName(method);
			new HttpAsyncTask(re).execute();
		}

		return re;
	}

	/**
	 * Request text
	 * 
	 * @author: TruognHN
	 * @param method
	 * @param dataText
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	protected HTTPRequest sendHttpRequest(String method, String dataText,
			ActionEvent actionEvent) {
		HTTPRequest re = new HTTPRequest();
		re.setUrl(ServerPath.SERVER_PATH);
//		re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//				Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
		re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
				Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
		re.setAction(actionEvent.action);
		re.setContentType(HTTPMessage.CONTENT_JSON);
		re.setMethod(Constants.HTTPCONNECTION_POST);
		re.setDataText(dataText);
		re.setObserver(this);
		re.setUserData(actionEvent);
		re.setMethodName(method);
		new HttpAsyncTask(re, false).execute();
		return re;
	}

	/**
	 * Request text
	 * 
	 * @author: HieuNH
	 * @param method
	 * @param data
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest sendHttpRequest(String method, Vector data,
			ActionEvent actionEvent, int timeout) {
		StringBuffer strBuffer = new StringBuffer();
//		strBuffer.append(NetworkUtil.getJSONString(method, data));
		strBuffer.append(NetworkUtil.getJSONObject(data));
		HTTPRequest re = new HTTPRequest();
		re.setUrl(ServerPath.SERVER_PATH);
//		re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//				Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
		re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
				Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
		re.setAction(actionEvent.action);
		re.setContentType(HTTPMessage.CONTENT_JSON);
		re.setMethod(Constants.HTTPCONNECTION_POST);
		re.setDataText(strBuffer.toString());
		re.setMethodName(method);
		re.setObserver(this);
		re.setUserData(actionEvent);
		new HttpAsyncTask(re, timeout).execute();
		return re;
	}

	/**
	 * Request multipart
	 * 
	 * @author: HieuNH
	 * @param method
	 * @param data
	 * @param imgPath
	 * @param actionEvent
	 * @param fileName
	 * @param fileField
	 * @param fileType
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest httpMultiPartRequest(String method, Vector data,
			String imgPath, ActionEvent actionEvent, String fileName,
			String fileField, String fileType) {
		HTTPRequest re = new HTTPMultiPartRequest();
		String dataText = NetworkUtil.getJSONObject(data).toString();
		if (GlobalUtil.checkActionSave(actionEvent.action)) {
			// VTLog.e("AbstractModelService --> Save Log voi ten :",
			// GlobalUtil.getFormatLogFile(timer, method));
			LogDTO log = new LogDTO();
			log.setType(AbstractTableDTO.TableType.LOG);
			log.logId = (String) actionEvent.logData;
			log.value = dataText;
			log.state = LogDTO.STATE_NEW;
			log.tableType = LogDTO.TYPE_IMAGE;
			log.tableId = imgPath;// chuoi img_path
			log.userId = String.valueOf(GlobalInfo.getInstance().getProfile()
					.getUserData().id);
			log.createDate = DateUtils.now();
			log.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			actionEvent.logData = log;

			// luu log
			SQLUtils.getInstance().insert(log);
			// kick lai luong offline
			// TransactionProcessManager.getInstance().resetChecking();
		}

		return re;
	}

	/**
	 * Request multipart test
	 * 
	 * @author: PhucNT
	 * @param method
	 * @param data
	 * @param imgPath
	 * @param actionEvent
	 * @param fileName
	 * @param fileField
	 * @param fileType
	 * @return: void
	 * @throws:
	 */
	protected HTTPRequest httpMultiPartRequest(String method, String data,
			String imgPath, ActionEvent actionEvent, String fileName,
			String fileField, String fileType) {
		//Bitmap bmp = GlobalInfo.getInstance().getBitmapData();
		HTTPRequest re = new HTTPMultiPartRequest();
		re.setContentType(HTTPMessage.MULTIPART_JSON);
		re.setDataTypeSend(HTTPRequest.CONTENT_TYPE_BINARY);
		MultiPartInputStream multiPartStream;
		try {
//			multiPartStream = new MultiPartInputStream(data, imgPath,
//					fileName, fileField, fileType, Constants.MAX_UPLOAD_IMAGE_WIDTH,
//					Constants.MAX_UPLOAD_IMAGE_HEIGHT);
			multiPartStream = new MultiPartInputStream(GlobalInfo.getInstance()
					.getAppContext(), data, imgPath, fileName, fileField,
					fileType, Constants.MAX_UPLOAD_IMAGE_WIDTH,
					Constants.MAX_UPLOAD_IMAGE_HEIGHT);
			
			((HTTPMultiPartRequest) re).setMultipartStream(multiPartStream);
			((HTTPMultiPartRequest) re).setDataText(data);
			re.setUrl(ServerPath.SERVER_PATH);
//			re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//					Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
			re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
					Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
			re.setAction(actionEvent.action);
			re.setMethod("POST");
			re.setMethodName(method);
			re.setObserver(this);
			re.setUserData(actionEvent);

			new HttpAsyncTask(re, false).execute();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return re;
	}
	
	
	/**
	 * Request multipart  1 file zip
	 * 
	 * @author: TamPQ
	 * @param method
	 * @param data
	 * @param imgPath
	 * @param actionEvent
	 * @param fileName
	 * @param fileField
	 * @param fileType
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	protected HTTPRequest httpMultiPartRequest(String method, String filePath,
			ActionEvent actionEvent, String fileName, String fileField,
			String fileType) {
		HTTPRequest re = new HTTPMultiPartRequest();
		re.setContentType(HTTPMessage.MULTIPART_JSON);
		re.setDataTypeSend(HTTPRequest.CONTENT_TYPE_BINARY);
		String dataText = NetworkUtil.getJSONObject(
				(Vector<String>) actionEvent.viewData).toString();
		MultiPartInputStream multiPartStream;
		try {
			multiPartStream = new MultiPartInputStream(dataText, filePath,
					fileName, fileField, fileType, "");
			((HTTPMultiPartRequest) re).setMultipartStream(multiPartStream);
			((HTTPMultiPartRequest) re).setDataText(dataText);
			re.setUrl(ServerPath.SERVER_PATH);
//			re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//					Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
			re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
					Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
			re.setAction(actionEvent.action);
			re.setMethodName(method);
			re.setMethod("POST");
			re.setObserver(this);
			re.setUserData(actionEvent);

			new HttpAsyncTask(re).execute();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return re;
	}

	/**
	 * Request multipart voi anh duoc decode tu link image
	 * 
	 * @author: BangHN
	 * @param method
	 * @param data
	 * @param imgPath
	 * @param actionEvent
	 * @param fileName
	 * @param fileField
	 * @param fileType
	 * @param isUsedDecodeBitmapFromImgPath
	 * @return
	 * @return: HTTPRequest
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	protected HTTPRequest httpMultiPartRequestWithDeoceImageFromImagePath(
			String method, Vector data, String imgPath,
			ActionEvent actionEvent, String fileName, String fileField,
			String fileType) {
		HTTPRequest re = new HTTPMultiPartRequest();
		re.setContentType(HTTPMessage.MULTIPART_JSON);
		re.setDataTypeSend(HTTPRequest.CONTENT_TYPE_BINARY);
		MultiPartInputStream multiPartStream;
		try {
//			String dataText = NetworkUtil.getJSONString(method, data);
//			multiPartStream = new MultiPartInputStream(dataText, imgPath,
//					fileName, fileField, fileType);
			
			String dataText = NetworkUtil.getJSONString(method, data);
			multiPartStream = new MultiPartInputStream(GlobalInfo.getInstance()
					.getAppContext(), dataText, imgPath, fileName, fileField,
					fileType, Constants.MAX_UPLOAD_IMAGE_WIDTH,
					Constants.MAX_UPLOAD_IMAGE_HEIGHT);
			
			((HTTPMultiPartRequest) re).setMultipartStream(multiPartStream);
			((HTTPMultiPartRequest) re).setDataText(dataText);
			re.setUrl(ServerPath.SERVER_PATH);
//			re.setOAuthInfo(ServerPath.SERVER_PATH_OAUTH, 
//					Constants.OAUTH_KEY, Constants.OAUTH_SECRET);
			re.setOAuth2Info(ServerPath.SERVER_PATH_OAUTH, 
					Constants.CLIENT_ID, Constants.CLIENT_SECRET, Constants.GRANT_TYPE, Constants.RESPONSE_TYPE);
			re.setAction(actionEvent.action);
			re.setMethod("POST");
			re.setMethodName(method);
			re.setObserver(this);
			re.setUserData(actionEvent);

			new HttpAsyncTask(re).execute();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
		return re;
	}
	
	/**
	* Gui chuoi JSON len server
	* @author: dungdq3
	* @param: JSONObject jsonObject
	* @param: ActionEvent e
	* @return: void
	* @date: Jan 6, 2014
	*/
	protected void sendJSONOfflineToServer(JSONObject jsonObject, ActionEvent e){
		JSONArray jarr = new JSONArray();
		jarr.put(jsonObject);
		String logId = GlobalUtil.generateLogId();
		e.logData = logId;

		Vector<Object> para = new Vector<Object>();
		para.add(IntentConstants.INTENT_LIST_SQL);
		para.add(jarr.toString());
		para.add(IntentConstants.INTENT_MD5);
		para.add(StringUtil.md5(jarr.toString()));
		para.add(IntentConstants.INTENT_LOG_ID);
		para.add(logId);
//		para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//		para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
		para.add(IntentConstants.INTENT_IMEI_PARA);
		para.add(GlobalInfo.getInstance().getDeviceIMEI());
		sendHttpRequestOffline("queryController/executeSql", para, e);
	}
	
	/**
	* Hàm chung setModel và sẽ sendRequestOffline
	* @author: dungdq3
	* @param: ActionEvent e
	* @param: long returnCode
	* @param: JSONObject sqlPara
	* @param: String string
	* @return: void
	* @throws: 
	* @date: Jan 9, 2014
	*/
	protected void setModelAndSendJSON(ActionEvent e, long returnCode,
			JSONObject sqlPara, String message, AbstractController abController) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		if(returnCode>0){
			if(sqlPara!=null){
				sendJSONOfflineToServer(sqlPara, e);
			}
			model.setModelData(returnCode);
			model.setModelMessage(message);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			abController.handleModelEvent(model);
		}else{
			setHandleErrorModel(model, message, abController);
		}
	}
	
	/**
	* Hàm chung setModel và sẽ sendRequestOffline
	* @author: dungdq3
	* @param: ModelEvent model
	* @param: AbstractController abController
	* @param: String message
	* @return: void
	* @throws: 
	* @date: Jan 9, 2014
	*/
	protected void setHandleErrorModel(ModelEvent model, String message, 
			AbstractController abController) {
		// TODO Auto-generated method stub
		model.setModelCode(ErrorConstants.ERROR_COMMON);
		model.setModelMessage(message);
		abController.handleErrorModelEvent(model);
	}
	
	/**
	* Hàm chung setModel và sẽ sendRequestOffline
	* @author: dungdq3
	* @param: ActionEvent e
	* @param: Object object
	* @param: JSONObject sqlPara
	* @param: String string
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	* @date: Jan 9, 2014
	*/
	protected void setModelAndSendJSON(ActionEvent e, Object object,
			JSONObject sqlPara, String message, AbstractController abController) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		if(object!=null){
			if(sqlPara!=null){
				sendJSONOfflineToServer(sqlPara, e);
			}
			model.setModelData(object);
			model.setModelMessage(message);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			abController.handleModelEvent(model);
		}else{
			setHandleErrorModel(model, message, abController);
		}
	}
}
