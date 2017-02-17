/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.view;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Image Text Marker Map View
 * ImageTextMarkerMapView.java
 * @version: 1.0 
 * @since:  08:33:49 20 Jan 2014
 */
public class ImageTextMarkerMapView extends LinearLayout {
	public RelativeLayout llRoot;
	private ImageView ivMaker;
	private TextView tvIndex;
	private int widthPopup;
	public int icon;
	public String name;
	protected OnEventControlListener listener;
	protected int action;
	public int userData;
	public int dyOffset;

	public ImageTextMarkerMapView(Context context, int icon, String name, int textColor) {
		super(context);
		initMarker();
		setIconMarker(icon);
		setTextMarker(name, textColor);
	}

	public void setIconMarker(int drawable) {
		ivMaker.setImageResource(drawable);
	}

	public void setTextMarker(String text, int textColor) {
		tvIndex.setText(text);
		tvIndex.setTextColor(ImageUtil.getColor(textColor));
		widthPopup = getWidthOfPopup();
	}

	public void setListener(OnEventControlListener listener, int action, int index) {
		this.listener = listener;
		this.action = action;
		this.userData = index;
	}

	private void initMarker() {
		this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_text_image_marker_on_map, this);
		llRoot = (RelativeLayout) v.findViewById(R.id.llRoot);
		ivMaker = (ImageView) v.findViewById(R.id.ivMaker);
		tvIndex = (TextView) v.findViewById(R.id.tvIndex);
		widthPopup = getWidthOfPopup();

		tvIndex.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEvent(action, ImageTextMarkerMapView.this, userData);
			}
		});
		ivMaker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onEvent(action, ImageTextMarkerMapView.this, userData);
			}
		});
	}

	public int getWidthOfPopup() {
		Rect bounds = new Rect();
		Paint textPaint = tvIndex.getPaint();
		textPaint.getTextBounds(tvIndex.getText().toString(), 0, tvIndex.getText().toString().length(), bounds);
		int width = bounds.width();
		dyOffset = 5 + bounds.height();
		return width;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int adjustedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthPopup, mode);
		super.onMeasure(adjustedWidthMeasureSpec, heightMeasureSpec);
	}

}
