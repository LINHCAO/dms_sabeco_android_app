/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.Vector;

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
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VNMSpinnerTextAdapter;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.MarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.sabeco.R;

/**
 * man hinh reset vi tri cua khach hang trong module gs npp
 * 
 * @author : BangHN since : 1.0 version : 1.0
 */
public class CustomerSaleLocationResetView extends FragmentMapView implements OnItemSelectedListener {
	public static final String TAG = CustomerSaleLocationResetView.class.getName();
	// ds lich su cap nhat (dung cho spinner)
	Vector<SpinnerItemDTO> listHistoryText = new Vector<SpinnerItemDTO>();
	ArrayList<CustomerUpdateLocationDTO> listHistoryUpdateLocation;
	VNMSpinnerTextAdapter adapterLine;
	public static final int ACTION_CLEAR_OK = 11;
	public static final int ACTION_CLEAR_CANCEL = 12;
	Bundle savedInstanceState;
	CustomerDTO customerInfo;// thong tin user
	// view header
	CustomerSaleLocationResetHeaderView header;
	// vi tri hien tai cua khach hang
	double cusLat, cusLng;
	// vi tri nhan vien
	double staffLat, staffLng; 
	// lan dau khoi tao view ?
	boolean isFirstInitView = false;
	OverlayViewLayer overlayMarkerCustomer;

