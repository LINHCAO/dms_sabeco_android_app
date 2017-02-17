/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.view.ComboboxFollowProblemDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
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
import com.viettel.sabeco.R;

/**
 * 
 * MH theo dõi khắc phục vấn đề role GST
 * 
 * @author: YenNTH
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVFollowProblemView extends BaseFragment implements OnEventControlListener, VinamilkTableListener,
		OnClickListener, OnTouchListener, OnItemSelectedListener {

	public static final int ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL = 5;
	public static final int ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL = 6;

	public static final String TAG = TBHVFollowProblemView.class.getName();
	private int currentCalender;// Luu trang thai chon ngay cua from or to
	Spinner spinnerStaffCode;// ma GSNPP
	Spinner spinnerStatus;// loai khac phuc
	VNMEditTextClearable edTN;// tu ngay
	VNMEditTextClearable edDN;// den ngay
	Button btSearch;// button tim
	Button btAdd;// button add

	private DMSTableView tbList;// table ds van de
	GlobalBaseActivity parent;
	TBHVFollowProblemDTO dto;// Du lieu man hinh
	private static final int DATE_FROM_CONTROL = 1;
	private static final int DATE_TO_CONTROL = 2;
	public static final int CONFIRM_UPDATE_OK = 31;
	public static final int CONFIRM_UPDATE_CANCEL = 32;
	public static final int CONFIRM_UPDATE_NEW_OK = 35;
	public static final int CONFIRM_UPDATE_NEW_CANCEL = 36;
	public static final int CONFIRM_DELETE_OK = 33;
	public static final int CONFIRM_DELETE_CANCEL = 34;

	// combobox
	private List<DisplayProgrameItemDTO> listGSNPPTTTT = null;// ds gsnpp,tttt
	private List<DisplayProgrameItemDTO> listStatus = null;// ds trang thai

	// save du lieu cho viec tim kiem
	private String staffCodeForRequest = Constants.STR_BLANK;// staff id
	private String statusForRequest = Constants.STR_BLANK;// trang thai
	private String fromDateForRequest = Constants.STR_BLANK;// tu ngay
	private String toDateForRequest = Constants.STR_BLANK;// den ngay
	private String typeProblemForRequest = Constants.STR_BLANK;//loai van de

	TBHVFollowProblemDetailView followProblemDetail;// MH chi tiet van de
	TBHVFollowProblemItemDTO dtoGoToDetail;// Du lieu 1 van de
	// dialog product detail view
	AlertDialog alertFollowProblemDetail;
	boolean fromDetail;
	private Spinner spinnerTypeProblem;
	private ArrayList<DisplayProgrameItemDTO> listTypeProblem;// ds loai van de
	ComboboxFollowProblemDTO comboboxDTO = new ComboboxFollowProblemDTO();
	
	public static TBHVFollowProblemView newInstance() {
		TBHVFollowProblemView instance = new TBHVFollowProblemView();
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_follow_problem_list, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_TBHV_LIST_FOLLOW_PROBLEM));

		spinnerStaffCode = (Spinner) v.findViewById(R.id.spinnerStaffCode);
		spinnerStaffCode.setOnItemSelectedListener(this);
		spinnerStatus = (Spinner) v.findViewById(R.id.spinnerStatus);
		spinnerTypeProblem = (Spinner) v.findViewById(R.id.spinnerTypeProblem);
		edTN = (VNMEditTextClearable) v.findViewById(R.id.edTN);
		edTN.setOnTouchListener(this);
		edDN = (VNMEditTextClearable) v.findViewById(R.id.edDN);
		edDN.setOnTouchListener(this);
		edDN.setIsHandleDefault(false);
		edTN.setIsHandleDefault(false);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		btAdd = (Button) v.findViewById(R.id.btAddNote);
		btAdd.setOnClickListener(this);
		tbList = (DMSTableView) v.findViewById(R.id.tbProblem);
		tbList.setListener(this);
		// send request
		resetVar();
		getListProblem(true, true);
		
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		resetVar();
	}
	/**
	 * 
	 * Lấy danh sách loại vấn đề GSNPP cua GST
	 * 
	 * @author: YenNTH
	 * @param repareReset
	 * @param getCombobox
	 * @return: void
	 * @throws:
	 */
	private void getListProblem(boolean repareReset, boolean getCombobox) {
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
		if (!StringUtil.isNullOrEmpty(this.staffCodeForRequest)) {
			data.putString(IntentConstants.INTENT_STAFF_ID_PARA, staffCodeForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.statusForRequest)) {
			data.putString(IntentConstants.INTENT_STATE, statusForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.typeProblemForRequest)) {
			data.putString(IntentConstants.INTENT_TYPE_PROBLEM, typeProblemForRequest);
		}

		// staff id cua TBHV
		data.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		data.putBoolean(IntentConstants.INTENT_CHECK_COMBOBOX, getCombobox);

		ActionEvent e = new ActionEvent();
		if (repareReset) {
			e.tag = 11;
		}
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_FOLLOW_LIST_PROBLEM;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_TBHV_FOLLOW_LIST_PROBLEM:
			if (modelEvent.getActionEvent().tag == 11) {
				tbList.getPagingControl().totalPage = -1;
				tbList.getPagingControl().setCurrentPage(1);
			}
			dto = (TBHVFollowProblemDTO) modelEvent.getModelData();
			ComboboxFollowProblemDTO combboxDTO = (ComboboxFollowProblemDTO) dto.comboboxDTO;
			if ( combboxDTO != null) {
				comboboxDTO = combboxDTO;
				updateDataCombobox(comboboxDTO);
			}
			renderData();
			requestInsertLogKPI(HashMapKPI.GSBH_THEODOIKHACPHUCVANDE, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE: {
			if (followProblemDetail != null) {
				if (alertFollowProblemDetail != null) {
					alertFollowProblemDetail.dismiss();
				}
			}
			// update row
			TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) modelEvent.getActionEvent().viewData;
			updateRow(dto, true);
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	/**
	 * 
	 * Cap nhat lai mot row cua table
	 * 
	 * @author: YenNTH
	 * @param dtoFollow
	 * @param done
	 * @return: void
	 * @throws:
	 */
	private void updateRow(TBHVFollowProblemItemDTO dtoFollow, boolean done) {
		// TODO Auto-generated method stub
		int positionRow = 0;
		for (int i = 0, length = dto.list.size(); i < length; i++) {
			TBHVFollowProblemItemDTO tempDTO = dto.list.get(i);
			if (tempDTO.id == dtoFollow.id) {
				positionRow = i;
				break;
			}
		}
		List<DMSTableRow> listRow = tbList.getListChildRow();
		if (listRow != null && listRow.size() > 0) {
			TBHVFollowProblemRow followRow = (TBHVFollowProblemRow) listRow.get(positionRow);
			followRow.updateCheckBoxDone(done);
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_TBHV_FOLLOW_LIST_PROBLEM: {
			break;
		}
		case ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE: {
			parent.showDialog(StringUtil.getString(R.string.TEXT_UPDATE_STATUS_FAIL));
			break;
		}
		default:
			break;
		}
		super.handleErrorModelViewEvent(modelEvent);
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
//		if (tbList.getHeaderView().getHeaderCount() == 0) {
//			tbList.getHeaderView().addColumns(TableDefineContanst.TBHV_FOLLOW_PROBLEM_TABLE_WIDTH,TableDefineContanst.TBHV_FOLLOW_PROBLEM_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),ImageUtil.getColor(R.color.TABLE_HEADER_BG));
//			tbList.setNumItemsPage(Constants.NUM_ITEM_PER_PAGE);
//		}
		tbList.clearAllDataAndHeader();
		initHeaderTable(tbList, new TBHVFollowProblemRow(parent, this, true));
		// TODO Auto-generated method stub
		if (dto != null) {
			int position = (tbList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE + 1;
			if(dto.list.size()>0){
				for (TBHVFollowProblemItemDTO followProblemItemDTO: dto.list) {
					TBHVFollowProblemRow row = new TBHVFollowProblemRow(parent, this, false);
					row.render(followProblemItemDTO, position);
					position++;
					row.setListener(this);
					tbList.addRow(row);
				}
			}else{
				tbList.showNoContentRow();
			}
			
			if (tbList.getPagingControl().totalPage < 0)
				tbList.setTotalSize(dto.total, 1);
		}
	}

	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case CONFIRM_UPDATE_NEW_OK: {
			// send request problem is done.
			TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) data;
			// 3 thuc hien dong van de
			dto.status = TBHVFollowProblemItemDTO.STATUS_NEW; 
			dto.doneDate = Constants.STR_BLANK;
			updateCheckBox(dto, fromDetail);
			if (followProblemDetail != null) {
				if (alertFollowProblemDetail != null) {
					alertFollowProblemDetail.dismiss();
				}
			}
			getListProblem(true, true);
			break;
		}
		case CONFIRM_UPDATE_OK: {
			// send request problem is done.
			TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) data;
			dto.status = TBHVFollowProblemItemDTO.STATUS_APPROVE;
			dto.doneDate = Constants.STR_BLANK;
			updateCheckBox(dto, fromDetail);
			if (followProblemDetail != null) {
				if (alertFollowProblemDetail != null) {
					alertFollowProblemDetail.dismiss();
				}
			}
			getListProblem(true, true);
			break;
		}
		case CONFIRM_UPDATE_CANCEL: {
			TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) data;
			dto.status = TBHVFollowProblemItemDTO.STATUS_DONE; 
			updateRow(dto, false);
			break;
		}
		case CONFIRM_DELETE_OK: {
			// send request problem is deleted
			TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) data;
			dto.status = TBHVFollowProblemItemDTO.STATUS_DELETED;
			dto.updateDate = DateUtils.now();
			dto.updateUser = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().userCode);
			deleteTBHVProblem(dto, fromDetail);
			getListProblem(true, true);
			break;
		}
		case CONFIRM_DELETE_CANCEL: {
			break;
		}
		case ACTION_AGRRE_NEW_FOLLOW_PROBLEM_DETAIL: {
			TBHVFollowProblemItemDTO dtTemp = (TBHVFollowProblemItemDTO) data;
			dtTemp.doneDate = Constants.STR_BLANK;
			dtTemp.numReturn++;
			updateCheckBox(dtTemp, true);			
			if (followProblemDetail != null) {
				if (alertFollowProblemDetail != null) {
					alertFollowProblemDetail.dismiss();
				}
			}
			getListProblem(true, true);
			break;
		}
		case ACTION_CANCEL_NEW_FOLLOW_PROBLEM_DETAIL: {
			TBHVFollowProblemItemDTO dto = (TBHVFollowProblemItemDTO) data;
			dto.status = TBHVFollowProblemItemDTO.STATUS_DONE; 
			updateRow(dto, false);
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
			dtoGoToDetail = (TBHVFollowProblemItemDTO) data;
			showFollowProblemDetail(dtoGoToDetail);
			break;
		}
		case ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE: {
			confirmResolveUpdateProblem(data, false);
			break;
		}
		case ActionEventConstant.DELETE_TBHV_FOLLOW_PROBLEM_DONE: {
			confirmResolveDeleteProblem(data, false);
			break;
		}
		default:
			break;
		}

	}

	/**
	 * Hien thi dialog confirm resolve problem
	 * 
	 * @author: YenNTH
	 * @param data
	 * @return: void
	 * @throws:
	 */

	public void confirmResolveUpdateProblem(Object data, boolean isFromDetail) {
		fromDetail = isFromDetail;
		// show dialog
		GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this,parent,
						StringUtil.getString(R.string.TEXT_CONFIRM_UPDATE_PROBLEM_DONE),
						StringUtil.getString(R.string.TEXT_AGREE),
						CONFIRM_UPDATE_OK, StringUtil.getString(R.string.TEXT_CANCEL),
						CONFIRM_UPDATE_CANCEL, data, false, false);
	}

	/**
	 * confirm chuyen trang thai van de sang tao moi
	 * @author YenNTH
	 * @param data
	 * @param isFromDetail
	 */
	public void confirmResolveUpdateNewProblem(Object data, boolean isFromDetail) {
		fromDetail = isFromDetail;
		// show dialog
		GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this,parent,
						StringUtil.getString(R.string.TEXT_CONFIRM_UPDATE_NEW_PROBLEM_DONE),
						StringUtil.getString(R.string.TEXT_AGREE),
						CONFIRM_UPDATE_NEW_OK, StringUtil.getString(R.string.TEXT_CANCEL),
						CONFIRM_UPDATE_NEW_CANCEL, data, false, false);
	}

	/**
	 * confirm co xoa row hay kg?
	 * 
	 * @author YenNTH
	 * @param data
	 * @param isFromDetail
	 */
	public void confirmResolveDeleteProblem(Object data, boolean isFromDetail) {
		fromDetail = isFromDetail;
		// show dialog
		GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this,parent,
						StringUtil.getString(R.string.TEXT_CONFIRM_DELETE_PROBLEM_DONE),
						StringUtil.getString(R.string.TEXT_AGREE),
						CONFIRM_DELETE_OK, StringUtil.getString(R.string.TEXT_CANCEL),
						CONFIRM_DELETE_CANCEL, data, false, false);
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
		if (listGSNPPTTTT == null || listStatus == null || listTypeProblem == null) {
			staffCodeForRequest = Constants.STR_BLANK;
			statusForRequest = Constants.STR_BLANK;
			typeProblemForRequest = Constants.STR_BLANK;
		} else {
			if(listGSNPPTTTT.size()>0){
				int selectionPT = spinnerStaffCode.getSelectedItemPosition();
				if (selectionPT < 0){
					selectionPT = 0;
				}
				DisplayProgrameItemDTO dtoNVBH = listGSNPPTTTT.get(selectionPT);
				staffCodeForRequest = dtoNVBH.value;
			}else {
				staffCodeForRequest = Constants.STR_BLANK;
			}
			int selectionSP = spinnerStatus.getSelectedItemPosition();
			if (selectionSP < 0)
				selectionSP = 0;
			DisplayProgrameItemDTO dtoStatus = listStatus.get(selectionSP);
			statusForRequest = dtoStatus.value;
			if(listTypeProblem.size()>0 ){
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
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.ACTION_TBHV_GO_TO_ADD_REQUIREMENT;
		TBHVController.getInstance().handleSwitchFragment(e);
	}
	public void onClick(View v) {
		if (v == btSearch) {
			searchListProblem();
		} else if (v == btAdd) {
			addNote();
		}
		if (followProblemDetail != null && followProblemDetail.btCloseFollowProblemDetail == v) {
			if (alertFollowProblemDetail != null) {
				alertFollowProblemDetail.dismiss();
			}
		}
	}

	/**
	 * 
	 * Xu ly duyet van de
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @param fromDetail
	 * @return: void
	 * @throws:
	 */
	public void updateCheckBox(TBHVFollowProblemItemDTO dto, boolean isFromDetail) {
		parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		dto.updateDate = DateUtils.now();
		dto.updateUser = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().userCode);
		e.viewData = dto;
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * delete 1 row problem
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @param isFromDetail
	 */
	public void deleteTBHVProblem(TBHVFollowProblemItemDTO dto, boolean isFromDetail) {
		parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		e.viewData = dto;
		if (isFromDetail) {
			e.tag = 9;
		}
		e.sender = this;
		e.action = ActionEventConstant.DELETE_TBHV_FOLLOW_PROBLEM_DONE;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (v == edTN) {
			if (!edTN.onTouchEvent(arg1)) {
				currentCalender = DATE_FROM_CONTROL;
				parent.fragmentTag = TBHVFollowProblemView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edTN.getText().toString(), true);
			}
		}
		if (v == edDN) {
			if (!edDN.onTouchEvent(arg1)) {
				currentCalender = DATE_TO_CONTROL;
				parent.fragmentTag = TBHVFollowProblemView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, edDN.getText().toString(), true);
			}
		}
		return true;
	}

	/**
	 * 
	 * Duoc goi khi chon ngay picker date
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
			edTN.setText(new StringBuilder().append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
		}
		if (currentCalender == DATE_TO_CONTROL) {
			edDN.setTextColor(ImageUtil.getColor(R.color.BLACK));
			edDN.setText(new StringBuilder().append(sDay).append("/").append(sMonth).append("/").append(year).append(" "));
		}
	}

	/**
	 * 
	 * Cap nhat ds GSNPP/TTTT & trang thai
	 * 
	 * @author: YenNTH
	 * @param modelData
	 * @return: void
	 * @throws:
	 */
	private void updateDataCombobox(ComboboxFollowProblemDTO modelData) {
		// TODO Auto-generated method stub
		if (listGSNPPTTTT == null || listStatus == null ) {
			listGSNPPTTTT = new ArrayList<DisplayProgrameItemDTO>();
			listStatus = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = new DisplayProgrameItemDTO();
			itemDTO.name = StringUtil.getString(R.string.ALL);
			itemDTO.value = Constants.STR_BLANK;
			listGSNPPTTTT.add(itemDTO);
			listStatus.add(itemDTO);
			if (modelData.listNVBH != null) {
				for (int j = 0, jlength = modelData.listNVBH.size(); j < jlength; j++) {
					itemDTO = modelData.listNVBH.get(j);
					listGSNPPTTTT.add(itemDTO);
				}
			}

			// tao du lieu cho trang thai
			for (int j = 1; j <= 3; j++) {
				DisplayProgrameItemDTO itemStatusDTO = new DisplayProgrameItemDTO();
				if (j == TBHVFollowProblemItemDTO.STATUS_NEW) {
					itemStatusDTO.name = StringUtil.getString(R.string.TEXT_PROBLEM_CREATE_NEW);
				} else if (j == TBHVFollowProblemItemDTO.STATUS_DONE) {
					itemStatusDTO.name = StringUtil.getString(R.string.TEXT_PROBLEM_HAS_DONE);
				} else if (j == TBHVFollowProblemItemDTO.STATUS_APPROVE) {
					itemStatusDTO.name = StringUtil.getString(R.string.TEXT_PROBLEM_HAS_APPROVED);
				}
				itemStatusDTO.value = Constants.STR_BLANK + j;
				listStatus.add(itemStatusDTO);
			}
		}
		int lengthType = listGSNPPTTTT.size();
		int lengthDepart = listStatus.size();
		String nvbhName[] = new String[lengthType];
		String statusName[] = new String[lengthDepart];
		// khoi tao gia tri cho ma NVBH
		for (int i = 0; i < lengthType; i++) {
			DisplayProgrameItemDTO dto = listGSNPPTTTT.get(i);
			nvbhName[i] = dto.name;
		}
		// khoi tao gia tri cho trang thai
		for (int i = 0; i < lengthDepart; i++) {
			DisplayProgrameItemDTO dto = listStatus.get(i);
			statusName[i] = dto.name;
		}
		SpinnerAdapter adapterGSNPPTTTT = new SpinnerAdapter(parent, R.layout.simple_spinner_item, nvbhName);
		this.spinnerStaffCode.setAdapter(adapterGSNPPTTTT);
		spinnerStaffCode.setSelection(0);

		SpinnerAdapter adapterDepart = new SpinnerAdapter(parent, R.layout.simple_spinner_item, statusName);
		this.spinnerStatus.setAdapter(adapterDepart);
		spinnerStatus.setSelection(0);
	}
	/**
	 * 
	 * cap nhat dannh sach loai van de khi chon gsbh/tttt
	 * 
	 * @author: YenNTH
	 * @param modelData
	 * @param type
	 * @return: void
	 * @throws:
	 */
	private void updateTypeFollowProblem(ComboboxFollowProblemDTO modelData, String type) {
		// TODO Auto-generated method stub
		listTypeProblem = null;
		if (listTypeProblem == null) {
			listTypeProblem = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = new DisplayProgrameItemDTO();
			itemDTO.name = StringUtil.getString(R.string.ALL);
			itemDTO.value = Constants.STR_BLANK;
			listTypeProblem.add(itemDTO);
			if (modelData != null && modelData.listTypeProblem != null) {
				for (int j = 0, jlength = modelData.listTypeProblem.size(); j < jlength; j++) {
					itemDTO = modelData.listTypeProblem.get(j);
					if(type.equalsIgnoreCase(Constants.STR_BLANK)){
						listTypeProblem.add(itemDTO);
					}else if(type.equalsIgnoreCase(Constants.TYPE_GS)){
						if(itemDTO.type.equalsIgnoreCase(Constants.FEEDBACK_TYPE_GSNPP)){
							listTypeProblem.add(itemDTO);
						}
					}else {
						if(itemDTO.type.equalsIgnoreCase(Constants.FEEDBACK_TYPE_TTTT)){
							listTypeProblem.add(itemDTO);
						}
					}
				}
			}

		}
		int lengthTypeProblem = listTypeProblem.size();
		String typeProblemName[] = new String[lengthTypeProblem];
		// khoi tao gia tri cho loai van de
				for (int i = 0; i < lengthTypeProblem; i++) {
					DisplayProgrameItemDTO dto = listTypeProblem.get(i);
					typeProblemName[i] = dto.name;
				}
		SpinnerAdapter adapterDepart = new SpinnerAdapter(parent, R.layout.simple_spinner_item, typeProblemName);
		this.spinnerTypeProblem.setAdapter(adapterDepart);
		spinnerTypeProblem.setSelection(0);
	}
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spinnerStaffCode) {
			if (arg2 == 0) {
				updateTypeFollowProblem(comboboxDTO, Constants.STR_BLANK);
			}else{
				if(listGSNPPTTTT.get(arg2).type != Constants.STR_BLANK){
					updateTypeFollowProblem(comboboxDTO, listGSNPPTTTT.get(arg2).type.toString());
				}
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
	private void showFollowProblemDetail(TBHVFollowProblemItemDTO dto) {
		if (alertFollowProblemDetail == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			followProblemDetail = new TBHVFollowProblemDetailView(parent, this);
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
	 * 
	 * reset cac gia tri khi nhan cap nhat
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void resetVar() {
		// TODO Auto-generated method stub
		staffCodeForRequest = Constants.STR_BLANK;
		statusForRequest = Constants.STR_BLANK;
		fromDateForRequest = Constants.STR_BLANK;
		toDateForRequest = Constants.STR_BLANK;
		typeProblemForRequest = Constants.STR_BLANK;
		edDN.setText(Constants.STR_BLANK);
		edTN.setText(Constants.STR_BLANK);
		spinnerStaffCode.setSelection(0);
		spinnerStatus.setSelection(0);
		spinnerTypeProblem.setSelection(0);
		listGSNPPTTTT = null;
		listStatus = null;
		listTypeProblem = null;
	}
}
