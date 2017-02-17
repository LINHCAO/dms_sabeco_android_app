/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.dto.db.CustomerInputQuantityDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
 
/**
 * Customer Program Done Row
 * CustomerProgramDoneRow.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  13:59:52 16-06-2014
 */
public class CustomerProgramDoneRow extends DMSTableRow implements OnClickListener {
//	private final Context context;
//	View view;
	public TextView tvNum;
	public TextView tvCusCode;
	public TextView tvCusName;
	public TextView tvAddress;
	public TextView tvCustomerLevel; 
	public ImageView ivInput;
	public ImageView ivEdit;
//	public VinamilkTableListener listener;
	CustomerInputQuantityDTO item;
//	TextView tvNoResultInfo;  

//	public CustomerProgramDoneRow(Context con){
//		super(con);
//		context=con;
//	}
	 
	/**
	 * Ham khoi tao
	 * @param context
	 * @param lis
	 * @param type
	 * @param hasLevel : chuong trinh co muc khay khong
	 */
	public CustomerProgramDoneRow(Context context, CustomerProgrameDoneView lis, boolean hasLevel) {
		super(context, R.layout.layout_customer_program_done_with_level_row);
		setListener(lis);
		// STT
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		// Ma khach hang
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		  
		// Ten khach hang
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		// Dia chi
		tvAddress = (TextView) this.findViewById(R.id.tvAddress);
		// Muc tham gia
		tvCustomerLevel = (TextView) this.findViewById(R.id.tvCustomerLevel); 
		// Icon Nhap moi
		ivInput = (ImageView) this.findViewById(R.id.ivInput);
		ivInput.setOnClickListener(this); 
		// Icon Sua
		ivEdit = (ImageView) this.findViewById(R.id.ivEdit);
		ivEdit.setOnClickListener(this);

//		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
//		setOnClickListener(this); 
	}
	
	/**
	 * Ham khoi tao
	 * @param context
	 * @param lis
	 * @param type
	 * @param hasLevel : chuong trinh co muc khay khong
	 */
	public CustomerProgramDoneRow(Context context, CustomerProgrameDoneView lis) {
		super(context, R.layout.layout_customer_program_done_row);
		setListener(lis);
		// STT
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		// Ma khach hang
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		  
		// Ten khach hang
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		// Dia chi
		tvAddress = (TextView) this.findViewById(R.id.tvAddress);
		// Muc tham gia
		tvCustomerLevel = (TextView) this.findViewById(R.id.tvCustomerLevel); 
		// Icon Nhap moi
		ivInput = (ImageView) this.findViewById(R.id.ivInput);
		ivInput.setOnClickListener(this); 
		// Icon Sua
		ivEdit = (ImageView) this.findViewById(R.id.ivEdit);
		ivEdit.setOnClickListener(this);

//		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
//		setOnClickListener(this); 
	}

	/**
	 * Render 1 dong du lieu
	 * @author: quangvt1
	 * @since: 13:09:03 16-05-2014
	 * @return: void
	 * @throws:  
	 * @param pos
	 * @param item
	 */
	public void render(int pos, CustomerInputQuantityDTO item) {
		this.item = item;
		tvNum.setText(String.valueOf(pos));  
		tvCusCode.setText(item.CUSTOMER_CODE); 
		tvCusName.setText(item.CUSTOMER_NAME); 
		tvAddress.setText(item.CUSTOMER_ADDRESS); 
		tvCustomerLevel.setText(item.LEVEL_NAME); 
		
		if(item.ALLOW_EDIT){  
			if (item.hasInput) {
				ivInput.setVisibility(View.GONE);
				ivEdit.setVisibility(View.VISIBLE);
			} else {
				ivInput.setVisibility(View.VISIBLE);
				ivEdit.setVisibility(View.GONE);
			}
		}else{
			if (item.hasInput) {
				ivInput.setVisibility(View.GONE);
				ivEdit.setVisibility(View.VISIBLE);
			}else{
				ivInput.setVisibility(View.GONE);
				ivEdit.setVisibility(View.GONE);
			} 
		}
	} 
 
	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}else if (v == ivEdit) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.INPUT_QUANTITY_DONE, ivEdit, item);
		}else if (v == ivInput) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.INPUT_QUANTITY_DONE, ivInput, item);
		}
	}
}
