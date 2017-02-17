/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO.AreaItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.GlobalBaseActivity;
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

/**
 * Màn hình tạo mới khách hàng 
 * CreateCustomerView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  08:33:49 6 Jan 2014
 */
public class CreateCustomerView extends FragmentMapView implements OnTouchListener {
	public static final String TAG = CreateCustomerView.class.getName();
	
	public static final int REQUEST_EDIT_CUSTOMER = -1;
	public static final int REQUEST_CREATE_CUSTOMER = -2;

	private static final int ACTION_INSERT_CUSTOMER = -1;

	private static final int ACTION_UPDATE_CUSTOMER = -2;

	private static final int ACTION_CANCEL_CUSTOMER = -3;

	private static final int REQUEST_VIEW_CUSTOMER = -4;
	
	Bundle savedInstanceState;
	// thong tin khach hang tu man hinh khac gui toi
	CreateCustomerInfoDTO dto = null;
	//CustomerDTO customerInfo = null;
	CreateCustomerHeaderView header;
	MarkerMapView maker;
	private int typeRequest = REQUEST_CREATE_CUSTOMER;
	// toa do khach hang
	double cusLat = -1, cusLng = -1;
	// toa do nhan vien
	double staffLat, staffLng; 
	// vi tri nhan vien
	MarkerMapView makerPositionStaff;

	OverlayViewOptions markerCustomerOpts = new OverlayViewOptions();
	MarkerMapView markerCustomer;
	OverlayViewLayer markerCustomerLayer;

	//user info
	// user id
	private String userId;
	// shopID
	private String shopId;
	// customer id
	private String customerId;
	// user name
	private String userName;

	private LatLng lastRequestLatLng;
	private boolean isGetDiaBan;

	public static CreateCustomerView newInstance(Bundle data) {
		CreateCustomerView f = new CreateCustomerView();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);		
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.savedInstanceState = savedInstanceState;
		View v = super.onCreateView(inflater, container, savedInstanceState);
		isGetDiaBan = false;
		initUserInfo();
		initHeaderText();
		initHeaderMenu();
		initData();
		
