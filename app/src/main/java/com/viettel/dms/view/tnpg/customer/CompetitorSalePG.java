/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.customer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.db.ProductCompetitorListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Competitor Sale PG
 * CompetitorSalePG.java
 * @version: 1.0 
 * @since:  08:30:49 20 Jan 2014
 */
public class CompetitorSalePG extends BaseFragment {
	public static final String TAG = CompetitorSalePG.class.getName();
	private GlobalBaseActivity parent; // parent
	// private Button btSaveCompetProduct;
	private LinearLayout llCompetProductList;
	// private String customerCode;
	private long staffID;
	// =0: kiem ton, =1: san luong ban doi thu
	private int type;
	private ProductCompetitorListDTO listProductCompetitor;
	private double lat = -1;
	private double lng = -1;
	public static final int ACTION_POS_OK = 1;
	private static final int ACTION_OK_2 = 2;
	private CountDownTimer timer = null;
	private CustomerListItem customerListItem;
	//private double distance;
	private static final int ACTION_SAVE_COMPETITOR = 3;
	private static final int ACTION_CANCEL_COMPETITOR = 4;
	private static final int ACTION_END_VISIT_COMPETITOR = 5;
	private static final int ACTION_VISIT_CUSTOMER = 6;
	// dong y back
	public static final int ACTION_AGRRE_BACK = 7;
	// tu choi back
	public static final int ACTION_CANCEL_BACK = 8;
	private static final int ACTION_SAVE = 9;
	private static final int ACTION_NOTIFY_TAKE_CHECK_REMAIN_COMPETITOR_OK = 10;
	private static final int ACTION_NOTIFY_TAKE_CHECK_SALE_COMPETITOR_OK = 11;
	private static final int ACTION_FINISH_VISIT_CUS_OK = 16;
	private String beginVisitTime;
	private String prefix = "";

