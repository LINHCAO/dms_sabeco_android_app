/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.CabinetStaffDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
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
 * Tracking Cabinet Staff View
 * TrackingCabinetStaffView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:31:12 14 Jan 2014
 */
public class TrackingCabinetStaffView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	// main activity
	public static final String TAG = TrackingCabinetStaffView.class.getName();
	private SupervisorActivity parent;
	private TextView tvMaNV;
	private TextView tvTenNhanVien;
	private Button btXemTatCa;
	private VinamilkTableView tbTableList;
	private CabinetStaffDTO dto;
	private boolean isFirst = true;
	private int countCabinet;
	private String shopId;
	private String idNvbh;
	private String maNvbh;
	private String nvbh;
	private boolean isShowViewAll;
	private boolean isCount = false;// co goi request count customer hay kg

	public static TrackingCabinetStaffView getInstance(Bundle b) {
		TrackingCabinetStaffView instance = new TrackingCabinetStaffView();
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
				R.layout.layout_tracking_cabinet_staff, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		Bundle data = (Bundle) getArguments();
		shopId = data.getString(IntentConstants.INTENT_SHOP_ID);
		idNvbh = data.getString(IntentConstants.INTENT_STAFF_ID);
		maNvbh = data.getString(IntentConstants.INTENT_STAFF_CODE);
		nvbh = data.getString(IntentConstants.INTENT_STAFF_NAME);
		isShowViewAll = data.getBoolean(IntentConstants.INTENT_IS_VIEW_ALL);
		setTitleHeaderView(StringUtil
				.getString(R.string.TEXT_FOLLOW_UP_FROM_STAFF_TO)
				+ new SimpleDateFormat(" dd/MM/yyyy").format(new Date()));
		tvMaNV = (TextView) view.findViewById(R.id.tvMaNV);
		tvMaNV.setText(maNvbh);
		tvTenNhanVien = (TextView) view.findViewById(R.id.tvTenNhanVien);
		tvTenNhanVien.setText(nvbh);
		btXemTatCa = (Button) view.findViewById(R.id.btXemTatCa);
		btXemTatCa.setOnClickListener(this);
		if (!isShowViewAll) {
			btXemTatCa.setVisibility(View.GONE);
		}
		tbTableList = (VinamilkTableView) view.findViewById(R.id.tbTableList);
		tbTableList.setListener(this);

		if (isFirst) {
			getCountCabinetStaff();
		}
		return v;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btXemTatCa) {
			btXemTatCa.setVisibility(View.GONE);
			getCountCabinetStaff();
		}
		super.onClick(v);
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
	 * HieuNH lay count danh sach bao cao trong ngay
	 */
	private void getCountCabinetStaff() {
		isCount = true;
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, idNvbh);
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		if (btXemTatCa.getVisibility() == View.VISIBLE) {
			b.putString(IntentConstants.INTENT_IS_ALL, "0");
		} else {
			b.putString(IntentConstants.INTENT_IS_ALL, "1");
		}
		e.viewData = b;
		e.action = ActionEventConstant.GET_COUNT_CABINET_STAFF;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH lay danh sach bao cao trong ngay
	 */
	private void getCabinetStaff() {
		if (!this.parent.isShowProgressDialog()) {
			this.parent.showProgressDialog(getString(R.string.loading));
		}
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, idNvbh);
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		if (btXemTatCa.getVisibility() == View.VISIBLE) {
			b.putString(IntentConstants.INTENT_IS_ALL, "0");
		} else {
			b.putString(IntentConstants.INTENT_IS_ALL, "1");
		}
		String page = " limit "
				+ ((tbTableList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;
		if (isCount) {
			page = " limit " + (0) + "," + Constants.NUM_ITEM_PER_PAGE;
		} else {
			page = " limit "
					+ ((tbTableList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE)
					+ "," + Constants.NUM_ITEM_PER_PAGE;
		}
		b.putString(IntentConstants.INTENT_PAGE, page);
		e.viewData = b;
		e.action = ActionEventConstant.GET_CABINET_STAFF;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH tao view sau khi request xong
	 */

	private void renderLayout() {
		if (isCount) {
			isCount = false;
			tbTableList.getPagingControl().setCurrentPage(1);
			tbTableList.getPagingControl().setTotalPage(-1);
		}
		List<TableRow> listRows = new ArrayList<TableRow>();
		CabinetStaffRow row;
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbTableList.getPagingControl().getCurrentPage() - 1);
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			row = new CabinetStaffRow(parent);
			row.render(pos, dto.arrList.get(i));
			listRows.add(row);
			pos++;
		}
		tbTableList.addContent(listRows);
		if (tbTableList.getPagingControl().totalPage < 0) {
			tbTableList.setTotalSize(dto.totalList);
		}
		this.parent.closeProgressDialog();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_COUNT_CABINET_STAFF:
			countCabinet = (Integer) modelEvent.getModelData();
			if (btXemTatCa.getVisibility() == View.VISIBLE && countCabinet == 0) {
				btXemTatCa.setVisibility(View.GONE);
			}
			getCabinetStaff();
			break;
		case ActionEventConstant.GET_CABINET_STAFF:
			dto = (CabinetStaffDTO) modelEvent.getModelData();
			dto.totalList = countCabinet;
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
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getCountCabinetStaff();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
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
		if (control == tbTableList) {
			getCabinetStaff();
			// load more data for table product list
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub

	}
}
