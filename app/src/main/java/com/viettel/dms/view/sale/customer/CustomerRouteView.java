/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.StaffCustomerDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerListItem.VISIT_STATUS;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.AnimationImageView;
import com.viettel.dms.view.control.AnimationLinearLayout;
import com.viettel.dms.view.control.ImageButtonOnMap;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinerRoute;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.CustomerPopupView;
import com.viettel.map.view.TextMarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * duong di toi uu toi khach hang
 * 
 * @author banghn
 * 
 * @author : BangHN since : 1.0 version : 1.0
 */
public class CustomerRouteView extends FragmentMapView implements OnItemSelectedListener {
	public static final String TAG = CustomerRouteView.class.getName();
	private final int ACTION_CLICK_MARKER_CUSTOMER = 101;
	private final int ACTION_CLICK_FLAG_CUSTOMER = 102;
	private String[] arrLineChoose = new String[] { StringUtil.getString(R.string.TEXT_MONDAY),
			StringUtil.getString(R.string.TEXT_TUESDAY), StringUtil.getString(R.string.TEXT_WEDNESDAY),
			StringUtil.getString(R.string.TEXT_THURSDAY), StringUtil.getString(R.string.TEXT_FRIDAY),
			StringUtil.getString(R.string.TEXT_SATURDAY), StringUtil.getString(R.string.TEXT_SUNDAY) };
	public CustomerListDTO cusDto;// cusList
	@SuppressWarnings("unused")
	private static final int MENU_STAFF_TAB = 0;
	private static final int MENU_STAFF_POSITION = 1;
	private static final int MENU_ATTENDANCE = 2;
	private static final int MENU_PLAN_PROCESS = 3;

	private static final int ACTION_OK = 14;
	private static final int ACTION_CANCEL = 15;

	SpinerRoute spiner;// spiner tuyen trong tuan
	SpinnerAdapter adapterLine;// spiner tuyen trong tuan
	ImageButtonOnMap direction;// chi dan lo trinh toi uu 

	double lat, lng;// toa do ban do
	int totalCustomerInVisitPlan = 0; // tong so kh trong tuyen
	double latNVBH, lngNVBH;// toa do ban do
	boolean isRequestingGetListCustomer = false;
	int selectingItem = -1;
	private OverlayViewLayer popupLayer;
	private OverlayViewLayer customerLayer;
	
	private boolean isTBHVReportVisitCustomerDayOfDetailNPP=false;
	private String staffID;
	private String shopID;
	AnimationLinearLayout llRoutingInfo;
	private AnimationImageView ivShowRoutingInfo;

	public static CustomerRouteView getInstance(Bundle b) {
		CustomerRouteView f = new CustomerRouteView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) activity;
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		if(getArguments()!=null){
			if(getArguments().getBoolean(IntentConstants.FROM_TBHV_REPORT)){
				isTBHVReportVisitCustomerDayOfDetailNPP=getArguments().getBoolean(IntentConstants.FROM_TBHV_REPORT);
				if(!StringUtil.isNullOrEmpty(getArguments().getString(IntentConstants.INTENT_STAFF_ID))){
					staffID=getArguments().getString(IntentConstants.INTENT_STAFF_ID);
				}
				if(!StringUtil.isNullOrEmpty(getArguments().getString(IntentConstants.INTENT_SHOP_ID))){
					shopID=getArguments().getString(IntentConstants.INTENT_SHOP_ID);
				}
			}
		}
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		lat = latNVBH = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		lng = lngNVBH = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();

		if(isTBHVReportVisitCustomerDayOfDetailNPP){
			enableMenuBar(this);
			addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE), R.drawable.icon_task, MENU_PLAN_PROCESS);
			addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING), R.drawable.icon_clock, MENU_ATTENDANCE);
			addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION), R.drawable.icon_map, MENU_STAFF_POSITION);
