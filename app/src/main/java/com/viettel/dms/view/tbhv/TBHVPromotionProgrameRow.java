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

import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 *  Row cua MH CTKM dung cho lop TBHVPromotionProgramView
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVPromotionProgrameRow extends TableRow implements OnClickListener{
	public static final int ACTION_CLICK_CODE_PROGRAM = 1;
	private final Context context;
	private final View view;
	//so thu tu
	private TextView tvSTT;
	//ma chuong trinh
	private TextView tvCode;
	//ten chuong trinh
	private TextView tvName;
	//tu ngay - den ngay
	private TextView tvDate;
	//du lieu cua row
	PromotionProgrameDTO currentDTO;
	// listener xu ly su kien tren row
	protected VinamilkTableListener listener;

	public TBHVPromotionProgrameRow(Context con){
		super(con);
		context=con;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_promotion_row, this);
	}
	
	public TBHVPromotionProgrameRow(Context context, VinamilkTableListener listener) {
		this(context);
		this.listener = listener;
		setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvPromotionSTT);
		tvCode = (TextView) view.findViewById(R.id.tvPromotionCode);
		tvCode.setOnClickListener(this);
		tvName = (TextView) view.findViewById(R.id.tvPromotionName);
		tvDate = (TextView) view.findViewById(R.id.tvPromotionDate);
	}
	
	/**
	 * 
	*  render du lieu cho row
	*  @author: Nguyen Thanh Dung
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, PromotionProgrameDTO item) {
		this.currentDTO = item;
		tvSTT.setText("" + position);
		tvCode.setText(item.PROMOTION_PROGRAM_CODE);
		tvName.setText(item.PROMOTION_PROGRAM_NAME);
		tvDate.setText(item.FROM_DATE + " - " + item.TO_DATE);
	}

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvCode) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_PROMOTION_PROGRAM_DETAIL, null, this.currentDTO);
		} else if (v == this && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}
}
