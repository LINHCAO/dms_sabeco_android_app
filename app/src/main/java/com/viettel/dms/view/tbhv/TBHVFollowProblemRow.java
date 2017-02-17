/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
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
 * 
 * Row quan ly van de cua GST dung trong MH TBHVFolowProblemView
 *  role GST
 * @author: YenNTH
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVFollowProblemRow extends DMSTableRow implements OnClickListener {
	private TextView tvSTT;// So thu tu
	private TextView tvStaffCode;// staff code & staff name
	private TextView tvType;// Loai van de
	private TextView tvContent;// Noi dung van de
	private TextView tvRemindDate;// Ngày nhắc nhở
	private TextView tvCreateDate;// Ngay tao van de
	private CheckBox cbNote;// Duyet van de
	private ImageView ivDelete;// xoa van de
	private LinearLayout rlIcon;//rl chua icon
	private TextView tvStatus;// trang thai

	TBHVFollowProblemItemDTO data;

	public TBHVFollowProblemRow(Context context,VinamilkTableListener listener, boolean isHeader) {
		super(context, R.layout.layout_tbhv_follow_problem_row);
		setListener(listener);
		this.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvStaffCode = (TextView) this.findViewById(R.id.tvStaffCode);
		tvType = (TextView) this.findViewById(R.id.tvType);
		tvContent = (TextView) this.findViewById(R.id.tvContent);
		tvRemindDate = (TextView) this.findViewById(R.id.tvRemindDate);
		tvCreateDate = (TextView) this.findViewById(R.id.tvCreateDate);
		cbNote = (CheckBox) this.findViewById(R.id.cbNote);
		ivDelete = (ImageView) this.findViewById(R.id.ivDelete);
		rlIcon = (LinearLayout) this.findViewById(R.id.rlIcon);
		ivDelete.setOnClickListener(this);
		tvStatus = (TextView) this.findViewById(R.id.tvStatus);
		if(isHeader){
			showRowSum("OK", cbNote, ivDelete);
			removeImage(ivDelete);
			removeImage(cbNote);
		}
	}

	/**
	 * 
	 * render du lieu
	 * 
	 * @author: YenNTH
	 * @param item
	 * @param position
	 * @return: void
	 * @throws:
	 */
	public void render(TBHVFollowProblemItemDTO item, int position) {
		this.data = item;
		tvSTT.setText(Constants.STR_BLANK + position);
		SpannableObject obj = new SpannableObject();
		obj.addSpan(item.staffName,ImageUtil.getColor(R.color.COLOR_USER_NAME),android.graphics.Typeface.NORMAL);
		if (item.chanelTypeDTO.objectType == UserDTO.TYPE_GSNPP) {
			tvStaffCode.setText(obj.getSpan() + " " + StringUtil.getString(R.string.TEXT_GS));
		} else {
			tvStaffCode.setText(obj.getSpan() + " " + StringUtil.getString(R.string.TEXT_TT));
		}
		tvType.setText(item.type);
		tvContent.setText(item.content.trim());
		tvCreateDate.setText(item.createDate);
		tvRemindDate.setText(item.remindDate);
		if (item.status == TBHVFollowProblemItemDTO.STATUS_NEW) {
			cbNote.setVisibility(View.GONE);
			ivDelete.setVisibility(View.VISIBLE);
			//da qua ngay nhac
			if (DateUtils.isCompareWithToDate(item.remindDate) < 0) {
				setTextColorRow(Color.RED, item);				
			}
			 tvStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_CREATE_NEW));
		} else if (item.status == TBHVFollowProblemItemDTO.STATUS_DONE) {
			cbNote.setVisibility(View.VISIBLE);
			ivDelete.setVisibility(View.GONE);
			cbNote.setChecked(false);
			cbNote.setEnabled(true);
			//ngay thuc hien
			if (DateUtils.isCompareWithToDate(item.doneDate) == 0) {
				// ngay hien tai
				setBackgroundColorRow(ImageUtil.getColor(R.color.COLOR_BLUE_DONE_TODAY));
			}
			 tvStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_DONE));
		} else if (item.status == TBHVFollowProblemItemDTO.STATUS_APPROVE) {
			cbNote.setVisibility(View.VISIBLE);
			ivDelete.setVisibility(View.GONE);
			cbNote.setChecked(true);
			cbNote.setEnabled(false);
			setBackgroundColorRow(ImageUtil.getColor(R.color.COLOR_GRAY_DONE));
			tvStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_APPROVED));
		}
		cbNote.setOnClickListener(this);
	}

	/**
	 * Set mau cho dong text
	 * 
	 * @param color
	 * @param item
	 */
	private void setTextColorRow(int color, TBHVFollowProblemItemDTO item) {
		tvSTT.setTextColor(color);
		SpannableObject obj = new SpannableObject();
		obj.addSpan(item.staffName,color,android.graphics.Typeface.NORMAL);
		if (item.chanelTypeDTO.objectType == UserDTO.TYPE_GSNPP) {
			tvStaffCode.setText(obj.getSpan() + " " + StringUtil.getString(R.string.TEXT_GS));
		} else {
			tvStaffCode.setText(obj.getSpan() + " " + StringUtil.getString(R.string.TEXT_TT));
		}
		tvStaffCode.setTextColor(color);
		tvType.setTextColor(color);
		tvContent.setTextColor(color);
		tvRemindDate.setTextColor(color);
		tvCreateDate.setTextColor(color);
		tvStatus.setTextColor(color);
	}

	/**
	 * Set mau background tren row
	 * 
	 * @author YenNTH
	 * @param color
	 */
	private void setBackgroundColorRow(int color) {
		tvSTT.setBackgroundColor(color);
		tvStaffCode.setBackgroundColor(color);
		tvType.setBackgroundColor(color);
		tvContent.setBackgroundColor(color);
		tvRemindDate.setBackgroundColor(color);
		tvCreateDate.setBackgroundColor(color);
		rlIcon.setBackgroundColor(color);
		tvStatus.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
		if (v == cbNote) {
			cbNote.setChecked(false);
			cbNote.setEnabled(false);
			listener.handleVinamilkTableRowEvent(ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE, null,this.data);
		} else if (v == ivDelete) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.DELETE_TBHV_FOLLOW_PROBLEM_DONE, null,this.data);
		} else if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
			if(this.data != null){
				listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_FOLLOW_PROBLEM_DETAIL, null,this.data);
			}
		}
	}

	/**
	 * cap nhat duyet van de
	 * 
	 * @author: YenNTH
	 * @param success
	 * @return: void
	 * @throws:
	 */
	public void updateCheckBoxDone(boolean success) {
		cbNote.setChecked(success);
		cbNote.setEnabled(!success);
	}

}
