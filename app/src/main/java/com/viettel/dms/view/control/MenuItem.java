/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Menu item dang: Icon + text, action
 * @author : BangHN since : 11:29:38 AM version :
 */
public class MenuItem extends LinearLayout {

	public OnEventControlListener listener;
	public View view;
	// icon
	public ImageView ivIconMenu;
	// icon phan cach
	public ImageView ivSeparate;
	// text menu
	public TextView tvTitle;

	public MenuItem(Context context, int iconRscId, final int action) {
		super(context);
		initMenuItem(context, "", iconRscId, action);
	}
	
	
	public MenuItem(Context context, String text, int iconRscId, final int action) {
		super(context);
		initMenuItem(context, text, iconRscId, action);
	}
	
	public void setSeparateVisible(int visible){
		ivSeparate.setVisibility(visible);
	}

	private void initMenuItem(Context context, String text, int iconRscId, final int action) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.layout_menu_bar_item, this);
		ivIconMenu = (ImageView) view.findViewById(R.id.ivIconMenu);
		ivSeparate = (ImageView) view.findViewById(R.id.ivSeparate);
		tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		
		if(StringUtil.isNullOrEmpty(text)){
			tvTitle.setVisibility(View.GONE);
		}else{
			tvTitle.setText(text);
			tvTitle.setVisibility(View.VISIBLE);
		}
		
		ivIconMenu.setImageResource(iconRscId);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEvent(action, MenuItem.this, null);
			}
		});
		
		tvTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEvent(action, MenuItem.this, null);
			}
		});
	}

	public void setOnEventControlListener(OnEventControlListener lis) {
		listener = lis;
	}
}
