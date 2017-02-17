/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;

import android.content.Context;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.viettel.sabeco.R;

/**
 * Tạo 1 view có thể có hiệu ứng khi ẩn hiện AnimationView.java
 * 
 * @author: duongdt3
 * @version: 1.0
 * @since: 10:57:15 9 Jan 2014
 */
public class AnimationLinearLayout extends LinearLayout {
	private Animation inAnimation;
	private Animation outAnimation;

	public AnimationLinearLayout(Context context) {
		super(context);
		initAnimation();
	}

	/**
	 * gán hiệu ứng mặc định
	 * @author: duongdt3
	 * @since: 11:18:42 9 Jan 2014
	 * @return: void
	 * @throws:  
	 */
	private void initAnimation() {
		//set animation when GONE 
		Animation outAnimation = (Animation) AnimationUtils.loadAnimation(getContext(), R.anim.slide_down_to_top);
		Animation inAnimation = (Animation) AnimationUtils.loadAnimation(getContext(), R.anim.slide_top_to_down);
		this.setInAnimation(inAnimation);
		this.setOutAnimation(outAnimation);
	}

	public void setInAnimation(Animation inAnimation) {
		this.inAnimation = inAnimation;
	}

	public void setOutAnimation(Animation outAnimation) {
		this.outAnimation = outAnimation;
	}

	@Override
	public void setVisibility(int visibility) {
		if (getVisibility() != visibility) {
			if (visibility == VISIBLE) {
				if (inAnimation != null){
					startAnimation(inAnimation);
				}
			} else if ((visibility == INVISIBLE) || (visibility == GONE)) {
				if (outAnimation != null){
					startAnimation(outAnimation);
				}
			}
		}

		super.setVisibility(visibility);
	}

	public void initAnimationFade(){
		Animation fadeIn = new AlphaAnimation(0, 1);
		fadeIn.setInterpolator(new DecelerateInterpolator()); //add this
		fadeIn.setStartOffset(600);
		fadeIn.setDuration(600);

		Animation fadeOut = new AlphaAnimation(1, 0);
		fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
		fadeOut.setDuration(600);

		this.setInAnimation(fadeIn);
		this.setOutAnimation(fadeOut);
	}


}
