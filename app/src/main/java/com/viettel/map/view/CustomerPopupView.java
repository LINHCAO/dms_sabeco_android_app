/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.map.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerListItem.VISIT_STATUS;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Popup thong tin khach hang tren ban do
 * 
 * @author banghn
 * @since 1.0
 */
@SuppressLint("ViewConstructor")
public class CustomerPopupView extends LinearLayout implements OnClickListener {
	public static final int POP_UP_CLOSE = 1001;
	public static final int GOTO_INFO = 1002;
	public static final int GOTO_ORDER = 1003;

	public TextView tvName; // tv display name
	public TextView tvAddress; // tv About
	public RelativeLayout llRoot; // Update UI for popup
	public int widthPopup;
	public ImageView ivDelete; // dong poupup
	private TextView tvPage; // khach hang thu ?/tong
	private TextView tvCellPhone; // di dong
	private TextView tvStablePhone;// co dinh
	public TextView tvInfo;// thong tin chi tiet
	public TextView tvOrder;// dat hang
	public Object userData;
	public String isNVGS = "";// 0: nvbh, 1: tnpg, 2: gs, 3: tbhv
	public OnEventControlListener lis;

	public CustomerPopupView(Context context, String info, String isNVGS) {
		super(context);
		this.isNVGS = isNVGS;
		loadControls();
	}

	/**
	 * Init control popup
	 * 
	 * @author banghn
	 */
	private void loadControls() {
		this.setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.layout_user_on_map, this);
		llRoot = (RelativeLayout) v.findViewById(R.id.llRoot);
		tvName = (TextView) v.findViewById(R.id.tvName);
		ivDelete = (ImageView) v.findViewById(R.id.ivDelete);
		tvPage = (TextView) v.findViewById(R.id.tvPage);
		tvAddress = (TextView) v.findViewById(R.id.tvAddress);
		tvCellPhone = (TextView) v.findViewById(R.id.tvCellPhone);
		tvStablePhone = (TextView) v.findViewById(R.id.tvStablePhone);
		tvInfo = (TextView) v.findViewById(R.id.tvInfo);
		tvOrder = (TextView) v.findViewById(R.id.tvOrder);
		widthPopup = getWidthOfPopup();
		if (isNVGS.equals(Constants.TYPE_GS) || isNVGS.equals(Constants.TYPE_GST)) {// nvgs: an chuc nang dat hang
			tvOrder.setVisibility(View.GONE);
			tvInfo.setVisibility(View.GONE);
			//an thu tu tren popup
			tvPage.setVisibility(View.GONE);
		}else if(isNVGS.equals(Constants.TYPE_TT)){
			tvOrder.setVisibility(View.GONE);
		}
		ivDelete.setOnClickListener(this);
		tvInfo.setOnClickListener(this);
		tvOrder.setOnClickListener(this); 
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// int mode = MeasureSpec.getMode(widthMeasureSpec);
		// int adjustedMaxWidth = (int) (MAX_WIDTH_DP * SCALE + 0.5f);
		// int adjustedWidth = Math.min(widthPopup, adjustedMaxWidth);
		// int adjustedWidthMeasureSpec =
		// MeasureSpec.makeMeasureSpec(adjustedWidth, mode);
		super.onMeasure(widthPopup, heightMeasureSpec);
	}

	public int getWidthOfPopup() {
		Rect bounds = new Rect();
		Paint textPaint = tvAddress.getPaint();
		textPaint.getTextBounds(tvAddress.getText().toString(), 0, tvAddress.getText().toString().length(), bounds);
		int width = bounds.width();
		return width + 40;
	}

	public void setListener(OnEventControlListener lis) {
		this.lis = lis;
	}

	/**
	 * Update thong tin popup
	 * 
	 * @author banghn
	 */
	public void updateInfo(CustomerListItem dto, int index, int isOrWithSelectedDay, int total) {
		SpannableObject objCellPhone = new SpannableObject();
		objCellPhone.addSpan("Di động:" + Constants.STR_SPACE, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		objCellPhone.addSpan(dto.aCustomer.getMobilephone(), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);

		SpannableObject objStablePhone = new SpannableObject();
		objStablePhone.addSpan("Cố định:" + Constants.STR_SPACE, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		objStablePhone.addSpan(dto.aCustomer.getPhone(), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);

		userData = index;
		tvName.setText(dto.aCustomer.getCustomerCode() + " - " + dto.aCustomer.getCustomerName());
		tvAddress.setText("Địa chỉ: " + dto.aCustomer.getStreet());
		tvCellPhone.setText(objCellPhone.getSpan());
		tvStablePhone.setText(objStablePhone.getSpan());
		String str = "";
		if (isOrWithSelectedDay == 0) {
			if (StringUtil.isNullOrEmpty(dto.seqInDayPlan)) {
				str = "0";
			} else {
				str = dto.seqInDayPlan;
			}
			tvPage.setText(str + "/" + total);
		} else {
//			tvPage.setText("");
			tvPage.setText("!/" + total);
			// str = "!";
		}
		if(dto.canOrderException()){
			tvOrder.setVisibility(View.VISIBLE);
		}else {
			if (dto.isOr == 0 && dto.isTooFarShop) {
				// neu da co check ghe tham lan dau thi van hien thi
				if (isVisit(dto)) {
					tvOrder.setVisibility(View.VISIBLE);
				} else {
					tvOrder.setVisibility(View.GONE);
				}
			} else {
				tvOrder.setVisibility(View.VISIBLE);
			}
		} 
		if (!isNVGS.equals(Constants.TYPE_NVBH)) {// neu la nvgs thi an di
			tvOrder.setVisibility(View.GONE);
		}
	}

	/**
	 * Da ghe tham khach hang chua
	 * 
	 * @author : BangHN since : 1.0
	 */
	public boolean isVisit(CustomerListItem item) {
		return item.visitStatus != VISIT_STATUS.NONE_VISIT;
	}

	@Override
	public void onClick(View v) {
		if (v == ivDelete) {
			lis.onEvent(POP_UP_CLOSE, ivDelete, null);
		} else if (v == tvInfo) {
			lis.onEvent(GOTO_INFO, tvInfo, userData);
		} else if (v == tvOrder) {
			lis.onEvent(GOTO_ORDER, tvOrder, userData);
		}
	}
}
