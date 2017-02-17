/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 *  Row man hinh :Hien thi dialog danh sach khach hang cua man hinh 07- 01. Them Yeu Cau
 *  @author: HoanPD1
 *  @version: 1.0
 *  @since: 1.0
 */
public class TBHVCustomerListRow extends TableRow implements OnClickListener{
	public static final int ACTION_VIEW_CUSTOMER = 0;
	
	Context context;
	View view;
	// row 
	//private TableRow row;
	//so thu tu
	TextView tvSTT;
//	// ma khach hang
//	TextView tvCusCode;
	//ten khach hang
	TextView tvCusName;
	//Dia chi
	TextView tvCusAdd;
	//row dto
	CustomerDTO dto;
	// listener when row have action
	protected VinamilkTableListener listener;
	
	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener _listener) {
		this.listener = _listener;
	}

	/** khoi tao control
	*  Mo ta chuc nang cua ham
	*  @author: HoanPD1
	 * @param context
	 */
	public TBHVCustomerListRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_customer_list_row, this);
//		row = (TableRow) view.findViewById(R.id.row);
//		row.setOnClickListener(this);
		setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvCusName = (TextView) view.findViewById(R.id.tvCustomerName);
		tvCusAdd = (TextView) view.findViewById(R.id.tvAddress);
	}

	/**
	 * 
	*  Render row
	*  @author: HoanPD1
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, CustomerDTO item){
		this.dto = item;
		tvSTT.setText("" + position);
		if (!StringUtil.isNullOrEmpty(item.customerCode)) {
			tvCusName.setText(item.customerCode.substring(0, 3) + " - " + item.customerName);
		}else{
			tvCusName.setText(item.customerName);
		}
		tvCusAdd.setText(item.address);
		
	}
	
	/**
	 * 
	*  Chon row
	*  @author: HoanPD1
	*  @return: void
	*  @param : v
	*  @throws:
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if( v == this && listener != null){
			listener.handleVinamilkTableRowEvent(ACTION_VIEW_CUSTOMER, v, dto);
		}else if (v != this && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
		}
	
	}

	/**
	*  Cap nhat color cho row duoc chon
	*  @author: HoanPD1
	*  @return: void
	*  @throws: 
	*/
	public void updateSelectedColor(int color) {
		// TODO Auto-generated method stub
		tvSTT.setBackgroundColor(color);
		tvCusName.setBackgroundColor(color);
		tvCusAdd.setBackgroundColor(color);
	}
}