	public static CompetitorSalePG getInstance(Bundle b) {
		CompetitorSalePG f = new CompetitorSalePG();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_competitor_salepg, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		v.setDrawingCacheEnabled(true);
		v.buildDrawingCache();
		lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
				.getLatitude();
		lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
				.getLongtitude();
		llCompetProductList = (LinearLayout) v.findViewById(R.id.llCompetitor);
		if (getArguments().getSerializable(
				IntentConstants.INTENT_CUSTOMER_LIST_ITEM) != null) {
			customerListItem = (CustomerListItem) getArguments()
					.getSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM);
		}
		if (getArguments().getLong(IntentConstants.INTENT_STAFF_ID) > 0) {
			staffID = getArguments().getLong(IntentConstants.INTENT_STAFF_ID);
		}
		beginVisitTime = DateUtils.now();
		type = getArguments().getInt(IntentConstants.INTENT_TYPE, 0);
		enableMenuBar(this);
		if (type == Constants.CHECK_REMAIN_COMPETITOR) {
			prefix = "02-04-04";
			setTitleHeaderView(StringUtil
					.getString(R.string.TEXT_HEADER_TITLE_CHECK_REMAIN_COMPETITOR));
			addMenuItem(StringUtil.getString(R.string.TEXT_SAVE_REMAIN),
					R.drawable.icon_save, ACTION_SAVE);
		} else if (type == Constants.CHECK_SALE_COMPETITOR) {
			prefix = "02-04-05";
			setTitleHeaderView(StringUtil
					.getString(R.string.TEXT_HEADER_TITLE_CHECK_SALE_COMPETITOR));
			addMenuItem(StringUtil.getString(R.string.TEXT_SAVE_SALE),
					R.drawable.icon_save, ACTION_SAVE);
		}
		if (customerListItem != null && customerListItem.hasTodayTakeAttendance) {
			parent.removeMenuCloseCustomer();
		}
		if (lat > 0 && lng > 0) {
			getInformationCompetitor();
		}
		restartLocatingUpdate();
		return v;
	}

	/**
	 * Kiem tra xem co can kiem hang ton ko!?
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	private void checkRemained() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_CUSTOMER_ID,customerListItem.aCustomer.getCustomerId());
		data.putLong(IntentConstants.INTENT_STAFF_ID, staffID);
		e.viewData = data;
		e.sender = this;
		if (type == Constants.CHECK_REMAIN_COMPETITOR) {
			e.action = ActionEventConstant.CHECK_REMAINED_COMPETITOR;
		} else if (type == Constants.CHECK_SALE_COMPETITOR) {
			e.action = ActionEventConstant.CHECK_SALED_COMPETITOR;
		}
		e.isNeedCheckTimeServer = false;
		TNPGController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay thong tin doi thu canh tranh
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	private void getInformationCompetitor() {

		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_INFORMATION_COMPETITOR;
		e.isNeedCheckTimeServer = false;
		TNPGController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {

		ActionEvent actionEvent = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_INFORMATION_COMPETITOR:
			listProductCompetitor = (ProductCompetitorListDTO) modelEvent
					.getModelData();
			renderLayout();
			break;
		case ActionEventConstant.SAVE_SALE_COMPETITOR:
			parent.requestInsertActionLog(beginVisitTime, ActionLogDTO.TYPE_SALE_COMPETITOR, null,
					customerListItem.aCustomer.customerId,
					String.valueOf(customerListItem.isOr));
			parent.removeMenuCloseCustomer();
			GlobalUtil.showDialogConfirm(this, parent, StringUtil
					.getString(R.string.TEXT_SAVE_SALE_COMPETITOR_SUCCESS),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					ACTION_NOTIFY_TAKE_CHECK_SALE_COMPETITOR_OK, null, false);
			break;
		case ActionEventConstant.CHECK_SALED_COMPETITOR:
			gotoCustomerListPG();
			break;
		case ActionEventConstant.SAVE_REMAIN_COMPETITOR:
			// ghi log ghe tham khi dat hang
			parent.requestInsertActionLog(beginVisitTime, ActionLogDTO.TYPE_REMAIN_COMPETITOR, null,
					customerListItem.aCustomer.customerId,
					String.valueOf(customerListItem.isOr));
			parent.removeMenuCloseCustomer();
			GlobalUtil.showDialogConfirm(this, parent, StringUtil
					.getString(R.string.TEXT_SAVE_REMAIN_COMPETITOR_SUCCESS),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					ACTION_NOTIFY_TAKE_CHECK_REMAIN_COMPETITOR_OK, null, false);
			break;
		case ActionEventConstant.CHECK_REMAINED_COMPETITOR:
			goToSaleCompetitor();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * render layout
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	private void renderLayout() {

		for (ProductCompetitorDTO competitorDTO : listProductCompetitor
				.getArrProductCompetitor()) {
			CompetitorSaleTablePG tablePG = new CompetitorSaleTablePG(parent, competitorDTO, type);
			llCompetProductList.setDrawingCacheEnabled(true);
			llCompetProductList.buildDrawingCache();
			llCompetProductList.addView(tablePG);

		}
	}

	/**
	 * Di chuyen den man hinh danh sach khach hang PG
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	private void gotoCustomerListPG() {

		removeFragmentFromBackStack();
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_LIST_PG;
		TNPGController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {

		ActionEvent actionEvent = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (actionEvent.action) {
		case ActionEventConstant.CHECK_REMAINED_COMPETITOR:
			setTitleHeaderView(StringUtil
					.getString(R.string.TEXT_HEADER_TITLE_CHECK_REMAIN_COMPETITOR));
			getInformationCompetitor();
			break;
		case ActionEventConstant.CHECK_SALED_COMPETITOR:
			setTitleHeaderView(StringUtil
					.getString(R.string.TEXT_HEADER_TITLE_CHECK_SALE_COMPETITOR));
			getInformationCompetitor();
			break;
		default:
			parent.showDialog(StringUtil
					.getString(R.string.MESSAGE_ERROR_COMMON));
			break;
		}
	}

	/**
	 * di chuyen toi man hinh ban hang cua doi thu
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	private void goToSaleCompetitor() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		//bundle.putDouble(IntentConstants.INTENT_DISTANCE, distance);
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM, customerListItem);
		bundle.putInt(IntentConstants.INTENT_TYPE, 1);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_CHECK_REMAIN_COMPETITOR;
		TNPGController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Thuc hien confirmed luu kiem ton
	 * 
	 * @author: banghn
	 */
	public void saveRemainProduct() {
		String title = "";
		if (type == Constants.CHECK_REMAIN_COMPETITOR) {
			title = StringUtil.getString(R.string.TEXT_SAVE_REMAIN_COMPETITOR);
		} else if (type == Constants.CHECK_SALE_COMPETITOR) {
			title = StringUtil.getString(R.string.TEXT_SAVE_SALE_COMPETITOR);
		}
		GlobalUtil.showDialogConfirm(this, this.parent, title,
				StringUtil.getString(R.string.TEXT_AGREE),
				ACTION_SAVE_COMPETITOR,
				StringUtil.getString(R.string.TEXT_DENY),
				ACTION_CANCEL_COMPETITOR, null);
	}

	/**
	 * lưu thông tin bán hàng của đối thủ
	 * 
	 * @author: dungdq3
	 * @param competitorListDTO
	 * @return: void
	 */
	private void saveSaleCompetitor(ProductCompetitorListDTO competitorListDTO) {

		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		data.putString(IntentConstants.INTENT_CUSTOMER_ID,
				customerListItem.aCustomer.getCustomerId());
		data.putLong(IntentConstants.INTENT_STAFF_ID, staffID);
		data.putSerializable(IntentConstants.INTENT_OP_SALE_VOLUME_LIST,
				competitorListDTO);
		data.putString(IntentConstants.INTENT_STAFF_CODE, GlobalInfo
				.getInstance().getProfile().getUserData().userCode);
		data.putInt(IntentConstants.INTENT_TYPE, 0); // 1 - BSG, 0- bia doi thu
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.SAVE_SALE_COMPETITOR;
		e.isNeedCheckTimeServer = false;
		TNPGController.getInstance().handleViewEvent(e);
	}

	/**
	 * lưu thông tin kiểm hàng tồn của đối thủ
	 * 
	 * @author: dungdq3
	 * @param competitorListDTO
	 * @return: void
	 */
	private void saveRemainCompetitor(ProductCompetitorListDTO competitorListDTO) {

		parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		data.putString(IntentConstants.INTENT_CUSTOMER_ID,
				customerListItem.aCustomer.getCustomerId());
		data.putLong(IntentConstants.INTENT_STAFF_ID, staffID);
		data.putSerializable(IntentConstants.INTENT_OP_STOCK_TOTAL_LIST,
				competitorListDTO);
		data.putString(IntentConstants.INTENT_STAFF_CODE, GlobalInfo
				.getInstance().getProfile().getUserData().userCode);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.SAVE_REMAIN_COMPETITOR;
		e.isNeedCheckTimeServer = false;
		TNPGController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_OK_2:
			gotoCustomerListPG();
			break;
		case ACTION_SAVE_COMPETITOR:
			checkQuantity();
			break;
		case ACTION_END_VISIT_COMPETITOR:
			parent.onEvent(ACTION_FINISH_VISIT_CUS_OK, null, null);
			break;
		case ACTION_VISIT_CUSTOMER:
			gotoCustomerInfo();
			break;
		case ACTION_AGRRE_BACK:
			GlobalUtil.popBackStack(getActivity());
			gotoCustomerListPG();
			break;
		case ACTION_SAVE:
			saveRemainProduct();
			break;
		case ACTION_NOTIFY_TAKE_CHECK_REMAIN_COMPETITOR_OK:
			if (customerListItem.isTodayCheckedSaleCompetitor > 0) {
				parent.showDialog(StringUtil
						.getString(R.string.FINISH_ALL_ACTION));
				parent.endVisitCustomerBar();
				gotoCustomerListPG();
			} else {
				goToSaleCompetitor();
			}
			break;
		case ACTION_NOTIFY_TAKE_CHECK_SALE_COMPETITOR_OK:
			if (customerListItem.isTodayCheckedRemainCompetitor > 0
					&& customerListItem.isTodayCheckedSaleCompetitor > 0
					&& customerListItem.hasTodayTakeAttendance) {
				parent.showDialog(StringUtil
						.getString(R.string.FINISH_ALL_ACTION));
				parent.endVisitCustomerBar();
			}

			// ket thuc ghe tham
			onEvent(ACTION_END_VISIT_COMPETITOR, null, null);
			break;
		default:
			break;
		}
	}

	/**
	 * Kiem tra so luong bia doi thu
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	private void checkQuantity() {

		boolean flag = false;
		ProductCompetitorListDTO competitorListDTO = new ProductCompetitorListDTO();
		if (type == Constants.CHECK_REMAIN_COMPETITOR) {
			for (ProductCompetitorDTO productCompetitorDTO : listProductCompetitor
					.getArrProductCompetitor()) {
				ProductCompetitorDTO competitorDTO = new ProductCompetitorDTO();
				competitorDTO.setCodeCompetitor(productCompetitorDTO
						.getCodeCompetitor());
				competitorDTO.setNameCompetitor(productCompetitorDTO
						.getNameCompetitor());
				competitorDTO.setIdCompetitor(productCompetitorDTO
						.getIdCompetitor());
				for (OpProductDTO opProductDTO : productCompetitorDTO
						.getArrProduct()) {
					if (opProductDTO.getQuantity() >= 0) {
						competitorDTO.getArrProduct().add(opProductDTO);
						flag = true;
					}
				}
				if (competitorDTO.getArrProduct().size() > 0) {
					competitorListDTO.getArrProductCompetitor().add(
							competitorDTO);
				}
			}
		} else if (type == Constants.CHECK_SALE_COMPETITOR) {
			for (ProductCompetitorDTO productCompetitorDTO : listProductCompetitor
					.getArrProductCompetitor()) {
				ProductCompetitorDTO competitorDTO = new ProductCompetitorDTO();
				competitorDTO.setCodeCompetitor(productCompetitorDTO
						.getCodeCompetitor());
				competitorDTO.setNameCompetitor(productCompetitorDTO
						.getNameCompetitor());
				competitorDTO.setIdCompetitor(productCompetitorDTO
						.getIdCompetitor());
				for (OpProductDTO opProductDTO : productCompetitorDTO
						.getArrProduct()) {
					if (opProductDTO.getQuantity() > 0) {
						competitorDTO.getArrProduct().add(opProductDTO);
						flag = true;
					}
				}
				if (competitorDTO.getArrProduct().size() > 0) {
					competitorListDTO.getArrProductCompetitor().add(
							competitorDTO);
				}
			}
		}
		if (flag) {
			if (type == Constants.CHECK_REMAIN_COMPETITOR) {
				saveRemainCompetitor(competitorListDTO);
			} else if (type == Constants.CHECK_SALE_COMPETITOR) {
				if (customerListItem.isTodayCheckedSaleCompetitor > 0) {
					parent.showDialog(StringUtil
							.getString(R.string.FINISH_ALL_ACTION));
					parent.endVisitCustomerBar();
					gotoCustomerListPG();
				} else {
					saveSaleCompetitor(competitorListDTO);
				}
			}
		} else {
			if (type == Constants.CHECK_REMAIN_COMPETITOR) {
				if (customerListItem.isTodayCheckedSaleCompetitor > 0) {
					parent.showDialog(StringUtil
							.getString(R.string.FINISH_ALL_ACTION));
					parent.endVisitCustomerBar();
					gotoCustomerListPG();
				} else {
					goToSaleCompetitor();
				}
			} else if (type == Constants.CHECK_SALE_COMPETITOR) {
				parent.requestUpdateActionLog("0", null, customerListItem, this);
				parent.endVisitCustomerBar();
				gotoCustomerListPG();
			}
		}
	}

	/**
	 * Di chuyển tới màn hình thông tin khách hàng.
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	private void gotoCustomerInfo() {

		removeFragmentFromBackStack();
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, prefix);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID,
				customerListItem.aCustomer.getCustomerId());
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onDestroyView() {

		super.onDestroyView();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {

		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				parent.showLoadingDialog();
				listProductCompetitor.getArrProductCompetitor().clear();
				llCompetProductList.removeAllViews();
				checkRemained();
			}
			break;
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			// tranh truong hop notify nhieu lan
			if (this.lat > 0 && this.lng > 0) {
				return;
			}
			parent.closeProgressDialog();
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLongtitude();
			if (lat < 0 && lng < 0) {
//				parent.showDialog(StringUtil
//						.getString(R.string.TEXT_ALERT_CANT_LOCATE_YOUR_POSITION));
				return;
			} else {
				this.lat = lat;
				this.lng = lng;
				// getCustomerList(1, true);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * Bắt sự kiện khi nhấn back
	 * 
	 * @author: dungdq3
	 * @return: void
	 */
	public int onBackPressed() {

		int handleBack = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(StringUtil.getString(R.string.TEXT_CONFIRM_BACK_FIRST_LAYOUT));
		sb.append(" ");
		if (type == Constants.CHECK_REMAIN_COMPETITOR) {
			sb.append(StringUtil.getString(R.string.TEXT_TITLE_CHECK_REMAIN));
		} else if (type == Constants.CHECK_SALE_COMPETITOR) {
			sb.append(StringUtil.getString(R.string.TEXT_TITLE_CHECK_SALE));
		}

		GlobalUtil.showDialogConfirm(this, parent, sb.toString(),
				StringUtil.getString(R.string.TEXT_AGREE), ACTION_AGRRE_BACK,
				StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_BACK,
				null);
		return handleBack;
	}
}
