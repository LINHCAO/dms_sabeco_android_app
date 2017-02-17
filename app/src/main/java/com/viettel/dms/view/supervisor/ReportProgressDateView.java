/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.ProgressDateReportDTO;
import com.viettel.dms.dto.view.ProgressDateReportDTO.ProgressDateReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Report Progress Date View
 * ReportProgressDateView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:27:57 14 Jan 2014
 */
@SuppressLint("SimpleDateFormat")
public class ReportProgressDateView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	private static final int ACTION_DATE = 1;
	private static final int ACTION_ACC = 2;
	private static final int ACTION_MHTT = 3;
	private static final int ACTION_CTTB = 4;
	private static final int ACTION_PSDS = 5;
	public static final int ACTION_MNV_CLICK = 6;
	public static final int ACTION_VIEW_DETAIL = 7;
	// main activity
	public static final String TAG = ReportProgressDateView.class.getName();
	private SupervisorActivity parent;
	private VinamilkTableView tbList;
	// private TextView tvMessage;
	private ProgressDateReportDTO dto;
	private boolean isFirst = true;

	public static ReportProgressDateView getInstance(Bundle b) {
		ReportProgressDateView instance = new ReportProgressDateView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_report_progress_date, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE)
				+ new SimpleDateFormat(" dd/MM/yyyy").format(new Date()));

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),
				R.drawable.icon_order, ACTION_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB),
				R.drawable.icon_reminders, ACTION_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT),
				R.drawable.icon_list_star, ACTION_MHTT);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, ACTION_DATE);
		setMenuItemFocus(5);

		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		if (isFirst) {
			getReport();
		}
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (!isFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * HieuNH lay danh sach bao cao trong ngay
	 */
	private void getReport() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(
				IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		b.putString(
				IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().shopId));
		e.viewData = b;
		e.action = ActionEventConstant.GET_REPORT_PROGRESS_DATE;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH tao view sau khi request xong
	 */

	private void renderLayout() {
		List<TableRow> listRows = new ArrayList<TableRow>();
		ProgressDateReportRow row;
		int kh = 0, th = 0;
		Double keHoach = 0.0, thucHien = 0.0, conLai = 0.0, keHoachTuyen = 0.0 ;
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			row = new ProgressDateReportRow(parent, ACTION_MNV_CLICK, this);
			row.render(dto.arrList.get(i), dto.minScoreNotification);
			listRows.add(row);
			kh += dto.arrList.get(i).kh;
			th += dto.arrList.get(i).th;
			keHoach += dto.arrList.get(i).keHoach;
			thucHien += dto.arrList.get(i).thucHien;
			keHoachTuyen += dto.arrList.get(i).keHoachTuyen;
			// <HaiTC> don't calculated here because "conLai" each item < 0
			// -> 0 (false)
			// conLai += dto.arrList.get(i).conLai;
		}
		// calculated here
		conLai = keHoach - thucHien > 0 ? keHoach - thucHien : 0;
		// </HaiTC> end

		row = new ProgressDateReportRow(parent, ACTION_MNV_CLICK, null);
		row.render("", StringUtil.getString(R.string.TEXT_HEADER_TABLE_SUM),
				kh, th, keHoach, thucHien, conLai, "", dto.minScoreNotification,keHoachTuyen);
		listRows.add(row);

		tbList.addContent(listRows);
		this.parent.closeProgressDialog();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_REPORT_PROGRESS_DATE:
			dto = (ProgressDateReportDTO) modelEvent.getModelData();
			renderLayout();
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

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (SupervisorActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
	}

	/**
	 * xu ly cac action
	 */

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub

		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case ACTION_MNV_CLICK: {
			 ProgressDateReportItem item = (ProgressDateReportItem) data;
			 Bundle b = new Bundle();
			 b.putString(IntentConstants.INTENT_STAFF_CODE, item.maNvbh);
			 b.putString(IntentConstants.INTENT_STAFF_ID, item.id);
			 b.putString(IntentConstants.INTENT_STAFF_PHONE, item.mobile);
			 b.putString(IntentConstants.INTENT_STAFF_NAME, item.nvbh);
			 e.action = ActionEventConstant.STAFF_INFORMATION;
			 e.sender = this;
			 e.viewData = b;
			 SuperviorController.getInstance().handleSwitchFragment(e);

			break;
		}
		case ACTION_ACC:
			isFirst = false;
			e.action = ActionEventConstant.ACC_SALE_PROG_REPORT;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MHTT: {
			isFirst = false;
			e.action = ActionEventConstant.PROG_REPOST_SALE_FOCUS;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		case ACTION_CTTB: {
			isFirst = false;
			e.action = ActionEventConstant.DIS_PRO_COM_PROG_REPORT;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		case ACTION_PSDS:
			isFirst = false;
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_VIEW_DETAIL: {
			ProgressDateReportItem item = (ProgressDateReportItem) data;
			Bundle b = new Bundle();
			b.putString(IntentConstants.INTENT_STAFF_CODE, item.maNvbh);
			b.putString(IntentConstants.INTENT_STAFF_ID, item.id);
			b.putString(IntentConstants.INTENT_STAFF_PHONE, item.mobile);
			b.putString(IntentConstants.INTENT_STAFF_NAME, item.nvbh);
			b.putString(IntentConstants.INTENT_VALUE, item.diem);
			b.putDouble(IntentConstants.INTENT_MIN_SCORE_NOTIFICATION, dto.minScoreNotification);
			e.action = ActionEventConstant.GO_TO_REPORT_PROGRESS_DATE_DETAIL_VIEW;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			break;
		}

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getReport();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
