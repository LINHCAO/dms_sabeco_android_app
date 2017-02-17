/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.model;

import java.util.ArrayList;
import java.util.Vector;

import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.StaffCustomerDetailDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.photo.PhotoDTO;
import com.viettel.dms.dto.view.AccSaleProgReportDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.C2PurchaselViewDTO;
import com.viettel.dms.dto.view.C2SaleOrderlViewDTO;
import com.viettel.dms.dto.view.CabinetStaffDTO;
import com.viettel.dms.dto.view.ComboboxDisplayProgrameDTO;
import com.viettel.dms.dto.view.ComboboxFollowProblemDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.CustomerUpdateLocationDTO;
import com.viettel.dms.dto.view.DisProComProgReportDTO;
import com.viettel.dms.dto.view.DisplayProgrameModel;
import com.viettel.dms.dto.view.FollowProblemDTO;
import com.viettel.dms.dto.view.FollowProblemItemDTO;
import com.viettel.dms.dto.view.GSNPPTakeAttendaceDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO;
import com.viettel.dms.dto.view.GsnppLessThan2MinsDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionDTO;
import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO;
import com.viettel.dms.dto.view.HistoryItemDTO;
import com.viettel.dms.dto.view.ImageListDTO;
import com.viettel.dms.dto.view.ListAlbumUserDTO;
import com.viettel.dms.dto.view.ListCustomerAttentProgrameDTO;
import com.viettel.dms.dto.view.ListFindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.ListProductDTO;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ManagerEquipmentDTO;
import com.viettel.dms.dto.view.PhotoThumbnailListDto;
import com.viettel.dms.dto.view.ProgReportProDispDetailSaleDTO;
import com.viettel.dms.dto.view.ProgressDateDetailReportDTO;
import com.viettel.dms.dto.view.ProgressDateReportDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO;
import com.viettel.dms.dto.view.PromotionProgrameModel;
import com.viettel.dms.dto.view.ReportNVBHVisitCustomerDTO;
import com.viettel.dms.dto.view.ReviewsStaffViewDTO;
import com.viettel.dms.dto.view.StaffInfoDTO;
import com.viettel.dms.dto.view.SuperviorTrackAndFixProblemOfGSNPPViewDTO;
import com.viettel.dms.dto.view.VisitDTO;
import com.viettel.dms.dto.view.WrongPlanCustomerDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.C2_PURCHASE_TABLE;
import com.viettel.dms.sqllite.db.C2_SALE_ORDER_TABLE;
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
 * Tang xu ly model cua NVGS
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
@SuppressWarnings("serial")
public class SuperviorModel extends AbstractModelService {
	protected static SuperviorModel instance;

	protected SuperviorModel() {
	}

