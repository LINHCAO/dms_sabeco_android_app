/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;
import java.util.Vector;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SynDataController;
import com.viettel.dms.download.DownloadFile;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.DBVersionDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.syndata.SynDataDTO;
import com.viettel.dms.dto.syndata.SynDataTableDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.sync.SynDataTableDAO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.LoginView;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;

/**
 * Model dong bo
 * @author: ThuatTQ
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class SynDataModelService extends AbstractModelService {

	protected static SynDataModelService instance;

	protected SynDataModelService() {
	}

	public static SynDataModelService getInstance() {
		if (instance == null) {
			instance = new SynDataModelService();
		}
		return instance;
	}

	public void onReceiveData(HTTPMessage mes) {

		ActionEvent actionEvent = (ActionEvent) mes.getUserData();
		ModelEvent model = new ModelEvent();
		String msgText = mes.getDataText();
		model.setDataText(msgText);
		model.setCode(mes.getCode());
		model.setParams(((HTTPResponse) mes).getRequest().getDataText());
		model.setActionEvent(actionEvent);

		if (StringUtil.isNullOrEmpty((String) mes.getDataText())) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SynDataController.getInstance().handleErrorModelEvent(model);
			return;
		}

		// chung thuc thoi gian sai
		if (!StringUtil.isNullOrEmpty(msgText)
				&& msgText.contains(Constants.EXPIRED_TIMESTAMP)) {
			model.setModelCode(ErrorConstants.ERROR_EXPIRED_TIMESTAMP);
			SynDataController.getInstance().handleErrorModelEvent(model);
			return;
		}

		switch (mes.getAction()) {
			case ActionEventConstant.ACTION_GET_LINK_SQL_FILE: {
				JSONObject json;

				try {
					json = new JSONObject((String) mes.getDataText());
					VTLog.i("SynData", DateUtils.now() + ": Respone url db: "
							+ json.toString());
					JSONObject result = json.getJSONObject("result");
					int errCode = result.getInt("errorCode");
					model.setModelCode(errCode);
					if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
						DBVersionDTO glo = new DBVersionDTO();
						// parse va luu tru cac gia tri
						glo.parseGetLinkDB(result.getJSONObject("response"));
						model.setModelData(glo);
						SynDataController.getInstance().handleModelEvent(model);
					} else {
						model.setModelMessage(result.getString("errorMessage"));
						SynDataController.getInstance()
								.handleErrorModelEvent(model);
					}

				} catch (JSONException e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SynDataController.getInstance().handleErrorModelEvent(model);
				} catch (Exception e) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SynDataController.getInstance().handleErrorModelEvent(model);
				}
				break;
			}
			case ActionEventConstant.ACTION_SYN_SYNDATA_MAX:
			case ActionEventConstant.ACTION_SYN_SYNDATA: {
				try {
					String dataText = (String) mes.getDataText();
					JSONObject json = new JSONObject(dataText);
					JSONObject result = json.getJSONObject("result");
					if (result == null){
						ServerLogger.sendLog("Token syndata",(String) mes.getDataText(), TabletActionLogDTO.LOG_SERVER);
						throw new ErrorSynDataNull();
					}
					JSONObject response = result.getJSONObject("response");
					// get response & error code
					int errCode = result.getInt("errorCode");
					if (ErrorConstants.ERROR_CODE_SUCCESS == errCode) {
						int type = response.getInt("type");
						if (SynDataDTO.TYPE_FILE == type) {
							String url = response.getString("rowData");
							String fileName = StringUtil
									.getFileNameRromURLString(url);
							VTLog.e("SYNDATA", "Get file dong bo du lieu: " + url);
							downloadFileUpdateAndUnzip(url, fileName);

							// doc file va tien hanh update dong bo
							File file = new File(ExternalStorage.getPathSynData(),
									fileName);
							if (file.exists()) {
								executeSynDataFile(model, file, response);
							} else {
								ServerLogger.sendLog(
										"SYNDATA_ERROR",
										"File not exists: "
												+ file.getAbsolutePath()
												+ "  - Respones: " + dataText,
										TabletActionLogDTO.LOG_CLIENT);
								model.setDataText("Exception sync res  : File not exists");
								model.setModelCode(ErrorConstants.ERROR_COMMON);
								SynDataController.getInstance()
										.handleErrorModelEvent(model);
								GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
							}
							file = null;
						} else {
							VTLog.e("SYNDATA", "Dong bo du lieu theo rowdata");
							executeSynDataRows(model, result);
						}
					} else {
						// log loi len server truong hop dong bo tra ve loi
						if (errCode != ErrorConstants.ERROR_SESSION_RESET) {
							ServerLogger.sendLog("SYNDATA_ERROR",
									"Param: " + model.getParams()
											+ "  - Respones: " + dataText,
									TabletActionLogDTO.LOG_SERVER);
						}
						model.setDataText("Exception sync res: " + dataText);
						model.setModelCode(errCode);
						SynDataController.getInstance()
								.handleErrorModelEvent(model);
						GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
					}
				} catch (Exception e) {
					model.setIsSendLog(false);
					boolean isErrorNullData = (e instanceof ErrorSynDataNull);
					//loi mang, null data, khong can goi log
					if (!isErrorNullData) {
						ServerLogger.sendLog("SYNDATA_ERROR", e.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
					}
					VTLog.e("SYNDATA_ERROR", "isErrorNullData");
					VTLog.e("SYNDATA_ERROR", VNMTraceUnexceptionLog.getReportFromThrowable(e));
					model.setModelMessage(e.getMessage());
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SynDataController.getInstance().handleErrorModelEvent(model);
					GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
				}
				break;
			}
			default:
				break;
		}
	}

	public void onReceiveError(HTTPResponse response) {
		ActionEvent actionEvent = (ActionEvent) response.getUserData();
		ModelEvent model = new ModelEvent();
		String msgText = response.getDataText();
		model.setDataText(msgText);
		model.setParams(((HTTPResponse) response).getRequest().getDataText());
		model.setActionEvent(actionEvent);
		//chung thuc thoi gian sai
		if(!StringUtil.isNullOrEmpty(msgText) && msgText.contains(Constants.EXPIRED_TIMESTAMP)){
			model.setModelCode(ErrorConstants.ERROR_EXPIRED_TIMESTAMP);
		}else{
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
		}
		model.setModelMessage(response.getErrMessage());
		SynDataController.getInstance().handleErrorModelEvent(model);

	}



	/**
	 * Dong bo du lieu voi RowData tu server tra ve
	 * Moi lan tra la N dong du lieu dong goi kieu json
	 * @author: BANGHN
	 * @param model
	 * @param result
	 * @throws Exception
	 */
	private void executeSynDataRows(ModelEvent model, JSONObject result) throws Exception{
		int errCode = result.getInt("errorCode");
		model.setModelCode(errCode);
		if(GlobalInfo.getInstance().stateSynData == GlobalInfo.SYNDATA_CANCELED){
			return;
		}
		if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
			GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_EXECUTING;
			JSONObject response = result.getJSONObject("response");
			// lay cac tham so tra ve
			SynDataDTO synDataDTO = new SynDataDTO();
			synDataDTO.setLastLogId_update(response.getLong("lastLogId_update"));
			synDataDTO.setMaxDBLogId(response.getLong("maxDBLogId"));
			synDataDTO.setState(response.getString("state"));
			JSONArray rowData = null;

			try{
				rowData = response.getJSONArray("rowData");
			}catch (JSONException ex) {
				// TODO: handle exception
			}
			if (rowData != null && rowData.length() > 0){
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				for (int i = 0, size = rowData.length(); i < size; i++){
					synDataDTO.getRowData().add(mapper.readValue(rowData.get(i).toString(),SynDataTableDTO.class));
				}
			}
			// luu lastlog_id vao share preference
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();

			Editor prefsPrivateEditor = sharedPreferences.edit();
			long lastLogId = Long.parseLong((String) model.getActionEvent().userData);
			boolean isUpdateSuccess = true;
			// save data to DB.
			if (synDataDTO.getRowData() != null && synDataDTO.getRowData().size() > 0) {

				SynDataTableDAO dao = new SynDataTableDAO((List<SynDataTableDTO>)synDataDTO.getRowData());
				try {
					dao.synData(lastLogId, synDataDTO.getLastLogId_update());
				} catch (Exception ex) {
					isUpdateSuccess = false;
				}
			}
			if(isUpdateSuccess){
				//cap nhat lastLog id
				if(synDataDTO.getLastLogId_update() > 0)
					prefsPrivateEditor.putString(LoginView.LAST_LOG_ID,
							Long.toString(synDataDTO.getLastLogId_update()));
				prefsPrivateEditor.commit();
				// Goi xu ly len tan tren
				model.setModelData(synDataDTO);
				model.setIsSendLog(false);
				SynDataController.getInstance().handleModelEvent(model);
				GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
			}else{
				ServerLogger.sendLog("executeSynDataRows:"+ model.getParams(),
						"isUpdateSuccess = false: "+model.getDataText(), false, TabletActionLogDTO.LOG_CLIENT);
				model.setModelCode(ErrorConstants.ERROR_WAITING);
				SynDataController.getInstance().handleErrorModelEvent(model);
				GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
			}
		} else {
			model.setIsSendLog(false);
			if(errCode != ErrorConstants.ERROR_SESSION_RESET){
				ServerLogger.sendLog(model.getParams(), model.getDataText(), false, TabletActionLogDTO.LOG_SERVER);
			}
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SynDataController.getInstance().handleErrorModelEvent(model);
			GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
		}
	}



	/**
	 * Dong bo bang cach doc du lieu tu file
	 * File chua:
	 * 	- Thong tin lastLogId, trang thai
	 *  - N rows Data, moi row la M dong du lieu
	 * @author: BANGHN
	 * @param model
	 * @param file : File sau khi download tu server
	 * @throws Exception
	 */
	private void executeSynDataFile(ModelEvent model, File file, JSONObject response) throws Exception {
		//xem nhu co file roi la thanh cong, neu qua trinh syn loi thi tra ve loi
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_EXECUTING;
		// Get the text file
		if (file.exists()) {
			// try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;
			SynDataDTO synDataDTO = new SynDataDTO();
			synDataDTO.setLastLogId_update(response.getLong("lastLogId_update"));
			synDataDTO.setMaxDBLogId(response.getLong("maxDBLogId"));
			synDataDTO.setState(response.getString("state"));
			// luu lastlog_id vao share preference
			SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
			Editor prefsPrivateEditor = sharedPreferences.edit();

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			boolean isUpdateSuccess = true;
			long lastLogId = Long
					.parseLong((String) model.getActionEvent().userData);

			//uu  luong khoan maxid o mot dong
			long totalline = GlobalUtil.countLineOfFile(file);
			long tmpLogId = (long) ((float) (synDataDTO.getLastLogId_update() - lastLogId) / (float) totalline);
			long lineIndex = 0;
			while ((line = br.readLine()) != null) {
				lineIndex++;
				synDataDTO.getRowData().add(
						mapper.readValue(line, SynDataTableDTO.class));
				SynDataTableDAO dao = new SynDataTableDAO(
						(List<SynDataTableDTO>) synDataDTO.getRowData());
				try {
					dao.synData(lastLogId, synDataDTO.getLastLogId_update());
				} catch (Exception ex) {
					isUpdateSuccess = false;
					VTLog.logToFile("SYN", DateUtils.now()
							+ "- Insert SynData failure " + ex.toString());
				}
				Bundle b = new Bundle();
				b.putLong(IntentConstants.INTENT_LAST_LOG_ID, lastLogId
						+ (tmpLogId * lineIndex));
				b.putLong(IntentConstants.INTENT_MAX_LOG_ID, synDataDTO.getMaxDBLogId());
				((GlobalBaseActivity) GlobalInfo.getInstance()
						.getActivityContext()).sendBroadcast(
						ActionEventConstant.ACTION_SYN_PERCENT, b);
				synDataDTO.getRowData().clear();
			}
			br.close();
			if (isUpdateSuccess) {
				// cap nhat lastLog id
				if (synDataDTO.getLastLogId_update() > 0)
					prefsPrivateEditor.putString(LoginView.LAST_LOG_ID,
							Long.toString(synDataDTO.getLastLogId_update()));
				prefsPrivateEditor.commit();
				// Goi xu ly len tan tren
				model.setModelData(synDataDTO);
				model.setIsSendLog(false);
				SynDataController.getInstance().handleModelEvent(model);
				GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
			}else{
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				SynDataController.getInstance().handleErrorModelEvent(model);
				GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
			}
		} else {
			VTLog.logToFile("SynData", DateUtils.now()
					+ ": Exception sync res  : File not exists");
			model.setDataText("Exception sync res  : File not exists");
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SynDataController.getInstance().handleErrorModelEvent(model);
			GlobalInfo.getInstance().stateSynData = GlobalInfo.SYNDATA_NONE;
		}
	}



	/**
	 * Download .zip file specified by url, then unzip it to a folder in
	 * external storage.
	 *
	 * @param url
	 */
	private void downloadFileUpdateAndUnzip(String url, String fileName)
			throws Exception {
		File zipDir = ExternalStorage.getPathSynData();
		// File path to store .zip file before unzipping
		//GlobalUtil.pathSynDataFileName = StringUtil.getFileNameRromURLString(url);
		File zipFile = new File(ExternalStorage.getPathSynData(), fileName + ".zip");
		File outputDir = ExternalStorage.getPathSynData();

		try {
			DownloadFile.downloadWithURLConnection(url, zipFile, zipDir);
			DownloadFile.unzipFile(GlobalInfo.getInstance().getAppContext(), zipFile, outputDir);
		} catch (Exception e) {
			throw e;
		} finally {
			zipFile.delete();
		}
	}


	/**
	 * Request dong bo du lieu luc nhan cap nhat
	 * @param actionEvent
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HTTPRequest requestSynData(ActionEvent actionEvent) {
		HTTPRequest re = null;
		try {
			Vector<String> info = (Vector<String>) actionEvent.viewData;
			VTLog.i("SYN DATA", "Request syn data " + info.toString());
//			re = sendHttpRequest("syndata.getDataFromServer", info, actionEvent);
			re = sendHttpRequest("synDataController/getDataFromServer", info, actionEvent);

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return re;
	}


	/**
	 * Request lay link download file DB
	 *
	 * @author : BangHN since : 11:56:17 AM
	 */
	@SuppressWarnings("unchecked")
	public HTTPRequest requestGetLinkSqlFile(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		HTTPRequest re = null;
		try {
			Vector<String> info = (Vector<String>) actionEvent.viewData;
			re = sendHttpRequest("synDataController/createSQLiteFile", info, actionEvent,
					AbstractModelService.TIME_OUT_GET_FILE_SQL);

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return re;
	}

	class ErrorSynDataNull extends Exception{

	}

}
