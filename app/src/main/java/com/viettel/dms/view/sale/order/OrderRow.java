/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.viettel.dms.dto.db.SaleOrderDetailDTO;
import com.viettel.sabeco.R;

/** 
 * Order Row
 * OrderRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:15:27 14 Jan 2014
 */
public class OrderRow {
	Context _context;
	View view;
	TextView tvSTT;
	TextView tvMMH;
	TextView tvTMH;
	TableLayout tbContent;
	LinearLayout header;
	public OrderRow(Context context,View v){
		_context = context;
		view = v;
		tvSTT = (TextView)view.findViewById(R.id.tvSTT);
		tvMMH = (TextView)view.findViewById(R.id.tvMMH);
		tvTMH = (TextView)view.findViewById(R.id.tvTMH);
		tbContent = (TableLayout)view.findViewById(R.id.tbContent);
		header = (LinearLayout) view.findViewById(R.id.header);
		
	}
	
	public void renderLayout(int position,SaleOrderDetailDTO dto,boolean isFocused){
		if(isFocused){
//			tbContent.setBackgroundColor(R.color.OGRANGE);
		}else{
			tbContent.setBackgroundResource(R.drawable.table_row_selector);
		}
		tvSTT.setText(String.valueOf(position));
		tvMMH.setText("xx");
		tvTMH.setText("dd");
	}
	
	
	public void setHeaderVisible(int visible){
		header.setVisibility(visible);
	}
}

