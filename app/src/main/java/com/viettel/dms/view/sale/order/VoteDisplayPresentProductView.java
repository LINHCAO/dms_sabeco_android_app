/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.io.File;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDTO;
import com.viettel.dms.dto.db.ProDisplayProgrameDetailDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.DisplayPresentProductInfo;
import com.viettel.dms.dto.view.VoteDisplayPresentProductViewDTO;
import com.viettel.dms.dto.view.VoteDisplayProductDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.LoginView;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.sale.customer.CustomerListView;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

 
/**
 * Cham chuong trinh trung bay cua khach hang
 * VoteDisplayPresentProductView.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  14:14:18 13-05-2014
 */
public class VoteDisplayPresentProductView extends BaseFragment implements OnEventControlListener, OnClickListener, OnItemSelectedListener,VinamilkTableListener {
	// tag for fragment
	public static final String TAG = VoteDisplayPresentProductView.class.getName();
	// back action
	public static final int BACK_ACTION = 0;
	public final int SAVE_VOTE_DISPLAY_ACTION = 1;
	public final int CANCEL_SAVE_VOTE_DISPLAY_ACTION = 2;

	// action chup hinh trung bay sau khi cham CT
	public final int ACTION_AGREE_TAKE_IMAGE_DP = 3;
	public final int ACTION_DEPLAY_TAKE_IMAGE_DP = 4;
		
	// table list product
	DMSTableView tbProductPromotionList;
	// control select programe code
	Spinner spPromotionProgrameCode; 
	// programe name
	TextView tvPromotionProgrameName;
	// number product
	TextView tvNumberProduct;
	// button save
	Button btSave;
	// get main activity
	private SalesPersonActivity parent;
	// number item on page of table
	public static final int LIMIT_ROW_PER_PAGE = 10;
	// the first load product list
	boolean isFirstLoadProduct = false;
	// change programe code
	boolean isChangeDisplayProgramCode = false; 
	// customer id
	public CustomerListItem customerListObject;

	// current diplay programe code
	String displayProgramCode;
	// current diplay programe id
	String displayProgramId; 
	// list display program to vote
	public VoteDisplayPresentProductViewDTO modelViewData = new VoteDisplayPresentProductViewDTO();
	private DisplayPresentProductInfo presentProduct;
	// current promotion selected
	int currentSelected = -1;
	
	// Switch dat hay khong
	private ToggleButton toggleButtonAttain;

	int page = 0;
	String startTime = Constants.STR_BLANK;
	boolean isLoadListPrograme = false;
	boolean callRemoveMenuClose = false;
	// current display programe id
	String currentDisplayProgrameID = Constants.STR_BLANK;  
	//file chup anh trung bay
	File takenPhoto;
	private boolean isVoted = false;
	
	public static VoteDisplayPresentProductView getInstance(Bundle data) {
		VoteDisplayPresentProductView instance = new VoteDisplayPresentProductView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onAttach(Activity activity) { 
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) { 
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Layout
		int idLayout = R.layout.layout_vote_display_present_product_view;
		ViewGroup view = (ViewGroup) inflater.inflate(idLayout, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		// Set title
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_VOTE_DISPLAY_PROGRAME));

		// Binding control
		initView(v);
		this.customerListObject = (CustomerListItem) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM);
		this.isFirstLoadProduct = true;
		
