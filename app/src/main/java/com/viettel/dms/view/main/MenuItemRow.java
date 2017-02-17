/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.control.MenuItemDTO;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Menu Item row
 * MenuItemRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:02:58 14 Jan 2014
 */
public class MenuItemRow extends LinearLayout {
	OnEventControlListener listener;
	Context mContext;
	LinearLayout llRow;
	public TextView tvText;
	ImageView ivIcon;
	
	public MenuItemRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initMenu();
	}

	public MenuItemRow(Context context, View row) {
		super(context);
		mContext = context;
		initMenu(row);
	}

	private void initMenu() {
		LayoutInflater vi = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.layout_fragment_menu_item, null);
		
		tvText = (TextView) view.findViewById(R.id.tvText);
		ivIcon = (ImageView) view.findViewById(R.id.ivIcon);
		llRow = (LinearLayout) view.findViewById(R.id.llRow);
	}
	
	private void initMenu(View row) {
		llRow = (LinearLayout) row.findViewById(R.id.llRow);
		tvText = (TextView) row.findViewById(R.id.tvText);
		ivIcon = (ImageView) row.findViewById(R.id.ivIcon);
	}

	public void populateFrom(MenuItemDTO item) {
		if(item != null){
			tvText.setText(item.getTextMenu());
			ivIcon.setImageResource(item.getIconMenu());
			if(item.isSelected()){
				llRow.setBackgroundResource(R.drawable.bg_vnm_menu_focus);
			}else{
				llRow.setBackgroundResource(R.drawable.fragment_menu_item_selector);
			}
		}
	}
	
	
	public void setBackground(int res){
		if(llRow != null)
			llRow.setBackgroundResource(res);
	}
}
