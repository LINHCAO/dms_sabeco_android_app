/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVProgressDateReportDTO.TBHVProgressDateReportItem;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * 
 * row dung trong man hinh bao cao ngay cua TBHV
 * 
 * @author: Nguyen Huu Hieu
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVProgressDateReportRow extends TableRow implements OnClickListener {
	private View view;

	public TextView tvTenNPP;
	public TextView tvTenGSNPP;
	public TextView tvKeHoach;
	public TextView tvThucHien;
	public TextView tvConLai;
	public TextView tvSKUKeHoach;
	public TextView tvSKUThucHien;
	public int ACTION_MVN_CLICK = 0;
	public OnEventControlListener listener;
	TBHVProgressDateReportItem item;
	// 0: report from TBHV sumary , 1: report from TBHV detail
	public int typeScreen = -1;

	public TBHVProgressDateReportRow(Context context, int actionMNVClick, OnEventControlListener listener, int type) {
		super(context);
		ACTION_MVN_CLICK = actionMNVClick;
		this.listener = listener;
		this.typeScreen = type;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (listener instanceof TBHVReportProgressDateView) {
			view = vi.inflate(R.layout.layout_tbhv_progress_date_report_row, this);
		} else {
			view = vi.inflate(R.layout.layout_tbhv_progress_date_detail_report_row, this);
		}
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvTenNPP = (TextView) view.findViewById(R.id.tvTenNPP);
		tvTenGSNPP = (TextView) view.findViewById(R.id.tvTenGSNPP);
		if (typeScreen == 0) {
			tvTenNPP.setGravity(Gravity.CENTER);
			tvTenGSNPP.setOnClickListener(this);
			tvTenGSNPP.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			tvTenNPP.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		}
		tvTenNPP.setOnClickListener(this);
		
		tvSKUKeHoach = (TextView) view.findViewById(R.id.tvSKUKeHoach);
		tvSKUThucHien = (TextView) view.findViewById(R.id.tvSKUThucHien);
		tvKeHoach = (TextView) view.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) view.findViewById(R.id.tvConLai);
	}

	public void render(TBHVProgressDateReportItem progressDateReportItem) {
		item = progressDateReportItem;
		// tvTenNPP.setText(progressDateReportItem.tenNPP);
		if (typeScreen == 0) {
			tvTenNPP.setText(progressDateReportItem.maNPP);
		} else {
			tvTenNPP.setText(progressDateReportItem.tenNPP);
		}
		tvTenGSNPP.setText(progressDateReportItem.tenGSNPP);
		tvSKUKeHoach.setText(StringUtil.decimalFormatSymbols("#.##", progressDateReportItem.skuKeHoach));
		tvSKUThucHien.setText(StringUtil.decimalFormatSymbols("#.##", progressDateReportItem.skuThucHien));
		tvKeHoach.setText(StringUtil.parseAmountMoney(progressDateReportItem.keHoach));
		tvThucHien.setText(StringUtil.parseAmountMoney(progressDateReportItem.thucHien));
		tvConLai.setText(StringUtil.parseAmountMoney(progressDateReportItem.conLai));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (listener != null && tvTenNPP == v) {
			listener.onEvent(ACTION_MVN_CLICK, v, item);
		}
		if (listener != null && tvTenGSNPP == v) {
			listener.onEvent(ACTION_MVN_CLICK, v, item);
		}
	}
}
