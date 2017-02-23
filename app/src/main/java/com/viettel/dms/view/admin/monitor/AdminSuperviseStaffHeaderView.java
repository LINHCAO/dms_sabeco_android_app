/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.admin.monitor;

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
 * Header for admin supervise staff view
 * 
 * @author : tuanlt1989
 */
public class AdminSuperviseStaffHeaderView extends LinearLayout {

	public AdminSuperviseStaffHeaderView(Context context) {
		super(context);
		initView(context);
	}

	public AdminSuperviseStaffHeaderView(Context context, AttributeSet attrs) {
		super(context);
		initView(context);
	}

	/**
	 * Init view
	 * @param mContext
     */
	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.layout_admin_supervise_staff_header, this);
	}
}
