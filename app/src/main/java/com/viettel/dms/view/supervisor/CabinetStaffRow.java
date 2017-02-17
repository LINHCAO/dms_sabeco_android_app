/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CabinetStaffDTO.CabinetStaffItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Cabinet Staff Row
 * CabinetStaffRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:21:52 14 Jan 2014
 */
public class CabinetStaffRow extends TableRow implements OnClickListener {
	private View view;
	public TextView tvSTT;
	public TextView tvMaKh;
	public TextView tvTenKh;
	public TextView tvDiaChi;
	public TextView tvKeHoach;
	public TextView tvThucHien;
	public TextView tvConLai;
	public TextView tvDanhGia;
	public TextView tvThietbi;

	public CabinetStaffRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tracking_cabinet_staff_row, this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvMaKh = (TextView) view.findViewById(R.id.tvMaKh);
		tvTenKh = (TextView) view.findViewById(R.id.tvTenKh);
		tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);
		tvKeHoach = (TextView) view.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) view.findViewById(R.id.tvConLai);
		tvDanhGia = (TextView) view.findViewById(R.id.tvDanhGia);
		tvThietbi = (TextView) view.findViewById(R.id.tvThietbi);
	}

	public void render(int stt, CabinetStaffItem cabinetStaffItem) {
		tvSTT.setText("" + stt);
		tvMaKh.setText(cabinetStaffItem.maKh);
		tvTenKh.setText(cabinetStaffItem.tenKh);
		tvDiaChi.setText(cabinetStaffItem.diaChi);
		tvKeHoach.setText(StringUtil.parseAmountMoney("" + cabinetStaffItem.keHoach));
		tvThucHien.setText(StringUtil.parseAmountMoney("" + cabinetStaffItem.thucHien));
		tvConLai.setText(StringUtil.parseAmountMoney("" + cabinetStaffItem.conLai));
		tvDanhGia.setText(cabinetStaffItem.danhGia);
		tvThietbi.setText(cabinetStaffItem.tenThietbi);
		if (cabinetStaffItem.ketqua <= 0) {// chua dat --> hightlight mau hong
			tvSTT.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvMaKh.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvTenKh.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvDiaChi.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvKeHoach.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvThucHien.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvDanhGia.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvThietbi.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
