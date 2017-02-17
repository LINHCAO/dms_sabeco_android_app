/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.Calendar;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;
/**
 * Row hien thi thong tin danh sach diem ban trong nha phan phoi
 * @author banghn
 * @since 1.1
 */
public class CustomerSaleListRow extends DMSTableRow implements OnClickListener {

	//stt
	private TextView tvSTT;
	//nhan vien ban hang
	private TextView tvNVBH;
	//ten khach hang
	private TextView tvCustomer;
	//dia chi khach hang
	private TextView tvAddress;
	//ngay udpate toa do
	private TextView tvDateUpdate;
	//so lan update toa do
	private TextView tvNumUpdate;
	//icon ban do, cho phep dat hang tu xa
	private ImageView ivMap;
	private ImageView ivAllowOrder;
	//ngay hien tại
	Calendar calendar = Calendar.getInstance();
	String sToday = DateUtils.defaultDateFormat.format(calendar.getTime());
	
	private CustomerListItem item;
	
	public CustomerSaleListRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_customer_sale_list_row);
		setListener(lis);
		this.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvNVBH = (TextView) this.findViewById(R.id.tvNVBH);
		tvCustomer = (TextView) this.findViewById(R.id.tvCustomer);
		tvAddress = (TextView) this.findViewById(R.id.tvAddress);
		tvDateUpdate = (TextView) this.findViewById(R.id.tvDateUpdate);
		tvNumUpdate = (TextView) this.findViewById(R.id.tvNumUpdate);
		ivMap = (ImageView) this.findViewById(R.id.ivMap);
		ivAllowOrder = (ImageView) this.findViewById(R.id.ivAllowOrder);
		tvCustomer.setOnClickListener(this);
		ivAllowOrder.setOnClickListener(this);
		ivMap.setOnClickListener(this);
		tvNumUpdate.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)context);
		}else if(v==ivAllowOrder){
			listener.handleVinamilkTableRowEvent(ActionEventConstant.ALLOW_ORDER, this, item);
		}else if(v==ivMap){
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW, this, item);
		}else if(v==tvNumUpdate){
			listener.handleVinamilkTableRowEvent(ActionEventConstant.NUM_UPDATE, this, item);
		} else if(v == tvCustomer) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_CUSTOMER_INFO, this, item);
		}
	}

	public void render(int pos, CustomerListItem item1) {
		this.item=item1;
		tvSTT.setText("" + pos);
		if(!StringUtil.isNullOrEmpty(item.staffSale.name) && !StringUtil.isNullOrEmpty(item.staffSale.staffCode)){
			SpannableObject obj = new SpannableObject();
			obj.addSpan(item.staffSale.name, ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			obj.addSpan("\n" + item.staffSale.staffCode, ImageUtil.getColor(R.color.COLOR_USER_NAME),
					android.graphics.Typeface.NORMAL);
			tvNVBH.setText(obj.getSpan());
		}
		if(!StringUtil.isNullOrEmpty(item.aCustomer.getCustomerCode())) {
			tvCustomer.setText(item.aCustomer.getCustomerCode().substring(0, 3) + " - " + item.aCustomer.getCustomerName());
		}else{
			tvCustomer.setText(item.aCustomer.getCustomerName());
		}
		tvAddress.setText(item.aCustomer.address);
		if(item.numUpdatePosition > 0 ){
			SpannableString content = new SpannableString("" + item.numUpdatePosition);
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
			content.setSpan(new ForegroundColorSpan(ImageUtil.getColor(R.color.COLOR_LINK)), 0, content.length(), 0);
			tvNumUpdate.setText(content);
		}else{
			tvNumUpdate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvNumUpdate.setText("" + item.numUpdatePosition);
		}
		tvDateUpdate.setText(item.lastUpdatePosition);
		if(item.aCustomer.getLat() > 0 && item.aCustomer.getLng() > 0){
			ivMap.setImageResource(R.drawable.icon_map);
			ivMap.setVisibility(View.VISIBLE);
		}else{
			//ivMap.setVisibility(View.INVISIBLE);
			ivMap.setImageResource(0);
			ivMap.setEnabled(false);
		}
		if(!StringUtil.isNullOrEmpty(item.exceptionOrderDate) && sToday.equals(item.exceptionOrderDate)){
			ivAllowOrder.setImageResource(R.drawable.icon_check);
		}else{
			ivAllowOrder.setImageResource(R.drawable.icon_check_gray);
		}
	}	
}
