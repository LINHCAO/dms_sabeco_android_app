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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.dto.db.CustomerInputQuantityDetailDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.AbstractAlertDialog;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;
 

/**
 * Dialog them san luong ban thuc hien cua khach hang
 * DialogAddJoinQuantity.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  15:36:35 20-05-2014
 */
public class DialogAddDoneQuantity extends AbstractAlertDialog {  
	public static final int ACTION_ACCEPT_SAVE_DONE = -333;
	public static final int ACTION_CANCEL_SAVE_DONE = -444; 
	// list row
	private VinamilkTableView tbProductList;  
	// Button Chon
	private Button btChoose;
	// Button dong
	private Button btClose; 
	// Text hien thi chu ki
	private TextView tvPeriod; 
	// Thong tin khach hang duoc chon de them san luong
	CustomerInputQuantityDTO customer; 
	// Chi co khach hang cho duyet moi duoc chinh sua
	private boolean isAllowEdit;
	// LinearLayout hien thi ch biet so lieu lay tu SMS
	private LinearLayout llInfoSMS;
	public boolean isGetTotalPage;
 
	/**
	 * Ham khoi tao
	 * @param context
	 * @param base
	 * @param title
	 * @param proInfo
	 */
	public DialogAddDoneQuantity(Context context, BaseFragment base, CustomerInputQuantityDTO dto) {
		super(context, base, "");
		View v = setViewLayout(R.layout.layout_dialog_add_done_quantity);  
		this.customer = dto;
		initView(v); 
		setPeriodText(dto);
		setTitleForDialog(dto);
	} 
	
	/**
	 * Gan thong tin khach hang
	 * @author: quangvt1
	 * @since: 15:52:11 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param cus
	 */
	public void setCustomer(CustomerInputQuantityDTO cus) {
		this.customer = cus;

		// Khach hang cho duyet moi dc chinh sua
		isAllowEdit = true;
		// Neu trong thoi gian cho phep va du lieu tu NPP
		// type = 1 : lay so lieu tu NPP : cho phep chinh sua
		// type = 2 : so lieu tu SMS cua PG : chi duoc xem
		if (customer.ALLOW_EDIT && cus.type == 1) {
			isAllowEdit = true;
			// Khong duoc sua thi an nut Luu di
			btChoose.setVisibility(View.VISIBLE);
		} else {
			isAllowEdit = false;
			btChoose.setVisibility(View.GONE);
		}
		if (customer.type == 1) {
			llInfoSMS.setVisibility(View.GONE);
		}
		renderLayout();
	}

	/**
	 * Cap nhat title cho dialog tuy thuoc vao khach hang
	 * @author: quangvt1
	 * @since: 16:01:38 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param cus
	 */
	private void setPeriodText(CustomerInputQuantityDTO cus) {
		String strFromDate = DateUtils.convertDateOneFromFormatToAnotherFormat(
				cus.PERIOD_FROM_DATE, DateUtils.DATE_FORMAT_NOW, "d/M/yy");
		String strToDate = DateUtils.convertDateOneFromFormatToAnotherFormat(
				cus.PERIOD_TO_DATE, DateUtils.DATE_FORMAT_NOW, "d/M/yy");

		tvPeriod.setText(strFromDate + " - " + strToDate);
	} 
	
