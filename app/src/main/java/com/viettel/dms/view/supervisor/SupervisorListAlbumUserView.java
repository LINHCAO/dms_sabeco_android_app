/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.commonsware.cwac.cache.SimpleWebImageCache;
import com.commonsware.cwac.thumbnail.ThumbnailAdapter;
import com.commonsware.cwac.thumbnail.ThumbnailBus;
import com.commonsware.cwac.thumbnail.ThumbnailMessage;
import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.download.ExternalStorage;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.dto.db.MediaItemDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.ListAlbumUserDTO;
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
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VNMStableGridView;
import com.viettel.dms.view.listener.OnImageTakingPopupListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.PopupChooseDisplayProgram;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;
import com.viettel.utils.ImageValidatorTakingPhoto;
import com.viettel.utils.VTLog;

/**
 * 
 * Danh sach album hinh anh cua khach hang
 * 
 * @author: Nguyen Thanh Dung
 * @version: 1.1
 * @since: 1.0
 */

public class SupervisorListAlbumUserView extends BaseFragment implements OnItemClickListener, OnClickListener,
		OnItemSelectedListener, OnTouchListener, OnImageTakingPopupListener {
	public static final int MENU_IMAGE = 17;
	public static final int MENU_CUSTOMER = 15;

	public static final int ACTION_CLOSE = 16;

	public static final int[] IMAGE_IDS = { R.id.imgAlbumImage };
	public static final String TAG = SupervisorListAlbumUserView.class.getName();
	//
	private ThumbnailAdapter thumbs = null;
	private ThumbnailAdapter programeThumbs = null;
	private ThumbnailBus bus;
	ListAlbumUserDTO model = new ListAlbumUserDTO();

	private GlobalBaseActivity parent; // parent
	private VNMStableGridView gvUserImageView;
	private VNMStableGridView gvProgrameImageView;
	// Button chup hinh
	Button btTakePhoto;
	// Button tim kiem
	Button btSearch;
	// ma khach hang
	TextView tvKH;// ma khach hang
	// Ten khach hang
	TextView tvHoTenKH;// ho ten

	VNMEditTextClearable etFromDate;
	VNMEditTextClearable etToDate;
	Spinner spinnerPhotoPrograme;
	LinearLayout rlSearch;
	TextView tvNoDataResult;
	private int ofAlbum;
	// file luu hinh chup
	File takenPhoto;
	// thong tin CTTB chup hinh
	public DisplayProgrameDTO dpTakePhoto = null;

	private int currentCalender;
	private static final int DATE_FROM_CONTROL = 1;
	private static final int DATE_TO_CONTROL = 2;

	// save du lieu for request
	private String fromDate;
	private String toDate;
	private String fromDateForRequest = "";
	private String toDateForRequest = "";
	private String programeCode = "";
	private List<DisplayProgrameItemDTO> listPrograme = null; // du lieu cho
																// combobox
																// chuong trinh

	// dialog chup anh
	AlertDialog popupTakeImage;

	private CustomerDTO customer;
	// private boolean isFromTBHV;
	private String shopId;
	private String gsnppStaffId;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public static SupervisorListAlbumUserView getInstance(Bundle data) {
		SupervisorListAlbumUserView instance = new SupervisorListAlbumUserView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;

			shopId = getArguments().getString(IntentConstants.INTENT_SHOP_ID);
			gsnppStaffId = getArguments().getString(IntentConstants.INTENT_STAFF_OWNER_ID);
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_album_user_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		Bundle data = (Bundle) getArguments();
		customer = (CustomerDTO) data.getSerializable(IntentConstants.INTENT_CUSTOMER);
		String title = StringUtil.getString(R.string.TILE_VIEW_NUMBER_LIST_USER_GSNPP);
		initView(view);

		SpannableObject spanObject = new SpannableObject();
		spanObject.addSpan(title);
		spanObject.addSpan(Constants.STR_SPACE + customer.getCustomerName(), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(spanObject);
		if(!StringUtil.isNullOrEmpty(customer.getCustomerCode())) {
			tvKH.setText(customer.getCustomerCode().substring(0, 3));
		}else{
			tvKH.setText("");
		}
		tvHoTenKH.setText(customer.getCustomerName());

		if (model.isFirstInit) {
			gvUserImageView.setAdapter(thumbs);
			thumbs.notifyDataSetChanged();
			gvProgrameImageView.setAdapter(programeThumbs);
			programeThumbs.notifyDataSetChanged();
			updateDataCombobox();
		} else {
			gvProgrameImageView.setVisibility(View.GONE);
			model.setCustomer(customer);
			model.getListAlbum().clear();
			model.getListProgrameAlbum().clear();
			model.isFirstInit = true;
			initData();
			getListAlbumOfUser(true);
		}
		return v;
	}

	/**
	 * 
	 * Qua man hinh danh sach diem ban
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	private void gotoCustomerSaleList() {
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_CUSTOMER_SALE_LIST;
		e.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param view
	 * @return: void
	 * @throws:
	 */

	private void initView(ViewGroup view) {
		gvUserImageView = (VNMStableGridView) view.findViewById(R.id.gvImageUserAlbumView);
		gvUserImageView.setOnItemClickListener(this);
		gvUserImageView.setExpanded(true);
		gvProgrameImageView = (VNMStableGridView) view.findViewById(R.id.gvImageProgrameAlbumView);
		gvProgrameImageView.setOnItemClickListener(this);
		gvProgrameImageView.setExpanded(true);

		btTakePhoto = (Button) view.findViewById(R.id.btTakePhoto);
		btTakePhoto.setOnClickListener(this);

		btSearch = (Button) view.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);

		tvKH = (TextView) view.findViewById(R.id.tvKH);
		tvHoTenKH = (TextView) view.findViewById(R.id.tvHoTenKH);

		etFromDate = (VNMEditTextClearable) view.findViewById(R.id.etFromDate);
		etToDate = (VNMEditTextClearable) view.findViewById(R.id.etToDate);
		etFromDate.setText(DateUtils.convertDateTimeWithFormat(DateUtils.getFirstDateOfOffsetMonth(-2),
				DateUtils.defaultDateFormat.toPattern()));
		etToDate.setText(DateUtils.convertDateTimeWithFormat(DateUtils.getStartTimeOfDay(new Date()),
				DateUtils.defaultDateFormat.toPattern()));
		etFromDate.setOnTouchListener(this);
		etToDate.setOnTouchListener(this);
		etToDate.setIsHandleDefault(false);
		etFromDate.setIsHandleDefault(false);
		spinnerPhotoPrograme = (Spinner) view.findViewById(R.id.spinnerPhotoPrograme);
		spinnerPhotoPrograme.setOnItemSelectedListener(this);

		rlSearch = (LinearLayout) view.findViewById(R.id.rlSearch);
		tvNoDataResult = (TextView) view.findViewById(R.id.tvNoDataResult);
		tvNoDataResult.setVisibility(View.GONE);
	}

	/**
	 * 
	 * khoi tao data cho man hinh
	 * 
	 * @author: HaiTC3 suport comment DungNT
	 * @return: void
	 * @throws:
	 */
	private void initData() {
		String[] typeAlbum = { "0", "1", "2" };
		String[] nameAlbum = { StringUtil.getString(R.string.TEXT_PHOTO_CLOSE_DOOR),
				StringUtil.getString(R.string.TEXT_PHOTO_DISPLAY),
				StringUtil.getString(R.string.TEXT_PHOTO_SALE_POSITION) };
		for (int i = 0, size = typeAlbum.length; i < size; i++) {
			AlbumDTO albumInfo = new AlbumDTO();
			String type = typeAlbum[i];
			String name = nameAlbum[i];

			albumInfo.setAlbumType(Integer.parseInt(type));
			albumInfo.setAlbumTitle(name);
			model.getListAlbum().add(albumInfo);
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_CUSTOMER: {
			gotoCustomerSaleList();
			break;
		}
		case ACTION_CLOSE:
			if (popupTakeImage != null && popupTakeImage.isShowing()) {
				popupTakeImage.dismiss();
			}
			break;
		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v == btTakePhoto) {
			showTakingImageDialog();
		} else if (v == btSearch) {
			GlobalUtil.forceHideKeyboard(parent);
			// luu lai gia tri de thuc hien tim kiem
			String dateTimePattern = StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN);
			Pattern pattern = Pattern.compile(dateTimePattern);
			if (!StringUtil.isNullOrEmpty(this.etFromDate.getText().toString())) {
				String strTN = this.etFromDate.getText().toString().trim();
				fromDate = etFromDate.getText().toString();
				Matcher matcher = pattern.matcher(strTN);
				if (!matcher.matches()) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
					return;
				} else {
					try {
						Date tn = StringUtil.stringToDate(strTN, "");
						String strFindTN = StringUtil.dateToString(tn, "yyyy-MM-dd");

						fromDateForRequest = strFindTN;
					} catch (Exception ex) {
						parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
						return;
					}
				}
			} else {
				fromDate = "";
				fromDateForRequest = "";
			}
			if (!StringUtil.isNullOrEmpty(this.etToDate.getText().toString())) {
				String strDN = this.etToDate.getText().toString().trim();
				toDate = etToDate.getText().toString();
				Matcher matcher = pattern.matcher(strDN);
				if (!matcher.matches()) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
					return;
				} else {
					try {
						Date dn = StringUtil.stringToDate(strDN, "");
						String strFindDN = StringUtil.dateToString(dn, "yyyy-MM-dd");

						toDateForRequest = strFindDN;
					} catch (Exception ex) {
						parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
						return;
					}
				}
			} else {
				toDate = "";
				toDateForRequest = "";
			}
			// luu lai combobox chuong trinh
			if (model.getListDisplayPrograme() == null) {
				programeCode = "";
			} else {
				int selected = spinnerPhotoPrograme.getSelectedItemPosition();
				if (selected < 0) {
					selected = 0;
				}
				if (listPrograme != null && listPrograme.size() > 0) {
					DisplayProgrameItemDTO itemDTO = listPrograme.get(selected);
					programeCode = itemDTO.value;
				}
			}

			if (!StringUtil.isNullOrEmpty(fromDateForRequest) && !StringUtil.isNullOrEmpty(toDateForRequest)
					&& DateUtils.compareDate(fromDateForRequest, toDateForRequest) == 1) {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID_2),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
			} else {
				getListAlbumOfPrograme(programeCode, fromDateForRequest, toDateForRequest);
			}
		} else {
			super.onClick(v);
		}
	}

	/**
	 * Request lay thong tin hinh anh cua chuong trinh theo dieu kien search
	 * 
	 * @author thanhnn
	 * @param programeCode2
	 * @param fromDateForRequest2
	 * @param toDateForRequest2
	 */
	private void getListAlbumOfPrograme(String programeCode2, String fromDateForRequest2, String toDateForRequest2) {
		// TODO Auto-generated method stub
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomer().getCustomerId());
		bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);

		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE, programeCode2);
		bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDateForRequest2);
		bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDateForRequest2);

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_PROGRAME;
		e.sender = this;
		e.viewData = bundle;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * Hien thi dialog chup hinh
	 * 
	 * @author: AnhND
	 * @param hasDeleteItem
	 * @return: void
	 * @throws:
	 */
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
		popupTakeImage.show();
	}

	public void takePhoto(int album) {
		LatLng cusLatLng = new LatLng(customer.lat, customer.lng);
		LatLng myLatLng = new LatLng(GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude(), GlobalInfo
				.getInstance().getProfile().getMyGPSInfo().getLongtitude());
		// neu hoat dong che do khong ket noi thi ko check k.c chup anh
		if (GlobalInfo.getInstance().getStateConnectionMode() == Constants.CONNECTION_OFFLINE) {
			this.ofAlbum = album;
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_SUPERVISOR_TAKE_PHOTO);
		} else if (myLatLng.lat > 0 && myLatLng.lng > 0 && cusLatLng.lat > 0 && cusLatLng.lng > 0
				&& GlobalUtil.getDistanceBetween(myLatLng, cusLatLng) <= model.shopDistance) {
			this.ofAlbum = album;
			takenPhoto = GlobalUtil.takePhoto(this, GlobalBaseActivity.RQ_SUPERVISOR_TAKE_PHOTO);
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
		} else if (arg0 == gvProgrameImageView) {
			if (arg2 >= model.getListProgrameAlbum().size()) {
				return;
			}
			goProgrameShowImage(arg2);
		}
	}

	/**
	 * Vao man hinh chi tiet album cua chuong trinh
	 * 
	 * @author ThanhNN
	 */
	private void goProgrameShowImage(int index) {
		// TODO Auto-generated method stub
		if (model.getListProgrameAlbum().get(index).getNumImage() > 0) {
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomer().getCustomerId());
			bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, model.getCustomer().getCustomerName());
			bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, model.getCustomer().getCustomerCode());
			bundle.putString(IntentConstants.INTENT_CREATE_USER, model.getCustomer().getCreateUser());
			bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDate);
			bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDate);
			bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
			bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, model.getListProgrameAlbum().get(index));

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
			e.sender = this;
			e.viewData = bundle;
			SuperviorController.getInstance().handleSwitchFragment(e);
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */

	private void slideShowImage(int index) {
		if (model.getListAlbum().get(index).getNumImage() > 0) {
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomer().getCustomerId());
			bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
			bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, model.getCustomer().getCustomerCode());
			bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, model.getCustomer().getCustomerName());
			bundle.putString(IntentConstants.INTENT_CREATE_USER, model.getCustomer().getCreateUser());
			bundle.putSerializable(IntentConstants.INTENT_ALBUM_INFO, model.getListAlbum().get(index));

			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_ALBUM_DETAIL_USER;
			e.sender = this;
			e.viewData = bundle;
			SuperviorController.getInstance().handleSwitchFragment(e);
		}
	}

	/**
	 * request get list album for user
	 * 
	 * @author: HaiTC3 review code support comment for DungNT
	 * @return: void
	 * @throws:
	 */
	private void getListAlbumOfUser(boolean requestCombobox) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, model.getCustomer().getCustomerId());
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, 0);
		bundle.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		bundle.putBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, requestCombobox);
		bundle.putString(IntentConstants.INTENT_STAFF_OWNER_ID, gsnppStaffId);

		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		e.sender = this;
		e.viewData = bundle;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GO_TO_LIST_ALBUM_USER: {
			ListAlbumUserDTO tempModel = (ListAlbumUserDTO) modelEvent.getModelData();
			model.shopDistance = tempModel.shopDistance;// gan khoang cach cho
														// phep chup hinh
			for (int i = 0, size = model.getListAlbum().size(); i < size; i++) {
				AlbumDTO albumDTOOld = model.getListAlbum().get(i);
				AlbumDTO albumDTONew = tempModel.getListAlbum().get(i);

				albumDTOOld.setNumImage(albumDTONew.getNumImage());
				albumDTOOld.setThumbUrl(albumDTONew.getThumbUrl());
			}

			// add data to model
			if (tempModel.getListDisplayPrograme() != null && tempModel.getListDisplayPrograme().size() > 0) {
				if (model.getListDisplayPrograme() != null) {
					model.getListDisplayPrograme().clear();
				}
				model.setListDisplayPrograme(tempModel.getListDisplayPrograme());
				if (tempModel.getListPhotoDPrograme() != null && tempModel.getListPhotoDPrograme().size() > 0) {
					if (model.getListPhotoDPrograme() != null) {
						model.getListPhotoDPrograme().clear();
					}
					if (popupTakeImage != null) {
						popupTakeImage = null;
					}
					model.setListPhotoDPrograme(tempModel.getListPhotoDPrograme());

				}
				if (listPrograme != null) {
					listPrograme.clear();
					listPrograme = null;
				}
				updateDataCombobox();
			}
			if (tempModel.getListProgrameAlbum() != null && tempModel.getListProgrameAlbum().size() > 0) {
				model.getListProgrameAlbum().clear();
				model.setListProgrameAlbum(tempModel.getListProgrameAlbum());
				gvProgrameImageView.setVisibility(View.VISIBLE);
				rlSearch.setVisibility(View.VISIBLE);
			}

			try {
				bus = new ThumbnailBus();

				thumbs = new ThumbnailAdapter(parent, new ImageAdapter(parent, model.getListAlbum()),
						new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101, bus), IMAGE_IDS);
				gvUserImageView.setAdapter(thumbs);
				thumbs.notifyDataSetChanged();

				// update lai combobox chuong trinh

				// render giao dien phan hinh anh cua chuong trinh
				programeThumbs = new ThumbnailAdapter(parent, new ImageAdapter(parent, model.getListProgrameAlbum()),
						new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101, bus), IMAGE_IDS);
				gvProgrameImageView.setAdapter(programeThumbs);
				programeThumbs.notifyDataSetChanged();

			} catch (Exception e1) {
				// TODO: handle exception
			}
			break;
		}
		case ActionEventConstant.UPLOAD_PHOTO_TO_SERVER: {
			requestInsertLogKPI(HashMapKPI.GSNPP_CHUPHINHTAIDIEMBAN, modelEvent.getActionEvent().startTimeFromBoot);
			getListAlbumOfUser(false);
			// gui broadcast cho man hinh xem chi tiet album neu nhan chup hinh
			// tu chi tiet album
			SupervisorPhotoThumbnailListUserView orderFragment = (SupervisorPhotoThumbnailListUserView) getActivity()
					.getFragmentManager().findFragmentByTag(SupervisorPhotoThumbnailListUserView.TAG);
			if (orderFragment != null) {
				orderFragment.receiveBroadcast(ActionEventConstant.UPDATE_TAKEN_PHOTO, null);
			}
			dpTakePhoto = null;
			
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
			SupervisorPhotoThumbnailListUserView orderFragment = (SupervisorPhotoThumbnailListUserView) getActivity()
					.getFragmentManager().findFragmentByTag(SupervisorPhotoThumbnailListUserView.TAG);
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
				gvProgrameImageView.setVisibility(View.VISIBLE);
				gvProgrameImageView.requestFocus();
			} else {
				gvProgrameImageView.setVisibility(View.GONE);
				tvNoDataResult.setVisibility(View.VISIBLE);
				tvNoDataResult.requestFocus();
			}
			// render lai giao dien phan hinh anh chuong trinh
			// clear programeThumbs
			programeThumbs = new ThumbnailAdapter(parent, new ImageAdapter(parent, model.getListProgrameAlbum()),
					new SimpleWebImageCache<ThumbnailBus, ThumbnailMessage>(null, null, 101, bus), IMAGE_IDS);
			gvProgrameImageView.setAdapter(programeThumbs);
			programeThumbs.notifyDataSetChanged();
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}

	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
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
			// KunKunLog.d("PhucNT4", " location    " + position);
			if (convertView == null) {
				LayoutInflater layout = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				row = layout.inflate(R.layout.layout_album_user, null);
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
				// holder.imageView.setTag(list.get(position).getThumbUrl());
			}

			holder.titleAlbum.setText(list.get(position).getAlbumTitle());
			holder.numImage.setText(list.get(position).getNumImage() + Constants.STR_SPACE
					+ StringUtil.getString(R.string.TEXT_PHOTO));
			return row;
		}
	}

	public static class ViewHolder {
		public ImageView imageView;
		public ImageView imageViewBg;
		public TextView titleAlbum;
		public TextView numImage;
	}

	public void updateTakenPhoto() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		Vector<String> viewData = new Vector<String>();

