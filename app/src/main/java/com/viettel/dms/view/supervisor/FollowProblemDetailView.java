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
import com.viettel.dms.dto.view.FollowProblemItemDTO;
import com.viettel.dms.dto.view.HistoryItemDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.dms.view.sale.statistic.HistoryRow;
import com.viettel.sabeco.R;

/**
 * hien thi chi tiet van de GSBH
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class FollowProblemDetailView extends LinearLayout implements
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
	// history
	private VinamilkTableView tbHistory;
	// button close
	public Button btCloseFollowProblemDetail;
	// button Duyet
	public Button btUpdateFollowProblemDetail;
	// button Yeu cau lam lai
	public Button btNewFollowProblemDetail;	
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
	FollowProblemItemDTO dtoData;
	private int[] HISTORY_WIDTHS = { 80, 230, 230};
	private String[] HISTORY_TITLES = { StringUtil.getString(R.string.TEXT_STT), StringUtil.getString(R.string.TEXT_HEADER_TABLE_SKU), StringUtil.getString(R.string.TEXT_SALES_)};
	private final Context context;

	public FollowProblemDetailView(Context con){
		super(con);
		context=con;
	}
	
	/**
	 * @param context
	 * @param attrs
	 */
	public FollowProblemDetailView(Context context, BaseFragment listener) {
		this(context);
		this.listener = listener;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_follow_problem_detail,null);
		btCloseFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btCloseFollowProblemDetail);
		btCloseFollowProblemDetail.setOnClickListener(this);
		btUpdateFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btUpdateFollowProblemDetail);
		btUpdateFollowProblemDetail.setOnClickListener(this);
		btNewFollowProblemDetail = (Button) viewLayout.findViewById(R.id.btNewFollowProblemDetail);
		btNewFollowProblemDetail.setOnClickListener(this);
		tvFollowProblemStaff = (TextView) viewLayout.findViewById(R.id.tvFollowProblemStaff);
		tvFollowProblemCustomer = (TextView) viewLayout.findViewById(R.id.tvFollowProblemCustomer);
		tvTypeProblem = (TextView) viewLayout.findViewById(R.id.tvTypeProblem);
		tvFollowProblemCreateDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemCreateDate);
		tvFollowProblemRemindDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemRemindDate);
		tvFollowProblemDoneDate = (TextView) viewLayout.findViewById(R.id.tvFollowProblemDoneDate);
		tvFollowProblemStatus = (TextView) viewLayout.findViewById(R.id.tvFollowProblemStatus);
		tvFollowProblemContent = (TextView) viewLayout.findViewById(R.id.tvFollowProblemContent);
		tbHistory = (VinamilkTableView) viewLayout.findViewById(R.id.tbHistory);
		tbHistory.getHeaderView().addColumns(
				HISTORY_WIDTHS,
				HISTORY_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
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
	public void updateLayout(FollowProblemItemDTO dto) {
		this.dtoData = dto;
		tvFollowProblemStaff.setText(dtoData.staffCode + " - " + dtoData.staffName);
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
			btNewFollowProblemDetail.setVisibility(View.GONE);
			btUpdateFollowProblemDetail.setVisibility(View.GONE);
		} else if (dtoData.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE) {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_BUTTON_DONE));
			btNewFollowProblemDetail.setVisibility(View.VISIBLE);
			btUpdateFollowProblemDetail.setVisibility(View.VISIBLE);
		} else {
			tvFollowProblemStatus.setText(StringUtil.getString(R.string.TEXT_PROBLEM_HAS_APPROVED));
			btNewFollowProblemDetail.setVisibility(View.GONE);
			btUpdateFollowProblemDetail.setVisibility(View.GONE);
		}
		tvTypeProblem.setText(dtoData.type);
		if(!StringUtil.isNullOrEmpty(dtoData.content)){
			tvFollowProblemContent.setText(dtoData.content);
			tvFollowProblemContent.setVisibility(View.VISIBLE);
			llFollowProblemContent.setVisibility(View.VISIBLE);
		}else {
			tvFollowProblemContent.setText(Constants.STR_BLANK);
			tvFollowProblemContent.setVisibility(View.GONE);
			llFollowProblemContent.setVisibility(View.GONE);
		}
		renderDataTableHistory();
	}

	/**
	 * 
	 * SKU
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void renderDataTableHistory() {
		if (dtoData != null && dtoData.vHistory != null && dtoData.vHistory.size() > 0) {
			tbHistory.setVisibility(View.VISIBLE);
			tbHistory.clearAllData();
			tbHistory.hideNoContentRow();
			int i=1;
			for (HistoryItemDTO historyItemDTO: dtoData.vHistory) {
				historyItemDTO.STT = Constants.STR_BLANK + i;
				HistoryRow row = new HistoryRow(context);
				row.render(historyItemDTO);
				i++;
				tbHistory.addRow(row);
			}
		}else{
			tbHistory.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btCloseFollowProblemDetail) {
			listener.onClick(view);
		}else if (view == btUpdateFollowProblemDetail) {
			// send request cap nhat du lieu
			// bat waiting
			FollowProblemView remainFragment = (FollowProblemView) listener.getActivity().getFragmentManager().findFragmentByTag(FollowProblemView.TAG);
			if (remainFragment != null) {
				remainFragment.confirmDoneProblem(dtoData);
			}
		}else if (view == btNewFollowProblemDetail) {
			// send request cap nhat du lieu
			// bat waiting
			FollowProblemView remainFragment = (FollowProblemView) listener.getActivity().getFragmentManager().findFragmentByTag(FollowProblemView.TAG);
			if (remainFragment != null) {
				dtoData.status = FeedBackDTO.FEEDBACK_STATUS_CREATE;// 3 thanhtest
				dtoData.numReturn++;
				dtoData.doneDate = null;
				dtoData.updateUser=String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().userCode);
				dtoData.updateDate=DateUtils.now();
				String mess = Constants.STR_BLANK;
				mess = StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_NEW_FOLLOW_PROBLEM_DETAIL);
				GlobalUtil.showDialogConfirm(remainFragment,(SupervisorActivity) remainFragment.getActivity(),mess, StringUtil.getString(R.string.TEXT_AGREE),ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL,StringUtil.getString(R.string.TEXT_DENY),ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL, dtoData);
			}
		}
	}
}
