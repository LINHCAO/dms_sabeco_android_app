/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ProgrameForProductDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * hien thi thong tin mot chuong trinh di kem voi san pham
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class SelectProgrameForProduct extends TableRow implements
		OnClickListener {
	Context _context;
	View view;
	// STT
	TextView tvSTT;
	// code
	TextView tvProgrameCode;
	// Name
	TextView tvProgrameName;

	// programe type
	TextView tvProgrameType;

	// text view notify end row
	TextView tvNotifyNull;

	// listener
	protected VinamilkTableListener listener;
	// table parent
	private View tableParent;
	private TableRow row;
	// data to render layout for row
	ProgrameForProductDTO myData;

	/**
	 * constructor for class
	 * 
	 * @param context
	 * @param aRow
	 */
	public SelectProgrameForProduct(Context context, View aRow) {
		super(context);
		tableParent = aRow;
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi
				.inflate(R.layout.layout_select_programe_for_product_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvProgrameCode = (TextView) view.findViewById(R.id.tvProgrameCode);
		tvProgrameName = (TextView) view.findViewById(R.id.tvProgrameName);
		tvProgrameType = (TextView) view.findViewById(R.id.tvProgrameType);
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);

		myData = new ProgrameForProductDTO();
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	 * init layout for row
	 * 
	 * @author: HaiTC3
	 * @param position
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int position, ProgrameForProductDTO item) {
		this.myData = item;
		tvNotifyNull.setVisibility(View.GONE);
		tvSTT.setText(String.valueOf(position));
		tvProgrameCode.setText(item.programeCode);
		tvProgrameName.setText(item.programeName);
		tvProgrameType.setText(item.programeTypeName);
		if (StringUtil.isNullOrEmpty(item.programeTypeName)) {
			if (item.programeType == ProgrameForProductDTO.PROGRAME_DISPLAY) {
				tvProgrameType.setText(StringUtil
						.getString(R.string.TEXT_HEADER_MENU_CTTB));
			} else {
				tvProgrameType.setText(StringUtil
						.getString(R.string.TEXT_NAME_PROMOTION_ZM));
			}
		}
	}

	/**
	 * 
	 * hien thi dong cuoi cung cua bang neu co du lieu, hoac hien thi thong bao
	 * null neu khong co du lieu nao
	 * 
	 * @author: HaiTC3
	 * @param message
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutEndRowNotSelectProgrameOrNull(String message) {
		this.myData = null;
		tvSTT.setVisibility(View.GONE);
		tvProgrameCode.setVisibility(View.GONE);
		tvProgrameName.setVisibility(View.GONE);
		tvProgrameType.setVisibility(View.GONE);
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setText(message);
	}

	public View getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == row && listener != null) {
			listener.handleVinamilkTableRowEvent(
					ActionEventConstant.ACTION_SELECT_PROGRAME, tableParent,
					this.myData);
		}
	}
}
