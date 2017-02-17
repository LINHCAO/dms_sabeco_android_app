/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerListItem.VISIT_STATUS;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
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
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.CustomerPopupView;
import com.viettel.map.view.MarkerMapView;
import com.viettel.map.view.TextMarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.sabeco.R;

/**
 * Ban do giam sat lo trinh TBHV
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVRouteSupervisionMapView extends FragmentMapView {
	private GlobalBaseActivity parent; // parent
	private THBVRouteSupervisionItem routeSupervisionItem; // a dto item
	//private boolean isRequestGetListCustomer; // is request or not
	private CustomerListDTO cusDto; // customer dto
	private int totalCustomerInVisitPlan; // total Customer In Visit Plan
	private boolean isFirstInitView = true; // is First Init View
	private TBHVRouteSupervisionMapHeader header; // header of view
	private ListStaffDTO gsnppNvbhInfo;
	private OverlayViewLayer popUpLayer;
	private OverlayViewLayer customerLayer;
	private OverlayViewLayer nvbh_gsLayer;

	public static final String TAG = TBHVRouteSupervisionMapView.class.getName();
	private static final int ACTION_CLICK_MARKER_CUSTOMER = 1;
	private static final int ACTION_CLICK_FLAG_CUSTOMER = 3;
	private String from = Constants.STR_BLANK;
	AnimationLinearLayout llRoutingInfo;
	private AnimationImageView ivShowRoutingInfo;

	public static TBHVRouteSupervisionMapView newInstance(Bundle b) {
		TBHVRouteSupervisionMapView f = new TBHVRouteSupervisionMapView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) activity;
		routeSupervisionItem = (THBVRouteSupervisionItem) getArguments().getSerializable(IntentConstants.INTENT_STAFF_DTO);
		from = getArguments().getString(IntentConstants.INTENT_FROM);
//		routeSupervisionItem.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
//		routeSupervisionItem.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		super.onAttach(activity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		// enable menu bar
		SpannableObject title = initTitle(from);
		setTitleHeaderView(title);

		if (routeSupervisionItem.lat > 0 && routeSupervisionItem.lng > 0) {
			mapView.moveTo(new LatLng(routeSupervisionItem.lat, routeSupervisionItem.lng));
		} else {
			mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
		}

		popUpLayer = new OverlayViewLayer();
		mapView.addLayer(popUpLayer);
		customerLayer = new OverlayViewLayer();
		mapView.addLayer(customerLayer);
		nvbh_gsLayer = new OverlayViewLayer();
		mapView.addLayer(nvbh_gsLayer);

		addInfoHeader(from);
		getPositionOfGsnppAndNvbh();
		//layout routing info
		initRoutingInfoLayout();
		//show help layout
		showRoutingInfoLayout();
		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
	}
	
	/**
	 * initTitle
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private SpannableObject initTitle(String from) {
		SpannableObject title = new SpannableObject();
		if(from.equalsIgnoreCase(Constants.TYPE_NVBH)){
			title.addSpan(StringUtil.getString(R.string.TITLE_VIEW_TBHV_02_01_VIEW_NVBH) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		}else {
			title.addSpan(StringUtil.getString(R.string.TITLE_VIEW_TBHV_02_02_VIEW_TTTT) + Constants.STR_SPACE,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		}
		return title;
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
		case CustomerPopupView.POP_UP_CLOSE: {
			clearMapLayer(popUpLayer);
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * Lay vi tri cu gsnpp, nvbh
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void getPositionOfGsnppAndNvbh() {
		String list[] = {Constants.STR_BLANK + routeSupervisionItem.staffIdGS};
		Bundle b = new Bundle();
		b.putStringArray(IntentConstants.INTENT_STAFF_LIST, list);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_POS_GSNPP_NVBH;
		e.viewData = b;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * Hien thi pop up thong tin KH
	 * @author: TamPQ
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
		} else {// click tren marker ghe tham
			opts.position(new com.viettel.maps.base.LatLng(cusDto.cusList.get(index).visitedLng, cusDto.cusList
					.get(index).visitedLat));
		}
		opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
		CustomerPopupView popupView = new CustomerPopupView(parent, "", Constants.TYPE_GST);
		popupView.setListener(this);
		popupView.updateInfo(cusDto.cusList.get(index), index, isOrWithSelectedDay, totalCustomerInVisitPlan);

		clearMapLayer(popUpLayer);
		popUpLayer.addItemObj(popupView, opts);
	}

	/**
	 * addInfoHeader
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void addInfoHeader(String from) {
		if (isFirstInitView) {
			LinearLayout llHeader;
			llHeader = new LinearLayout(parent);

			llHeader.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			llHeader.setOrientation(LinearLayout.HORIZONTAL);

			header = new TBHVRouteSupervisionMapHeader(getActivity());
			header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			header.updateView(routeSupervisionItem, from);
			
			llHeader.addView(header);
			llHeader.setGravity(Gravity.CENTER | Gravity.TOP);
			rlMainMapView.addView(llHeader);
			isFirstInitView = false;
		}
		mapView.invalidate();

	}

	/**
	 * getCustomerAndSaleManInVisitPlan
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getCustomerListForSupervision() {
		parent.showLoadingDialog();
		//isRequestGetListCustomer = true;
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(routeSupervisionItem.staffIdGS));
		b.putString(IntentConstants.INTENT_SHOP_ID, String.valueOf(routeSupervisionItem.shopId));
		b.putString(IntentConstants.INTENT_VISIT_PLAN, Constants.DAY_LINE[(DateUtils.getCurrentDay())]);
		// 0: ko lay ngoai tuyen, 1 lay them ngoai tuyen voi dk da
		b.putString(IntentConstants.INTENT_GET_WRONG_PLAN, "1"); 
		// ghe tham
		e.viewData = b;
		e.action = ActionEventConstant.TBHV_CUSTOMER_LIST_FOR_SUPERVISION;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.TBHV_CUSTOMER_LIST_FOR_SUPERVISION:
			cusDto = (CustomerListDTO) modelEvent.getModelData();
			drawCustomerLocation();
			parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_POS_GSNPP_NVBH:
			gsnppNvbhInfo = (ListStaffDTO) modelEvent.getModelData();
			getCustomerListForSupervision();
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
	 * Ve marker cua nhan vien, khach hang
	 * 
	 * @author : TamPQ since : 1.0
	 */
	private void drawCustomerLocation() {

		if (cusDto == null) {
			return;
		}
		clearCustomerMarkerOnMap();
		ArrayList<LatLng> arrayPosition = new ArrayList<LatLng>();
		int size = cusDto.cusList.size();
		totalCustomerInVisitPlan = size;
		for (int i = 0; i < size; i++) {
			CustomerListItem cus = cusDto.cusList.get(i);
			if (cus.aCustomer.getLat() > 0 && cus.aCustomer.getLng() > 0) {
				arrayPosition.add(new LatLng(cus.aCustomer.getLat(), cus.aCustomer.getLng()));

				if (cus.isOr == 1) {// khach hang ngoai tuyen
					showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr,
							R.drawable.icon_circle_yellow);
				} else if ((cus.visitStatus == VISIT_STATUS.VISITED_CLOSED || cus.visitStatus == VISIT_STATUS.VISITED_FINISHED)
						&& cus.isTodayOrdered) {
					showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr,
							R.drawable.icon_circle_blue);
				} else if ((cus.visitStatus == VISIT_STATUS.VISITED_CLOSED || cus.visitStatus == VISIT_STATUS.VISITED_FINISHED)
						&& !cus.isTodayOrdered) {
					// da ghe tham hoac ghe tham dong cua va ko co don hang
					showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr,
							R.drawable.icon_circle_red);
				} else if (cus.visitStatus == VISIT_STATUS.VISITING) {
					// dang ghe tham
					showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr,
							R.drawable.icon_circle_orange);
				} else {
					// chua ghe tham
					showMarkerInMap(cus.aCustomer.getLat(), cus.aCustomer.getLng(), i, cus.isOr,
							R.drawable.icon_circle_green);
				}

				// toa do ghe tham cua nvbh voi khach hang
				showVisitedOfSaleMan(cus, i, cus.isOr);
			}
			if (cus.isOr == 1) {
				totalCustomerInVisitPlan--;
			}
		}

		if (gsnppNvbhInfo.arrList.size() > 0 && gsnppNvbhInfo.arrList.get(0).lat > 0 && gsnppNvbhInfo.arrList.get(0).lng > 0) {
			showNVBHMarker();
			arrayPosition.add(new LatLng(gsnppNvbhInfo.arrList.get(0).lat, gsnppNvbhInfo.arrList.get(0).lng));
		}

		if (GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude() > 0
				&& GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude() > 0) {
			drawMarkerMyPosition();
			arrayPosition.add(new LatLng(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude(), GlobalInfo
					.getInstance().getProfile().getMyGPSInfo().getLongtitude()));
		}

		// fit overlay ban do
		fitBounds(arrayPosition);
	}
	 
		/**
		 * Hien overlay nhan vien ban hang
		 * 
		 * @author: QuangVT
		 * @since: 3:51:26 PM Dec 5, 2013
		 * @return: void
		 * @throws:
		 */
		@SuppressWarnings("unchecked")
		private void showNVBHMarker() {
			clearMapLayer(nvbh_gsLayer);
			MarkerMapView maker = new MarkerMapView(getActivity(), R.drawable.icon_map_position_pink);
			OverlayViewOptions opts = new OverlayViewOptions();
			opts.position(new LatLng(routeSupervisionItem.lat, routeSupervisionItem.lng));
			opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			nvbh_gsLayer = new OverlayViewLayer();
			mapView.addLayer(nvbh_gsLayer);
			nvbh_gsLayer.addItemObj(maker, opts);
		} 

	/**
	 * 
	 * Toa do ghe tham cua nv doi voi kh
	 * @author: YenNTH
	 * @param dto
	 * @param index
	 * @param isOrWithSelectedDay
	 * @return: void
	 * @throws:
	 */
		private void showVisitedOfSaleMan(CustomerListItem dto, int index, int isOrWithSelectedDay) {
			//duongdt3 update ngoai tuyen khong hien thi co khoang cach
			if (dto.visitedLat <= 0 || dto.visitedLng <= 0 || parent == null || isOrWithSelectedDay == 1) {
				
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
			opts.position(new com.viettel.maps.base.LatLng(dto.visitedLat, dto.visitedLng));
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
			opts.position(new com.viettel.maps.base.LatLng(dto.visitedLat, dto.visitedLng));
			opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
			opts.offsetHeight(10);
			customerLayer.addItemObj(text, opts);
		}

	/**
	 * clear marker
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void clearCustomerMarkerOnMap() {
		clearMapLayer(customerLayer);
		clearMapLayer(popUpLayer);
		clearMapLayer(nvbh_gsLayer);
	}

	/**
	 * Hien thi marker toa do hien tai
	 * 
	 * @author : TamPQ since : 1.0
	 */
	private void showMarkerInMap(double lat, double lng, int index, int isOrWithSelectedDay, int drawable) {
		String seqInDay = "";
		if (index >= 0) {
			if (cusDto.cusList.get(index).isOr == 0) {
				if (!StringUtil.isNullOrEmpty(cusDto.cusList.get(index).seqInDayPlan)) {
					seqInDay = cusDto.cusList.get(index).seqInDayPlan;
				} else {
					seqInDay = "0";
				}
			} else {
				seqInDay = "!";
			}
		}

		TextMarkerMapView maker = new TextMarkerMapView(getActivity(), drawable, seqInDay);
		maker.setListener(this, ACTION_CLICK_MARKER_CUSTOMER, new int[] { index, isOrWithSelectedDay });
		OverlayViewOptions opts = new OverlayViewOptions();
		opts.position(new com.viettel.maps.base.LatLng(lat, lng));
		opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
		customerLayer.addItemObj(maker, opts);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getPositionOfGsnppAndNvbh();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
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
