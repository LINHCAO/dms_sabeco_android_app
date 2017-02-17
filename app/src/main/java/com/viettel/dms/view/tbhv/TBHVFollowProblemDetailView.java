/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * 
 * MH Hien thi chi tiet van de GST
 * 
 * @author: YenNTH
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVFollowProblemDetailView extends LinearLayout implements
		OnClickListener {

	public static final int CLOSE_POPUP_DETAIL_PROMOTION = 1000;
	public static final int ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL = 5;
	public static final int ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL = 6;

	// nhan vien
	TextView tvFollowProblemStaff;
	TextView tvTitleFollowProblemStaff;
	// ngay tao
	TextView tvFollowProblemCreateDate;
	TextView tvTitleFollowProblemCreateDate;
	// ngay thuc hien
	TextView tvFollowProblemDoneDate;
	TextView tvTitleFollowProblemDoneDate;
	// trang thai
	TextView tvFollowProblemStatus;
	TextView tvTitleFollowProblemStatus;
	// noi dung
	TextView tvFollowProblemContent;
	TextView tvTitleFollowProblemContent;
	// loai van de
	TextView tvTypeProblem;
	TextView tvTitleTypeProblem;
	// ngay nhac nho
	TextView tvRemindDate;
	TextView tvTitleRemindDate;
	// khach hang
	TextView tvCustomer;
	TextView tvTitleCustomer;
	LinearLayout llCustomer;
	// button close
	public Button btCloseFollowProblemDetail;// dong
	public Button btUpdateFollowProblemDetail;// duyet
	public Button btNewFollowProblemDetail;// yeu cau lam lai

	// BaseFragment
	public BaseFragment listener;
	public View viewLayout;
	TBHVFollowProblemItemDTO dtoData;
	Context context;
	public TBHVFollowProblemDetailView(Context context, BaseFragment listener) {
		super(context);
		this.context = context;
		this.listener = listener;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_tbhv_follow_problem_detail, null);
		btCloseFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btCloseFollowProblemDetail);
		btCloseFollowProblemDetail.setOnClickListener(this);
		btUpdateFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btUpdateFollowProblemDetail);
		btUpdateFollowProblemDetail.setOnClickListener(this);
		btNewFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btNewFollowProblemDetail);
		btNewFollowProblemDetail.setOnClickListener(this);
		tvFollowProblemStaff = (TextView) viewLayout.findViewById(R.id.tvFollowProblemStaff);
		tvTitleFollowProblemStaff = (TextView) viewLayout.findViewById(R.id.tvTitleFollowProblemStaff);
		tvFollowProblemCreateDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemCreateDate);
		tvTitleFollowProblemCreateDate = (TextView) viewLayout.findViewById(R.id.tvTitleFollowProblemCreateDate);
		tvFollowProblemDoneDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemDoneDate);
		tvTitleFollowProblemDoneDate = (TextView) viewLayout.findViewById(R.id.tvTitleFollowProblemDoneDate);
		tvFollowProblemStatus = (TextView) viewLayout.findViewById(R.id.tvFollowProblemStatus);
		tvTitleFollowProblemStatus = (TextView) viewLayout.findViewById(R.id.tvTitleFollowProblemStatus);
		tvFollowProblemContent = (TextView) viewLayout.findViewById(R.id.tvFollowProblemContent);
		tvTitleFollowProblemContent = (TextView) viewLayout.findViewById(R.id.tvTitleFollowProblemContent);
		tvTypeProblem = (TextView) viewLayout.findViewById(R.id.tvTypeProblem);
		tvTitleTypeProblem = (TextView) viewLayout.findViewById(R.id.tvTitleTypeProblem);
		tvRemindDate = (TextView) viewLayout.findViewById(R.id.tvRemindDate);
		tvTitleRemindDate = (TextView) viewLayout.findViewById(R.id.tvTitleRemindDate);
		tvCustomer = (TextView) viewLayout.findViewById(R.id.tvCustomer);
		tvTitleCustomer = (TextView) viewLayout.findViewById(R.id.tvTitleCustomer);
		llCustomer = (LinearLayout) viewLayout.findViewById(R.id.llCustomer);
	}

	/**
	 * 
	 * Cap nhat layout
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	public void updateLayout(TBHVFollowProblemItemDTO dto) {

		this.dtoData = dto;		
		String s = dtoData.staffCode;
		if(!StringUtil.isNullOrEmpty(s)){
			s += " - ";
		}
		s += dtoData.staffName;
		if (dto.chanelTypeDTO.objectType == UserDTO.TYPE_GSNPP) {
			tvFollowProblemStaff.setText(s + StringUtil.getString(R.string.TEXT_GS));
		} else {
			tvFollowProblemStaff.setText(s + StringUtil.getString(R.string.TEXT_TT));
		}
		if(StringUtil.isNullOrEmpty(dtoData.staffCode) && StringUtil.isNullOrEmpty(dtoData.staffName)){
			tvFollowProblemStaff.setText(Constants.STR_BLANK);
			tvTitleFollowProblemStaff.setText(Constants.STR_BLANK);
		}
		tvFollowProblemCreateDate.setText(dtoData.createDate);
		if(StringUtil.isNullOrEmpty(dtoData.createDate)){
			tvFollowProblemCreateDate.setText(Constants.STR_BLANK);
			tvTitleFollowProblemCreateDate.setText(Constants.STR_BLANK);
		}
		tvFollowProblemDoneDate.setText(dtoData.doneDate);
		tvTitleFollowProblemDoneDate.setVisibility(View.VISIBLE);
		if(StringUtil.isNullOrEmpty(dtoData.doneDate)){
			tvFollowProblemDoneDate.setText(Constants.STR_BLANK);
			tvTitleFollowProblemDoneDate.setVisibility(View.INVISIBLE);
		}
		
		if (dtoData.status == TBHVFollowProblemItemDTO.STATUS_NEW) {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_CREATE_NEW));
			GlobalUtil.setEnableButton(btUpdateFollowProblemDetail, false);
			GlobalUtil.setEnableButton(btNewFollowProblemDetail, false);
		} else if (dtoData.status == TBHVFollowProblemItemDTO.STATUS_DONE) {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_DONE));
			GlobalUtil.setEnableButton(btUpdateFollowProblemDetail, true);
			GlobalUtil.setEnableButton(btNewFollowProblemDetail, true);
		} else if (dtoData.status == TBHVFollowProblemItemDTO.STATUS_APPROVE) {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_APPROVED));			
			GlobalUtil.setEnableButton(btUpdateFollowProblemDetail, false);
			GlobalUtil.setEnableButton(btNewFollowProblemDetail, false);
		}else{
			tvFollowProblemStatus.setText(Constants.STR_BLANK);
			tvTitleFollowProblemStatus.setText(Constants.STR_BLANK);
		}		
		tvFollowProblemContent.setText(dtoData.content);
		if(StringUtil.isNullOrEmpty(dtoData.content)){
			tvFollowProblemContent.setText(Constants.STR_BLANK);
			tvTitleFollowProblemContent.setText(Constants.STR_BLANK);
		}
		tvTypeProblem.setText(dtoData.type);
		tvRemindDate.setText(dtoData.remindDate);
		tvTitleRemindDate.setVisibility(View.VISIBLE);
		if(StringUtil.isNullOrEmpty(dtoData.remindDate)){
			tvRemindDate.setText(Constants.STR_BLANK);
			tvTitleRemindDate.setVisibility(View.INVISIBLE);
		}
		s = "";
		if(!StringUtil.isNullOrEmpty(dtoData.customer_code)){
			s = dtoData.customer_code.substring(0, 3);
		}
		if(!StringUtil.isNullOrEmpty(s)){
			s += " - ";
		}
		s += dtoData.customer_name;
		if(!StringUtil.isNullOrEmpty(dtoData.customer_name)){
			s += ", ";
		}
		s += dtoData.housenumber; 
		if(!StringUtil.isNullOrEmpty(dtoData.housenumber)){
			s += ", ";
		}
		if(!StringUtil.isNullOrEmpty(dtoData.address)){
			s += dtoData.address;
		}
		tvCustomer.setText(s);
		tvCustomer.setVisibility(View.VISIBLE);
		tvTitleCustomer.setVisibility(View.VISIBLE);
		llCustomer.setVisibility(View.VISIBLE);
		if(StringUtil.isNullOrEmpty(s)){
			llCustomer.setVisibility(View.GONE);
			tvCustomer.setVisibility(View.GONE);
			tvTitleCustomer.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btCloseFollowProblemDetail) {
			listener.onClick(view);//dong dialog
		} else if (view == btUpdateFollowProblemDetail) {
			listener.onClick(view);//dong dialog
			dtoData.status = TBHVFollowProblemItemDTO.STATUS_APPROVE;
			TBHVFollowProblemView remainFragment = (TBHVFollowProblemView) listener.getActivity().getFragmentManager().findFragmentByTag(TBHVFollowProblemView.TAG);
			if (remainFragment != null) {
				remainFragment.confirmResolveUpdateProblem(dtoData, true);
			}			
		} else if (view == btNewFollowProblemDetail) {
			listener.onClick(view);//dong dialog
			dtoData.status = TBHVFollowProblemItemDTO.STATUS_NEW;
			TBHVFollowProblemView remainFragment = (TBHVFollowProblemView) listener.getActivity().getFragmentManager().findFragmentByTag(TBHVFollowProblemView.TAG);
			if (remainFragment != null) {
				String mess = Constants.STR_BLANK;
				mess = StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_NEW_FOLLOW_PROBLEM_DETAIL);
				GlobalUtil.showDialogConfirm(remainFragment,(TBHVActivity) remainFragment.getActivity(),mess, StringUtil.getString(R.string.TEXT_AGREE),ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL,StringUtil.getString(R.string.TEXT_DENY),ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL, dtoData);
			}			
		}
	}

}
