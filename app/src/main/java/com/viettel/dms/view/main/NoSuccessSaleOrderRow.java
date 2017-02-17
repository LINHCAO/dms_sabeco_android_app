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

import com.viettel.dms.dto.view.NoSuccessSaleOrderDto.NoSuccessSaleOrderItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 *  Row trong popup dong hang loi
 *  @author: HieuNH
 *  @version: 1.0
 *  @since: 1.0
 */
public class NoSuccessSaleOrderRow extends TableRow implements OnClickListener {
	private View view;
	private TextView tvSTT;
	private TextView tvOrderNum;
	private TextView tvCusCode;
	private TextView tvTenKH;
	private TextView tvTotal;
	private TextView tvDescription;

	public NoSuccessSaleOrderRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_no_success_order_list_row, this);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);

		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvOrderNum = (TextView) view.findViewById(R.id.tvOrderNum);
		tvCusCode = (TextView) view.findViewById(R.id.tvCusCode);
		tvTenKH = (TextView) view.findViewById(R.id.tvTenKH);
		tvTotal = (TextView) view.findViewById(R.id.tvTotal);
		tvDescription = (TextView) view.findViewById(R.id.tvDescription);
	}

	public void renderLayout(int pos, NoSuccessSaleOrderItem item) {
		tvSTT.setText("" + pos);
		tvOrderNum.setText(item.saleOrder.orderNumber);
		tvCusCode.setText(item.customer.customerCode);
		tvTenKH.setText(item.customer.customerName);
		tvTotal.setText(StringUtil.parseAmountMoney(item.saleOrder.total));
		tvDescription.setText(item.saleOrder.description);
	}

	@Override
	public void onClick(View v) {

	}

}
