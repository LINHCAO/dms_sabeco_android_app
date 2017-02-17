/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.viettel.sabeco.R;

/**
 * icon button hien thi tren ban do
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 */
public class ImageButtonOnMap extends LinearLayout {
	public ImageView ivImageButton;
	public ImageButtonOnMap(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public ImageButtonOnMap(Context context) {
		super(context);
		initView(context);
	}
	
	
	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_image_button_on_map, this);
		ivImageButton = (ImageView)view.findViewById(R.id.ivImageButton);
	}
}
