/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

/**
 * Customer gridview co dinh, cho phep disable scrollbar
 * (dung de dat trong scrollview)
 * @author BANGHN
 * @version 1.0
 * import: com.viettel.vinamilk.view.control.VNMStableGridView
 */
public class VNMStableGridView extends GridView {
	boolean expanded = false;

	public VNMStableGridView(Context context) {
		super(context);
	}

	public VNMStableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VNMStableGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public boolean isExpanded() {
		return expanded;
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// HACK!
		if (isExpanded()) {
			int expandSpec = MeasureSpec.makeMeasureSpec(
					Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
			super.onMeasure(widthMeasureSpec, expandSpec);

			ViewGroup.LayoutParams params = getLayoutParams();
			params.height = getMeasuredHeight();
		} else {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
