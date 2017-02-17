/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.depot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TotalStatisticsItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Total Statistics Row 
 * TotalStatisticsRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:11:43 14 Jan 2014
 */
public class TotalStatisticsRow extends TableRow {
	TextView tvNumber;
	TextView tvProductCode;
	TextView tvProductName;
	TextView tvPromotion;
	TextView tvQuantity;
	TextView tvRemain;
	TextView tvSoldOut;
	private View view;

	public TotalStatisticsRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_total_statistics_row, this);
		tvNumber = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvPromotion = (TextView) view.findViewById(R.id.tvPromotion);
		tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
		tvRemain = (TextView) view.findViewById(R.id.tvRemain);
		tvSoldOut = (TextView) view.findViewById(R.id.tvSoldOut);
	}

	public void render(int pos, TotalStatisticsItem dto) {
		tvNumber.setText("" + pos);
		tvProductCode.setText(dto.productCode);
		tvProductName.setText(dto.productName);
		tvPromotion.setText(dto.productPromotion);
		tvQuantity.setText(dto.quantity);
		tvRemain.setText(dto.available_quantity);
		
		if(!StringUtil.isNullOrEmpty(dto.available_quantity) && !StringUtil.isNullOrEmpty(dto.quantity)){
			int soldOut = Integer.parseInt(dto.quantity) - Integer.parseInt(dto.available_quantity);
			tvSoldOut.setText("" + soldOut);
		}else{
			tvSoldOut.setText("0");
		}
	}

}
