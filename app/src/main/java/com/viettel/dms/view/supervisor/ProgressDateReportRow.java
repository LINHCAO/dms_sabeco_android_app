/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ProgressDateReportDTO.ProgressDateReportItem;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Progress Date Report Row
 * ProgressDateReportRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:27:15 14 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class ProgressDateReportRow extends TableRow implements OnClickListener{
	private View view;
		
	public TextView tvMaNvbh;
	public TextView tvNvbh;		
	public TextView tvKh;
	public TextView tvTh;
	public TextView tvKeHoach;
	public TextView tvThucHien;
	public TextView tvConLai;
	public TextView tvDiem;
	public TextView tvDoanhSoKeHoachTuyen;
	public ImageButton ibDetail;
	// text notify when null
	TextView tvNotifyNull;
	public int ACTION_MVN_CLICK = 0;
	public OnEventControlListener listener;
	ProgressDateReportItem item;

	public ProgressDateReportRow(Context context, int actionMNVClick, OnEventControlListener listener) {
		super(context);
		ACTION_MVN_CLICK = actionMNVClick;
		this.listener = listener;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_progress_date_report_row, this);
		TableRow row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvMaNvbh = (TextView) view.findViewById(R.id.tvMaNvbh);
		tvMaNvbh.setOnClickListener(this);
		tvNvbh = (TextView) view.findViewById(R.id.tvNvbh);
		tvKh = (TextView) view.findViewById(R.id.tvKh);
		tvTh = (TextView) view.findViewById(R.id.tvTh);
		tvKeHoach = (TextView) view.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) view.findViewById(R.id.tvConLai);
		tvDiem = (TextView) view.findViewById(R.id.tvDiem);		
		tvDoanhSoKeHoachTuyen = (TextView) view.findViewById(R.id.tvDoanhSoKeHoachTuyen);		
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);
		ibDetail = (ImageButton)view.findViewById(R.id.ibDetail);
		ibDetail.setOnClickListener(this);
	}

	public void render(ProgressDateReportItem progressDateReportItem, double minScoreNotification) {
		item = progressDateReportItem;
		tvMaNvbh.setText(progressDateReportItem.maNvbh);
		tvNvbh.setText(progressDateReportItem.nvbh);
		tvKh.setText("" + progressDateReportItem.kh);
		tvTh.setText("" + progressDateReportItem.th);
		tvKeHoach.setText(StringUtil.parseAmountMoney(Math.round(progressDateReportItem.keHoach / 1000)));
		tvDoanhSoKeHoachTuyen.setText(StringUtil.parseAmountMoney(Math
				.round(progressDateReportItem.keHoachTuyen / 1000)));
		tvThucHien.setText(StringUtil.parseAmountMoney(Math.round(progressDateReportItem.thucHien / 1000)));
		tvConLai.setText(StringUtil.parseAmountMoney(Math.round(progressDateReportItem.conLai / 1000)));
		tvDiem.setText(progressDateReportItem.diem);
		tvDiem.setTextColor(Color.BLACK);
		if(!StringUtil.isNullOrEmpty(tvDiem.getText().toString()) && Float.parseFloat(tvDiem.getText().toString()) < minScoreNotification){
			tvDiem.setTextColor(Color.RED);
		}
	}
	
	public void render(String maNvbh, String nvbh, int kh, int th, Double keHoach, Double thucHien, Double conLai, String diem, double minScoreNotification, Double keHoachTuyen) {
		tvMaNvbh.setText(maNvbh);		
		tvNvbh.setText(nvbh);
		tvKh.setText("" + kh);
		tvTh.setText("" + th);
		tvKeHoach.setText(StringUtil.parseAmountMoney(Math.round(keHoach / 1000)));
		tvThucHien.setText(StringUtil.parseAmountMoney(Math.round(thucHien / 1000)));
		tvConLai.setText(StringUtil.parseAmountMoney(Math.round(conLai / 1000)));
		tvDoanhSoKeHoachTuyen.setText(StringUtil.parseAmountMoney(Math
				.round(keHoachTuyen / 1000)));
		tvDiem.setText(diem);
		tvDiem.setTextColor(Color.BLACK);
		if(!StringUtil.isNullOrEmpty(tvDiem.getText().toString()) && Float.parseFloat(tvDiem.getText().toString()) < minScoreNotification){
			tvDiem.setTextColor(Color.RED);
		}
		tvMaNvbh.setTypeface(null, Typeface.BOLD);
		tvNvbh.setTypeface(null, Typeface.BOLD);
		tvKh.setTypeface(null, Typeface.BOLD);
		tvTh.setTypeface(null, Typeface.BOLD);
		tvKeHoach.setTypeface(null, Typeface.BOLD);
		tvThucHien.setTypeface(null, Typeface.BOLD);
		tvConLai.setTypeface(null, Typeface.BOLD);
		tvDiem.setTypeface(null, Typeface.BOLD);
		tvDoanhSoKeHoachTuyen.setTypeface(null, Typeface.BOLD);
		ibDetail.setImageResource(R.color.TRANSPARENT);
	}
	
	/**
	 * 
	 * show row notify null
	 * 
	 * @param contentNotify
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void showNotifyNull(String contentNotify) {
		tvMaNvbh.setVisibility(View.GONE);
		tvNvbh.setVisibility(View.GONE);
		tvKh.setVisibility(View.GONE);
		tvTh.setVisibility(View.GONE);
		tvKeHoach.setVisibility(View.GONE);
		tvThucHien.setVisibility(View.GONE);
		tvConLai.setVisibility(View.GONE);
		tvDiem.setVisibility(View.GONE);
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setBackgroundColor(ImageUtil.getColor(R.color.WHITE));
		tvNotifyNull.setText(contentNotify);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == ibDetail){
			if(listener != null && item != null){
				listener.onEvent(ReportProgressDateView.ACTION_VIEW_DETAIL, this, item);
			}
		} else if (v == tvMaNvbh) {
			if(listener != null && item != null) {
				listener.onEvent(ReportProgressDateView.ACTION_MNV_CLICK, this, item);
			}
		}
	}

}
