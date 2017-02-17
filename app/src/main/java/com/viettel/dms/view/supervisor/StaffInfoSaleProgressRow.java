/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.SaleInMonthItemDto;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Staff Info Sale Progress Row
 * StaffInfoSaleProgressRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:29:58 14 Jan 2014
 */
public class StaffInfoSaleProgressRow extends DMSTableRow implements OnClickListener {
	
	TextView tvMonth;
	TextView tvTotalCus;
	TextView tvNoPSDS;
	TextView tvRevenue;

	public StaffInfoSaleProgressRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_sale_progress_row);
		setListener(lis);
		this.setOnClickListener(this);
		tvMonth = (TextView) this.findViewById(R.id.tvMonth);
		tvTotalCus = (TextView) this.findViewById(R.id.tvTotalCus);
		tvNoPSDS = (TextView) this.findViewById(R.id.tvNoPSDS);
		tvRevenue = (TextView) this.findViewById(R.id.tvSaleProg);
	}

	public void render(SaleInMonthItemDto item) {
		tvMonth.setText("Tháng " + item.month);
		tvTotalCus.setText("" + item.numCustomer);
		tvNoPSDS.setText("" + item.noPSDS);
		tvRevenue.setText(StringUtil.parseAmountMoney( item.revenue ));
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}

}
