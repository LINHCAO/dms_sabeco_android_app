/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.image;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.ImageListItemDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Hien thi 1 dong trong man hinh danh sach hinh anh KH - Giam sat ban hang
 * 
 * SupervisorImageListRow.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  2:32:04 PM Dec 12, 2013
 */
public class SupervisorImageListRow extends DMSTableRow implements OnClickListener { 
	/*----------------DEFAULT----------------*/
	
	/*----------------CONSTANT----------------*/
	//---Action when click customer code
	public static final int ACTION_VIEW_ALBUM = 0;
	
	/*----------------COLUMNS----------------*/ 
	private TextView tvSTT;				// So thu tu 
	private TextView tvCusCode;			// Ma khach hang
	private TextView tvCusName;			// Ten khach hang
	private TextView tvCusAdd;			// Dia chi
	private TextView tvImageNumber;		// so hinh hinh anh
	private TextView tvStaffName;		// Ten nhan vien ban hang
	private TextView tvVisitPlan; 		// Tuyen cua khach hang
	
	/*----------------DTO----------------*/
	private	ImageListItemDTO dto;	// du lieu render
	/**
	 * Ham khoi tao
	 * 
	 * @param context
	 * @param aRow
	 */
	public SupervisorImageListRow(Context context, SupervisorImageListView supervisorImageListView) {
		super(context, R.layout.layout_supervisor_image_list_row);
		setListener(supervisorImageListView);
		// Binding columns
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		
		tvCusCode = (TextView) this.findViewById(R.id.tvCustomerCode);
		tvCusCode.setOnClickListener(this);
		
		tvCusAdd 		= (TextView) this.findViewById(R.id.tvAddress);
		tvCusName 		= (TextView) this.findViewById(R.id.tvCustomerName);
		tvStaffName 	= (TextView) this.findViewById(R.id.tvNvbnName);
		tvVisitPlan 	= (TextView) this.findViewById(R.id.tvVisitPlan);
		tvImageNumber 	= (TextView) this.findViewById(R.id.tvImageNumber);
	}

	/**
	 * Render du lieu
	 * 
	 * @author: QuangVT
	 * @since: 2:33:59 PM Dec 12, 2013
	 * @return: void
	 * @throws:  
	 * @param position
	 * @param item
	 */
	public void renderLayout(int position, ImageListItemDTO item) {
		this.dto = item;
		
		final String strSTT = "" + position;
		final String strCusCode = item.customerCode.subSequence(0, 3).toString();
		final String strCusName = item.customerName;
		final String strAddress = item.address;
		final String strNumImg 	= "" + item.imageNumber;
		final String strPlan	= item.visitPlan;
		final String strStaffName = item.nvbhStaffName;
		
		tvSTT.setText(strSTT);
		tvCusCode.setText(strCusCode);
		tvCusName.setText(strCusName);
		tvCusAdd.setText(strAddress);
		tvImageNumber.setText(strNumImg);
		tvVisitPlan.setText(strPlan);
		tvStaffName.setText(strStaffName);
	} 
	
	/**
	 * Thong bao khong co du lieu
	 * 
	 * @author: QuangVT
	 * @since: 2:33:46 PM Dec 12, 2013
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutNoResult() {
		tvSTT.setVisibility(View.GONE);
		tvCusCode.setVisibility(View.GONE);
		tvCusName.setVisibility(View.GONE);
		tvCusAdd.setVisibility(View.GONE);
		tvImageNumber.setVisibility(View.GONE);
		tvVisitPlan.setVisibility(View.GONE);
		tvStaffName.setVisibility(View.GONE);
	}

	/**
	 * Lay listener cua row
	 * 
	 * @author: QuangVT
	 * @since: 2:34:42 PM Dec 12, 2013
	 * @return: VinamilkTableListener
	 * @throws:  
	 * @return
	 */
	public VinamilkTableListener getListener() {
		return listener;
	}

	/**
	 * Gan listener cho row
	 * 
	 * @author: QuangVT
	 * @since: 2:34:30 PM Dec 12, 2013
	 * @return: void
	 * @throws:  
	 * @param _listener
	 */
	public void setListener(VinamilkTableListener _listener) {
		this.listener = _listener;
	}

	
	@Override
	public void onClick(View v) {
		if (v == tvCusCode && listener != null) {
			listener.handleVinamilkTableRowEvent(ACTION_VIEW_ALBUM, v, dto);
		} else if (context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}
}
