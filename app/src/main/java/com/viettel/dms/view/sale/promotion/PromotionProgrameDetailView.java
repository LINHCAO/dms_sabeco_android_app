/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.PromotionProgrameDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * hien thi chi tiet chuong trinh khuyen mai
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class PromotionProgrameDetailView extends LinearLayout implements OnClickListener {

	public static final int CLOSE_POPUP_DETAIL_PROMOTION =1000;
	// promotion code
	TextView tvPromotionCode;
	// promotion name
	TextView tvPromotionName;
	// promotion from date
	TextView tvPromotionFromDate;
	// promotioin to date
	TextView tvPromotionToDate;
	TextView tvObjectApply;
	// promotion descs
	TextView tvPromotionDescription;
	// button close
	public Button btClosePopupPromotionDetail;
	// parent
	//public SalesPersonActivity parent;
	
	// BaseFragment
	public BaseFragment listener;
	public View viewLayout;
	PromotionProgrameDTO dtoData;
	// doi tuong ap dung
	TextView tvPromotionObjectApply;
	// label doi tuong ap dung
	TextView lbPromotionObjectApply;
	// linearlayout doi tuong ap dung
	LinearLayout llPromotionObjectApply;
	/**
	 * @param context
	 * @param attrs
	 */
	public PromotionProgrameDetailView(Context context, BaseFragment listener) {
		super(context);
		this.listener = listener;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_promotion_detail, null);
		btClosePopupPromotionDetail = (Button) viewLayout.findViewById(R.id.btClosePromotionDetail);
		btClosePopupPromotionDetail.setOnClickListener(this);
		tvPromotionCode = (TextView) viewLayout.findViewById(R.id.tvPromotionCode);
		tvPromotionName = (TextView) viewLayout.findViewById(R.id.tvPromotionName);
		tvPromotionFromDate = (TextView) viewLayout.findViewById(R.id.tvPromotionFromDate);
		tvPromotionToDate = (TextView) viewLayout.findViewById(R.id.tvPromotionToDate);
		tvPromotionDescription = (TextView) viewLayout.findViewById(R.id.tvPromotionDescription);
		tvPromotionObjectApply = (TextView) viewLayout.findViewById(R.id.tvPromotionObjectApply);
		llPromotionObjectApply = (LinearLayout) viewLayout.findViewById(R.id.llPromotionObjectApply);
		lbPromotionObjectApply = (TextView) viewLayout.findViewById(R.id.lbPromotionObjectApply);
	}
	
	/**
	 * 
	*  update layout
	*  @author: HaiTC3
	*  @return: void
	*  @throws:11383
	 */
	public void updateLayout(PromotionProgrameDTO dto){
		this.dtoData = dto;
		tvPromotionCode.setText(dtoData.PROMOTION_PROGRAM_CODE);
		tvPromotionName.setText(dtoData.PROMOTION_PROGRAM_NAME);
		tvPromotionFromDate.setText(dtoData.FROM_DATE);
		tvPromotionToDate.setText(dtoData.TO_DATE);
		String descrip = Constants.STR_BLANK;
		if (dtoData.DESCRIPTION != null && dtoData.DESCRIPTION.length() > 0) {
			descrip = dtoData.DESCRIPTION;
		}
		tvPromotionDescription.setText(Html.fromHtml(descrip));
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType== UserDTO.TYPE_GSNPP){
			llPromotionObjectApply.setVisibility(View.GONE);
			lbPromotionObjectApply.setVisibility(View.GONE);
			tvPromotionObjectApply.setVisibility(View.GONE);
		}else {
			llPromotionObjectApply.setVisibility(View.VISIBLE);
			lbPromotionObjectApply.setVisibility(View.VISIBLE);
			tvPromotionObjectApply.setVisibility(View.VISIBLE);
			tvPromotionObjectApply.setText(Constants.STR_BLANK);
		}
	
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view==btClosePopupPromotionDetail){
			listener.onClick(view);
		}
	}
}
