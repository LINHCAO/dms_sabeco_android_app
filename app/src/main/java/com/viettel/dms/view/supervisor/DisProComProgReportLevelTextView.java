/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.sabeco.R;

/**
 * DisProComProgReportLevelTextView
 * 
 * @author hieunq1
 * 
 */
public class DisProComProgReportLevelTextView extends LinearLayout {
	public TextView tvLevel;

	public DisProComProgReportLevelTextView(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = null;
		view = vi.inflate(R.layout.layout_dis_pro_com_prog_report_level, this);
		tvLevel = (TextView) view.findViewById(R.id.tvLevel);
	}

	/**
	 * 
	 * thiet lap lai width cho textview muc chuong trinh
	 * 
	 * @author: HaiTC3
	 * @param width
	 * @return: void
	 * @throws:
	 * @since: Feb 23, 2013
	 */
	public void setWithForTextViewLevel(int width, boolean isFirstTV, boolean isLastTV) {
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(width);
		param.height = LayoutParams.FILL_PARENT;
		if (isFirstTV) {
			param.setMargins(GlobalUtil.dip2Pixel(0), GlobalUtil.dip2Pixel(0),
					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		} else if (isLastTV) {
			param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(0),
					GlobalUtil.dip2Pixel(2), GlobalUtil.dip2Pixel(1));
		} else {
			param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(0),
					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		}

		tvLevel.setLayoutParams(param);
	}
	
	/**
	 * 
	*  thiet lap lai width cho textview muc chuong trinh
	*  @author: HaiTC3
	*  @param width
	*  @param isFirstTV
	*  @return: void
	*  @throws:
	*  @since: Feb 23, 2013
	 */
	public void setWithForTextViewLevelTBHV(int width, boolean isFirstTV) {
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(width);
		param.height = LayoutParams.FILL_PARENT;
		if(isFirstTV){
			param.setMargins(GlobalUtil.dip2Pixel(2), GlobalUtil.dip2Pixel(0),
					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(0));
		}
		else{
			param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(0),
					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(0));
		}
		
		tvLevel.setLayoutParams(param);
	}

	/**
	 * setMarginLeftRight
	 * @param ldip
	 * @param rdip
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 31, 2012
	 */
	void setMarginLeftRight(int ldip, int rdip) {
		Resources r = getResources();
		float lpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				ldip, r.getDisplayMetrics());
		float rpx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				rdip, r.getDisplayMetrics());
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		lp.leftMargin = (int) lpx;
		lp.rightMargin = (int) rpx;
		tvLevel.setLayoutParams(lp);
	}

	
	
	/**
	 * render text
	 * 
	 * @param text
	 */
	public void render(String text) {
		tvLevel.setText(text);
	}
}
