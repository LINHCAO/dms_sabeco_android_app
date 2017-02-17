/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.Calendar;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Man hinh ke hoach huan luyen
 * 
 * @author hieunq1
 * 
 */
public class GSNPPTrainingPlanView extends BaseFragment implements OnEventControlListener, VinamilkTableListener {

	private static final int ACTION_ACC = 1;
	private static final int ACTION_PLAN = 2;

	private GlobalBaseActivity parent;

	public GSNPPTrainingPlanDTO dto;
	private LinearLayout llCalendar;

	public static final String TAG = GSNPPTrainingPlanView.class.getName();

	public static GSNPPTrainingPlanView newInstance() {
		GSNPPTrainingPlanView f = new GSNPPTrainingPlanView();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_plan_train_result_report, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_GSNPP_04_01_VIEW));
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated,
				ACTION_ACC);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_TABLE_PLAN), R.drawable.icon_calendar, ACTION_PLAN);
		setMenuItemFocus(ACTION_PLAN);

		TextView tvMonth = (TextView) v.findViewById(R.id.tvMonth);
		tvMonth.setText(StringUtil.getString(R.string.TEXT_MONTH) + " "
				+ DateUtils.getCurrentTimeByTimeType(Calendar.MONTH) + ", "
				+ DateUtils.getCurrentTimeByTimeType(Calendar.YEAR));
		llCalendar = (LinearLayout) v.findViewById(R.id.llCalendarRow);

		if (dto == null) {
			getGsnppTrainingPlan();
		} else {
			renderLayout();
		}

		//kick lai luong dinh vi, han che ko dinh vi duoc khi vao cham ww
		PositionManager.getInstance().reStart();
		return v;
	}

	/**
	 * getReport
	 * 
	 * @author: HieuNQ
	 * @return: void
	 * @throws:
	 */
	private void getGsnppTrainingPlan() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		e.viewData = b;
		e.action = ActionEventConstant.GSNPP_TRAINING_PLAN;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GSNPP_TRAINING_PLAN:
			dto = (GSNPPTrainingPlanDTO) modelEvent.getModelData();
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
		llCalendar.removeAllViews();

		Date now = DateUtils.getStartTimeOfDay(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(DateUtils.getStartTimeOfMonth(now));
		int month = calendar.get(Calendar.MONTH);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		dayOfWeek = (dayOfWeek == Calendar.SUNDAY ? 8 : dayOfWeek);
		calendar.add(Calendar.DAY_OF_MONTH, Calendar.MONDAY - dayOfWeek - 1);
		while (true) {
			GSNPPTrainingPlanRow child = new GSNPPTrainingPlanRow(parent);
			child.setListener(this);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				String ds = DateUtils.defaultDateFormat.format(calendar.getTime());
				child.render(month == calendar.get(Calendar.MONTH), now.getTime() == calendar.getTime().getTime(),
						calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.DAY_OF_MONTH), dto.listResult.get(ds));
			}
			llCalendar.addView(child);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (month != calendar.get(Calendar.MONTH)) {
				break;
			}
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof MenuItem) {
			switch (eventType) {
			case ACTION_ACC: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GSNPP_TRAINING_RESULT_ACC_REPORT;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_PLAN:

				break;
			default:
				break;
			}
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		Date now = DateUtils.getStartTimeOfDay(new Date());
		GSNPPTrainingPlanIem item = (GSNPPTrainingPlanIem) data;
		if (item.date.compareTo(now) > 0) {
			return;
		}
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID, item.trainDetailId);
		b.putLong(IntentConstants.INTENT_STAFF_ID, item.staffId);
		b.putString(IntentConstants.INTENT_SALE_TYPE_CODE, item.saleTypeCode);
		b.putLong(IntentConstants.INTENT_SHOP_ID, item.shopId);
		b.putString(IntentConstants.INTENT_STAFF_NAME, item.staffName);
		b.putDouble(IntentConstants.INTENT_STAFF_TRAIN_SCORE, item.score);
		b.putString(IntentConstants.INTENT_STAFF_TRAIN_DATE, item.dateString);
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
			getGsnppTrainingPlan();
			break;

		default:
			break;
		}
	}

}
