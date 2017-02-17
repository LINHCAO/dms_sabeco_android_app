/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinerRoute;
import com.viettel.sabeco.R;

/**
 * header thong tin dia chi trong ban do reset toa do cua gs npp
 * 
 * @author : BangHN since : 1.0 version : 1.1
 */
public class CustomerSaleLocationResetHeaderView extends LinearLayout {
	TextView tvCustomerName;
	TextView tvCustomerAddress;
	Button btReset;// xoa vi tri
	// TextView tvHistoryInfo;//show lich su cap nhat
	SpinerRoute spHistoryUpdate;// spiner lich su cap nhat

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomerSaleLocationResetHeaderView(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public CustomerSaleLocationResetHeaderView(Context context,
			AttributeSet attrs) {
		super(context);
		initView(context);
	}

	/**
	 * Init header
	 * 
	 * @author : BangHN since : 1.0
	 */
	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.layout_customer_sale_location_reset_header, this);
		tvCustomerAddress = (TextView) view
				.findViewById(R.id.tvCustomerAddress);
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		// tvHistoryInfo = (TextView)view.findViewById(R.id.tvHistoryInfo);
		btReset = (Button) view.findViewById(R.id.btReset);
		spHistoryUpdate = (SpinerRoute) view.findViewById(R.id.spHistoryUpdate);
		spHistoryUpdate.setMinimumWidth(180);
		spHistoryUpdate.setVisibility(View.GONE);
	}

	/**
	 * Set thong tin header cua khach hang
	 * 
	 * @author : BangHN since : 1.0
	 */
	public void setCustomerInfo(CustomerDTO customer) {
		if (customer != null) {
			if(!StringUtil.isNullOrEmpty(customer.getCustomerCode())) {
				tvCustomerName.setText(customer.getCustomerCode().substring(0, 3)
						+ " - " + customer.getCustomerName());
			}else{
				tvCustomerName.setText(customer.getCustomerName());
			}
			if (!StringUtil.isNullOrEmpty(customer.address)) {
				tvCustomerAddress.setText(customer.address);
			}
			else{
				tvCustomerAddress.setText(customer.getStreet());
			}
			if (customer.getLat() > 0 && customer.getLng() > 0) {
				btReset.setVisibility(View.VISIBLE);
			} else {
				btReset.setVisibility(View.INVISIBLE);
			}
		}
	}

	/**
	 * Set button reset an hien
	 * 
	 * @author banghn
	 * @param visible
	 */
	public void setButtonResetVisible(int visible) {
		btReset.setVisibility(visible);
	}
}