//		viewData.add(IntentConstants.INTENT_STAFF_ID);
//		viewData.add(Integer.toString(GlobalInfo.getInstance().getProfile().getUserData().id));
//
//		viewData.add(IntentConstants.INTENT_SHOP_ID);
////		if (!StringUtil.isNullOrEmpty(shopId)) {
////			viewData.add(shopId);
//		viewData.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
////		}

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
			dto.fileSize = takenPhoto.length();
			dto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			dto.type = 1;
			dto.status = 1;

			if (!StringUtil.isNullOrEmpty(shopId)) {
				dto.shopId = Integer.valueOf(shopId);
			}

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

			if (dpTakePhoto != null && ofAlbum == MediaItemDTO.TYPE_IMAGE_RIVAL) {
				dto.displayProgrameId = dpTakePhoto.displayProgrameId;
				viewData.add(IntentConstants.INTENT_DISPLAY_PROGRAM_ID);
				viewData.add("" + dpTakePhoto.displayProgrameId);
			}
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
		// e.isBlockRequest = true;
		UserController.getInstance().handleViewEvent(e);
	}

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		String filePath = "";
		switch (requestCode) {
		case GlobalBaseActivity.RQ_SUPERVISOR_TAKE_PHOTO:
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
		// TODO Auto-generated method stub
		super.onDestroyView();

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (thumbs != null) {
			thumbs.recycleAllBitmaps();
		}
		if (programeThumbs != null) {
			programeThumbs.recycleAllBitmaps();
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		if (arg0 == etFromDate) {
			if (!etFromDate.onTouchEvent(arg1)) {
				currentCalender = DATE_FROM_CONTROL;
				parent.fragmentTag = SupervisorListAlbumUserView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etFromDate.getText().toString(), true);
			}
		}
		if (arg0 == etToDate) {
			if (!etToDate.onTouchEvent(arg1)) {
				currentCalender = DATE_TO_CONTROL;
				parent.fragmentTag = SupervisorListAlbumUserView.TAG;

				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etToDate.getText().toString(), true);
			}
		}
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * Ham tra ve khi picker date
	 * 
	 * @author: ThanhNN8
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return: void
	 * @throws:
	 */
	public void updateDate(int dayOfMonth, int monthOfYear, int year) {
		// TODO Auto-generated method stub
		String sDay = String.valueOf(dayOfMonth);
		String sMonth = String.valueOf(monthOfYear + 1);
		if (dayOfMonth < 10) {
			sDay = "0" + sDay;
		}
		if (monthOfYear + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		if (currentCalender == DATE_FROM_CONTROL) {
			if (DateUtils.checkDateInOffsetMonth(sDay, sMonth, year, -2)) {
				etFromDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
				etFromDate.setText(new StringBuilder()
				// Month is 0 based so add 1
						.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
			} else {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
			}
		}
		if (currentCalender == DATE_TO_CONTROL) {
			if (DateUtils.checkDateInOffsetMonth(sDay, sMonth, year, -2)) {
				etToDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
				etToDate.setText(new StringBuilder()
				// Month is 0 based so add 1
						.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
			} else {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
			}
		}
	}

	private void updateDataCombobox() {
		if (listPrograme == null) {
			listPrograme = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = new DisplayProgrameItemDTO();
			itemDTO.name = StringUtil.getString(R.string.ALL);
			itemDTO.value = "";
			if (model.getListDisplayPrograme() != null && model.getListDisplayPrograme().size() > 0) {
				listPrograme.add(itemDTO);
			}

			if (model.getListDisplayPrograme() != null) {
				for (int j = 0, jlength = model.getListDisplayPrograme().size(); j < jlength; j++) {
					itemDTO = model.getListDisplayPrograme().get(j);
					listPrograme.add(itemDTO);
				}
			}

		}
		int lengthPrograme = listPrograme.size();
		String programeCode[] = new String[lengthPrograme];
		// khoi tao gia tri cho ma chuong trinh
		for (int i = 0; i < lengthPrograme; i++) {
			DisplayProgrameItemDTO dto = listPrograme.get(i);
			programeCode[i] = dto.name;
		}

		SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, programeCode);
		this.spinnerPhotoPrograme.setAdapter(adapterNVBH);
		spinnerPhotoPrograme.setSelection(0);

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
			dpTakePhoto = (DisplayProgrameDTO) data;
			takePhoto(MediaItemDTO.TYPE_IMAGE_RIVAL);
			break;
		default:
			break;
		}

		popupTakeImage.dismiss();
	}
}
