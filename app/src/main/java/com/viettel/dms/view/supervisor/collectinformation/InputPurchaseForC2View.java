/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.collectinformation;

import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.C2PurchaseDetailViewDTO;
import com.viettel.dms.dto.view.C2PurchaselViewDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * MH Popup nhap don mua cua C2s
 * InputPurchaseForC2View.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:37:27 12-12-2013
 */
public class InputPurchaseForC2View extends ScrollView implements OnClickListener, OnEventControlListener, OnTouchListener{

	public static final int ACTION_CHANGE_REAL_ORDER = 1;
	public static final int ACTION_CLOSE_POPUP_CHOOSE_DATE_OK = 5;
	private final GlobalBaseActivity parent;
	public final View viewLayout;
	// listtener
	protected OnEventControlListener listener;
	private VinamilkTableView tbCusList;
	private TextView etSourceName;
	// ma khach hang
	public VNMEditTextClearable etOrderDate;
	// Button tim kiem
	private Button btSave;
	private Button btClose;
	private String strOrderDateDefault = "";
	private Date orderDateDefault;
	private InputOrderForC2TotalRow totalRow;
	private C2PurchaselViewDTO orderDTO;
	// action tim kiem
	public int actionSave;
	// action chon row
	public int actionClose;
	private TextView tvTitle;
	
