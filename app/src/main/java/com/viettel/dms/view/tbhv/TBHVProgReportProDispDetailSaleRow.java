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

import com.viettel.dms.dto.view.TBHVProgReportProDispDetailSaleRowDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua lop (interface)
 * 
 * @author: HoanPD1
 * @modified: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVProgReportProDispDetailSaleRow extends TableRow implements OnClickListener {

	// view
	private View view;
	// text ma khach hang
	public TextView tvCustomerCode;
	// text ten khach hang
	public TextView tvCustomerName;
	// dia chi
	public TextView tvCustomerAddress;
	// tvMuc
	public TextView tvMuc;
	// tvMuc
	public TextView tvDinhMuc;
	// DS con lai
	public TextView tvRemainSale;
	// text view result
	public TextView tvResult;
	// text view notify null
	public TextView tvNotifyNull;
	// data
	private TBHVProgReportProDispDetailSaleRowDTO data;
	protected VinamilkTableListener listener;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public TBHVProgReportProDispDetailSaleRow(Context context, boolean sum) {
		super(context);
		// TODO Auto-generated constructor stub
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_prog_report_pro_disp_detail_sale_row, this);

		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
		tvCustomerCode.setOnClickListener(this);
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvCustomerAddress = (TextView) view.findViewById(R.id.tvCustomerAddress);
		tvRemainSale = (TextView) view.findViewById(R.id.tvRemainSale);
		tvMuc = (TextView) view.findViewById(R.id.tvMuc);
		tvDinhMuc = (TextView) view.findViewById(R.id.tvDinhMuc);
		tvResult = (TextView) view.findViewById(R.id.tvResult);
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);
	}

	// render row
	public void render(TBHVProgReportProDispDetailSaleRowDTO item) {
		this.data = item;
		tvCustomerCode.setText(item.customerCode);
		tvCustomerName.setText(item.customerName);
		tvCustomerAddress.setText(item.customerAddress);
		tvRemainSale.setText(StringUtil.parseAmountMoney("" + Math.round(item.remainSale / 1000.0)));
		tvMuc.setText(item.displayProgLevel);
		tvDinhMuc.setText(StringUtil.parseAmountMoney("" + Math.round(item.amountPlan / 1000.0)));
		
		if (item.amount == 0) {
			tvCustomerCode.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvCustomerName.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvCustomerAddress.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvRemainSale.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvResult.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvMuc.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvDinhMuc.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
		} 

		if (item.result > 0) {
			tvResult.setText(StringUtil.getString(R.string.ATTAIN));
		} else {
			tvResult.setText(StringUtil.getString(R.string.TEXT_NOT_ATTAIN));
		}
	}

	/**
	 * 
	 * render row notify if don't have any row
	 * 
	 * @param strNotify
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 12, 2013
	 */
	public void renderRowNull(String strNotify) {
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setText(strNotify);
		tvCustomerCode.setVisibility(View.GONE);
		tvCustomerName.setVisibility(View.GONE);
		tvCustomerAddress.setVisibility(View.GONE);
		tvRemainSale.setVisibility(View.GONE);
		tvResult.setVisibility(View.GONE);
		tvMuc.setVisibility(View.GONE);
		tvDinhMuc.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvCustomerCode && listener != null) {
			listener.handleVinamilkTableRowEvent(0, v, data);
		}
	}
}
