/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ManagerEquipmentDTO.ManagerEquipmentItem;
import com.viettel.sabeco.R;

/**
 * 
 *  Row tong cong cua MH TBHVManagerEquipmentDetailView
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVManagerEquipmentDetailAllRow extends TableRow{	
	ManagerEquipmentItem item;
	private View view;
	
	public TextView tvTongCong;//tong cong
	public TextView tvSoThietBi;//so thiet bi
	public TextView tvKhongDat;//khong dat

	public TBHVManagerEquipmentDetailAllRow(Context context) {
		super(context);		
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_manager_equipment_detail_all_row, this);		
		tvTongCong = (TextView) view.findViewById(R.id.tvTongCong);
		tvSoThietBi = (TextView) view.findViewById(R.id.tvSoKhachHang);
		tvKhongDat = (TextView) view.findViewById(R.id.tvKhongDat);				
	}
	
	public void render(int soThietBi, int khongDat) {		
		tvSoThietBi.setText("" + soThietBi);
		tvKhongDat.setText("" + khongDat);				
		tvTongCong.setTypeface(null, Typeface.BOLD);		
		tvSoThietBi.setTypeface(null, Typeface.BOLD);
		tvKhongDat.setTypeface(null, Typeface.BOLD);
	}
}
