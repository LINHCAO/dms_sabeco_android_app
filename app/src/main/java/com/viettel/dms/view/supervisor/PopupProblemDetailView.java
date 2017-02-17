/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

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
import com.viettel.dms.dto.view.SupervisorProblemOfGSNPPDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * hien thi thong tin chi tiet van de theo doi khac phuc cua gsnpp
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class PopupProblemDetailView extends LinearLayout implements
		OnClickListener {

	// BaseFragment
	public BaseFragment listener;
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
	// check box update status
	Button btPerform;
	// data to display popup
	SupervisorProblemOfGSNPPDTO currentDTO;
	// su dung an cac label neu gia tri null
	// linear layout khach hang
	LinearLayout llCustomerInfo;
	// linear layout noi dung
	LinearLayout llProblemContent;
	// ngay nhac nho
	TextView lbRemindDate;
	// ngay thuc hien
	TextView lbDoneDate;
	// khach hang
	TextView lbCustomerInfo;
	// noi dung
	TextView lbProblemContent;

	/**
	 * @param context
	 * @param attrs
	 */
	public PopupProblemDetailView(Context context, BaseFragment listener) {
		super(context);
		this.context = context;

		this.listener = listener;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater.inflate(
				R.layout.layout_custom_popup_detail_problem_gsnpp_view, null);
		tvTypeProblem = (TextView) viewLayout.findViewById(R.id.tvTypeProblem);
		tvCreateDate = (TextView) viewLayout.findViewById(R.id.tvCreateDate);
		tvRemindDate = (TextView) viewLayout.findViewById(R.id.tvRemindDate);
		tvProblemStatus = (TextView) viewLayout
				.findViewById(R.id.tvProblemStatus);
		tvDoneDate = (TextView) viewLayout.findViewById(R.id.tvDoneDate);
		tvCustomerInfo = (TextView) viewLayout
				.findViewById(R.id.tvCustomerInfo);
		tvProblemContent = (TextView) viewLayout
				.findViewById(R.id.tvProblemContent);
		btCloseProblemDetail = (Button) viewLayout
				.findViewById(R.id.btCloseProblemDetail);
		btCloseProblemDetail.setOnClickListener(this.listener);
		btPerform = (Button) viewLayout.findViewById(R.id.btPerform);
		btPerform.setOnClickListener(this.listener);
		llCustomerInfo = (LinearLayout) viewLayout.findViewById(R.id.llCustomerInfo);
		llProblemContent = (LinearLayout) viewLayout.findViewById(R.id.llProblemContent);
		lbRemindDate = (TextView) viewLayout.findViewById(R.id.lbRemindDate);
		lbDoneDate = (TextView) viewLayout.findViewById(R.id.lbDoneDate);
		lbCustomerInfo = (TextView) viewLayout.findViewById(R.id.lbCustomerInfo);
		lbProblemContent = (TextView) viewLayout.findViewById(R.id.lbProblemContent);
		
	}

	/**
	 * 
	 * render layout for popup with object SupervisorProblemOfGSNPP
	 * 
	 * @param object
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 7, 2012
	 */
	public void renderLayoutWithObject(SupervisorProblemOfGSNPPDTO object) {	
		if(object == null)
			return;
		currentDTO = object;
		tvTypeProblem.setText(object.problemType);
		tvCreateDate.setText(object.createDate);
		tvRemindDate.setText(object.remindDate);
		if (object.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
			tvProblemStatus.setText(StringUtil
					.getString(R.string.TEXT_FEEDBACK_TYPE_CREATE));
		} else if (object.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			tvProblemStatus.setText(StringUtil
					.getString(R.string.TEXT_FEEDBACK_TYPE_GSNPP_DONE));
		} else if (object.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
			tvProblemStatus.setText(StringUtil
					.getString(R.string.TEXT_FEEDBACK_TYPE_TBHV_DONE));
		}
		// display done date
		if(!StringUtil.isNullOrEmpty(object.doneDate)){
			lbDoneDate.setVisibility(View.VISIBLE);
			tvDoneDate.setVisibility(View.VISIBLE);
			tvDoneDate.setText(object.doneDate);
		}else {
			lbDoneDate.setVisibility(View.INVISIBLE);
			tvDoneDate.setVisibility(View.INVISIBLE);
		}
		
		// display customer info
		String customerInfo = Constants.STR_BLANK;

		if (object.customerInfo.getCustomerCode() != null
				&& object.customerInfo.getCustomerCode().length() > 3) {
			customerInfo += object.customerInfo.getCustomerCode().substring(0,
					3)
					+ " - " + object.customerInfo.getCustomerName();
		} else {
			String strCustomerInfo = Constants.STR_BLANK;
			if (!StringUtil
					.isNullOrEmpty(object.customerInfo.getCustomerCode())) {
				strCustomerInfo += object.customerInfo.getCustomerCode();
				if (!StringUtil.isNullOrEmpty(object.customerInfo
						.getCustomerName())) {
					strCustomerInfo += " - ";
				}
			}
			if (!StringUtil
					.isNullOrEmpty(object.customerInfo.getCustomerName())) {
				strCustomerInfo += object.customerInfo.getCustomerName();
			}

			customerInfo += strCustomerInfo;
		}

		if (!StringUtil.isNullOrEmpty(object.customerInfo.getHouseNumber())) {
			customerInfo += ", " + object.customerInfo.getHouseNumber();
			if (!StringUtil.isNullOrEmpty(object.customerInfo.getStreet())) {
				customerInfo += " " + object.customerInfo.getStreet();
			}
		} else {
			if (!StringUtil.isNullOrEmpty(object.customerInfo.getStreet())) {
				customerInfo += ", " + object.customerInfo.getStreet();
			}
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			llCustomerInfo.setVisibility(View.VISIBLE);
			lbCustomerInfo.setVisibility(View.VISIBLE);
			tvCustomerInfo.setVisibility(View.VISIBLE);
			tvCustomerInfo.setText(customerInfo.trim());
		}else {
			llCustomerInfo.setVisibility(View.GONE);
			lbCustomerInfo.setVisibility(View.GONE);
			tvCustomerInfo.setVisibility(View.GONE);
		}

		// display content
		if(!StringUtil.isNullOrEmpty(object.problemContent)){
			llProblemContent.setVisibility(View.VISIBLE);
			lbProblemContent.setVisibility(View.VISIBLE);
			tvProblemContent.setVisibility(View.VISIBLE);
			tvProblemContent.setText(object.problemContent);
		}else {
			llProblemContent.setVisibility(View.GONE);
			lbProblemContent.setVisibility(View.GONE);
			tvProblemContent.setVisibility(View.GONE);
		}



		if (this.currentDTO.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
			btPerform.setVisibility(View.VISIBLE);
		}else{
			btPerform.setVisibility(View.GONE);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
