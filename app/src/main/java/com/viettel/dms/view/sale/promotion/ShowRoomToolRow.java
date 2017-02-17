/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ShowRoomToolDto;
import com.viettel.sabeco.R;

/**
 * Show Room Tool Row
 * ShowRoomToolRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:20:34 14 Jan 2014
 */
public class ShowRoomToolRow extends TableRow {

	private View view;
	TextView tvNumber;
	public TextView tvToolCode;
	TextView tvToolName;
	CheckBox cbPassed;
	CheckBox cbFailed;

	public ShowRoomToolRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_showroom_tool_row, this);
		tvNumber = (TextView) view.findViewById(R.id.tvSTT);
		tvToolCode = (TextView) view.findViewById(R.id.tvToolCode);
		tvToolName = (TextView) view.findViewById(R.id.tvToolName);
		cbPassed = (CheckBox) view.findViewById(R.id.cbPass);
		cbFailed = (CheckBox) view.findViewById(R.id.cbFailed);
	}

	public void render(int pos, ShowRoomToolDto showToolDto) {
		tvNumber.setText("" + pos);
		tvToolCode.setText(showToolDto.toolCode);
		tvToolName.setText(showToolDto.toolName);
	}

}
