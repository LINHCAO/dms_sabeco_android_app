/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.graphics.Paint;
import android.text.SpannableString;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.SuperscriptSpanAdjuster;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * row theo doi va khac phuc (nvbh, tttt)
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class NoteListRow extends DMSTableRow implements OnClickListener {
	public TextView tvNumer;// stt
	public TextView tvContent;// noi dung
	public TextView tvRemindDate;// ngay nhac nho
	public TextView tvDoneDate;// ngay thuc hien
	public CheckBox cbDone;// checkbox thuc hien
	public LinearLayout rlNote;
	public TextView tvMKH;// ma kh
	public TextView tvType;// loai van de
	int action;
	FeedBackDTO dto;// dto theo doi khac phuc

	public NoteListRow(Context context, VinamilkTableListener listener, int action,
			FeedBackDTO dto) {
		super(context, R.layout.layout_note_list_row);
		setListener(listener);
		this.action = action;
		this.dto = dto;
		this.setOnClickListener(this);
		initView();
	}
	/**
	 * initView
	 * @author: yennth16
	 * @since: 11:51:07 23-04-2015
	 * @return: void
	 * @throws:
	 */
	public void initView() {
		tvNumer = (TextView) this.findViewById(R.id.tvSTT);
		tvContent = (TextView) this.findViewById(R.id.tvContent);
		tvRemindDate = (TextView) this.findViewById(R.id.tvRemindDate);
		tvDoneDate = (TextView) this.findViewById(R.id.tvOpDate);
		cbDone = (CheckBox) this.findViewById(R.id.cbNote);
		rlNote = (LinearLayout) this.findViewById(R.id.rlNote);
		tvMKH = (TextView) this.findViewById(R.id.tvMKH);
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
	public void render(int pos, FeedBackDTO item) {
		tvNumer.setText(Constants.STR_BLANK + pos);
		tvType.setText(item.apParamName);
		tvContent.setText(item.content);
		if (item.customerCode != null && item.customerCode.length() > 3) {
			tvMKH.setText(Constants.STR_BLANK
					+ item.customerCode.substring(0, 3));
			if (item.createUserId == Long.valueOf(GlobalInfo.getInstance()
					.getProfile().getUserData().staffOwnerId)) {
				tvMKH.setTextColor(ImageUtil.getColor(R.color.RED));
				SpannableString contentAmount = new SpannableString(
						tvMKH.getText() + "*");
				contentAmount.setSpan(new SuperscriptSpanAdjuster(1.5 / 5.0),
						contentAmount.length() - 1, contentAmount.length(),
						SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
				tvMKH.append(contentAmount);
			}
			tvMKH.append(" - " + item.customerName);
		}
		if (item.remindDate != null) {
			tvRemindDate.setText(item.remindDate);
		} else {
			tvRemindDate.setText(Constants.STR_BLANK);
		}

		if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE
				|| item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
			tvDoneDate.setText(item.doneDate);
			cbDone.setChecked(true);
			cbDone.setEnabled(false);
			tvContent.setPaintFlags(tvContent.getPaintFlags()
					| Paint.STRIKE_THRU_TEXT_FLAG);
		} else {
			tvDoneDate.setText(Constants.STR_BLANK);
			cbDone.setChecked(false);
		}

		// neu la type cua NVGS thi to do
		if (item.createUserId == GlobalInfo.getInstance().getProfile()
				.getUserData().staffOwnerId) {
			if (!StringUtil.isNullOrEmpty(item.customerCode)) {
				tvMKH.setText(Constants.STR_BLANK
						+ item.customerCode.substring(0, 3) + "-"
						+ item.customerName + "*");
				tvMKH.setTextColor(ImageUtil.getColor(R.color.RED));
			}
		} else {
			if (!StringUtil.isNullOrEmpty(item.customerCode))
				tvMKH.setText(Constants.STR_BLANK
						+ item.customerCode.substring(0, 3) + "-"
						+ item.customerName);
		}

		// type =2,3
		if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE
				&& DateUtils.compareWithNow(item.doneDate, "dd/MM/yyyy") == 0) {
			setColorBackgroundForRow(ImageUtil
					.getColor(R.color.COLOR_BLUE_DONE_TODAY));
		} else if ((item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE || item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE)) {
			setColorBackgroundForRow(ImageUtil
					.getColor(R.color.COLOR_GRAY_DONE));
		} else if (item.status == FeedBackDTO.FEEDBACK_STATUS_CREATE
				&& DateUtils.compareWithNow(item.remindDate, "dd/MM/yyyy") == -1) {
			setColorForTextRow(ImageUtil.getColor(R.color.RED));
		}
	}

	/**
	 * 
	 * set color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 */
	public void setColorForTextRow(int color) {
		tvNumer.setTextColor(color);
		tvContent.setTextColor(color);
		tvRemindDate.setTextColor(color);
		tvDoneDate.setTextColor(color);
		tvRemindDate.setTextColor(color);
		tvMKH.setTextColor(color);
		tvType.setTextColor(color);
	}

	/**
	 * 
	 * set background color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 */
	public void setColorBackgroundForRow(int color) {
		tvNumer.setBackgroundColor(color);
		tvContent.setBackgroundColor(color);
		tvRemindDate.setBackgroundColor(color);
		tvDoneDate.setBackgroundColor(color);
		tvRemindDate.setBackgroundColor(color);
		cbDone.setBackgroundColor(color);
		tvMKH.setBackgroundColor(color);
		tvType.setBackgroundColor(color);
		cbDone.setBackgroundColor(color);
		rlNote.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
			if (listener != null) {
				if(dto != null){
					listener.handleVinamilkTableRowEvent(action, v, dto);
				}
			}
		}
	}

}
