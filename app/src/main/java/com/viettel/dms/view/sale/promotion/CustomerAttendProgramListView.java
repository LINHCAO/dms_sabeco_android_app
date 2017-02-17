/*
8 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.FragmentMenuContanst.MenuCommand;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.ComboBoxDisplayProgrameItemDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.dto.view.ListProductQuantityJoin;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * DS Khach hang tham gia CT HTBH CustomerAttendProgramListView.java
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerAttendProgramListView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener,
		OnItemSelectedListener {
	public static final String TAG = CustomerAttendProgramListView.class.getName();
	private static final int ACTION_CANCEL = -2;
	private static final int ACTION_OK_DELETE = -5;
	 // parent
	private GlobalBaseActivity parent;
	// tbCusList
	private DMSTableView tbCusList;
	// edMKH
	private VNMEditTextClearable edMKH;
	// spinner status
	private Spinner spinnerStatus;
	// textview level
	private TextView tvLevel;
	// spinner Level
	private Spinner spinnerLevel;
	// btSearch
	private Button btSearch; 
	//button them khach hang
	private Button btAddCustomer;

	private String textCusCode = "";
	private String textCusCodeOld = "";

	private int selectedStatus = 0;
	private int selectedLevel = 0;
	private boolean isUpdateData = false;

	private boolean isBack = false;
	public boolean isBackFromPopStack = false; 
	// -- Bien flag de cho biet du lieu search co thay doi khong
	boolean isSearchOrSelectSpiner = false;
	private int currentPage = -1;
	public CustomerAttendProgramListDTO cusDto = null;// cusList
	boolean isFirst = true;
	boolean isRenderHeaderTable = false;

	List<ComboBoxDisplayProgrameItemDTO> listStatus = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
	List<ComboBoxDisplayProgrameItemDTO> listLevel = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
	private ProInfoDTO saleSupportDTO;
	private boolean isHaveLevel = false;
	// so muc tham gia
	int totalLevel;

	// so trang
	int page;
	// get total page or not
	boolean isGetTotalPage;
	// man hinh danh sach khach hang muon them
	CustomerAddSupportSalesView dialogCustomer;
	// Dialog them san luong ban tham gia
	DialogAddJoinQuantity dialogAddQuantity;

	public static CustomerAttendProgramListView newInstance(Bundle args) {
		CustomerAttendProgramListView f = new CustomerAttendProgramListView();
		// Supply index input as an argument.
		args = (args != null ? args : new Bundle());
		args.putBoolean("isReload", true);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_attend_program_list_fragment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_ATTEND));
		Bundle bundle = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE.getDataInfo();
		saleSupportDTO = (ProInfoDTO) bundle.getSerializable(IntentConstants.INTENT_PROMOTION);
		
		MenuCommand menu = null;
		if(saleSupportDTO.isHaveDone){
			menu = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE;
			enableMenuBar(this, menu, FragmentMenuContanst.INDEX_MENU_NVBH_CUSTOMER_ATTEND_PROGRAM); 
		}else{
			menu = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMANDs;
			enableMenuBar(this, menu, FragmentMenuContanst.INDEX_MENU_NVBH_CUSTOMER_ATTEND_PROGRAM - 1); 
		}
		 
		initView(view, v);
		// reset lai bien isInsertingActionLogVisit
		parent.isInsertingActionLogVisit = false;

		if (isFirst) {
			// Lay thong tin chi tiet cua chuong trinh
			getDetailPrograme();
		} else {
			isBack = true;
		}

		return v;
	}

	/**
	 * Khoi tao cac control
	 * 
	 * @author: quangvt1
	 * @since: 16:54:04 15-05-2014
	 * @return: void
	 * @throws:
	 * @param view
	 * @param v
	 */
	private void initView(ViewGroup view, View v) {
		// -- Edittext Search
		edMKH = (VNMEditTextClearable) v.findViewById(R.id.edCusCode);

		// -- Trang thai
		tvLevel = (TextView) v.findViewById(R.id.tvLevel);
		spinnerStatus = (Spinner) v.findViewById(R.id.spinnerStatus);
		spinnerStatus.setOnItemSelectedListener(this);
		selectedStatus = 0;
		initDataStatus();

		// -- Muc (level)
		spinnerLevel = (Spinner) v.findViewById(R.id.spinnerLevel);
		spinnerLevel.setOnItemSelectedListener(this);
		selectedLevel = 0;

		// -- Search
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);

		// -- Add Customer
		btAddCustomer = (Button) v.findViewById(R.id.btAddCustomer);
		btAddCustomer.setOnClickListener(this);
		// -- Table customer
		tbCusList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
