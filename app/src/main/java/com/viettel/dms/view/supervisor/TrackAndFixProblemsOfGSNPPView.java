/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.SuperviorTrackAndFixProblemOfGSNPPViewDTO;
import com.viettel.dms.dto.view.SupervisorProblemOfGSNPPDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * tracking and fix problems of GSNPP that TBHV created
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TrackAndFixProblemsOfGSNPPView extends BaseFragment implements
		VinamilkTableListener, OnTouchListener, OnItemClickListener,
		OnItemSelectedListener {
	public static final String TAG = TrackAndFixProblemsOfGSNPPView.class
			.getName();
	// limit row in page
	public static final int LIMIT_ROW_PER_PAGE = 10;
	// show screen track of gsnpp
	private static final int ACTION_MENU_TRACK = 2;
	// show screen list problem that gsnpp need do
	private static final int ACTION_MENU_GSNPP_NEED_DO = 1;
	private static final int ACTION_DONE_OK = 3;
	private static final int ACTION_DONE_CANCEL = 4;
	boolean isDoneLoadFirst = false;// vo man hinh lan dau tien hay kg?
	// control list status
	Spinner spStatusCode;
	// control input from date
	VNMEditTextClearable etInputFromDate;
	// control input end date
	VNMEditTextClearable etInputEndDate;
	// control button search
	Button btSearch;
	// control table list problems
	DMSTableView tbListProblems;
	// parent activity
	SupervisorActivity parent;
	// current calendar
	int currentCalendar;
	// action from date
	private static final int DATE_FROM_CONTROL = 1;
	// action end date
	private static final int DATE_TO_CONTROL = 2;
	// data info of screen : list problem info, list problem status, total item
	// problem in db
	SuperviorTrackAndFixProblemOfGSNPPViewDTO dataInfoListProblem = new SuperviorTrackAndFixProblemOfGSNPPViewDTO();
	// check click button search problem
	boolean isSearchingProblem = false;
	// date from date
	String fromDate = Constants.STR_BLANK;
	// date end date
	String toDate = Constants.STR_BLANK;
	// current status selected
	int currentStatusSelected = -1;
	// check refresh screen after update problem to db & server
	boolean isRefreshScreen = false;
	// alert popup problem detail
	AlertDialog alertFollowProblemDetail;
	// popup problem detail
	PopupProblemDetailView popupProblemDetail;
	// current object Update to server
	SupervisorProblemOfGSNPPDTO currentObjectUpdateToServer;

	public static TrackAndFixProblemsOfGSNPPView newInstance(Bundle b) {
		TrackAndFixProblemsOfGSNPPView instance = new TrackAndFixProblemsOfGSNPPView();
		instance.setArguments(b);
		return instance;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (SupervisorActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_track_and_fix_problems_of_gsnpp_view,container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(getString(R.string.TITLE_VIEW_GSNPP_TRACK_AND_FIX_PROBLEMS_GSNPP));
		initView(v);
		this.createHeaderMenu();
		if (!isDoneLoadFirst) {
			getListProblemsOfScreen(0, true, true);
		}
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (isDoneLoadFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * init controls of screen
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void initView(View v) {
		this.spStatusCode = (Spinner) v.findViewById(R.id.spStatusCode);
		this.spStatusCode.setOnItemSelectedListener(this);
		this.renderListProblemStatus();
		this.etInputEndDate = (VNMEditTextClearable) v.findViewById(R.id.etInputEndDate);
		this.etInputEndDate.setIsHandleDefault(false);
		etInputEndDate.setOnTouchListener(this);
		this.etInputFromDate = (VNMEditTextClearable) v.findViewById(R.id.etInputFromDate);
		etInputFromDate.setOnTouchListener(this);
		this.etInputFromDate.setIsHandleDefault(false);
		this.btSearch = (Button) v.findViewById(R.id.btSearch);
		this.btSearch.setOnClickListener(this);
		this.tbListProblems = (DMSTableView) v.findViewById(R.id.tbListProblems);
		this.tbListProblems.setListener(this);
	}

	/**
	 * create header menu for screen
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void createHeaderMenu() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_MENU_NEED_DO_IT),R.drawable.icon_checkpoint, ACTION_MENU_GSNPP_NEED_DO);
		addMenuItem(StringUtil.getString(R.string.TEXT_MENU_ITEM_TRACK),R.drawable.icon_order, ACTION_MENU_TRACK);
		setMenuItemFocus(1);
	}

	/**
	 * get list problem of sceen
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void getListProblemsOfScreen(int numPage, boolean isGetTotalItem, boolean getListStatus) {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent event = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_CREATE_USER_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().staffOwnerId));
		data.putBoolean(IntentConstants.INTENT_GET_LIST_PROBLEM_TYPE,getListStatus);
		data.putBoolean(IntentConstants.INTENT_IS_ALL, isGetTotalItem);
		String page = " limit " + (numPage * LIMIT_ROW_PER_PAGE) + ","+ LIMIT_ROW_PER_PAGE;
		data.putString(IntentConstants.INTENT_PAGE, page);
		if (this.isDoneLoadFirst) {
			if (this.spStatusCode.getSelectedItemPosition() > 0 && currentStatusSelected > 0) {
				data.putString(IntentConstants.INTENT_TYPE_PROBLEM_GSNPP,String.valueOf(currentStatusSelected));
			}
			if (!StringUtil.isNullOrEmpty(fromDate)) {
				data.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE,fromDate);
			}
			if (!StringUtil.isNullOrEmpty(toDate)) {
				data.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE,toDate);
			}
		}
		event.sender = this;
		event.action = ActionEventConstant.GET_LIST_PROBLEM_OF_GSNPP_NEED_DO_IT;
		event.viewData = data;
		SuperviorController.getInstance().handleViewEvent(event);
	}

	/**
	 * 
	 * render layout for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void renderLayout() {
		if (!this.isDoneLoadFirst) {
			initHeaderTable(tbListProblems, new TrackAndFixProblemOfGSNPPRow(parent, this));
		}
		tbListProblems.clearAllData();
		if (this.dataInfoListProblem != null&& this.dataInfoListProblem.listProblemsOfGSNPP != null
				&& this.dataInfoListProblem.listProblemsOfGSNPP.size() > 0) {
			int pos = 1;
			for (SupervisorProblemOfGSNPPDTO dto : dataInfoListProblem.listProblemsOfGSNPP) {
				TrackAndFixProblemOfGSNPPRow row = new TrackAndFixProblemOfGSNPPRow(parent, this);
				row.setClickable(true);
				row.setTag(Integer.valueOf(pos));
				row.renderLayoutForRow(pos+ (tbListProblems.getPagingControl().getCurrentPage() - 1)* LIMIT_ROW_PER_PAGE, dto);
				pos++;
				tbListProblems.addRow(row);
			}
		} else {
			tbListProblems.showNoContentRow();
		}
		
	}

	/**
	 * update text box from date and to date
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void updateFromDateAndEndDate(int dayOfMonth, int monthOfYear,
			int year) {
		String sDay = String.valueOf(dayOfMonth);
		String sMonth = String.valueOf(monthOfYear + 1);
		if (dayOfMonth < 10) {
			sDay = "0" + sDay;
		}
		if (monthOfYear + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		if (currentCalendar == DATE_FROM_CONTROL) {
			etInputFromDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			etInputFromDate.setText(new StringBuilder().append(sDay)
					.append("/").append(sMonth).append("/").append(year)
					.append(" "));
		}
		if (currentCalendar == DATE_TO_CONTROL) {
			etInputEndDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			etInputEndDate.setText(new StringBuilder().append(sDay).append("/")
					.append(sMonth).append("/").append(year).append(" "));
		}
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case ACTION_MENU_TRACK:
			e.action = ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_DONE_OK: {
			requestUpdateFeedbackStatus(this.currentObjectUpdateToServer);
			break;
		}
		case ACTION_DONE_CANCEL: {
			this.renderLayout();
			break;
		}
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * render list problem status
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void renderListProblemStatus() {
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, Constants.ARRCHOOSE);
		this.spStatusCode.setAdapter(adapterLine);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_LIST_PROBLEM_OF_GSNPP_NEED_DO_IT:
			SuperviorTrackAndFixProblemOfGSNPPViewDTO dataTMP = (SuperviorTrackAndFixProblemOfGSNPPViewDTO) modelEvent
					.getModelData();
			if (dataTMP != null) {
				// update data
				if (!this.isDoneLoadFirst || isSearchingProblem) {
					this.dataInfoListProblem = dataTMP;
				} else {
					this.dataInfoListProblem.listProblemsOfGSNPP = dataTMP.listProblemsOfGSNPP;
				}

				if (!this.isDoneLoadFirst || isSearchingProblem
						|| isRefreshScreen) {
					// update paging
					if (this.dataInfoListProblem.listProblemsOfGSNPP != null
							&& this.dataInfoListProblem.listProblemsOfGSNPP
									.size() > 0) {
						tbListProblems.getPagingControl().setVisibility(
								View.VISIBLE);
						if (tbListProblems.getPagingControl().totalPage < 0
								|| isSearchingProblem || isRefreshScreen) {
							tbListProblems
									.setTotalSize(this.dataInfoListProblem.totalItem,1);
							tbListProblems.getPagingControl().setCurrentPage(1);
						}
					} else {
						tbListProblems.getPagingControl().setVisibility(
								View.GONE);
					}
				}

				// update render layout
				this.renderLayout();
				if (!this.isDoneLoadFirst) {
					this.isDoneLoadFirst = true;
				}
				if (this.isSearchingProblem) {
					this.isSearchingProblem = false;
				}
				if (this.isRefreshScreen) {
					this.isRefreshScreen = false;
				}
			}
			this.parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_CANTHUCHIEN, modelEvent.getActionEvent().startTimeFromBoot);

			break;
		case ActionEventConstant.ACTION_UPDATE_CHANGE_PROBLEM_STATUS:
			isSearchingProblem = true;
			this.getListProblemsOfScreen(0, true, false);
			this.parent.closeProgressDialog();
			break;
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
		case ActionEventConstant.GET_LIST_PROBLEM_OF_GSNPP_NEED_DO_IT:
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.ACTION_UPDATE_CHANGE_PROBLEM_STATUS:
			this.parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbListProblems) {
			if (tbListProblems.getPagingControl().getCurrentPage() - 1 >= 0) {
				this.getListProblemsOfScreen(tbListProblems.getPagingControl()
						.getCurrentPage() - 1, false, false);
			} else {
				this.getListProblemsOfScreen(0, false, false);
			}
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		// change problem status
		if (action == ActionEventConstant.ACTION_UPDATE_CHANGE_PROBLEM_STATUS) {
			SupervisorProblemOfGSNPPDTO currentProblem = (SupervisorProblemOfGSNPPDTO) data;
			showPopupConfirmedDoProblem(currentProblem);
		}
		// show popup problem detail
		else if (action == ActionEventConstant.SHOW_PROBLEM_DETAIL_POPUP) {
			SupervisorProblemOfGSNPPDTO currentProblem = (SupervisorProblemOfGSNPPDTO) data;
			this.showFollowProblemDetail(currentProblem);
		}
	}
	
	
	/**
	* Confirmed van de thuc hien
	* @author: BangHN
	* @return: void
	* @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void showPopupConfirmedDoProblem(SupervisorProblemOfGSNPPDTO data){
		this.currentObjectUpdateToServer = data;
		GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent,
				StringUtil.getString(R.string.TEXT_CONFIRM_UPDATE_PROBLEM_DONE_2),
				StringUtil.getString(R.string.TEXT_AGREE), ACTION_DONE_OK, StringUtil.getString(R.string.TEXT_DENY),
				ACTION_DONE_CANCEL, null, false, false);
	}

	/**
	 * display popup problem detail
	 * @param dto
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 7, 2012
	 */
	private void showFollowProblemDetail(SupervisorProblemOfGSNPPDTO dto) {
		if (alertFollowProblemDetail == null) {
			Builder build = new AlertDialog.Builder(parent,
					R.style.CustomDialogTheme);
			popupProblemDetail = new PopupProblemDetailView(parent, this);
			build.setView(popupProblemDetail.viewLayout);
			alertFollowProblemDetail = build.create();
			Window window = alertFollowProblemDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255,
					255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		popupProblemDetail.renderLayoutWithObject(dto);
		alertFollowProblemDetail.show();
	}

	/**
	 * update problem of gsnpp status
	 * @param currentData
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 7, 2012
	 */
	private void requestUpdateFeedbackStatus(SupervisorProblemOfGSNPPDTO currentData) {
		this.parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent event = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_FEED_BACK_ID,String.valueOf(currentData.feedbackId));
		data.putString(IntentConstants.INTENT_FEED_BACK_STATUS,String.valueOf(FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE));
		data.putString(IntentConstants.INTENT_DONE_DATE, DateUtils.now());
		data.putString(IntentConstants.INTENT_UPDATE_DATE, DateUtils.now());
		data.putString(IntentConstants.INTENT_UPDATE_USER, GlobalInfo.getInstance().getProfile().getUserData().userCode);
		event.sender = this;
		event.action = ActionEventConstant.ACTION_UPDATE_CHANGE_PROBLEM_STATUS;
		event.viewData = data;
		SuperviorController.getInstance().handleViewEvent(event);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (arg0 == etInputFromDate) {
			if (!etInputFromDate.onTouchEvent(arg1)) {
				currentCalendar = DATE_FROM_CONTROL;
				parent.fragmentTag = TrackAndFixProblemsOfGSNPPView.TAG;
				parent.showDialog(GlobalBaseActivity.DATE_DIALOG_ID);
			}
		}
		if (arg0 == etInputEndDate) {
			if (!etInputEndDate.onTouchEvent(arg1)) {
				currentCalendar = DATE_TO_CONTROL;
				parent.fragmentTag = TrackAndFixProblemsOfGSNPPView.TAG;
				parent.showDialog(GlobalBaseActivity.DATE_DIALOG_ID);
			}
		}
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg1 == this.btSearch) {

		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		if (arg0 == spStatusCode) {
			if (currentStatusSelected != spStatusCode.getSelectedItemPosition()) {
				currentStatusSelected = spStatusCode.getSelectedItemPosition();
			}
			onClick(btSearch);
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btSearch) {
			this.isSearchingProblem = true;
			String strFromDate = etInputFromDate.getText().toString().trim();
			String strToDate = etInputEndDate.getText().toString().trim();
			try {
				if (!StringUtil.isNullOrEmpty(strFromDate)) {
					Date tn = StringUtil.stringToDate(strFromDate, Constants.STR_BLANK);
					fromDate = StringUtil.dateToString(tn, "yyyy-MM-dd");
				} else {
					fromDate = Constants.STR_BLANK;
				}
				if (!StringUtil.isNullOrEmpty(strToDate)) {
					Date tn = StringUtil.stringToDate(strToDate, Constants.STR_BLANK);
					toDate = StringUtil.dateToString(tn, "yyyy-MM-dd");
				} else {
					toDate = Constants.STR_BLANK;
				}
			} catch (Exception e) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
				return;
			}
			if (!StringUtil.isNullOrEmpty(fromDate) && !StringUtil.isNullOrEmpty(toDate)
					&& fromDate.compareTo(toDate) > 0) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_ERROR));
			} else {
				currentStatusSelected = this.spStatusCode.getSelectedItemPosition();
				this.getListProblemsOfScreen(0, true, false);
			}
		} 
		else if (v == popupProblemDetail.btPerform) {
			this.currentObjectUpdateToServer = popupProblemDetail.currentDTO;
			showPopupConfirmedDoProblem(popupProblemDetail.currentDTO);
			if (this.alertFollowProblemDetail.isShowing()) {
				this.alertFollowProblemDetail.dismiss();
			}
		} 
		else if (v == popupProblemDetail.btCloseProblemDetail) {
			if (this.alertFollowProblemDetail.isShowing()) {
				this.alertFollowProblemDetail.dismiss();
			}
		} else {
			super.onClick(v);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				isSearchingProblem = true;
				reset();
				this.getListProblemsOfScreen(0, true, false);
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
	
	/**
	 * reset du lieu man hinh ve ban dau
	 * @author YenNTH
	 * 
	 */
	private void reset() {
		// TODO Auto-generated method stub
		if (spStatusCode != null) {
			spStatusCode.setSelection(0);
		}
		currentStatusSelected = 0;
		etInputFromDate.setText(Constants.STR_BLANK);
		etInputEndDate.setText(Constants.STR_BLANK);
	}

}
