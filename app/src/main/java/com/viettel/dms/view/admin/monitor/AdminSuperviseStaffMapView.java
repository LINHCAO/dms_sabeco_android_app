/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.admin.monitor;

import android.app.Activity;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.control.SpinnerItemDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.CustomerPositionLogDTO;
import com.viettel.dms.dto.view.CustomerUpdateLocationDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VNMSpinnerTextAdapter;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.CustomerSaleLocationResetHeaderView;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.MarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.sabeco.R;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Admin supervise staff mapview
 *
 * @author : tuanlt1989
 */
public class AdminSuperviseStaffMapView extends FragmentMapView implements OnItemSelectedListener {
	public static final String TAG = AdminSuperviseStaffMapView.class.getName();
	private AdminSuperviseStaffHeaderView header; //Header
	private boolean isFirstLoad = false; //Check is first load or no

	public static AdminSuperviseStaffMapView newInstance(Bundle data) {
		AdminSuperviseStaffMapView f = new AdminSuperviseStaffMapView();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		header = new AdminSuperviseStaffHeaderView(parent);
		// enable menu bar
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_ADMIN_SUPERVISE_STAFF));
		double myLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		double myLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		if (myLat > 0 && myLng > 0) {
			mapView.moveTo(new LatLng(myLat, myLng));
		} else {
			mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
		}
		parent.restartLocating(true);
		// init du lieu dau
		setCustomerLocationHeader();
		// vi tri hien tai
		drawMarkerMyPosition();
		return v;
	}

	/**
	 * Hien thi header thong tin khach hang tren ban do
	 *
	 * @author : BangHN since : 1.0
	 */
	private void setCustomerLocationHeader() {
		if (!isFirstLoad) {
			LinearLayout llHeader;
			llHeader = new LinearLayout(rlMainMapView.getContext());
			llHeader.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			llHeader.setOrientation(LinearLayout.HORIZONTAL);
			header = new AdminSuperviseStaffHeaderView(getActivity());
			header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			llHeader.addView(header);
			llHeader.setGravity(Gravity.CENTER | Gravity.TOP);
			rlMainMapView.addView(llHeader);
			isFirstLoad = true;
		}
		mapView.invalidate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.UPDATE_CUSTOMER_LOATION:
			parent.closeProgressDialog();
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.ACTION_UPDATE_POSITION:
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg1 == null) {
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
