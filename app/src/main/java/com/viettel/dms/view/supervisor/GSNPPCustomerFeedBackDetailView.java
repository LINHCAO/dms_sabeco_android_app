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
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * hien thi chi tiet gop y GSNPP
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class GSNPPCustomerFeedBackDetailView extends LinearLayout implements
		OnClickListener {
	public static final int CLOSE_POPUP_DETAIL_PROMOTION = 1000;
	public static final int ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL = 5;
	public static final int ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL = 6;
	// nhan vien
	private TextView tvFollowProblemStaff;
	// khach hang
	private TextView tvFollowProblemCustomer;
	//loai van de
	private TextView tvTypeProblem;
	// ngay tao
	private TextView tvFollowProblemCreateDate;
	//ngay nhac nho
	private TextView tvFollowProblemRemindDate;
	// ngay thuc hien
	private TextView tvFollowProblemDoneDate;
	// trang thai
	private TextView tvFollowProblemStatus;
	// noi dung
	private TextView tvFollowProblemContent;
	// button close
	public Button btCloseFollowProblemDetail;
	// label ngay nhac nho
	TextView lbFollowProblemRemindDate;
	// label ngay thuc hien
	TextView lbFollowProblemDoneDate;
	// linearlayout kh
	LinearLayout llFollowProblemCustomer;
	// linearlayout noi dung
	LinearLayout llFollowProblemContent;

	// BaseFragment
	public BaseFragment listener;
	public View viewLayout;
	FeedBackDTO dtoData;
	Context context;

	public GSNPPCustomerFeedBackDetailView(Context con){
		super(con);
		context=con;
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public GSNPPCustomerFeedBackDetailView(Context context, BaseFragment listener) {
		this(context);
		this.listener = listener;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_gsnpp_customer_feedback_detail_view,null);
		btCloseFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btCloseFollowProblemDetail);
		btCloseFollowProblemDetail.setOnClickListener(this);
		tvFollowProblemStaff = (TextView) viewLayout.findViewById(R.id.tvFollowProblemStaff);
		tvFollowProblemCustomer = (TextView) viewLayout.findViewById(R.id.tvFollowProblemCustomer);
		tvTypeProblem = (TextView) viewLayout.findViewById(R.id.tvTypeProblem);
		tvFollowProblemCreateDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemCreateDate);
		tvFollowProblemRemindDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemRemindDate);
		tvFollowProblemDoneDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemDoneDate);
		tvFollowProblemStatus = (TextView) viewLayout.findViewById(R.id.tvFollowProblemStatus);
		tvFollowProblemContent = (TextView) viewLayout.findViewById(R.id.tvFollowProblemContent);
		lbFollowProblemRemindDate = (TextView) viewLayout.findViewById(R.id.lbFollowProblemRemindDate);
		lbFollowProblemDoneDate = (TextView) viewLayout.findViewById(R.id.lbFollowProblemDoneDate);
		llFollowProblemCustomer = (LinearLayout) viewLayout.findViewById(R.id.llFollowProblemCustomer);
		llFollowProblemContent = (LinearLayout) viewLayout.findViewById(R.id.llFollowProblemContent);
	}

	/**
	 * 
	 * update layout
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void updateLayout(FeedBackDTO dto) {
		this.dtoData = dto;
		if(dtoData.staffDto != null){
			tvFollowProblemStaff.setText(dtoData.staffDto.staffCode + " - " + dtoData.staffDto.name);
		}
		if(dtoData.customerCode != null && dtoData.customerName != null){
		tvFollowProblemCustomer.setText(dtoData.customerCode+ " - " + dtoData.customerName);
		}
		String customerInfo = Constants.STR_BLANK;
		if (dtoData.customerCode != null
				&& dtoData.customerCode.length() > 3) {
			customerInfo += dtoData.customerCode.substring(0,3) + " - " + dtoData.customerName;
		} else {
			String strCustomerInfo = Constants.STR_BLANK;
			if (!StringUtil.isNullOrEmpty(dtoData.customerCode)) {
				strCustomerInfo += dtoData.customerCode;
				if (!StringUtil.isNullOrEmpty(dtoData.customerName)) {
					strCustomerInfo += " - ";
				}
			}
			if (!StringUtil.isNullOrEmpty(dtoData.customerName)) {
				strCustomerInfo += dtoData.customerName;
			}
			customerInfo += strCustomerInfo;
		}
		if (!StringUtil.isNullOrEmpty(dtoData.address)) {
			customerInfo += ", " + dtoData.address;
		} else {
			if(!StringUtil.isNullOrEmpty(dtoData.houseNumber)){
				customerInfo += ", " + dtoData.houseNumber;
				if(!StringUtil.isNullOrEmpty(dtoData.street)){
					customerInfo += Constants.STR_SPACE + dtoData.street;
				}
			}
			else{
				if(!StringUtil.isNullOrEmpty(dtoData.street)){
					customerInfo += ", " + dtoData.street;
				}
			}
		}
		if(!StringUtil.isNullOrEmpty(customerInfo)){
			llFollowProblemCustomer.setVisibility(View.VISIBLE);
			tvFollowProblemCustomer.setText(customerInfo.trim());
		}else {
			llFollowProblemCustomer.setVisibility(View.GONE);
			tvFollowProblemCustomer.setText(Constants.STR_BLANK);
		}
		tvFollowProblemCreateDate.setText(dtoData.createDate);
		if(!StringUtil.isNullOrEmpty(dtoData.remindDate)){
			lbFollowProblemRemindDate.setVisibility(View.VISIBLE);
			tvFollowProblemRemindDate.setVisibility(View.VISIBLE);
			tvFollowProblemRemindDate.setText(dtoData.remindDate);
		}else {
			lbFollowProblemRemindDate.setVisibility(View.INVISIBLE);
			tvFollowProblemRemindDate.setVisibility(View.INVISIBLE);
			tvFollowProblemRemindDate.setText(Constants.STR_BLANK);
		}
		if(!StringUtil.isNullOrEmpty(dtoData.doneDate)){
			lbFollowProblemDoneDate.setVisibility(View.VISIBLE);
			tvFollowProblemDoneDate.setVisibility(View.VISIBLE);
			tvFollowProblemDoneDate.setText(dtoData.doneDate);
		}else {
			lbFollowProblemDoneDate.setVisibility(View.INVISIBLE);
			tvFollowProblemDoneDate.setVisibility(View.INVISIBLE);
			tvFollowProblemDoneDate.setText(Constants.STR_BLANK);
		}
		if (dtoData.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {//tao moi
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_CREATE_NEW));			
		} else if (dtoData.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_BUTTON_DONE));
		} else {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_APPROVED));
		}
		if(!StringUtil.isNullOrEmpty(dtoData.apParamName)){
			tvTypeProblem.setText(dtoData.apParamName);
		}else {
			tvTypeProblem.setText(Constants.STR_BLANK);
		}
		
		if(!StringUtil.isNullOrEmpty(dtoData.content)){
			tvFollowProblemContent.setText(dtoData.content);
			tvFollowProblemContent.setVisibility(View.VISIBLE);
			llFollowProblemContent.setVisibility(View.VISIBLE);
		}else {
			tvFollowProblemContent.setText(Constants.STR_BLANK);
			tvFollowProblemContent.setVisibility(View.GONE);
			llFollowProblemContent.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btCloseFollowProblemDetail) {
			listener.onClick(view);
		}
	}
}
