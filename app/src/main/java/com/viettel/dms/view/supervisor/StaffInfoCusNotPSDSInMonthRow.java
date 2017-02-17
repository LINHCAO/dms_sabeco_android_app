/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class StaffInfoCusNotPSDSInMonthRow extends TableRow implements OnClickListener {
	//private Context context;
	private View view;
	private TextView tvCusName;
	private TextView tvCusStreet;
	private TextView tvCusCode;
	private TextView tvSTT;

	public StaffInfoCusNotPSDSInMonthRow(Context context) {
		super(context);
		//this.context = context;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_staff_info_cus_no_psds_in_month_row, this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCusCode = (TextView) view.findViewById(R.id.tvCusCode);
		tvCusName = (TextView) view.findViewById(R.id.tvCusName);
		tvCusStreet = (TextView) view.findViewById(R.id.tvCusStreet);
	}

	public void renderLayout(CustomerListItem item, int pos) {
		tvSTT.setText("" + pos);
		tvCusCode.setText(item.aCustomer.customerCode);
		tvCusName.setText(item.aCustomer.customerName);
		tvCusStreet.setText(item.aCustomer.getStreet());
	}

	@Override
	public void onClick(View v) {

	}

}
