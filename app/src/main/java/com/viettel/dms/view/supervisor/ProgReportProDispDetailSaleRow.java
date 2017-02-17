/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ProgReportProDispDetailSaleRowDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * 
 * 
 * Row cho man hinh bao cao chi tiet trung bay - khach hang cua nhan vien ban hang 
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class ProgReportProDispDetailSaleRow extends TableRow implements OnClickListener{
	private View view;
	public LinearLayout llLevel;
	public TextView tvCustomerCode;
	public TextView tvCustomerName;
	public TextView tvCustomerAddress;
	public TextView tvCustomerLevel;
	public TextView tvCustomerAmountPlan;
	public TextView tvRemainSale;
	//private ProgReportProDispDetailSaleRowDTO data;
	protected VinamilkTableListener listener;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public ProgReportProDispDetailSaleRow(Context context, boolean sum) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi.inflate(
					R.layout.layout_prog_report_pro_disp_detail_sale_row_sum,
					this);
		} else {
			view = vi.inflate(
					R.layout.layout_prog_report_pro_disp_detail_sale_row, this);
		}
		TableRow row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);

		tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvCustomerAddress = (TextView) view
				.findViewById(R.id.tvCustomerAddress);
		tvRemainSale = (TextView) view.findViewById(R.id.tvRemainSale);
		tvCustomerLevel = (TextView) view.findViewById(R.id.tvCustomerLevel);
		tvCustomerAmountPlan = (TextView) view.findViewById(R.id.tvCustomerAmountPlan);
	}

	public void render(ProgReportProDispDetailSaleRowDTO item) {
		//this.data = item;
		tvCustomerCode.setText(item.customerCode);
		tvCustomerName.setText(item.customerName);
		tvCustomerAddress.setText(item.CustomerAddress);
		tvRemainSale.setText(StringUtil.parseAmountMoney(""
				+ Math.round(item.RemainSale / 1000.0)));
		tvCustomerLevel.setText(item.level);
		tvCustomerAmountPlan.setText(StringUtil.parseAmountMoney(""
				+ Math.round(item.amountPlan / 1000.0)));
		if(item.result == 0){
			setColorBackgroundForRow(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));			
		}
	}
	
	/**
	 * 
	 * set background color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Nov 6, 2012
	 */
	public void setColorBackgroundForRow(int color) {
		tvCustomerCode.setBackgroundColor(color);
		tvCustomerName.setBackgroundColor(color);
		tvCustomerAddress.setBackgroundColor(color);
		tvCustomerLevel.setBackgroundColor(color);
		tvCustomerAmountPlan.setBackgroundColor(color);
		tvRemainSale.setBackgroundColor(color);		
	}

	public void renderSum(long RemainSaleTotal) {
		tvRemainSale.setText(StringUtil.parseAmountMoney(""
				+ RemainSaleTotal));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
