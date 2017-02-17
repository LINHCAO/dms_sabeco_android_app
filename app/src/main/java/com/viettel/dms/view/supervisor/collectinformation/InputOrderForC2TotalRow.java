/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.collectinformation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.C2SaleOrderDetailViewDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Row tong trong popup dat don mua & ban cua C2 
 * InputOrderForC2TotalRow.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:39:19 12-12-2013
 */
public class InputOrderForC2TotalRow extends RelativeLayout implements OnClickListener{
	public static final int ACTION_VIEW_CUSTOMER = 0;
	
	Context context;
	View view;
	// row 
	TextView tvTotalQuantity;
	//row dto
	C2SaleOrderDetailViewDTO dto;
	// listener when row have action
	protected VinamilkTableListener listener;
	
	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener _listener) {
		this.listener = _listener;
	}

	public InputOrderForC2TotalRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_input_order_for_c2_total_row, this);
		setOnClickListener(this);
		tvTotalQuantity = (TextView) view.findViewById(R.id.tvTotalQuantity);
	}

	/**
	 * Render layout
	 * @author: dungnt19
	 * @since: 10:39:43 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param total
	 */
	public void renderLayout(long total){
		tvTotalQuantity.setText("" + total);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( v == this && listener != null){
//			listener.handleVinamilkTableRowEvent(ACTION_VIEW_CUSTOMER, v, dto);
		}else if (v != this && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
		}
	
	}
}
