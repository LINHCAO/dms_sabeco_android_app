/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map;

import android.view.View;

import com.viettel.maps.layers.MapLayer;

/**
 * Layer view
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */

@SuppressWarnings("rawtypes")
public class OverlayViewLayer extends MapLayer {
	public static int zMarkerLayerIndexCounter = 300;
	protected int offsetX;
	protected int offsetY;
	private OverlayViewItemObj obj;

	@SuppressWarnings("unchecked")
	public OverlayViewLayer() {
		offsetX = 0;
		offsetY = 0;
		mZIndex = zMarkerLayerIndexCounter;
		zMarkerLayerIndexCounter++;
		zCustomLayerIndexCounter--;
	}

	@SuppressWarnings("unchecked")
	public OverlayViewItemObj addItemObj(View view, OverlayViewOptions opts) {
		if (opts == null) {
			return null;
		} else {
			obj = new OverlayViewItemObj(view, opts);
			add(obj);
			getMapView().addView(view);
			return obj;
		}
	}

	public OverlayViewItemObj getItemObj() {
		return obj;
	}
}
