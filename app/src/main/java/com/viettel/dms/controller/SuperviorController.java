/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.controller;

import java.util.Vector;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.SaleModel;
import com.viettel.dms.model.SuperviorModel;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CustomerRouteView;
import com.viettel.dms.view.sale.image.ListAlbumUserView;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsView;
import com.viettel.dms.view.supervisor.AccSaleProgReportView;
import com.viettel.dms.view.supervisor.CustomerSaleList;
import com.viettel.dms.view.supervisor.CustomerSaleLocationResetView;
import com.viettel.dms.view.supervisor.DisProComProgReportView;
import com.viettel.dms.view.supervisor.FollowProblemView;
import com.viettel.dms.view.supervisor.GSBHRouteSupervisionView;
import com.viettel.dms.view.supervisor.GSNPPPostFeedbackView;
import com.viettel.dms.view.supervisor.GSNPPReportVisitCustomerOnPlanOfDayView;
import com.viettel.dms.view.supervisor.GSNPPTrainingPlanView;
import com.viettel.dms.view.supervisor.GSNPPTrainingResultDayReportView;
import com.viettel.dms.view.supervisor.GsnppRouteSupervisionMapView;
import com.viettel.dms.view.supervisor.GsnppTrainingResultAccReportView;
import com.viettel.dms.view.supervisor.ManagerEquipmentView;
import com.viettel.dms.view.supervisor.ProgReportProDispDetailSaleView;
import com.viettel.dms.view.supervisor.ProgressReportSalesFocusView;
import com.viettel.dms.view.supervisor.ReportProgressDateDetailView;
import com.viettel.dms.view.supervisor.StaffDisProComProgReportView;
import com.viettel.dms.view.supervisor.StaffInformationView;
import com.viettel.dms.view.supervisor.StaffPositionView;
import com.viettel.dms.view.supervisor.SuperVisorDisplayProgramView;
import com.viettel.dms.view.supervisor.SuperVisorPromotionProgramView;
import com.viettel.dms.view.supervisor.SuperVisorTakeAttendanceView;
import com.viettel.dms.view.supervisor.SupervisorIntroduceProductView;
import com.viettel.dms.view.supervisor.SupervisorPhotoThumbnailListProgrameView;
import com.viettel.dms.view.supervisor.SupervisorPhotoThumbnailListUserView;
import com.viettel.dms.view.supervisor.SupervisorProductListView;
import com.viettel.dms.view.supervisor.TrackAndFixProblemsOfGSNPPView;
import com.viettel.dms.view.supervisor.TrackingCabinetStaffView;
import com.viettel.dms.view.supervisor.collectinformation.CustomerC2ListView;
import com.viettel.dms.view.supervisor.collectinformation.CustomerOfC2ListView;
import com.viettel.dms.view.supervisor.collectinformation.ReportCustomerC2View;
import com.viettel.dms.view.supervisor.image.SupervisorImageListView;
import com.viettel.dms.view.supervisor.statistic.GSNPPCustomerNotPSDSInMonthView;
import com.viettel.dms.view.supervisor.statistic.GSNPPGeneralStatisticsView;
import com.viettel.dms.view.supervisor.training.ReviewsStaffView;
import com.viettel.dms.view.supervisor.training.TrainingListView;
import com.viettel.viettellib.network.http.HTTPRequest;

