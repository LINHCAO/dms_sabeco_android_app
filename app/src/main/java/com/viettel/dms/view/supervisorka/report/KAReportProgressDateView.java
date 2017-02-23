/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisorka.report;

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
import com.viettel.dms.view.supervisor.ProgressDateReportRow;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class KAReportProgressDateView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	private static final int ACTION_DATE = 1;
	private static final int ACTION_ACC = 2;
	private static final int ACTION_MHTT = 3;
	private static final int ACTION_CTTB = 4;
	private static final int ACTION_PSDS = 5;
	public static final int ACTION_MNV_CLICK = 6;
	public static final int ACTION_VIEW_DETAIL = 7;
	// main activity
	public static final String TAG = KAReportProgressDateView.class.getName();
	private SupervisorActivity parent;
	private VinamilkTableView tbList;
	// private TextView tvMessage;
	private ProgressDateReportDTO dto;
	private boolean isFirst = true;

	public static KAReportProgressDateView getInstance(Bundle b) {
		KAReportProgressDateView instance = new KAReportProgressDateView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_gska_report_progress_date_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TEXT_TITLE_REPORT_PROGRESS_DATE)
				+ new SimpleDateFormat(" dd/MM/yyyy").format(new Date()));
		initView(v);
		if (isFirst) {
			getReport();
		}
		return v;
	}

	@Override
	public void onResume() {
		if (!isFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * Init view
	 */
	private void initView(View v){
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, ACTION_DATE);
		setMenuItemFocus(5);
		tbList = (VinamilkTableView)v.findViewById(R.id.tbCusList);
		tbList.setListener(this);
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
		super.onAttach(activity);
		try {
			parent = (SupervisorActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
	}

	/**
	 * xu ly cac action
	 */

	@Override
	public void onEvent(int eventType, View control, Object data) {
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
