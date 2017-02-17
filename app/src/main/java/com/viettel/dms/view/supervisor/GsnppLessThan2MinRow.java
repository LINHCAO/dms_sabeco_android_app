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

import com.viettel.dms.dto.view.GsnppLessThan2MinsDTO.LessThan2MinsItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class GsnppLessThan2MinRow extends TableRow implements OnClickListener {

	private View view;
	private TextView tvCusName;
	private TextView tvCusStreet;
	private TextView tvStartTime;
	private TextView tvEndTime;
	private TextView tvRevenue;

	/**
	 * @param context
	 */
	public GsnppLessThan2MinRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_less_than_2_min_row, this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvCusName = (TextView) view.findViewById(R.id.tvCusName);
		tvCusStreet = (TextView) view.findViewById(R.id.tvCusStreet);
		tvStartTime = (TextView) view.findViewById(R.id.tvStartTime);
		tvEndTime = (TextView) view.findViewById(R.id.tvEndTime);
		tvRevenue = (TextView) view.findViewById(R.id.tvRevenue);
	}
	
	public void renderLayout(LessThan2MinsItem item) {
		tvCusName.setText(item.customerName);
		tvCusStreet.setText(item.customerAddress);
		tvStartTime.setText(item.startTime);
		tvEndTime.setText(item.endTime);
		tvRevenue.setText(StringUtil.parseAmountMoney(item.sales));
	}
	
	@Override
	public void onClick(View v) {

	}

}
