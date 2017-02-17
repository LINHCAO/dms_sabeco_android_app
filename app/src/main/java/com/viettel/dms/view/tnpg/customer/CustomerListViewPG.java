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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerListItem.VISIT_STATUS;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
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
import java.util.List;

/**
 * Danh sach khach hang cua PG
 * 
 * @author : Dungdq3
 */
public class CustomerListViewPG extends BaseFragment implements OnEventControlListener, OnClickListener,
		VinamilkTableListener, OnItemSelectedListener {
	private static final int ACTION_PATH = 2;
	private static final int ACTION_CUS_LIST = 3;
	private static final int ACTION_OK = 4;
	private static final int ACTION_CANCEL = 5;
	public static final int ACTION_POS_OK = 6;
	private static final int ACTION_OK_2 = 7;

	public static final String TAG = CustomerListViewPG.class.getName();

	private GlobalBaseActivity parent; // parent
	public CustomerListDTO cusDto;// cusList
	private DMSTableView tbCusListPG;// tbCusList
	private VNMEditTextClearable edMKHPG;// edMKH
	private VNMEditTextClearable edTKHPG;// edTKH
	private Spinner spinnerLinePG;// spinnerLine
	private Button btSearchPG; // btSearch
	private int currentPage = -1;

	private String textCusCode = "";
	private String textCusName = "";

	private int currentSpinerSelectedItem = -1;
	private boolean isUpdateData = false;

	private double lat = -1;
	private double lng = -1;
	public boolean isBackFromPopStack = false;
	CountDownTimer timer = null;
	boolean isSearchOrSelectSpiner = false;
	private boolean isRequestCusList = false;
//	private String objectType;
	
	public static CustomerListViewPG newInstance() {
		CustomerListViewPG f = new CustomerListViewPG();
		// Supply index input as an argument.
		Bundle args = new Bundle();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_list_viewpg, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_ROUTE), R.drawable.icon_map, ACTION_PATH);
		addMenuItem(StringUtil.getString(R.string.TEXT_CUS_LIST), R.drawable.menu_customer_icon, ACTION_CUS_LIST);
		setMenuItemFocus(2);

		edMKHPG = (VNMEditTextClearable) v.findViewById(R.id.edCusCodePG);
		edTKHPG = (VNMEditTextClearable) v.findViewById(R.id.edCusNamePG);
		spinnerLinePG = (Spinner) v.findViewById(R.id.spinnerPathPG);
		spinnerLinePG.setOnItemSelectedListener(this);
		btSearchPG = (Button) v.findViewById(R.id.btSearchPG);
		btSearchPG.setOnClickListener(this);
		tbCusListPG = (DMSTableView) view.findViewById(R.id.tbCusListPG);
		tbCusListPG.setListener(this);
		initHeaderTable(tbCusListPG, new CustomerListRowPG(parent, this));
		// reset lai bien isInsertingActionLogVisit
		parent.isInsertingActionLogVisit = false;
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item,
				Constants.ARRAY_LINE_CHOOSE);

		if (!isBackFromPopStack) {
			restartLocatingUpdate();

			spinnerLinePG.setAdapter(adapterLine);
			if (cusDto != null && currentPage != -1) {
				spinnerLinePG.setSelection(currentSpinerSelectedItem);
				renderLayout();
			} else {
				spinnerLinePG.setSelection(DateUtils.getCurrentDay());
				currentSpinerSelectedItem = spinnerLinePG.getSelectedItemPosition();

				if ((lat > 0 && lng > 0) || GlobalInfo.getInstance().getStateConnectionMode() 
						== Constants.CONNECTION_OFFLINE) {
					getCustomerListPG(1, true);
				}
			}
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER));
			// changeTitleAndSearch(currentSpinerSelectedItem);

			// TamPQ: kiem tra trang thai menu ghe tham
			ActionLogDTO actionLog = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
			if (actionLog != null && (actionLog.isCheckRemainAndSale()|| actionLog.isVisited())) {
				parent.endVisitCustomerBar();
			}
		} else {
			isBackFromPopStack = false;
		}

		return v;
	}

	/**
	 * Lay danh sach khach hang cua PG
	 * 
	 * @author: dungdq3
	 * @return: void
	 * @throws:
	 */
	private void getCustomerListPG(int page, boolean isGetTotalPage) {
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
		bundle.putInt(IntentConstants.INTENT_ROLE_TYPE, 1);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, textCusName);

		e.viewData = bundle;
		e.action = ActionEventConstant.GET_CUSTOMER_LIST;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
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
			requestInsertLogKPI(HashMapKPI.TTTT_DANHSACHKHACHHANG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.UPDATE_ACTION_LOG:
			GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
			GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer((ActionLogDTO) e.viewData);
			processVisitCustomer((CustomerListItem) e.userData);
			//goToCheckRemainComptitor((CustomerListItem) e.userData);
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
	 * render giao dien
	 * @author: dungdq3
	 * @return: void
	 */
	private void renderLayout() {
		if (currentPage != -1) {
			tbCusListPG.setTotalSize(cusDto.totalCustomer, currentPage);
			tbCusListPG.getPagingControl().setCurrentPage(currentPage);
		} else {
			tbCusListPG.setTotalSize(cusDto.totalCustomer, 1);
			currentPage = tbCusListPG.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusListPG.getPagingControl().getCurrentPage() - 1);
		List<DMSTableRow> listRows = new ArrayList<DMSTableRow>();
		tbCusListPG.clearAllData();
		if (cusDto.cusList.size() > 0) {
			for (CustomerListItem customerListItem: cusDto.cusList) {
				CustomerListRowPG row = new CustomerListRowPG(parent, this);
				row.render(pos, customerListItem, currentSpinerSelectedItem);
				pos++;
				listRows.add(row);
				tbCusListPG.addRow(row);
			}
		} else{
			tbCusListPG.showNoContentRow();
		}

	}

	/**
	 * di toi man hinh chi tiet Khach hang
	 * 
	 * @author : TamPQ since : 10:59:39 AM
	 */
	public void gotoCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-01");
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * man hinh ban do
	 * 
	 * @author: dungdq3
	 * @return: void
	 * @throws:
	 */
	private void gotoCustomerRoutePGView() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_ROUTE_PG;
		TNPGController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Chuyen den man hinh cham cong PG
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	private void gotoTTTTTakeAttendance(CustomerListItem dto) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		//bundle.putDouble(IntentConstants.INTENT_DISTANCE, cusDto.distance);
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM, dto);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_TTTT_TAKE_ATTENDANCE;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_PATH:
			gotoCustomerRoutePGView();
			break;
		case ACTION_OK:
			CustomerListItem item = (CustomerListItem) data;
			// ket thuc ghe tham
			parent.requestUpdateActionLog("0", null, item, this);
			break;
		case ACTION_POS_OK:
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS;
			UserController.getInstance().handleSwitchFragment(e);
			// notify menu
			Bundle b = new Bundle();
			b.putInt(IntentConstants.INTENT_INDEX, 0);
			sendBroadcast(ActionEventConstant.NOTIFY_MENU, b);
			break;
		case ACTION_OK_2:
			cusDto = null;
			currentPage = -1;
			getCustomerListPG(1, true);
			break;

		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSearchPG:
			isSearchOrSelectSpiner = true;

			textCusCode = edMKHPG.getText().toString().trim();
			textCusName = edTKHPG.getText().toString().trim();

			// hide ban phim
			GlobalUtil.forceHideKeyboard(parent);

			getCustomerListPG(1, true);
			break;
		default:
			break;
		}

	}

	private void resetAllValue() {
		spinnerLinePG.setSelection(DateUtils.getCurrentDay());
		currentSpinerSelectedItem = spinnerLinePG.getSelectedItemPosition();
		textCusName = "";
		textCusCode = "";
		edMKHPG.setText(textCusCode);
		edTKHPG.setText(textCusName);
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
		if (control == tbCusListPG) {

			currentPage = tbCusListPG.getPagingControl().getCurrentPage();
			getCustomerListPG(currentPage, false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int act, View control, Object data) {
		switch (act) {
		case ActionEventConstant.GO_TO_CUSTOMER_INFO: {
			CustomerListItem item = (CustomerListItem) data;
			currentPage = tbCusListPG.getPagingControl().getCurrentPage();
			gotoCustomerInfo(item.aCustomer.getCustomerId());
			break;
		}
		case ActionEventConstant.VISIT_CUSTOMER: {
			CustomerListItem item = (CustomerListItem) data;
			if (isValidDistanceToTakeAttendanceAndCheckRemain(item)) {
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
					processVisitCustomer(item);
				}
			} else {
				String mess = Constants.STR_BLANK;
				if(!StringUtil.isNullOrEmpty(item.aCustomer.customerCode)) {
					mess = StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_1) + " "
							+ item.aCustomer.customerName + " - " + item.aCustomer.customerCode.substring(0, 3)
							+ StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_3);
				}else{
					mess = StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_1) + " "
							+ item.aCustomer.customerName
							+ StringUtil.getString(R.string.TEXT_TOO_FAR_FROM_CUS_3);
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
	 * ghe tham KH
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param item
	 * @return: void
	 * @throws:
	 */
	private void processVisitCustomer(CustomerListItem item) {
		if((item.hasTodayTakeAttendance || item.totalPG<1) && item.isTodayCheckedRemainCompetitor>0 && item.isTodayCheckedSaleCompetitor>0){
			parent.showDialog(StringUtil.getString(R.string.FINISH_ALL_ACTION));
		}else{
			//if(!item.hasTodayTakeAttendance && item.totalPG>0){
			if(item.totalPG > 0){
				gotoTTTTTakeAttendance(item);
			}else{
				goToCheckRemainComptitor(item);
			}
			
			if (item.visitStatus != VISIT_STATUS.VISITED_FINISHED) {
				if (item.visitStatus == VISIT_STATUS.VISITED_CLOSED) {
					saveLastVisitToActionLogProfile(item);
					parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
				} else if (item.visitStatus == VISIT_STATUS.VISITING) {
					saveLastVisitToActionLogProfile(item);
					parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
					if (item.isTodayCheckedRemainCompetitor>0 || item.isTodayCheckedSaleCompetitor>0) {
						parent.removeMenuCloseCustomer();
					}
				} else {// VISIT_STATUS.NONE_VISIT
					parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
					parent.requestStartInsertVisitActionLog(item);
					item.visitStatus = VISIT_STATUS.VISITING;
				}
				// set toa do khach hang dang ghe tham de toi uu dinh vi
				GlobalInfo.getInstance().setPositionCustomerVisiting(new LatLng(item.aCustomer.lat, item.aCustomer.lng));
			} else {// neu da ghe tham thi chi hien thi title Ghe tham
				parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
				saveLastVisitToActionLogProfile(item);
			}
			// check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi
			// khoach cach qua xa
			if (!StringUtil.isNullOrEmpty(item.exceptionOrderDate)) {
				requestUpdateExceptionOrderDate(item);
			}
		}
	}
	
	/**
	* Cap nhat lai ngay dat hang dat hang tu xa
	* @author: dungdq3
	* @param: item
	* @return: void
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

	/**
	* Luu action log ghe tham.
	* @author: dungdq3
	* @param: Tham số của hàm
	* @return: Kết qủa trả về
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	*/
	private void saveLastVisitToActionLogProfile(CustomerListItem item) {
		
		ActionLogDTO action = new ActionLogDTO();
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
		action.objectType = ActionLogDTO.TYPE_REMAIN_COMPETITOR;
		GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);
	}

	/**
	 * K tra lai khoang cach de cham trung bay, kiem ton
	 * @author: TamPQ
	 * @param item
	 * @return: boolean
	 * @throws:
	 */
	private boolean isValidDistanceToTakeAttendanceAndCheckRemain(CustomerListItem item) {
		if (cusDto != null) {
			item.updateCustomerDistance();
			return !item.isTooFarShop;
		} else {
			return false;
		}
	}

	/**
	* Chuyen sang man hinh kiem ton doi thu
	* @author: dungdq3
	* @return: void
	*/
	private void goToCheckRemainComptitor(CustomerListItem dto) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		//bundle.putDouble(IntentConstants.INTENT_DISTANCE, cusDto.distance);
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM, dto);
		if(dto.isTodayCheckedRemainCompetitor<1){
			bundle.putInt(IntentConstants.INTENT_TYPE, 0);
		}else{
			bundle.putInt(IntentConstants.INTENT_TYPE, 1);
		}
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_CHECK_REMAIN_COMPETITOR;
		TNPGController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerLinePG) {
			if (currentSpinerSelectedItem != spinnerLinePG.getSelectedItemPosition()) {
				currentSpinerSelectedItem = spinnerLinePG.getSelectedItemPosition();

				edMKHPG.setText("");
				edTKHPG.setText("");
				// edCusAdd.setText("");
				textCusCode = edMKHPG.getText().toString().trim();
				textCusName = edTKHPG.getText().toString().trim();
				// textCusAdd = edCusAdd.getText().toString().trim();

				// changeTitleAndSearch(currentSpinerSelectedItem);
				isSearchOrSelectSpiner = true;
				getCustomerListPG(1, true);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				parent.showLoadingDialog();
				isUpdateData = true;
				resetAllValue();
				getCustomerListPG(1, true);
				// demo gui file db.zip va log file len server
				// sendzipFile();
			}
			break;
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			ArrayList<DMSTableRow> rows = tbCusListPG.getListChildRow();
			if (rows != null && !rows.isEmpty()) {
				try {
					for (DMSTableRow row : rows) {
						CustomerListRowPG rowCus = (CustomerListRowPG) row;
						rowCus.reRenderVisitStatus();
					}
				} catch (Exception ex){
					VTLog.e("rerender status list cus pg", ex.getMessage());
				}
			} else{
				if (!isRequestCusList) {
					getCustomerListPG(1, true);
				}
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
