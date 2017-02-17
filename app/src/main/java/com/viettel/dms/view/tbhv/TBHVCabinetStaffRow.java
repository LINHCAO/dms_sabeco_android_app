/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

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
 * 
 *  Row hien thi thong tin quan ly thiet bi cua tung NV 
 *  su dung trong MH TBHVCabinetStaffView
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVCabinetStaffRow extends TableRow implements OnClickListener{
	private View view;
	public TextView tvSTT;//so thu tu
	public TextView tvMaKh;//Ma KH
	public TextView tvTenKh;//Ten KH
	public TextView tvDiaChi;//Dia chi
	public TextView tvKeHoach;//Ke hoach
	public TextView tvThucHien;//Thuc hien
	public TextView tvConLai;//Con lai
	public TextView tvDanhGia;//Danh gia

	public TBHVCabinetStaffRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_tracking_cabinet_staff_row, this);
		TableRow row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvMaKh = (TextView) view.findViewById(R.id.tvMaKh);
		tvTenKh = (TextView) view.findViewById(R.id.tvTenKh);
		tvDiaChi = (TextView) view.findViewById(R.id.tvDiaChi);		
		tvKeHoach = (TextView) view.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) view.findViewById(R.id.tvConLai);
		tvDanhGia = (TextView) view.findViewById(R.id.tvDanhGia);		
	}

	/**
	 * 
	*  Render layout
	*  @author: Nguyen Thanh Dung
	*  @param stt
	*  @param cabinetStaffItem
	*  @return: void
	*  @throws:
	 */
	public void render(int stt, CabinetStaffItem cabinetStaffItem) {
		tvSTT.setText("" + stt);
		tvMaKh.setText(cabinetStaffItem.maKh);
		tvTenKh.setText(cabinetStaffItem.tenKh);
		tvDiaChi.setText(cabinetStaffItem.diaChi);		
		tvKeHoach.setText(StringUtil.parseAmountMoney("" + cabinetStaffItem.keHoach));
		tvThucHien.setText(StringUtil.parseAmountMoney("" + cabinetStaffItem.thucHien));
		tvConLai.setText(StringUtil.parseAmountMoney("" + cabinetStaffItem.conLai));
		tvDanhGia.setText(cabinetStaffItem.danhGia);
		if(cabinetStaffItem.conLai > 0){//chua dat --> hightlight mau hong
			tvSTT.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvMaKh.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvTenKh.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvDiaChi.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvKeHoach.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
			tvThucHien.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);			
			tvDanhGia.setBackgroundResource(R.color.COLOR_VISIT_STORE_CLOSED);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
