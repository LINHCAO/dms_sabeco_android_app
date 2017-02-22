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
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CustomerListRow;
import com.viettel.sabeco.R;

/**
 * Report region for admin
 *
 * @author : tuanlt1989
 */
public class RegionProgReportView extends BaseFragment implements VinamilkTableListener {

	public static final String TAG = RegionProgReportView.class.getName();
	private GlobalBaseActivity parent;

	private DMSTableView tbReport;// tbCusList

	public static RegionProgReportView getInstance() {
		RegionProgReportView f = new RegionProgReportView();
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
				R.layout.layout_region_progress_report_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		// init view for screen
		initView(v);
		return v;
	}

	/**
	 * Init view
	 * @param view
     */
	public void initView(View view) {
		// khoi tao header cho table
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_REPORT_PROGRESS_REGION));
		tbReport = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbReport.setListener(this);
		initHeaderTable(tbReport, new CustomerListRow(parent, this));
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
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getAccList();
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

	}
}
