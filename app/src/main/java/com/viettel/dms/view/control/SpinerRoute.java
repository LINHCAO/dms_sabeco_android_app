/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.sabeco.R;

/**
 * thong tin chung
 * @author : BangHN
 * since   : 1.0
 * version : 1.0
 * com.viettel.vinamilk.view.control.SpinerRoute
 */
public class SpinerRoute extends LinearLayout {
	MySpinner spinerRoute;
	public SpinerRoute(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	public SpinerRoute(Context context) {
		super(context);
		initView(context);
	}
	
	
	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_spiner_route, this);
		spinerRoute = (MySpinner)view.findViewById(R.id.spinerRoute);
	}
	
	public void setAdapter(SpinnerAdapter apdater){
		spinerRoute.setAdapter(apdater);
		
	}
	public void setAdapter(VNMSpinnerTextAdapter apdater){
		spinerRoute.setAdapter(apdater);
		
	}
	
	public void setPrompt(String prompt){
		spinerRoute.setPrompt(prompt);
	}
	
	public void setMinimumWidth(int w){
		spinerRoute.setMinimumWidth(GlobalUtil.dip2Pixel(w));
	}
	
	public void setSelection(int selection){
		spinerRoute.setSelection(selection);
	}
	
	public int getSelectedItemPosition(){
		return spinerRoute.getSelectedItemPosition();
	}
	
	
	public void setOnItemSelectedListener(OnItemSelectedListener listener){
		spinerRoute.setOnItemSelectedListener(listener);
		spinerRoute.setOnItemSelectedEvenIfUnchangedListener(listener);
	}
	
	public Spinner getSpiner(){
		return spinerRoute;
	}
}