	public static CustomerSaleLocationResetView newInstance(Bundle data) {
		CustomerSaleLocationResetView f = new CustomerSaleLocationResetView();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		customerInfo = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
		if (customerInfo != null) {
			cusLat = customerInfo.getLat();
			cusLng = customerInfo.getLng();
		}
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) activity;
		super.onAttach(activity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		View v = super.onCreateView(inflater, container, savedInstanceState);
		header = new CustomerSaleLocationResetHeaderView(getActivity());
		// enable menu bar
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_GSNPP_CUSTOMER_SALE_UPDATE_LOCATION));
		double myLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		double myLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		if (cusLat > 0 && cusLng > 0) {
			mapView.moveTo(new LatLng(cusLat, cusLng));
		} else if (myLat > 0 && myLng > 0) {
			mapView.moveTo(new LatLng(myLat, myLng));
		} else {
			mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
		}

		// lat, lng cua nhan vien giam sat
		staffLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		staffLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		parent.restartLocating(true);
		overlayMarkerCustomer = new OverlayViewLayer();
		mapView.addLayer(overlayMarkerCustomer);

		// init du lieu dau
		setCustomerLocationHeader();
		getHistoryUpdateLocation();
		showMarkerInMap(cusLat, cusLng);
		// vi tri hien tai
		drawMarkerMyPosition();
		return v;
	}

	/**
	 * Lay danh sach lich su cap nhat vi tri cua khach hang
	 * 
	 * @author banghn
	 */
	private void getHistoryUpdateLocation() {
		isFirstInitView = true;
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(customerInfo.customerId));
		e.viewData = b;
		e.action = ActionEventConstant.GET_HISTORY_UPDATED_LOCATION;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * Hien thi header thong tin khach hang tren ban do
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void setCustomerLocationHeader() {
		if (!isFirstInitView) {
			LinearLayout llHeader;
			llHeader = new LinearLayout(rlMainMapView.getContext());

			llHeader.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			llHeader.setOrientation(LinearLayout.HORIZONTAL);

			header = new CustomerSaleLocationResetHeaderView(getActivity());
			header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			header.setCustomerInfo(customerInfo);
			llHeader.addView(header);
			llHeader.setGravity(Gravity.CENTER | Gravity.TOP);
			header.btReset.setOnClickListener(this);

			rlMainMapView.addView(llHeader);
			isFirstInitView = true;
		}
		mapView.invalidate();
	}

	/**
	 * Hien thi marker toa do hien tai
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void showMarkerInMap(double lat, double lng) {
		if (lat > 0 && lng > 0) {
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);

			MarkerMapView makerPositionCustomer = new MarkerMapView(getActivity(), R.drawable.icon_map_position_blue);
			overlayMarkerCustomer.addItemObj(makerPositionCustomer, opts);
		}
	}

	/**
	 * Marker voi thong tin lich su truy cap
	 * 
	 * @author banghn
	 * @param icon
	 */
	private void showMarkerWithTextIfon(double lat, double lng, int icon, String dateInfo) {
		if (lat > 0 && lng > 0 && parent != null) {
			// MarkerWithTextBelow maker;
			// maker = new MarkerWithTextBelow(parent, icon);
			// maker.setTextSize(15, Typeface.NORMAL);
			// maker.setMaxWith(120);
			// maker.setTextInfo(dateInfo);
			//
			// OverlayViewLayer overlayMaker = new OverlayViewLayer();
			// mapView.addLayer(overlayMaker);
			// OverlayViewOptions opts = new OverlayViewOptions();
			// opts.position(new LatLng(lat, lng));
			// opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			// overlayMaker.addItemObj(maker, opts);

			MarkerMapView marker = new MarkerMapView(parent, icon);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			overlayMarkerCustomer.addItemObj(marker, opts);

			Rect bounds = new Rect();
			Paint textPaint = new TextView(parent).getPaint();
			textPaint.getTextBounds(dateInfo, 0, dateInfo.length(), bounds);
			final int width = bounds.width();
			TextView text = new TextView(parent) {
				@Override
				protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
					super.onMeasure(width, heightMeasureSpec);
				}
			};
			text.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(dateInfo);
			text.setTextColor(ImageUtil.getColor(R.color.RED));
			opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
			overlayMarkerCustomer.addItemObj(text, opts);
		}
	}

	/**
	 * clear vi tri cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void resetCustomerLocation() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.UPDATE_CUSTOMER_LOATION;
		customerInfo.setLng(-1);
		customerInfo.setLat(-1);
		cusLat = customerInfo.getLat();
		cusLng = customerInfo.getLng();
		customerInfo.setUpdateUser(GlobalInfo.getInstance().getProfile().getUserData().userCode);
		customerInfo.setUpdateDate(DateUtils.now());

		// du lieu insert bang position log
		ArrayList<CustomerPositionLogDTO> log = new ArrayList<CustomerPositionLogDTO>();
		CustomerPositionLogDTO logPosition = new CustomerPositionLogDTO();
		logPosition.createDate = DateUtils.now();
		logPosition.customerId = customerInfo.customerId;
		logPosition.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		logPosition.lat = -1;
		logPosition.lng = -1;
		log.add(logPosition);
		if(listHistoryUpdateLocation.size() > 0){
			CustomerPositionLogDTO updatePosition = new CustomerPositionLogDTO();
			updatePosition = listHistoryUpdateLocation.get(0).positionLog;
			updatePosition.deleteDate = DateUtils.now();
			updatePosition.deleteUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
			log.add(updatePosition);

		}

		e.viewData = customerInfo;
		e.userData = log;
		e.sender = this;
		e.isNeedCheckTimeServer = true;
		SaleController.getInstance().handleViewEvent(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.UPDATE_CUSTOMER_LOATION:
			parent.showDialog(StringUtil.getString(R.string.TEXT_DELETE_LOCATION_SUCCESS));
			header.setButtonResetVisible(View.INVISIBLE);
			// TransactionProcessManager.getInstance().resetChecking();
			getHistoryUpdateLocation();
			break;
		case ActionEventConstant.GET_HISTORY_UPDATED_LOCATION:
			listHistoryUpdateLocation = (ArrayList<CustomerUpdateLocationDTO>) modelEvent.getModelData();
			// list toa do cap nhat du lieu
			ArrayList<LatLng> listUpdatePosition = new ArrayList<LatLng>();
			int size;
			if (listHistoryUpdateLocation != null && listHistoryUpdateLocation.size() > 0) {
				clearMapLayer(overlayMarkerCustomer);
				size = listHistoryUpdateLocation.size();
				listHistoryText = new Vector<SpinnerItemDTO>();
				SpinnerItemDTO spinnerItem;
				for (int i = 0; i < size; i++) {
					spinnerItem = new SpinnerItemDTO();
					spinnerItem.name = listHistoryUpdateLocation.get(i).staffSale.name;
					spinnerItem.content = listHistoryUpdateLocation.get(i).positionLog.createDate;
					listHistoryText.add(spinnerItem);
					// khong ve mot record cuoi cung, (record cuoi la vi tri
					// hien tai)
					if (i > 0) {
						showMarkerWithTextIfon(listHistoryUpdateLocation.get(i).positionLog.getLat(),
								listHistoryUpdateLocation.get(i).positionLog.getLng(),
								R.drawable.icon_map_position_pink,
								listHistoryUpdateLocation.get(i).positionLog.createDate);
					} else {
						// da xoa diem hien tai
						if (cusLat <= 0 || cusLng <= 0) {
							showMarkerWithTextIfon(listHistoryUpdateLocation.get(i).positionLog.getLat(),
									listHistoryUpdateLocation.get(i).positionLog.getLng(),
									R.drawable.icon_map_position_pink,
									listHistoryUpdateLocation.get(i).positionLog.createDate);
						} else {
							showMarkerInMap(listHistoryUpdateLocation.get(i).positionLog.getLat(),
									listHistoryUpdateLocation.get(i).positionLog.getLng());
						}
					}

					LatLng cusLo = new LatLng(listHistoryUpdateLocation.get(i).positionLog.getLat(),
							listHistoryUpdateLocation.get(i).positionLog.getLng());
					listUpdatePosition.add(cusLo);
				}
				// marker nhan vien gs va ban hang hien tai
				drawMarkerMyPosition();

				// khoi tao spinner
				adapterLine = new VNMSpinnerTextAdapter(getActivity(), R.layout.simple_spinner_item, listHistoryText);
				adapterLine.setHint(StringUtil.getString(R.string.HISTOY_UPDATED_LOCATION));
				header.spHistoryUpdate.setOnItemSelectedListener(this);
				header.spHistoryUpdate.setAdapter(adapterLine);
				// this.selectedSpinnerIndex = -1;
				header.spHistoryUpdate.setSelection(0);
				adapterLine.notifyDataSetChanged();
				header.spHistoryUpdate.setVisibility(View.VISIBLE);

			} else {
				header.spHistoryUpdate.setVisibility(View.GONE);
			}
			break;
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
	public void onClick(View v) {
		if (v == header.btReset) {
			GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent,
					StringUtil.getString(R.string.TEXT_CONFIRM_DELETE_LOCATION),
					StringUtil.getString(R.string.TEXT_AGREE), ACTION_CLEAR_OK,
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					ACTION_CLEAR_CANCEL, null, false, false);
		}
		super.onClick(v);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case ACTION_CLEAR_OK:
			if (GlobalUtil.checkNetworkAccess()) {
				resetCustomerLocation();
			} else {
				parent.showDialog(StringUtil.getString(R.string.TEXT_NETWORK_DISABLE));
			}
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			if (lat < 0 && lng < 0) {
				// parent.showDialog("Không xác định được vị trí!");
				return;
			} else {
				drawMarkerMyPosition();
			}
			break;
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isFirstInitView = false;
				parent.showLoadingDialog();
				customerInfo = SQLUtils.getInstance().getCustomerById(customerInfo.getCustomerId());
				cusLat = customerInfo.getLat();
				cusLng = customerInfo.getLng();
				setCustomerLocationHeader();
				getHistoryUpdateLocation();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg1 == null) {
			if (arg2 >= 0 && listHistoryUpdateLocation != null && listHistoryUpdateLocation.size() > 0) {
				LatLng pointerCenter = new LatLng(listHistoryUpdateLocation.get(arg2).positionLog.lat,
						listHistoryUpdateLocation.get(arg2).positionLog.lng);
				mapView.moveTo(pointerCenter);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
