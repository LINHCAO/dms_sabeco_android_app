/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.ManagerEquipmentDTO;
import com.viettel.dms.dto.view.ManagerEquipmentDTO.ManagerEquipmentItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
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
 * Manager Equipment View
 * ManagerEquipmentView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:26:42 14 Jan 2014
 */
public class ManagerEquipmentView extends BaseFragment implements OnItemSelectedListener, OnEventControlListener,
		OnClickListener, VinamilkTableListener {

	private static final int ACTION_ROW_CLICK = 1;
	// main activity
	public static final String TAG = ManagerEquipmentView.class.getName();
	private SupervisorActivity parent;
	private VinamilkTableView tbEquipment;
	private ManagerEquipmentDTO dto;
	// private int indexItemEquipmentSelected = -1;

	private boolean isFirst = true;// vo man hinh lan dau tien hay kg?

	public static ManagerEquipmentView getInstance(Bundle b) {
		ManagerEquipmentView instance = new ManagerEquipmentView();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_manager_equipment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TEXT_MANAGER_DEVICE));
		initView(v);
		if (isFirst) {
			getDanhSachThietBi();
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
	 * HieuNH khoi tao view
	 * 
	 * @param view
	 */
	private void initView(View view) {
		tbEquipment = (VinamilkTableView) view.findViewById(R.id.tbEquipment);
		tbEquipment.getHeaderView().addColumns(TableDefineContanst.EQUIPMENT_TABLE_WIDTHS,
				TableDefineContanst.EQUIPMENT_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		tbEquipment.setListener(this);
	}

	/**
	 * HieuNH request lay danh sach thiet bi
	 */
	private void getDanhSachThietBi() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_EQUIPMENT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * HieuNH tao view danh sach khach hang
	 */
	private void renderLayout() {
		ManagerEquipmentItem item;
		ManagerEquipmentRow row;
		List<TableRow> listRows = new ArrayList<TableRow>();
		int soThietBi = 0, khongDat = 0;
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			item = (ManagerEquipmentItem) dto.arrList.get(i);
			row = new ManagerEquipmentRow(parent, ACTION_ROW_CLICK);
			row.setClickable(true);
			row.setTag(Integer.valueOf(i));
			// if (indexItemEquipmentSelected == i) {
			// row.render(item, true);
			// } else {
			row.render(item);
			// }
			row.setListener(this);
			soThietBi += item.soThietBi;
			khongDat += item.khongDat;
			listRows.add(row);
		}
		row = new ManagerEquipmentRow(parent, -1);
		row.render("", "Tổng cộng", soThietBi, khongDat);
		listRows.add(row);
		tbEquipment.addContent(listRows);
		this.parent.closeProgressDialog();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_EQUIPMENT:
			dto = (ManagerEquipmentDTO) modelEvent.getModelData();
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
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (eventType == ACTION_ROW_CLICK) { // 1 row clicked
			isFirst = false;
			// indexItemEquipmentSelected = (Integer) control.getTag();
			// renderLayout();
			goToTrackingCabinetStaffView((ManagerEquipmentItem) data);
		}
	}

	/**
	 * go to man hinh quan ly tu nhan vien
	 */
	private void goToTrackingCabinetStaffView(ManagerEquipmentItem data) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		b.putString(IntentConstants.INTENT_STAFF_ID, data.id);
		b.putString(IntentConstants.INTENT_STAFF_CODE, data.maNVBH);
		b.putString(IntentConstants.INTENT_STAFF_NAME, data.nvbh);
		boolean isShowViewAll = true;
		if (data.soThietBi == data.khongDat) {
			isShowViewAll = false;
		}
		b.putBoolean(IntentConstants.INTENT_IS_VIEW_ALL, isShowViewAll);
		b.putString(IntentConstants.INTENT_SENDER, ManagerEquipmentView.TAG);
		e.viewData = b;
		e.action = ActionEventConstant.ACTION_GO_TO_TRACKING_CABINET_STAFF;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getDanhSachThietBi();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

}
