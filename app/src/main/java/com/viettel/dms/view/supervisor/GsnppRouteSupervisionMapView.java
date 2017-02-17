/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
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
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.dto.view.ListStaffDTO;
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
import com.viettel.map.dto.GeomDTO;
import com.viettel.map.view.CustomerPopupView;
import com.viettel.map.view.MarkerMapView;
import com.viettel.map.view.TextMarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

 
/**
 * Hien thi ban do lo trinh
 * 
 * GsnppRouteSupervisionMapView.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  3:44:24 PM Dec 5, 2013
 */
public class GsnppRouteSupervisionMapView extends FragmentMapView {
	/*------------- DEFAULT -------------*/
	//--- TAG
	public static final String TAG = GsnppRouteSupervisionMapView.class.getName();
	//--- PARENT
	private GlobalBaseActivity parent;
	
	/*------------- ACTION CONSTANT -------------*/
	private static final int ACTION_CLICK_FLAG_CUSTOMER = 102;		// click flag customer
	private static final int ACTION_CLICK_MARKER_CUSTOMER = 101;	// click marker customer
	
	/*------------- DTO -------------*/
	public CustomerListDTO cusDto;// cusList
	private GsnppRouteSupervisionItem staffDTO;// thong tin nhan vien ban han
	private ListStaffDTO nvbhInfo;
	
	/*------------- OVERLAY -------------*/
	private OverlayViewLayer customerLayer;	// overlay customer
	private OverlayViewLayer popupLayer;	// overlay popup
	private OverlayViewLayer staffLayer;	// overlay staff

