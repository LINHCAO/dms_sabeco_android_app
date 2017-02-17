/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.sabeco.R;

/**
 * Header table info for complex header
 * HeaderTableInfo.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:34:36 21 Nov 2013
 */
public class HeaderTableInfo {
	public HeaderTableInfo[] arrChildHeader;
	public int width;
	public String text;
	public View view;
	
	public HeaderTableInfo(String text, int width){
		this(text, width, null);
	}

	public HeaderTableInfo(String text, int width, HeaderTableInfo[] arrChildHeader){
		this.text = text;
		this.width = width;
		this.arrChildHeader = arrChildHeader;
	}

	private View renderTextView(Context context, int textColor, int bgColor, boolean isTitle){
		TextView myTextView = new TextView(context);
		 
		//title have width = MATCH_PARENT & height = WRAP_CONTENT
		if (isTitle) {
			LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			param.bottomMargin = GlobalUtil.dip2Pixel(1);
			param.topMargin = GlobalUtil.dip2Pixel(1);
			param.leftMargin = GlobalUtil.dip2Pixel(1);
			param.rightMargin = GlobalUtil.dip2Pixel(1);
			myTextView.setLayoutParams(param);
		} else {
			TableRow.LayoutParams param = new TableRow.LayoutParams();
			// normal header
			param.width = width;
			param.height = LayoutParams.MATCH_PARENT;
			param.bottomMargin = GlobalUtil.dip2Pixel(1);
			param.topMargin = GlobalUtil.dip2Pixel(1);
			param.leftMargin = GlobalUtil.dip2Pixel(1);
			param.rightMargin = GlobalUtil.dip2Pixel(1);
			myTextView.setLayoutParams(param);
		}	
		
		
		myTextView.setMinHeight(GlobalUtil.dip2Pixel(35));
		myTextView.setText(text);
		myTextView.setPadding(0, GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5));
		myTextView.setTextColor(textColor);
		myTextView.setBackgroundColor(bgColor);
		myTextView.setTypeface(null, Typeface.BOLD);
		myTextView.setGravity(Gravity.CENTER);
		
		return myTextView;
	}
	
	private ViewGroup renderViewGroup(Context context){
		LinearLayout ll = new LinearLayout(context);
		TableRow.LayoutParams param=new TableRow.LayoutParams();
		param.width = TableRow.LayoutParams.WRAP_CONTENT;
		param.height = TableRow.LayoutParams.WRAP_CONTENT;
		ll.setLayoutParams(param);
		ll.setBackgroundColor(ImageUtil.getColor(R.color.TABLE_BG));
		//theo chieu ngang
		ll.setOrientation(LinearLayout.VERTICAL);
		return ll;
		
	}
	public View render(Context context, int textColor, int bgColor){
		View v = null;
		//simple header
		if (this.arrChildHeader == null || this.arrChildHeader.length == 0) {
			//this is text view
			v = renderTextView(context, textColor, bgColor, false);
		}else{
			
			//Group
			ViewGroup group = renderViewGroup(context);
			//add header view
			View titleHeader = renderTextView(context, textColor, bgColor, true);
			group.addView(titleHeader);
			
			
			//child views in a LinearLayout
			LinearLayout llChild = new LinearLayout(context);
			for (int i = 0; i < arrChildHeader.length; i++) {
				View child = arrChildHeader[i].render(context, textColor, bgColor);
				llChild.addView(child);
			}
			
			//add child view into group
			group.addView(llChild);
			
			v = group;
			
		}
		return v;
	}
}
