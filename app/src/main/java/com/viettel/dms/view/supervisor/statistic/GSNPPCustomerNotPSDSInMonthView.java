/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.statistic;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO;
import com.viettel.dms.dto.view.CustomerNotPsdsInMonthReportViewDTO.CustomerNotPsdsInMonthReportItem;
import com.viettel.dms.dto.view.ListStaffDTO;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.VisitDTO;
import com.viettel.dms.dto.view.VisitDTO.VisitItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
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
 * Hiển thị danh sách chưa PSDS của GSBH
 * SupervisorCustomerNotPSDSInMonthView.java
 * @author: QuangVT 
 */
public class GSNPPCustomerNotPSDSInMonthView extends BaseFragment implements
		OnItemSelectedListener, OnEventControlListener, OnClickListener,
		VinamilkTableListener {
	
	//-------------- Default --------------//
	// Tag(Phan biet vs fragment khac)
	public static final String TAG = GSNPPCustomerNotPSDSInMonthView.class.getName();
	// Parent(Activity)
	private GlobalBaseActivity parent; 
	// Vao man hinh lan dau tien hay kg?
	private boolean isFirst = true;
	
	//-------------- Constant --------------//
	// So luong item tren 1 trang
	private static final int NUM_ITEM_IN_PAGE = Constants.NUM_ITEM_PER_PAGE;
	// Su kien click 1 dong tren table
	private static final int ACTION_ROW_CLICK = 1;
	// Danh sach cac tuyen
	private String[] arrLineChoose = new String[] { "Tất cả", "Thứ 2", "Thứ 3",
			"Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật" };

	//-------------- User Info --------------//
	// Id user
	private long userId;
	// Id shop
	private String shopId;
	
	//-------------- DTO --------------//
	// DTO danh sach KH chua PDSD
	private CustomerNotPsdsInMonthReportViewDTO dtoNoPSDS;
	// DTO cac lan ghe tham 1 KH
	private VisitDTO dtoVisit; 
	// danh sach nhan vien ban hang
	private ListStaffDTO dtoListStaff;
	
	//-------------- Table --------------//
	// Table danh sach KH chua PSDS
	private VinamilkTableView tbCustomer;
	// Table cac lan ghe tham
	private VinamilkTableView tbVisited;
	// Page hien tai cua bang tbCustomer
	private int currentPage = -1;
	
	//-------------- Spinner --------------//
	// Combobox tuyent
	private Spinner spLine;
	// Combobox NVBH
	private Spinner spStaff;
	// Index Select Spinner Line
	private int indexSelectLine = 0;
	// Index Select Spinner Staff
	private int indexSelectStaff = 0;
	// danh sach NVBH
	private String[] arrStaffChoose = null;
	
	//-------------- TextView --------------//
	// Tv Cac lan ghe tham KH
	private TextView tvVisited;
	// Tv thong bao cac lan ghe tham KH(khi chua ghe tham KH)
	private TextView tvMessageVisited;
	// ScrollView
	private ScrollView mScrollView;

	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author quangvt1
	 * @param b
	 * @return GSBHCustomerNotPSDSInMonthView
	 */
	public static GSNPPCustomerNotPSDSInMonthView getInstance(Bundle b) {
		GSNPPCustomerNotPSDSInMonthView instance = new GSNPPCustomerNotPSDSInMonthView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_gsbh_customer_not_psds_in_month, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		// Lay thong tin khach hang tu Profile
		initUserInfo();
		
		// Khoi tao Title Header
		final String title = StringUtil.getString(R.string.TEXT_CUSTOMER_NOT_PSDS_IN_MONTH_FOR_SUPERVISOR);
		setTitleHeaderView(title);
		
		// Khoi tao menu
		initHeadermenu();
		
		// Binding control va khoi tao
		initView(v); 
		
		if (isFirst) {
			isFirst = false;
			getListSaleStaff();
		} else {
			initSpNVBH();
			renderLayout();
		}
		
		return v;
	}

	/**
	 * Tao adapter cho Spinner Staff
	 * 
	 * @author quangvt1
	 */
	private void initSpNVBH() { 
		arrStaffChoose = new String[dtoListStaff.arrList.size()];
		int i=0;
		for (StaffItem staffItem: dtoListStaff.arrList) {
			if (!StringUtil.isNullOrEmpty(staffItem.code)) {
				arrStaffChoose[i] = staffItem.code + "-" + staffItem.name;
			} else {
				arrStaffChoose[i] = staffItem.name;
			}
			i++;
		}
		SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrStaffChoose);
		spStaff.setAdapter(adapterNVBH);
		indexSelectStaff = spStaff.getSelectedItemPosition();
		//textStaffId = dtoListStaff.arrList.get(spStaff.getSelectedItemPosition()).id;
	}

	/**
	 * Lay danh sach nhan vien cua GSNPP
	 * 
	 * @author quangvt1
	 */
	private void getListSaleStaff() { 
		parent.showLoadingDialog();
		
		final String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		final long staffOwnerId = GlobalInfo.getInstance().getProfile().getUserData().id;
		
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle(); 
		b.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		b.putString(IntentConstants.INTENT_STAFF_OWNER_ID, String.valueOf(staffOwnerId));
		b.putBoolean(IntentConstants.INTENT_ORDER, true);
		b.putBoolean(IntentConstants.INTENT_IS_ALL, true);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_NVBH;
		e.sender = this;
		
		SuperviorController.getInstance().handleViewEvent(e); 
	}

	/**
	 * Khoi tao menu
	 * 
	 * @author quangvt1
	 */
	private void initHeadermenu() {
		// enable menu bar
		enableMenuBar(this, FragmentMenuContanst.GSBH_REPORT_MENU_COMMANDs, FragmentMenuContanst.INDEX_MENU_GSNPP_REPORT_CUSTOMER_NOT_PSDS);
	}

	/**
	 * Binđing control va khoi tao view
	 * 
	 * @author quangvt1
	 * @param view
	 */
	private void initView(View view) {
		spStaff = (Spinner) view.findViewById(R.id.spNVBH);
		spStaff.setOnItemSelectedListener(this);
		
		spLine = (Spinner) view.findViewById(R.id.spTuyen);
		spLine.setOnItemSelectedListener(this);

		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrLineChoose);
		spLine.setAdapter(adapterLine);
		spLine.setSelection(0); 
		
		mScrollView = (ScrollView) view.findViewById(R.id.mScrollView);
		
		tvVisited = (TextView) view.findViewById(R.id.tvCacLanGheTham);
		tvVisited.setVisibility(View.INVISIBLE);
		
		tvMessageVisited = (TextView) view.findViewById(R.id.tvMessageCacLanGheTham);
		tvMessageVisited.setVisibility(View.GONE);

		tbCustomer = (VinamilkTableView) view.findViewById(R.id.tbCustomer);
		tbCustomer.setNumItemsPage(NUM_ITEM_IN_PAGE);
		tbCustomer.getHeaderView().addHeader(GSNPPCustomerNotPSDSInMonthRow.getTableHeader(parent));
		tbCustomer.setListener(this);

		tbVisited = (VinamilkTableView) view.findViewById(R.id.tbCacLanGheTham);
		tbVisited.getHeaderView().addHeader(VisitRow.getTableHeader(parent));
		tbVisited.setListener(this);
		tbVisited.setVisibility(View.INVISIBLE);
	}

	/**
	 * Lay tuyen tu 1 chuoi ngay
	 * 
	 * @author quangvt1
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
	 * Set thong tin User tu Profile
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void initUserInfo() {
		userId = GlobalInfo.getInstance().getProfile().getUserData().id;
		shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
	}

	/**
	 *  Lay danh sach bao cao trong ngay
	 *  
	 *  @author quangvt1
	 *  @param page
	 *  @return void
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
		if (arrLineChoose != null && indexSelectLine < arrLineChoose.length) {
			int visitPlan = getVisitPlan(arrLineChoose[indexSelectLine]);
			b.putInt(IntentConstants.INTENT_DAY_IN_WEEK, visitPlan);
		}
		if (arrStaffChoose != null && indexSelectStaff < arrStaffChoose.length) {
			// Lay danh sach staffId 
			String staffId = getListStaffIdForReport();
			b.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		}
		e.viewData = b;
		e.action = ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * Lay danh sach Id nhan vien cho bao cao
	 * 
	 * @author quangvt1
	 * @return
	 */
	private String getListStaffIdForReport() {
		String staffId = "";  
		if(indexSelectStaff == 0 && dtoListStaff.arrList.size() > 1){
			ArrayList<String> arrStaffNoAll = new ArrayList<String>();
			for (int i = 1; i < dtoListStaff.arrList.size(); i++) {
				String id = dtoListStaff.arrList.get(i).id;
				arrStaffNoAll.add(id);
			} 
			staffId = TextUtils.join(",", arrStaffNoAll);
		}else{
			if(indexSelectStaff >= 0 & indexSelectStaff < dtoListStaff.arrList.size()) {
				staffId = dtoListStaff.arrList.get(indexSelectStaff).id;
			}
		} 
		return staffId;
	}

	/**
	 * Render danh sach khach hang
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void renderLayout() {
		if (dtoNoPSDS.totalList >= 0) {
			tbCustomer.setTotalSize(dtoNoPSDS.totalList);	
			tbVisited.invalidate();
		}
		tbCustomer.getPagingControl().setCurrentPage(currentPage);
		
		tvMessageVisited.setVisibility(View.GONE);
		tbVisited.setVisibility(View.GONE);
		tvVisited.setVisibility(View.INVISIBLE);
		
		final int currPage = tbCustomer.getPagingControl().getCurrentPage();
		int pos = 1 + NUM_ITEM_IN_PAGE * ( currPage - 1);
		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0, s = dtoNoPSDS.arrList.size(); i < s; i++, pos++) {
			GSNPPCustomerNotPSDSInMonthRow row = new GSNPPCustomerNotPSDSInMonthRow(parent, this, ACTION_ROW_CLICK);
			row.render(pos, dtoNoPSDS.arrList.get(i)); 
			listRows.add(row);
		}

		tbCustomer.addContent(listRows);
		this.parent.closeProgressDialog();
		
		// Khi scroll xuong duoi, chuyen trang thi scroll len tren
		setScrollToPosition(0, 0);
	}

	/**
	 * Reset background cac row
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void resetBackgroundRows() {
		List<TableRow> list = tbCustomer.getListRowView();
		GSNPPCustomerNotPSDSInMonthRow row;
		//doi background thanh binh thuong cho tat ca dong
		for (int i = 0, size = list.size(); i < size; i++) {
			row = (GSNPPCustomerNotPSDSInMonthRow) list.get(i);
			row.changeBackground(false);
		}		
	}

	/**
	 * Tao view danh sach cac lan vieng tham khach hang cua NVBH
	 * 
	 * @author quangvt1
	 * @return void
	 */
	private void renderLayoutVisit() {
		tvVisited.setVisibility(View.VISIBLE);
		tvMessageVisited.setVisibility(View.GONE);
		tbVisited.setVisibility(View.VISIBLE);
		if (dtoVisit != null) {
			List<TableRow> listRows = new ArrayList<TableRow>();
			for (int i = 0, s = dtoVisit.arrList.size(); i < s; i++) {
				VisitItem item = dtoVisit.arrList.get(i);
				item.stt = "" + (i + 1);
				VisitRow row = new VisitRow(parent);
				row.render(item);
				listRows.add(row);
			}
			tbVisited.addContent(listRows);
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


	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH:
			dtoNoPSDS = (CustomerNotPsdsInMonthReportViewDTO) modelEvent.getModelData();
			renderLayout();
			requestInsertLogKPI(HashMapKPI.GSNPP_BAOCAOKHCHUAPSDS, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_LIST_VISIT:
			dtoVisit = (VisitDTO) modelEvent.getModelData();
			renderLayoutVisit();
			break;
		case ActionEventConstant.GET_LIST_NVBH:
			dtoListStaff = (ListStaffDTO) modelEvent.getModelData();
			if (dtoListStaff != null) { 
				initSpNVBH(); 
				
				// Lay danh sach khach hang khong PDSD
				final int page = 0;
				getReport(page);
			}
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
			parent = (GlobalBaseActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCustomer) {
			int page = tbCustomer.getPagingControl().getCurrentPage();
			getReport(page); 
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
    	if(action == ACTION_ROW_CLICK){
    		CustomerNotPsdsInMonthReportItem item = (CustomerNotPsdsInMonthReportItem) data;
    		final String cusCode = item.customerCode;
    		final String cusName = item.customerName; 
    		final String strVisit = StringUtil.getString(R.string.TEXT_OF_VISITS_) + " (" + cusCode + " - " + cusName + ")";
    		tvVisited.setText(strVisit);
    		tvVisited.setVisibility(View.VISIBLE);
    		
    		// Reset Background
    		resetBackgroundRows();
    		
    		//sau do doi lai background cho dong duoc chon
    		GSNPPCustomerNotPSDSInMonthRow row = (GSNPPCustomerNotPSDSInMonthRow) control; 
    		row.changeBackground(true);
    				
    		if (item.visitNumberInMonth > 0) {
    			getListVisited(item);
    		} else {
    			tvVisited.setVisibility(View.VISIBLE);
    			tbVisited.setVisibility(View.GONE);
    			tvMessageVisited.setVisibility(View.VISIBLE);
    			
    			scrollToListVisited();
    		}
    	}
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
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * request lay danh sach cac lan ghe tham khach hang cua NVBH
	 * 
	 * @param item
	 */
	private void getListVisited(CustomerNotPsdsInMonthReportItem item) {
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
				if (spLine != null && spStaff != null) {
					indexSelectLine = 0;
					indexSelectStaff = 0;
					
					spLine.setSelection(0);
					spStaff.setSelection(0);
					
					// Lay danh sach khach hang khong PDSD
					final int page = 0;
					getReport(page);
				}
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {  
		if (parent == spLine || parent == spStaff) {
			int currSelectLine = spLine.getSelectedItemPosition();
			int currSelectStaff = spStaff.getSelectedItemPosition();
			
			if(currSelectLine != indexSelectLine || currSelectStaff != indexSelectStaff){
				indexSelectLine  = currSelectLine;
				indexSelectStaff = currSelectStaff;
				
				// Lay danh sach khach hang khong PDSD
				final int page = 0;
				getReport(page);
			} 
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) { 
	}

}