//			addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING), R.drawable.menu_customer_icon, MENU_STAFF_TAB);
			setMenuItemFocus(1);
		}else{
			// enable menu bar
			enableMenuBar(this, FragmentMenuContanst.NVBH_CUSTOMER_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_CUSTOMER_ROUTE);
		}
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_ROUTE));

		customerLayer = new OverlayViewLayer();
		mapView.addLayer(customerLayer);
		popupLayer = new OverlayViewLayer();
		mapView.addLayer(popupLayer);

		setCustomerRoute(DateUtils.getCurrentDay());
		getCustomerInVisitPlan();
		//dinh vi lai
		restartLocatingUpdate();
		//layout routing info
		initRoutingInfoLayout();
		//show help layout
		showRoutingInfoLayout();
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isRequestingGetListCustomer) {
			setCustomerRoute(selectingItem);
			drawCustomerLocation();
		}
	}

	/**
	 * load danh sach khach hang trong tuyen
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void getCustomerInVisitPlan() {
		isRequestingGetListCustomer = true;
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		if(isTBHVReportVisitCustomerDayOfDetailNPP){
			b.putString(IntentConstants.INTENT_STAFF_ID, staffID);
			b.putString(IntentConstants.INTENT_SHOP_ID, shopID);
		}else{
			b.putString(IntentConstants.INTENT_STAFF_ID,
					String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
			b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		}
		b.putString(IntentConstants.INTENT_VISIT_PLAN, getVisitPlan(arrLineChoose[spiner.getSelectedItemPosition()]));
		// 1: lay khach hang ngoai tuyen, 0 : nguoc lai
		if (getVisitPlan(arrLineChoose[spiner.getSelectedItemPosition()]).equals(
				Constants.DAY_LINE[(DateUtils.getCurrentDay())])) {
			b.putBoolean(IntentConstants.INTENT_GET_WRONG_PLAN, true);
		} else {
			b.putBoolean(IntentConstants.INTENT_GET_WRONG_PLAN, false);
		}
		b.putInt(IntentConstants.INTENT_ROLE_TYPE, 0);

		e.viewData = b;
		e.action = ActionEventConstant.GET_CUSTOMER_LIST_FOR_ROUTE;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

//	/**
//	 * set toa do tam ban do
//	 * 
//	 * @author : BangHN since : 1.0
//	 */
//	private void setMapViewLocation(double lat, double lng) {
//		LatLng pointerCenter = new LatLng(lat, lng);
//		this.lat = lat;
//		this.lng = lng;
//		mapView.setCenter(pointerCenter);
//	}

	private String getVisitPlan(String date) {
		String d = "";
		if (date.equals(StringUtil.getString(R.string.TEXT_MONDAY))) {
			d = StringUtil.getString(R.string.TEXT_MONDAY_);
		} else if (date.equals(StringUtil.getString(R.string.TEXT_TUESDAY))) {
			d = StringUtil.getString(R.string.TEXT_TUESDAY_);
		} else if (date.equals(StringUtil.getString(R.string.TEXT_WEDNESDAY))) {
			d = StringUtil.getString(R.string.TEXT_WEDNESDAY_);
		} else if (date.equals(StringUtil.getString(R.string.TEXT_THURSDAY))) {
			d = StringUtil.getString(R.string.TEXT_THUSDAY_);
		} else if (date.equals(StringUtil.getString(R.string.TEXT_FRIDAY))) {
			d = StringUtil.getString(R.string.TEXT_FRIDAY_);
		} else if (date.equals(StringUtil.getString(R.string.TEXT_SATURDAY))) {
			d = StringUtil.getString(R.string.TEXT_SATURDAY_);
		} else if (date.equals(StringUtil.getString(R.string.TEXT_SUNDAY))) {
			d = StringUtil.getString(R.string.TEXT_SUNDAY_);
		}
		return d;

	}

	/**
	 * Hien thi thong tin tren duong di
	 * 
	 * @author: BangHN
	 *            : Vi tri cua mot diem tren duong di
	 * @return: void
	 */
	private void showCustomerInfoPopup(int[] data, boolean isClikInCustomer) {
		int index = data[0];
		int isOrWithSelectedDay = data[1];
		if (index < 0) {
			return;
		}

		OverlayViewOptions opts = new OverlayViewOptions();
		if (isClikInCustomer) {// click tren marker customer
			opts.position(new com.viettel.maps.base.LatLng(cusDto.cusList.get(index).aCustomer.getLat(), cusDto.cusList
					.get(index).aCustomer.getLng()));
			opts.offsetHeight(-15);
		} else {// click tren marker ghe tham
			opts.position(new com.viettel.maps.base.LatLng(cusDto.cusList.get(index).visitedLat, cusDto.cusList
					.get(index).visitedLng));
			opts.offsetHeight(-23);
		}
		opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
		CustomerPopupView popupInfoView;
		if(isTBHVReportVisitCustomerDayOfDetailNPP){
			popupInfoView = new CustomerPopupView(parent, "", Constants.TYPE_GST);
		}else{
			popupInfoView = new CustomerPopupView(parent, "", Constants.TYPE_NVBH);
		}
		popupInfoView.setListener(this);
		popupInfoView.updateInfo(cusDto.cusList.get(index), index, isOrWithSelectedDay, totalCustomerInVisitPlan);

		clearMapLayer(popupLayer);
		popupLayer.addItemObj(popupInfoView, opts);
	}

	/**
	 * set layout tuyen ghe tham khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void setCustomerRoute(int day) {
		spiner = new SpinerRoute(getActivity());
		spiner.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		spiner.setMinimumWidth(110);
		spiner.setGravity(Gravity.CENTER_HORIZONTAL);
		adapterLine = new SpinnerAdapter(getActivity(), R.layout.simple_spinner_item, arrLineChoose);
		spiner.setOnItemSelectedListener(this);
		spiner.setAdapter(adapterLine);
		spiner.setSelection(day);
		selectingItem = spiner.getSelectedItemPosition();

		LinearLayout llSpiner;
		llSpiner = new LinearLayout(rlMainMapView.getContext());

		llSpiner.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		llSpiner.setOrientation(LinearLayout.HORIZONTAL);
		llSpiner.addView(spiner);
		llSpiner.setGravity(Gravity.RIGHT | Gravity.TOP);

		rlMainMapView.addView(llSpiner);
		mapView.invalidate();
	}

	/**
	 * Ve marker thong tin khach hang, nvbh
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void drawCustomerLocation() {
		if (cusDto == null) {
			return;
		}
		clearCustomerMarkerOnMap();
		ArrayList<LatLng> arrLatLng= new ArrayList<LatLng>();
		int size = cusDto.cusList.size();
		totalCustomerInVisitPlan = size;
		for (int i = 0; i < size; i++) {
			CustomerListItem cus = cusDto.cusList.get(i);
			if (cus.aCustomer.getLat() > 0 && cus.aCustomer.getLng() > 0) {
//				if (lat <= 0 || lng <= 0) {
//					lat = cus.aCustomer.getLat();
//					lng = cus.aCustomer.getLng();
//					setMapViewLocation(lat, lng);
//				}

				// neu show cac ngay khac hom nay thi marker toan mau xanh
				if (!getVisitPlan(arrLineChoose[spiner.getSelectedItemPosition()]).equals(
						Constants.DAY_LINE[(DateUtils.getCurrentDay())])) {
					showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, 0, cus.seqInDayPlan,
							R.drawable.icon_circle_green);
				} else {
					if (cus.isOr == 1) {
						totalCustomerInVisitPlan--;
						showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr, cus.seqInDayPlan,
								R.drawable.icon_circle_yellow);
					}// da (ghe tham hoac dong cua) va co don hang
					else if ((cus.visitStatus == VISIT_STATUS.VISITED_CLOSED || cus.visitStatus == VISIT_STATUS.VISITED_FINISHED)
							&& cus.isTodayOrdered) {
						showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr, cus.seqInDayPlan,
								R.drawable.icon_circle_blue);
					} else if ((cus.visitStatus == VISIT_STATUS.VISITED_CLOSED || cus.visitStatus == VISIT_STATUS.VISITED_FINISHED)
							&& !cus.isTodayOrdered) {
						// da ghe tham hoac ghe tham dong cua va ko co don hang
						showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr, cus.seqInDayPlan,
								R.drawable.icon_circle_red);
					} else if (cus.visitStatus == VISIT_STATUS.VISITING) {
						// dang ghe tham
						showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr, cus.seqInDayPlan,
								R.drawable.icon_circle_orange);
					} else {
						// chua ghe tham
						showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr, cus.seqInDayPlan,
								R.drawable.icon_circle_green);
					}

					// toa do ghe tham cua nvbh voi khach hang
					if (cus.isOr == 1) {
						showVisitedOfSaleMan(cus, i, 1);
					} else {
						showVisitedOfSaleMan(cus, i, 0);
					}
				}
				LatLng latLng= new LatLng(cus.aCustomer.getLat(), cus.aCustomer.getLng());
				arrLatLng.add(latLng);
			}
			
		}
		fitBounds(arrLatLng);
		if (latNVBH > 0 && lngNVBH > 0) {
			drawMarkerMyPosition();
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
		clearMapLayer(customerLayer);
		clearMapLayer(popupLayer);
	}

	/**
	 * Hien thi toa do nvbh so voi khach hang & khoang cach chenh lech
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void showVisitedOfSaleMan(CustomerListItem dto, int index, int isOrWithSelectedDay) {
		if (dto.visitedLat <= 0 || dto.visitedLng <= 0 || parent == null) {
			return;
		}
		TextMarkerMapView marker;
		com.viettel.map.dto.LatLng cus = new com.viettel.map.dto.LatLng(dto.aCustomer.getLat(),
				dto.aCustomer.getLng());
		com.viettel.map.dto.LatLng sale = new com.viettel.map.dto.LatLng(dto.visitedLat, dto.visitedLng);
		double distance = GlobalUtil.getDistanceBetween(cus, sale);
		if (distance <= dto.shopDistance) {// be hon 300m
			marker = new TextMarkerMapView(parent, R.drawable.icon_flag_blue, "");
		} else {
			marker = new TextMarkerMapView(parent, R.drawable.icon_flag_red, "");
		}
		marker.setListener(this, ACTION_CLICK_FLAG_CUSTOMER, new int[] { index, isOrWithSelectedDay });
		OverlayViewOptions opts = new OverlayViewOptions();
		opts.position(new LatLng(dto.visitedLat, dto.visitedLng));
		opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_LEFT);
		customerLayer.addItemObj(marker, opts);

		String formate = "";
		if (distance >= 1000) {
			float tempDistance = (float) distance / 1000;
			DecimalFormat df = new DecimalFormat("#.##");
			formate = df.format(tempDistance);
			formate = "(" + dto.aCustomer.customerCode + "): " + formate + " km";
		} else {
			DecimalFormat df = new DecimalFormat("#.##");
			formate = df.format(distance);
			formate = "(" + dto.aCustomer.customerCode + "): " + formate + " m";
		}

		Rect bounds = new Rect();
		Paint textPaint = new TextView(parent).getPaint();
		textPaint.getTextBounds(formate.toString(), 0, formate.toString().length(), bounds);
		final int width = bounds.width();
		TextView text = new TextView(parent) {
			@Override
			protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
				super.onMeasure(width, heightMeasureSpec);
			}
		};
		text.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		text.setText(formate);
		text.setTextColor(ImageUtil.getColor(R.color.RED));
		opts = new OverlayViewOptions();
		opts.position(new LatLng(dto.visitedLat, dto.visitedLng));
		opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
		opts.offsetHeight(10);
		customerLayer.addItemObj(text, opts);
	}

	/**
	 * Hien thi marker toa do hien tai
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void showMarkerInMap(double lat, double lng, int index, int isOrWithSelectedDay, String seqInPlan,
			int drawable) {
		String seq = "";
		if (isOrWithSelectedDay == 0) {
			if (StringUtil.isNullOrEmpty(seqInPlan)) {
				seq = "0";
			} else {
				seq = seqInPlan;
			}
		} else {
			seq = "!";
		}

		if (getActivity() == null)
			return;
		TextMarkerMapView marker = new TextMarkerMapView(getActivity(), drawable, seq);
		marker.setListener(this, ACTION_CLICK_MARKER_CUSTOMER, new int[] { index, isOrWithSelectedDay });
		OverlayViewOptions opts = new OverlayViewOptions();
		opts.position(new com.viettel.maps.base.LatLng(lat, lng));
		opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
		customerLayer.addItemObj(marker, opts);
	}
	
	/**
	 * di toi man hinh chi tiet Khach hang
	 * 
	 * @author : TamPQ since : 10:59:39 AM
	 */
	private void gotoCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-02");
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Ghe tham khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void visitCustomer(CustomerListItem item) {
		ActionLogDTO action = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
		if (GlobalInfo.getInstance().getProfile().isVisitingCustomer() && action != null
				&& action.aCustomer.customerId != item.aCustomer.customerId) {
			// ket thuc ghe tham
			// kiem tra neu vao lai dung khach hang do thi khong insertlog
			SpannableObject textConfirmed = new SpannableObject();
			textConfirmed.addSpan(StringUtil
					.getString(R.string.TEXT_ALREADY_VISIT_CUSTOMER),
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
			if(!StringUtil.isNullOrEmpty(action.aCustomer.customerCode)) {
				textConfirmed.addSpan(" " + action.aCustomer.customerCode.substring(0, 3),
						ImageUtil.getColor(R.color.WHITE),
						android.graphics.Typeface.BOLD);
			}
			textConfirmed.addSpan(" - ",
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(action.aCustomer.customerName,
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(" trong ",
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
			textConfirmed.addSpan(DateUtils.getVisitTime(action.startTime),
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(StringUtil.getString(R.string.TEXT_ASK_END_VISIT_CUSTOMER),
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
			
			GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent, textConfirmed.getSpan(),
					StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					ACTION_CANCEL, item, false, false);
		} else {
			processOrder(item);
		}
	}

	/**
	 * Di toi kiem hang ton
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void gotoRemainProductView(String customerId, String customerCode, String customerName,
			String customerAddress, int customerTypeId, int isOr) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		b.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCode);
		b.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerName);
		b.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, customerAddress);
		b.putString(IntentConstants.INTENT_IS_OR, String.valueOf(isOr));
		b.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, customerTypeId);
		e.viewData = b;
		e.action = ActionEventConstant.GO_TO_REMAIN_PRODUCT_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Chuyen den man hinh dat hang
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void gotoCreateOrder(CustomerListItem dto) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, dto.aCustomer.getCustomerId());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, dto.aCustomer.getCustomerName());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, dto.aCustomer.getStreet());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, dto.aCustomer.getCustomerCode());
		bundle.putString(IntentConstants.INTENT_ORDER_ID, "0");
		bundle.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, dto.aCustomer.getCustomerTypeId());
		bundle.putSerializable(IntentConstants.INTENT_SUGGEST_ORDER_LIST, null);
		bundle.putString(IntentConstants.INTENT_IS_OR, String.valueOf(dto.isOr));
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_ORDER_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * K tra lai khoang cach de cham trung bay, kiem ton
	 * 
	 * @author: TamPQ
	 * @param item
	 * @return: voidvoid
	 * @throws:
	 */
	private boolean isValidDistanceToVoteDisplayProgAndCheckRemain(CustomerListItem item) {
		item.updateCustomerDistance();
		return !item.isTooFarShop;
	}
	
	/**
	 * Thuc hien ghe tham, dat hang,....
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void processOrder(CustomerListItem item) {
		if (item.isOr == 0) {// KH trong tuyen
			// neu co chuong trinh trung bay chua cham
			if (isValidDistanceToVoteDisplayProgAndCheckRemain(item) && item.isHaveSaleOrder && !item.isTodayCheckedRemain) {
				// neu co don dat hang & chua kiem hang ton
				gotoRemainProductView(item.aCustomer.getCustomerId(), item.aCustomer.getCustomerCode(),
						item.aCustomer.getCustomerName(), item.aCustomer.getStreet(),
						item.aCustomer.getCustomerTypeId(), item.isOr);
			} else {
				gotoCreateOrder(item);
			}

			if (item.visitStatus != VISIT_STATUS.VISITED_FINISHED) {
				if (item.visitStatus == VISIT_STATUS.VISITED_CLOSED) {
					saveLastVisitToActionLogProfile(item);
					parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
				} else if (item.visitStatus == VISIT_STATUS.VISITING) {
					saveLastVisitToActionLogProfile(item);
					parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
					if (item.isTodayCheckedRemain || item.isTodayVoted || item.isTodayOrdered) {
						// da ghe tham, da dat hang, da cham trung bay, da kiem
						// hang
						// ton trong ngay thi an menu dong cua
						parent.removeMenuCloseCustomer();
					}
				} else {// VISIT_STATUS.NONE_VISIT
					parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);
					parent.requestStartInsertVisitActionLog(item);
					item.visitStatus = VISIT_STATUS.VISITING;
				}
			} else {// neu da ghe tham thi chi hien thi title Dang ghe tham
				parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
				saveLastVisitToActionLogProfile(item);
			}

			// check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi
			// khoach cach qua xa
			if (!StringUtil.isNullOrEmpty(item.exceptionOrderDate)) {
				requestUpdateExceptionOrderDate(item);
			}
		} else {// KH ngoai tuyen
			gotoCreateOrder(item);

			if (item.visitStatus == VISIT_STATUS.VISITED_FINISHED) {
				parent.initCustomerNameOnMenuVisit(item.aCustomer.shortCode, item.aCustomer.customerName);
			} else {
				parent.initMenuVisitCustomer(item.aCustomer.shortCode, item.aCustomer.customerName);

			}
			parent.removeMenuCloseCustomer();
			parent.removeMenuFinishCustomer();

			if (item.visitStatus == VISIT_STATUS.NONE_VISIT) {
				parent.requestStartInsertVisitActionLog(item);
				item.visitStatus = VISIT_STATUS.VISITING;
			} else {
				saveLastVisitToActionLogProfile(item);
			}
		}
	}

	/**
	 * //check to update EXCEPTION_ORDER_DATE: bien cho phep dat hang khi khoach
	 * cach qua xa
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void requestUpdateExceptionOrderDate(CustomerListItem item) {
		StaffCustomerDTO staffCusDto = new StaffCustomerDTO();
		staffCusDto.staffCustomerId = item.staffCustomerId;

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_EXCEPTION_ORDER_DATE;
		e.viewData = staffCusDto;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param item
	 * @return: voidvoid
	 * @throws:
	 */
	private void saveLastVisitToActionLogProfile(CustomerListItem item) {
		ActionLogDTO action = new ActionLogDTO();
		// chu y ko co id cua action_log
		action.id = item.visitActLogId;
		action.aCustomer.customerId = item.aCustomer.customerId;
		action.aCustomer.customerName = item.aCustomer.customerName;
		action.aCustomer.customerCode = item.aCustomer.customerCode;
		action.aCustomer.shortCode = item.aCustomer.shortCode;
		action.startTime = item.visitStartTime;
		action.endTime = item.visitEndTime;
		action.isOr = item.isOr;
		action.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLastLatitude();
		action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLastLongtitude();
		// action.objectId = item.visit;
		action.objectType = "0";
		GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_ROUTE:
			ArrayList<Object> data = (ArrayList<Object>) modelEvent.getModelData();
			cusDto = (CustomerListDTO) data.get(0);
			drawCustomerLocation();
			break;
		case ActionEventConstant.UPDATE_ACTION_LOG:
			GlobalInfo.getInstance().getProfile().setVisitingCustomer(false);
			GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer((ActionLogDTO) e.viewData);
			processOrder((CustomerListItem) e.userData);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_CLICK_MARKER_CUSTOMER:
			showCustomerInfoPopup((int[]) data, true);
			break;
		case ACTION_CLICK_FLAG_CUSTOMER:
			showCustomerInfoPopup((int[]) data, false);
			break;
		case MENU_PLAN_PROCESS:{
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
		}
			break;
		case MENU_ATTENDANCE: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ATTENDANCE;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_STAFF_POSITION: {
			Bundle b = new Bundle();
			ActionEvent e = new ActionEvent();
			b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
//		case MENU_STAFF_TAB: {
//			ActionEvent e = new ActionEvent();
//			e = new ActionEvent();
//			e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
//			e.sender = this;
//			TBHVController.getInstance().handleSwitchFragment(e);
//			break;
//		}
		case ACTION_OK:
			CustomerListItem item = (CustomerListItem) data;
			// ket thuc ghe tham
			parent.requestUpdateActionLog("0", null, item, this);
			break;
		case CustomerPopupView.POP_UP_CLOSE:
			clearMapLayer(popupLayer);
			break;
		case CustomerPopupView.GOTO_INFO: {
			int index = (Integer) data;
			gotoCustomerInfo(cusDto.cusList.get(index).aCustomer.getCustomerId());
			break;
		}
		case CustomerPopupView.GOTO_ORDER: {
			int index = (Integer) data;
			visitCustomer(cusDto.cusList.get(index));
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			// tranh truong hop notify nhieu lan
			VTLog.i("Location", DateUtils.now() + " Map: location changed: (lat-lng) "
					+ GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude() + " - "
					+ GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());

			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			if (lat < 0 && lng < 0) {
				return;
			} else {
				drawMarkerMyPosition();
			}
			break;
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getCustomerInVisitPlan();
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
			if (selectingItem != -1 && selectingItem != arg2) {
				selectingItem = arg2;
				getCustomerInVisitPlan();
			} else {
				selectingItem = arg2;
			}
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	/**
	 * showRoutingInfoLayout
	 */
	public void showRoutingInfoLayout(){
		if (llRoutingInfo != null && llRoutingInfo.getChildCount() == 0) {
			llRoutingInfo.initAnimationFade();
			LayoutInflater inflater = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			ViewGroup layoutView = (ViewGroup)inflater.inflate(R.layout.layout_help_routing, llRoutingInfo, false);
			ImageView iv = (ImageView) layoutView.findViewById(R.id.btClose);
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (llRoutingInfo != null) {
						llRoutingInfo.setVisibility(View.GONE);
						ivShowRoutingInfo.setVisibility(View.VISIBLE);
					}
				}
			});
			//add view
			llRoutingInfo.addView(layoutView);
			//show header
			llRoutingInfo.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * initRoutingInfoLayout
	 */
	private void initRoutingInfoLayout() {
		llRoutingInfo = new AnimationLinearLayout(parent);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lp.bottomMargin = GlobalUtil.dip2Pixel(5);
		llRoutingInfo.setLayoutParams(lp);
		llRoutingInfo.setOrientation(LinearLayout.HORIZONTAL);
		llRoutingInfo.setVisibility(View.GONE);
		rlMainMapView.addView(llRoutingInfo);
		ivShowRoutingInfo = new AnimationImageView(parent);
		ivShowRoutingInfo.setBackgroundResource(R.drawable.bg_white_rounded);
		ivShowRoutingInfo.setVisibility(View.GONE);
		ivShowRoutingInfo.setScaleType(ImageView.ScaleType.FIT_CENTER);
		RelativeLayout.LayoutParams lpImageInfo = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lpImageInfo.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lpImageInfo.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		lpImageInfo.width = GlobalUtil.dip2Pixel(48);
		lpImageInfo.height = GlobalUtil.dip2Pixel(48);
		lpImageInfo.leftMargin = GlobalUtil.dip2Pixel(65);
		lpImageInfo.bottomMargin = GlobalUtil.dip2Pixel(5);
		ivShowRoutingInfo.setLayoutParams(lpImageInfo);
		ivShowRoutingInfo.setPadding(GlobalUtil.dip2Pixel(5),
				GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5),
				GlobalUtil.dip2Pixel(5));
		ivShowRoutingInfo.setImageResource(R.drawable.icon_info);
		ivShowRoutingInfo.initAnimationFade();
		ivShowRoutingInfo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ivShowRoutingInfo.setVisibility(View.GONE);
				llRoutingInfo.setVisibility(View.VISIBLE);
			}
		});
		rlMainMapView.addView(ivShowRoutingInfo);
	}
}
