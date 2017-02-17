/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ShowRoomGoodsDto;
import com.viettel.sabeco.R;

/**
 * Show Room Goods Row
 * ShowRoomGoodsRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:20:18 14 Jan 2014
 */
public class ShowRoomGoodsRow extends TableRow {

	public View view;
	public TextView tvNumber;
	public TextView tvGoodsCode;
	public TextView tvGoodsName;
	public CheckBox cbPassed;
	public CheckBox cbFailed;

	public ShowRoomGoodsRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_showroom_goods_row, this);
		tvNumber = (TextView) view.findViewById(R.id.tvSTT);
		tvGoodsCode = (TextView) view.findViewById(R.id.tvGoodsCode);
		tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
		cbPassed = (CheckBox) view.findViewById(R.id.cbPass);
		cbFailed = (CheckBox) view.findViewById(R.id.cbFailed);
	}
	
	public void render(int pos, ShowRoomGoodsDto showGoodsDto) {
		tvNumber.setText("" + pos);
		tvGoodsCode.setText(showGoodsDto.goodsCode);
		tvGoodsName.setText(showGoodsDto.goodsName);
	}

}
