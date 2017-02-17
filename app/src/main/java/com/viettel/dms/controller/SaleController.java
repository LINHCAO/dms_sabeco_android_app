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

import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.model.SaleModel;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CreateCustomerView;
import com.viettel.dms.view.sale.customer.CustomerFeedBackListView;
import com.viettel.dms.view.sale.customer.CustomerListView;
import com.viettel.dms.view.sale.customer.PostFeedbackView;
import com.viettel.dms.view.sale.debit.CustomerDebitDetailView;
import com.viettel.dms.view.sale.debit.CustomerDebtView;
import com.viettel.dms.view.sale.debit.PaymentVoucherView;
import com.viettel.dms.view.sale.image.ImageListView;
import com.viettel.dms.view.sale.image.ListAlbumUserView;
import com.viettel.dms.view.sale.image.PhotoThumbnailListView;
import com.viettel.dms.view.sale.image.PhotoThumbnailListViewForSearch;
import com.viettel.dms.view.sale.order.IntroduceProductView;
import com.viettel.dms.view.sale.order.VoteDisplayPresentProductView;
import com.viettel.dms.view.sale.promotion.CustomerAttendProgramListView;
import com.viettel.dms.view.sale.promotion.CustomerProgrameDoneView;
import com.viettel.dms.view.sale.promotion.PaidShowRoomPromotionView;
import com.viettel.dms.view.sale.promotion.PromotionProgramView;
import com.viettel.dms.view.sale.promotion.ReviewShowRoomFragment;
import com.viettel.dms.view.sale.promotion.SaleSupportProgramDetailView;
import com.viettel.dms.view.sale.statistic.NVBHCollectBSGSale;
import com.viettel.dms.view.sale.statistic.NVBHCollectOpponentSale;
import com.viettel.dms.view.sale.statistic.NVBHCustomerNotPSDSInMonthView;
import com.viettel.dms.view.sale.statistic.NVBHGeneralStatisticsView;
import com.viettel.dms.view.sale.statistic.NoteListView;

