/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.sabeco.R;

/**
 * Row van de theo doi khac phuc
 * CustomerFeedBackRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:08:24 14 Jan 2014
 */
public class CustomerFeedBackRow extends DMSTableRow implements OnClickListener {
	// so thu tu
	public TextView tvNumer;
	// noi dung 
	public TextView tvContent;
	// ngay nhac nho
	public TextView tvRemindDate;
	// ngay thuc hien
	public TextView tvDoneDate;
	// loai nhac nho
	public TextView tvType;
	// checkbox thuc hien van de
	public CheckBox cbDone;
	// textview thong bao khong co du lieu
//	public TextView tvNoResultInfo;
//	View view;
	// iv xoa van de
	public ImageView ivDetele;
//	private final Context context;
	//TableRow row;
//	VinamilkTableListener listener;
	//dto man hinh
	FeedBackDTO item;
	// layout me cua checkbox
	LinearLayout llCB;
	
//	public CustomerFeedBackRow(Context con){
//		super(con);
//		context=con;
//	}

	public CustomerFeedBackRow(Context context, CustomerFeedBackListView lis) {
		super(context, R.layout.layout_customer_feed_back_row);
		setListener(lis);
//		this(context);
//		listener = lis;
//		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		view = vi.inflate(R.layout.layout_customer_feed_back_row, this);
		tvNumer = (TextView) this.findViewById(R.id.tvSTT);
		tvContent = (TextView) this.findViewById(R.id.tvContent);
		tvRemindDate = (TextView) this.findViewById(R.id.tvRemindDate);
		tvDoneDate = (TextView) this.findViewById(R.id.tvOpDate);
		tvType = (TextView) this.findViewById(R.id.tvType);
		ivDetele = (ImageView) this.findViewById(R.id.ivDelete);
		cbDone = (CheckBox) this.findViewById(R.id.cbCheckNote);
//		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
		llCB=(LinearLayout) this.findViewById(R.id.llNote);
		setOnClickListener(this);
		ivDetele.setOnClickListener(this);
		cbDone.setOnClickListener(this);
	}

	/**
	 * 
	 * render layout
	 *
	 * @author: DungNT19
	 * @since: 1.0
	 * @return: void
	 * @throws:  
	 * @param pos
	 * @param item
	 */
	public void renderLayout(int pos, FeedBackDTO item) {
		this.item = item;
		tvNumer.setText("" + pos);
		tvContent.setText(item.content);
		tvType.setText(item.apParamName);
		if (item.remindDate != null) {
			tvRemindDate.setText(item.remindDate);
		} else {
			tvRemindDate.setText("");
		}
		if (!StringUtil.isNullOrEmpty(item.doneDate)) {
			tvDoneDate.setText(item.doneDate);
			cbDone.setChecked(true);
			cbDone.setEnabled(false);
			// Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			tvDoneDate.setText("");
			cbDone.setChecked(false);
		}

		if (item.staffId != item.createUserId || !StringUtil.isNullOrEmpty(item.doneDate)) {
			ivDetele.setImageDrawable(null);
			ivDetele.setEnabled(false);
		} else {
			ivDetele.setVisibility(View.VISIBLE);
		}

		if (!StringUtil.isNullOrEmpty(item.doneDate)) {
			setTextColor(ImageUtil.getColor(R.color.BLACK));
			if (item.doneDate.equals(DateUtils.getCurrentDate())) {
				setRowColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_ORDER_SUCC));
			} else {
				setRowColor(ImageUtil.getColor(R.color.GREY));
			}
		} else if (!StringUtil.isNullOrEmpty(item.remindDate)
				&& DateUtils.compareWithNow(item.remindDate, "dd/MM/yyyy") == -1) {
			setBackground(R.drawable.style_row_default);
			setTextColor(ImageUtil.getColor(R.color.RED));
		} else {
			setBackground(R.drawable.style_row_default);
		}
	}

	/**
	 * set background cho row
	 * 
	 * @author: TamPQ
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
		ivDetele.setBackgroundResource(styleRowDefault);
		//cbDone.setBackgroundResource(styleRowDefault);
		llCB.setBackgroundResource(styleRowDefault);
	}

	/**
	 * set mau cho text cua row
	 * 
	 * @author: TamPQ
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
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
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
		ivDetele.setBackgroundColor(color);
		//cbDone.setBackgroundColor(color);
		llCB.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
//		if (v == this && context != null) {
//			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
//		}
//		else 
		if (v == ivDetele) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.DELETE_FEEDBACK, ivDetele, item);
		} else if (v == cbDone) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.UPDATE_FEEDBACK, cbDone, item);
		}
	}
	
//	/**
//	 * Mo ta muc dich cua ham
//	 * 
//	 * @author: TamPQ
//	 * @return: voidvoid
//	 * @throws:
//	 */
//	public void renderNoResult() {
//		tvNumer.setVisibility(View.GONE);
//		tvContent.setVisibility(View.GONE);
//		tvType.setVisibility(View.GONE);
//		tvRemindDate.setVisibility(View.GONE);
//		tvDoneDate.setVisibility(View.GONE);
//		llDone.setVisibility(View.GONE);
//		ivDetele.setVisibility(View.GONE);
//		llDelete.setVisibility(View.GONE);
//		tvNoResultInfo.setVisibility(View.VISIBLE);
//		tvNoResultInfo.setText(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
//	}

}
