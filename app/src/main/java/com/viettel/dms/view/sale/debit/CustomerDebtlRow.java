/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.debit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row du lieu cua danh sach cong no khach hang
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerDebtlRow extends TableRow implements OnClickListener {
	private VinamilkTableListener listener;
	private final View view;
	private TextView tvSTT;// STT
	private TextView tvCustomerCode;// ma KH
	private TextView tvCustomerName;// ten KH
	private TextView tvCustomerAdd;// dia chi KH
	private TextView tvDebt;// So no
	private TextView tvDayNumber;// So ngay
	private ImageView ivDetail;
	private CustomerDebtDTO item;

	public CustomerDebtlRow(Context con){
		super(con);
		LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_cus_debt_row, this);
	}
	
	public CustomerDebtlRow(Context context, VinamilkTableListener lis) {
		this(context);
		listener = lis;
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCustomerCode = (TextView) view.findViewById(R.id.tvCustomerCode);
		tvCustomerName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvCustomerAdd = (TextView) view.findViewById(R.id.tvCustomerAdd);
		tvDebt = (TextView) view.findViewById(R.id.tvDebt);
		tvDayNumber = (TextView) view.findViewById(R.id.tvDayNumber);
		ivDetail = (ImageView) view.findViewById(R.id.ivDetail);

		ivDetail.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == ivDetail) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GET_CUS_DEBIT_DETAIL, v, item);
		}
	}

	/**
	 * Render du lieu o moi dong
	 * 
	 * @author: BangHN
	 * @param pos
	 * @param
	 * @return: voidvoid
	 * @throws:
	 */
	public void renderLayout(int pos, CustomerDebtDTO dto) {
		item = dto;
		tvSTT.setText("" + pos);
		tvCustomerCode.setText(dto.customer.shortCode);
		tvCustomerName.setText(dto.customer.customerName);
		tvCustomerAdd.setText(dto.customer.address);
		tvDebt.setText(StringUtil.parseAmountMoney(dto.debitDetail.remain));
		tvDayNumber.setText("" + dto.remainDay);
	}
}
