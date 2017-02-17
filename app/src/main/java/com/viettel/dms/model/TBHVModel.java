/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackTBHVDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.db.TrainingResultDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CabinetStaffDTO;
import com.viettel.dms.dto.view.ComboboxDisplayProgrameDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.GSNPPTakeAttendaceDTO;
import com.viettel.dms.dto.view.GSTGeneralStatisticsViewDTO;
import com.viettel.dms.dto.view.ImageListDTO;
import com.viettel.dms.dto.view.IntroduceProductDTO;
import com.viettel.dms.dto.view.ListProductDTO;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ReportNVBHVisitCustomerDTO;
import com.viettel.dms.dto.view.ReportProgressMonthViewDTO;
import com.viettel.dms.dto.view.TBHVAddRequirementViewDTO;
import com.viettel.dms.dto.view.TBHVAttendanceDTO;
import com.viettel.dms.dto.view.TBHVCustomerListDTO;
import com.viettel.dms.dto.view.TBHVCustomerNotPSDSReportDTO;
import com.viettel.dms.dto.view.TBHVDisProComProgReportDTO;
import com.viettel.dms.dto.view.TBHVDisProComProgReportNPPDTO;
import com.viettel.dms.dto.view.TBHVDisplayProgrameModel;
import com.viettel.dms.dto.view.TBHVFollowProblemDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
import com.viettel.dms.dto.view.TBHVManagerEquipmentDTO;
import com.viettel.dms.dto.view.TBHVNoPSDSDetailDTO;
import com.viettel.dms.dto.view.TBHVProgReportProDispDetailSaleDTO;
import com.viettel.dms.dto.view.TBHVProgressDateReportDTO;
import com.viettel.dms.dto.view.TBHVProgressReportSalesFocusViewDTO;
import com.viettel.dms.dto.view.TBHVPromotionProgrameDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO;
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO;
import com.viettel.dms.dto.view.VisitDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.FEED_BACK_TABLE;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
import com.viettel.viettellib.json.me.JSONArray;
import com.viettel.viettellib.json.me.JSONObject;
import com.viettel.viettellib.network.http.HTTPMessage;
import com.viettel.viettellib.network.http.HTTPRequest;
import com.viettel.viettellib.network.http.HTTPResponse;

