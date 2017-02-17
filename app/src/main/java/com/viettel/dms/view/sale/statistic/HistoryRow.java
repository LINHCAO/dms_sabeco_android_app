/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.HistoryItemDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * History Row
 * HistoryRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:21:08 14 Jan 2014
 */
public class HistoryRow extends TableRow implements OnClickListener{	
	private final View view;
	
	private TextView tvStt;
	private TextView tvSKU;
	private TextView tvDoanhSo;		
	
	public HistoryRow(Context context) {
		super(context);		
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_history_row, this);
		setOnClickListener(this);
		tvStt = (TextView) view.findViewById(R.id.tvStt);
		tvSKU = (TextView) view.findViewById(R.id.tvSKU);
		tvDoanhSo = (TextView) view.findViewById(R.id.tvDoanhSo);
	}
	
	public void render(HistoryItemDTO historyItem) {		
		tvStt.setText(historyItem.STT);
		tvSKU.setText(historyItem.sku);
		tvDoanhSo.setText(StringUtil.parseAmountMoney(historyItem.doanhSo));				
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
