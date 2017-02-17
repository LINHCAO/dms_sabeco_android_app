/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.customer;

import java.text.DecimalFormat;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Customer List Row PG
 * CustomerListRowPG.java
 * @version: 1.0 
 * @since:  08:30:58 20 Jan 2014
 */
public class CustomerListRowPG extends DMSTableRow implements OnClickListener {
	private TextView tvNumPG;
	private  TextView tvCusCodePG;
	private TextView tvCusNamePG;
	private TextView tvAddressPG;
	private TextView tvPathPG;
	private TextView tvDistancePG;
	private TextView tvVisitPG;
	public ImageView ivVisitCusPG;
	CustomerListItem item;


	public CustomerListRowPG(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_customer_list_row_pg);
		setListener(lis);
		tvNumPG = (TextView) this.findViewById(R.id.tvNumPG);
		tvCusCodePG = (TextView) this.findViewById(R.id.tvCusCodePG);
		tvCusNamePG = (TextView) this.findViewById(R.id.tvCusNamePG);
		tvAddressPG = (TextView) this.findViewById(R.id.tvAddPG);
		tvPathPG = (TextView) this.findViewById(R.id.tvPathPG);
		tvDistancePG = (TextView) this.findViewById(R.id.tvDistancePG);
		tvVisitPG = (TextView) this.findViewById(R.id.tvVisitPG);
		ivVisitCusPG = (ImageView) this.findViewById(R.id.visitCusPG);
		this.setOnClickListener(this);
		tvCusCodePG.setOnClickListener(this);
		ivVisitCusPG.setOnClickListener(this);
	}

	public void reRenderVisitStatus(){
		if (item != null){
			item.updateCustomerDistance();
			renderVisitStatus();
		}
	}

	private void renderVisitStatus(){
		ivVisitCusPG.setEnabled(true);

		if (item.cusDistance >= 1000) {
			float tempDistance = (float) item.cusDistance / 1000;
			DecimalFormat df = new DecimalFormat("0.00");
			String formater = df.format(tempDistance);
			tvDistancePG.setText("" + formater + " km");
		} else if (item.cusDistance >= 0) {
			tvDistancePG.setText("" + item.cusDistance + " m");
		} else {
			tvDistancePG.setText("");
		}

		if (item.isVisit()) {
			ivVisitCusPG.setImageResource(R.drawable.icon_check);
		} else {
			ivVisitCusPG.setImageResource(R.drawable.icon_door);
		}

		if (item.isTooFarShop) {
			// neu da co check ghe tham lan dau thi van hien thi
			if (item.isVisit()) {
				ivVisitCusPG.setVisibility(View.VISIBLE);
			} else {
				ivVisitCusPG.setEnabled(false);
				ivVisitCusPG.setImageResource(0);
			}
		}
	}
	public void render(int pos, CustomerListItem item, int curDay) {
		this.item = item;
		tvNumPG.setText("" + pos);
		if(!StringUtil.isNullOrEmpty(item.aCustomer.getCustomerCode())) {
			tvCusCodePG.setText(item.aCustomer.getCustomerCode().substring(0, 3));
		}else{
			tvCusCodePG.setText("");
		}
		tvCusNamePG.setText(item.aCustomer.getCustomerName());
		tvAddressPG.setText(item.aCustomer.getStreet());
		tvPathPG.setText(item.cusPlan);

		if (item.visitStatus == VISIT_STATUS.VISITED_CLOSED) {
			setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
		} else if (item.isTodayOrdered) {
			setBackGroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_ORDER_SUCC));
		} else {
			tvNumPG.setBackgroundResource(R.drawable.style_row_default);
			tvCusCodePG.setBackgroundResource(R.drawable.style_row_default);
			tvCusNamePG.setBackgroundResource(R.drawable.style_row_default);
			tvCusNamePG.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
			tvVisitPG.setBackgroundResource(R.drawable.style_row_default);
			tvVisitPG.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
			tvAddressPG.setBackgroundResource(R.drawable.style_row_default);
			tvAddressPG.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
			tvPathPG.setBackgroundResource(R.drawable.style_row_default);
			tvPathPG.setPadding(GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5), 0);
			tvDistancePG.setBackgroundResource(R.drawable.style_row_default);
		}

		tvVisitPG.setText(item.seqInDayPlan);
		//render status visit
		renderVisitStatus();
	}

	/**
	* set màu nền cho các view.
	* @author: dungdq3
	* @param: int color
	* @return: void
	*/
	private void setBackGroundColor(int color) {
		// TODO Auto-generated method stub
		tvNumPG.setBackgroundColor(color);
		tvCusCodePG.setBackgroundColor(color);
		tvCusNamePG.setBackgroundColor(color);
		tvVisitPG.setBackgroundColor(color);
		tvAddressPG.setBackgroundColor(color);
		tvPathPG.setBackgroundColor(color);
		tvDistancePG.setBackgroundColor(color);
		ivVisitCusPG.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
		if (v == tvCusCodePG) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_CUSTOMER_INFO, tvCusCodePG, item);
		} else if (v == ivVisitCusPG) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.VISIT_CUSTOMER, ivVisitCusPG, item);
		}
	}

}
