/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.OrderViewDTO;
import com.viettel.sabeco.R;

/**
 *  Thong tin header (man hinh tao don hang)
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class HeaderOrderInfo extends LinearLayout {
	// ma khach hang
	TextView tvMKH;
	// ten khach hang
	TextView tvTKH;
	// loai khach hang
	TextView tvAddress;
	
	/**
	 * @param context
	 */
	public HeaderOrderInfo(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_header_order_view, this);
		initView(view);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public HeaderOrderInfo(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_header_order_view, this);
		initView(view);

	}
	
	/**
	*  Khoi tao view
	*  @author: TruongHN3
	*  @param view
	*  @return: void
	*  @throws:
	 */
	private void initView(View view){
		tvMKH = (TextView) view.findViewById(R.id.tvMKH);
		tvTKH = (TextView) view.findViewById(R.id.tvTKH);
		tvAddress = (TextView) view.findViewById(R.id.tvAddress);
	}
	
	/**
	*  Cap nhat thong tin text
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	public void updateData(OrderViewDTO order){
		tvMKH.setText(order.customer.getCustomerCode());
		tvTKH.setText(order.customer.getCustomerName());
		tvAddress.setText(order.customer.getStreet());
		
	}
}
