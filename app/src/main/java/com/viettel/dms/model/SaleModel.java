/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.Vector;

import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.AbstractTableDTO.TableType;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerAvatarDTO;
import com.viettel.dms.dto.db.CustomerCatLevelDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerListDoneProgrameDTO;
import com.viettel.dms.dto.db.CustomerPositionLogDTO;
import com.viettel.dms.dto.db.CustomerStockHistoryDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.db.DisplayProgrameLvDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProCusAttendDTO;
import com.viettel.dms.dto.db.ProCusHistoryDTO;
import com.viettel.dms.dto.db.ProCusMapDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDTO;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.db.ProductCompetitorListDTO;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.db.RoutingCustomerDTO;
import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.WorkLogDTO;
import com.viettel.dms.dto.me.NotifyOrderDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.AutoCompleteFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.CategoryCodeDTO;
import com.viettel.dms.dto.view.ComboboxDisplayProgrameDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.AreaItem;
import com.viettel.dms.dto.view.CusDebitDetailDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.dto.view.CustomerInfoDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.CustomerProgrameDTO;
import com.viettel.dms.dto.view.DisplayPresentProductInfo;
import com.viettel.dms.dto.view.DisplayProgrameItemModel;
import com.viettel.dms.dto.view.DisplayProgrameModel;
import com.viettel.dms.dto.view.DisplayProgrameViewDTO;
import com.viettel.dms.dto.view.GSBHGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsTNPGViewDTO;
import com.viettel.dms.dto.view.GeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.ImageListDTO;
import com.viettel.dms.dto.view.IntroduceProductDTO;
import com.viettel.dms.dto.view.ListAlbumUserDTO;
import com.viettel.dms.dto.view.ListCustomerAttentProgrameDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListNoteInfoViewDTO;
import com.viettel.dms.dto.view.ListOrderMngDTO;
import com.viettel.dms.dto.view.ListProductDTO;
import com.viettel.dms.dto.view.ListProductQuantityJoin;
import com.viettel.dms.dto.view.ListRemainProductDTO;
import com.viettel.dms.dto.view.ListSaleOrderDTO;
import com.viettel.dms.dto.view.NewCustomerListDTO;
import com.viettel.dms.dto.view.NoSuccessSaleOrderDto;
import com.viettel.dms.dto.view.NoteInfoDTO;
import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.dto.view.ProgrameForProductDTO;
import com.viettel.dms.dto.view.RemainProductViewDTO;
import com.viettel.dms.dto.view.SaleInMonthDTO;
import com.viettel.dms.dto.view.SaleOrderCustomerDTO;
import com.viettel.dms.dto.view.SaleOrderDataResult;
import com.viettel.dms.dto.view.SaleOrderViewDTO;
import com.viettel.dms.dto.view.SaleProductInfoDTO;
import com.viettel.dms.dto.view.SaleStatisticsAccumulateDayDTO;
import com.viettel.dms.dto.view.SaleStatisticsProductInDayInfoViewDTO;
import com.viettel.dms.dto.view.SaleSupportProgramDetailModel;
import com.viettel.dms.dto.view.SaleSupportProgramModel;
import com.viettel.dms.dto.view.VoteDisplayPresentProductViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.CUSTOMER_CATEGORY_LEVEL_TABLE;
import com.viettel.dms.sqllite.db.CalPromotions;
import com.viettel.dms.sqllite.db.ROUTING_TABLE;
import com.viettel.dms.sqllite.db.SALES_ORDER_DETAIL_TABLE;
import com.viettel.dms.sqllite.db.SALE_ORDER_TABLE;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.sale.customer.CustomerFeedBackDto;
import com.viettel.map.dto.GeomDTO;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;

