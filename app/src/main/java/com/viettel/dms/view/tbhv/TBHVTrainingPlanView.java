/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO.TBHVTrainingPlanItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Ke hoach huan luyen GSNPP
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVTrainingPlanView extends BaseFragment implements VinamilkTableListener, OnEventControlListener,
		OnItemSelectedListener {

	public static final String TAG = TBHVTrainingPlanView.class.getName();
	private static final int ACTION_ACC = 1;
	private static final int ACTION_PLAN = 2;
	private GlobalBaseActivity parent; // activity
	private LinearLayout llCalendar; // calendar
	private TextView tvMonth; // month
	private TextView tvSpinnerTitle;
	public TBHVTrainingPlanDTO dto; // dto view
	private Spinner spinnerLine;
	SpinnerAdapter adapterLine;
	String adapterList[];

	public static TBHVTrainingPlanView newInstance() {
		TBHVTrainingPlanView f = new TBHVTrainingPlanView();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_gsnpp_tranining_plan_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_TBHV_GSNPP_TRAINING_PLAN_VIEW));
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_customer_list,
				ACTION_ACC);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_TABLE_PLAN), R.drawable.icon_calendar, ACTION_PLAN);
		setMenuItemFocus(ACTION_PLAN);

		tvMonth = (TextView) v.findViewById(R.id.tvMonth);
		tvMonth.setText(StringUtil.getString(R.string.TEXT_MONTH) + " "
				+ DateUtils.getCurrentTimeByTimeType(Calendar.MONTH) + ", "
				+ DateUtils.getCurrentTimeByTimeType(Calendar.YEAR));

		llCalendar = (LinearLayout) v.findViewById(R.id.llCalendarRow);
		spinnerLine = (Spinner) v.findViewById(R.id.spinnerPath);
		spinnerLine.setOnItemSelectedListener(this);
		tvSpinnerTitle = (TextView) v.findViewById(R.id.tvSpinnerTitle);

		if (adapterLine != null && dto != null) {
			spinnerLine.setAdapter(adapterLine);
			spinnerLine.setSelection(dto.spinnerItemSelected);
			renderLayout();
		} else {
			getReport();
		}
		return v;
	}

	/**
	 * get Report
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
		b.putLong(IntentConstants.INTENT_SHOP_ID,Long.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		e.viewData = b;
		e.action = ActionEventConstant.TBHV_TRAINING_PLAN;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay lich huan luyen cua GSNPP tuong ung spinner
	 * 
	 * @author: TamPQ
	 * @param selectedItemPosition
	 * @return: voidvoid
	 * @throws:
	 */
	private void getTrainingPlanDetailOfGsnpp(int selectedItemPosition) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_ID,Long.valueOf(dto.spinnerStaffList.arrList.get(selectedItemPosition).id));
		b.putString(IntentConstants.INTENT_SHOP_ID, dto.spinnerStaffList.arrList.get(selectedItemPosition).nvbhShopId);
		e.viewData = b;
		e.action = ActionEventConstant.GSNPP_TRAINING_PLAN_FOR_TBHV;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.TBHV_TRAINING_PLAN:
			dto = (TBHVTrainingPlanDTO) modelEvent.getModelData();

			adapterList = new String[dto.spinnerStaffList.arrList.size()];
			for (int i = 0; i < dto.spinnerStaffList.arrList.size(); i++) {
				adapterList[i] = dto.spinnerStaffList.arrList.get(i).nvbhShopCode + " - "
						+ dto.spinnerStaffList.arrList.get(i).name;
			}
			adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, adapterList);
			spinnerLine.setAdapter(adapterLine);

			// load gs co huan luyen ngay hien tai
			for (int i = 0; i < dto.spinnerStaffList.arrList.size(); i++) {
				if (dto.spinnerStaffList.arrList.get(i).nvbhId != null) {
					dto.spinnerItemSelected = i;
					break;
				}
			}
			if (dto.spinnerItemSelected != -1) {
				spinnerLine.setSelection(dto.spinnerItemSelected);
			} else {
				dto.spinnerItemSelected = spinnerLine.getSelectedItemPosition();
			}

			renderLayout();
			parent.closeProgressDialog();

			break;
		case ActionEventConstant.GSNPP_TRAINING_PLAN_FOR_TBHV:
			dto.trainingPlanOfGsnppDto = (GSNPPTrainingPlanDTO) modelEvent.getModelData();
			renderLayout();
			parent.closeProgressDialog();
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

	/**
	 * render Layout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
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
			TBHVTrainigPlanRow child = new TBHVTrainigPlanRow(parent);
			child.setListener(this);
			for (int i = 0; i < 7; i++) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				String ds = DateUtils.defaultDateFormat.format(calendar.getTime());
				child.render(month == calendar.get(Calendar.MONTH), now.getTime() == calendar.getTime().getTime(),
						calendar.get(Calendar.DAY_OF_WEEK), calendar.get(Calendar.DAY_OF_MONTH),
						dto.tbhvTrainingPlan.get(ds), dto.trainingPlanOfGsnppDto.listResult.get(ds));
			}
			llCalendar.addView(child);
			calendar.add(Calendar.DAY_OF_MONTH, 1);
			if (month != calendar.get(Calendar.MONTH)) {
				break;
			}
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		}
	}

	/**
	 * go to Giam sat huan luyen ngay
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void gotoDayTrainingSupervision(StaffItem staffItem, GSNPPTrainingPlanIem gsnppPlanItem,
			boolean isGsnppTrainedToday) {
		Bundle b = new Bundle();
		b.putSerializable(IntentConstants.INTENT_TBHV_TRAINING_PLAN_TODAY_ITEM, staffItem);
		b.putSerializable(IntentConstants.INTENT_GSNPP_TRAINING_PLAN_TODAY_ITEM, gsnppPlanItem);
		b.putBoolean(IntentConstants.INTENT_TBHV_IS_TRAINED_TODAY, isGsnppTrainedToday);
		ActionEvent e = new ActionEvent();
		e.viewData = b;
		e.action = ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT;
		e.sender = this;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control instanceof MenuItem) {
			switch (eventType) {
			case ACTION_ACC: {
				TBHVTrainingPlanItem item = dto.tbhvTrainingPlan.get(DateUtils.getCurrentDate());
				Bundle b = new Bundle();
				b.putSerializable(IntentConstants.INTENT_TBHV_TRAINING_PLAN_TODAY_ITEM, item);
				b.putSerializable(IntentConstants.INTENT_TBHV_GSNPP_TRAINING_PLAN_SPINNER, dto.spinnerStaffList);

				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.TBHV_PLAN_TRAINING_HISTORY_ACC;
				e.sender = this;
				e.viewData = b;
				TBHVController.getInstance().handleSwitchFragment(e);
				break;
			}
			default:
				break;
			}
		} else if (eventType == TBHVTrainingDateColumn.SALE_PERSON_NAME_CLICK) {
			// khi click vao ten NVBH --> vao chi tiet
			boolean isGsnppTrainedToday = false;
			Vector<Object> vt = (Vector<Object>) data;
			GSNPPTrainingPlanIem planGsnppItem = (GSNPPTrainingPlanIem) vt.get(0);

			if (vt.size() > 1) {
				TBHVTrainingPlanItem item = (TBHVTrainingPlanItem) vt.get(1);
				if (item.dateString.equals(DateUtils.getCurrentDate())) {
					isGsnppTrainedToday = true;
				}
			}
			if (planGsnppItem != null) {
				Date now = DateUtils.getStartTimeOfDay(new Date());
				if (planGsnppItem.date.compareTo(now) > 0) {
					return;
				}
				gotoDayTrainingSupervision(dto.spinnerStaffList.arrList.get(dto.spinnerItemSelected), planGsnppItem,
						isGsnppTrainedToday);
			}
		} else if (eventType == TBHVTrainingDateColumn.SUPERVISOR_NAME_CLICK) {
			// khi nhan vao tip ten GSNPP

			TBHVTrainingPlanItem item = (TBHVTrainingPlanItem) data;
			spinnerLine.setVisibility(View.VISIBLE);
			tvSpinnerTitle.setVisibility(View.VISIBLE);
			for (int i = 0; i < dto.spinnerStaffList.arrList.size(); i++) {
				if (item.staffId == Integer.valueOf(dto.spinnerStaffList.arrList.get(i).id)) {
					dto.spinnerItemSelected = i;
					break;
				}
			}
			spinnerLine.setSelection(dto.spinnerItemSelected);
			getTrainingPlanDetailOfGsnpp(dto.spinnerItemSelected);
		} else if (eventType == TBHVTrainingDateColumn.SUPERVISOR_ICON_CLICK) {
			// khi nhan vao icon GSNPP
			spinnerLine.setVisibility(View.GONE);
			tvSpinnerTitle.setVisibility(View.GONE);
			for (int i = 0, size = llCalendar.getChildCount(); i < size; i++) {
				TBHVTrainigPlanRow child = (TBHVTrainigPlanRow) llCalendar.getChildAt(i);
				child.showTipGSNPP();
			}
		} else if (eventType == TBHVTrainingDateColumn.COLUMN_CLICK) {
			// khi nhan 1 vung tren column --> tro lai man hinh nhu moi vao
			// for (int i = 0, size = llCalendar.getChildCount(); i < size; i++)
			// {
			// TBHVGsnppTrainigPlanRow child = (TBHVGsnppTrainigPlanRow)
			// llCalendar
			// .getChildAt(i);
			// child.showNameSalePerson();
			// }
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// if (action == 0) {
		// Date now = DateUtils.getStartTimeOfDay(new Date());
		// TBHVGsnppTrainingPlanItem item = (TBHVGsnppTrainingPlanItem) data;
		// if (item.date.compareTo(now) > 0) {
		// return;
		// }
		// gotoDayTrainingSupervision(item, );
		// } else if (action == 1) {
		// for (int i = 0, size = llCalendar.getChildCount(); i < size; i++) {
		// TBHVGsnppTrainigPlanRow child = (TBHVGsnppTrainigPlanRow) llCalendar
		// .getChildAt(i);
		// // child.showTipGSNPP(true);
		// }
		// }

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				spinnerLine.setVisibility(View.VISIBLE);
				getReport();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg2 != dto.spinnerItemSelected) {
			dto.spinnerItemSelected = arg2;
			getTrainingPlanDetailOfGsnpp(dto.spinnerItemSelected);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

}
