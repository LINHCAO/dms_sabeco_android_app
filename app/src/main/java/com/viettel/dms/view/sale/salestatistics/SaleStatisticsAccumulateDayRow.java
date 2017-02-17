/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.salestatistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.SaleStatisticsAccumulateDayDTO.SaleStatisticsAccumulateDayItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * 
 * row dung cho man hinh don tong luy ke ngay cua NVBH
 * 
 * @author: Nguyen Huu Hieu
 * @version: 1.1
 * @since: 1.0
 */
public class SaleStatisticsAccumulateDayRow extends TableRow implements
		OnClickListener {
	SaleStatisticsAccumulateDayItem item;
	private final View view;

	public TextView tvStt;
	public TextView tvMaHang;
	public TextView tvTenMatHang;
	public TextView tvNH;
	public TextView tvKeHoach;
	public TextView tvThucHien;
	public TextView tvConLai;
	public TextView tvTienDo;

	public SaleStatisticsAccumulateDayRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_sale_statistics_accumulate_day_row,
				this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvStt = (TextView) view.findViewById(R.id.tvStt);
		tvMaHang = (TextView) view.findViewById(R.id.tvMaHang);
		tvTenMatHang = (TextView) view.findViewById(R.id.tvTenMatHang);
		tvNH = (TextView) view.findViewById(R.id.tvNH);
		tvKeHoach = (TextView) view.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) view.findViewById(R.id.tvConLai);
		tvTienDo = (TextView) view.findViewById(R.id.tvTienDo);
	}

	public void render(int pos,
			SaleStatisticsAccumulateDayItem saleStatisticsAccumulateDayItem) {
		item = saleStatisticsAccumulateDayItem;
		tvStt.setText("" + pos);
		tvMaHang.setText(saleStatisticsAccumulateDayItem.maHang);
		tvTenMatHang.setText(saleStatisticsAccumulateDayItem.tenMatHang);
		tvNH.setText("" + saleStatisticsAccumulateDayItem.nh);
		tvKeHoach.setText(StringUtil
				.parseAmountMoney(saleStatisticsAccumulateDayItem.keHoach));
		tvThucHien.setText(StringUtil
				.parseAmountMoney(saleStatisticsAccumulateDayItem.thucHien));
		tvConLai.setText(StringUtil
				.parseAmountMoney(saleStatisticsAccumulateDayItem.conLai));
		tvTienDo.setText(saleStatisticsAccumulateDayItem.tienDo);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
