/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.TBHVCustomerNotPSDSReportDTO;
import com.viettel.dms.dto.view.TBHVCustomerNotPSDSReportDTO.TBHVCustomerNotPSDSReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;
 
/**
 * Man hinh Khach hang chua PSDS cua TBHV
 * 
 * TBHVReportCustomerNotPSDSView.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  3:56:28 PM Dec 16, 2013
 */
public class TBHVReportCustomerNotPSDSView extends BaseFragment implements
		OnEventControlListener, OnClickListener, VinamilkTableListener {

	/*------------------DEFAUT------------------*/
	//--- Tag
	public static final String TAG = TBHVReportCustomerNotPSDSView.class.getName();
	//--- Parent
	private TBHVActivity parent;	
	
	/*------------------ACTION CONSTANT------------------*/  
	private static final int ACTION_NPP_CLICK = 1;//row clicked  
	private static final int ACTION_GSBH_CLICK = 2;//row clicked  

	// Table for render
	private DMSTableView tbList;
	
	// DTO for render
	private TBHVCustomerNotPSDSReportDTO dto;
	
	// Kiem tram load lan dau tien
	private boolean isFirst = true;

	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author: QuangVT
	 * @since: 3:58:49 PM Dec 16, 2013
	 * @return: TBHVReportCustomerNotPSDSView
	 * @throws:  
	 * @param b
	 * @return
	 */
	public static TBHVReportCustomerNotPSDSView getInstance(Bundle b) {
		TBHVReportCustomerNotPSDSView instance = new TBHVReportCustomerNotPSDSView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final int idLayout = R.layout.layout_tbhv_report_customer_not_psds;
		ViewGroup view = (ViewGroup) inflater.inflate( idLayout, container, false);
		
		// Set title
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_TBHV_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH));
		// Tao menu
		initHeaderMenu();
		tbList = (DMSTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		initHeaderTable(tbList, new TBHVCustomerNotPSDSReportRow(parent, this,-1, -1, this));
		if (isFirst) {
			getReport();
		}
		return v;
	}

	/**
	 * Khoi tao menu
	 * 
	 * @author: QuangVT
	 * @since: 4:01:45 PM Dec 16, 2013
	 * @return: void
	 * @throws:
	 */
	private void initHeaderMenu() { 
		enableMenuBar(this, FragmentMenuContanst.GST_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSBH_REPORT_CUSTOMER_NOT_PSDS);
	}

	@Override
	public void onResume() {
		if (!isFirst) {
			renderLayout();
		}
		super.onResume();
	}
 
	/**
	 * Lay danh sach bao cao trong ngay
	 * 
	 * @author: QuangVT
	 * @since: 4:02:07 PM Dec 16, 2013
	 * @return: void
	 * @throws:
	 */
	private void getReport() {
		// Show dialog loading...
		this.parent.showProgressDialog(getString(R.string.loading));
		
		Bundle b = new Bundle();
		final long staff_owner_id = GlobalInfo.getInstance().getProfile().getUserData().id;
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID,	 String.valueOf(staff_owner_id));
		
		ActionEvent e = new ActionEvent();
		e.viewData = b;
		e.action = ActionEventConstant.GET_TBHV_REPORT_CUSTOMER_NOT_PSDS;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}
 
	/**
	 * Render table report
	 * 
	 * @author: QuangVT
	 * @since: 4:04:01 PM Dec 16, 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		tbList.clearAllData();
		List<DMSTableRow> listRows = new ArrayList<DMSTableRow>();
		TBHVCustomerNotPSDSReportRow row;
		int totalCusVisit = 0, totalCusPSDS = 0, totalCusNotPSDS = 0;
		
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			TBHVCustomerNotPSDSReportItem item = dto.arrList.get(i);
			row = new TBHVCustomerNotPSDSReportRow(parent,this, ACTION_NPP_CLICK, ACTION_GSBH_CLICK, this); 
			row.render(item);
			listRows.add(row);
			tbList.addRow(row);
			totalCusVisit   += item.numCusVisit;
			totalCusPSDS    += item.numCusPSDS;
			totalCusNotPSDS += item.numCusNotPSDS; 
		}
		
		if (dto.arrList.size() > 0) {
			TBHVCustomerNotPSDSReportRow rowTotal = new TBHVCustomerNotPSDSReportRow(parent, this, -1, -1, this);
			rowTotal.renderTotal(totalCusVisit, totalCusPSDS, totalCusNotPSDS);
			listRows.add(rowTotal);
			tbList.addRow(rowTotal);
		}
		
		this.parent.closeProgressDialog();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_TBHV_REPORT_CUSTOMER_NOT_PSDS:
			dto = (TBHVCustomerNotPSDSReportDTO) modelEvent.getModelData();
			renderLayout();
			requestInsertLogKPI(HashMapKPI.GSBH_BAOCAOTONGHOPKHCHUAPSDS, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}
	
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			parent = (TBHVActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
	}
 
	/**
	 * Xu ly cac action
	 * 
	 * @author: QuangVT
	 * @since: 4:04:01 PM Dec 16, 2013
	 * @return: void
	 * @throws:
	 */
	@Override
	public void onEvent(int eventType, View control, Object data) {  
		switch (eventType) {
		case ACTION_NPP_CLICK:  
		case ACTION_GSBH_CLICK:{
			TBHVCustomerNotPSDSReportItem item = (TBHVCustomerNotPSDSReportItem) data;
			final String shopId  = item.NPPId;
			final String staffId = item.GSBHId;
			
			gotoTBHVNotPSDSDetail(shopId, staffId); 
			break;
		}   
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * Di toi man hinh bao cao danh sach KH chua PSDS chi tiet cua GST
	 * 
	 * @author: QuangVT
	 * @since: 2:30:33 PM Dec 18, 2013
	 * @return: void
	 * @throws:  
	 * @param staffId
	 */
	private void gotoTBHVNotPSDSDetail(String shopId, String staffId) {
		Bundle b = new Bundle();			 
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId); 
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID, staffId);
		
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_TBHV_REPORT_CUSTOMER_NOT_PSDS_DETAIL;
		e.sender = this;
		e.viewData = b;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getReport();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	} 
}
