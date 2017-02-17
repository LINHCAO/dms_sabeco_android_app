/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.NewCustomerItem;
import com.viettel.dms.dto.view.NewCustomerListDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
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
 * Danh sách Khách hàng thêm mới chưa được duyệt 
 * NVBHListNewCustomer
 * @author: duongdt3
 * @since:  08:56:46 03/01/2014
 * @update: 08:56:46 03/01/2014
 */
public class NewCustomerListView extends BaseFragment implements OnEventControlListener, VinamilkTableListener, OnItemSelectedListener {
	//TAG of fragment
	public static final String TAG = NewCustomerListView.class.getName();
	//parent Activity
	private GlobalBaseActivity parent;
	
	//BEGIN USER INFO:  
	private String userId;
	private String shopId;
	//END USER INFO
	
	//BEGIN PAGING: Num row in page, current page
	private static final int NUM_ITEM_PER_PAGE = Constants.NUM_ITEM_PER_PAGE;
	private static final int ACTION_VIEW_CUSTOMER_INFO = -1;
	private static final int ACTION_DELETE_CUSTOMER = -2;
	private static final int ACTION_CANCEL_CUSTOMER = -3;
	private int currentPage = -1;
	//END PAGING
	
	//BEGIN DECLARE
	private VNMEditTextClearable edTKH;
	private Spinner spinnerState;
	//index spinner
	private int currentIndexSpinnerState = 0;
	private Button btSearchCustomer;
	private Button btCreateCustomer;
	private DMSTableView tbNewCustomer;
	//END DECLARE
	private NewCustomerListDTO dto;
	private int totalItem = 0;
	private String lastCustomerNameSearch = "";
	private long customerIdDelete = 0;
	
	//BEGIN NEW INSTANCE
	public static NewCustomerListView newInstance(Bundle b) {
		NewCustomerListView instance = new NewCustomerListView();
		instance.setArguments(b);
		return instance;
	}

	public static NewCustomerListView getInstance(Bundle b) {
		return newInstance(b);
	}
	//END NEW INSTANCE
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_list_new_customer, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		initUserInfo();
		initView(v);
		initHeaderText();
		initHeaderMenu();
		if (dto == null) {
			initData();
		}else{
			//tbNewCustomer.getPagingControl().setCurrentPage(currentPage);
			renderLayout();
		}
		