	/*------------- VARIABLES -------------*/
	private int totalCustomerInVisitPlan = 0; // tong so kh trong tuyen
	AnimationLinearLayout llRoutingInfo;
	private AnimationImageView ivShowRoutingInfo;
	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author: QuangVT
	 * @since: 3:49:06 PM Dec 5, 2013
	 * @return: GsnppRouteSupervisionMapView
	 * @throws:  
	 * @param b
	 * @return
	 */
	public static GsnppRouteSupervisionMapView getInstance(Bundle b) {
		GsnppRouteSupervisionMapView f = new GsnppRouteSupervisionMapView();
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
		super.onCreate(savedInstanceState);
		staffDTO = (GsnppRouteSupervisionItem) getArguments().getSerializable(IntentConstants.INTENT_STAFF_DTO);
	}
 
	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, container, savedInstanceState);
		// enable menu bar
		SpannableObject title = initTitle();
		setTitleHeaderView(title);

		if (GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude() > 0
				&& GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude() > 0) {
			moveToCenterMyPosition();
		} else {
			mapView.moveTo(new com.viettel.maps.base.LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
		}

		customerLayer = new OverlayViewLayer();
		mapView.addLayer(customerLayer);
		popupLayer = new OverlayViewLayer();
		mapView.addLayer(popupLayer);
		staffLayer = new OverlayViewLayer();
		mapView.addLayer(staffLayer);
		getPositionOfNvbh();
		//layout routing info
		initRoutingInfoLayout();
		//show help layout
		showRoutingInfoLayout();
		return v;

	} 
	 
	/**
	 * Lay vi tri cua nhan vien ban hang
	 * 
	 * @author: QuangVT
	 * @since: 3:50:02 PM Dec 5, 2013
	 * @return: void
	 * @throws:
	 */
	private void getPositionOfNvbh() {
		String list[] = { "" + staffDTO.aStaff.staffId };
		Bundle b = new Bundle();
		b.putStringArray(IntentConstants.INTENT_STAFF_LIST, list);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_POS_GSNPP_NVBH;
		e.viewData = b;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}
 
	/**
	 * Khoi tao title cho view
	 * 
	 * @author: QuangVT
	 * @since: 3:50:24 PM Dec 5, 2013
	 * @return: SpannableObject
	 * @throws:  
	 * @return
	 */
	private SpannableObject initTitle() {
		String dateTime = DateUtils.getCurrentDateTimeWithFormat("dd/MM/yyyy");
		SpannableObject title = new SpannableObject();
		title.addSpan(getParentPrefixTitle() + "-01. "); 
		title.addSpan(StringUtil.getString(R.string.TITLE_VIEW_SALE_MAN_ROUTE_VIEW_DEFAULT) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		title.addSpan(dateTime, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		title.addSpan(" " + StringUtil.getString(R.string.TEXT_OF_NVBH) + " ", ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		title.addSpan(staffDTO.aStaff.name, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		return title;
	} 
	 
	/**
	 * load danh sach khach hang trong tuyen
	 * 
	 * @author: QuangVT
	 * @since: 3:50:38 PM Dec 5, 2013
	 * @return: void
	 * @throws:
	 */
	private void getCustomerListForSupervision() {
		parent.showLoadingDialog(); 
		ActionEvent e = new ActionEvent();

		VTLog.i("SaleRoute", "Tuyen hien tai: " + Constants.DAY_LINE[(DateUtils.getCurrentDay())]);

		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, "" + staffDTO.aStaff.staffId);
		b.putString(IntentConstants.INTENT_SHOP_ID, "" + staffDTO.aStaff.shopId);
		b.putString(IntentConstants.INTENT_VISIT_PLAN, Constants.DAY_LINE[(DateUtils.getCurrentDay())]);
		b.putBoolean(IntentConstants.INTENT_GET_WRONG_PLAN, true);// 0: ko lay
																// ngoai tuyen,
																// 1 lay them
																// ngoai tuyen
																// voi dk da

		e.viewData = b;
		e.action = ActionEventConstant.GET_CUSTOMER_LIST_FOR_ROUTE;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	} 
	 
	/**
	 * Hien thi thong tin tren duong di
	 * 
	 * @author: QuangVT
	 * @since: 3:50:52 PM Dec 5, 2013
	 * @return: void
	 * @throws:  
	 * @param data
	 * @param isClikInCustomer
	 */
	private void showCustomerInfoPopup(int[] data, boolean isClikInCustomer) {
		int index = data[0];
		int isOrWithSelectedDay = data[1];
		if (index < 0) {
			return;
		}

		OverlayViewOptions opts = new OverlayViewOptions();
		if (isClikInCustomer) {// click tren marker customer
			opts.position(new LatLng(cusDto.cusList.get(index).aCustomer.getLat(), cusDto.cusList.get(index).aCustomer
					.getLng()));
			opts.offsetHeight(-15);
		} else {// click tren marker ghe tham
			opts.position(new com.viettel.maps.base.LatLng(cusDto.cusList.get(index).visitedLat, cusDto.cusList
					.get(index).visitedLng));
			opts.offsetHeight(-23);
		}
		opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
		CustomerPopupView popupInfoView = new CustomerPopupView(parent, "", Constants.TYPE_GS);
		popupInfoView.setListener(this);
		popupInfoView.updateInfo(cusDto.cusList.get(index), index, isOrWithSelectedDay, totalCustomerInVisitPlan);

		clearMapLayer(popupLayer);
		popupLayer.addItemObj(popupInfoView, opts);
	} 
	 
	/**
	 * Ve marker cua nhan vien, khach hang
	 * 
	 * @author: QuangVT
	 * @since: 3:51:12 PM Dec 5, 2013
	 * @return: void
	 * @throws:
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

		if (nvbhInfo.arrList.size() > 0 && nvbhInfo.arrList.get(0).lat > 0 && nvbhInfo.arrList.get(0).lng > 0) {
			showNVBHMarker();
			arrayPosition.add(new LatLng(nvbhInfo.arrList.get(0).lat, nvbhInfo.arrList.get(0).lng));
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
		clearMapLayer(staffLayer);
		MarkerMapView maker = new MarkerMapView(getActivity(), R.drawable.icon_map_position_pink);
		OverlayViewOptions opts = new OverlayViewOptions();
		opts.position(new LatLng(staffDTO.lat, staffDTO.lng));
		opts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
		staffLayer = new OverlayViewLayer();
		mapView.addLayer(staffLayer);
		staffLayer.addItemObj(maker, opts);
	} 
	 
	/**
	 * Xoa cac overlay tren map
	 * 
	 * @author: QuangVT
	 * @since: 3:51:56 PM Dec 5, 2013
	 * @return: void
	 * @throws:
	 */
	private void clearCustomerMarkerOnMap() {
		clearMapLayer(customerLayer);
		clearMapLayer(staffLayer);
		clearMapLayer(popupLayer);
	} 
	 
	/**
	 * Hien thi marker toa do hien tai
	 * 
	 * @author: QuangVT
	 * @since: 3:52:18 PM Dec 5, 2013
	 * @return: void
	 * @throws:  
	 * @param lat
	 * @param lng
	 * @param index
	 * @param isOrWithSelectedDay
	 * @param drawable
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
	 
	/**
	 * Hien thi toa do nvbh so voi khach hang & khoang cach chenh lech
	 * @author: QuangVT
	 * @since: 3:53:17 PM Dec 5, 2013
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @param index
	 * @param isOrWithSelectedDay
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

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_POS_GSNPP_NVBH:
			nvbhInfo = (ListStaffDTO) modelEvent.getModelData();
			getCustomerListForSupervision();
			break;
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_ROUTE:
			ArrayList<Object> data = (ArrayList<Object>) modelEvent.getModelData();
			cusDto = (CustomerListDTO) data.get(0);
			GeomDTO nvbh = (GeomDTO) data.get(1);
			staffDTO.lat = nvbh.lat;
			staffDTO.lng = nvbh.lng;
			drawCustomerLocation();
			parent.closeProgressDialog();
			break; 
		default:
			break;
		}
		super.handleModelViewEvent(modelEvent);
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
		case CustomerPopupView.POP_UP_CLOSE:
			clearMapLayer(popupLayer);
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	} 
	 
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getCustomerListForSupervision();
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
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
		RelativeLayout.LayoutParams lpImageInfo = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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
