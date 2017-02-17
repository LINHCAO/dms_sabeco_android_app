/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.LogDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.SaleOrderViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 *  Row trong danh sach don hang
 *  @author: TruongHN
 *  @version: 1.0
 *  @since: 1.0
 */
public class OrderMngRow extends DMSTableRow implements OnClickListener {
	public static final int ACTION_CLICK_SDH = 0;
	public static final int ACTION_CLICK_MKH = 1;
	public static final int ACTION_CLICK_DELETE = 2;
	public static final int ACTION_CLICK_SELECT = 3;
	
	// textview STT
	private TextView tvSTT;
	// textview Ngay
	private TextView tvDay;
	// textview Khach hang
	public TextView tvMKH;
	// textview so don hang
	private TextView tvSDH;
	// textview thanh tien
	private TextView tvTT;
	private ImageView ivDelete;
//	public CheckBox cbSelect;
	private TextView tvDescript;
	// textview STT
	private TextView tvNVBH;

	private LinearLayout llAction;
	
	private SaleOrderViewDTO item;

	
	public OrderMngRow(Context context, ListOrderView listen) {
		super(context, GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES ? R.layout.mng_order_item : R.layout.mng_order_item_gsnpp,
				(GlobalInfo.getInstance().getIsShowPrice() == 1 ? null : new int[]{R.id.tvTT}));
		setListener(listen);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvDay = (TextView) this.findViewById(R.id.tvDay);
		tvMKH = (TextView) this.findViewById(R.id.tvMKH);
		tvMKH.setOnClickListener(this);
		tvSDH = (TextView) this.findViewById(R.id.tvSDH);
		tvSDH.setOnClickListener(this);
		tvTT = (TextView) this.findViewById(R.id.tvTT);
		tvDescript = (TextView) this.findViewById(R.id.tvDescript);
		ivDelete = (ImageView) this.findViewById(R.id.ivDelete);
		ivDelete.setOnClickListener(this);
		llAction = (LinearLayout) this.findViewById(R.id.llAction);

		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
			llAction.setVisibility(VISIBLE);
			tvMKH.setTextColor(getResources().getColor(R.color.COLOR_USER_NAME));
		} else {
			tvNVBH = (TextView) this.findViewById(R.id.tvNVBH);
			llAction.setVisibility(GONE);
			tvMKH.setTextColor(getResources().getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
		}
	}

	public void renderLayout(int position, SaleOrderViewDTO item) {
		this.item=item;
		tvSTT.setText("" + position);
		Date d= DateUtils.parseDateFromString(item.getOrderDate(), "dd/MM/yyyy HH:mm");
		String lastOrder= DateUtils.convertDateTimeWithFormat(d, "dd/MM/yyyy HH:mm");
		tvDay.setText(lastOrder);
		if (item.customer != null){
			tvMKH.setText(item.customer.customerCode + '-' +item.customer.customerName.trim());
		}else{
			tvMKH.setText("");	
		}
		tvMKH.setTag(item);
		tvSDH.setText(item.getOrderNumber());
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP ||
				GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSTINH) {
			tvNVBH.setText(item.staffCode + "-" + item.staffName);
		}
		
		// bo sung *
		// Chi hien thi cho don hang trong ngay
		if ( !StringUtil.isNullOrEmpty(item.priorityCode) && 
				(ApParamDTO.PRIORITY_IN_DAY.equals(item.priorityCode) || ApParamDTO.PRIORITY_NOW.equals(item.priorityCode)) && 
				DateUtils.compareWithNow(item.saleOrder.createDate, "yyyy-MM-dd") == 0){
			SpannableObject objTotal = new SpannableObject();
			objTotal.addSpan("*", ImageUtil.getColor(R.color.RED), android.graphics.Typeface.BOLD);
			tvSDH.append(objTotal.getSpan());
		}
		
