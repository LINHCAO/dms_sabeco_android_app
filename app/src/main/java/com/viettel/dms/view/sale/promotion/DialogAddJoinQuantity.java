/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.promotion;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableRow;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.view.ComboBoxDisplayProgrameItemDTO;
import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.dto.view.ListProductQuantityJoin;
import com.viettel.dms.dto.view.ListProductQuantityJoin.ProductQuantityJoin;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.AbstractAlertDialog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;
 

/**
 * Dialog them san luong ban tham gia cho khach hang
 * DialogAddJoinQuantity.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:36:35 20-05-2014
 */
public class DialogAddJoinQuantity extends AbstractAlertDialog implements OnItemSelectedListener { 
	// Action save join
	public static final int ACTION_ACCEPT_SAVE_JOIN = -111;
	// Action cancel save join
	public static final int ACTION_CANCEL_SAVE_JOIN = -222; 
	// list row
	private VinamilkTableView tbProductList; 
	// spinner muc tham gia
	private Spinner spinnerLevel; 
	// Button Chon
	private Button btChoose;
	// Button dong
	private Button btClose; 
	// Level select hien tai
	private int selectLevel = 0; 
	// chuong trinh tham gia
	private ProInfoDTO proInfo;
	// Thong tin khach hang duoc chon de them san luong
	CustomerAttendProgramListItem customer;
	// danh sach level
	private List<ComboBoxDisplayProgrameItemDTO> listLevel = new ArrayList<ComboBoxDisplayProgrameItemDTO>();
	// Chi co khach hang cho duyet moi duoc chinh sua
	private boolean isAllowEdit;
	// Linear select level
	private LinearLayout llLevel;
	// Danh sach san pham
	private ListProductQuantityJoin productList;
	// Khach hang co thay doi muc tham gia khong
	private boolean isChangeLevel = false; 
 
	/**
	 * Ham khoi tao
	 * @param context
	 * @param base
	 * @param title
	 * @param proInfo
	 */
	public DialogAddJoinQuantity(Context context, BaseFragment base) {
		super(context, base, "");
		View v = setViewLayout(R.layout.layout_dialog_add_join_quantity);  
		initView(v); 
	}

	/**
	 * Gan thong tin chuong trinh
	 * @author: quangvt1
	 * @since: 15:52:02 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param proInfo
	 */
	public void setProInfo(ProInfoDTO proInfo){
		this.proInfo = proInfo;
		renderDataLevel(proInfo.listLevel);
	}
	
	/**
	 * Gan thong tin khach hang
	 * @author: quangvt1
	 * @since: 15:52:11 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param cus
	 */
	public void setCustomer(CustomerAttendProgramListItem cus){
		this.customer = cus;
		
		// Khach hang cho duyet moi dc chinh sua
		isAllowEdit = (customer.status == 0);
		
		btChoose.setEnabled(isAllowEdit);
		if(isAllowEdit){
			btChoose.setVisibility(View.VISIBLE);
		}else{
			btChoose.setVisibility(View.GONE);
		}
		spinnerLevel.setEnabled(isAllowEdit);
		
		setTitleForDialog(cus);
		
		// Mac dinh chon level cua khach hang tham gia
		if(cus.level > 0 && cus.level <= proInfo.totalLevel){
			selectLevel = cus.level - 1;
			spinnerLevel.setSelection(selectLevel);
		}
	}
	
	/**
	 * Gan danh sach san pham
	 * @author: quangvt1
	 * @since: 18:02:05 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param productList
	 */
	public void setProductList(ListProductQuantityJoin productList){
		this.productList = productList;
	}
	
	/**
	 * Cap nhat title cho dialog tuy thuoc vao khach hang
	 * @author: quangvt1
	 * @since: 16:01:38 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param cus
	 */
	private void setTitleForDialog(CustomerAttendProgramListItem cus) {
		 String title = "";
		 title += StringUtil.getString(R.string.TEXT_QUANTITY_REGISTER);
		 title += " ";
		 title += cus.customerCode;
		 title += " - ";
		 title += cus.customerName;
		 
		 this.setTitle(title);
	} 
	