/**
 * Controller xu ly nghiep vu NVBH
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class SaleController extends AbstractController {

	static SaleController instance;

	protected SaleController() {
	}

	public static SaleController getInstance() {
		if (instance == null) {
			instance = new SaleController();
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
				case ActionEventConstant.GET_MAX_CUSTOMER_AVATAR_ID:
					SaleModel.getInstance().getMaxCustomerAvatarID(e);
					break;
				case ActionEventConstant.DELETE_AVATAR:
					SaleModel.getInstance().deleteAvatar(e);
					break;
				case ActionEventConstant.UPDATE_AVATAR:
					SaleModel.getInstance().updateAvatar(e);
					break;
				case ActionEventConstant.INSERT_NEW_AVATAR:
					SaleModel.getInstance().insertNewAvatar(e);
					break;
				case ActionEventConstant.INSERT_NEW_AVATAR_ALBUM:
					SaleModel.getInstance().insertNewAvatarFromAlbum(e);
					break;
				case ActionEventConstant.CREATE_CUSTOMER:
					SaleModel.getInstance().insertCustomer(e);
					break;
				case ActionEventConstant.UPDATE_CUSTOMER:
					SaleModel.getInstance().updateCustomerInfo(e);
					break;
				case ActionEventConstant.DELETE_CUSTOMER:
					SaleModel.getInstance().deleteCustomerInfo(e);
					break;
				case ActionEventConstant.GET_CREATE_CUSTOMER_AREA_INFO:
					SaleModel.getInstance().getCreateCustomerAreaInfo(e);
					break;
				case ActionEventConstant.GET_CREATE_CUSTOMER_INFO:
					SaleModel.getInstance().getCreateCustomerInfo(e);
					break;
				case ActionEventConstant.GET_AREA_CUSTOMER_INFO:
					SaleModel.getInstance().getAreaCustomerInfo(e);
					break;
				case ActionEventConstant.GET_AREA_CUSTOMER_INFO_NEW_CUSTOMER:
					SaleModel.getInstance().getAreaCustomerInfoNewCus(e);
					break;
				case ActionEventConstant.GET_LIST_NEW_CUSTOMER:
					SaleModel.getInstance().getListNewCustomer(e);
					break;
				case ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_TNPG:
					SaleModel.getInstance().getGeneralStatisticsReportInfoForTTTT(e);
					break;
				case ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH:
					SaleModel.getInstance().getReportCustomerNotPSDSForSaleMan(e);
					break;
				case ActionEventConstant.REVIEW_SHOWROOM_TOOL_LIST:
					SaleModel.getInstance().getReviewShowroom(e);
					break;
				case ActionEventConstant.REVIEW_SHOWROOM_GOODS_LIST:
					SaleModel.getInstance().getReviewShowroom_GoodList(e);
					break;
				case ActionEventConstant.PAID_PROMOTION_LIST:
					SaleModel.getInstance().getPaidPromotionList(e);
					break;
				case ActionEventConstant.PAY_DEBT:
					SaleModel.getInstance().payDebt(e);
					break;
				case ActionEventConstant.GET_LAST_SALE_ORDERS:
					SaleModel.getInstance().getLastSaleOrders(e);
					break;
				case ActionEventConstant.GET_REMAIN_PRODUCT:
					SaleModel.getInstance().getRemainProduct(e);
					break;
				case ActionEventConstant.GET_PRODUCT_LIST:
					SaleModel.getInstance().getProductList(e);
					break;
				case ActionEventConstant.GET_LIST_DISPLAY_PROGRAM_PRODUCT:
					SaleModel.getInstance().requestGetListDisplayProgramProduct(e);
					break;
				case ActionEventConstant.GET_VOTE_DISPLAY_PROGRAME_VIEW: {
					SaleModel.getInstance().requestGetVoteDisplayProgrameView(e);
					break;
				}
				case ActionEventConstant.GET_LIST_VOTE_DISPLAY_PRODUCT:
					SaleModel.getInstance().requestGetListVoteDisplayProduct(e);
					break;
				case ActionEventConstant.SAVE_VOTE_DISPLAY_PRESENT_PRODUCT:
					SaleModel.getInstance().requestUpdateVoteProductDisplay(e);
					break;
				case ActionEventConstant.GET_LIST_CATEGORY_CODE_PRODUCT:
					SaleModel.getInstance().getListCategoryCodeProduct(e);
					break;
				case ActionEventConstant.GET_PRODUCTS_TO_ADD_ORDER:
					SaleModel.getInstance().requestGetListProductAddToOrderView(e);
					break;
				case ActionEventConstant.GET_INIT_LIST_PRODUCT_ADD_TO_ORDER_VIEW:
					SaleModel.getInstance().requestGetInitListProductAddToOrderView(e);
					break;
				case ActionEventConstant.GET_LIST_CUSTOMER_ATTENT_PROGRAME:
					SaleModel.getInstance().getListCustomerAttentPrograme(e);
					break;
				case ActionEventConstant.GET_LIST_PROGRAME_FOR_PRODUCT:
					SaleModel.getInstance().getListProgrameForProduct(e);
					break;
				case ActionEventConstant.GET_INTRODUCE_PRODUCT:
					SaleModel.getInstance().getIntroduceProduct(e);
					break;
				case ActionEventConstant.GET_LIST_COMBOBOX_DISPLAY_PROGRAME:
					SaleModel.getInstance().getListComboboxDisplayPrograme(e);
					break;
				case ActionEventConstant.UPDATE_CLIENT_THUMNAIL_URL:
					SaleModel.getInstance().updateClientThumbnailUrl(e);
					break;
				case ActionEventConstant.GO_TO_LIST_ALBUM_USER:
					SaleModel.getInstance().getListAlbumUser(e);
					break;
				case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER:
					SaleModel.getInstance().getAlbumDetailUser(e);
					break;
				case ActionEventConstant.NO_SUCCESS_ORDER_LIST:
					SaleModel.getInstance().getNoSuccessOrderList(e);
					break;
				case ActionEventConstant.ACTION_GET_ALL_ORDER_FAIL:
				case ActionEventConstant.ACTION_GET_ALL_ORDER_FAIL_FOR_WARNING:
					SaleModel.getInstance().requestGetAllOrderFail(e);
					break;
				case ActionEventConstant.GET_LIST_INDUSTRY_PRODUCT_AND_LIST_PRODUCT:
					SaleModel.getInstance().requestGetListIndustryProductAndListProduct(e);
					break;
				case ActionEventConstant.GET_LIST_PRODUCT_PRE_SALE_SOLD:
					SaleModel.getInstance().requestListProductPreSaleSold(e);
					break;
				case ActionEventConstant.GET_LIST_PRODUCT_VAN_SALE_SOLD:
					SaleModel.getInstance().requestListProductVanSaleSold(e);
					break;
				case ActionEventConstant.GET_LIST_PRODUCT_SALE_STATISTICS_ACCUMULATE_DAY:
					SaleModel.getInstance().requestSaleStatisticsAccumulateDay(e);
					break;
				case ActionEventConstant.GET_COUNT_SALE_STATISTICS_ACCUMULATE_DAY_LIST_PRODUCT:
					SaleModel.getInstance().requestCountSaleStatisticsAccumulateDayListProduct(e);
					break;
				case ActionEventConstant.GET_SALE_STATISTICS_ACCUMULATE_DAY_LIST_PRODUCT:
					SaleModel.getInstance().requestSaleStatisticsAccumulateDayListProduct(e);
					break;
				case ActionEventConstant.CREATE_NEW_ORDER:
					SaleModel.getInstance().requestCreateOrder(e);
					break;
				case ActionEventConstant.EDIT_AND_SEND_ORDER:
					SaleModel.getInstance().requestEditAndSendOrder(e);
					break;
				case ActionEventConstant.GET_LIST_PROMOTION_PROGRAME:
					SaleModel.getInstance().requestGetListPromotionPrograme(e);
					break;
				case ActionEventConstant.GET_LIST_DISPLAY_PROGRAM:
					SaleModel.getInstance().requestGetListDisplayPrograme(e);
					break;
				case ActionEventConstant.GET_CUSTOMER_LIST:
					SaleModel.getInstance().getListCustomer(e);
					break;
				case ActionEventConstant.GET_CUSTOMER_LIST_FOR_OPPONENT_SALE:
					SaleModel.getInstance().getListCustomerForOpponentSale(e);
					break;
				case ActionEventConstant.CHECK_VISIT_FROM_ACTION_LOG:
					SaleModel.getInstance().checkVisitFromActionLog(e);
					break;
				case ActionEventConstant.GET_CUSTOMER_LIST_FOR_ROUTE:
					SaleModel.getInstance().getCustomerListForRoute(e);
					break;
				case ActionEventConstant.TBHV_CUSTOMER_LIST_FOR_SUPERVISION:
					SaleModel.getInstance().getTBHVCustomerInVisitPlan(e);
					break;
				case ActionEventConstant.GET_SALE_ORDER:
					SaleModel.getInstance().getListSalesOrderInDate(e);
					break;
				case ActionEventConstant.GET_ORDER_FOR_EDIT:
					SaleModel.getInstance().getOrderForEdit(e);
					break;
				case ActionEventConstant.NOTE_LIST_VIEW:
				case ActionEventConstant.GET_LIST_CUS_FEED_BACK:
					SaleModel.getInstance().getListCusFeedBack(e);
					break;
				case ActionEventConstant.GET_CUSTOMER_BASE_INFO:
					SaleModel.getInstance().getCustomerBaseInfo(e);
					break;
				case ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_SALEMAN:
					SaleModel.getInstance().getGeneralStatisticsReportInfoForSaleman(e);
					break;
				case ActionEventConstant.GET_GENERAL_STATISTICS_REPORT_INFO_FOR_SUPERVIOR:
					SaleModel.getInstance().getGeneralStatisticsReportInfoForSupervior(e);
					break;
				case ActionEventConstant.GET_PROMOTION_PRODUCT_FROM_SALE_PRODUCT:
					SaleModel.getInstance().getPromotionProducts(e);
					break;
				case ActionEventConstant.DELETE_SALE_ORDER:
					SaleModel.getInstance().deleteSaleOrder(e);
					break;
				case ActionEventConstant.POST_FEEDBACK:
					SaleModel.getInstance().postFeedBack(e);
					break;
				case ActionEventConstant.POST_LIST_FEEDBACK:
					SaleModel.getInstance().postListFeedBack(e);
					break;
				case ActionEventConstant.UPDATE_FEEDBACK:
					SaleModel.getInstance().updateDoneDateFeedBack(e);
					break;
				case ActionEventConstant.DELETE_FEEDBACK:
					SaleModel.getInstance().deleteFeedBack(e);
					break;
				case ActionEventConstant.START_INSERT_ACTION_LOG:
					SaleModel.getInstance().inserVisitActionLogToDB(e);
					break;
				case ActionEventConstant.INSERT_ACTION_LOG:
					SaleModel.getInstance().inserActionLogToDB(e);
					break;
				case ActionEventConstant.DELETE_ACTION_LOG:
					SaleModel.getInstance().deleteActionLogToDB(e);
					break;
				case ActionEventConstant.UPDATE_ACTION_LOG:
					SaleModel.getInstance().updateActionLogToDB(e);
					break;
				case ActionEventConstant.SAVE_NUMBER_REMAIN_PRODUCT:
					SaleModel.getInstance().requestSaveRemainProduct(e);
					break;
				case ActionEventConstant.GET_COMMON_DATA_ORDER:
					SaleModel.getInstance().getCommonDataInOrder(e);
					break;
				case ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL:
					SaleModel.getInstance().getPromotionDetail(e);
					break;
				case ActionEventConstant.UPDATE_CUSTOMER_LOATION:
					SaleModel.getInstance().requestUpdateCustomerLocation(e);
					break;
				case ActionEventConstant.GET_LIST_IMAGE_CUS:
					SaleModel.getInstance().getCusImageList(e);
					break;
				case ActionEventConstant.INSERT_MEDIA_ITEM:
					SaleModel.getInstance().requestInsertMediaItem(e);
					break;
				case ActionEventConstant.GO_TO_PRODUCT_INFO_DETAIL:
					SaleModel.getInstance().requestGetProductInfoDetail(e);
					break;
				case ActionEventConstant.GET_ORDER_IN_LOG:
					SaleModel.getInstance().requestGetOrderInLog(e);
					break;
				case ActionEventConstant.ACTION_GET_CUSTOMER_SALE_LIST:
					SaleModel.getInstance().requestGetCustomerSaleList(e);
					break;
				case ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE:
					SaleModel.getInstance().requestUpdateExceptionOrderDate(e);
					break;
				case ActionEventConstant.GET_LIST_TYPE_PROBLEM_NVBH_GSNPP:
					SaleModel.getInstance().getListTypeProblemNVBHGSNPP(e);
					break;
				case ActionEventConstant.GO_TO_IMAGE_LIST_GSNPP:
					SaleModel.getInstance().getCusImageListGSNPP(e);
					break;
				case ActionEventConstant.ACTION_GET_CUSTOMER_DEBT:
					SaleModel.getInstance().requestGetCustomerDebt(e);
					break;
				case ActionEventConstant.GET_CUS_DEBIT_DETAIL:
					SaleModel.getInstance().requestDebitDetail(e);
					break;
				case ActionEventConstant.GET_TYPE_FEEDBACK:
					SaleModel.getInstance().requestGetTypeFeedback(e);
					break;
				case ActionEventConstant.GET_CUSTOMER_NO_PSDS:
					SaleModel.getInstance().requestGetCusNoPSDS(e);
					break;
				case ActionEventConstant.GO_TO_LIST_ALBUM_PROGRAME:
					SaleModel.getInstance().gotoListAlbumPrograme(e);
					break;
				case ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME:
					SaleModel.getInstance().getAlbumDetailPrograme(e);
					break;
				case ActionEventConstant.GO_TO_ALBUM_DETAIL_FOR_SEARCH:
					SaleModel.getInstance().getAlbumDetailForSearch(e);
					break;
				case ActionEventConstant.ACTION_GET_SALE_SUPPORT_PROGRAM_INFO: {
					SaleModel.getInstance().getSaleSupportProgramInfo(e);
					break;
				}
				case ActionEventConstant.GET_SALE_SUPPORT_PROGROGAM_LEVEL: {
					SaleModel.getInstance().getSaleSupportProgramLevel(e);
					break;
				}
				case ActionEventConstant.GET_LIST_CUSTOMER_ATTEND: {
					SaleModel.getInstance().getCustomerListAttendProgram(e);
					break;
				}
				case ActionEventConstant.GET_LIST_CUSTOMER_C2_ON_PLAN: {
					SaleModel.getInstance().getListC2Customer(e);
					break;
				}
				case ActionEventConstant.GET_INFORMATION_COMPETITOR:
					SaleModel.getInstance().getInformationCompetitor(e);
					break;
				case ActionEventConstant.GET_INFORMATION_BSG:
					SaleModel.getInstance().getInformationBSG(e);
					break;
				case ActionEventConstant.SAVE_SALE_COMPETITOR:
					SaleModel.getInstance().saveSaleCompetitor(e);
					break;
				case ActionEventConstant.GET_LIST_CUSTOMER_ADD_SUPPORT_SALES:
					SaleModel.getInstance().getAddCustomerList(e);
					break;
				case ActionEventConstant.DELETE_CUSTOMER_JOIN_HTBH:
					SaleModel.getInstance().deleteCustomerJoinHTBH(e);
					break;
				case ActionEventConstant.GET_PRODUCT_LIST_JOIN:
					SaleModel.getInstance().getListProductForJoin(e);
					break;
				case ActionEventConstant.GET_DETAIL_PROGRAME:
					SaleModel.getInstance().getDetailPrograme(e);
					break;
				case ActionEventConstant.GET_DETAIL_PROGRAME_DONE:
					SaleModel.getInstance().getDetailProgrameDone(e);
					break;
				case ActionEventConstant.SAVE_CHOOSE_CUSTOMER_ATTEND:
					SaleModel.getInstance().saveCustomerAttend(e);
					break;
				case ActionEventConstant.SAVE_JOIN_PRODUCT:
					SaleModel.getInstance().saveJoinProduct(e);
					break;
				case ActionEventConstant.ACTION_GET_ALL_PROGRAME_NEED_TYPE_QUANTITY:
					SaleModel.getInstance().getAllProgrameNeedTypeQuantity(e);
					break;
				case ActionEventConstant.GET_LIST_CUSTOMER_DONE_PROGRAME:
					SaleModel.getInstance().getListCustomerDonePrograme(e);
					break;
				case ActionEventConstant.SAVE_DONE_PRODUCT:
					SaleModel.getInstance().saveDoneProduct(e);
					break;
				case ActionEventConstant.GET_INPUT_QUANTITY_DONE:
					SaleModel.getInstance().getListProductDonePrograme(e);
					break;
				case ActionEventConstant.ACTION_GET_CUSTOMER_SALE_LIST_TRAINING:
					SaleModel.getInstance().requestGetCustomerSaleListTraining(e);
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
			// ActionEvent aEvent = null;
			// if (e.action ==
			// ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER &&
			// e.userData != null){
			// aEvent = (ActionEvent)e.userData;
			// }
			// if (aEvent != null && aEvent.isNeedCheckTimeServer ) {
			// aEvent.isCheckTimeSuccess = true;
			// aEvent.controller.handleViewEvent(aEvent);
			// }else{
			if (e.sender != null) {
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

		case ActionEventConstant.GO_TO_VIEW_CUSTOMER_INFO: {
			CreateCustomerView frag = CreateCustomerView.newInstance((Bundle) e.viewData);
			switchFragment(fm, frag, CreateCustomerView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_CREATE_CUSTOMER: {
			CreateCustomerView frag = CreateCustomerView.newInstance((Bundle) e.viewData);
			switchFragment(fm, frag, CreateCustomerView.TAG, false);
			break;
		}
		case ActionEventConstant.GOTO_COLLECT_OPPONENT_SALE: {
			Bundle data = (Bundle) e.viewData;
			NVBHCollectOpponentSale frag = NVBHCollectOpponentSale.newInstance(data);
			switchFragment(fm, frag, NVBHCollectOpponentSale.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_COLLECT_BSG_SALE: {
			Bundle data = (Bundle) e.viewData;
			NVBHCollectBSGSale frag = NVBHCollectBSGSale.newInstance(data);
			switchFragment(fm, frag, NVBHCollectBSGSale.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_GENERAL_STATISTICS: {
			Bundle data = (Bundle) e.viewData;
			NVBHGeneralStatisticsView frag = NVBHGeneralStatisticsView.getInstance(data);
			switchFragment(fm, frag, NVBHGeneralStatisticsView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_NVBH_NEED_DONE_VIEW:{
			NoteListView frag = NoteListView.newInstance();
			switchFragment(fm, frag, NoteListView.TAG, true);
			break;
		}
		case ActionEventConstant.REVIEW_SHOWROOM:{
			ReviewShowRoomFragment reviewFrag = ReviewShowRoomFragment.getInstance();
			switchFragment(fm, reviewFrag, ReviewShowRoomFragment.TAG, true);
			break;
		}
		case ActionEventConstant.PAID_PROMOTION_LIST:{
			PaidShowRoomPromotionView reviewFrag = PaidShowRoomPromotionView.getInstance();
			switchFragment(fm, reviewFrag, PaidShowRoomPromotionView.TAG, true);
			break;
		}
		case ActionEventConstant.GOTO_VOTE_DISPLAY_PRESENT_PRODUCT: {
			Bundle data = (Bundle) e.viewData;
			VoteDisplayPresentProductView statisticsProduct = VoteDisplayPresentProductView.getInstance(data);
			switchFragment(fm, statisticsProduct, VoteDisplayPresentProductView.TAG, false);
			break;
		}
		// GOTO_PRODUCT_INTRODUCTION
		case ActionEventConstant.GOTO_PRODUCT_INTRODUCTION: {
			Bundle data = (Bundle) e.viewData;
			IntroduceProductView statisticsProduct = IntroduceProductView.newInstance(data);
			switchFragment(fm, statisticsProduct, IntroduceProductView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_LIST_ALBUM_USER: {
			Bundle data = (Bundle) e.viewData;
			ListAlbumUserView listAlbum = ListAlbumUserView.getInstance(data);
			switchFragment(fm, listAlbum, ListAlbumUserView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_USER: {
			Bundle data = (Bundle) e.viewData;
			PhotoThumbnailListView listAlbum = PhotoThumbnailListView.getInstance(data);
			switchFragment(fm, listAlbum, PhotoThumbnailListView.TAG, false);
			break;
		}
		case ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH_SALE: {
			Bundle data = (Bundle) e.viewData;
			NVBHCustomerNotPSDSInMonthView view = NVBHCustomerNotPSDSInMonthView.getInstance(data);
			switchFragment(fm, view, NVBHCustomerNotPSDSInMonthView.TAG, true);
			break;
		}
		case ActionEventConstant.GET_CUSTOMER_LIST: {
			Bundle data = (Bundle) e.viewData;
			CustomerListView view = CustomerListView.newInstance(data);
			switchFragment(fm, view, CustomerListView.TAG, true);
			break;
		}
		case ActionEventConstant.NOTE_LIST_VIEW:{
			NoteListView view = NoteListView.newInstance();
			switchFragment(fm, view, NoteListView.TAG, false);
			break;
		}
		case ActionEventConstant.GET_LIST_CUS_FEED_BACK: {
			Bundle b = (Bundle) e.viewData;
			CustomerFeedBackListView cusFbFrag =  CustomerFeedBackListView.newInstance(b);
			switchFragment(fm, cusFbFrag, CustomerFeedBackListView.TAG, false);
			break;
		}
		case ActionEventConstant.GET_LIST_IMAGE_CUS:{
			ImageListView cusFbFrag =  ImageListView.getInstance();
			switchFragment(fm, cusFbFrag, ImageListView.TAG, true);
			break;
		}
		case ActionEventConstant.POST_FEEDBACK: {
			Bundle b = (Bundle) e.viewData;
			PostFeedbackView view = PostFeedbackView.newInstance(b);
			switchFragment(fm, view, PostFeedbackView.TAG, false);
			break;
		}
		case ActionEventConstant.GOTO_CUSTOMER_DEBT_LIST: {
			CustomerDebtView view = CustomerDebtView.newInstance();
			switchFragment(fm, view, CustomerDebtView.TAG, true);
			break;
		}
		case ActionEventConstant.GET_CUS_DEBIT_DETAIL: {
			CustomerDebtDTO dto = (CustomerDebtDTO) e.viewData;
			CustomerDebitDetailView view = CustomerDebitDetailView.newInstance(dto);
			switchFragment(fm, view, CustomerDebitDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_ALBUM_DETAIL_PROGRAME: {
			Bundle data = (Bundle) e.viewData;
			PhotoThumbnailListViewForSearch listAlbum = PhotoThumbnailListViewForSearch.getInstance(data);
			switchFragment(fm, listAlbum, PhotoThumbnailListViewForSearch.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_LIST_ALBUM_USER_FOR_SEARCH: {
			Bundle data = (Bundle) e.viewData;
			PhotoThumbnailListViewForSearch listAlbum = PhotoThumbnailListViewForSearch.getInstance(data);
			switchFragment(fm, listAlbum, PhotoThumbnailListViewForSearch.TAG, false);
			break;
		}
		case ActionEventConstant.PAYMENT_VOUCHER: {
			Bundle data = (Bundle) e.viewData;
			PaymentVoucherView frag = PaymentVoucherView.newInstance(data);
			switchFragment(fm, frag, PaymentVoucherView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_PROMOTION_PROGRAM: {
			PromotionProgramView addCusFrag = PromotionProgramView.getInstance();
			switchFragment(fm, addCusFrag, PromotionProgramView.TAG, true);
			break;
		}
		case ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL: {
			SaleSupportProgramDetailView addCusFrag = SaleSupportProgramDetailView.getInstance((Bundle) e.viewData);
			switchFragment(fm, addCusFrag, SaleSupportProgramDetailView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DONE: {
			CustomerProgrameDoneView addCusFrag = CustomerProgrameDoneView.newInstance((Bundle) e.viewData);
			switchFragment(fm, addCusFrag, CustomerProgrameDoneView.TAG, false);
			break;
		}
		case ActionEventConstant.GO_TO_CUSTOMER_ATTEND_PROGRAM: {
			CustomerAttendProgramListView cusFrag = CustomerAttendProgramListView.newInstance((Bundle) e.viewData);
			switchFragment(fm, cusFrag, CustomerAttendProgramListView.TAG, false);
			break;
		}
		}

	}

}
