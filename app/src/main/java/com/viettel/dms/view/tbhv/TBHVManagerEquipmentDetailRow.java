/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVManagerEquipmentDTO.TBHVManagerEquipmentItem;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 *  Table row cua Man hinh quan ly thiet bi chi tiet theo NVBH cua NPP 
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVManagerEquipmentDetailRow extends TableRow implements OnClickListener{
	private int ACTION_CLICK = 0;
	Context _context;
	protected OnEventControlListener listener;
	TBHVManagerEquipmentItem item;
	private View view;
	
	public TextView tvMaNVBH;//Ma NVBH
	public TextView tvNVBH;//Ten NVBH
	public TextView tvSoThietBi;//So thiet bi		
	public TextView tvKhongDat;	//Khong dat
	private TableRow row;

	public TBHVManagerEquipmentDetailRow(Context context, int action) {
		super(context);
		_context = context;
		this.ACTION_CLICK = action;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_manager_equipment_detail_row, this);
		row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
		tvMaNVBH.setOnClickListener(this);
		tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
		tvSoThietBi = (TextView) view.findViewById(R.id.tvSoKhachHang);
		tvKhongDat = (TextView) view.findViewById(R.id.tvKhongDat);				
	}
	
	/**
	 * 
	*  Render layout voi du lieu cung cap
	*  @author: Nguyen Thanh Dung
	*  @param managerEquipmentItem
	*  @return: void
	*  @throws:
	 */
	public void render(TBHVManagerEquipmentItem managerEquipmentItem) {		
		item = managerEquipmentItem;
		tvMaNVBH.setText(managerEquipmentItem.maNVBH);
		tvNVBH.setText(managerEquipmentItem.nvbh);
		tvSoThietBi.setText("" + managerEquipmentItem.soThietBi);
		tvKhongDat.setText("" + managerEquipmentItem.khongDat);				
	}

	/**
	 * 
	*  Render layout voi du lieu cung cap
	*  @author: Nguyen Thanh Dung
	*  @param managerEquipmentItem
	*  @return: void
	*  @throws:
	 */
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
		if(arg0 == tvMaNVBH) {
			listener.onEvent(ACTION_CLICK, this, item);
		} else if (arg0 == row && _context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)_context);
		}
	}

}
