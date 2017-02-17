/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map;

import com.viettel.maps.MapOptions;
import com.viettel.maps.base.LatLng;

/**
 * OverlayViewOptions
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class OverlayViewOptions extends MapOptions {

	private LatLng position;
	private boolean visible = true;
	private int drawMode = OverlayViewItemObj.DRAW_BOTTOM_CENTER;
	private int dw;
	private int dh;

	public OverlayViewOptions() {
		position = null;
	}

	public OverlayViewOptions position(LatLng pt) {
		position = pt;
		return this;
	}

	public OverlayViewOptions visible(boolean visible) {
		this.visible = visible;
		return this;
	}

	public LatLng getPosition() {
		return position;
	}

	public boolean isVisible() {
		return visible;
	}

	public int drawMode(int drawMode) {
		this.drawMode = drawMode;
		return drawMode;
	}

	public int getdrawMode() {
		return drawMode;
	}

	public int offsetWidth(int w) {
		dw = w;
		return dw;
	}

	public int offsetHeight(int h) {
		dh = h;
		return dh;
	}

	public int getOffsetWidth() {
		return dw;
	}

	public int getOffsetHeight() {
		return dh;
	}
}
