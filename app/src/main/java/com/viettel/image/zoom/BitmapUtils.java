/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.image.zoom;



import android.graphics.Bitmap;

/**
 * 
 * Bitmap utils
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class BitmapUtils {
	
	public static Bitmap resizeBitmap( Bitmap input, int destWidth, int destHeight )
	{
		int srcWidth = input.getWidth();
		int srcHeight = input.getHeight();
		boolean needsResize = false;
		float p;
		if ( srcWidth > destWidth || srcHeight > destHeight ) {
			needsResize = true;
			if ( srcWidth > srcHeight && srcWidth > destWidth ) {
				p = (float)destWidth / (float)srcWidth;
				destHeight = (int)( srcHeight * p );
			} else {
				p = (float)destHeight / (float)srcHeight;
				destWidth = (int)( srcWidth * p );
			}
		} else {
			destWidth = srcWidth;
			destHeight = srcHeight;
		}
		if ( needsResize ) {
			Bitmap output = Bitmap.createScaledBitmap( input, destWidth, destHeight, true );
			return output;
		} else {
			return input;
		}
	}
}
