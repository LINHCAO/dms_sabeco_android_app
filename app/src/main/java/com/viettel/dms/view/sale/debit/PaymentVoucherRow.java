/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.debit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.sale.debit.PaymentVoucherDTO.PaymentVoucherItem;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class PaymentVoucherRow extends TableRow implements OnClickListener {
	private PaymentVoucherView listener;
	private View view;
	private TextView sTT;
	private TextView tvOrderCode;
	private TextView tvOrderDate;
	private TextView tvOrderReturnedDate;
	private TextView tvTotalReturnAmount;
	private TextView tvCusPaidAmount;
	private TextView tvReturnedAmount;
	private TextView tvReturnDate;
	public RelativeLayout llNote;
	public CheckBox cbNote;
	public TextView tvTextNull;
	public PaymentVoucherItem itemDto;

	public PaymentVoucherRow(Context context, PaymentVoucherView lis) {
		super(context);
		listener = lis;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_payment_voucher_row, this);
		sTT = (TextView) view.findViewById(R.id.tvSTT);
		tvOrderCode = (TextView) view.findViewById(R.id.orderCode);
		tvOrderDate = (TextView) view.findViewById(R.id.orderDate);
		tvOrderReturnedDate = (TextView) view.findViewById(R.id.orderReturnedDate);
		tvTotalReturnAmount = (TextView) view.findViewById(R.id.totalReturnAmount);
		tvCusPaidAmount = (TextView) view.findViewById(R.id.cusPaidAmount);
		tvReturnedAmount = (TextView) view.findViewById(R.id.returnedAmount);
		tvReturnDate = (TextView) view.findViewById(R.id.returnDate);
		llNote = (RelativeLayout) view.findViewById(R.id.llNote);
		cbNote = (CheckBox) view.findViewById(R.id.cbNote);
		cbNote.setOnClickListener(this);
		tvTextNull = (TextView) view.findViewById(R.id.tvTextNull);
	}

	@Override
	public void onClick(View v) {
		if (v == cbNote) {
			if (cbNote.isChecked()) {
				itemDto.isCheck = true;
				listener.onEvent(ActionEventConstant.PLUS_VOUCHER_OUTPUT, cbNote, itemDto);
			} else {
				itemDto.isCheck = false;
				listener.onEvent(ActionEventConstant.SUBTRACT_VOUCHER_OUTPUT, cbNote, itemDto);
			}
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param pos
	 * @param cusDebDetailItem
	 * @return: voidvoid
	 * @throws:
	 */
	public void renderLayout(int pos, PaymentVoucherItem item) {
		itemDto = item;
		sTT.setText("" + pos);
		tvOrderCode.setText(item.orderNumber);
		tvOrderDate.setText(item.orderDate);
		tvOrderReturnedDate.setText(item.orderReturnDate);
		tvTotalReturnAmount.setText(StringUtil.parseAmountMoney("" + item.totalReturnAmount));
		tvCusPaidAmount.setText(StringUtil.parseAmountMoney("" + item.cusPaidAmount));
		tvReturnedAmount.setText(StringUtil.parseAmountMoney("" + item.returnedAmount));
		tvReturnDate.setText(item.returnDate);
		if (item.totalReturnAmount - item.returnedAmount <= 0) {
			cbNote.setVisibility(View.GONE);
		} else {
			cbNote.setVisibility(View.VISIBLE);
		}
	}
}
