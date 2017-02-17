/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.dto.view.ReportNVBHVisitCustomerDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.global.Profile;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkHeaderTable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

 
/**
 * 02-04. Canh bao ghe tham tren tuyen
 * 
 * GSNPPReportVisitCustomerOnPlanOfDayView.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  2:48:48 PM Dec 4, 2013
 */
public class GSNPPReportVisitCustomerOnPlanOfDayView extends BaseFragment implements
		VinamilkTableListener {
	/*-------------- DEFAULT--------------*/
	// tag for fragment
	public static final String TAG = GSNPPReportVisitCustomerOnPlanOfDayView.class.getName();
	// get main activity
	private SupervisorActivity parent;
	
	/*-------------- MENU CONSTANT--------------*/
	private static final int MENU_STAFF_TAB 		 = 0;	// menu lo trinh
	private static final int MENU_STAFF_POSITION 	 = 1;	// menu xem vi tri
	private static final int MENU_STAFF_TIMEKEEPING  = 2; 	// menu cham cong
	private static final int MENU_STAFF_GOING_ONLINE = 3; 	// menu di tuyen
	private static final int MENU_LIST_ORDER = 4;

	/*--------------CONSTANT--------------*/
	private final String CODE_START_TIME  = "DT_START";		// start time
	private final String CODE_MIDDLE_TIME = "DT_MIDDLE";	// middle time
	private final String CODE_END_TIME 	  = "DT_END";		// end time

	/*--------------STRING COMPARE--------------*/
	private String startTime  = Constants.STR_BLANK; // start time compare
	private String middleTime = Constants.STR_BLANK; // middle time compare
	private String endTime 	  = Constants.STR_BLANK; // end time compare
 
	/*--------------TABLE--------------*/ 
	//--- table report visit customer 
	private VinamilkTableView tbReportVisitCustomer; 
	//--- Width of table
	private static int[] HEADER_TABLE_WIDTHS = { 100, 200, 205, 105, 105, 105, 60 }; 
	//--- List header for table
	private ArrayList<ShopParamDTO> listTimeHeader = new ArrayList<ShopParamDTO>();  
		
	/*--------------VARIABLES--------------*/ 
	//--- Flag check load data the first
	public boolean isDoneLoadFirst = false; 
	//--- Curent percent 
	private int currentPercent = 0;  
	//--- List report info of NVBH
	private ArrayList<ReportNVBHVisitCustomerDTO> listReportNVBHInfo = new ArrayList<ReportNVBHVisitCustomerDTO>();

	
	/**
	 * Lay the hien cua doi tuong
	 * 
	 * @author: QuangVT
	 * @since: 2:44:53 PM Dec 4, 2013
	 * @return: ReportVisitCustomerOnPlanOfDayView
	 * @throws:  
	 * @param data
	 * @return
	 */
	public static GSNPPReportVisitCustomerOnPlanOfDayView getInstance(Bundle data) {
		GSNPPReportVisitCustomerOnPlanOfDayView instance = new GSNPPReportVisitCustomerOnPlanOfDayView();
		instance.setArguments(data);
		return instance;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final int layoutID = R.layout.layout_gsnpp_report_visit_customer_on_plan_of_day_view;
		ViewGroup view = (ViewGroup) inflater.inflate(layoutID, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		
		// Kho tao view
		this.initView(v);
		
		// Set title cho man hinh
		this.setTitleForScreen();
		
		// Khoi tao menu
		this.initMenuActionBar();
		
		// Kiem tra load lan dau
		if (!this.isDoneLoadFirst) {
			this.getTimeDefineInShopparam();
		}
		
		return v;
	}


	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (SupervisorActivity) activity;
	}

	/**
	 * Set title cho man hinh
	 * 
	 * @author: QuangVT
	 * @since: 2:45:43 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	public void setTitleForScreen() {
		final String title = getString(R.string.TITLE_VIEW_GSNPP_REPORT_VISIT_CUSTOMER_ON_PLAN);
		setTitleHeaderView(title);
	} 
	
	/**
	 * Lay thoi gian start_time, middle_time va end_time trong bang shop_param
	 * 
	 * @author: QuangVT
	 * @since: 2:57:14 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	private void getTimeDefineInShopparam() {
		// Show dialog loading...
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		
		// Get info
		final Profile profile = GlobalInfo.getInstance().getProfile();
		final UserDTO userDTO = profile.getUserData(); 
		
		ActionEvent e = new ActionEvent();
		Bundle data   = new Bundle();  
		data.putString(IntentConstants.INTENT_SHOP_ID, String.valueOf(userDTO.shopIdProfile));
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(userDTO.id));

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GSNPP_GET_TIME_OF_HEADER_DEFINE_SHOP_PARAM;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	 
	/**
	 * Lay bao cao NVBH ghe tham khach hang trong ngay
	 * 
	 * @author: QuangVT
	 * @since: 2:46:44 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	private void getListReportNVBHVisitCustomer() {
		// Show dialog loading...
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
				
		// Get info
		final Profile profile = GlobalInfo.getInstance().getProfile();
		final UserDTO userDTO = profile.getUserData();
		
		ActionEvent e = new ActionEvent();
		Bundle data   = new Bundle();  
		data.putString(IntentConstants.INTENT_SHOP_ID, String.valueOf(userDTO.shopId));
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(userDTO.id));
		data.putString(IntentConstants.INTENT_START_TIME_COMPARE, startTime);
		data.putString(IntentConstants.INTENT_MIDDLE_TIME_COMPARE, middleTime);
		data.putString(IntentConstants.INTENT_END_TIME_COMPARE, endTime);

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER;
		SuperviorController.getInstance().handleViewEvent(e);
	}


	@Override
	public void onResume() {
		if (this.isDoneLoadFirst) {
			this.isDoneLoadFirst = false;
			this.renderDataForTable();
			this.isDoneLoadFirst = true;
		}
		super.onResume();
	}

	/**
	 * Khoi tao table
	 * 
	 * @author: QuangVT
	 * @since: 2:47:39 PM Dec 4, 2013
	 * @return: void
	 * @throws:  
	 * @param v
	 */
	private void initView(View v) {
		tbReportVisitCustomer = (VinamilkTableView) v.findViewById(R.id.tbReportVisitCustomer);
		tbReportVisitCustomer.setOnClickListener(this);
	}

	/**
	 * Tao menu
	 * 
	 * @author: QuangVT
	 * @since: 2:47:53 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_INVOICE_LIST), R.drawable.icon_order,
				MENU_LIST_ORDER);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE),
				R.drawable.icon_task, MENU_STAFF_GOING_ONLINE);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING),
				R.drawable.icon_clock, MENU_STAFF_TIMEKEEPING);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION),
				R.drawable.icon_map, MENU_STAFF_POSITION);
		addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING),
				R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		setMenuItemFocus(2);
	} 
	
	/**
	 * Render table
	 * 
	 * @author: QuangVT
	 * @since: 4:17:57 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	private void renderDataForTable() {
		if (listTimeHeader.size() > 0) { 
			
			// display header
			if (!this.isDoneLoadFirst &&  this.listTimeHeader.size() >= 3) {  
					renderHeaderTable(); 
			}

			// display data row
			if (listReportNVBHInfo != null && listReportNVBHInfo.size()>0) { 
				tbReportVisitCustomer.clearAllData();
				tbReportVisitCustomer.hideNoContentRow();
				for (ReportNVBHVisitCustomerDTO reportNVBHVisitCustomerDTO: listReportNVBHInfo) {
					GSNPPReportVisitCustomerOnPlanOfDayRow row = new GSNPPReportVisitCustomerOnPlanOfDayRow(parent, this);
					row.renderLayout(reportNVBHVisitCustomerDTO);
					tbReportVisitCustomer.addRow(row);
				}
			}else{
				tbReportVisitCustomer.showNoContentRow();
			}
		}
	}

	/**
	 * Tao header cho table
	 * 
	 * @author: QuangVT
	 * @since: 3:04:02 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	private void renderHeaderTable() {
		// Remove All Column
		tbReportVisitCustomer.getHeaderView().removeAllColumns();
		
		// Columns header Table
		final String column1 = StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE);
		final String column2 = StringUtil.getString(R.string.TEXT_HEADER_TABLE_STAFF_NAME);
		final String column3 = this.getDescHeaderWithCode(CODE_START_TIME).trim();
		final String column4 = this.getDescHeaderWithCode(CODE_MIDDLE_TIME).trim();
		final String column5 = this.getDescHeaderWithCode(CODE_END_TIME).trim();
		final String column6 = StringUtil.getString(R.string.TEXT_HEADER_TABLE_CURRENT_TIME) + "(" + this.currentPercent + "%)";
		final String column7 = " "; 
		
		// List header table
		String[] HEADER_TABLE_TITLES = {column1,column2, column3, column4, column5, column6, column7}; 
		
		// Add column header for table
		final int textColor = ImageUtil.getColor(R.color.BLACK);
		final int bgColor   = ImageUtil.getColor(R.color.TABLE_HEADER_BG);
		VinamilkHeaderTable header =  tbReportVisitCustomer.getHeaderView();
		header.addColumns(HEADER_TABLE_WIDTHS, HEADER_TABLE_TITLES, textColor, bgColor);
	} 
	
	/**
	 *  Update date for current data
	 * 
	 * @author: QuangVT
	 * @since: 4:18:14 PM Dec 4, 2013
	 * @return: void
	 * @throws:
	 */
	@SuppressLint("SimpleDateFormat")
	public void updateDataforCurrentData() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		final String timeNow = sdf.format(cal.getTime());
		//final String startTime  = this.getValueTimeWithCode(CODE_START_TIME);
		//final String middleTime = this.getValueTimeWithCode(CODE_MIDDLE_TIME);
		//final String endTime    = this.getValueTimeWithCode(CODE_END_TIME);

		long disNowWithMiddle = DateUtils.getDistanceMinutesFrom2HoursNegative(middleTime, timeNow);
		long disNowWithEnd    = DateUtils.getDistanceMinutesFrom2HoursNegative(endTime, timeNow);
		long minuTotalInDay   = DateUtils.getDistanceMinutesFrom2HoursNegative(middleTime, endTime) * 2;  
		long disCurrWithStart = disNowWithMiddle + minuTotalInDay / 2;
		
		int currentPercentTotal = (int) (((float) disCurrWithStart / (float) minuTotalInDay) * 100);
		currentPercentTotal = (currentPercentTotal <= 100) ? currentPercentTotal : 100;  
		this.currentPercent = currentPercentTotal; 
		  
		for (int i = 0, size = this.listReportNVBHInfo.size(); i < size; i++) {
			ReportNVBHVisitCustomerDTO dto = this.listReportNVBHInfo.get(i);

			// Highlight Middle
			dto.maxNumCustomerVisitMiddle = (int) (Math.ceil((double) dto.numTotalCustomerVisit / 2)); 
			if (disNowWithMiddle > 0 && dto.numCustomerVisitedMiddle < dto.maxNumCustomerVisitMiddle) {
				dto.isHighLightMiddle = true;
			}  
			
			// Highlight End
			dto.maxNumCustomerVisitEnd = dto.numTotalCustomerVisit;
			if (disNowWithEnd > 0
					&& dto.numCustomerVisitedEnd < dto.maxNumCustomerVisitEnd) {
				dto.isHighLightEnd = true;
			}
			
			// Highlight Current
			int percent = (int) (((float) dto.numCustomerCurrentVisitedInPlan / (float) dto.numTotalCustomerVisit) * 100);
			if (percent < this.currentPercent) {
				dto.isHighLightCurrent = true;
			}

			// Highlight NVBH
			if (dto.isHighLightEnd || dto.isHighLightMiddle || dto.isHighLightCurrent) {
				dto.isHighLightNVBH = true;
			}
			
			// Kiem tra xem hien tai hien thi theo cot giua hay cuoi
			// Neu thoi gian hien tai <= thoi gian giua thi hien thi theo thoi gian giua
			// Nguoc lai hien thi theo thoi gian cuoi
			if (disNowWithMiddle <= 0){
				dto.viewCurrenFollow = ReportNVBHVisitCustomerDTO.FOLLOW_MIDDLE;
			}else{
				dto.viewCurrenFollow = ReportNVBHVisitCustomerDTO.FOLLOW_END;
			}
		} 
	}  
	
	/**
	 * Lay header tuong ung voi ma code trong shop param
	 * 
	 * @author: QuangVT
	 * @since: 3:37:23 PM Dec 4, 2013
	 * @return: String
	 * @throws:  
	 * @param strCode
	 * @return
	 */
	public String getDescHeaderWithCode(String strCode) {
		String result = Constants.STR_BLANK;
		for (int i = 0, size = this.listTimeHeader.size(); i < size; i++) {
			if (this.listTimeHeader.get(i).type.contains(strCode)) {
				result = this.listTimeHeader.get(i).name;
				break;
			}
		}
		return result;
	}
 
	
	/**
	 * Lay gia tri thoi gian tuong ung voi ma code trong shop param
	 * 
	 * @author: QuangVT
	 * @since: 3:14:02 PM Dec 4, 2013
	 * @return: String
	 * @throws:  
	 * @param strCode : ma code trong apparam
	 * @return
	 */
	public String getValueTimeWithCode(String strCode) {
		String result = Constants.STR_BLANK;
		for (int index = 0, size = this.listTimeHeader.size(); index < size; index++) {
			final String type = this.listTimeHeader.get(index).type;
			if (type.contains(strCode)) {
				result = this.listTimeHeader.get(index).code;
				break;
			}
		}
		return result;
	}


	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}


	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		if (action == ActionEventConstant.GO_TO_MAP_FROM_REPORT_VISIT_CUSTOMER) {
			this.gotoMapOfNVBH((ReportNVBHVisitCustomerDTO) data);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GSNPP_GET_TIME_OF_HEADER_DEFINE_SHOP_PARAM: {
			listTimeHeader = (ArrayList<ShopParamDTO>) modelEvent
					.getModelData();
			if (listTimeHeader.size() ==3) {
				for (ShopParamDTO dto : listTimeHeader) {
					if (dto.type.contains(CODE_START_TIME)) {
						startTime = dto.code;
					} else if (dto.type.contains(CODE_MIDDLE_TIME)) {
						middleTime = dto.code;
					} else if (dto.type.contains(CODE_END_TIME)) {
						endTime = dto.code;
					}
				}
				this.getListReportNVBHVisitCustomer();
			}else{
				this.parent.closeProgressDialog();
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_VISIT_CUSTOMER),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
			}
			break;
		}
		case ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER: {
			listReportNVBHInfo = (ArrayList<ReportNVBHVisitCustomerDTO>) modelEvent.getModelData();
			this.updateDataforCurrentData();
			renderDataForTable();
			this.isDoneLoadFirst = true;
			this.parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_THEODOIKETQUADITUYENNHANVIEN, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}


	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GSNPP_GET_TIME_OF_HEADER_DEFINE_SHOP_PARAM: {
			this.parent.closeProgressDialog();
			break;
		}
		case ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER: {
			this.parent.closeProgressDialog();
			break;
		}
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}


	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				this.isDoneLoadFirst = false;
				this.getTimeDefineInShopparam();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	} 
	 
	/**
	 * Hien thi ban do di tuyen cua nhan vien ban hang duoc chon
	 * 
	 * @author: QuangVT
	 * @since: 4:18:59 PM Dec 4, 2013
	 * @return: void
	 * @throws:  
	 * @param data
	 */
	public void gotoMapOfNVBH(ReportNVBHVisitCustomerDTO data) {
		// Get info
		final Profile profile = GlobalInfo.getInstance().getProfile();
		final UserDTO userDTO = profile.getUserData();
				
		GsnppRouteSupervisionItem dto = new GsnppRouteSupervisionItem();
		dto.aStaff = new StaffDTO();
		dto.aStaff.staffId 	 = Integer.valueOf(data.staffId);
		dto.aStaff.staffCode = data.staffCode;
		dto.aStaff.name 	 = data.staffName;
		dto.aStaff.shopId 	 = Integer.valueOf(userDTO.shopId);
		dto.lat = data.latPosition;
		dto.lng = data.lngPosition;
		
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-04");
		b.putSerializable(IntentConstants.INTENT_STAFF_DTO, dto);
		
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	/**
	 *
	 * go go report visit customer on plan view - update for CR0074
	 *
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void gotoListOrder() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GO_TO_LIST_ORDER;
		e.sender = this;
		e.viewData = b;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case MENU_STAFF_TAB:
			e.action = ActionEventConstant.GET_GSNPP_ROUTE_SUPERVISION;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case MENU_STAFF_POSITION: {
			Bundle b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.SUPERVISE_STAFF_POSITION;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		case MENU_LIST_ORDER:
			this.gotoListOrder();
			break;
		case MENU_STAFF_GOING_ONLINE:
			break;
		case MENU_STAFF_TIMEKEEPING: {
			Bundle b = new Bundle();
			e.action = ActionEventConstant.GSNPP_GET_LIST_SALE_FOR_ATTENDANCE;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

}
