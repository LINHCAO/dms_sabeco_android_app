/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.FollowProblemItemDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * row theo doi khac phuc van de (GSBH)
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class FollowProblemRow extends DMSTableRow implements OnClickListener {
	private TextView tvSTT;// so thu tu
	private TextView tvStaffCode;// ma nv
	private TextView tvKH;// ten kh
	private TextView tvType;// loai van de
	private TextView tvContent;// noi dung
	private TextView tvStatus;// trang thai
	private TextView tvCreateDate;// ngay tao
	private CheckBox cbNote;// check box thuc hien
	public LinearLayout rlNote;


	FollowProblemItemDTO data;

	public FollowProblemRow(Context context, FollowProblemView lis) {
		super(context, R.layout.layout_supervisor_follow_problem_row);
		setListener(lis);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvStaffCode = (TextView) this.findViewById(R.id.tvStaffCode);
		tvKH = (TextView) this.findViewById(R.id.tvKH);
		tvType = (TextView) this.findViewById(R.id.tvType);
		tvContent = (TextView) this.findViewById(R.id.tvContent);
		tvStatus = (TextView) this.findViewById(R.id.tvStatus);
		tvCreateDate = (TextView) this.findViewById(R.id.tvCreateDate);
		cbNote = (CheckBox) this.findViewById(R.id.cbNote);
		rlNote = (LinearLayout) this.findViewById(R.id.rlNote);
		this.setOnClickListener(this);
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
	public void render(FollowProblemItemDTO item, int position) {
		this.data = item;
		tvSTT.setText(Constants.STR_BLANK + position);
		if (!StringUtil.isNullOrEmpty(item.staffName) && !StringUtil.isNullOrEmpty(item.staffCode)) {
			SpannableObject obj = new SpannableObject();
			obj.addSpan(item.staffCode, ImageUtil.getColor(R.color.COLOR_USER_NAME),android.graphics.Typeface.NORMAL);
			obj.addSpan("\n" + item.staffName, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
			tvStaffCode.setText(obj.getSpan()); 
		}
		
		if (item.customerCode != null && !"null".equals(item.customerCode) && item.customerName != null
				&& !"null".equals(item.customerName)) {
			tvKH.setText(item.customerCode + " - " + item.customerName);
		}
		tvType.setText(item.type);
		if(!StringUtil.isNullOrEmpty(item.content)){
			tvContent.setText(item.content.trim());
		}
		tvCreateDate.setText(item.createDate);
		if (item.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
			cbNote.setVisibility(View.GONE);
			tvStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_CREATE));
		} else if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			cbNote.setVisibility(View.VISIBLE);
			cbNote.setChecked(false);
			cbNote.setEnabled(true);
			tvStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_GSNPP_DONE));
		} else if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
			cbNote.setVisibility(View.VISIBLE);
			cbNote.setChecked(true);
			cbNote.setEnabled(false);
			tvStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_TBHV_DONE));
		}

		if (item.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
			// Các vấn đề đã thực hiện hiển thị background màu xám
			this.setColorBackgroundForRow(ImageUtil.getColor(R.color.COLOR_GRAY_DONE));
			//Ngay nhac nho < ngay hien tai & phai khac "da thuc hien hoac da duyet"
		} else if (DateUtils.compareWithNow(item.remindDate, "dd/MM/yyyy") == -1 && item.status != FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			// Các vấn đề đã qua ngày nhắc nhở nhưng chưa thực hiện hiển
			// thị text màu đỏ.
			this.setColorForTextRow(ImageUtil.getColor(R.color.RED));
		}
		cbNote.setOnClickListener(this);
	}

	/**
	 * 
	 * set color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 * 
	 */
	public void setColorForTextRow(int color) {
		tvSTT.setTextColor(color);		
		SpannableObject obj = new SpannableObject();
		obj.addSpan(data.staffName, color, android.graphics.Typeface.NORMAL);
		obj.addSpan("\n" + data.staffCode, color, android.graphics.Typeface.NORMAL);
		tvStaffCode.setText(obj.getSpan());

		tvKH.setTextColor(color);
		tvType.setTextColor(color);
		tvContent.setTextColor(color);
		tvStatus.setTextColor(color);
		tvCreateDate.setTextColor(color);
		cbNote.setTextColor(color);
	}

	/**
	 * 
	 * set background color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 * 
	 */
	public void setColorBackgroundForRow(int color) {
		tvSTT.setBackgroundColor(color);
		tvStaffCode.setBackgroundColor(color);
		tvKH.setBackgroundColor(color);
		tvType.setBackgroundColor(color);
		tvContent.setBackgroundColor(color);
		tvStatus.setBackgroundColor(color);
		tvCreateDate.setBackgroundColor(color);
		cbNote.setBackgroundColor(color);
		rlNote.setBackgroundColor(color);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == this) {
			if(this.data != null){
				listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_FOLLOW_PROBLEM_DETAIL, null, this.data);
			}
		} else if (v == cbNote) {
			cbNote.setChecked(false);
//			cbNote.setEnabled(false);
			listener.handleVinamilkTableRowEvent(ActionEventConstant.UPDATE_GSNPP_FOLLOW_PROBLEM_DONE, null, this.data);
		} else if (context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}

	/**
	 * 
	 * cap nhat row khi thuc hien van de 
	 * 
	 * @author: YenNTH
	 * @param success
	 * @return: void
	 * @throws:
	 */
	public void updateCheckBoxDone(boolean success) {
		cbNote.setChecked(success);
		cbNote.setEnabled(!success);
		setColorBackgroundForRow(ImageUtil.getColor(R.color.COLOR_GRAY_DONE));		
		setColorForTextRow(ImageUtil.getColor(R.color.BLACK));
		SpannableObject obj = new SpannableObject();
		obj.addSpan(data.staffName, ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan("\n" + data.staffCode, ImageUtil.getColor(R.color.COLOR_USER_NAME), android.graphics.Typeface.NORMAL);
		tvStaffCode.setText(obj.getSpan());
		if (success) {
			tvStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_TBHV_DONE));
		}
	}

	/**
	 * 
	 * cap nhat row khi tao moi
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void updateCheckBoxNew() {
		cbNote.setVisibility(View.GONE);
		tvStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_CREATE));
	}
}