/**
 * Controller xu ly nghiep vu cua NVGS
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SuperviorController extends AbstractController {

	static SuperviorController instance;

	protected SuperviorController() {
	}

	public static SuperviorController getInstance() {
		if (instance == null) {
			instance = new SuperviorController();
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
					case ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION:
						SuperviorModel.getInstance().getListSaleRoadmapSupervisor(e);
						break;
					case ActionEventConstant.ACC_SALE_PROG_REPORT:
						SuperviorModel.getInstance().getAccSaleProgReport(e);
						break;
					// bao cao tien do ban MHTT den ngay //
					case ActionEventConstant.PROG_REPOST_SALE_FOCUS:
						SuperviorModel.getInstance().getProgRepostSaleFocus(e);
						break;
					case ActionEventConstant.DIS_PRO_COM_PROG_REPORT:
						SuperviorModel.getInstance().getDisProComProReport(e);
						break;
					case ActionEventConstant.GET_REPORT_PROGRESS_DATE:
						SuperviorModel.getInstance().getProgressDateReport(e);
						break;
					case ActionEventConstant.GET_REPORT_PROGRESS_DATE_DETAIL:
						SuperviorModel.getInstance().getProgressDateDetailReport(e);
						break;
					case ActionEventConstant.GET_LIST_GSNPP_BY_GSBH:
						SuperviorModel.getInstance().getListGSNPPByGSBH(e);
						break;
					case ActionEventConstant.GET_LIST_NVBH_BY_GSBH:
						SuperviorModel.getInstance().getListNVBHByGSBH(e);
						break;
					case ActionEventConstant.GET_LIST_NVBH:
						SuperviorModel.getInstance().getListNVBH(e);
						break;
					case ActionEventConstant.GET_LIST_VISIT:
						SuperviorModel.getInstance().getListVisit(e);
						break;
					case ActionEventConstant.GET_LIST_EQUIPMENT:
						SuperviorModel.getInstance().getListEquipment(e);
						break;
					case ActionEventConstant.GET_COUNT_CABINET_STAFF:
						SuperviorModel.getInstance().getCountCabinetStaff(e);
						break;
					case ActionEventConstant.GET_CABINET_STAFF:
						SuperviorModel.getInstance().getCabinetStaff(e);
						break;
					case ActionEventConstant.STAFF_DIS_PRO_COM_PROG_REPORT: {
						SuperviorModel.getInstance().getStaffDisProComProReport(e);
						break;
					}
					// bao cao tien do CTTB chi tiet NVBH ngay
					case ActionEventConstant.PROG_REPOST_PRO_DISP_DETAIL_SALE: {
						SuperviorModel.getInstance().getProgReportProDispDetailSale(e);
						break;
					}
					case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME: {
						SuperviorModel.getInstance().requestGetListPromotionPrograme(e);
						break;
					}
					case ActionEventConstant.GET_LIST_CUSTOMER_ATTENT_PROGRAME: {
						SuperviorModel.getInstance().getListCustomerAttentPrograme(e);
						break;
					}
					case ActionEventConstant.GET_LIST_COMBOBOX_DISPLAY_PROGRAME: {
						SuperviorModel.getInstance().getListComboboxDisplayPrograme(e);
						break;
					}
					case ActionEventConstant.GET_LIST_DISPLAY_PROGRAM: {
						SuperviorModel.getInstance().requestGetListDisplayPrograme(e);
						break;
					}
					case ActionEventConstant.STAFF_INFORMATION: {
						SuperviorModel.getInstance().requestStaffInfo(e);
						break;
					}
					case ActionEventConstant.GSNPP_TRAINING_RESULT_ACC_REPORT: {
						SuperviorModel.getInstance().getAccTrainResultReport(e);
						break;
					}
					case ActionEventConstant.GSNPP_TRAINING_PLAN_DAY_REPORT: {
						SuperviorModel.getInstance().getGsnppTrainingResultDayReport(e);
						break;
					}
					case ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM: {
						SuperviorModel.getInstance().getFollowlistProblem(e);
						break;
					}
					case ActionEventConstant.GSNPP_TRAINING_PLAN_FOR_TBHV:
					case ActionEventConstant.GSNPP_TRAINING_PLAN: {
						SuperviorModel.getInstance().getGsnppTrainingPlan(e);
						break;
					}
					case ActionEventConstant.GET_REVIEWS_INFO_DONE: {
						SuperviorModel.getInstance().getReviewsDoneOfSTAFF(e);
						break;
					}
					case ActionEventConstant.UPDATE_OR_INSERT_REVIEWS_TO_DB: {
						SuperviorModel.getInstance().updateReviewsToDBAndServer(e);
						break;
					}
					case ActionEventConstant.GET_LIST_PRODUCT_ADD_REVIEWS_STAFF: {
						SuperviorModel.getInstance().getListProductToAddReviewsStaff(e);
						break;
					}
					case ActionEventConstant.UPDATE_GSNPP_FOLLOW_PROBLEM_DONE: {
						SuperviorModel.getInstance().updateGSNPPFollowProblemDone(e);
						break;
					}
					case ActionEventConstant.GET_WRONG_PLAN_CUSTOMER: {
						SuperviorModel.getInstance().getWrongCustomer(e);
						break;
					}
					case ActionEventConstant.GET_LIST_HISTORY: {
						SuperviorModel.getInstance().getListHistory(e);
						break;
					}
					case ActionEventConstant.GET_HISTORY_UPDATED_LOCATION: {
						SuperviorModel.getInstance().getCustomerHistoryUpdateLocation(e);
						break;
					}
					case ActionEventConstant.GET_LIST_POSITION_SALE_PERSONS:
						SuperviorModel.getInstance().getListPositionSalePersons(e);
						break;
					case ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE:
						SuperviorModel.getInstance().requestUpdateExceptionOrderDate(e);
						break;
					case ActionEventConstant.GET_LIST_PROBLEM_OF_GSNPP_NEED_DO_IT:
						SuperviorModel.getInstance().getListProblemOfGSNPP(e);
						break;
					case ActionEventConstant.ACTION_UPDATE_CHANGE_PROBLEM_STATUS:
						SuperviorModel.getInstance().requestUpdateFeedbackStatusOfGSNPP(e);
						break;
					case ActionEventConstant.GET_LESS_THAN_2_MINS:
						SuperviorModel.getInstance().requestGetLessThan2Mins(e);
						break;
					case ActionEventConstant.GET_GSNPP_IMAGE_LIST:
						SuperviorModel.getInstance().getGsnppImageList(e);
						break;
					case ActionEventConstant.GO_TO_LIST_ALBUM_USER:
						SuperviorModel.getInstance().getListAlbumUser(e);
						break;
					case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER:
						SuperviorModel.getInstance().getAlbumDetailUser(e);
						break;
					case ActionEventConstant.GET_PRODUCT_LIST:
						SuperviorModel.getInstance().getListProductStorage(e);
						break;
					case ActionEventConstant.GSNPP_GET_TIME_OF_HEADER_DEFINE_SHOP_PARAM:
						SuperviorModel.getInstance().getListTimeDefineApparamForReportVisitCustomer(e);
						break;
					case ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER:
						SuperviorModel.getInstance().getListReportNVBHVisitCustomer(e);
						break;
					case ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE:
						SuperviorModel.getInstance().getListSaleForAttendance(e);
						break;
					case ActionEventConstant.GO_TO_LIST_ALBUM_PROGRAME:
						SuperviorModel.getInstance().getListAlbumPrograme(e);
						break;
					case ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME:
						SuperviorModel.getInstance().getAlbumDetailPrograme(e);
						break;
					case ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH:
						SuperviorModel.getInstance().getReportCustomerNotPSDSForSupervisor(e);
						break;
					case ActionEventConstant.GET_CUSTOMER_LIST_OF_C2:
						SuperviorModel.getInstance().getListCustomerOfC2(e);
						break;
					case ActionEventConstant.GET_LIST_PRODUCT_FOR_C2_ORDER: {
						SuperviorModel.getInstance().getListProductForC2Order(e);
						break;
					}
					case ActionEventConstant.GET_LIST_PRODUCT_FOR_C2_PURCHASE: {
						SuperviorModel.getInstance().getListProductForC2Purchase(e);
						break;
					}
					case ActionEventConstant.SAVE_ORDER_FOR_C2: {
						SuperviorModel.getInstance().saveOrderForC2(e);
						break;
					}
					case ActionEventConstant.GET_LIST_CUSTOMER_C2: {
						SuperviorModel.getInstance().getListC2Customer(e);
						break;
					}
					case ActionEventConstant.SAVE_PURCHASE_FOR_C2: {
						SuperviorModel.getInstance().savePurchaseForC2(e);
						break;
					}
					case ActionEventConstant.GET_TRAINING_LIST_NVBH:
						SuperviorModel.getInstance().getTrainingListNVBH(e);
						break;
					case ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG:
						SaleModel.getInstance().checkVisitFromActionLog(e);
						break;
					default:// test
						// UserModel.getInstance().requestTest(e);
						break;
				}
				// }
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
				modelEvent.setIsSendLog(false);
				handleErrorModelEvent(modelEvent);
			}
		} else {
			handleErrorModelEvent(modelEvent);
		}
	}

	@Override
	public void handleSwitchActivity(ActionEvent e) {
		switch (e.action) {

		default:
			break;

		}
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
		case ActionEventConstant.GO_TO_CUSTOMER_ROUTE: {
			CustomerRouteView frag = CustomerRouteView.getInstance((Bundle) e.viewData);
			switchFragment(fm, frag, CustomerRouteView.TAG, false);
			break;
		}
		
		case ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH_SALE: {
			Bundle data = (Bundle) e.viewData;
			GSNPPCustomerNotPSDSInMonthView view = GSNPPCustomerNotPSDSInMonthView.getInstance(data);
			switchFragment(fm, view, GSNPPCustomerNotPSDSInMonthView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS_DETAIL: {
			NVBHGeneralStatisticsView genSta = NVBHGeneralStatisticsView.getInstance((Bundle)e.viewData);
			switchFragment(fm, genSta, NVBHGeneralStatisticsView.TAG, false);
			break;
		}
		case ActionEventConstant.ACTION_MANAGER_EQUIPMENT: {
			ManagerEquipmentView equipment = ManagerEquipmentView.getInstance((Bundle) e.viewData);
			switchFragment(fm, equipment, ManagerEquipmentView.TAG, true);
			break;
		}
		case ActionEventConstant.ACTION_GO_TO_TRACKING_CABINET_STAFF: {
			TrackingCabinetStaffView cabinet = TrackingCabinetStaffView.getInstance((Bundle) e.viewData);
			switchFragment(fm, cabinet, TrackingCabinetStaffView.TAG, false);
			break;
		}
		case ActionEventConstant.PROG_REPOST_SALE_FOCUS: {
			ProgressReportSalesFocusView cus = ProgressReportSalesFocusView.newInstance((Bundle) e.viewData);
			switchFragment(fm, cus, ProgressReportSalesFocusView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS: {
			GSNPPGeneralStatisticsView cus = GSNPPGeneralStatisticsView.getInstance((Bundle) e.viewData);
			switchFragment(fm, cus, GSNPPGeneralStatisticsView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_REPORT_PROGRESS_DATE_DETAIL_VIEW: {
			ReportProgressDateDetailView cus = ReportProgressDateDetailView.getInstance((Bundle) e.viewData);
			switchFragment(fm, cus, ReportProgressDateDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION: {
			GSBHRouteSupervisionView frag = GSBHRouteSupervisionView.newInstance();
			switchFragment(fm, frag, GSBHRouteSupervisionView.TAG, true);
			break;
		}
		case ActionEventConstant.ACC_SALE_PROG_REPORT: {
			AccSaleProgReportView accFrag =  AccSaleProgReportView.getInstance();
			switchFragment(fm, accFrag, AccSaleProgReportView.TAG, true);
			break;
		}
		case ActionEventConstant.DIS_PRO_COM_PROG_REPORT: {
			DisProComProgReportView disFrag =  DisProComProgReportView.getInstance();
			switchFragment(fm, disFrag, DisProComProgReportView.TAG, true);
			break;
		}
		case ActionEventConstant.STAFF_DIS_PRO_COM_PROG_REPORT: {
			@SuppressWarnings("unchecked")
			Vector<String> vector = (Vector<String>) e.viewData;
			String proId = vector.get(vector.indexOf(IntentConstants.INTENT_DISPLAY_PROGRAM_ID) + 1);
			String proCode = vector.get(vector.indexOf(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE) + 1);
			String proName = vector.get(vector.indexOf(IntentConstants.INTENT_DISPLAY_PROGRAM_NAME) + 1);
			StaffDisProComProgReportView disFrag = StaffDisProComProgReportView.getInstance();
			disFrag.disProId = proId;
			disFrag.disProCode = proCode;
			disFrag.disProName = proName;
			switchFragment(fm, disFrag, StaffDisProComProgReportView.TAG, false);
			break;
		}
		case ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW:{
			Bundle b = (Bundle) e.viewData;
			GsnppRouteSupervisionMapView disFrag = GsnppRouteSupervisionMapView.getInstance(b);
			switchFragment(fm, disFrag, GsnppRouteSupervisionMapView.TAG, false);
			break;
		}
		case ActionEventConstant.PROG_REPOST_PRO_DISP_DETAIL_SALE: {
			ProgReportProDispDetailSaleView cus = ProgReportProDispDetailSaleView.newInstance((Bundle) e.viewData);
			switchFragment(fm, cus, ProgReportProDispDetailSaleView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_DISPLAY_PROGRAM: {
			SuperVisorDisplayProgramView paymentOrder = SuperVisorDisplayProgramView.getInstance(1);
			switchFragment(fm, paymentOrder, SuperVisorDisplayProgramView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_PROMOTION_PROGRAM: {
			SuperVisorPromotionProgramView addCusFrag = SuperVisorPromotionProgramView.getInstance();
			switchFragment(fm, addCusFrag, SuperVisorPromotionProgramView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_LIST_CUSTOMER_ATTEND_PROGRAM: {
			break;
		}
		case ActionEventConstant.GSNPP_TRAINING_RESULT_ACC_REPORT: {
			GsnppTrainingResultAccReportView cus = GsnppTrainingResultAccReportView.newInstance();
			switchFragment(fm, cus, GsnppTrainingResultAccReportView.TAG, true);
			break;
		}
		case ActionEventConstant.GSNPP_TRAINING_PLAN_DAY_REPORT: {
			GSNPPTrainingResultDayReportView cus =  GSNPPTrainingResultDayReportView.newInstance(((Bundle) e.viewData));
			switchFragment(fm, cus, GSNPPTrainingResultDayReportView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM: {
			FollowProblemView cus = FollowProblemView.newInstance();
			switchFragment(fm, cus, FollowProblemView.TAG, true);
			break;
		}
		case ActionEventConstant.POST_FEEDBACK_GSNPP: {
			Bundle b = (Bundle) e.viewData;
			GSNPPPostFeedbackView view =  GSNPPPostFeedbackView.newInstance(b);
			switchFragment(fm, view, GSNPPPostFeedbackView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_LIST_PROBLEM_OF_GSNPP_NEED_DO_IT: {
			TrackAndFixProblemsOfGSNPPView cus = TrackAndFixProblemsOfGSNPPView.newInstance(new Bundle());
			switchFragment(fm, cus, TrackAndFixProblemsOfGSNPPView.TAG, true);
			break;
		}
		case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME: {
			SuperVisorPromotionProgramView cus = SuperVisorPromotionProgramView.getInstance();
			switchFragment(fm, cus, SuperVisorPromotionProgramView.TAG, true);
			break;
		}
		case ActionEventConstant.GSNPP_TRAINING_PLAN: {
			GSNPPTrainingPlanView cus = GSNPPTrainingPlanView.newInstance();
			switchFragment(fm, cus, GSNPPTrainingPlanView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW: {
			ReviewsStaffView cus = ReviewsStaffView.getInstance();
			switchFragment(fm, cus, ReviewsStaffView.TAG, false);
			break;
		}
		case ActionEventConstant.STAFF_INFORMATION: {
			Bundle bd = (Bundle) e.viewData;
			StaffInformationView staff = StaffInformationView.getInstance(bd);
			switchFragment(fm, staff, StaffInformationView.TAG, false);
			break;
		}
		case ActionEventConstant.SUPERVISE_STAFF_POSITION: {
			Bundle b = (Bundle) e.viewData;
			StaffPositionView staff = StaffPositionView.getInstance(b);
			switchFragment(fm, staff, StaffPositionView.TAG, true);
			break;
		}
		case ActionEventConstant.SUPERVISE_STAFF_POSITION2: {
			Bundle b = (Bundle) e.viewData;
			StaffPositionView staff =StaffPositionView.getInstance(b);
			switchFragment(fm, staff, StaffPositionView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_CUSTOMER_SALE_LIST: {
			CustomerSaleList staff = CustomerSaleList.getInstance();
			switchFragment(fm, staff, CustomerSaleList.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_REPORT_VISIT_CUSTOMER_ON_PLAN: {
			Bundle b = (Bundle) e.viewData;
			GSNPPReportVisitCustomerOnPlanOfDayView staff = GSNPPReportVisitCustomerOnPlanOfDayView.getInstance(b);
			switchFragment(fm, staff, GSNPPReportVisitCustomerOnPlanOfDayView.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_CUSTOMER_SALE_LOCATION_RESET: {
			CustomerSaleLocationResetView cus = CustomerSaleLocationResetView.newInstance((Bundle) e.viewData);
			switchFragment(fm, cus, CustomerSaleLocationResetView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_GSNPP_IMAGE_LIST: {
			SupervisorImageListView statisticsProduct = SupervisorImageListView.getInstance((Bundle) e.viewData);
			switchFragment(fm, statisticsProduct, SupervisorImageListView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_LIST_ALBUM_USER: {
			Bundle data = (Bundle) e.viewData;
			ListAlbumUserView listAlbum =  ListAlbumUserView.getInstance(data);
			switchFragment(fm, listAlbum, ListAlbumUserView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER: {
			Bundle data = (Bundle) e.viewData;
			SupervisorPhotoThumbnailListUserView listAlbum = SupervisorPhotoThumbnailListUserView.getInstance(data);
			switchFragment(fm, listAlbum, SupervisorPhotoThumbnailListUserView.TAG, false);
			break;
		} 
		case ActionEventConstant.GO_TO_PRODUCT_LIST: {
			SupervisorProductListView listProduct = SupervisorProductListView.newInstance();
			switchFragment(fm, listProduct, SupervisorProductListView.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_PRODUCT_INTRODUCTION: {
			Bundle data = (Bundle) e.viewData;
			SupervisorIntroduceProductView statisticsProduct = SupervisorIntroduceProductView.newInstance(data);
			switchFragment(fm, statisticsProduct, SupervisorIntroduceProductView.TAG, false);
			break;
		}
		case ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE: {
			SuperVisorTakeAttendanceView statisticsProduct = SuperVisorTakeAttendanceView.getInstance();
			switchFragment(fm, statisticsProduct, SuperVisorTakeAttendanceView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME: {
			Bundle data = (Bundle) e.viewData;
			SupervisorPhotoThumbnailListProgrameView listAlbum = SupervisorPhotoThumbnailListProgrameView.getInstance(data);
			switchFragment(fm, listAlbum, SupervisorPhotoThumbnailListProgrameView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_CUSTOMER_LIST_OF_C2: {
			Bundle data = (Bundle) e.viewData;
			CustomerOfC2ListView listAlbum = CustomerOfC2ListView.getInstance(data);
			switchFragment(fm, listAlbum, CustomerOfC2ListView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_CUSTOMER_LIST_C2: {
			Bundle data = (Bundle) e.viewData;
			CustomerC2ListView listAlbum = CustomerC2ListView.getInstance(data);
			switchFragment(fm, listAlbum, CustomerC2ListView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_TRAINING_LIST: {
			Bundle data = (Bundle) e.viewData;
			TrainingListView listAlbum = TrainingListView.newInstance(data);
			switchFragment(fm, listAlbum, TrainingListView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_REPORT_CUSTOMER_C2: {
			Bundle data = (Bundle) e.viewData;
			ReportCustomerC2View listAlbum = ReportCustomerC2View.getInstance(data);
			switchFragment(fm, listAlbum, ReportCustomerC2View.TAG, true);
			break;
		}
		default:
			break;

		}
	}
}
