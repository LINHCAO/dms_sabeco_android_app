/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.ReportProgressMonthCellDTO;
import com.viettel.dms.dto.view.ReportProgressMonthViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * display report progress month NPP detail view
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressMonthNPPDetailView extends BaseFragment implements VinamilkTableListener,
		OnItemSelectedListener {
	public static final String TAG = TBHVReportProgressMonthNPPDetailView.class.getName();
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

	// flag check load data the first
	public boolean isDoneLoadFirst = false;
	// get main activity
	private TBHVActivity parent;
	// table report progress month
	private VinamilkTableView tbReportPregressMonthNPPDetail;
	// list NPP code
	Spinner spNPPCode;
	// list GSNPP code;
	Spinner spGSNPPCode;
	// progress percent
	TextView tvNumProgress;
	// list GSNPP
	List<ReportProgressMonthCellDTO> listReportProgessMonthDTO = new ArrayList<ReportProgressMonthCellDTO>();
	// current report progress month cell dto
	ReportProgressMonthCellDTO currentReportMonthSelected = new ReportProgressMonthCellDTO();
	// percent progress
	int percent = 0;
	// is selected gsnpp before screen
	boolean isSelectedGSNPPBS = false;
	// list NPP
	List<ReportProgressMonthCellDTO> listNPPDTO = new ArrayList<ReportProgressMonthCellDTO>();
	// list GSNPP
	List<ReportProgressMonthCellDTO> listGSNPPDTO = new ArrayList<ReportProgressMonthCellDTO>();
	// report progress view data
	private ReportProgressMonthViewDTO reportProgessViewData = new ReportProgressMonthViewDTO();
	// current index NPP selected
	private int currentNPPIndex = -1;
	// current index GSNPP selected
	private int currentGSNPPIndex = -1;

	public static TBHVReportProgressMonthNPPDetailView getInstance(Bundle data) {
		TBHVReportProgressMonthNPPDetailView instance = new TBHVReportProgressMonthNPPDetailView();
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

	@SuppressWarnings("unchecked")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_report_progress_month_npp_detail_view,
				container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.initView(v);
		this.cerateHeaderMenu();

		this.setTitleForScreen();

		if (getArguments() != null) {
			if (getArguments().getSerializable(IntentConstants.INTENT_LIST_NPP_GSNPP) != null) {
				this.listReportProgessMonthDTO = (List<ReportProgressMonthCellDTO>) getArguments().getSerializable(
						IntentConstants.INTENT_LIST_NPP_GSNPP);
			}
			this.percent = getArguments().getInt(IntentConstants.INTENT_PERCENT_PROGRESS_REPORT_MONTH);
			this.isSelectedGSNPPBS = getArguments()
					.getBoolean(IntentConstants.INTENT_TBHV_SELECTED_GSNPP_BEFORE_SCREEN);

			if (getArguments().getSerializable(IntentConstants.INTENT_CURRENT_REPORT_MONTH_CELL_DTO) != null) {
				this.currentReportMonthSelected = (ReportProgressMonthCellDTO) getArguments().getSerializable(
						IntentConstants.INTENT_CURRENT_REPORT_MONTH_CELL_DTO);
			}

		}
		tvNumProgress.setText(this.percent + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
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
			if (this.listNPPDTO.get(i).staffOwnerID == this.currentReportMonthSelected.staffOwnerID) {
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
			if (this.listGSNPPDTO.get(i).staffID == this.currentReportMonthSelected.staffID) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 
	 * create header menu
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void cerateHeaderMenu() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS), R.drawable.icon_order, ACTION_MENU_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB), R.drawable.icon_reminders, ACTION_MENU_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT), R.drawable.icon_list_star, ACTION_MENU_MHTT);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated,
				ACTION_MENU_MONTH);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE), R.drawable.icon_calendar,
				ACTION_MENU_DATE);
		setMenuItemFocus(4);

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
		if (this.listReportProgessMonthDTO != null) {
			if (this.listNPPDTO != null) {
				this.listNPPDTO.clear();
			}
			for (int i = 0, size = this.listReportProgessMonthDTO.size(); i < size; i++) {
				boolean exist = false;
				ReportProgressMonthCellDTO tmpObject = this.listReportProgessMonthDTO.get(i);
				for (int j = 0, size2 = this.listNPPDTO.size(); j < size2; j++) {
					if (tmpObject.staffOwnerID == this.listNPPDTO.get(j).staffOwnerID) {
						exist = true;
					}
				}
				if (!exist) {
					this.listNPPDTO.add(tmpObject);
				}
			}
		}
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
			this.spNPPCode.setSelection(this.currentNPPIndex);
			this.spGSNPPCode.setSelection(this.currentGSNPPIndex);
			this.renderLayout();
		}
		super.onResume();
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
		// render table
		List<TableRow> listRows = new ArrayList<TableRow>();
		if (this.reportProgessViewData != null) {
			// add list row
			for (int i = 0, size = this.reportProgessViewData.listReportProgessMonthDTO.size(); i < size; i++) {
				ReportProgressMonthCellDTO dto = this.reportProgessViewData.listReportProgessMonthDTO.get(i);
				TBHVReportProgressMonthDetailRow row = new TBHVReportProgressMonthDetailRow(parent, tbReportPregressMonthNPPDetail,
						true);
				row.setClickable(true);
				// update number order for product on view
				// row.setListener(this);
				row.renderLayoutForRowDetail(dto, this.percent);
				listRows.add(row);
			}
			// add row total
			TBHVReportProgressMonthSumRow rowTotal = new TBHVReportProgressMonthSumRow(parent,
					tbReportPregressMonthNPPDetail);
			rowTotal.setClickable(true);
			rowTotal.renderLayoutForRow(this.reportProgessViewData.totalReportObject, this.percent);
			listRows.add(rowTotal);
			tbReportPregressMonthNPPDetail.addContent(listRows);
		}
	}

	/**
	 * 
	 * get report progress month NPP detail
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void requestGetReportProgressMonthNPPDetail(long idGSNPP) {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		if (idGSNPP != -1) {
			data.putString(IntentConstants.INTENT_SHOP_ID,
					String.valueOf(this.listNPPDTO.get(this.currentNPPIndex).staffOwnerID));
			data.putString(IntentConstants.INTENT_STAFF_OWNER_ID, String.valueOf(idGSNPP));
		} else {
			data.putString(IntentConstants.INTENT_SHOP_ID,
					String.valueOf(this.listNPPDTO.get(this.currentNPPIndex).staffOwnerID));
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_REPORT_PROGESS_MONTH_NPP_DETAIL_INFO;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * init control for screen
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		spNPPCode = (Spinner) v.findViewById(R.id.spNPPCode);
		spNPPCode.setOnItemSelectedListener(this);
		spGSNPPCode = (Spinner) v.findViewById(R.id.spGSNPPCode);
		spGSNPPCode.setOnItemSelectedListener(this);
		tvNumProgress = (TextView) v.findViewById(R.id.tvNumProgress);
		tbReportPregressMonthNPPDetail = (VinamilkTableView) v.findViewById(R.id.tbReportPregressMonthNPPDetail);
	}

	/**
	 * 
	 * set title for screen view
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void setTitleForScreen() {
		SpannableObject obj = new SpannableObject();
		obj.addSpan(getString(R.string.TITLE_VIEW_TBHV_REPORT_PROGRESS_MONTH_NPP_DETAIL) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		obj.addSpan(DateUtils.defaultDateFormat.format(new Date()), ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		setTitleHeaderView(obj);
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
		String[] arrPromotion = new String[this.listNPPDTO.size()];
		for (int i = 0, size = this.listNPPDTO.size(); i < size; i++) {
//			arrPromotion[i] = this.listNPPDTO.get(i).staffOwnerCode + " - " + this.listNPPDTO.get(i).staffOwnerName;
			arrPromotion[i] = this.listNPPDTO.get(i).staffOwnerCode;
		}
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPromotion);
		// this.isFirstLoadProduct = true;
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
		boolean isHaveItemALL = true;
		ArrayList<String> listParams = new ArrayList<String>();
		// add item "ALL" when list.size > 0
		// if (this.listReportProgessMonthDTO.size() > 1) {
		listParams.add(StringUtil.getString(R.string.ALL));
		// }

		for (int i = 0, size = this.listReportProgessMonthDTO.size(); i < size; i++) {
			ReportProgressMonthCellDTO tmpObject = this.listReportProgessMonthDTO.get(i);
			if (tmpObject.staffOwnerID == idNPP) {
				listGSNPPDTO.add(tmpObject);
				listParams.add(tmpObject.staffCode + " - " + tmpObject.staffName);
			}
		}
		if (this.listGSNPPDTO.size() <= 1 && listParams.size() > 0) {
			isHaveItemALL = false;
			listParams.remove(0);
		}
		paramsList = listParams.toArray(new String[listParams.size()]);
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, paramsList);
		// this.isFirstLoadProduct = true;
		this.spGSNPPCode.setAdapter(adapterLine);
		if (!this.isDoneLoadFirst && this.isSelectedGSNPPBS) {
			int index = this.getIndexGSNPPSelectedBeforeScreen();
			if (isHaveItemALL) {
				index = index + 1;
			}
			this.spGSNPPCode.setSelection(index);
		}
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
		case ActionEventConstant.GET_REPORT_PROGESS_MONTH_NPP_DETAIL_INFO: {
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
		case ActionEventConstant.GET_REPORT_PROGESS_MONTH_NPP_DETAIL_INFO:
			this.parent.closeProgressPercentDialog();
			break;

		default:
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

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spNPPCode) {
			// display list GSNPP when choose one NPP
			if (this.listNPPDTO != null && this.listNPPDTO.size() >= arg2 && this.currentNPPIndex != arg2) {
				this.currentNPPIndex = arg2;
				this.currentGSNPPIndex = -1;
				this.initSpinnerListGSNPPCode(this.listNPPDTO.get(arg2).staffOwnerID);
			}
		} else if (arg0 == spGSNPPCode) {
			if (this.listGSNPPDTO != null && this.listGSNPPDTO.size() >= arg2 && this.currentGSNPPIndex != arg2) {
				this.currentGSNPPIndex = arg2;
				// request get list NVBH
				// co item "tat ca"
				if (this.listGSNPPDTO.size() > 1) {
					int index = arg2 - 1;
					if (index >= 0) {
						this.requestGetReportProgressMonthNPPDetail(this.listGSNPPDTO.get(index).staffID);
					} else {
						this.requestGetReportProgressMonthNPPDetail(index);
					}
				} else { // ko co item tat ca
					this.requestGetReportProgressMonthNPPDetail(this.listGSNPPDTO.get(arg2).staffID);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

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
				if (this.listGSNPPDTO.size() > 1) {
					int index = currentGSNPPIndex - 1;
					if (index >= 0) {
						this.requestGetReportProgressMonthNPPDetail(this.listGSNPPDTO.get(index).staffID);
					} else {
						this.requestGetReportProgressMonthNPPDetail(index);
					}
				} else { // ko co item tat ca
					this.requestGetReportProgressMonthNPPDetail(this.listGSNPPDTO.get(currentGSNPPIndex).staffID);
				}
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

}
