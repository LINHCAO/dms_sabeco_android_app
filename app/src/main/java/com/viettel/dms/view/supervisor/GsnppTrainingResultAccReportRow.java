/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO;
import com.viettel.dms.dto.view.GsnppTrainingResultAccReportDTO.GsnppTrainingResultAccReportItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * row cua man hinh bao cao luy ke ket qua huan luyen
 * 
 * @author hieunq1
 */
@SuppressLint("ViewConstructor")
public class GsnppTrainingResultAccReportRow extends TableRow implements OnClickListener{
	private View view;

	private GsnppTrainingResultAccReportItem data;

	protected VinamilkTableListener listener;

	private TextView tvMaNVBH;

	private TextView tvNVBH;

	private TextView tvDate;

	private TextView tvPlanAmount;

	private TextView tvAmount;

	private TextView tvKH;

	private TextView tvPSDS;

	private TextView tvMoi;

	private TextView tvON;

	private TextView tvPoint;

	private TextView tvNT;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public GsnppTrainingResultAccReportRow(Context context, boolean sum) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi.inflate(R.layout.layout_acc_train_result_report_row_sum,
					this);

		} else {
			view = vi
					.inflate(R.layout.layout_acc_train_result_report_row, this);
		}
		TableRow row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		
		tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
		tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
		tvDate = (TextView) view.findViewById(R.id.tvDate);
		tvPlanAmount = (TextView) view.findViewById(R.id.tvPlanAmount);
		tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		tvKH = (TextView) view.findViewById(R.id.tvKH);
		tvPSDS = (TextView) view.findViewById(R.id.tvPSDS);
		tvMoi = (TextView) view.findViewById(R.id.tvMoi);
		tvON = (TextView) view.findViewById(R.id.tvON);
		tvNT = (TextView) view.findViewById(R.id.tvNT);
		tvPoint = (TextView) view.findViewById(R.id.tvPoint);
		tvMaNVBH.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if(listener != null)
					listener.handleVinamilkTableRowEvent(0, v, data);
			}
		});
	}

	/**
	 * render row
	 * 
	 * @param accSaleProgReportItem
	 */
	public void render(GsnppTrainingResultAccReportItem item) {
		this.data = item;
		tvMaNVBH.setText(data.staffCode);
		tvNVBH.setText(data.staffName);
		tvDate.setText(data.date);
		tvPlanAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math
				.round(data.amountMonth / 1000.0))));
		tvAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math
				.round(data.amount / 1000.0))));
		tvKH.setText(String.valueOf(data.numCustomerPlan));
		tvPSDS.setText(String.valueOf(data.numCustomerOrder));
		tvMoi.setText(String.valueOf(data.numCustomerNew));
		tvON.setText(String.valueOf(data.numCustomerIr));
		tvNT.setText(String.valueOf(data.numCustomerOr));
		tvPoint.setText(String.valueOf((float)Math.round(data.score * 100) / 100.0));
	}

	/**
	 * render sum row
	 * 
	 * @param planTotal
	 * @param soldTotal
	 * @param remainTotal
	 */
	public void renderSum(GsnppTrainingResultAccReportDTO dto) {
		tvPlanAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math
				.round(dto.amountMonth / 1000.0))));
		tvAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math
				.round(dto.amount / 1000.0))));
		tvKH.setText(String.valueOf(dto.numCustomerPlan));
		tvPSDS.setText(String.valueOf(dto.numCustomerOrder));
		tvMoi.setText(String.valueOf(dto.numCustomerNew));
		tvON.setText(String.valueOf(dto.numCustomerOn));
		tvNT.setText(String.valueOf(dto.numCustomerOr));
		tvPoint.setText(String.valueOf(dto.listResult.size() > 0 ? String.valueOf((float)Math.round((dto.score/dto.listResult.size()) * 100) / 100.0) : 0.0));
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
}
