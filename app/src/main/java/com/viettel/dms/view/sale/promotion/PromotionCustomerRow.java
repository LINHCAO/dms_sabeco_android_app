/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.SaleOrderViewDTO;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Promotion Customer Row
 * PromotionCustomerRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:19:41 14 Jan 2014
 */
public class PromotionCustomerRow extends TableRow {

	Context _context;
	View view;
	TextView tvSTT;
	TextView tvCode;
	TextView tvName;
	TextView tvLevel;
	TextView tvQuantity;
	TextView tvNone;

	// listener
	protected OnEventControlListener listener;
	private TableRow row;

	public PromotionCustomerRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_customer_row, this);
		row = (TableRow)view.findViewById(R.id.row);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCode = (TextView) view.findViewById(R.id.tvCode);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvLevel = (TextView) view.findViewById(R.id.tvLevel);
		tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
		tvNone = (TextView) view.findViewById(R.id.tvNone);
	}

	public int[] getWidthColumns(){
	
		final int size = row.getChildCount();
		final int[] widths = new int[size];
		
		ViewTreeObserver vto = this.getViewTreeObserver();
		vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				for (int i = 0; i < size; i++) {
					widths[i] = row.getChildAt(i).getWidth();		
				}
				getViewTreeObserver()
						.removeGlobalOnLayoutListener(this);
			}
		});
		return widths;
		
	}
	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	public void renderLayout(int position, SaleOrderViewDTO item) {
//		tvSTT.setText("" + position);
//		tvCode.setText(item.getORDER_DATE());
//		tvName.setText(item.getCUSTOMER_CODE());
//		tvDate.setText(item.getCUSTOMER_NAME());
//		tvPromotionTypeCode.setText(item.getORDER_NUMBER());
//		tvPromotionType.setText(item.getORDER_NUMBER());
		tvSTT.setText("1");
		tvCode.setText("A01");
		tvName.setText("Luan Dang Van");
		tvLevel.setText("1");
		tvQuantity.setText("5.000.000");
		tvNone.setText("None");
	}

	public View getView() {
		return view;
	}
}
