/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerAttendProgramListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Customer list row
 * CustomerListRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:08:47 14 Jan 2014
 */
public class CustomerAttendProgramListRow extends DMSTableRow implements OnClickListener {
//	private final Context context;
	// view
//	View view;
	//so thu tu
	TextView tvNum;
	// Ma khach hang
	public TextView tvCusCode;
	// Ten khach hang
	TextView tvCusName;
	//dia chi
	TextView tvAddress;
	// muc chuong trinh ma khach hang tham gia
	TextView tvCustomerLevel;
	// ngay tham gia chuong trinh
	TextView tvAttenDate;
	// trang thai
	TextView tvStatus;
	// icon xoa khach hang
	public ImageView ivDelete;
	// dto item
	CustomerAttendProgramListItem item;
	// thong bao khong có du lieu
	TextView tvNoResultInfo;
	//Loai chuong trinh: 1: HT01, 2: HT02, 3: HT03, 4: HT04, 5: HT05
	int typePrograme;
	//chuong trinh co muc hay khong
	@SuppressWarnings("unused")
	private boolean hasLevel;

	
	/**
	 * 
	 * @param context
	 * @param lis
	 * @param type : Loai chuong trinh: 1: HT01, 2: HT02, 3: HT03, 4: HT04, 5: HT05
	 * @param hasLevel
	 */
	public CustomerAttendProgramListRow(Context context, VinamilkTableListener lis, int type, boolean hasLevel) {
		super(context, R.layout.layout_customer_attend_program_with_level_list_row);
		setListener(lis);
		typePrograme = type;
		this.hasLevel = hasLevel;
		// STT
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		// Ma khach hang
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		
		// Chuong trinh HT03
		if(typePrograme == 3){
			tvCusCode.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME)); 
			tvCusCode.setOnClickListener(this);
		}
		
		// Ten khach hang
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		// Dia chi
		tvAddress = (TextView) this.findViewById(R.id.tvAddress);
		// Muc tham gia
		tvCustomerLevel = (TextView) this.findViewById(R.id.tvCustomerLevel);
		// Ngay tham gia
		tvAttenDate = (TextView) this.findViewById(R.id.tvAttendDate);
		// Trang thai
		tvStatus = (TextView) this.findViewById(R.id.tvStatus);
		// Xoa
		ivDelete = (ImageView) this.findViewById(R.id.ivDelete);
		ivDelete.setOnClickListener(this);

//		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
//		setOnClickListener(this);
	}
	
	public CustomerAttendProgramListRow(Context context, VinamilkTableListener lis, int type) {
		super(context, R.layout.layout_customer_attend_program_list_row);
		setListener(lis);
		typePrograme = type;
		hasLevel = false;
		// STT
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		// Ma khach hang
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		
		// Chuong trinh HT03
		if(typePrograme == 3){
			tvCusCode.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME)); 
			tvCusCode.setOnClickListener(this);
		}
		
		// Ten khach hang
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		// Dia chi
		tvAddress = (TextView) this.findViewById(R.id.tvAddress);
		// Muc tham gia
		tvCustomerLevel = (TextView) this.findViewById(R.id.tvCustomerLevel);
		// Ngay tham gia
		tvAttenDate = (TextView) this.findViewById(R.id.tvAttendDate);
		// Trang thai
		tvStatus = (TextView) this.findViewById(R.id.tvStatus);
		// Xoa
		ivDelete = (ImageView) this.findViewById(R.id.ivDelete);
		ivDelete.setOnClickListener(this);

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
	public void render(int pos, CustomerAttendProgramListItem item, boolean hasLevel) {
		this.item = item;
		tvNum.setText(String.valueOf(pos)); 
		
		tvCusCode.setText(item.customerCode); 
		tvCusName.setText(item.customerName); 
		tvAddress.setText(item.address);
		if(hasLevel) {
			tvCustomerLevel.setText(item.levelName);
		}
		tvAttenDate.setText(item.attendDate);
		tvStatus.setText(item.statusName);
		 
		//cho duyet thi cho phep xoa
		if(item.status == 0) {
			ivDelete.setVisibility(View.VISIBLE);
		} else {
			ivDelete.setVisibility(View.INVISIBLE);
		}
		
		// Neu chuong trinh la HT03 va khong co so luong dang ki tham gia
		// thi ten KH to do
		if(typePrograme == 3 && !item.isHaveJoin){
			tvCusName.setTextColor(Color.RED);
		}
	} 

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
		// Neu la chuong trinh HT03 thi cho add san luong dang ki tham gia
		if (v == tvCusCode && typePrograme == 3) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.ADD_JOIN_QUANTITY, tvCusCode, item);
		} else if (v == ivDelete) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.DELETE_CUSTOMER, ivDelete, item);
		}
	}
}
