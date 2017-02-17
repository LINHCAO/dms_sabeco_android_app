/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.CreateCustomerInfoDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.image.zoom.ImageViewTouch;
import com.viettel.sabeco.R;

/**
 * thong tin chung
 * @author  : BangHN
 * since   : 1.0
 * version : 1.0
 */
public class CustomerLocationUpdateHeaderView extends LinearLayout {
	// ten khach hang
	TextView tvCustomerName;
	// dia chi khach hang
	TextView tvCustomerAddress;
	// button cap nhat
	Button btUpdate;
	// ll cap nhat
	LinearLayout llUpdate;
	private LinearLayout llDiaBan;
	Spinner spinnerProvine;
	Spinner spinnerDistrict;
	Spinner spinnerPrecinct;
	private ImageView ivUpdate;
	private ImageView ivDiaBan;

	/**
	 * Instantiates a new Customer location update header view.
	 * @param context the context
     */
	public CustomerLocationUpdateHeaderView(Context context) {
		super(context);
		initView(context);
	}
	
	
	/**
	 * @param context
	 * @param attrs
	 */
	public CustomerLocationUpdateHeaderView(Context context, AttributeSet attrs) {
		super(context);
		initView(context);
	}
	
	/**
	 * Init header
	 * @author : BangHN
	 * since : 1.0
	 */
	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_customer_location_update_header, this);
		tvCustomerAddress = (TextView)view.findViewById(R.id.tvCustomerAddress);
		tvCustomerName = (TextView)view.findViewById(R.id.tvCustomerName);
		btUpdate = (Button)view.findViewById(R.id.btUpdate);
		llUpdate = (LinearLayout)view.findViewById(R.id.llUpdate);
		ivUpdate = (ImageView)view.findViewById(R.id.ivUpdate);
		llDiaBan = (LinearLayout) view.findViewById(R.id.llDiaBan);
		ivDiaBan = (ImageView) view.findViewById(R.id.ivDiaBan);
		spinnerProvine = (Spinner) view.findViewById(R.id.spinnerProvine);
		spinnerDistrict = (Spinner) view.findViewById(R.id.spinnerDistrict);
		spinnerPrecinct = (Spinner) view.findViewById(R.id.spinnerPrecinct);
	}
	
	/**
	 * Set thong tin header cua khach hang
	 * @author : BangHN
	 * since : 1.0
	 */
	public void setCustomerInfo(CustomerDTO customer){
		if(customer != null){
			int typeUser = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
			if(!StringUtil.isNullOrEmpty(customer.shortCode)) {
				tvCustomerName.setText(customer.shortCode + " - " + customer.getCustomerName());
			}else{
				tvCustomerName.setText(customer.getCustomerName());
			}
			tvCustomerAddress.setText(customer.address);
			if((typeUser == UserDTO.TYPE_PRESALES|| typeUser == UserDTO.TYPE_VALSALES || typeUser==UserDTO.TYPE_TNPG)
					&& (customer.getLat() <= 0 ||customer.getLng() <= 0)){
				llUpdate.setVisibility(View.VISIBLE);
				ivUpdate.setVisibility(View.VISIBLE);
				if (customer.getAreaId() != 0){
					//an hien thi dia ban
					llDiaBan.setVisibility(View.GONE);
					ivDiaBan.setVisibility(View.GONE);
				} else{
					//an hien thi dia ban
					llDiaBan.setVisibility(View.VISIBLE);
					ivDiaBan.setVisibility(View.VISIBLE);
				}
			}else{
				llUpdate.setVisibility(View.GONE);
				ivUpdate.setVisibility(View.GONE);
				//an hien thi dia ban
				llDiaBan.setVisibility(View.GONE);
				ivDiaBan.setVisibility(View.GONE);
			}
		}
	}

	public void showDiaBan(){
		llDiaBan.setVisibility(View.VISIBLE);
	}
}
