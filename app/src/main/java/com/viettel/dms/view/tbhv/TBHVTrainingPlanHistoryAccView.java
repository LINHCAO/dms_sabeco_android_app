/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO.TBHVTrainingPlanItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO.TBHVHistoryPlanTrainingItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Lich su ke hoach giam sat
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVTrainingPlanHistoryAccView extends BaseFragment implements OnEventControlListener,
		VinamilkTableListener, OnItemSelectedListener {

	public static final String TAG = TBHVTrainingPlanHistoryAccView.class.getName();
	private static final int ACTION_ACC = 1;
	private static final int ACTION_PLAN = 2;

	public static TBHVTrainingPlanHistoryAccView newInstance(Bundle b) {
		TBHVTrainingPlanHistoryAccView f = new TBHVTrainingPlanHistoryAccView();
		// Supply index input as an argument.
		f.setArguments(b);
		return f;
	}

	private GlobalBaseActivity parent; // activity
	private VinamilkTableView tbList; // list table
	private TBHVTrainingPlanHistoryAccDTO dto; // dto view
	private TBHVTrainingPlanHistoryAccDTO trainedDto; // dto view
	public ListStaffDTO spinerGsnppList;// // spiner
										// Gsnpp List
	private Spinner spinnerLine;
	SpinnerAdapter adapterLine;
	int curItemSelected;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
		spinerGsnppList = (ListStaffDTO) getArguments().getSerializable(
				IntentConstants.INTENT_TBHV_GSNPP_TRAINING_PLAN_SPINNER);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater
				.inflate(R.layout.layout_tbhv_history_plan_training_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_TBHV_HISTORY_PLAN_TRAINING_VIEW));

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_customer_list,
				ACTION_ACC);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_TABLE_PLAN), R.drawable.icon_calendar, ACTION_PLAN);
		setMenuItemFocus(ACTION_ACC);

		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);

		String adapterList[] = new String[spinerGsnppList.arrList.size() + 1];
		adapterList[0] = StringUtil.getString(R.string.TEXT_ALL_TRANING);
		for (int i = 0; i < spinerGsnppList.arrList.size(); i++) {
			adapterList[i + 1] = spinerGsnppList.arrList.get(i).nvbhShopCode + " - "
					+ spinerGsnppList.arrList.get(i).name;
		}

		spinnerLine = (Spinner) v.findViewById(R.id.spinnerPath);
		spinnerLine.setOnItemSelectedListener(this);
		adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, adapterList);
		spinnerLine.setAdapter(adapterLine);
		curItemSelected = spinnerLine.getSelectedItemPosition();

		if (dto == null) {
			getReport(0, null);
		} else {
			renderLayout();
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
	private void getReport(int gsnppStaffId, String shopId) {// staffId = 0 :
																// chon tat ca,
																// #0:
		// staffId tuong ung
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		b.putInt(IntentConstants.INTENT_GSNPP_STAFF_ID, gsnppStaffId);
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		e.viewData = b;
		e.action = ActionEventConstant.TBHV_PLAN_TRAINING_HISTORY_ACC;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.TBHV_PLAN_TRAINING_HISTORY_ACC:
			dto = (TBHVTrainingPlanHistoryAccDTO) modelEvent.getModelData();
			if (spinnerLine.getSelectedItemPosition() == 0) {
				trainedDto = (TBHVTrainingPlanHistoryAccDTO) modelEvent.getModelData();
			}
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

	/**
	 * render Layout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		List<TableRow> listRows = new ArrayList<TableRow>();
		TBHVHistoryPlanTrainingRow child;
		if (dto.listResult.size() > 0) {
			for (TBHVHistoryPlanTrainingItem e : dto.listResult) {
				child = new TBHVHistoryPlanTrainingRow(parent, false);
				if (curItemSelected > 0) {
					child.render(e, trainedDto);
				} else {
					child.render(e, null);
				}
				child.setListener(this);
				listRows.add(child);
			}
		} else {
			child = new TBHVHistoryPlanTrainingRow(parent, false);
			child.renderNoResult();
			listRows.add(child);
		}

		tbList.addContent(listRows);
	}

	/**
	 * go to Giam sat huan luyen ngay
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void gotoDayTrainingSupervision(TBHVTrainingPlanItem item) {
		Bundle b = new Bundle();
		b.putSerializable(IntentConstants.INTENT_TBHV_TRAINING_PLAN_TODAY_ITEM, item);
		ActionEvent e = new ActionEvent();
		e.viewData = b;
		e.action = ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT;
		e.sender = this;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_PLAN: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = b;
			e.action = ActionEventConstant.TBHV_TRAINING_PLAN;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if (action == ActionEventConstant.GSNPP_TRAINING_PLAN_FOR_TBHV) {
			TBHVHistoryPlanTrainingItem item = (TBHVHistoryPlanTrainingItem) data;

			ListStaffDTO temp = new ListStaffDTO();
			StaffItem gsnppItem = temp.newStaffItem();
			gsnppItem.code = item.staffCode;
			gsnppItem.name = item.staffName;
			gsnppItem.id = String.valueOf(item.staffId);

			GSNPPTrainingPlanDTO temp2 = new GSNPPTrainingPlanDTO();
			GSNPPTrainingPlanIem gsnppDayTrainingDto = temp2.newPlanTrainResultReportItem();
			gsnppDayTrainingDto.dateString = item.date;
			gsnppDayTrainingDto.shopCode = item.nvbhShopCode;
			gsnppDayTrainingDto.shopName = item.nvbhShopName;
			gsnppDayTrainingDto.score = item.score;
			gsnppDayTrainingDto.staffName = item.nvbhStaffName;
			gsnppDayTrainingDto.staffId = item.nvbhStaffId;
			gsnppDayTrainingDto.trainDetailId = item.nvbh_trainDetailId;
			gsnppDayTrainingDto.shopId = item.nvbhShopId;

			boolean isGsnppTrainedToday = item.isTrainedTodayByTbvh(trainedDto);

			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_TBHV_TRAINING_PLAN_TODAY_ITEM, gsnppItem);
			b.putSerializable(IntentConstants.INTENT_GSNPP_TRAINING_PLAN_TODAY_ITEM, gsnppDayTrainingDto);
			b.putBoolean(IntentConstants.INTENT_TBHV_IS_TRAINED_TODAY, isGsnppTrainedToday);
			ActionEvent e = new ActionEvent();
			e.viewData = b;
			e.action = ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
		}

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				getReport(0, null);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (curItemSelected != arg2) {
			curItemSelected = arg2;
			if (curItemSelected != 0) {
				getReport(Integer.valueOf(spinerGsnppList.arrList.get(curItemSelected - 1).id),
						spinerGsnppList.arrList.get(curItemSelected - 1).nvbhShopId);
			} else {
				getReport(0, null);
			}

		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
