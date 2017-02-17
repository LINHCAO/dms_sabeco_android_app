/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.TBHVAttendanceDTO;
import com.viettel.dms.dto.view.TBHVAttendanceDTO.TVBHAttendanceItem;
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
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * 02-03. TBHV Cham cong ngay
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TBHVAttendanceView extends BaseFragment implements VinamilkTableListener {
	// tag for fragment
	public static final String TAG = TBHVAttendanceView.class.getName();
	// show tab giam sat lo trinh
	private static final int MENU_STAFF_TAB = 0;
	// sho tab xem vi tri
	private static final int MENU_STAFF_POSITION = 1;
	// show tab cham cong ngay
	private static final int MENU_ATTENDANCE = 2;
	// show tab di tuyen ngay
	private static final int MENU_PLAN_PROCESS = 3;
	private static final int MENU_LIST_ORDER = 9;

	// table report take attend
	DMSTableView tbReportTakeAttend;
	// parent activity
	private TBHVActivity parent;
	// data for screen
	private TBHVAttendanceDTO dto = new TBHVAttendanceDTO();
	//
	boolean isFirst;

	public static TBHVAttendanceView getInstance(Bundle data) {
		TBHVAttendanceView instance = new TBHVAttendanceView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (TBHVActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_report_take_attend_of_day_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.initView(v);
		this.setTitleForScreen();
		initHeaderTable(tbReportTakeAttend, new TBHVAttendanceRow(parent, this));
		this.initMenuActionBar();
		if(!isFirst) {
			isFirst = true;
			this.getTbhvAttendance();
		}
		return v;
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(isFirst) {
			renderLayout();
		}
	}

	/**
	 * init all control in view
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	private void initView(View v) {
		tbReportTakeAttend = (DMSTableView) v.findViewById(R.id.tbReportTakeAttend);
		tbReportTakeAttend.setOnClickListener(this);
	}

	/**
	 * init menu header
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	private void initMenuActionBar() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
				MENU_LIST_ORDER);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task, MENU_PLAN_PROCESS);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, MENU_ATTENDANCE);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
		//addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		setMenuItemFocus(3);
	}

	/**
	 * set title for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void setTitleForScreen() {
		SpannableObject obj = new SpannableObject();
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_TAKE_ATTEND_OF_DAY_VIEW) + " ",
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * 
	 * get report take attend of day
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void getTbhvAttendance() {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		String staffID= String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
		b.putString(IntentConstants.INTENT_STAFF_ID, staffID);
		e.action = ActionEventConstant.TBHV_ATTENDANCE;
		e.sender = this;
		e.viewData = b;
		TBHVController.getInstance().handleViewEvent(e);
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
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_LIST_ORDER:
			this.gotoListOrder();
			break;
		case MENU_STAFF_TAB: {
			ActionEvent e = new ActionEvent();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_STAFF_POSITION: {
			ActionEvent e = new ActionEvent();
			Bundle b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_PLAN_PROCESS: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_ATTENDANCE:
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	public void renderLayout() {
		tbReportTakeAttend.clearAllData();
		if (dto.arrGsnppList.size() > 0) {
			for (TVBHAttendanceItem attendanceItem: dto.arrGsnppList) {
				TBHVAttendanceRow row = new TBHVAttendanceRow(parent, this);
				row.render(attendanceItem);
				tbReportTakeAttend.addRow(row);
			}
		}else{
			tbReportTakeAttend.showNoContentRow();
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.TBHV_ATTENDANCE: {
			dto = (TBHVAttendanceDTO) modelEvent.getModelData();
			if (dto.listParam.size() < 3) {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_ATTENDANCE),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
			}else{
				dto.processCount();
				this.renderLayout();
			}
			this.parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSBH_CHAMCONG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.TBHV_ATTENDANCE: {
			GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_ATTENDANCE),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
			this.parent.closeProgressDialog();
			break;
		}
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				this.getTbhvAttendance();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case ActionEventConstant.TBHV_ATTENDANCE_DETAIL:
			Bundle bundle = new Bundle();
			TVBHAttendanceItem item = (TVBHAttendanceItem) data;
			bundle.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(item.gsnppStaffId));
			bundle.putString(IntentConstants.INTENT_SHOP_ID, String.valueOf(item.nvbhShopId));
			ActionEvent e = new ActionEvent();
			e.viewData = bundle;
			e.sender = this;
			e.action = ActionEventConstant.TBHV_GET_LIST_SALE_FOR_ATTENDANCE;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		default:
			break;
		}
	}
}
