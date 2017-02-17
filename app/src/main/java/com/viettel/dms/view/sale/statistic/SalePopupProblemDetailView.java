/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * hien thi thong tin chi tiet van de theo doi khac phuc TTTT
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.1
 */
public class SalePopupProblemDetailView extends LinearLayout implements OnClickListener {

	// OnEventControlListener
	public OnEventControlListener listener;
	int actionClose, actionDelete, actionDone;
	// context
	Context context;
	// root layout
	public View viewLayout;
	// type problem
	TextView tvTypeProblem;
	// create date
	TextView tvCreateDate;
	// remind date
	TextView tvRemindDate;
	// problem status
	TextView tvProblemStatus;
	// done date
	TextView tvDoneDate;
	// customer info
	TextView tvCustomerInfo;
	// problem content
	TextView tvProblemContent;
	// button close popup
	Button btCloseProblemDetail;
	// button delete popup
	Button btDeleteProblemDetail;
	// button done popup
	Button btDoneProblemDetail;
	// data to display popup
	FeedBackDTO currentDTO;
	// label loai van de
	TextView lbTypeProblem;
	// label trang thai
	TextView lbStatus;
	// label ngay tao
	TextView lbCreateDate;
	// ngay nhac nho
	TextView lbRemindDate;
	// ngay thuc hien
	TextView lbDoneDate;
	// khach hang
	TextView lbCustomerInfo;
	// noi dung
	TextView lbProblemContent;
	// ll ngay thuc hien
	LinearLayout llDoneDate;
	// ll khach hang
	LinearLayout llCustomerInfo;
	// ll khach hang
	LinearLayout llProblemContent;

