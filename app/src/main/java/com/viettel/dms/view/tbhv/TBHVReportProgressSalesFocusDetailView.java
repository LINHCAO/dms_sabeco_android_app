/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.ReportSalesFocusEmployeeInfo;
import com.viettel.dms.dto.view.TBHVProgressReportSalesFocusViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
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
 * display report progress sales focus detail for tbhv
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressSalesFocusDetailView extends BaseFragment implements VinamilkTableListener,
		OnItemSelectedListener {

	public static final String TAG = TBHVReportProgressSalesFocusDetailView.class.getName();

	// show report progress date
	private static final int ACTION_MENU_DATE = 5;
	// show report progress in month
	private static final int ACTION_MENU_MONTH = 4;
	// show report progress display program
	private static final int ACTION_MENU_CTTB = 2;
	// show report progress customer not PSDS
	private static final int ACTION_MENU_PSDS = 1;
	// show report progress sales focus products
	private static final int ACTION_MENU_MHTT = 3;

	// parent activity
	TBHVActivity parent;
	// group view for screen
	private View viewGroup;
	// header item
	private View vItemHeader;
	// linerar layout MHTT
	LinearLayout vMHTT;
	// view tmp
	Vector<View> vTmp = new Vector<View>();
	// bang bao cao tien do ban hang trong tam
	private VinamilkTableView tbProgReportsSalesFocusDetail;
	// flag check load data the first
	public boolean isDoneLoadFirst = false;
	// list NPP code
	Spinner spNPPCode;
	// list GSNPP code;
	Spinner spGSNPPCode;
	// progress percent
	TextView tvNumProgress;
	// report infor of screen
	private TBHVProgressReportSalesFocusViewDTO reportDataOldScreen = new TBHVProgressReportSalesFocusViewDTO();
	// current GSNPP selected
	ReportSalesFocusEmployeeInfo currentOldReportSalesSeleted = new ReportSalesFocusEmployeeInfo();
	// report infor of screen
	private TBHVProgressReportSalesFocusViewDTO reportDataScreen = new TBHVProgressReportSalesFocusViewDTO();
	// list NPP
	List<ReportSalesFocusEmployeeInfo> listNPPDTO = new ArrayList<ReportSalesFocusEmployeeInfo>();
	// list GSNPP
	List<ReportSalesFocusEmployeeInfo> listGSNPPDTO = new ArrayList<ReportSalesFocusEmployeeInfo>();
	// current index NPP selected
	private int currentNPPIndex = -1;
	// current index GSNPP selected
	private int currentGSNPPIndex = -1;
	// is selected GSPP before screen
	boolean isSelectedGSNPP = false;

	public static TBHVReportProgressSalesFocusDetailView getInstance(Bundle data) {
		TBHVReportProgressSalesFocusDetailView instance = new TBHVReportProgressSalesFocusDetailView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		parent = (TBHVActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_progress_report_sales_focus_detail_view,
				container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		viewGroup = v;

		this.initView(v);
		this.setTitleForScreen();
		initHeaderMenu();

		if (getArguments() != null) {
			if (getArguments().getSerializable(IntentConstants.INTENT_DATA_REPORT_PROGRESS_SALES_FOCUS) != null) {
				this.reportDataOldScreen = (TBHVProgressReportSalesFocusViewDTO) getArguments().getSerializable(
						IntentConstants.INTENT_DATA_REPORT_PROGRESS_SALES_FOCUS);
			}

			this.isSelectedGSNPP = getArguments().getBoolean(IntentConstants.INTENT_TBHV_SELECTED_GSNPP_BEFORE_SCREEN);

			if (getArguments().getSerializable(IntentConstants.INTENT_INDEX_REPORT_PROGRESS_SALES_FOCUS) != null) {
				this.currentOldReportSalesSeleted = (ReportSalesFocusEmployeeInfo) getArguments().getSerializable(
						IntentConstants.INTENT_INDEX_REPORT_PROGRESS_SALES_FOCUS);
			}

		}
		tvNumProgress.setText(this.reportDataOldScreen.progressSales
				+ StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));

		if (!this.isDoneLoadFirst) {
			this.createListNPP();
		}
		this.initSpinnerListNPPCode();
		if (!this.isDoneLoadFirst) {
			this.spNPPCode.setSelection(this.getIndexNPPSelectedBeforeScreen());
		}

		return v;
	}

	/**
	 * 
	 * get index item npp selected before screen
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getIndexNPPSelectedBeforeScreen() {
		int index = -1;
		for (int i = 0, size = this.listNPPDTO.size(); i < size; i++) {
			if (this.listNPPDTO.get(i).staffOwnerId == this.currentOldReportSalesSeleted.staffOwnerId) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 
	 * get index item gsnpp selected before screen
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: int
	 * @throws:
	 */
	public int getIndexGSNPPSelectedBeforeScreen() {
		int index = -1;
		for (int i = 0, size = this.listGSNPPDTO.size(); i < size; i++) {
			if (this.listGSNPPDTO.get(i).staffId == this.currentOldReportSalesSeleted.staffId) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 
	 * create list NPP to display in spinner control
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void createListNPP() {
		if (this.reportDataOldScreen != null) {
			if (this.listNPPDTO != null) {
				this.listNPPDTO.clear();
			}
			for (ReportSalesFocusEmployeeInfo reportSalesFocusEmployeeInfo: reportDataOldScreen.listFocusInfoRow) {
				boolean exist = false;
				for (int j = 0, size2 = this.listNPPDTO.size(); j < size2; j++) {
					if (reportSalesFocusEmployeeInfo.staffOwnerId == this.listNPPDTO.get(j).staffOwnerId) {
						exist = true;
					}
				}
				if (!exist) {
					this.listNPPDTO.add(reportSalesFocusEmployeeInfo);
				}
			}
		}
	}

	/**
	 * 
	 * init spinner list NPP code
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void initSpinnerListNPPCode() {
		String[] arrPromotion = new String[listNPPDTO.size()];
		int i=0;
		for (ReportSalesFocusEmployeeInfo reportSalesFocusEmployeeInfo: listNPPDTO) {
			arrPromotion[i] = reportSalesFocusEmployeeInfo.staffOwnerCode;
			i++;
		}
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPromotion);
		this.spNPPCode.setAdapter(adapterLine);
	}

	/**
	 * 
	 * init spinner list GSNPP
	 * 
	 * @author: HaiTC3
	 * @param staffID
	 * @return: void
	 * @throws:
	 */
	public void initSpinnerListGSNPPCode(long idNPP) {
		String[] paramsList = new String[] {};
		if (this.listGSNPPDTO != null) {
			this.listGSNPPDTO.clear();
		}
		ArrayList<String> listParams = new ArrayList<String>();
		boolean isHaveItemALL = true;
		// add item "ALL" when list.size > 0
		// if (this.reportDataOldScreen.listFocusInfoRow.size() > 1) {
		listParams.add(StringUtil.getString(R.string.ALL));
		// }

		for (ReportSalesFocusEmployeeInfo reportSalesFocusEmployeeInfo: reportDataOldScreen.listFocusInfoRow) {
			if (reportSalesFocusEmployeeInfo.staffOwnerId == idNPP) {
				listGSNPPDTO.add(reportSalesFocusEmployeeInfo);
				listParams.add(reportSalesFocusEmployeeInfo.staffCode + " - " + reportSalesFocusEmployeeInfo.staffName);
			}
		}
		if (listGSNPPDTO.size() <= 1 && listParams.size() > 0) {
			isHaveItemALL = false;
			listParams.remove(0);
		}
		paramsList = listParams.toArray(new String[listParams.size()]);
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, paramsList);
		// this.isFirstLoadProduct = true;
		this.spGSNPPCode.setAdapter(adapterLine);

		if (!this.isDoneLoadFirst && this.isSelectedGSNPP) {
			int index = this.getIndexGSNPPSelectedBeforeScreen();
			if (isHaveItemALL) {
				index = index + 1;
			}
			this.spGSNPPCode.setSelection(index);
		}
	}

	/**
	 * init control view
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	private void initView(View v) {
		spNPPCode = (Spinner) v.findViewById(R.id.spNPPCode);
		spNPPCode.setOnItemSelectedListener(this);
		spGSNPPCode = (Spinner) v.findViewById(R.id.spGSNPPCode);
		spGSNPPCode.setOnItemSelectedListener(this);
		tvNumProgress = (TextView) v.findViewById(R.id.tvNumProgress);
		tbProgReportsSalesFocusDetail = (VinamilkTableView) v.findViewById(R.id.tbProgReportsSalesFocusDetail);
		tbProgReportsSalesFocusDetail.setListener(this);
	}

	/**
	 * 
	 * create hader menu bar
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void initHeaderMenu() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS), R.drawable.icon_order, ACTION_MENU_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB), R.drawable.icon_reminders, ACTION_MENU_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT), R.drawable.icon_list_star, ACTION_MENU_MHTT);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated,
				ACTION_MENU_MONTH);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE), R.drawable.icon_calendar,
				ACTION_MENU_DATE);
		setMenuItemFocus(ACTION_MENU_MHTT);
	}

	/**
	 * 
	 * set title screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void setTitleForScreen() {
		SpannableObject obj = new SpannableObject();
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_REPORT_SALES_FOCUS_DETAIL_VIEW) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * 
	 * render layout for screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void renderDataForLayout() {
		// create header for table
		vMHTT = (LinearLayout) viewGroup.findViewById(R.id.llMHTT);
		vMHTT.setVisibility(View.VISIBLE);
		tbProgReportsSalesFocusDetail.setVisibility(View.VISIBLE);
		LayoutInflater vi = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (View v : vTmp) {
			vMHTT.removeView(v);
		}
		vTmp.removeAllElements();
		for (int i = 0; i < this.reportDataScreen.arrMMTTText.size(); i++) {
			String number = this.reportDataScreen.arrMMTTText.get(i);
			vItemHeader = vi.inflate(R.layout.layout_progress_report_sales_focus_header_item, null);
			TextView tv = (TextView) vItemHeader.findViewById(R.id.tvMHTT);
			tv.setText("MHTT " + number + StringUtil.getString(R.string.TEXT_DVT_VND));
			vMHTT.addView(vItemHeader);
			vTmp.add(vItemHeader);
		}
		tbProgReportsSalesFocusDetail.clearAllData();
		for (ReportSalesFocusEmployeeInfo reportSalesFocusEmployeeInfo: reportDataScreen.listFocusInfoRow) {

			TBHVReportProgressSalesFocusDetailRow row = new TBHVReportProgressSalesFocusDetailRow(parent,
					tbProgReportsSalesFocusDetail, false, true);
			row.renderLayoutForTBHVReportFocus(this.reportDataOldScreen.progressSales, reportSalesFocusEmployeeInfo);
			tbProgReportsSalesFocusDetail.addRow(row);
		}

		TBHVReportProgressSalesFocusDetailRow row = new TBHVReportProgressSalesFocusDetailRow(parent,
				tbProgReportsSalesFocusDetail, true, true);
		// row tong
		row.renderLayoutForTBHVReportFocus(this.reportDataOldScreen.progressSales,
				this.reportDataScreen.objectReportTotal);
		tbProgReportsSalesFocusDetail.addRow(row);
	}

	@Override
	public void onResume() {
		
		if (this.isDoneLoadFirst) {
			this.spNPPCode.setSelection(this.currentNPPIndex);
			this.spGSNPPCode.setSelection(this.currentGSNPPIndex);
			this.renderDataForLayout();
		}
		super.onResume();
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_SALES_FORCUS_DETAIL:
			this.isDoneLoadFirst = true;
			if (modelEvent.getModelData() != null) {
				this.reportDataScreen = (TBHVProgressReportSalesFocusViewDTO) modelEvent.getModelData();
				this.renderDataForLayout();
			}
			this.parent.closeProgressDialog();
			break;

		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_TBHV_REPORT_PROGRESS_SALES_FORCUS_DETAIL:
			this.parent.closeProgressDialog();
			break;

		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		if (arg0 == spNPPCode) {
			// display list GSNPP when choose one NPP
			if (this.listNPPDTO != null && this.listNPPDTO.size() >= arg2 && this.currentNPPIndex != arg2) {
				this.currentNPPIndex = arg2;
				this.currentGSNPPIndex = -1;
				this.initSpinnerListGSNPPCode(this.listNPPDTO.get(arg2).staffOwnerId);
			}
		} else if (arg0 == spGSNPPCode) {
			if (this.listGSNPPDTO != null && this.listGSNPPDTO.size() >= arg2 && this.currentGSNPPIndex != arg2) {
				this.currentGSNPPIndex = arg2;
				// request get list NVBH
				if (this.listGSNPPDTO.size() > 1) { // have menu item "all"
					int index = arg2 - 1;
					if (index >= 0) {
						this.requestGetReportProgressSalesFocusDetail(this.listGSNPPDTO.get(index).staffId);
					} else {
						this.requestGetReportProgressSalesFocusDetail(index);
					}
				} else {
					this.requestGetReportProgressSalesFocusDetail(this.listGSNPPDTO.get(arg2).staffId);
				}
			}
		}
	}

	/**
	 * Mo ta chuc nang cua ham
	 * 
	 * @author: HaiTC3
	 * @param i
	 * @return: void
	 * @throws:
	 */
	private void requestGetReportProgressSalesFocusDetail(long idGSNPP) {
		
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		if (idGSNPP != -1) {
			data.putString(IntentConstants.INTENT_SHOP_ID,
					String.valueOf(this.listNPPDTO.get(this.currentNPPIndex).staffOwnerId));
			data.putString(IntentConstants.INTENT_STAFF_OWNER_ID, String.valueOf(idGSNPP));
		} else {
			data.putString(IntentConstants.INTENT_SHOP_ID,
					String.valueOf(this.listNPPDTO.get(this.currentNPPIndex).staffOwnerId));
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_REPORT_PROGRESS_SALES_FORCUS_DETAIL;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		
		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case ACTION_MENU_DATE:
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.ACTION_TBHV_REPORT_PROGRESS_DATE;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MENU_MONTH:
			this.isDoneLoadFirst = false;
			e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MENU_CTTB: {
			this.isDoneLoadFirst = false;
			e.action = ActionEventConstant.GET_TBHV_DIS_PRO_COM_PROG_REPORT;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case ACTION_MENU_PSDS: {
			this.isDoneLoadFirst = false;
			e.action = ActionEventConstant.GO_TO_TBHV_REPORT_CUSTOMER_NOT_PSDS;
			e.sender = this;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		}
		case ACTION_MENU_MHTT:
			this.isDoneLoadFirst = false;
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_SALES_FORCUS;
			TBHVController.getInstance().handleSwitchFragment(e);
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (this.listGSNPPDTO.size() > 1) { // have menu item "all"
					int index = currentGSNPPIndex - 1;
					if (index >= 0) {
						this.requestGetReportProgressSalesFocusDetail(this.listGSNPPDTO.get(index).staffId);
					} else {
						this.requestGetReportProgressSalesFocusDetail(index);
					}
				} else {
					this.requestGetReportProgressSalesFocusDetail(this.listGSNPPDTO.get(currentGSNPPIndex).staffId);
				}
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

}
