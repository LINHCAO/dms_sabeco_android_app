/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ManagerEquipmentDTO.ManagerEquipmentItem;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Manager Equipment Row
 * ManagerEquipmentRow.java
 * @version: 1.0 
 * @since:  08:30:32 20 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class ManagerEquipmentRow extends TableRow implements OnClickListener{
	private int ACTION_CLICK = 0;
	protected OnEventControlListener listener;
	ManagerEquipmentItem item;
	private View view;
	
	public TextView tvMaNVBH;
	public TextView tvNVBH;
	public TextView tvSoThietBi;		
	public TextView tvKhongDat;	

	public ManagerEquipmentRow(Context context, int action) {
		super(context);
		this.ACTION_CLICK = action;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_manager_equipment_row, this);
		TableRow row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
		tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
		tvSoThietBi = (TextView) view.findViewById(R.id.tvSoKhachHang);
		tvKhongDat = (TextView) view.findViewById(R.id.tvKhongDat);				
	}
	

	public void render(ManagerEquipmentItem managerEquipmentItem) {		
		item = managerEquipmentItem;
		tvMaNVBH.setText(managerEquipmentItem.maNVBH);
		tvNVBH.setText(managerEquipmentItem.nvbh);
		tvSoThietBi.setText("" + managerEquipmentItem.soThietBi);
		tvKhongDat.setText("" + managerEquipmentItem.khongDat);				
	}
	
	public void render(String maNVBH, String nvbh, int soThietBi, int khongDat) {		
		tvMaNVBH.setText(maNVBH);
		tvNVBH.setText(nvbh);
		tvSoThietBi.setText("" + soThietBi);
		tvKhongDat.setText("" + khongDat);				
		tvMaNVBH.setTypeface(null, Typeface.BOLD);
		tvNVBH.setTypeface(null, Typeface.BOLD);
		tvSoThietBi.setTypeface(null, Typeface.BOLD);
		tvKhongDat.setTypeface(null, Typeface.BOLD);
	}
	
	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(listener != null){
			listener.onEvent(ACTION_CLICK, this, item);
		}
	}

}
