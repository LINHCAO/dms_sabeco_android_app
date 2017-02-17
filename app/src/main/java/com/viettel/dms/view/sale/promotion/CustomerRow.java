/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerAttentProgrameDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Customer Row
 * CustomerRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:18:12 14 Jan 2014
 */
public class CustomerRow extends TableRow implements OnClickListener{

	Context _context;
	View view;
	//so thu tu
	TextView tvSTT;
	//ma khach hang
	TextView tvCode;
	//ten khach hang
	TextView tvName;
	//muc tham gia
	TextView tvLevel;
	//doanh so con lai
	TextView tvSalesRemain;
//	TextView tvNone;

	// listener
	protected OnEventControlListener listener;
	private TableRow row;

	public CustomerRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_customer_row, this);
		row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCode = (TextView) view.findViewById(R.id.tvCode);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvLevel = (TextView) view.findViewById(R.id.tvLevel);
		tvSalesRemain = (TextView) view.findViewById(R.id.tvSalesRemain);
//		tvNone = (TextView) view.findViewById(R.id.tvNone);
	}
	/**
	 * 
	*  lay chieu rong cua row
	*  @author: ThanhNN8
	*  @return
	*  @return: int[]
	*  @throws:
	 */
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
	/**
	 * 
	*  render du lieu cho row
	*  @author: ThanhNN8
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, CustomerAttentProgrameDTO item) {
		tvSTT.setText("" + position);
		String customerCode = item.getCustomerCode();
		if (customerCode.length() > 3) {
			customerCode = customerCode.substring(0, 3);
		}
		tvCode.setText(customerCode);
		tvName.setText(item.getCustomerName());
		tvLevel.setText(item.getCustomerLevel());
		String moneySalesRemain = StringUtil.parseAmountMoney(item.getCustomerSalesRemain());
		tvSalesRemain.setText(moneySalesRemain);
	}

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == row && _context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)_context);
		}
	}
}
