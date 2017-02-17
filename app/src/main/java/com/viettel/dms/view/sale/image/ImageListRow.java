/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.image;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.ImageListItemDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * The hien 1 dong trong bang danh sach khach hang 
 * Trong man hinh 04-01. Danh sach hinh anh
 * 
 * @author quangvt1
 */
public class ImageListRow extends DMSTableRow implements OnClickListener {
	//-- Action --
	public static final int ACTION_VIEW_ALBUM = 0; 
	
	//-- Column -- 
	private TextView tvSTT;			 // so thu tu 
	private TextView tvCusCode;		 // ma khach hang
	private TextView tvCusName;		 // ten khach hang
	private TextView tvCusAdd;		 // Dia chi
	private TextView tvVisitPlan;	 // Tuyen
	private TextView tvImageNumber;	 // so hinh hinh anh
	
	// -- Variables -- 
//	private View view;				// View
	private ImageListItemDTO mDTO;	// DTO render
//	private Context mContext;		// Context
//	protected VinamilkTableListener mListener;	// listener  khi click tren dong

	/**
	 * Lay Listener cua dong
	 * 
	 * @author quangvt1
	 * @return VinamilkTableListener
	 */
//	public VinamilkTableListener getListener() {
//		return mListener;
//	}

	/**
	 * Set Listener cho dong
	 * 
	 * @author quangvt1
	 * @return void
	 */
//	public void setListener(VinamilkTableListener listener) {
//		this.mListener = listener;
//	}

	/**
	 * Khoi tao control
	 * 
	 * @author quangvt1
	 * @param context
	 */ 
	public ImageListRow(Context context, ImageListView lis) {
		super(context, R.layout.layout_image_list_row);
		setListener(lis);
//		super(context);
//		this.mContext = context; 
		
//		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		view = vi.inflate(R.layout.layout_image_list_row, this);
		
//		LinearLayout row = (LinearLayout) this.findViewById(R.id.row);
//		row.setOnClickListener(this);
		
		tvSTT 		= (TextView) this.findViewById(R.id.tvSTT);
		tvCusAdd	= (TextView) this.findViewById(R.id.tvAddress);
		tvCusName 	= (TextView) this.findViewById(R.id.tvCustomerName);
		tvCusCode 	= (TextView) this.findViewById(R.id.tvCustomerCode);
		tvCusCode.setOnClickListener(this);
		tvImageNumber 	= (TextView) this.findViewById(R.id.tvImageNumber);
		tvVisitPlan   	= (TextView) this.findViewById(R.id.tvVisitPlan);
	}

	/**
	 * Render dong
	 * 
	 * @author quangvt1
	 * @param position
	 * @param item
	 */ 
	public void renderLayout(int position, ImageListItemDTO item) {
		this.mDTO = item;
		
		final String strPosition = position + "";
		final String strCusCode  = item.customerCode.subSequence(0, 3).toString();
		final String strCusName  = item.customerName;
		final String strCusAdd   = item.address;
		final String strNumImg   = item.imageNumber + "";
		final String strViPlan   = item.visitPlan;
		
		tvSTT.setText(strPosition);
		tvCusAdd.setText(strCusAdd);
		tvCusCode.setText(strCusCode);
		tvCusName.setText(strCusName);
		tvVisitPlan.setText(strViPlan);
		tvImageNumber.setText(strNumImg);
	}

	@Override
	public void onClick(View v) {
		if (v == tvCusCode && listener != null) {
			listener.handleVinamilkTableRowEvent(ACTION_VIEW_ALBUM, v, mDTO);
		} else if (context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}
}
