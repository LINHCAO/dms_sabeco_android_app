/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO;
import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO.GsnppTrainingResultAccReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Man hinh bao luy ke ket qua huan luyen
 * 
 * @author hieunq1
 * 
 */
public class GsnppTrainingResultAccReportView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener {

	private static final int ACTION_ACC = 1;
	private static final int ACTION_PLAN = 2;

	private GlobalBaseActivity parent;
	public TextView tvPlanDate;
	public TextView tvSoldDate;
	public TextView tvProgress;
	private VinamilkTableView tbList;

	public GsnppTrainingResultAccReportDTO dto;

	public static final String TAG = GsnppTrainingResultAccReportView.class
			.getName();

	public static GsnppTrainingResultAccReportView newInstance() {
		GsnppTrainingResultAccReportView f = new GsnppTrainingResultAccReportView();
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
				R.layout.layout_acc_train_result_report, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_VIEW_GSNPP_TRAINING_RESULT_ACC_REPORT)
				+ " " + DateUtils.defaultDateFormat.format(new Date()));
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_TABLE_PLAN),
				R.drawable.icon_calendar, ACTION_PLAN);
		setMenuItemFocus(ACTION_ACC);

		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);

		if (dto == null) {
			getReport();
		} else {
			renderLayout();
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
	private void getReport() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		e.viewData = b;
		e.action = ActionEventConstant.GSNPP_TRAINING_RESULT_ACC_REPORT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GSNPP_TRAINING_RESULT_ACC_REPORT:
			dto = (GsnppTrainingResultAccReportDTO) modelEvent.getModelData();
			renderLayout();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	private void renderLayout() {
		List<TableRow> listRows = new ArrayList<TableRow>();
		for (GsnppTrainingResultAccReportItem e : dto.listResult) {
			GsnppTrainingResultAccReportRow child = new GsnppTrainingResultAccReportRow(
					parent, false);
			child.render(e);
			child.setListener(this);
			listRows.add(child);
		}
		GsnppTrainingResultAccReportRow child = new GsnppTrainingResultAccReportRow(
				parent, true);
		child.renderSum(dto);
		listRows.add(child);
		tbList.addContent(listRows);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof MenuItem) {
			switch (eventType) {
			case ACTION_ACC: {
				break;
			}
			case ACTION_PLAN: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GSNPP_TRAINING_PLAN;
				e.sender = this;
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
		GsnppTrainingResultAccReportItem item = (GsnppTrainingResultAccReportItem) data;
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID,item.trainDetailId);
		b.putLong(IntentConstants.INTENT_STAFF_ID, item.staffId);
		b.putLong(IntentConstants.INTENT_SHOP_ID, item.shopId);
		b.putString(IntentConstants.INTENT_STAFF_NAME, item.staffName);
		b.putString(IntentConstants.INTENT_SALE_TYPE_CODE, item.saleTypeCode);
		b.putDouble(IntentConstants.INTENT_STAFF_TRAIN_SCORE, item.score);
		b.putString(IntentConstants.INTENT_STAFF_TRAIN_DATE, item.date);
		ActionEvent e = new ActionEvent();
		e.viewData = b;
		e.action = ActionEventConstant.GSNPP_TRAINING_PLAN_DAY_REPORT;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			getReport();
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
