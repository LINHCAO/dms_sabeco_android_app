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

import com.viettel.dms.dto.view.CusDebitDetailDTO.CusDebitDetailItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerDebitDetailRow extends TableRow implements OnClickListener {
	private CustomerDebitDetailView listener;
	private View view;
	public TextView sTT;
	public TextView orderCode;
	public TextView orderDate;
	public TextView totalDebt;
	public TextView paidAmount;
	public TextView remainingAmount;
	public TextView receipt;
	public TextView paidDate;
	public RelativeLayout llNote;
	public CheckBox cbNote;
	public TextView tvTextNull;
	public CusDebitDetailItem itemDto;

	public CustomerDebitDetailRow(Context context, CustomerDebitDetailView lis) {
		super(context);
		listener = lis;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_cus_debt_detail_row, this);
		sTT = (TextView) view.findViewById(R.id.tvSTT);
		orderCode = (TextView) view.findViewById(R.id.orderCode);
		orderDate = (TextView) view.findViewById(R.id.orderDate);
		totalDebt = (TextView) view.findViewById(R.id.totalDebt);
		paidAmount = (TextView) view.findViewById(R.id.paidAmount);
		remainingAmount = (TextView) view.findViewById(R.id.remainingAmount);
		receipt = (TextView) view.findViewById(R.id.receipt);
		paidDate = (TextView) view.findViewById(R.id.paidDate);
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
				listener.onEvent(ActionEventConstant.PLUS_PAY_DEBT, cbNote, itemDto);
			} else {
				itemDto.isCheck = false;
				listener.onEvent(ActionEventConstant.SUBTRACT_PAY_DEBT, cbNote, itemDto);
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
	public void renderLayout(int pos, CusDebitDetailItem item) {
		itemDto = item;
		sTT.setText("" + pos);
		orderCode.setText(item.orderNumber);
		orderDate.setText(item.orderDate);
		totalDebt.setText(StringUtil.parseAmountMoney("" + item.totalDebit));
		paidAmount.setText(StringUtil.parseAmountMoney("" + item.paidAmount));
		remainingAmount.setText(StringUtil.parseAmountMoney("" + item.remainingAmount));
		receipt.setText(item.receipt);
		paidDate.setText(item.paidDate);
		if (item.remainingAmount <= 0) {
			cbNote.setVisibility(View.GONE);
		} else {
			cbNote.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	public void renderNullLayout() {
		sTT.setVisibility(View.GONE);
		orderCode.setVisibility(View.GONE);
		orderDate.setVisibility(View.GONE);
		totalDebt.setVisibility(View.GONE);
		paidAmount.setVisibility(View.GONE);
		llNote.setVisibility(View.GONE);
		remainingAmount.setVisibility(View.GONE);
		receipt.setVisibility(View.GONE);
		paidDate.setVisibility(View.GONE);
		tvTextNull.setVisibility(View.VISIBLE);
		tvTextNull.setText(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
	}
}
