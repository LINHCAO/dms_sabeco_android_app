/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.CustomerPositionLogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.sqllite.db.SQLUtils;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.training.TrainingListView;
import com.viettel.dms.view.tnpg.customer.CustomerListViewPG;
import com.viettel.map.FragmentMapView;
import com.viettel.map.OverlayViewItemObj;
import com.viettel.map.OverlayViewLayer;
import com.viettel.map.OverlayViewOptions;
import com.viettel.map.view.MarkerMapView;
import com.viettel.maps.base.LatLng;
import com.viettel.maps.services.AdminItem;
import com.viettel.maps.services.AdminLevelType;
import com.viettel.maps.services.AdminService;
import com.viettel.maps.services.AdminServiceResult;
import com.viettel.maps.services.ServiceStatus;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

import java.util.ArrayList;

/**
 * man hinh cap nhat vi tri khach hang
 * 
 * @author : BangHN since : 1.0 version : 1.0
 */
public class CustomerLocationUpdateView extends FragmentMapView implements OnTouchListener, AdapterView.OnItemSelectedListener {
	public static final String TAG = CustomerLocationUpdateView.class.getName();

	public static final int MENU_LIST_FEEDBACKS = 11;
	public static final int MENU_MAP = 12;
	public static final int MENU_IMAGE = 13;
	public static final int MENU_INFO_DETAIL = 14;
	public static final int MENU_REVIEW = 15;

	private static final int TAB_INDEX = 4;
	Bundle savedInstanceState;
	// thong tin khach hang tu man hinh khac gui toi
	CustomerDTO customerInfo;
	CustomerLocationUpdateHeaderView header;
	MarkerMapView maker;
	boolean isViewPosition = false;// chi xem ko cap nhat
	// toa do khach hang
	double cusLat, cusLng;
	// toa do nhan vien
	double staffLat, staffLng; 
	// timer show dinh vi khi chua co vi tri
	CountDownTimer timer = null;
	// vi tri nhan vien
	MarkerMapView makerPositionStaff;
	CreateCustomerInfoDTO dtoDiaBan = new CreateCustomerInfoDTO();
	boolean isFirstInitView = false;
	OverlayViewOptions markerOpts = new OverlayViewOptions();
	MarkerMapView marker;
	OverlayViewLayer markerLayer;
	private String tagFrom;
	private LatLng lastRequestLatLng;
	private boolean isGetDiaBan;

	public static CustomerLocationUpdateView newInstance(Bundle data) {
		CustomerLocationUpdateView f = new CustomerLocationUpdateView();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		customerInfo = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
		// doc lai tu DB, truong hop sau cap nhat vi tri chuyen sang tab khac ko
		// notify duoc cap nhat vi tri
		customerInfo = SQLUtils.getInstance().getCustomerById(customerInfo.getCustomerId());
		tagFrom= getArguments().getString(IntentConstants.INTENT_SENDER, Constants.STR_BLANK);
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
		header = new CustomerLocationUpdateHeaderView(getActivity());
		header.setOnTouchListener(this);
		isGetDiaBan = false;
		// enable menu bar
//		int typeUser = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_POSITION), R.drawable.icon_map, MENU_MAP);
		addMenuItem(StringUtil.getString(R.string.TEXT_PICTURE), R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(StringUtil.getString(R.string.TEXT_FEEDBACK), R.drawable.icon_reminders, MENU_LIST_FEEDBACKS);
		addMenuItem(StringUtil.getString(R.string.TEXT_INFO), R.drawable.icon_detail, MENU_INFO_DETAIL, View.INVISIBLE);
		setMenuItemFocus(1);   
		
		setTitleHeaderView(getFullTitleString());
		
		initData();
		setCustomerLocationHeader();

		// vi tri can update tren ban do
		// MarkerOptions markerOpts = new MarkerOptions();
		// Bitmap icon = BitmapFactory.decodeResource(parent.getResources(),
		// R.drawable.icon_flag, null);
		marker = new MarkerMapView(getActivity(), R.drawable.icon_flag);
		markerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_LEFT);
		markerLayer = new OverlayViewLayer();
		mapView.addLayer(markerLayer);
		markerLayer.addItemObj(marker, markerOpts);
		showCustomerPositionMarker();
		return v;
	}

