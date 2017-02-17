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

import com.viettel.dms.dto.view.ImageInfoShopDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Row man hinh ds hinh anh cua NPP
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVListImageRow extends TableRow implements OnClickListener{
	public static final int ACTION_VIEW_DETAIL_NPP = 0;
	Context context;
	View view;
	// row 
	private TableRow row;
	//so luong hinh anh
	TextView tvNumImage;
	// ma NPP
	TextView tvShop;
	// ten GSNPP
	TextView tvStaffCode;
	// text hien thi khong co ket qua
	TextView tvNoResultInfo;
	//row dto
	ImageInfoShopDTO dto;
	// listener when row have action
	protected VinamilkTableListener listener;
	
	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener _listener) {
		this.listener = _listener;
	}

	// khoi tao control
	public TBHVListImageRow(Context context, View aRow) {
		super(context);
		this.context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_list_image_of_npp_row, this);
		TableRow row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvNumImage = (TextView) view.findViewById(R.id.tvNumImage);
		tvStaffCode = (TextView) view.findViewById(R.id.tvStaffCode);
		tvStaffCode.setOnClickListener(this);
		tvShop = (TextView) view.findViewById(R.id.tvShop);
		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
	}
	
	// render layout
	public void renderLayout(int position, ImageInfoShopDTO item){
		this.dto = item;
		tvStaffCode.setText(item.supervisorName);
		tvShop.setText(item.shopCode);
		tvNumImage.setText(String.valueOf(item.numImages));
	}		
	
	/**
	* Hien thi khong co du lieu
	* @author: TruongHN
	* @return: void
	* @throws:
	 */
	public void renderLayoutNoResult() {
		tvStaffCode.setVisibility(View.GONE);
		tvShop.setVisibility(View.GONE);
		tvNumImage.setVisibility(View.GONE);
		tvNoResultInfo.setVisibility(View.VISIBLE);
		tvNoResultInfo.setText(StringUtil.getString(R.string.TEXT_CAN_NOT_FIND_NPP));
	}

	
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0 ==tvStaffCode && listener != null){
			listener.handleVinamilkTableRowEvent(ACTION_VIEW_DETAIL_NPP, arg0, dto);
		}else if (arg0 == row && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
		}
	}
}
