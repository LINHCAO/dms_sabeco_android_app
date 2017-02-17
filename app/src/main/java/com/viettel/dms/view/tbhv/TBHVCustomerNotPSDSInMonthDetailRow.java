/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Color;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.HeaderTableInfo;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row hiển thị thông tin KH không phát sinh doanh số trong tháng cho TBHV
 * Khi xem chi tiet
 * GSBHCustomerNotPSDSInMonthRow.java
 * 
 * @author: QuangVT
 * @version: 1.0 
 */
public class TBHVCustomerNotPSDSInMonthDetailRow extends TableRow implements
		OnClickListener { 
 
	// Header table
	private static HeaderTableInfo[] TABLE_HEADER = null;
	// Action click row
	private int ACTION_CLICK = 0;
	// Listener xu ly khi click
	protected VinamilkTableListener listener;
	// Item de render
	private CustomerNotPsdsInMonthReportItem item;
	
	//View root
	private View view;
	// Column STT
	public TextView tvStt;
	// Column Thong tin khach hang
	public TextView tvCus;
	// Column Address
	public TextView tvAddress;
	// Column Staff Name
	public TextView tvStaffName;
	// Column tuyen
	public TextView tvLine;
	// Column so lan ghe tham
	public TextView tvNumVisited;
	// Column doanh so thang truoc
	public TextView tvDsThangTruoc;
	// Column ngay dat hang cuoi cung
	public TextView tvNgayDhCuoi;
	// Danh sach column
	private View[] arrView;
	// Dang duoc chon
	public boolean isSelected = false;

	/**
	 * Lay Header cua table
	 * 
	 * @author quangvt1
	 * @param con
	 * @return
	 */
	public static HeaderTableInfo[] getTableHeader(Context con) {
		if (TABLE_HEADER == null) {
			TBHVCustomerNotPSDSInMonthDetailRow row = new TBHVCustomerNotPSDSInMonthDetailRow(con);
			TABLE_HEADER = new HeaderTableInfo[] {
					new HeaderTableInfo(row.tvStt.getText().toString(), row.tvStt.getLayoutParams().width),
					new HeaderTableInfo(row.tvCus.getText().toString(), row.tvCus.getLayoutParams().width),
					new HeaderTableInfo(row.tvAddress.getText().toString(), row.tvAddress.getLayoutParams().width),
					new HeaderTableInfo(row.tvStaffName.getText().toString(), row.tvStaffName.getLayoutParams().width),
					new HeaderTableInfo(row.tvLine.getText().toString(), row.tvLine.getLayoutParams().width),
					new HeaderTableInfo(row.tvNumVisited.getText().toString(), row.tvNumVisited.getLayoutParams().width),
					new HeaderTableInfo(row.tvDsThangTruoc.getText().toString(), row.tvDsThangTruoc.getLayoutParams().width),
					new HeaderTableInfo(row.tvNgayDhCuoi.getText().toString(), row.tvNgayDhCuoi.getLayoutParams().width) };
		}
		return TABLE_HEADER;
	} 

	/**
	 * Khoi tao doi tuong
	 * 
	 * @author quangvt1
	 * @param context
	 */
	private TBHVCustomerNotPSDSInMonthDetailRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_customer_not_psds_in_month_report_detail_row, this, false);
		TableRow row = (TableRow) view.findViewById(R.id.row);

		tvStt 		= (TextView) view.findViewById(R.id.tvStt);
		tvCus 		= (TextView) view.findViewById(R.id.tvMaKh);
		tvAddress 	= (TextView) view.findViewById(R.id.tvDiaChi);
		tvStaffName = (TextView) view.findViewById(R.id.tvNVBH);
		tvLine 		= (TextView) view.findViewById(R.id.tvTuyen);
		tvNumVisited 	= (TextView) view.findViewById(R.id.tvSlgt);
		tvDsThangTruoc 	= (TextView) view.findViewById(R.id.tvDsThangTruoc);
		tvNgayDhCuoi 	= (TextView) view.findViewById(R.id.tvNgayDhCuoi);

		arrView = new View[] { tvStt, tvCus, tvAddress, tvStaffName, tvLine,
				tvNumVisited, tvDsThangTruoc, tvNgayDhCuoi };
		
		// remove in row
		row.removeAllViews();
		
		// add to this
		for (int i = 0; i < arrView.length; i++) {
			this.addView(arrView[i]);
		}
		
		this.setOnClickListener(this);
	}
	
	/**
	 * Khoi tao doi tuong
	 * 
	 * @author quangvt1
	 * @param context
	 * @param listener
	 * @param action
	 */
	public TBHVCustomerNotPSDSInMonthDetailRow(Context context,
			VinamilkTableListener listener, int action) {
		this(context);
		this.ACTION_CLICK = action;
		this.listener = listener;
	}

	/**
	 * Thay doi background cua dong
	 * 
	 * @author quangvt1
	 * @param isSelected
	 */
	public void changeBackground(boolean isSelected) {
		if (isSelected && !this.isSelected) {
			setBackgroundRowByResource(R.drawable.bg_item_focus);
		} else if(!isSelected && this.isSelected) {
			setBackgroundRowByResource(R.drawable.style_row_default);
		}
		
		this.isSelected = isSelected;
	}

	/**
	 * Set background cho dong
	 * 
	 * @author quangvt1
	 * @param drawable
	 */
	private void setBackgroundRowByResource(int drawable) {
		for (int i = 0; i < arrView.length; i++) {
			ImageUtil.setBackgroundBySelectorDrawable(arrView[i], drawable);
		}
	}

	/**
	 * Render view
	 * 
	 * @author quangvt1
	 * @param pos
	 * @param info
	 */
	public void render(int pos, CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem info) {
		item = info;
		
		final String strSTT 	   = pos + "";
		final String strCus 	   = info.customerCode + " - " + info.customerName;
		final String strAddress    = info.address;
		final String strStaffName  = info.staffName;
		final String strRouting    = info.listRoutingDate;
		final String strNumVisited = info.visitNumberInMonth + "/" + info.visitNumberInMonthPlan;
		final String strAmount     = StringUtil.parseAmountMoney(info.amountInLastMonth);
		final String strLastOrder  = info.lastOrderDate;
		
		tvStt.setText(strSTT);
		tvCus.setText(strCus);
		tvAddress.setText(strAddress); 
		tvStaffName.setText(strStaffName);  
		tvLine.setText(strRouting); 
		
		String strUnderLine = StringUtil.getUnderlineText(strNumVisited);
		Spanned underLineSLGT = StringUtil.getHTMLText( strUnderLine ); 
		tvNumVisited.setText(underLineSLGT); 
		  
		tvDsThangTruoc.setText(strAmount);
		tvNgayDhCuoi.setText(strLastOrder);
		
		// co nguy co mat phan phoi
		if (item.isLossDistribution == 1) {
			setTextColor(Color.RED);
		}
	}

	/**
	 * Set TextColor cho cac cot
	 * 
	 * @author quangvt1
	 * @param color
	 */
	private void setTextColor(int color) {
		tvStt.setTextColor(color);
		tvCus.setTextColor(color);
		tvStaffName.setTextColor(color);
		tvAddress.setTextColor(color);
		tvLine.setTextColor(color);
		tvNumVisited.setTextColor(color);
		tvDsThangTruoc.setTextColor(color);
		tvNgayDhCuoi.setTextColor(color);
	} 
	
	@Override
	public void onClick(View arg0) {
		if (listener != null) {
			listener.handleVinamilkTableRowEvent(ACTION_CLICK, this, item);
		}
	}
}
