/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.CustomerFeedBackListView;
import com.viettel.sabeco.R;

/**
 * 
 * row danh sach gop y role GSNPP
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class GSNPPCustomerFeedBackRow extends DMSTableRow implements OnClickListener {

	public TextView tvNumer;// so thu tu
	public TextView tvContent;// noi dung
	public TextView tvRemindDate;// ngay nhac nho
	public TextView tvDoneDate;// ngay thuc hien
	public TextView tvType;// loai van de
	public TextView tvNoResultInfo;// thong bao
	FeedBackDTO item;
	private LinearLayout row;

	public GSNPPCustomerFeedBackRow(Context context, CustomerFeedBackListView lis) {
		super(context, R.layout.layout_gsnpp_customer_feedback_row);
		setListener(lis);
		row = (LinearLayout) this.findViewById(R.id.row);
		tvNumer = (TextView) this.findViewById(R.id.tvSTT);
		tvContent = (TextView) this.findViewById(R.id.tvContent);
		tvRemindDate = (TextView) this.findViewById(R.id.tvRemindDate);
		tvDoneDate = (TextView) this.findViewById(R.id.tvOpDate);
		tvType = (TextView) this.findViewById(R.id.tvType);
	}

	/**
	 * 
	 * render layout
	 * 
	 * @author: YenNTH
	 * @param pos
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int pos, FeedBackDTO item) {
		this.item = item;
		tvNumer.setText(Constants.STR_BLANK + pos);
		tvContent.setText(item.content);
		tvType.setText(item.apParamName);
		if (item.remindDate != null) {
			tvRemindDate.setText(item.remindDate);
		} else {
			tvRemindDate.setText(Constants.STR_BLANK);
		}
		if (!StringUtil.isNullOrEmpty(item.doneDate)) {
			tvDoneDate.setText(item.doneDate);
		} else {
			tvDoneDate.setText(Constants.STR_BLANK);
		}
		if(item.status == FeedBackDTO.FEEDBACK_STATUS_CREATE && !StringUtil.isNullOrEmpty(item.remindDate)
				&& DateUtils.compareWithNow(item.remindDate, "dd/MM/yyyy") == -1){
			setBackground(R.drawable.style_row_default);
			setTextColor(ImageUtil.getColor(R.color.RED));
		}else if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			setRowColor(ImageUtil.getColor(R.color.COLOR_BLUE_DONE_TODAY));
		} else if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE){
			setRowColor(ImageUtil.getColor(R.color.GREY));
		}else {
			setBackground(R.drawable.style_row_default);
		}
	}

	/**
	 * set mau cho background
	 * 
	 * @author: YenNTH
	 * @param styleRowDefault
	 * @return: voidvoid
	 * @throws:
	 */
	private void setBackground(int styleRowDefault) {
		tvNumer.setBackgroundResource(styleRowDefault);
		tvContent.setBackgroundResource(styleRowDefault);
		tvContent.setPadding(GlobalUtil.dip2Pixel(10), 0, GlobalUtil.dip2Pixel(10), 0);
		tvType.setBackgroundResource(styleRowDefault);
		tvRemindDate.setBackgroundResource(styleRowDefault);
		tvDoneDate.setBackgroundResource(styleRowDefault);
	}

	/**
	 * set mau cho text
	 * 
	 * @author: YenNTH
	 * @param color
	 * @return: voidvoid
	 * @throws:
	 */
	private void setTextColor(int color) {
		tvNumer.setTextColor(color);
		tvContent.setTextColor(color);
		tvType.setTextColor(color);
		tvRemindDate.setTextColor(color);
		tvDoneDate.setTextColor(color);
	}

	/**
	 * set mau cho row
	 * 
	 * @author: YenNTH
	 * @param color
	 * @return: voidvoid
	 * @throws:
	 */
	private void setRowColor(int color) {
		tvNumer.setBackgroundColor(color);
		tvContent.setBackgroundColor(color);
		tvType.setBackgroundColor(color);
		tvRemindDate.setBackgroundColor(color);
		tvDoneDate.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
		if (v == row) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_FOLLOW_PROBLEM_DETAIL, null, item);
		}else if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}
	


}
