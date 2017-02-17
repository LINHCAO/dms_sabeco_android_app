/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.sabeco.R;


/**
 * Marker hien thi icon tren ban do
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 */
public class MarkerMapView extends LinearLayout{
//	private static final int MAX_WIDTH_DP = 64;    
	final float SCALE = getContext().getResources().getDisplayMetrics().density;
	public RelativeLayout llRoot; 
	public int widthPopup;
	private ImageView ivMaker;
	Context mContext;
	public MarkerMapView(Context context, int icon) {
		super(context);
		initMarker();
		setIconMarker(icon);
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
		View v = inflater.inflate(R.layout.layout_marker_on_map, this);
		llRoot = (RelativeLayout) v.findViewById(R.id.llRoot);
		ivMaker =(ImageView) v.findViewById(R.id.ivMaker);
	}
	
	/**
	 * set width, height cho icon marker
	 * @author banghn
	 * @param w: rong
	 * @param h: cao
	 */
	public void setIconSize(int w, int h){
		ivMaker.getLayoutParams().height = GlobalUtil.dip2Pixel(h);
		ivMaker.getLayoutParams().width = GlobalUtil.dip2Pixel(w);
	}
	
	@Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int mode = MeasureSpec.getMode(widthMeasureSpec);
//        int adjustedMaxWidth = (int)(MAX_WIDTH_DP * SCALE + 0.5f);
//        int adjustedWidth = Math.min(widthPopup, adjustedMaxWidth);
//        int adjustedWidthMeasureSpec = MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
//        super.onMeasure(adjustedWidthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthPopup, heightMeasureSpec);
    }
	
	public void setIconMarker(int drawable){
		BitmapDrawable bd=(BitmapDrawable) this.getResources().getDrawable(drawable);
		int width=bd.getBitmap().getWidth();
		widthPopup = GlobalUtil.dip2Pixel(width);
		ivMaker.setImageResource(drawable);
	}
	
	public void setIconBitmap(Bitmap btImage){
		widthPopup = GlobalUtil.dip2Pixel(btImage.getWidth());
		ivMaker.setImageBitmap(btImage);
	}
}
