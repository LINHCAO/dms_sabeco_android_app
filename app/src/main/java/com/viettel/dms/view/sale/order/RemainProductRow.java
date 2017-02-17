/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.viettel.dms.dto.view.RemainProductViewDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/** 
 * Kiem hang ton
 * RemainProductRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:16:51 14 Jan 2014
 */
public class RemainProductRow extends DMSTableRow implements OnClickListener {

	public static final int ACTION_CLICK_SDH = 0;
	public static final int ACTION_CLICK_MKH = 1;
	public static final int ACTION_CLICK_DELETE = 2;
	public static final int ACTION_CLICK_SELECT = 3;
	public static final int ACTION_CLICK_SHOW_PROMOTION_DETAIL = 4;
	Context _context;
	TextView tvSTT;
	public TextView tvMMH;
	TextView tvTMH;
	TextView tvPrice;
	EditText edRemainQuanlity;
	CheckBox cbCheck;

	// listener
	protected OnEventControlListener listener;

	public RemainProductRow(Context context, RemainProductView lis) {
		super(context, R.layout.remain_product_row);
		_context = context;
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvMMH = (TextView) this.findViewById(R.id.tvMMH);
		tvTMH = (TextView) this.findViewById(R.id.tvTMH);
		tvPrice = (TextView) this.findViewById(R.id.tvPrice);
		edRemainQuanlity = (EditText) this.findViewById(R.id.edRemainQuanlity);
		edRemainQuanlity.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					edRemainQuanlity.setSelection(edRemainQuanlity.getText().length());
				}
			}
		});
		cbCheck =(CheckBox)this.findViewById(R.id.cbCheck);
		cbCheck.setOnClickListener(this);
	}

	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	public void renderLayout(int position, RemainProductViewDTO item) {
		tvSTT.setText("" + position);
		tvMMH.setText(item.productCode);
		tvTMH.setText(item.productName);
		tvPrice.setText(item.uom);
		edRemainQuanlity.setText(item.remainNumber);
		
		if(StringUtil.isNullOrEmpty(item.promotionProgrameDetailId)){
			
		}
		cbCheck.setTag(item);
		if (item.isCheck){
			cbCheck.setChecked(true);
			cbCheck.setSelected(true);
		}else {
			cbCheck.setChecked(false);
			cbCheck.setSelected(false);
		}
	}

	@Override
	public void onClick(View v) {
//		if (v== cbCheck){
//			RemainProductViewDTO item  = (RemainProductViewDTO)v.getTag();
//			listener.onEvent(ACTION_CLICK_SELECT, this, item);
//		}
		
		if (v == this && _context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)_context);
		}
	}
}
