/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;

/**
 * Tao mot table row cho VinamilkTable
 * VinamilkTableRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  09:20:14 8 Jan 2014
 */
public abstract class VinamilkTableRow extends TableRow implements View.OnClickListener {	

	public VinamilkTableRow(Context context) {
		super(context);
		this.setOnClickListener(this);
	}

	public VinamilkTableRow(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * Hiển thị màu chữ cho 1 ViewGroup (nếu dùng biến row)
	 * @author: duongdt3
	 * @since: 09:15:35 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param color get by ImageUtil.getColor(idResourceColor)
	 */
	protected void setTextColorRow(ViewGroup vGroup, int color) {
		for (int i = 0, size = vGroup.getChildCount(); i < size; i++) {
			View child = vGroup.getChildAt(i);
			GlobalUtil.setTextColorView(child, color);
		}
	}
	
	/**
	 * Hiện màu chữ cho 1 mảng View
	 * @author: duongdt3
	 * @since: 09:42:45 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param vGroup
	 * @param color  get by ImageUtil.getColor(idResourceColor)
	 */
	protected void setTextColorArrayViewInRow(View[] arrView, int color) {
		GlobalUtil.setTextColorViewInRow(arrView, color);
	}
	
	/**
	 * Hiển thị màu chữ cho Row 
	 * @author: duongdt3
	 * @since: 09:15:35 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param color  get by ImageUtil.getColor(idResourceColor)
	 */
	protected void setTextColorRow(int color) {
		setTextColorRow(this, color);
	}
	
	/**
	 * Hiển thị chữ đậm cho View Group (nếu dùng biến row)
	 * @author: duongdt3
	 * @since: 09:25:58 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	protected void setBoldTextRow(ViewGroup vGroup) {
		for (int i = 0, size = vGroup.getChildCount(); i < size; i++) {
			View child = vGroup.getChildAt(i);
			GlobalUtil.setBoldView(child);
		}
	}
	
	/**
	 * Hiển thị chữ đậm cho 1 mảng View
	 * @author: duongdt3
	 * @since: 09:44:34 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param arView
	 */
	protected void setBoldTextRow(View[] arrView) {
		GlobalUtil.setBoldArrayViewInRow(arrView);
	}
	
	/**
	 * Hiển thị chữ đậm cho Row
	 * @author: duongdt3
	 * @since: 09:25:58 8 Jan 2014
	 * @return: void
	 * @throws:
	 */
	protected void setBoldTextRow() {
		setBoldTextRow(this);
	}
	
	/**
	 * Hiển thị nền màu cho 1 ViewGroup (nếu xài biến row)
	 * @author: duongdt3
	 * @since: 09:52:43 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param vGroup
	 * @param color  get by ImageUtil.getColor(idResourceColor)
	 */
	public void setBackgroundRowByColor(ViewGroup vGroup, int color) {
		for (int i = 0, size = vGroup.getChildCount(); i < size; i++) {
			View child = vGroup.getChildAt(i);
			ImageUtil.setBackgroundByColor(child, color);
		}
	}
	
	/**
	 * Hiển thị nền màu cho Row
	 * @author: duongdt3
	 * @since: 09:53:27 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param color get by ImageUtil.getColor(idResourceColor)
	 */
	public void setBackgroundRowByColor(int color) {
		setBackgroundRowByColor(this, color);
	}
	
	/**
	 *  Hiển thị nền màu cho 1 mảng view
	 * @author: duongdt3
	 * @since: 09:55:21 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param arrView
	 * @param color get by ImageUtil.getColor(idResourceColor)
	 */
	public void setBackgroundRowByColor(View[] arrView, int color) {
		for (int i = 0, size = arrView.length;i < size; i++) {
			View child = arrView[i];
			ImageUtil.setBackgroundByColor(child, color);
		}
	}
	
	/**
	 * Hiển thị nền selector cho 1 ViewGroup (nếu dùng biến row)
	 * @author: duongdt3
	 * @since: 09:58:53 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param vGroup
	 * @param idSelectorDrawable
	 */
	public void setBackgroundRowBySelectorDrawable(ViewGroup vGroup, int idSelectorDrawable){
		for (int i = 0, size = vGroup.getChildCount(); i < size; i++) {
			View child = vGroup.getChildAt(i);
			ImageUtil.setBackgroundBySelectorDrawable(child, idSelectorDrawable);
		}
	}
	
	/**
	 * Hiển thị nền selector cho Row
	 * @author: duongdt3
	 * @since: 09:59:30 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param idSelectorDrawable
	 */
	public void setBackgroundRowBySelectorDrawable(int idSelectorDrawable){
		setBackgroundRowBySelectorDrawable(this, idSelectorDrawable);
	}
	
	/**
	 * Hiển thị nền selector cho 1 mảng view
	 * @author: duongdt3
	 * @since: 09:59:50 8 Jan 2014
	 * @return: void
	 * @throws:  
	 * @param arrView
	 * @param idSelectorDrawable
	 */
	public void setBackgroundRowBySelectorDrawable(View[] arrView, int idSelectorDrawable) {
		for (int i = 0, size = arrView.length;i < size; i++) {
			View child = arrView[i];
			ImageUtil.setBackgroundBySelectorDrawable(child, idSelectorDrawable);
		}
	}
	
	@Override
	public void onClick(View v) {
		//ẩn bàn phím
		GlobalUtil.forceHideKeyboardInput(getContext(), this);
	}
	
}
