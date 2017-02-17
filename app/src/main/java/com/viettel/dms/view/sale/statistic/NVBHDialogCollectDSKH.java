/**
\ * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.AbstractAlertDialog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.sale.customer.CustomerListRowForOpponentSale;
import com.viettel.sabeco.R;

/**
 * NVBHDialogCollectDSKH.java
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 11:31:16 AM Mar 6, 2014
 */
public class NVBHDialogCollectDSKH extends AbstractAlertDialog implements
		android.view.View.OnClickListener, VinamilkTableListener,
		OnItemSelectedListener {

	// Views
	// button tim kiem khach hang
	private Button btnSearchCus;
	// spinner tuyen
	private Spinner spCusTuyen;
	// spinner khach hang c2
	private Spinner spCusC2;
	// editText nhap thong tin tim kiem
	private VNMEditTextClearable edChooseCus;
	// table hien thi danh sach khach hang
	private VinamilkTableView tbCus;
	// parameters
	// vi tri dang duoc chon tren spinner
	private int currentSelectedPlan;
	// vi tri dang duoc chon tren spinner C2
	private int currentSelectedC2;
	// danh sach khach hang
	private CustomerListDTO customerListDTO;
	// danh sach khach hang C2
	private CustomerListDTO customerC2;
	// trang hien tai dang duoc chon tren table
	private int currentPage;
	// co lay tong so trang ko?!
	public boolean isGetTotalPage;
	// thong tin khach hang duoc chon
	private CustomerListItem customerListItem;
	
	private final int INDEX_NONE_SELECT_PLAN = 0;
	private final int INDEX_NONE_SELECT_C2 = 0;
	
	// mang tuyen
	private static final String ARRAY_LINE_CHOOSE[] = { "", "Tất cả", "Thứ 2",
			"Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật" };

	/**
	 * @param context
	 * @param base
	 * @param title
	 */
	public NVBHDialogCollectDSKH(Context context, BaseFragment base,
			CharSequence title, CustomerListDTO cusC2) {
		super(context, base, title);
		// TODO Auto-generated constructor stub
		View v = setViewLayout(R.layout.dialog_choose_customer);
		initView(v);
		if (customerListDTO == null) {
			CustomerListRowForOpponentSale row = new CustomerListRowForOpponentSale(parent, this);
			tbCus.getHeaderView().addHeader(row.getHeaderForTable());
			isGetTotalPage = true; 
			
			if(cusC2 == null){
				getListCusC2OnPlan(); 
			}else{
				this.customerC2 = cusC2;
			}
		} else {
			renderLayout();
		}
	}

	/**
	 * Lay danh sach khach hang C2 trong tuyen cua nhan vien
	 * @author: quangvt1
	 * @since: 17:37:41 05-05-2014
	 * @return: void
	 * @throws:
	 */
	private void getListCusC2OnPlan() {
		parent.showLoadingDialog(); 

		Bundle bundle = new Bundle(); 
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		bundle.putLong(IntentConstants.INTENT_SHOP_ID,Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		bundle.putInt(IntentConstants.INTENT_PAGE, -1); // Load 1 lan het luon
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, false); 
		
		setHandleViewEvent(ActionEventConstant.GET_LIST_CUSTOMER_C2_ON_PLAN, bundle, SaleController.getInstance());
	}

	/**
	 * render layout
	 * 
	 * @author: dungdq3
	 * @since: 9:31:22 AM Mar 7, 2014
	 * @return: void
	 * @throws:
	 */
	public void renderLayout() { 
		// TODO Auto-generated method stub
		if (isGetTotalPage)
			tbCus.setTotalSize(customerListDTO.totalCustomer);
		if (currentPage > 0)
			tbCus.getPagingControl().setCurrentPage(currentPage);
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbCus.getPagingControl().getCurrentPage() - 1);
		if (customerListDTO.cusList.size() > 0) {
			tbCus.clearAllData();
			for (CustomerListItem customerListItem : customerListDTO.cusList) {
				CustomerListRowForOpponentSale row = new CustomerListRowForOpponentSale(parent, this);
				row.renderLayout(pos, customerListItem);
				pos++;
				tbCus.addRow(row);
				if (this.customerListItem == null) {
					row.setBackgroundRowByColor(ImageUtil.getColor(R.color.WHITE));
				} else if (customerListItem.aCustomer.customerId == this.customerListItem.aCustomer.customerId) {
					row.setBackgroundRowByColor(ImageUtil.getColor(R.color.LIGHT_GRAY_BG));
				}
			}
		} else {
			tbCus.showNoContentRowWithString(StringUtil.getString(R.string.MESSAGE_NO_CUSTOMER_INFO));
		}
		forceHideKeyBoardForDialog();
	}

	/**
	 * Lay danh sach khach hang
	 * 
	 * @author: dungdq3
	 * @param page
	 * @since: 9:31:20 AM Mar 7, 2014
	 * @return: void
	 * @throws:
	 */
	public void getCustomerList(int page) {  
		// show dialog loading....
		parent.showLoadingDialog();
		
		this.currentPage = page;
		
		// Loai request
		int typeRequest = 0;
		if(currentSelectedPlan == INDEX_NONE_SELECT_PLAN && currentSelectedC2 == INDEX_NONE_SELECT_C2){
			typeRequest = 0; // lay ca 2
		}else if(currentSelectedPlan != INDEX_NONE_SELECT_PLAN){
			typeRequest = 1; // lay danh sach khach hang trong tuyen
		}else if(currentSelectedC2 != INDEX_NONE_SELECT_C2){
			typeRequest  = 2; // lay danh sach khach hang tu c2
		}
		
		// Lay chuoi search 
		String strSearch = edChooseCus.getText().toString().trim();
		// Staff id
		long staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		// ShopId
		long shopId = Long.parseLong(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		// plan
		String plan = DateUtils.getVisitPlan(ARRAY_LINE_CHOOSE[currentSelectedPlan]);
		// id customer c2
		long cusId = this.customerC2.cusList.get(currentSelectedC2).aCustomer.customerId;
		
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		bundle.putLong(IntentConstants.INTENT_SHOP_ID, shopId);
		bundle.putString(IntentConstants.INTENT_VISIT_PLAN, plan);
		bundle.putLong(IntentConstants.INTENT_CUSTOMER_ID, cusId);
		
		// Loai lay danh sach khach hang
		// 0: danh sach khach hang trong tuyen va danh sach khach hang tu C2
		// 1: danh sach khach hanf trong tuyen
		// 2: danh sach khach ahnf tu c2 
		bundle.putInt(IntentConstants.INTENT_TYPE_GET_CUSTOMER, typeRequest);
		
		// false: ko lay ngoai tuyen, true lay them ngoai tuyen voi dk da ghe tham
		bundle.putBoolean(IntentConstants.INTENT_GET_WRONG_PLAN, false);
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		// check data search
		bundle.putInt(IntentConstants.INTENT_ROLE_TYPE, 0);
		bundle.putString(IntentConstants.INTENT_DATA, strSearch);

		setHandleViewEvent(ActionEventConstant.GET_CUSTOMER_LIST_FOR_OPPONENT_SALE, bundle, SaleController.getInstance());
	}

	/**
	 * Load view
	 * 
	 * @author: dungdq3
	 * @since: 11:49:17 AM Mar 6, 2014
	 * @return: void
	 * @throws:
	 * @param v
	 */
	private void initView(View v) {
		// button search
		btnSearchCus = (Button) v.findViewById(R.id.btSearchCus);
		btnSearchCus.setOnClickListener(this);

		// editext choose customer
		edChooseCus = (VNMEditTextClearable) v.findViewById(R.id.vnmChooseCus);

		// table customer
		tbCus = (VinamilkTableView) v.findViewById(R.id.vnmTableCus);
		tbCus.setListener(this); 
		
		// Combobox tuyen
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, ARRAY_LINE_CHOOSE); 
		spCusTuyen = (Spinner) v.findViewById(R.id.spCusTuyen);
		spCusTuyen.setAdapter(adapterLine);
		currentSelectedPlan = 0;
		spCusTuyen.setSelection(0);
		spCusTuyen.setOnItemSelectedListener(this);
		
		// Combobox khach hang c2
		spCusC2 = (Spinner) v.findViewById(R.id.spCusC2);
		spCusC2.setAdapter(adapterLine);
		currentSelectedC2 = 0;
		spCusC2.setSelection(0);
		spCusC2.setOnItemSelectedListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnSearchCus) {
			requestGetListCustomer();
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbCus) {
			currentPage = tbCus.getPagingControl().getCurrentPage();
			isGetTotalPage = false;
			getCustomerList(currentPage);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.UPDATE_CUSTOMER:
			setEventFromAlert(action, control, data);
			customerListItem = (CustomerListItem) data;
			dismiss();
			break;

		default:
			break;
		}
	}
	
	/**
	 * Reset ve khong select khach hang nao
	 * @author: quangvt1
	 * @since: 14:00:08 08-05-2014
	 * @return: void
	 * @throws:
	 */
	public void resetDontSelectCustomer(){
		this.customerListItem = null;
	}
	
	/**
	 * Select 1 khach hang
	 * @author: quangvt1
	 * @since: 14:00:22 08-05-2014
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	public void setCustomerSelected(CustomerListItem item){
		this.customerListItem = item;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View v, int arg2, long arg3) {
		// Khi select tuyen thi mac dinh khong select C2 va nguoc lai 
		if (arg0 == spCusTuyen && currentSelectedPlan != spCusTuyen.getSelectedItemPosition()) {
			currentSelectedPlan = spCusTuyen.getSelectedItemPosition();
			
			// set combobox C2 none select
			currentSelectedC2 = INDEX_NONE_SELECT_C2;
			spCusC2.setSelection(currentSelectedC2);
			
			// Lay lai danh sach khach hang
			requestGetListCustomer();
		}else if(arg0 == spCusC2 && currentSelectedC2 != spCusC2.getSelectedItemPosition()){
			currentSelectedC2 = spCusC2.getSelectedItemPosition();
			
			// set combobox plan none select
			currentSelectedPlan = INDEX_NONE_SELECT_PLAN;
			spCusTuyen.setSelection(currentSelectedPlan);
			
			// Lay lai danh sach khach hang
			requestGetListCustomer();
		}
	}

	/**
	 * Request lay danh sach customer
	 * @author: quangvt1
	 * @since: 18:54:03 05-05-2014
	 * @return: void
	 * @throws:
	 */
	private void requestGetListCustomer() {
		isGetTotalPage = true;
		currentPage = 1;
		getCustomerList(1);
	}
	

	@Override
	public void onNothingSelected(AdapterView<?> arg0) { 
	}

	/**
	 * set CustomerListDTO
	 * 
	 * @author: dungdq3
	 * @since: 8:09:25 AM Mar 10, 2014
	 * @return: void
	 * @throws:
	 * @param customerListDTO
	 */
	public void setCustomerListDTO(CustomerListDTO customerListDTO) {
		this.customerListDTO = customerListDTO;
	}
	
	/**
	 * set CustomerListDTO
	 * 
	 * @author: dungdq3
	 * @since: 8:09:25 AM Mar 10, 2014
	 * @return: void
	 * @throws:
	 */
	public void setCustomerC2(CustomerListDTO cusC2) {
		this.customerC2 = cusC2;
		insertItemNoneToC2();
		updateComboboxC2();
	} 
	
	/**
	 * Insert them item none select vao danh sach khach hang C2
	 * @author: quangvt1
	 * @since: 13:34:59 06-05-2014
	 * @return: void
	 * @throws:
	 */
	private void insertItemNoneToC2() {
		 CustomerListItem none = new CustomerListItem();
		 none.aCustomer = new CustomerDTO();
		 none.aCustomer.customerName = "";
		 none.aCustomer.customerId = -1;
		 
		 this.customerC2.cusList.add(0, none);
	}

	private void updateComboboxC2() {
		int count = 0; 
		count += this.customerC2.cusList.size(); // So luong khach hang c2 
		
		String[] arrayCusC2 = new String[count]; 
		
		// them danh danh sach khach hang c2
		int index = 0;
		for (CustomerListItem item : this.customerC2.cusList) {
			arrayCusC2[index++] = item.aCustomer.customerName;
		}
		
		SpinnerAdapter adapterCusC2 = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrayCusC2);  
		spCusC2.setAdapter(adapterCusC2);
	}

	/**
	 * Tra ve gia tri mac dinh
	 * @author: quangvt1
	 * @since: 15:00:10 06-05-2014
	 * @return: void
	 * @throws:
	 */
	public void resetValueSelected(){
		currentSelectedC2 = INDEX_NONE_SELECT_C2;
		currentSelectedPlan = INDEX_NONE_SELECT_PLAN;
		currentPage = 1;
		
		spCusC2.setSelection(currentSelectedC2);
		spCusTuyen.setSelection(currentSelectedPlan);
		edChooseCus.setText(Constants.STR_BLANK);
	}
	
	@Override
	public void onShow(DialogInterface dialog) {
		// TODO Auto-generated method stub
		super.onShow(dialog);
		forceHideKeyBoardForDialog();
	}

}
