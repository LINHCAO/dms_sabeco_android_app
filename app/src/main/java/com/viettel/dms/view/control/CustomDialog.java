/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.control;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.viettel.sabeco.R;


/**
 *  Tao simple custom dialog su dung trong delete dialog
 *  @author: BangHN
 *  @version: 1.0
 *  @since: Jun 29, 2011
 */

public class CustomDialog extends Dialog {

    public CustomDialog(Context context, int theme) {
        super(context, theme);
    }

    public CustomDialog(Context context) {
        super(context);
    }

    /**
     *  @author: BangHN
     *  @version: 1.0
     *  @since: Jun 29, 2011
     */
    public static class Builder {

        private Context context;
        private View contentView;


        public Builder(Context context) {
            this.context = context;
        }

        
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        /**
         * Create the custom dialog
         */
        public CustomDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final CustomDialog dialog = new CustomDialog(context, 
            		R.style.Dialog);
            View layout = inflater.inflate(R.layout.dialog, null);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            if (contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.content))
                        .removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content))
                        .addView(contentView, 
                                new LayoutParams(
                                        LayoutParams.FILL_PARENT, 
                                        LayoutParams.WRAP_CONTENT));
            }
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
//            dialog.setContentView(layout);
            return dialog;
        }

    }

}
