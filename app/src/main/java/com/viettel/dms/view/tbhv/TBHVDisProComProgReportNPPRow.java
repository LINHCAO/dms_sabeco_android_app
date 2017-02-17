/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVDisProComProgReportNPPRow extends TableRow implements OnClickListener {

	// view
	private View view;
	// text Ma NVBH
	public TextView tvMaNVBH;
	// text ten NVBH
	public TextView tvTenNVBH;
	// data
	// private TBHVStaffDisProComProgReportItem data;
	protected VinamilkTableListener listener;
	// tong
	private TextView tvJoin;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public TBHVDisProComProgReportNPPRow(Context context, boolean sum) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi.inflate(R.layout.layout_tbhv_staff_dis_pro_com_prog_report_sum_row, this);

		} else {
			view = vi.inflate(R.layout.layout_tbhv_staff_dis_pro_com_prog_report_row, this);
		}
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
		tvMaNVBH.setOnClickListener(this);
		tvTenNVBH = (TextView) view.findViewById(R.id.tvTenNVBH);
		tvJoin = (TextView) view.findViewById(R.id.tvJoin);
	}

	// /**
	// * render row
	// * @param staffDisProComProgReportItem
	// */
	// public void render(TBHVStaffDisProComProgReportItem
	// staffDisProComProgReportItem) {
	// this.data = staffDisProComProgReportItem;
	// tvMaNVBH.setText(data.staffCode);
	// tvTenNVBH.setText(data.staffName);
	// tvJoin.setText(data.resultNumber + "/" + data.joinNumber);
	// }

	/**
	 * render sum row
	 * 
	 * @param resultNumber
	 * @param joinNumber
	 */
	public void renderSum(int resultNumber, int joinNumber) {
		tvJoin.setText(resultNumber + "/" + joinNumber);
	}

	@Override
	public void onClick(View paramView) {
		// // TODO Auto-generated method stub
		// if (paramView == tvMaNVBH && listener != null) {
		// listener.handleVinamilkTableRowEvent(0, paramView, data);
		// }
	}

}