	/**
	 * Khoi tao du lieu ban do
	 * 
	 * @author banghn
	 */
	private void initData() {
		if (customerInfo != null) {
			cusLat = customerInfo.getLat();
			cusLng = customerInfo.getLng();
		}

		if (cusLat > 0 && cusLng > 0) {
			mapView.moveTo(new LatLng(cusLat, cusLng));
			isViewPosition = true;
		} else {
			isViewPosition = false;
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();

			if (lat > 0 && lng > 0) {
				mapView.moveTo(new LatLng(lat, lng));
			} else {
				mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
			}

			parent.restartLocating(true);
		}
	}

	/**
	 * Hien thi header thong tin khach hang tren ban do
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void setCustomerLocationHeader() {
		if (!isFirstInitView) {
			LinearLayout llHeader;
			llHeader = new LinearLayout(mapView.getContext());

			llHeader.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			llHeader.setOrientation(LinearLayout.HORIZONTAL);

			header = new CustomerLocationUpdateHeaderView(getActivity());
			header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			header.setOnTouchListener(this);
			header.setCustomerInfo(customerInfo);

			llHeader.addView(header);
			llHeader.setGravity(Gravity.CENTER | Gravity.TOP);

			header.spinnerDistrict.setOnItemSelectedListener(this);
			header.spinnerPrecinct.setOnItemSelectedListener(this);
			header.spinnerProvine.setOnItemSelectedListener(this);
			header.btUpdate.setOnClickListener(this);
			rlMainMapView.addView(llHeader);
			isFirstInitView = true;
		}
		mapView.invalidate();
	}

	/**
	 * Hien thi popup avatar khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void showCustomerPositionMarker() {
		if (cusLat > 0 && cusLng > 0 && isViewPosition) {
			// Bitmap icon = BitmapFactory.decodeResource(parent.getResources(),
			// R.drawable.icon_map_position_blue, null);
			markerOpts.position(new LatLng(cusLat, cusLng));
			markerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			marker.setIconMarker(R.drawable.icon_map_position_blue);
			mapView.invalidate();
		}else{
			markerOpts.position(new LatLng(cusLat, cusLng));
			markerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
			marker.setIconMarker(R.drawable.icon_flag);
			mapView.invalidate();
		}
	}

	/**
	 * Hien thi marker toa do hien tai
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void showFlagMarkerInMap(double lat, double lng) {
		markerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_LEFT);
		markerOpts.position(new LatLng(lat, lng));
		mapView.invalidate();

		if(!isViewPosition && customerInfo.getAreaId() == 0){
			suggestAdressCustomer(new LatLng(lat, lng));
		}
	}

	private void suggestAdressCustomer(LatLng latlng) {
		lastRequestLatLng = latlng;
		AdminService adSer = new AdminService();
		adSer.setLevelType(AdminLevelType.COMMUNE);
		adSer.getFeature(latlng, new AdminServiceListenerEx(latlng) {

			@Override
			public void onAdminServiceCompleted(AdminServiceResult result) {
				parent.closeProgressDialog();
				//phai kiem tra request danh cho lat lng hien tai, tranh request nhieu lan lay sai lat lng
				if (lastRequestLatLng.equals(getLocRequest())) {
					boolean isRequestDiaBanWhenFail = false;
					if (result.getStatus() == ServiceStatus.OK) {
						if (result.getItemSize() > 0) {
							AdminItem adminItem = result.getItem(0);
							parent.showToastMessage(StringUtil.getString(R.string.TEXT_REQUEST_DIA_BAN_OK) + " " + adminItem.getName() + " (" + adminItem.getCode() + ")");
							requestInitAreaData(adminItem.getCode());
							isGetDiaBan = true;
						} else{
							isRequestDiaBanWhenFail = true;
							parent.showToastMessage(StringUtil.getString(R.string.TEXT_NOT_FOUND_DIA_BAN));
						}
					} else {
						isRequestDiaBanWhenFail = true;
						parent.showToastMessage(StringUtil.getString(R.string.TEXT_REQUEST_DIA_BAN_FAIL));
					}

					//chua lay dia ban thi lay dia ban cho nguoi dung chon
					if (isRequestDiaBanWhenFail && !isGetDiaBan){
						requestInitAreaData("");
					}
				}
			}
		});
	}

	/**
	 * gotoFeedBackList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void gotoFeedBackList() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customerInfo);
		bundle.putString(IntentConstants.INTENT_SENDER, tagFrom);
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_CUS_FEED_BACK;
		SaleController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * di toi man hinh chi tiet Khach hang
	 * 
	 * @author : BangHN since : 10:59:39 AM
	 */
	public void gotoCustomerInfo(String customerId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bunde.putString(IntentConstants.INTENT_SENDER, tagFrom);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * update thong tin user (update lat, lng)
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void updateCustomerLocationToDB() {
		// kiem tra truong hop bo qua check dinh vi
		if (cusLat <= 0 || cusLng <= 0) {
			parent.showDialog(StringUtil.getString(R.string.MESSAGE_CANT_UPDATE_CUS_LOCATION));
		} else if ((customerInfo != null && customerInfo.getAreaId() == 0) && (dtoDiaBan == null || dtoDiaBan.curentIdPrecinct <= 0)) {
			parent.showDialog(StringUtil.getString(R.string.TEXT_UPDATE_CUSTOMER_LOC_NOT_DIA_BAN));
		} else{
			parent.showProgressDialog(StringUtil.getString(R.string.loading));
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.UPDATE_CUSTOMER_LOATION;
			customerInfo.setLng(cusLng);
			customerInfo.setLat(cusLat);
			customerInfo.setUpdateUser(GlobalInfo.getInstance().getProfile().getUserData().userCode);
			customerInfo.setUpdateDate(DateUtils.now());

			// du lieu insert bang position log
			ArrayList<CustomerPositionLogDTO> log = new ArrayList<CustomerPositionLogDTO>();
			CustomerPositionLogDTO logPosition = new CustomerPositionLogDTO();
			logPosition.createDate = DateUtils.now();
			logPosition.customerId = customerInfo.customerId;
			logPosition.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			logPosition.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			logPosition.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			log.add(logPosition);

			if(customerInfo.getAreaId() == 0 && dtoDiaBan != null) {
				customerInfo.setAreaId((int) dtoDiaBan.curentIdPrecinct);
			}

			e.viewData = customerInfo;
			e.userData = log;
			e.sender = this;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.UPDATE_CUSTOMER_LOATION:
			parent.showDialog(StringUtil.getString(R.string.TEXT_UPDATE_LOCATION_SUCCESS));
			isViewPosition = true;
			customerInfo.setLat(cusLat);
			customerInfo.setLat(cusLng);
			header.setCustomerInfo(customerInfo);
			showCustomerPositionMarker();
			mapView.invalidate();
			break;
		case ActionEventConstant.GET_CREATE_CUSTOMER_AREA_INFO:{
				ArrayList<CreateCustomerInfoDTO.AreaItem> arrayArea = (ArrayList<CreateCustomerInfoDTO.AreaItem>) modelEvent.getModelData();
				int type = ((Bundle)modelEvent.getActionEvent().viewData).getInt(IntentConstants.INTENT_AREA_TYPE);
				this.setArrayAreaInfo(type, arrayArea);
				break;
			}
		case ActionEventConstant.GET_AREA_CUSTOMER_INFO: {
			dtoDiaBan = (CreateCustomerInfoDTO) modelEvent.getModelData();
			renderDiaBan();
		}
			break;
		default:
			break;
		}
		super.handleModelViewEvent(modelEvent);
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
		if (cusLat > 0 && cusLng > 0) {
			updateCustomerLocationToDB();
		} else {
			parent.showDialog(StringUtil.getString(R.string.MESSAGE_CANT_UPDATE_CUS_LOCATION));
		}
		super.onClick(v);
	}

	@Override
	public void onClickMarkerMyPositionEvent() {
		cusLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		cusLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		// show la co vi tri moi
		if (!isViewPosition) {
			showFlagMarkerInMap(cusLat, cusLng);
		}
		// mapView.moveTo(new LatLng(cusLat, cusLng));
		// drawMarkerMyPosition();
		super.onClickMarkerMyPositionEvent();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case MENU_LIST_FEEDBACKS: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoFeedBackList();
		}
			break;
		case MENU_INFO_DETAIL: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerInfo(customerInfo.getCustomerId());
		}
			break;
		case MENU_IMAGE: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoListAlbumUser(customerInfo);
		}
			break;