		return v;
	}
	
	/**
	 * lấy thông tin nhân viên hiện đang đăng nhập + thông tin màn hình khác gởi qua
	 * @author: duongdt3
	 * @since: 09:05:05 6 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void initUserInfo() {
		userId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
		userName = GlobalInfo.getInstance().getProfile().getUserData().userName;
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		
		
		customerId = getArguments().getString(IntentConstants.INTENT_CUSTOMER_ID, "");
		//có truyển qua ID => cần cập nhật, mặc
		if (!StringUtil.isNullOrEmpty(customerId)) {
			boolean isEdit = getArguments().getBoolean(IntentConstants.INTENT_IS_EDIT, false);
			if (isEdit) {
				typeRequest = REQUEST_EDIT_CUSTOMER;
			}else{
				typeRequest = REQUEST_VIEW_CUSTOMER;
			}
		}

		if (typeRequest != REQUEST_VIEW_CUSTOMER){
			restartLocatingUpdate();
		}
	}

	/**
	 * show header text
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */	
	private void initHeaderText() {
		// TODO Auto-generated method stub
		String title = StringUtil.getString(R.string.TITLE_VIEW_CREATE_CUSTOMER);
		setTitleHeaderView(title);
	}
	
	/**
	 * init header menu
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	private void initHeaderMenu() {
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.NVBH_CUSTOMER_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_NEW_CUSTOMER_LIST);
	}
	
	/**
	 * Go to list khách hàng chưa duyệt
	 * @author: duongdt3
	 * @since: 20:28:45 7 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void goToNewCustomerList(){
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		e.viewData = b;
		e.sender = this;
		e.action = ActionEventConstant.GET_NEW_CUSTOMER_LIST;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * init data
	 * @author: duongdt3
	 * @since: 16:44:08 4 Jan 2014
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings("unchecked")
	private void initData() {		
		// vi tri can update tren ban do
		markerCustomer = new MarkerMapView(getActivity(), R.drawable.icon_flag);
		markerCustomer.setVisibility(View.GONE);
		markerCustomerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_LEFT);
		markerCustomerLayer = new OverlayViewLayer();
		mapView.addLayer(markerCustomerLayer);		
		markerCustomerLayer.addItemObj(markerCustomer, markerCustomerOpts);
		showFlagMarkerInMap(cusLat, cusLng);
		drawMarkerMyPosition();
		
		setCustomerLocationHeader();
		//request data
		requestData();
	}

	/**
	 * get data on db
	 * @author: duongdt3
	 * @since: 09:07:03 6 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void requestData() {
		parent.showLoadingDialog();
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, userId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_CREATE_CUSTOMER_INFO;
		// send request
		SaleController.getInstance().handleViewEvent(e);
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
	
	/**
	 * Hien thi header thong tin khach hang tren ban do
	 * @author: duongdt3
	 * @since: 16:44:22 4 Jan 2014
	 * @return: void
	 * @throws:
	 */
	private void setCustomerLocationHeader() {
		if (header == null) {
			//nếu ko phải là View mode thì cho chỉnh sửa
			header = new CreateCustomerHeaderView(getActivity(), (typeRequest !=  REQUEST_VIEW_CUSTOMER));
			header.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			header.setOnTouchListener(this);
			header.setParrent(this);
			
			header.btSave.setOnClickListener(this);
			this.setHeaderView(header);
		}
	}

	/**
	 * Hien thi marker khach hang
	 * @author: duongdt3
	 * @since: 16:44:29 4 Jan 2014
	 * @return: void
	 * @throws:
	 */
	private void showCustomerPositionMarker() {
		markerCustomerOpts.position(new LatLng(cusLat, cusLng));
		markerCustomerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_CENTER);
		markerCustomer.setIconMarker(R.drawable.icon_flag);
		mapView.invalidate();
	}

	/**
	 * Hien thi marker toa do hien tai
	 * @author: duongdt3
	 * @since: 16:44:40 4 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param lat
	 * @param lng
	 */
	private void showFlagMarkerInMap(double lat, double lng) {
		markerCustomerOpts.drawMode(OverlayViewItemObj.DRAW_BOTTOM_LEFT);
		markerCustomerOpts.position(new LatLng(lat, lng));
		//chỉ hiện khi có vị trí
		if (lat > 0 && lng > 0) {
			//hiển thị marker của khách hàng
			markerCustomer.setVisibility(View.VISIBLE);
		}else{
			//không có vị trí thì ẩn
			markerCustomer.setVisibility(View.GONE);
		}
		mapView.invalidate();
	}

    /**
     * Cập nhât thông tin khách hàng 
     * @author: duongdt3
     * @since: 17:17:34 4 Jan 2014
     * @return: void
     * @throws:
     */
	private void sendRequestInsertCustomerToDB() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.CREATE_CUSTOMER;		
		Bundle data = new Bundle();
		data.putSerializable(IntentConstants.INTENT_CUSTOMER, dto.cusInfo);
		e.viewData = data;
		e.sender = this;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}
	

	/**
	 * cập nhật thông tin khách hàng
	 * @author: duongdt3
	 * @since: 14:37:10 7 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void sendRequestUpdateCustomerToDB() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.UPDATE_CUSTOMER;		
		Bundle data = new Bundle();
		data.putSerializable(IntentConstants.INTENT_CUSTOMER, dto.cusInfo);
		e.viewData = data;
		e.sender = this;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}
	

	/**
	 * Hỏi khi tạo khách hàng
	 * @author: duongdt3
	 * @param info 
	 * @since: 14:41:34 7 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void confirmCreateCustomer() {
		GlobalUtil.showDialogConfirm(this, this.parent, StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_CREATE_CUSTOMER), StringUtil.getString(R.string.TEXT_AGREE),
				ACTION_INSERT_CUSTOMER, StringUtil.getString(R.string.TEXT_DENY),
				ACTION_CANCEL_CUSTOMER, null);
	}
	
	/**
	 * Hỏi khi cập nhật khách hàng
	 * @author: duongdt3
	 * @param info 
	 * @since: 14:41:34 7 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void confirmUpdateCustomer() {
		GlobalUtil.showDialogConfirm(this, this.parent, StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_UPDATE_CUSTOMER), StringUtil.getString(R.string.TEXT_AGREE),
				ACTION_UPDATE_CUSTOMER, StringUtil.getString(R.string.TEXT_DENY),
				ACTION_CANCEL_CUSTOMER, null);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
			case ActionEventConstant.GET_AREA_CUSTOMER_INFO_NEW_CUSTOMER: {
				dto = (CreateCustomerInfoDTO) modelEvent.getModelData();
				if (dto != null){
					header.updateDiaBanCustomer(dto);
				}
			}
			break;
    		case ActionEventConstant.GET_CREATE_CUSTOMER_INFO:{
    			dto = (CreateCustomerInfoDTO) modelEvent.getModelData();
    			if (dto != null) {
    				if (typeRequest == REQUEST_CREATE_CUSTOMER) {
    					dto.cusInfo = new CustomerDTO();
    				}
    				
    				cusLat = dto.cusInfo.getLat();
					cusLng = dto.cusInfo.getLng();
					
    				showFlagMarkerInMap(cusLat, cusLng);
    				if (cusLat > 0 && cusLng > 0) {
    					mapView.moveTo(new LatLng(cusLat, cusLng));
    				} else {
    					double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
    					double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();

    					if (lat > 0 && lng > 0) {
    						mapView.moveTo(new LatLng(lat, lng));
    					} else {
    						mapView.moveTo(new LatLng(Constants.LAT_DMS_TOWNER, Constants.LNG_DMS_TOWNER));
    					}
    				}
    				header.setCustomerInfo(dto);
    			}
    			break;
    		}
    		case ActionEventConstant.GET_CREATE_CUSTOMER_AREA_INFO:{
    			ArrayList<AreaItem> arrayArea = (ArrayList<AreaItem>) modelEvent.getModelData();
    			int type = ((Bundle)modelEvent.getActionEvent().viewData).getInt(IntentConstants.INTENT_AREA_TYPE);
    			header.setArrayAreaInfo(type, arrayArea);
    			break;
    		}
    		case ActionEventConstant.UPDATE_CUSTOMER:{
    			parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_UPDATE_CUSTOMER_SUCCESS));
    			goToNewCustomerList();
    			break;
    		}
    		case ActionEventConstant.CREATE_CUSTOMER:{
				if(modelEvent.getModelData() != null && !modelEvent.getModelData().equals(0)) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_CREATE_CUSTOMER_SUCCESS));
				} else {
					parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_CREATE_CUSTOMER_SUCCESS_NOT_ROUTE));
				}
    			goToNewCustomerList();
    			break;
    		}	
    		default:
    			super.handleModelViewEvent(modelEvent);
    			break;
		}

		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		//case ActionEventConstant.GET_CREATE_CUSTOMER_INFO
		//case ActionEventConstant.GET_CREATE_CUSTOMER_AREA_INFO

		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}
	
	/**
	 * Gởi yêu cầu thêm, cập nhật
	 * @author: duongdt3
	 * @since: 17:25:29 7 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void sendRequestCreateOrUpdateWhenVailInput(){
		//cập nhật giá trị các control trong header view vào dto
		header.getInfoFromView();
		//cập nhật vị trí
		dto.cusInfo.setLat(cusLat);
		dto.cusInfo.setLng(cusLng);
		String dateNow = DateUtils.now();
		
		if (typeRequest == REQUEST_CREATE_CUSTOMER) {
			//tạo thì thêm người tạo, ngày tạo, shop_id của NHBH hiện tại, status = 2 => chờ xử lý
			dto.cusInfo.setShopId(shopId);
			dto.cusInfo.setStaffId(Long.valueOf(userId));
			dto.cusInfo.setCreateDate(dateNow);
			dto.cusInfo.setCreateUser(userName);
			dto.cusInfo.setStatus(CUSTOMER_TABLE.STATE_CUSTOMER_WAIT_APPROVED);
			
			//send request to DB
			sendRequestInsertCustomerToDB();
		}else if (typeRequest == REQUEST_EDIT_CUSTOMER) {
			//sửa thì thêm người sửa, ngày sửa
			dto.cusInfo.setUpdateDate(dateNow);
			dto.cusInfo.setUpdateUser(userName);
			
			//send request to DB
			sendRequestUpdateCustomerToDB();
		}
	}

	/**
	 * Check & Update customer to DB
	 * @author: duongdt3
	 * @since: 10:10:08 7 Jan 2014
	 * @return: void
	 * @throws:
	 */
	void updateCustomerToDB(){
		//check điều kiện
		boolean isVaild = header.checkInputInfo();
		if (isVaild) {
			//tạm thời ko cần quan tâm tới vị trí
			if (typeRequest == REQUEST_CREATE_CUSTOMER) {
				confirmCreateCustomer();
			}else if (typeRequest == REQUEST_EDIT_CUSTOMER) {
				confirmUpdateCustomer();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == header.btSave) {
			updateCustomerToDB();
		}
		super.onClick(v);
	}

/*	@Override
	public void onClickMarkerMyPositionEvent() {
		cusLat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		cusLng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		// show la co vi tri moi
		//showFlagMarkerInMap(cusLat, cusLng);
		//mapView.moveTo(new LatLng(cusLat, cusLng));
		//super.onClickMarkerMyPositionEvent();
	}*/

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_INSERT_CUSTOMER:
		case ACTION_UPDATE_CUSTOMER:{
			sendRequestCreateOrUpdateWhenVailInput();
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * Di toi man hinh danh gia nhan vien Danh cho module gs npp
	 * 
	 * @author banghn
	 * @param item
	 */
	public void gotoReviewsStaffView() {
		Bundle data = new Bundle();
		ActionEvent event = new ActionEvent();
		event.action = ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW;
		event.sender = this;
		event.viewData = data;
		SuperviorController.getInstance().handleSwitchFragment(event);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				this.cusLat = -1;
				this.cusLng = -1;
				initData();
				//setCustomerLocationHeader();
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
		//nếu đang xem thi ko cho chọn vị trí 
		if (typeRequest != REQUEST_VIEW_CUSTOMER) {
			this.cusLat = latlng.getLatitude();
			this.cusLng = latlng.getLongitude();

			suggestAdressCustomer(new LatLng(this.cusLat, this.cusLng));
			showFlagMarkerInMap(latlng.getLatitude(), latlng.getLongitude());
		}
		return true;
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

	private void requestInitAreaData(String areaCode) {
		parent.showLoadingDialog();
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putSerializable(IntentConstants.INTENT_DATA, dto);
		data.putString(IntentConstants.INTENT_AREA_CODE, areaCode);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_AREA_CUSTOMER_INFO_NEW_CUSTOMER;
		// send request
		SaleController.getInstance().handleViewEvent(e);
	}

}
