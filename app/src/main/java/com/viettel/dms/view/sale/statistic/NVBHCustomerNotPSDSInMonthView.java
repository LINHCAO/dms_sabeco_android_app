/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.FragmentMenuContanst;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem;
import com.viettel.dms.dto.view.VisitDTO;
import com.viettel.dms.dto.view.VisitDTO.VisitItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.VisitRow;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Hiển thị danh sách chưa PSDS của NVBH
 * CustomerNotPSDSInMonthSaleView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  16:24:19 23 Nov 2013
 */
public class NVBHCustomerNotPSDSInMonthView extends BaseFragment implements
		OnItemSelectedListener, OnEventControlListener, OnClickListener,
		VinamilkTableListener {

	private static final int ACTION_ROW_CLICK = 1;
	// main activity
	public static final String TAG = NVBHCustomerNotPSDSInMonthView.class.getName();
	private static final int NUM_ITEM_IN_PAGE = Constants.NUM_ITEM_PER_PAGE;

	// BEGIN USER INFO
	private long userId;
	private String shopId;
	// END USER INFO

	private GlobalBaseActivity parent;
	private Spinner spTuyen;
	private String[] arrLineChoose = new String[] { "Tất cả", "Thứ 2", "Thứ 3",
			"Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật" };
	private TextView tvCacLanGheTham;
	private TextView tvMessageCacLanGheTham;
	private VinamilkTableView tbCustomer;
	private VinamilkTableView tbCacLanGheTham;
	private CustomerNotPsdsInMonthReportViewDTO dto;
	private VisitDTO dtoVisit;
	private int indexSpTuyen = 0;
	private boolean isFirst = true;// vo man hinh lan dau tien hay kg?
	private int currentPage = -1;
	private ScrollView mScrollView;

	public static NVBHCustomerNotPSDSInMonthView getInstance(Bundle b) {
		NVBHCustomerNotPSDSInMonthView instance = new NVBHCustomerNotPSDSInMonthView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_not_psds_in_month, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initUserInfo();
		setTitleHeaderView(StringUtil.getString(R.string.TEXT_CUSTOMER_NOT_PSDS_IN_MONTH));
		initHeadermenu();
		initView(v);
		if (isFirst) {
			getReport(0);
		}
		return v;
	}

	private void initHeadermenu() {
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.NVBH_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_NVBH_REPORT_CUSTOMER_NOT_PSDS);
	}


	private void initView(View view) {
		spTuyen = (Spinner) view.findViewById(R.id.spTuyen);
		spTuyen.setOnItemSelectedListener(this);

		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrLineChoose);
		spTuyen.setAdapter(adapterLine);

		mScrollView = (ScrollView) view.findViewById(R.id.mScrollView); 
		
		tvCacLanGheTham = (TextView) view.findViewById(R.id.tvCacLanGheTham);
		tvMessageCacLanGheTham = (TextView) view.findViewById(R.id.tvMessageCacLanGheTham);
		tvMessageCacLanGheTham.setVisibility(View.INVISIBLE);

		tbCustomer = (VinamilkTableView) view.findViewById(R.id.tbCustomer);
		tbCustomer.setNumItemsPage(NUM_ITEM_IN_PAGE);
		tbCustomer.getHeaderView().addHeader(NVBHCustomerNotPSDSInMonthRow.getTableHeader(parent));
		tbCustomer.setListener(this);

		tbCacLanGheTham = (VinamilkTableView) view.findViewById(R.id.tbCacLanGheTham);
		tbCacLanGheTham.getHeaderView().addHeader(VisitRow.getTableHeader(parent));
		tbCacLanGheTham.setListener(this);
		tbCacLanGheTham.setVisibility(View.INVISIBLE);
	}

	/**
	 * lay tuyen
	 * 
	 * @param date
	 * @return
	 */
	private int getVisitPlan(String date) {
		int d = 0;
		if (date.equals("Thứ 2")) {
			d = 2;
		} else if (date.equals("Thứ 3")) {
			d = 3;
		} else if (date.equals("Thứ 4")) {
			d = 4;
		} else if (date.equals("Thứ 5")) {
			d = 5;
		} else if (date.equals("Thứ 6")) {
			d = 6;
		} else if (date.equals("Thứ 7")) {
			d = 7;
		} else if (date.equals("Chủ nhật")) {
			d = 8;
		}
		return d;
	}
	
	/**
	 * Get user info
	 * @author: duongdt3
	 * @since: 10:58:01 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void initUserInfo() {
		userId = GlobalInfo.getInstance().getProfile().getUserData().id;
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
	}

	/**
	 * Get report from DB
	 * @author: duongdt3
	 * @since: 11:04:03 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param page
	 */
	private void getReport(int page) {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();

		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId );
		b.putString(IntentConstants.INTENT_STAFF_ID, userId + "");
		b.putInt(IntentConstants.INTENT_NUM_ITEM_IN_PAGE, NUM_ITEM_IN_PAGE);
		
		if (page > 0) {
			b.putInt(IntentConstants.INTENT_PAGE, page);
			currentPage = page;
		}else{
			currentPage = 1;
		}
		
		if (arrLineChoose != null && indexSpTuyen < arrLineChoose.length) {
			b.putInt(IntentConstants.INTENT_DAY_IN_WEEK, getVisitPlan(arrLineChoose[indexSpTuyen]));
		}

		e.viewData = b;
		e.action = ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * render table layout
	 * @author: duongdt3
	 * @since: 11:04:16 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		if (dto.totalList >= 0) {
			tbCustomer.setTotalSize(dto.totalList);	
			tbCacLanGheTham.invalidate();
		}
		tbCustomer.getPagingControl().setCurrentPage(currentPage);
		
		tvMessageCacLanGheTham.setVisibility(View.GONE);
		tbCacLanGheTham.setVisibility(View.GONE);
		tvCacLanGheTham.setVisibility(View.INVISIBLE);
		int pos = 1 + NUM_ITEM_IN_PAGE
				* (tbCustomer.getPagingControl().getCurrentPage() - 1);
		tbCustomer.clearAllData();
		if(dto.arrList.size()>0){
			for (CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem dto1: dto.arrList) {
				NVBHCustomerNotPSDSInMonthRow row = new NVBHCustomerNotPSDSInMonthRow(parent, this, ACTION_ROW_CLICK);
				row.render(pos, dto1);
				pos++;
				tbCustomer.addRow(row);
			}
		}else{
			tbCustomer.showNoContentRow();
		}
		this.parent.closeProgressDialog();
		
		// Khi scroll xuong duoi, chuyen trang thi scroll len tren
		setScrollToPosition(0, 0);
	}

	/**
	 * Reset row background
	 * @author: duongdt3
	 * @since: 11:04:28 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void resetBackgroundRows() {
		List<TableRow> list = tbCustomer.getListRowView();
		NVBHCustomerNotPSDSInMonthRow row;
		//doi background thanh binh thuong cho tat ca dong
		for (int i = 0, size = list.size(); i < size; i++) {
			row = (NVBHCustomerNotPSDSInMonthRow) list.get(i);
			row.changeBackground(false);
		}		
	}

	/**
	 * Render table visit layout
	 * @author: duongdt3
	 * @since: 11:04:54 16 Dec 2013
	 * @return: void
	 * @throws:
	 */
	private void renderLayoutVisit() {
		tvCacLanGheTham.setVisibility(View.VISIBLE);
		tvMessageCacLanGheTham.setVisibility(View.GONE);
		tbCacLanGheTham.setVisibility(View.VISIBLE);
		if (dtoVisit != null) {
			List<TableRow> listRows = new ArrayList<TableRow>();
			for (int i = 0, s = dtoVisit.arrList.size(); i < s; i++) {
				VisitItem item = dtoVisit.arrList.get(i);
				item.stt = "" + (i + 1);
				VisitRow row = new VisitRow(parent);
				row.render(item);
				listRows.add(row);
			}
			tbCacLanGheTham.addContent(listRows);
		}
		
		scrollToListVisited();
	}

	/**
	 * Scroll man hinh toi danh sach ghe tham
	 * 
	 * @author: QuangVT1
	 * @since: 3:10:55 PM Jan 6, 2014
	 * @return: void
	 * @throws:
	 */
	private void scrollToListVisited() {
		final int x = tbCustomer.getLeft();
    	final int y = tbCustomer.getBottom();
		setScrollToPosition(x, y);
	}

	/**
	 * Scroll man hinh toi 1 vi tri
	 * 
	 * @author quangvt1 
	 * @param x
	 * @param y
	 */
	private void setScrollToPosition(final int x, final int y) {
		mScrollView.post(new Runnable() { 
	        public void run() {  
	             mScrollView.scrollTo(x, y);
	        } 
		});
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH:
			dto = (CustomerNotPsdsInMonthReportViewDTO) modelEvent.getModelData();
			renderLayout();
			break;
		case ActionEventConstant.GET_LIST_VISIT:
			dtoVisit = (VisitDTO) modelEvent.getModelData();
			renderLayoutVisit();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbCustomer) {
			int page = tbCustomer.getPagingControl().getCurrentPage();
			getReport(page);
			// load more data for table product list
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
    	if(action == ACTION_ROW_CLICK){
    		tvCacLanGheTham.setVisibility(View.VISIBLE);
    		tvCacLanGheTham.setText("Các lần ghé thăm ("
    				+ ((CustomerNotPsdsInMonthReportItem) data).customerCode + " - "
    				+ ((CustomerNotPsdsInMonthReportItem) data).customerName + ")");
    		NVBHCustomerNotPSDSInMonthRow row = (NVBHCustomerNotPSDSInMonthRow) control;
    		resetBackgroundRows();
    		//sau do doi lai background cho dong duoc chon
    		row.changeBackground(true);
    				
    		if (((CustomerNotPsdsInMonthReportItem) data).visitNumberInMonth > 0) {
    			getDanhSachCacLanGheTham((CustomerNotPsdsInMonthReportItem) data); 
    		} else {
    			tvCacLanGheTham.setVisibility(View.VISIBLE);
    			tbCacLanGheTham.setVisibility(View.GONE);
    			tvMessageCacLanGheTham.setVisibility(View.VISIBLE); 
    			 
    			scrollToListVisited();
    		} 
    	}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * request lay danh sach cac lan ghe tham khach hang cua NVBH
	 * @author: duongdt3
	 * @since: 11:05:32 16 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	private void getDanhSachCacLanGheTham(CustomerNotPsdsInMonthReportItem item) {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, item.staffId);
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, item.customerId);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_VISIT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (spTuyen!=null) {
					if(indexSpTuyen != 0){
						spTuyen.setSelection(0);
					}else{
						getReport(0);
					}
				}
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spTuyen && arg0.getSelectedItemPosition() != indexSpTuyen) {
			indexSpTuyen = arg0.getSelectedItemPosition();
			getReport(0);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {

	}

}
