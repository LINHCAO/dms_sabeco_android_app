/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.collectinformation;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Row ds KH cua C2
 * CustomerOfC2ListRow.java
 * @author: dungnt19
 * @version: 1.0 
 * @since:  10:42:27 12-12-2013
 */
public class CustomerOfC2ListRow extends DMSTableRow implements OnClickListener {
	TextView tvNum;
	public TextView tvCusCode;
	TextView tvCusName;
	TextView tvAddress;
	public ImageView ivVisitCus;
	CustomerListItem item;
	
	public CustomerOfC2ListRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_customer_of_c2_list_row);
		setListener(lis);
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		tvAddress = (TextView) this.findViewById(R.id.tvAdd);
		ivVisitCus = (ImageView) this.findViewById(R.id.visitCus);
		this.setOnClickListener(this);
		tvCusCode.setOnClickListener(this);
		ivVisitCus.setOnClickListener(this);
	}

	/**
	 * Render layout
	 * @author: dungnt19
	 * @since: 10:42:52 12-12-2013
	 * @return: void
	 * @throws:  
	 * @param pos
	 * @param item
	 */
	public void render(int pos, CustomerListItem item) {
		this.item = item;
		tvNum.setText("" + pos);
		if(StringUtil.isNullOrEmpty(item.aCustomer.getCustomerCode())){
			tvCusCode.setText(Constants.STR_BLANK);
		}else{
			tvCusCode.setText(item.aCustomer.getCustomerCode());
		}
		tvCusName.setText(item.aCustomer.getCustomerName());
		tvAddress.setText(item.aCustomer.getStreet());

		if (item.isTodayOrdered) {
			ivVisitCus.setImageResource(R.drawable.icon_check);
		} else {
			ivVisitCus.setImageResource(R.drawable.icon_door);
		}
	}


	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
		if (v == tvCusCode) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_CUSTOMER_INFO, tvCusCode, item);
		} else if (v == ivVisitCus) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.VISIT_CUSTOMER, ivVisitCus, item);
		}
	}
}
