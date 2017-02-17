/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.EditText;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.DisplayProgrameItemDTO;
import com.viettel.dms.dto.view.TBHVCustomerListDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.NoDefaultSpinner;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.customer.PostFeedbackView;
import com.viettel.dms.view.tbhv.TBHVCustomerListView;
import com.viettel.sabeco.R;

/**
 * MH theo doi khac phuc - tao van de (GSBH)
 * 
 * @author : YenNTH 
 */
public class GSNPPPostFeedbackView extends BaseFragment implements OnClickListener, OnEventControlListener,
		OnTouchListener,OnItemSelectedListener {

	public static final String TAG = GSNPPPostFeedbackView.class.getName();
	private GlobalBaseActivity parent;// parent
	Button btFeedBack;// btFeedBack
	VNMEditTextClearable etDate;// etDate
	NoDefaultSpinner spinnerType;// spinnerType
	NoDefaultSpinner spinnerStaff;// spinnerStaff
	EditText etContent;// etContent
	private int mDay;// mDay
	private int mMonth;// mMonth
	private int mYear;// mYear
	private String remindDateTime = Constants.STR_BLANK;// remindDateTime
	boolean firstSelect = true;// firstSelect
	SpinnerAdapter adapterLine;// adapterLine
	SpinnerAdapter adapterStaff;// adapterStaff

	private static final int ACTION_GET_CUSTOMER_LIST = 3;
	private static final int ACTION_CHOOSE_CUSTOMER = 4;
	// Search customer
	private String customerCodeSearch = Constants.STR_BLANK;// customer code for search
	private String customerNameSearch = Constants.STR_BLANK;// customer name for search
	boolean isGetTotalPage;// get total page or not
	CustomerDTO customer;// Thong tin KH tu MH Huan luyen ngay TBHV
	private List<DisplayProgrameItemDTO> listNVBH = null;
	int page;
	// dialog product detail view
	AlertDialog customerListPopup;
	TBHVCustomerListView followProblemDetail;// MH chi tiet van de
	TBHVCustomerListDTO customerListDto = new TBHVCustomerListDTO();// Ds Kh
																	// hien thi
	private VNMEditTextClearable etCusCode;
	private ArrayList<ApParamDTO> typeList;


	// dong y luu
	public static final int ACTION_AGRRE_SAVE = 1;
	// tu choi luu
	public static final int ACTION_CANCEL_SAVE = 2;
	
	public static GSNPPPostFeedbackView newInstance(Bundle b) {
		GSNPPPostFeedbackView f = new GSNPPPostFeedbackView();
		f.setArguments(b);
		return f;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) getActivity();
		listNVBH = (List<DisplayProgrameItemDTO>) getArguments().getSerializable(IntentConstants.INTENT_LIST_NVBH);

		super.onAttach(activity);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_gsnpp_post_feed_back, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_GSNPP_POST_FEEDBACK));

		// init
		btFeedBack = (Button) v.findViewById(R.id.btFeedBack);
		btFeedBack.setOnClickListener(this);
		etDate = (VNMEditTextClearable) v.findViewById(R.id.etDate);
		etDate.setText(DateUtils.getCurrentDate());
		etDate.setOnTouchListener(this);
		etDate.setIsHandleDefault(false);
		remindDateTime = DateUtils.convertDateOneFromFormatToAnotherFormat(etDate.getText().toString(),
				DateUtils.defaultDateFormat.toPattern(), DateUtils.defaultSqlDateFormat.toPattern()) + " 00:00:00";
		spinnerType = (NoDefaultSpinner) v.findViewById(R.id.spinnerType);
		spinnerStaff = (NoDefaultSpinner) v.findViewById(R.id.spinnerStaff);
		spinnerStaff.setOnItemSelectedListener(this);
		etContent = (EditText) v.findViewById(R.id.etContent);
		etCusCode = (VNMEditTextClearable) v.findViewById(R.id.etCusCode);
		etCusCode.setOnTouchListener(this);
		etCusCode.setIsHandleDefault(false);
		customer = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
		if (customer != null) {
			if(!StringUtil.isNullOrEmpty(customer.getCustomerCode())) {
				etCusCode.setText(customer.getCustomerCode().substring(0, 3) + " - " + customer.getCustomerName());
			}else{
				etCusCode.setText(customer.getCustomerName());
			}
		}
		String nvbhName[] = new String[listNVBH.size()+1];
		if(listNVBH.size() > 0) {
			nvbhName[0] = StringUtil.getString(R.string.ALL);
		}
		for (int i = 1; i <= listNVBH.size(); i++) {
			DisplayProgrameItemDTO dto = listNVBH.get(i-1);
			nvbhName[i] = dto.name;
		}
		adapterStaff = new SpinnerAdapter(parent, R.layout.simple_spinner_item, nvbhName);
		spinnerStaff.setAdapter(adapterStaff);
		spinnerStaff.setSelection(0);
		getTypeFeedback();

		return v;
	}

	/**
	 * lay danh sach van de- MH tao moi van de cua GSBH
	 * 
	 * @author: YenNTH
	 * @return: voidvoid
	 * @throws:
	 */
	private void getTypeFeedback() {
		Bundle b = new Bundle();
		b.putInt(IntentConstants.INTENT_FROM, PostFeedbackView.FROM_GSNPP_POST_FEEDBACK_VIEW);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_TYPE_FEEDBACK;
		e.viewData = b;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
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
			followProblemDetail = new TBHVCustomerListView(parent, this, ACTION_GET_CUSTOMER_LIST,
					ACTION_CHOOSE_CUSTOMER);
			build.setView(followProblemDetail.viewLayout);
			customerListPopup = build.create();
			customerListPopup.setCanceledOnTouchOutside(false);
			Window window = customerListPopup.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
			followProblemDetail.ivClose.setOnClickListener(this);
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
		if(listNVBH.size() > 0){
			if(spinnerStaff.getSelectedItemPosition() > 0) {
				data.putString(IntentConstants.INTENT_STAFF_ID, listNVBH.get(spinnerStaff.getSelectedItemPosition() - 1).value);
			} else {
				data.putString(IntentConstants.INTENT_STAFF_ID, Constants.STR_BLANK);
			}
		}else{
			data.putString(IntentConstants.INTENT_STAFF_ID, Constants.STR_BLANK);
		}
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
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

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_CUSTOMER_LIST_FOR_POST_FEEDBACK: {
			TBHVCustomerListDTO temp = (TBHVCustomerListDTO) modelEvent.getModelData();
			if (page == 1 && isGetTotalPage) {
				customerListDto.totalCustomer = temp.totalCustomer;
				followProblemDetail.setTotalSize(customerListDto.totalCustomer);
			}
			customerListDto.listCustomer.clear();
			customerListDto.listCustomer.addAll(temp.listCustomer);
			if (followProblemDetail != null) {
				followProblemDetail.renderLayout(customerListDto, customer);
			}
			break;
		}
		case ActionEventConstant.GET_TYPE_FEEDBACK: {
			typeList = (ArrayList<ApParamDTO>) modelEvent.getModelData();
			String[] arrLineChoose = new String[typeList.size()];
			for (int i = 0; i < typeList.size(); i++) {
				arrLineChoose[i] = typeList.get(i).apParamName;
			}
			adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrLineChoose);
			spinnerType.setAdapter(adapterLine);
			spinnerType.setSelection(0);

			break;
		}
		case ActionEventConstant.POST_FEEDBACK:
		case ActionEventConstant.POST_LIST_FEEDBACK:
			btFeedBack.setEnabled(true);
			parent.showDialog(StringUtil.getString(R.string.TEXT_ADD_REQUEST_SUCCESS));
			ActionEvent ev = new ActionEvent();
			ev.sender = this;
			Bundle bundle = new Bundle();
			bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
			ev.viewData = bundle;
			ev.action = ActionEventConstant.GO_TO_FOLLOW_LIST_PROBLEM;
			SuperviorController.getInstance().handleSwitchFragment(ev);
			break;
		default:
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		btFeedBack.setEnabled(true);
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * updateDisplay
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void updateDate(int day, int month, int year) {
		this.mDay = day;
		this.mMonth = month;
		this.mYear = year;

		String sDay = String.valueOf(mDay);
		String sMonth = String.valueOf(mMonth + 1);
		if (mDay < 10) {
			sDay = "0" + sDay;
		}
		if (mMonth + 1 < 10) {
			sMonth = "0" + sMonth;
		}

		etDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
		etDate.setText(new StringBuilder()
		// Month is 0 based so add 1
				.append(sDay).append("/").append(sMonth).append("/").append(mYear).append(" "));
		remindDateTime = mYear + "-" + sMonth + "-" + sDay + " " + "00:00:00";

	}

	/**
	 * luu thong them van de cho NVBH cua GSBH
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	private void postFeedBack() {
		parent.showProgressDialog(getString(R.string.loading), false);
		btFeedBack.setEnabled(false);
		if(spinnerStaff.getSelectedItemPosition() > 0) {
			FeedBackDTO itemdto = new FeedBackDTO();
			itemdto.remindDate = remindDateTime;
			itemdto.content = etContent.getText().toString().trim();
			itemdto.createDate = DateUtils.now();
			itemdto.staffId = Integer.parseInt(((DisplayProgrameItemDTO) listNVBH.get(spinnerStaff
					.getSelectedItemPosition() - 1)).value);

			if (customer != null) {
				itemdto.customerId = customer.getCustomerId();
			}
			itemdto.status = FeedBackDTO.FEEDBACK_STATUS_CREATE;
			itemdto.type = Integer.parseInt(typeList.get(spinnerType.getSelectedItemPosition()).apParamCode);
			itemdto.isDeleted = 0;
			itemdto.createUserId = GlobalInfo.getInstance().getProfile().getUserData().id;
			itemdto.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;

			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "05-01-01");
			bundle.putSerializable(IntentConstants.INTENT_FEEDBACK_DTO, itemdto);

			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.action = ActionEventConstant.POST_FEEDBACK;
			e.viewData = bundle;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
		} else {
			ArrayList<FeedBackDTO> feedBackDTOs = new ArrayList<FeedBackDTO>();
			for (DisplayProgrameItemDTO displayProgrameItemDTO:
				 listNVBH) {
				FeedBackDTO itemdto = new FeedBackDTO();
				itemdto.remindDate = remindDateTime;
				itemdto.content = etContent.getText().toString().trim();
				itemdto.createDate = DateUtils.now();
				itemdto.staffId = Integer.parseInt(displayProgrameItemDTO.value);
				if (customer != null) {
					itemdto.customerId = customer.getCustomerId();
				}
				itemdto.status = FeedBackDTO.FEEDBACK_STATUS_CREATE;
				itemdto.type = Integer.parseInt(typeList.get(spinnerType.getSelectedItemPosition()).apParamCode);
				itemdto.isDeleted = 0;
				itemdto.createUserId = GlobalInfo.getInstance().getProfile().getUserData().id;
				itemdto.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;
				feedBackDTOs.add(itemdto);
			}

			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, "05-01-01");
			bundle.putParcelableArrayList(IntentConstants.INTENT_FEEDBACK_DTO, feedBackDTOs);

			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.action = ActionEventConstant.POST_LIST_FEEDBACK;
			e.viewData = bundle;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
		}
		
	}

	@Override
	public void onClick(View v) {
		if(v == btFeedBack){
			GlobalUtil.forceHideKeyboard(parent);
			if (StringUtil.isNullOrEmpty(etContent.getText().toString().trim())) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_INPUT_CONTENT));
			}else if(DateUtils.compareWithNow(remindDateTime, DateUtils.DATE_FORMAT_SQL_DEFAULT) == -1){
				parent.showDialog(StringUtil.getString(R.string.TEXT_REMIND_DATE_NOTICE));
			}else if(StringUtil.isNullOrEmpty(etDate.getText().toString().trim())){
				parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_DAY));
			}else {
				if (spinnerType.getSelectedItemPosition() == -1) {
					parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_FEEDBACK_TYPE));
				} else if(listNVBH.size() <= 0){
					parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_FEEDBACK_STAFF));
				}else {
					String mess = Constants.STR_BLANK;
					mess = StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_SAVE);
					// hien thi dialog confirm
					GlobalUtil.showDialogConfirm(this, this.parent, mess, StringUtil.getString(R.string.TEXT_AGREE),
							ACTION_AGRRE_SAVE, StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_SAVE, null);
					
				}

			}
		}else if(v == followProblemDetail.ivClose){
			customerListPopup.dismiss();
		}
	
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
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
				etCusCode.setText(customer.customerCode.substring(0, 3) + " - " + customer.customerName);
			}else{
				etCusCode.setText(customer.customerName);
			}
			customerListPopup.dismiss();
			break;
		}
		case ACTION_AGRRE_SAVE: {
			postFeedBack();
			break;
		}
		case ACTION_CANCEL_SAVE: {
			break;
		}
		default:
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		if (v == etDate) {
			if (!etDate.onTouchEvent(arg1)) {
				parent.fragmentTag = GSNPPPostFeedbackView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etDate.getText().toString(), true);
			}
		}
		if (v == etCusCode) {
			if (!etCusCode.onTouchEvent(arg1)) {
				showCustomerList();
				page = 1;
				customerCodeSearch = Constants.STR_BLANK;
				customerNameSearch = Constants.STR_BLANK;
				isGetTotalPage = true;
				getCustomerList(page, isGetTotalPage);
			}
		}
		return false;
	}

	/**
	 * 
	 * reset value khi nhan cap nhat
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void resetValue(){
		spinnerStaff.setSelection(0);
		etCusCode.setText(Constants.STR_BLANK);
		spinnerType.setSelection(0);
		etDate.setText(DateUtils.getCurrentDate());
		remindDateTime = DateUtils.convertDateOneFromFormatToAnotherFormat(etDate.getText().toString(),
				DateUtils.defaultDateFormat.toPattern(), DateUtils.defaultSqlDateFormat.toPattern()) + " 00:00:00";
		etContent.setText(Constants.STR_BLANK);
		
	}
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				resetValue();
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if (arg0 == spinnerStaff) {
			etCusCode.setText(Constants.STR_BLANK);
			customer = null;
		}
		
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}

}