	public static SuperviorModel getInstance() {
		if (instance == null) {
			instance = new SuperviorModel();
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
			SuperviorController.getInstance().handleErrorModelEvent(model);
			return;
		}
		switch (mes.getAction()) {
		case ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE:
			JSONObject json;
			try {
				json = new JSONObject((String) mes.getDataText());
				JSONObject result = json.getJSONObject("result");
				int errCode = result.getInt("errorCode");
				model.setModelCode(errCode);
				if (errCode == ErrorConstants.ERROR_CODE_SUCCESS) {
					SuperviorController.getInstance().handleModelEvent(model);
				} else {
					model.setModelMessage(result.getString("errorMessage"));
					SuperviorController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Throwable e) {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
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
				// log.state = LogDTO.STATE_NEW;
				// SQLUtils.getInstance().insert(log);
			} else {
				// log.state = LogDTO.STATE_FAIL;
				// SQLUtils.getInstance().update(log);
				updateLog(actionEvent, LogDTO.STATE_FAIL);
			}
			// luu thanh cong
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			model.setModelCode(ErrorConstants.ERROR_NO_CONNECTION);
			model.setModelMessage(response.getErrMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * getListSaleRoadmapSupervisor
	 * @author: TamPQ
	 * @throws Exception
	 * @return:
	 * @throws:
	 */
	public void getListSaleRoadmapSupervisor(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;

		long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		String today = bundle.getString(IntentConstants.INTENT_DAY);
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);

		GsnppRouteSupervisionDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getGsnppRouteSupervision(staffId, today, shopId);
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
	 * 
	 * llay danh sach bao cao tien do ban hang luy ke den ngay
	 * 
	 * @author: HieuNQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getAccSaleProgReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
		String shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
		AccSaleProgReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getAccSaleProgReport(staffId, shopId);
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
	 * 
	 * lay danh sach bao cao tien do ban MHTT den ngay
	 * 
	 * @author: HoanPD1
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getProgRepostSaleFocus(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		// Vector<?> data = (Vector<?>) e.viewData;
		Bundle data = (Bundle) e.viewData;
		{

			// int staffId = Integer.parseInt(data.get(
			// data.lastIndexOf(IntentConstants.INTENT_STAFF_ID) + 1)
			// .toString());
			ProgressReportSalesFocusDTO dto = null;
			try {

				dto = SQLUtils.getInstance().getProgReportSalesFocus(data);
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

	/**
	 * 
	 * lay danh sach bao cao tien do chung CTTB ngay
	 * 
	 * @author: HieuNQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getDisProComProReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long staffId;
		String shopId = "";
		if (viewInfo != null) {
			if (viewInfo.containsKey(IntentConstants.INTENT_SHOP_ID)) {
				shopId = viewInfo.getString(IntentConstants.INTENT_SHOP_ID);
			}
			if (viewInfo.containsKey(IntentConstants.INTENT_STAFF_ID)) {
				staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_STAFF_INFO));
				UserController.getInstance().handleErrorModelEvent(model);
				return;
			}
		} else {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
			UserController.getInstance().handleErrorModelEvent(model);
			return;
		}
		DisProComProgReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getDisProComProReport(staffId, shopId);
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
	 * HieuNH lay danh sach bao cao ngay cua nhan vien giam sat
	 * 
	 * @param e
	 */
	public void getProgressDateReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long staffOwnerId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID));
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		ProgressDateReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getProgressDateReport(staffOwnerId, shopId);
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
	 * HieuNH lay danh sach bao cao ngay cua nhan vien giam sat
	 * 
	 * @param e
	 */
	public void getProgressDateDetailReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
		ProgressDateDetailReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getProgressDateDetailReport(staffId);
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
	 * Lay danh sach vieng tham khach hang cua nhan vien ban hang
	 * 
	 * @author: QuangVT
	 * @since: 4:15:46 PM Dec 19, 2013
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

	/**
	 * HieuNH lay danh sach thiet bi
	 * 
	 * @param e
	 */
	public void getListEquipment(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		long staffOwnerId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID));
		ManagerEquipmentDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListEquipment(staffOwnerId);
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
	 * HieuNH lay count danh sach theo doi tu nhan vien
	 * 
	 * @param e
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
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(count);
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
	 * HieuNH lay danh sach theo doi tu nhan vien
	 * 
	 * @param e
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
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(count);
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
	 * HieuNH lay danh sach NVBH
	 * 
	 * @param e
	 */
	public void getListNVBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		boolean orderStaffCode = viewInfo.getBoolean(IntentConstants.INTENT_ORDER);
		boolean isHaveAll = viewInfo.getBoolean(IntentConstants.INTENT_IS_ALL);
		try {
			ListStaffDTO dto;
			if (!orderStaffCode) {
				dto = SQLUtils.getInstance().getListNVBH(shopId, staffOwnerId);
			} else {
				dto = SQLUtils.getInstance().getListNVBHOrderCode(shopId, staffOwnerId, isHaveAll);
			}
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		} 
	}

	/**
	 * TrungNT lay danh sach NVBH theo GSBH
	 *
	 * @param e
	 */
	public void getListNVBHByGSBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		String staffId = viewInfo.getString(IntentConstants.INTENT_STAFF_ID);
		boolean isHaveAll = viewInfo.getBoolean(IntentConstants.INTENT_IS_ALL);
		try {
			ListStaffDTO dto = null;
			if(StringUtil.isNullOrEmpty(staffId)) {
				dto = SQLUtils.getInstance().getListNVBHByGSBH(staffOwnerId, isHaveAll);
			} else {
				dto = SQLUtils.getInstance().getListNVBHByGSNPP(staffId, isHaveAll, true);
			}
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * TrungNT lay danh sach GSNPP theo GSBH
	 *
	 * @param e
	 */
	public void getListGSNPPByGSBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;

		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		boolean isHaveAll = viewInfo.getBoolean(IntentConstants.INTENT_IS_ALL);
		try {
			ListStaffDTO dto = SQLUtils.getInstance().getListGSNPPByGSBH(staffOwnerId, isHaveAll);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * lay danh sach Báo cáo tiến độ CTTB theo NVBH ngày
	 * 
	 * @author: HieuNQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getStaffDisProComProReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			Bundle viewInfo = (Bundle) e.viewData;
			long staffId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_STAFF_ID));
			long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
			int proId = Integer.parseInt(viewInfo.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_ID));
			DisProComProgReportDTO dto = null;
			dto = SQLUtils.getInstance().getStaffDisProComProReportDTO(staffId, shopId, proId);
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
	 * 
	 * lay danh sach bao cao tien do CTTB chi tiet NVBH ngay
	 * 
	 * @author: HoanPD1
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getProgReportProDispDetailSale(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;

		long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		String displayProgrameCode = bundle.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
		int checkAll = bundle.getInt(IntentConstants.INTENT_ID);
		String page = bundle.getString(IntentConstants.INTENT_PAGE);
		ProgReportProDispDetailSaleDTO dto = null;
		try {
			dto = SQLUtils.getInstance()
					.getProgReportProDispDetailSaleDTO(staffId, displayProgrameCode, checkAll, page);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach khuyen mai
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	public void requestGetListPromotionPrograme(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle bundle = (Bundle) event.viewData;
		PromotionProgrameModel result = null;
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		String ext = bundle.getString(IntentConstants.INTENT_PAGE);
		boolean checkLoadMore = bundle.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);

		try {
			result = SQLUtils.getInstance().getSupervisorListPromotionPrograme(shopId, ext, checkLoadMore);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach khach hang tham gia chuong trinh
	 * 
	 * @author: ThanhNN8
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getListCustomerAttentPrograme(ActionEvent e) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		try {
			// insert to sql Lite & request to server
			Bundle data = (Bundle) e.viewData;
			String extPage = data.getString(IntentConstants.INTENT_PAGE);
			long displayProgrameId = data.getLong(IntentConstants.INTENT_DISPLAY_PROGRAM_ID);
			String displayProgrameCode = data.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
			String customerCode = data.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			String customerName = data.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			long staffId = data.getLong(IntentConstants.INTENT_STAFF_ID);
			boolean checkPagging = data.getBoolean(IntentConstants.INTENT_CHECK_PAGGING, false);
			ListCustomerAttentProgrameDTO results = SQLUtils.getInstance().getListCustomerAttentPrograme(extPage,
					displayProgrameCode, displayProgrameId, customerCode, customerName, staffId, checkPagging);

			model.setModelData(results);
			model.setActionEvent(e);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);

			SuperviorController.getInstance().handleModelEvent(model);

		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay danh sach loai CT va danh sach nganh hang
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	public void getListComboboxDisplayPrograme(ActionEvent event) {
		// TODO Auto-generated method stub
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
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Lay ds chuong trinh khuyen mai
	 * @author: ThanhNN8
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
			result = SQLUtils.getInstance().getListSuperVisorDisplayPrograme(ext);
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
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	public void getAccTrainResultReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		long staffId = b.getLong(IntentConstants.INTENT_STAFF_ID);
		String shopId = b.getString(IntentConstants.INTENT_SHOP_ID);
		GsnppTrainingResultAccReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getAccTrainResultReportDTO(staffId, shopId);
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
	 * getStaffTrainResultReportDTO
	 * @author: HieuNQ
	 * @throws Exception
	 * @return:
	 * @throws:
	 */
	public void getGsnppTrainingResultDayReport(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		long staffTrainId = b.getLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID);
		long shopId = b.getLong(IntentConstants.INTENT_SHOP_ID);
		GSNPPTrainingResultDayReportDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getGsnppTrainingResultDayReport(staffTrainId, shopId);
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
	 * getPlanTrainResultReportDTO
	 * @author: HieuNQ
	 * @throws Exception
	 * @return:
	 * @throws:
	 */
	public void getGsnppTrainingPlan(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		long staffId = b.getLong(IntentConstants.INTENT_STAFF_ID);
		String shopId = b.getString(IntentConstants.INTENT_SHOP_ID);
		GSNPPTrainingPlanDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getGsnppTrainingPlan(staffId, shopId);
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
	 * 
	 * lay thong tin cac danh gia da thuc hien doi voi NVBH tren khach hang
	 * trong ngay
	 * 
	 * @author: HaiTC3
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void getReviewsDoneOfSTAFF(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		ReviewsStaffViewDTO dto = new ReviewsStaffViewDTO();
		try {
			dto = SQLUtils.getInstance().getReviewsInfoOfStaff(b);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getReviewsDoneOfSTAFF error: ", "sql get reviews info of staff error",
						TabletActionLogDTO.LOG_CLIENT);
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getReviewsDoneOfSTAFF error: ", "sql get reviews info of staff error",
					TabletActionLogDTO.LOG_EXCEPTION);
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * cap nhat du lieu xuong DB local, DB server
	 * 
	 * @author: HaiTC3
	 * @param actionEvent
	 * @return: void
	 * @throws:
	 */
	public HTTPRequest updateReviewsToDBAndServer(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);

		HTTPRequest re = null;
		try {
			int result = 0;
			Bundle data = (Bundle) actionEvent.viewData;
			String shop_id = data.getString(IntentConstants.INTENT_SHOP_ID);
			String supperStaff_id = data.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
			String staff_id = data.getString(IntentConstants.INTENT_STAFF_ID);
			// String customer_id = data
			// .getString(IntentConstants.INTENT_CUSTOMER_ID);
			// String trainingDetailId = data
			// .getString(IntentConstants.INTENT_TRAINING_DETAIL_ID);
			ReviewsStaffViewDTO reviewsInfo = (ReviewsStaffViewDTO) data
					.getSerializable(IntentConstants.INTENT_REVIEWS_INFO);

			reviewsInfo = SQLUtils.getInstance().updateIdForTrainingResult(shop_id, supperStaff_id, staff_id,
					reviewsInfo);

			result = SQLUtils.getInstance().insertAndUpdateReviewsStaffDTO(reviewsInfo);
			if (result == 1) {
				JSONArray listSql = reviewsInfo.generalSQLInsertAndUpdateToServer();
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
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu chấm trưng bày không thành công. Kiểm tra lại dữ liệu.");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method updateReviewsToDBAndServer error: ",
						"sql save review info to db local incorrect", TabletActionLogDTO.LOG_CLIENT);
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method updateReviewsToDBAndServer error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
		return re;
	}

	/**
	 * requestStaffInfo
	 * 
	 * @author: TamPQ
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void requestStaffInfo(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		String staffId = bundle.getString(IntentConstants.INTENT_STAFF_ID);
		int page = bundle.getInt(IntentConstants.INTENT_PAGE);
		int isLoadSaleInMonth = bundle.getInt(IntentConstants.INTENT_LOAD_SALE_IN_MONTH);
		int isGetTotalPage = bundle.getInt(IntentConstants.INTENT_GET_TOTAL_PAGE);

		StaffInfoDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getStaffInformation(staffId, page, isGetTotalPage, isLoadSaleInMonth);
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(dto);
			SuperviorController.getInstance().handleModelEvent(model);
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Ham lay danh sach theo doi khac phuc cua GSBH
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void getFollowlistProblem(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		FollowProblemDTO result = new FollowProblemDTO();
		try {
			Bundle data = (Bundle) event.viewData;
			result = SQLUtils.getInstance().getListFollowProblem(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	public void getComboboxFollowlistProblem(ActionEvent event) {
		// TODO Auto-generated method stub
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		ComboboxFollowProblemDTO result = new ComboboxFollowProblemDTO();
		try {
			result = SQLUtils.getInstance().getComboboxListFollowProblem();
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: ThanhNN8
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void updateGSNPPFollowProblemDone(ActionEvent e) {
		// TODO Auto-generated method stub
		long returnCode = -1;
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		FollowProblemItemDTO dto = (FollowProblemItemDTO) e.viewData;
		try {
			returnCode = SQLUtils.getInstance().updateGSNPPFollowProblemDone(dto);

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
				SuperviorController.getInstance().handleModelEvent(model);

			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Update feedback không thành công. Kiểm tra lại dữ liệu.");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}

		} catch (Exception ex) {
		}
	}

	/**
	 * 
	 * lay d/s san pham them vao danh gia NVBH
	 * 
	 * @author: HaiTC3
	 * @param event
	 * @return: void
	 * @throws:
	 */
	public void getListProductToAddReviewsStaff(ActionEvent event) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(event);
		Bundle data = (Bundle) event.viewData;
		ListFindProductSaleOrderDetailViewDTO result = null;
		try {
			result = SQLUtils.getInstance().getListProductToAddReviewsStaff(data);
			if (result != null) {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setModelData(result);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListProductToAddReviewsStaff error: ",
						"sql get list product to add reivews staff error", TabletActionLogDTO.LOG_CLIENT);
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListProductToAddReviewsStaff error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * 
	 * getWrongCustomer
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void getWrongCustomer(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;

		long staffId = b.getLong(IntentConstants.INTENT_STAFF_ID);

		WrongPlanCustomerDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getWrongCustomerList(staffId);
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

	}

	/**
	 * 
	 * getListHistory trong man hinh chi tiet theo doi va giam sat
	 * @author: HieuNH
	 * @return: void
	 * @throws:
	 */
	public void getListHistory(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;

		String staffId = b.getString(IntentConstants.INTENT_STAFF_ID);
		String customerId = b.getString(IntentConstants.INTENT_CUSTOMER_ID);
		String productId = b.getString(IntentConstants.INTENT_PRODUCT_ID);

		Vector<HistoryItemDTO> dto = null;
		try {
			dto = SQLUtils.getInstance().getListHistory(staffId, customerId, productId);
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

	}

	/**
	 * Lay danh sach lich su cap nhat vi tri cua khach hang
	 * 
	 * @author banghn
	 * @param e
	 */
	public void getCustomerHistoryUpdateLocation(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;

		String customerId = b.getString(IntentConstants.INTENT_CUSTOMER_ID);

		ArrayList<CustomerUpdateLocationDTO> dto = null;
		try {
			dto = SQLUtils.getInstance().getCustomerHistoryUpdateLocation(customerId);
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
	}

	/**
	 * getListSaleRoadmapSupervisor
	 * @author: TamPQ
	 * @throws Exception
	 * @throws:
	 */
	public void getListPositionSalePersons(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle bundle = (Bundle) e.viewData;
		long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		String shopId = bundle.getString(IntentConstants.INTENT_SHOP_ID);
		GsnppRouteSupervisionDTO dto = null;
		try {
			dto = SQLUtils.getInstance().getListPositionSalePersons(staffId, shopId);
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
	 * update exception order date: Cho phep dat ghe tham ngoai le
	 * 
	 * @author: BangHN
	 * @param e
	 * @return
	 * @return: HTTPRequest
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public HTTPRequest requestUpdateExceptionOrderDate(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		HTTPRequest re = null;
		if (GlobalUtil.checkNetworkAccess()) {
			try {
				StaffCustomerDTO staffCusDto = (StaffCustomerDTO) e.viewData;
				// cap nhat gia tri exception oder date trong visit_plan

				long success = SQLUtils.getInstance().updateExceptionOrderDate(staffCusDto);

				if (success > 0) {
					JSONObject sqlUpdateExceptionDate = staffCusDto.generateInserOrUpdateExceptionOrderDate();
					JSONArray jarr = new JSONArray();
					jarr.put(sqlUpdateExceptionDate);
					JSONObject sqlStaffCusDetail = new JSONObject();
					StaffCustomerDetailDTO staffCustomerDetailDTO = new StaffCustomerDetailDTO();
					staffCustomerDetailDTO = staffCusDto.staffCustomerDetailDTO;
					if (staffCusDto.exceptionOrderDate == null && staffCustomerDetailDTO.staffCustomerDetailId > 0) {
						sqlStaffCusDetail = staffCustomerDetailDTO.generateUpdateSql();
						jarr.put(sqlStaffCusDetail);
					} else {
						sqlStaffCusDetail = staffCustomerDetailDTO.generateCreateSql();
						jarr.put(sqlStaffCusDetail);
					}
					String logId = GlobalUtil.generateLogId();
					e.logData = logId;
					e.viewData = staffCusDto;
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
					// UserController.getInstance().handleModelEvent(model);
					re = sendHttpRequest("queryController/executeSql", para, e);
				}
			} catch (Exception ex) {
				model.setModelMessage(ex.getMessage());
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				UserController.getInstance().handleErrorModelEvent(model);
			}
		}else{
			model.setModelMessage(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE));
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			UserController.getInstance().handleErrorModelEvent(model);
		}

		return re;

	}

	/**
	 * 
	 * get list track and fix problem of gsnpp
	 * 
	 * @param e
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void getListProblemOfGSNPP(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		SuperviorTrackAndFixProblemOfGSNPPViewDTO dto = new SuperviorTrackAndFixProblemOfGSNPPViewDTO();
		try {
			dto = SQLUtils.getInstance().getListTrackAndFixProblemOfGSNPP(b);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListProblemOfGSNPP error: ", "sql get reviews info of staff error",
						TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListProblemOfGSNPP error: ", "sql get reviews info of staff error",
					TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * 
	 * request update feedback status from GSNPP
	 * 
	 * @param e
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 7, 2012
	 */
	public HTTPRequest requestUpdateFeedbackStatusOfGSNPP(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		String feedBackId = data.getString(IntentConstants.INTENT_FEED_BACK_ID);
		String feedBackStatus = data.getString(IntentConstants.INTENT_FEED_BACK_STATUS);
		String doneDate = data.getString(IntentConstants.INTENT_DONE_DATE);
		String updateDate = data.getString(IntentConstants.INTENT_UPDATE_DATE);
		String updateUser = data.getString(IntentConstants.INTENT_UPDATE_USER);

		HTTPRequest re = null;
		try {
			int result = SQLUtils.getInstance().requestUpdateFeedbackStatus(feedBackId, feedBackStatus, doneDate,updateDate,updateUser );
			if (result == 1) {
				FeedBackDTO feedBack = new FeedBackDTO();
				JSONObject sqlUpdate = feedBack.generateUpdateFeedBackSql(feedBackId, feedBackStatus, doneDate, updateDate,updateUser );
				JSONArray listSQL = new JSONArray();
				listSQL.put(sqlUpdate);
				String logId = GlobalUtil.generateLogId();
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
				model.setModelData(String.valueOf(result));
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu ghi chú không thành công. Kiểm tra lại dữ liệu.");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method requestUpdateFeedbackStatusOfGSNPP error: ",
						"save note to db local incorrect", TabletActionLogDTO.LOG_CLIENT);
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			model.setModelMessage(ex.getMessage());
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setIsSendLog(false);
			ServerLogger.sendLog("method requestUpdateFeedbackStatusOfGSNPP error: ", ex.getMessage(),
					TabletActionLogDTO.LOG_EXCEPTION);
			SuperviorController.getInstance().handleErrorModelEvent(model);
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
	public void requestGetLessThan2Mins(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;

		String lessThan2MinList = b.getString(IntentConstants.INTENT_STRING_LIST);
		long staffId = b.getLong(IntentConstants.INTENT_STAFF_ID);

		GsnppLessThan2MinsDTO dto = null;
		try {
			dto = SQLUtils.getInstance().requestGetLessThan2Mins(staffId, lessThan2MinList);
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

	} 
	
	/**
	 * Danh sach hinh anh khach hang GSNPP
	 *  
	 * @author: QuangVT
	 * @since: 1:21:11 PM Dec 12, 2013
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getGsnppImageList(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		try {
			ImageListDTO dto = SQLUtils.getInstance().getSupervisorImageList(data);

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
		String shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		String type = String.valueOf(data.getInt(IntentConstants.INTENT_ALBUM_TYPE));
		String numTop = String.valueOf(data.getInt(IntentConstants.INTENT_MAX_IMAGE_PER_PAGE));
		String page = String.valueOf(data.getInt(IntentConstants.INTENT_PAGE));
		PhotoThumbnailListDto result = null;
		try {
			result = new PhotoThumbnailListDto();
			ArrayList<PhotoDTO> listPhoto = SQLUtils.getInstance().getSupervisorAlbumDetailUser(customerId, type,
					numTop, page, shopId);
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
	 * Lay ds album cua user
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
	 * Lay ds san pham cua shop ma gsnpp quan ly
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param e
	 * @return: void
	 * @throws:
	 */

	public void getListProductStorage(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		ListProductDTO result = null;
		try {
			result = SQLUtils.getInstance().getSupervisorProductList(data);
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
	 * lay d/s thoi gian cho header cua table trong canh bao ghe tham tren tuyen
	 * cua NVBH trong table apparam 
	 * 
	 * @author: QuangVT
	 * @since: 4:21:41 PM Dec 4, 2013
	 * @return: void
	 * @throws:  
	 * @param e
	 */
	public void getListTimeDefineApparamForReportVisitCustomer(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle b = (Bundle) e.viewData;
		ArrayList<ShopParamDTO> dto = new ArrayList<ShopParamDTO>();
		try {
			dto = SQLUtils.getInstance().getListDefineHeaderTable(b);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListTimeDefineApparamForReportVisitCustomer error: ",
						"lay d/s time dinh nghia trong appram bi loi", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListTimeDefineApparamForReportVisitCustomer error: ",
					"lay d/s time dinh nghia trong appram bi loi", TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * Lay ds cham cong cac nhan vien cua nhan vien giam sat
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
			result = SQLUtils.getInstance().getSupervisorAttendanceList(data);
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
			model.setModelMessage(StringUtil.getString(R.string.TEXT_NOTIFY_NO_HAVE_DATA));
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
				SaleController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				model.setIsSendLog(false);
				ServerLogger.sendLog("method getListReportNVBHVisitCustomer error: ",
						"lay d/s bao cao NVBH ghe tham khach hang", TabletActionLogDTO.LOG_CLIENT);
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			model.setIsSendLog(false);
			ServerLogger.sendLog("method getListReportNVBHVisitCustomer error: ",
					"lay d/s bao cao NVBH ghe tham khach hang", TabletActionLogDTO.LOG_EXCEPTION);
			SaleController.getInstance().handleErrorModelEvent(model);
		}

	}

	/**
	 * lay danh sach hinh anh chuong trinh cua khach hang
	 * 
	 * @author ThanhNN
	 * @param e
	 */
	public void getListAlbumPrograme(ActionEvent e) {
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
			ArrayList<PhotoDTO> listPhoto = SQLUtils.getInstance().getAlbumDetailPrograme(customerId, type, numTop,
					page, programeCode, fromDate, toDate, shopId);
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
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			SaleController.getInstance().handleErrorModelEvent(model);
		}
	}
	
	/**
	 * Lay danh sach NVBH chua PDSD trong thang cua GSBH
	 * 
	 * @author quangvt1
	 * @param e
	 */
	public void getListNVBHOfGSBHNoPFSD(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);  
		try {
			ListStaffDTO dto; 
			dto = SQLUtils.getInstance().getListNVBHNotPSDS(shopId, staffOwnerId);
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		} 
	}

	/**
	 * Lay danh sach KH khong PSDS trong thang cho GSBH
	 * 
	 * @author quangvt1
	 * @param e
	 */
	public void getReportCustomerNotPSDSForSupervisor(ActionEvent e) {
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
	 * Lay danh sach khach hang cua C2
	 * @author: Nguyen Thanh Dung
	 * @throws Exception
	 * @return: void
	 */
	public void getListCustomerOfC2(ActionEvent actionEvent) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(actionEvent);
		Bundle bundle = (Bundle) actionEvent.viewData;
		try {
			long staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
			long shopId = bundle.getLong(IntentConstants.INTENT_SHOP_ID);
			long c2CustomerId = bundle.getLong(IntentConstants.INTENT_CUSTOMER_ID);
			String code = bundle.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			String name = bundle.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			int page = bundle.getInt(IntentConstants.INTENT_PAGE);
			boolean isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
			int type = bundle.getInt(IntentConstants.INTENT_ROLE_TYPE);
			try {
				CustomerListDTO dto = SQLUtils.getInstance().getListCustomerOfC2(staffId, shopId, c2CustomerId, name, code, page, isGetTotalPage, type);
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
					ServerLogger.sendLog("method getListCustomerOfC2: ", buff.toString(), TabletActionLogDTO.LOG_CLIENT);
					model.setIsSendLog(false);
					model.setModelData(dto);
					model.setModelCode(ErrorConstants.ERROR_COMMON);
					SaleController.getInstance().handleErrorModelEvent(model);
				}
			} catch (Exception e) {
				ServerLogger.sendLog("method getListCustomerOfC2 1: ", e.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
				model.setIsSendLog(false);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage(e.getMessage());
				SaleController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			ServerLogger.sendLog("method getListCustomerOfC2 2: ", ex.getMessage(), TabletActionLogDTO.LOG_EXCEPTION);
			model.setIsSendLog(false);
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SaleController.getInstance().handleErrorModelEvent(model);
		} finally {
		}
	}
	
	/**
	 * 
	 * get list product for C2 sale or buy
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */

	public void getListProductForC2Order(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		C2SaleOrderlViewDTO result = null;
		try {
			result = new C2SaleOrderlViewDTO(); 
			result = SQLUtils.getInstance().getListProductForC2Order(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Luu don hang cho KH cua C2
	 * 
	 * @param e
	 * @return: HTTPRequest
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public HTTPRequest saveOrderForC2(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			Bundle bundle = (Bundle) e.viewData;
			C2SaleOrderlViewDTO order = (C2SaleOrderlViewDTO) bundle.getSerializable(IntentConstants.INTENT_ORDER);
			boolean isSuccess = SQLUtils.getInstance().createOrderForC2(order);
			if (isSuccess) {
				// if (order.orderInfo.isSend == 1) {
				JSONArray listSql = order.generateNewOrderSql();

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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				re = sendHttpRequestOffline("queryController/executeSql", para, e, LogDTO.TYPE_NORMAL, String.valueOf(order.orderInfo.c2SaleOrderId),
						C2_SALE_ORDER_TABLE.TABLE_NAME);
				
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("order id: " + order.orderInfo.c2SaleOrderId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu đơn hàng C2 không thành công. Kiểm tra lại dữ liệu.");
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
	 * Lay ds KH la C2
	 * @return: HTTPRequest
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
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
				CustomerListDTO dto = SQLUtils.getInstance().getListC2Customer(staffId, shopId, page, isGetTotalPage);
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
	 * get list product for C2 sale or buy
	 * @return
	 * @return: C2SaleOrderlViewDTO
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */

	public void getListProductForC2Purchase(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle data = (Bundle) e.viewData;
		C2PurchaselViewDTO result = null;
		try {
			result = new C2PurchaselViewDTO(); 
			result = SQLUtils.getInstance().getListProductForC2Purchase(data);
		} catch (Exception ex) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(ex));
			result = null;
		}
		if (result != null) {
			model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
			model.setModelData(result);
			SuperviorController.getInstance().handleModelEvent(model);
		} else {
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}

	/**
	 * Luu don mua hang cua C2
	 * 
	 * @param e
	 * @return: HTTPRequest
	 * @throws:
	 * @author: Nguyen Thanh Dung
	 * @date: 06/12/2013
	 */
	public HTTPRequest savePurchaseForC2(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);

		HTTPRequest re = null;
		try {
			// insert to sql Lite & request to server
			Bundle bundle = (Bundle) e.viewData;
			C2PurchaselViewDTO order = (C2PurchaselViewDTO) bundle.getSerializable(IntentConstants.INTENT_ORDER);
			boolean isSuccess = SQLUtils.getInstance().createPurchaseForC2(order);
			if (isSuccess) {
				// if (order.orderInfo.isSend == 1) {
				JSONArray listSql = order.generateNewOrderSql();

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
//				para.add(IntentConstants.INTENT_STAFF_ID_PARA);
//				para.add(GlobalInfo.getInstance().getProfile().getUserData().id);
				para.add(IntentConstants.INTENT_IMEI_PARA);
				para.add(GlobalInfo.getInstance().getDeviceIMEI());

				re = sendHttpRequestOffline("queryController/executeSql", para, e, LogDTO.TYPE_NORMAL, String.valueOf(order.orderInfo.c2PurchaseId),
						C2_PURCHASE_TABLE.TABLE_NAME);
				
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SaleController.getInstance().handleModelEvent(model);

			} else {
				ServerLogger.sendLog("order id: " + order.orderInfo.c2PurchaseId, TabletActionLogDTO.LOG_CLIENT);
				model.setIsSendLog(false);

				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Lưu đơn hàng C2 không thành công. Kiểm tra lại dữ liệu.");
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
	 * Lấy danh sách nhân viên bán hàng màn hình huấn luyện
	 * @param e
     */
	public void getTrainingListNVBH(ActionEvent e) {
		ModelEvent model = new ModelEvent();
		model.setActionEvent(e);
		Bundle viewInfo = (Bundle) e.viewData;
		long shopId = Long.parseLong(viewInfo.getString(IntentConstants.INTENT_SHOP_ID));
		String staffOwnerId = viewInfo.getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		boolean orderStaffCode = viewInfo.getBoolean(IntentConstants.INTENT_ORDER);
		boolean isHaveAll = viewInfo.getBoolean(IntentConstants.INTENT_IS_ALL);
		try {
			ListStaffDTO dto;
			if (!orderStaffCode) {
				dto = SQLUtils.getInstance().getTrainingListNVBH(shopId, staffOwnerId);
			} else {
				dto = SQLUtils.getInstance().getListNVBHOrderCode(shopId, staffOwnerId, isHaveAll);
			}
			if (dto != null) {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_CODE_SUCCESS);
				SuperviorController.getInstance().handleModelEvent(model);
			} else {
				model.setModelData(dto);
				model.setModelCode(ErrorConstants.ERROR_COMMON);
				model.setModelMessage("Không có danh sach");
				SuperviorController.getInstance().handleErrorModelEvent(model);
			}
		} catch (Exception ex) {
			model.setModelCode(ErrorConstants.ERROR_COMMON);
			model.setModelMessage(ex.getMessage());
			SuperviorController.getInstance().handleErrorModelEvent(model);
		}
	}
}
