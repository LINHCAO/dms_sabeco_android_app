/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.GsnppRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.StaffPositionPopUp;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.TextMarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.maps.base.LatLngBounds;
import com.viettel.sabeco.R;

/**
 * MH theo doi vi tri GSBH, NVBH, TTTT role GST
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVGsnppNvbhPositionView extends FragmentMapView implements
		OnEventControlListener, OnItemSelectedListener, OnCheckedChangeListener {
	public static final String TAG = TBHVGsnppNvbhPositionView.class.getName();
	// menu giam sat
	private static final int MENU_STAFF_TAB = 0;
	// menu vi tri NVBH
	private static final int MENU_STAFF_POSITION = 1;
	// show tab cham cong ngay
	private static final int MENU_ATTENDANCE = 7;
	// show tab di tuyen ngay
	private static final int MENU_PLAN_PROCESS = 3;
	private static final int MENU_LIST_ORDER = 9;

	// cac action khi nhan vao popup thong tin gsnpp, tttt, nvbh
	private static final int ACTION_CLICK_MARKER_GSNPP = 4;
	private static final int ACTION_CLICK_MARKER_NVBH = 5;
	private static final int ACTION_CLICK_MARKER_GSNPP_NO_ROUTING = 6;
	private static final int ACTION_CLICK_MARKER_TTTT = 8;

	TBHVRouteSupervisionDTO gsnppList;// ds gsnpp
	TBHVRouteSupervisionDTO gsnppTtttList;// ds tttt
	GsnppRouteSupervisionDTO nvbhList;// ds nvbh
	TBHVGsnppNvbhPositionHeader header;// header

	// vi tri cua toi (TBHV)
	private double latImHere;
	private double lngImHere;
	// vi tri mac dinh
	private double latDefault;
	private double lngDefault;
	// luu chi so nhan vien dang duoc chon
	private boolean isViewingGSNPP = true;
	// private boolean isViewingNVBH = false;
	private boolean isViewingTTTT = true;
	private boolean isGsnpp = false;
	private boolean isTttt = false;
	private boolean isNvbh = false;

	private GlobalBaseActivity parent;
	private OverlayViewLayer markerLayerGSNPP;// marker gsnpp
	private OverlayViewLayer markerLayerNVBH;// marker nvbh
	private OverlayViewLayer markerLayerTTTT;// marker tttt
	private OverlayViewLayer popupLayer;// popup

	class LatLngEx extends LatLng{
		static final int TYPE_NONE = 0;
		static final int TYPE_NVBH = 1;
		static final int TYPE_GSNPP = 2;
		static final int TYPE_TNPG = 3;
		static final int TYPE_GSBH = 4;
		int type = TYPE_NONE;
		
		public LatLngEx(double latitude, double longitude, int type) {
			super(latitude, longitude);
			this.type = type;
		}
		
		@SuppressLint("DefaultLocale")
		@Override
		public String toString() {
			String result = String.format("LatLngEx["+ this.getLatitude() + ", "+this.getLongitude()+", %d %d %s]", this.getLatitudeE6(), this.getLongitudeE6(), getType());
			return result;
		}
		
		@Override
		public LatLng clone() {
			return super.clone();
		}
		private String getType(){
			String resultType = "";
			switch (type) {
			case TYPE_NVBH:
				resultType = "NVBH";
				break;
			case TYPE_GSBH:
				resultType = "GSBH";
				break;
			case TYPE_GSNPP:
				resultType = "GSNPP";
				break;
			case TYPE_TNPG:
				resultType = "TNPG";
				break;
			default:
				resultType = "TYPE_NONE";
				break;
			}
			return resultType;
		}
		
	}
	ArrayList<LatLng> arrayPosition = new ArrayList<LatLng>();
	private boolean isResume = false;

	public static TBHVGsnppNvbhPositionView getInstance(Bundle b) {
		TBHVGsnppNvbhPositionView f = new TBHVGsnppNvbhPositionView();
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		enableMenuBar(this);
		initMenuActionBar();
		setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_VIEW_TBHV_SUPERVISE_GSNPP_VIEW));

		latDefault = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
				.getLatitude();
		latImHere = latDefault;
		lngDefault = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
				.getLongtitude();
		lngImHere = lngDefault;
		if (latDefault > 0 && lngDefault > 0) {
			arrayPosition.add(new LatLngEx(latImHere, lngImHere, LatLngEx.TYPE_GSBH));
			drawMarkerMyPosition();
			moveToCenterMyPosition();
		} else {
			mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER,
					Constants.LNG_DMS_TOWNER));
		}

		markerLayerGSNPP = new OverlayViewLayer();
		mapView.addLayer(markerLayerGSNPP);

		popupLayer = new OverlayViewLayer();
		mapView.addLayer(popupLayer);

		markerLayerTTTT = new OverlayViewLayer();
		mapView.addLayer(markerLayerTTTT);

		markerLayerNVBH = new OverlayViewLayer();
		mapView.addLayer(markerLayerNVBH);

		if (gsnppTtttList != null) {
			isResume  = true;
			layoutStaffPos();
		} else {
			getGsnppPosition();
		}

		return v;
	}

	/**
	 * Khoi tao menu
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
				MENU_LIST_ORDER);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE),
				R.drawable.icon_task, MENU_PLAN_PROCESS);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING),
				R.drawable.icon_clock, MENU_ATTENDANCE);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION),
				R.drawable.icon_map, MENU_STAFF_POSITION);
/*		addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING),
				R.drawable.menu_customer_icon, MENU_STAFF_TAB);*/

		setMenuItemFocus(4);
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

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_LIST_ORDER:
			this.gotoListOrder();
			break;
		case ACTION_CLICK_MARKER_GSNPP:
			String staffCode = (String) data;
			int index = -1;
			for (int i = 0; i < gsnppTtttList.itemGSNPPList.size(); i++) {
				if (staffCode != null && staffCode.equals(gsnppTtttList.itemGSNPPList.get(i).staffCodeGS)) {
					index = i;
					break;
				}
			}
			showGsnppInfoPopup(index, true);
			break;
		case ACTION_CLICK_MARKER_GSNPP_NO_ROUTING:
			// xem popup khi click tren 1 GSNPP (khi co them marker NVBH)
			staffCode = (String) data;
			index = -1;
			for (int i = 0; i < gsnppTtttList.itemGSNPPList.size(); i++) {
				if (staffCode != null && staffCode.equals(gsnppTtttList.itemGSNPPList.get(i).staffCodeGS)) {
					index = i;
					break;
				}
			}
			showGsnppInfoPopup(index, false);
			break;
		case ACTION_CLICK_MARKER_NVBH:
			// xem popup khi click tren 1 NVBH
			// xem popup khi click tren 1 GSNPP (khi co them marker NVBH)
			staffCode = (String) data;
			index = -1;
			for (int i = 0; i < gsnppList.itemListNVBH.size(); i++) {
				if (staffCode != null
						&& staffCode
								.equals(gsnppList.itemListNVBH.get(i).staffCodeGS)) {
					index = i;
					break;
				}
			}
			showNVBHInfoPopup(index);
			break;
		case ACTION_CLICK_MARKER_TTTT:
			String staffCodeTttt = (String) data;
			int indexTttt = -1;
			for (int i = 0; i < gsnppTtttList.itemListTTTT.size(); i++) {
				if (staffCodeTttt != null
						&& staffCodeTttt.equals(gsnppTtttList.itemListTTTT
								.get(i).staffCodeGS)) {
					indexTttt = i;
					break;
				}
			}
			showTTTTInfoPopup(indexTttt, true);
			break;
		case MENU_STAFF_TAB:
			// chuyen sang man hinh giam sat
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case MENU_ATTENDANCE:
			this.showTBHVAttendance();
			break;
		case MENU_PLAN_PROCESS:
			Bundle b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case StaffPositionPopUp.STAFF_POS:
			index = (Integer) data;
			if (isGsnpp) {
				isGsnpp = false;
				/* clearMapLayer(popupLayerGSNPP); */
				clearMapLayer(popupLayer);
				clearGSNPPMarkerOnMap();
				clearNVBHMarkerOnMap();
				header.spinnerStaffCode.getSpiner().setSelection(index + 1);
				getNVBHPosition(gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1));
				if(gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat >0 && gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lng>0){
					double lat = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat;
					double lng = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lng;
					String name = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffNameGS;
					String gsnppCode = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffCodeGS;
					int drawable = R.drawable.icon_map_position_pink;
					showMarkerGSNPPInMap(lat, lng, 0, name, gsnppCode,
							drawable, ACTION_CLICK_MARKER_GSNPP_NO_ROUTING,
							false);
				}
				fitBounds(arrayPosition);
			} else if (isTttt) {
				gotoSaleRouteInfo(gsnppTtttList.itemListTTTT.get(index),
						Constants.TYPE_TT);
				isTttt = false;
				fitBounds(arrayPosition);
			} else if (isNvbh) {
				gotoSaleRouteInfo(gsnppList.itemListNVBH.get(index),
						Constants.TYPE_NVBH);
				isNvbh = false;
				fitBounds(arrayPosition);
			}

			break;
		case StaffPositionPopUp.POPUP_CLOSE:
			clearMapLayer(popupLayer);
			isTttt = false;
			isGsnpp = false;
			isNvbh = false;
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * 
	 * chuyen toi MH xem lo trinh NVBH, TTTT
	 * 
	 * @author: YenNTH
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void gotoSaleRouteInfo(THBVRouteSupervisionItem item, String from) {
		Bundle b = new Bundle();
		b.putSerializable(IntentConstants.INTENT_STAFF_DTO, item);
		b.putString(IntentConstants.INTENT_FROM, from);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION_MAP;
		e.sender = this;
		e.viewData = b;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * hien thi man hinh cham cong
	 * 
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 * 
	 */
	public void showTBHVAttendance() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_ATTENDANCE;
		e.sender = this;
		e.viewData = b;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * hien thi popup thong tin gsnpp
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return: voidvoid
	 * @throws:
	 */
	private void showGsnppInfoPopup(Integer index, boolean isRouting) {
		if (index >= 0 && index < gsnppTtttList.itemGSNPPList.size()) {
			TBHVRouteSupervisionDTO.THBVRouteSupervisionItem item = gsnppTtttList.itemGSNPPList.get(index);
			if (item.lat > 0 && item.lng > 0) {
				StaffPositionPopUp popupView = new StaffPositionPopUp(parent);
				popupView.setListener(this);
				if (isRouting) {
					popupView.updateInfoTBHV(5, true, item.staffNameGS,
							item.gsnppMobile, item.updateTime, null, index);
					popupView.tvInfo.setText(StringUtil
							.getString(R.string.TEXT_VIEW_POSITION_NVBH));
					popupView.tvInfo.setVisibility(View.VISIBLE);

				} else {
					popupView.updateInfo(true, false, item.staffNameGS,
							item.gsnppMobile, item.updateTime, null, index);
					popupView.tvInfo.setVisibility(View.GONE);
				}
				isGsnpp = true;
				isTttt = false;
				isNvbh = false;
				OverlayViewOptions opts = new OverlayViewOptions();
				opts.position(new LatLng(item.lat, item.lng));
				opts.offsetHeight(-25);

				clearMapLayer(popupLayer);
				popupLayer.addItemObj(popupView, opts);

				// set vi tri duoc chon ve trung tam ban do
				mapView.moveTo(new LatLng(item.lat, item.lng));
			}
		}

	}

	/**
	 * Hien thi popup thong tin NVBH
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return: voidvoid
	 * @throws:
	 */
	private void showNVBHInfoPopup(Integer index) {
		if (index >= 0 && index < gsnppList.itemListNVBH.size()) {
			THBVRouteSupervisionItem item = gsnppList.itemListNVBH.get(index);
			if (item.lat > 0 && item.lng > 0) {
				StaffPositionPopUp popupView = new StaffPositionPopUp(parent);
				popupView.updateInfoTBHV(1, true, item.staffNameGS,
						item.gsnppMobile, item.updateTime,
						item.visitingCusName, index);
				popupView.tvInfo.setText(StringUtil
						.getString(R.string.TEXT_VIEW_ROUTE_NVBH));
				popupView.setListener(this);
				isNvbh = true;
				isGsnpp = false;
				isTttt = false;
				OverlayViewOptions opts = new OverlayViewOptions();
				opts.position(new LatLng(item.lat, item.lng));
				opts.offsetHeight(-25);
				clearMapLayer(popupLayer);
				popupLayer.addItemObj(popupView, opts);

				// set vi tri duoc chon ve trung tam ban do
				mapView.moveTo(new LatLng(item.lat, item.lng));
			}
		}

	}

	/**
	 * 
	 * Hien thi popup thong tin to truong tiep thi
	 * 
	 * @author: YenNTH
	 * @param index
	 * @param isRouting
	 * @return: void
	 * @throws:
	 */
	private void showTTTTInfoPopup(Integer index, boolean isRouting) {
		if (index >= 0 && index < gsnppTtttList.itemListTTTT.size()) {
			TBHVRouteSupervisionDTO.THBVRouteSupervisionItem item = gsnppTtttList.itemListTTTT
					.get(index);
			if (item.lat > 0 && item.lng > 0) {
				StaffPositionPopUp popupView = new StaffPositionPopUp(parent);
				popupView.setListener(this);
				if (isRouting) {
					popupView.updateInfoTBHV(11, true, item.staffNameGS,
							item.gsnppMobile, item.updateTime,
							item.visitingCusName, index);
					popupView.tvInfo.setText(StringUtil
							.getString(R.string.TEXT_VIEW_ROUTE_TTTT));
				} else {
					popupView.updateInfo(true, false, item.staffNameGS,
							item.gsnppMobile, item.updateTime,
							item.visitingCusName, index);
				}
				isTttt = true;
				isNvbh = false;
				isGsnpp = false;
				OverlayViewOptions opts = new OverlayViewOptions();
				opts.position(new LatLng(item.lat, item.lng));
				opts.offsetHeight(-25);
				clearMapLayer(popupLayer);
				popupLayer.addItemObj(popupView, opts);

				// set vi tri duoc chon ve trung tam ban do
				mapView.moveTo(new LatLng(item.lat, item.lng));
			}
		}

	}

	/**
	 * Lay ds vi tri cua cac GSNPP
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getGsnppPosition() {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo
				.getInstance().getProfile().getUserData().shopId);
		bundle.putString(IntentConstants.INTENT_DAY, DateUtils.getToday());

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
		e.sender = this;
		e.viewData = bundle;

		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * Add header
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void layoutStaffPos() {

		LinearLayout llHeader;
		llHeader = new LinearLayout(parent);

		llHeader.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		llHeader.setOrientation(LinearLayout.HORIZONTAL);

		header = new TBHVGsnppNvbhPositionHeader(getActivity());
		header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT));
		header.updateView(gsnppTtttList);
		header.cbGsnpp.setOnCheckedChangeListener(this);
		header.cbTttt.setOnCheckedChangeListener(this);
		header.spinnerStaffCode.setOnItemSelectedListener(this);

		header.cbGsnpp.setChecked(isViewingGSNPP);
		header.spinnerStaffCode.getSpiner().setEnabled(isViewingGSNPP);
		header.cbTttt.setChecked(isViewingTTTT);

		llHeader.addView(header);
		llHeader.setGravity(Gravity.CENTER | Gravity.TOP);

		rlMainMapView.addView(llHeader);
		mapView.invalidate();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION:
			removeInPositionArray(new int []{LatLngEx.TYPE_GSNPP,LatLngEx.TYPE_NVBH, LatLngEx.TYPE_TNPG });
			gsnppList = (TBHVRouteSupervisionDTO) modelEvent.getModelData();
			gsnppTtttList = gsnppList;
			layoutStaffPos();
			// ve len ban do
			if (gsnppTtttList.itemGSNPPList != null) {
				drawGsnppPosition();
				header.cbGsnpp.setChecked(true);
				requestInsertLogKPI(HashMapKPI.GSBH_XEMVITRIGSNPPTRENBANDO, modelEvent.getActionEvent().startTimeFromBoot);
			}
			if (gsnppTtttList.itemListTTTT != null && isViewingTTTT) {
				drawTTTTPosition();
				header.cbTttt.setChecked(true);
				requestInsertLogKPI(HashMapKPI.GSBH_XEMVITRITTTTTRENBANDO, modelEvent.getActionEvent().startTimeFromBoot);
			}
			
			break;
		case ActionEventConstant.TBHV_NVBH_POSITION:
			removeInPositionArray(new int []{LatLngEx.TYPE_GSNPP,LatLngEx.TYPE_NVBH, LatLngEx.TYPE_TNPG });
			if (gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat > 0
					&& gsnppTtttList.itemGSNPPList
							.get(header.selectedSpinnerIndex - 1).lng > 0) {
				arrayPosition.add(new LatLngEx(gsnppTtttList.itemGSNPPList
						.get(header.selectedSpinnerIndex - 1).lat,
						gsnppTtttList.itemGSNPPList
								.get(header.selectedSpinnerIndex - 1).lng, LatLngEx.TYPE_GSNPP));
			}
			gsnppList = (TBHVRouteSupervisionDTO) modelEvent.getModelData();
			if (gsnppList.itemListNVBH != null) {
				drawNvbhPosition();
			}
			requestInsertLogKPI(HashMapKPI.GSBH_XEMVITRINVBHTRENBANDO, modelEvent.getActionEvent().startTimeFromBoot);
			break;

		default:
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg1 == null) {
			if (header.selectedSpinnerIndex != arg2) {
				header.selectedSpinnerIndex = arg2;
				clearGSNPPMarkerOnMap();
				clearNVBHMarkerOnMap();
				if (arg2 == 0) {
					getGsnppPosition();
					fitBounds(arrayPosition);
				} else {
					// tranh TH chon 1 GS, huy check TTTT
					if (isResume) {
						isResume = false;
					} else {
						clearTTTTMarkerOnMap();
						header.cbTttt.setChecked(false);
					}
					header.adapterGsnpp.setHint(gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffCodeGS + " - "
							+ gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffNameGS);
					header.adapterGsnpp.notifyDataSetChanged();
					if (header.cbGsnpp.isChecked()) {
						if(gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat >0 && gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lng>0){
							double lat = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat;
							double lng = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lng;
							String name = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffNameGS;
							String gsnppCode = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffCodeGS;
							int drawable = R.drawable.icon_map_position_pink;
							showMarkerGSNPPInMap(lat, lng, 0, name, gsnppCode, drawable, ACTION_CLICK_MARKER_GSNPP_NO_ROUTING, false);
							arrayPosition.add(new LatLngEx(lat, lng, LatLngEx.TYPE_GSNPP));
						}
						getNVBHPosition(gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1));
						fitBounds(arrayPosition);
					}
				}
			}
		}
	}
	
	/**
	 * tao 1 vung Zoom mac dinh cho mang Position
	 * 
	 * @author: duongdt
	 * @return: void
	 * @throws:
	 */
	@Override
	public void fitBounds(ArrayList<LatLng> arrayPosition) {
		LatLngBounds bound = null;
		for (int i = 0, size = arrayPosition.size(); i <size; i++) {
			com.viettel.maps.base.LatLng latlng = arrayPosition.get(i).clone();
			bound = getBound(bound, latlng);
		}
		mapView.fitBounds(bound, true);
	}

	@Override
	public void onClickMarkerMyPositionEvent() {
		super.onClickMarkerMyPositionEvent();
	}

	/**
	 * Ve marker thong tin staff cho gsbh
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void drawGsnppPosition() {
		if (gsnppTtttList != null) {
			clearGSNPPMarkerOnMap();
			for (int i = 0, size = gsnppTtttList.itemGSNPPList.size(); i < size; i++) {
				TBHVRouteSupervisionDTO.THBVRouteSupervisionItem staff = gsnppTtttList.itemGSNPPList.get(i);
				if (staff.lat > 0 && staff.lng > 0) {
					if (latDefault <= 0 || lngDefault <= 0) {
						latDefault = staff.lat;
						lngDefault = staff.lng;
						mapView.moveTo(new LatLng(latDefault, lngDefault));
					}
					showMarkerGSNPPInMap(staff.lat, staff.lng, i,
							staff.staffNameGS, staff.staffCodeGS,
							R.drawable.icon_map_position_pink,
							ACTION_CLICK_MARKER_GSNPP);
					arrayPosition.add(new LatLngEx(staff.lat, staff.lng, LatLngEx.TYPE_GSNPP));
				}
			}
			if (arrayPosition.size() > 0) {
				// vi tri nhan vien dang dung
				if (latImHere > 0 && lngImHere > 0) {
					drawMarkerMyPosition();
				}
				
				fitBounds(arrayPosition);
			}
		}
	}

	/**
	 * clear marker thong tin gsnpp
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void clearGSNPPMarkerOnMap() {
		removeInPositionArray(new int []{LatLngEx.TYPE_GSNPP});
		clearMapLayer(markerLayerGSNPP);
		/* clearMapLayer(popupLayerGSNPP); */
		clearMapLayer(popupLayer);
	}

	/**
	 * clear marker thong tin tttt
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void clearTTTTMarkerOnMap() {
		removeInPositionArray(new int []{LatLngEx.TYPE_TNPG});
		clearMapLayer(markerLayerTTTT);
		clearMapLayer(popupLayer);
	}

	/**
	 * clear marker thong tin nvbh
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void clearNVBHMarkerOnMap() {
		removeInPositionArray(new int []{LatLngEx.TYPE_NVBH});
		clearMapLayer(markerLayerNVBH);
		/* clearMapLayer(popupLayerNVBH); */
		clearMapLayer(popupLayer);
	}

	/**
	 * Ve marker thong tin nvbh
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void drawNvbhPosition() {
		// isViewingNVBH = true;
		if (gsnppList.itemListNVBH != null) {
			clearNVBHMarkerOnMap();
			// arrayPosition = new ArrayList<LatLng>();
			for (int i = 0, size = gsnppList.itemListNVBH.size(); i < size; i++) {
				THBVRouteSupervisionItem staff = gsnppList.itemListNVBH.get(i);
				if (staff.lat > 0 && staff.lng > 0) {
					if (latDefault <= 0 || lngDefault <= 0) {
						latDefault = staff.lat;
						lngDefault = staff.lng;
						mapView.moveTo(new LatLng(latDefault, lngDefault));
					}
					// if (!StringUtil.isNullOrEmpty(staff.trainingDate)) {
					// showMarkerInMap(staff.lat, staff.lng, i,
					// staff.aStaff.name, staff.aStaff.staffCode,
					// R.drawable.icon_map_position_yellow,
					// ACTION_CLICK_MARKER_NVBH);
					// } else {
					// showMarkerInMap(staff.lat, staff.lng, i,
					// staff.aStaff.name, staff.aStaff.staffCode,
					// R.drawable.icon_map_position_yellow,
					// ACTION_CLICK_MARKER_NVBH);
					// }
					showMarkerInMapNVBH(staff.lat, staff.lng, i,
							staff.staffNameGS, staff.staffCodeGS,
							R.drawable.icon_map_position_yellow,
							ACTION_CLICK_MARKER_NVBH);
					arrayPosition.add(new LatLngEx(staff.lat, staff.lng, LatLngEx.TYPE_NVBH));
				}
			}
			// double latGsnpp =
			// gsnppTtttList.itemList.get(header.selectedSpinnerIndex - 1).lat;
			// double lngGsnpp =
			// gsnppTtttList.itemList.get(header.selectedSpinnerIndex - 1).lng;
			// if (latGsnpp > 0 && lngGsnpp > 0) {
			// showMarkerInMap(latGsnpp, lngGsnpp, header.selectedSpinnerIndex -
			// 1,
			// gsnppTtttList.itemList.get(header.selectedSpinnerIndex -
			// 1).staffNameNVBH,
			// gsnppTtttList.itemList.get(header.selectedSpinnerIndex -
			// 1).staffCodeNVBH,
			// R.drawable.icon_map_position_pink,
			// ACTION_CLICK_MARKER_GSNPP_NO_ROUTING);
			// arrayPosition.add(new LatLng(latGsnpp, lngGsnpp));
			// }

			// vi tri nhan vien dang dung
			if (arrayPosition.size() > 0) {
				if (latImHere > 0 && lngImHere > 0) {
					drawMarkerMyPosition();
				}
				fitBounds(arrayPosition);
			} else {
				moveToCenterMyPosition();
			}
		}
	}

	/**
	 * 
	 * ve marker gsnpp truong hop khi chon vao text xem thong tin NVBH khong
	 * hien thi popup thong tin
	 * 
	 * @author: YenNTH
	 * @param lat
	 * @param lng
	 * @param name
	 * @param drawable
	 * @return: void
	 * @throws:
	 */
	public void drawGSNPPAfterViewNVBHPosition(double lat, double lng,
			String name, int drawable) {
		if (getActivity() != null) {
			TextMarkerMapView marker = new TextMarkerMapView(parent, drawable,
					Constants.STR_BLANK);
			marker.setListener(this, -1, null);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			markerLayerGSNPP.addItemObj(marker, opts);

			Rect bounds = new Rect();
			Paint textPaint = new TextView(parent).getPaint();
			textPaint.getTextBounds(name, 0, name.length(), bounds);
			final int width = bounds.width();
			TextView text = new TextView(parent) {
				@Override
				protected void onMeasure(int widthMeasureSpec,
						int heightMeasureSpec) {
					super.onMeasure(width, heightMeasureSpec);
				}
			};
			text.setLayoutParams(new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(name);
			text.setTextColor(ImageUtil.getColor(R.color.RED));
			opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
			markerLayerGSNPP.addItemObj(text, opts);
			arrayPosition.add(new LatLngEx(lat, lng, LatLngEx.TYPE_GSNPP));
		}
	}

	/**
	 * Ve marker thong tin staff cho to truong tiep thi
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void drawTTTTPosition() {
		if (gsnppTtttList.itemListTTTT != null) {
			clearTTTTMarkerOnMap();
			// arrayPosition = new ArrayList<LatLng>();
			for (int i = 0, size = gsnppTtttList.itemListTTTT.size(); i < size; i++) {
				TBHVRouteSupervisionDTO.THBVRouteSupervisionItem staff = gsnppTtttList.itemListTTTT
						.get(i);
				if (staff.lat > 0 && staff.lng > 0) {
					if (latDefault <= 0 || lngDefault <= 0) {
						latDefault = staff.lat;
						lngDefault = staff.lng;
						mapView.moveTo(new LatLng(latDefault, lngDefault));
					}
					showMarkerInMapTTTT(staff.lat, staff.lng, i, staff.staffNameGS, staff.staffCodeGS, R.drawable.icon_map_position_blue, ACTION_CLICK_MARKER_TTTT);
					arrayPosition.add(new LatLngEx(staff.lat, staff.lng, LatLngEx.TYPE_TNPG));
				}
			}
			if (arrayPosition.size() > 0) {
				// vi tri nhan vien dang dung
				if (latImHere > 0 && lngImHere > 0) {
					drawMarkerMyPosition();
				}
				fitBounds(arrayPosition);
			} else {
				moveToCenterMyPosition();
			}
		}
	}

	/**
	 * Di chuyen ban do toi vi tri dang dung
	 * 
	 * @author: BANGHN
	 */
	protected void moveToCenterMyPosition() {
		if (GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude() > 0
				&& GlobalInfo.getInstance().getProfile().getMyGPSInfo()
						.getLongtitude() > 0) {
			LatLng myLocation = new LatLng(GlobalInfo.getInstance()
					.getProfile().getMyGPSInfo().getLatitude(), GlobalInfo
					.getInstance().getProfile().getMyGPSInfo().getLongtitude());
			mapView.moveTo(myLocation);
			// mapView.invalidate();
		}
	}

	/**
	 * Hien thi marker GSNPP
	 * 
	 * @author: YenNTH
	 * @param lat2
	 * @param lng2
	 * @param i
	 * @param staffNameGS
	 * 
	 * @return: voidvoid
	 * @throws:
	 */
	private void showMarkerGSNPPInMap(double lat, double lng, int index,
			String name, String staffCode, int drawable, int action,
			boolean isShowNvbhPositionButton) {
		if (getActivity() != null) {

			TextMarkerMapView marker = new TextMarkerMapView(parent, drawable,
					Constants.STR_BLANK);
			marker.setListener(this, action, staffCode);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			markerLayerGSNPP.addItemObj(marker, opts);

			Rect bounds = new Rect();
			Paint textPaint = new TextView(parent).getPaint();
			textPaint.getTextBounds(name, 0, name.length(), bounds);
			final int width = bounds.width();
			TextView text = new TextView(parent) {
				@Override
				protected void onMeasure(int widthMeasureSpec,
						int heightMeasureSpec) {
					super.onMeasure(width, heightMeasureSpec);
				}
			};
			text.setLayoutParams(new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(name);
			text.setTextColor(ImageUtil.getColor(R.color.RED));
			opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
			markerLayerGSNPP.addItemObj(text, opts);
		}
	}

	/**
	 * Hien thi marker GSNPP with button view NVBH position
	 * 
	 * @author: YenNTH
	 * @param lat2
	 * @param lng2
	 * @param i
	 * @param staffNameGS
	 * 
	 * @return: voidvoid
	 * @throws:
	 */
	private void showMarkerGSNPPInMap(double lat, double lng, int index,
			String name, String staffCode, int drawable, int action) {
		showMarkerGSNPPInMap(lat, lng, index, name, staffCode, drawable,
				action, true);
	}

	/**
	 * 
	 * hien thi popup thong tin to truong tiep thi
	 * 
	 * @author: YenNTH
	 * @param lat
	 * @param lng
	 * @param index
	 * @param name
	 * @param staffCode
	 * @param drawable
	 * @param action
	 * @return: void
	 * @throws:
	 */
	private void showMarkerInMapTTTT(double lat, double lng, int index,
			String name, String staffCode, int drawable, int action) {
		if (getActivity() != null) {

			TextMarkerMapView marker = new TextMarkerMapView(parent, drawable,
					Constants.STR_BLANK);
			marker.setListener(this, action, staffCode);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			markerLayerTTTT.addItemObj(marker, opts);

			Rect bounds = new Rect();
			Paint textPaint = new TextView(parent).getPaint();
			textPaint.getTextBounds(name, 0, name.length(), bounds);
			final int width = bounds.width();
			TextView text = new TextView(parent) {
				@Override
				protected void onMeasure(int widthMeasureSpec,
						int heightMeasureSpec) {
					super.onMeasure(width, heightMeasureSpec);
				}
			};
			text.setLayoutParams(new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(name);
			text.setTextColor(ImageUtil.getColor(R.color.BLUE));
			opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
			markerLayerTTTT.addItemObj(text, opts);
		}
	}

	/**
	 * 
	 * hien thi popup thong tin nvbh
	 * 
	 * @author: YenNTH
	 * @param lat
	 * @param lng
	 * @param index
	 * @param name
	 * @param staffCode
	 * @param drawable
	 * @param action
	 * @return: void
	 * @throws:
	 */
	private void showMarkerInMapNVBH(double lat, double lng, int index,
			String name, String staffCode, int drawable, int action) {
		if (getActivity() != null) {

			TextMarkerMapView marker = new TextMarkerMapView(parent, drawable,
					Constants.STR_BLANK);
			marker.setListener(this, action, staffCode);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			markerLayerNVBH.addItemObj(marker, opts);

			Rect bounds = new Rect();
			Paint textPaint = new TextView(parent).getPaint();
			textPaint.getTextBounds(name, 0, name.length(), bounds);
			final int width = bounds.width();
			TextView text = new TextView(parent) {
				@Override
				protected void onMeasure(int widthMeasureSpec,
						int heightMeasureSpec) {
					super.onMeasure(width, heightMeasureSpec);
				}
			};
			text.setLayoutParams(new ViewGroup.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			text.setText(name);
			text.setTextColor(ImageUtil.getColor(R.color.GREEN));
			opts = new OverlayViewOptions();
			opts.position(new LatLng(lat, lng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
			markerLayerNVBH.addItemObj(text, opts);
		}
	}

	/**
	 * Lay danh sach NVBH thuoc quyen cua GSBH
	 * 
	 * @author: YenNTH
	 * @param thbvRouteSupervisionItem
	 * @return: voidvoid
	 * @throws:
	 */
	private void getNVBHPosition(THBVRouteSupervisionItem item) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_STAFF_ID, item.staffIdGS);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, Constants.STR_BLANK
				+ item.shopId);
		bundle.putString(IntentConstants.INTENT_DAY, DateUtils.getToday());

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_NVBH_POSITION;
		e.sender = this;
		e.viewData = bundle;

		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			// tranh truong hop notify nhieu lan
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLongtitude();
			if (lat < 0 && lng < 0) {
				return;
			} else {
				latImHere = lat;
				lngImHere = lng;
				removeInPositionArray(new int[]{LatLngEx.TYPE_GSBH});
				arrayPosition.add(new LatLngEx(latImHere, lngImHere, LatLngEx.TYPE_GSBH));
				drawMarkerMyPosition();
			}
			break;
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				header.spinnerStaffCode.setSelection(0);
				clearGSNPPMarkerOnMap();
				clearNVBHMarkerOnMap();
				clearTTTTMarkerOnMap();

				header.cbGsnpp.setChecked(true);
				// drawGsnppPosition();
				header.cbTttt.setChecked(true);
				// drawTTTTPosition();
				getGsnppPosition();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton v, boolean isChecked) {
		// TODO Auto-generated method stub
		if (isChecked) {
			if (v == header.cbGsnpp) {
				isViewingGSNPP = true;

				header.spinnerStaffCode.getSpiner().setEnabled(true);
				// all
				if (header.spinnerStaffCode.getSpiner()
						.getSelectedItemPosition() == 0) {
					drawGsnppPosition();
				}else{
					removeInPositionArray( new int[] {LatLngEx.TYPE_GSNPP ,LatLngEx.TYPE_NVBH} );
					//co chon GSNPP, ve GSNPP + NVBH cua GSNPP
					if(gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat >0 && gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lng>0){
						double lat = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lat;
						double lng = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).lng;
						String name = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffNameGS;
						String gsnppCode = gsnppTtttList.itemGSNPPList.get(header.selectedSpinnerIndex - 1).staffCodeGS;
						int drawable = R.drawable.icon_map_position_pink;
						showMarkerGSNPPInMap(lat, lng, 0, name, gsnppCode,
								drawable, ACTION_CLICK_MARKER_GSNPP_NO_ROUTING,
								false);
						arrayPosition.add(new LatLngEx(lat, lng, LatLngEx.TYPE_GSNPP));
						drawNvbhPosition();
					}
				}
			} else if (v == header.cbTttt) {
				isViewingTTTT = true;
				drawTTTTPosition();
			}
		} else {
			if (v == header.cbGsnpp) {
				//uncheck GSNPP => remove pos GSNPP + NVBH
				isViewingGSNPP = false;
				// isViewingNVBH = false;
				clearGSNPPMarkerOnMap();
				clearNVBHMarkerOnMap();
				header.spinnerStaffCode.getSpiner().setEnabled(false);
			} else if (v == header.cbTttt) {
				isViewingTTTT = false;
				clearTTTTMarkerOnMap();
			}
		}
		
		fitBounds(arrayPosition);
	}

	private void removeInPositionArray(int[] arrRemoveType) {
		
		for (int i = arrayPosition.size() - 1; i >= 0 ; i--) {
			
			boolean isRemove = false;
			//arr type want Remove
			for (int iRemove = 0; iRemove < arrRemoveType.length; iRemove++) {
				LatLng latLng = arrayPosition.get(i);
				if (latLng instanceof LatLngEx) {
					LatLngEx latLngEx = (LatLngEx)latLng;
					if (latLngEx.type == arrRemoveType[iRemove]) {
						isRemove = true;
						break;
					}
				}
			}
			//must remove
			if (isRemove) {
				arrayPosition.remove(i);
			}
		}
	}

}
