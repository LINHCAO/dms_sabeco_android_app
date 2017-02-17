/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerCanLoseItemDto;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Staff Info Cus Can Lose
 * StaffInfoCusCanLose.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:29:19 14 Jan 2014
 */
public class StaffInfoCusCanLose extends DMSTableRow implements OnClickListener{

	public TextView tvCusCode;
	public TextView tvCusName;
	public TextView tvCusAdd;
	public TextView tvLastOrder;
	public TextView tvNum;

	public StaffInfoCusCanLose(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_can_lose_cus);
		setListener(lis);
		this.setOnClickListener(this);
		tvNum = (TextView) this.findViewById(R.id.tvSTT);
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		tvCusAdd = (TextView) this.findViewById(R.id.tvCusAdd);
		tvLastOrder = (TextView) this.findViewById(R.id.tvLastOrder);
	}
	
	public void render(int pos, CustomerCanLoseItemDto item) {
		tvNum.setText("" + pos);
		tvCusCode.setText(item.cusCode.substring(0, 3));
		tvCusName.setText(item.cusName);
		tvCusAdd.setText(item.cusAdd);
		tvLastOrder.setText(item.cusLastOrder);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
