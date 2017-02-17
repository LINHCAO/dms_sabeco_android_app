/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TableRow;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.TBHVCustomerListDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * Hien thi dialog danh sach khach hang cua man hinh 07- 01. Them Yeu Cau
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVCustomerListView extends ScrollView implements OnClickListener, VinamilkTableListener {

	private GlobalBaseActivity parent;
	public View viewLayout;
	// listtener
	protected OnEventControlListener listener;
	private VinamilkTableView tbCusList;
	// ma khach hang
	private VNMEditTextClearable edCusCode;
	// ten khach hang hoac dia chi
	private VNMEditTextClearable edCusName;
	// Button tim kiem
	private Button btSearch;
	// image close
	public ImageView ivClose;

	private String textCusCode = Constants.STR_BLANK;//ma kh
	private String textCusName = Constants.STR_BLANK;//ten kh

	// trang
	private int page = 0;
	// action tim kiem
	public static int actionGetCustomer;
	// action chon row
	public static int actionChooseCustomer;

	public TBHVCustomerListView(Context context, OnEventControlListener listener, int actionGetCustomer,
			int actionChooseCustomer) {
		super(context);
		parent = (GlobalBaseActivity) context;
		TBHVCustomerListView.actionGetCustomer = actionGetCustomer;
		TBHVCustomerListView.actionChooseCustomer = actionChooseCustomer;
		this.listener = listener;
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_tbhv_customer_list_view, null);
		edCusCode = (VNMEditTextClearable) viewLayout.findViewById(R.id.edCusCode);
		edCusName = (VNMEditTextClearable) viewLayout.findViewById(R.id.edCusName);
		btSearch = (Button) viewLayout.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		ivClose = (ImageView) viewLayout.findViewById(R.id.ivClose);
		tbCusList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCusList);
		tbCusList.setListener(this);
		layoutTableHeader(viewLayout);

	}

	/**
	 * header list
	 * 
	 * @author: HoanPD1
	 * @param v
	 * @return: void
	 * @throws:
	 */
	private void layoutTableHeader(View v) {
		String[] LIST_CUSTOMER_TABLE_WIDTHS = { StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_CUSTOMER), StringUtil.getString(R.string.TEXT_ADDRESS) };
		int[] LIST_CUSTOMER_TABLE_TITLES = { 70, 270, 410 };
		tbCusList.getHeaderView().addColumns(LIST_CUSTOMER_TABLE_TITLES, LIST_CUSTOMER_TABLE_WIDTHS,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
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
	public void renderLayout(TBHVCustomerListDTO dto, CustomerDTO cusItem) {
		edCusCode.setText(dto.codeCus);
		edCusName.setText(dto.nameCus);
		textCusCode = dto.codeCus;
		textCusName = dto.nameCus;
		if (dto != null) {
			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusList.getPagingControl().getCurrentPage() - 1);
			List<TableRow> listRows = new ArrayList<TableRow>();
			if (dto.listCustomer.size() > 0) {
				for (CustomerDTO cus: dto.listCustomer) {
					TBHVCustomerListRow row = new TBHVCustomerListRow(parent);
					row.setListener(this);
					row.renderLayout(pos, cus);
					if (cusItem != null && cusItem.customerId == cus.customerId) {
						row.updateSelectedColor(ImageUtil.getColor(R.color.LIGHT_GRAY_BG));
					}

					pos++;
					listRows.add(row);
				}
			} else {
				parent.showDialog(StringUtil.getString(R.string.TEXT_LABLE_TBHV_CAN_NOT_FIND_CUSTOMER));
			}

			tbCusList.addContent(listRows);
		}
	}

	/**
	 * 
	 * dung phan trang
	 * 
	 * @author: HoanPD1
	 * @param size
	 * @return: void
	 * @throws:
	 */
	public void setTotalSize(int size) {
		tbCusList.setTotalSize(size);
	}

	/**
	 * 
	 * Tim kiem
	 * 
	 * @author: HoanPD1
	 * @param v
	 * @return: void
	 * @throws:
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btSearch) {
			InputMethodManager inputManager = (InputMethodManager) parent
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(btSearch.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
			textCusCode = edCusCode.getText().toString().trim();
			// textCusCode = edCusCode.getText().toString().substring(0, 3);
			textCusName = edCusName.getText().toString().trim();
			tbCusList.getPagingControl().setCurrentPage(1);

			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
			bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, textCusName);
			page = tbCusList.getPagingControl().getCurrentPage();
			bundle.putInt(IntentConstants.INTENT_PAGE, page);
			bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, true);
			listener.onEvent(actionGetCustomer, this, bundle);
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbCusList) {
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
			bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME, textCusName);
			page = tbCusList.getPagingControl().getCurrentPage();
			bundle.putInt(IntentConstants.INTENT_PAGE, page);
			bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, false);
			listener.onEvent(actionGetCustomer, this, bundle);
		}
	}

	/**
	 * 
	 * chuyen du lieu khi click vao row
	 * 
	 * @author: HoanPD1
	 * @param action
	 * @param control
	 * @param data
	 * @return: void
	 * @throws:
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub
		switch (action) {
		case TBHVCustomerListRow.ACTION_VIEW_CUSTOMER: {
			CustomerDTO dto = (CustomerDTO) data;

			CustomerDTO customer = new CustomerDTO();
			customer.customerId = dto.customerId;
			customer.customerCode = dto.customerCode;
			customer.customerName = dto.customerName;
			listener.onEvent(actionChooseCustomer, this, customer);
			break;
		}
		default:
			break;
		}
	}
}
