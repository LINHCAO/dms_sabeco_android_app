/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.HeaderTableInfo;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row hiển thị thông tin KH không phát sinh doanh số trong tháng cho NVBH
 * CustomerNotPSDSInMonthRow.java
 * 
 * @author: duongdt3
 * @version: 1.0
 * @since: 16:22:07 23 Nov 2013
 */
public class NVBHCustomerNotPSDSInMonthRow extends TableRow implements
		OnClickListener {

	// header table of this row
	// header table of this row
	private static HeaderTableInfo[] TABLE_HEADER = null;

	public static HeaderTableInfo[] getTableHeader(Context con) {
		if (TABLE_HEADER == null) {
			NVBHCustomerNotPSDSInMonthRow row = new NVBHCustomerNotPSDSInMonthRow(con);
			TABLE_HEADER = new HeaderTableInfo[] {
					new HeaderTableInfo(row.tvStt.getText().toString(), row.tvStt.getLayoutParams().width),
					new HeaderTableInfo(row.tvMaKh.getText().toString(), row.tvMaKh.getLayoutParams().width),
					new HeaderTableInfo(row.tvTenKh.getText().toString(), row.tvTenKh.getLayoutParams().width),
					new HeaderTableInfo(row.tvDiaChi.getText().toString(), row.tvDiaChi.getLayoutParams().width),
					new HeaderTableInfo(row.tvTuyen.getText().toString(), row.tvTuyen.getLayoutParams().width),
					new HeaderTableInfo(row.tvSlgt.getText().toString(), row.tvSlgt.getLayoutParams().width),
					new HeaderTableInfo(row.tvDsThangTruoc.getText().toString(), row.tvDsThangTruoc.getLayoutParams().width),
					new HeaderTableInfo(row.tvNgayDhCuoi.getText().toString(), row.tvNgayDhCuoi.getLayoutParams().width) };
		}
		return TABLE_HEADER;
	}

	private int ACTION_CLICK = 0;
	protected VinamilkTableListener listener;
	CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem item;
	private View view;

	public TextView tvStt;
	public TextView tvMaKh;
	public TextView tvTenKh;
	public TextView tvDiaChi;
	public TextView tvTuyen;
	public TextView tvSlgt;
	public TextView tvDsThangTruoc;
	public TextView tvNgayDhCuoi;
	private View[] arrView;

	private NVBHCustomerNotPSDSInMonthRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_customer_not_psds_in_month_report_row, this, false);
		TableRow row = (TableRow) view.findViewById(R.id.row);

		tvStt = (TextView) view.findViewById(R.id.tvStt);
		tvMaKh = (TextView) view.findViewById(R.id.tvMaKh);
		tvTenKh = (TextView) view.findViewById(R.id.tvTenKh);
		tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);
		tvTuyen = (TextView) view.findViewById(R.id.tvTuyen);
		tvSlgt = (TextView) view.findViewById(R.id.tvSlgt);
		tvDsThangTruoc = (TextView) view.findViewById(R.id.tvDsThangTruoc);
		tvNgayDhCuoi = (TextView) view.findViewById(R.id.tvNgayDhCuoi);

		arrView = new View[] { tvStt, tvMaKh, tvTenKh, tvDiaChi, tvTuyen,
				tvSlgt, tvDsThangTruoc, tvNgayDhCuoi };
		// remove in row
		row.removeAllViews();
		// add to this
		for (int i = 0; i < arrView.length; i++) {
			this.addView(arrView[i]);
		}
		this.setOnClickListener(this);
	}
	public NVBHCustomerNotPSDSInMonthRow(Context context,
			VinamilkTableListener listener, int action) {
		this(context);
		this.ACTION_CLICK = action;
		this.listener = listener;
	}

	/**
	 * Change background of row
	 * @author: duongdt3
	 * @since: 11:05:52 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param isSelected
	 */
	public void changeBackground(boolean isSelected) {
		if (isSelected) {
			setBackgroundRowByResource(R.drawable.bg_item_focus);
		} else {
			setBackgroundRowByResource(R.drawable.style_row_default);
		}
	}

	/**
	 * Change background of Aray View
	 * @author: duongdt3
	 * @since: 11:06:29 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param drawable
	 */
	private void setBackgroundRowByResource(int drawable) {
		for (int i = 0; i < arrView.length; i++) {
			ImageUtil.setBackgroundBySelectorDrawable(arrView[i], drawable);
		}
	}

	public void render(int pos, CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem info) {
		item = info;
		tvStt.setText("" + pos);
		tvMaKh.setText(info.customerCode);
		tvTenKh.setText(info.customerName);
		if (item.isLossDistribution == 1) {// co nguy co mat phan phoi
			setTextColorRow(Color.RED);
		}
		
		tvDiaChi.setText(info.address);
		tvTuyen.setText(info.listRoutingDate);
		String slgt = info.visitNumberInMonth + "/"
				+ info.visitNumberInMonthPlan;
		
		String strUnderLine = StringUtil.getUnderlineText(slgt);
		Spanned underLineSLGT = StringUtil.getHTMLText( strUnderLine ); 
		tvSlgt.setText(underLineSLGT);
		
		String dsThangTruoc = StringUtil.parseAmountMoney(info.amountInLastMonth);
		tvDsThangTruoc.setText(dsThangTruoc);
		tvNgayDhCuoi.setText(info.lastOrderDate);
	}

	/**
	 * Hiển thị màu chữ cho dòng
	 * @author: duongdt3
	 * @since: 09:15:35 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param color
	 */
	private void setTextColorRow(int color) {
		GlobalUtil.setTextColorViewInRow(arrView, color);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.handleVinamilkTableRowEvent(ACTION_CLICK, this, item);
		}
	}
}
