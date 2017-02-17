/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.DisProComProgReportDTO;
import com.viettel.dms.dto.view.DisProComProgReportDTO.DisProComProgReportItem;
import com.viettel.dms.dto.view.DisProComProgReportDTO.DisProComProgReportItemResult;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * row cua man hinh bao cao chuong trinh chung bay nhan vien ban hang
 * 
 * @author hieunq1
 * 
 */
@SuppressLint("ViewConstructor")
public class StaffDisProComProgReportRow extends TableRow implements
		OnClickListener {
	private View view;
	public TextView tvMaNVBH;
	public TextView tvTenNVBH;
	private DisProComProgReportItem data;
	protected VinamilkTableListener listener;
	public LinearLayout llLevel;
	public TextView tvTotal;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public StaffDisProComProgReportRow(Context context, boolean sum) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi
					.inflate(
							R.layout.layout_staff_dis_pro_com_prog_report_sum_row,
							this);

		} else {
			view = vi.inflate(
					R.layout.layout_staff_dis_pro_com_prog_report_row, this);
		}
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
		tvTenNVBH = (TextView) view.findViewById(R.id.tvTenNVBH);
		llLevel = (LinearLayout) view.findViewById(R.id.layoutLevel);
		tvTotal = (TextView) view.findViewById(R.id.tvTotal);
	}

	/**
	 * render row
	 * 
	 * @param staffDisProComProgReportItem
	 */
	public void render(DisProComProgReportItem item, int maxLevelDisPlay, int widthTVLevel) {
		this.data = item;
		tvMaNVBH.setText(item.programCodeShort);
		tvTenNVBH.setText(item.programNameShort);
		for (int i = 0; i < maxLevelDisPlay; i++) {
			DisProComProgReportItemResult rs = item.arrLevelCode.get(i);
			llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/"
					+ rs.joinNumber, ImageUtil.getColor(R.color.BLACK),
					Typeface.NORMAL));
		}
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvTotal.setLayoutParams(param);
		tvTotal.setText(item.itemResultTotal.resultNumber + "/"
				+ item.itemResultTotal.joinNumber);

	}

	private TextView createColumns(int width, String title, int resourceColor,
			int type) {
		TextView myTextView = new TextView(getContext());
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(width);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		myTextView.setLayoutParams(param);
		myTextView.setMinimumHeight(GlobalUtil.dip2Pixel(35));
		// myTextView.setTextAppearance(getContext(),
		// R.style.TextViewVinamilkTable);
		myTextView.setDuplicateParentStateEnabled(true);
		myTextView.setBackgroundResource(R.drawable.style_row_default);
		myTextView.setText(title);
		myTextView.setTextColor(resourceColor);
		myTextView.setTypeface(null, type);
		myTextView.setGravity(Gravity.CENTER);

		return myTextView;
	}

	/**
	 * render sum row
	 * 
	 * @param resultNumber
	 * @param joinNumber
	 */
	public void renderSum(DisProComProgReportDTO dto, int widthTVLevel) {
		for (int i = 0; i < dto.maxLevelDisPlay; i++) {
			DisProComProgReportItemResult rs = dto.arrResultTotal.get(i);
			llLevel.addView(createColumns(widthTVLevel, rs.resultNumber + "/"
					+ rs.joinNumber, ImageUtil.getColor(R.color.BLACK),
					Typeface.BOLD));
		}
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvTotal.setLayoutParams(param);
		tvTotal.setText(dto.dtoResultTotal.resultNumber + "/"
				+ dto.dtoResultTotal.joinNumber);
	}

	@Override
	public void onClick(View paramView) {
		// TODO Auto-generated method stub
		if (listener != null) {
			listener.handleVinamilkTableRowEvent(0, paramView, data);
		}
	}

}
