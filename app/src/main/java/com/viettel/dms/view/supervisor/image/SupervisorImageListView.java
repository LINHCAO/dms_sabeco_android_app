/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.image;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.AlbumDTO;
import com.viettel.dms.dto.view.ImageListDTO;
import com.viettel.dms.dto.view.ImageListItemDTO;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.dms.view.sale.image.ImageListRow;
import com.viettel.sabeco.R;


/**
 *  04-00. Danh sách hình ảnh GSBH
 * 
 * SupervisorImageListView.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  6:02:00 PM Dec 6, 2013
 */
public class SupervisorImageListView extends BaseFragment implements OnEventControlListener, VinamilkTableListener,
		OnClickListener, OnTouchListener, OnItemSelectedListener {
	/*----------------DEFAULT----------------*/
	//----Tag
	public static final String TAG = SupervisorImageListView.class.getName();
	//----Parent
	private GlobalBaseActivity parent;
	
	/*----------------MENU CONSTANT----------------*/
	private static final int MENU_IMAGE = 17;		// menu hinh anh
	private static final int MENU_CUSTOMER = 15;	// menu khach hang
	
	/*----------------CONSTANT----------------*/
	// cho biet dang chon fromDate
	private static final int DATE_FROM_CONTROL = 1;	
	// cho biet dang chon toDate   
	private static final int DATE_TO_CONTROL = 2;
	// So luong dong trong 1 trang 
	private static final int NUM_ITEM_PER_PAGE = Constants.NUM_ITEM_PER_PAGE; 
	
	/*----------------SEARCH INFO----------------*/
	private String fromDateForRequest = Constants.STR_BLANK;	// Ngay bat dau
	private String toDateForRequest   = Constants.STR_BLANK;	// Ngay ket thuc
	private String customerName 	  = Constants.STR_BLANK;	// Ma khach hang
	private String customerCode 	  = Constants.STR_BLANK;	// Ten khach hang
	
	/*----------------DTO----------------*/
	public ImageListDTO imageListDTO;	// DTO render
	// danh sach nhan vien ban hang
	private ListStaffDTO dtoListStaff;
	
	/*----------------CONTROL----------------*/
	private Button btReInput;// Button nhap lai
	private Button btSearch; // Button tim kiem
	
	private Spinner spStaff; // Combobox Nhan vien ban hang
	private Spinner spLine;	 // Combobox Tuyen
	
	private SpinnerAdapter adLine;	// Adapter cho combobox Line
	private SpinnerAdapter adStaff;	// Adaoter cho combobox Staff
	
	private VNMEditTextClearable edFromDate;// TextView ngay bat dau
	private VNMEditTextClearable edToDate;	// TextView ngay ket thuc
	private VNMEditTextClearable edCusCode;	// ma khach hang
	private VNMEditTextClearable edCusName;	// ten khach hang
	
	private DMSTableView tbCusList;  // Table danh sach khach hang
	
	/*----------------VARIABLES----------------*/
	private String fromDate;		// fromdate hien tren textview
	private String toDate;			// todate hien tren textview
	private String gsnppStaffId;	// ID GSBH
	private String gsnppShopId;		// ShopID GSBH
	private String fromDateDefault;	// Chuoi mac dinh cua fromdate
	private String toDateDefault;	// Chuoi mac dinh cua toDate
	
	private int selectedLineIndex;	// Index select combobox Line
	private int selectedNVBHIndex;	// Index select combobox Staff
	private int selectedLineDefault;
	private int selectedStaffDefault; // index lua chon tuyen mac dinh
	
	private boolean isSearch;				// Trang thai dang search
	@SuppressWarnings("unused")
	private boolean isReload     = false;	// Reload hay khong
	private boolean isUpdateData = false;	// UpdateData khong
	private boolean isFromTBHV;				// Dang o role TBHV hay khong
	private boolean getTotalPage; 			// Can lay so trang khong
	
	private int currentPage = -1;	// Trang hien tai	 
	private long nvbhStaffId;		// ID Staff
	private int currentCalender;	// dang select fromdate hay todate

	private String[] nvbhStr = {}; // danh sach nhan vien ban hang cua GS
	private TextView tvLabelStaff;
	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author: QuangVT
	 * @since: 6:02:25 PM Dec 6, 2013
	 * @return: SupervisorImageListView
	 * @throws:  
	 * @param b
	 * @return
	 */
	public static SupervisorImageListView getInstance(Bundle b) {
		SupervisorImageListView f = new SupervisorImageListView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
		gsnppStaffId = getArguments().getString(IntentConstants.INTENT_STAFF_ID);
		if (StringUtil.isNullOrEmpty(gsnppStaffId)) {
			gsnppStaffId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
			gsnppShopId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		} else {
			isFromTBHV = true;
			gsnppShopId = getArguments().getString(IntentConstants.INTENT_SHOP_ID);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_image_list, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		if(parent instanceof SupervisorActivity) {
			setTitleHeaderView(getString(R.string.TITLE_VIEW_GSNPP_IMAGE_LIST));
		} else {
			setTitleHeaderView(getString(R.string.TITLE_VIEW_GSBH_IMAGE_LIST));
		}

//		if (!isFromTBHV) {
//			enableMenuBar(this);
//			initMenuActionBar();
//		}
		if (parent instanceof SupervisorActivity) {
			enableMenuBar(this);
			initMenuActionBar();
		}

		// Binding control
		initView(v);

		if (imageListDTO != null && currentPage > 0) {
			spLine.setAdapter(adLine);
			spStaff.setAdapter(adStaff);
			// Lay danh sach khach hang
			renderLayout();
		} else {
			adStaff = new SpinnerAdapter(parent, R.layout.simple_spinner_item, nvbhStr);
			spStaff.setAdapter(adStaff);

			fromDate = fromDateDefault;
			toDate   = toDateDefault;
			getListSaleStaff();
		}

		return v;
	}

	/**
	 * Lay danh sach nhan vien
	 * @author: hoanpd1
	 * @since: 10:16:08 17-09-2014
	 * @return: void
	 * @throws:  
	 */
	private void getListSaleStaff() {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(gsnppShopId));
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,
				String.valueOf(gsnppStaffId));
		b.putBoolean(IntentConstants.INTENT_ORDER, true);
		b.putBoolean(IntentConstants.INTENT_IS_ALL, true);

		if(parent instanceof SupervisorActivity) {
			e.viewData = b;
			e.action = ActionEventConstant.GET_LIST_NVBH;
			e.sender = this;
			SuperviorController.getInstance().handleViewEvent(e);
		} else {
			e.viewData = b;
			e.action = ActionEventConstant.GET_LIST_STAFF;
			e.sender = this;
			TBHVController.getInstance().handleViewEvent(e);
		}
	}

	/**
	 * Khoi tao view tren man hinh
	 *
	 * @author: QuangVT
	 * @since: 2:19:33 PM Dec 9, 2013
	 * @return: void
	 * @throws:  
	 * @param v
	 */
	private void initView(View v) {
		edCusCode = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
		edCusName = (VNMEditTextClearable) v.findViewById(R.id.edCusName);
		
		spStaff = (Spinner) v.findViewById(R.id.spStaff);
		spStaff.setOnItemSelectedListener(this);
		
		spLine = (Spinner) v.findViewById(R.id.spLine);
		spLine.setOnItemSelectedListener(this);
		
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		
		btReInput = (Button) v.findViewById(R.id.btReInput);
		btReInput.setOnClickListener(this);
		
		edCusCode  = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);
		edCusName  = (VNMEditTextClearable) v.findViewById(R.id.edCusName);
		edFromDate = (VNMEditTextClearable) v.findViewById(R.id.edFromDate);
		edToDate   = (VNMEditTextClearable) v.findViewById(R.id.edToDate);
		
		edFromDate.setOnTouchListener(this);
		edFromDate.setIsHandleDefault(false);
		edToDate.setOnTouchListener(this);
		edToDate.setIsHandleDefault(false);

		final String pattern = DateUtils.defaultDateFormat.toPattern();
		fromDateDefault = DateUtils.convertDateTimeWithFormat(DateUtils.getFirstDateOfOffsetMonth(-2), pattern);
		toDateDefault   = DateUtils.convertDateTimeWithFormat(DateUtils.getStartTimeOfDay(new Date()), pattern);
		edFromDate.setText(fromDateDefault);
		edToDate.setText(toDateDefault);
		
		adLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, Constants.ARRAY_LINE_CHOOSE);
		spLine.setAdapter(adLine);
		selectedLineDefault = Constants.ARRAY_LINE_CHOOSE.length - 1;
		selectedLineIndex = selectedLineDefault;
		spLine.setSelection(selectedLineIndex);
		tvLabelStaff = (TextView) v.findViewById(R.id.tvLabelStaff);
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH){
			tvLabelStaff.setText(StringUtil.getString(R.string.TITLE_GSNPP));
		}else{
			tvLabelStaff.setText(StringUtil.getString(R.string.TEXT_TITLE_NVBH));
		}
		tbCusList = (DMSTableView) v.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		initHeaderTable(tbCusList, new SupervisorImageListRow(parent, this));
	}
 
	
	/**
	 * Khoi tao menu cho man hinh khi click tu danh sach diem ban cua GSNPP
	 * 
	 * @author: QuangVT
	 * @since: 6:02:57 PM Dec 6, 2013
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		final String strPicture  = StringUtil.getString(R.string.TEXT_PICTURE);
		final String strCustomer = StringUtil.getString(R.string.TEXT_CUSTOMER);
		final int    menuIndex   = 1;
		
		addMenuItem(strPicture, R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(strCustomer, R.drawable.menu_customer_icon, MENU_CUSTOMER, View.INVISIBLE);
		setMenuItemFocus(menuIndex);
	} 
	
	
	/**
	 * Lay danh sach khach hang
	 * 
	 * @author: QuangVT
	 * @since: 6:03:26 PM Dec 6, 2013
	 * @return: void
	 * @throws:  
	 * @param page
	 * @param isGetTotolPage
	 */
	private void getCustomerList(int page, boolean isGetTotolPage) {
		// Sho dialog loading...
		parent.showLoadingDialog();
		
		this.getTotalPage = isGetTotolPage;
		
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle(); 
		data.putInt(IntentConstants.INTENT_PAGE, page);
		data.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotolPage);
		
		// Ma khach hang
		if (!StringUtil.isNullOrEmpty(customerCode.trim())) {
			data.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCode);
		}
		
		// Ten khach hang
		final String cusName = StringUtil.getEngStringFromUnicodeString(customerName.trim());
		if (!StringUtil.isNullOrEmpty(customerName)) {
			data.putString(IntentConstants.INTENT_CUSTOMER_NAME, cusName);
		}
		
		// Tuyen
		final String line = DateUtils.getVisitPlan(Constants.ARRAY_LINE_CHOOSE[selectedLineIndex]);
		data.putString(IntentConstants.INTENT_VISIT_PLAN, line);
		
		data.putLong(IntentConstants.INTENT_STAFF_ID, nvbhStaffId);
		data.putInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, NUM_ITEM_PER_PAGE);
		data.putString(IntentConstants.INTENT_STAFF_OWNER_ID, String.valueOf(gsnppStaffId));
		data.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDateForRequest);
		data.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDateForRequest);
		data.putString(IntentConstants.INTENT_SHOP_ID, gsnppShopId);
		data.putBoolean(IntentConstants.INTENT_IS_SEARCH, isSearch);

		if(parent instanceof SupervisorActivity) {
			e.viewData = data;
			e.action = ActionEventConstant.GET_GSNPP_IMAGE_LIST;
			e.sender = this;
			SuperviorController.getInstance().handleViewEvent(e);
		} else {
			e.viewData = data;
			e.action = ActionEventConstant.GET_GSBH_IMAGE_LIST;
			e.sender = this;
			TBHVController.getInstance().handleViewEvent(e);
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_NVBH:
		case ActionEventConstant.GET_LIST_STAFF:
			dtoListStaff = (ListStaffDTO) modelEvent.getModelData();
			if (dtoListStaff != null) {
				//dtoListStaff.addItemAll();
				updateStaffSpiner();
				// Lay danh sach khach hang 
				getCustomerList(1, true);
			}
			break;
		case ActionEventConstant.GET_GSNPP_IMAGE_LIST:
		case ActionEventConstant.GET_GSBH_IMAGE_LIST:
			ImageListDTO tempDto = (ImageListDTO) modelEvent.getModelData();
			if (imageListDTO != null) {
				imageListDTO.listItem.clear();
				imageListDTO.listItem.addAll(tempDto.listItem);
			} else {
				imageListDTO = tempDto;
			}

			if (true == getTotalPage) {
				imageListDTO.totalCustomer = tempDto.totalCustomer;
			}

			if (isUpdateData) {
				isUpdateData = false;
				currentPage = -1;
			}
			
			renderLayout();
			
			// Close dialog
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_DANHSACHHINHANH, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}  
	
	/**
	 * Update combobox nhan vien ban hang
	 * 
	 * @author: QuangVT
	 * @since: 2:39:05 PM Dec 9, 2013
	 * @return: void
	 * @throws:
	 */
	private void updateStaffSpiner() {
		if (dtoListStaff != null && dtoListStaff.arrList !=null) {
			//neu co danh sach staff
			if (dtoListStaff.arrList.size()> 0) {
				nvbhStr = new String[dtoListStaff.arrList.size()];
				for (int i = 0; i < dtoListStaff.arrList.size(); i++) {
					//neu <> tat ca thi se hien thi [Code - Name] 
					nvbhStr[i] = dtoListStaff.arrList.get(i).getCodeNameString();
				}
			}else{
				//khong co staff nao, hien rong 
				nvbhStr = new String[]{};
			}
			
			adStaff = new SpinnerAdapter(parent, R.layout.simple_spinner_item, nvbhStr);
			spStaff.setAdapter(adStaff);
					
			selectedStaffDefault = 0;
			selectedNVBHIndex = selectedStaffDefault;
			spStaff.setSelection(selectedNVBHIndex);
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}
 
	/**
	 * Render layout for list customer - image view
	 * 
	 * @author: QuangVT
	 * @since: 2:43:21 PM Dec 9, 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (currentPage <= 0) {
			tbCusList.setNumItemsPage(NUM_ITEM_PER_PAGE);
			tbCusList.setTotalSize(imageListDTO.totalCustomer, 1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		} else { 
			tbCusList.setNumItemsPage(NUM_ITEM_PER_PAGE);
			tbCusList.setTotalSize(imageListDTO.totalCustomer, currentPage);
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		}
		tbCusList.clearAllData();
		int pos = 1 + NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1); 
		if (imageListDTO.listItem.size() > 0) {
			for (int i = 0, s = imageListDTO.listItem.size(); i < s; i++, pos++) {
				SupervisorImageListRow row = new SupervisorImageListRow(parent, this);
				row.renderLayout(pos, imageListDTO.listItem.get(i));   
				row.setListener(this);
				tbCusList.addRow(row);
			}
		} else {
			tbCusList.showNoContentRow();
//			SupervisorImageListRow row = new SupervisorImageListRow(parent, null);
//			row.setClickable(true);
//			row.setOnClickListener(this);
//			row.renderLayoutNoResult();
//			tbCusList.addRow(row);
		}

	} 
	 
	@Override
	public void onClick(View v) {
		if (v == btSearch) { 
			customerCode = edCusCode.getText().toString().trim();
			customerName = edCusName.getText().toString().trim();

			selectedLineIndex = spLine.getSelectedItemPosition();
			selectedNVBHIndex = spStaff.getSelectedItemPosition();
			 
			if (dtoListStaff != null && dtoListStaff.arrList != null && dtoListStaff.arrList.size() > 0) {
				if (!StringUtil.isNullOrEmpty(dtoListStaff.arrList.get(selectedNVBHIndex).id)) {
					nvbhStaffId = Integer.valueOf(dtoListStaff.arrList.get(selectedNVBHIndex).id);
				} else {
					nvbhStaffId = 0;
				}
			}

			// hide ban phim
			GlobalUtil.forceHideKeyboard(parent);
			
			// luu lai gia tri de thuc hien tim kiem
			String dateTimePattern = StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN);
			Pattern pattern = Pattern.compile(dateTimePattern);
			getFromDateRequest(pattern);
			getToDateRequest(pattern);

			String currentFromDate = fromDateDefault;
			String currentToDate   = toDateDefault;

			// Tim kiem: co thay doi it nhat 1 tham so
			if ( !StringUtil.isNullOrEmpty(customerCode)
					|| !StringUtil.isNullOrEmpty(customerName) || !currentFromDate.equals(fromDate)
					|| !currentToDate.equals(toDate) ||  selectedNVBHIndex != selectedStaffDefault || selectedLineIndex != selectedLineDefault) {
				if (!StringUtil.isNullOrEmpty(fromDateForRequest) && !StringUtil.isNullOrEmpty(toDateForRequest)
						&& DateUtils.compareDate(fromDateForRequest, toDateForRequest) == 1) {
					GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID_2),
							StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
					edFromDate.requestFocus();
				} else {
					isSearch = true;
					tbCusList.getPagingControl().totalPage = -1;
					currentPage = -1;
					getCustomerList(1, true);
				}
			} else {// Mode load ds mac dinh ban dau
				if (isSearch == true) { 
					isSearch = false;
					tbCusList.getPagingControl().totalPage = -1;
					currentPage = -1;
					fromDate = fromDateDefault;
					toDate   = toDateDefault;
					fromDateForRequest = Constants.STR_BLANK;
					toDateForRequest   = Constants.STR_BLANK;
					getCustomerList(1, true);
				}
				isSearch = false;
			}
		} else if (v == btReInput) {
			resetLayout();
		}
	}

	/**
	 * Lay thoi gian todate
	 * 
	 * @author: QuangVT
	 * @since: 4:36:41 PM Dec 13, 2013
	 * @return: void
	 * @throws:  
	 * @param pattern
	 */
	private void getToDateRequest(Pattern pattern) {
		if (!StringUtil.isNullOrEmpty(edToDate.getText().toString())) {
			String strDN = edToDate.getText().toString().trim();
			toDate = strDN;
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
	}

	/**
	 * Lay thoi gian fromdate
	 * 
	 * @author: QuangVT
	 * @since: 4:36:45 PM Dec 13, 2013
	 * @return: void
	 * @throws:  
	 * @param pattern
	 */
	private void getFromDateRequest(Pattern pattern) {
		if (!StringUtil.isNullOrEmpty(edFromDate.getText().toString())) {
			String strTN = edFromDate.getText().toString().trim();
			fromDate = strTN;
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
	}
 
	/**
	 * Reset cac gia tri ve mac dinh
	 * 
	 * @author: QuangVT
	 * @since: 2:43:59 PM Dec 9, 2013
	 * @return: void
	 * @throws:
	 */
	private void resetAllValue() {
		//reset dto
		imageListDTO = null;
		isSearch = false;
		customerName = Constants.STR_BLANK;
		customerCode = Constants.STR_BLANK; 
		nvbhStaffId = 0;
		currentPage = -1;
		selectedNVBHIndex = selectedStaffDefault;
		selectedLineIndex = Constants.ARRAY_LINE_CHOOSE.length - 1;
		
		resetLayout();
		
		fromDate = fromDateDefault;
		toDate   =  toDateDefault;
		fromDateForRequest = Constants.STR_BLANK;
		toDateForRequest   = Constants.STR_BLANK;
	}

	/**
	 * 
	 * Reset gia tri tren layout ve gia tri mac dinh
	 * 
	 * @author: Nguyen Thanh Dung
	 * @return: void
	 * @throws:
	 */
	private void resetLayout() {
		edFromDate.setText(fromDateDefault);
		edToDate.setText(toDateDefault);
		spLine.setSelection(Constants.ARRAY_LINE_CHOOSE.length - 1);
		spStaff.setSelection(selectedStaffDefault);
		edCusCode.setText(Constants.STR_BLANK);
		edCusName.setText(Constants.STR_BLANK);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_CUSTOMER:
			gotoCustomerSaleList();
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * Di toi man hinh Khach hang
	 * 
	 * @author: QuangVT
	 * @since: 2:47:20 PM Dec 9, 2013
	 * @return: void
	 * @throws:
	 */
	private void gotoCustomerSaleList() {  
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		e.sender   = this;
		e.viewData = bundle;
		e.action   = ActionEventConstant.GO_TO_CUSTOMER_SALE_LIST;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusList) {
			currentPage = tbCusList.getPagingControl().getCurrentPage();
			getCustomerList(currentPage, false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {

		switch (action) {
		case ImageListRow.ACTION_VIEW_ALBUM: { 
			isReload = true;
			ImageListItemDTO dto = (ImageListItemDTO) data;

			CustomerDTO customer = new CustomerDTO();
			customer.customerId = dto.customerId;
			customer.customerCode = dto.customerCode;
			customer.customerName = dto.customerName;
			customer.setLat(dto.lat);
			customer.setLng(dto.lng);
			customer.setStreet(dto.street);
			customer.setHouseNumber(dto.houseNumber);
			AlbumDTO album = new AlbumDTO();
			album.setNumImage(dto.imageNumber); 

			if(isSearch){
				gotoListAlbumUserForSearch(customer);
			}else{
				gotoListAlbumUser(customer);
			}
			break;
		}
		default:
			break;
		}
	} 
	 
 
	/**
	 * Toi man hinh ds album cua kh
	 * 
	 * @author: QuangVT
	 * @since: 2:48:09 PM Dec 9, 2013
	 * @return: void
	 * @throws:  
	 * @param customer
	 */
	private void gotoListAlbumUser(CustomerDTO customer) {
	 	Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_TYPE, 4);
		if(parent instanceof SupervisorActivity) {
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "07-02");
		} else {
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "05-01");
		}
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		 
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * Di toi man hinh album hinh anh cua khach hang luc search
	 * 
	 * @author: QuangVT
	 * @since: 4:24:59 PM Dec 13, 2013
	 * @return: void
	 * @throws:  
	 * @param customer
	 */
	private void gotoListAlbumUserForSearch(CustomerDTO customer) {
		final String fromDate = edFromDate.getText().toString().trim();
		final String toDate   = edToDate.getText().toString().trim();
		
		Bundle bundle = new Bundle();
		if(parent instanceof SupervisorActivity) {
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "07-02");
		} else {
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "05-01");
		}
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customer.getCustomerId());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, customer.getCustomerName());
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, customer.getCustomerCode());
		bundle.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDate);
		bundle.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDate); 
		bundle.putInt(IntentConstants.INTENT_TYPE, 4); 

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER_FOR_SEARCH;
		SaleController.getInstance().handleSwitchFragment(e);
	}
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == edFromDate) {
			if (!v.onTouchEvent(event)) {
				currentCalender = DATE_FROM_CONTROL;
				parent.fragmentTag = SupervisorImageListView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edFromDate.getText().toString(), true);
			}
		}

		if (v == edToDate) {
			if (!v.onTouchEvent(event)) {
				currentCalender = DATE_TO_CONTROL;
				parent.fragmentTag = SupervisorImageListView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edToDate.getText().toString(), true);
			}
		}
		return false;
	}

	/**
	 * Update date khi chon fromdate hoac todate
	 * 
	 * @author: QuangVT
	 * @since: 2:48:27 PM Dec 9, 2013
	 * @return: void
	 * @throws:  
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 */
	public void updateDate(int dayOfMonth, int monthOfYear, int year) {
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
				edFromDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
				edFromDate.setText(new StringBuilder()
				// Month is 0 based so add 1
						.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
			} else {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
			}
		}
		if (currentCalender == DATE_TO_CONTROL) {
			if (DateUtils.checkDateInOffsetMonth(sDay, sMonth, year, -2)) {
				edToDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
				edToDate.setText(new StringBuilder()
				// Month is 0 based so add 1
						.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
			} else {
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 0, null, false);
			}
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spLine) {
			if (selectedLineIndex != spLine.getSelectedItemPosition()) {
				selectedLineIndex = spLine.getSelectedItemPosition();
			}
		} else if (arg0 == spStaff) {
			if (selectedNVBHIndex != spStaff.getSelectedItemPosition()) {
				selectedNVBHIndex = spStaff.getSelectedItemPosition();
			}
		}
		onClick(btSearch);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isUpdateData = true;
				resetAllValue();
				getListSaleStaff();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