//		case MENU_REVIEW:
//			GlobalUtil.popBackStack(this.getActivity());
//			gotoReviewsStaffView();
//			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * Di toi man hinh danh gia nhan vien Danh cho module gs npp
	 * 
	 * @author banghn
	 */
	public void gotoReviewsStaffView() {
		Bundle data = new Bundle();
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW;
		event.sender = this;
		event.viewData = data;
		SuperviorController.getInstance().handleSwitchFragment(event);
	}

	/**
	 * Toi man hinh ds album cua kh
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param customer
	 * @return: void
	 * @throws:
	 */

	private void gotoListAlbumUser(CustomerDTO customer) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		bundle.putString(IntentConstants.INTENT_SENDER, tagFrom);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		
		int typeUser = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		if(typeUser == UserDTO.TYPE_VALSALES || typeUser == UserDTO.TYPE_PRESALES){
			SaleController.getInstance().handleSwitchFragment(e); 
		}else{
			TNPGController.getInstance().handleSwitchFragment(e);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			// tranh truong hop notify nhieu lan
			if (this.cusLat > 0 && this.cusLng > 0) {
				return;
			}
			parent.closeProgressDialog();
			VTLog.i("Location", "Map: location changed: (lat-lng) "
					+ GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude() + " - "
					+ GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude());
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			if (lat < 0 && lng < 0) {
				//parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_CANT_LOCATE_YOUR_POSITION));
				return;
			} else {
				cusLat = lat;
				cusLng = lng;
				mapView.moveTo(new LatLng(lat, lng));
				showFlagMarkerInMap(lat, lng);
				drawMarkerMyPosition();
			}
			break;
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isFirstInitView = false;
				customerInfo = SQLUtils.getInstance().getCustomerById(customerInfo.getCustomerId());
				initData();
				setCustomerLocationHeader();
				showCustomerPositionMarker();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v == header) {
			// khong xu ly
			return true;
		}
		return false;
	}

	@Override
	public boolean onSingleTap(LatLng latlng, Point point) {
		if (!isViewPosition) {
			this.cusLat = latlng.getLatitude();
			this.cusLng = latlng.getLongitude();
			showFlagMarkerInMap(latlng.getLatitude(), latlng.getLongitude());
		}
		return true;
	}

	@Override
	protected int getTabIndex() { 
		return TAB_INDEX;
	}
	
	@Override
	protected String getTitleString() {  
		return StringUtil.getString(R.string.TEXT_HEADER_TITLE_LOCATION_UPDATE_DEFAULT);
	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View v, int pos, long id) {
	 	if (spinner == header.spinnerDistrict) {
			if (this.dtoDiaBan.currentIndexDistrict != pos) {
				this.dtoDiaBan.setCurrentDistrict(pos);
				//request lại danh sách xã
				this.requestDataArea(CUSTOMER_TABLE.AREA_TYPE_PRECINCT, this.dtoDiaBan.curentIdDistrict);
			}
		} else if (spinner == header.spinnerPrecinct) {
			if (this.dtoDiaBan.currentIndexPrecinct != pos) {
				this.dtoDiaBan.setCurrentPrecinct(pos);
			}
		} else if (spinner == header.spinnerProvine) {
			if (this.dtoDiaBan.currentIndexProvince != pos) {
				this.dtoDiaBan.setCurrentProvince(pos);
				//request lại danh sách huyện
				this.requestDataArea(CUSTOMER_TABLE.AREA_TYPE_DISTRICT, this.dtoDiaBan.curentIdProvine);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> adapterView) {

	}

	/**
	 * Hiển thị danh sách huyện
	 * @author: duongdt3
	 * @since: 09:01:10 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void renderListDicstrict(){
		// Danh sách huyện
		if(dtoDiaBan.listDistrict != null){
			int sizeDistrict = dtoDiaBan.listDistrict.size();
			String[] arrDistrict = new String[sizeDistrict];

			for (int i = 0; i < sizeDistrict; i++) {
				CreateCustomerInfoDTO.AreaItem item = dtoDiaBan.listDistrict.get(i);
				arrDistrict[i] = item.areaName;
			}
			SpinnerAdapter adapterDistrict = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrDistrict);
			header.spinnerDistrict.setAdapter(adapterDistrict);
		}
	}


	void renderListProvince(){
		// Danh sách tỉnh
		if(dtoDiaBan.listProvine != null){
			int sizeProvine = dtoDiaBan.listProvine.size();
			String[] arrProvine = new String[sizeProvine];

			for (int i = 0; i < sizeProvine; i++) {
				CreateCustomerInfoDTO.AreaItem item = dtoDiaBan.listProvine.get(i);
				arrProvine[i] = item.areaName;
			}
			SpinnerAdapter adapterProvine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrProvine);
			header.spinnerProvine.setAdapter(adapterProvine);

		}
	}

	/**
	 * Hiển thị danh sách xã
	 * @author: duongdt3
	 * @since: 09:01:35 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void renderListPrecinct(){
		// Danh sách xã
		if(dtoDiaBan.listPrecinct != null){
			int sizePrecinct = dtoDiaBan.listPrecinct.size();
			String[] arrPrecinct = new String[sizePrecinct];

			for (int i = 0; i < sizePrecinct; i++) {
				CreateCustomerInfoDTO.AreaItem item = dtoDiaBan.listPrecinct.get(i);
				arrPrecinct[i] = item.areaName;
			}
			SpinnerAdapter adapterPrecinct = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPrecinct);
			header.spinnerPrecinct.setAdapter(adapterPrecinct);
		}
	}

	/**
	 * lấy danh sách dữ liệu địa bàn với id địa bàn cha + loại địa bàn
	 * @author: duongdt3
	 * @since: 15:55:46 6 Jan 2014
	 * @return: void
	 * @throws:
	 * @param type
	 * @param parentAreaId
	 */
	protected void requestDataArea(int type, long parentAreaId) {
		parent.showLoadingDialog();
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putInt(IntentConstants.INTENT_AREA_TYPE, type);
		data.putLong(IntentConstants.INTENT_PARRENT_AREA_ID, parentAreaId);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_CREATE_CUSTOMER_AREA_INFO;
		// send request
		SaleController.getInstance().handleViewEvent(e);
	}

	public void setArrayAreaInfo(int type, ArrayList<CreateCustomerInfoDTO.AreaItem> arrayArea) {
		switch (type) {
			case CUSTOMER_TABLE.AREA_TYPE_PRECINCT:
				//list xã
				dtoDiaBan.listPrecinct = arrayArea;
				dtoDiaBan.setCurrentPrecinct(-1);
				renderListPrecinct();
				break;
			case CUSTOMER_TABLE.AREA_TYPE_DISTRICT:
				//list huyện
				dtoDiaBan.listDistrict = arrayArea;
				dtoDiaBan.setCurrentDistrict(-1);
				renderListDicstrict();
				break;
			default:
				break;
		}
	}

	private void requestInitAreaData(String areaCode) {
		parent.showLoadingDialog();
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_AREA_CODE, areaCode);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_AREA_CUSTOMER_INFO;
		// send request
		SaleController.getInstance().handleViewEvent(e);
	}

	public void renderDiaBan() {
		// Danh sách tỉnh
		renderListProvince();
		header.spinnerProvine.setSelection(dtoDiaBan.currentIndexProvince);

		// Danh sách huyện
		renderListDicstrict();
		header.spinnerDistrict.setSelection(dtoDiaBan.currentIndexDistrict);

		// Danh sách xã
		renderListPrecinct();
		header.spinnerPrecinct.setSelection(dtoDiaBan.currentIndexPrecinct);
	}
}

