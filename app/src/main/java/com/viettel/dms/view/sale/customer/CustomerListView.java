/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerListItem.VISIT_STATUS;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;

/**
 * Danh sach khach hang
 * 
 * @author : TamPQ since : 9:48:52 AM version :
 */
public class CustomerListView extends BaseFragment implements OnEventControlListener, OnClickListener,
		VinamilkTableListener, OnItemSelectedListener {
	private static final int ACTION_OK = -1;
	private static final int ACTION_CANCEL = -2;
	public static final int ACTION_POS_OK = -3;
	private static final int ACTION_OK_2 = -4;

	public static final String TAG = CustomerListView.class.getName();

	private GlobalBaseActivity parent; // parent
	public CustomerListDTO cusDto;// cusList
	private DMSTableView tbCusList;// tbCusList
	private VNMEditTextClearable edMKH;// edMKH
	private VNMEditTextClearable edTKH;// edTKH
	private Spinner spinnerLine;// spinnerLine
	private Button btSearch; // btSearch
	private int currentPage = -1;
	// string luu ma khach hang
	private String textCusCode = "";
	// string luu ten khach hang
	private String textCusName = "";

	private int currentSpinerSelectedItem = -1;
	private boolean isUpdateData = false;

	private boolean isBack = false;
	public boolean isBackFromPopStack = false;
	CountDownTimer timer = null;
	boolean isSearchOrSelectSpiner = false;
	private boolean isRequestCusList = false;

	public static CustomerListView newInstance(Bundle args ) {
		CustomerListView f = new CustomerListView();
		// Supply index input as an argument.
		args = (args !=null ? args : new Bundle());
		args.putBoolean("isReload", true);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_list_fragment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.NVBH_CUSTOMER_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_CUSTOMER_LIST);

		edMKH = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
		edTKH = (VNMEditTextClearable) v.findViewById(R.id.edCusName);
		spinnerLine = (Spinner) v.findViewById(R.id.spinnerPath);
		spinnerLine.setOnItemSelectedListener(this);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		tbCusList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
//		layoutTableHeader();
		// khoi tao header cho table
		initHeaderTable(tbCusList, new CustomerListRow(parent, this));
		// reset lai bien isInsertingActionLogVisit
		parent.isInsertingActionLogVisit = false;
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item,
				Constants.ARRAY_LINE_CHOOSE);

		if (!isBackFromPopStack) {
			//dinh vi lai
			restartLocatingUpdate();

			spinnerLine.setAdapter(adapterLine);
			if (cusDto != null && currentPage != -1) {
				spinnerLine.setSelection(currentSpinerSelectedItem);
				getCustomerList(currentPage, true);
				//renderLayout();
			} else {
				spinnerLine.setSelection(DateUtils.getCurrentDay());
				currentSpinerSelectedItem = spinnerLine.getSelectedItemPosition();
				getCustomerList(1, true);
			}
			
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER));
			// changeTitleAndSearch(currentSpinerSelectedItem);

			// TamPQ: kiem tra trang thai menu ghe tham
			ActionLogDTO actionLog = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
			if (actionLog != null && actionLog.isVisited()) {
				parent.endVisitCustomerBar();
			}

		} else {
			isBackFromPopStack = false;
		}

		return v;
	}

	/**
	 * getCustomerList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getCustomerList(int page, boolean isGetTotalPage) {
		if(!isRequestCusList) {
			isRequestCusList = true;
			parent.showLoadingDialog();
			ActionEvent e = new ActionEvent();

			Bundle bundle = new Bundle();
			bundle.putInt(IntentConstants.INTENT_PAGE, page);
			bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
			bundle.putLong(IntentConstants.INTENT_SHOP_ID,
					Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId));
			bundle.putString(IntentConstants.INTENT_VISIT_PLAN,
					DateUtils.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[currentSpinerSelectedItem]));
			// false: ko lay ngoai tuyen, true lay them ngoai tuyen voi dk da ghe tham
			bundle.putBoolean(IntentConstants.INTENT_GET_WRONG_PLAN, false);
			bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);

			// check data search
			bundle.putInt(IntentConstants.INTENT_ROLE_TYPE, 0);
			bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
			bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, textCusName);

			e.viewData = bundle;
			e.action = ActionEventConstant.GET_CUSTOMER_LIST;
			e.sender = this;
			SaleController.getInstance().handleViewEvent(e);
		} else {
			ServerLogger.sendLog("CustomerListView", "getCustomerList lan 2", TabletActionLogDTO.LOG_CLIENT);
		}
	}

	public SpannableObject titleString(int dayIndex) {
		if (dayIndex < 0) {
			dayIndex = DateUtils.getCurrentDay();
		}
		String today = DateUtils.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[dayIndex]);
		String st = "(" + StringUtil.getString(R.string.TEXT_IN_ROUTE) + " " + today + ")";

		SpannableObject obj = new SpannableObject();
		obj.addSpan(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(st, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		return obj;
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_CUSTOMER_LIST:
			if (isSearchOrSelectSpiner) {
				isSearchOrSelectSpiner = false;
				cusDto = null;
				currentPage = -1;
			}
			CustomerListDTO tempDto = (CustomerListDTO) modelEvent.getModelData();
			if (cusDto == null) {
				cusDto = tempDto;
			} else {
				cusDto.cusList = tempDto.cusList;
				//cusDto.distance = tempDto.distance;
			}

			if (isUpdateData) {
				isUpdateData = false;
				currentPage = -1;
				cusDto.totalCustomer = tempDto.totalCustomer;
			}

			if (cusDto != null) {
				renderLayout();
			}
			parent.closeProgressDialog();
			isRequestCusList = false;
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHKHACHHANG, e.startTimeFromBoot);
			break;
		case ActionEventConstant.UPDATE_ACTION_LOG:
			GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
			GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer((ActionLogDTO) e.viewData);
			processOrder((CustomerListItem) e.userData);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action){
			case ActionEventConstant.GET_CUSTOMER_LIST: {
				isRequestCusList = false;
			}
			break;
		}
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (currentPage != -1) {
			if (isBack) {
				isBack = false;
			}
			tbCusList.setTotalSize(cusDto.totalCustomer,currentPage);
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		} else {
			tbCusList.setTotalSize(cusDto.totalCustomer,1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
		tbCusList.clearAllData();
		if (cusDto.cusList.size() > 0) {
//			tbCusList.hideNoContentRow();
			for (CustomerListItem item: cusDto.cusList) {
				CustomerListRow row = new CustomerListRow(parent, this);
				row.render(pos, item, currentSpinerSelectedItem);
				pos++;
				tbCusList.addRow(row);
			}
		} else{
			tbCusList.showNoContentRow();
		}
	}

//	public void layoutTableHeader() {
//		// customer list table
//		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 45, 58, 200, 70, 240, 170, 80, 70 };
//		String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
//				StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
//				StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME), StringUtil.getString(R.string.TEXT_VISIT_ORDER_VT),
//				StringUtil.getString(R.string.TEXT_ADDRESS), StringUtil.getString(R.string.LINE1),
//				StringUtil.getString(R.string.TEXT_DISTANCE_VT), " " };
//		tbCusList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
//				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
//	}

	/**
	 * di toi man hinh chi tiet Khach hang
	 * 
	 * @author : TamPQ since : 10:59:39 AM
	 */
	public void gotoCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-01");
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getMenuIndexString());
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}
	
	/**
	 * Chuyen den man hinh dat hang
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void gotoCreateOrder(CustomerListItem dto) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, dto.aCustomer.getCustomerId());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, dto.aCustomer.getCustomerName());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, dto.aCustomer.getStreet());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, dto.aCustomer.getCustomerCode());
		bundle.putString(IntentConstants.INTENT_ORDER_ID, "0");
		bundle.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, dto.aCustomer.getCustomerTypeId());
		bundle.putSerializable(IntentConstants.INTENT_SUGGEST_ORDER_LIST, null);
		bundle.putString(IntentConstants.INTENT_IS_OR, String.valueOf(dto.isOr));
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_ORDER_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {

		switch (eventType) {
		case ACTION_OK:
			CustomerListItem item = (CustomerListItem) data;
			// ket thuc ghe tham
			parent.requestUpdateActionLog("0", null, item, this);
			break;
		case ACTION_POS_OK:
//			ActionEvent e = new ActionEvent();
//			e.sender = this;
//			e.viewData = new Bundle();
//			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS;
//			UserController.getInstance().handleSwitchFragment(e);
//			// notify menu
//			Bundle b = new Bundle();
//			b.putInt(IntentConstants.INTENT_INDEX, 0);
//			sendBroadcast(ActionEventConstant.NOTIFY_MENU, b);
			break;
		case ACTION_OK_2:
			cusDto = null;
			currentPage = -1;
			getCustomerList(1, true);
			break;

		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSearch:
			isSearchOrSelectSpiner = true;

			textCusCode = edMKH.getText().toString().trim();
			textCusName = edTKH.getText().toString().trim();

			// hide ban phim
			GlobalUtil.forceHideKeyboard(parent);

			getCustomerList(1, true);
			break;
		default:
			break;
		}

	}

	/**
	 * ghe tham dat hang
	 * 
	 * @author: TamPQ
	 * @param item
	 * @return: voidvoid
	 * @throws:
	 */
	private void processOrder(CustomerListItem item) {
		if (item.isOr == 0) {// KH trong tuyen
			
//			// Hien tai chua cho cham trung bay --> comment lai doan nay
//			// neu co chuong trinh trung bay chua cham
			if (isValidDistanceToVoteDisplayProgAndCheckRemain(item)
					&& item.isHaveDisplayProgramNotYetVote) {
				gotoVoteDisplayPresentProduct(item);
			} else  
//			// End cham trung bay
			if (isValidDistanceToVoteDisplayProgAndCheckRemain(item) && item.isHaveSaleOrder && !item.isTodayCheckedRemain) { 
				// neu co don dat hang & chua kiem hang ton
				gotoRemainProductView(item.aCustomer.getCustomerId(), item.aCustomer.getCustomerCode(),
						item.aCustomer.getCustomerName(), item.aCustomer.getStreet(),
						item.aCustomer.getCustomerTypeId(), item.isOr);
			} else {
				gotoCreateOrder(item);
			} 

			if (item.visitStatus != VISIT_STATUS.VISITED_FINISHED) {
				if (item.visitStatus == VISIT_STATUS.VISITED_CLOSED) {
					saveLastVisitToActionLogProfile(item);
					parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
				} else if (item.visitStatus == VISIT_STATUS.VISITING) {
					saveLastVisitToActionLogProfile(item);
					parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
					if (item.isTodayCheckedRemain || item.isTodayVoted || item.isTodayOrdered) {
						// da ghe tham, da dat hang, da cham trung bay, da kiem
						// hang
						// ton trong ngay thi an menu dong cua
						parent.removeMenuCloseCustomer();
					}
				} else {// VISIT_STATUS.NONE_VISIT
					parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
					parent.requestStartInsertVisitActionLog(item);
					item.visitStatus = VISIT_STATUS.VISITING;
				}
			} else {// neu da ghe tham thi chi hien thi title Ghe tham
				parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
				saveLastVisitToActionLogProfile(item);
			}

			// check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi
			// khoach cach qua xa
			if (!StringUtil.isNullOrEmpty(item.exceptionOrderDate)) {
				requestUpdateExceptionOrderDate(item);
			}
			// set toa do khach hang dang ghe tham de toi uu dinh vi
			GlobalInfo.getInstance().setPositionCustomerVisiting(new LatLng(item.aCustomer.lat, item.aCustomer.lng));
		} else {// KH ngoai tuyen
			gotoCreateOrder(item);

			if (item.visitStatus == VISIT_STATUS.VISITED_FINISHED) {
				parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
			} else {
				parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);

			}
			parent.removeMenuCloseCustomer();
			parent.removeMenuFinishCustomer();

			if (item.visitStatus == VISIT_STATUS.NONE_VISIT) {
				parent.requestStartInsertVisitActionLog(item);
				item.visitStatus = VISIT_STATUS.VISITING;
			} else {
				saveLastVisitToActionLogProfile(item);
			}
		}
		resetAllValue();
		cusDto = null;
	}

	/**
	 * //check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi khoach
	 * cach qua xa
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void requestUpdateExceptionOrderDate(CustomerListItem item) {
		StaffCustomerDTO staffCusDto = new StaffCustomerDTO();
		staffCusDto.staffCustomerId = item.staffCustomerId;

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE;
		e.viewData = staffCusDto;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);

	}

	private void saveLastVisitToActionLogProfile(CustomerListItem item) {
		ActionLogDTO action = new ActionLogDTO();
		// chu y ko co id cua action_log
		action.id = item.visitActLogId;
		action.aCustomer.customerId = item.aCustomer.customerId;
		action.aCustomer.customerName = item.aCustomer.customerName;
		action.aCustomer.customerCode = item.aCustomer.customerCode;
		action.aCustomer.shortCode = item.aCustomer.shortCode;
		action.startTime = item.visitStartTime;
		action.endTime = item.visitEndTime;
		action.isOr = item.isOr;
		action.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLastLatitude();
		action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLastLongtitude();
		//long second=DateUtils.getDistanceSecondsFrom2Date(item.visitStartTime, item.visitEndTime);
		//action.interval_time= String.valueOf(second);
		action.objectType = "0";
		action.distance = item.cusDistance;
		GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);
	}

	/**
	 * Di toi kiem hang ton
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void gotoRemainProductView(String customerId, String customerCode, String customerName,
			String customerAddress, int customerTypeId, int isOr) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		b.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCode);
		b.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerName);
		b.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, customerAddress);
		b.putString(IntentConstants.INTENT_IS_OR, String.valueOf(isOr));
		b.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, customerTypeId);
		e.viewData = b;
		e.action = ActionEventConstant.GO_TO_REMAIN_PRODUCT_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	} 
	
	/**
	 * Di toi man hinh cham trung bay
	 * @author: quangvt1
	 * @since: 15:05:28 08-05-2014
	 * @return: void
	 * @throws:  
	 * @param listItem
	 */
	private void gotoVoteDisplayPresentProduct(CustomerListItem listItem) {
		Bundle b = new Bundle();
		b.putSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM, listItem);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b;
		e.action = ActionEventConstant.GOTO_VOTE_DISPLAY_PRESENT_PRODUCT;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	private void resetAllValue() {
		spinnerLine.setSelection(DateUtils.getCurrentDay());
		currentSpinerSelectedItem = spinnerLine.getSelectedItemPosition();
		// changeTitleAndSearch(currentSpinerSelectedItem);
		textCusName = "";
		textCusCode = "";
		edMKH.setText(textCusCode);
		edTKH.setText(textCusName);
		currentPage = -1;
		cusDto = null;
	}

	public void changeTitleAndSearch(int index) {
		if (index == 7) {
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_2));
		} else {
			setTitleHeaderView(titleString(index));
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusList) {

			currentPage = tbCusList.getPagingControl().getCurrentPage();
			getCustomerList(currentPage, false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int act, View control, Object data) {
		switch (act) {
		case ActionEventConstant.GO_TO_CUSTOMER_INFO: {
			CustomerListItem item = (CustomerListItem) data;
			currentPage = tbCusList.getPagingControl().getCurrentPage();
			isBack = true;
			gotoCustomerInfo(item.aCustomer.getCustomerId());
			break;
		}
		case ActionEventConstant.VISIT_CUSTOMER: {
			CustomerListItem item = (CustomerListItem) data;
			if (isValidDistanceWhenBeginVisitation(item)) {
				ActionLogDTO action = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
				if (action != null && action.aCustomer.customerId != item.aCustomer.customerId
						&& DateUtils.compareDate(action.startTime, DateUtils.now()) == 0
						&& StringUtil.isNullOrEmpty(action.endTime)) {
					// kiem tra neu vao lai dung khach hang do thi khong
					// insertlog
					SpannableObject textConfirmed = new SpannableObject();
					textConfirmed.addSpan(StringUtil
							.getString(R.string.TEXT_ALREADY_VISIT_CUSTOMER),
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.NORMAL);
					if(!StringUtil.isNullOrEmpty(action.aCustomer.customerCode)) {
						textConfirmed.addSpan(" " + action.aCustomer.customerCode.substring(0, 3),
								ImageUtil.getColor(R.color.WHITE),
								android.graphics.Typeface.BOLD);
					}
					textConfirmed.addSpan(" - ",
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(action.aCustomer.customerName,
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(" trong ",
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.NORMAL);
					textConfirmed.addSpan(DateUtils.getVisitTime(action.startTime),
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.BOLD);
					textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_ASK_END_VISIT_CUSTOMER),
							ImageUtil.getColor(R.color.WHITE),
							android.graphics.Typeface.NORMAL);
					
					GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent, textConfirmed.getSpan(),
							StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK,
							StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CANCEL, item, true, true);
				} else {
					processOrder(item);
				}
			} else {
				String mess = Constants.STR_BLANK;
				if(!StringUtil.isNullOrEmpty(item.aCustomer.customerCode)) {
					mess = StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_1) + " "
							+ item.aCustomer.customerName + " - " + item.aCustomer.customerCode.substring(0, 3)
							+ StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_2);
				}else{
					mess = StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_1) + " "
							+ item.aCustomer.customerName
							+ StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_2);
				}

				GlobalUtil.showDialogConfirm(this, parent, mess, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
						ACTION_OK_2, item, false);
			}

			break;
		}
		default:
			break;
		}
	}

	/**
	 * K tra lai khoang cach khi bat dau ghe tham
	 * 
	 * @author: TamPQ
	 * @param item
	 * @return: voidvoid
	 * @throws:
	 */
	private boolean isValidDistanceWhenBeginVisitation(CustomerListItem item) {
		boolean isValid = true;
		item.isTooFarShop = false;
		if(cusDto != null){
			if (item.isOr == 0 && !item.isVisit() && !item.canOrderException()) {
				item.updateCustomerDistance();
				item.isTooFarShop = false;
				isValid = !item.isTooFarShop;
			}
		}else{
			isValid = false;
		}
		return isValid;
	}

	/**
	 * K tra lai khoang cach de cham trung bay, kiem ton
	 * 
	 * @author: TamPQ
	 * @param item
	 * @return: voidvoid
	 * @throws:
	 */
	private boolean isValidDistanceToVoteDisplayProgAndCheckRemain(CustomerListItem item) {
		item.updateCustomerDistance();
		return !item.isTooFarShop;
//		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerLine) {
			if (currentSpinerSelectedItem != spinnerLine.getSelectedItemPosition()) {
				currentSpinerSelectedItem = spinnerLine.getSelectedItemPosition();

				edMKH.setText("");
				edTKH.setText("");
				textCusCode = edMKH.getText().toString().trim();
				textCusName = edTKH.getText().toString().trim();
				isSearchOrSelectSpiner = true;
				getCustomerList(1, true);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				parent.showLoadingDialog();
				isUpdateData = true;
				resetAllValue();
				getCustomerList(1, true);
			}
			break;
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			ArrayList<DMSTableRow> rows = tbCusList.getListChildRow();
			if (rows != null && !rows.isEmpty()) {
				try {
					for (DMSTableRow row : rows) {
						CustomerListRow rowCus = (CustomerListRow) row;
						rowCus.reRenderVisitStatus();
					}
				} catch (Exception ex){
					VTLog.e("rerender status list cus", ex.getMessage());
				}
			} else{
				if (!isRequestCusList) {
					getCustomerList(1, true);
				}
			}

			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
