/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ProgressDateDetailReportDTO.ProgressDateDetailReportItem;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Progress Date Detail Report Row
 * ProgressDateDetailReportRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:27:00 14 Jan 2014
 */
public class ProgressDateDetailReportRow extends TableRow {
	private View view;
	
	public TextView tvSTT;
	public TextView tvMaNvbh;
	public TextView tvNvbh;		
	public TextView tvDiaChi;	
	public TextView tvMucTieu;
	public TextView tvThucHien;	
	public TextView tvDiem;
	public TextView tvMoi;
	public TextView tvOn;
	public TextView tvNT;	
	ProgressDateDetailReportItem item;

	public ProgressDateDetailReportRow(Context context) {
		super(context);		
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_progress_date_detail_report_row, this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvMaNvbh = (TextView) view.findViewById(R.id.tvMaNvbh);
		tvNvbh = (TextView) view.findViewById(R.id.tvNvbh);
		tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);		
		tvMucTieu = (TextView) view.findViewById(R.id.tvMucTieu);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);		
		tvDiem = (TextView) view.findViewById(R.id.tvDiem);		
		tvMoi = (TextView) view.findViewById(R.id.tvMoi);
		tvOn = (TextView) view.findViewById(R.id.tvOn);
		tvNT = (TextView) view.findViewById(R.id.tvNT);		
	}

	public void render(ProgressDateDetailReportItem progressDateReportItem, double minScoreNotification) {
		item = progressDateReportItem;
		tvSTT.setText(progressDateReportItem.stt);
		tvMaNvbh.setText(progressDateReportItem.maKh);
		tvNvbh.setText(progressDateReportItem.kh);
		tvDiaChi.setText("" + progressDateReportItem.diaChi);		
		tvMucTieu.setText(StringUtil.parseAmountMoney(Math.round(progressDateReportItem.mucTieu / 1000)));
		tvThucHien.setText(StringUtil.parseAmountMoney(Math.round(progressDateReportItem.thucHien / 1000)));		
		tvDiem.setText("" + progressDateReportItem.diem);
		tvMoi.setText("" + progressDateReportItem.moi);
		tvOn.setText("" + progressDateReportItem.on);
		tvNT.setText("" + progressDateReportItem.nt);
		tvDiem.setTextColor(ImageUtil.getColor(R.color.BLACK));
		if(!StringUtil.isNullOrEmpty(tvDiem.getText().toString()) && Float.parseFloat(tvDiem.getText().toString()) < minScoreNotification){
			tvDiem.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}
	
	public void render(String maNvbh, String nvbh, String diaChi, Double mucTieu, Double thucHien, Double diem, int moi, int on, int nt, double minScoreNotification) {
		tvMaNvbh.setText(maNvbh);
		tvNvbh.setText(nvbh);
		tvDiaChi.setText(diaChi);		
		tvMucTieu.setText(StringUtil.parseAmountMoney(Math.round(mucTieu / 1000)));
		tvThucHien.setText(StringUtil.parseAmountMoney(Math.round(thucHien / 1000)));		
		tvDiem.setText("" + diem);
		tvMoi.setText("" + moi);
		tvOn.setText("" + on);
		tvNT.setText("" + nt);
		tvDiem.setTextColor(ImageUtil.getColor(R.color.BLACK));
		if(!StringUtil.isNullOrEmpty(tvDiem.getText().toString()) && Float.parseFloat(tvDiem.getText().toString()) < minScoreNotification){
			tvDiem.setTextColor(ImageUtil.getColor(R.color.RED));
		}
		tvMaNvbh.setTypeface(null, Typeface.BOLD);
		tvNvbh.setTypeface(null, Typeface.BOLD);
		tvDiaChi.setTypeface(null, Typeface.BOLD);		
		tvMucTieu.setTypeface(null, Typeface.BOLD);
		tvThucHien.setTypeface(null, Typeface.BOLD);		
		tvDiem.setTypeface(null, Typeface.BOLD);
		tvMoi.setTypeface(null, Typeface.BOLD);
		tvOn.setTypeface(null, Typeface.BOLD);
		tvNT.setTypeface(null, Typeface.BOLD);		
	}

}