/**
 * Tang xu ly model cua TBHV
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class TBHVModel extends AbstractModelService {
	protected static TBHVModel instance;

	protected TBHVModel() {
	}

	public static TBHVModel getInstance() {
		if (instance == null) {
			instance = new TBHVModel();
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
		if (StringUtil.isNullOrEmpty((String) mes.getDataText())) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
			return;
		}
		switch (mes.getAction()) {
		default:
			int errCodeDefault = ErrorConstants.ERROR_COMMON;
			try {
				JSONObject json = new JSONObject((String) mes.getDataText());
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
					// UserController.getInstance().handleModelEvent(model);
				} else if (errCodeDefault == ErrorConstants.ERROR_UNIQUE_CONTRAINTS) {
					// request loi trung khoa -- khong thuc hien goi lai len
					// server nua
					updateLog(actionEvent, LogDTO.STATE_UNIQUE_CONTRAINTS);
				} else {
					// ghi log loi len server
					updateLog(actionEvent, LogDTO.STATE_FAIL);
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

		if (actionEvent.action == ActionEventConstant.ACTION_LOGIN) {
			GlobalInfo.getInstance().getProfile().getUserData().setLoginState(UserDTO.NOT_LOGIN);
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			model.setModelMessage(response.getErrMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		} else if (GlobalUtil.checkActionSave(actionEvent.action) && actionEvent.logData != null) {
			// xu ly chung cho cac request
			LogDTO log = (LogDTO) actionEvent.logData;
			if (LogDTO.STATE_NONE.equals(log.state)) {
				updateLog(actionEvent, LogDTO.STATE_NEW);
				// log.state = LogDTO.STATE_NEW;
				// SQLUtils.getInstance().insert(log);
			} else {
				// log.state = LogDTO.STATE_FAIL;
				// SQLUtils.getInstance().update(log);
				updateLog(actionEvent, LogDTO.STATE_FAIL);
			}
			// luu thanh cong
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			TBHVController.getInstance().handleModelEvent(model);
		} else if (actionEvent.action == ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER) {
			model.setModelData(ErrorConstants.ERROR_NO_CONNECTION);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			model.setModelMessage(response.getErrMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * Lay ds thiet bi theo NVBH cua NPP
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListEquipment(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		TBHVManagerEquipmentDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getTBHVListEquipment(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sách");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * requestListRouteSupervision
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestListRouteSupervision(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		Bundle bundle = (Bundle) e.viewData;
		long staff_id = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		int day = bundle.getInt(IntentConstants.INTENT_DAY_OF_WEEK);
		TBHVRouteSupervisionDTO dto;
		try {
			dto = SQLUtils.getInstance().getTBHVListRouteSupervision(staff_id, shopId, day);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sách");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach khuyen mai
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */

	public void requestGetListPromotionPrograme(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle bundle = (Bundle) event.viewData;
		TBHVPromotionProgrameDTO result = null;
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		boolean checkLoadMore = bundle.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);

		try {
			result = SQLUtils.getInstance().getTBHVListPromotionPrograme(shopId, page, checkLoadMore);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds CTKM thất bại");
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay ds chuong trinh khuyen mai cua TBHV
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	public void requestGetListDisplayPrograme(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle ext = (Bundle) event.viewData;
		boolean checkRequestCombobox = ext.getBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, false);
		TBHVDisplayProgrameModel result = null;
		try {
			result = SQLUtils.getInstance().getListTBHVDisplayPrograme(ext);
			if (checkRequestCombobox) {
				ComboboxDisplayProgrameDTO comboboxModel = new ComboboxDisplayProgrameDTO();
				try {
					// comboboxModel.listTypePrograme = SQLUtils.getInstance()
					// .getListTypeDisplayPrograme();
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
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds CTTB thất bại");
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * HieuNH lay danh sach bao cao ngay cua TBHV
	 * 
	 * @param e
	 */
	public void getTBHVProgressDateReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		TBHVProgressDateReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getTBHVProgressDateReport(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * HieuNH lay danh sach not PSDS cua TBHV
	 * 
	 * @param e
	 */
	public void getTBHVNotPSDSReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		TBHVCustomerNotPSDSReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getTBHVNotPSDSReport(staffOwnerId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * HieuNH lay danh sach bao cao ngay cua NVGS cua NPP thuoc TBHV
	 * 
	 * @param e
	 */
	public void getTBHVProgressDateDetailReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		TBHVProgressDateReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getTBHVProgressDateDetailReport(shopId, staffOwnerId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * HieuNH lay danh sach Nha phan phoi
	 * 
	 * @param e
	 */
	public void getListNPP(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		ArrayList<ShopDTO> dto = null;
		try {
			dto = SQLUtils.getInstance().getListNPP(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * HieuNH lay danh sach NVGS of report date
	 * 
	 * @param e
	 */
	public void getListNVGSOfTBHVReportDate(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		ListStaffDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListNVGSOfTBHVReportDate(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * HieuNH lay danh sach NVGS
	 * 
	 * @param e
	 */
	public void getListNVGSOfTBHVReportPSDS(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		ListStaffDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListNVGSOfTBHVReportPSDS(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * lay danh sach bao cao tien do chung CTTB ngay (TBHV)
	 * 
	 * @author: HoanPD
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getTBHVDisProComProReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			TBHVDisProComProgReportDTO dto = SQLUtils.getInstance().getTBHVDisProComProReport((Bundle) e.viewData);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);

				model.setIsSendLog(false);
				ServerLogger.sendLog("method getTBHVDisProComProReport error: ",
						"sql get report progress cttb for tbhv of module tbhv error", TabletActionLogDTO.LOG_CLIENT);

				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());

			model.setIsSendLog(false);
			ServerLogger.sendLog("method getTBHVDisProComProReport error: ",
					"sql get report progress cttb for tbhv of module tbhv error", TabletActionLogDTO.LOG_CLIENT);

			TBHVController.getInstance().handleErrorModelEvent(model);
		}

		finally {
		}

	}

	/**
	 * 
	 * lay danh sach: bao cao tien do chung CTTB ngay (TBHV)
	 * 
	 * @author: HoanPD
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getTBHVStaffDisProComProReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		try {
			TBHVDisProComProgReportNPPDTO dto = null;
			dto = SQLUtils.getInstance().getTBHVDisProComProReportNPP(viewInfo);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");

				model.setIsSendLog(false);
				ServerLogger.sendLog("method getTBHVStaffDisProComProReport error: ",
						"sql get report progress cttb for npp of module tbhv error", TabletActionLogDTO.LOG_CLIENT);

				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());

			model.setIsSendLog(false);
			ServerLogger.sendLog("method getTBHVStaffDisProComProReport error: ",
					"sql get report progress cttb for npp of module tbhv error", TabletActionLogDTO.LOG_CLIENT);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * Lay ds sp cua TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getTBHVListProductStorage(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		ListProductDTO result = null;
		try {
			result = SQLUtils.getInstance().getTBHVListProductStorage(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds sản phẩm thất bại");
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay thong tin san pham TBHV
	 * @author: Nguyen Thanh Dung
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
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy thông tin giới thiệu sản phẩm thất bại");
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get report progress in month
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getReportProgressInMonthTBHV(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		ReportProgressMonthViewDTO result = new ReportProgressMonthViewDTO();
		try {
			result = SQLUtils.getInstance().getReportProgressInMonthTBHV(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getReportProgressInMonthTBHV error: ",
					"sql get report progress in month error", TabletActionLogDTO.LOG_CLIENT);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get Gsnpp Training Plan
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void getTbhvTrainingPlan(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		long staffId = data.getLong(IntentConstants.INTENT_STAFF_ID);
		long shopId = data.getLong(IntentConstants.INTENT_SHOP_ID);
		try {
			TBHVTrainingPlanDTO dto = SQLUtils.getInstance().getTbhvTrainingPlan(staffId, shopId);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			TBHVController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * Lấy danh sách loại vấn đề GSNPP/TTTT cua GST
	 * 
	 * @author: YenNTH
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getFollowlistProblem(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		TBHVFollowProblemDTO result = new TBHVFollowProblemDTO();
		try {
			Bundle data = (Bundle) event.viewData;
			result = SQLUtils.getInstance().getTBHVListFollowProblem(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds vấn đề thất bại");
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * Update trang thai cua van de GST
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void updateFollowProblemDone(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) e.viewData;
		try {
			returnCode = SQLUtils.getInstance().updateTBHVFollowProblemDone(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateUpdateFollowProblemSql(dto);
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
				TBHVController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("tbhv problem id: " + dto.id, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update training shop manager result không thành công. Kiểm tra lại dữ liệu.");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * HieuNH delete van de cua tbhv
	 * 
	 * @param e
	 */
	public void deleteFollowProblemDone(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) e.viewData;
		try {
			returnCode = SQLUtils.getInstance().deleteTBHVFollowProblemDone(dto);

			// send to server
			if (returnCode != -1) {
				JSONObject sqlPara = dto.generateDeleteFollowProblemSql(dto);
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
				TBHVController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("tbhv problem id: " + dto.id, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update feedback không thành công. Kiểm tra lại dữ liệu.");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * getDayTrainingSupervision
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void getDayTrainingSupervision(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		long trainDetailId = data.getLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID);
		long shopId = data.getLong(IntentConstants.INTENT_SHOP_ID);
		try {
			TBHVTrainingPlanDayResultReportDTO dto = SQLUtils.getInstance().getDayTrainingSupervision(trainDetailId, shopId);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			TBHVController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * getHistoryPlanTraining
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void getPlanTrainingHistoryAcc(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		long staffId = data.getLong(IntentConstants.INTENT_STAFF_ID);
		long gsnppStaffId = data.getLong(IntentConstants.INTENT_GSNPP_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		try {
			TBHVTrainingPlanHistoryAccDTO dto = SQLUtils.getInstance().getPlanTrainingHistoryAcc(staffId, gsnppStaffId,
					shopId);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			TBHVController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * get report progress in month for module TBHV detail NPP screen
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getReportProgressInMonthTBHVDetailNPP(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		ReportProgressMonthViewDTO result = new ReportProgressMonthViewDTO();
		try {
			result = SQLUtils.getInstance().getReportProgressInMonthTBHVDetailNPP(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getReportProgressInMonthTBHVDetailNPP error: ",
					"sql get report progress in month detal NPP error", TabletActionLogDTO.LOG_CLIENT);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * HieuNH lay danh sach NVBH
	 * 
	 * @param e
	 */
	public void getListNVBHNotPSDS(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		try {
			ListStaffDTO dto = SQLUtils.getInstance().getListNVBHNotPSDS(shopId, staffOwnerId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * trungnt56 lay danh sach staff
	 *
	 * @param e
	 */
	public void getListStaffForGSBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		boolean orderStaffCode = viewInfo.getBoolean(IntentConstants.INTENT_ORDER);
		boolean isHaveAll = viewInfo.getBoolean(IntentConstants.INTENT_IS_ALL);
		try {
			ListStaffDTO dto;
			dto = SQLUtils.getInstance().getListStaffForGSBHOrderCode(Long.parseLong(staffOwnerId), isHaveAll);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Danh sach hinh anh khach hang GSBH
	 *
	 * @author: trungnt56
	 * @since: 1:21:11 PM Dec 12, 2013
	 * @return: void
	 * @throws:
	 * @param e
	 */
	public void getGsbhImageList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		try {
			ImageListDTO dto = SQLUtils.getInstance().getGSBHImageList(data);

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
	 * HieuNH lay danh sach NVBH
	 * @param e
	 */
	public void getListNVBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		try {
			ListStaffDTO dto = SQLUtils.getInstance().getListNVBH(shopId, staffOwnerId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get report progress sales focus infor
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getReportProgressSalesFocusInfo(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		TBHVProgressReportSalesFocusViewDTO result = new TBHVProgressReportSalesFocusViewDTO();
		try {
			result = SQLUtils.getInstance().getReportProgressTBHVSalesFocusInfo(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getReportProgressSalesFocusInfo error: ",
					"sql get report progress sales focus info error", TabletActionLogDTO.LOG_CLIENT);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * send request get report progress sales focus detail info view
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getReportProgressSalesFocusDetailInfo(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		TBHVProgressReportSalesFocusViewDTO result = new TBHVProgressReportSalesFocusViewDTO();
		try {
			result = SQLUtils.getInstance().getReportProgressTBHVSalesFocusDetailInfo(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getReportProgressSalesFocusDetailInfo error: ",
					"sql get report progress sales focus detail info error", TabletActionLogDTO.LOG_CLIENT);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * send request get report progress sales focus detail info view
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getTBHVReviewsGSNPPDetailInfo(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		List<FeedBackTBHVDTO> listTrainingReviews = new ArrayList<FeedBackTBHVDTO>();
		try {
			listTrainingReviews = SQLUtils.getInstance().getTrainingReviewsGSNPPOfTBHVInfo(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			listTrainingReviews = null;
		}
		if (listTrainingReviews != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(listTrainingReviews);
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getTBHVReviewsGSNPPDetailInfo error: ", "sql get reveiws gsnpp of tbhv error",
					TabletActionLogDTO.LOG_CLIENT);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * tbhv update training reviews to db local and db server
	 * 
	 * @author: HaiTC3
	 * @param actionEvent
	 * @return
	 * @return: HTTPRequest
	 * @throws:
	 */
	public HTTPRequest tbhvUpdateTrainingReviewsToDBAndServer(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		HTTPRequest re = null;
		try {
			int result = 0;
			Bundle data = (Bundle) actionEvent.viewData;
			@SuppressWarnings("unchecked")
			List<FeedBackTBHVDTO> listTrainingReviews = (List<FeedBackTBHVDTO>) data
					.getSerializable(IntentConstants.INTENT_TBHV_LIST_TRAINING_SHOP_OBJECT);

			listTrainingReviews = SQLUtils.getInstance().tbhvUpdateIdForTrainingResult(listTrainingReviews);

			result = SQLUtils.getInstance().tbhvInsertAndUpdateReviewsStaffDTO(listTrainingReviews);

			if (result == 1) {
				JSONArray listSql = this.generalSQLInsertAndUpdateTrainingReviewsToServer(listTrainingReviews);
				String logId = GlobalUtil.generateLogId();
				actionEvent.userData = result;
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
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu đánh giá không thành công. Vui lòng kiểm tra lại dữ liệu");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method tbhvUpdateTrainingReviewsToDBAndServer error: ",
						"sql update training reviews to db and server error", TabletActionLogDTO.LOG_CLIENT);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method tbhvUpdateTrainingReviewsToDBAndServer error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * 
	 * general sql insert and update/delete training reviews of tbhv
	 * 
	 * @author: HaiTC3
	 * @param listTrainingReviews
	 * @return
	 * @return: JSONArray
	 * @throws:
	 */
	public JSONArray generalSQLInsertAndUpdateTrainingReviewsToServer(List<FeedBackTBHVDTO> listTrainingReviews) {
		JSONArray kq = new JSONArray();

		// general SQL for reviews info. insert new reviews and update content
		// for old reviews
		for (int i = 0, size = listTrainingReviews.size(); i < size; i++) {
			FeedBackTBHVDTO objectData = listTrainingReviews.get(i);
			// new reviews
			if (objectData.currentState == TrainingResultDTO.STATE_NEW_INSERT) {
				kq.put(objectData.feedBackBasic.generateFeedbackSql());
			}
			// update reviews
			else if (objectData.currentState == TrainingResultDTO.STATE_NEW_UPDATE) {
				kq.put(objectData.feedBackBasic.generateUpdateContentFeedbackSql());
			}
			// delete reviews
			else if (objectData.currentState == TrainingResultDTO.STATE_DELETED) {
				kq.put(objectData.feedBackBasic.generalSqlDeleteFeedBackOutOfDB());
			}
		}
		return kq;
	}

	/**
	 * HieuNH lay danh sach bao cao khach hang chua psds trong thang
	 * 
	 * @param e
	 */
	public void getCountCustomerNotPsdsInMonthReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		String visit_plan = viewInfo.getString(IntentConstants.INTENT_VISIT_PLAN);
		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID);
		try {
			int count = 0;
			count = SQLUtils.getInstance().getCountCustomerNotPsdsInMonthReport(shopId, staffOwnerId, visit_plan,
					staffId);
			if (count >= 0) {
				model.setModelData(Integer.valueOf(count));
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(count);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}


	/**
	 * HieuNH lay danh sach thiet bi TBHV
	 * 
	 * @param e
	 */
	public void getListEquipmentTBHV(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long parentShopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_PARENT_SHOP_ID));
		TBHVManagerEquipmentDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListEquipmentTBHV(parentShopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Cap nhat duong dan hinh anh duoi tablet cho sp
	 * @author: Nguyen Thanh Dung
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
			TBHVController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Cập nhật url thumbnail bị lỗi");
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * lay danh sach bao cao tien do CTTB chi tiet NVBH ngay ( TBHV)
	 * 
	 * @author: HoanPD1
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void getTBHVProgReportProDispDetailSale(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		String displayProgrameCode = bundle.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
		String displayProgrameLevel = null;
		int checkAll = bundle.getInt(IntentConstants.INTENT_ID);
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		TBHVProgReportProDispDetailSaleDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getTBHVProgReportProDispDetailSaleDTO(staffId, displayProgrameCode,
					displayProgrameLevel, checkAll, page);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * Lay so luong thiet bi NVBH quan ly
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getCountCabinetStaff(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		int isAll = Integer.parseInt(viewInfo.getString(IntentConstants.INTENT_IS_ALL));
		try {
			int count = SQLUtils.getInstance().getCountCabinetStaff(shopId, staffId, isAll);
			if (count != -1) {
				model.setModelData(count);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(count);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sách");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * Lay ds thiet bi NVBH quan ly
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getCabinetStaff(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		int isAll = Integer.parseInt(viewInfo.getString(IntentConstants.INTENT_IS_ALL));
		String page = viewInfo.getString(IntentConstants.INTENT_PAGE);
		CabinetStaffDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getCabinetStaff(shopId, staffId, isAll, page);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sách");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay thong tin so luong ctkm & cttb dang chay
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void getBusinessSupportProgrameInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		Bundle dto = null;
		try {
			dto = SQLUtils.getInstance().getBusinessSupportProgrameInfo(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * lay danh sach khach hang thuoc GSNPP
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getCustomerListForPostFeedback(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		try {
			TBHVCustomerListDTO dto = SQLUtils.getInstance().getCustomerListForPostFeedback(data);

			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		} finally {

		}
	}

	/**
	 * Lay thong tin cho man hinh tao yeu cau: lay GSNPP, loai van de
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getAddRequirementInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		TBHVAddRequirementViewDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getAddRequirementInfo(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Them van de
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest addProblem(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);
		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			FeedBackDTO feedBack = (FeedBackDTO) actionEvent.viewData;
			boolean result = SQLUtils.getInstance().createTBHVFeedback(feedBack);

			if (result) {
				JSONArray listSql = new JSONArray();
				JSONObject insertSql = feedBack.generateFeedbackSql();
				listSql.put(insertSql);

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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				re = sendHttpRequestOffline("queryController/executeSql", para, actionEvent, LogDTO.TYPE_NORMAL,
						String.valueOf(feedBack.feedBackId), FEED_BACK_TABLE.FEED_BACK_TABLE);

				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				// model.setModelMessage("Lưu đơn hàng không thành công. Kiểm tra lại dữ liệu.");
				TBHVController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("feedback id: " + feedBack.feedBackId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu feedback không thành công. Kiểm tra lại dữ liệu.");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog(ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);

			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

		return re;
	}

	/**
	 * Lay danh sach GSBH - MH theo doi vi tri NVBH, GSBH, TTTT role GST
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void getGsnppPosition(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;

		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		TBHVRouteSupervisionDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getGsnppPosition(shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach NVBH - MH theo doi vi tri NVBH, GSBH, TTTT role GST
	 * 
	 * @author: YenNTH
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void getNvbhPosition(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		String staffId = bundle.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		TBHVRouteSupervisionDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getNvbhPosition(staffId, shopId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: voidvoid
	 * @throws:
	 */
	public void getPositionOfGsnppAndNvbh(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		String[] list = bundle.getStringArray(IntentConstants.INTENT_STAFF_LIST);
		try {
			ListStaffDTO listStaff = SQLUtils.getInstance().getPositionOfGsnppAndNvbh(list);
			if (listStaff != null) {
				model.setModelData(listStaff);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(listStaff);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach cac nhan vien ghe tham tren tuyen
	 * 
	 * @author: dungdq3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getVisitCusNotification(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		String staffID= bundle.getString(IntentConstants.INTENT_STAFF_ID);
		try {
			TBHVVisitCustomerNotificationDTO dto = SQLUtils.getInstance().getVisitCusNotification(shopId, staffID);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception e2) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(e2.getMessage());
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * lay thong tin bao cao cham cong ngay cua NPP
	 * 
	 * @param event
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void getTbhvAttendance(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		TBHVAttendanceDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getTBHVAttendance(data);
			if (dto != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(dto);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getReportTakeAttendOfDay error: ",
						"sql get report take attend of tbhv error", TabletActionLogDTO.LOG_CLIENT);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getReportTakeAttendOfDay error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay ds cham cong cac nhan vien cua TBHV
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void getListSaleForAttendance(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		GSNPPTakeAttendaceDTO result = null;
		try {
			result = SQLUtils.getInstance().getTBHVListAttendance(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage("Lấy ds sản phẩm thất bại");
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * get list report NVBH visit customer
	 * 
	 * @param e
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void getListReportNVBHVisitCustomer(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		ArrayList<ReportNVBHVisitCustomerDTO> dto = new ArrayList<ReportNVBHVisitCustomerDTO>();
		try {
			dto = SQLUtils.getInstance().getListReportVisitCustomerInDay(b);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				TBHVController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListReportNVBHVisitCustomer error: ",
						"lay d/s bao cao NVBH ghe tham khach hang", TabletActionLogDTO.LOG_CLIENT);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListReportNVBHVisitCustomer error: ",
					"lay d/s bao cao NVBH ghe tham khach hang", TabletActionLogDTO.LOG_EXCEPTION);
			TBHVController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * get general statistics report info (date and month) for GST
	 * @author: duongdt3
	 * @since: 10:00:31 29 Nov 2013
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getGeneralStatisticsReportInfoForGST(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID);
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);

		GSTGeneralStatisticsViewDTO reportInfo = null;
		try {
			reportInfo = SQLUtils.getInstance().getGeneralStatisticsReportForGST(staffId, shopId);
			
			if (reportInfo != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(reportInfo);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getGeneralStatisticsReportInfoForGST error: ",
						"sql get general statistics report error", TabletActionLogDTO.LOG_CLIENT);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getGeneralStatisticsReportInfoForGST error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			TBHVController.getInstance().handleErrorModelEvent(model);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			reportInfo = null;
		}
	}

	/**
	 * Lay danh sach NPP, GSBH, NVBH cua GST
	 * 
	 * @author: QuangVT
	 * @since: 6:13:31 PM Dec 17, 2013
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getListShopSupervisorAndStaff(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String staffId = data.getString(IntentConstants.INTENT_STAFF_ID); // id GST

		TBHVNoPSDSDetailDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListShopSupervisorAndStaff(staffId);
			
			if (dto != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(dto);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListShopSupervisorAndStaff error: ",
						"sql get general statistics report error", TabletActionLogDTO.LOG_CLIENT);
				TBHVController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListShopSupervisorAndStaff error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			TBHVController.getInstance().handleErrorModelEvent(model);
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			dto = null;
		} 
	}
	
	/**
	 * Lay danh sach KH khong PSDS trong thang cho GST
	 * 
	 * @author quangvt1
	 * @param e
	 */
	public void getReportCustomerNotPSDSForTBHV(ActionEvent e) {
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
			dto = SQLUtils.getInstance().getReportCustomerNotPSDSForSupervisor(shopId, dayInWeek, staffId, page, numItemInPage);
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
	 * lay danh sach vieng tham khach hang cua nhan vien ban hang
	 * 
	 * @author: QuangVT
	 * @since: 4:14:59 PM Dec 19, 2013
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getListVisit(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
		long customerId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_CUSTOMER_ID));
		VisitDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListVisit(staffId, customerId);
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
}
