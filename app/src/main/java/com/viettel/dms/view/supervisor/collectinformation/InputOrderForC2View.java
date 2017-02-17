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
import com.viettel.dms.dto.view.C2SaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.C2SaleOrderlViewDTO;
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
 * MH nhap don hang cua KH cua C2
 * InputOrderForC2View.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:34:58 12-12-2013
 */
public class InputOrderForC2View extends ScrollView implements OnClickListener, OnEventControlListener, OnTouchListener{

	public static final int ACTION_CHANGE_REAL_ORDER = 1;
	public static final int ACTION_CLOSE_POPUP_CHOOSE_DATE_OK = 5;
	private final GlobalBaseActivity parent;
	public final View viewLayout;
	// listtener
	protected OnEventControlListener listener;
	private VinamilkTableView tbCusList;
	private TextView tvCustomerName;
	// ma khach hang
	public VNMEditTextClearable etOrderDate;
	// Button tim kiem
	private Button btSave;
	private Button btClose;
	private String strOrderDateDefault = "";
	private Date orderDateDefault;
	private InputOrderForC2TotalRow totalRow;
	private C2SaleOrderlViewDTO orderDTO;
	// action tim kiem
	public static int actionSave;
	// action chon row
	public static int actionClose;

	public InputOrderForC2View(Context con){
		super(con);
		parent = (GlobalBaseActivity) con;
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_input_order_for_c2_view, null);
	}
	
	public InputOrderForC2View(Context context, OnEventControlListener listener, int actionSave,int actionClose) {
		this(context);
		InputOrderForC2View.actionSave = actionSave;
		InputOrderForC2View.actionClose = actionClose;
		this.listener = listener;
		tvCustomerName = (TextView) viewLayout.findViewById(R.id.tvCustomerName);
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
	 * Tao table
	 * @author: dungnt19
	 * @since: 10:35:19 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param v
	 */
	private void layoutTableHeader(View v) {
		String[] LIST_CUSTOMER_TABLE_WIDTHS = { StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_HEADER_PRODUCT_CODE), StringUtil.getString(R.string.TEXT_PRODUCT_NAME_2), 
				StringUtil.getString(R.string.TEXT_COLUM_PRICE), StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT) };
		int[] LIST_CUSTOMER_TABLE_TITLES = { 70, 130, 330, 110, 110 };
		tbCusList.getHeaderView().addColumns(LIST_CUSTOMER_TABLE_TITLES, LIST_CUSTOMER_TABLE_WIDTHS,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}

	/**
	 * Render layout 
	 * @author: dungnt19
	 * @since: 10:35:38 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @param cusItem
	 */
	public void renderLayout(C2SaleOrderlViewDTO dto, CustomerListItem cusItem) {
		orderDTO = dto;
		etOrderDate.setText(strOrderDateDefault);
		dto.orderInfo.orderDate = DateUtils.convertDateTimeWithFormat(orderDateDefault, DateUtils.DATE_FORMAT_NOW);
		
		if(cusItem != null) {
			tvCustomerName.setText(cusItem.aCustomer.customerCode + " - " + cusItem.aCustomer.customerName + ", " + cusItem.aCustomer.address);
		}
		if (dto != null) {
			for(int i = 0, size = orderDTO.listProduct.size(); i < size; i++) {
				InputOrderForC2Row row = (InputOrderForC2Row) tbCusList.getListRelativeView().get(i);
				row.etPrice.setText("");
				row.etRealOrder.setText("");
			}
	//			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
	//			List<TableRow> listRows = new ArrayList<TableRow>();
	//			for (C2SaleOrderDetailViewDTO cus: dto.listProduct) {
	//				InputOrderForC2Row row = new InputOrderForC2Row(parent);
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
		
		etOrderDate.requestFocus();
	}
	
	/**
	 * Render layout 
	 * @author: dungnt19
	 * @since: 10:35:38 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param dto
	 * @param cusItem
	 */
	public void createLayout(C2SaleOrderlViewDTO dto) {
		orderDTO = dto;
		etOrderDate.setText(strOrderDateDefault);
		dto.orderInfo.orderDate = DateUtils.convertDateTimeWithFormat(orderDateDefault, DateUtils.DATE_FORMAT_NOW);
		
		if (dto != null) {
			tbCusList.clearAllData();
			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
			for (C2SaleOrderDetailViewDTO cus: dto.listProduct) {
				InputOrderForC2Row row = new InputOrderForC2Row(parent);
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

	@Override
	public void onClick(View v) {
		InputMethodManager inputManager = (InputMethodManager) parent
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(btSave.getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
		if (v == btSave) {
			boolean check = true;
			for(int i = 0, size = orderDTO.listProduct.size(); i < size; i++) {
				InputOrderForC2Row row = (InputOrderForC2Row) tbCusList.getListRelativeView().get(i); //duyet tung row.
				C2SaleOrderDetailViewDTO detail = orderDTO.listProduct.get(i); // lay cac sp.
				detail.orderDetail.price = 0;
				String strPrice = row.etPrice.getText().toString().trim().replace(",", "");
				if(!StringUtil.isNullOrEmpty(strPrice)) {
					detail.orderDetail.price = Long.parseLong(strPrice);
				}
				
				detail.orderDetail.amount = detail.orderDetail.price * detail.orderDetail.quantity;
				
				if((detail.orderDetail.price > 0 || detail.orderDetail.quantity > 0) && detail.orderDetail.amount == 0) {
					if(detail.orderDetail.quantity == 0) {
						parent.showDialog(StringUtil.getString(R.string.ERROR_INVALID_REAL_ORDER));
						row.etRealOrder.requestFocus();
					} else if(detail.orderDetail.price == 0) {
						parent.showDialog(StringUtil.getString(R.string.TEXT_INPUT_PRODUCT_PRICE));
						row.etPrice.requestFocus();
					}
					check = false;
					break;
				}
			}
			
			if(check) {
//				if(StringUtil.isNullOrEmpty(etOrderDate.getText().toString())) {
//					orderDTO.orderInfo.orderDate = "";
//				}
				
				if(StringUtil.isNullOrEmpty(etOrderDate.getText().toString())) {
					GlobalUtil.showDialogConfirm((BaseFragment) listener, parent, StringUtil.getString(R.string.TEXT_C2_MUST_CHOOSE_ORDER_DATE), 
							StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), 
							ACTION_CLOSE_POPUP_CHOOSE_DATE_OK, null, false);
//					parent.showDialog(StringUtil.getString(R.string.TEXT_C2_MUST_CHOOSE_ORDER_DATE));
				} else if(orderDTO.totalQuantity > 0) {
					GlobalUtil.showDialogConfirm((BaseFragment) listener, parent,
						StringUtil.getString(R.string.TEXT_CONFIRM_SAVE_ORDER_C2),
						StringUtil.getString(R.string.TEXT_AGREE), InputOrderForC2View.actionSave,
						StringUtil.getString(R.string.TEXT_DENY), -1, null);
				} else {
					parent.showDialog(StringUtil.getString(R.string.TEXT_C2_TOTAL_QUANTIY_ORDER_MUST_LARGE_THAN_0));
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
	 * Tinh lai so tien tung san pham & tong so sp 
	 * @author: dungnt19
	 * @since: 10:36:23 12-12-2013
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
			C2SaleOrderDetailViewDTO selectedDetail = orderDTO.listProduct.get(indexRow - 1);
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
		parent.fragmentTag = CustomerOfC2ListView.TAG;
		parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etOrderDate.getText().toString(), true);
	}
}
