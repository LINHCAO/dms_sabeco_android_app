/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.image.zoom;

/**
 * Cubic
 * Cubic.java
 * @version: 1.0 
 * @since:  08:33:32 20 Jan 2014
 */
public class Cubic {
	
	public static float easeOut( float time, float start, float end, float duration )
	{
		return end * ( ( time = time / duration - 1 ) * time * time + 1 ) + start;
	}
}
