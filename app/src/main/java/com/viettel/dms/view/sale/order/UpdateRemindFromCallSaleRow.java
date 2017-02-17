/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Update Remind From Call Sale Row
 * UpdateRemindFromCallSaleRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:17:15 14 Jan 2014
 */
public class UpdateRemindFromCallSaleRow extends TableRow {
	public TextView tvNumer;
	public TextView tvDoneDate;
	public CheckBox cbDone;
	public TextView tvContent;
	public TextView tvRemindDate;
	public TextView tvType;
	private View view;

	public UpdateRemindFromCallSaleRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi
				.inflate(R.layout.layout_update_remind_from_callsale_row, this);
		tvNumer = (TextView) view.findViewById(R.id.tvSTT);
		tvContent = (TextView) view.findViewById(R.id.tvContent);
		tvRemindDate = (TextView) view.findViewById(R.id.tvRemindDate);
		tvDoneDate = (TextView) view.findViewById(R.id.tvOpDate);
		cbDone = (CheckBox) view.findViewById(R.id.cbNote);
		tvType = (TextView)view.findViewById(R.id.tvType);
	}

	public void renderLayout(int pos, FeedBackDTO item) {
		tvNumer.setText("" + pos);
		if(item.remindDate != null){
			tvRemindDate.setText(item.remindDate);
		}else{
			tvRemindDate.setText("");
		}
		if(item.doneDate != null){
			tvDoneDate.setText(item.doneDate);
		}else{	
			tvDoneDate.setText("");
		}
		if(!StringUtil.isNullOrEmpty(item.doneDate)){
			cbDone.setChecked(true);
			cbDone.setEnabled(false);
		}else{
			cbDone.setChecked(false);
		}
		tvType.setText(item.getTypeTitle(item.type));
		tvContent.setText(item.content);
	}

}
