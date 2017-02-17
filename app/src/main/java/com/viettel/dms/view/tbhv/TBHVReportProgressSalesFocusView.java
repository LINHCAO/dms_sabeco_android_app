/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.io.Serializable;
import java.util.Date;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.ReportSalesFocusEmployeeInfo;
import com.viettel.dms.dto.view.TBHVProgressReportSalesFocusViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;

/**
 * display report sales product focus view
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressSalesFocusView extends BaseFragment implements VinamilkTableListener {

	public static final String TAG = TBHVReportProgressSalesFocusView.class.getName();
	private static final int ACTION_MENU_DATE = 5; // show report progress date
	private static final int ACTION_MENU_MONTH = 4; // show report progress in
													// month
	private static final int ACTION_MENU_CTTB = 3; // show report progress
													// display program
	private static final int ACTION_MENU_PSDS = 2; // show report progress
													// customer not PSDS
	private static final int ACTION_MENU_MHTT = 1; // show report progress sales
													// focus products

	// parent activity
	TBHVActivity parent;
	// view group content header of tb and row
	private View viewGroup;
	// view header
	private View vItemHeader;
	// layout MHTT
	LinearLayout vMHTT;
	// notify no have data
	TextView tvNotify;
	// vector view tmp
	Vector<View> vTmp = new Vector<View>();
	TextView tvPlanDate;// so ngay ban hang ke hoach
	TextView tvSoldDate;// so ngay ban hang da qua
	TextView tvProgress;// Tien do

	// bang bao cao tien do ban hang trong tam
	private VinamilkTableView tbProgReportsSalesFocus;
	// flag check load data the first
	public boolean isDoneLoadFirst = false;
	// report infor of screen
	private TBHVProgressReportSalesFocusViewDTO reportDataScreen = new TBHVProgressReportSalesFocusViewDTO();

	public static TBHVReportProgressSalesFocusView getInstance(Bundle data) {
		TBHVReportProgressSalesFocusView instance = new TBHVReportProgressSalesFocusView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (TBHVActivity) getActivity();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		if (this.isDoneLoadFirst) {
			this.renderData();
		}
		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_progress_report_sales_focus_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		viewGroup = v;
		tvPlanDate = (TextView) v.findViewById(R.id.tvPlanDate);
		tvSoldDate = (TextView) v.findViewById(R.id.tvSoldDate);
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
		tvNotify = (TextView) v.findViewById(R.id.tvNotify);
		tbProgReportsSalesFocus = (VinamilkTableView) v.findViewById(R.id.tbProgReportsSalesFocus);
		tbProgReportsSalesFocus.setListener(this);

		this.setTitleForScreen();
		initHeaderMenu();

		if (!this.isDoneLoadFirst) {
			this.requestGetReportProgressSalesFocusInfo();
		}

		return v;
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
		setMenuItemFocus(3);
	}

	/**
	 * 
	 * request get report progress sales focus info
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void requestGetReportProgressSalesFocusInfo() {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		data.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.TBHV_GET_REPORT_PROGESS_SALES_FORCUS_INFO;
		TBHVController.getInstance().handleViewEvent(e);
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
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_REPORT_SALES_FOCUS_VIEW) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
	}

	/**
	 * 
	 * render data for screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void renderData() {
		// render header infor
		tvPlanDate.setText(String.valueOf(this.reportDataScreen.numPlanDate));
		tvSoldDate.setText(String.valueOf(this.reportDataScreen.numPlanDateDone));
		tvProgress.setText(this.reportDataScreen.progressSales + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));

		// create header for table
		vMHTT = (LinearLayout) viewGroup.findViewById(R.id.layMHTT);
		vMHTT.setVisibility(View.VISIBLE);
		tbProgReportsSalesFocus.setVisibility(View.VISIBLE);
		LayoutInflater vi = (LayoutInflater) parent.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (View v : vTmp) {
			vMHTT.removeView(v);
		}
		vTmp.removeAllElements();
		for (int i = 0; i < this.reportDataScreen.arrMMTTText.size(); i++) {
			String number = this.reportDataScreen.arrMMTTText.get(i);
			vItemHeader = vi.inflate(R.layout.layout_progress_report_sales_focus_header_item, null);
			TextView tv = (TextView) vItemHeader.findViewById(R.id.tvMHTT);
			tv.setText(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT) + Constants.STR_SPACE + number
					+ Constants.STR_SPACE + StringUtil.getString(R.string.TEXT_DVT_VND));
			vMHTT.addView(vItemHeader);
			vTmp.add(vItemHeader);
		}
		if (this.reportDataScreen.arrMMTTText.size() == 0) {
			vItemHeader = vi.inflate(R.layout.layout_progress_report_sales_focus_header_item, null);
			TextView tv = (TextView) vItemHeader.findViewById(R.id.tvMHTT);
			tv.setText(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT) + Constants.STR_SPACE + "0"
					+ Constants.STR_SPACE + StringUtil.getString(R.string.TEXT_DVT_VND));
			vMHTT.addView(vItemHeader);
			vTmp.add(vItemHeader);
		}

		tbProgReportsSalesFocus.clearAllData();
		for (ReportSalesFocusEmployeeInfo reportSalesFocusEmployeeInfo: reportDataScreen.listFocusInfoRow) {
			TBHVReportProgressSalesFocusRow row = new TBHVReportProgressSalesFocusRow(parent, tbProgReportsSalesFocus,
					false, false);
			row.renderLayoutForTBHVReportFocus(reportDataScreen.progressSales, reportSalesFocusEmployeeInfo);
			row.setListener(this);
			tbProgReportsSalesFocus.addRow(row);
		}

		TBHVReportProgressSalesFocusRow row = new TBHVReportProgressSalesFocusRow(parent, tbProgReportsSalesFocus,
				true, false);
		// row tong
		row.renderLayoutForTBHVReportFocus(reportDataScreen.progressSales, reportDataScreen.objectReportTotal);
		tbProgReportsSalesFocus.addRow(row);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleModelViewEvent(com.
	 * viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.TBHV_GET_REPORT_PROGESS_SALES_FORCUS_INFO:
			this.isDoneLoadFirst = true;
			if (modelEvent.getModelData() != null) {
				this.reportDataScreen = (TBHVProgressReportSalesFocusViewDTO) modelEvent.getModelData();
				this.renderData();
			}
			this.parent.closeProgressDialog();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleErrorModelViewEvent
	 * (com.viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.TBHV_GET_REPORT_PROGESS_SALES_FORCUS_INFO:
			this.parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableloadMore(android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbProgReportsSalesFocus) {
			if (action == TBHVReportProgressSalesFocusRow.ACTION_SHOW_REPORT_PROGRESS_SALE_FOCUS_DETAIL_CLICK_NPP) {
				gotoReportProgressSalesFocusDetail((ReportSalesFocusEmployeeInfo) data, false);
			} else if (action == TBHVReportProgressSalesFocusRow.ACTION_SHOW_REPORT_PROGRESS_SALE_FOCUS_DETAIL_CLICK_GSNPP) {
				gotoReportProgressSalesFocusDetail((ReportSalesFocusEmployeeInfo) data, true);
			}

		}
	}

	/**
	 * 
	 * display report progress sales focus detail
	 * 
	 * @author: HaiTC3
	 * @param currentSeleted
	 * @return: void
	 * @throws:
	 */
	public void gotoReportProgressSalesFocusDetail(ReportSalesFocusEmployeeInfo currentSeleted, boolean isSelectedGSNPP) {
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putSerializable(IntentConstants.INTENT_DATA_REPORT_PROGRESS_SALES_FOCUS,
				(Serializable) this.reportDataScreen);
		data.putSerializable(IntentConstants.INTENT_INDEX_REPORT_PROGRESS_SALES_FOCUS, currentSeleted);
		data.putBoolean(IntentConstants.INTENT_TBHV_SELECTED_GSNPP_BEFORE_SCREEN, isSelectedGSNPP);

		e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_SALES_FORCUS_DETAIL;
		e.sender = this;
		e.viewData = data;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				this.requestGetReportProgressSalesFocusInfo();
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

}
