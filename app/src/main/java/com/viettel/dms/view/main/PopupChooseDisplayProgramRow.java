/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.DisplayProgrameDTO;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row hien thi thong tin CTTB tren popup
 * 
 * @author BANGHN
 * @version 1.0
 */
public class PopupChooseDisplayProgramRow extends TableRow implements
		OnClickListener {
	int actionView = -1;
	private TableRow row;
	TextView tvSTT;
	TextView tvCode;
	TextView tvName;
	// listener when row have action
	protected VinamilkTableListener listener;
	//dto
	DisplayProgrameDTO dto;
	
	public void setListener(VinamilkTableListener listener){
		this.listener = listener;
	}
	
	public PopupChooseDisplayProgramRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.layout_popup_display_program_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvCode = (TextView) view.findViewById(R.id.tvCode);
		tvName = (TextView) view.findViewById(R.id.tvName);
	}

	public void renderLayout(DisplayProgrameDTO dto){
		this.dto = dto;
		tvCode.setText(dto.displayProgrameCode);
		tvName.setText(dto.displayProgrameName);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == row && listener != null) {
			listener.handleVinamilkTableRowEvent(actionView, v, dto);
		}
	}
}
