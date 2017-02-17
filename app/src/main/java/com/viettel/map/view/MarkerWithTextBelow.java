/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.view;

import java.text.DecimalFormat;

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

import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Marker ban do voi text ben duoi (Rename tu class: SaleVisitMarkerView)
 * 
 * @author banghn
 * @since 1.0
 */
public class MarkerWithTextBelow extends LinearLayout {
	// int MAX_WIDTH_DP = 80;
	final float SCALE = getContext().getResources().getDisplayMetrics().density;
	public TextView tvTextInfo; // khoang cach cua nhan vien ban hang
	public ImageView ivMarker;// icon marker
	public RelativeLayout llRoot; // Update UI for popup
	public int widthPopup;
	public Object userData;
	int action;// action
	OnEventControlListener listener;// lister su kien

	public MarkerWithTextBelow(Context context, int icon) {
		super(context);
		loadControls();
		setIconMarker(icon);
	}

	public void setListener(OnEventControlListener listener, int action, Object userData) {
		this.listener = listener;
		this.action = action;
		this.userData = userData;
	}

	/**
	 * Init control view marker
	 * 
	 * @author banghn
	 */
	private void loadControls() {
		this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_maker_with_text_below, this);
		llRoot = (RelativeLayout) v.findViewById(R.id.llRoot);
		tvTextInfo = (TextView) v.findViewById(R.id.tvDistance);
		ivMarker = (ImageView) v.findViewById(R.id.ivMarker);

		tvTextInfo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onEvent(action, MarkerWithTextBelow.this, userData);
			}
		});
		ivMarker.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener != null)
					listener.onEvent(action, MarkerWithTextBelow.this, userData);
			}
		});
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int mode = MeasureSpec.getMode(widthMeasureSpec);
		// int adjustedMaxWidth = (int) (MAX_WIDTH_DP * SCALE + 0.5f);
		// int adjustedWidth = Math.min(widthPopup, adjustedMaxWidth);
		// int adjustedWidthMeasureSpec =
		// MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
		super.onMeasure(widthPopup, heightMeasureSpec);
	}

	public int getWidthOfPopup() {
		Rect bounds = new Rect();
		Paint textPaint = tvTextInfo.getPaint();
		textPaint.getTextBounds(tvTextInfo.getText().toString(), 0, tvTextInfo.getText().toString().length(), bounds);
		int width = bounds.width();
		return width;
	}

	/**
	 * Thong tin ghe tham cua nhan vien voi khach hang
	 * 
	 * @param distance
	 * @param index
	 * @param seq
	 */
	public void updateSaleVitsitedInfo(double distance, int index, CustomerListItem dto) {
		userData = new int[] { index, dto.isOr };
//		String seq = "";
//		if (dto.isOr == 0) {
//			if (!StringUtil.isNullOrEmpty(dto.seqInDayPlan)) {
//				seq = dto.seqInDayPlan;
//			} else {
//				seq = "0";
//			}
//		} else {
//			seq = "!";
//		}
		String formate = "";
		if (distance >= 1000) {
			float tempDistance = (float) distance / 1000;
			DecimalFormat df = new DecimalFormat("0.00");
			formate = df.format(tempDistance);
			formate = "(" + dto.aCustomer.customerCode + "): " + formate + " km";
		} else {
			DecimalFormat df = new DecimalFormat("0.00");
			formate = df.format(distance);
			formate = "(" + dto.aCustomer.customerCode + "): " + formate + " m";
		}
		tvTextInfo.setText(formate);
		widthPopup = getWidthOfPopup();
	}

	/**
	 * set width, height cho icon marker
	 * 
	 * @author banghn
	 * @param w
	 *            : rong
	 * @param h
	 *            : cao
	 */
	public void setIconSize(int w, int h) {
		ivMarker.getLayoutParams().height = GlobalUtil.dip2Pixel(h);
		ivMarker.getLayoutParams().width = GlobalUtil.dip2Pixel(w);
	}

	public void setIconMarker(int drawable) {
		ivMarker.setImageResource(drawable);
	}

	public void setTextSize(float size, int typeFace) {
		tvTextInfo.setTextSize(size);
		tvTextInfo.setTypeface(null, typeFace);
	}

	public void setTextInfo(String text) {
		tvTextInfo.setText(text);
	}

	public void setMaxWith(int w) {
		widthPopup = w;
	}
}
