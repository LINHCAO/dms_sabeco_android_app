/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.FragmentMenuContanst.MenuCommand;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.db.ProInfoDTO;
import com.viettel.dms.dto.view.SaleSupportProgramDetailModel;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;

/**
 * 
 * Chi tiet chuong trinh HTBH
 * @author: dungnt19
 * @version: 1.0 
 * @since:  16:51:46 06-05-2014
 */
public class SaleSupportProgramDetailView extends BaseFragment {

	public static final String TAG = SaleSupportProgramDetailView.class.getName();
	public static final int CLOSE_POPUP_DETAIL_PROMOTION =1000;
	TextView tvPromotionName;
	TextView tvTime;
	TextView tvCustomerApply;
	TextView tvCategory;
	TextView tvBudget;
	TextView tvContent;
		
	SaleSupportProgramDetailModel dtoData = new SaleSupportProgramDetailModel();
	private GlobalBaseActivity parent;
	boolean isFirst = true;
	
	public static SaleSupportProgramDetailView getInstance(Bundle args) {
		SaleSupportProgramDetailView instance = new SaleSupportProgramDetailView();
		// Supply index input as an argument.
		instance.setArguments(args);

		return instance;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (SalesPersonActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.layout_sale_support_detail, null);
		View view = super.onCreateView(inflater, v, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_SALE_SUPPORT_PROGRAM_INFO));
		Bundle args = getArguments(); 
		ProInfoDTO saleSupportDTO = (ProInfoDTO) args.getSerializable(IntentConstants.INTENT_PROMOTION); 
		
		if(saleSupportDTO != null) {
			FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE.setDataInfo(args);
		} else {
			Bundle bundle = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE.getDataInfo();
			saleSupportDTO = (ProInfoDTO) bundle.getSerializable(IntentConstants.INTENT_PROMOTION);
		}
		
		MenuCommand menu = null;
		if(saleSupportDTO.isHaveDone){
			menu = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMAND_HAVE_DONE;
			enableMenuBar(this, menu, FragmentMenuContanst.INDEX_MENU_NVBH_PROGRAM_INFO); 
		}else{
			menu = FragmentMenuContanst.NVBH_PROGRAM_MENU_COMMANDs;
			enableMenuBar(this, menu, FragmentMenuContanst.INDEX_MENU_NVBH_PROGRAM_INFO - 1); 
		}
		
		 
		dtoData.programeId = saleSupportDTO.PRO_INFO_ID; 
		
		tvPromotionName = (TextView) view.findViewById(R.id.tvPromotionName);
		tvTime = (TextView) view.findViewById(R.id.tvTime);
		tvCategory = (TextView) view.findViewById(R.id.tvCategory);
		tvCustomerApply = (TextView) view.findViewById(R.id.tvCustomerApply);
		tvBudget = (TextView) view.findViewById(R.id.tvBudget);
		tvContent = (TextView) view.findViewById(R.id.tvContent);
		
		if(isFirst) {
			getSaleSupportProgrameInfo();
		}
		return view;
	}
	

	/**
	 * Lay thong tin chi tiet CT HTBH
	 * @author: dungnt19
	 * @since: 09:04:28 08-05-2014
	 * @return: void
	 * @throws:  
	 */
	private void getSaleSupportProgrameInfo() {   
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.ACTION_GET_SALE_SUPPORT_PROGRAM_INFO;
		Bundle bundle = new Bundle();
		bundle.putLong(IntentConstants.INTENT_ID, dtoData.programeId);
		e.viewData = bundle;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(!isFirst) {
			updateLayout();
		}
	}
	
	/**
	 * Update layout
	 * @author: dungnt19
	 * @since: 15:14:43 08-05-2014
	 * @return: void
	 * @throws:
	 */
	public void updateLayout(){
		tvPromotionName.setText(dtoData.programeCode + " - " + dtoData.programeName);
		tvTime.setText(dtoData.fromDate + " - " + dtoData.toDate);
		tvCustomerApply.setText(dtoData.customerTypeName);
		tvCategory.setText(dtoData.category);
		tvBudget.setText(dtoData.budget);
		tvContent.setText(dtoData.content);
	}
	
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.ACTION_GET_SALE_SUPPORT_PROGRAM_INFO: {
			isFirst = false;
			SaleSupportProgramDetailModel modelData = (SaleSupportProgramDetailModel) modelEvent.getModelData();
			dtoData = modelData;

			updateLayout();
			requestInsertLogKPI(HashMapKPI.NVBH_THONGTINCHUNGCHUONGTRINHHOTROBANHANG, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}
	
	@Override
	public void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) { 
				getSaleSupportProgrameInfo();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	} 
}
