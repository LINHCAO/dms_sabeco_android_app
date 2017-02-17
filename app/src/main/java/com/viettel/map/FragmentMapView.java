/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.viettel.dms.dto.db.TabletActionLogDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ServerLogger;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.AnimationImageView;
import com.viettel.dms.view.control.AnimationLinearLayout;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.map.view.MarkerMapView;
import com.viettel.maps.MapView;
import com.viettel.maps.MapView.MapEventListener;
import com.viettel.maps.base.LatLng;
import com.viettel.maps.base.LatLngBounds;
import com.viettel.maps.controls.BaseControl;
import com.viettel.maps.controls.MapTypeControl;
import com.viettel.maps.controls.ZoomControl;
import com.viettel.maps.layers.MapLayer;
import com.viettel.maps.layers.VectorLayer;
import com.viettel.maps.objects.CircleOptions;
import com.viettel.maps.services.AdminService;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Lop base ban do tren fragment
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class FragmentMapView extends BaseFragment implements
		OnEventControlListener, MapEventListener {
	final LatLng center = new LatLng(10.784829, 106.68307);
	public LinearLayout layoutZoomControl;
	// activity chua ban do
	public GlobalBaseActivity parent;
	// background map view
	public View bgMapView;
	// view chinh chua ban do & cac marker, control
	public RelativeLayout rlMainMapView;

	// hien thi button dinh vi vi tri cua toi
	protected boolean isViewMyPositionControl = true;
	public MapView mapView;
	public ZoomControl zoomControl;
	// Marker myPosMarker;
	// MarkerLayer myPosMarkerLayer;
	MapTypeControl mapTypeControl;
	OverlayViewLayer myPosLayer;
	VectorLayer myPosVectorLayer;
	AnimationImageView ivHideShowHeader = null;
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) initMap(savedInstanceState);
		return super.onCreateView(inflater, view, savedInstanceState);
	}

	/**
	 * Set header for mapview
	 * @author: duongdt3
	 * @since: 09:27:25 9 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param header
	 */
	protected void setHeaderView(View header){
		llHeader.addView(header);
		//add control hide header
		initHideHeaderControl();
		
		ivHideShowHeader.setVisibility(View.GONE);
		//hiện header lên
		llHeader.setVisibility(View.VISIBLE);
		ivHideShowHeader.setImageResource(R.drawable.icon_go_up);
		ivHideShowHeader.setVisibility(View.VISIBLE);
		mapView.invalidate();
	}
	
	AnimationLinearLayout llHeader;
	/**
	 * Khoi tao ban do Viettel Map
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private ViewGroup initMap(Bundle savedInstanceState) {
		bgMapView = new View(parent);
		bgMapView.setBackgroundColor(Color.rgb(237, 234, 226));
		bgMapView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		rlMainMapView = new RelativeLayout(parent);
		rlMainMapView.setLayoutParams(new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		rlMainMapView.addView(bgMapView);
		
		//add map
		mapView = new MapView(parent);
		// mapView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
		// LayoutParams.FILL_PARENT));
		// mapView.setMapKey("TTUDCNTT_KEY_TEST");
		mapView.setMapKey(GlobalInfo.VIETTEL_MAP_KEY);
		mapView.setZoomControlEnabled(true);
		mapView.setMapTypeControlEnabled(false);
		mapView.moveTo(center);
		mapView.setMapEventListener(this);
		rlMainMapView.addView(mapView);

		zoomControl = (ZoomControl) mapView
				.getControl(BaseControl.TYPE_CONTROL_ZOOM);
		if (zoomControl != null) {
			mapView.removeView(zoomControl);
			RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.FILL_PARENT,
					RelativeLayout.LayoutParams.FILL_PARENT);
			zoomControl.setLayoutParams(lp);
			zoomControl.setGravity(Gravity.LEFT | Gravity.BOTTOM);
			rlMainMapView.addView(zoomControl);
		}

		if (isViewMyPositionControl) {
			initMyPositionControl();
		}
		
		//duongdt add header linear layout
		llHeader = new AnimationLinearLayout(parent);
		llHeader.setId(R.id.table);
		
		llHeader.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		llHeader.setOrientation(LinearLayout.HORIZONTAL);
		llHeader.setGravity(Gravity.CENTER | Gravity.TOP);
		llHeader.setVisibility(View.GONE);
		rlMainMapView.addView(llHeader);

		//init google maps button
		ImageButton googleMap = new ImageButton(parent);
		googleMap.setBackgroundColor(Color.TRANSPARENT);
		googleMap.setImageResource(R.drawable.ico_google_map);
		googleMap.setFocusable(true);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// Align bottom-right, and add bottom-margin
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		params.bottomMargin = GlobalUtil.dip2Pixel(-3);
		params.rightMargin = GlobalUtil.dip2Pixel(55);
		googleMap.setLayoutParams(params);
		googleMap.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
				double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
				GlobalUtil.launchGoogleMapApp(parent, lat, lng, 16);
			}
		});
		rlMainMapView.addView(googleMap);
		return rlMainMapView;
	}

	/**
	 * Ve button hien thi vi tri cua toi
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public void initMyPositionControl() {
		LinearLayout llMyPosition;
		llMyPosition = new LinearLayout(mapView.getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		lp.setMargins(0, 0, GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5));
		llMyPosition.setLayoutParams(lp);
		llMyPosition.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
		llMyPosition.setOrientation(LinearLayout.VERTICAL);

		ImageView ivMyPosition = new ImageView(parent);
		ivMyPosition.setLayoutParams(new LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		ivMyPosition.setPadding(GlobalUtil.dip2Pixel(5),
				GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5),
				GlobalUtil.dip2Pixel(5));
		ivMyPosition.setImageResource(R.drawable.icon_location_2);
		ivMyPosition.setBackgroundResource(R.drawable.custom_button_with_border);
		ivMyPosition.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickMarkerMyPositionEvent();
			}
		});
		llMyPosition.addView(ivMyPosition);
		rlMainMapView.addView(llMyPosition);
	}
	
	/**
	 * Thêm control ẩn header trên map
	 * @author: duongdt3
	 * @since: 09:15:56 9 Jan 2014
	 * @return: void
	 * @throws:
	 */
	public void initHideHeaderControl() {
		LinearLayout llMyPosition;
		llMyPosition = new LinearLayout(mapView.getContext());
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		////nối với llHeader
		lp.addRule(RelativeLayout.ALIGN_BOTTOM, llHeader.getId());
		lp.setMargins(0, GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5), 0);
		llMyPosition.setLayoutParams(lp);
		//llMyPosition.setGravity(Gravity.RIGHT | Gravity.TOP);
		llMyPosition.setGravity(Gravity.RIGHT);
		
		llMyPosition.setOrientation(LinearLayout.VERTICAL);

		ivHideShowHeader = new AnimationImageView(parent);
		ivHideShowHeader.setLayoutParams(new LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		ivHideShowHeader.setPadding(GlobalUtil.dip2Pixel(5),
				GlobalUtil.dip2Pixel(5), GlobalUtil.dip2Pixel(5),
				GlobalUtil.dip2Pixel(5));
		ivHideShowHeader.setImageResource(R.drawable.icon_go_down);
		//ivHideShowHeader.setBackgroundResource(R.drawable.custom_button_with_border);
		ivHideShowHeader.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onClickMarkerHideHeaderEvent();
			}
		});
		llMyPosition.addView(ivHideShowHeader);
		rlMainMapView.addView(llMyPosition);
	}

	/**
	 * Ve marker vi tri cua toi hien tai
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	@SuppressWarnings("unchecked")
	protected void drawMarkerMyPosition() {
		clearMapLayer(myPosLayer);
		clearMapLayerVector(myPosVectorLayer);

		if (GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude() > 0
				&& GlobalInfo.getInstance().getProfile().getMyGPSInfo()
						.getLongtitude() > 0) {
			LatLng latLng = new com.viettel.maps.base.LatLng(GlobalInfo
					.getInstance().getProfile().getMyGPSInfo().getLatitude(),
					GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());
			drawMarkerMyPosition(latLng, GlobalInfo.getInstance().getProfile().getMyGPSInfo().accuracy);
		} else {
			parent.showDialog(StringUtil
					.getString(R.string.TEXT_ALERT_CANT_LOCATE_YOUR_POSITION));
		}
	}

	private void clearMapLayerVector(VectorLayer layer) {
		if (layer != null) {
			layer.clear();
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
	 * Ve marker vi tri cua toi hien tai
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	@SuppressWarnings("unchecked")
	protected void drawMarkerMyPosition(LatLng myLocation, double radius) {
//		VTLog.i("yen", "ve1" + DateUtils.now()+ "pos" + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
//				+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());
		clearMapLayer(myPosLayer);
		clearMapLayerVector(myPosVectorLayer);

		if (radius > 0) {
			//add vector layer
			myPosVectorLayer = new VectorLayer();
			CircleOptions circleOp = new CircleOptions();
			circleOp.center(myLocation).radius(radius).strokeWidth(1).strokeColor(Color.argb(60, 0, 128, 255)).fillColor(Color.argb(40, 0, 128, 255));
			mapView.addLayer(myPosVectorLayer);
			myPosVectorLayer.addCircle(circleOp);
		}

		MarkerMapView maker = new MarkerMapView(parent,
				R.drawable.icon_location);
		OverlayViewOptions opts = new OverlayViewOptions();
		opts.position(myLocation);
		opts.drawMode(OverlayViewItemObj.DRAW_CENTER);
		myPosLayer = new OverlayViewLayer();
		mapView.addLayer(myPosLayer);
		myPosLayer.addItemObj(maker, opts);
		if (GlobalInfo.getInstance().isSendLogPosition()) {
			ServerLogger.sendLogLogin("Log ghi thời gian định vị - FragmentMapView - drawMarkerMyPosition", "Vẽ lại icon vị trí - ngày: "
					+ DateUtils.now() + "-lat,lng: " + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude()
					+ "," + GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude(), TabletActionLogDTO.LOG_LOCATION);
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	public void clearMapLayer(OverlayViewLayer layer) {
		if (layer != null) {
			for (int i = 0; i < layer.getMapObjectTotal(); i++) {
				OverlayViewItemObj obj = (OverlayViewItemObj) layer
						.getMapObject(i);
				mapView.removeView(obj.getView());
			}
			layer.clear();
		}
	}

	/**
	 * Xu ly fit overlay
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public void fitBounds(ArrayList<LatLng> arrayPosition) {
		LatLngBounds bound = null;
		for (int i = 0, size = arrayPosition.size(); i <size; i++) {
			//duongdt update clone, fixbug thay doi arrayPosition sau getBound
			com.viettel.maps.base.LatLng latlng = arrayPosition.get(i).clone();
			bound = getBound(bound, latlng);
		}

		mapView.fitBounds(bound, true);
	}

	/**
	 * Xu ly fit overlay cho item
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public LatLngBounds getBound(LatLngBounds bound, LatLng latLng) {
		if (bound == null) {
			bound = new LatLngBounds();
			bound.setSouthWest(latLng);
			bound.setNorthEast(latLng);
		} else {
			int lat = latLng.getLatitudeE6();
			int lng = latLng.getLongitudeE6();
			if (bound.getSouthWest().getLatitudeE6() > lat)
				bound.getSouthWest().setLatitudeE6(lat);
			if (bound.getNorthEast().getLatitudeE6() < lat)
				bound.getNorthEast().setLatitudeE6(lat);
			if (bound.getSouthWest().getLongitudeE6() > lng)
				bound.getSouthWest().setLongitudeE6(lng);
			if (bound.getNorthEast().getLongitudeE6() < lng)
				bound.getNorthEast().setLongitudeE6(lng);
		}
		return bound;
	}

	/**
	 * Xu ly khi click len button vi tri cua toi
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	public void onClickMarkerMyPositionEvent() {
		drawMarkerMyPosition();
		moveToCenterMyPosition();
	}
	
	public void onClickMarkerHideHeaderEvent() {
		//ẩn hiện header
		if (llHeader.getVisibility() == View.VISIBLE) {
			ivHideShowHeader.setVisibility(View.GONE);
			llHeader.setVisibility(View.GONE);
			ivHideShowHeader.setImageResource(R.drawable.icon_go_down);
			ivHideShowHeader.setBackgroundResource(R.drawable.custom_button_with_border);
			ivHideShowHeader.setVisibility(View.VISIBLE);
		}else if (llHeader.getVisibility() == View.GONE){
			ivHideShowHeader.setVisibility(View.GONE);
			llHeader.setVisibility(View.VISIBLE);
			ivHideShowHeader.setImageResource(R.drawable.icon_go_up);
			ivHideShowHeader.setBackgroundDrawable(null);
			ivHideShowHeader.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public boolean onSingleTap(LatLng latlng, Point point) {
		VTLog.d("GIS", "onSingleTapUp : " + latlng.toString());
		return false;
	}

	@Override
	public boolean onLongTap(LatLng latlng, Point point) {
		return false;
	}

	@Override
	public boolean onDoubleTap(LatLng latlng, Point point) {
		VTLog.d("GIS", "onSingleTapUp : " + latlng.toString());
		return false;
	}

	@Override
	public void onCenterChanged() {
	}

	@Override
	public void onBoundChanged() {
	}

	protected abstract class AdminServiceListenerEx implements AdminService.AdminServiceListener{
		private LatLng locRequest;
		public AdminServiceListenerEx(LatLng mlocRequest) {
			this.locRequest = mlocRequest;
		}

		public LatLng getLocRequest() {
			return locRequest;
		}

		public void setLocRequest(LatLng locRequest) {
			this.locRequest = locRequest;
		}

		@Override
		public void onAdminServicePreProcess(AdminService arg0) {
			parent.showProgressDialog(StringUtil.getString(R.string.TEXT_GET_LOCATION_INFO), false);
		}
	}
}