	/**
	 * Cap nhat title cho dialog tuy thuoc vao khach hang
	 * Sản lượng đăng ký  Ma KH - Ten KH
	 * @author: quangvt1
	 * @since: 16:01:38 20-05-2014
	 * @return: void
	 * @throws:  
	 * @param cus
	 */
	private void setTitleForDialog(CustomerInputQuantityDTO cus) {
		 String title = "";
		 title += StringUtil.getString(R.string.TEXT_QUANTITY_DONE);
		 title += " ";
		 title += cus.CUSTOMER_CODE;
		 title += " - ";
		 title += cus.CUSTOMER_NAME;
		 
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
		btChoose = (Button) viewLayout.findViewById(R.id.btChoose);
		btChoose.setOnClickListener(this);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this); 
		tbProductList = (VinamilkTableView) viewLayout.findViewById(R.id.tbProductList);  
		tvPeriod = (TextView) viewLayout.findViewById(R.id.tvPeriod);
		llInfoSMS = (LinearLayout) viewLayout.findViewById(R.id.llInfoSMS); 
		
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
		int[] TABLE_WIDTHS = { 45, 130, 330, 150, 150 }; 
		String[] TABLE_TITLES = {
				StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME), 
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_FROM_NPP), 
				StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT) };
		
		tbProductList.getHeaderView().addColumns(
				TABLE_WIDTHS, TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}  
 
	/**
	 * Lay danh sach khach hang chua tham gia chuong trinh khi chon button them
	 * khach hang
	 * 
	 * @author: hoanpd1
	 * @since: 11:25:44 16-05-2014
	 * @return: void
	 * @throws:
	 * @param i
	 * @param j
	 */
	public void getProductList(int page, boolean isGetTotalPage) {
		// Show dialog loading...
		if (!parent.isShowProgressDialog()) {
			parent.showLoadingDialog();
		}
 
		// Dong goi du lieu truyen xuong de lay danh sach khach hang
		Bundle bundle = new Bundle();
		// -- Trang nao?
		bundle.putInt(IntentConstants.INTENT_PAGE, page); 
		// -- Co lay so trang khong
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		// -- Ma chuong trinh
		bundle.putLong(IntentConstants.INTENT_PROGRAM_ID, customer.PRO_INFO_ID);
		bundle.putLong(IntentConstants.INTENT_CUSTOMER_ID, customer.OBJECT_ID);
		// -- Chu trinh(Period)
		bundle.putLong(IntentConstants.INTENT_PERIOD_ID, customer.PRO_CUS_PERIOD);
		bundle.putLong(IntentConstants.INTENT_PRO_CUS_MAP_ID, customer.PRO_CUS_MAP_ID);
		// -- Muc (Level)
		bundle.putInt(IntentConstants.INTENT_LEVEL, customer.JOIN_LEVEL);
		setHandleViewEvent(
				ActionEventConstant.GET_INPUT_QUANTITY_DONE,
				bundle, SaleController.getInstance());
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
		if (this.customer != null && this.customer.details.size() > 0) { 
			for (CustomerInputQuantityDetailDTO detail : this.customer.details) { 
				ProductListQuantityDoneRow row = new ProductListQuantityDoneRow(parent, isAllowEdit, customer.type);
				row.renderLayout(pos, detail); 
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
			processSaveDoneProduct();
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
	private void processSaveDoneProduct() {
		boolean validInput = validateInput(); 
		if(!validInput){
			parent.showDialog(StringUtil.getString(R.string.TEXT_ERROR_QUANTITY_SHOP_LESS_THEN_SALE_QUANTITY));
			return;
		}
		
		int countInput = getCountProductInput();
		if (countInput == customer.details.size() && countInput > 0) {
			// Neu da tung nhap thi kiem tra du lieu co thay doi khong
			if(customer.hasInput){
				boolean isChange = isChangeQuantity();
				// Neu co thay doi thi kiem tra co phai do nguoi khac nhap khong
				if(isChange){ 
					if (isHaveInputStaffOther()) {
						GlobalUtil.showDialogConfirm(listener, this.parent,
										StringUtil.getString(R.string.TEXT_CONFIRM_SAVE_BY_OTHER),
										StringUtil.getString(R.string.TEXT_AGREE), ACTION_ACCEPT_SAVE_DONE,
										StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_SAVE_DONE, customer);
					} else {
						saveQuantityDone(customer);
					}
				}else{
					parent.showDialog(StringUtil.getString(R.string.TEXT_ERROR_DONT_CHANGE));
				}
			}
			// Neu chua tung nhap
			else{
				saveQuantityDone(customer);  
			} 
		}else{
			if(countInput > 0 && countInput < customer.details.size()){
				parent.showDialog(StringUtil.getString(R.string.TEXT_ERROR_DONT_INPUT_ALL_QUANTITY));
			}else{
				parent.showDialog(StringUtil.getString(R.string.TEXT_ERROR_DONT_INPUT_QUANTITY));
			}
		} 
	}

	/**
	 * Kiem tra gia tri nhap san luong co vuot qua so lieu ban
	 * tu NPP hay khong
	 * @author: quangvt1
	 * @since: 16:25:00 23-06-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private boolean validateInput() {
		boolean isValid = true;
		
		for (CustomerInputQuantityDetailDTO detail : this.customer.details) {
			if(detail.NEWQUANTITY > detail.QUANTITY_SHOP &&  detail.QUANTITY_SHOP >= 0){
				isValid = false;
				break;
			}
		}
		
		return isValid;
	}

	/**
	 * Kiem tra du lieu co duoc nhap boi 1 NVBH khac khong?
	 * @author: quangvt1
	 * @since: 08:43:17 20-06-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private boolean isHaveInputStaffOther() {
		boolean isHaveOrther = false;
		String staffCode  = GlobalInfo.getInstance().getProfile().getUserData().userCode; 
		
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			if(!staffCode.equals(detail.UPDATE_USER)){
				isHaveOrther = true;
				break;
			}
		}
		
		return isHaveOrther;
	}

	 
	/**
	 * Lay so luong san pham duoc nhap san luong
	 * @author: quangvt1
	 * @since: 14:39:11 19-06-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private int getCountProductInput() { 
		int count = 0;
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			if(detail.isInputNewQuantity == true){
				 count++;
			}
		}
		return count;
	}

	/**
	 * Kiem tra du lieu co thay doi khong
	 * @author: quangvt1
	 * @since: 14:39:29 19-06-2014
	 * @return: boolean
	 * @throws:  
	 * @return
	 */
	private boolean isChangeQuantity() {
		boolean isChange = false;
		for (CustomerInputQuantityDetailDTO detail : customer.details) {
			if(detail.NEWQUANTITY != detail.OLDQUANTITY){
				isChange = true;
				break;
			}
		}
		return isChange;
	}

	/**
	 * Thuc hien save thong tin san luong ban
	 * @author: quangvt1
	 * @since: 16:36:10 22-05-2014
	 * @return: void
	 * @throws:  
	 */
	public void saveQuantityDone(CustomerInputQuantityDTO customer) { 
		// Show dialog loading...
		if(!parent.isShowProgressDialog()){
			parent.showLoadingDialog(); 
		}
		UserDTO user = GlobalInfo.getInstance().getProfile().getUserData();
		Bundle bundle = new Bundle();  
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER_OBJECT, customer); 
		bundle.putString(IntentConstants.INTENT_STAFF_ID, user.id + ""); 
		bundle.putString(IntentConstants.INTENT_STAFF_CODE, user.userCode);
		bundle.putInt(IntentConstants.INTENT_ACTION_TYPE, 1);
		
		ActionEvent e = new ActionEvent();
		e.viewData = bundle;
		e.action = ActionEventConstant.SAVE_DONE_PRODUCT;
		e.sender = listener;
		SaleController.getInstance().handleViewEvent(e);
	}  
}
