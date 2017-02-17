/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 *  Spinner override (co the nhan su kien khi click item cu)
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class MySpinner extends Spinner {
	OnItemSelectedListener listener;

	public MySpinner(Context context, AttributeSet attrs) {
	    super(context, attrs);
	}

	@Override
	public void setSelection(int position) {
	    super.setSelection(position);
	    if (listener != null)
	        listener.onItemSelected(null, null, position, 0);
	}

	public void setOnItemSelectedEvenIfUnchangedListener(
	        OnItemSelectedListener listener) {
	    this.listener = listener;
	}
}
