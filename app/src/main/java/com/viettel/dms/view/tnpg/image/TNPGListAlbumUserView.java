/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.image;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.commonsware.cwac.thumbnail.ThumbnailAdapter;
import com.commonsware.cwac.thumbnail.ThumbnailBus;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.ListAlbumUserDTO;
import com.viettel.dms.dto.view.StaffInfoDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ErrorConstants;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.ServerPath;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VNMStableGridView;
import com.viettel.dms.view.listener.OnImageTakingPopupListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.PopupChooseDisplayProgram;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

import java.io.File;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Man hinh chua cac album cua mot khach hang (Role TNPG)
 * 
 * @author: QuangVT
 * @version: 1.0 
 */
public class TNPGListAlbumUserView extends BaseFragment implements OnItemClickListener, OnClickListener,
	  OnImageTakingPopupListener {  
	public static final String TAG = TNPGListAlbumUserView.class.getName(); // Tag
	private GlobalBaseActivity parent; // parent

	// CONSTANT ACTION MENU
	public static final int MENU_LIST_FEEDBACKS = 11; 	// menu gop y
	public static final int MENU_MAP 			= 12;	// menu vi tri
	public static final int MENU_IMAGE 			= 13;	// menu hinh anh
	public static final int MENU_INFO_DETAIL 	= 14;	// menu thong tin khach hanh
	public static final int MENU_REVIEW 		= 15;	// menu danh gia

	// Loai album
	public static final int TYPES_ALBUM_USER[]    = {0, 1, 2, 4};
	// Ten album
	public static final String NAMES_ALBUM_USER[] = { "Hình ảnh đóng cửa",
			"Hình ảnh trưng bày", "Hình ảnh điểm bán",
			"Hình ảnh đối thủ cạnh tranh" };
		
	// ACTION DIALOG
	public static final int ACTION_CLOSE = 16;	// action close popup

	//ALBUM
	public static final int[] IMAGE_IDS = { R.id.imgAlbumImage }; // id Imagview hien hinh anh
	private static final int TAB_INDEX = 3;
	private ThumbnailAdapter thumbs = null;		// Adapter cho Thumbnail
	private ThumbnailBus bus;					// bus cho Thumbnail
	private VNMStableGridView gvUserImageView; 	// GV danh sach hinh anh
	private ListAlbumUserDTO model = new ListAlbumUserDTO();	// danh sach album

	// CONTROL
	private Button btTakePhoto;	// button chup hinh 
	private TextView tvKH;		// ma khach hang
	private TextView tvHoTenKH;	// ho ten
	private TextView tvNoDataResult; // dong the hien khong co ket qua
	private int ofAlbum; 		// thuoc album nao
	
	// File chup hinh
	File takenPhoto;
	// Dialog chup anh
	private AlertDialog popupTakeImage;
	
	// Thong tin
	private CustomerDTO customer;	// thong tin khach hang
	private String shopId;			// shopId
	private long staffId;			// staffId NV
	private String sender;

	/**
	 * Lay doi tuong ListAlbumUserView
	 * 
	 * @author: QuangVT1
	 * @param data
	 * @return
	 * @return: ListAlbumUserView
	 * @throws:
	 */ 
	public static TNPGListAlbumUserView getInstance(Bundle data) {
		TNPGListAlbumUserView instance = new TNPGListAlbumUserView();
		instance.setArguments(data);

		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity; 
		VTLog.i("QuangVT1", "fragment onAttach");
	}
	
	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		VTLog.i("QuangVT1", "fragment onActivityCreated");
	}
	
	/* (non-Javadoc)
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		super.onResume();
		VTLog.i("QuangVT1", "fragment onResume");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		VTLog.i("QuangVT1", "fragment onSaveInstanceState");
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onStart()
	 */
	@Override
	public void onStart() {
		super.onStart();
		VTLog.i("QuangVT1", "fragment onStart");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		VTLog.i("QuangVT1", "fragment onCreateView");
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tnpg_album_user_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		// Lay thong tin khach hang
		Bundle data = (Bundle) getArguments();
		customer = (CustomerDTO) data.getSerializable(IntentConstants.INTENT_CUSTOMER);
		shopId   = data.getString(IntentConstants.INTENT_SHOP_ID);
		if (StringUtil.isNullOrEmpty(shopId)) { 
			int typeUser = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
			if (typeUser == UserDTO.TYPE_PRESALES || typeUser == UserDTO.TYPE_VALSALES) {
				shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
			}else if(typeUser == UserDTO.TYPE_GSNPP){
				shopId = GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile;
			}else if(typeUser == UserDTO.TYPE_TNPG){
				shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
			}
		}
		 
		staffId = GlobalInfo.getInstance().getProfile().getUserData().id; 

		// Khoi tao MenuBar
		enableMenuBar(this);
		initMenuActionBar();

		// Binding control
		initView(view);

		// Khoi tao Title Header
		initTitleHeader(data);
		if(!StringUtil.isNullOrEmpty(customer.getCustomerCode())) {
			tvKH.setText(customer.getCustomerCode().substring(0, 3));
		}else{
			tvKH.setText("");
		}
		tvHoTenKH.setText(customer.getCustomerName());

		if (model.isFirstInit) {
			gvUserImageView.setAdapter(thumbs);
			thumbs.notifyDataSetChanged(); 
		} else { 
			model.setCustomer(customer);
			model.getListAlbum().clear();
			model.getListProgrameAlbum().clear();
			model.isFirstInit = true;
			initData();
			getListAlbumOfUser(false);
		}
		
		return v;
	}

	/**
	 * Khoi tao Title Header
	 * 
	 * @author quangvt1
	 * @param data
	 */
	private void initTitleHeader(Bundle data) {
		SpannableObject spanObject = new SpannableObject();
		spanObject.addSpan(getMenuIndexString());
		spanObject.addSpan(". ");
		spanObject.addSpan(getTitleString());
		spanObject.addSpan(" " + customer.getCustomerName(), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(spanObject);
	}

	/**
	 * Khoi tao menu cho man hinh
	 * 
	 * @author: QuangVT1
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		addMenuItem(StringUtil.getString(R.string.TEXT_POSITION), R.drawable.icon_map, MENU_MAP);
		addMenuItem(StringUtil.getString(R.string.TEXT_PICTURE), R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(StringUtil.getString(R.string.TEXT_FEEDBACK), R.drawable.icon_reminders, MENU_LIST_FEEDBACKS);
		addMenuItem(StringUtil.getString(R.string.TEXT_INFO), R.drawable.icon_detail, MENU_INFO_DETAIL, View.INVISIBLE);
		setMenuItemFocus(2);
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
	 * gotoFeedBackList
	 * 
	 * @author: QuangVT
	 * @since: 2:01:25 PM Dec 6, 2013
	 * @return: void
	 * @throws:
	 */
	private void gotoFeedBackList() {
		if (model.getCustomer() == null) {
			return;
		}
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, model.getCustomer());
		bundle.putString(IntentConstants.INTENT_SENDER, sender);
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_CUS_FEED_BACK;
		TNPGController.getInstance().handleSwitchFragment(e);
	} 
	
	/**
	 * Di toi man hinh cap nhat vi tri khach hang
	 * 
	 * @author: QuangVT
	 * @since: 2:01:58 PM Dec 6, 2013
	 * @return: void
	 * @throws:
	 */
	private void gotoCustomerLocation() {
		if (model.getCustomer() == null) {
			return;
		}

		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, model.getCustomer());
		bundle.putString(IntentConstants.INTENT_SENDER, sender);
		e.viewData = bundle;
		e.action = ActionEventConstant.GOTO_CUSTOMER_LOCATION;
		UserController.getInstance().handleSwitchFragment(e); 
	}
 
	/**
	 * Qua man hinh chi tiet co ban khach hang
	 * 
	 * @author: QuangVT
	 * @since: 2:01:46 PM Dec 6, 2013
	 * @return: void
	 * @throws:
	 */
	public void gotoCustomerInfo() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(model.getCustomer().customerId));
		bunde.putString(IntentConstants.INTENT_SENDER, sender);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Binding control tren man hinh
	 * 
	 * @author: QuangVT1
	 * @param view
	 * @return: void
	 * @throws:
	 */ 
	private void initView(ViewGroup view) {
		gvUserImageView = (VNMStableGridView) view.findViewById(R.id.gvImageUserAlbumView);
		gvUserImageView.setOnItemClickListener(this);
		gvUserImageView.setExpanded(true);
	 
		btTakePhoto = (Button) view.findViewById(R.id.btTakePhoto);
		btTakePhoto.setOnClickListener(this); 

		tvKH = (TextView) view.findViewById(R.id.tvKH);
		tvHoTenKH = (TextView) view.findViewById(R.id.tvHoTenKH); 

		tvNoDataResult = (TextView) view.findViewById(R.id.tvNoDataResult);
		tvNoDataResult.setVisibility(View.GONE);
	}

	/** 
	 * Khoi tao Album cho man hinh
	 * 
	 * @author: QuangVT1
	 * @return: void
	 * @throws:
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void initData() {
		int[] typeAlbum    = TYPES_ALBUM_USER;
		String[] nameAlbum = NAMES_ALBUM_USER;
		
		for (int index = 0, size = typeAlbum.length; index < size; index++) {
			AlbumDTO albumInfo = new AlbumDTO();
			int type = typeAlbum[index];
			String name = nameAlbum[index];

			albumInfo.setAlbumType(type);
			albumInfo.setAlbumTitle(name);
			model.getListAlbum().add(albumInfo);
		}

		bus = new ThumbnailBus(); 
		ImageAdapter imgAdapter   = new ImageAdapter(parent, model.getListAlbum());
		SimpleWebImageCache cache = new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101, bus);
		thumbs = new ThumbnailAdapter(parent, imgAdapter, cache, IMAGE_IDS);
		gvUserImageView.setAdapter(thumbs); 
	}

	@Override
	public void onImageTakingPopupEvent(int eventType, Object data) {
		switch (eventType) {
		case OnImageTakingPopupListener.ACTION_TAKING_IMAGE_LOCATION:
			takePhoto(MediaItemDTO.TYPE_LOCATION_IMAGE);
			break;
		case OnImageTakingPopupListener.ACTION_TAKING_IMAGE_CTTB:
			takePhoto(MediaItemDTO.TYPE_DISPLAY_PROGAME_IMAGE);
			break;
		case OnImageTakingPopupListener.ACTION_TAKING_IMAGE_RIVAl: 
			takePhoto(MediaItemDTO.TYPE_IMAGE_RIVAL);
			break;
		default:
			break;
		}

		popupTakeImage.dismiss();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_MAP: {
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerLocation();
			break;
		}
		case MENU_LIST_FEEDBACKS: {
			GlobalUtil.popBackStack(this.getActivity());
			gotoFeedBackList();
			break;
		}
		case MENU_INFO_DETAIL: {
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerInfo();
			break;
		}
//		case MENU_REVIEW:
//			GlobalUtil.popBackStack(this.getActivity());
//			gotoReviewsStaffView();
//			break;
		case ACTION_CLOSE:
			if (popupTakeImage != null && popupTakeImage.isShowing()) {
				popupTakeImage.dismiss();
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		VTLog.i("QuangVT", "start camera");
		if (v == btTakePhoto) {
			showTakingImageDialog();
		} else {
			super.onClick(v);
		}
	} 
 
	/**
	 * Show popup chup anh trung bay
	 * 
	 * @author: BANGHN
	 */
	private void showTakingImageDialog() {
		if (popupTakeImage == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			PopupChooseDisplayProgram popup = new PopupChooseDisplayProgram(parent, this);

			ArrayList<DisplayProgrameDTO> data = model.getListPhotoDPrograme();
			popup.setTitlePopup("Chọn album chụp hình");
			popup.setDataDisplayPrograme(data);
			build.setView(popup.view);
			popupTakeImage = build.create();
			popupTakeImage.setCanceledOnTouchOutside(false);
			Window window = popupTakeImage.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		
		if(popupTakeImage != null && !popupTakeImage.isShowing()){
			popupTakeImage.show();
		}
		
		popupTakeImage.show();
	}

	/**
	 * Chup hinh
	 * 
	 * @author quangvt1
	 * @param album
	 */
	public void takePhoto(int album) {
		CustomerDTO cusDTO = model.getCustomer();
		LatLng cusLatLng = new LatLng(cusDTO.lat, cusDTO.lng); 
		LatLng myLatLng = new LatLng(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude(), GlobalInfo
				.getInstance().getProfile().getMyGPSInfo().getLongtitude());
		// neu hoat dong che do khong ket noi thi ko check k.c chup anh
		if (GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_OFFLINE) {
			this.ofAlbum = album;
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO);
		} else if (myLatLng.lat > 0 && myLatLng.lng > 0 && cusLatLng.lat > 0 && cusLatLng.lng > 0
				&& GlobalUtil.getDistanceBetween(myLatLng, cusLatLng) <= model.shopDistance) {
			this.ofAlbum = album;
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_TAKE_PHOTO);
		} else {
			String mess = Constants.STR_BLANK;
			if(!StringUtil.isNullOrEmpty(customer.customerCode)) {
				mess = StringUtil.getString(R.string.TEXT_TAKE_PHOTO_TOO_FAR_FROM_SHOP_1) + " "
						+ customer.customerCode.substring(0, 3) + " - " + customer.customerName + " "
						+ StringUtil.getString(R.string.TEXT_TAKE_PHOTO_TOO_FAR_FROM_SHOP_2);
			}else{
				mess = StringUtil.getString(R.string.TEXT_TAKE_PHOTO_TOO_FAR_FROM_SHOP_1) + " "
						+ customer.customerName + " "
						+ StringUtil.getString(R.string.TEXT_TAKE_PHOTO_TOO_FAR_FROM_SHOP_2);
			}
			GlobalUtil.showDialogConfirm(this, parent, mess, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
					ACTION_CLOSE, null, false);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == gvUserImageView) {
			if (arg2 >= model.getListAlbum().size()) {
				return;
			}
			slideShowImage(arg2);
		}  
	} 

	/**
	 * Hien slide hinh anh
	 * 
	 * @author: QuangVT
	 * @param index
	 * @return: void
	 * @throws:
	 */ 
	private void slideShowImage(int index) {
		if (model.getListAlbum().get(index).getNumImage() > 0) {
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getMenuIndexString());
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomer().getCustomerId());
			bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
			bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, model.getCustomer().getCustomerName());
			bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, model.getCustomer().getCustomerCode());
			bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, model.getListAlbum().get(index));

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
			e.sender = this;
			e.viewData = bundle;
			TNPGController.getInstance().handleSwitchFragment(e);
		}
	}

	/**
	 * 
	 * request get list album for user
	 * 
	 * @author: HaiTC3 review code support comment for DungNT
	 * @return: void
	 * @throws:
	 */
	public void getListAlbumOfUser(boolean requestCombobox) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomer().getCustomerId());
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		bundle.putBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, requestCombobox);

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		e.sender = this;
		e.viewData = bundle;
		TNPGController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GO_TO_LIST_ALBUM_USER: {
			ListAlbumUserDTO tempModel = (ListAlbumUserDTO) modelEvent.getModelData();
			model.shopDistance = tempModel.shopDistance;
			model.setCustomer(tempModel.getCustomer());
			customer = tempModel.getCustomer();
			for (int i = 0, size = model.getListAlbum().size(); i < size; i++) {
				AlbumDTO albumDTOOld = model.getListAlbum().get(i);
				AlbumDTO albumDTONew = tempModel.getListAlbum().get(i);

				albumDTOOld.setNumImage(albumDTONew.getNumImage());
				albumDTOOld.setThumbUrl(albumDTONew.getThumbUrl());
			} 
 
			thumbs.notifyDataSetChanged();
			requestInsertLogKPI(HashMapKPI.TTTT_DANHSACHALBUMKHACHHANG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
			getListAlbumOfUser(false);
			// gui broadcast cho man hinh xem chi tiet album neu nhan chup hinh
			// tu chi tiet album
			TNPGPhotoThumbnailListView orderFragment = (TNPGPhotoThumbnailListView) getActivity().getFragmentManager()
					.findFragmentByTag(TNPGPhotoThumbnailListView.TAG);
			if (orderFragment != null) {
				orderFragment.receiveBroadcast(ActionEventConstant.UPDATE_TAKEN_PHOTO, null);
			} 
			break;
		}
		case ActionEventConstant.UPDATE_NEW_URL_TO_DB: {
			// xoa file anh hien tai
			File file = new File(takenPhoto.getAbsolutePath());
			if (file.exists())
				file.delete();

			getListAlbumOfUser(false);
			// gui broadcast cho man hinh xem chi tiet album neu nhan chup hinh
			// tu chi tiet album
			TNPGPhotoThumbnailListView orderFragment = (TNPGPhotoThumbnailListView) getActivity().getFragmentManager()
					.findFragmentByTag(TNPGPhotoThumbnailListView.TAG);
			if (orderFragment != null) {
				orderFragment.receiveBroadcast(ActionEventConstant.UPDATE_TAKEN_PHOTO, null);
			}
			break;
		}
		case ActionEventConstant.INSERT_MEDIA_ITEM: {
			// Long mediaId = (Long)modelEvent.getModelData();
			// uploadPhoto(mediaId.longValue()); 
			break;
		}
		case ActionEventConstant.GO_TO_LIST_ALBUM_PROGRAME: {
			// update lai model
			@SuppressWarnings("unchecked")
			ArrayList<AlbumDTO> tempArrayListPrograme = (ArrayList<AlbumDTO>) modelEvent.getModelData();
			if (tempArrayListPrograme != null && tempArrayListPrograme.size() > 0) {
				model.getListProgrameAlbum().clear();
				model.setListProgrameAlbum(tempArrayListPrograme);
				tvNoDataResult.setVisibility(View.GONE); 
			} else { 
				tvNoDataResult.setVisibility(View.VISIBLE);
				tvNoDataResult.requestFocus();
			} 
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		} 
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {

		case ActionEventConstant.INSERT_MEDIA_ITEM:
			switch (modelEvent.getModelCode()) {

			case ErrorConstants.ERROR_CODE_SUCCESS:
				// da ghi log , day la truong hop offline
				getListAlbumOfUser(true);
				break;
			default:
				super.handleErrorModelViewEvent(modelEvent);
				break;
			}
			break;

		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	public class ImageAdapter extends BaseAdapter {

		private ArrayList<AlbumDTO> list = new ArrayList<AlbumDTO>();

		public ImageAdapter(Context c, ArrayList<AlbumDTO> list) {
			this.list = list;
		}

		@Override
		public int getCount() {
			if (list != null) {
				return list.size();
			} else {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup pt) {
			View row = convertView;
			ViewHolder holder = null;
			if (convertView == null) {
				LayoutInflater layout = (LayoutInflater) ((GlobalBaseActivity) parent)
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = layout.inflate(R.layout.layout_tnpg_album_user, null);
				holder = new ViewHolder();
				holder.imageView = (ImageView) row.findViewById(R.id.imgAlbumImage);
				holder.imageViewBg = (ImageView) row.findViewById(R.id.imgAlbumBackground);
				holder.titleAlbum = (TextView) row.findViewById(R.id.tvAlbumName);
				holder.numImage = (TextView) row.findViewById(R.id.tvNumImage);
				row.setTag(holder);
			} else {
				holder = (ViewHolder) row.getTag();
			}

			holder.imageView.setImageResource(R.drawable.album_default);
			holder.imageViewBg.setImageResource(R.drawable.album_background);
			if (!StringUtil.isNullOrEmpty(list.get(position).getThumbUrl())) {
				if (list.get(position).getThumbUrl().contains(ExternalStorage.SDCARD_PATH))
					holder.imageView.setTag(list.get(position).getThumbUrl());
				else
					holder.imageView.setTag(ServerPath.IMAGE_PATH + list.get(position).getThumbUrl());
			}

			holder.titleAlbum.setText(list.get(position).getAlbumTitle());
			holder.numImage.setText(list.get(position).getNumImage() + " hình ảnh");

			return row;
		}

	}

	/**
	 * Class Holder giup cai thien hieu nang
	 *  
	 * @author quangvt1
	 */
	public static class ViewHolder {
		public ImageView imageView;
		public ImageView imageViewBg;
		public TextView titleAlbum;
		public TextView numImage;
	}

	/**
	 * Update Taken Photo
	 * 
	 * @author: QuangVT
	 * @since: 2:02:53 PM Dec 6, 2013
	 * @return: void
	 * @throws:
	 */
	public void updateTakenPhoto() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		Vector<String> viewData = new Vector<String>();

//		viewData.add(IntentConstants.INTENT_STAFF_ID);
//		viewData.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
//
//		viewData.add(IntentConstants.INTENT_SHOP_ID);
//		shopIdForUpPhoto = GlobalInfo.getInstance().getProfile().getUserData().shopIdProfile;
//		if (!StringUtil.isNullOrEmpty(shopIdForUpPhoto)) {
//			viewData.add(shopIdForUpPhoto);
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
			dto.objectId = Long.parseLong(model.getCustomer().getCustomerId());
			dto.objectType = ofAlbum;
			dto.mediaType = 0;// loai hinh anh , 1 loai video
			dto.url = takenPhoto.getAbsolutePath();
			dto.thumbUrl = takenPhoto.getAbsolutePath();
			dto.createDate = DateUtils.now();
			dto.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
			dto.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			dto.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			dto.type = 1;
			dto.status = 1;
			if (!StringUtil.isNullOrEmpty(shopId)) {
				dto.shopId = Integer.valueOf(shopId);
			}

			dto.fileSize = takenPhoto.length();
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			viewData.add(IntentConstants.INTENT_OBJECT_ID);
			viewData.add(model.getCustomer().getCustomerId());
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
			viewData.add(IntentConstants.INTENT_CUSTOMER_CODE);
			viewData.add(model.getCustomer().customerCode);
			viewData.add(IntentConstants.INTENT_STATUS);
			viewData.add("1");
			viewData.add(IntentConstants.INTENT_TYPE);
			viewData.add("1");
		} catch (NumberFormatException e1) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e1));
		}

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.UPLOAD_PHOTO_TO_SERVER;
		e.viewData = viewData;
		e.userData = dto;
		e.sender = this;
		UserController.getInstance().handleViewEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * @see com.viettel.dms.view.main.BaseFragment#receiveBroadcast(int, android.os.Bundle)
	 */
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				getListAlbumOfUser(true);
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.viettel.dms.view.main.BaseFragment#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String filePath = "";
		switch (requestCode) {
		case GlobalBaseActivity.RQ_TAKE_PHOTO:
			VTLog.i("TamPQ", "end camera");
			if (resultCode == Activity.RESULT_OK) {
				filePath = takenPhoto.getAbsolutePath();
				ImageValidatorTakingPhoto validator = new ImageValidatorTakingPhoto(parent, filePath,
						Constants.MAX_FULL_IMAGE_HEIGHT);
				validator.setDataIntent(data);
				if (validator.execute()) {
					VTLog.e("TakePhoto", ".................result taking photo : OK");
					updateTakenPhoto();
				}
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (thumbs != null) {
			thumbs.recycleAllBitmaps();
		} 
	} 
	
	@Override
	protected int getTabIndex() { 
		return TAB_INDEX;
	}
	
	@Override
	protected String getTitleString() {  
		return StringUtil.getString(R.string.TITLE_VIEW_ALBUM_LIST_USER_FROM_ALBUM_DEFAULT);
	}
}
