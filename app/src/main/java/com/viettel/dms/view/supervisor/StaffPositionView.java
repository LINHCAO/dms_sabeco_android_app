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
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.control.SpinnerItemDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinerRoute;
import com.viettel.dms.view.control.VNMSpinnerTextAdapter;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.TextMarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.sabeco.R;

/**
 * Xem vi tri NVBH
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class StaffPositionView extends FragmentMapView implements OnEventControlListener, OnItemSelectedListener {
	public static final String TAG = StaffPositionView.class.getName();
	// menu giam sat
	private static final int MENU_STAFF_TAB = 0;
	// menu vi tri NVBH
	private static final int MENU_STAFF_POSITION = 1;
	// hien thi man hinh cham cong
	private static final int MENU_STAFF_TIMEKEEPING = 7;
	// hien thi man hinh canh bao ghe tham tren tuyen
	private static final int MENU_STAFF_GOING_ONLINE = 3;
	// xem thong tin 1 NVBH tren ban do
	private static final int ACTION_CLICK_MARKER_STAFF = 4;
	private static final int MENU_LIST_ORDER = 5;

	public GsnppRouteSupervisionDTO staffList;// cusList

	private double lat;
	private double lng;
	// vi tri NVGS
	private double latNVGS;
	private double lngNVGS;
	// ds nhan vien ban hang
	private GsnppRouteSupervisionDTO saleRoadMapDto;
	// spiner ds NVBH
	private VNMSpinnerTextAdapter adapterStaff= null;
	// ds NVBH
	private SpinerRoute spiner;
	// linearLayout spiner
	private LinearLayout llSpiner;
	// icon refresh lai man hinh
	// private ImageButtonOnMap refreshBt;
	// luu chi so nhan vien dang duoc chon
	private int selectedSpinnerIndex = -1;

	private GlobalBaseActivity parent;
	GsnppRouteSupervisionItem currentStaff;
	private String currentSupervisorId;
	private OverlayViewLayer nvbh_gsOverlay;
	private OverlayViewLayer popUpLayer;
	private boolean isRefreshView = false;

	public static StaffPositionView getInstance(Bundle b) {
		StaffPositionView f = new StaffPositionView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) activity;
		super.onAttach(activity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_GSNPP_STAFF_POSITION));
		lat = latNVGS = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		lng = lngNVGS = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		if (lat > 0 && lng > 0) {
			LatLng pos = new LatLng(latNVGS, lngNVGS);
			drawMarkerMyPosition(pos, GlobalInfo.getInstance().getProfile().getMyGPSInfo().getAccuracy());
			mapView.moveTo(pos);
		} else {
			mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
		}
		nvbh_gsOverlay = new OverlayViewLayer();
		mapView.addLayer(nvbh_gsOverlay);
		popUpLayer = new OverlayViewLayer();
		mapView.addLayer(popUpLayer);
		Bundle bundle = getArguments();
		if (bundle != null) {
			currentStaff = (GsnppRouteSupervisionItem) bundle.getSerializable(IntentConstants.INTENT_STAFF_DTO);
			currentSupervisorId = bundle.getString(IntentConstants.INTENT_STAFF_ID);
		} else {
			currentStaff = null;
			currentSupervisorId = null;
		}
		// enable menu bar
		enableMenuBar(this);
		initMenuActionBar();
		layoutStaffPos();
		getStaffPosition();
		isRefreshView = false;
		return v;
	}

	/**
	 * Khoi tao menu
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		int role=GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		if(role==UserDTO.TYPE_GSNPP) {
			addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
					MENU_LIST_ORDER);
		}
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task,
				MENU_STAFF_GOING_ONLINE);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock,
				MENU_STAFF_TIMEKEEPING);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
		if(role==UserDTO.TYPE_GSNPP){
			addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		}
		if(role==UserDTO.TYPE_GSTINH){
			setMenuItemFocus(2);
		}else{
			setMenuItemFocus(4);
		}

	}

	private void layoutStaffPos() {
		spiner = new SpinerRoute(getActivity());
		spiner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		spiner.setMinimumWidth(180);
		llSpiner = new LinearLayout(rlMainMapView.getContext());
		llSpiner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		llSpiner.setOrientation(LinearLayout.HORIZONTAL);
		llSpiner.setGravity(Gravity.RIGHT | Gravity.TOP);

		rlMainMapView.addView(llSpiner);
		mapView.invalidate();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_CLICK_MARKER_STAFF:
			// xem popup khi click tren 1 NVBH
			showStaffInfoPopup((Integer) data);
			break;
		case MENU_STAFF_TAB: {
			if (StringUtil.isNullOrEmpty(currentSupervisorId)) {
				// chuyen sang man hinh giam sat
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
			} else {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
				e.sender = this;
				TBHVController.getInstance().handleSwitchFragment(e);
			}
			break;
		}
		case MENU_LIST_ORDER:
			this.gotoListOrder();
			break;
		case MENU_STAFF_GOING_ONLINE:
			if (StringUtil.isNullOrEmpty(currentSupervisorId)) {
				this.gotoReportVisitCustomerOnPlan();
			} else {
				Bundle b = new Bundle();
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
				e.sender = this;
				e.viewData = b;
				TBHVController.getInstance().handleSwitchFragment(e);
			}
			break;
		case MENU_STAFF_TIMEKEEPING: {
			if (StringUtil.isNullOrEmpty(currentSupervisorId)) {
				ActionEvent e = new ActionEvent();
				Bundle b = new Bundle();
				e.action = ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE;
				e.sender = this;
				e.viewData = b;
				SuperviorController.getInstance().handleSwitchFragment(e);
			} else {
				Bundle b = new Bundle();
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.TBHV_ATTENDANCE;
				e.sender = this;
				e.viewData = b;
				TBHVController.getInstance().handleSwitchFragment(e);
			}
			break;
		}
		case MENU_STAFF_POSITION: {
			if (StringUtil.isNullOrEmpty(currentSupervisorId)) {
			} else {
				Bundle b = new Bundle();
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
				e.sender = this;
				e.viewData = b;
				TBHVController.getInstance().handleSwitchFragment(e);
			}

			break;
		}
		case StaffPositionPopUp.STAFF_POS:
			int index = (Integer) data;
			gotoSaleRouteInfo(saleRoadMapDto.itemList.get(index));
			break;
		case StaffPositionPopUp.POPUP_CLOSE:
			clearMapLayer(popUpLayer);
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 *
	 * go go report visit customer on plan view - update for CR0074
	 *
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoListOrder() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ORDER;
		e.sender = this;
		e.viewData = b;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Hien thi thong tin tren duong di
	 * 
	 * @author: TamPQ
	 * @param index
	 *            : Vi tri cua mot diem tren duong di
	 * @return: void
	 */
	private void showStaffInfoPopup(int index) {
		if (index >= 0 && index < saleRoadMapDto.itemList.size()) {
			GsnppRouteSupervisionItem item = saleRoadMapDto.itemList.get(index);
			if (item.lat > 0 && item.lng > 0) {
				OverlayViewOptions opts = new OverlayViewOptions();
				opts.position(new LatLng(item.lat, item.lng));
				opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
				opts.offsetHeight(-25);
				StaffPositionPopUp popupStaffView = new StaffPositionPopUp(parent);
				popupStaffView.setListener(this);
				popupStaffView.updateInfo(false, true, item.aStaff.name, item.aStaff.mobile, item.updateTime,
						item.visitingCusName, index);
				clearMapLayer(popUpLayer);
				popUpLayer.addItemObj(popupStaffView, opts);

				// set vi tri duoc chon ve trung tam ban do
//				mapView.moveTo(new LatLng(item.lat, item.lng));
				moveToPositionStaff(index);
				
			} else {
				clearMapLayer(popUpLayer);
				parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_CANT_LOCATE_YOUR_POSITION));
			}
			
			// hien thi ten NVH vao spinner
			adapterStaff.setHint(item.aStaff.name);
			adapterStaff.notifyDataSetChanged();
		}
	}
	
	private void moveToPositionStaff(int index){
		GsnppRouteSupervisionItem item = saleRoadMapDto.itemList.get(index);
		if (item.lat > 0 && item.lng > 0) {
			mapView.moveTo(new LatLng(item.lat, item.lng));
		}
	}

	/**
	 * Lay ds vi tri cua cac NVBH
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void getStaffPosition() {
		Bundle bundle = new Bundle();
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		// For TBHV view
		if (!StringUtil.isNullOrEmpty(currentSupervisorId)) {
			staffId = Long.parseLong(currentSupervisorId);
		}
		if (currentStaff != null) {
			bundle.putString(IntentConstants.INTENT_SHOP_ID, "" + currentStaff.aStaff.shopId);
		} else {
			bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		}
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		bundle.putString(IntentConstants.INTENT_DAY, DateUtils.getToday());
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_LIST_POSITION_SALE_PERSONS;
		e.sender = this;
		e.viewData = bundle;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_POSITION_SALE_PERSONS:			
			saleRoadMapDto = (GsnppRouteSupervisionDTO) modelEvent.getModelData();
			if (saleRoadMapDto != null) {
				if (saleRoadMapDto.itemList.size() > 0) {
					// khoi tao ds nhan vien
					Vector<SpinnerItemDTO> listStafffItem = new Vector<SpinnerItemDTO>();
					GsnppRouteSupervisionItem item;
					SpinnerItemDTO spinnerItem;
					for (int i = 0, size = saleRoadMapDto.itemList.size(); i < size; i++) {
						item = saleRoadMapDto.itemList.get(i);
						spinnerItem = new SpinnerItemDTO();
						spinnerItem.name = item.aStaff.name;
						if (item.lat <= 0 || item.lng <= 0) {
							spinnerItem.content = StringUtil.getString(R.string.TEXT_CANT_LOCATE_YOUR_POSITION);
						}
						listStafffItem.add(spinnerItem);
					}
//					if(adapterStaff == null){
						adapterStaff = new VNMSpinnerTextAdapter(getActivity(), R.layout.simple_spinner_item,
								listStafffItem);
						spiner.setOnItemSelectedListener(this);
						spiner.setAdapter(adapterStaff);
						adapterStaff.setHint(StringUtil.getString(R.string.TEXT_LIST_STAFF));
						this.selectedSpinnerIndex = -1;
//					}
					// spiner.setSelection(0);
					adapterStaff.notifyDataSetChanged();

					llSpiner.removeAllViews();
					llSpiner.addView(spiner);
					llSpiner.setVisibility(View.VISIBLE);
				} else {
					llSpiner.setVisibility(View.GONE);
				}

				// ve len ban do
				drawStaffPosition();

				if (currentStaff != null) {
					for (int i = 0, size = saleRoadMapDto.itemList.size(); i < size; i++) {
						GsnppRouteSupervisionItem item1 = saleRoadMapDto.itemList.get(i);
						if (item1.aStaff.staffId == currentStaff.aStaff.staffId) {
							showStaffInfoPopup(i);
							break;
						}
					}

				}
			}
			requestInsertLogKPI(HashMapKPI.GSNPP_XEMVITRINHANVIENTRENBANDO, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_LIST_POSITION_SALE_PERSONS:
			break;
		default:
			// closeProgressDialog();
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}

	}

	/**
	 * Ve marker thong tin staff
	 * 
	 * @author : TamPQ since : 1.0
	 */
	private void drawStaffPosition() {
		if (saleRoadMapDto != null) {
			clearCustomerMarkerOnMap();
			ArrayList<LatLng> arrayPosition = new ArrayList<LatLng>();
			for (int i = 0, size = saleRoadMapDto.itemList.size(); i < size; i++) {
				GsnppRouteSupervisionItem staff = saleRoadMapDto.itemList.get(i);
				if (staff.lat > 0 && staff.lng > 0) {
					//duongdt khong di chuyen den vi tri cua NV nua
					/*if (lat <= 0 || lng <= 0) {
						lat = staff.lat;
						lng = staff.lng;
						if(!isRefreshView){
							mapView.moveTo(new LatLng(lat, lng));
						}
					}*/
					showMarkerInMap(staff.lat, staff.lng, i, staff.aStaff.name, R.drawable.staff_position);
					arrayPosition.add(new LatLng(staff.lat, staff.lng));
				}
			}
			if(!isRefreshView){
				if (latNVGS > 0 && lngNVGS > 0) {
					arrayPosition.add(new LatLng(latNVGS, lngNVGS));
				}
			}else if (selectedSpinnerIndex != -1){
				moveToPositionStaff(selectedSpinnerIndex);
			}
			if(!isRefreshView){
				fitBounds(arrayPosition);
			}
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void clearCustomerMarkerOnMap() {
		clearMapLayer(nvbh_gsOverlay);
		clearMapLayer(popUpLayer);
	}

	/**
	 * Hien thi marker toa do hien tai
	 * 
	 * @author : TamPQ since : 1.0
	 */
	private void showMarkerInMap(double lat, double lng, int index, String name, int drawable) {
		if (getActivity() != null) {
			// ImageTextMarkerMapView marker = new
			// ImageTextMarkerMapView(getActivity(), drawable, name,
			// R.color.RED);
			// marker.setListener(this, ACTION_CLICK_MARKER_STAFF, index);
			// OverlayViewOptions opts = new OverlayViewOptions();
			// opts.position(new LatLng(lat, lng));
			// opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			// nvbh_gsOverlay.addItemObj(marker, opts);

			TextMarkerMapView marker = new TextMarkerMapView(parent, drawable, "");
			marker.setListener(this, ACTION_CLICK_MARKER_STAFF, index);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			nvbh_gsOverlay.addItemObj(marker, opts);

			Rect bounds = new Rect();
			Paint textPaint = new TextView(parent).getPaint();
			textPaint.getTextBounds(name, 0, name.length(), bounds);
			final int width = bounds.width();
			TextView text = new TextView(parent) {
				@Override
				protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
					super.onMeasure(width, heightMeasureSpec);
				}
			};
			text.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(name);
			text.setTextColor(ImageUtil.getColor(R.color.RED));
			opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
		
			nvbh_gsOverlay.addItemObj(text, opts);
		}
	}

	private void gotoSaleRouteInfo(GsnppRouteSupervisionItem item) {
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-02");
		b.putSerializable(IntentConstants.INTENT_STAFF_DTO, item);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg1 == null) {
			if (-1 != selectedSpinnerIndex && saleRoadMapDto.itemList != null && arg2 >= 0
					&& arg2 < saleRoadMapDto.itemList.size()) {
				selectedSpinnerIndex = arg2;
				showStaffInfoPopup(selectedSpinnerIndex);
			} else {
				selectedSpinnerIndex = arg2;
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			// tranh truong hop notify nhieu lan
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			if (lat < 0 && lng < 0) {
				return;
			} else {
				latNVGS = lat;
				lngNVGS = lng;
				drawMarkerMyPosition();
			}
			break;
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isRefreshView = true;
				drawMarkerMyPosition();
				getStaffPosition();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * 
	 * go go report visit customer on plan view - update for CR0074
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoReportVisitCustomerOnPlan() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_REPORT_VISIT_CUSTOMER_ON_PLAN;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}
	
}
