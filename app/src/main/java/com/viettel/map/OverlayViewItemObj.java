/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.View;

import com.viettel.maps.MapView;
import com.viettel.maps.Projection;
import com.viettel.maps.base.LatLng;
import com.viettel.maps.base.LatLngBounds;
import com.viettel.maps.objects.MapObject;

/**
 * Object layer
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class OverlayViewItemObj extends MapObject {
	private View view;
	private OverlayViewOptions options;
	private int dw;
	private int dh;
	private int drawMode = 0;

	public final static int DRAW_BOTTOM_CENTER = 0;
	public final static int DRAW_CENTER = 1;
	public final static int DRAW_BOTTOM_LEFT = 2;

	public OverlayViewItemObj(Context context, OverlayViewOptions opts) {
		super();
		options = null;
		if (opts != null)
			options = opts;
		else
			options = new OverlayViewOptions();
		updateBoundary();
	}

	public OverlayViewItemObj(View v, OverlayViewOptions opts) {
		super();
		view = v;
		options = null;
		if (opts != null)
			options = opts;
		else
			options = new OverlayViewOptions();
		updateBoundary();
	}

	private void updateBoundary() {
		mBoundary = null;
		if (options.getPosition() != null)
			mBoundary = new LatLngBounds(options.getPosition(), options.getPosition());
	}

	public void setOptions(OverlayViewOptions opts) {
		options = opts;
	}

	public View getView() {
		return view;
	}

	public void setView(View v) {
		view = v;
	}

	public void setPosition(LatLng pt) {
		options.position(pt);
		updateBoundary();
	}

	public LatLng getPosition() {
		return options.getPosition();
	}

	// public void setVisible(boolean visible) {
	// options.visible(visible);
	// }

	public boolean isVisible() {
		return options.isVisible();
	}

	// public void setDrawMode(int drawMode) {
	// this.drawMode = drawMode;
	// }

	// public void setOffsetWidth(int dw) {
	// this.dw = dw;
	// }
	//
	// public void setOffsetHeight(int dh) {
	// this.dh = dh;
	// }

	@Override
	public void draw(Canvas canvas, MapView mapView, Projection proj, LatLngBounds mapViewBoundary) {
		if (options.getPosition() == null || !options.isVisible() || view == null)
			return;
		LatLng position = options.getPosition();
		drawMode = options.getdrawMode();
		dw = options.getOffsetWidth();
		dh = options.getOffsetHeight();
		// if (!mapViewBoundary.contains(position))
		// return;
		Point ptView = proj.toViewPixel(position);
		int widthPopup = view.getMeasuredWidth();
		int heightPopup = view.getMeasuredHeight();

		int left;
		int top;

		if (drawMode == DRAW_CENTER) {
			left = (int) ptView.x - widthPopup / 2 + dw;
			top = (int) ptView.y - heightPopup / 2 + dh;
		} else if (drawMode == DRAW_BOTTOM_LEFT) {
			left = (int) ptView.x + dw;
			top = (int) ptView.y - heightPopup + dh;
		} else {// drawMode == DRAW_BOTTOM_CENTER
			left = (int) ptView.x - widthPopup / 2 + dw;
			top = (int) ptView.y - heightPopup + dh;
		}

		view.layout(left, top, left + widthPopup, top + heightPopup);
	}

	public boolean pointInObject(Point point, LatLng latlng) {
		return false;
	}

}
