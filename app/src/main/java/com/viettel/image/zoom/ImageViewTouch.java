/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.image.zoom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.ViewConfiguration;

import com.viettel.dms.view.main.BaseFragment;

/**
 * Image View Touch
 * ImageViewTouch.java
 * @version: 1.0 
 * @since:  08:33:14 20 Jan 2014
 */
public class ImageViewTouch extends ImageViewTouchBase {
	
	static final float					MIN_ZOOM	= 0.9f;
	protected ScaleGestureDetector	mScaleDetector;
	protected GestureDetector			mGestureDetector;
	protected int							mTouchSlop;
	protected float						mCurrentScaleFactor;
	protected float						mScaleFactor;
	protected int							mDoubleTapDirection;
	protected GestureListener			mGestureListener;
	protected ScaleListener				mScaleListener;
	BaseFragment parent;
	
	public ImageViewTouch( Context context, AttributeSet attrs )
	{
		super( context, attrs );
	}
	
	public ImageViewTouch(Context context) {
		// TODO Auto-generated constructor stub
		super( context);
	}

	@Override
	protected void init()
	{
		super.init();
		
		mTouchSlop = ViewConfiguration.getTouchSlop();
		mGestureListener = new GestureListener();
		mScaleListener = new ScaleListener();
		
		mScaleDetector = new ScaleGestureDetector( getContext(), mScaleListener );
		mGestureDetector = new GestureDetector( getContext(), mGestureListener, null, true );
		mCurrentScaleFactor = 1f;
		mDoubleTapDirection = 1;
	}
	public void setFragment(BaseFragment frag) {
		// TODO Auto-generated method stub
		parent = frag;
	}
	@Override
	public void setImageRotateBitmapReset( RotateBitmap bitmap, boolean reset )
	{
		super.setImageRotateBitmapReset( bitmap, reset );
		mScaleFactor = getMaxZoom() / 3;
	}
	
	@Override
	public boolean onTouchEvent( MotionEvent event )
	{
		mScaleDetector.onTouchEvent( event );
		if ( !mScaleDetector.isInProgress() ) mGestureDetector.onTouchEvent( event );
		int action = event.getAction();
		switch ( action & MotionEvent.ACTION_MASK ) {
			case MotionEvent.ACTION_UP:
				if ( getScale() < 1f ) {
					zoomTo( 1f, 50 );
					parent.isZooming = false;
				}else if (getScale() > 1f){
					parent.isZooming = true;
				}else{
					parent.isZooming = false;
				}
				break;
		}
		return true;
	}
	
	@Override
	public void onZoom( float scale )
	{
		super.onZoom( scale );
		if ( !mScaleDetector.isInProgress() ) mCurrentScaleFactor = scale;
	}
	
	
	public void onZoomIn(){
		mCurrentScaleFactor += 0.1f;
		zoomTo( 1f, 50 );
		invalidate();
	}
	protected float onDoubleTapPost( float scale, float maxZoom )
	{
		if ( mDoubleTapDirection == 1 ) {
			if ( ( scale + ( mScaleFactor * 2 ) ) <= maxZoom ) {
				return scale + mScaleFactor;
			} else {
				mDoubleTapDirection = -1;
				return maxZoom;
			}
		} else {
			mDoubleTapDirection = 1;
			return 1f;
		}
	}
	
	
	class GestureListener extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onDoubleTap( MotionEvent e )
		{
			float scale = getScale();
			float targetScale = scale;
			targetScale = onDoubleTapPost( scale, getMaxZoom() );
			targetScale = Math.min( getMaxZoom(), Math.max( targetScale, MIN_ZOOM ) );
			mCurrentScaleFactor = targetScale;
			zoomTo( targetScale, e.getX(), e.getY(), 200 );
			invalidate();
			return super.onDoubleTap( e );
		}
		
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			//parent.visibleHeader();
			return super.onSingleTapUp(e);
		}
		
		@Override
		public boolean onScroll( MotionEvent e1, MotionEvent e2, float distanceX, float distanceY )
		{
			if ( e1 == null || e2 == null ) return false;
			if ( e1.getPointerCount() > 1 || e2.getPointerCount() > 1 ) return false;
			if ( mScaleDetector.isInProgress() ) return false;
			if ( getScale() == 1f ) return false;
			scrollBy( -distanceX, -distanceY );
			invalidate();
			return super.onScroll( e1, e2, distanceX, distanceY );
		}
		
		@Override
		public boolean onFling( MotionEvent e1, MotionEvent e2, float velocityX, float velocityY )
		{
			if ( e1.getPointerCount() > 1 || e2.getPointerCount() > 1 ) return false;
			if ( mScaleDetector.isInProgress() ) return false;
			
			float diffX = e2.getX() - e1.getX();
			float diffY = e2.getY() - e1.getY();
			
			if ( Math.abs( velocityX ) > 800 || Math.abs( velocityY ) > 800 ) {
				scrollBy( diffX / 2, diffY / 2, 300 );
				invalidate();
			}
//			VTLog.e("PhucNT4","ImageViewTouch onFling --e1.getX" +e1.getX()+"--e2.getX--"+e2.getX()+"--velocityX--"+velocityX);
//			try {
//                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
//                    return false;
//                // right to left swipe
//                if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
//                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    parent.goRight();
//                } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
//                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
//                    parent.goLeft();
//                }
//            } catch (Exception e) {
//                // nothing
//            }
			return super.onFling( e1, e2, velocityX, velocityY );
		}
	}
	
	class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		
		@SuppressWarnings( "unused" )
		@Override
		public boolean onScale( ScaleGestureDetector detector )
		{
			float span = detector.getCurrentSpan() - detector.getPreviousSpan();
			float targetScale = mCurrentScaleFactor * detector.getScaleFactor();
			if ( true ) {
				targetScale = Math.min( getMaxZoom(), Math.max( targetScale, MIN_ZOOM ) );
				zoomTo( targetScale, detector.getFocusX(), detector.getFocusY() );
				mCurrentScaleFactor = Math.min( getMaxZoom(), Math.max( targetScale, MIN_ZOOM ) );
				mDoubleTapDirection = 1;
				invalidate();
				return true;
			}
			return false;
		}
	}


}