	/**
	 * Init View
	 * @author: quangvt1
	 * @since: 17:28:11 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param viewLayout
	 */
	private void initView(View viewLayout) { 
		spinnerLevel = (Spinner) viewLayout.findViewById(R.id.spLevelJoin);
		spinnerLevel.setOnItemSelectedListener(this); 
		btChoose = (Button) viewLayout.findViewById(R.id.btChoose);
		btChoose.setOnClickListener(this);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this); 
		tbProductList = (VinamilkTableView) viewLayout.findViewById(R.id.tbProductList);  
		llLevel = (LinearLayout) viewLayout.findViewById(R.id.llLevel);
		
		layoutTableHeader();
	} 
	
	/**
	 * Tao header cho table
	 * @author: quangvt1
	 * @since: 17:52:06 20-05-2014
	 * @return: void
	 * @throws:
	 */
	private void layoutTableHeader() {
		int[] TABLE_WIDTHS = { 45, 130, 330, 180 }; 
		String[] TABLE_TITLES = {
				StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME), 
				StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT) };
		
		tbProductList.getHeaderView().addColumns(
				TABLE_WIDTHS, TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}

	/**
	 * Lay danh sach san phan de dan ki san luong tham gia
	 * @author: quangvt1
	 * @since: 17:42:41 20-05-2014
	 * @return: void
	 * @throws:
	 */
	public void getProductList() {
		// Show dialog loading...
		if (!parent.isShowProgressDialog()) {
			parent.showLoadingDialog();
		} 
		String level = "0";
		if(listLevel.size() > 0){
			level = listLevel.get(selectLevel).value;
		}
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_PROGRAM_ID, proInfo.PRO_INFO_ID);  
		bundle.putLong(IntentConstants.INTENT_CUSTOMER_ID, customer.customerId);  
		bundle.putString(IntentConstants.INTENT_LEVEL, level);  

		setHandleViewEvent(ActionEventConstant.GET_PRODUCT_LIST_JOIN, bundle, SaleController.getInstance());
	}

	/**
	 * Render cac muc cua chuong trinh
	 * 
	 * @author: hoanpd1
	 * @since: 09:55:59 25-07-2014
	 * @return: void
	 * @throws:  
	 * @param listLevel1
	 */
	private void renderDataLevel(List<ComboBoxDisplayProgrameItemDTO> listLevel1) {
		ArrayList<String> levelArray = new ArrayList<String>();
		ComboBoxDisplayProgrameItemDTO arg0 = new ComboBoxDisplayProgrameItemDTO();
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
		if (listLevel1.size() > 1) {
			llLevel.setVisibility(View.VISIBLE);
		} else {
			llLevel.setVisibility(View.GONE);
		}
	}

	/**
	 * 
	 * render Layout
	 * 
	 * @author: HoanPD1
	 * @param dto
	 * @param cusItem
	 * @return: void
	 * @throws:
	 */
	public void renderLayout() { 
		int pos = 1;
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (productList != null && productList.listProduct.size() > 0) { 
			for (ProductQuantityJoin item : productList.listProduct) { 
				ProductListQuantityJoinRow row = new ProductListQuantityJoinRow(parent, isAllowEdit);
				row.renderLayout(pos, item); 
				pos++;
				listRows.add(row);
			}
		} else {
			tbProductList.showNoContentRowWithString(StringUtil.getString(R.string.TEXT_PRODUCT_LIST_INFO_NORESULT));
		}
		tbProductList.clearAllData();
		tbProductList.addContent(listRows);
		forceHideKeyBoardForDialog(); 
	}

	@Override
	public void onClick(View v) {
		if (v == btClose) {
			dismiss();
		} else if (v == btChoose) {
			// Kiem tra thong tin co hop le de save khong
			processSaveJoinProduct();
		}
	}  

	/**
	 * Kiem tra thong tin nhap truoc khi save
	 * Thong tin hop le khi co it nhat 1 thong tin thay doi va cac san pham deu duoc dang ki
	 * @author: quangvt1
	 * @since: 14:35:26 21-05-2014
	 * @return: void
	 * @throws:
	 */
	private void processSaveJoinProduct() {
		// Kiem tra nguoi dung co thay doi muc khong
		isChangeLevel = (customer.level != (selectLevel + 1));
		
		// Kiem tra cac san pham co duoc nhap het khong
		boolean isJoinAllProduct = isJoinAllProduct();
		if(!isJoinAllProduct){
			// Show dialog vui long chon het
			parent.showDialog(StringUtil.getString(R.string.TEXT_ERROR_DONT_JOIN_ALL_PRODUCT));
			return;
		}
		
		// Lay danh sach dang ki san luong thay doi
		// Truong hop them moi cung tinh la thay doi: tu 0 --> dang ki
		ListProductQuantityJoin listProductChange = getAllProductChange();
		
		if(listProductChange.listProduct.size() <= 0){ 
			// khong thay doi level va khong nhap gi -Du lieu khong co gi thay doi
			parent.showDialog(StringUtil.getString(R.string.TEXT_ERROR_DONT_CHANGE));
		}else{
			parent.showLoadingDialog();
			saveQuantityJoinAfterConfirm(listProductChange);
		}
	}

	/**
	 * Thuc hien save thong tin dang ki san luong sau khi da confirm
	 * @author: quangvt1
	 * @since: 16:36:10 22-05-2014
	 * @return: void
	 * @throws:  
	 * @param listProductChange
	 */
	public void saveQuantityJoinAfterConfirm(ListProductQuantityJoin listProductChange) { 
		Bundle bundle = new Bundle();
		bundle.putBoolean(IntentConstants.INTENT_CHANGE_LEVEL, isChangeLevel);
		bundle.putSerializable(IntentConstants.INTENT_PROMOTION, proInfo);
		bundle.putSerializable(IntentConstants.INTENT_PRODUCT_LIST, listProductChange);
		bundle.putInt(IntentConstants.INTENT_NEW_LEVEL, selectLevel + 1);
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		bundle.putString(IntentConstants.INTENT_STAFF_ID, GlobalInfo
				.getInstance().getProfile().getUserData().id + ""); 
		bundle.putString(IntentConstants.INTENT_STAFF_CODE, GlobalInfo
				.getInstance().getProfile().getUserData().userCode);
		
		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.SAVE_JOIN_PRODUCT;
		e.sender = listener;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Kiem tra nguoi dung co nhap het khong
	 * @author: quangvt1
	 * @since: 15:57:08 21-05-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private boolean isJoinAllProduct() {
		boolean isJoinAll = true;
		for (ProductQuantityJoin item : productList.listProduct){
			if(item.newQuantity == 0){
				isJoinAll = false;
			}
		}
		
		return isJoinAll;
	}

	/**
	 * Lay danh sach san pham co san luong dang ki thay doi
	 * @author: quangvt1
	 * @since: 14:53:36 21-05-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private ListProductQuantityJoin getAllProductChange() {
		ListProductQuantityJoin listProductChange = new ListProductQuantityJoin();
		listProductChange.pro_cus_map_id = productList.pro_cus_map_id;
		
		for (ProductQuantityJoin item : productList.listProduct){
			if(item.newQuantity != item.oldQuantity && item.newQuantity > 0){
				// Co thay doi
				listProductChange.listProduct.add(item);
			}
		}
		
		return listProductChange;
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		 if (arg0 == spinnerLevel && selectLevel != spinnerLevel.getSelectedItemPosition()) {
			selectLevel = spinnerLevel.getSelectedItemPosition();
			getProductList();
		}

	} 
	 
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	} 
}
