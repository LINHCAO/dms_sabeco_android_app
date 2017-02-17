/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVRouteSaleListDTO.TBHVRouteSaleListItem;
import com.viettel.dms.util.ImageUtil;
import com.viettel.sabeco.R;

/**
 * 
 * row dung cho man hinh danh sach diem ban di huan luyen
 * 
 * @author: HieuNH6
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVRouteSaleListRow extends TableRow implements OnClickListener {
	private View view;
	public TextView tvKhachHang;
	public TextView tvDiaChi;
	public TextView tvThoiGianNVBH;
	public TextView tvThuTuNVBH;
	public TextView tvThoiGianGSNPP;
	public TextView tvThuTuGSNPP;

	// public TextView tvMota;

	public TBHVRouteSaleListRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_route_sale_list_row, this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);

		tvKhachHang = (TextView) view.findViewById(R.id.tvKhachHang);
		tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);
		tvThoiGianNVBH = (TextView) view.findViewById(R.id.tvThoiGianNVBH);
		tvThuTuNVBH = (TextView) view.findViewById(R.id.tvThuTuNVBH);
		tvThoiGianGSNPP = (TextView) view.findViewById(R.id.tvThoiGianGSNPP);
		tvThuTuGSNPP = (TextView) view.findViewById(R.id.tvThuTuGSNPP);
		// tvMota = (TextView) view.findViewById(R.id.tvMota);
	}

	public void renderLayout(TBHVRouteSaleListItem item) {
		tvKhachHang.setText(item.khachHang);
		tvDiaChi.setText(item.diaChi);
		tvThoiGianNVBH.setText(item.thoiGianNVBH);
		tvThoiGianGSNPP.setText(item.thoiGianGSNPP);
		if (item.thuTuGSNPP > 0) {
			tvThuTuGSNPP.setText("" + item.thuTuGSNPP);
		} else {
			tvThuTuGSNPP.setText("");
		}
		if (item.thuTuNVBH > 0) {
			tvThuTuNVBH.setText("" + item.thuTuNVBH);
		} else {
			tvThuTuNVBH.setText("");
		}
		// tvMota.setText(item.mota);

		// xu ly dac biet
		// Nếu NVBH đang ghé thăm điểm bán này thì sau thời gian hiển thị thêm
		// dấu “*”
		if (item.isNVBHVisiting) {
			tvThoiGianNVBH.setText(item.thoiGianNVBH + " *");
		}
		if (item.isNPPVisiting) {
			tvThoiGianGSNPP.setText(item.thoiGianGSNPP + " *");
			tvThoiGianGSNPP.setTextColor(Color.RED);
		}
		// Bôi đỏ background của các khách hàng được tính là sai tuyến (tương
		// ứng với số sai tuyến trong danh sách ở màn hình chính)
		if (item.isWrongPlan) {
			tvKhachHang.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvDiaChi.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvThoiGianNVBH.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvThuTuNVBH.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvThoiGianGSNPP.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvThuTuGSNPP.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			// tvMota.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));

		}
	}

	@Override
	public void onClick(View v) {

	}
}