//		layoutTableHeader();
	}

	/**
	 * Lay thong tin chi tiet cua chuong trinh
	 * 
	 * @author: dungnt19
	 * @since: 11:05:05 11-05-2014
	 * @return: void
	 * @throws:
	 */
	private void getDetailPrograme() {
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_ID, saleSupportDTO.PRO_INFO_ID);
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_DETAIL_PROGRAME;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * getCustomerList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getCustomerList(int page, boolean isGetTotalPage) {
		// Show dialog loading...
		if (!parent.isShowProgressDialog()) {
			parent.showLoadingDialog();
		}

		// Get UserData
		UserDTO user = GlobalInfo.getInstance().getProfile().getUserData();

		// Dong goi du lieu truyen xuong de lay danh sach khach hang
		Bundle bundle = new Bundle();
		// -- Trang nao?
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		// -- StaffId
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, user.id);
		// -- ShopId
		bundle.putLong(IntentConstants.INTENT_SHOP_ID, Long.parseLong(user.shopId));
		// -- Trang thai (Status)
		bundle.putString(IntentConstants.INTENT_STATUS, listStatus.get(selectedStatus).value);
		// -- Co lay so trang khong
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		// -- Ma chuong trinh
		bundle.putLong(IntentConstants.INTENT_ID, saleSupportDTO.PRO_INFO_ID);
		// -- Du lieu search khach hang
		bundle.putString(IntentConstants.INTENT_NAME, textCusCode);
		// -- Muc (Level)
		if (listLevel.size() > 0) {
			bundle.putString(IntentConstants.INTENT_LEVEL, listLevel.get(selectedLevel).value);
		} else {
			bundle.putString(IntentConstants.INTENT_LEVEL, "0");
		}

		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_CUSTOMER_ATTEND;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_DETAIL_PROGRAME: {
			// Close dialog loading...
			parent.closeProgressDialog();

			ProInfoDTO tmp = (ProInfoDTO) modelEvent.getModelData();
			saleSupportDTO.copyPropertiesForDTO(tmp);
			totalLevel = saleSupportDTO.totalLevel; 
			
			if ((saleSupportDTO.TYPE != 3 && saleSupportDTO.TYPE != 4) || totalLevel == 0) {
				// Khong co muc thi an combobox di
				tvLevel.setVisibility(View.INVISIBLE);
				spinnerLevel.setVisibility(View.INVISIBLE);
				totalLevel = 0;
			} else {
				// Hien thi so muc cua chuong trinh
				tvLevel.setVisibility(View.VISIBLE);
				spinnerLevel.setVisibility(View.VISIBLE);
				renderDataLevel(saleSupportDTO.listLevel);
			}
			if (totalLevel == 1) {
				// Khong co muc thi an combobox di
				tvLevel.setVisibility(View.INVISIBLE);
				spinnerLevel.setVisibility(View.INVISIBLE); 
				totalLevel = 1;
			}
			isHaveLevel = (totalLevel > 1);
			
			layoutTableHeader();
			isRenderHeaderTable = true;
			
			getCustomerList(1, true);
			break;
		}
		case ActionEventConstant.GET_LIST_CUSTOMER_ATTEND: {
			textCusCodeOld = edMKH.getText().toString().trim();
			if (isSearchOrSelectSpiner) {
				isSearchOrSelectSpiner = false;
				cusDto = null;
				currentPage = -1;
			}
			
			CustomerAttendProgramListDTO tempDto = (CustomerAttendProgramListDTO) modelEvent.getModelData();
			if (cusDto == null) {
				cusDto = tempDto;
			} else {
				cusDto.cusList = tempDto.cusList;
			}

			if (isUpdateData) {
				isUpdateData = false;
				currentPage = -1;
				cusDto.totalCustomer = tempDto.totalCustomer;
			}

			if (cusDto != null) {
				renderLayout();
			}
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHDIEMBANTHAMGIA, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		case ActionEventConstant.DELETE_CUSTOMER_JOIN_HTBH:{
			// Close dialog loading...
			parent.closeProgressDialog();
			parent.showDialogAutoClose(StringUtil.getString(R.string.TEXT_ALERT_DELETE_CUSTOMER_SUCCESS), 2000);
			
			resetAllValue();
			getCustomerList(1, true);
			break;
		}
		case ActionEventConstant.GET_PRODUCT_LIST_JOIN:{
			ListProductQuantityJoin dto = (ListProductQuantityJoin) modelEvent.getModelData(); 
			dialogAddQuantity.setProductList(dto);
			dialogAddQuantity.renderLayout();

			// Close dialog loading...
			parent.closeProgressDialog(); 
			break;
		}
		
		case ActionEventConstant.GET_LIST_CUSTOMER_ADD_SUPPORT_SALES: {
			CustomerAttendProgramListDTO tempDto = (CustomerAttendProgramListDTO) modelEvent.getModelData();
			dialogCustomer.setCustomerC2(tempDto);
			requestInsertLogKPI(HashMapKPI.NVBH_THEMDIEMBANTHAMGIA, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		case ActionEventConstant.SAVE_CHOOSE_CUSTOMER_ATTEND: {
			resetAllValue();
			getCustomerList(1, true);
			parent.closeProgressDialog();
			break;
		}
		case ActionEventConstant.SAVE_JOIN_PRODUCT: { 
			parent.closeProgressDialog();
			dialogAddQuantity.dismiss();
			parent.showDialog(StringUtil.getString(R.string.TEXT_JOIN_PRODUCT_SUCCESS));
			
			cusDto = null;
			currentPage = -1;
			getCustomerList(1, true);
			break;
		} 

		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * Tao du lieu cho combobox trang thai
	 * 
	 * @author: quangvt1
	 * @since: 17:03:12 15-05-2014
	 * @return: void
	 * @throws:
	 */
	private void initDataStatus() {
		// Trang thai
		ComboBoxDisplayProgrameItemDTO arg = new ComboBoxDisplayProgrameItemDTO();
		arg.value = "-1";
		arg.name = StringUtil.getString(R.string.ALL);
		listStatus.add(arg);

		arg = new ComboBoxDisplayProgrameItemDTO();
		arg.value = "0";
		arg.name = StringUtil.getString(R.string.TEXT_STATUS_WAIT_FOR_APPROVE);
		listStatus.add(arg);

		arg = new ComboBoxDisplayProgrameItemDTO();
		arg.value = "1";
		arg.name = StringUtil.getString(R.string.TEXT_STATUS_ATTEND);
		listStatus.add(arg);

		arg = new ComboBoxDisplayProgrameItemDTO();
		arg.value = "2";
		arg.name = StringUtil.getString(R.string.TEXT_STATUS_PAUSE_ATTEND);
		listStatus.add(arg);

		String[] statusArray = new String[] {
				StringUtil.getString(R.string.ALL),
				StringUtil.getString(R.string.TEXT_STATUS_WAIT_FOR_APPROVE),
				StringUtil.getString(R.string.TEXT_STATUS_ATTEND),
				StringUtil.getString(R.string.TEXT_STATUS_PAUSE_ATTEND) };
		SpinnerAdapter adapterStatus = new SpinnerAdapter(parent, R.layout.simple_spinner_item, statusArray);
		spinnerStatus.setAdapter(adapterStatus);
	}

	/**
	 * Render cac muc cua chuong trinh
	 * 
	 * @author: quangvt1
	 * @since: 16:31:24 15-05-2014
	 * @return: void
	 * @throws:
	 * @param totalLevel
	 *            : tong so muc cua chuong trinh
	 */
	private void renderDataLevel(List<ComboBoxDisplayProgrameItemDTO> listLevel1) {
		ArrayList<String> levelArray = new ArrayList<String>();
		ComboBoxDisplayProgrameItemDTO arg0 = new ComboBoxDisplayProgrameItemDTO();
		arg0.value = "0";
		arg0.name = StringUtil.getString(R.string.ALL);
		levelArray.add(StringUtil.getString(R.string.ALL));
		listLevel.add(arg0);

		for (int i = 0; i < listLevel1.size(); i++) {
			arg0 = new ComboBoxDisplayProgrameItemDTO();
			arg0.value = (i + 1) + "";
			String level = listLevel1.get(i).name;
			arg0.name = level;
			levelArray.add(level);

			listLevel.add(arg0);
		}

		String[] levelArr = levelArray.toArray(new String[levelArray.size()]);
		SpinnerAdapter adapterLevel = new SpinnerAdapter(parent, R.layout.simple_spinner_item, levelArr);
		spinnerLevel.setAdapter(adapterLevel);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (!isRenderHeaderTable) {
			layoutTableHeader();
			isRenderHeaderTable = true;
		}
		if (currentPage != -1) {
			if (isBack) {
				isBack = false; 
			}
			tbCusList.setTotalSize(cusDto.totalCustomer, currentPage);
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		} else {
			tbCusList.setTotalSize(cusDto.totalCustomer, 1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbCusList.getPagingControl().getCurrentPage() - 1);
		tbCusList.clearAllData();
		if (cusDto.cusList.size() > 0) {
//			tbCusList.hideNoContentRow();
			for (CustomerAttendProgramListItem item : cusDto.cusList) {
				if(isHaveLevel){
					CustomerAttendProgramListRow row = new CustomerAttendProgramListRow(parent, this, saleSupportDTO.TYPE, isHaveLevel);
					row.render(pos, item, isHaveLevel);
					pos++;
					tbCusList.addRow(row);
				}else{
					CustomerAttendProgramListRow row = new CustomerAttendProgramListRow(parent, this, saleSupportDTO.TYPE);
					row.render(pos, item, isHaveLevel);
					pos++;
					tbCusList.addRow(row);
				}
			}
		} else {
			tbCusList.showNoContentRow();
		}

	}

	public void layoutTableHeader() {  
		tbCusList.clearAllDataAndHeader();
		if(isHaveLevel){
			initHeaderTable(tbCusList, new CustomerAttendProgramListRow(parent, this, 0, true));
//			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 45, 58, 200, 275, 70, 120, 100, 70 };
//			String[] CUSTOMER_LIST_TABLE_TITLES = { 
//					StringUtil.getString(R.string.TEXT_STT),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
//					StringUtil.getString(R.string.TEXT_ADDRESS),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_LEVEL),
//					StringUtil.getString(R.string.TEXT_DATE_ATTEND),
//					StringUtil.getString(R.string.TEXT_FOLLOW_PROBLEM_STATUS),
//					" " };
//
//			tbCusList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		} else {
			initHeaderTable(tbCusList, new CustomerAttendProgramListRow(parent, this, 0));
//			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 45, 58, 200, 345, 120, 100, 70 };
//			String[] CUSTOMER_LIST_TABLE_TITLES = {
//					StringUtil.getString(R.string.TEXT_STT),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
//					StringUtil.getString(R.string.TEXT_ADDRESS),
//					StringUtil.getString(R.string.TEXT_DATE_ATTEND),
//					StringUtil.getString(R.string.TEXT_FOLLOW_PROBLEM_STATUS),
//					" " };
//
//			tbCusList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}

	}

	/**
	 * di toi man hinh chi tiet Khach hang
	 * 
	 * @author : TamPQ since : 10:59:39 AM
	 */
	public void gotoCustomerInfo(String customerId) { 
		
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-01");
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getMenuIndexString());
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {

		switch (eventType) {
		case ACTION_OK_DELETE:{
			CustomerAttendProgramListItem item = (CustomerAttendProgramListItem) data;
			
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_ID, item.pro_cus_map_id);
			
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.viewData = bundle;
			e.action = ActionEventConstant.DELETE_CUSTOMER_JOIN_HTBH;
			SaleController.getInstance().handleViewEvent(e); 
			break;
		}
		case DialogAddJoinQuantity.ACTION_ACCEPT_SAVE_JOIN: { 
			ListProductQuantityJoin listProductChange = (ListProductQuantityJoin) data;
			parent.showLoadingDialog();
			dialogAddQuantity.saveQuantityJoinAfterConfirm(listProductChange);
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSearch: {
			// hide ban phim
			GlobalUtil.forceHideKeyboard(parent);

			textCusCode = edMKH.getText().toString().trim();
			boolean isChangeCuscode = !textCusCode.equalsIgnoreCase(textCusCodeOld);

			if (isChangeCuscode || isSearchOrSelectSpiner) {
				isSearchOrSelectSpiner = true;
				getCustomerList(1, true);
			}

			break;
		}
		// Hien thi popup them khach hang tham gia
		case R.id.btAddCustomer: {
			dialogCustomer = new CustomerAddSupportSalesView(parent, this, StringUtil.getString(R.string.TEXT_SELECT_CUSTOMER), cusDto.cusList);
			dialogCustomer.show();
			dialogCustomer.renderDataLevel(saleSupportDTO.listLevel);
			dialogCustomer.isGetTotalPage = true;
			dialogCustomer.getAddCustomerList(1, true);
			break;
		}

		default:
			break;
		}
	}

	/**
	 * Reset cac gia tri khi nhan cap nhat
	 * @author: dungnt19
	 * @since: 1.0
	 * @return: void
	 * @throws:
	 */
	private void resetAllValue() {
		selectedLevel = 0;
		spinnerLevel.setSelection(selectedLevel);

		selectedStatus = 0;
		spinnerStatus.setSelection(selectedStatus);
		
		textCusCode = ""; 
		edMKH.setText(textCusCode);
		
		currentPage = -1;
		cusDto = null;
		
		isRenderHeaderTable = false;
		
		isSearchOrSelectSpiner = false;
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusList) {

			currentPage = tbCusList.getPagingControl().getCurrentPage();
			getCustomerList(currentPage, false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int act, View control, Object data) {
		switch (act) {
		case ActionEventConstant.ADD_JOIN_QUANTITY: { 
			CustomerAttendProgramListItem item = (CustomerAttendProgramListItem) data;  
			dialogAddQuantity = new DialogAddJoinQuantity(parent, this);
			dialogAddQuantity.setProInfo(saleSupportDTO); 
			
			dialogAddQuantity.show(); 
			dialogAddQuantity.setCustomer(item); 
			dialogAddQuantity.getProductList();
			dialogAddQuantity.forceHideKeyBoardForDialog();
			break;
		}
		case ActionEventConstant.DELETE_CUSTOMER: {
			CustomerAttendProgramListItem item = (CustomerAttendProgramListItem) data;
				
			// Show dialog confirm delete customer  
			CharSequence strConfirm = getStringConfirmDeleteCusJoinHTBH(item.customerName);
			String strOk 	 = StringUtil.getString(R.string.TEXT_AGREE);
			String strCancel = StringUtil.getString(R.string.TEXT_DENY); 
			GlobalUtil.showDialogConfirm(this, this.parent, strConfirm, strOk, ACTION_OK_DELETE, strCancel, ACTION_CANCEL, item);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * Tao chuoi thong bao notify xoa khach hang
	 * @author: quangvt1
	 * @since: 15:15:32 21-05-2014
	 * @return: CharSequence
	 * @throws:  
	 * @param customerName
	 * @return
	 */
	private CharSequence  getStringConfirmDeleteCusJoinHTBH(String customerName) {  
		SpannableObject spanObject = new SpannableObject();
		spanObject.addSpan(StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_DELETE_CUSTOMER_JOIN_HTBH_PREFIX)); 
		spanObject.addSpan(" " + customerName, ImageUtil.getColor(R.color.WHITE),
				android.graphics.Typeface.BOLD);
		spanObject.addSpan(" ");
		spanObject.addSpan(StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_DELETE_CUSTOMER_JOIN_HTBH_POST)); 
		return spanObject.getSpan();
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerStatus) {
			if (selectedStatus != spinnerStatus.getSelectedItemPosition()) {
				selectedStatus = spinnerStatus.getSelectedItemPosition();
				isSearchOrSelectSpiner = true;
				onClick(btSearch);
			}
		} else if (arg0 == spinnerLevel) {
			if (selectedLevel != spinnerLevel.getSelectedItemPosition()) {
				selectedLevel = spinnerLevel.getSelectedItemPosition();
				isSearchOrSelectSpiner = true;
				onClick(btSearch);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

	@Override
	public void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) { 
				isUpdateData = true;
				resetAllValue();
				getDetailPrograme();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
