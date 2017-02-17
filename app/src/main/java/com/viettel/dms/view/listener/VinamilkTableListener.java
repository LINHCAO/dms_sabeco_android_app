/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.listener;

import android.view.View;

/**
 *  control listener for vinamilk table
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public interface VinamilkTableListener {
	void handleVinamilkTableloadMore(View control, Object data);
	void handleVinamilkTableRowEvent(int action, View control, Object data);
}
