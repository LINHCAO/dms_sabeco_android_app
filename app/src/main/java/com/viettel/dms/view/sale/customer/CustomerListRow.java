/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.CustomerListItem.VISIT_STATUS;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Customer list row
 * CustomerListRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:08:47 14 Jan 2014
 */
public class CustomerListRow extends DMSTableRow implements OnClickListener {
//	private final Context context;
//	View view;
	//so thu tu
	TextView tvNum;
	// ma khach hang 
	public TextView tvCusCode;
	// ten khach hang
	TextView tvCusName;
	// dia chi khach hang
	TextView tvAddress;
	// tuyen
	TextView tvPath;
	// khoanh cach tu nv den khach hang
	TextView tvDistance;
	// thu tu ghe tham
	TextView tvVisit;
	// image the hien thang thai ghe tham
	public ImageView ivVisitCus;
	// Dto row
	CustomerListItem item;
	
	public CustomerListRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_customer_list_row);
		setListener(lis);
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		tvCusCode = (TextView) this.findViewById(R.id.tvCusCode);
		tvCusName = (TextView) this.findViewById(R.id.tvCusName);
		tvAddress = (TextView) this.findViewById(R.id.tvAdd);
		tvPath = (TextView) this.findViewById(R.id.tvPath);
		tvDistance = (TextView) this.findViewById(R.id.tvDistance);
		tvVisit = (TextView) this.findViewById(R.id.tvVisit);
		ivVisitCus = (ImageView) this.findViewById(R.id.visitCus);
		setOnClickListener(this);
		tvCusCode.setOnClickListener(this);
		ivVisitCus.setOnClickListener(this);
	}
	/**
	 * 
	 * Render layout row
	 *
	 * @author: DungNT19
	 * @since: 1.0
	 * @return: void
	 * @throws:  
	 * @param pos
	 * @param item
	 * @param curDay
	 */
	public void render(int pos, CustomerListItem item, int curDay) {
		this.item = item;
		tvNum.setText(String.valueOf(pos));
		if(!StringUtil.isNullOrEmpty(item.aCustomer.getCustomerCode())) {
			tvCusCode.setText(Constants.STR_BLANK + item.aCustomer.getCustomerCode().substring(0, 3));
		} else {
			tvCusCode.setText(Constants.STR_BLANK);
		}
		tvCusName.setText(Constants.STR_BLANK+item.aCustomer.getCustomerName());
		tvAddress.setText(Constants.STR_BLANK+item.aCustomer.getStreet());
		tvPath.setText(item.cusPlan);

		tvVisit.setText(item.seqInDayPlan);

		renderVisitStatus();
	}


	public void reRenderVisitStatus() {
		if (this.item != null){
			this.item.updateCustomerDistance();
			renderVisitStatus();
		}
	}

	private void renderVisitStatus() {
		ivVisitCus.setEnabled(true);

		if (item.cusDistance >= 1000) {
			float tempDistance = (float) item.cusDistance / 1000;
			DecimalFormat df = new DecimalFormat("0.00");
			String formater = df.format(tempDistance);
			tvDistance.setText("" + formater + " km");
		} else if (item.cusDistance >= 0) {
			tvDistance.setText("" + item.cusDistance + " m");
		} else {
			tvDistance.setText("");
		}

		if (item.isVisit()) {
			ivVisitCus.setImageResource(R.drawable.icon_check);
		} else {
			ivVisitCus.setImageResource(R.drawable.icon_door);
		}

		if (item.visitStatus == VISIT_STATUS.VISITED_CLOSED) {
			setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
		} else if (item.isTodayOrdered) {
			setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_ORDER_SUCC));
		} else {
			tvNum.setBackgroundResource(R.drawable.style_row_default);
			tvCusCode.setBackgroundResource(R.drawable.style_row_default);
			tvCusName.setBackgroundResource(R.drawable.style_row_default);
			tvCusName.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
			tvVisit.setBackgroundResource(R.drawable.style_row_default);
			tvVisit.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
			tvAddress.setBackgroundResource(R.drawable.style_row_default);
			tvAddress.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
			tvPath.setBackgroundResource(R.drawable.style_row_default);
			tvPath.setPadding(GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5), 0);
			tvDistance.setBackgroundResource(R.drawable.style_row_default);
		}

		if(item.canOrderException()){
			ivVisitCus.setVisibility(View.VISIBLE);
		}else {
			if (item.isOr == 0 && item.isTooFarShop) {
				// neu da co check ghe tham lan dau thi van hien thi
				if (item.isVisit()) {
					ivVisitCus.setVisibility(View.VISIBLE);
				} else {
					ivVisitCus.setEnabled(false);
					ivVisitCus.setImageResource(0);
				}
			} else if(item.isOr==1) {
				if(item.aCustomer.lat > 0 && item.aCustomer.lng > 0){
					ivVisitCus.setVisibility(View.VISIBLE);
				}else{
					ivVisitCus.setEnabled(false);
					ivVisitCus.setImageResource(0);
				}
			}
		}
	}

	/**
	* set màu nền cho các view.
	* @author: dungdq3
	* @param: int color
	* @return: void
	*/
	private void setBackGroundColor(int color) {
		// TODO Auto-generated method stub
		tvNum.setBackgroundColor(color);
		tvCusCode.setBackgroundColor(color);
		tvCusName.setBackgroundColor(color);
		tvVisit.setBackgroundColor(color);
		tvAddress.setBackgroundColor(color);
		tvPath.setBackgroundColor(color);
		tvDistance.setBackgroundColor(color);
		ivVisitCus.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
		if (v == tvCusCode) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_CUSTOMER_INFO, tvCusCode, item);
		} else if (v == ivVisitCus) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.VISIT_CUSTOMER, ivVisitCus, item);
		}
	}
}
