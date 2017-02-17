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
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.C2SaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.C2SaleOrderlViewDTO;
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
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Ds KH cua C2
 * CustomerOfC2ListView.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:43:12 12-12-2013
 */
public class CustomerOfC2ListView extends BaseFragment implements OnEventControlListener, OnClickListener,
		VinamilkTableListener {
	public static final int ACTION_SAVE_ORDER_FOR_C2 = 3;
	public static final int ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK = 4;
	public static final int ACTION_CLOSE_POPUP_CHOOSE_DATE_OK = 5;

	public static final String TAG = CustomerOfC2ListView.class.getName();

	private GlobalBaseActivity parent; // parent
	public CustomerListItem c2Customer;
	public CustomerListDTO cusDto;// cusList
	public C2SaleOrderlViewDTO c2SaleOrder = new C2SaleOrderlViewDTO();
	private TextView tvC2Name;
	private DMSTableView tbCusList;// tbCusList
	private VNMEditTextClearable edMKH;// edMKH
	private VNMEditTextClearable edTKH;// edTKH
	private Button btSearch; // btSearch
	private int currentPage = -1;

	private String textCusCode = "";
	private String textCusName = "";

	private boolean isUpdateData = false;

	public boolean isBackFromPopStack = false;
	CountDownTimer timer = null;
	boolean isSearchOrSelectSpiner = false;
	AlertDialog inputOrderPopup;
	InputOrderForC2View inputOrderForC2View;
	private CustomerListItem selectedCusItem;
	
	public static CustomerOfC2ListView getInstance(Bundle args) {
		CustomerOfC2ListView f = new CustomerOfC2ListView();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_list_of_c2, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		c2Customer = (CustomerListItem) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);

		tvC2Name = (TextView) v.findViewById(R.id.tvC2Name);
		tvC2Name.setText(c2Customer.aCustomer.customerName);
		edMKH = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
		edTKH = (VNMEditTextClearable) v.findViewById(R.id.edCusName);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		tbCusList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		initHeaderTable(tbCusList, new CustomerOfC2ListRow(parent, this));

		if (!isBackFromPopStack) {
			if (cusDto != null && currentPage != -1) {
				renderLayout();
			} else {
				isUpdateData = true;
				getCustomerList(1, true);
			}
			setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_LIST_OF_C2));
		} else {
			isBackFromPopStack = false;
		}

		return v;
	}

	/**
	 * Lay ds KH
	 * @author: dungnt19
	 * @since: 10:43:31 12-12-2013
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
		bundle.putLong(IntentConstants.INTENT_SHOP_ID,Long.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		bundle.putLong(IntentConstants.INTENT_CUSTOMER_ID, c2Customer.aCustomer.customerId);
		// false: ko lay ngoai tuyen, true lay them ngoai tuyen voi dk da ghe tham
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);

		// check data search
		bundle.putInt(IntentConstants.INTENT_ROLE_TYPE, 0);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, textCusName);

		e.viewData = bundle;
		e.action = ActionEventConstant.GET_CUSTOMER_LIST_OF_C2;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * Lay ds sp de dat hang
	 * @author: dungnt19
	 * @since: 10:43:42 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void getListProductForC2() {
		ActionEvent e = new ActionEvent();

		Bundle bundle = new Bundle();
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_PRODUCT_FOR_C2_ORDER;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_CUSTOMER_LIST_OF_C2: {
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
				if(cusDto.cusList.size()>0){
					createInputPopup();
				}
				renderLayout();
			}
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHKHACHHANGMUAHANGCUAC2, modelEvent.getActionEvent().startTimeFromBoot);

			break;
		}
		case ActionEventConstant.GET_LIST_PRODUCT_FOR_C2_ORDER: {
			c2SaleOrder = (C2SaleOrderlViewDTO) modelEvent.getModelData();
			c2SaleOrder.orderInfo.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			c2SaleOrder.orderInfo.shopId = Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId);
			c2SaleOrder.orderInfo.c2Id = Long.valueOf(c2Customer.aCustomer.getCustomerId());
			c2SaleOrder.orderInfo.status = 1;
			if(inputOrderForC2View != null) {
				inputOrderForC2View.createLayout(c2SaleOrder);
			}
			inputOrderPopup.show();
			inputOrderPopup.dismiss();
			break;
		}
		case ActionEventConstant.SAVE_ORDER_FOR_C2: {
			resetData();
			parent.showDialog(StringUtil.getString(R.string.SAVE_ORDER_C2_SUCCESS));
			if(inputOrderForC2View != null && inputOrderPopup.isShowing()) {
				inputOrderPopup.dismiss();
			}
			getCustomerList(currentPage, false);
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
	 * @since: 10:44:39 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void resetData() {
		for (C2SaleOrderDetailViewDTO detailView : c2SaleOrder.listProduct) {
			detailView.orderDetail.quantity = 0;
			detailView.orderDetail.price = 0;
			detailView.orderDetail.amount = 0;
		}
		c2SaleOrder.totalQuantity = 0;
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.SAVE_ORDER_FOR_C2: {
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
	 * Render layout MH
	 * @author: dungnt19
	 * @since: 10:44:48 12-12-2013
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
			tbCusList.setTotalSize(cusDto.totalCustomer,1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
		tbCusList.clearAllData();
		if (cusDto.cusList.size() > 0) {
			for (CustomerListItem item: cusDto.cusList) {
				CustomerOfC2ListRow row = new CustomerOfC2ListRow(parent, this);
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
	 * @since: 10:45:06 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param customerId
	 */
	public void gotoCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "03-01");
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Reset cac gia tri cua view ve mac dinh
	 * @author: dungnt19
	 * @since: 10:45:17 12-12-2013
	 * @return: void
	 * @throws:
	 */
	private void resetAllValue() {
		textCusName = "";
		textCusCode = "";
		edMKH.setText(textCusCode);
		edTKH.setText(textCusName);
		currentPage = -1;
		cusDto = null;
		inputOrderPopup = null;
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
		case ActionEventConstant.VISIT_CUSTOMER: {
			CustomerListItem item = (CustomerListItem) data;
			showInputOrderForC2(item);
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
//				getListProductForC2();
				// demo gui file db.zip va log file len server
				// sendzipFile();
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
	 * @param cusItem
	 */
	private void createInputPopup() {
		if (inputOrderPopup == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			inputOrderForC2View = new InputOrderForC2View(parent, this, ACTION_SAVE_ORDER_FOR_C2,ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK);
			build.setView(inputOrderForC2View.viewLayout);
			inputOrderPopup = build.create();
			inputOrderPopup.setCanceledOnTouchOutside(false);
			Window window = inputOrderPopup.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
			
			getListProductForC2();
		}
	}
	/**
	 * Hien thi popup dat hang cua KH cua C2
	 * @author: dungnt19
	 * @since: 10:45:38 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param cusItem
	 */
	private void showInputOrderForC2(CustomerListItem cusItem) {
//		if (inputOrderPopup == null) {
//			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
//			inputOrderForC2View = new InputOrderForC2View(parent, this, ACTION_SAVE_ORDER_FOR_C2,ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK);
//			inputOrderForC2View.etOrderDate.setOnTouchListener(this);
//			build.setView(inputOrderForC2View.viewLayout);
//			inputOrderPopup = build.create();
//			inputOrderPopup.setCanceledOnTouchOutside(false);
//			Window window = inputOrderPopup.getWindow();
//			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
//			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//			window.setGravity(Gravity.CENTER);
//			
////			getListProductForC2();
//		}
		selectedCusItem = cusItem;
		inputOrderForC2View.renderLayout(c2SaleOrder, cusItem);
		inputOrderPopup.show();
	}
	
	@Override
	public void onEvent(int eventType, View control, Object data) {
//		if(control == inputOrderForC2View) {
			switch (eventType) {
			case ACTION_SAVE_ORDER_FOR_C2: {
				ActionEvent e = new ActionEvent();
				c2SaleOrder.orderInfo.customerId = selectedCusItem.aCustomer.customerId;
				Bundle bundle = new Bundle();
				bundle.putSerializable(IntentConstants.INTENT_ORDER, c2SaleOrder);
				e.viewData = bundle;
				e.action = ActionEventConstant.SAVE_ORDER_FOR_C2;
				e.sender = this;
				SuperviorController.getInstance().handleViewEvent(e);
				
				break;	
			}
			case ACTION_BUTTON_CLOSE_POPUP_INPUT_CLICK: {
				resetData();
				if(inputOrderPopup.isShowing()) {
					inputOrderPopup.dismiss();
				}
				break;
			}
			case ACTION_CLOSE_POPUP_CHOOSE_DATE_OK: {
				if(inputOrderForC2View != null) {
					inputOrderForC2View.showChooseDateDialog();
				}
				break;
			}
			default:
				break;
			}
//		}
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
	 * Call back chon thoi gian
	 * @author: dungnt19
	 * @since: 10:45:54 12-12-2013
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
		c2SaleOrder.orderInfo.orderDate = DateUtils.getDateString(DateUtils.DATE_FORMAT_SQL_DEFAULT, dayOfMonth, monthOfYear, year) + " 00:00:00";
	}
}
