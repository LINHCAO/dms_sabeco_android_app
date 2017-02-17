/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Right Header Menu Bar
 * RightHeaderMenuBar.java
 * @version: 1.0 
 * @since:  08:29:18 20 Jan 2014
 */
public class RightHeaderMenuBar extends LinearLayout {
	public OnEventControlListener listener;
	public Context context;
	public LinearLayout view;

	public RightHeaderMenuBar(Context context) {
		super(context);
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = (LinearLayout) inflater.inflate(
				R.layout.layout_right_header_menu_bar, this);

	}

	public void addMenuItem(int iconRscId, int action, int separateVisible) {
		MenuItem newItem = new MenuItem(context, iconRscId, action);
		newItem.setOnEventControlListener(listener);
		newItem.setSeparateVisible(separateVisible);
		view.addView(newItem);
	}
	
	public void addMenuItem(String text, int iconRscId, int action, int separateVisible) {
		MenuItem newItem = new MenuItem(context, text, iconRscId, action);
		newItem.setOnEventControlListener(listener);
		newItem.setSeparateVisible(separateVisible);
		view.addView(newItem);
	}

	public void setOnEventControlListener(OnEventControlListener lis) {
		listener = lis;
	}

	public void setMenuItemFocus(int index) {
		this.indexFocus = index;
		LinearLayout item = (LinearLayout) view.getChildAt(index);
		item.setBackgroundResource(R.drawable.bg_vnm_header);		
	}

	
	//duongdt
	private int indexFocus = -1;
	//duongdt
	public int getMenuItemFocus(){
	    return indexFocus;
	}

}
