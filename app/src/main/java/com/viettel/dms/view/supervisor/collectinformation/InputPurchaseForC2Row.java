/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.collectinformation;

import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.C2PurchaseDetailViewDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Row cua popup dat don mua cua C2
 * InputPurchaseForC2Row.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:40:28 12-12-2013
 */
public class InputPurchaseForC2Row extends RelativeLayout implements OnClickListener, TextWatcher{
	public static final int ACTION_VIEW_CUSTOMER = 0;
	
	Context context;
	View view;
	// row 
	//private TableRow row;
	//so thu tu
	TextView tvSTT;
//	// ma khach hang
	//ten khach hang
	TextView tvCusName;
	//Dia chi
	TextView tvCusAdd;
	LinearLayout llRealOrder;
	LinearLayout llPrice;
	// thuc dat
	EditText etRealOrder;
	//row dto
	C2PurchaseDetailViewDTO rowDTO;
	// listener when row have action
	protected OnEventControlListener listener;

	private int index;
	
	public OnEventControlListener getListener() {
		return listener;
	}

	public void setListener(OnEventControlListener _listener) {
		this.listener = _listener;
	}

	public InputPurchaseForC2Row(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_input_purchase_for_c2_row, this);
//		row = (TableRow) view.findViewById(R.id.row);
//		row.setOnClickListener(this);
		setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCusName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvCusAdd = (TextView) view.findViewById(R.id.tvAddress);
		etRealOrder = (EditText) view.findViewById(R.id.etRealOrder);
		etRealOrder.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					etRealOrder.setSelection(etRealOrder.getText().length());
				}
			}
		});
		etRealOrder.addTextChangedListener(this);
		llRealOrder = (LinearLayout) view.findViewById(R.id.llRealOrder);
		llPrice = (LinearLayout) view.findViewById(R.id.llPrice);
	}

	/**
	 * Render layout
	 * @author: dungnt19
	 * @since: 10:40:50 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param position
	 * @param item
	 */
	public void renderLayout(int position, C2PurchaseDetailViewDTO item){
		index = position;
		this.rowDTO = item;
		tvSTT.setText("" + position);
		tvCusName.setText(item.productCode);
		tvCusAdd.setText(item.productName);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( v == this && listener != null){
//			listener.handleVinamilkTableRowEvent(ACTION_VIEW_CUSTOMER, v, dto);
		}else if (v != this && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
		}
	
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		long realOrder = 0;
		if (etRealOrder.getText().toString().trim().length() > 0) {
			realOrder = Long.parseLong(etRealOrder.getText().toString().trim());
		}
		if (rowDTO != null && realOrder != rowDTO.orderDetail.quantity) {
			// update row total
			ArrayList<String> param = new ArrayList<String>();
			param.add(index + "");
			param.add(realOrder + "");
			listener.onEvent(InputOrderForC2View.ACTION_CHANGE_REAL_ORDER, InputPurchaseForC2Row.this, param);
		}		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}
}
