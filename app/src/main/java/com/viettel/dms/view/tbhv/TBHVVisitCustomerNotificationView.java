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
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO;
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem;
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
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Canh bao ghe tham tren tuyen
 * 
 * @author: DungDQ
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVVisitCustomerNotificationView extends BaseFragment implements VinamilkTableListener,
		OnEventControlListener {
	private GlobalBaseActivity parent; // activity
	private DMSTableView tbList; // list table
	TBHVVisitCustomerNotificationDTO dto;

	private static final int MENU_STAFF_TAB = 0;
	private static final int MENU_STAFF_POSITION = 1;
	private static final int MENU_ATTENDANCE = 2;
	private static final int MENU_PLAN_PROCESS = 3;
	private static final int MENU_LIST_ORDER = 9;

	public static final String TAG = TBHVVisitCustomerNotificationView.class.getName();

	public static TBHVVisitCustomerNotificationView getInstance() {
		TBHVVisitCustomerNotificationView f = new TBHVVisitCustomerNotificationView();
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_visit_cus_notification, container, false);
		tbList = (DMSTableView) view.findViewById(R.id.tbList);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeader();
		enableMenuBar(this);
		initMenuActionBar();
		initHeaderTable(tbList, new TBHVVisitCustomerNotificationRow(parent, this));
		getTBHVVisitCusNotification();

		return v;
	}

	/**
	* set title cho layout
	* @author: dungdq3
	* @return: void
	*/
	private void setTitleHeader() {
		// TODO Auto-generated method stub
		SpannableObject obj = new SpannableObject();
		obj.addSpan(
				getString(R.string.TITLE_VIEW_GST_REPORT_VISIT_CUSTOMER_ON_PLAN)
						+ " ", ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * Lay ds cac gs
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getTBHVVisitCusNotification() {
		Bundle b = new Bundle();
		parent.showLoadingDialog();
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		String staffID= String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
		b.putString(IntentConstants.INTENT_STAFF_ID, staffID);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
		e.sender = this;
		e.viewData = b;
		TBHVController.getInstance().handleViewEvent(e);

	}

	/**
	 * Khoi tao menu bar
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
				MENU_LIST_ORDER);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task, MENU_PLAN_PROCESS);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, MENU_ATTENDANCE);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
		//addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		setMenuItemFocus(2);
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
		case MENU_PLAN_PROCESS:
			break;
		case MENU_ATTENDANCE: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ATTENDANCE;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_STAFF_POSITION: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_STAFF_TAB: {
			ActionEvent e = new ActionEvent();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (e.action) {
		case ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION:
			dto = (TBHVVisitCustomerNotificationDTO) modelEvent.getModelData();
			if (dto.listParam.size() == 0) {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_VISIT_CUSTOMER),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
			}else{
				renderLayout();
			}
			requestInsertLogKPI(HashMapKPI.GSBH_CANHBAOGHETHAMTUYEN, modelEvent.getActionEvent().startTimeFromBoot);
			break;

		default:
			break;
		}
		super.handleModelViewEvent(modelEvent);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		parent.showDialog(StringUtil.getString(R.string.TEXT_HAVE_NO_SHOP_PARAM));
	}

	/**
	 * render layout.
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		tbList.clearAllData();
		if (dto.arrList.size() > 0) {
			for (TBHVVisitCustomerNotificationItem customerNotificationItem: dto.arrList) {
				TBHVVisitCustomerNotificationRow row = new TBHVVisitCustomerNotificationRow(parent, this);
				row.render(customerNotificationItem, dto.listParam.get("DT_START").code, dto.listParam.get("DT_MIDDLE").code,
						dto.listParam.get("DT_END").code);
				tbList.addRow(row);
			}
		}else{
			tbList.showNoContentRow();
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if (action == ActionEventConstant.TBHV_SHOW_REPORT_VISIT_CUSTOMER_DETAIL_NPP) {
			int index = 0;
			TBHVVisitCustomerNotificationItem item = (TBHVVisitCustomerNotificationItem) data;
			for (TBHVVisitCustomerNotificationItem customerNotificationItem: dto.arrList) {
				if (customerNotificationItem.gsnppStaffId == item.gsnppStaffId
						&& customerNotificationItem.nvbhShopId == item.nvbhShopId) {
					break;
				}
				index++;
			}
			ActionEvent event = new ActionEvent();
			Bundle newData = new Bundle();
			newData.putSerializable(IntentConstants.INTENT_LIST_GSNPP, this.dto.arrList);
			newData.putInt(IntentConstants.INTENT_INDEX, index);
			event.action = ActionEventConstant.TBHV_SHOW_REPORT_VISIT_CUSTOMER_DETAIL_NPP;
			event.sender = this;
			event.viewData = newData;
			TBHVController.getInstance().handleSwitchFragment(event);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				dto = null;
				getTBHVVisitCusNotification();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
