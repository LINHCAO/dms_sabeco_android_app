/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.view.control.VinamilkTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Row them khach hang vao chuong trinh ho tro ban hang
 * 
 * @author: hoanpd1
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerAddSupportSalesRow extends VinamilkTableRow implements
		OnClickListener {
	Context context;
	// so thu tu
	TextView tvSTT;
	// Khach hang
	TextView tvCustomer;
	// Dia chi
	TextView tvCusAdd;
	// Checkbox chon khach hang
	CheckBox cbChoose;
	// Checkbox chon khach hang
	LinearLayout llChoose;
	// row dto
	CustomerAttendProgramListItem dtoItem;
	// listener when row have action
	protected VinamilkTableListener listener;

	private CustomerAddSupportSalesRow(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * khoi tao control Mo ta chuc nang cua ham
	 * 
	 * @author: HoanPD1
	 * @param context
	 */
	public CustomerAddSupportSalesRow(Context context,
			VinamilkTableListener listener) {
		this(context);
		this.listener = listener;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.layout_customer_add_support_sales_row, this);
		setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCustomer = (TextView) view.findViewById(R.id.tvCustomer);
		tvCustomer.setOnClickListener(this);
		tvCusAdd = (TextView) view.findViewById(R.id.tvAddress);
		llChoose = (LinearLayout) view.findViewById(R.id.llChoose);
		cbChoose = (CheckBox) view.findViewById(R.id.cbChoose);
		cbChoose.setOnClickListener(this);
	}

	/**
	 * 
	 * Render row
	 * 
	 * @author: HoanPD1
	 * @param position
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int position, CustomerAttendProgramListItem item) {
		this.dtoItem = item;
		tvSTT.setText("" + position);
		tvCustomer.setText(item.customerCode + " - " + item.customerName);
		tvCusAdd.setText(item.address1);
		cbChoose.setChecked(dtoItem.isCheck); 
		//tvCustomer.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));

	}

	/**
	 * 
	 * Chon row
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @param : v
	 * @throws:
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if ((v == cbChoose) && listener != null) {
			dtoItem.isCheck = true;
			listener.handleVinamilkTableRowEvent(ActionEventConstant.ACTION_CHOOSE_CUSTOMER, v, dtoItem);
		}
	}
}