	public InputPurchaseForC2View(Context con){
		super(con);
		parent = (GlobalBaseActivity) con;
		LayoutInflater inflater = parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_input_purchase_for_c2_view, null);
	}

	public InputPurchaseForC2View(Context context, OnEventControlListener listener, int actionSave,int actionClose) {
		this(context);
		this.actionSave = actionSave;
		this.actionClose = actionClose;
		this.listener = listener;
		tvTitle = (TextView) viewLayout.findViewById(R.id.tvTitle);
		etSourceName = (TextView) viewLayout.findViewById(R.id.etSourceName);
		etOrderDate = (VNMEditTextClearable) viewLayout.findViewById(R.id.etOrderDate);
		etOrderDate.setOnTouchListener(this);
		btSave = (Button) viewLayout.findViewById(R.id.btSave);
		btSave.setOnClickListener(this);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		tbCusList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCusList);
		layoutTableHeader(viewLayout);
		
		final String strPattern  = DateUtils.defaultDateFormat.toPattern();
		orderDateDefault = DateUtils.now(strPattern);
		strOrderDateDefault  = DateUtils.convertDateTimeWithFormat(orderDateDefault, strPattern);
	}

	/**
	 * Layout table
	 * @author: dungnt19
	 * @since: 10:37:56 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param v
	 */
	private void layoutTableHeader(View v) {
		String[] LIST_CUSTOMER_TABLE_WIDTHS = { StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_HEADER_PRODUCT_CODE), StringUtil.getString(R.string.TEXT_PRODUCT_NAME_2), 
				StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT) };
		int[] LIST_CUSTOMER_TABLE_TITLES = { 70, 110, 462, 110 };
		tbCusList.getHeaderView().addColumns(LIST_CUSTOMER_TABLE_TITLES, LIST_CUSTOMER_TABLE_WIDTHS,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}

	/**
	 * Render layout Popup
	 * @author: dungnt19
	 * @since: 10:38:09 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @param cusItem
	 */
	public void createLayout(C2PurchaselViewDTO dto) {
		orderDTO = dto;
		etOrderDate.setText(strOrderDateDefault);
		etSourceName.setText("");
		dto.orderInfo.c2PurchaseDate = DateUtils.convertDateTimeWithFormat(orderDateDefault, DateUtils.DATE_FORMAT_NOW);
		
		if (dto != null) {
			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
			tbCusList.clearAllData();
			for (C2PurchaseDetailViewDTO cus: dto.listProduct) {
				InputPurchaseForC2Row row = new InputPurchaseForC2Row(parent);
				row.setListener(this);
				row.renderLayout(pos, cus);
				pos++;
				tbCusList.addRelativeLayout(row);
			}
			totalRow = new InputOrderForC2TotalRow(parent);
			totalRow.renderLayout(dto.totalQuantity);
			tbCusList.addRelativeLayout(totalRow);
		}
	}
	/**
	 * Render layout Popup
	 * @author: dungnt19
	 * @since: 10:38:09 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @param cusItem
	 */
	public void renderLayout(C2PurchaselViewDTO dto, CustomerListItem cusItem) {
		orderDTO = dto;
		tvTitle.setText(StringUtil.getString(R.string.TEXT_INPUT_PURCHASE_FOR_CUSTOMER_OF_C2) + " - " + cusItem.aCustomer.customerName);
		etOrderDate.setText(strOrderDateDefault);
		etSourceName.setText("");
		dto.orderInfo.c2PurchaseDate = DateUtils.convertDateTimeWithFormat(orderDateDefault, DateUtils.DATE_FORMAT_NOW);
		
		if (dto != null) {
			for (int i = 0, size = dto.listProduct.size(); i < size; i++) {
				InputPurchaseForC2Row row = (InputPurchaseForC2Row)tbCusList.getListRelativeView().get(i);
				row.etRealOrder.setText("");
			}
//			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
//			List<TableRow> listRows = new ArrayList<TableRow>();
//			for (C2PurchaseDetailViewDTO cus: dto.listProduct) {
//				InputPurchaseForC2Row row = new InputPurchaseForC2Row(parent);
//				row.setListener(this);
//				row.renderLayout(pos, cus);
//				
//				pos++;
//				listRows.add(row);
//			}
//			
//			totalRow = new InputOrderForC2TotalRow(parent);
//			totalRow.renderLayout(dto.totalQuantity);
//			listRows.add(totalRow);
//
//			tbCusList.addContent(listRows);
			
			if(totalRow != null) {
				totalRow.tvTotalQuantity.setText(String.valueOf(dto.totalQuantity));
			}
		}
		
		etSourceName.requestFocus();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		InputMethodManager inputManager = (InputMethodManager) parent.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(btSave.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		
		if (v == btSave) {
			boolean check = true;
			for(int i = 0, size = orderDTO.listProduct.size(); i < size; i++) {
				InputPurchaseForC2Row row = (InputPurchaseForC2Row) tbCusList.getListRelativeView().get(i);
				C2PurchaseDetailViewDTO detail = orderDTO.listProduct.get(i);
				String strQuantity = row.etRealOrder.getText().toString().trim();
				
				if(detail.orderDetail.quantity == 0 && !StringUtil.isNullOrEmpty(strQuantity)) {
					parent.showDialog(StringUtil.getString(R.string.ERROR_INVALID_REAL_ORDER));
					row.etRealOrder.requestFocus();
					check = false;
					break;
				}
			}
			if(check) {
//				if(StringUtil.isNullOrEmpty(etOrderDate.getText().toString())) {
//					orderDTO.orderInfo.c2PurchaseDate = "";
//				}
				
				if(StringUtil.isNullOrEmpty(etSourceName.getText().toString())) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_PURCHASE_FROM));
				} else if(StringUtil.isNullOrEmpty(etOrderDate.getText().toString())) {
					GlobalUtil.showDialogConfirm((BaseFragment) listener, parent, StringUtil.getString(R.string.TEXT_C2_MUST_CHOOSE_PURCHASE_DATE), 
							StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 
							ACTION_CLOSE_POPUP_CHOOSE_DATE_OK, null, false);
//					parent.showDialog(StringUtil.getString(R.string.TEXT_C2_MUST_CHOOSE_PURCHASE_DATE));
				} else if(orderDTO.totalQuantity > 0) {
					orderDTO.orderInfo.fromShopName = etSourceName.getText().toString().trim();
					GlobalUtil.showDialogConfirm((BaseFragment) listener, parent,
						StringUtil.getString(R.string.TEXT_CONFIRM_SAVE_ORDER_C2),
						StringUtil.getString(R.string.TEXT_AGREE), actionSave,
						StringUtil.getString(R.string.TEXT_DENY), -1, null);
				} else {
					parent.showDialog(StringUtil.getString(R.string.TEXT_C2_TOTAL_QUANTIY_PURCHASE_MUST_LARGE_THAN_0));
				}
			}
			
		} else if(v == btClose) {
			listener.onEvent(actionClose, this, null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ACTION_CHANGE_REAL_ORDER: {
			ArrayList<String> param = (ArrayList<String>) data;
			int index = Integer.parseInt(param.get(0));
			String newQuantity = param.get(1);
			// tinh tong
			if (totalRow != null && tbCusList != null) {
				reCalTotalPrice(index, newQuantity, null);
			}
			break;
		}
		
		default:
			break;
		}
	}
	
	/**
	 * Tinh lai tong so luong
	 * @author: dungnt19
	 * @since: 10:38:36 12-12-2013
	 * @return: int
	 * @throws:  
	 * @param indexRow
	 * @param newQuantity
	 * @param newPrice
	 * @return
	 */
	private int reCalTotalPrice(int indexRow, String newQuantity, String newPrice) {
		int res = -1;
		// tinh tong
		if (indexRow > 0 && indexRow <= orderDTO.listProduct.size()) {
			C2PurchaseDetailViewDTO selectedDetail = orderDTO.listProduct.get(indexRow - 1);
			if (selectedDetail != null) {
				res = indexRow - 1;
				long oldQuantity = selectedDetail.orderDetail.quantity;
				// tinh lai quantity moi
				if(newQuantity != null) {
					if (StringUtil.isNullOrEmpty(newQuantity)) {
						selectedDetail.orderDetail.quantity = 0;
					} else {
						selectedDetail.orderDetail.quantity = Integer.parseInt(newQuantity.trim());
					}
				}
				
				//Tinh lai price moi
				if(newPrice != null) {
					if(StringUtil.isNullOrEmpty(newPrice)) {
						selectedDetail.orderDetail.price = 0;
					} else {
						selectedDetail.orderDetail.price = Long.parseLong(newPrice.trim());
					}
				}
				long newAmount = selectedDetail.orderDetail.price * selectedDetail.orderDetail.quantity;
				selectedDetail.orderDetail.amount = newAmount;

				// tinh lai tong
				orderDTO.totalQuantity = orderDTO.totalQuantity - oldQuantity + selectedDetail.orderDetail.quantity;
				totalRow.renderLayout(orderDTO.totalQuantity);
			}
		}
		return res;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (v == etOrderDate) {
//			if (!v.onTouchEvent(event)) {
				showChooseDateDialog();
//			}
		}
		return false;
	}

	/**
	 * Hien thi popup cho date
	 * @author: dungnt19
	 * @since: 16:18:42 28-12-2013
	 * @return: void
	 * @throws:
	 */
	public void showChooseDateDialog() {
		etOrderDate.requestFocus();
		parent.fragmentTag = CustomerC2ListView.TAG;
		parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etOrderDate.getText().toString(), true);
	}
}
