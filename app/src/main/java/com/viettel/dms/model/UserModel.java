/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import android.os.Bundle;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.AbstractTableDTO;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.WorkLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.NVBHReportForcusProductInfoViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;
import com.viettel.viettellib.network.http.NetworkUtil;

/**
 * Model cua user
 * @author: BangHN
 * @version: 1.1
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class UserModel extends AbstractModelService {
	protected static UserModel instance;

	protected UserModel() {
	}

	public static UserModel getInstance() {
		if (instance == null) {
			instance = new UserModel();
		}
		return instance;
	}

	public void onReceiveData(HTTPMessage mes) {
		VTLog.i("UserModel", "OnReceive: action:" + mes.getAction());
		ActionEvent actionEvent = (ActionEvent) mes.getUserData();
		ModelEvent model = new ModelEvent();
		String msgText = mes.getDataText();
		model.setDataText(msgText);
		model.setCode(mes.getCode());
		model.setParams(((HTTPResponse) mes).getRequest().getDataText());
		model.setActionEvent(actionEvent);
		// DMD check null or empty
		if (StringUtil.isNullOrEmpty((String) mes.getDataText())) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
			return;
		}
		// chung thuc thoi gian sai
		if (!StringUtil.isNullOrEmpty(msgText) && msgText.contains(Constants.EXPIRED_TIMESTAMP)) {
			model.setModelCode(ErrorConstants.ERROR_EXPIRED_TIMESTAMP);
			UserController.getInstance().handleErrorModelEvent(model);
			return;
		}

		switch (mes.getAction()) {
		case ActionEventConstant.RE_LOGIN:
		case ActionEventConstant.ACTION_LOGIN: {
			JSONObject json;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");

				int errCode = result.getInt("errorCode");
				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
					model.setModelCode(errCode);
					UserDTO user = new UserDTO();
					user.parseFromJSONLogin(result.getJSONObject("response"));
					model.setModelData(user);
					model.setModelMessage(result.getString("errorMessage"));
					UserController.getInstance().handleModelEvent(model);
				} else {
					model.setModelCode(errCode);
					model.setModelMessage(result.getString("errorMessage"));
					UserController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Throwable e) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			}
			break;
		}
		case ActionEventConstant.ACTION_UPDATE_DELETED_DB: {
			JSONObject json;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");

				int errCode = result.getInt("errorCode");
				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
					model.setModelCode(errCode);
					model.setModelMessage(result.getString("errorMessage"));
					UserController.getInstance().handleModelEvent(model);
				} else {
					model.setModelCode(errCode);
					model.setModelMessage(result.getString("errorMessage"));
					UserController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Throwable e) {
				ServerLogger.sendLog("Exception",
						e.getMessage() + "||" + mes.getDataText(), TabletActionLogDTO.LOG_EXCEPTION);
				VTLog.d("Exception", e.getMessage() + "||" + mes.getDataText());
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			}
			break;
		}
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER: {
			JSONObject json;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				int errCode = result.getInt("errorCode");
				model.setModelCode(errCode);
				model.setModelData(actionEvent.userData);
				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
					JSONObject jsonResponse = result.getJSONObject("response");
					String currentDateServr = jsonResponse.getString("serverDate");
					ArrayList<String> listOrderId = new ArrayList<String>();
					JSONArray jsonArray = jsonResponse.getJSONArray("haveReturnOrder");
					if (jsonArray != null) {
						for (int i = 0, size = jsonArray.length(); i < size; i++) {
							listOrderId.add(jsonArray.getString(i));
						}
					}
					boolean isSync = false;
					boolean isSuccess = false;
					if (actionEvent.viewData != null) {
						Bundle bundle = (Bundle) actionEvent.viewData;
						isSync = bundle.getBoolean(IntentConstants.INTENT_IS_SYNC, false);
					}
					try {
						// kiem tra thoi gian
						if (isSync) {
							// neu la dong bo offline thi kiem tra o
							// TransactionProcessManager
							Bundle bundle = new Bundle();
							bundle.putString(IntentConstants.INTENT_SERVER_DATE, currentDateServr);
							bundle.putStringArrayList(IntentConstants.INTENT_HAVE_RETURN_ORDER, listOrderId);
							model.setModelData(bundle);
							model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
							UserController.getInstance().handleModelEvent(model);
						} else {
							String currentDateClient = DateUtils.now();
							// kiem tra thoi gian hop le
							SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date dateServer = formatter.parse(currentDateServr);
							// kiem tra thoi gian khi online
							// neu khac ngay, hoac cung ngay ma chenh lech > 1h
							// --> fail
							Date dateClient = formatter.parse(currentDateClient);
							long secs = (dateServer.getTime() - dateClient.getTime()) / 1000;
							int hours = (int) secs / 3600;

							SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd");
							Date dateServer1 = formatter1.parse(currentDateServr);
							Date dateClient1 = formatter1.parse(currentDateClient);

							if (dateServer1.compareTo(dateClient1) == 0 && Math.abs(hours) <= 24) {
								// client - server <= 1: ok
								if (hours > -1) {
									// thanh cong
									isSuccess = true;
								}
							}
							if (isSuccess) {
								model.setModelData(ModelEvent.MODEL_RESULT_SUCCESS);
								model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
								UserController.getInstance().handleModelEvent(model);
							} else {
								model.setModelData(ModelEvent.MODEL_RESULT_FAIL_TIME_ONLINE);
								model.setModelCode(ErrorConstants.ERROR_COMMON);
								UserController.getInstance().handleErrorModelEvent(model);
							}
						}

					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
						model.setModelData(ModelEvent.MODEL_RESULT_COMMON_FAIL);
						model.setModelCode(ErrorConstants.ERROR_COMMON);
						UserController.getInstance().handleErrorModelEvent(model);
					}

				} else {
					if (errCode != ErrorConstants.ERROR_SESSION_RESET) {
						model.setModelCode(ErrorConstants.ERROR_COMMON);
					}
					model.setModelData(ModelEvent.MODEL_RESULT_COMMON_FAIL);
					UserController.getInstance().handleErrorModelEvent(model);
				}
			} catch (JSONException e) {
				model.setModelData(ModelEvent.MODEL_RESULT_COMMON_FAIL);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			} catch (Exception e) {
				model.setModelData(ModelEvent.MODEL_RESULT_COMMON_FAIL);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			}
			break;
		}
		case ActionEventConstant.CHANGE_PASS: {
			JSONObject json;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				if(result != null) {
					int errCode = result.getInt("errorCode");
					if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
						model.setModelCode(errCode);
						int response = result.getInt("response");
						model.setModelData(response);
						model.setModelMessage(result.getString("errorMessage"));
						UserController.getInstance().handleModelEvent(model);
					} else {
						model.setModelCode(errCode);
						model.setModelMessage(result.getString("errorMessage"));
						UserController.getInstance().handleErrorModelEvent(model);
					}
				}else{
					ServerLogger.sendLog("Token change pass",(String) mes.getDataText(), TabletActionLogDTO.LOG_SERVER);
					model.setModelCode(ErrorConstants.ERROR_SESSION_RESET);
					model.setModelMessage(Constants.MESSAGE_ERROR_SESSION_RESET);
					UserController.getInstance().handleErrorModelEvent(model);
				}
			} catch (JSONException e) {
				ServerLogger.sendLog("Exception",
						e.getMessage() + "||" + mes.getDataText(), TabletActionLogDTO.LOG_EXCEPTION);
				VTLog.d("Exception", e.getMessage() + "||" + mes.getDataText());
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			}
			break;
		}
		case ActionEventConstant.ACTION_RETRY_REQUEST:
		case ActionEventConstant.RETRY_UPLOAD_PHOTO:
		case ActionEventConstant.REQUEST_UPDATE_POSITION:
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			int errCode = ErrorConstants.ERROR_COMMON;
			try {
				JSONObject json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				errCode = result.getInt("errorCode");
				model.setModelCode(errCode);
			} catch (Exception e) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lỗi : " + e.getMessage());
			} finally {
				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					// TH mac dinh la request create/update/delete du lieu
					updateLog(actionEvent, LogDTO.STATE_SUCCESS);
					UserController.getInstance().handleModelEvent(model);
				} else if (errCode == ErrorConstants.ERROR_UNIQUE_CONTRAINTS) {
					// request loi trung khoa -- khong thuc hien goi lai len
					// server nua
					UserController.getInstance().handleErrorModelEvent(model);
					updateLog(actionEvent, LogDTO.STATE_UNIQUE_CONTRAINTS);
				} else {
					UserController.getInstance().handleErrorModelEvent(model);
					updateLog(actionEvent, LogDTO.STATE_FAIL);
				}
			}
			break;

		default:
			int errCodeDefault = ErrorConstants.ERROR_COMMON;
			try {
				JSONObject json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				errCodeDefault = result.getInt("errorCode");
				model.setModelCode(errCodeDefault);
				model.setModelData(actionEvent.userData);
//			} catch (JSONException e) {
//				model.setModelCode(ErrorConstants.ERROR_COMMON);
//				model.setModelMessage("Lỗi : " + e.getMessage());
			} catch (Exception e) {
				ServerLogger.sendLog("Exception",
						e.getMessage() + "||" + mes.getDataText(), TabletActionLogDTO.LOG_EXCEPTION);
				VTLog.d("Exception", e.getMessage() + "||" + mes.getDataText());
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lỗi : " + e.getMessage());
			} finally {
				// thanh cong, that bai khong gui tra kq ve view nua
				if (errCodeDefault == ErrorConstants.ERROR_CODE_SUCCESS) {
					// TH mac dinh la request create/update/delete du lieu
					updateLog(actionEvent, LogDTO.STATE_SUCCESS);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					// UserController.getInstance().handleModelEvent(model);
				} else if (errCodeDefault == ErrorConstants.ERROR_UNIQUE_CONTRAINTS) {
					// request loi trung khoa -- khong thuc hien goi lai len
					// server nua
					// UserController.getInstance().handleErrorModelEvent(model);
					updateLog(actionEvent, LogDTO.STATE_UNIQUE_CONTRAINTS);
				} else {
					// ghi log loi len server
					updateLog(actionEvent, LogDTO.STATE_FAIL);
					// UserController.getInstance().handleErrorModelEvent(model);
				}
			}
		}
	}

	public void onReceiveError(HTTPResponse response) {
		ActionEvent actionEvent = (ActionEvent) response.getUserData();
		ModelEvent model = new ModelEvent();
		String msgText = response.getDataText();
		model.setDataText(msgText);
		model.setParams(((HTTPResponse) response).getRequest().getDataText());
		model.setActionEvent(actionEvent);

		if (actionEvent.action == ActionEventConstant.ACTION_LOGIN) {
			GlobalInfo.getInstance().getProfile().getUserData().setLoginState(UserDTO.NOT_LOGIN);
			// chung thuc thoi gian sai
			if (!StringUtil.isNullOrEmpty(msgText) && msgText.contains(Constants.EXPIRED_TIMESTAMP)) {
				model.setModelCode(ErrorConstants.ERROR_EXPIRED_TIMESTAMP);
			} else {
				model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			}
			model.setModelMessage(response.getErrMessage());
			UserController.getInstance().handleErrorModelEvent(model);
		} else if (GlobalUtil.checkActionSave(actionEvent.action) && actionEvent.logData != null) {
			// xu ly chung cho cac request
			LogDTO log = (LogDTO) actionEvent.logData;
			if (LogDTO.STATE_NONE.equals(log.state)) {
				updateLog(actionEvent, LogDTO.STATE_NEW);
			}
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			UserController.getInstance().handleErrorModelEvent(model);
		} else if (actionEvent.action == ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER) {
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_SERVER_DATE, String.valueOf(ErrorConstants.ERROR_NO_CONNECTION));
			bundle.putBoolean(IntentConstants.INTENT_HAVE_RETURN_ORDER, false);
			model.setModelData(bundle);

			model.setModelData(bundle);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			UserController.getInstance().handleModelEvent(model);

		} else {
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			model.setModelMessage(response.getErrMessage());
			UserController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Request login http
	 * 
	 * @author: DoanDM
	 * @param actionEvent
	 * @return: HTTPRequest
	 * @throws:
	 */
	@SuppressWarnings("rawtypes")
	public HTTPRequest requestLoginHTTP(ActionEvent actionEvent) {
		HTTPRequest re = null;
		try {
			Vector info = (Vector) actionEvent.viewData;
			re = sendHttpRequest("authController/login", info, actionEvent, AbstractModelService.TIME_OUT_LOGIN,
					AbstractModelService.TIME_OUT_LOGIN, true);

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return re;
	}

	/**
	 * Retry lai request
	 * @author: TruongHN
	 * @param actionEvent
	 * @return: HTTPRequest
	 * @throws:
	 */
	public HTTPRequest requestRetry(ActionEvent actionEvent) {
		HTTPRequest re = null;
		try {
			// // data
			// ArrayList<String> data = (ArrayList<String>)actionEvent.viewData;
			// // get json
			// JSONObject json = new JSONObject(data.get(1));
			// // get method
			// String method = json.getString("method");
			// JSONObject para = json.getJSONObject("params");
			// para.put("logId", data.get(0));
			//
			//
			// JSONObject dataText = new JSONObject();
			// dataText.put("method", method);
			// dataText.put("params", para);
			// Log.i("REQUEST_RETRY", "logId = " +
			// ((LogDTO)actionEvent.logData).logId + " action = " +
			// actionEvent.action + " viewData = " +
			// actionEvent.viewData.toString() + " time = " + DateUtils.now() +
			// " milisecon = " + System.currentTimeMillis());
			// VTLog.logToFile("REQUEST_RETRY", "logId = " +
			// ((LogDTO)actionEvent.logData).logId + " action = " +
			// actionEvent.action + " viewData = " +
			// actionEvent.viewData.toString() + " time = " + DateUtils.now() +
			// " milisecon = " + System.currentTimeMillis());
			re = sendHttpRequest("queryController/executeSql", actionEvent.viewData.toString(), actionEvent);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return re;
	}

	/**
	 * Lay ngay hien tai tren server
	 * 
	 * @author: TruongHN
	 * @param e
	 * @return: HTTPRequest
	 * @throws:
	 */
	public HTTPRequest getCurrentDateTimeServer(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		try {
			// kiem tra network
			if (GlobalUtil.checkNetworkAccess()) {
				// neu co mang, lay thoi gian hien tai tren server
				Vector<Object> para = new Vector<Object>();
//				para.add(IntentConstants.INTENT_STAFF_ID);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_ROLE_TYPE);
				para.add(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType);
//				para.add(IntentConstants.INTENT_SHOP_ID);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
				re = sendHttpRequest("synDataController/getServerInfo", para, e);
			} else {
				// thuc hien offline
				Bundle bundle = new Bundle();
				bundle.putString(IntentConstants.INTENT_SERVER_DATE, String.valueOf(ErrorConstants.ERROR_NO_CONNECTION));
				bundle.putBoolean(IntentConstants.INTENT_HAVE_RETURN_ORDER, false);
				model.setModelData(bundle);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				UserController.getInstance().handleModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelData(ModelEvent.MODEL_RESULT_COMMON_FAIL);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * Request upload photo
	 * 
	 * @author: PhucNT
	 * @param e
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	public HTTPRequest requestUploadPhoto(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		try {

			MediaItemDTO mediaDTO = (MediaItemDTO) e.userData;

			long mediaId = SQLUtils.getInstance().insertMediaItem(mediaDTO);
			if (mediaId > 0) {

				// JSONArray sqlPara = mediaDTO.generateInsertMediaItem();

				String logId = GlobalUtil.generateLogId();
				e.logData = logId;
				// send to server
				// Vector<Object> para = new Vector<Object>();
				// para.add(IntentConstants.INTENT_LIST_SQL);
				// para.add(sqlPara);
				// para.add(IntentConstants.INTENT_LOG_ID);
				// para.add(logId);
				// para.add(IntentConstants.INTENT_STAFF_ID_PARA);
				// para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				// para.add(IntentConstants.INTENT_IMEI_PARA);
				// para.add(GlobalInfo.getInstance().getDeviceIMEI());

				Vector<String> data = (Vector<String>) e.viewData;
				data.add(IntentConstants.INTENT_IMAGE_ID);
				data.add(Long.toString(mediaId));
				data.add(IntentConstants.INTENT_LOG_ID);
				data.add(logId);
				data.add(IntentConstants.INTENT_IMEI_PARA);
				data.add(GlobalInfo.getInstance().getDeviceIMEI());

				// ObjectMapper mapper = new ObjectMapper();
				// String str = mapper.writeValueAsString(mediaDTO);
				//
				// data.add(IntentConstants.INTENT_MEDIA_ITEM_OBJECT);
				// data.add(str);

				// avatar
				String imgPath = data.get(data.lastIndexOf(IntentConstants.INTENT_TAKEN_PHOTO) + 1).toString();

				if (StringUtil.isNullOrEmpty(imgPath)) {
					// remove avatar
					// if
					// (bundle.containsKey(IntentConstants.INTENT_REMOVE_AVATAR))
					// {
					// userInfo.add(IntentConstants.INTENT_REMOVE_AVATAR);
					// userInfo.add(bundle
					// .getString(IntentConstants.INTENT_REMOVE_AVATAR));
					// }
					// re = sendHttpRequest("profile.updateUserProfile2",
					// userInfo, e);
				} else {
					re = httpMultiPartRequest("mediaController/addImage", data, imgPath, e, "fileName.jpg", NetworkUtil.PHOTO,
							NetworkUtil.JPG);

				}
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(mediaId);
				UserController.getInstance().handleModelEvent(model);
			} else {
				ServerLogger.sendLog("Insert mot hinh anh bị thất bại " + mediaDTO.id, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Insert hình thất bại");
				UserController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Throwable throwable) {
			ServerLogger.sendLog(throwable.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);

			ModelEvent modelEvent = new ModelEvent();
			modelEvent.setModelCode(ErrorConstants.ERROR_COMMON);
			modelEvent.setModelMessage("requestUpdateMyInfo - " + throwable.getMessage() + "/" + throwable.toString());
			modelEvent.setIsSendLog(false);
			UserController.getInstance().handleErrorModelEvent(modelEvent);
		}
		return re;
	}

	/**
	 * Retry upload photo
	 * 
	 * @author: PhucNT
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest requestRetryUploadPhoto(ActionEvent e) {
		// TODO Auto-generated method stub
		HTTPRequest re = null;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {

			String imgPath = "";
			LogDTO log = (LogDTO) e.logData;
			// tim duong dan image path.
			if (!StringUtil.isNullOrEmpty(log.tableId)) {
				imgPath = log.tableId;
			}
			re = httpMultiPartRequest("mediaController/addImage",
					e.viewData.toString(), imgPath, e, "fileName.jpg",
					NetworkUtil.PHOTO, NetworkUtil.JPG);
		} catch (Throwable ex) {
			if (ex instanceof FileNotFoundException) {
				updateLog(e, LogDTO.STATE_IMAGE_DELETED);
			}
			// model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("request insert hình thất bại");
			UserController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	@SuppressWarnings("unchecked")
	public HTTPRequest uploadDBFile(ActionEvent e) {
		HTTPRequest re = null;
		try {
			Vector<String> vt = (Vector<String>) e.viewData;
			// tim duong dan image path.
			String filePath = vt.elementAt(1);
			re = httpMultiPartRequest("mediaController/uploadLogFile", filePath, e,
					"VinamilkDatabase.zip", NetworkUtil.PHOTO, NetworkUtil.JPG);
		} catch (Exception ex) {
		}
		return re;

	}

	/**
	 * update new link url
	 * 
	 * @author: PhucNT
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest requestUdateNewLink(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			MediaItemDTO mediaDTO = (MediaItemDTO) e.viewData;

			int mediaId = SQLUtils.getInstance().updateNewLinkPhoto(mediaDTO);
			if (mediaId != -1) {

				model.setModelMessage("Update hình thành công");
				model.setModelData(Long.valueOf(mediaId));
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				UserController.getInstance().handleModelEvent(model);
			} else {
				ServerLogger.sendLog("Update link mot hinh anh bị thất bại " + mediaDTO.id,
						TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Insert hình thất bại");
				UserController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);

			UserController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * Request delete nhung bang ghi log cu truoc 40 ngay
	 * 
	 * @author banghn
	 * @param e
	 */
	public void requestDeleteOldLogTable(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			long result = SQLUtils.getInstance().deleteOldLogTable();
			model.setModelData(result);

			if (result > 0) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				UserController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Request kiem tra db co phai cua user dang dang nhap hay kg?
	 * 
	 * @author hieunh
	 * @param e
	 */
	public void requestCheckUserDB(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle data = (Bundle) e.viewData;
			String staffCode = data.getString(IntentConstants.INTENT_STAFF_CODE);
			int roleType = data.getInt(IntentConstants.INTENT_ROLE_TYPE);
			boolean result = SQLUtils.getInstance().checkUserDB(staffCode, roleType);
			model.setModelData(result);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			UserController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * request update sau khi deleted thanh cong
	 * 
	 * @author banghn
	 * @param actionEvent
	 * @return
	 */
	public HTTPRequest requestUpdateDeletedDB(ActionEvent actionEvent) {
		HTTPRequest re = null;
		try {
			@SuppressWarnings("unchecked")
			Vector<String> info = (Vector<String>) actionEvent.viewData;
			re = sendHttpRequest("synDataController/updateStaffSynDataStatus", info, actionEvent);

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		return re;
	}

	/**
	 * 
	 * get forcus product info
	 * 
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void requestGetForcusProductInfo(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);
		Bundle data = (Bundle) actionEvent.viewData;
		NVBHReportForcusProductInfoViewDTO result = null;
		try {
			result = SQLUtils.getInstance().getForcusProductInfoView(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				UserController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetForcusProductInfo error: ",
						"sql get forcus product info for NVBH", TabletActionLogDTO.LOG_CLIENT);
				UserController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetForcusProductInfo error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			UserController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * doi pass
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return
	 * @return: voidvoid
	 * @throws:
	 */
	public HTTPRequest requestChangePass(ActionEvent e) {
		HTTPRequest re = null;
		try {
			Vector<?> info = (Vector<?>) e.viewData;
			re = sendHttpRequest("authController/changePassword", info, e, AbstractModelService.TIME_OUT_LOGIN,
					AbstractModelService.TIME_OUT_LOGIN, false);

		} catch (Exception ex) {
		}
		return re;

	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void requestStaffType(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			// insert to sql Lite & request to server
			int staffTypeId = (Integer) e.viewData;
			String staffType = SQLUtils.getInstance().requestStaffType(staffTypeId);

			model.setModelData(staffType);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}
	
	/**
	 * Request send position log
	 * @author banghn
	 * @param e
	 * @return
	 */
	public HTTPRequest requestSendPositionLog(ActionEvent e) {
		HTTPRequest re = null;
		try {
			@SuppressWarnings("rawtypes")
			Vector info = (Vector) e.viewData;
			re = sendHttpRequest("queryController/executeSql", info, e, AbstractModelService.TIME_OUT_LOGIN,
					AbstractModelService.TIME_OUT_LOGIN, false);
			
		} catch (Exception ex) {
		}
		return re;
		
	}
	/**
	 * Them du lieu bang cham cong
	 * @param e
	 * @return
	 */
	public HTTPRequest inserWorkLogTo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		WorkLogDTO workLog = (WorkLogDTO) e.viewData;
		workLog.setType(AbstractTableDTO.TableType.WORK_LOG_TABLE);
		try {
//			long insetLocal = SQLUtils.getInstance().insertWorkLog(workLog);
//			if (insetLocal != -1) {// insert local thanh cong != -1
				JSONObject sqlPara = workLog.generateCreateSql();
				JSONArray jarr = new JSONArray();
				jarr.put(sqlPara);
				String logId = GlobalUtil.generateLogId();
				e.logData = logId;

				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(jarr.toString());
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(jarr.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				re = sendHttpRequestOffline("queryController/executeSql", para, e);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
//			} else {
//				ServerLogger.sendLog("inserWorkLogTo - "
//						+ workLog.workLogId, TabletActionLogDTO.LOG_CLIENT);
//				model.setIsSendLog(false);
//				model.setModelCode(ErrorConstants.ERROR_COMMON);
//				SaleController.getInstance().handleErrorModelEvent(model);
//			}
		} catch (Exception ex) {
			ServerLogger.sendLog("inserWorkLogTo - " + ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * Xoa du lieu cham cong ko thoa dieu kien
	 * @param e
	 * @return
     */
	public HTTPRequest deleteWorkLog(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		ArrayList<WorkLogDTO> log = (ArrayList<WorkLogDTO>) e.viewData;
		try {
			JSONArray jarr = new JSONArray();
			JSONObject sqlPara = new JSONObject();
			for(WorkLogDTO workLogDTO: log) {
				sqlPara = workLogDTO.generateDeletWorkLog();
				jarr.put(sqlPara);
			}
			String logId = GlobalUtil.generateLogId();
			e.logData = logId;
			Vector<Object> para = new Vector<Object>();
			para.add(IntentConstants.INTENT_LIST_SQL);
			para.add(jarr.toString());
			para.add(IntentConstants.INTENT_MD5);
			para.add(StringUtil.md5(jarr.toString()));
			para.add(IntentConstants.INTENT_LOG_ID);
			para.add(logId);
			para.add(IntentConstants.INTENT_IMEI_PARA);
			para.add(GlobalInfo.getInstance().getDeviceIMEI());
			re = sendHttpRequestOffline("queryController/executeSql", para, e);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			UserController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

}
