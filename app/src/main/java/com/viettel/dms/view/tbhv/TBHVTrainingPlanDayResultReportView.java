/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO.TBHVTrainingPlanDayResultReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Giam sat huan luyen ngay
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVTrainingPlanDayResultReportView extends BaseFragment implements VinamilkTableListener,
		OnEventControlListener {

	private static final int ACTION_ACC = 1;
	private static final int ACTION_PLAN = 2;

	public static final String TAG = TBHVTrainingPlanDayResultReportView.class.getName();

	public static TBHVTrainingPlanDayResultReportView newInstance(Bundle viewData) {
		TBHVTrainingPlanDayResultReportView f = new TBHVTrainingPlanDayResultReportView();
		// Supply index input as an argument.
		if (viewData != null) {
			f.setArguments(viewData);
		}
		return f;
	}

	private GlobalBaseActivity parent; // activity
	private VinamilkTableView tbList;// list
	private TBHVTrainingPlanDayResultReportDTO dto;// view dto
	private TextView tvMaNPP;// NPP code
	private TextView tvGSNPP;// GSNPP code
	private TextView tvNVBH;// NVBH code
	private TextView tvDiemHL;// DiemHL
	private Button btEvaluate; // evaluating button
	private StaffItem gsnppItem; // dto item
	private GSNPPTrainingPlanIem nvbhTrainingPlanDto; // dto item
	private boolean isGsnppTrainedToday;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();

		gsnppItem = (StaffItem) getArguments().getSerializable(IntentConstants.INTENT_TBHV_TRAINING_PLAN_TODAY_ITEM);
		nvbhTrainingPlanDto = (GSNPPTrainingPlanIem) getArguments().getSerializable(
				IntentConstants.INTENT_GSNPP_TRAINING_PLAN_TODAY_ITEM);
		isGsnppTrainedToday = getArguments().getBoolean(IntentConstants.INTENT_TBHV_IS_TRAINED_TODAY);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_day_training_supervision_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		setTitleHeaderView(initTitle());

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_customer_list,
				ACTION_ACC);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_TABLE_PLAN), R.drawable.icon_calendar, ACTION_PLAN);
		setMenuItemFocus(ACTION_ACC);

		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		tvMaNPP = (TextView) view.findViewById(R.id.tvMaNPP);
		tvGSNPP = (TextView) view.findViewById(R.id.tvGsnpp);
		tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
		tvDiemHL = (TextView) view.findViewById(R.id.tvDiemTL);
		btEvaluate = (Button) view.findViewById(R.id.btEvaluate);
		btEvaluate.setOnClickListener(this);
		if (isGsnppTrainedToday) {
			btEvaluate.setVisibility(View.VISIBLE);
		} else {
			btEvaluate.setVisibility(View.GONE);
		}
		tvMaNPP.setText(nvbhTrainingPlanDto.shopCode);

		tvGSNPP.setText(gsnppItem.name);
		tvDiemHL.setText("" + nvbhTrainingPlanDto.score);
		tvNVBH.setText(nvbhTrainingPlanDto.staffName);

		if (nvbhTrainingPlanDto != null) {
			if (dto == null) {
				getReport();
			} else {
				renderLayout();
			}
		}
		return v;
	}

	/**
	 * init title view
	 * 
	 * @author : TamPQ since : 1.0
	 */
	private SpannableObject initTitle() {
		Date date1 = null;
		String dateTime = "";
		if (nvbhTrainingPlanDto != null) {
			try {
				date1 = DateUtils.defaultDateFormat.parse(nvbhTrainingPlanDto.dateString);
			} catch (ParseException e) {
			}
			dateTime = DateUtils.getDayOfWeek(date1) + ", " + DateUtils.defaultDateFormat.format(date1);
		}

		SpannableObject title = new SpannableObject();
		title.addSpan(StringUtil.getString(R.string.TITLE_VIEW_TBHV_03_01_VIEW) + " ",
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		title.addSpan(dateTime, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		return title;
	}

	/**
	 * getReport
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getReport() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));

		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID, nvbhTrainingPlanDto.trainDetailId);
		b.putLong(IntentConstants.INTENT_SHOP_ID, nvbhTrainingPlanDto.shopId);
		e.viewData = b;
		e.action = ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);

	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.TBHV_TRAINING_PLAN_DAY_RESULT_REPORT:
			dto = (TBHVTrainingPlanDayResultReportDTO) modelEvent.getModelData();
			// dto.score = nvbhTrainingPlanDto.score;
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
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		tvDiemHL.setText("" + dto.score);
		boolean bd = nvbhTrainingPlanDto.dateString.equalsIgnoreCase(DateUtils.defaultDateFormat.format(new Date()));
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (dto.listResult.size() > 0) {
			for (TBHVTrainingPlanDayResultReportItem e : dto.listResult) {
				TBHVDayTrainingSupervisionRow child = new TBHVDayTrainingSupervisionRow(parent, false, tbList);
				child.render(e, isGsnppTrainedToday, bd);
				child.setListener(this);
				listRows.add(child);
			}
			TBHVDayTrainingSupervisionRow child = new TBHVDayTrainingSupervisionRow(parent, true, tbList);
			child.renderSum(dto);
			listRows.add(child);

		} else {
			TBHVDayTrainingSupervisionRow child = new TBHVDayTrainingSupervisionRow(parent, false, tbList);
			child.renderNoResult();
			listRows.add(child);
		}
		tbList.addContent(listRows);
	}

	@Override
	public void onClick(View v) {
		if (v == btEvaluate) {
			Bundle b = new Bundle();

			b.putString(IntentConstants.INTENT_STAFF_OWNER_CODE, gsnppItem.code); // gsnpp
			b.putString(IntentConstants.INTENT_STAFF_OWNER_ID, gsnppItem.id);// gsnpp
			b.putString(IntentConstants.INTENT_STAFF_OWNER_NAME, gsnppItem.name);// gsnpp
			b.putString(IntentConstants.INTENT_STAFF_NAME, nvbhTrainingPlanDto.staffName);// nvbh

			b.putString(IntentConstants.INTENT_SHOP_CODE, nvbhTrainingPlanDto.shopCode);
			b.putString(IntentConstants.INTENT_SHOP_NAME, nvbhTrainingPlanDto.shopName);
			b.putLong(IntentConstants.INTENT_TRAINING_DETAIL_ID, nvbhTrainingPlanDto.trainDetailId);

			// b.putInt(IntentConstants.INTENT_NVBH_STAFF_ID,
			// dayTrainingDto.nvbh_staff_id);//nvbh
			// b.putString(IntentConstants.INTENT_NVBH_STAFF_NAME,
			// dayTrainingDto.nvbh_staff_name);//nvbh
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = b;
			e.action = ActionEventConstant.GO_TO_TBHV_REVIEWS_INFO_VIEW;
			TBHVController.getInstance().handleSwitchFragment(e);
		} else {
			super.onClick(v);
		}
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
		if (action == ActionEventConstant.ACTION_TBHV_GO_TO_ADD_REQUIREMENT) {
			TBHVTrainingPlanDayResultReportItem item = (TBHVTrainingPlanDayResultReportItem) data;
			CustomerDTO cus = new CustomerDTO();
			cus.customerId = item.custId;
			cus.customerCode = item.custCode;
			cus.customerName = item.custName;

			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_CUSTOMER, cus);
			b.putSerializable(IntentConstants.INTENT_GSNPP, gsnppItem);

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.ACTION_TBHV_GO_TO_ADD_REQUIREMENT;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				getReport();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
