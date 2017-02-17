/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.text.SimpleDateFormat;
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
import android.widget.EditText;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.ListStaffDTO.StaffItem;
import com.viettel.dms.dto.view.TBHVAddRequirementViewDTO;
import com.viettel.dms.dto.view.TBHVCustomerListDTO;
import com.viettel.dms.dto.view.TBHVFollowProblemItemDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * 
 *  MH them van de GST
 * 
 * @author: YenNTH
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVAddRequirementView extends BaseFragment implements OnEventControlListener, OnClickListener,
		OnTouchListener, OnItemSelectedListener {

	public static final String TAG = TBHVAddRequirementView.class.getName();
	private int currentCalender;// Luu trang thai chon ngay cua from or to
	Spinner spinnerStaffCode;// ma GSNPP
	Spinner spinnerStatus;// loai khac phuc
	VNMEditTextClearable edTN;// tu ngay
	VNMEditTextClearable edDN;// den ngay
	EditText edContent;// Noi dung yeu cau
	Button btSave;// button tim

	TBHVActivity parent;
	TBHVAddRequirementViewDTO dto;// Du lieu man hinh
	private boolean isFirstTime;// kiem tra lan dau tien vao man hinh
	private static final int DATE_TO_CONTROL = 2;
	private static final int ACTION_GET_CUSTOMER_LIST = 3;
	private static final int ACTION_CHOOSE_CUSTOMER = 4;
	public static final int CONFIRM_OK = 31;
	public static final int CONFIRM_CANCEL = 32;

	// combobox
	private List<DisplayProgrameItemDTO> listGSNPPTTTT = null;// ds gsnpp/tttt
	private List<DisplayProgrameItemDTO> listStatus = null;// ds trang thai

	// save du lieu cho viec tim kiem
	private String staffIdSupervisorForRequest = Constants.STR_BLANK;// staff id
	private String typeProblemForRequest = Constants.STR_BLANK;// trang thai
	private String customerIdForRequest = Constants.STR_BLANK;// tu ngay
	private String remindDateForRequest = Constants.STR_BLANK;// den ngay
	private String contentForRequest = Constants.STR_BLANK;// noi dung
	private String objectTypeRequest = Constants.STR_BLANK;// object type cua NV duoc chon

	// Search customer
	private String customerCodeSearch = Constants.STR_BLANK;// customer code for search
	private String customerNameSearch = Constants.STR_BLANK;// customer name for search
	boolean isGetTotalPage;// get total page or not

	TBHVCustomerListView followProblemDetail;// MH chi tiet van de
	TBHVCustomerListDTO customerListDto = new TBHVCustomerListDTO();// Ds Kh
																	// hien thi
	// dialog product detail view
	AlertDialog alertFollowProblemDetail;
	boolean isShowCustomerList;

	CustomerDTO customer;// Thong tin KH tu MH Huan luyen ngay TBHV
	StaffItem gsnppInfo;// Thong tin GSNPP
	boolean isFromTrainingView;
	int page;
	int oldSelectedIndex = 0;

	public static TBHVAddRequirementView newInstance(Bundle b) {
		TBHVAddRequirementView instance = new TBHVAddRequirementView();
		// Supply index input as an argument.
		instance.setArguments(b);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (TBHVActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_tbhv_add_requirement_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_TBHV_ADD_REQUIREMENT));

		spinnerStaffCode = (Spinner) v.findViewById(R.id.spinnerStaffCode);
		spinnerStaffCode.setOnItemSelectedListener(this);
		spinnerStatus = (Spinner) v.findViewById(R.id.spinnerStatus);
		edTN = (VNMEditTextClearable) v.findViewById(R.id.edTN);
		edTN.setOnTouchListener(this);
		edDN = (VNMEditTextClearable) v.findViewById(R.id.edDN);
		edDN.setOnTouchListener(this);
		edDN.setIsHandleDefault(false);
		edTN.setIsHandleDefault(false);
		btSave = (Button) v.findViewById(R.id.btSave);
		btSave.setOnClickListener(this);
		edContent = (EditText) v.findViewById(R.id.edContent);
		edDN.setText(DateUtils.getCurrentDate());
		Bundle data = (Bundle) getArguments();
		// send request
		if (!isFirstTime) {
			if (data != null) {
				if (data.getSerializable(IntentConstants.INTENT_CUSTOMER) != null) {
					customer = (CustomerDTO) data.getSerializable(IntentConstants.INTENT_CUSTOMER);
				}
				if (data.getSerializable(IntentConstants.INTENT_GSNPP) != null) {
					gsnppInfo = (StaffItem) data.getSerializable(IntentConstants.INTENT_GSNPP);
				}
			}

			// getListProblem(true, true);
			getInfoToCreateRequirement();
			isFirstTime = true;
		}

		// Layout text KH
		if (gsnppInfo != null) {
			isFromTrainingView = true;
			edTN.setImageClearVisibile(false);
			edTN.setEnabled(false);
			spinnerStaffCode.setEnabled(false);
			GlobalUtil.setEnableEditTextClear(edTN, false);
			edTN.setPadding(GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5), 0);
		} else {
		}

		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (isFirstTime) {
			renderData();
		}
	}

	/**
	 * 
	 * Lay ds van de theo GSNPP/TTTT cua GST
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void getInfoToCreateRequirement() {
		parent.showProgressDialog(getString(R.string.loading));
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		ActionEvent e = new ActionEvent();
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_ADD_REQUIREMENT_INFO;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_TBHV_ADD_REQUIREMENT_INFO:
			dto = (TBHVAddRequirementViewDTO) modelEvent.getModelData();
			if (dto != null) {
				updateDataCombobox(dto);
			}
			renderData();
			break;
		case ActionEventConstant.TBHV_ADD_PROBLEM: {
			parent.showDialog(StringUtil.getString(R.string.TEXT_ADD_REQUEST_SUCCESS));
			ActionEvent ev = new ActionEvent();
			ev.sender = this;
			Bundle bundle = new Bundle();
			bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
			ev.viewData = bundle;
			ev.action = ActionEventConstant.GO_TO_TBHV_FOLLOW_LIST_PROBLEM;
			TBHVController.getInstance().handleSwitchFragment(ev);
			break;
		}
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_POST_FEEDBACK: {			
			TBHVCustomerListDTO temp = (TBHVCustomerListDTO) modelEvent.getModelData();
			if (page == 1 && isGetTotalPage) {
				customerListDto.totalCustomer = temp.totalCustomer;
				followProblemDetail.setTotalSize(customerListDto.totalCustomer);
			}
			customerListDto.listCustomer.clear();
			customerListDto.listCustomer.addAll(temp.listCustomer);
			if (followProblemDetail != null && isShowCustomerList) {
				followProblemDetail.renderLayout(customerListDto, customer);
			}			
			break;
		}
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.GET_TBHV_FOLLOW_LIST_PROBLEM: {
			break;
		}
		case ActionEventConstant.TBHV_ADD_PROBLEM: {
			parent.showDialog(StringUtil.getString(R.string.TEXT_NOTIFY_ADD_PROBLEM));
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
	 * Layout cho man hinh
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void renderData() {
		if (isFromTrainingView) {
			if (listGSNPPTTTT != null) {
				for (int i = 0, s = listGSNPPTTTT.size(); i < s; i++) {
					if (listGSNPPTTTT.get(i).value.equals(gsnppInfo.id)) {
						oldSelectedIndex = i;
						spinnerStaffCode.setSelection(i);
						break;
					}
				}
			}
		}

		// Hien thi KH duoc chon tu MH huan luyen
		if (customer != null) {
			StringBuilder str = new StringBuilder();
			if (customer.customerCode.length() >= 3) {
				str.append(customer.customerCode.substring(0, 3));
			} else {
				str.append(customer.customerCode);
			}
			str.append(" - ");
			str.append(customer.customerName);

			edTN.setText(str.toString());
		}
	}

	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (control != null && control == followProblemDetail) {
			switch (eventType) {

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
					edTN.setText(customer.customerCode.substring(0, 3) + " - " + customer.customerName);
				}else{
					edTN.setText(customer.customerName);
				}
				alertFollowProblemDetail.dismiss();
				break;
			}
			default:
				break;
			}
		} else {
			switch (eventType) {
			case CONFIRM_OK: {
				// luu lai gia tri de thuc hien tim kiem
				String dateTimePattern = StringUtil.getString(R.string.TEXT_DATE_TIME_PATTERN);
				Pattern pattern = Pattern.compile(dateTimePattern);
				if (!StringUtil.isNullOrEmpty(this.edDN.getText().toString())) {
					String strDN = this.edDN.getText().toString().trim();

					Matcher matcher = pattern.matcher(strDN);
					if (!matcher.matches()) {
						parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
						return;
					} else {
						try {
							Date dn = StringUtil.stringToDate(strDN, Constants.STR_BLANK);
							String strFindDN = StringUtil.dateToString(dn, DateUtils.DATE_FORMAT_NOW);

							remindDateForRequest = strFindDN;
						} catch (Exception ex) {
							parent.showDialog(StringUtil.getString(R.string.TEXT_END_DATE_SEARCH_SYNTAX_ERROR));
							return;
						}
					}
				} else {
					remindDateForRequest = Constants.STR_BLANK;
				}

				staffIdSupervisorForRequest = Constants.STR_BLANK;
				typeProblemForRequest = Constants.STR_BLANK;
				objectTypeRequest = Constants.STR_BLANK;
				if (listStatus != null) {
					// type problem
					int selectionSP = spinnerStatus.getSelectedItemPosition();
					if (selectionSP > -1) {
						DisplayProgrameItemDTO dtoStatus = listStatus.get(selectionSP);
						typeProblemForRequest = dtoStatus.value;
					}
				}
				if (listGSNPPTTTT != null) {
					// Id GSNPP
					int selectionPT = spinnerStaffCode.getSelectedItemPosition();
					if (selectionPT < 0) {
						staffIdSupervisorForRequest = Constants.STR_BLANK;
						
					} else {
						DisplayProgrameItemDTO dtoNVBH = listGSNPPTTTT.get(selectionPT);
						staffIdSupervisorForRequest = dtoNVBH.value;
					}
				}
				// Content
				contentForRequest = edContent.getText().toString().trim();
				if (customer != null) {
					if (!StringUtil.isNullOrEmpty(this.edTN.getText().toString())) {
						customerIdForRequest = String.valueOf(customer.customerId);
					} else {
						customer = null;
						customerIdForRequest = Constants.STR_BLANK;
					}
				}

				saveRequirement();
				break;
			}
			case CONFIRM_CANCEL: {
				break;
			}
			default:
				break;
			}
		}
	}

	/**
	 * Hien thi dialog confirm resolve problem
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */

	public void onClick(View v) {
		if (v == btSave) {
			GlobalUtil.forceHideKeyboard(parent);

			String strDN = this.edDN.getText().toString().trim();

			if (StringUtil.isNullOrEmpty(strDN)) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_DAY));
			} else if (StringUtil.isNullOrEmpty(edContent.getText().toString().trim())) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_INPUT_CONTENT));
			} else if (StringUtil.isNullOrEmpty(edDN.getText().toString().trim())) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_DAY));
			} else {
				GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent,
						StringUtil.getString(R.string.TEXT_CONFIRM_CREATE_PROBLEM),
						StringUtil.getString(R.string.TEXT_AGREE), CONFIRM_OK,
						StringUtil.getString(R.string.TEXT_DENY), CONFIRM_CANCEL, null, false, false);
			}
		}else if(v == followProblemDetail.ivClose){
			alertFollowProblemDetail.dismiss();
		}
	}

	/**
	 * Luu van de
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void saveRequirement() {
		parent.showProgressDialog(getString(R.string.loading));
		FeedBackDTO feedBack = new FeedBackDTO();

		if (!StringUtil.isNullOrEmpty(this.customerIdForRequest)) {
			feedBack.customerId = customerIdForRequest;
		}

		if (!StringUtil.isNullOrEmpty(this.remindDateForRequest)) {
			feedBack.remindDate = remindDateForRequest;
		}

		if (!StringUtil.isNullOrEmpty(this.staffIdSupervisorForRequest)) {// id
																			// cua
																			// GSNPP
			feedBack.staffId = Long.parseLong(staffIdSupervisorForRequest);
		}
		if (!StringUtil.isNullOrEmpty(this.typeProblemForRequest)) {
			feedBack.type = Integer.parseInt(typeProblemForRequest);
		}

		// Ngay tao
		feedBack.createDate = DateUtils.now();

		// staff id cua TBHV
		feedBack.createUserId = GlobalInfo.getInstance().getProfile().getUserData().id;

		// Content
		feedBack.content = contentForRequest;

		// status
		feedBack.status = TBHVFollowProblemItemDTO.STATUS_NEW;

		ActionEvent e = new ActionEvent();
		e.viewData = feedBack;
		e.sender = this;
		e.action = ActionEventConstant.TBHV_ADD_PROBLEM;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * Xu ly duyet van de
	 * @author: YenNTH
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	public void updateCheckBox(TBHVFollowProblemItemDTO dto, boolean isFromDetail) {
		parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		e.viewData = dto;
		if (isFromDetail) {
			e.tag = 9;
		}
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_TBHV_FOLLOW_PROBLEM_DONE;
		TBHVController.getInstance().handleViewEvent(e);
	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		// TODO Auto-generated method stub
		if (v == edTN) {
			if (!edTN.onTouchEvent(arg1)) {
				if (!isFromTrainingView) {
					// Reset
					if (StringUtil.isNullOrEmpty(edTN.getText().toString().trim())) {
						customer = null;
					}

					isShowCustomerList = true;
					showFollowProblemDetail(customer);
					page = 1;
					customerCodeSearch = Constants.STR_BLANK;
					customerNameSearch = Constants.STR_BLANK;
					isGetTotalPage = true;
					getCustomerList(page, isGetTotalPage);
				}
			}
		}
		if (v == edDN) {
			if (!edDN.onTouchEvent(arg1)) {
				currentCalender = DATE_TO_CONTROL;
				parent.fragmentTag = TBHVAddRequirementView.TAG;
				parent.showDialog(GlobalBaseActivity.DATE_DIALOG_ID);
				parent.resetDatePickerDialog();
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

		boolean result = true;
		String remindDateString = new StringBuilder().append(sDay).append("/").append(sMonth).append("/").append(year)
				.toString();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
			Date createDate = formatter.parse(DateUtils.getCurrentDate());
			Date deliveryDate = formatter.parse(remindDateString);

			if (deliveryDate.compareTo(createDate) == -1) {
				result = false;
			}
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}

		if (result == true) {
			if (currentCalender == DATE_TO_CONTROL) {
				edDN.setTextColor(ImageUtil.getColor(R.color.BLACK));
				edDN.setText(remindDateString);
			}
		} else {
			parent.showDialog(StringUtil.getString(R.string.TEXT_LABLE_TBHV_INFO_REMIND_DATE_ERROR));
		}
	}

	/**
	 * 
	 * Cap nhat ds GSNPP/TTTT 
	 * 
	 * @author: YenNTH
	 * @param modelData
	 * @return: void
	 * @throws:
	 */
	private void updateDataCombobox(TBHVAddRequirementViewDTO modelData) {
		// TODO Auto-generated method stub
		if (listGSNPPTTTT == null) {
			listGSNPPTTTT = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = null;

			if (modelData.listNVBH != null) {
				for (int j = 0, jlength = modelData.listNVBH.size(); j < jlength; j++) {
					itemDTO = modelData.listNVBH.get(j);
					listGSNPPTTTT.add(itemDTO);
				}
			}
		}
		int lengthType = listGSNPPTTTT.size();
		String nvbhName[] = new String[lengthType];
		// khoi tao gia tri cho ma NVBH
		for (int i = 0; i < lengthType; i++) {
			DisplayProgrameItemDTO dto = listGSNPPTTTT.get(i);
			nvbhName[i] = dto.name;
		}
		SpinnerAdapter adapterNVBH = new SpinnerAdapter(parent, R.layout.simple_spinner_item, nvbhName);
		this.spinnerStaffCode.setAdapter(adapterNVBH);
		spinnerStaffCode.setSelection(0);
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
	private void updateTypeFollowProblem(TBHVAddRequirementViewDTO modelData, String type) {
		// TODO Auto-generated method stub
		listStatus = null;
		if (listStatus == null) {
			listStatus = new ArrayList<DisplayProgrameItemDTO>();
			DisplayProgrameItemDTO itemDTO = null;
			if (modelData.listTypeProblem != null) {
				for (int j = 0, jlength = modelData.listTypeProblem.size(); j < jlength; j++) {
					itemDTO = modelData.listTypeProblem.get(j);
					if(type.equalsIgnoreCase(Constants.TYPE_GS)){
						if(itemDTO.type.equalsIgnoreCase(Constants.FEEDBACK_TYPE_GSNPP)){
							listStatus.add(itemDTO);
						}
					}else {
						if(itemDTO.type.equalsIgnoreCase(Constants.FEEDBACK_TYPE_TTTT)){
							listStatus.add(itemDTO);
						}
					}
				}
			}

		}
		int lengthDepart = listStatus.size();
		String statusName[] = new String[lengthDepart];
		for (int i = 0; i < lengthDepart; i++) {
			DisplayProgrameItemDTO dto = listStatus.get(i);
			statusName[i] = dto.name;
		}
		SpinnerAdapter adapterDepart = new SpinnerAdapter(parent, R.layout.simple_spinner_item, statusName);
		this.spinnerStatus.setAdapter(adapterDepart);
		spinnerStatus.setSelection(0);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (arg0 == spinnerStaffCode) {
			if (oldSelectedIndex != arg2) {
				oldSelectedIndex = arg2;
				edTN.setText(Constants.STR_BLANK);
				customer = null;
			}
			if(listGSNPPTTTT.get(arg2).type != Constants.STR_BLANK){
				updateTypeFollowProblem(dto, listGSNPPTTTT.get(arg2).type.toString());
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
	private void showFollowProblemDetail(CustomerDTO dto) {
		if (alertFollowProblemDetail == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			followProblemDetail = new TBHVCustomerListView(parent, this, ACTION_GET_CUSTOMER_LIST,
					ACTION_CHOOSE_CUSTOMER);
			build.setView(followProblemDetail.viewLayout);
			alertFollowProblemDetail = build.create();
			alertFollowProblemDetail.setCanceledOnTouchOutside(false);
			Window window = alertFollowProblemDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
			followProblemDetail.ivClose.setOnClickListener(this);
		}
		alertFollowProblemDetail.show();
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				resetVar();
				getInfoToCreateRequirement();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * 
	 * reset cac gia tri
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void resetVar() {
		// TODO Auto-generated method stub
		staffIdSupervisorForRequest = Constants.STR_BLANK;
		typeProblemForRequest = Constants.STR_BLANK;
		customerIdForRequest = Constants.STR_BLANK;
		remindDateForRequest = Constants.STR_BLANK;
		edDN.setText(DateUtils.getCurrentDate());
		edContent.setText(Constants.STR_BLANK);
		oldSelectedIndex = 0;
		spinnerStatus.setSelection(0);
		objectTypeRequest = Constants.STR_BLANK;
		if (isFromTrainingView) {
			if (listGSNPPTTTT != null) {
				for (int i = 0, s = listGSNPPTTTT.size(); i < s; i++) {
					if (listGSNPPTTTT.get(i).value.equals(gsnppInfo.id)) {
						spinnerStaffCode.setSelection(i);
						break;
					}
				}
			}

			customerIdForRequest = String.valueOf(customer.customerId);
		} else {
			edTN.setText(Constants.STR_BLANK);
			customer = null;
			spinnerStaffCode.setSelection(0);
		}
	}

	/**
	 * 
	 * get danh sach khach hang
	 * 
	 * @author: YenNTH
	 * @param : page
	 * @param : isGetTotolPage
	 * @return: void
	 * @throws:
	 */
	private void getCustomerList(int page, boolean getTotalPage) {// (int page)
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putInt(IntentConstants.INTENT_PAGE, page);
		int selectionPT = spinnerStaffCode.getSelectedItemPosition();
		DisplayProgrameItemDTO dtoNVBH;
		if (selectionPT < 0) {
			staffIdSupervisorForRequest = Constants.STR_BLANK;
			objectTypeRequest = Constants.STR_BLANK;
		} else {
			dtoNVBH = listGSNPPTTTT.get(selectionPT);
			staffIdSupervisorForRequest = dtoNVBH.value;
			objectTypeRequest = dtoNVBH.type;
		}
		data.putString(IntentConstants.INTENT_STAFF_ID, staffIdSupervisorForRequest);
		data.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, getTotalPage);
		data.putString(IntentConstants.INTENT_CUSTOMER_CODE, customerCodeSearch);
		data.putString(IntentConstants.INTENT_CUSTOMER_NAME, customerNameSearch);
		data.putString(IntentConstants.INTENT_OBJECT_TYPE, objectTypeRequest);
		customerListDto.codeCus = customerCodeSearch;
		customerListDto.nameCus = customerNameSearch;
		e.viewData = data;
		e.action = ActionEventConstant.GET_CUSTOMER_LIST_FOR_POST_FEEDBACK;
		e.sender = this;
		TBHVController.getInstance().handleViewEvent(e);
	}
	

}