	public SalePopupProblemDetailView(Context context, OnEventControlListener listener, int actionClose, int actionDelete, int actionDone) {
		super(context);
		this.context = context;
		this.listener = listener;
		this.actionClose = actionClose;
		this.actionDelete = actionDelete;
		this.actionDone = actionDone;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_custom_popup_detail_problem_nvbh_view, null);
		tvTypeProblem = (TextView) viewLayout.findViewById(R.id.tvTypeProblem);
		tvCreateDate = (TextView) viewLayout.findViewById(R.id.tvCreateDate);
		tvRemindDate = (TextView) viewLayout.findViewById(R.id.tvRemindDate);
		tvProblemStatus = (TextView) viewLayout.findViewById(R.id.tvStatus);
		tvDoneDate = (TextView) viewLayout.findViewById(R.id.tvDoneDate);
		tvCustomerInfo = (TextView) viewLayout.findViewById(R.id.tvCustomerInfo);
		tvProblemContent = (TextView) viewLayout.findViewById(R.id.tvProblemContent);
		btCloseProblemDetail = (Button) viewLayout.findViewById(R.id.btCloseProblemDetail);
		btCloseProblemDetail.setOnClickListener(this);
		btDeleteProblemDetail = (Button) viewLayout.findViewById(R.id.btDeleteProblemDetail);
		btDeleteProblemDetail.setOnClickListener(this);
		btDoneProblemDetail = (Button) viewLayout.findViewById(R.id.btDoneProblemDetail);
		btDoneProblemDetail.setOnClickListener(this);
		lbTypeProblem = (TextView) viewLayout.findViewById(R.id.lbTypeProblem);
		lbStatus = (TextView) viewLayout.findViewById(R.id.lbStatus);
		lbCreateDate = (TextView) viewLayout.findViewById(R.id.lbCreateDate);
		lbRemindDate = (TextView) viewLayout.findViewById(R.id.lbRemindDate);
		lbDoneDate = (TextView) viewLayout.findViewById(R.id.lbDoneDate);
		lbCustomerInfo = (TextView) viewLayout.findViewById(R.id.lbCustomerInfo);
		lbProblemContent = (TextView) viewLayout.findViewById(R.id.lbProblemContent);
		llDoneDate = (LinearLayout) viewLayout.findViewById(R.id.llDoneDate);
		llCustomerInfo = (LinearLayout) viewLayout.findViewById(R.id.llCustomerInfo);
		llProblemContent = (LinearLayout) viewLayout.findViewById(R.id.llProblemContent);
	}
	

	/**
	 * 
	 * render layout for popup with object SupervisorProblemOfGSNPP
	 * 
	 * @param object
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 */
	public void renderLayoutWithObject(FeedBackDTO object) {
		if (object == null)
			return;
		currentDTO = object;
		if (!StringUtil.isNullOrEmpty(object.apParamName)) {
			lbTypeProblem.setVisibility(View.VISIBLE);
			tvTypeProblem.setVisibility(View.VISIBLE);
			tvTypeProblem.setText(object.apParamName);
		}else {
			lbTypeProblem.setVisibility(View.GONE);
			tvTypeProblem.setVisibility(View.GONE);
		}
		if(!StringUtil.isNullOrEmpty(object.createDate)){
			lbCreateDate.setVisibility(View.VISIBLE);
			tvCreateDate.setVisibility(View.VISIBLE);
			tvCreateDate.setText(object.createDate);
		}else {
			lbCreateDate.setVisibility(View.GONE);
			tvCreateDate.setVisibility(View.GONE);
		}
		if(!StringUtil.isNullOrEmpty(object.remindDate)){
			lbRemindDate.setVisibility(View.VISIBLE);
			tvRemindDate.setVisibility(View.VISIBLE);
			tvRemindDate.setText(object.remindDate);
		}else {
			lbRemindDate.setVisibility(View.GONE);
			tvRemindDate.setVisibility(View.GONE);
		}
		btDeleteProblemDetail.setVisibility(View.GONE);
		btDoneProblemDetail.setVisibility(View.VISIBLE);
		btCloseProblemDetail.setVisibility(View.VISIBLE);
		//khong xoa feedback cua gs
		if(object.createUserId == GlobalInfo.getInstance().getProfile().getUserData().id
				&& object.status == FeedBackDTO.FEEDBACK_STATUS_CREATE){
			btDeleteProblemDetail.setVisibility(View.VISIBLE);
		}
		if (object.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
			tvProblemStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_CREATE));
		} else if (object.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			tvProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_DONE));
			btDoneProblemDetail.setVisibility(View.GONE);
		} else if (object.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
			tvProblemStatus.setText(StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_TBHV_DONE));
			btDoneProblemDetail.setVisibility(View.GONE);
		}
		// display done date
		if(!StringUtil.isNullOrEmpty(object.doneDate)){
			llDoneDate.setVisibility(View.VISIBLE);
			lbDoneDate.setVisibility(View.VISIBLE);
			tvDoneDate.setVisibility(View.VISIBLE);
			tvDoneDate.setText(object.doneDate);
		}else {
			llDoneDate.setVisibility(View.GONE);
			lbDoneDate.setVisibility(View.GONE);
			tvDoneDate.setVisibility(View.GONE);
		}
		// display customer info
		String customerInfo = Constants.STR_BLANK;
		if (object.customerCode != null && object.customerCode.length() > 3) {
			customerInfo += object.customerCode.substring(0, 3) + " - "+ object.customerName;
		} else {
			String strCustomerInfo = Constants.STR_BLANK;
			if (!StringUtil.isNullOrEmpty(object.customerCode)) {
				strCustomerInfo += object.customerCode;
				if (!StringUtil.isNullOrEmpty(object.customerName)) {
					strCustomerInfo += " - ";
				}
			}
			if (!StringUtil.isNullOrEmpty(object.customerName)) {
				strCustomerInfo += object.customerName;
			}
			customerInfo += strCustomerInfo;
		}
		if (!StringUtil.isNullOrEmpty(object.address)) {
			customerInfo += ", " + object.address;
		} else {
			if (!StringUtil.isNullOrEmpty(object.houseNumber)) {
				customerInfo += ", " + object.houseNumber;
				if (!StringUtil.isNullOrEmpty(object.street)) {
					customerInfo += Constants.STR_SPACE + object.street;
				}
			} else {
				if (!StringUtil.isNullOrEmpty(object.street)) {
					customerInfo += ", " + object.street;
				}
			}
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			llCustomerInfo.setVisibility(View.VISIBLE);
			lbCustomerInfo.setVisibility(View.VISIBLE);
			tvCustomerInfo.setVisibility(View.VISIBLE);
			if (object.createUserId == Long.valueOf(GlobalInfo.getInstance().getProfile().getUserData().staffOwnerId)){
				tvCustomerInfo.setText(customerInfo.trim()+"*");
				tvCustomerInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}else {
				tvCustomerInfo.setText(customerInfo.trim());
				tvCustomerInfo.setTextColor(ImageUtil.getColor(R.color.BLACK));
			}
		}else {
			llCustomerInfo.setVisibility(View.GONE);
			lbCustomerInfo.setVisibility(View.GONE);
			tvCustomerInfo.setVisibility(View.GONE);
		}
		// display content
		if(!StringUtil.isNullOrEmpty(object.content)){
			llProblemContent.setVisibility(View.VISIBLE);
			lbProblemContent.setVisibility(View.VISIBLE);
			tvProblemContent.setVisibility(View.VISIBLE);
			tvProblemContent.setText(object.content);
		}else {
			llProblemContent.setVisibility(View.GONE);
			lbProblemContent.setVisibility(View.GONE);
			tvProblemContent.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (listener != null) {
			if (arg0 == btCloseProblemDetail) {
				listener.onEvent(actionClose, arg0, currentDTO);
			} else if (arg0 == btDeleteProblemDetail) {
				listener.onEvent(actionDelete, arg0, currentDTO);
			} else if (arg0 == btDoneProblemDetail) {
				listener.onEvent(actionDone, arg0, currentDTO);
			}
		}
	}

}
