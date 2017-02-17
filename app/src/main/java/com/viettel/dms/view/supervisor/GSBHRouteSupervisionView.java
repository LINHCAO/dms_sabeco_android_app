/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.GsnppLessThan2MinsDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.dto.view.WrongPlanCustomerDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.AnimationImageView;
import com.viettel.dms.view.control.AnimationLinearLayout;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Danh sach giam sat lo trinh nhan vien ban hang cua GSBH
 * GsnppRouteSupervisionView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  09:39:38 5 Dec 2013
 */
public class GSBHRouteSupervisionView extends BaseFragment implements VinamilkTableListener, OnEventControlListener {

	private GlobalBaseActivity parent;
	private DMSTableView tbCusList;
	private GsnppRouteSupervisionDTO dto;
	private GsnppRouteSupervisionDTO wrongDto;
	private GsnppRouteSupervisionDTO rightDto;
	private Button btViewAll;
	private AlertDialog alertRemindDialog;
	private boolean isViewAllVisible = true;

	public static final String TAG = GSBHRouteSupervisionView.class.getName();
	private static final int MENU_STAFF_TAB = 0;
	private static final int MENU_STAFF_POSITION = 1;
	// hien thi man hinh cham cong
	private static final int MENU_STAFF_TIMEKEEPING = 7;
	// hien thi man hinh canh bao ghe tham tren tuyen
	private static final int MENU_STAFF_GOING_ONLINE = 3;
	private static final int MENU_LIST_ORDER = 4;
	public static GSBHRouteSupervisionView newInstance() {
		GSBHRouteSupervisionView f = new GSBHRouteSupervisionView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_sale_roadmap_monitor, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_HEADER_MONITORING_SALES_ROUTE_VIEW));
		tbCusList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		initHeaderTable(tbCusList, new GsnppRouteSupervisionRow(parent, this));
		btViewAll = (Button) view.findViewById(R.id.btViewAll);
		btViewAll.setOnClickListener(this);
		btViewAll.setVisibility(View.GONE);
		if (dto != null) {
			if (!isViewAllVisible) {
				btViewAll.setVisibility(View.GONE);
				renderLayout(dto);
			} else {
				renderLayout(wrongDto);
				btViewAll.setVisibility(View.VISIBLE);
			}

		} else {
			getGsnppRouteSupervision();
		}
		//kick lai luong dinh vi, han che dinh vi cham o gsnpp
		PositionManager.getInstance().reStart();
		
		enableMenuBar(this);
		initMenuActionBar();
		return v;
	}

	/**
	 * Init menu action bar
	 * @author: duongdt3
	 * @since: 08:41:57 13 Jan 2014
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
				MENU_LIST_ORDER);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task,
				MENU_STAFF_GOING_ONLINE);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock,
				MENU_STAFF_TIMEKEEPING);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
		addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);

		setMenuItemFocus(5);
	}

	/**
	 * get data from DB
	 * @author: duongdt3
	 * @since: 08:43:07 13 Jan 2014
	 * @return: void
	 * @throws:
	 */
	private void getGsnppRouteSupervision() {
		parent.showLoadingDialog();
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		bundle.putString(IntentConstants.INTENT_DAY, DateUtils.getToday());

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION;
		e.sender = this;
		e.viewData = bundle;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * getListSaleRoadMapSupervisor
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getWrongPlanCustomer(long staffId) {
		parent.showLoadingDialog();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_WRONG_PLAN_CUSTOMER;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay ds be hon 2 phut
	 * 
	 * @author: TamPQ
	 * @param staffId
	 * @return: voidvoid
	 * @throws:
	 */
	private void getLessThan2MinsOrMorThan30MinsCustomer(long staffId, String list, boolean isLessThan2Min) {
		parent.showLoadingDialog();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STRING_LIST, list);
		b.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		b.putBoolean(IntentConstants.INTENT_LESS_THAN_2_MINS, isLessThan2Min);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_LESS_THAN_2_MINS;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * showWrongCustomerDialog
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void showWrongCustomerDialog(WrongPlanCustomerDTO wrongDto) {
		Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);

		WrongCustomerListView view = new WrongCustomerListView(parent, wrongDto);
		build.setView(view.viewLayout);
		alertRemindDialog = build.create();
		Window window = alertRemindDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		alertRemindDialog.show();
	}

	/**
	 * Mo ta muc dich cua ham
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void showLessThan2MinDialog(GsnppLessThan2MinsDTO dto) {
		Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);

		GsnppLessThan2MinView view = new GsnppLessThan2MinView(parent, dto);
		build.setView(view.viewLayout);
		alertRemindDialog = build.create();
		Window window = alertRemindDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		if (!alertRemindDialog.isShowing()) {
			alertRemindDialog.show();
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION:
			dto = (GsnppRouteSupervisionDTO) modelEvent.getModelData();
			wrongDto = new GsnppRouteSupervisionDTO();
			rightDto = new GsnppRouteSupervisionDTO();
			for (int i = 0, s = dto.itemList.size(); i < s; i++) {
				GsnppRouteSupervisionItem item = dto.itemList.get(i);
				if (item.numWrongPlan > 0 || item.numMoreThan30Min > 0 || item.numLessThan2Min > 0) {
					wrongDto.itemList.add(item);
				} else {
					rightDto.itemList.add(item);
				}
			}

			if (wrongDto.itemList.size() > 0) {
				renderLayout(wrongDto);
				isViewAllVisible = true;
				btViewAll.setVisibility(View.VISIBLE);
			} else {
				renderLayout(dto);
				isViewAllVisible = false;
				btViewAll.setVisibility(View.GONE);
			}
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_GIAMSATLOTRINHBANHANG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_WRONG_PLAN_CUSTOMER:
			parent.closeProgressDialog();
			WrongPlanCustomerDTO wrongDto = (WrongPlanCustomerDTO) modelEvent.getModelData();
			showWrongCustomerDialog(wrongDto);
			requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHKHACHHANGGHETHAMSAITUYEN, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_LESS_THAN_2_MINS:
			parent.closeProgressDialog();
			Bundle b = (Bundle) modelEvent.getActionEvent().viewData;
			GsnppLessThan2MinsDTO dto = (GsnppLessThan2MinsDTO) modelEvent.getModelData();
			dto.isLessThan2Min = b.getBoolean(IntentConstants.INTENT_LESS_THAN_2_MINS);
			showLessThan2MinDialog(dto);
			requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHKHACHHANGGHETHAMSAITHOIGIAN, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	private void renderLayout(GsnppRouteSupervisionDTO sDto) {
		if (sDto == null || sDto.itemList == null) {
			return;
		}
		tbCusList.clearAllData();
		if (sDto.itemList.size() > 0) {	
			for (int i = 0, s = sDto.itemList.size(); i < s; i++) {
				GsnppRouteSupervisionRow row = new GsnppRouteSupervisionRow(parent, this);
				row.render(sDto.itemList.get(i));
				tbCusList.addRow(row);
			}
		} else {
			tbCusList.showNoContentRow();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btViewAll) {
			renderLayout(dto);
			isViewAllVisible = false;
			btViewAll.setVisibility(View.GONE);
		} else if (v.getId() == R.id.btClose) {
			this.alertRemindDialog.dismiss();
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW:
			GsnppRouteSupervisionItem dto = (GsnppRouteSupervisionItem) data;
			Bundle b = new Bundle(); 
			b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-01");
			b.putSerializable(IntentConstants.INTENT_STAFF_DTO, dto);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case ActionEventConstant.STAFF_INFORMATION:
			dto = (GsnppRouteSupervisionItem) data;
			b = new Bundle();
			b.putString(IntentConstants.INTENT_STAFF_CODE, dto.aStaff.staffCode);
			b.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(dto.aStaff.staffId));
			if (dto.aStaff.mobile != null) {
				b.putString(IntentConstants.INTENT_STAFF_PHONE, String.valueOf(dto.aStaff.mobile));
			}
			b.putString(IntentConstants.INTENT_STAFF_NAME, String.valueOf(dto.aStaff.name));
			e = new ActionEvent();
			e.action = ActionEventConstant.STAFF_INFORMATION;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case ActionEventConstant.GET_WRONG_PLAN_CUSTOMER:
			dto = (GsnppRouteSupervisionItem) data;
			if (dto.numWrongPlan > 0) {
				getWrongPlanCustomer(dto.aStaff.staffId);
			}
			break;
		case ActionEventConstant.GET_LESS_THAN_2_MINS:
			dto = (GsnppRouteSupervisionItem) data;
			if (dto.numLessThan2Min > 0) {
				getLessThan2MinsOrMorThan30MinsCustomer(dto.aStaff.staffId, dto.lessThan2MinList, true);
			}
			break;
		case ActionEventConstant.GET_MORE_THAN_30_MINS:
			dto = (GsnppRouteSupervisionItem) data;
			if (dto.numMoreThan30Min > 0) {
				getLessThan2MinsOrMorThan30MinsCustomer(dto.aStaff.staffId, dto.moreThan30MinList, false);
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				getGsnppRouteSupervision();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_STAFF_TAB:

			break;
		case MENU_STAFF_POSITION: {
			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_SALE_ROAD_MAP, dto);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.SUPERVISE_STAFF_POSITION;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_STAFF_GOING_ONLINE:
			this.gotoReportVisitCustomerOnPlan();
			break;
		case MENU_LIST_ORDER:
			this.gotoListOrder();
			break;
		case MENU_STAFF_TIMEKEEPING: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			break;
		}
		super.onEvent(eventType, control, data);
	}

	/**
	 * 
	 * go go report visit customer on plan view - update for CR0074
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoReportVisitCustomerOnPlan() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_REPORT_VISIT_CUSTOMER_ON_PLAN;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 *
	 * go go report visit customer on plan view - update for CR0074
	 *
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoListOrder() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ORDER;
		e.sender = this;
		e.viewData = b;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}
}
