/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.ReportProgressMonthCellDTO;
import com.viettel.dms.dto.view.ReportProgressMonthViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Man hinh bao cao tien do luy ke thang
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressMonthView extends BaseFragment implements VinamilkTableListener {
	// tag for fragment
	public static final String TAG = TBHVReportProgressMonthView.class.getName();
	// show report progress date
	private static final int ACTION_MENU_DATE = 5;
	// show report progress in month
	private static final int ACTION_MENU_MONTH = 4;
	// show report progress display program
	private static final int ACTION_MENU_CTTB = 3;
	// show report progress customer not PSDS
	private static final int ACTION_MENU_PSDS = 2;
	// show report progress sales focus products
	private static final int ACTION_MENU_MHTT = 1;

	// action when click npp on row
	public static final int ACTION_SHOW_REPORT_PROGRESS_MONT_NPP_DETAIL_CLICK_NPP = 0;
	// action when click gsnpp on row
	public static final int ACTION_SHOW_REPORT_PROGRESS_MONT_NPP_DETAIL_CLICK_GSNPP = 1;

	// flag check load data the first
	public boolean isDoneLoadFirst = false;

	// get main activity
	private TBHVActivity parent;
	// number date sale plan
	private TextView tvNumDaySalePlan;
	// header table
	LinearLayout llHeaderTable;
	// number date sold
	private TextView tvNumDaySold;
	// number progress
	private TextView tvNumProgress;
	// table report progress month
	private VinamilkTableView tbReportPregressMonth;

	// data for screen
	private ReportProgressMonthViewDTO reportProgessViewData = new ReportProgressMonthViewDTO();

	public static TBHVReportProgressMonthView getInstance(Bundle data) {
		TBHVReportProgressMonthView instance = new TBHVReportProgressMonthView();
		instance.setArguments(data);
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
		ViewGroup view = (ViewGroup) inflater
				.inflate(R.layout.layout_tbhv_report_progress_month_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.initView(v);
		this.setTitleForScreen();
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS), R.drawable.icon_order, ACTION_MENU_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB), R.drawable.icon_reminders, ACTION_MENU_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT), R.drawable.icon_list_star, ACTION_MENU_MHTT);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated,
				ACTION_MENU_MONTH);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE), R.drawable.icon_calendar,
				ACTION_MENU_DATE);
		setMenuItemFocus(ACTION_MENU_MONTH);
		if (!this.isDoneLoadFirst) {
			this.getReportProgressMonth();
		}
		return v;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (this.isDoneLoadFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * 
	 * init all control for view
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		tvNumDaySalePlan = (TextView) v.findViewById(R.id.tvNumDaySalePlan);
		tvNumDaySold = (TextView) v.findViewById(R.id.tvNumDaySold);
		tvNumProgress = (TextView) v.findViewById(R.id.tvNumProgress);
		llHeaderTable = (LinearLayout) v.findViewById(R.id.llHeaderTable);
		tbReportPregressMonth = (VinamilkTableView) v.findViewById(R.id.tbReportPregressMonth);
		tbReportPregressMonth.getPagingControl().setVisibility(View.GONE);
	}

	/**
	 * 
	 * set title for screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void setTitleForScreen() {
		SpannableObject obj = new SpannableObject();
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_REPORT_PROGRESS_MONTH) + Constants.STR_SPACE, ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.NORMAL);
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
	public void renderLayout() {
		// render day sale plan , day sold , percent
		this.tvNumDaySalePlan.setText(String.valueOf(this.reportProgessViewData.numDaySalePlan));
		this.tvNumDaySold.setText(String.valueOf(this.reportProgessViewData.numDaySoldPlan));
		this.tvNumProgress.setText(this.reportProgessViewData.progessSold + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));

		// render table
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (this.reportProgessViewData != null) {
			llHeaderTable.setPadding(GlobalUtil.dip2Pixel(2), GlobalUtil.dip2Pixel(2), GlobalUtil.dip2Pixel(2), 0);
			tbReportPregressMonth.setVisibility(View.VISIBLE);
			// add list row
			for (int i = 0, size = this.reportProgessViewData.listReportProgessMonthDTO.size(); i < size; i++) {
				ReportProgressMonthCellDTO dto = this.reportProgessViewData.listReportProgessMonthDTO.get(i);
				TBHVReportProgressMonthRow row = new TBHVReportProgressMonthRow(parent, tbReportPregressMonth, false);
				row.setClickable(true);
				// update number order for product on view
				row.setListener(this);
				row.renderLayoutForRow(dto, this.reportProgessViewData.progessSold);
				listRows.add(row);
			}
			// add row total
			TBHVReportProgressMonthSumRow rowTotal = new TBHVReportProgressMonthSumRow(parent, tbReportPregressMonth);
			rowTotal.setClickable(true);
			rowTotal.renderLayoutForRow(this.reportProgessViewData.totalReportObject,
					this.reportProgessViewData.progessSold);
			listRows.add(rowTotal);
			tbReportPregressMonth.addContent(listRows);
		}

	}

	/**
	 * 
	 * request get report progress month infor to DB
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void getReportProgressMonth() {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		data.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_REPORT_PROGESS_MONTH_INFO;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onAttach(android.app.Activity
	 * )
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (TBHVActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
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
		case ActionEventConstant.GET_REPORT_PROGESS_MONTH_INFO: {
			this.isDoneLoadFirst = true;
			if (modelEvent.getModelData() != null) {
				this.reportProgessViewData = (ReportProgressMonthViewDTO) modelEvent.getModelData();
				this.renderLayout();
			}
			this.parent.closeProgressDialog();
			break;
		}
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
		case ActionEventConstant.GET_REPORT_PROGESS_MONTH_INFO:
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
		if (action == ACTION_SHOW_REPORT_PROGRESS_MONT_NPP_DETAIL_CLICK_NPP && control == tbReportPregressMonth) {
			gotoReportProgressMonthDetailNPP((ReportProgressMonthCellDTO) data, false);
		} else if (action == ACTION_SHOW_REPORT_PROGRESS_MONT_NPP_DETAIL_CLICK_GSNPP
				&& control == tbReportPregressMonth) {
			gotoReportProgressMonthDetailNPP((ReportProgressMonthCellDTO) data, true);
		}
	}

	/**
	 * 
	 * display report progress month for module detail NPP
	 * 
	 * @author: HaiTC3
	 * @param currentReportMonth
	 * @return: void
	 * @throws:
	 */
	public void gotoReportProgressMonthDetailNPP(ReportProgressMonthCellDTO currentReportMonth, boolean isSelectedGSNPP) {
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putSerializable(IntentConstants.INTENT_LIST_NPP_GSNPP,
				(Serializable) this.reportProgessViewData.listReportProgessMonthDTO);
		data.putInt(IntentConstants.INTENT_PERCENT_PROGRESS_REPORT_MONTH, this.reportProgessViewData.progessSold);
		data.putSerializable(IntentConstants.INTENT_CURRENT_REPORT_MONTH_CELL_DTO, currentReportMonth);
		data.putBoolean(IntentConstants.INTENT_TBHV_SELECTED_GSNPP_BEFORE_SCREEN, isSelectedGSNPP);
		e.action = ActionEventConstant.GOTO_TBHV_REPORT_PROGRESS_MONTH_DETAIL_NPP;
		e.sender = this;
		e.viewData = data;
		TBHVController.getInstance().handleSwitchFragment(e);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onEvent(int,
	 * android.view.View, java.lang.Object)
	 */
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
			this.getReportProgressMonth();
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
				this.getReportProgressMonth();
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

}
