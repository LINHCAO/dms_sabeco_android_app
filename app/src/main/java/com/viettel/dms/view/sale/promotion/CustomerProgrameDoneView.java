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
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerListDoneProgrameDTO;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.db.ProPeriodDTO;
import com.viettel.dms.dto.view.ComboBoxDisplayProgrameItemDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * DS Khach hang tham gia CT HTBH da thuc hien (San luong thuc hien)
 * 
 * @author: dungnt19
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerProgrameDoneView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener,
		OnItemSelectedListener { 

	public static final String TAG = CustomerProgrameDoneView.class.getName();

	private GlobalBaseActivity parent; // parent
	private DMSTableView tbCusList;// tbCusList
	private VNMEditTextClearable edMKH;// edMKH
	private Spinner spinnerPeriod;// spinner period
	private TextView tvLevel;// textview level
	private Spinner spinnerLevel;// spinner Level
	private Button btSearch; // btSearch 

	private String textCusCode = "";
	private String textCusCodeOld = "";

	private int selectedPeriod = 0;
	private int selectedLevel = 0;
	private boolean isUpdateData = false;

	// -- so trang
	int page;
	// -- so muc tham gia
	int totalLevel; 
	private boolean isBack = false; 
	// -- Bien flag de cho biet du lieu search co thay doi khong
	boolean isSearchOrSelectSpiner = false;
	// -- Trang hien tai
	private int currentPage = -1;
	// -- Danh sach khach hang
	public CustomerListDoneProgrameDTO cusDto = null;// cusList
	// -- Bien flag de biet vao man hinh lan dau tien
	boolean isFirst = true;
	// -- Render header cho table chua
	boolean isRenderHeaderTable = false;  
	// -- Danh sach cac muc
	List<ComboBoxDisplayProgrameItemDTO> listLevel = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
	// -- Thong tin chuong trinh
	private ProInfoDTO saleSupportDTO;
	// -- Chuong trinh co muc khong
	private boolean isHaveLevel = false;
	// -- get total page or not
	boolean isGetTotalPage; 
	// -- man hinh danh sach khach hang muon them
	CustomerAddSupportSalesView dialogCustomer; 
	// -- Dialog them san luong ban tham gia
	DialogAddDoneQuantity dialogAddDoneQuantity;
	boolean isAllowEdit;
	int type;
	boolean hasInput;
	// Chu ki co tu ngay bat dau den ket thuc cua thang khong
	boolean isFullMonth = false;
	long customerId;

	/**
	 * New Instance
	 * @author: quangvt1
	 * @since: 15:32:38 20-06-2014
	 * @return: CustomerProgrameDoneView
	 * @throws:  
	 * @param args
	 * @return
	 */
	public static CustomerProgrameDoneView newInstance(Bundle args) {
		CustomerProgrameDoneView f = new CustomerProgrameDoneView(); 
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_program_done_fragment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		//-- Set header
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_CUSTOMER_DONE));
		
		// -- Lay thong tin chuong trinh
		// -- 3 tab se gan vao COMMAND_HAVE_MENU, truong hop la tab dau tien thi get tu Arguments
		saleSupportDTO = (ProInfoDTO) getArguments().getSerializable(IntentConstants.INTENT_PROMOTION);
		// -- Neu dto != null co nghia la man hinh dau tien --> phai set dto vao COMMAND_HAVE_MENU
		if(saleSupportDTO != null) {
			FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE.setDataInfo(getArguments());
		} 
		// -- Neu dto == null co nghia la do chuyen tab --> lay dto tu COMMAND_HAVE_MENU
		else {
			Bundle bundle = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE.getDataInfo();
			saleSupportDTO = (ProInfoDTO) bundle.getSerializable(IntentConstants.INTENT_PROMOTION);
		}
		
		// -- Init tab
		MenuCommand menu = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE; 
		enableMenuBar(this, menu, FragmentMenuContanst.INDEX_MENU_NVBH_NEED_DONE_VIEW); 
		
		//-- Binding control & init view
		initView(view, v); 

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
		spinnerPeriod = (Spinner) v.findViewById(R.id.spinnerStatus);
		spinnerPeriod.setOnItemSelectedListener(this);
		selectedPeriod = 0; 

		// -- Muc (level)
		spinnerLevel = (Spinner) v.findViewById(R.id.spinnerLevel);
		spinnerLevel.setOnItemSelectedListener(this);
		selectedLevel = 0;

		// -- Search
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);  
		
		// -- Table customer
		tbCusList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
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
		// -- Data
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_ID, saleSupportDTO.PRO_INFO_ID);

		// -- Action
		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_DETAIL_PROGRAME_DONE;
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
 
		// Dong goi du lieu truyen xuong de lay danh sach khach hang
		Bundle bundle = new Bundle();
		// -- Trang nao?
		bundle.putInt(IntentConstants.INTENT_PAGE, page); 
		// -- Chu trinh(Period)
		if( saleSupportDTO.listPeriod.size() > selectedPeriod){
			bundle.putLong(IntentConstants.INTENT_PERIOD_ID, saleSupportDTO.listPeriod.get(selectedPeriod).PRO_PERIOD_ID);
		}
		// -- Co lay so trang khong
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		// -- Ma chuong trinh
		bundle.putLong(IntentConstants.INTENT_PROGRAM_ID, saleSupportDTO.PRO_INFO_ID);
		// -- Du lieu search khach hang
		bundle.putString(IntentConstants.INTENT_NAME, textCusCode);
		// -- Muc (Level)
		if (listLevel.size() > 0) {
			bundle.putString(IntentConstants.INTENT_LEVEL, listLevel.get(selectedLevel).value);
		} else {
			bundle.putString(IntentConstants.INTENT_LEVEL, "0");
		}
		bundle.putInt(IntentConstants.INTENT_TYPE, saleSupportDTO.TYPE);
		
		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.GET_LIST_CUSTOMER_DONE_PROGRAME;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_DETAIL_PROGRAME_DONE: {
			// Close dialog loading...
			parent.closeProgressDialog();
			
			// Lay thong tin chuong trinh
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
			// an spinner muc khi chuong trình chi co mot muc---------------
			if (totalLevel == 1) {
				// Khong co muc thi an combobox di
				tvLevel.setVisibility(View.INVISIBLE);
				spinnerLevel.setVisibility(View.INVISIBLE); 
				totalLevel = 1;
			}
			//----------------------------------
			isHaveLevel = (totalLevel > 1); 
			
			// Render cac chu ki tinh thuong da qua
			renderDataPeriod(saleSupportDTO.listPeriod);
			
			// Khoi tao header cho table
			layoutTableHeader();
			isRenderHeaderTable = true;
			
			// Lay danh sach khach hang
			getCustomerList(1, true);
			break;
		}
		case ActionEventConstant.GET_LIST_CUSTOMER_DONE_PROGRAME: {
			textCusCodeOld = edMKH.getText().toString().trim();
			if (isSearchOrSelectSpiner) {
				isSearchOrSelectSpiner = false;
				cusDto = null;
				currentPage = -1;
			}
			
			CustomerListDoneProgrameDTO tempDto = (CustomerListDoneProgrameDTO) modelEvent.getModelData();
			if (cusDto == null) {
				cusDto = tempDto;
			} else {
				cusDto.listCustomer = tempDto.listCustomer;
			}

			if (isUpdateData) {
				isUpdateData = false;
				currentPage = -1;
				cusDto.totalRow = tempDto.totalRow;
			}

			if (cusDto != null) {
				renderLayout();
			}
			parent.closeProgressDialog();
			break;
		}  
		case ActionEventConstant.SAVE_DONE_PRODUCT: { 
			parent.closeProgressDialog();
			dialogAddDoneQuantity.dismiss();
			parent.showDialog(StringUtil.getString(R.string.TEXT_DONE_PRODUCT_SUCCESS));
			
			cusDto = null;
			currentPage = -1;
			getCustomerList(1, true);
			break;
		} 
		case ActionEventConstant.GET_INPUT_QUANTITY_DONE: {
			CustomerInputQuantityDTO tempDto = (CustomerInputQuantityDTO) modelEvent.getModelData();
			tempDto.ALLOW_EDIT= isAllowEdit;
			tempDto.type= type;
			tempDto.hasInput=hasInput;
			tempDto.isFullMonth =isFullMonth ;
			tempDto.OBJECT_ID = customerId;
			dialogAddDoneQuantity.setCustomer(tempDto);
			parent.closeProgressDialog();
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * Render combobox Period
	 * @author: quangvt1
	 * @since: 11:31:39 12-06-2014
	 * @return: void
	 * @throws:  
	 * @param listPeriod
	 */
	private void renderDataPeriod(List<ProPeriodDTO> listPeriod) {
		List<String> list = new ArrayList<String>();
		for (ProPeriodDTO proPeriodDTO : listPeriod) {
			String strPeriod = getStringPeriod(proPeriodDTO);
			list.add(strPeriod);
		} 
		SpinnerAdapter adapterPeriod = new SpinnerAdapter(parent, R.layout.simple_spinner_item, list.toArray(new String[list.size()]));
		selectedPeriod = 0;
		spinnerPeriod.setAdapter(adapterPeriod);
	} 

	/**
	 * Lay chuoi thoi gian chu ki tinh thuong
	 * FromDate - ToDate : 1/1/14 - 31/1/14
	 * @author: quangvt1
	 * @since: 11:31:55 12-06-2014
	 * @return: String
	 * @throws:  
	 * @param proPeriodDTO
	 * @return
	 */
	private String getStringPeriod(ProPeriodDTO proPeriodDTO) { 
		String strFromDate = DateUtils.convertDateOneFromFormatToAnotherFormat(
				proPeriodDTO.FROM_DATE, DateUtils.DATE_FORMAT_NOW, "d/M/yy");
		String strToDate = DateUtils.convertDateOneFromFormatToAnotherFormat(
				proPeriodDTO.TO_DATE, DateUtils.DATE_FORMAT_NOW, "d/M/yy");
		
		return strFromDate + " - " + strToDate;
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
			tbCusList.setTotalSize(cusDto.totalRow, currentPage);
			tbCusList.getPagingControl().setCurrentPage(currentPage);
		} else {
			tbCusList.setTotalSize(cusDto.totalRow, 1);
			currentPage = tbCusList.getPagingControl().getCurrentPage();
		}

		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbCusList.getPagingControl().getCurrentPage() - 1);
		tbCusList.clearAllData();
		if (cusDto.listCustomer.size() > 0) {
//			tbCusList.hideNoContentRow();
			for (CustomerInputQuantityDTO item : cusDto.listCustomer) {
				if(isHaveLevel){
					CustomerProgramDoneRow row = new CustomerProgramDoneRow(parent, this, isHaveLevel);
					row.render(pos, item);
					pos++;
					tbCusList.addRow(row);
				}else{
					CustomerProgramDoneRow row = new CustomerProgramDoneRow(parent, this);
					row.render(pos, item);
					pos++;
					tbCusList.addRow(row);
				}
			}
		} else {
			tbCusList.showNoContentRow();
		}
	}
	
	/**
	 * tao layout cho header của bảng
	 * @author: hoanpd1
	 * @since: 15:19:36 14-11-2014
	 * @return: void
	 * @throws:
	 */
	public void layoutTableHeader() {  
		tbCusList.clearAllDataAndHeader();
		if(isHaveLevel){
			initHeaderTable(tbCusList, new CustomerProgramDoneRow(parent, this, true));
//			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 45, 73, 250, 388, 100, 84 };
//			String[] CUSTOMER_LIST_TABLE_TITLES = { 
//					StringUtil.getString(R.string.TEXT_STT),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
//					StringUtil.getString(R.string.TEXT_ADDRESS),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_LEVEL),
//					StringUtil.getString(R.string.TEXT_SL_DONE) };
//
//			tbCusList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		} else {
			initHeaderTable(tbCusList, new CustomerProgramDoneRow(parent, this));
//			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 45, 73, 250, 490, 84 };
//			String[] CUSTOMER_LIST_TABLE_TITLES = {
//					StringUtil.getString(R.string.TEXT_STT),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
//					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
//					StringUtil.getString(R.string.TEXT_ADDRESS),
//					StringUtil.getString(R.string.TEXT_SL_DONE) };
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
		default:
			break;
		}
	}
	
	@Override
	public void onEvent(int eventType, View control, Object data) { 
		switch (eventType) { 
		case DialogAddDoneQuantity.ACTION_ACCEPT_SAVE_DONE: { 
			CustomerInputQuantityDTO customer = (CustomerInputQuantityDTO) data;
			parent.showLoadingDialog();
			dialogAddDoneQuantity.saveQuantityDone(customer);
			break;
		}
		default:
			super.onEvent(eventType, control, data);
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

		selectedPeriod = 0;
		spinnerPeriod.setSelection(selectedPeriod);
		
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
		case ActionEventConstant.INPUT_QUANTITY_DONE: { 
			CustomerInputQuantityDTO item = (CustomerInputQuantityDTO) data;  
			isAllowEdit = item.ALLOW_EDIT;
			type = item.type;
			hasInput =item.hasInput;
			isFullMonth =item.isFullMonth;
			customerId =item.OBJECT_ID;
			dialogAddDoneQuantity = new DialogAddDoneQuantity(parent, this,item); 		 
			dialogAddDoneQuantity.show();
			dialogAddDoneQuantity.isGetTotalPage = true;
			dialogAddDoneQuantity.getProductList(1, true);
			dialogAddDoneQuantity.forceHideKeyBoardForDialog();
			break;
		} 
		default:
			break;
		}
	} 

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerPeriod) {
			if (selectedPeriod != spinnerPeriod.getSelectedItemPosition()) {
				selectedPeriod = spinnerPeriod.getSelectedItemPosition();
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
