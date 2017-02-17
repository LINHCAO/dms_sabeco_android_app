/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.ComboboxFollowProblemDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.FollowProblemDTO;
import com.viettel.dms.dto.view.FollowProblemItemDTO;
import com.viettel.dms.dto.view.HistoryItemDTO;
import com.viettel.dms.dto.view.TBHVCustomerListDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.tbhv.TBHVCustomerListView;
import com.viettel.sabeco.R;

/**
 * 
 * MH theo dõi khắc phục vấn đề role GSBH
 * 
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
public class FollowProblemView extends BaseFragment implements OnEventControlListener, VinamilkTableListener,
		OnClickListener, OnTouchListener, OnItemSelectedListener {

	public static final String TAG = FollowProblemView.class.getName();
	private static final int ACTION_MENU_TRACK = 2;
	// show screen list problem that gsnpp need do
	private static final int ACTION_MENU_GSNPP_NEED_DO = 1;
	private static final int ACTION_GET_CUSTOMER_LIST = 3;
	private static final int ACTION_CHOOSE_CUSTOMER = 4;
	public static final int ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL = 5;
	public static final int ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL = 6;

	private int currentCalender;
	Spinner spinnerStaffCode;// ma NVBH
	Spinner spinnerStatus;// loai khac phuc
	Spinner spinnerTypeProblem;// loai khac phuc
	VNMEditTextClearable etCustomerCode;// ma khach hang
	VNMEditTextClearable edTN;// tu ngay
	VNMEditTextClearable edDN;// den ngay
	Button btSearch;// button tim
	Button btAddNote;// button them ghi chu

	private DMSTableView tbList;
	GlobalBaseActivity parent;
	FollowProblemDTO dto;// dto theo doi khac phuc
	private boolean isFirstTime;// kiem tra lan dau tien vao man hinh
	private static final int DATE_FROM_CONTROL = 1;// control tu ngay
	private static final int DATE_TO_CONTROL = 2;// control den ngay
	public static final int CONFIRM_OK = 31;
	public static final int CONFIRM_CANCEL = 32;

	// combobox
	private List<DisplayProgrameItemDTO> listNVBH = null;// list nvbh
	private List<DisplayProgrameItemDTO> listStatus = null;// list trang thai
	private List<DisplayProgrameItemDTO> listTypeProblem = null;// list van de

	// save du lieu cho viec tim kiem
	private String staffCodeForRequest = Constants.STR_BLANK;// ma nv
	private String statusForRequest = Constants.STR_BLANK;// trang thai
	private String typeProblemForRequest = Constants.STR_BLANK;//loai van de
	private String customerCodeForRequest = Constants.STR_BLANK;//ma kh
	private String fromDateForRequest = Constants.STR_BLANK;// tu ngay
	private String toDateForRequest = Constants.STR_BLANK;//den ngay

	FollowProblemDetailView followProblemDetail;// MH chi tiet van de
	TBHVCustomerListView customerListView;// ds khach hang
	FollowProblemItemDTO dtoGoToDetail;// dto theo doi khac phuc khi click xem chi tiet
	AlertDialog alertFollowProblemDetail;	// dialog product detail view
	AlertDialog customerListPopup;	// dialog product detail view
	int page;
	boolean isGetTotalPage;// get total page or not
	TBHVCustomerListDTO customerListDto = new TBHVCustomerListDTO();// Ds Kh
																	// hien thi
	CustomerDTO customer;// Thong tin KH tu MH Huan luyen ngay TBHV
	// Search customer
	private String customerCodeSearch = Constants.STR_BLANK;// customer code for search
	private String customerNameSearch = Constants.STR_BLANK;// customer name for search
	private int staffIndex;
	private int statusIndex;
	private int typeProblemIndex;

	public static FollowProblemView newInstance() {
		FollowProblemView instance = new FollowProblemView();
		// Supply index input as an argument.
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		isFirstTime = true;
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_supervisor_follow_problem_list, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_SUPERVISOR_LIST_FOLLOW_PROBLEM));
		this.createHeaderMenu();
		spinnerStaffCode = (Spinner) v.findViewById(R.id.spinnerStaffCode);
		spinnerStaffCode.setOnItemSelectedListener(this);
		spinnerStatus = (Spinner) v.findViewById(R.id.spinnerStatus);
		spinnerStatus.setOnItemSelectedListener(this);
		spinnerTypeProblem = (Spinner) v.findViewById(R.id.spinnerTypeProblem);
		spinnerTypeProblem.setOnItemSelectedListener(this);
		etCustomerCode = (VNMEditTextClearable) v.findViewById(R.id.tvCustomerCode);
		etCustomerCode.setOnTouchListener(this);
		etCustomerCode.setIsHandleDefault(false);
		edTN = (VNMEditTextClearable) v.findViewById(R.id.edTN);
		edTN.setOnTouchListener(this);
		edDN = (VNMEditTextClearable) v.findViewById(R.id.edDN);
		edDN.setOnTouchListener(this);
		edDN.setIsHandleDefault(false);
		edTN.setIsHandleDefault(false);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		btAddNote = (Button) v.findViewById(R.id.btAddNote);
		btAddNote.setOnClickListener(this);
		tbList = (DMSTableView) v.findViewById(R.id.tbProblem);
		tbList.setListener(this);
		// send request
		resetVar();
		getListProblem(true, true);
		return v;
	}

	/**
	 * 
	 * create header menu for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: YenNTH
	 * 
	 */
	public void createHeaderMenu() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_MENU_NEED_DO_IT), R.drawable.icon_checkpoint,ACTION_MENU_GSNPP_NEED_DO);
		addMenuItem(StringUtil.getString(R.string.TEXT_MENU_ITEM_TRACK), R.drawable.icon_order, ACTION_MENU_TRACK);
		setMenuItemFocus(2);
	}

	/**
	 * 
	 * SKU
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getListHistory() {
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_ID, dtoGoToDetail.staffId);
		b.putString(IntentConstants.INTENT_CUSTOMER_ID, dtoGoToDetail.customerId);
		b.putString(IntentConstants.INTENT_PRODUCT_ID, dtoGoToDetail.productId);
		e.viewData = b;
		e.action = ActionEventConstant.GET_LIST_HISTORY;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * request du lieu
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getListProblem(boolean repareReset, boolean getCombobox) {
		// TODO Auto-generated method stub
		parent.showProgressDialog(getString(R.string.loading));
		Bundle data = new Bundle();
		int page = 0;
		if (!repareReset) {
			page = (tbList.getPagingControl().getCurrentPage() - 1);
		}
		String strPage = " limit " + (page * Constants.NUM_ITEM_PER_PAGE) + "," + Constants.NUM_ITEM_PER_PAGE;
		data.putString(IntentConstants.INTENT_PAGE, strPage);
		data.putBoolean(IntentConstants.INTENT_CHECK_PAGGING, !repareReset);
		if (!StringUtil.isNullOrEmpty(this.fromDateForRequest)) {
			data.putString(IntentConstants.INTENT_FIND_ORDER_FROM_DATE, fromDateForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.toDateForRequest)) {
			data.putString(IntentConstants.INTENT_FIND_ORDER_TO_DATE, toDateForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.customerCodeForRequest)) {
			data.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCodeForRequest.substring(0, 3));
		}
		if (!StringUtil.isNullOrEmpty(this.staffCodeForRequest)) {
			data.putString(IntentConstants.INTENT_STAFF_ID_PARA, staffCodeForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.statusForRequest)) {
			data.putString(IntentConstants.INTENT_STATE, statusForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.typeProblemForRequest)) {
			data.putString(IntentConstants.INTENT_TYPE_PROBLEM, typeProblemForRequest);
		}
		// hien tai chua co staff ID
		data.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, getCombobox);
		ActionEvent e = new ActionEvent();
		if (repareReset) {
			e.tag = 11;
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_POST_FEEDBACK: {
			TBHVCustomerListDTO temp = (TBHVCustomerListDTO) modelEvent.getModelData();
			if (page == 1 && isGetTotalPage) {
				customerListDto.totalCustomer = temp.totalCustomer;
				customerListView.setTotalSize(customerListDto.totalCustomer);
			}
			customerListDto.listCustomer.clear();
			customerListDto.listCustomer.addAll(temp.listCustomer);
			if (customerListView != null) {
				customerListView.renderLayout(customerListDto, customer);
			}
			break;
		}
		case ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM:
			if (modelEvent.getActionEvent().tag == 11) {
				tbList.getPagingControl().totalPage = -1;
				tbList.getPagingControl().setCurrentPage(1);
			}
			dto = (FollowProblemDTO) modelEvent.getModelData();
			ComboboxFollowProblemDTO combboxDTO = (ComboboxFollowProblemDTO) dto.comboboxDTO;
			if (combboxDTO != null) {
				updateDataCombobox(combboxDTO);
			}
			renderData();
			requestInsertLogKPI(HashMapKPI.GSNPP_THEODOIKHACPHUC, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.UPDATE_GSNPP_FOLLOW_PROBLEM_DONE: {
			if (followProblemDetail != null) {
				if (alertFollowProblemDetail != null) {
					alertFollowProblemDetail.dismiss();
				}
			}
			// update row
			FollowProblemItemDTO dto = (FollowProblemItemDTO) modelEvent.getActionEvent().viewData;
			updateRow(dto, true);
			//dto.
			parent.showDialog(R.string.TEXT_CONFIRM_PROBLEM_DONE);
			break;
		}
		case ActionEventConstant.GET_LIST_HISTORY: {
			dtoGoToDetail.vHistory = (Vector<HistoryItemDTO>) (modelEvent.getModelData());
			showFollowProblemDetail(dtoGoToDetail);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	/**
	 * Cap nhat lai mot row cua table
	 * 
	 * @author: YenNTH
	 * @param dto2
	 * @return: void
	 * @throws:
	 */
	private void updateRow(FollowProblemItemDTO dtoFollow, boolean done) {
		// TODO Auto-generated method stub
		int positionRow = 0;
		for (int i = 0, length = dto.list.size(); i < length; i++) {
			FollowProblemItemDTO tempDTO = dto.list.get(i);
			if (tempDTO.feedBackId == dtoFollow.feedBackId) {
				positionRow = i;
				break;
			}
		}
		List<DMSTableRow> listRow = tbList.getListChildRow();
		if (listRow != null && listRow.size() > 0) {
			FollowProblemRow followRow = (FollowProblemRow) listRow.get(positionRow);
			if (dtoFollow.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_OWNER_DONE) {
				followRow.updateCheckBoxDone(done);
				// them show dialog
				getListProblem(true, false);
			} else if (dtoFollow.status == FeedBackDTO.FEEDBACK_STATUS_CREATE) {
				//
				followRow.updateCheckBoxNew();
				followRow.setColorBackgroundForRow(ImageUtil.getColor(R.color.WHITE));
				if(DateUtils.compareWithNow(dtoFollow.remindDate, "dd/MM/yyyy") == -1){
					followRow.setColorForTextRow(ImageUtil.getColor(R.color.RED));
				}
			}
		}
		parent.showDialog(R.string.TEXT_CONFIRM_PROBLEM_DONE);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.UPDATE_GSNPP_FOLLOW_PROBLEM_DONE: {
			// update row
			FollowProblemItemDTO dto = (FollowProblemItemDTO) modelEvent.getActionEvent().viewData;
			updateRow(dto, false);
			break;
		}
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	/**
	 * 
	 * Render layout cho man hinh
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void renderData() {
		if (isFirstTime) {
			initHeaderTable(tbList, new FollowProblemRow(parent, this));
			tbList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
			isFirstTime = false;
		}
		// TODO Auto-generated method stub
		int position = (tbList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
		tbList.clearAllData();
		if(dto.list.size()>0){
//			tbList.hideNoContentRow();
			for (FollowProblemItemDTO itemDTO: dto.list) {
				FollowProblemRow row = new FollowProblemRow(parent, this);
				row.render(itemDTO, position);
				position++;
				row.setListener(this);
				tbList.addRow(row);
			}
		}else{
			tbList.showNoContentRow();
		}
		if (tbList.getPagingControl().totalPage < 0){
			tbList.setTotalSize(dto.total, 1);
		}

	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resetVar();
	}
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case CONFIRM_OK: {
			// send request problem is done.
			FollowProblemItemDTO dto = (FollowProblemItemDTO) data;
			dto.status = 3; // 3 thuc hien dong van de
			dto.numReturn++;
			dto.updateUser=String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().userCode);
			dto.updateDate=DateUtils.now();
			updateCheckBox(dto, false);
			getListProblem(true, false);
			break;
		}
		case CONFIRM_CANCEL: {
			FollowProblemItemDTO dto = (FollowProblemItemDTO) data;
			updateRow(dto, false);
			break;
		}
		case ACTION_MENU_GSNPP_NEED_DO:
			e.sender = this;
			e.viewData = new Bundle();
			e.action = ActionEventConstant.GO_TO_LIST_PROBLEM_OF_GSNPP_NEED_DO_IT;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_GET_CUSTOMER_LIST: {
			Bundle bundle = (Bundle) data;
			customerCodeSearch = bundle.getString(IntentConstants.INTENT_CUSTOMER_CODE);
			customerNameSearch = bundle.getString(IntentConstants.INTENT_CUSTOMER_NAME);
			page = bundle.getInt(IntentConstants.INTENT_PAGE);
			isGetTotalPage = bundle.getBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE);
			getCustomerList(page, isGetTotalPage);
			break;
		}
		case ACTION_CHOOSE_CUSTOMER: {
			customer = (CustomerDTO) data;
			if(!StringUtil.isNullOrEmpty(customer.customerCode)) {
				etCustomerCode.setText(customer.customerCode.substring(0, 3) + " - " + customer.customerName);
			}else{
				etCustomerCode.setText(customer.customerName);
			}
			customerListPopup.dismiss();
			break;
		}
		case ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL: {
			updateCheckBox((FollowProblemItemDTO)data, true);			
			break;
		}
		case ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL: {
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		getListProblem(false, false);
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.GO_TO_FOLLOW_PROBLEM_DETAIL: {
			// show detail problem
			dtoGoToDetail = (FollowProblemItemDTO) data;
			if ("SKU".equals(dtoGoToDetail.type)) {
				getListHistory();
			} else {
				showFollowProblemDetail(dtoGoToDetail);
			}
			break;
		}
		case ActionEventConstant.UPDATE_GSNPP_FOLLOW_PROBLEM_DONE: {
			// show dialog
			confirmDoneProblem(data);
			break;
		}

		default:
			break;
		}

	}

	/**
	 * Show dialog confirm co phe duyet van de ko?
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return: void
	 * @throws:
	 */

	public void confirmDoneProblem(Object data) {
		GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent,StringUtil.getString(R.string.TEXT_CONFIRM_UPDATE_PROBLEM_DONE),StringUtil.getString(R.string.TEXT_AGREE), CONFIRM_OK, StringUtil.getString(R.string.TEXT_DENY),CONFIRM_CANCEL, data, false, false);
	}

	/**
	 * 
	 * Hien thi pop up danh sach khach hang
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void showCustomerList() {
		if (customerListPopup == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			customerListView = new TBHVCustomerListView(parent, this, ACTION_GET_CUSTOMER_LIST, ACTION_CHOOSE_CUSTOMER);
			build.setView(customerListView.viewLayout);
			customerListPopup = build.create();
			customerListPopup.setCanceledOnTouchOutside(false);
			Window window = customerListPopup.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
			customerListView.ivClose.setOnClickListener(this);
		}
		customerListPopup.show();
	}

	/**
	 * 
	 * lay danh sach khach hang
	 * 
	 * @author: YenNTH
	 * @param : page
	 * @param : isGetTotolPage
	 * @return: void
	 * @throws:
	 */
	private void getCustomerList(int page, boolean getTotalPage) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putInt(IntentConstants.INTENT_PAGE, page);
		if (listNVBH.size() > 0) {
			data.putString(IntentConstants.INTENT_STAFF_ID,listNVBH.get(spinnerStaffCode.getSelectedItemPosition()).value);
		} else {
			data.putString(IntentConstants.INTENT_STAFF_ID, Constants.STR_BLANK);
		}
		data.putString(IntentConstants.INTENT_SHOP_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		data.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, getTotalPage);
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCodeSearch);
		data.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerNameSearch);
		customerListDto.codeCus = customerCodeSearch;
		customerListDto.nameCus = customerNameSearch;

		e.viewData = data;
		e.action = ActionEventConstant.GET_CUSTOMER_LIST_FOR_POST_FEEDBACK;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * xu ly su kien khi nhan button tim kiem
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void searchListProblem(){
		GlobalUtil.forceHideKeyboard(parent);
		// luu lai gia tri de thuc hien tim kiem
		String dateTimePattern = StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN);
		Pattern pattern = Pattern.compile(dateTimePattern);
		if (!StringUtil.isNullOrEmpty(this.edTN.getText().toString())) {
			String strTN = this.edTN.getText().toString().trim();
			Matcher matcher = pattern.matcher(strTN);
			if (!matcher.matches()) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
				return;
			} else {
				try {
					Date tn = StringUtil.stringToDate(strTN, Constants.STR_BLANK);
					String strFindTN = StringUtil.dateToString(tn, "yyyy-MM-dd");

					fromDateForRequest = strFindTN;
				} catch (Exception ex) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_START_DATE_SEARCH_SYNTAX_ERROR));
					return;
				}
			}
		} else {
			fromDateForRequest = Constants.STR_BLANK;
		}
		if (!StringUtil.isNullOrEmpty(this.edDN.getText().toString())) {
			String strDN = this.edDN.getText().toString().trim();
			Matcher matcher = pattern.matcher(strDN);
			if (!matcher.matches()) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
				return;
			} else {
				try {
					Date dn = StringUtil.stringToDate(strDN, Constants.STR_BLANK);
					String strFindDN = StringUtil.dateToString(dn, "yyyy-MM-dd");
					toDateForRequest = strFindDN;
				} catch (Exception ex) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
					return;
				}
			}
		} else {
			toDateForRequest = Constants.STR_BLANK;
		}
		
		if (!StringUtil.isNullOrEmpty(etCustomerCode.getText().toString())) {
			customerCodeForRequest = etCustomerCode.getText().toString().trim();
		} else {
			customerCodeForRequest = Constants.STR_BLANK;
		}
		if (listNVBH == null || listStatus == null || listTypeProblem == null) {
			staffCodeForRequest = Constants.STR_BLANK;
			statusForRequest = Constants.STR_BLANK;
			typeProblemForRequest = Constants.STR_BLANK;
		} else {
			if(listNVBH.size()>0 ){
				int selectionPT = spinnerStaffCode.getSelectedItemPosition();
				if (selectionPT < 0){
					selectionPT = 0;
				}
				DisplayProgrameItemDTO dtoNVBH = listNVBH.get(selectionPT);
				staffCodeForRequest = dtoNVBH.value;
			}else {
				staffCodeForRequest = Constants.STR_BLANK;
			}
			int selectionSP = spinnerStatus.getSelectedItemPosition();
			if (selectionSP < 0)
				selectionSP = 0;

			DisplayProgrameItemDTO dtoStatus = listStatus.get(selectionSP);
			statusForRequest = dtoStatus.value;
			if(listTypeProblem.size() > 0){
				int selectionTypeProblem = spinnerTypeProblem.getSelectedItemPosition();
				if (selectionTypeProblem < 0){
					selectionTypeProblem = 0;
				}
				DisplayProgrameItemDTO dtoTypeProblem = listTypeProblem.get(selectionTypeProblem);
				typeProblemForRequest = dtoTypeProblem.value;
			}else {
				typeProblemForRequest = Constants.STR_BLANK;
			}
		}
		if (!StringUtil.isNullOrEmpty(fromDateForRequest)&& !StringUtil.isNullOrEmpty(toDateForRequest)&& DateUtils.compareDate(fromDateForRequest,toDateForRequest) == 1) {
			GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_DATE_TIME_INVALID_2),StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),0, null, false);
		} else {
			getListProblem(true, false);
		}
	
	}

	/**
	 * 
	 * xu ly khi nhan button them ghi chu
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void addNote(){
		Bundle b = new Bundle();
		List<DisplayProgrameItemDTO> dataNvbh = new ArrayList<DisplayProgrameItemDTO>();
		if (listNVBH.size() > 1) {
			for (int i = 0, size = listNVBH.size(); i < size - 1; i++) {
				dataNvbh.add((DisplayProgrameItemDTO) listNVBH.get(i + 1));
			}
		} else {
			for (int i = 0, size = listNVBH.size(); i < size; i++) {
				dataNvbh.add((DisplayProgrameItemDTO) listNVBH.get(i));
			}
		}
		b.putSerializable(IntentConstants.INTENT_LIST_NVBH, (Serializable) dataNvbh);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.POST_FEEDBACK_GSNPP;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
	
	}
	/**
	 * thuc hien su kien click vao control tren man hinh
	 */
	public void onClick(View v) {
		if (v == btAddNote) {
			addNote();
		} else if (v == btSearch) {
			searchListProblem();
		}else if( customerListView != null &&  v == customerListView.ivClose) {
			if(customerListPopup != null && customerListPopup.isShowing()){
				customerListPopup.dismiss();
			}
		}
		if (followProblemDetail != null && followProblemDetail.btCloseFollowProblemDetail == v) {
			if (alertFollowProblemDetail != null && alertFollowProblemDetail.isShowing()) {
				alertFollowProblemDetail.dismiss();
			}
		}
	}

	/**
	 * 
	 * cap nhat du lieu cho combobox
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @param fromDetail
	 * @return: void
	 * @throws:
	 */
	public void updateCheckBox(FollowProblemItemDTO dto, boolean fromDetail) {
//		parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		e.viewData = dto;
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_GSNPP_FOLLOW_PROBLEM_DONE;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (v == edTN) {
			if (!edTN.onTouchEvent(arg1)) {
				currentCalender = DATE_FROM_CONTROL;
				parent.fragmentTag = FollowProblemView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edTN.getText().toString(), true);
			}
		}
		if (v == edDN) {
			if (!edDN.onTouchEvent(arg1)) {
				currentCalender = DATE_TO_CONTROL;
				parent.fragmentTag = FollowProblemView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edDN.getText().toString(), true);
			}
		}
		if (v == etCustomerCode) {
			if (!etCustomerCode.onTouchEvent(arg1)) {
				showCustomerList();
				page = 1;
				customerCodeSearch = Constants.STR_BLANK;
				customerNameSearch = Constants.STR_BLANK;
				isGetTotalPage = true;
				getCustomerList(page, isGetTotalPage);
			}
		}
		return true;
	}

	/**
	 * Ham tra ve khi picker date
	 * 
	 * @author: YenNTH
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return: void
	 * @throws:
	 */
	public void update(int dayOfMonth, int monthOfYear, int year) {
		// TODO Auto-generated method stub
		String sDay = String.valueOf(dayOfMonth);
		String sMonth = String.valueOf(monthOfYear + 1);
		if (dayOfMonth < 10) {
			sDay = "0" + sDay;
		}
		if (monthOfYear + 1 < 10) {
			sMonth = "0" + sMonth;
		}
		if (currentCalender == DATE_FROM_CONTROL) {
			edTN.setTextColor(ImageUtil.getColor(R.color.BLACK));
			edTN.setText(new StringBuilder()
			// Month is 0 based so add 1
					.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
		}
		if (currentCalender == DATE_TO_CONTROL) {
			edDN.setTextColor(ImageUtil.getColor(R.color.BLACK));
			edDN.setText(new StringBuilder()
			// Month is 0 based so add 1
					.append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
		}
	}

	/**
	 * Cap nhat du lieu cho loai CT va nganh hang
	 * 
	 * @author: YenNTH
	 * @param modelData
	 * @return: void
	 * @throws:
	 */
	private void updateDataCombobox(ComboboxFollowProblemDTO modelData) {
		// TODO Auto-generated method stub
		if (listNVBH == null || listStatus == null || listTypeProblem == null) {
			listNVBH = new ArrayList<DisplayProgrameItemDTO>();
			listStatus = new ArrayList<DisplayProgrameItemDTO>();
			listTypeProblem = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = new DisplayProgrameItemDTO();
			itemDTO.name = StringUtil.getString(R.string.ALL);
			itemDTO.value = Constants.STR_BLANK;
			if (modelData.listNVBH.size() > 1) {
				listNVBH.add(itemDTO);
			}
			listStatus.add(itemDTO);
			if (modelData.listTypeProblem.size() > 1) {
				listTypeProblem.add(itemDTO);
			}
			if (modelData.listNVBH != null) {
				for (int j =0, jlength = modelData.listNVBH.size(); j < jlength; j++) {
					itemDTO = modelData.listNVBH.get(j);
					listNVBH.add(itemDTO);
				}
			}

			if (modelData.listTypeProblem != null) {
				for (int j = 0, jlength = modelData.listTypeProblem.size(); j < jlength; j++) {
					itemDTO = modelData.listTypeProblem.get(j);
					listTypeProblem.add(itemDTO);
				}
			}
			DisplayProgrameItemDTO itemStatusDTO;
			itemStatusDTO = new DisplayProgrameItemDTO();
			itemStatusDTO.name = StringUtil.getString(R.string.TEXT_PROBLEM_CREATE_NEW);
			itemStatusDTO.value = DisplayProgrameItemDTO.VALUE_PROBLEM_CREATE_NEW;
			listStatus.add(itemStatusDTO);
			itemStatusDTO = new DisplayProgrameItemDTO();
			itemStatusDTO.name = StringUtil.getString(R.string.TEXT_NVBH_IMPLEMENTED);
			itemStatusDTO.value = DisplayProgrameItemDTO.VALUE_NVBH_IMPLEMENTED;
			listStatus.add(itemStatusDTO);
			itemStatusDTO = new DisplayProgrameItemDTO();
			itemStatusDTO.name = StringUtil.getString(R.string.TEXT_FEEDBACK_TYPE_TBHV_DONE);
			itemStatusDTO.value = DisplayProgrameItemDTO.VALUE_FEEDBACK_TYPE_TBHV_DONE;
			listStatus.add(itemStatusDTO);

		}
		int lengthType = listNVBH.size();
		int lengthDepart = listStatus.size();
		int lengthTypeProblem = listTypeProblem.size();
		String nvbhName[] = new String[lengthType];
		String statusName[] = new String[lengthDepart];
		String typeProblemName[] = new String[lengthTypeProblem];
		// khoi tao gia tri cho ma NVBH
		for (int i = 0; i < lengthType; i++) {
			DisplayProgrameItemDTO dto = listNVBH.get(i);
			nvbhName[i] = dto.name;
		}
		// khoi tao gia tri cho trang thai
		for (int i = 0; i < lengthDepart; i++) {
			DisplayProgrameItemDTO dto = listStatus.get(i);
			statusName[i] = dto.name;
		}
		// khoi tao gia tri cho loai van de
		for (int i = 0; i < lengthTypeProblem; i++) {
			DisplayProgrameItemDTO dto = listTypeProblem.get(i);
			typeProblemName[i] = dto.name;
		}
		SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, nvbhName);
		this.spinnerStaffCode.setAdapter(adapterNVBH);
		spinnerStaffCode.setSelection(0);

		SpinnerAdapter adapterDepart = new SpinnerAdapter(parent, R.layout.simple_spinner_item, statusName);
		this.spinnerStatus.setAdapter(adapterDepart);
		spinnerStatus.setSelection(0);

		SpinnerAdapter adapterTypeProblem = new SpinnerAdapter(parent, R.layout.simple_spinner_item, typeProblemName);
		this.spinnerTypeProblem.setAdapter(adapterTypeProblem);
		spinnerTypeProblem.setSelection(0);

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spinnerStaffCode && staffIndex != arg2) {
			staffIndex = arg2;
			etCustomerCode.setText(Constants.STR_BLANK);
			onClick(btSearch);
		}else if (arg0 == spinnerStatus) {
			if (statusIndex != spinnerStatus.getSelectedItemPosition()) {
				statusIndex = spinnerStatus.getSelectedItemPosition();
				onClick(btSearch);
			}
		}else if (arg0 == spinnerTypeProblem) {
			if (typeProblemIndex != spinnerTypeProblem.getSelectedItemPosition()) {
				typeProblemIndex = spinnerTypeProblem.getSelectedItemPosition();
				onClick(btSearch);
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * Hien thi pop up chi tiet van de
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	private void showFollowProblemDetail(FollowProblemItemDTO dto) {
		if (alertFollowProblemDetail == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			followProblemDetail = new FollowProblemDetailView(parent, this);
			build.setView(followProblemDetail.viewLayout);
			alertFollowProblemDetail = build.create();
			Window window = alertFollowProblemDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		followProblemDetail.updateLayout(dto);
		alertFollowProblemDetail.show();
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				resetVar();
				getListProblem(true, true);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * reset cac gia tri khi nhan cap nhat
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void resetVar() {
		// TODO Auto-generated method stub
		staffIndex = 0;
		statusIndex =0;
		typeProblemIndex = 0;
		staffCodeForRequest = Constants.STR_BLANK;
		statusForRequest = Constants.STR_BLANK;
		typeProblemForRequest = Constants.STR_BLANK;
		fromDateForRequest = Constants.STR_BLANK;
		customerCodeForRequest = Constants.STR_BLANK;
		toDateForRequest = Constants.STR_BLANK;
		etCustomerCode.setText(Constants.STR_BLANK);
		edDN.setText(Constants.STR_BLANK);
		edTN.setText(Constants.STR_BLANK);
		spinnerStaffCode.setSelection(0);
		spinnerStatus.setSelection(0);
		listNVBH = null;
		listStatus = null;
		listTypeProblem = null;
	}
}
