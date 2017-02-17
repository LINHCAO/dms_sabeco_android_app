/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import android.os.Bundle;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.PGController;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.db.ProductCompetitorListDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.TimeKeeperDTO;
import com.viettel.dms.dto.view.ListAlbumUserDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListOrderViewDTO;
import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.dms.dto.view.PGTakeAttendanceViewDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.dto.view.SaleOrderDataResult;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.PG_SALE_ORDER_TABLE;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONException;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Model cua user
 * @author: BangHN
 * @version: 1.1
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class PGModel extends AbstractModelService {
	protected static PGModel instance;

	protected PGModel() {
	}

	public static PGModel getInstance() {
		if (instance == null) {
			instance = new PGModel();
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
//		case ActionEventConstant.ACTION_UPDATE_DELETED_DB: {
//			JSONObject json;
//			try {
//				json = new JSONObject((String) mes.getDataText());
//				JSONObject result = json.getJSONObject("result");
//
//				int errCode = result.getInt("errorCode");
//				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
//					model.setModelCode(errCode);
//					model.setModelMessage(result.getString("errorMessage"));
//					UserController.getInstance().handleModelEvent(model);
//				} else {
//					model.setModelCode(errCode);
//					model.setModelMessage(result.getString("errorMessage"));
//					UserController.getInstance().handleErrorModelEvent(model);
//				}
//			} catch (Throwable e) {
//				model.setModelCode(ErrorConstants.ERROR_COMMON);
//				UserController.getInstance().handleErrorModelEvent(model);
//			}
//			break;
//		}
		default:
			int errCodeDefault = ErrorConstants.ERROR_COMMON;
			try {
				JSONObject json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				errCodeDefault = result.getInt("errorCode");
				model.setModelCode(errCodeDefault);
				model.setModelData(actionEvent.userData);
			} catch (JSONException e) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lỗi : " + e.getMessage());
			} catch (Exception e) {
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
		model.setDataText(response.getDataText());
		model.setParams(((HTTPResponse) response).getRequest().getDataText());
		model.setActionEvent(actionEvent);

		if (GlobalUtil.checkActionSave(actionEvent.action) && actionEvent.logData != null) {
			// xu ly chung cho cac request
			LogDTO log = (LogDTO) actionEvent.logData;
			if (LogDTO.STATE_NONE.equals(log.state)) {
				updateLog(actionEvent, LogDTO.STATE_NEW);
			}
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			model.setModelMessage(response.getErrMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Lay ds PG de cham cong
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListPGForTakeAttendance(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		PGTakeAttendanceViewDTO result = new PGTakeAttendanceViewDTO();
		try {
			result = SQLUtils.getInstance().getListPGForTakeAttendance(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
//			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds PG thất bại");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		
	}
	/**
	 * get danh sach loai van de TTTT
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListTypeProblemTTTT(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Vector<ApParamDTO> vTypeProblem;
		try {
			vTypeProblem = SQLUtils.getInstance().getListTypeProblemTTTT();
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(vTypeProblem);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	

	/**
	* Lay thong tin doi thu canh tranh
	* @author: dungdq3
	* @param: 
	* @return: void
	*/
	public void getInformationCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		try {
			ProductCompetitorListDTO dto = SQLUtils.getInstance().getInformationCompetitorPG();

			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}
	
	/**
	* Luu thong tin cham cong
	* @author: Nguyen Thanh Dung
	* @param: 
	* @return: 
	*/
	
	public void savePGTakeAttendance(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		try {
			Bundle data = (Bundle) e.viewData;
			ArrayList<TimeKeeperDTO> listCSTDTO = SQLUtils.getInstance().savePGTakeAttendance(data);

			// boolean insertSuccess = true;
			if (listCSTDTO != null) {
				JSONArray sqlPara = new JSONArray();
				TimeKeeperDTO detail;
				for (int i = 0, size = listCSTDTO.size(); i < size; i++) {
					detail = listCSTDTO.get(i);
					// update sales order detail
					sqlPara.put(detail.generateInsertOrUpdateSql());
				}

				String logId = GlobalUtil.generateLogId();
				e.logData = logId;
				// send to server
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(sqlPara);
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(sqlPara.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TNPGController.getInstance().handleModelEvent(model);
			} else {
				ServerLogger.sendLog("Luu thong tin cham cong khong thanh cong", TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Luu thong tin cham cong khong thanh cong");
				TNPGController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("Luu thong tin cham cong khong thanh cong", TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);

			TNPGController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	* Luu danh sach kiem ton
	* @author: dungdq3
	* @param: ActionEvent e
	* @return: void
	*/
	public void saveRemainCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle b = (Bundle) e.viewData;

		long cusID=Long.parseLong(b.getString(IntentConstants.INTENT_CUSTOMER_ID));
		long staffID=b.getLong(IntentConstants.INTENT_STAFF_ID);
		ProductCompetitorListDTO listProductCompetitor=(ProductCompetitorListDTO) b.getSerializable(IntentConstants.INTENT_OP_STOCK_TOTAL_LIST);
		String staffCode=b.getString(IntentConstants.INTENT_STAFF_CODE);

		try {
			long insertSuccess = SQLUtils.getInstance().saveRemainCompetitor(staffID, cusID, listProductCompetitor,staffCode);

			if (insertSuccess>0) {
				JSONArray jarr = new JSONArray();
				for(ProductCompetitorDTO productCompetitor : listProductCompetitor.getArrProductCompetitor()){
					for(OpProductDTO opProduct : productCompetitor.getArrProduct()){
						JSONObject sqlPara=opProduct.generateListProductCompetitor(staffID, cusID, 0, staffCode);
						jarr.put(sqlPara);
					}
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelData(insertSuccess);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(insertSuccess);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}


	/**
	 * Lay ds hinh anh cua album
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */ 
	public void getAlbumDetailUser(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String type = String.valueOf(data.getInt(IntentConstants.INTENT_ALBUM_TYPE));
		String numTop = String.valueOf(data.getInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE));
		String page = String.valueOf(data.getInt(IntentConstants.INTENT_PAGE));
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		boolean isGetTotalImage = data.getBoolean(IntentConstants.INTENT_IS_GET_NUM_TOTAL_ITEM);
		PhotoThumbnailListDto result = null;
		try {
			result = SQLUtils.getInstance().getAlbumDetailUser(customerId, type, numTop, page,
					shopId, isGetTotalImage); 
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach album cua user
	 * 
	 * @author: QuangVT
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void getListAlbumUser(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		ListAlbumUserDTO result = null;
		try {
			result = new ListAlbumUserDTO(); 
			result = SQLUtils.getInstance().getAlbumUserInfo(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	* Kiem tra xem co can kiem hang ton ko!?
	* @author: dungdq3
	* @param: ActionEvent e
	* @return: void
	*/
	public void checkRemainedCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		long result = -1;
		try {
			result = SQLUtils.getInstance().checkRemainedCompetitor(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = -1;
		}
		if (result > 0) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	* Kiem tra xem co can kiem ban hang doi thu ko!?
	* @author: dungdq3
	* @param: ActionEvent e
	* @return: void
	*/
	public void checkSaledCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		long result = -1;
		try {
			result = SQLUtils.getInstance().checkSaledCompetitor(data);
		} catch (Exception ex) {
			result = -1;
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		if (result > 0) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay thong tin action log de cho biet da ghe tham KH hay chua?
	 * @author: dungnt19
	 * @since: 11:42:48 16-12-2013
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void checkTNPGHaveActionFromActionLog(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle viewInfo = (Bundle) e.viewData;

		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID);
		String customerId = viewInfo
				.getString(IntentConstants.INTENT_CUSTOMER_ID);

		try {
			ActionLogDTO dto = SQLUtils.getInstance().checkTNPGHaveActionFromActionLog(staffId, customerId);
			model.setModelData(dto);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			TNPGController.getInstance().handleModelEvent(model);
		} catch (Exception e2) {
		}
	}

	
	/**
	* Luu danh sach san pham ban cua doi thu
	* @author: dungdq3
	* @param: ActionEvent e
	* @return: void
	*/
	public void saveSaleCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle b = (Bundle) e.viewData;

		long cusID=Long.parseLong(b.getString(IntentConstants.INTENT_CUSTOMER_ID));
		long staffID=b.getLong(IntentConstants.INTENT_STAFF_ID);
		ProductCompetitorListDTO listProductCompetitor=(ProductCompetitorListDTO) b.getSerializable(IntentConstants.INTENT_OP_SALE_VOLUME_LIST);
		String staffCode=b.getString(IntentConstants.INTENT_STAFF_CODE);
		int type = b.getInt(IntentConstants.INTENT_TYPE); // 1 - BSG, 0- bia doi thu

		try {
			long success = SQLUtils.getInstance().saveSaleCompetitorPG(staffID, cusID, listProductCompetitor, type);

			if (success >0) {
				JSONArray jarr = new JSONArray();
				for(ProductCompetitorDTO productCompetitor : listProductCompetitor.getArrProductCompetitor()){
					for(OpProductDTO opProduct : productCompetitor.getArrProduct()){
						JSONObject sqlPara=opProduct.generateListProductCompetitorPG(staffID, cusID, 1, type, staffCode);
						jarr.put(sqlPara);
					}
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);
				
				model.setModelData(success);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(success);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach don hang, san pham cho PG
	 * @param event
     */
	public void requestGetOrderView(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		ListOrderViewDTO result = null;
		try {
			result = SQLUtils.getInstance().requestGetOrderView(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				PGController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetOrderView error: ", "sql get list product add to order view error", TabletActionLogDTO.LOG_CLIENT);
				PGController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetOrderView error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			PGController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Tao don hang pg
	 * @param actionEvent
	 * @return
     */
	public HTTPRequest requestCreateOrder(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);
		HTTPRequest re = null;
		try {
			ListOrderViewDTO order = (ListOrderViewDTO) actionEvent.viewData;
			order = SQLUtils.getInstance().createPGSaleOrder(order);
			if (order.insertSuccess) {
				JSONArray listSql = order.generateNewOrderSql(order);
				if(order.saleOrderDTO != null) {
					re = sendCreateOrderSqlToServer(actionEvent, listSql, order.saleOrderDTO.pgSaleOrderId);
				}else{
					re = sendCreateOrderSqlToServer(actionEvent, listSql, order.pgOrderUpdateDTO.pgSaleOrderId);
				}
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				PGController.getInstance().handleModelEvent(model);
			} else {
				if(order.saleOrderDTO != null) {
					ServerLogger.sendLog("Insert pg order id: " + order.saleOrderDTO.pgSaleOrderId, TabletActionLogDTO.LOG_CLIENT);
				}else{
					ServerLogger.sendLog("Update pg order id: " + order.pgOrderUpdateDTO.pgSaleOrderId, TabletActionLogDTO.LOG_CLIENT);
				}
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("PG Lưu đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				PGController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			PGController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * sendCreateOrderSqlToServer
	 * @param actionEvent
	 * @param listSql
	 * @param orderId
     * @return
     */
	private HTTPRequest sendCreateOrderSqlToServer(ActionEvent actionEvent, JSONArray listSql, long orderId) {
		HTTPRequest re;
		String logId = GlobalUtil.generateLogId();
		actionEvent.logData = logId;
		// send to server
		Vector<Object> para = new Vector<Object>();
		para.add(IntentConstants.INTENT_LIST_SQL);
		para.add(listSql);
		para.add(IntentConstants.INTENT_MD5);
		para.add(StringUtil.md5(listSql.toString()));
		para.add(IntentConstants.INTENT_LOG_ID);
		para.add(logId);
		para.add(IntentConstants.INTENT_IMEI_PARA);
		para.add(GlobalInfo.getInstance().getDeviceIMEI());
		re = sendHttpRequestOffline("queryController/executeSql", para, actionEvent, LogDTO.TYPE_ORDER,
				String.valueOf(orderId), PG_SALE_ORDER_TABLE.PG_SALE_ORDER_TABLE);
		return re;
	}
	/**
	 * Lay danh sach bao cao don hang, san pham cho PG
	 * @param event
	 */
	public void requestGetReportOrderView(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		ListOrderViewDTO result = null;
		try {
			result = SQLUtils.getInstance().requestGetReportOrderView(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				PGController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetReportOrderView error: ", "sql get list product add to order view error", TabletActionLogDTO.LOG_CLIENT);
				PGController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetReportOrderView error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			PGController.getInstance().handleErrorModelEvent(model);
		}
	}
}
