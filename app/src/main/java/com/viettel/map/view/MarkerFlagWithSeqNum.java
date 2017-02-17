/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;


/**
 * Marker hien thi icon tren ban do
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 */
public class MarkerFlagWithSeqNum extends LinearLayout{
	private static final int MAX_WIDTH_DP = 48;    
	final float SCALE = getContext().getResources().getDisplayMetrics().density;
	public RelativeLayout llRoot; //layout root
	public int widthPopup;//chieu rong popup
	private ImageView ivMaker;//icon marker
	private TextView tvIndex;//text hien thi
	Context mContext;//context
	OnEventControlListener listener;//lister su kien
	Object userData;//data user dinh nghia
	int action;//action
	
	
	public MarkerFlagWithSeqNum(Context context, int icon, String seq) {
		super(context);
		initMarker();
		setIconMarker(icon);
		setTextMarker(seq);
	}
	
	public void setListener(OnEventControlListener listener, int action, Object userData){
		this.listener = listener;
		this.action = action;
		this.userData = userData;
	}
	
	
	/**
	 * init popup
	 * @author : BangHN
	 * since : 1.0
	 */
	private void initMarker() {
		this.setLayoutParams(new ViewGroup.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_marker_flag_with_seq_num, this);
		llRoot = (RelativeLayout) v.findViewById(R.id.llRoot);
		ivMaker =(ImageView) v.findViewById(R.id.ivMaker);
		tvIndex =(TextView) v.findViewById(R.id.tvIndex);
		widthPopup = GlobalUtil.dip2Pixel(48);
		
//		tvIndex.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				listener.onEvent(action, MarkerFlagWithSeqNum.this, userData);
//			}
//		});
//		ivMaker.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				listener.onEvent(action, MarkerFlagWithSeqNum.this, userData);
//			}
//		});
		
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int adjustedMaxWidth = (int)(MAX_WIDTH_DP * SCALE + 0.5f);
        int adjustedWidth = Math.min(widthPopup, adjustedMaxWidth);
        int adjustedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
        super.onMeasure(adjustedWidthMeasureSpec, heightMeasureSpec);
    }
	
	public void setIconMarker(int drawable){
		ivMaker.setImageResource(drawable);
	}
	
	
	public void setTextMarker(String text){
		tvIndex.setText(text);
	}
	
	public void setFontSize(float size){
		tvIndex.setTextSize(size);
	}
}
