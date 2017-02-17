/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.controller;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.TBHVModel;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.image.SupervisorImageListView;
import com.viettel.dms.view.supervisor.statistic.GSNPPGeneralStatisticsView;
import com.viettel.dms.view.tbhv.TBHVAddRequirementView;
import com.viettel.dms.view.tbhv.TBHVAttendanceView;
import com.viettel.dms.view.tbhv.TBHVCustomerNotPSDSInMonthDetailView;
import com.viettel.dms.view.tbhv.TBHVDisProComProgReportNPPView;
import com.viettel.dms.view.tbhv.TBHVDisProComProgReportView;
import com.viettel.dms.view.tbhv.TBHVDisplayProgramView;
import com.viettel.dms.view.tbhv.TBHVFollowProblemView;
import com.viettel.dms.view.tbhv.TBHVGsnppNvbhPositionView;
import com.viettel.dms.view.tbhv.TBHVIntroduceProductView;
import com.viettel.dms.view.tbhv.TBHVManagerEquipmentDetailView;
import com.viettel.dms.view.tbhv.TBHVManagerEquipmentView;
import com.viettel.dms.view.tbhv.TBHVProductListView;
import com.viettel.dms.view.tbhv.TBHVProgReportProDispDetailSaleView;
import com.viettel.dms.view.tbhv.TBHVPromotionProgramView;
import com.viettel.dms.view.tbhv.TBHVReportCustomerNotPSDSView;
import com.viettel.dms.view.tbhv.TBHVReportProgressDateDetailView;
import com.viettel.dms.view.tbhv.TBHVReportProgressDateView;
import com.viettel.dms.view.tbhv.TBHVReportProgressMonthNPPDetailView;
import com.viettel.dms.view.tbhv.TBHVReportProgressMonthView;
import com.viettel.dms.view.tbhv.TBHVReportProgressSalesFocusDetailView;
import com.viettel.dms.view.tbhv.TBHVReportProgressSalesFocusView;
import com.viettel.dms.view.tbhv.TBHVReportVisitCustomerDayOfDetailNPP;
import com.viettel.dms.view.tbhv.TBHVReviewsGSNPPView;
import com.viettel.dms.view.tbhv.TBHVRouteSupervisionMapView;
import com.viettel.dms.view.tbhv.TBHVRouteSupervisionView;
import com.viettel.dms.view.tbhv.TBHVTakeAttendanceView;
import com.viettel.dms.view.tbhv.TBHVTrackingCabinetStaffView;
import com.viettel.dms.view.tbhv.TBHVTrainingPlanDayResultReportView;
import com.viettel.dms.view.tbhv.TBHVTrainingPlanHistoryAccView;
import com.viettel.dms.view.tbhv.TBHVTrainingPlanView;
import com.viettel.dms.view.tbhv.TBHVVisitCustomerNotificationView;
import com.viettel.dms.view.tbhv.statistic.GSBHGeneralStatisticsDateView;
import com.viettel.dms.view.tbhv.statistic.GSBHGeneralStatisticsMonthView;
import com.viettel.dms.view.tnpg.statistic.TNPGGeneralStatisticView;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 * TBHVController -- Controller cho TBHV
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVController extends AbstractController {

	static TBHVController instance;

	protected TBHVController() {
	}

	public static TBHVController getInstance() {
		if (instance == null) {
			instance = new TBHVController();
		}
		return instance;
	}

	/**
	 * Xu ly cac request tu view
	 * 
	 * @author: TruongHN
	 * @param e
	 * @return: void
	 * @throws:
	 */
	public void handleViewEvent(final ActionEvent e) {
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				switch (e.action) {
				case ActionEventConstant.GET_GENERAL_STATISTICS_DATE_REPORT_INFO:
					TBHVModel.getInstance().getGeneralStatisticsReportInfoForGST(e);
					break;
				case ActionEventConstant.GET_GENERAL_STATISTICS_MONTH_REPORT_INFO:
					TBHVModel.getInstance().getGeneralStatisticsReportInfoForGST(e);
					break;
				case ActionEventConstant.TBHV_ROUTE_SUPERVISION:
					TBHVModel.getInstance().requestListRouteSupervision(e);
					break;
				case ActionEventConstant.GET_TBHV_LIST_EQUIPMENT: {
					TBHVModel.getInstance().getListEquipment(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_LIST_PROMOTION_PROGRAME: {
					TBHVModel.getInstance().requestGetListPromotionPrograme(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_LIST_DISPLAY_PROGRAM: {
					TBHVModel.getInstance().requestGetListDisplayPrograme(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_DATE: {
					TBHVModel.getInstance().getTBHVProgressDateReport(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_DATE_DETAIL: {
					TBHVModel.getInstance().getTBHVProgressDateDetailReport(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT: {
					TBHVModel.getInstance().getTBHVDisProComProReport(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_LIST_PRODUCT_STORAGE: {
					TBHVModel.getInstance().getTBHVListProductStorage(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_INTRODUCE_PRODUCT: {
					TBHVModel.getInstance().getIntroduceProduct(e);
					break;
				}
				case ActionEventConstant.UPDATE_CLIENT_THUMNAIL_URL:
					TBHVModel.getInstance().updateClientThumbnailUrl(e);
					break;
				case ActionEventConstant.GET_TBHV_NPP_DIS_PRO_COM_PROG_REPORT: {
					TBHVModel.getInstance().getTBHVStaffDisProComProReport(e);
					break;
				}
				case ActionEventConstant.GET_REPORT_PROGESS_MONTH_INFO: {
					TBHVModel.getInstance().getReportProgressInMonthTBHV(e);
					break;
				}
				case ActionEventConstant.TBHV_TRAINING_PLAN: {
					TBHVModel.getInstance().getTbhvTrainingPlan(e);
					break;
				}
				case ActionEventConstant.GET_LIST_NPP: {
					TBHVModel.getInstance().getListNPP(e);
					break;
				}
				case ActionEventConstant.GET_LIST_NVGS_OF_DATE: {
					TBHVModel.getInstance().getListNVGSOfTBHVReportDate(e);
					break;
				}
				case ActionEventConstant.GET_LIST_NVGS_OF_PSDS: {
					TBHVModel.getInstance().getListNVGSOfTBHVReportPSDS(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_FOLLOW_LIST_PROBLEM: {
					TBHVModel.getInstance().getFollowlistProblem(e);
					break;
				}
				case ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE: {
					TBHVModel.getInstance().updateFollowProblemDone(e);
					break;
				}
				case ActionEventConstant.DELETE_TBHV_FOLLOW_PROBLEM_DONE: {
					TBHVModel.getInstance().deleteFollowProblemDone(e);
					break;
				}
				case ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT: {
					TBHVModel.getInstance().getDayTrainingSupervision(e);
					break;
				}
				case ActionEventConstant.TBHV_PLAN_TRAINING_HISTORY_ACC: {
					TBHVModel.getInstance().getPlanTrainingHistoryAcc(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_REPORT_CUSTOMER_NOT_PSDS: {
					TBHVModel.getInstance().getTBHVNotPSDSReport(e);
					break;
				}
				case ActionEventConstant.GET_REPORT_PROGESS_MONTH_NPP_DETAIL_INFO: {
					TBHVModel.getInstance().getReportProgressInMonthTBHVDetailNPP(e);
					break;
				}
				case ActionEventConstant.GET_LIST_NVBH: {
					TBHVModel.getInstance().getListNVBH(e);
					break;
				}
				case ActionEventConstant.GET_LIST_NVBH_NOT_PSDS: {
					TBHVModel.getInstance().getListNVBHNotPSDS(e);
					break;
				}				
				case ActionEventConstant.TBHV_GET_REPORT_PROGESS_SALES_FORCUS_INFO: {
					TBHVModel.getInstance().getReportProgressSalesFocusInfo(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_SALES_FORCUS_DETAIL: {
					TBHVModel.getInstance().getReportProgressSalesFocusDetailInfo(e);
					break;
				}
				case ActionEventConstant.GET_LIST_EQUIPMENT_TBHV: {
					TBHVModel.getInstance().getListEquipmentTBHV(e);
					break;
				}

				case ActionEventConstant.GET_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE: {
					TBHVModel.getInstance().getTBHVProgReportProDispDetailSale(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_REVIEWS_INFO: {
					TBHVModel.getInstance().getTBHVReviewsGSNPPDetailInfo(e);
					break;
				}
				case ActionEventConstant.TBHV_UPDATE_TRAINING_REVIEWS_INFO_TO_DB: {
					TBHVModel.getInstance().tbhvUpdateTrainingReviewsToDBAndServer(e);
					break;
				}
				case ActionEventConstant.GET_COUNT_CABINET_STAFF: {
					TBHVModel.getInstance().getCountCabinetStaff(e);
					break;
				}
				case ActionEventConstant.GET_CABINET_STAFF: {
					TBHVModel.getInstance().getCabinetStaff(e);
					break;
				}
				case ActionEventConstant.GET_TBHV_BUSINESS_SUPPORT_PROGRAME_INFO: {
					TBHVModel.getInstance().getBusinessSupportProgrameInfo(e);
					break;
				}

				case ActionEventConstant.GET_CUSTOMER_LIST_FOR_POST_FEEDBACK: {
					TBHVModel.getInstance().getCustomerListForPostFeedback(e);
					break;
				}

				case ActionEventConstant.GET_TBHV_ADD_REQUIREMENT_INFO: {
					TBHVModel.getInstance().getAddRequirementInfo(e);
					break;
				}
				case ActionEventConstant.TBHV_ADD_PROBLEM: {
					TBHVModel.getInstance().addProblem(e);
					break;
				}
				case ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION: {
					TBHVModel.getInstance().getGsnppPosition(e);
					break;
				}
				case ActionEventConstant.TBHV_NVBH_POSITION: {
					TBHVModel.getInstance().getNvbhPosition(e);
					break;
				}
				case ActionEventConstant.GET_POS_GSNPP_NVBH: {
					TBHVModel.getInstance().getPositionOfGsnppAndNvbh(e);
					break;
				}
				case ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION: {
					TBHVModel.getInstance().getVisitCusNotification(e);
					break;
				}
				case ActionEventConstant.TBHV_ATTENDANCE: {
					TBHVModel.getInstance().getTbhvAttendance(e);
					break;
				}
				case ActionEventConstant.TBHV_GET_LIST_SALE_FOR_ATTENDANCE: {
					TBHVModel.getInstance().getListSaleForAttendance(e);
					break;
				}
				case ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER: {
					TBHVModel.getInstance().getListReportNVBHVisitCustomer(e);
					break;
				}
				case ActionEventConstant.GET_LIST_SHOP_SUPERVISOR_STAFF: {
					TBHVModel.getInstance().getListShopSupervisorAndStaff(e);
					break;
				}
				case ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH:
					TBHVModel.getInstance().getReportCustomerNotPSDSForTBHV(e);
					break;
				case ActionEventConstant.GET_LIST_VISIT:
					TBHVModel.getInstance().getListVisit(e);
					break;
				case ActionEventConstant.GET_LIST_STAFF:
					TBHVModel.getInstance().getListStaffForGSBH(e);
					break;
				case ActionEventConstant.GET_GSBH_IMAGE_LIST:
					TBHVModel.getInstance().getGsbhImageList(e);
					break;
				default:

					break;
				}
				return null;
			}
		};
		task.execute();
	}

	/**
	 * Xu ly du lieu tra ve tu model
	 * 
	 * @author: TruongHN
	 * @param modelEvent
	 * @return: void
	 * @throws:
	 */
	public void handleModelEvent(final ModelEvent modelEvent) {
		if (modelEvent.getModelCode() == ErrorConstants.ERROR_CODE_SUCCESS) {
			final ActionEvent e = modelEvent.getActionEvent();
			HTTPRequest request = e.request;
			if (e.sender != null && (request == null || (request != null && request.isAlive()))) {
				if (e.sender instanceof GlobalBaseActivity) {
					final GlobalBaseActivity sender = (GlobalBaseActivity) e.sender;
					if (sender.isFinished)
						return;
					sender.runOnUiThread(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							sender.handleModelViewEvent(modelEvent);
						}
					});
				} else if (e.sender instanceof BaseFragment) {
					final BaseFragment sender = (BaseFragment) e.sender;
					if (sender.getActivity() == null || sender.isFinished) {
						return;
					}
					sender.getActivity().runOnUiThread(new Runnable() {
						public void run() {
							// TODO Auto-generated method stub
							sender.handleModelViewEvent(modelEvent);
						}
					});
				}
			} else {
				handleErrorModelEvent(modelEvent);
			}
		} else {
			handleErrorModelEvent(modelEvent);
		}
	}

	@Override
	public void handleSwitchActivity(ActionEvent e) {
	}

	@Override
	public void handleSwitchFragment(ActionEvent e) {
		Activity base = null;
		if (e.sender instanceof Activity) {
			base = (Activity) e.sender;
		} else if (e.sender instanceof Fragment) {
			base = ((Fragment) e.sender).getActivity();
		}
		if (base != null)
			GlobalUtil.forceHideKeyboard(base);

		FragmentManager fm = base.getFragmentManager();
		switch (e.action) {
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_MONTH_DETAIL_TNPG:
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE_DETAIL_TNPG: {
			TNPGGeneralStatisticView frag = TNPGGeneralStatisticView.getInstance((Bundle)e.viewData);
			switchFragment(fm, frag, TNPGGeneralStatisticView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_MONTH_DETAIL_GSBH:
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE_DETAIL_GSBH: {
			GSNPPGeneralStatisticsView frag = GSNPPGeneralStatisticsView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, GSNPPGeneralStatisticsView.TAG, false);
			break;
		}
		
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_DATE: {
			GSBHGeneralStatisticsDateView frag = GSBHGeneralStatisticsDateView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, GSBHGeneralStatisticsDateView.TAG, true);			
			break;
		}
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_MONTH: {
			GSBHGeneralStatisticsMonthView frag =  GSBHGeneralStatisticsMonthView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, GSBHGeneralStatisticsMonthView.TAG, true);
			break;
		}
		case ActionEventConstant.ACTION_TBHV_REPORT_PROGRESS_DATE: {
			TBHVReportProgressDateView frag = TBHVReportProgressDateView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportProgressDateView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_REPORT_PROGRESS_DATE_DETAIL: {
			TBHVReportProgressDateDetailView frag = TBHVReportProgressDateDetailView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportProgressDateDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH_SALE:{
			TBHVReportCustomerNotPSDSView frag = TBHVReportCustomerNotPSDSView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportCustomerNotPSDSView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_SUPPORT_SALE: {
			TBHVDisplayProgramView frag = TBHVDisplayProgramView.getInstance(1);
			switchFragment(fm, frag, TBHVDisplayProgramView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_PROMOTION_PROGRAM: {
			TBHVPromotionProgramView frag = TBHVPromotionProgramView.getInstance();
			switchFragment(fm, frag, TBHVPromotionProgramView.TAG, true);
			break;
		}
		case ActionEventConstant.TBHV_ROUTE_SUPERVISION: {
			TBHVRouteSupervisionView frag = TBHVRouteSupervisionView.newInstance();
			switchFragment(fm, frag, TBHVRouteSupervisionView.TAG, true);
			break;
		}
		case ActionEventConstant.TBHV_ROUTE_SUPERVISION_MAP: {
			Bundle b = (Bundle) e.viewData;
			TBHVRouteSupervisionMapView frag = TBHVRouteSupervisionMapView.newInstance(b);
			switchFragment(fm, frag, TBHVRouteSupervisionMapView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_PRODUCT_LIST: {
			TBHVProductListView frag = TBHVProductListView.getInstance();
			switchFragment(fm, frag, TBHVProductListView.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_PRODUCT_INTRODUCTION: {
			Bundle data = (Bundle) e.viewData;
			TBHVIntroduceProductView frag = TBHVIntroduceProductView.getInstance(data);
			switchFragment(fm, frag, TBHVIntroduceProductView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_FOLLOW_LIST_PROBLEM: {
			TBHVFollowProblemView frag = TBHVFollowProblemView.newInstance();
			switchFragment(fm, frag, TBHVFollowProblemView.TAG, true);
			break;
		}
		case ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT: {
			TBHVDisProComProgReportView frag = TBHVDisProComProgReportView.getInstance();
			switchFragment(fm, frag, TBHVDisProComProgReportView.TAG, true);
			break;
		}

		case ActionEventConstant.GET_TBHV_NPP_DIS_PRO_COM_PROG_REPORT: {
			Bundle b = (Bundle) e.viewData;
			TBHVDisProComProgReportNPPView frag = TBHVDisProComProgReportNPPView.getInstance(b);
			switchFragment(fm, frag, TBHVDisProComProgReportNPPView.TAG, false);
			break;
		}

		case ActionEventConstant.TBHV_TRAINING_PLAN: {
			TBHVTrainingPlanView frag = TBHVTrainingPlanView.newInstance();
			switchFragment(fm, frag, TBHVTrainingPlanView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_IMAGE_LIST: {
			SupervisorImageListView statisticsProduct = SupervisorImageListView.getInstance((Bundle) e.viewData);
			switchFragment(fm, statisticsProduct, SupervisorImageListView.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH: {
			TBHVReportProgressMonthView frag = TBHVReportProgressMonthView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportProgressMonthView.TAG, true);			
			break;
		}
		case ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_SALES_FORCUS: {
			TBHVReportProgressSalesFocusView frag  = TBHVReportProgressSalesFocusView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportProgressSalesFocusView.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH_DETAIL_NPP: {
			TBHVReportProgressMonthNPPDetailView frag = TBHVReportProgressMonthNPPDetailView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportProgressMonthNPPDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_SALES_FORCUS_DETAIL: {
			TBHVReportProgressSalesFocusDetailView frag = TBHVReportProgressSalesFocusDetailView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, TBHVReportProgressSalesFocusDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.TBHV_PLAN_TRAINING_HISTORY_ACC: {
			Bundle b = (Bundle) e.viewData;
			TBHVTrainingPlanHistoryAccView frag = TBHVTrainingPlanHistoryAccView.newInstance(b);
			switchFragment(fm, frag, TBHVTrainingPlanHistoryAccView.TAG, true);
			break;
		}
		case ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT: {
			Bundle b = (Bundle) e.viewData;
			TBHVTrainingPlanDayResultReportView frag = TBHVTrainingPlanDayResultReportView.newInstance(b);
			switchFragment(fm, frag, TBHVTrainingPlanDayResultReportView.TAG, false);
			break;
		}
		case ActionEventConstant.GOTO_TBHV_MANAGER_EQUIPMENT: {
			Bundle b = (Bundle) e.viewData;
			TBHVManagerEquipmentView frag = TBHVManagerEquipmentView.getInstance(b);
			switchFragment(fm, frag, TBHVManagerEquipmentView.TAG, true);
			break;
		}
		case ActionEventConstant.TBHV_ATTENDANCE: {
			Bundle b = (Bundle) e.viewData;
			TBHVAttendanceView frag = TBHVAttendanceView.getInstance(b);
			switchFragment(fm, frag, TBHVAttendanceView.TAG, true);
			break;
		}
		case ActionEventConstant.TBHV_SHOW_REPORT_VISIT_CUSTOMER_DETAIL_NPP: {
			Bundle b = (Bundle) e.viewData;
			TBHVReportVisitCustomerDayOfDetailNPP frag = TBHVReportVisitCustomerDayOfDetailNPP.getInstance(b);
			switchFragment(fm, frag, TBHVReportVisitCustomerDayOfDetailNPP.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_MANAGER_EQUIPMENT_DETAIL: {
			Bundle b = (Bundle) e.viewData;
			TBHVManagerEquipmentDetailView frag = TBHVManagerEquipmentDetailView.getInstance(b);
			switchFragment(fm, frag, TBHVManagerEquipmentDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_TRACKING_CABINET_STAFF: {
			Bundle b = (Bundle) e.viewData;
			TBHVTrackingCabinetStaffView frag = TBHVTrackingCabinetStaffView.getInstance(b);
			switchFragment(fm, frag, TBHVTrackingCabinetStaffView.TAG, false);
			break;
		}
		case ActionEventConstant.GET_TBHV_PROG_REPOST_PRO_DISP_DETAIL_SALE: {
			Bundle b = (Bundle) e.viewData;
			TBHVProgReportProDispDetailSaleView frag = TBHVProgReportProDispDetailSaleView.getInstance(b);
			switchFragment(fm, frag, TBHVProgReportProDispDetailSaleView.TAG, false);
			break;
		}

		case ActionEventConstant.GO_TO_TBHV_REVIEWS_INFO_VIEW: {
			Bundle b = (Bundle) e.viewData;
			TBHVReviewsGSNPPView frag = TBHVReviewsGSNPPView.getInstance(b);
			switchFragment(fm, frag, TBHVReviewsGSNPPView.TAG, false);
			break;
		}
		case ActionEventConstant.ACTION_TBHV_GO_TO_ADD_REQUIREMENT: {
			Bundle b = (Bundle) e.viewData;
			TBHVAddRequirementView frag = TBHVAddRequirementView.newInstance(b);
			switchFragment(fm, frag, TBHVAddRequirementView.TAG, false);
			break;

		}
		case ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION: {
			Bundle b = (Bundle) e.viewData;
			TBHVGsnppNvbhPositionView frag = TBHVGsnppNvbhPositionView.getInstance(b);
			switchFragment(fm, frag, TBHVGsnppNvbhPositionView.TAG, true);
			break;

		}
		case ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION: {
			TBHVVisitCustomerNotificationView frag = TBHVVisitCustomerNotificationView.getInstance();
			switchFragment(fm, frag, TBHVVisitCustomerNotificationView.TAG, true);
			break;
		}
		case ActionEventConstant.TBHV_GET_LIST_SALE_FOR_ATTENDANCE: {
			Bundle b = (Bundle) e.viewData;
			TBHVTakeAttendanceView frag = TBHVTakeAttendanceView.newInstance(b);
			switchFragment(fm, frag, TBHVTakeAttendanceView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_TBHV_REPORT_CUSTOMER_NOT_PSDS_DETAIL: {
			Bundle b = (Bundle) e.viewData;
			TBHVCustomerNotPSDSInMonthDetailView frag = TBHVCustomerNotPSDSInMonthDetailView.getInstance(b);
			switchFragment(fm, frag, TBHVCustomerNotPSDSInMonthDetailView.TAG, false);
			break;
		}
		}
	}

}
