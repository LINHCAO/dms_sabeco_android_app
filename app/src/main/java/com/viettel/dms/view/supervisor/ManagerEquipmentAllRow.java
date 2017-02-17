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

import com.viettel.dms.dto.view.ManagerEquipmentDTO.ManagerEquipmentItem;
import com.viettel.sabeco.R;
/**
 * 
 *  row tat ca dung cho man hinh quan ly thiet bi cua TBHV
 *  @author: Nguyen Huu Hieu
 *  @version: 1.1
 *  @since: 1.0
 */
public class ManagerEquipmentAllRow extends TableRow{	
	ManagerEquipmentItem item;
	private View view;
	
	public TextView tvTongCong;
	public TextView tvSoThietBi;		
	public TextView tvKhongDat;	

	public ManagerEquipmentAllRow(Context context) {
		super(context);		
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_manager_equipment_all_row, this);		
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
