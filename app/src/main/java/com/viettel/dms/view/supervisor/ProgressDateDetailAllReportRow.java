/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.DecimalFormat;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ProgressDateDetailReportDTO.ProgressDateDetailReportItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Progress Date Detail All Report Row
 * ProgressDateDetailAllReportRow.java
 * @version: 1.0 
 * @since:  08:30:05 20 Jan 2014
 */
public class ProgressDateDetailAllReportRow extends TableRow {
	private View view;
	
	public TextView tvTong;	
	public TextView tvMucTieu;
	public TextView tvThucHien;	
	public TextView tvDiem;
	public TextView tvMoi;
	public TextView tvOn;
	public TextView tvNT;	
	//thong bao khong co du lieu
	TextView tvNoResultInfo;
	ProgressDateDetailReportItem item;

	public ProgressDateDetailAllReportRow(Context context) {
		super(context);		
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_progress_date_detail_all_report_row, this);
		tvTong = (TextView) view.findViewById(R.id.tvTong);		
		tvMucTieu = (TextView) view.findViewById(R.id.tvMucTieu);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);		
		tvDiem = (TextView) view.findViewById(R.id.tvDiem);		
		tvMoi = (TextView) view.findViewById(R.id.tvMoi);
		tvOn = (TextView) view.findViewById(R.id.tvOn);
		tvNT = (TextView) view.findViewById(R.id.tvNT);		
		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
	}	
	
	public void render(Double mucTieu, Double thucHien, Double diem, int moi, int on, int nt) {				
		tvMucTieu.setText(StringUtil.parseAmountMoney(Math.round(mucTieu / 1000)));
		tvThucHien.setText(StringUtil.parseAmountMoney(Math.round(thucHien / 1000)));
		DecimalFormat df = new DecimalFormat("#.#");
		tvDiem.setText("TB: " + df.format(diem));
		tvMoi.setText("" + moi);
		tvOn.setText("" + on);
		tvNT.setText("" + nt);
		tvTong.setTypeface(null, Typeface.BOLD);		
		tvMucTieu.setTypeface(null, Typeface.BOLD);
		tvThucHien.setTypeface(null, Typeface.BOLD);		
		tvDiem.setTypeface(null, Typeface.BOLD);
		tvMoi.setTypeface(null, Typeface.BOLD);
		tvOn.setTypeface(null, Typeface.BOLD);
		tvNT.setTypeface(null, Typeface.BOLD);		
	}

	/**
	 * 
	*  Layout row thong bao khong co du lieu
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	public void renderLayoutNoResult() {
		tvTong.setVisibility(View.GONE);
		tvMucTieu.setVisibility(View.GONE);
		tvThucHien.setVisibility(View.GONE);
		tvDiem.setVisibility(View.GONE);
		tvMoi.setVisibility(View.GONE);
		tvOn.setVisibility(GONE);
		tvNT.setVisibility(GONE);
		tvNoResultInfo.setVisibility(View.VISIBLE);
		tvNoResultInfo.setText(StringUtil.getString(R.string.TEXT_GSNPP_NOT_REPORT_PROGRESS_DATE));
	}
}