/**
 * Tang xu ly model cua NVBH
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class SaleModel extends AbstractModelService {
	protected static SaleModel instance;

	protected SaleModel() {
	}

	public static SaleModel getInstance() {
		if (instance == null) {
			instance = new SaleModel();
		}
		return instance;
	}

	public void onReceiveData(HTTPMessage mes) {
		ActionEvent actionEvent = (ActionEvent) mes.getUserData();
		ModelEvent model = new ModelEvent();
		model.setDataText(mes.getDataText());
		model.setCode(mes.getCode());
		model.setParams(((HTTPResponse) mes).getRequest().getDataText());
		model.setActionEvent(actionEvent);
		// DMD check null or empty
		if (StringUtil.isNullOrEmpty((String) mes.getDataText())) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
			return;
		}
		switch (mes.getAction()) {
		case ActionEventConstant.UPDATE_CUSTOMER_LOATION:
			JSONObject json;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				int errCode = result.getInt("errorCode");
				model.setModelCode(errCode);
				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelMessage(result.getString("errorMessage"));
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Throwable e) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
			break;
		default:
			int errCodeDefault = ErrorConstants.ERROR_COMMON;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				errCodeDefault = result.getInt("errorCode");
				model.setModelCode(errCodeDefault);
				model.setModelData(actionEvent.userData);
			} catch (Exception e) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lỗi : " + e.getMessage());
			} finally {
				// thanh cong, that bai khong gui tra kq ve view nua
				if (errCodeDefault == ErrorConstants.ERROR_CODE_SUCCESS) {
					// TH mac dinh la request create/update/delete du lieu
					updateLog(actionEvent, LogDTO.STATE_SUCCESS);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					// SaleController.getInstance().handleModelEvent(model);
				} else if (errCodeDefault == ErrorConstants.ERROR_UNIQUE_CONTRAINTS) {
					// request loi trung khoa -- khong thuc hien goi lai len
					// server nua
					// SaleController.getInstance().handleErrorModelEvent(model);
					updateLog(actionEvent, LogDTO.STATE_UNIQUE_CONTRAINTS);
				} else {
					// ghi log loi len server
					updateLog(actionEvent, LogDTO.STATE_FAIL);
					// SaleController.getInstance().handleErrorModelEvent(model);
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

		if (GlobalUtil.checkActionSave(actionEvent.action)
				&& actionEvent.logData != null) {
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

	public void getReviewShowroom(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		SaleController.getInstance().handleModelEvent(model);
	}

	public void getReviewShowroom_GoodList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		SaleController.getInstance().handleModelEvent(model);
	}

	public void getPaidPromotionList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		SaleController.getInstance().handleModelEvent(model);
	}

	public HTTPRequest payDebt(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			CusDebitDetailDTO cusDebitDetailDto = (CusDebitDetailDTO) e.viewData;
			boolean result = SQLUtils.getInstance().createPayReceived(cusDebitDetailDto);
			if (result) {
				JSONArray listSql = cusDebitDetailDto.generatePayDebtSql();
				re = sendPayDebtSqlToServer(e, listSql);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("DEBIT ID: " + cusDebitDetailDto.debitId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Gach no ko thanh cong");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * 
	 * Lay danh sach san pham MVBH, GSBH
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getProductList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		ListProductDTO result = null;
		try {
			result = SQLUtils.getInstance().getProductList(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds sản phẩm thất bại");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	public void getListCategoryCodeProduct(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		CategoryCodeDTO result = null;
		try {
			result = SQLUtils.getInstance().getListCategoryCodeProduct();
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
	 * Lay danh sach don hang gan day cua khach hang
	 * 
	 * @author : BangHN since : 11:29:09 AM
	 */
	@SuppressWarnings({ "unchecked" })
	public void getLastSaleOrders(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		ArrayList<SaleOrderCustomerDTO> lastOrderCustomer = null;
		Vector<Object> data = (Vector<Object>) e.viewData;
		String customerId = null;
		String page = null, numTop = null;
		if (data.lastIndexOf(IntentConstants.INTENT_CUSTOMER_ID) >= 0) {
			customerId = data.get(data.lastIndexOf(IntentConstants.INTENT_CUSTOMER_ID) + 1).toString();
		}
		if (data.lastIndexOf(IntentConstants.INTENT_PAGE) >= 0) {
			page = data.get(data.lastIndexOf(IntentConstants.INTENT_PAGE) + 1).toString();
		}
		if (data.lastIndexOf(IntentConstants.INTENT_NUMTOP) >= 0) {
			numTop = data.get(data.lastIndexOf(IntentConstants.INTENT_NUMTOP) + 1).toString();
		}
		String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		try {
			lastOrderCustomer = SQLUtils.getInstance().getLastSaleOrders(customerId, shopId, Integer.parseInt(page), Integer.parseInt(numTop));
			lastOrderCustomer = SQLUtils.getInstance().getLastSaleOrders(customerId, shopId, Integer.parseInt(page), Integer.parseInt(numTop));

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelData(lastOrderCustomer);
			SaleController.getInstance().handleModelEvent(model);
			return;
		}
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		model.setModelData(lastOrderCustomer);
		SaleController.getInstance().handleModelEvent(model);
	}

	/**
	 * Lay thong tin doanh so khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	@SuppressWarnings("unchecked")
	public void getSaleLevelCustomer(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Vector<Object> data = (Vector<Object>) e.viewData;
		String customerId = null;
		if (data.lastIndexOf(IntentConstants.INTENT_CUSTOMER_ID) >= 0) {
			customerId = data.get(data.lastIndexOf(IntentConstants.INTENT_CUSTOMER_ID) + 1).toString();
		}
		ArrayList<CustomerCatLevelDTO> lastOrderCustomer = new ArrayList<CustomerCatLevelDTO>();
		try {
			CUSTOMER_CATEGORY_LEVEL_TABLE cusCat = new CUSTOMER_CATEGORY_LEVEL_TABLE(SQLUtils.getInstance().getmDB());

			lastOrderCustomer = cusCat.getListCustomerCatLevel(customerId);

		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelData(lastOrderCustomer);
			SaleController.getInstance().handleModelEvent(model);
			return;
		}
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		model.setModelData(lastOrderCustomer);
		SaleController.getInstance().handleModelEvent(model);
	}

	/**
	 * Lay ds kiem ton
	 * 
	 * @author: HieuNH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getRemainProduct(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		ListRemainProductDTO result = null;
		try {
			result = SQLUtils.getInstance().getRemainProduct(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getRemainProduct error: ", "sql get remain product error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getRemainProduct error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get promotion programe detail
	 * 
	 * @author: HaiTC3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getPromotionProgrameDetail(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		PromotionProgrameDTO result = null;
		try {
			result = SQLUtils.getInstance().getPromotionProgrameDetail(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getPromotionProgrameDetail error: ", "sql get promotion programe info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getPromotionProgrameDetail error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * get list display program product with customerId
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void requestGetListDisplayProgramProduct(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		List<DisplayPresentProductInfo> result = null;
		try {
			result = SQLUtils.getInstance().getListDisplayProgramProduct(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetListDisplayProgramProduct error: ", "sql get list display programe product error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetListDisplayProgramProduct error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach chuong trinh trung bay cua khach hang
	 * 
	 * @author: quangvt1
	 * @since: 13:50:49 09-05-2014
	 * @return: void
	 * @throws:
	 * @param event
	 */
	public void requestGetVoteDisplayProgrameView(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		String idCustomer = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String idStaff = data.getString(IntentConstants.INTENT_STAFF_ID);

		try {
			VoteDisplayPresentProductViewDTO result = null;
			result = SQLUtils.getInstance().getVoteDisplayProgrameProductViewData(idStaff, idCustomer);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetVoteDisplayProgrameView error: ", "sql get list display programe product error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetVoteDisplayProgrameView error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach san pham cham trung bay cua 1 chuong trinh
	 * 
	 * @author: quangvt1
	 * @since: 16:06:09 09-05-2014
	 * @return: void
	 * @throws:
	 * @param event
	 */
	public void requestGetListVoteDisplayProduct(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		String programId = data.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_ID);
		DisplayPresentProductInfo result = null;
		try {
			result = SQLUtils.getInstance().getListVoteDisplayProduct(programId);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				ServerLogger.sendLog("method requestGetListVoteDisplayProduct error: ", "sql get list product of display programe to vote error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			ServerLogger.sendLog("method requestGetListVoteDisplayProduct error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	 /**
	  * Lu cham trung bay 
	  *
	  * @author: hoanpd1
	  * @since: 14:19:52 09-09-2014
	  * @return: HTTPRequest
	  * @throws:  
	  * @param actionEvent
	  * @return
	  */
	public HTTPRequest requestUpdateVoteProductDisplay(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		HTTPRequest re = null;
		try {
			Bundle data = (Bundle) actionEvent.viewData;
			ProDisplayProgrameDTO programe = (ProDisplayProgrameDTO) data.getSerializable(IntentConstants.INTENT_DISPLAY_PROGRAM_MODEL);

			boolean isSuccess = SQLUtils.getInstance().voteCustomerDisplayProgrameScoreDTO(programe);

			if (isSuccess) {
				JSONArray listSql = programe.generateNewVoteDisplayJson();
				String logId = GlobalUtil.generateLogId();
				actionEvent.userData = programe;
				actionEvent.logData = logId;

				// send to server
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(listSql);
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(listSql.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				re = sendHttpRequestOffline("queryController/executeSql", para, actionEvent);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(actionEvent.userData);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestUpdateVoteProductDisplay error: ", "sql insert vote display programe product to db local error or update maxId error", TabletActionLogDTO.LOG_CLIENT);
				model.setModelMessage("Lưu chấm trưng bày không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestUpdateVoteProductDisplay error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * 
	 * khoi tao cac textview de thuc hien autoComplete
	 * 
	 * @author: HieuNH6
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void requestGetInitListProductAddToOrderView(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		AutoCompleteFindProductSaleOrderDetailViewDTO result = null;
		try {
			result = SQLUtils.getInstance().getInitListProductAddToOrderView(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetListProductAddToOrderView error: ", "sql get list product add to order view error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetListProductAddToOrderView error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * request get list product
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void requestGetListProductAddToOrderView(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		ListFindProductSaleOrderDetailViewDTO result = null;
		try {
			result = SQLUtils.getInstance().getListProductAddToOrderView(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetListProductAddToOrderView error: ", "sql get list product add to order view error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetListProductAddToOrderView error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	// getListCustomerAttentPrograme
	/**
	 * 
	 * lay danh sach khach hang tham gia chuong trinh trung bay
	 * 
	 * @author: ThanhNN8
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListCustomerAttentPrograme(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		try {
			// insert to sql Lite & request to server
			Bundle data = (Bundle) e.viewData;
			String extPage = data.getString(IntentConstants.INTENT_PAGE);
			String displayProgrameCode = data.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
			String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			long staffId = data.getLong(IntentConstants.INTENT_STAFF_ID);
			long displayProgrameId = data.getLong(IntentConstants.INTENT_DISPLAY_PROGRAM_ID);
			boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);
			ListCustomerAttentProgrameDTO results = SQLUtils.getInstance().getListCustomerAttentPrograme(extPage, displayProgrameCode, displayProgrameId, customerCode, customerName, staffId, checkPagging);

			model.setModelData(results);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);

			SaleController.getInstance().handleModelEvent(model);

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get list programe for product
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getListProgrameForProduct(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		String customer_id = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String customerTypeId = data.getString(IntentConstants.INTENT_CUSTOMER_TYPE_ID);
		String shop_id = data.getString(IntentConstants.INTENT_SHOP_ID);
		String staff_id = data.getString(IntentConstants.INTENT_STAFF_ID);
		List<ProgrameForProductDTO> listPrograme = new ArrayList<ProgrameForProductDTO>();
		try {
			listPrograme = SQLUtils.getInstance().getListProgrameForProduct(shop_id, staff_id, customer_id, customerTypeId);
			if (listPrograme != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(listPrograme);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListProgrameForProduct error: ", "sql get list programe for product error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListProgrameForProduct error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Lay thong tin gioi thieu san pham
	 * 
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	public void getIntroduceProduct(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		String productId = data.getString(IntentConstants.INTENT_PRODUCT_ID);
		IntroduceProductDTO result = null;
		try {
			result = SQLUtils.getInstance().getIntroduceProduct(productId);
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
	 * 
	 * Lay danh sach loai CT va danh sach nganh hang
	 * 
	 * @author: ThanhNN8
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getListComboboxDisplayPrograme(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		ComboboxDisplayProgrameDTO result = new ComboboxDisplayProgrameDTO();
		try {
			result.listDepartPrograme = SQLUtils.getInstance().getListDepartDisplayPrograme();
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
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	public void updateClientThumbnailUrl(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		MediaItemDTO dto = (MediaItemDTO) data.get(IntentConstants.INTENT_DATA);
		int result = 0;
		try {
			result = SQLUtils.getInstance().updateClientThumbnailUrl(dto);
		} catch (Exception ex) {
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
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
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
			result = SQLUtils.getInstance().getAlbumDetailUser(customerId, type, numTop, page, shopId, isGetTotalImage);
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
	 * Lay ds don hang chua send thanh cong
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getNoSuccessOrderList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		ArrayList<String> listOrderId = data.getStringArrayList(IntentConstants.INTENT_ARRAY_STRING_LIST);

		NoSuccessSaleOrderDto dto = null;
		try {
			dto = SQLUtils.getInstance().getNoSuccessOrderList(listOrderId);

			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception x) {
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay tat ca don hang chua chuyen thanh cong: bao gom chua goi len server,
	 * don hang loi bi tra ve....
	 * 
	 * @author: TruongHN
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestGetAllOrderFail(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		NoSuccessSaleOrderDto dto = null;
		try {
			dto = SQLUtils.getInstance().getAllOrderFail();
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * request get list industry product and list product for the first industry
	 * in list
	 * 
	 * @param e
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public void requestGetListIndustryProductAndListProduct(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		SaleStatisticsProductInDayInfoViewDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListIndustryProductAndListProduct((Bundle) e.viewData);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get list product sold
	 * 
	 * @param e
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public void requestListProductPreSaleSold(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		ArrayList<SaleProductInfoDTO> dto = null;
		try {
			dto = SQLUtils.getInstance().getListProductPreSaleSold((Bundle) e.viewData);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get list product van sale sold
	 * 
	 * @param e
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 17, 2013
	 */
	public void requestListProductVanSaleSold(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		ArrayList<SaleProductInfoDTO> dto = null;
		try {
			dto = SQLUtils.getInstance().getListProductVanSaleSold((Bundle) e.viewData);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * lay danh sach san pham trong man hinh don tong luy ke ngay
	 * 
	 * @author: HieuNH6
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestSaleStatisticsAccumulateDay(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID);
		String productCode = viewInfo.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = viewInfo.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String industry = viewInfo.getString(IntentConstants.INTENT_INDUSTRY);
		String page = viewInfo.getString(IntentConstants.INTENT_PAGE);
		ArrayList<String> dto = null;
		try {
			dto = SQLUtils.getInstance().getSaleStatisticsAccumulateDay(shopId, staffId, productCode, productName, industry, page);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * lay danh sach san pham trong man hinh don tong luy ke ngay
	 * 
	 * @author: HieuNH6
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestSaleStatisticsAccumulateDayListProduct(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID);
		String productCode = viewInfo.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = viewInfo.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String industry = viewInfo.getString(IntentConstants.INTENT_INDUSTRY);
		String page = viewInfo.getString(IntentConstants.INTENT_PAGE);
		SaleStatisticsAccumulateDayDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getSaleStatisticsAccumulateDayListProduct(shopId, staffId, productCode, productName, industry, page);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * lay count danh sach san pham trong man hinh don tong luy ke ngay
	 * 
	 * @author: HieuNH6
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestCountSaleStatisticsAccumulateDayListProduct(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID);
		String productCode = viewInfo.getString(IntentConstants.INTENT_PRODUCT_CODE);
		String productName = viewInfo.getString(IntentConstants.INTENT_PRODUCT_NAME);
		String industry = viewInfo.getString(IntentConstants.INTENT_INDUSTRY);
		int count = 0;
		try {
			count = SQLUtils.getInstance().getCountSaleStatisticsAccumulateDayListProduct(shopId, staffId, productCode, productName, industry);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(count);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Request tao don hang
	 * 
	 * @author: TruongHN
	 * @param actionEvent
	 * @return: HTTPRequest
	 * @throws:
	 */
	public HTTPRequest requestCreateOrder(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			OrderViewDTO order = (OrderViewDTO) actionEvent.viewData;
			SaleOrderDataResult result = SQLUtils.getInstance().createOrder(order);
			if (result.isCreateSqlLiteSuccess) {
				// if (order.orderInfo.isSend == 1) {
				JSONArray listSql = order.generateNewOrderSql();
				re = sendCreateOrderSqlToServer(actionEvent, listSql, order.orderInfo.saleOrderId);
				// }
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				// model.setModelMessage("Lưu đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("order id: " + order.orderInfo.saleOrderId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * Generate sql tao don hang gui server
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param actionEvent
	 * @return
	 * @return: HTTPRequest
	 * @throws:
	 */
	private HTTPRequest sendCreateOrderSqlToServer(ActionEvent actionEvent, JSONArray listSql, long orderId) {
		HTTPRequest re;

		String logId = GlobalUtil.generateLogId();
		actionEvent.logData = logId;
		// send to server
		Vector<Object> para = new Vector<Object>();
		// para.add(IntentConstants.INTENT_LIST_DECLARE);
		// para.add(sqlPara.get(0));
		para.add(IntentConstants.INTENT_LIST_SQL);
		para.add(listSql);
		para.add(IntentConstants.INTENT_MD5);
		para.add(StringUtil.md5(listSql.toString()));
		para.add(IntentConstants.INTENT_LOG_ID);
		para.add(logId);
//		para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//		para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
		para.add(IntentConstants.INTENT_IMEI_PARA);
		para.add(GlobalInfo.getInstance().getDeviceIMEI());

		re = sendHttpRequestOffline("queryController/executeSql", para, actionEvent, LogDTO.TYPE_ORDER, String.valueOf(orderId), SALE_ORDER_TABLE.TABLE_NAME);
		return re;
	}

	/**
	 * chuyen don hang
	 * 
	 * @author: PhucNT
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest requestTranferListOrder2(ActionEvent actionEvent) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		HTTPRequest re = null;
		try {
			SaleOrderViewDTO order = (SaleOrderViewDTO) actionEvent.viewData;
			// Update is_send field
			// order.saleOrder.isSend = 1;
			boolean updateSuccess = SQLUtils.getInstance().updateSentOrder(order.saleOrder);

			long orderId = order.saleOrder.saleOrderId;

			if (updateSuccess) {
				if (order.saleOrder.synState == 2) {// order has exist on server
					JSONArray listSql = new JSONArray();
					listSql.put(order.saleOrder.generateUpdateSentOrderSql());
					re = sendCreateOrderSqlToServer(actionEvent, listSql, orderId);
				} else {
					OrderViewDTO result = new OrderViewDTO();
					result.listBuyOrders = new ArrayList<OrderDetailViewDTO>();
					result.listPromotionOrders = new ArrayList<OrderDetailViewDTO>();

					ListSaleOrderDTO listChosenProduct = SQLUtils.getInstance().getSaleOrderForSend(orderId);
					result.orderInfo = listChosenProduct.saleOrderDTO;

					for (OrderDetailViewDTO detail : listChosenProduct.listData) {
						if (detail.orderDetailDTO.isFreeItem == 0) {
							result.listBuyOrders.add(detail);
						} else {
							result.listPromotionOrders.add(detail);
						}
					}

					// result.listBuyOrders = listChosenProduct.listData;
					//
					// // Get ds promotion from sqlite
					// ListSaleOrderDTO listPromotionProduct =
					// SQLUtils.getInstance().getPromotionProductsForSend(orderId);
					// result.listPromotionOrders =
					// listPromotionProduct.listData;

					result.orderInfo.updateDate = order.saleOrder.updateDate;// DateUtils.now();
					result.orderInfo.updateUser = order.saleOrder.updateUser;// GlobalInfo.getInstance().getProfile().getUserData().userCode;
					// result.orderInfo.importCode =
					// order.saleOrder.importCode;// ""

					JSONArray listSql = result.generateNewOrderSql();
					re = sendCreateOrderSqlToServer(actionEvent, listSql, orderId);
				}

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelMessage("Lưu đơn hàng thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("Chuyển đơn hàng "
						+ order.getSaleOrderId() + " không thành công ", TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);

			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {

		}

		return re;
	}

	/**
	 * TODO Sửa order trên local và gởi lên server.
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param :
	 * @return : HTTPRequest
	 * @throws :
	 * @time : 2:20:42 PM
	 */
	public HTTPRequest requestEditAndSendOrder(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			OrderViewDTO order = (OrderViewDTO) actionEvent.viewData;
			List<SaleOrderDetailDTO> listSaleOrderDetail = new ArrayList<SaleOrderDetailDTO>();
			SALES_ORDER_DETAIL_TABLE orderDetailTable = new SALES_ORDER_DETAIL_TABLE(SQLUtils.getInstance().getmDB());
			listSaleOrderDetail = orderDetailTable.getAllDetailOfSaleOrder(order.orderInfo.saleOrderId);

			// Save syn state
			int synState = order.orderInfo.synState;
			order.orderInfo.synState = 0; // reset -> 0 to show status
			boolean isSuccess = SQLUtils.getInstance().updateOrder(order, listSaleOrderDetail);

			order.orderInfo.synState = synState;

			if (isSuccess) {
				if (order.orderInfo.synState == 2) {// order has exist on server
					JSONArray listSql = SQLUtils.getInstance().generateSqlUpdateOrder(order, listSaleOrderDetail);
					re = sendCreateOrderSqlToServer(actionEvent, listSql, order.orderInfo.saleOrderId);
				} else {
					// if (order.orderInfo.isSend == 1) {
					JSONArray listSql = order.generateNewOrderSql();
					re = sendCreateOrderSqlToServer(actionEvent, listSql, order.orderInfo.saleOrderId);
					// }
				}

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				ServerLogger.sendLog("order id: " + order.orderInfo.saleOrderId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Sửa đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * Lay ds CT HTBH
	 * 
	 * @author: dungnt19
	 * @since: 19:57:05 07-05-2014
	 * @return: void
	 * @throws:
	 * @param event
	 */
	public void requestGetListPromotionPrograme(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle bundle = (Bundle) event.viewData;
		SaleSupportProgramModel result = null;
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		String ext = bundle.getString(IntentConstants.INTENT_PAGE);
		boolean checkLoadMore = bundle.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);

		try {
			result = SQLUtils.getInstance().getListSaleSupportProgram(shopId, ext, checkLoadMore);
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
	 * Lay ds chuong trinh khuyen mai
	 * 
	 * @author: SoaN
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void requestGetListDisplayPrograme(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle ext = (Bundle) event.viewData;
		boolean checkRequestCombobox = ext.getBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, false);
		DisplayProgrameModel result = null;
		try {
			result = SQLUtils.getInstance().getListDisplayPrograme(ext);
			if (checkRequestCombobox) {
				ComboboxDisplayProgrameDTO comboboxModel = new ComboboxDisplayProgrameDTO();
				try {
					comboboxModel.listDepartPrograme = SQLUtils.getInstance().getListDepartDisplayPrograme();
				} catch (Exception ex) {
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
					comboboxModel = null;
				}
				result.setComboboxDTO(comboboxModel);
			} else {
				result.setComboboxDTO(null);
			}
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
	 * Lay ds san pham chuong trinh trung bay
	 * 
	 * @author: SoaN
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void requestGetDisplayProgrameItem(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle ext = (Bundle) event.viewData;
		DisplayProgrameItemModel result = null;
		try {
			result = SQLUtils.getInstance().getListDisplayProgrameItem(ext);
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
	 * Lay thong tin chuong trinh trung bay
	 * 
	 * @author: SoaN
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void requestGetDisplayProgrameInfo(ActionEvent event) {

		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle ext = (Bundle) event.viewData;

		DisplayProgrameDTO programeDTO = null;
		List<DisplayProgrameLvDTO> lvDTO = null;
		DisplayProgrameViewDTO viewDTO = null;

		try {
			programeDTO = SQLUtils.getInstance().getDisplayProgrameInfo(ext);
			lvDTO = SQLUtils.getInstance().getDisplayLvByProgrameCode(ext);
			//
			viewDTO = new DisplayProgrameViewDTO();
			viewDTO.setCode(programeDTO.displayProgrameCode);
			viewDTO.setName(programeDTO.displayProgrameName);
			viewDTO.setStartDate(programeDTO.fromDate);
			viewDTO.setEndDate(programeDTO.toDate);
			viewDTO.setDisplayProgLevel(lvDTO);
			//
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
		}
		if (viewDTO != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(viewDTO);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Lay danh sach khach hang
	 * 
	 * @author: dungdq3
	 *            actionEvent
	 * @throws Exception
	 * @return: void
	 */
	public void getListCustomer(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		Bundle bundle = (Bundle) actionEvent.viewData;
		try {
			long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
			long shopId = bundle.getLong(IntentConstants.INTENT_SHOP_ID);
			String code = bundle.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			String name = bundle.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			int page = bundle.getInt(IntentConstants.INTENT_PAGE);
			String visit_plan = bundle.getString(IntentConstants.INTENT_VISIT_PLAN);
			boolean isGetWrongPlan = bundle.getBoolean(IntentConstants.INTENT_GET_WRONG_PLAN);
			boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
			int type = bundle.getInt(IntentConstants.INTENT_ROLE_TYPE);

			try {
				CustomerListDTO dto = SQLUtils.getInstance().getCustomerList(staffId, shopId, name, code, null, visit_plan, isGetWrongPlan, page, isGetTotalPage, type);

				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					StringBuffer buff = new StringBuffer();
					buff.append("staffId: ");
					buff.append(staffId);
					buff.append("/shopId: ");
					buff.append(shopId);
					buff.append("/name: ");
					buff.append(name);
					buff.append("/visit_plan: ");
					buff.append(visit_plan);
					buff.append("/isGetWrongPlan: ");
					buff.append(isGetWrongPlan);
					ServerLogger.sendLog("method getListCustomer: ", buff.toString(), TabletActionLogDTO.LOG_CLIENT);
					model.setIsSendLog(false);
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception e) {
				ServerLogger.sendLog("method getListCustomer1: ", e.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(e.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("method getListCustomer2: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {
		}
	}

	public void checkVisitFromActionLog(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		@SuppressWarnings("rawtypes")
		Vector viewInfo = (Vector) e.viewData;

		long staffId = Long.parseLong(viewInfo.get(viewInfo.lastIndexOf(IntentConstants.INTENT_STAFF_ID) + 1).toString());

		try {
			ActionLogDTO dto = SQLUtils.getInstance().checkVisitFromActionLog(staffId);
			model.setModelData(dto);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception e2) {
		}
	}

	/**
	 * get danh sach khach hang trong tuyen
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void getCustomerListForRoute(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		Bundle b = (Bundle) actionEvent.viewData;

		long staffId = Long.parseLong(b.getString(IntentConstants.INTENT_STAFF_ID));
		long shopId = Long.parseLong(b.getString(IntentConstants.INTENT_SHOP_ID));
		String visitPlan = b.getString(IntentConstants.INTENT_VISIT_PLAN);
		boolean isGetWrongPlan = b.getBoolean(IntentConstants.INTENT_GET_WRONG_PLAN);
		int type = b.getInt(IntentConstants.INTENT_ROLE_TYPE);

		try {
			CustomerListDTO dto = SQLUtils.getInstance().getCustomerListForRoute(staffId, shopId, visitPlan, isGetWrongPlan, type);
			GeomDTO nvbh = SQLUtils.getInstance().getPosition(staffId);
			ArrayList<Object> data = new ArrayList<Object>();
			data.add(dto);
			data.add(nvbh);

			if (dto != null) {
				model.setModelData(data);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelMessage("Có " + dto.cusList.size()
						+ " khách hàng.");
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
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
	 * get danh sach khach hang trong tuyen cho ban do tbhv
	 * 
	 * @author : TamPQ since : 1.0
	 */
	public void getTBHVCustomerInVisitPlan(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		Bundle b = (Bundle) actionEvent.viewData;

		long staffId = Long.parseLong(b.getString(IntentConstants.INTENT_STAFF_ID));
		long shopId = Long.parseLong(b.getString(IntentConstants.INTENT_SHOP_ID));
		String visitPlan = b.getString(IntentConstants.INTENT_VISIT_PLAN);
		String isGetWrongPlan = b.getString(IntentConstants.INTENT_GET_WRONG_PLAN);

		boolean getWrongPlan = false;
		if (isGetWrongPlan.equals("1")) {
			getWrongPlan = true;
		}
		try {
			CustomerListDTO dto = SQLUtils.getInstance().getTBHVCustomerInVisitPlan(staffId, shopId, visitPlan, getWrongPlan);

			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelMessage("Có " + dto.cusList.size()
						+ " khách hàng.");
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
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
	 * get danh sach don hang trong ngay
	 * 
	 * @author PhucNT
	 * @param ae
	 */
	public void getListSalesOrderInDate(ActionEvent ae) {
		ListOrderMngDTO listSaleOrderView = null;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(ae);

		Bundle viewInfo = (Bundle) ae.viewData;
		String to_date = "", customer_code = "", customer_name = "", staffId = "", shop_id = "", staffOwnerId = "";
		int  status;
		int page;
		boolean bApproved = true;

		if (viewInfo != null) {
			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_STAFFID)) {
				staffId = viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_STAFFID);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				SaleController.getInstance().handleErrorModelEvent(model);
				return;
			}

			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_STAFFOWNERID)) {
				staffOwnerId = viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_STAFFOWNERID);
			}

			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_SHOP_ID)) {
				shop_id = viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_SHOP_ID);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				SaleController.getInstance().handleErrorModelEvent(model);
				return;
			}

			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_TO_DATE)) {
				to_date = viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
			}

			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_CUSTOMER_CODE)) {
				customer_code = viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_CUSTOMER_CODE);
			}

			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_CUSTOMER_NAME)) {
				customer_name = viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_CUSTOMER_NAME);
			}
			
			if (viewInfo.containsKey(IntentConstants.INTENT_FIND_ORDER_BAPPROVED)) {
				if ("1".equals(viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_BAPPROVED))) {
					bApproved = true;
				} else if ("0".equals(viewInfo.getString(IntentConstants.INTENT_FIND_ORDER_BAPPROVED))) {
					bApproved = false;
				}
			}
			status = viewInfo.getInt(IntentConstants.INTENT_FIND_ORDER_STATUS);
			page = viewInfo.getInt(IntentConstants.INTENT_PAGE);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
			return;
		}
		try {
			SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(SQLUtils.getInstance().getmDB());
			// staffId = "255";

			listSaleOrderView = orderTable.getSalesOrderInDate(to_date, customer_code, customer_name, status, staffId, staffOwnerId, shop_id, bApproved, page);
			int count = orderTable.getTotalSalesOrderNotSend(staffId, shop_id);

			// listSaleOrderView = orderTable.getSalesOrderInDate(from_date,
			// to_date, customer_code, customer_name,
			// typeRoute, status, staffId, shop_id, bApproved, page);
			// int count = orderTable.getTotalSalesOrderNotSend(from_date,
			// to_date, customer_code, customer_name,
			// typeRoute, status, staffId, shop_id, bApproved);

			NotifyOrderDTO notifyOrder = null;
			if (viewInfo.getBoolean(IntentConstants.INTENT_GET_LIST_LOG)) {
				notifyOrder = SQLUtils.getInstance().getOrderNeedNotify();
			}
			if (listSaleOrderView != null) {
				listSaleOrderView.totalIsSend = count;
				listSaleOrderView.notifyDTO = notifyOrder;
				model.setModelData(listSaleOrderView);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			} else {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			}

			SaleController.getInstance().handleModelEvent(model);
			return;
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
			return;
		} finally {

		}
	}

	/**
	 * Lay thong tin & chi tiet don hang de view len khi xem & sua
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getOrderForEdit(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle v = (Bundle) event.viewData;
		long orderId;

		OrderViewDTO result = new OrderViewDTO();

		try {

			if (v.getString(IntentConstants.INTENT_ORDER) != null) {
				orderId = Long.parseLong(v.getString(IntentConstants.INTENT_ORDER));
			} else {
				model.setModelMessage("Không có thông tin Id đơn hàng");
				SaleController.getInstance().handleErrorModelEvent(model);
				return;
			}

			// get ds from sqllite
			ListSaleOrderDTO listChosenProduct = SQLUtils.getInstance().getSaleOrderForEdit(orderId);
			result.orderInfo = listChosenProduct.saleOrderDTO;
			result.listBuyOrders = listChosenProduct.listData;

			// Get ds promotion from sqlite
			ListSaleOrderDTO listPromotionProduct = SQLUtils.getInstance().getPromotionProductsForEdit(orderId, result.orderInfo.orderType);
			result.listPromotionOrders = listPromotionProduct.listData;

			int index = 0, size = result.listPromotionOrders.size();
			List<OrderDetailViewDTO> listPromotionForPromo21 = new ArrayList<OrderDetailViewDTO>();
			for (index = 0; index < size; index++) {
				OrderDetailViewDTO detailViewDTO = result.listPromotionOrders.get(index);
				if (CalPromotions.ZV21.equals(detailViewDTO.orderDetailDTO.programeTypeCode)) {
					detailViewDTO.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER_TYPE_PRODUCT;
					listPromotionForPromo21.add(detailViewDTO);
				} else if (CalPromotions.ZV19.equals(detailViewDTO.orderDetailDTO.programeTypeCode)
						|| CalPromotions.ZV20.equals(detailViewDTO.orderDetailDTO.programeTypeCode)) {
					detailViewDTO.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ORDER;
				}

			}

			if (listPromotionForPromo21.size() > 0) {
				OrderDetailViewDTO orderJoinTableDTO = new OrderDetailViewDTO();
				SaleOrderDetailDTO orderDTO = new SaleOrderDetailDTO();
				orderJoinTableDTO.orderDetailDTO = orderDTO;
				orderJoinTableDTO.promotionType = OrderDetailViewDTO.PROMOTION_FOR_ZV21;
				orderJoinTableDTO.orderDetailDTO.programeType = listPromotionForPromo21.get(0).orderDetailDTO.programeType;
				orderJoinTableDTO.orderDetailDTO.programeCode = listPromotionForPromo21.get(0).orderDetailDTO.programeCode;

				orderJoinTableDTO.listPromotionForPromo21.addAll(listPromotionForPromo21);

				for (OrderDetailViewDTO detailViewDTO : listPromotionForPromo21) {
					result.listPromotionOrders.remove(detailViewDTO);
				}

				result.listPromotionOrders.add(orderJoinTableDTO);
			}
		} catch (Exception ex) {
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
	 * load danh sach Gop y NVBH
	 * 
	 * @author : TamPQ
	 * 
	 */
	public void getListCusFeedBack(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		String staffId = bundle.getString(IntentConstants.INTENT_STAFF_ID);
		String customerId = bundle.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String type = bundle.getString(IntentConstants.INTENT_TYPE);
		String status = bundle.getString(IntentConstants.INTENT_STATE);
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		String doneDate = bundle.getString(IntentConstants.INTENT_DONE_DATE);
		boolean getTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		int from = bundle.getInt(IntentConstants.INTENT_FROM);

		CustomerFeedBackDto dto = SQLUtils.getInstance().getFeedBackList(staffId, customerId, type, status, doneDate, page, getTotalPage, (BaseFragment) e.sender, from);
		if (dto != null) {
			model.setModelData(dto);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Không có feedback.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * load thong tin co ban cua khach hang
	 * 
	 * @author : BangHN since : 8:25:08 AM
	 */
	@SuppressWarnings({ "unchecked" })
	public void getCustomerBaseInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		CustomerInfoDTO customerInfoDTO = new CustomerInfoDTO();
		Vector<Object> viewInfo = (Vector<Object>) e.viewData;
		String customerId = "";
		String numTop = "", page = "";
		if (viewInfo != null) {
			if (viewInfo.lastIndexOf(IntentConstants.INTENT_CUSTOMER_ID) >= 0) {
				customerId = viewInfo.get(viewInfo.lastIndexOf(IntentConstants.INTENT_CUSTOMER_ID) + 1).toString();
			}

			if (viewInfo.lastIndexOf(IntentConstants.INTENT_PAGE) >= 0) {
				page = viewInfo.get(viewInfo.lastIndexOf(IntentConstants.INTENT_PAGE) + 1).toString();
			}
			if (viewInfo.lastIndexOf(IntentConstants.INTENT_NUMTOP) >= 0) {
				numTop = viewInfo.get(viewInfo.lastIndexOf(IntentConstants.INTENT_NUMTOP) + 1).toString();
			}
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
			SaleController.getInstance().handleErrorModelEvent(model);
			return;
		}

		String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;

		try {
			// thong tin customer & customer type
			customerInfoDTO = SQLUtils.getInstance().getCustomerInfo(customerId, shopId);
			if (customerInfoDTO == null
					|| customerInfoDTO.getCustomer() == null) {
				// fixed bug truong hop customer ko co customerType
				customerInfoDTO = new CustomerInfoDTO();
				customerInfoDTO.setCustomer(SQLUtils.getInstance().getCustomerById(customerId));
				// lay so don hang trong thang
				int saleOrdersInMonth = SQLUtils.getInstance().getNumberOrdersInMonth(customerId);
				customerInfoDTO.saleOrdersInMonth = saleOrdersInMonth;

				// so mat hang khac nhau ban trong ngay
				customerInfoDTO.sku = SQLUtils.getInstance().getSKUOfCustomerInMonth(customerId, shopId);

				long amountInMonth = SQLUtils.getInstance().getAverageSalesInMonth(customerId, shopId);
				customerInfoDTO.amountInMonth = amountInMonth;
			}

			// lay doanh so trong 3 thang gan day
			ArrayList<SaleInMonthDTO> saleIn3Month = SQLUtils.getInstance().getAverageSalesIn3MonthAgo(customerId);
			Calendar cal = Calendar.getInstance();
			int month = cal.get(Calendar.MONTH);
			int size = saleIn3Month.size();

			int oneMonthAgo = (month - 0 + 12) % 12;
			if (oneMonthAgo == 0)
				oneMonthAgo = 12;
			int twoMonthAgo = (month - 1 + 12) % 12;
			if (twoMonthAgo == 0)
				twoMonthAgo = 12;
			int threeMonthAgo = (month - 2 + 12) % 12;
			if (threeMonthAgo == 0)
				threeMonthAgo = 12;

			for (int i = 0; i < size; i++) {
				if (saleIn3Month.get(i).month == threeMonthAgo) {
					customerInfoDTO.amountInThreeMonthAgo = saleIn3Month.get(i).quantity;
				} else if (saleIn3Month.get(i).month == twoMonthAgo) {
					customerInfoDTO.amountInTwoMonthAgo = saleIn3Month.get(i).quantity;
				} else if (saleIn3Month.get(i).month == oneMonthAgo) {
					customerInfoDTO.amountInOneMonthAgo = saleIn3Month.get(i).quantity;
				}
			}

			// get nhung don hang gan day
			customerInfoDTO.listOrderCustomer = SQLUtils.getInstance().getLastSaleOrders(customerId, shopId, Integer.parseInt(page), Integer.parseInt(numTop));

			// chuong trinh khach hang tham gia
//			ArrayList<CustomerProgrameDTO> listPro = SQLUtils.getInstance().getCustomerProgrames(customerId);
//			customerInfoDTO.setListCustomerPrograme(listPro);

			model.setModelData(customerInfoDTO);
		} catch (Exception ex) {
			VTLog.i("UserModel", "getCustomerBaseInfo : " + ex.toString());
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
			SaleController.getInstance().handleErrorModelEvent(model);
			return;
		}
		model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
		SaleController.getInstance().handleModelEvent(model);
	}

	/**
	 * 
	 * get general statistics report info (date and month) for Saleman
	 * 
	 * @author: duongdt3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getGeneralStatisticsReportInfoForSaleman(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);

		GeneralStatisticsViewDTO reportInfo = null;
		try {
			reportInfo = SQLUtils.getInstance().getGeneralStatisticsReportForSaleman(staffId, shopId);

			if (reportInfo != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(reportInfo);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getGeneralStatisticsReportInfoForSaleman error: ", "sql get general statistics report error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getGeneralStatisticsReportInfoForSaleman error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			reportInfo = null;
		}
	}

	/**
	 * 
	 * get general statistics report info (date and month) for Saleman
	 * 
	 * @author: duongdt3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getGeneralStatisticsReportInfoForTTTT(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String idPG = data.getString(IntentConstants.INTENT_STAFF_ID, "");
		String date = data.getString(IntentConstants.INTENT_DATE, "");
		// mac dinh la co load danh sach
		boolean isLoadStaffList = data.getBoolean(IntentConstants.INTENT_IS_LOAD_STAFF_LIST, true);
		GeneralStatisticsTNPGViewDTO reportInfo = null;
		try {
			reportInfo = SQLUtils.getInstance().getGeneralStatisticsReportForTNPG(staffId, shopId, idPG, date, isLoadStaffList);

			if (reportInfo != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(reportInfo);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getGeneralStatisticsReportInfoForTTTT error: ", "sql get general statistics report error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getGeneralStatisticsReportInfoForTTTT error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			reportInfo = null;
		}
	}

	/**
	 * get general statistics report info (date and month) for Supervior (GSBH)
	 * 
	 * @author: duongdt3
	 * @since: 14:23:11 25 Nov 2013
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getGeneralStatisticsReportInfoForSupervior(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String parrentStaffId = data.getString(IntentConstants.INTENT_PARENT_STAFF_ID);
		boolean isLoadListStaff = data.getBoolean(IntentConstants.INTENT_IS_LOAD_STAFF_LIST, false);

		GSBHGeneralStatisticsViewDTO reportInfo = null;
		try {
			reportInfo = SQLUtils.getInstance().getGeneralStatisticsReportForSupervior(staffId, shopId, parrentStaffId, isLoadListStaff);

			if (reportInfo != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(reportInfo);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getGeneralStatisticsReportInfoForSupervior error: ", "sql get general statistics report error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getGeneralStatisticsReportInfoForSupervior error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			reportInfo = null;
		}
	}

	/**
	 * get list note for general statistics view
	 * 
	 * @author: HaiTC3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListNoteForGeneralStatisticsView(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);

		ListNoteInfoViewDTO listNote = null;
		try {
			listNote = SQLUtils.getInstance().getListNoteForGeneralStaticsView(staffId, shopId);
			if (listNote != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(listNote);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				ServerLogger.sendLog("method getListNoteForGeneralStatisticsView: ", "sql get list note for general statics view error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			ServerLogger.sendLog("method getListNoteForGeneralStatisticsView: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * update note infor to database
	 * 
	 * @author: HaiTC3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest requestGetUpdateNoteStatusToDB(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		NoteInfoDTO noteUpdate = (NoteInfoDTO) data.getSerializable(IntentConstants.INTENT_NOTE_OBJECT);

		ListNoteInfoViewDTO noteListInfo = null;
		HTTPRequest re = null;
		try {
			noteListInfo = SQLUtils.getInstance().updateNoteInfoToDB(staffId, shopId, noteUpdate);
			if (noteListInfo != null) {
				JSONObject sqlUpdate = noteUpdate.generateJsonUpdateFeedBack();
				JSONArray listSQL = new JSONArray();
				listSQL.put(sqlUpdate);
				String logId = GlobalUtil.generateLogId();
				e.userData = noteListInfo;
				e.logData = logId;
				// send to server
				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(listSQL);
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(listSQL.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				re = sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(e.userData);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu ghi chú không thành công. Kiểm tra lại dữ liệu.");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestGetUpdateNoteStatusToDB error: ", "save note to db local incorrect", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestGetUpdateNoteStatusToDB error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * Lay ds san pham khuyen mai tu san pham ban
	 * 
	 * @author: TruongHN
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	public void getPromotionProducts(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		try {
			// insert to sql Lite & request to server
			OrderViewDTO orderDTO = (OrderViewDTO) actionEvent.viewData;
			SortedMap<Long, List<OrderDetailViewDTO>> results = SQLUtils.getInstance().calculatePromotionProducts(orderDTO);

			model.setModelData(results);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Xóa một đơn hàng
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest deleteSaleOrder(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;

		try {
			SaleOrderViewDTO dto = (SaleOrderViewDTO) e.viewData;
			SALES_ORDER_DETAIL_TABLE orderDetailTable = new SALES_ORDER_DETAIL_TABLE(SQLUtils.getInstance().getmDB());
			List<SaleOrderDetailDTO> listSaleOrderDetail = new ArrayList<SaleOrderDetailDTO>();
			listSaleOrderDetail = orderDetailTable.getAllDetailOfSaleOrder(dto.saleOrder.saleOrderId);

			ActionLogDTO actionLogDTO = new ActionLogDTO();
			actionLogDTO.objectType = "4";
			actionLogDTO.objectId = String.valueOf(dto.saleOrder.saleOrderId);
			actionLogDTO.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;

			// Check order is last order of customer
			SALE_ORDER_TABLE orderTable = new SALE_ORDER_TABLE(SQLUtils.getInstance().getmDB());
			dto.lastOrder = orderTable.getPreviousLastOrderDate(dto);
			Date currentDate = DateUtils.parseDateFromString(DateUtils.getCurrentDate(), DateUtils.defaultDateFormat);
			Date orderDate = DateUtils.parseDateFromString(dto.saleOrder.createDate, DateUtils.defaultDateFormat_2);
			// Khong cap nhat last order neu xoa don hang
			if (!StringUtil.isNullOrEmpty(dto.lastOrder)
					&& dto.lastOrder.equals(dto.saleOrder.orderDate)// do orderDate va createDate lech nhau nen lay lai truong so sanh
					|| (orderDate.compareTo(currentDate) < 0)) {
				dto.isFinalOrder = 0;
			} else {
				dto.isFinalOrder = 1;
			}
			// dto.isFinalOrder = orderTable.checkIsLastOrder(dto);

			boolean result = SQLUtils.getInstance().deleteSaleOrder(dto, listSaleOrderDetail, actionLogDTO);

			if (result) {
				if (dto.saleOrder.synState == 2) {// order exist on server
					JSONArray listSql = SQLUtils.getInstance().generateSqlDeleteOrder(dto, listSaleOrderDetail, actionLogDTO);
					String logId = GlobalUtil.generateLogId();
					e.logData = logId;
					// send to server
					Vector<Object> para = new Vector<Object>();
					para.add(IntentConstants.INTENT_LIST_SQL);
					para.add(listSql);
					para.add(IntentConstants.INTENT_MD5);
					para.add(StringUtil.md5(listSql.toString()));
					para.add(IntentConstants.INTENT_LOG_ID);
					para.add(logId);
//					para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//					para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
					para.add(IntentConstants.INTENT_IMEI_PARA);
					para.add(GlobalInfo.getInstance().getDeviceIMEI());

					re = sendHttpRequestOffline("queryController/executeSql", para, e);
				} else {// send to server vi luon tao action log
					JSONArray listSql = new JSONArray();
					listSql.put(actionLogDTO.generateDeleteActionWhenDeleteOrder());

					String logId = GlobalUtil.generateLogId();
					e.logData = logId;
					// send to server
					Vector<Object> para = new Vector<Object>();
					para.add(IntentConstants.INTENT_LIST_SQL);
					para.add(listSql);
					para.add(IntentConstants.INTENT_MD5);
					para.add(StringUtil.md5(listSql.toString()));
					para.add(IntentConstants.INTENT_LOG_ID);
					para.add(logId);
//					para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//					para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
					para.add(IntentConstants.INTENT_IMEI_PARA);
					para.add(GlobalInfo.getInstance().getDeviceIMEI());

					re = sendHttpRequestOffline("queryController/executeSql", para, e);
				}

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelMessage("Xóa đơn hàng thành công");
				SaleController.getInstance().handleModelEvent(model);
			} else {

				ServerLogger.sendLog("Xóa đơn hàng " + dto.getSaleOrderId()
						+ " không thành công.", TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelMessage("Xóa đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {

		}

		return re;
	}

	/**
	 * postListFeedBack
	 *
	 * @author: trungnt56
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void postListFeedBack(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bd = (Bundle) e.viewData;
		try {
			ArrayList<FeedBackDTO> dtos = SQLUtils.getInstance().postListFeedBack(bd.<FeedBackDTO>getParcelableArrayList(IntentConstants.INTENT_FEEDBACK_DTO));

			// send to server
			if (dtos != null && dtos.size() > 0) {
				JSONArray jarr = new JSONArray();
				FeedBackDTO feedBackDTO;
				for (int i = 0, size = dtos.size(); i < size; i++) {
					feedBackDTO = dtos.get(i);
					// update sales order detail
					jarr.put(feedBackDTO.generateFeedbackSql(feedBackDTO.remindDate));
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
				// if(result.getCode()==200){
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
				// }

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu feedback không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lưufeedback không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * postFeedBack
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void postFeedBack(ActionEvent e) {
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bd = (Bundle) e.viewData;
		FeedBackDTO dto = (FeedBackDTO) bd.getSerializable(IntentConstants.INTENT_FEEDBACK_DTO);
		try {
			dto.setType(TableType.FEEDBACK_TABLE);
			returnCode = SQLUtils.getInstance().postFeedBack(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateFeedbackSql(dto.remindDate);
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);
				// if(result.getCode()==200){
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
				// }

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu feedback không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lưufeedback không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * updateFeedBack TTTT, NVBH
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void updateDoneDateFeedBack(ActionEvent e) {
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		FeedBackDTO dto = (FeedBackDTO) e.viewData;
		try {
			dto.setType(TableType.FEEDBACK_TABLE);
			returnCode = SQLUtils.getInstance().updateDoneDateFeedBack(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateUpdateFeedbackSql();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update feedback không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Update feedback không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * delete feedback TTTT, NVBH
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void deleteFeedBack(ActionEvent e) {
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		FeedBackDTO dto = (FeedBackDTO) e.viewData;
		try {
			dto.setType(TableType.FEEDBACK_TABLE);
			returnCode = SQLUtils.getInstance().updateDeleteFeedBack(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateDeleteFeedbackSql(dto);
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update feedback không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
		}
	}

	/**
	 * inset mot hanh dong xuong action_log (local & server db)
	 * 
	 * @author : BangHN since : 1.0
	 */
	public HTTPRequest inserActionLogToDB(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		ActionLogDTO actionLog = (ActionLogDTO) e.viewData;
		actionLog.setType(TableType.ACTION_LOG);
		try {
			long insetLocal = SQLUtils.getInstance().insertActionLog(actionLog);
			if (insetLocal != -1) {// insert local thanh cong != -1
				JSONObject sqlPara = actionLog.generateActionLogSql();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				re = sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				ServerLogger.sendLog("inserActionLogToDB - "
						+ actionLog.aCustomer.customerId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Insert action log không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Throwable ex) {
			ServerLogger.sendLog("inserActionLogToDB - " + ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Insert action log không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * delete mot hanh dong xuong action_log (local & server db)
	 * 
	 * @author : BangHN since : 1.0
	 */
	public HTTPRequest deleteActionLogToDB(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		ActionLogDTO actionLog = (ActionLogDTO) e.viewData;
		actionLog.setType(TableType.ACTION_LOG);
		try {
			long insetLocal = SQLUtils.getInstance().deleteActionLogWhenRemoveExceptionOrderDate(actionLog);
			if (insetLocal != -1) {// insert local thanh cong != -1
				JSONObject sqlPara = actionLog.generateDeleteActionLogSql();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				re = sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Insert action log không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Insert action log không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * inser Visit ActionLog To DB
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void inserVisitActionLogToDB(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		ActionLogDTO actionLog = (ActionLogDTO) e.viewData;
		actionLog.setType(TableType.ACTION_LOG);

		try {
			boolean alreadyHaveVisitLog = SQLUtils.getInstance().alreadyHaveVisitLog(actionLog);
			if (!alreadyHaveVisitLog) {
				VTLog.e("TamPQ", "vodddddddddddddddddddddddddddddd");
				inserActionLogToDB(e);
			} else {
				ServerLogger.sendLog("inserVisitActionLogToDB - "
						+ actionLog.aCustomer.customerId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Insert action log không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception e2) {
			ServerLogger.sendLog("inserVisitActionLogToDB " + e2.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Insert action log không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	public HTTPRequest updateActionLogToDB(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		ActionLogDTO actionLog = (ActionLogDTO) e.viewData;
		actionLog.setType(TableType.ACTION_LOG);
		try {
			// TamPQ: kiem tra neu co action_log ghe tham khac co end_time =
			// null thi ket thuc luon action_log do voi end_time = start_time +
			// 6 phut
			ArrayList<ActionLogDTO> listActLog = SQLUtils.getInstance().getVisitActionLogWithEndTimeIsNull(GlobalInfo.getInstance().getProfile().getUserData().id, actionLog.aCustomer.customerId);
			if (listActLog.size() > 0) {
				for (ActionLogDTO actionLogDTO : listActLog) {
					if (actionLogDTO.id != actionLog.id) {
						actionLogDTO.endTime = DateUtils.plusTime(actionLogDTO.startTime, 6);
						long second = DateUtils.getDistanceSecondsFrom2Date(actionLogDTO.startTime, actionLogDTO.endTime);
						actionLogDTO.interval_time = String.valueOf(second);
					}
				}
				SQLUtils.getInstance().updateEndtimeListActionLogToDB(listActLog);
				JSONArray sqlArr = new ActionLogDTO().generateUpdateEntimeListActionLogSql(listActLog);

				String logId = GlobalUtil.generateLogId();
				e.logData = logId;

				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(sqlArr.toString());
				para.add(IntentConstants.INTENT_MD5);
				para.add(StringUtil.md5(sqlArr.toString()));
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);
			}

			long updateLocal = SQLUtils.getInstance().updateActionLogToDB(actionLog);

			if (updateLocal > 0) {// insert local thanh cong
				JSONObject sqlPara = actionLog.generateUpdateActionLogSql();
				JSONObject sqlParaInsert = new JSONObject();
				ActionLogDTO actionLogDTO = new ActionLogDTO();
				actionLogDTO = getActionLog(actionLog);
				long insetLocal = SQLUtils.getInstance().insertActionLog(actionLogDTO);
				if (insetLocal != -1) {// insert local thanh cong != -1
					sqlParaInsert = actionLogDTO.generateActionLogSql();
				}
				JSONArray jarr = new JSONArray();
				jarr.put(sqlPara);
				jarr.put(sqlParaInsert);
				String logId = GlobalUtil.generateLogId();
				e.logData = logId;

				Vector<Object> para = new Vector<Object>();
				para.add(IntentConstants.INTENT_LIST_SQL);
				para.add(jarr.toString());
				para.add(IntentConstants.INTENT_LOG_ID);
				para.add(logId);
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				re = sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update action log không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Update action log không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

    /**
     * Lay lai thong tin lat, lng, distance luu action log
     *
     * @author: yennth16
     */
	public ActionLogDTO getActionLog(ActionLogDTO actionLogDTO){
        ActionLogDTO actionLog = new ActionLogDTO();
        actionLog = actionLogDTO;
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO = SQLUtils.getInstance().getCustomerById(actionLog.aCustomer.getCustomerId());
        LatLng myLatLng = new LatLng(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude(), GlobalInfo
                .getInstance().getProfile().getMyGPSInfo().getLongtitude());
        LatLng cusLatLng = new LatLng(customerDTO.getLat(), customerDTO.getLng());
        actionLog.distance = GlobalUtil.getDistanceBetween(myLatLng, cusLatLng);
        actionLogDTO.objectType = actionLog.TYPE_END_VISIT;
        actionLog.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
        actionLog.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
        return actionLog;
    }

	/**
	 * save nhung san pham ton kho
	 * 
	 * @author: PhucNT
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	public HTTPRequest requestSaveRemainProduct(ActionEvent e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			ArrayList<RemainProductViewDTO> listRemain = (ArrayList<RemainProductViewDTO>) e.viewData;

			ArrayList<CustomerStockHistoryDTO> listCSTDTO = SQLUtils.getInstance().saveRemainProduct(listRemain);

			// boolean insertSuccess = true;
			if (listCSTDTO != null) {
				// cap nhat lai id max cho bang table

				JSONArray sqlPara = generateSaveRemainProductSql(listCSTDTO);

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

				re = sendHttpRequestOffline("queryController/executeSql", para, e);
				// model.setModelMessage("Những đơn hàng đã được chuyển thành công.");

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				ServerLogger.sendLog("Lưu danh sách hàng tồn lưu không thành công", TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("Lưu danh sách hàng tồn lưu không thành công", TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);

			SaleController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	private JSONArray generateSaveRemainProductSql(ArrayList<CustomerStockHistoryDTO> listOrder) {
		// TODO Auto-generated method stub

		JSONArray listSql = new JSONArray();
		CustomerStockHistoryDTO detail;
		for (int i = 0, size = listOrder.size(); i < size; i++) {
			detail = listOrder.get(i);
			// update sales order detail
			listSql.put(detail.generateUpdateRemainProductSql());
		}

		return listSql;
	}

	/**
	 * Lay ds muc do khan
	 * 
	 * @author: TruongHN
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getCommonDataInOrder(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			// lay ds muc do khan
			ArrayList<ApParamDTO> listPara = SQLUtils.getInstance().getListParaByType(bundle.getString(IntentConstants.INTENT_PRIORITY_CODE));

			// String shopId =
			// GlobalInfo.getInstance().getProfile().getUserData().shopId;
			// String staffId =
			// String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);

			// lay ds tuyen cua khach hang
			// VisitPlanDTO visit =
			// SQLUtils.getInstance().getVisitPlanOfCustomer(bundle.getString(IntentConstants.INTENT_CUSTOMER_ID),
			// shopId, staffId);

			ArrayList<Object> result = new ArrayList<Object>();
			result.add(listPara);
			// result.add(visit);
			model.setModelData(result);

			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);

		} catch (Exception ex) {
			// goi log len server
			ServerLogger.sendLog(ex.getStackTrace()[1].getMethodName(), ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * get promotion detail
	 * 
	 * @author: HaiTC3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getPromotionDetail(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			// insert to sql Lite & request to server
			PromotionProgrameDTO result = SQLUtils.getInstance().getPromotionProgrameDetail((Bundle) e.viewData);

			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getPromotionDetail error: ", "sql get promotion progrmae detail info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getPromotionDetail error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * update thong tin vi tri khach hang
	 ** 
	 * @author : BangHN since : 1.0
	 */
	public HTTPRequest requestUpdateCustomerLocation(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		try {
			CustomerDTO customerInfo = (CustomerDTO) e.viewData;
			ArrayList<CustomerPositionLogDTO> log = new ArrayList<CustomerPositionLogDTO>();
			log = (ArrayList<CustomerPositionLogDTO>) e.userData;
			CustomerPositionLogDTO logPostion = new CustomerPositionLogDTO();
			logPostion = log.get(0);
			CustomerPositionLogDTO updatePostion = new CustomerPositionLogDTO();
			if(log.size() > 1){
				updatePostion = log.get(1);
			}
			// cap nhat position moi trong customer_table
			long updateCustomer = SQLUtils.getInstance().updateCustomerLocation(customerInfo);
			// hien tai khong can insert record position log khi cap nhat vi tri
			// moi, cho dong bo ve
			if (updateCustomer > 0) {
				JSONObject sqlUpdateCustomerPosition = customerInfo.generateUpdateLocationSql();
				JSONObject sqlInsertPositionLog = logPostion.generateInsertCustomerPositionLogSql();
				JSONObject sqlUpdateCustomerPositionLog = new JSONObject();
				if(log.size() > 1) {
					sqlUpdateCustomerPositionLog = updatePostion.generateUpdateLocationSql();
				}
				JSONArray jarr = new JSONArray();
				jarr.put(sqlUpdateCustomerPosition);
				jarr.put(sqlInsertPositionLog);
				if(log.size() > 1){
					jarr.put(sqlUpdateCustomerPositionLog);
				}
				if (customerInfo.getAreaId() > 0){
					JSONObject sqlUpdateAreaCustomer = customerInfo.generateUpdateCustomerAreaSql();
					jarr.put(sqlUpdateAreaCustomer);
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

				// re = sendHttpRequestOffline("queryController/executeSql", para, e);
				// model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				// SaleController.getInstance().handleModelEvent(model);

				re = sendHttpRequest("queryController/executeSql", para, e);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * 
	 * Danh sach hinh anh khach hang NVBH
	 * 
	 * @author quangvt1
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getCusImageList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		try {
			ImageListDTO dto = SQLUtils.getInstance().getImageList(data);

			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {
		}
	}

	/**
	 * 
	 * Danh sach hinh anh khach hang GSNPP
	 * 
	 * @author: HoanPD1
	 * @param e
	 * @return: void
	 * @throws:
	 * @date: Jan 8, 2013
	 */
	public void getCusImageListGSNPP(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		// @SuppressWarnings("rawtypes")
		Bundle data = (Bundle) e.viewData;
		// Vector viewInfo = (Vector) e.viewData;
		//
		// int staffId = Integer.parseInt(viewInfo.get(
		// viewInfo.lastIndexOf(IntentConstants.INTENT_STAFF_ID) + 1)
		// .toString());
		// String cusCode = viewInfo.get(
		// viewInfo.lastIndexOf(IntentConstants.INTENT_CUSTOMER_CODE) + 1)
		// .toString();
		// String cusName = viewInfo.get(
		// viewInfo.lastIndexOf(IntentConstants.INTENT_CUSTOMER_NAME) + 1)
		// .toString();
		// int page = Integer.parseInt(viewInfo.get(
		// viewInfo.lastIndexOf(IntentConstants.INTENT_PAGE) + 1)
		// .toString());
		try {
			ImageListDTO dto = SQLUtils.getInstance().getImageList(data);

			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {

		}
	}

	/**
	 * 
	 * Request insert media item
	 * 
	 * @author: PhucNT
	 * @param e
	 * @return
	 * @return: HTTPRequest
	 * @throws:
	 */
	public HTTPRequest requestInsertMediaItem(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			MediaItemDTO mediaDTO = (MediaItemDTO) e.viewData;

			long mediaId = SQLUtils.getInstance().insertMediaItem(mediaDTO);
			if (mediaId != -1) {

				JSONArray sqlPara = mediaDTO.generateInsertMediaItem();

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

				re = sendHttpRequestOffline("queryController/executeSql", para, e);
			} else {
				ServerLogger.sendLog("Insert mot hinh anh bị thất bại "
						+ mediaDTO.id, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Insert hình thất bại");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * Lay thong tin chi tiet cua san pham
	 * 
	 * @author: ThanhNN8
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestGetProductInfoDetail(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			// insert to sql Lite & request to server
			ProductDTO result = SQLUtils.getInstance().getProductInfoDetail((Bundle) e.viewData);

			model.setModelData(result);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay ds order trong log table
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void requestGetOrderInLog(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			// ArrayList<LogDTO> list = SQLUtils.getInstance().getOrderInLog();
			NotifyOrderDTO notifyDTO = SQLUtils.getInstance().getOrderNeedNotify();
			model.setModelData(notifyDTO);

			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			// model.setModelMessage(ex.getMessage());
			// model.setModelCode(ErrorConstants.ERROR_COMMON);
			// SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * request get customer sale list
	 * 
	 * @author banghn
	 * @param e
	 */
	public void requestGetCustomerSaleList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle bundle = (Bundle) e.viewData;
		try {
			String staffId = bundle.getString(IntentConstants.INTENT_STAFF_ID);
			String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
			String code = bundle.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			String nameAddress = bundle.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			int page = bundle.getInt(IntentConstants.INTENT_PAGE);
			String visitPlan = bundle.getString(IntentConstants.INTENT_VISIT_PLAN);
			String ownerId = bundle.getString(IntentConstants.INTENT_USER_ID);
			try {
				CustomerListDTO dto = SQLUtils.getInstance().requestGetCustomerSaleList(ownerId, shopId, staffId, code, nameAddress, visitPlan, page);

				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
		} finally {
		}

	}

	/**
	 * update ExceptionOrderDate ve null
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestUpdateExceptionOrderDate(ActionEvent e) {
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		StaffCustomerDTO dto = (StaffCustomerDTO) e.viewData;
		try {
			dto.setType(TableType.VISIT_PLAN_TABLE);
			returnCode = SQLUtils.getInstance().updateExceptionOrderDate(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateUpdateExceptionOrderDate();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update exception_order_date ko thanh cong.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Update exception_order_date ko thanh cong.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * get danh sach loai van de NVBH
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListTypeProblemNVBHGSNPP(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Vector<ApParamDTO> vTypeProblem;
		try {
			vTypeProblem = SQLUtils.getInstance().getListTypeProblemNVBHGSNPP();
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
	 * Request lay danh sach cong no khach hang
	 * 
	 * @author: BangHN
	 * @param e
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */

	public void requestGetCustomerDebt(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String cusCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String cusNameAdd = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
		try {
			ArrayList<CustomerDebtDTO> listData = SQLUtils.getInstance().requestGetCustomerDebt(cusCode, cusNameAdd);
			if (listData != null) {
				model.setModelData(listData);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Chi tiet cong no
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void requestDebitDetail(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle bundle = (Bundle) e.viewData;
		try {
			long debitId = bundle.getLong(IntentConstants.INTENT_DEBIT_ID);
			try {
				CusDebitDetailDTO dto = SQLUtils.getInstance().requestDebitDetail(debitId);

				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
		} finally {
		}
	}

	/**
	 * lay danh sach van de MH them van de (GSBH)
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void requestGetTypeFeedback(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		try {
			int from = bundle.getInt(IntentConstants.INTENT_FROM);
			try {
				ArrayList<ApParamDTO> list = SQLUtils.getInstance().requestGetTypeFeedback(from);

				if (list != null) {
					model.setModelData(list);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelData(list);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
		} finally {
		}

	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param listSql
	 * @return
	 * @return: HTTPRequestvoid
	 * @throws:
	 */
	private HTTPRequest sendPayDebtSqlToServer(ActionEvent actionEvent, JSONArray listSql) {
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
//		para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//		para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
		para.add(IntentConstants.INTENT_IMEI_PARA);
		para.add(GlobalInfo.getInstance().getDeviceIMEI());

		re = sendHttpRequestOffline("queryController/executeSql", para, actionEvent);
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
	public void requestGetCusNoPSDS(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle b = (Bundle) e.viewData;
		// String staffId = b.getString(IntentConstants.INTENT_STAFF_ID);
		// String month = b.getString(IntentConstants.INTENT_DAY);
		int rptSaleHisId = b.getInt(IntentConstants.INTENT_ID);
		boolean isGetTotalPage = b.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
		int page = b.getInt(IntentConstants.INTENT_PAGE);
		try {
			try {
				CustomerListDTO dto = SQLUtils.getInstance().requestGetCusNoPSDS(rptSaleHisId, isGetTotalPage, page);

				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
		} finally {
		}
	}

	/**
	 * lay danh sach hinh anh chuong trinh cua khach hang
	 * 
	 * @author ThanhNN
	 * @param e
	 */
	public void gotoListAlbumPrograme(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		try {
			// lay gia tri ra de truyen vao cau request
			try {
				ArrayList<AlbumDTO> list = SQLUtils.getInstance().getAlbumProgrameInfo(bundle);

				if (list != null) {
					model.setModelData(list);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelData(list);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
		} finally {
		}
	}

	/**
	 * Lay ds hinh anh cua chuong trinh
	 * 
	 * @author: thanhnn
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void getAlbumDetailPrograme(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String type = String.valueOf(data.getInt(IntentConstants.INTENT_ALBUM_TYPE));
		String numTop = String.valueOf(data.getInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE));
		String page = String.valueOf(data.getInt(IntentConstants.INTENT_PAGE));
		String fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		String programeCode = data.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		PhotoThumbnailListDto result = null;
		try {
			result = new PhotoThumbnailListDto();
			ArrayList<PhotoDTO> listPhoto = SQLUtils.getInstance().getAlbumDetailPrograme(customerId, type, numTop, page, programeCode, fromDate, toDate, shopId);
			result.getAlbumInfo().getListPhoto().addAll(listPhoto);
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
	 * Lay chi tiet hinh anh cua khach hang o che do search
	 * 
	 * @author: QuangVT
	 * @since: 6:00:44 PM Dec 12, 2013
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getAlbumDetailForSearch(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String numTop = String.valueOf(data.getInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE));
		String page = String.valueOf(data.getInt(IntentConstants.INTENT_PAGE));
		String fromDate = data.getString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE);
		String toDate = data.getString(IntentConstants.INTENT_FIND_ORDER_TO_DATE);
		PhotoThumbnailListDto result = null;
		try {
			result = new PhotoThumbnailListDto();
			ArrayList<PhotoDTO> listPhoto = SQLUtils.getInstance().getAlbumDetailForSearch(customerId, numTop, page, fromDate, toDate);
			result.getAlbumInfo().getListPhoto().addAll(listPhoto);
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
	 * Lấy danh sách KH chưa PSDS cho NVBH
	 * 
	 * @author: duongdt3
	 * @since: 09:14:53 8 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getReportCustomerNotPSDSForSaleMan(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		int dayInWeek = viewInfo.getInt(IntentConstants.INTENT_DAY_IN_WEEK, 0);
		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID, "");
		int numItemInPage = viewInfo.getInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, 0);
		int page = viewInfo.getInt(IntentConstants.INTENT_PAGE, 0);
		CustomerNotPsdsInMonthReportViewDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getReportCustomerNotPSDSForSaleMan(shopId, dayInWeek, staffId, page, numItemInPage);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Xoa avatar
	 * 
	 * @author: dungdq3
	 * @param: ActionEvent e
	 * @return: void
	 * @date: Jan 4, 2014
	 */
	public void deleteAvatar(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		String staffCode = b.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String status = b.getString(IntentConstants.INTENT_STATUS);
		String shopID = b.getString(IntentConstants.INTENT_SHOP_ID);
		String customerID = b.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String customerAvatarID = b.getString(IntentConstants.INTENT_CUSTOMER_AVATAR_ID);
		String staffID = b.getString(IntentConstants.INTENT_STAFF_ID);
		try {
			CustomerAvatarDTO customerAvatarDTO = new CustomerAvatarDTO(shopID, customerAvatarID, customerID, null, null, status, staffCode, staffID);
			returnCode = SQLUtils.getInstance().deleteAvatar(customerAvatarDTO);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = customerAvatarDTO.generateJSONDeleteAvatar();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Cap nhat avatar không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Cap nhat avatar không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Cap nhat hinh anh diem ban
	 * 
	 * @author: dungdq3
	 * @param: ActionEvent e
	 * @return: void
	 * @date: Jan 6, 2014
	 */
	public void updateAvatar(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		String mediaItemID = b.getString(IntentConstants.INTENT_MEDIA_ITEM_ID);
		String staffCode = b.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String thumbURL = b.getString(IntentConstants.INTENT_THUMB_URL);
		String customerAvatarID = b.getString(IntentConstants.INTENT_CUSTOMER_AVATAR_ID);
		String staffID = b.getString(IntentConstants.INTENT_STAFF_ID);
		//boolean isNewAvatar = b.getBoolean(IntentConstants.INTENT_KEY);
		try {
			CustomerAvatarDTO customerAvatarDTO = new CustomerAvatarDTO(null, customerAvatarID, null, mediaItemID, thumbURL, "1", staffCode, staffID);
			returnCode = SQLUtils.getInstance().updateAvatar(customerAvatarDTO);

			// send to server
			if (returnCode != -1) {
				// Khong gui cau json len server
				// Server tu update
//				if (!isNewAvatar) {
//					JSONObject sqlPara = customerAvatarDTO.generateJSONUpdateAvatar();
//					JSONArray jarr = new JSONArray();
//					jarr.put(sqlPara);
//					String logId = GlobalUtil.generateLogId();
//					e.logData = logId;
//
//					Vector<Object> para = new Vector<Object>();
//					para.add(IntentConstants.INTENT_LIST_SQL);
//					para.add(jarr.toString());
//					para.add(IntentConstants.INTENT_MD5);
//					para.add(StringUtil.md5(jarr.toString()));
//					para.add(IntentConstants.INTENT_LOG_ID);
//					para.add(logId);
//					para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//					para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
//					para.add(IntentConstants.INTENT_IMEI_PARA);
//					para.add(GlobalInfo.getInstance().getDeviceIMEI());
//					sendHttpRequestOffline("queryController/executeSql", para, e);
//				}
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Cap nhat avatar không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Cap nhat avatar không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Them avatar moi, hinh anh moi chup
	 * Do khi gui hinh len server thi server da insert customer avatar roi
	 * do do khong can gui cau json insert nua
	 * 
	 * @author: dungdq3
	 * @param: Tham số của hàm
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 * @date: Jan 6, 2014
	 */
	public void insertNewAvatar(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		String mediaItemID = b.getString(IntentConstants.INTENT_MEDIA_ITEM_ID);
		String staffCode = b.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String thumbURL = b.getString(IntentConstants.INTENT_THUMB_URL);
		String shopID = b.getString(IntentConstants.INTENT_SHOP_ID);
		String customerID = b.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String status = b.getString(IntentConstants.INTENT_STATUS);
		String staffID = b.getString(IntentConstants.INTENT_STAFF_ID);
		//boolean isTakePhoto = b.getBoolean(IntentConstants.INTENT_KEY);
		try {
			CustomerAvatarDTO customerAvatarDTO = new CustomerAvatarDTO(shopID, null, customerID, mediaItemID, thumbURL, status, staffCode, staffID);
			returnCode = SQLUtils.getInstance().insertAvatar(customerAvatarDTO);

			// send to server
			if (returnCode != -1) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Them avatar không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Them avatar không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	} 
	
	/**
	 * Insert avatar cho khach hang
	 * Hinh anh tu album
	 * @author: quangvt1
	 * @since: 17:30:06 20-06-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void insertNewAvatarFromAlbum(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		String mediaItemID = b.getString(IntentConstants.INTENT_MEDIA_ITEM_ID);
		String staffCode = b.getString(IntentConstants.INTENT_CUSTOMER_CODE);
		String thumbURL = b.getString(IntentConstants.INTENT_THUMB_URL);
		String shopID = b.getString(IntentConstants.INTENT_SHOP_ID);
		String customerID = b.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String status = b.getString(IntentConstants.INTENT_STATUS);
		String staffID = b.getString(IntentConstants.INTENT_STAFF_ID);
		boolean isTakePhoto = b.getBoolean(IntentConstants.INTENT_KEY);
		try {
			CustomerAvatarDTO customerAvatarDTO = new CustomerAvatarDTO(shopID, null, customerID, mediaItemID, thumbURL, status, staffCode, staffID);
			returnCode = SQLUtils.getInstance().insertAvatar(customerAvatarDTO);
			
			// send to server
			if (returnCode != -1) {  
				if (!isTakePhoto) {
					JSONObject sqlPara = customerAvatarDTO.generateJSONInsertAvatar();
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
//					para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//					para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
					para.add(IntentConstants.INTENT_IMEI_PARA);
					para.add(GlobalInfo.getInstance().getDeviceIMEI());
					sendHttpRequestOffline("queryController/executeSql", para, e);
				}
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Them avatar không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Them avatar không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay max id cua customerAvatar de send 2 server
	 * 
	 * @author: dungdq3
	 * @param: ActionEvent e
	 * @return: void
	 * @date: Jan 6, 2014
	 */
	public void getMaxCustomerAvatarID(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		long result = -1;
		try {
			result = SQLUtils.getInstance().getMaxCustomerAvatarID(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = -1;
		}
		if (result > 0) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SaleController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds sản phẩm thất bại");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lấy danh sách KH mới chưa được duyệt của NVBH
	 * 
	 * @author: duongdt3
	 * @since: 11:20:53 3 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getListNewCustomer(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;

		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID, "");
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID, "");
		String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME, "");
		int numItemInPage = data.getInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, com.viettel.dms.constants.Constants.NUM_ITEM_PER_PAGE);
		int currentIndexSpinnerState = data.getInt(IntentConstants.INTENT_STATE, 0);
		int currentPage = data.getInt(IntentConstants.INTENT_PAGE, -1);
		NewCustomerListDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListNewCustomer(staffId, shopId, customerName, numItemInPage, currentIndexSpinnerState, currentPage);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * lấy các thông tin cần trong màn hình tạo khách hàng
	 * 
	 * @author: duongdt3
	 * @since: 09:16:35 6 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getCreateCustomerInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;

		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID, "");
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID, "");
		String customerId = data.getString(IntentConstants.INTENT_CUSTOMER_ID, "");

		CreateCustomerInfoDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getCreateCustomerInfo(staffId, shopId, customerId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	public void getAreaCustomerInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String areaCode = data.getString(IntentConstants.INTENT_AREA_CODE, "");

		CreateCustomerInfoDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getAreaCustomerInfo(areaCode);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	public void getAreaCustomerInfoNewCus(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		CreateCustomerInfoDTO cus = (CreateCustomerInfoDTO) data.getSerializable(IntentConstants.INTENT_DATA);
		String areaCode = data.getString(IntentConstants.INTENT_AREA_CODE, "");

		try {
			SQLUtils.getInstance().getAreaCustomerInfo(cus, areaCode);
			if (cus != null) {
				model.setModelData(cus);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(cus);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * lấy danh sách địa bàn từ id địa bàn cha + loại địa bàn
	 * 
	 * @author: duongdt3
	 * @since: 15:51:16 6 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getCreateCustomerAreaInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;

		int typeArea = data.getInt(IntentConstants.INTENT_AREA_TYPE);
		long parentAreaId = data.getLong(IntentConstants.INTENT_PARRENT_AREA_ID);

		ArrayList<AreaItem> dto = null;
		try {
			dto = SQLUtils.getInstance().getCreateCustomerAreaInfo(parentAreaId, typeArea);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Thêm KH do staff tạo
	 * 
	 * @author: duongdt3
	 * @since: 09:31:54 7 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void insertCustomer(ActionEvent e) {
		long returnCode = -1;
		RoutingCustomerDTO routingCustomerDTO = new RoutingCustomerDTO();
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bd = (Bundle) e.viewData;
		CustomerDTO dto = (CustomerDTO) bd.getSerializable(IntentConstants.INTENT_CUSTOMER);
		try {
			dto.setType(TableType.CUSTOMER);
			returnCode = SQLUtils.getInstance().insertCustomer(dto, routingCustomerDTO);
//			if(returnCode != -1) {
//				routingId = SQLUtils.getInstance().getRoutingIdByStaffId(GlobalInfo.getInstance().getProfile().getUserData().id);
//				if(routingId != 0) {
//					routingCustomerDTO = new RoutingCustomerDTO();
//					routingCustomerDTO.customerId = returnCode;
//					routingCustomerDTO.routingId = routingId;
//					returnCode = SQLUtils.getInstance().insertRoutingCustomer(routingCustomerDTO);
//				}
//			}

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateCreateCustomerSql();
				JSONArray jarr = new JSONArray();
				jarr.put(sqlPara);
				if(routingCustomerDTO.routingCustomerId != null && !routingCustomerDTO.routingCustomerId.equals(0)) {
					JSONObject sqlRoutingCustomer = routingCustomerDTO.generateCreateRoutingCustomerSql();
					jarr.put(sqlRoutingCustomer);
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

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(routingCustomerDTO.routingCustomerId);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu customer không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lưu customer không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * cập nhật thông tin customer do STAFF tạo ra
	 * 
	 * @author: duongdt3
	 * @since: 09:31:54 7 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void updateCustomerInfo(ActionEvent e) {
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bd = (Bundle) e.viewData;
		CustomerDTO dto = (CustomerDTO) bd.getSerializable(IntentConstants.INTENT_CUSTOMER);
		try {
			dto.setType(TableType.CUSTOMER);
			returnCode = SQLUtils.getInstance().updateCustomer(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateUpdateCustomerSql();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu customer không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lưu customer không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Xóa khách hàng do staff tạo
	 * 
	 * @author: duongdt3
	 * @since: 14:25:56 7 Jan 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void deleteCustomerInfo(ActionEvent e) {
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bd = (Bundle) e.viewData;
		long customerId = bd.getLong(IntentConstants.INTENT_CUSTOMER_ID);
		try {
			CustomerDTO dto = SQLUtils.getInstance().getCustomerByIdForCreateCustomer(String.valueOf(customerId));
			dto.setType(TableType.CUSTOMER);
			returnCode = SQLUtils.getInstance().deleteCustomer(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateDeleteCustomerSql();
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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());
				sendHttpRequestOffline("queryController/executeSql", para, e);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu customer không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lưu customer không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach khach hang de cap nhat danh sach ban doi thu
	 * 
	 * @author: dungdq3
	 * @since: 10:23:45 AM Mar 7, 2014
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getListCustomerForOpponentSale(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle bundle = (Bundle) e.viewData;
		try {
			long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
			long shopId = bundle.getLong(IntentConstants.INTENT_SHOP_ID);
			String dataSearch = bundle.getString(IntentConstants.INTENT_DATA);
			int page = bundle.getInt(IntentConstants.INTENT_PAGE);
			String visit_plan = bundle.getString(IntentConstants.INTENT_VISIT_PLAN);
			boolean isGetWrongPlan = bundle.getBoolean(IntentConstants.INTENT_GET_WRONG_PLAN);
			boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
			int type = bundle.getInt(IntentConstants.INTENT_ROLE_TYPE);
			int typeRequest = bundle.getInt(IntentConstants.INTENT_TYPE_GET_CUSTOMER);
			long cusId = bundle.getLong(IntentConstants.INTENT_CUSTOMER_ID);

			try {
				CustomerListDTO dto = SQLUtils.getInstance().getListCustomerForOpponentSale(staffId, shopId, cusId, dataSearch, visit_plan, isGetWrongPlan, page, isGetTotalPage, type, typeRequest);

				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					StringBuffer buff = new StringBuffer();
					buff.append("staffId: ");
					buff.append(staffId);
					buff.append("/shopId: ");
					buff.append(shopId);
					buff.append("/name: ");
					buff.append(dataSearch);
					buff.append("/visit_plan: ");
					buff.append(visit_plan);
					buff.append("/isGetWrongPlan: ");
					buff.append(isGetWrongPlan);
					ServerLogger.sendLog("method getListCustomer: ", buff.toString(), TabletActionLogDTO.LOG_CLIENT);
					model.setIsSendLog(false);
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				ServerLogger.sendLog("method getListCustomer1: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("method getListCustomer2: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {
		}
	}

	/**
	 * Lay thong tin chung CT HTBH
	 * 
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getSaleSupportProgramInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			long programId = bundle.getLong(IntentConstants.INTENT_ID);
			SaleSupportProgramDetailModel result = SQLUtils.getInstance().getSaleSupportProgramDetail(programId);

			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getSaleSupportProgrameInfo1 error: ", "sql get detail info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getSaleSupportProgrameInfo2 error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getSaleSupportProgramLevel(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			long programId = bundle.getLong(IntentConstants.INTENT_ID);
			int numLevel = SQLUtils.getInstance().getSaleSupportProgramLevel(programId);
			Bundle b = new Bundle();
			b.putInt(IntentConstants.INTENT_VALUE, numLevel);

			model.setModelData(b);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			SaleController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getSaleSupportProgrameInfo2 error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay ds KH tham gia CT HTBH
	 * 
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getCustomerListAttendProgram(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			CustomerAttendProgramListDTO result = SQLUtils.getInstance().getCustomerListAttendProgram(bundle);

			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getCustomerListAttendProgram1 error: ", "sql get detail info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getCustomerListAttendProgram2 error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach khach hang c2 trong tuyen cua nhan vien ban hang
	 * 
	 * @author: quangvt1
	 * @since: 17:40:35 05-05-2014
	 * @return: void
	 * @throws:
	 * @param actionEvent
	 */
	public void getListC2Customer(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		Bundle bundle = (Bundle) actionEvent.viewData;
		try {
			long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
			long shopId = bundle.getLong(IntentConstants.INTENT_SHOP_ID);
			int page = bundle.getInt(IntentConstants.INTENT_PAGE);
			boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);

			try {
				CustomerListDTO dto = SQLUtils.getInstance().getListC2CustomerOnPlan(staffId, shopId, page, isGetTotalPage);

				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					StringBuffer buff = new StringBuffer();
					buff.append("staffId: ");
					buff.append(staffId);
					buff.append("/shopId: ");
					buff.append(shopId);
					ServerLogger.sendLog("method getListC2Customer: ", buff.toString(), TabletActionLogDTO.LOG_CLIENT);
					model.setIsSendLog(false);
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception e) {
				ServerLogger.sendLog("method getListC2Customer1: ", e.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(e.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("method getListC2Customer2: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {
		}
	}

	/**
	 * Lay thong tin doi thu canh tranh
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public void getInformationCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String cusId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String date = data.getString(IntentConstants.INTENT_DATE);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);

		try {
			ProductCompetitorListDTO dto = SQLUtils.getInstance().getInformationCompetitor(staffId, cusId, date);

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
	 * Lay thong tin cua BSG
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public void getInformationBSG(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String cusId = data.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String date = data.getString(IntentConstants.INTENT_DATE);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);

		try {
			ProductCompetitorListDTO dto = SQLUtils.getInstance().getInformationBSG(staffId, cusId, date);

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
	 * Luu danh sach san pham ban cua doi thu
	 * 
	 * @author: dungdq3
	 * @param: ActionEvent e
	 * @return: void
	 */
	public void saveSaleCompetitor(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle b = (Bundle) e.viewData;

		long cusID = Long.parseLong(b.getString(IntentConstants.INTENT_CUSTOMER_ID));
		long staffID = b.getLong(IntentConstants.INTENT_STAFF_ID);
		String staffCode = b.getString(IntentConstants.INTENT_STAFF_CODE);
		String date = b.getString(IntentConstants.INTENT_DATE);
		int type = b.getInt(IntentConstants.INTENT_TYPE); // 1 - BSG, 0- bia doi												  // thu
		ProductCompetitorListDTO listProductCompetitor = (ProductCompetitorListDTO) b.getSerializable(IntentConstants.INTENT_OP_SALE_VOLUME_LIST);

		try {
			long success = SQLUtils.getInstance().saveSaleCompetitor(staffID, staffCode, cusID, date, type, listProductCompetitor);

			if (success > 0) {
				JSONArray jarr = new JSONArray();
				for (ProductCompetitorDTO productCompetitor : listProductCompetitor.getArrProductCompetitor()) {
					for (OpProductDTO opProduct : productCompetitor.getArrProduct()) {
						JSONObject sqlPara = opProduct.generateListProductCompetitor(staffID, cusID, type, staffCode, date);
						if(sqlPara != null) {
							jarr.put(sqlPara);
						}
						sqlPara = opProduct.generateListProductPriceCompetitor(staffID, cusID, type, staffCode, date);
						if(sqlPara != null) {
							jarr.put(sqlPara);
						}
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
	 * Lay danh sach khach hang can add tham gia chuong trinh htbh
	 * 
	 * @author: hoanpd1
	 * @since: 11:36:38 16-05-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getAddCustomerList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			CustomerAttendProgramListDTO result = SQLUtils.getInstance().getAddCustomerList(bundle);

			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getCustomerListAttendProgram1 error: ", "sql get detail info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getCustomerListAttendProgram2 error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}
	
	
	/**
	 * Luu danh sach khach hang duoc chon tham gia chuong trinh HTBH
	 * 
	 * @author: HoanPD1
	 * @param: ActionEvent e
	 * @return: void
	 */
	public void saveCustomerAttend(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		CustomerAttendProgramListDTO dto = (CustomerAttendProgramListDTO)bundle.getSerializable(IntentConstants.INTENT_CUSTOMER_OBJECT);

		try {
			ProCusAttendDTO success = SQLUtils.getInstance().saveCustomerAttend(dto);

			if (success !=null) {
				JSONArray jarr = new JSONArray();
				for (ProCusMapDTO item : success.lisCusMapDto) {
						JSONObject sqlPara = item.generateCusMapSql();
						jarr.put(sqlPara);
				}
				for (ProCusHistoryDTO item : success.listCusHisDto) {
					JSONObject sqlPara = item.generateProCusHistorySql();
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
	 * Xoa khach hang tham gia CTHTBH
	 * Xoa 2 bang : PRO_CUS_MAP_DETAIL & PRO_CUS_MAP
	 * @author: quangvt1
	 * @since: 15:07:35 19-05-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void deleteCustomerJoinHTBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String pro_cus_map_id = data.getString(IntentConstants.INTENT_ID);
		try { 
			boolean isSuccess = SQLUtils.getInstance().deleteCustomerJoinHTBH(pro_cus_map_id);

			// send to server
			if (isSuccess) {
				CustomerAttendProgramListItem item = new CustomerAttendProgramListItem();
				JSONArray jarr = new JSONArray();
				
				// Gen cau sql delete cac dong cua bang Pro_cus_map_detail
				JSONObject sqlProCusMapDetail = item.generateDeleteProCusMapDetailSql(pro_cus_map_id);
				jarr.put(sqlProCusMapDetail);
				// Gen cau sql delete pro_cus_history
				JSONObject sqlHistory = item.generateDeleteHistorySql(pro_cus_map_id);
				jarr.put(sqlHistory); 
				// Gen cau sql delete pro_cus_map
				JSONObject sqlProCusMap = item.generateDeleteProCusMapSql(pro_cus_map_id);
				jarr.put(sqlProCusMap);
				
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
				
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Xóa thông tin khách hàng tham gia CTHTBH không thành công. Kiểm tra lại dữ liệu.");
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Xóa thông tin khách hàng tham gia CTHTBH không thành công. Kiểm tra lại dữ liệu.");
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach san pham de dang ki san luong tham gia
	 * @author: quangvt1
	 * @since: 16:43:42 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getListProductForJoin(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			long pro_info_id = bundle.getLong(IntentConstants.INTENT_PROGRAM_ID);
			long customer_id  = bundle.getLong(IntentConstants.INTENT_CUSTOMER_ID);
			String level	 = bundle.getString(IntentConstants.INTENT_LEVEL); 

			ListProductQuantityJoin result = SQLUtils.getInstance().getListProductForJoin(pro_info_id, customer_id, level);
			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListProductForJoin error: ", "sql get detail info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListProductForJoin error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Luu thong tin dang ki san luong tham gia
	 * @author: quangvt1
	 * @since: 16:24:27 21-05-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void saveJoinProduct(ActionEvent e) { 
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle data = (Bundle) e.viewData; 
		boolean isChangeLevel = data.getBoolean(IntentConstants.INTENT_CHANGE_LEVEL); 
		ListProductQuantityJoin productList = (ListProductQuantityJoin) data.get(IntentConstants.INTENT_PRODUCT_LIST); 
		int newLevel =  data.getInt(IntentConstants.INTENT_NEW_LEVEL);
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String staffCode = data.getString(IntentConstants.INTENT_STAFF_CODE);

		try {
			boolean success = SQLUtils.getInstance().saveJoinProduct(data);

			if (success) { 
				JSONArray jarr = productList.getJsonUpdate(isChangeLevel, newLevel, staffId, staffCode);
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
				model.setModelMessage(StringUtil
						.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}
	 
	/**
	 * Luu thong tin san luong ban CT HTBH
	 * @author: quangvt1
	 * @since: 14:44:06 19-06-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void saveDoneProduct(ActionEvent e) { 
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		
		Bundle data = (Bundle) e.viewData;  
		CustomerInputQuantityDTO customer = (CustomerInputQuantityDTO) data.get(IntentConstants.INTENT_CUSTOMER_OBJECT); 
		String staffId   = data.getString(IntentConstants.INTENT_STAFF_ID);
		String staffCode = data.getString(IntentConstants.INTENT_STAFF_CODE);
		
		try {
			boolean success = SQLUtils.getInstance().saveDoneProduct(customer, staffId, staffCode);
			
			if (success) { 
				JSONArray jarr = customer.getJsonUpdate(staffId, staffCode);
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
				model.setModelMessage(StringUtil
						.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay thong tin chi tiet cua chuong trinh
	 * @author: quangvt1
	 * @since: 14:12:56 23-05-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getDetailPrograme(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			long proInfoId = bundle.getLong(IntentConstants.INTENT_ID); 

			ProInfoDTO result = SQLUtils.getInstance().getDetailPrograme(proInfoId);
			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getDeailPrograme error: ", "sql get detail info error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getDeailPrograme error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}
	
	/**
	 * Lay thong tin chi tiet cua chuong trinh
	 * @author: quangvt1
	 * @since: 14:12:56 23-05-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getDetailProgrameDone(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle bundle = (Bundle) e.viewData;
			long proInfoId = bundle.getLong(IntentConstants.INTENT_ID); 
			
			ProInfoDTO result = SQLUtils.getInstance().getDetailProgrameDone(proInfoId);
			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getDetailProgrameDone error: ", "sql getDetailProgrameDone error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
			
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getDetailProgrameDone error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay dnah sach cac chuong trinh can nhap san luong ban
	 * @author: quangvt1
	 * @since: 08:12:56 12-06-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getAllProgrameNeedTypeQuantity(ActionEvent e) { 
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try { 
			String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;

			SaleSupportProgramModel result = SQLUtils.getInstance().getAllProgrameNeedTypeQuantity(shopId);
			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getAllProgrameNeedTypeQuantity error: ", "sql getAllProgrameNeedTypeQuantity error : null", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getAllProgrameNeedTypeQuantity error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach khach hang xong 1 chu ki tinh thuong
	 * Lay ve de tien hanh nhap san luong ban cho chu ki do.
	 * @author: quangvt1
	 * @since: 15:10:06 13-06-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getListCustomerDonePrograme(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		
		Bundle data = (Bundle) e.viewData;
		
		try {  
			
			CustomerListDoneProgrameDTO result = SQLUtils.getInstance().getListCustomerDonePrograme(data);
			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListCustomerDonePrograme error: ", "sql getListCustomerDonePrograme error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListCustomerDonePrograme error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	} 
	
	/**
	 * lay sanh sach san pham can nhap san luong thuc hien cua khach hang
	 * 
	 * @author: hoanpd1
	 * @since: 08:56:44 11-08-2014
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getListProductDonePrograme(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		try {  
			CustomerInputQuantityDTO result = SQLUtils.getInstance().getListProductDonePrograme(data);
			if (result != null) {
				model.setModelData(result);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListCustomerDonePrograme error: ", "sql getListCustomerDonePrograme error", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListCustomerDonePrograme error: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lấy danh sách khách hàng màn hình huấn luyện GSNPP, GSBH
	 * @param e
     */
	public void requestGetCustomerSaleListTraining(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		try {
			String staffId = bundle.getString(IntentConstants.INTENT_STAFF_ID);
			String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
			String code = bundle.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			String nameAddress = bundle.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			int page = bundle.getInt(IntentConstants.INTENT_PAGE);
			String visitPlan = bundle.getString(IntentConstants.INTENT_VISIT_PLAN);
			String ownerId = bundle.getString(IntentConstants.INTENT_USER_ID);
			String shopIdParent = bundle.getString(IntentConstants.INTENT_PARENT_SHOP_ID);
			boolean isAll = bundle.getBoolean(IntentConstants.INTENT_IS_ALL);
			String id = bundle.getString(IntentConstants.INTENT_ID);
			try {
				CustomerListDTO dto = SQLUtils.getInstance().requestGetCustomerSaleListTraing(id, shopIdParent, ownerId, shopId, staffId, code, nameAddress, visitPlan, page, isAll);
				if (dto != null) {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
					SaleController.getInstance().handleModelEvent(model);
				} else {
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception ex) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(ex.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
		} finally {
		}

	}
}
