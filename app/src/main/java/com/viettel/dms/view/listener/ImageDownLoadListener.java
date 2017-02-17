/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.listener;

import android.graphics.Bitmap;

/**
 * Image download
 * ImageDownLoadListener.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:59:34 14 Jan 2014
 */
public interface ImageDownLoadListener {
	public void onStart();
	public void onFinished(Bitmap bitmap);
}