		// Neu load lan dau tien -> chua co danh sach chuong trinh
		if (!this.isLoadListPrograme) { 
			// Lay danh sach chuong trinh cua khach hang
			this.getVoteDisplayProgrameProductView();
		}
		// Da co danh sach chuong trinh 
		else {
			this.currentSelected = -1;
			this.renderDisplayProgramInfo();
		}
		this.startTime = DateUtils.now();
		return v;
	}
 
	/**
	 * 
	 * init view
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		tbProductPromotionList = (DMSTableView) v.findViewById(R.id.tbProductPromotionList); 
		tbProductPromotionList.setNumItemsPage(LIMIT_ROW_PER_PAGE);
		spPromotionProgrameCode = (Spinner) v.findViewById(R.id.spPromotionProgrameCode);
		spPromotionProgrameCode.setOnItemSelectedListener(this); 
		tvNumberProduct = (TextView) v.findViewById(R.id.tvNumberProduct);
		tvPromotionProgrameName = (TextView) v.findViewById(R.id.tvPromotionProgrameName);
		btSave = (Button) v.findViewById(R.id.btSave);
		btSave.setOnClickListener(this);
		
		toggleButtonAttain = (ToggleButton) v.findViewById(R.id.toggleButtonAttain);
		toggleButtonAttain.setOnCheckedChangeListener(new OnCheckedChangeListener() {  

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
		    	isVoted = isChecked;  
		    }
		});
	} 
	
	/**
	 * Tao adapter va render du lieu cho combobox dnah sach chuong trinh
	 * @author: quangvt1
	 * @since: 14:57:58 09-05-2014
	 * @return: void
	 * @throws:
	 */
	private void renderDisplayProgramInfo() {
		String[] arrPromotion = new String[this.modelViewData.listDisplayProgrameInfo.size()];
		for (int i = 0, size = this.modelViewData.listDisplayProgrameInfo.size(); i < size; i++) {
			arrPromotion[i] = this.modelViewData.listDisplayProgrameInfo.get(i).displayProgramCode;
		}
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPromotion);
		this.spPromotionProgrameCode.setAdapter(adapterLine);
	} 
	 
	/**
	 * 
	 * render layout after get data from db
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (isFirstLoadProduct) {
			initHeaderTable(tbProductPromotionList, new VoteDisplayPresentProductRow(parent, this));
		} else if (isChangeDisplayProgramCode) {
		}
		int pos = (tbProductPromotionList.getPagingControl().getCurrentPage() - 1) * LIMIT_ROW_PER_PAGE + 1;
		tbProductPromotionList.clearAllData();
		if (this.presentProduct.listProductDisplay.size() > 0) {
			for (VoteDisplayProductDTO dto : presentProduct.listProductDisplay) {
				VoteDisplayPresentProductRow row = new VoteDisplayPresentProductRow(parent, this);
				row.setClickable(true);
				row.setTag(Integer.valueOf(pos));
				row.renderLayout(pos, dto); 
				pos++;
				tbProductPromotionList.addRow(row);
			}
		} else {
			tbProductPromotionList.showNoContentRow();
		}

	} 
	
	/**
	 * Lay danh sach chuong trinh
	 * @author: quangvt1
	 * @since: 13:38:17 09-05-2014
	 * @return: void
	 * @throws:
	 */
	private void getVoteDisplayProgrameProductView() {
		// Show dialog loading...
		if (!parent.isShowProgressDialog()) {
			this.parent.showProgressDialog(getString(R.string.loading));
		}
		
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		String staffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id); 
		// Id khach hang hien tai
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, this.customerListObject.aCustomer.getCustomerId());
		// id nhan vien
		data.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		// shopid cua nhan vien - hien tai chua dung toi
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_VOTE_DISPLAY_PROGRAME_VIEW;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}
 
	/**
	 * Lay thong tin trung bay cua 1 chuong trinh
	 * @author: quangvt1
	 * @since: 13:38:38 09-05-2014
	 * @return: void
	 * @throws:  
	 */
	private void getListVoteDisplayProductDTO() {
		if(!isValidSelectProgram()){
			return;
		}
		parent.showProgressDialog(getString(R.string.loading));
		
		DisplayPresentProductInfo currentDisplayPrograme = getCurrentDisplayPrograme();
		displayProgramCode = currentDisplayPrograme.displayProgramCode;
		displayProgramId = currentDisplayPrograme.displayProgrameID;

		// Data truyen xuong
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle(); 
		data.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, this.displayProgramCode);
		data.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_ID, this.displayProgramId);
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, this.customerListObject.aCustomer.getCustomerId()); 

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_VOTE_DISPLAY_PRODUCT;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Kiem tra gia tri select chuong trinh voi danh sach chuong trinh co hop len khong
	 * Dung de kiem tra truong hop select ben ngoai danh sach
	 * @author: quangvt1
	 * @since: 16:33:00 09-05-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private boolean isValidSelectProgram() { 
		int count = this.modelViewData.listDisplayProgrameInfo.size();
		return (currentSelected >= 0 && currentSelected < count && count > 0);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) { 
		// Close dialog loading...
		this.parent.closeProgressDialog();
		
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_VOTE_DISPLAY_PROGRAME_VIEW: {
			this.modelViewData = (VoteDisplayPresentProductViewDTO) modelEvent.getModelData();
			if (this.modelViewData != null && this.modelViewData.listDisplayProgrameInfo.size() > 0) { 

				// Da load xong danh sach chuong trinh
				this.isLoadListPrograme = true;
				
				// Do du lieu len combobox
				renderDisplayProgramInfo(); 
				currentSelected = -1;
				spPromotionProgrameCode.setSelection(0);

				// Cap nhat ten chuong trinh
				updateProgramNameOnBar();  
			}else{
				gotoNextView();
			}
			 
			break;
		}
		case ActionEventConstant.GET_LIST_VOTE_DISPLAY_PRODUCT: {  
			presentProduct = (DisplayPresentProductInfo) modelEvent.getModelData();  
			this.renderLayoutListProduct();
			break;
		}
		case ActionEventConstant.SAVE_VOTE_DISPLAY_PRESENT_PRODUCT: { 
			
			// Luu action log cham trung bay
			saveActionLogVoteDisplay();
			
			// chup hinh trung bay
			DisplayPresentProductInfo currentDisplayPrograme = getCurrentDisplayPrograme();
			processTakeImageDisplayPrograme(currentDisplayPrograme);  
			
			//remove menu dong cua
			if (!this.callRemoveMenuClose) {
				parent.removeMenuCloseCustomer();
				this.callRemoveMenuClose = true;
			} 
			requestInsertLogKPI(HashMapKPI.NVBH_CHAMTRUNGBAY, actionEvent.startTimeFromBoot);
			break;
		}
		case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
			parent.closeProgressDialog();
			continueAfterVoteDP();
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * Luu action log sau khi cham trung bay thanh cong
	 * @author: quangvt1
	 * @since: 14:34:39 13-05-2014
	 * @return: void
	 * @throws:
	 */
	private void saveActionLogVoteDisplay() {
		ActionLogDTO action = new ActionLogDTO();
		action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		action.aCustomer.customerId = this.customerListObject.aCustomer.customerId;
		action.objectId = this.currentDisplayProgrameID;
		action.objectType = "2";
		action.startTime = this.startTime;
		action.isOr = this.customerListObject.isOr;
		parent.requestInsertActionLog(action);
	}

	/**
	 * Cap nhat lai ten chuong trinh dang select
	 * @author: quangvt1
	 * @since: 14:56:45 09-05-2014
	 * @return: void
	 * @throws:
	 */
	private void updateProgramNameOnBar() {
		if(isValidSelectProgram()){
			DisplayPresentProductInfo currentDisplayPrograme = getCurrentDisplayPrograme();
			tvPromotionProgrameName.setText(currentDisplayPrograme.displayProgramName);
		}
	}  
	
	/**
	 * Xu ly chup hinh cham trung bay
	 * @author: BANGHN
	 */
	private void processTakeImageDisplayPrograme(DisplayPresentProductInfo dp) {
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		boolean isChecked = sharedPreferences.getBoolean(LoginView.DMS_IGNORE_MSG_TAKE_PICTURE_DP, false);
		if(!isChecked){
			View checkBoxView = View.inflate(parent, R.layout.checkbox, null);
			CheckBox checkBox = (CheckBox) checkBoxView.findViewById(R.id.checkbox);
			checkBox.setText(StringUtil.getString(R.string.TEXT_IGNORE_MSG_LATER));
			checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					updateSetting(isChecked);
				}
			});
	
			SpannableObject textConfirmed = new SpannableObject();
			textConfirmed.addSpan(StringUtil
					.getString(R.string.NOTICE_TAKE_IMAGE_DISPLAY_PROGRAM_1),
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
	
			textConfirmed.addSpan(dp.displayProgramCode + " ",
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(StringUtil.getString(R.string.OF),
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
			textConfirmed.addSpan(
					" "
							+ this.customerListObject.aCustomer.customerCode
									.substring(0, 3) + "-"
							+ this.customerListObject.aCustomer.customerName,
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.BOLD);
			textConfirmed.addSpan(StringUtil
					.getString(R.string.NOTICE_TAKE_IMAGE_DISPLAY_PROGRAM_2),
					ImageUtil.getColor(R.color.WHITE),
					android.graphics.Typeface.NORMAL);
	
			GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, checkBoxView,
					StringUtil.getString(R.string.TEXT_TAKE_PHOTO_DP), textConfirmed.getSpan(),
					StringUtil.getString(R.string.TEXT_AGREE),
					ACTION_AGREE_TAKE_IMAGE_DP, null, -1, dp, false, false);
		}else{
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO_VOTE_DP);
		}
	} 
	
	/**
	 *  Tiep tuc luon cham trung bay sau khi chup anh trung bay
	 * @author: quangvt1
	 * @since: 14:36:50 13-05-2014
	 * @return: void
	 * @throws:
	 */
	private void continueAfterVoteDP(){ 
		// Remove chuong trinh vua cham xong
		if (this.modelViewData.listDisplayProgrameInfo.size() > 0) {  
			this.modelViewData.listDisplayProgrameInfo.remove(currentSelected);
		}
					
		// Cham cac chuong trinh con lai, neu het thi qua man hinh ke tiep
		if (this.modelViewData.listDisplayProgrameInfo.size() > 0) {
			currentSelected = -1;
			this.renderDisplayProgramInfo();
			this.toggleButtonAttain.setChecked(false);
			this.isVoted = false;
		} else {
			gotoNextView();
		}
	}
	
	
	/**
	 * Upload hinh chup cham trung bay
	 * @author: BANGHN
	 */
	private void updateTakenPhoto(){
		// upload
		DisplayPresentProductInfo curentDP = modelViewData.listDisplayProgrameInfo
				.get(this.spPromotionProgrameCode
						.getSelectedItemPosition());
		
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		Vector<String> viewData = new Vector<String>();

//		viewData.add(IntentConstants.INTENT_STAFF_ID);
//		viewData.add(Integer.toString(GlobalInfo.getInstance().getProfile()
//				.getUserData().id));
//
//		viewData.add(IntentConstants.INTENT_SHOP_ID);
//		if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile()
//				.getUserData().shopId)) {
//			viewData.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
//		}

		if (takenPhoto != null) {
			viewData.add(IntentConstants.INTENT_FILE_NAME);
			viewData.add(takenPhoto.getName());
			viewData.add(IntentConstants.INTENT_TAKEN_PHOTO);
			viewData.add(takenPhoto.getAbsolutePath());
		}

		// tao doi tuong mediaItem de insert row du lieu
		MediaItemDTO dto = new MediaItemDTO();

		try {
			dto.objectId = customerListObject.aCustomer.customerId;
			dto.objectType = MediaItemDTO.TYPE_DISPLAY_PROGAME_IMAGE;
			dto.mediaType = 0; // loai hinh anh , 1 loai video
			dto.url = takenPhoto.getAbsolutePath();
			dto.thumbUrl = takenPhoto.getAbsolutePath();
			dto.createDate = DateUtils.now();
			dto.createUser = GlobalInfo.getInstance().getProfile()
					.getUserData().userCode;
			dto.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLatitude();
			dto.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo()
					.getLongtitude();
			dto.displayProgrameId = Long.parseLong(curentDP.displayProgrameID);
			dto.fileSize = takenPhoto.length();
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			dto.type = 1;
			dto.status = 1;
			if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile()
					.getUserData().shopId)) {
				dto.shopId = Integer.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
			}
			viewData.add(IntentConstants.INTENT_OBJECT_ID);
			viewData.add(String.valueOf(dto.objectId));
			viewData.add(IntentConstants.INTENT_OBJECT_TYPE_IMAGE);
			viewData.add(String.valueOf(dto.objectType));
			viewData.add(IntentConstants.INTENT_MEDIA_TYPE);
			viewData.add(String.valueOf(dto.mediaType));
			viewData.add(IntentConstants.INTENT_URL);
			viewData.add(String.valueOf(dto.url));
			viewData.add(IntentConstants.INTENT_THUMB_URL);
			viewData.add(String.valueOf(dto.thumbUrl));
			viewData.add(IntentConstants.INTENT_CREATE_DATE);
			viewData.add(String.valueOf(dto.createDate));
			viewData.add(IntentConstants.INTENT_CREATE_USER);
			viewData.add(String.valueOf(dto.createUser));
			viewData.add(IntentConstants.INTENT_LAT);
			viewData.add(String.valueOf(dto.lat));
			viewData.add(IntentConstants.INTENT_LNG);
			viewData.add(String.valueOf(dto.lng));
			viewData.add(IntentConstants.INTENT_FILE_SIZE);
			viewData.add(String.valueOf(dto.fileSize));
			
			viewData.add(IntentConstants.INTENT_DISPLAY_PROGRAM_ID);
			viewData.add(curentDP.displayProgrameID);
			viewData.add(IntentConstants.INTENT_CUSTOMER_CODE);
			viewData.add(this.customerListObject.aCustomer.customerCode);
			viewData.add(IntentConstants.INTENT_STATUS);
			viewData.add("1");
			viewData.add(IntentConstants.INTENT_TYPE);
			viewData.add("1");
		} catch (NumberFormatException e1) {  
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.UPLOAD_PHOTO_TO_SERVER;
		e.viewData = viewData;
		e.userData = dto;
		e.sender = this;
		UserController.getInstance().handleViewEvent(e);
	}
	
	/**
	 * Update setting bo qua thong bao chup anh trung bay lan sau
	 * @author: BANGHN
	 * @param isIgnore : bo qua?
	 */
	private void updateSetting(boolean isIgnore){
		SharedPreferences sharedPreferences = GlobalInfo.getInstance().getDmsPrivateSharePreference();
		Editor prefsPrivateEditor = sharedPreferences.edit();
		prefsPrivateEditor.putBoolean(LoginView.DMS_IGNORE_MSG_TAKE_PICTURE_DP, isIgnore);
		prefsPrivateEditor.commit();
	}

	/**
	 * 
	 * render layout cho d/s san pham cua chuong trinh trung bay
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutListProduct() {  
		renderLayout();
		if (isChangeDisplayProgramCode) {
			isChangeDisplayProgramCode = false;
		}
		if (isFirstLoadProduct) {
			isFirstLoadProduct = false;
		}
		this.btSave.setEnabled(true);
	}

	/**
	 * 
	 * lay index cua chuong trinh trung bay tiep theo
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getNextSelectedDisplay() {
		int kq = -1;
		for (int i = 0, size = this.modelViewData.listDisplayProgrameInfo.size(); i < size; i++) {
			if (!this.modelViewData.listDisplayProgrameInfo.get(i).isVoted) {
				kq = i;
				break;
			}
		}
		return kq;
	}

	/**
	 * 
	 * hien thi man hinh kiem tra hang hon
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void gotoNextView() {
		CustomerListView frag = (CustomerListView) parent.getFragmentManager().findFragmentByTag(CustomerListView.TAG);
		if (frag != null) {
			frag.isBackFromPopStack = true;
		}
		GlobalUtil.popBackStack(this.getActivity());
		if (customerListObject.isHaveSaleOrder && !customerListObject.isTodayCheckedRemain) {
			gotoRemainProductView(
					// neu co don dat hang & chua dc kiem hang ton
					this.customerListObject.aCustomer.getCustomerId(),
					this.customerListObject.aCustomer.getCustomerCode(),
					this.customerListObject.aCustomer.getCustomerName(), this.customerListObject.aCustomer.getStreet(),
					this.customerListObject.aCustomer.getCustomerTypeId());
		} else {
			gotoCreateOrder(this.customerListObject);
		}
	}

	/**
	 * 
	 * go to remain product view
	 * 
	 * @author: HaiTC3
	 * @param customerId
	 * @param customerCode
	 * @param customerName
	 * @return: void
	 * @throws:
	 */
	private void gotoRemainProductView(String customerId, String customerCode, String customerName,
			String customerAddress, int customerTypeId) {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		b.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCode);
		b.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerName);
		b.putString(IntentConstants.INTENT_CUSTOMER_ADDRESS, customerAddress);
		b.putString(IntentConstants.INTENT_IS_OR, String.valueOf(this.customerListObject.isOr));
		b.putInt(IntentConstants.INTENT_CUSTOMER_TYPE_ID, customerTypeId);
		e.viewData = b;
		e.action = ActionEventConstant.GO_TO_REMAIN_PRODUCT_VIEW;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * go to create order view
	 * 
	 * @author: HaiTC3
	 * @param dto
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
	 * 
	 * validate vote number user input
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean validateVoteNumber() {
		boolean kq = true;
		if (this.modelViewData != null
				&& this.presentProduct != null
				&& this.presentProduct.listProductDisplay.size() > 0) {
			for (int i = 0, size = tbProductPromotionList.getListChildRow().size(); i < size; i++) {
				VoteDisplayPresentProductRow row = (VoteDisplayPresentProductRow) tbProductPromotionList.getListChildRow().get(i);
				String voteNumber = row.etVote.getText().toString();

				if (!StringUtil.isNullOrEmpty(voteNumber)) {
					if (!StringUtil.isValidateIntergeNonNegativeInput(voteNumber)) {
						kq = false;
						row.etVote.requestFocus();
						break;
					}
				}
			}
		}
		return kq;
	}
 
	/**
	 * Kiem tra cac gia tri cham trung bay cua nguoi dung nhap
	 * @author: quangvt1
	 * @since: 13:40:23 13-05-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	public boolean isValidInputBeforeSave() {
		boolean isValid = true;
		
		// Kiem tra cac gia tri nhap
		if (!validateVoteNumber()) {
			GlobalUtil.showDialogConfirm(this.parent, getString(R.string.TEXT_NOTIFY_INPUT_VOTE_NUMBER_INCORRECT),
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, Constants.STR_BLANK, 0, null);
			isValid = false;
		} 
		
		return isValid;
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent actionEvent = modelEvent.getActionEvent();
		switch (actionEvent.action) {
		case ActionEventConstant.SAVE_VOTE_DISPLAY_PRESENT_PRODUCT:
			this.btSave.setEnabled(true); 
			break;
		case ActionEventConstant.GET_LIST_VOTE_DISPLAY_PRODUCT: 
			this.renderLayout();
			break;
		case ActionEventConstant.GET_VOTE_DISPLAY_PROGRAME_VIEW:
			parent.closeProgressDialog();
			break;
		case ActionEventConstant.ACTION_GET_CURRENT_DATE_TIME_SERVER:
			parent.closeProgressDialog();
			this.btSave.setEnabled(true);
			super.handleErrorModelViewEvent(modelEvent);
			break;
		case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER:
			parent.closeProgressDialog();
			continueAfterVoteDP();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	} 
	
	/**
	 * Luu thong tin cham trung bay
	 * @author: quangvt1
	 * @since: 15:43:21 12-05-2014
	 * @return: void
	 * @throws:
	 */
	public void requestSaveVoteDisplay() { 
		DisplayPresentProductInfo currentDisplayPrograme = getCurrentDisplayPrograme();
		this.currentDisplayProgrameID = currentDisplayPrograme.displayProgrameID;
		
		this.parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		Bundle data = getDataToSaveVoteDisplay();

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.SAVE_VOTE_DISPLAY_PRESENT_PRODUCT;
		e.isNeedCheckTimeServer = true;
		this.btSave.setEnabled(false);
		SaleController.getInstance().handleViewEvent(e); 
	}

	/**
	 * Lay chuong trinh dang duoc chon
	 * @author: quangvt1
	 * @since: 14:32:51 13-05-2014
	 * @return: DisplayPresentProductInfo
	 * @throws:  
	 * @return
	 */
	private DisplayPresentProductInfo getCurrentDisplayPrograme() {
		DisplayPresentProductInfo currentDisplayPrograme = this.modelViewData.listDisplayProgrameInfo.get(currentSelected);
		return currentDisplayPrograme;
	}

	/**
	 * Thong tin de luu khi cham TB
	 * @author: quangvt1
	 * @since: 15:32:35 12-05-2014
	 * @return: Bundle
	 * @throws:  
	 * @return
	 */
	private Bundle getDataToSaveVoteDisplay() { 
		UserDTO user 	 = GlobalInfo.getInstance().getProfile().getUserData();
		String staffId 	 = String.valueOf(user.id);
		String staffCode = String.valueOf(user.userCode);
		String dateNow 	 = DateUtils.now(); 
		
		// Thong tin chuong trinh
		ProDisplayProgrameDTO programe = new ProDisplayProgrameDTO();
		programe.proInfoId 	= this.currentDisplayProgrameID;
		programe.customerId = this.customerListObject.aCustomer.getCustomerId();
		programe.staffId 	= staffId;
		programe.createUser = staffCode;
		programe.createDate = dateNow;
		programe.imageDisplay = (isVoted == true) ? 1 : 0;
		
		// Chi tiet tung san pham cua chuong trinh
		for (VoteDisplayProductDTO vote : this.presentProduct.listProductDisplay) {
			if(vote.voteNumber > 0){
				ProDisplayProgrameDetailDTO detail = new ProDisplayProgrameDetailDTO();
				detail.productId 	= vote.productID + "";
				detail.quantity 	= vote.voteNumber;
				detail.staffId 		= staffId; 
				detail.createUser 	= staffCode;
				detail.createDate 	= dateNow;
				
				programe.listDetail.add(detail);
			}
		}
		
		Bundle data = new Bundle();
		data.putSerializable(IntentConstants.INTENT_DISPLAY_PROGRAM_MODEL, programe); 
		
		return data;
	}
 
	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (eventType == SAVE_VOTE_DISPLAY_ACTION) {
			this.requestSaveVoteDisplay();
		} else if (eventType == CANCEL_SAVE_VOTE_DISPLAY_ACTION) {
			// don't do any thing
		} else if(eventType == ACTION_AGREE_TAKE_IMAGE_DP){ 
				takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO_VOTE_DP);
		}else if(eventType == ACTION_DEPLAY_TAKE_IMAGE_DP){
			continueAfterVoteDP();
		}
	} 

	@Override
	public void onClick(View v) { 
		if (v == btSave) {
			// Neu khong cham mat hang nao ca
			if(isDontHavAnyVote()){ 
				continueAfterVoteDP();
			}else{
				GlobalUtil.forceHideKeyboard(parent);
				if (this.isValidInputBeforeSave()) {
					GlobalUtil.showDialogConfirm(this, this.parent, getString(R.string.TEXT_MESSAGE_CONFIRM_SAVE_VOTE_DISPLAY),
							getString(R.string.TEXT_AGREE), this.SAVE_VOTE_DISPLAY_ACTION,
							getString(R.string.TEXT_BUTTON_CLOSE), this.CANCEL_SAVE_VOTE_DISPLAY_ACTION, null);
				}else{ 
					GlobalUtil.showDialogConfirm(this.parent, getString(R.string.TEXT_NOTIFY_PLEASE_INPUT_VOTE), StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0,
							Constants.STR_BLANK, 0, null);
				}
			}
		}
	}

	/**
	 * Kiem tra co cham mat hang nao khong
	 * @author: quangvt1
	 * @since: 18:47:35 09-05-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private boolean isDontHavAnyVote() { 
		boolean isVote = false;
		if(this.presentProduct != null){
			for (VoteDisplayProductDTO vote : this.presentProduct.listProductDisplay) {
				if(vote.voteNumber > 0){
					isVote = true;
					break;
				}
			}
		}
		return !isVote;
	}  

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (currentSelected != arg2 && this.isLoadListPrograme) {
			currentSelected = arg2;

			// cap nhat ten chuong trinh duoc chon
			updateProgramNameOnBar(); 

			isChangeDisplayProgramCode = true; 

			// lay danh sach cac san pham cua chuong trinh
			this.getListVoteDisplayProductDTO();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) { 

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String filePath = "";
		switch (requestCode) {
		case GlobalBaseActivity.RQ_TAKE_PHOTO_VOTE_DP:
			if (resultCode == Activity.RESULT_OK) {
				filePath = takenPhoto.getAbsolutePath();
				ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(parent, filePath,
						Constants.MAX_FULL_IMAGE_HEIGHT);
				validator.setDataIntent(data);
				if (validator.execute()) {
					VTLog.e("TakePhoto", ".................result taking photo : OK");
					updateTakenPhoto();
				}
			}else{
				// request code cancel
				parent.closeProgressDialog();
				
				//bat buoc phai chup hinh trung bay, ko cho huy
				DisplayPresentProductInfo object = modelViewData.listDisplayProgrameInfo
						.get(this.spPromotionProgrameCode.getSelectedItemPosition());
				// chup hinh trung bay
				processTakeImageDisplayPrograme(object);
				
				//continueAfterVoteDP();
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				currentSelected = -1;
				this.renderDisplayProgramInfo();
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.view.listener.VinamilkTableListener#handleVinamilkTableloadMore(android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.viettel.dms.view.listener.VinamilkTableListener#handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		
	}
}
