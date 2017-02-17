/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.statistic;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.dto.db.ProductCompetitorListDTO;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CompetitorSaleTable;
import com.viettel.sabeco.R;

/**
 * Cap nhat san luong ban bia doi thu
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 10:51:58 AM Mar 6, 2014
 */
public class NVBHCollectOpponentSale extends BaseFragment implements
		View.OnClickListener, OnTouchListener, OnItemSelectedListener {

	public static final String TAG = NVBHCollectOpponentSale.class.getName();

	public static final int MENU_SALE_BSG = 0;
	public static final int MENU_SALE_OP = 1;
	
	// Views
	private GlobalBaseActivity parent;
	// danh sach thong tin cac san pham ban doi thu
	private LinearLayout lvOP;
	// editText ma khach hang- ten khach hang
	private EditText edCuscodeCusName;
	// button luu thong tin san luong ban doi thu
	private Button btSaveOpSale;
	// dialog chon khach hang
	private NVBHDialogCollectDSKH dialogCustomer;
	// Parameters
	// luu san luong ban doi thu sau khi cap nhat
	private static final int ACTION_SAVE_COMPETITOR = 3;
	// khong luu san luong ban doi thu sau khi cap nhat
	private static final int ACTION_CANCEL_COMPETITOR = 4;
	// danh sach sp ban doi thu de hien thi len linear
	private ProductCompetitorListDTO listProductCompetitor;
	// ma khach hang
	private String customerId;
	// Thong tin khach hang dang select
	private CustomerListItem customerItem;
	// danh sach sp ban doi thu de luu len server
	private ProductCompetitorListDTO competitorListDTO;
	// combobox thang
	private Spinner spMonth;
	// array month
	private String[] arrMonths;
	// current select month
	private int currSelectMonth;
	// danh sach khach hang cua c2
		CustomerListDTO cusC2 = null;

	public static NVBHCollectOpponentSale newInstance(Bundle data) {
		NVBHCollectOpponentSale f = new NVBHCollectOpponentSale();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_nvbh_collect_opponent_sale, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_COLLECT_OPPONENT_SALE));
		initMenu();
		initView(v);
		getInformationCompetitor();
		
		Bundle data = getArguments();
		if(data != null && data.containsKey(IntentConstants.INTENT_CUSTOMER)){
			CustomerListItem cusItem = (CustomerListItem) data.get(IntentConstants.INTENT_CUSTOMER);
			selectCustomer(cusItem);
			
			if(data.containsKey(IntentConstants.INTENT_CUSTOMER_LIST_ITEM)){
				cusC2 = (CustomerListDTO) data.get(IntentConstants.INTENT_CUSTOMER_LIST_ITEM);
			}
		}else{
			getInformationCompetitor();
		}
		
		return v;
	}

	/**
	 * Khoi tao menu
	 * @author: quangvt1
	 * @since: 17:45:31 07-05-2014
	 * @return: void
	 * @throws:
	 */
	private void initMenu() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_BSG), R.drawable.menu_report_icon, MENU_SALE_BSG);
		addMenuItem(StringUtil.getString(R.string.TEXT_OPPONENT), R.drawable.icon_list_item, MENU_SALE_OP); 
		setMenuItemFocus(2);
	}

	/**
	 * Lay thong tin doi thu
	 * 
	 * @author: dungdq3
	 * @since: 2:43:02 PM Mar 6, 2014
	 * @return: void
	 * @throws:
	 */
	private void getInformationCompetitor() { 
		parent.showProgressDialog(getString(R.string.loading));
		
		// Thong tin truyen xuong
		//-- Customer id
		String cusId = this.customerId;
		//-- date
		String date = getDateOfMonthSelected();
		//-- staffId 
		String staffId = GlobalInfo.getInstance().getProfile().getUserData().id + "";
		
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_CUSTOMER_ID, cusId);
		data.putString(IntentConstants.INTENT_DATE, date);
		data.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_INFORMATION_COMPETITOR;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Chuyen thang duoc chon sang ngay
	 * Gia tri tra ve la ngay dau tien cua thang duoc chon
	 * @author: quangvt1
	 * @since: 12:58:18 08-05-2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	private String getDayOfMonthSelected() {
		if(currSelectMonth == 0)
			return DateUtils.getDayOfPreviousMonth();
		
		return DateUtils.getDayOfMonth();
	}

	/**
	 * Chuyen thang duoc chon sang ngay
	 * Gia tri tra ve la ngay dau tien cua thang duoc chon
	 * @author: quangvt1
	 * @since: 12:58:18 08-05-2014
	 * @return: String
	 * @throws:
	 * @return
	 */
	private String getDateOfMonthSelected() {
		if(currSelectMonth == 0)
			return DateUtils.getDateOfPreviousMonth();

		return DateUtils.getDateOfMonth();
	}

	/**
	 * Khoi tao view trong layout
	 * 
	 * @author: dungdq3
	 * @since: 11:22:25 AM Mar 6, 2014
	 * @return: void
	 * @throws:
	 * @param v
	 */
	private void initView(View v) {
		// TODO Auto-generated method stub
		lvOP = (LinearLayout) v.findViewById(R.id.llOpponent);
		lvOP.setDrawingCacheEnabled(true);
		lvOP.buildDrawingCache();
		btSaveOpSale = (Button) v.findViewById(R.id.btSaveOpSale);
		edCuscodeCusName = (EditText) v.findViewById(R.id.edCuscodeCusName);
		edCuscodeCusName.setFocusable(false);
		dialogCustomer = new NVBHDialogCollectDSKH(parent, this, StringUtil.getString(R.string.TEXT_SELECT_CUSTOMER), cusC2);
		// set listener for View
		btSaveOpSale.setOnClickListener(this);
		edCuscodeCusName.setOnTouchListener(this);
		
		// Khoi tao ma khach hang la chuoi rong
		customerId = Constants.STR_BLANK;
		
		// combobox thang
		String monthPre = (DateUtils.getMonthPrevious(new Date()) + 1) + "";
		String currMonth = (DateUtils.getMonth(new Date()) + 1) + "";
		arrMonths = new String[]{monthPre, currMonth};
		SpinnerAdapter adapterMonth = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrMonths);
		spMonth = (Spinner) v.findViewById(R.id.spMonth);
		spMonth.setAdapter(adapterMonth); 
		spMonth.setOnItemSelectedListener(this);
		
		// select thang hien tai
		currSelectMonth = 1;
		spMonth.setSelection(currSelectMonth);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (v == btSaveOpSale) {
			if (StringUtil.isNullOrEmpty(customerId)) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_CUSTOMER));
			} else {
				checkQuantity();
			}
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent actionEvent = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_INFORMATION_COMPETITOR:
			listProductCompetitor = (ProductCompetitorListDTO) modelEvent.getModelData();
			lvOP.removeAllViews();
			boolean allowChangeQuantity = (this.customerItem != null);
			for (ProductCompetitorDTO competitorDTO : listProductCompetitor.getArrProductCompetitor()) {
				CompetitorSaleTable table = new CompetitorSaleTable(parent, competitorDTO, 1, allowChangeQuantity);
				lvOP.addView(table);
			}
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHSANPHAMCANCAPNHATBIADOITHU, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_OPPONENT_SALE:
			CustomerListDTO customerListDTO = (CustomerListDTO) modelEvent.getModelData();
			dialogCustomer.setCustomerListDTO(customerListDTO);
			dialogCustomer.renderLayout();
			requestInsertLogKPI(HashMapKPI.NVBH_DANHSACHKHACHHANGCANCAPNHATBIADOITHU, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_LIST_CUSTOMER_C2_ON_PLAN:
			CustomerListDTO cusC2 = (CustomerListDTO) modelEvent.getModelData();
			dialogCustomer.setCustomerC2(cusC2); 
			break;
		case ActionEventConstant.SAVE_SALE_COMPETITOR:
			parent.showDialogAutoClose(StringUtil.getString(R.string.TEXT_SAVE_SALE_COMPETITOR_SUCCESS), 2000);
			edCuscodeCusName.setText(Constants.STR_BLANK);
			customerId = Constants.STR_BLANK; 
			customerItem = null;
			dialogCustomer.resetDontSelectCustomer();
			GlobalUtil.forceHideKeyboard(parent);
			getInformationCompetitor();
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelViewEvent(modelEvent);
		ActionEvent actionEvent = modelEvent.getActionEvent();
		parent.closeProgressDialog();
		switch (actionEvent.action) {
		case ActionEventConstant.GET_INFORMATION_COMPETITOR:
			parent.showDialog(modelEvent.getModelMessage());
			break;
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_OPPONENT_SALE:
			parent.showDialog(modelEvent.getModelMessage());
			break;
		case ActionEventConstant.SAVE_SALE_COMPETITOR:
			parent.showDialog(modelEvent.getModelMessage());
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) { 
		if (v == edCuscodeCusName) {
			dialogCustomer.resetValueSelected();
			dialogCustomer.getCustomerList(1);
			dialogCustomer.show();
		}
		return false;
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		super.onEvent(eventType, control, data);
		switch (eventType) {
		case ActionEventConstant.UPDATE_CUSTOMER:
			CustomerListItem customerListItem = (CustomerListItem) data;
			selectCustomer(customerListItem);
			break;
		case ACTION_SAVE_COMPETITOR:
			saveSaleCompetitor();
			break;
		case MENU_SALE_BSG:
			goToCollectBSGSale();
			break;
		default:
			break;
		}
	}

	
	/**
	 * Update thong tin khach hang len Editext va lay thong tin bia
	 * @author: quangvt1
	 * @since: 13:47:02 08-05-2014
	 * @return: void
	 * @throws:  
	 * @param customerListItem
	 */
	private void selectCustomer(CustomerListItem customerListItem) {
		StringBuilder sb = new StringBuilder(customerListItem.aCustomer.getCustomerCode());
		sb.append(" - ");
		sb.append(customerListItem.aCustomer.getCustomerName());
		edCuscodeCusName.setText(sb.toString());
		customerId = customerListItem.aCustomer.getCustomerId();
		customerItem = customerListItem; 
		dialogCustomer.setCustomerSelected(customerItem);
		getInformationCompetitor();
	}
	
	/**
	 * Kiem tra số lượng nhập của danh sách bia đối thủ
	 * 
	 * @author: dungdq3
	 * @since: 1:56:44 PM Mar 7, 2014
	 * @return: void
	 * @throws:
	 */
	private void checkQuantity() { 
		boolean flag = false; // Bien co de biet du lieu co thay doi khong
		competitorListDTO = new ProductCompetitorListDTO();
		for (ProductCompetitorDTO productCompetitorDTO : listProductCompetitor.getArrProductCompetitor()) {
			ProductCompetitorDTO competitorDTO = new ProductCompetitorDTO();
			competitorDTO.setCodeCompetitor(productCompetitorDTO.getCodeCompetitor());
			competitorDTO.setNameCompetitor(productCompetitorDTO.getNameCompetitor());
			competitorDTO.setIdCompetitor(productCompetitorDTO.getIdCompetitor());
			for (OpProductDTO opProductDTO : productCompetitorDTO.getArrProduct()) { 
				
				// Kiem tra du lieu co thay doi khong
				if(isDataOpProductChange(opProductDTO)){
					competitorDTO.getArrProduct().add(opProductDTO);
				} 
			}
			if (competitorDTO.getArrProduct().size() > 0) {
				competitorListDTO.getArrProductCompetitor().add(competitorDTO);
				flag = true;
			}
		}
		if (flag) {
			GlobalUtil.showDialogConfirm(this, this.parent, StringUtil.getString(R.string.TEXT_SAVE_SALE_COMPETITOR), StringUtil.getString(R.string.TEXT_AGREE), ACTION_SAVE_COMPETITOR, StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_COMPETITOR, null);
		} else {
			parent.showDialog(StringUtil.getString(R.string.TEXT_INPUT_OPPONENT_SALE_NO_CHANGE));
		}
	}

	/**
	 * Kiem tra thong tin san pham cua doi thu co thay doi khong
	 * @author: quangvt1
	 * @since: 14:03:52 07-05-2014
	 * @return: boolean
	 * @throws:  
	 * @param opProductDTO
	 * @return
	 */
	private boolean isDataOpProductChange(OpProductDTO opProductDTO) {
		boolean isChange = false;
		// Neu thong tin da co ma du lieu thay doi
		if(opProductDTO.isInserted() == true && opProductDTO.getOldQuantity() != opProductDTO.getNewQuantity()){
			isChange = true;
		}else if(opProductDTO.isInserted() == false && opProductDTO.getNewQuantity() != 0){
			// Truong hop insert dong moi.
			isChange = true;
		}
		if(opProductDTO.isInserted() == true && opProductDTO.getOldPrice() != opProductDTO.getNewPrice()){
			isChange = true;
		}else if(opProductDTO.isInserted() == false && opProductDTO.getNewPrice() != 0){
			// Truong hop insert dong moi.
			isChange = true;
		}
		return isChange;
	}

	/**
	 * Luu kiem ban san pham bia doi thu
	 * 
	 * @author: dungdq3
	 * @since: 1:59:45 PM Mar 7, 2014
	 * @return: void
	 * @throws:
	 * @param competitorListDTO
	 */
	private void saveSaleCompetitor() { 
				
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		data.putString(IntentConstants.INTENT_CUSTOMER_ID, customerId);
		data.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		data.putSerializable(IntentConstants.INTENT_OP_SALE_VOLUME_LIST, competitorListDTO);
		data.putString(IntentConstants.INTENT_STAFF_CODE, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		data.putString(IntentConstants.INTENT_DATE, getDayOfMonthSelected());
		data.putInt(IntentConstants.INTENT_TYPE, 0); // 1 - BSG, 0 - bia doi thu
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.SAVE_SALE_COMPETITOR;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Reset lai tat ca cac gia tri
	 * 
	 * @author: dungdq3
	 * @since: 5:10:11 PM Mar 7, 2014
	 * @return: void
	 * @throws:
	 */
	private void resetAllValue() {
		// TODO Auto-generated method stub
		customerId = Constants.STR_BLANK;
		competitorListDTO = null;
		edCuscodeCusName.setText(Constants.STR_BLANK);
		listProductCompetitor = null;
		dialogCustomer = null;
		customerItem = null;
		currSelectMonth = 1;
		cusC2 = null;
		spMonth.setSelection(currSelectMonth);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			resetAllValue();
			dialogCustomer = new NVBHDialogCollectDSKH(parent, this, StringUtil.getString(R.string.TEXT_SELECT_CUSTOMER), null);
			//dialogCustomer.isGetTotalPage = true;
			//dialogCustomer.getCustomerList(1);
			getInformationCompetitor();
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) { 
		if(parent == spMonth && currSelectMonth != spMonth.getSelectedItemPosition()){
			currSelectMonth = spMonth.getSelectedItemPosition();
			if(!StringUtil.isNullOrEmpty(customerId)){
				getInformationCompetitor();
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * Di toi màn hình cập nhật sản lượng bán đối thủ
	 * 
	 * @author: dungdq3
	 * @param index 
	 * @since: 10:48:04 AM Mar 6, 2014
	 * @return: void
	 * @throws:  
	 */
	private void goToCollectBSGSale() { 
		Bundle b = new Bundle(); 
		
		if(customerItem != null){
			b.putSerializable(IntentConstants.INTENT_CUSTOMER, customerItem);
		}
		
		if(cusC2 != null){ 
			b.putSerializable(IntentConstants.INTENT_CUSTOMER_LIST_ITEM, cusC2);
		}
		
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b; 
		e.action = ActionEventConstant.GOTO_COLLECT_BSG_SALE;
		SaleController.getInstance().handleSwitchFragment(e);
	}
}
