/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.ShopParamDTO;
import com.viettel.dms.dto.db.StaffDTO;
import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.dto.view.ReportNVBHVisitCustomerDTO;
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO;
import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * bao cao lich su ghe tham cua TBHV tren d/s NPP
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TBHVReportVisitCustomerDayOfDetailNPP extends BaseFragment
		implements VinamilkTableListener, OnItemSelectedListener {
	// tag for fragment
	public static final String TAG = TBHVReportVisitCustomerDayOfDetailNPP.class
			.getName();
	// show tab giam sat lo trinh
	@SuppressWarnings("unused")
	private static final int MENU_STAFF_TAB = 0;
	// sho tab xem vi tri
	private static final int MENU_STAFF_POSITION = 1;
	// show tab cham cong ngay
	private static final int MENU_STAFF_TIMEKEEPING = 2;
	// show tab di tuyen ngay
	private static final int MENU_STAFF_GOING_ONLINE = 3;

	private final String CODE_START_TIME = "DT_START";
	private final String CODE_MIDDLE_TIME = "DT_MIDDLE";
	private final String CODE_END_TIME = "DT_END";

	// table report visit customer
	VinamilkTableView tbReportVisitCustomer;
	// spinner list GSNPP
	Spinner spListGSNPP;
	// index select gsnpp
	int indexGSNPPSelected = -1;
	// flag check load data the first
	public boolean isDoneLoadFirst = false;

	// get main activity
	private TBHVActivity parent;

	// list header for table
	private ArrayList<ShopParamDTO> listTimeHeader = new ArrayList<ShopParamDTO>();
	// list report info of NVBH
	private ArrayList<ReportNVBHVisitCustomerDTO> listReportNVBHInfo = new ArrayList<ReportNVBHVisitCustomerDTO>();

	// start time compare
	private String startTime = Constants.STR_BLANK;
	// middle time compare
	private String middleTime = Constants.STR_BLANK;
	// end time compare
	private String endTime = Constants.STR_BLANK;
	// curent percent
	private int currentPercent = 0;
	// width of table
	private static int[] HEADER_TABLE_WIDTHS = { 100, 175, 200, 115, 115, 115, 60 };
	// list GSNPP
	private ArrayList<TBHVVisitCustomerNotificationItem> listGSNPP = new ArrayList<TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem>();
	// index focus from parent screen
	private int index = -1;

	public static TBHVReportVisitCustomerDayOfDetailNPP getInstance(Bundle data) {
		TBHVReportVisitCustomerDayOfDetailNPP instance = new TBHVReportVisitCustomerDayOfDetailNPP();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_report_visit_customer_detail_npp_day_view,
				container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		if (getArguments().getSerializable(IntentConstants.INTENT_LIST_GSNPP) != null) {
			this.listGSNPP = (ArrayList<TBHVVisitCustomerNotificationItem>) getArguments()
					.getSerializable(IntentConstants.INTENT_LIST_GSNPP);
		}
		if (getArguments().getInt(IntentConstants.INTENT_INDEX) >= 0) {
			this.index = getArguments().getInt(IntentConstants.INTENT_INDEX);
		}

		this.initView(v);
		this.setTitleForScreen();

		this.initMenuActionBar();
		if (this.listGSNPP != null && listGSNPP.size() > 0) {
			this.initListGSNPP();
		}
		return v;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		parent = (TBHVActivity) activity;
	}

	/**
	 * 
	 * set title screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	public void setTitleForScreen() {
		SpannableObject obj = new SpannableObject();
		obj.addSpan(
				getString(R.string.TITLE_VIEW_TBHV_REPORT_VISIT_DETAIL_NPP_OF_DAY_VIEW)
						+ " ", ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * 
	 * get time for header of table that define in aparam table on DB
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	private void getTimeDefineInAparam() {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
		String staffId = String.valueOf(this.listGSNPP.get(indexGSNPPSelected).gsnppStaffId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		data.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GSNPP_GET_TIME_OF_HEADER_DEFINE_SHOP_PARAM;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * get list report NVBH visit customer in day
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	private void getListReportNVBHVisitCustomer() {
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		String shopId = String
				.valueOf(this.listGSNPP.get(indexGSNPPSelected).nvbhShopId);
		String staffId = String
				.valueOf(this.listGSNPP.get(indexGSNPPSelected).gsnppStaffId);
		data.putString(IntentConstants.INTENT_SHOP_ID, shopId);
		data.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		data.putString(IntentConstants.INTENT_START_TIME_COMPARE, startTime);
		data.putString(IntentConstants.INTENT_MIDDLE_TIME_COMPARE, middleTime);
		data.putString(IntentConstants.INTENT_END_TIME_COMPARE, endTime);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (this.isDoneLoadFirst) {
			this.isDoneLoadFirst = false;
			this.renderDataForTable();
		}
		super.onResume();
	}

	/**
	 * 
	 * init view control
	 * 
	 * @param v
	 * @return
	 * @return: vodi
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	private void initView(View v) {
		tbReportVisitCustomer = (VinamilkTableView) v
				.findViewById(R.id.tbReportVisitCustomer);
		tbReportVisitCustomer.setOnClickListener(this);
		spListGSNPP = (Spinner) v.findViewById(R.id.spListGSNPP);
		spListGSNPP.setOnItemSelectedListener(this);
	}

	/**
	 * 
	 * init list GSNPP
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	private void initListGSNPP() {
		if (this.listGSNPP.size() > 0) {
			String[] arrGSNPP = new String[this.listGSNPP.size()];
			int i=0;

			for (TBHVVisitCustomerNotificationItem notificationItem: listGSNPP) {
				arrGSNPP[i] = notificationItem.nvbhShopCode + " - "
						+ notificationItem.gsnppStaffName;
				i++;
			}

			SpinnerAdapter adapterGiamSatNPP = new SpinnerAdapter(parent,
					R.layout.simple_spinner_item, arrGSNPP);
			spListGSNPP.setAdapter(adapterGiamSatNPP);
			if (index >= 0) {
				spListGSNPP.setSelection(index);
			} else {
				spListGSNPP.setSelection(0);
			}
		}
	}

	/**
	 * 
	 * init header menu
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	private void initMenuActionBar() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_GOING_ONLINE),
				R.drawable.icon_task, MENU_STAFF_GOING_ONLINE);
		addMenuItem(StringUtil.getString(R.string.TEXT_STAFF_TIMEKEEPING),
				R.drawable.icon_clock, MENU_STAFF_TIMEKEEPING);
		addMenuItem(StringUtil.getString(R.string.TEXT_VIEW_POSITION),
				R.drawable.icon_map, MENU_STAFF_POSITION);
//		addMenuItem(StringUtil.getString(R.string.TEXT_MONITORING),
//				R.drawable.menu_customer_icon, MENU_STAFF_TAB);
		setMenuItemFocus(1);
	}

	/**
	 * 
	 * render layout with data for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 21, 2013
	 */
	private void renderDataForTable() {
		// display data row
		if (listTimeHeader.size() > 0 && listReportNVBHInfo.size() > 0) {
			// display header
			if (listTimeHeader.size() >= 3) {
				tbReportVisitCustomer.clearAllData();
				tbReportVisitCustomer.getHeaderView().removeAllColumns();
				String[] HEADER_TABLE_TITLES = {
						StringUtil
								.getString(R.string.TEXT_HEADER_TABLE_STAFF_CODE),
						StringUtil
								.getString(R.string.TEXT_HEADER_TABLE_STAFF_NAME),
						this.getDescHeaderWithCode(CODE_START_TIME).trim(),
						this.getDescHeaderWithCode(CODE_MIDDLE_TIME).trim(),
						this.getDescHeaderWithCode(CODE_END_TIME).trim(),
						StringUtil
								.getString(R.string.TEXT_HEADER_TABLE_CURRENT_TIME)
								+ "(" + this.currentPercent + "%)", " " };
				tbReportVisitCustomer.getHeaderView().addColumns(
						HEADER_TABLE_WIDTHS, HEADER_TABLE_TITLES,
						ImageUtil.getColor(R.color.BLACK),
						ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			}
			if (!this.isDoneLoadFirst) {
				this.isDoneLoadFirst = true;
			}
			if (listReportNVBHInfo != null && listReportNVBHInfo.size()>0) {
				// add list row
				tbReportVisitCustomer.clearAllData();
				for (ReportNVBHVisitCustomerDTO reportNVBHVisitCustomerDTO: listReportNVBHInfo) {
					TBHVReportVisitCustomerOnPlanOfDayRow row = new TBHVReportVisitCustomerOnPlanOfDayRow(parent, this);
					row.renderLayout(reportNVBHVisitCustomerDTO);
					tbReportVisitCustomer.addRow(row);
				}
			}else{
				tbReportVisitCustomer.showNoContentRow();
			}
		} else {
			
		}
	}

	/**
	 * 
	 * update dat for current data
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void updateDataforCurrentData() {
		Calendar cal = Calendar.getInstance();
		String timeNow = DateUtils.convertDateTimeWithFormat(cal.getTime(), "HH:mm");

		long minutesDistanceMiddle = DateUtils
				.getDistanceMinutesFrom2HoursNegative(
						getValueTimeWithCode(CODE_MIDDLE_TIME), timeNow);
		long minutesDistanceEnd = DateUtils
				.getDistanceMinutesFrom2HoursNegative(
						getValueTimeWithCode(CODE_END_TIME), timeNow);
		long halfTimeMustDo=DateUtils
				.getDistanceMinutesFrom2HoursNegative(
						getValueTimeWithCode(CODE_MIDDLE_TIME), getValueTimeWithCode(CODE_END_TIME));
		long fullTime=halfTimeMustDo*2;
		long minutesDistanceStartToNow= minutesDistanceMiddle+halfTimeMustDo;
		int currentPercentTotal = (int) (((float) minutesDistanceStartToNow / (float) fullTime) * 100);
		currentPercentTotal = (currentPercentTotal <= 100) ? currentPercentTotal
				: 100;
		this.currentPercent = currentPercentTotal;

		for (ReportNVBHVisitCustomerDTO reportNVBHVisitCustomerDTO : listReportNVBHInfo) {
			// dto.maxNumCustomerVisitMiddle = (int) (((float)
			reportNVBHVisitCustomerDTO.maxNumCustomerVisitMiddle = (int) (Math
					.ceil((double) reportNVBHVisitCustomerDTO.numTotalCustomerVisit / 2));
			if (minutesDistanceMiddle > 0
					&& reportNVBHVisitCustomerDTO.numCustomerVisitedMiddle < reportNVBHVisitCustomerDTO.maxNumCustomerVisitMiddle) {
				reportNVBHVisitCustomerDTO.isHighLightMiddle = true;
			}
			reportNVBHVisitCustomerDTO.maxNumCustomerVisitEnd = reportNVBHVisitCustomerDTO.numTotalCustomerVisit;
			if (minutesDistanceEnd > 0
					&& reportNVBHVisitCustomerDTO.numCustomerVisitedEnd < reportNVBHVisitCustomerDTO.maxNumCustomerVisitEnd) {
				reportNVBHVisitCustomerDTO.isHighLightEnd = true;
			}
			int percent = (int) (((float) reportNVBHVisitCustomerDTO.numCustomerCurrentVisitedInPlan / (float) reportNVBHVisitCustomerDTO.numTotalCustomerVisit) * 100);
			if (percent < this.currentPercent) {
				reportNVBHVisitCustomerDTO.isHighLightCurrent = true;
			}

			if (reportNVBHVisitCustomerDTO.isHighLightEnd || reportNVBHVisitCustomerDTO.isHighLightMiddle) {
				reportNVBHVisitCustomerDTO.isHighLightNVBH = true;
			}
		}
	}

	/**
	 * 
	 * get desc for header of table with Code
	 * 
	 * @param strCode
	 * @return
	 * @return: String
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
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
	 * 
	 * get value time with code from apparam
	 * 
	 * @param strCode
	 * @return
	 * @return: String
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public String getValueTimeWithCode(String strCode) {
		String result = Constants.STR_BLANK;
		for (int i = 0, size = this.listTimeHeader.size(); i < size; i++) {
			if (this.listTimeHeader.get(i).type.contains(strCode)) {
				result = this.listTimeHeader.get(i).code;
				break;
			}
		}
		return result;
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		ReportNVBHVisitCustomerDTO dto =(ReportNVBHVisitCustomerDTO) data;
		switch (action) {
		case ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW: {
			Bundle b = new Bundle();
			b.putString(IntentConstants.INTENT_STAFF_ID, dto.staffId);
			b.putString(IntentConstants.INTENT_SHOP_ID, dto.shopID);
			b.putBoolean(IntentConstants.FROM_TBHV_REPORT, true);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.GO_TO_CUSTOMER_ROUTE;
			e.sender = this;
			e.viewData = b;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			break;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GSNPP_GET_TIME_OF_HEADER_DEFINE_SHOP_PARAM: {
			listTimeHeader = (ArrayList<ShopParamDTO>) modelEvent
					.getModelData();
			if (listTimeHeader.size() == 3) {
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
				tbReportVisitCustomer.getHeaderView().removeAllColumns();
				tbReportVisitCustomer.clearAllData();
				GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ERROR_VISIT_CUSTOMER),
						StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1, null, false);
				parent.closeProgressDialog();
			}
			break;
		}
		case ActionEventConstant.GSNPP_GET_LIST_REPORT_NVBH_VISIT_CUSTOMER: {
			listReportNVBHInfo = (ArrayList<ReportNVBHVisitCustomerDTO>) modelEvent
					.getModelData();
			this.updateDataforCurrentData();
			renderDataForTable();
			this.parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSBH_CHITIETCANHBAOGHETHAMTUYEN, modelEvent.getActionEvent().startTimeFromBoot);
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
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				this.isDoneLoadFirst = false;
				this.getTimeDefineInAparam();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * 
	 * hien thi ban do di tuyen cua nhan vien ban hang duoc chon
	 * 
	 * @param data
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void gotoMapOfNVBH(ReportNVBHVisitCustomerDTO data) {
		GsnppRouteSupervisionItem dto = new GsnppRouteSupervisionItem();
		dto.aStaff = new StaffDTO();
		dto.aStaff.staffId = Integer.valueOf(data.staffId);
		dto.aStaff.staffCode = data.staffCode;
		dto.aStaff.shopId = (int) this.listGSNPP.get(indexGSNPPSelected).nvbhShopId;
		dto.aStaff.name = data.staffName;
		dto.lat = data.latPosition;
		dto.lng = data.lngPosition;
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "02-04-01");
		b.putSerializable(IntentConstants.INTENT_STAFF_DTO, dto);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		switch (eventType) {
//		case MENU_STAFF_TAB:
//			e.action = ActionEventConstant.TBHV_ROUTE_SUPERVISION;
//			e.sender = this;
//			TBHVController.getInstance().handleSwitchFragment(e);
//			break;
		case MENU_STAFF_POSITION:
			b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_SUPERVISE_GSNPP_POSITION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case MENU_STAFF_GOING_ONLINE:
			b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_VISIT_CUS_NOTIFICATION;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case MENU_STAFF_TIMEKEEPING:
			b = new Bundle();
			e = new ActionEvent();
			e.action = ActionEventConstant.TBHV_ATTENDANCE;
			e.sender = this;
			e.viewData = b;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * 
	 * hien thi man hinh bao cao cham cong ngay
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 22, 2013
	 */
	public void showReportTakeAttendOfDayScreen() {
		Bundle b = new Bundle();
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.TBHV_ATTENDANCE;
		e.sender = this;
		e.viewData = b;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spListGSNPP
				&& arg0.getSelectedItemPosition() != indexGSNPPSelected) {
			indexGSNPPSelected = arg0.getSelectedItemPosition();
			this.getTimeDefineInAparam();
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}
}
