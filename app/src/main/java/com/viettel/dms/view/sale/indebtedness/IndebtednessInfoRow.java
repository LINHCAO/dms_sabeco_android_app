/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.indebtedness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * 
 *  row indebtedness info
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class IndebtednessInfoRow extends TableRow{
	Context _context;
	View view;
	// STT info
	TextView tvSTT;
	// product code
	TextView tvCustomerCode;
	// product name
	TextView tvCustomerName;
	// product price
	TextView tvBalance;
	// specification of product
	TextView tvStatus;
	// product in stock
	TextView tvTime;
	// total product 
	ImageButton imbtDetail;

	// listener
	protected OnEventControlListener listener;

	/**
	 * init row
	 * @param context
	 * @param aRow
	 */
	public IndebtednessInfoRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_indebtedness_info_row, this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvBalance = (TextView) view.findViewById(R.id.tvBalance);
		tvStatus = (TextView) view.findViewById(R.id.tvStatus);
		tvTime = (TextView) view.findViewById(R.id.tvTime);
		imbtDetail = (ImageButton) view.findViewById(R.id.imbtDetail);
		imbtDetail.setOnClickListener((OnClickListener) listener);
	}

	/**
	 * 
	*  set listener
	*  @author: HaiTC3
	*  @param listener
	*  @return: void
	*  @throws:
	 */
	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	*  rander layout for indebtedness info row
	*  @author: HaiTC3
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, ProductDTO item) {
		tvSTT.setText("" + position);
		tvCustomerCode.setText("Ma khach hang");
		tvCustomerName.setText("ten khach hang");
		tvBalance.setText("Du no");
		tvStatus.setText("trang thai");
		tvTime.setText("thoi gian");
	}

	public View getView() {
		return view;
	}
}
