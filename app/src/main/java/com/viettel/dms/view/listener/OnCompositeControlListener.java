/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.listener;

import android.view.View;

/**
 * @author: AnhND
 * @since : May 21, 2011
 * 
 */
public interface OnCompositeControlListener {
	void onEvent( int eventType, View control, Object data);
	void onSoftKeyboardShown(boolean isShowing);
	
	
}
