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

import com.viettel.dms.dto.view.WrongPlanCustomerDTO.WrongPlanCustomerItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Wrong Customer List Row
 * WrongCustomerListRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:31:30 14 Jan 2014
 */
public class WrongCustomerListRow extends TableRow implements OnClickListener {
	private View view;
	public TextView tvCusName;
	public TextView tvCusStreet;
	public TextView tvPlan;
	public TextView tvTime;
	public TextView tvQuantityInDate;

	public WrongCustomerListRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_wrong_cus_row, this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);

		tvCusName = (TextView) view.findViewById(R.id.tvCusName);
		tvCusStreet = (TextView) view.findViewById(R.id.tvCusStreet);
		tvPlan = (TextView) view.findViewById(R.id.tvPlan);
		tvTime = (TextView) view.findViewById(R.id.tvTime);
		tvQuantityInDate = (TextView) view.findViewById(R.id.tvRevenue);
	}

	public void renderLayout(WrongPlanCustomerItem item) {
		tvCusName.setText(item.cusCodeName);
		tvCusStreet.setText(item.aCustomer.getStreet());
		tvPlan.setText(item.plan);
		tvTime.setText(item.visitedTime);
		tvQuantityInDate.setText(StringUtil.parseAmountMoney(item.quantityInDay));
	}

	@Override
	public void onClick(View v) {

	}
}