		return v;
	}
	
	/**
	 * initUserInfo from other View send to this
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	private void initUserInfo() {
		userId = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
	}
	
	/**
	 * init View in layout
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	private void initView(View view) {
		edTKH = (VNMEditTextClearable) view.findViewById(R.id.edTKH);
		spinnerState = (Spinner) view.findViewById(R.id.spinnerState);
		btSearchCustomer = (Button) view.findViewById(R.id.btSearchCustomer);
		btSearchCustomer.setOnClickListener(this);
		btCreateCustomer = (Button) view.findViewById(R.id.btCreateCustomer);
		btCreateCustomer.setOnClickListener(this);
		tbNewCustomer = (DMSTableView) view.findViewById(R.id.tbNewCustomer);
		tbNewCustomer.setNumItemsPage(NUM_ITEM_PER_PAGE);
		tbNewCustomer.setListener(this);
		//table dung row NewCustomerRow
//		HeaderTableInfo[] tableHeaderInfo = NewCustomerRow.getTableHeader(parent);
//		tbNewCustomer.getHeaderView().addHeader(tableHeaderInfo);
		initHeaderTable(tbNewCustomer, new NewCustomerRow(parent, this, 0, 0));
		//render danh sách trạng thái KH
		SpinnerAdapter adapterState = new SpinnerAdapter(parent, R.layout.simple_spinner_item, NewCustomerItem.ARRAY_CUSTOMER_STATE );
		this.spinnerState.setAdapter(adapterState);
		spinnerState.setOnItemSelectedListener(this);
		
		//back lại, hiển thị lại những gì đang tìm kiếm
		if (dto != null) {
			//set lai spinner
			this.spinnerState.setSelection(currentIndexSpinnerState);
			//set lai spinner
			edTKH.setText(lastCustomerNameSearch);
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
		String title = StringUtil.getString(R.string.TITLE_VIEW_LIST_NEW_CUSTOMER);
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
	 * request data on first time
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	private void initData() {
		//reset additional info
		lastCustomerNameSearch = "";
		currentPage = -1;
		totalItem = 0;
		currentIndexSpinnerState = -1;
		
		if (currentIndexSpinnerState < 0 ) {
			currentIndexSpinnerState = 0;
		}
		
		//set lai spinner
		this.spinnerState.setSelection(currentIndexSpinnerState);
		//set lai spinner
		edTKH.setText(lastCustomerNameSearch);
				
		requestData();
	}
	
	/**
	 * request data from DB
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	private void requestData() {
		//show dialog loading... 
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));		
		//create request
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, userId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		data.putString(IntentConstants.INTENT_CUSTOMER_NAME, lastCustomerNameSearch);
		data.putInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, NUM_ITEM_PER_PAGE);
		data.putInt(IntentConstants.INTENT_STATE, currentIndexSpinnerState);
		data.putInt(IntentConstants.INTENT_PAGE, currentPage);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_NEW_CUSTOMER;
		//send request
		SaleController.getInstance().handleViewEvent(e);
		
		//nếu lấy lần đầu thì xem như lấy trang 1
		if (currentPage < 0) {
			currentPage = 1;
		}
		
		//hide bàn phím
		GlobalUtil.forceHideKeyboardInput(parent, btSearchCustomer);
	}

	/**
	 * render layout, display data
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	private void renderLayout() {
		if (dto!= null) {
			//render thanh so trang
			tbNewCustomer.setTotalSize(totalItem, currentPage);
			tbNewCustomer.getPagingControl().setCurrentPage(currentPage);
			//render table
			tbNewCustomer.clearAllData();
			if (dto.cusList.size() > 0) {
				int stt = StringUtil.getSTT(currentPage, NUM_ITEM_PER_PAGE);
				for (int i = 0, size = dto.cusList.size(); i < size; i++) {
					NewCustomerItem info = dto.cusList.get(i);
					info.tvSTT = stt;
					NewCustomerRow row = new NewCustomerRow(parent, this, ACTION_VIEW_CUSTOMER_INFO, ACTION_DELETE_CUSTOMER );
					row.render(info);
					//add row
					tbNewCustomer.addRow(row);
					
					stt++;
				}
			}else{
				tbNewCustomer.showNoContentRow();
			}
		}
		//close Progress Dialog
		this.parent.closeProgressDialog();
	}
		
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {				
		switch (modelEvent.getActionEvent().action) {
			case ActionEventConstant.GET_LIST_NEW_CUSTOMER:{
				dto = (NewCustomerListDTO) modelEvent.getModelData();
				//neu co lay tong so item
				if (dto.totalItem >= 0) {
					totalItem  = dto.totalItem;
				}
				renderLayout();
				break;
			}
			case ActionEventConstant.DELETE_CUSTOMER:{
				parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_DELETE_CUSTOMER_SUCCESS));
				currentPage = -1;
				requestData();
				break;
			}
			default:
				super.handleModelViewEvent(modelEvent);
				break;
		}
	}
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		//close Progress Dialog
		this.parent.closeProgressDialog();
		switch (modelEvent.getActionEvent().action) {
			default:
				super.handleErrorModelViewEvent(modelEvent);
				break;
		}
	}
	
	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
    		case ACTION_DELETE_CUSTOMER:{
    			requestDeleteCustomer(customerIdDelete);
    			break;
    		}
			default:
				super.onEvent(eventType, control, data);
				break;
		}
	}
	
	/**
	 * table paging
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		//get current page 
		currentPage = tbNewCustomer.getPagingControl().getCurrentPage();
		//request data with page
		requestData();
	}

	/**
	 * event from Row
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */	
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		if (data instanceof NewCustomerItem ) {
			NewCustomerItem info = (NewCustomerItem)data;
			switch (action) {
    			case ACTION_VIEW_CUSTOMER_INFO:
    				if (info.isEdit) {
    					//neu co the edit, hien thi man hinh edit customer
    					goToEditCustomer(info);
					}else{
						//hien thi man hinh thong tin chi tiet KH
						goToViewCustomer(info);
					}
    				break;
    			case ACTION_DELETE_CUSTOMER: 
    				confirmDeleteCustomer(info);
    				break;
    			default:
    				break;
			}
		}
	}

	/**
	 * Hỏi khi xóa khách hàng
	 * @author: duongdt3
	 * @param info 
	 * @since: 14:41:34 7 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void confirmDeleteCustomer(NewCustomerItem info) {
		customerIdDelete  = info.customerId;
		String text = StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_DELETE_CUSTOMER) + " [" + info.tvTKH + "] ?";
		GlobalUtil.showDialogConfirm(this, this.parent, text, StringUtil.getString(R.string.TEXT_AGREE),
				ACTION_DELETE_CUSTOMER, StringUtil.getString(R.string.TEXT_DENY),
				ACTION_CANCEL_CUSTOMER, null);
	}

	/**
	 * send request delete customer 
	 * @author: duongdt3
	 * @since: 13:41:00 3 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void requestDeleteCustomer(long customerId) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.DELETE_CUSTOMER;		
		Bundle data = new Bundle();
		data.putLong(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		e.viewData = data;
		e.sender = this;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Chuyển đến màn hình thông tin khách hàng
	 * @author: duongdt3
	 * @since: 10:38:54 14 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param info
	 * @param isEdit
	 */
	private void goToCustomerInfoView(NewCustomerItem info, boolean isEdit) {
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		//truyen them id customer để hiển thị thông tin
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(info.customerId));
		data.putBoolean(IntentConstants.INTENT_IS_EDIT, isEdit);
		e.viewData = data;
		e.action = ActionEventConstant.GO_TO_VIEW_CUSTOMER_INFO;
		e.sender = this;
		SaleController.getInstance().handleSwitchFragment(e);
	}
	
	/**
	 * go to View edit customer
	 * @author: duongdt3
	 * @since: 13:40:57 3 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	private void goToEditCustomer(NewCustomerItem info) {
		goToCustomerInfoView(info, true);
	}

	/**
	 * go to View show customer info
	 * @author: duongdt3
	 * @since: 13:40:50 3 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	private void goToViewCustomer(NewCustomerItem info) {
		goToCustomerInfoView(info, false);
	}

	/**
	 * go to create customer
	 * @author: duongdt3
	 * @since: 19:52:34 3 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void goToCreateCustomer() {
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		e.viewData = data;
		e.action = ActionEventConstant.GO_TO_CREATE_CUSTOMER;
		e.sender = this;
		SaleController.getInstance().handleSwitchFragment(e);
	}
	
	/**
	 * Broadcast from Activity, (refresh data, ...)
	 * @author: duongdt3
	 * @since:  08:56:46 03/01/2014
	 * @update: 08:56:46 03/01/2014
	 * @return: void
	 */
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {				
				//Re request data
				initData();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> spinner, View arg1, int pos, long arg3) {
		if (spinner == spinnerState) {
			if (currentIndexSpinnerState != pos) {
				//update index spinner state
				currentIndexSpinnerState = pos;
				currentPage = -1;
				requestData();
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
	
	@Override
	public void onClick(View v) {
		if (v == btCreateCustomer) {
			//ẩn bàn phím
			GlobalUtil.forceHideKeyboardInput(parent, btSearchCustomer);
			//tao moi KH
			goToCreateCustomer();
		}else if (v == btSearchCustomer) {
			currentPage = -1;
			//remove khoảng trắng edit tìm kiếm
			edTKH.setText(edTKH.getText().toString().trim());
			lastCustomerNameSearch = edTKH.getText().toString();
			//tim kiem KH
			requestData();
		}
		super.onClick(v);
	}

}
