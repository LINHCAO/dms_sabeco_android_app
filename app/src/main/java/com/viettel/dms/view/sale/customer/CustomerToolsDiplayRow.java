/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.sabeco.R;

/**
 * Dong thong tin cong cu trung bay trong chi tiet khach hang
 * @author : BangHN
 * since : 2:15:44 PM
 * version :
 */
public class CustomerToolsDiplayRow extends TableRow {
	private final Context context;
	private final View view;
	TextView tvNum;//40
	TextView tvToolCode;//ma cong cu 250
	TextView tvToolName;//ten cong cu 350
	TextView tvTarget;//chi tieu 200
	TextView tvFact;//da dat 200

	public CustomerToolsDiplayRow(Context con) {
		super(con);
		context = con;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_customer_tools_display_row, this);
		tvNum = (TextView) view.findViewById(R.id.tvNum);
		tvToolCode = (TextView) view.findViewById(R.id.tvToolCode);
		tvToolName = (TextView) view.findViewById(R.id.tvToolName);
		tvTarget = (TextView) view.findViewById(R.id.tvTarget);
		tvFact = (TextView) view.findViewById(R.id.tvFact);
		
	}

}
