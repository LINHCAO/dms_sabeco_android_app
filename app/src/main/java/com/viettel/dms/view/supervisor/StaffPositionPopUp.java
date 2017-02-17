/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * 
 * Hien thi popup thong tin vi tri nhan vien
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class StaffPositionPopUp extends LinearLayout implements OnClickListener {
	public static final int POPUP_CLOSE = 1001;
	public static final int STAFF_POS = 1002;
	public RelativeLayout llRoot;
	public TextView tvCellPhone;
	public TextView tvInfo;
	public TextView tvUpdateTime;
	public TextView tvVisiting;
	public TextView tvName;
	public ImageView ivDelete;
	public int userIndex;
	public ImageView seperator;
	OnEventControlListener lis;

	public StaffPositionPopUp(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_staff_pos_pop_up_on_map, this);
		tvUpdateTime = (TextView) v.findViewById(R.id.tvUpdateTime);
		tvUpdateTime.setVisibility(View.GONE);
		tvVisiting = (TextView) v.findViewById(R.id.tvVisiting);
		tvVisiting.setVisibility(View.GONE);
		tvName = (TextView) v.findViewById(R.id.tvName);
		ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
		ivDelete.setOnClickListener(this);
		llRoot = (RelativeLayout) v.findViewById(R.id.llRoot);
		tvCellPhone = (TextView) v.findViewById(R.id.tvCellPhone);
		tvInfo = (TextView) v.findViewById(R.id.tvInfo);
		tvInfo.setOnClickListener(this);
		seperator = (ImageView) v.findViewById(R.id.seperator);
	}

	public void setListener(OnEventControlListener lis) {
		this.lis = lis;
	}

	public int getWidthOfPopup() {
		Rect bounds_1 = new Rect();
		Rect bounds_2 = new Rect();
		Rect bounds_3 = new Rect();
		Rect bounds_4 = new Rect();
		Rect bounds_5 = new Rect();
		int maxValue;
		Paint textPaint = tvName.getPaint();
		textPaint.getTextBounds(tvName.getText().toString(), 0, tvName.getText().toString().length(), bounds_1);
		textPaint = tvCellPhone.getPaint();
		textPaint.getTextBounds(tvCellPhone.getText().toString(), 0, tvCellPhone.getText().toString().length(),
				bounds_2);
		maxValue = Math.max(bounds_1.width() + ivDelete.getWidth() + 40, bounds_2.width() + 30);
		textPaint = tvUpdateTime.getPaint();
		textPaint.getTextBounds(tvUpdateTime.getText().toString(), 0, tvUpdateTime.getText().toString().length(),
				bounds_3);
		maxValue = Math.max(maxValue, bounds_3.width() + 30);
		textPaint = tvVisiting.getPaint();
		textPaint.getTextBounds(tvVisiting.getText().toString(), 0, tvVisiting.getText().toString().length(), bounds_4);
		maxValue = Math.max(maxValue, bounds_4.width() + 30);
		textPaint = tvInfo.getPaint();
		textPaint.getTextBounds(tvInfo.getText().toString(), 0, tvInfo.getText().toString().length(), bounds_5);
		maxValue = Math.max(maxValue, bounds_5.width() + 30);

		return maxValue;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int mode = MeasureSpec.getMode(widthMeasureSpec);
		// int adjustedMaxWidth = (int) (MAX_WIDTH_DP * SCALE + 0.5f);
		// int adjustedWidth = Math.min(getWidthOfPopup(), adjustedMaxWidth);
		// int adjustedWidthMeasureSpec =
		// MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
		super.onMeasure(getWidthOfPopup(), heightMeasureSpec);
	}

	/**
	 * Update thong tin popup role GSBH
	 * 
	 * @author TamPQ
	 */
	public void updateInfo(boolean isGsnpp, boolean haveRouting, String name, String mobile, String updateTime,
			String visiting, int index) {
		SpannableObject objCellPhone = new SpannableObject();
		objCellPhone.addSpan(StringUtil.getString(R.string.TEXT_CUSTOMER_PHONE) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		objCellPhone.addSpan(mobile, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		tvName.setText(name);
		if (isGsnpp) {
			tvName.setTextColor(ImageUtil.getColor(R.color.RED));
		} else {
			tvName.setTextColor(ImageUtil.getColor(R.color.RED));
		}

		if (!haveRouting) {
			tvInfo.setVisibility(View.GONE);
			seperator.setVisibility(View.GONE);
		} else {
			tvInfo.setVisibility(View.VISIBLE);
			seperator.setVisibility(View.VISIBLE);
		}

		tvCellPhone.setText(objCellPhone.getSpan());
		if (!StringUtil.isNullOrEmpty(updateTime)) {
			SpannableObject objUdpateTime = new SpannableObject();
			objUdpateTime.addSpan(StringUtil.getString(R.string.TEXT_UDPATE_TIME) + ":" + Constants.STR_SPACE,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
			objUdpateTime.addSpan(updateTime, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
			tvUpdateTime.setVisibility(View.VISIBLE);
			tvUpdateTime.setText(objUdpateTime.getSpan());
		}
		if (!StringUtil.isNullOrEmpty(visiting)) {
			SpannableObject objVisiting = new SpannableObject();
			objVisiting.addSpan(StringUtil.getString(R.string.TEXT_VISITING) + ":" + Constants.STR_SPACE,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
			objVisiting.addSpan(visiting, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
			tvVisiting.setVisibility(View.VISIBLE);
			tvVisiting.setText(objVisiting.getSpan());
		} else {
			tvVisiting.setVisibility(View.GONE);
		}
		userIndex = index;

		llRoot.setLayoutParams(new LayoutParams(getWidthOfPopup(), LayoutParams.WRAP_CONTENT));
	}

	/**
	 * 
	 * Cap nhat thong tin cho popup role GST
	 * 
	 * @author: YenNTH
	 * @param isGsnpp
	 * @param haveRouting
	 * @param name
	 * @param mobile
	 * @param updateTime
	 * @param visiting
	 * @param index
	 * @return: void
	 * @throws:
	 */
	public void updateInfoTBHV(int from, boolean haveRouting, String name, String mobile, String updateTime,
			String visiting, int index) {
		SpannableObject objCellPhone = new SpannableObject();
		objCellPhone.addSpan(StringUtil.getString(R.string.TEXT_CUSTOMER_PHONE) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		objCellPhone.addSpan(mobile, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		tvName.setText(name);
		if (from == 5) {
			tvName.setTextColor(ImageUtil.getColor(R.color.RED));
		} else if(from == 1){
			tvName.setTextColor(ImageUtil.getColor(R.color.GREEN));
		}else {
			tvName.setTextColor(ImageUtil.getColor(R.color.BLUE));
		}

		if (!haveRouting) {
			tvInfo.setVisibility(View.GONE);
			seperator.setVisibility(View.GONE);
		} else {
			tvInfo.setVisibility(View.VISIBLE);
			seperator.setVisibility(View.VISIBLE);
		}

		tvCellPhone.setText(objCellPhone.getSpan());
		if (!StringUtil.isNullOrEmpty(updateTime)) {
			SpannableObject objUdpateTime = new SpannableObject();
			objUdpateTime.addSpan(StringUtil.getString(R.string.TEXT_UDPATE_TIME) + ":" + Constants.STR_SPACE,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
			objUdpateTime.addSpan(updateTime, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
			tvUpdateTime.setVisibility(View.VISIBLE);
			tvUpdateTime.setText(objUdpateTime.getSpan());
		}
		if (!StringUtil.isNullOrEmpty(visiting)) {
			SpannableObject objVisiting = new SpannableObject();
			objVisiting.addSpan(StringUtil.getString(R.string.TEXT_VISITING) + ":" + Constants.STR_SPACE,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
			objVisiting.addSpan(visiting, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
			tvVisiting.setVisibility(View.VISIBLE);
			tvVisiting.setText(objVisiting.getSpan());
		} else {
			tvVisiting.setVisibility(View.GONE);
		}
		userIndex = index;

		llRoot.setLayoutParams(new LayoutParams(getWidthOfPopup(), LayoutParams.WRAP_CONTENT));
	}

	@Override
	public void onClick(View v) {
		if (v == ivDelete) {
			lis.onEvent(POPUP_CLOSE, ivDelete, null);
		} else if (v == tvInfo) {
			lis.onEvent(STAFF_POS, tvInfo, userIndex);
		}

	}

}
