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

import com.viettel.dms.dto.db.ShopDTO;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;
/**
 *  Row mo ta mot dong du lieu Shop khi dang nhap giam sat.
 *  @author: BangHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class ShopManagedRow extends TableRow implements OnClickListener {
	private int actionSelectedShop = -1;
	//private Context context;
	private View view;
	private TextView tvNPP;
	private TextView tvAddress;
	TableRow row;
	ShopDTO itemRow;
	//su kien click row
	private OnEventControlListener listener;
	
	public ShopManagedRow(Context context, int actionSelectedShop) {
		super(context);
		this.actionSelectedShop = actionSelectedShop;
		listener = (OnEventControlListener) context;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_shop_manage_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvNPP = (TextView) view.findViewById(R.id.tvNPP);
		tvAddress = (TextView) view.findViewById(R.id.tvAddress);
	}

	public void renderLayout(int pos, ShopDTO item) {
		itemRow = item;
		tvNPP.setText(item.shopCode + " - " + item.shopName);
		tvAddress.setText(item.street);
	}

	@Override
	public void onClick(View v) {
		if(v == row && listener != null && itemRow != null){
			listener.onEvent(actionSelectedShop, null, itemRow);
		}
	}

}
