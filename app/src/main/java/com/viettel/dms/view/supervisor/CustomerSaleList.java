/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
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
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * Man hinh danh sach diem ban, khach hang
 * 
 * @author banghn
 * @since 1.0
 */
public class CustomerSaleList extends BaseFragment implements OnItemSelectedListener, VinamilkTableListener,
		OnClickListener {
	public static final String TAG = CustomerSaleList.class.getName();
	private static final int ACTION_ALLOW_ORDER_OK = 1;
	private static final int ACTION_ALLOW_ORDER_CANCEL = 2;

	private static final int MENU_IMAGE = 17;
	private static final int MENU_CUSTOMER = 15;
	// key chuyen man hinh
	public static final int KEY = 2;

	private GlobalBaseActivity parent; // parent
	// danh sach nhan vien ban hang
	private ListStaffDTO dtoListStaff;
	// danh sach khach hang
	public CustomerListDTO cusDto;
	// list ds nhan vien o spiner
	private String[] arrNVBHChoose;
	private boolean isFirst = true;// vo man hinh lan dau tien hay kg?
	private boolean isBack = false;// back ve tu ban do
	int currentTuyen = -1;// tuyen dang chon
	int currentNVBH = -1;// KH dang chon
	private int currentPage = -1;

	private String textCusCode = "";
	private String textCusNameAddress = "";
	private String textStaffId = "";

	// view
	Spinner spNVBH;// spiner list nhan vien ban hang
	Spinner spTuyen;// spiner tuyen trong tuan
	VNMEditTextClearable edCusCode;// ma nhan vien
	VNMEditTextClearable edCusNameAddress;// ten va dia chi nhan vien
	Button btReText;// nhap lai
	Button btSearch;// tim kiem
	private DMSTableView tbCusList;// tbCusList
	// action log ghi lai khi tao exception order date
	ActionLogDTO action;

	// ngay hien tại
	Calendar calendar = Calendar.getInstance();
	String sToday = DateUtils.defaultDateFormat.format(calendar.getTime());

	public static CustomerSaleList getInstance() {
		CustomerSaleList f = new CustomerSaleList();
		// Supply index input as an argument.
		Bundle args = new Bundle();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_sale_list_fragment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_GSNPP_CUSTOMER_SALE_LIST));
		enableMenuBar(this);
		initMenuActionBar();
		initView(v);
		if (isFirst) {
			getListSaleStaff();
			isFirst=false;
		} else {
			initSpNVBH();
			renderLayout();
		}
		return v;
	}

	/**
	 * 
	 * Khoi tao menu cho man hinh
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 * @date: Jan 8, 2013
	 */
	private void initMenuActionBar() {
		addMenuItem(StringUtil.getString(R.string.TEXT_PICTURE), R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(StringUtil.getString(R.string.TEXT_CUSTOMER), R.drawable.menu_customer_icon, MENU_CUSTOMER,
				View.INVISIBLE);
		setMenuItemFocus(2);
	}

	/**
	 * Init view control layout
	 * 
	 * @author banghn
	 * @param v
	 */
	private void initView(View v) {
		spNVBH = (Spinner) v.findViewById(R.id.spNVBH);
		spNVBH.setOnItemSelectedListener(this);
		spTuyen = (Spinner) v.findViewById(R.id.spTuyen);
		spTuyen.setOnItemSelectedListener(this);
		edCusCode = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
		edCusNameAddress = (VNMEditTextClearable) v.findViewById(R.id.edCusNameAddress);
		btReText = (Button) v.findViewById(R.id.btReText);
		btReText.setOnClickListener(this);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		tbCusList = (DMSTableView) v.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		//layoutTableHeader();
		initHeaderTable(tbCusList, new CustomerSaleListRow(parent, this));
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, Constants.ARRAY_LINE_CHOOSE);
		spTuyen.setAdapter(adapterLine);
		currentTuyen = DateUtils.getCurrentDay();// them tat ca o dau
		spTuyen.setSelection(currentTuyen);
	}

	/**
	 * request lay danh sach nhan vien ban hang trong shop
	 * 
	 * @author banghn
	 */
	private void getListSaleStaff() {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		b.putBoolean(IntentConstants.INTENT_ORDER, true);
		b.putBoolean(IntentConstants.INTENT_IS_ALL, true);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_NVBH;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * request danh sach diem ban
	 * 
	 * @author banghn
	 * @param page
	 */
	private void getCustomerSaleList(int page) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_PAGE, page);

		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		bundle.putString(IntentConstants.INTENT_VISIT_PLAN, DateUtils.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[currentTuyen]));

		// check data search
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME,
				StringUtil.getEngStringFromUnicodeString(textCusNameAddress));
		bundle.putString(IntentConstants.INTENT_STAFF_ID, textStaffId);
		bundle.putString(IntentConstants.INTENT_USER_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);

		e.viewData = bundle;
		e.action = ActionEventConstant.ACTION_GET_CUSTOMER_SALE_LIST;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * khoi tao view list NVBH
	 * 
	 * @author banghn
	 */
	private void initSpNVBH() {
		arrNVBHChoose = new String[dtoListStaff.arrList.size()];
		int i=0;
		for (StaffItem staffItem: dtoListStaff.arrList) {
			if (!StringUtil.isNullOrEmpty(staffItem.code)) {
				arrNVBHChoose[i] = staffItem.code + "-" + staffItem.name;
			} else {
				arrNVBHChoose[i] = staffItem.name;
			}
			i++;
		}
		SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrNVBHChoose);
		spNVBH.setAdapter(adapterNVBH);
		currentNVBH = spNVBH.getSelectedItemPosition();
		if(currentNVBH > 0 && currentNVBH < dtoListStaff.arrList.size()) {
			textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
		}
	}

	/**
	 * renderLayout
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (currentPage != -1) {
			if (isBack) {
				isBack = false;
			}
			tbCusList.setTotalSize(cusDto.totalCustomer, currentPage);
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		} else {
			tbCusList.setTotalSize(cusDto.totalCustomer, 1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
		int size = cusDto.cusList.size();
		tbCusList.clearAllData();
		if (size > 0) {
			for (CustomerListItem item: cusDto.cusList) {
				CustomerSaleListRow row = new CustomerSaleListRow(parent, this);
				row.render(pos, item);
				pos++;
				tbCusList.addRow(row);
			}
		}else{
			tbCusList.showNoContentRow();
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_NVBH:
			dtoListStaff = (ListStaffDTO) modelEvent.getModelData();
			if (dtoListStaff != null) {
				//dtoListStaff.addItemAll();
				initSpNVBH();
				getCustomerSaleList(1);
			}
			break;
		case ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE:
			getCustomerSaleList(currentPage);
			updateActionLogCreateExceptionDate(e);
			break;
		case ActionEventConstant.ACTION_GET_CUSTOMER_SALE_LIST:
			CustomerListDTO tempDto = (CustomerListDTO) modelEvent.getModelData();
			if (cusDto == null) {
				cusDto = tempDto;
			} else {
				cusDto.cusList = tempDto.cusList;
			}

			if (cusDto != null) {
				renderLayout();
			}
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_DIEMBAN, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE:
			parent.showDialog(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE));
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSearch:
			if (dtoListStaff != null && dtoListStaff.arrList != null && dtoListStaff.arrList.size() > 0) {
				cusDto = null;
				currentPage = -1;
				textCusCode = edCusCode.getText().toString().trim();
				textCusNameAddress = edCusNameAddress.getText().toString().trim();
				currentTuyen = spTuyen.getSelectedItemPosition();
				textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
				// hide ban phim
				GlobalUtil.forceHideKeyboard(parent);
				getCustomerSaleList(1);
			}
			break;
		case R.id.btReText:
			edCusCode.setText("");
			edCusNameAddress.setText("");
			spNVBH.setSelection(0);
			currentTuyen = DateUtils.getCurrentDay() + 1;// them tat ca o dau
			spTuyen.setSelection(currentTuyen);
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getCustomerSaleList(currentPage);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * Show dialog confimed cho phep dat hang tu xa doi voi nhan vien
	 * 
	 * @author BangHN
	 * @param dto
	 */
	private void showConfirmedDialogAllowOrder(CustomerListItem dto) {
		SpannableObject textConfirmed = new SpannableObject();
		SpannableObject titleConfirm= new SpannableObject();
		// CHO PHEP DAT HANG TU XA
		if (dto.exceptionOrderDate == null || !sToday.equals(dto.exceptionOrderDate)) {
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_DHTX_1),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
			textConfirmed.addSpan(dto.staffSale.staffCode + "-" + dto.staffSale.name,
					ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_DHTX_2),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
			if(!StringUtil.isNullOrEmpty(dto.aCustomer.customerCode)) {
				textConfirmed.addSpan(dto.aCustomer.customerCode.substring(0, 3) + "-" + dto.aCustomer.customerName,
						ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.BOLD);
			}else{
				textConfirmed.addSpan(dto.aCustomer.customerName,
						ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.BOLD);
			}
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_DHTX_3),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
			titleConfirm.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_DHTX_0),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
		} else {// HUY CHO PHEP DAT HANG TU XA
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_CANCEL_DHTX_1),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
			textConfirmed.addSpan(dto.staffSale.staffCode + "-" + dto.staffSale.name,
					ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_CANCEL_DHTX_2),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
			if(!StringUtil.isNullOrEmpty(dto.aCustomer.customerCode)) {
				textConfirmed.addSpan(dto.aCustomer.customerCode.substring(0, 3) + "-" + dto.aCustomer.customerName,
						ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.BOLD);
			}else{
				textConfirmed.addSpan(dto.aCustomer.customerName,
						ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.BOLD);
			}
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_CANCEL_DHTX_3),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
			titleConfirm.addSpan(StringUtil.getString(R.string.TEXT_CONFIRM_CANCEL_DHTX_0),
					ImageUtil.getColor(R.color.WHITE), android.graphics.Typeface.NORMAL);
		}

		GlobalUtil.showDialogConfirm(this, titleConfirm.getSpan(), textConfirmed.getSpan(),
				StringUtil.getString(R.string.TEXT_AGREE), ACTION_ALLOW_ORDER_OK,
				StringUtil.getString(R.string.TEXT_DENY), ACTION_ALLOW_ORDER_CANCEL, dto);
	}

	/**
	 * request update ExceptionOrderDate khi thuc hien cho phep dat hang ngoai
	 * le
	 * 
	 * @author banghn
	 */
	private void updateExceptionOrderDate(CustomerListItem dto) {
		if (dto != null && dto.staffSale != null && dto.aCustomer != null) {
			parent.showProgressDialog(StringUtil.getString(R.string.loading));
			ActionEvent e = new ActionEvent();

			// du lieu insert bang position log
			StaffCustomerDTO staffCustomerDTO = new StaffCustomerDTO();
			staffCustomerDTO.staffId = dto.staffSale.staffId;
			staffCustomerDTO.customerId = dto.aCustomer.customerId;
			staffCustomerDTO.staffCustomerId = dto.staffCustomerId;
			if (dto.exceptionOrderDate == null || !sToday.equals(dto.exceptionOrderDate)) {
				staffCustomerDTO.exceptionOrderDate = DateUtils.now();
			} else {
				staffCustomerDTO.exceptionOrderDate = null;
			}

			e.action = ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE;
			e.viewData = staffCustomerDTO;
			e.sender = this;
			e.isNeedCheckTimeServer = false;
			SuperviorController.getInstance().handleViewEvent(e);

			// tao action log khi cho phep dat hang tu xa
			if (staffCustomerDTO.exceptionOrderDate != null) {
				action = new ActionLogDTO();
				action.objectId = "" + dto.staffCustomerId;
				action.objectType = ActionLogDTO.TYPE_EXCEPTION_ORDER;
				action.aCustomer = dto.aCustomer;
				action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
				action.startTime = DateUtils.now();
			} else {
				action = new ActionLogDTO();
				action.objectId = "" + dto.staffCustomerId;
				action.aCustomer = dto.aCustomer;
				action.objectType = "5";
				action.startTime = DateUtils.now();
				action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			}
		}
	}

	/**
	 * tao mot record luu action log sau khi tao exception order date thanh cong
	 * 
	 * @author banghn
	 * @param e
	 */
	private void updateActionLogCreateExceptionDate(ActionEvent e) {
		if (action != null && e != null) {
			StaffCustomerDTO staffCusDto = (StaffCustomerDTO) e.viewData;
			if (staffCusDto.exceptionOrderDate != null) {
				if (StringUtil.isNullOrEmpty(action.objectId) || action.objectId.equals("0")) {
					action.objectId = "" + staffCusDto.staffCustomerId;
				}
				action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
				action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
				action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
				action.endTime = DateUtils.now();
				parent.requestInsertActionLog(action);
			} else {
				parent.requestDeleteActionLog(action);
			}
		}
	}

	/**
	 * di toi man hinh ban do lich su cap nhat vi tri cua diem ban
	 * 
	 * @author banghn
	 */
	private void gotoCustomerSaleLocationMap(CustomerDTO cusDTO) {
		if (cusDTO == null) {
			return;
		}

		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, cusDTO);
		e.viewData = bundle;
		e.action = ActionEventConstant.GOTO_CUSTOMER_SALE_LOCATION_RESET;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_ALLOW_ORDER_OK:
			updateExceptionOrderDate((CustomerListItem) data);
			break;
		case MENU_IMAGE:
			gotoImageList();
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	private void gotoImageList() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_GSNPP_IMAGE_LIST;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spNVBH) {
			if (currentNVBH != spNVBH.getSelectedItemPosition()) {
				
				textCusCode = edCusCode.getText().toString().trim();
				textCusNameAddress = edCusNameAddress.getText().toString().trim();
				currentTuyen = DateUtils.getCurrentDay();// them tat ca o
																// dau
				edCusCode.setText("");
				edCusNameAddress.setText("");
				spTuyen.setSelection(currentTuyen);
				currentNVBH = spNVBH.getSelectedItemPosition();
				currentPage = -1;
				cusDto = null;
				textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;

				getCustomerSaleList(1);
			}
		} else if (arg0 == spTuyen) {
			if(currentTuyen!=spTuyen.getSelectedItemPosition()){
				textCusCode = edCusCode.getText().toString().trim();
				textCusNameAddress = edCusNameAddress.getText().toString().trim();
				edCusCode.setText("");
				edCusNameAddress.setText("");
				currentTuyen=spTuyen.getSelectedItemPosition();
				currentNVBH = spNVBH.getSelectedItemPosition();
				currentPage = -1;
				cusDto = null;
				if(dtoListStaff.arrList.size() > 0) {
					textStaffId = dtoListStaff.arrList.get(spNVBH.getSelectedItemPosition()).id;
				}else{
					textStaffId = "";
				}
				getCustomerSaleList(1);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusList) {
			currentPage = tbCusList.getPagingControl().getCurrentPage();
			getCustomerSaleList(currentPage);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		CustomerListItem item=(CustomerListItem) data;
		switch (action) {
		case ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW:
			// click row of table
			if (cusDto != null && item.aCustomer != null) {
				isBack = true;
				gotoCustomerSaleLocationMap(item.aCustomer);
			}
			break;
		case ActionEventConstant.ALLOW_ORDER: {
			if (GlobalUtil.checkNetworkAccess()) {
				if (cusDto != null && item.aCustomer != null) {
					showConfirmedDialogAllowOrder(item);
				}
			} else {
				parent.showDialog(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE));
			}
			break;
		}
		case ActionEventConstant.NUM_UPDATE: {
			if (cusDto != null && item.aCustomer != null&& item.numUpdatePosition > 0) {
				isBack = true;
				gotoCustomerSaleLocationMap(item.aCustomer);
			}
			break;
		}
		case ActionEventConstant.GO_TO_CUSTOMER_INFO: {
			gotoCustomerInfo(String.valueOf(item.aCustomer.customerId));
			break;	
		}
		default:
			break;
		}
	}
	
	/**
	 * Qua MH Thong tin KH
	 * @author: dungnt19
	 * @since: 10:31:06 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param customerId
	 */
	public void gotoCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "07-01");
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bunde.putString(IntentConstants.INTENT_SENDER, TAG);
		e.viewData = bunde;
		
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}
}
