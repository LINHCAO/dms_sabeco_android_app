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
import com.viettel.dms.dto.view.SupervisorProblemOfGSNPPDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * display track and fix problem row
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TrackAndFixProblemOfGSNPPRow extends DMSTableRow implements
		OnClickListener {

	// STT
	TextView tvSTT;
	// type problem
	TextView tvTypeProblem;
	// content problem
	TextView tvProblemContent;
	// customer info in problem
	TextView tvCustomerInfo;
	// remind date for gsnpp
	TextView tvRemindDate;
	// linner layout status of problem
	LinearLayout llStatus;
	// check box status of problem
	CheckBox cbStatus;
	// text notify when null
	TextView tvNotifyNull;
	// data for row
	SupervisorProblemOfGSNPPDTO currentData;
	
	/**
	 * @param context
	 */
	public TrackAndFixProblemOfGSNPPRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_supervisor_track_and_fix_problems_of_gsnpp_row);
		setListener(lis);
		this.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvTypeProblem = (TextView) this.findViewById(R.id.tvTypeProblem);
		tvProblemContent = (TextView) this.findViewById(R.id.tvProblemContent);
		tvCustomerInfo = (TextView) this.findViewById(R.id.tvCustomerInfo);
		tvRemindDate = (TextView) this.findViewById(R.id.tvRemindDate);
		llStatus = (LinearLayout) this.findViewById(R.id.llStatus);
		cbStatus = (CheckBox) this.findViewById(R.id.cbStatus);
		cbStatus.setOnClickListener(this);
	}

	/**
	 * 
	 * set color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void setColorForTextRow(int color) {
		tvSTT.setTextColor(color);
		tvTypeProblem.setTextColor(color);
		tvProblemContent.setTextColor(color);
		tvCustomerInfo.setTextColor(color);
		tvRemindDate.setTextColor(color);
	}

	/**
	 * 
	 * render layout for row
	 * 
	 * @param stt
	 * @param data
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void renderLayoutForRow(int stt, SupervisorProblemOfGSNPPDTO data) {
		if (data != null) {
			currentData = data;
			tvSTT.setText(String.valueOf(stt));
			if (!StringUtil.isNullOrEmpty(this.currentData.problemType)) {
				tvTypeProblem.setText(this.currentData.problemType.trim());
			}
			if (!StringUtil.isNullOrEmpty(this.currentData.problemContent)) {
				tvProblemContent
						.setText(this.currentData.problemContent.trim());
			}

			if (this.currentData.customerInfo.getCustomerCode() != null
					&& this.currentData.customerInfo.getCustomerCode().length() > 3) {
				if(!StringUtil.isNullOrEmpty(this.currentData.customerInfo
						.getCustomerCode())) {
					tvCustomerInfo.setText(this.currentData.customerInfo
							.getCustomerCode().substring(0, 3)
							+ " - "
							+ this.currentData.customerInfo.getCustomerName());
				}else{
					tvCustomerInfo.setText(this.currentData.customerInfo.getCustomerName());
				}
			} else {
				String strCustomerInfo = Constants.STR_BLANK;
				if (!StringUtil.isNullOrEmpty(this.currentData.customerInfo
						.getCustomerCode())) {
					strCustomerInfo += this.currentData.customerInfo
							.getCustomerCode();
					if (!StringUtil.isNullOrEmpty(this.currentData.customerInfo
							.getCustomerName())) {
						strCustomerInfo += " - ";
					}
				}
				if (!StringUtil.isNullOrEmpty(this.currentData.customerInfo
						.getCustomerName())) {
					strCustomerInfo += this.currentData.customerInfo
							.getCustomerName();
				}

				tvCustomerInfo.setText(strCustomerInfo);
			}
			if (!StringUtil.isNullOrEmpty(this.currentData.remindDate)) {
				tvRemindDate.setText(this.currentData.remindDate.trim());
			}
			if (this.currentData.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE
					|| this.currentData.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
				cbStatus.setChecked(true);
				cbStatus.setEnabled(false);
			} else {
				cbStatus.setEnabled(true);
				cbStatus.setChecked(false);
			}

			if (data.rowStatus == SupervisorProblemOfGSNPPDTO.OBJECT_TYPE_OUT_REMIND_NOT_DONE && !cbStatus.isChecked()) {
				this.setColorForTextRow(ImageUtil.getColor(R.color.RED));
			} else if (data.rowStatus == SupervisorProblemOfGSNPPDTO.OBJECT_TYPE_DONE_TO_DAY) {
				this.setColorBackgroundForRow(ImageUtil.getColor(R.color.COLOR_BLUE_DONE_TODAY));
			} else if (data.rowStatus == SupervisorProblemOfGSNPPDTO.OBJECT_TYPE_DONE_OUT_TO_DAY || cbStatus.isChecked()) {
				this.setColorBackgroundForRow(ImageUtil.getColor(R.color.COLOR_GRAY_DONE));
			}
		}
	}

	/**
	 * 
	 * set background color for row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void setColorBackgroundForRow(int color) {
		tvSTT.setBackgroundColor(color);
		tvTypeProblem.setBackgroundColor(color);
		tvProblemContent.setBackgroundColor(color);
		tvCustomerInfo.setBackgroundColor(color);
		tvRemindDate.setBackgroundColor(color);
		llStatus.setBackgroundColor(color);

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == this) {
			if(listener != null){
				if(this.currentData != null){
					listener.handleVinamilkTableRowEvent(
							ActionEventConstant.SHOW_PROBLEM_DETAIL_POPUP, this,
							this.currentData);
				}
			}
		} else if (arg0 == cbStatus && cbStatus.isChecked()) {
			listener.handleVinamilkTableRowEvent(
					ActionEventConstant.ACTION_UPDATE_CHANGE_PROBLEM_STATUS,
					this, this.currentData);
		}
	}
}