		tvSDH.setTag(item); 
		ivDelete.setTag(item);
		if(tvTT != null){
			if(item.getTotal()>0.0){
				tvTT.setText(StringUtil.parseAmountMoney(item.getTotal()));
			}else{
				tvTT.setText("0");
			}
		}

		String synState = String.valueOf(item.saleOrder.synState);
		String destroyCode = String.valueOf(item.saleOrder.destroyCode);

		if (synState.equals(LogDTO.STATE_NEW)
				|| synState.equals(LogDTO.STATE_FAIL)) {// chờ gửi
			tvDescript.setText(StringUtil.getString(R.string.TEXT_ORDER_NOT_SEND));
		} else if ((item.saleOrder.approved == 0 && synState.equals(LogDTO.STATE_SUCCESS))) {// chờ xử lý
			tvDescript.setText(StringUtil.getString(R.string.TEXT_WAITING_PROCESS));
		} else if (item.saleOrder.approved == 1
				&& synState.equals(LogDTO.STATE_SUCCESS)) { // thanh cong
			tvDescript.setText(StringUtil.getString(R.string.TEXT_SUCCESS));
		} else if (item.saleOrder.approved == 2
				&& synState.equals(LogDTO.STATE_SUCCESS)) {// tu choi
			if (StringUtil.isNullOrEmpty(item.getDESCRIPTION())) {
				tvDescript.setText("Từ chối: ");
			} else {
				tvDescript.setText("Từ chối: " + item.getDESCRIPTION());
			}
			this.ivDelete.setVisibility(View.VISIBLE);
			// don hang tu choi, can cap nhat highlight cả row màu cam
			setBgColor();
		} else if (item.saleOrder.approved == 3
				&& synState.equals(LogDTO.STATE_SUCCESS)
				&& destroyCode.equals("AUTO_DEL")) {// qua ngay chua duyet
			tvDescript.setText(StringUtil.getString(R.string.TEXT_OVERDATE_NOT_APROVE));
		} else if (!synState.equals(LogDTO.STATE_SUCCESS)) {
			if (synState.equals(LogDTO.STATE_INVALID_TIME)) {// Sai thoi gian
				tvDescript.setText(StringUtil.getString(R.string.TEXT_ORDER_WRONG));
				this.ivDelete.setVisibility(View.VISIBLE);
				setBgOrderError();
			} else if (synState.equals(LogDTO.STATE_UNIQUE_CONTRAINTS)) {// Trùng khoa don hang
				tvDescript.setText(StringUtil.getString(R.string.TEXT_ORDER_WRONG));
				this.ivDelete.setVisibility(View.VISIBLE);
				setBgOrderError();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == tvSDH)
			listener.handleVinamilkTableRowEvent(ACTION_CLICK_SDH, this, item);
		else if (v == tvMKH) {
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				listener.handleVinamilkTableRowEvent(ACTION_CLICK_MKH, this, item);
			}
		}
		else if (v == ivDelete)
			listener.handleVinamilkTableRowEvent(ACTION_CLICK_DELETE, this, item);
		if (v == this && context != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}

	public void setBgColor() {
		for (int i = 0; i < this.getChildCount(); i++) {
			View v = this.getChildAt(i);
			v.setBackgroundResource(R.color.OGRANGE);
		}
		ivDelete.setBackgroundResource(R.color.OGRANGE);
	}
	
	/**
	 * 
	*  Update background cho don hang loi
	*  @author: Nguyen Thanh Dung
	*  @return: void
	*  @throws:
	 */
	private void setBgOrderError() {
		tvSDH.setTextColor( getResources().getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
		tvMKH.setTextColor( getResources().getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
		for (int i = 0; i < this.getChildCount(); i++) {
			View v = this.getChildAt(i);
			v.setBackgroundResource(R.color.RED);
		}
		ivDelete.setBackgroundResource(R.color.RED);
		//setBackgroundResource(R.color.RED);
	}
}
