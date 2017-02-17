/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.collectinformation;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout.LayoutParams;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.PGController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.C2PurchaseDetailViewDTO;
import com.viettel.dms.dto.view.C2PurchaselViewDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
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
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * Danh sach KH la C2
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:27:35 12-12-2013
 */
public class CustomerC2ListView extends BaseFragment implements OnEventControlListener, OnClickListener,
		VinamilkTableListener {
	private static final int MENU_CUSTOMER_C2 = 1;
	private static final int MENU_REPORT = 2;
	public static final int ACTION_BUTTON_SAVE_POPUP_INPUT_CLICK = 3;
	public static final int ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK = 4;
	public static final int ACTION_CLOSE_POPUP_CHOOSE_DATE_OK = 5;

	public static final String TAG = CustomerC2ListView.class.getName();

	private GlobalBaseActivity parent; // parent
	public CustomerListDTO cusDto;// cusList
	public C2PurchaselViewDTO c2SaleOrder = new C2PurchaselViewDTO();
	private DMSTableView tbCusList;// tbCusList
	private int currentPage = -1;

	private boolean isUpdateData = false;
	public boolean isBackFromPopStack = false;
	CountDownTimer timer = null;
	boolean isSearchOrSelectSpiner = false;
	AlertDialog inputOrderPopup;
	InputPurchaseForC2View inputOrderForC2View;
	private CustomerListItem selectedCusItem;
	
	public static CustomerC2ListView getInstance(Bundle args) {
		CustomerC2ListView f = new CustomerC2ListView();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_c2_list, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		enableMenuBar(this);
//		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS), R.drawable.icon_clock, MENU_REPORT);
//		addMenuItem(StringUtil.getString(R.string.TEXT_INPUT_NEW), R.drawable.icon_task, MENU_CUSTOMER_C2);
//		setMenuItemFocus(2);
		tbCusList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		initHeaderTable(tbCusList, new CustomerC2ListRow(parent, this));

		if (!isBackFromPopStack) {
			if (cusDto != null && currentPage != -1) {
				renderLayout();
			} else {
				isUpdateData = true;
				getCustomerList(1, true);
				createInputPopup();
				getListProductForC2();
			}
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_LIST_C2));
		} else {
			isBackFromPopStack = false;
		}

		return v;
	}

	/**
	 * Lay ds KH
	 * @author: dungnt19
	 * @since: 10:27:57 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param page
	 * @param isGetTotalPage
	 */
	private void getCustomerList(int page, boolean isGetTotalPage) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();

		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		bundle.putLong(IntentConstants.INTENT_SHOP_ID,Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		// false: ko lay ngoai tuyen, true lay them ngoai tuyen voi dk da ghe tham
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);

		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_CUSTOMER_C2;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * Lay ds sp de dat hang
	 * @author: dungnt19
	 * @since: 10:28:12 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void getListProductForC2() {
		ActionEvent e = new ActionEvent();

		Bundle bundle = new Bundle();
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_PRODUCT_FOR_C2_PURCHASE;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_CUSTOMER_C2: {
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
			requestInsertLogKPI(HashMapKPI.GSNPP_THUTHAPDULIEUTHITRUONG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		case ActionEventConstant.GET_LIST_PRODUCT_FOR_C2_PURCHASE: {
			c2SaleOrder = (C2PurchaselViewDTO) modelEvent.getModelData();
			c2SaleOrder.orderInfo.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			c2SaleOrder.orderInfo.status = 1;
			if(inputOrderForC2View != null) {
				inputOrderForC2View.createLayout(c2SaleOrder);
			}
			inputOrderPopup.show();
			inputOrderPopup.dismiss();
			requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHSANPHAMC2MUANGOAI, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		case ActionEventConstant.SAVE_PURCHASE_FOR_C2: {
			resetData();
			parent.showDialog(StringUtil.getString(R.string.SAVE_ORDER_C2_SUCCESS));
			if(inputOrderForC2View != null && inputOrderPopup.isShowing()) {
				inputOrderPopup.dismiss();
			}
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}
	
	/**
	 * Reset du lieu dat hang
	 * @author: dungnt19
	 * @since: 10:31:50 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void resetData() {
		c2SaleOrder.orderInfo.fromShopName = "";
		c2SaleOrder.totalQuantity = 0;
		for (C2PurchaseDetailViewDTO detailView : c2SaleOrder.listProduct) {
			detailView.orderDetail.quantity = 0;
			detailView.orderDetail.price = 0;
			detailView.orderDetail.amount = 0;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.SAVE_PURCHASE_FOR_C2: {
			resetData();
			parent.showDialog(StringUtil.getString(R.string.SAVE_ORDER_C2_FAIL));
			if(inputOrderForC2View != null && inputOrderPopup.isShowing()) {
				inputOrderPopup.dismiss();
			}
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
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
//			if (isBack) {
//				isBack = false;
				tbCusList.setTotalSize(cusDto.totalCustomer, currentPage);
//			}
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		} else {
			tbCusList.setTotalSize(cusDto.totalCustomer, 1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
		tbCusList.clearAllData();
		if (cusDto.cusList.size() > 0) {
			for (CustomerListItem item: cusDto.cusList) {
				CustomerC2ListRow row = new CustomerC2ListRow(parent, this);
				row.render(pos, item);
				pos++;
				tbCusList.addRow(row);
			}
		} else{
			tbCusList.showNoContentRow();
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
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "03");
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bundle.putString(IntentConstants.INTENT_SENDER, TAG);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Reset cac gia tri ve mac dinh
	 * @author: dungnt19
	 * @since: 10:30:26 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void resetAllValue() {
		// textCusAdd = "";
		// edCusAdd.setText(textCusAdd);
		// searchAdd.setVisibility(View.GONE);
		currentPage = -1;
		cusDto = null;
	}

	/**
	 * Hien thi Ds KH cua C2
	 * @author: dungnt19
	 * @since: 10:29:46 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	public void gotoCustomerListOfC2(CustomerListItem item) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putSerializable(IntentConstants.INTENT_CUSTOMER, item);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_LIST_OF_C2;
		SuperviorController.getInstance().handleSwitchFragment(e);
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
			gotoCustomerInfo(item.aCustomer.getCustomerId());
			break;
		}
		case ActionEventConstant.ACTION_INPUT_C2_ORDER: {
			CustomerListItem item = (CustomerListItem) data;
			gotoCustomerListOfC2(item);
			break;
		}
		case ActionEventConstant.ACTION_INPUT_C2_PURCHASE: {
			CustomerListItem item = (CustomerListItem) data;
			showInputOrderForC2(item);
			c2SaleOrder.orderInfo.customerId = selectedCusItem.aCustomer.customerId;
			break;
		}
		default:
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				parent.showLoadingDialog();
				isUpdateData = true;
				resetAllValue();
				getCustomerList(1, true);
				getListProductForC2();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * Hien thi popup dat hang cua KH cua C2
	 * @author: dungnt19
	 * @since: 10:45:38 12-12-2013
	 * @return: void
	 * @throws:  
	 */
	private void createInputPopup() {
		if (inputOrderPopup == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			inputOrderForC2View = new InputPurchaseForC2View(parent, this, ACTION_BUTTON_SAVE_POPUP_INPUT_CLICK, ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK);
			build.setView(inputOrderForC2View.viewLayout);
			inputOrderPopup = build.create();
			inputOrderPopup.setCanceledOnTouchOutside(false);
			Window window = inputOrderPopup.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
			
//			getListProductForC2();
		}
	}
	/**
	 * Hien thi popup nhap don hang
	 * @author: dungnt19
	 * @since: 10:29:30 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param cusItem
	 */
	private void showInputOrderForC2(CustomerListItem cusItem) {
		selectedCusItem = cusItem;
		inputOrderForC2View.renderLayout(c2SaleOrder, cusItem);
		inputOrderPopup.show();
	}
	
	@Override
	public void onEvent(int eventType, View control, Object data) {
//		if(control == inputOrderForC2View) {
		switch (eventType) {
			case ACTION_BUTTON_SAVE_POPUP_INPUT_CLICK: {
				ActionEvent e = new ActionEvent();
				Bundle bundle = new Bundle();
				bundle.putSerializable(IntentConstants.INTENT_ORDER, c2SaleOrder);
				e.viewData = bundle;
				e.action = ActionEventConstant.SAVE_PURCHASE_FOR_C2;
				e.sender = this;
				SuperviorController.getInstance().handleViewEvent(e);

				break;
			}
			case ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK: {
				resetData();
				if (inputOrderPopup.isShowing()) {
					inputOrderPopup.dismiss();
				}
				break;
			}
			case ACTION_CLOSE_POPUP_CHOOSE_DATE_OK: {
				if (inputOrderForC2View != null) {
					inputOrderForC2View.showChooseDateDialog();
				}
				break;
			}
			case MENU_REPORT: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.GO_TO_REPORT_CUSTOMER_C2;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			default:
				break;
		}
//		}
	}

	/**
	 * call back chon thoi gian
	 * @author: dungnt19
	 * @since: 10:28:57 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 */
	public void updateDate(int dayOfMonth, int monthOfYear, int year) { 
		String sDay = String.valueOf(dayOfMonth);
		String sMonth = String.valueOf(monthOfYear + 1);
		if (dayOfMonth < 10) {
			sDay = "0" + sDay;
		}
		if (monthOfYear + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		Date currentDate = DateUtils.now(DateUtils.DATE_FORMAT_DEFAULT);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -c2SaleOrder.c2ReportTime);
		
		Date pastDate = DateUtils.getStartTimeOfDay(calendar.getTime());
		
		StringBuilder strDate = new StringBuilder();
		strDate.append(sDay).append("/").append(sMonth).append("/").append(year).append("");
		Date orderDate = DateUtils.parseDateFromString(strDate.toString(), DateUtils.defaultDateFormat);
		if(orderDate.compareTo(pastDate) < 0) {
			GlobalUtil.showDialogConfirm(this, parent,
					StringUtil.getStringAndReplace(R.string.TEXT_ORDER_DATE_CAN_NOT_LOWER_CURRENT_DATE_X_DAY, c2SaleOrder.c2ReportTime + ""),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null,
					false);
			return;
		} else if (orderDate.compareTo(currentDate) > 0){
			GlobalUtil.showDialogConfirm(this, parent,
					StringUtil.getString(R.string.TEXT_ORDER_DATE_CAN_NOT_OVER_CURRENT_DATE),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null,
					false);
			return;
		}
		
		inputOrderForC2View.etOrderDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
		inputOrderForC2View.etOrderDate.setText(strDate);
		c2SaleOrder.orderInfo.c2PurchaseDate = DateUtils.getDateString(DateUtils.DATE_FORMAT_SQL_DEFAULT, dayOfMonth, monthOfYear, year) + " 00:00:00";
	}
}
