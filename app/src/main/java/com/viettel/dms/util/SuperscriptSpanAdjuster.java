/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.util;

import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 *  Mo ta muc dich cua lop (interface)
 *  @author: Nguyen Thanh Dung
 *  @version: 1.1
 *  @since: 1.0
 */

public class SuperscriptSpanAdjuster extends MetricAffectingSpan {
    double ratio = 0.5;

    public SuperscriptSpanAdjuster() {
    }

    public SuperscriptSpanAdjuster(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.baselineShift += (int) (paint.ascent() * ratio);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        paint.baselineShift += (int) (paint.ascent() * ratio);
    }
}
