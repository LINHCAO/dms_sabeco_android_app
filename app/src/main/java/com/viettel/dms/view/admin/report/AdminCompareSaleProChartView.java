/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.admin.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Compare chart sale
 * 
 * @author : tuanlt1989
 */
public class AdminCompareSaleProChartView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener {

	public static final String TAG = AdminCompareSaleProChartView.class.getName();
	private static final int ACTION_NPP = 1; //Action report date
	private static final int ACTION_NVBH = 2; //Action report accumulate
	private GlobalBaseActivity parent;

	public static AdminCompareSaleProChartView getInstance() {
		AdminCompareSaleProChartView f = new AdminCompareSaleProChartView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_admin_compare_shop_pro_chart_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_ADMIN_COMPARE_SHOP_PRO_CHART));
		// enable menu bar
		initMenuHeader();
		// init view for screen
		initView(v);
		return v;
	}

	/**
	 * Init menu header
	 */
	public void initMenuHeader() {
        enableMenuBar(this);
        addMenuItem(StringUtil.getString(R.string.TEXT_LABLE_NPP),
                R.drawable.icon_accumulated, ACTION_NVBH);
        addMenuItem(
                StringUtil.getString(R.string.TEXT_HEADER_TABLE_NVBH),
                R.drawable.icon_calendar, ACTION_NPP);
        setMenuItemFocus(1);
    }

    /**
     * Init view
     * @param view
     */
	public void initView(View view) {
	}

	/**
	 * getCustomerList
	 * 
	 * @author: HieuNQ
	 * @return: void
	 * @throws:
	 */
	private void getAccList() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));

		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		e.viewData = b;
		e.action = ActionEventConstant.ACC_SALE_PROG_REPORT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.ACC_SALE_PROG_REPORT:
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.ACC_SALE_PROG_REPORT:
			parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * 
	 * render layout
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	private void renderLayout() {
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof MenuItem) {
			switch (eventType) {
			case ACTION_NPP: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_REPORT_PROGRESS_DATE;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			default:
				break;
			}
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
