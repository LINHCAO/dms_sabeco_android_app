/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

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
import android.widget.Button;
import android.widget.EditText;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.dto.db.ApParamDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.me.UserDTO;
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
import com.viettel.dms.view.tbhv.TBHVCustomerListView;
import com.viettel.sabeco.R;

import java.util.ArrayList;

/**
 * Tao gop y Su dung MH them ghi chu( MH theo doi khac phuc) Va MH them gop y(
 * MH danh sach khach hang)
 * 
 * @author : TamPQ since : 9:48:52 AM version :
 */
public class PostFeedbackView extends BaseFragment implements OnClickListener,
		OnEventControlListener, OnTouchListener {
	private GlobalBaseActivity parent;// parent
	Button btFeedBack;// btFeedBack
	VNMEditTextClearable etDate;// etDate
	VNMEditTextClearable etCusCode;// etDate
	NoDefaultSpinner spinnerType;// spinnerType
	EditText etContent;// etContent
	private int mDay;// mDay
	private int mMonth;// mMonth
	private int mYear;// mYear
	private String remindDateTime = Constants.STR_BLANK;// remindDateTime
	boolean firstSelect = true;// firstSelect
	SpinnerAdapter adapterLine;// adapterLine

	public static final int FROM_NOTE_LIST_VIEW = 0;
	public static final int FROM_FEEDBACK_LIST_VIEW = 1;
	public static final int FROM_GSNPP_POST_FEEDBACK_VIEW = 2;
	public static final int FROM_TTTT_POST_FEEDBACK_VIEW = 3;

	private static final int ACTION_GET_CUSTOMER_LIST = 4;
	private static final int ACTION_CHOOSE_CUSTOMER = 5;

	private static final int ACTION_SAVE_FEEDBACK = 6;
	private static final int ACTION_CANCEL_FEEDBACK = 7;
	private static final int ACTION_POST_FEEDBACK_SUCCESS = 8;
	// Search customer
	private String customerCodeSearch = Constants.STR_BLANK;// customer code for
															// search
	private String customerNameSearch = Constants.STR_BLANK;// customer name for
															// search
	boolean isGetTotalPage;// get total page or not
	CustomerDTO customer;// Thong tin KH tu MH Huan luyen ngay TBHV
	private int from;// tu man hinh NoteListView hay FeedbackListView qua
	int page;
	// dialog product detail view
	AlertDialog customerListPopup;
	TBHVCustomerListView followProblemDetail;// MH chi tiet van de
	TBHVCustomerListDTO customerListDto = new TBHVCustomerListDTO();// Ds Kh
																	// hien thi
	private ArrayList<ApParamDTO> typeList;

	public static final String TAG = PostFeedbackView.class.getName();
	private static final int TAB_INDEX = 1;

	public static PostFeedbackView newInstance(Bundle b) {
		PostFeedbackView f = new PostFeedbackView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) getActivity();
		customer = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
		from = getArguments().getInt(IntentConstants.INTENT_FROM);

		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_post_feed_back, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState); 
		
		setTitleHeaderView(getFullTitleString());

		// init
		btFeedBack = (Button) v.findViewById(R.id.btFeedBack);
		btFeedBack.setOnClickListener(this);
		etDate = (VNMEditTextClearable) v.findViewById(R.id.etDate);
		etDate.setOnTouchListener(this);
		etDate.setIsHandleDefault(false);
		etDate.setText(DateUtils.getCurrentDate());
		remindDateTime = DateUtils.now();
		spinnerType = (NoDefaultSpinner) v.findViewById(R.id.spinnerType);
		etContent = (EditText) v.findViewById(R.id.etContent);
		etCusCode = (VNMEditTextClearable) v.findViewById(R.id.etCusCode);
		setValueCustomer();
		customer = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
		if (customer != null) {
			if(!StringUtil.isNullOrEmpty(customer.getCustomerCode())) {
				etCusCode.setText(customer.getCustomerCode().substring(0, 3)
						+ " - " + customer.getCustomerName());
			}else{
				etCusCode.setText(customer.getCustomerName());
			}
		}
		if (typeList == null) {
			getTypeFeedback();
		}

		return v;
	}

	/**
	 * getTypeFeedback
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void getTypeFeedback() {
		Bundle b = new Bundle();
		b.putInt(IntentConstants.INTENT_FROM, from);
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
	 * @author: Nguyen Huu Hieu
	 * @return: void
	 * @throws:
	 */
	private void showCustomerList() {
		if (customerListPopup == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			followProblemDetail = new TBHVCustomerListView(parent, this, ACTION_GET_CUSTOMER_LIST, ACTION_CHOOSE_CUSTOMER);
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
	 * get danh sach khach hang
	 * 
	 * @author: HieuNH
	 * @param : page
	 * @param : isGetTotolPage
	 * @return: void
	 * @throws:
	 */
	private void getCustomerList(int page, boolean getTotalPage) {// (int page)
																  // {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();

		data.putInt(IntentConstants.INTENT_PAGE, page);
		data.putString(IntentConstants.INTENT_STAFF_ID, Constants.STR_BLANK
				+ GlobalInfo.getInstance().getProfile().getUserData().id);
		data.putString(IntentConstants.INTENT_SHOP_ID, Constants.STR_BLANK
				+ GlobalInfo.getInstance().getProfile().getUserData().shopId);
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
			int i = 0;
			for (ApParamDTO dto : typeList) {
				arrLineChoose[i] = dto.apParamName;
				i++;
			}
			adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrLineChoose);
			spinnerType.setAdapter(adapterLine);
			spinnerType.setSelection(0);
			break;
		}
		case ActionEventConstant.POST_FEEDBACK:
			btFeedBack.setEnabled(true);
			GlobalUtil.showDialogConfirm(this, parent, StringUtil.getString(R.string.TEXT_ADD_REQUEST_SUCCESS), 
					StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_POST_FEEDBACK_SUCCESS, null, false);
			break;
		default:
			break;
		}
		parent.closeProgressDialog();
	}

	/**
	 * Tra ve prefix cua parent parent
	 * @author: QuangVT
	 * @since: 3:51:08 PM Feb 7, 2014
	 * @return: String
	 * @throws:  
	 * @return
	 */
	private String getParentParentPrefix() { 
		String prefixParent = getParentPrefixTitle();
		prefixParent = (StringUtil.isNullOrEmpty(prefixParent) ? Constants.STR_BLANK : prefixParent);
		final int index = prefixParent.lastIndexOf("-");
		
		String prefixParentParent = Constants.STR_BLANK;
		if(!StringUtil.isNullOrEmpty(prefixParent) && index >= 0 && index < prefixParent.length()){
			prefixParentParent = prefixParent.substring(0, index);
		}
		
		return prefixParentParent;
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		btFeedBack.setEnabled(true);
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * updateDisplay
	 * 
	 * @author: TamPQ
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

		remindDateTime = mYear + "-" + sMonth + "-" + sDay + " " + "00:00:01";
	}

	/**
	 * postFeedBack
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void postFeedBack() {
		if (StringUtil.isNullOrEmpty(etContent.getText().toString().trim())) {
			parent.showDialog(StringUtil.getString(R.string.TEXT_INPUT_CONTENT));
		} else {
			if (spinnerType.getSelectedItemPosition() == -1) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_FEEDBACK_TYPE));
			} else if (DateUtils.compareDate(remindDateTime, DateUtils.now()) == -1) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_BEFORE_NOW));
			} else if (StringUtil.isNullOrEmpty(etDate.getText().toString().trim())) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_DAY));
			}
			else {
				if (from == PostFeedbackView.FROM_TTTT_POST_FEEDBACK_VIEW
						|| from == PostFeedbackView.FROM_NOTE_LIST_VIEW) {
					GlobalUtil.showDialogConfirm(this, this.parent, StringUtil.getString(R.string.TEXT_NOTIFY_ACTION_SAVE), 
							StringUtil.getString(R.string.TEXT_AGREE), ACTION_SAVE_FEEDBACK, 
							StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_FEEDBACK, null);
				} else {
					GlobalUtil.showDialogConfirm(this, this.parent, StringUtil.getString(R.string.POST_FEEDBACK), 
							StringUtil.getString(R.string.TEXT_AGREE), ACTION_SAVE_FEEDBACK, 
							StringUtil.getString(R.string.TEXT_DENY), ACTION_CANCEL_FEEDBACK, null);
				}
			}

		}
	}

	@Override
	public void onClick(View v) {
		if (v == btFeedBack) {
			GlobalUtil.forceHideKeyboard(parent);
			postFeedBack();
		} else if (v == followProblemDetail.ivClose) {
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
				etCusCode.setText(customer.customerCode.substring(0, 3) + " - "
					+ customer.customerName);
			}else{
				etCusCode.setText(customer.customerName);
			}
			customerListPopup.dismiss();
			break;
		}
		case ACTION_SAVE_FEEDBACK:
			parent.showProgressDialog(getString(R.string.loading), false);
			btFeedBack.setEnabled(false);
			FeedBackDTO itemdto = new FeedBackDTO();
			itemdto.remindDate = remindDateTime;
			itemdto.content = etContent.getText().toString().trim();
			itemdto.createDate = DateUtils.now();
			itemdto.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
			if (!StringUtil.isNullOrEmpty(etCusCode.getText().toString().trim())) {
				itemdto.customerId = customer.getCustomerId();
			}
			itemdto.status = FeedBackDTO.FEEDBACK_STATUS_CREATE;
			itemdto.type = Integer.parseInt(typeList.get(spinnerType.getSelectedItemPosition()).apParamCode);
			itemdto.isDeleted = 0;
			itemdto.createUserId = GlobalInfo.getInstance().getProfile().getUserData().id;
			itemdto.shopId = GlobalInfo.getInstance().getProfile().getUserData().shopId;

			Bundle bundle = new Bundle(); 
			bundle.putSerializable(IntentConstants.INTENT_FEEDBACK_DTO, itemdto);

			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.action = ActionEventConstant.POST_FEEDBACK;
			e.viewData = bundle;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
			break;
		case ACTION_POST_FEEDBACK_SUCCESS: {
			
			if (from == FROM_TTTT_POST_FEEDBACK_VIEW) {
				ActionEvent ev = new ActionEvent();
				ev.sender = this;
				Bundle b = new Bundle();
				b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentParentPrefix());
				b.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
				ev.viewData = b;
				ev.action = ActionEventConstant.GO_TO_TTTT_FOLLOW_LIST_PROBLEM;
				TNPGController.getInstance().handleSwitchFragment(ev);
			} else if (from == FROM_NOTE_LIST_VIEW) {
				ActionEvent ev = new ActionEvent();
				ev.sender = this;
				Bundle b = new Bundle();
				b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentParentPrefix());
				b.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
				ev.viewData = b;
				ev.action = ActionEventConstant.GO_TO_NVBH_NEED_DONE_VIEW;
				SaleController.getInstance().handleSwitchFragment(ev);
			} else {
				ActionEvent ev = new ActionEvent();
				ev.sender = this;
				Bundle b = new Bundle();
				b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentParentPrefix());
				b.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
				ev.viewData = b;
				ev.action = ActionEventConstant.GET_LIST_CUS_FEED_BACK;
				SaleController.getInstance().handleSwitchFragment(ev);
			}
		}
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent arg1) {
		if (v == etDate) {
			if (!etDate.onTouchEvent(arg1)) {
				parent.fragmentTag = PostFeedbackView.TAG;
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
	 * set du lieu cho edittext khach hang su dung MH them gop y va MH them ghi
	 * chu
	 * 
	 * @author: YenNTH
	 * @return: void
	 * @throws:
	 */
	public void setValueCustomer() {
		if (from == FROM_NOTE_LIST_VIEW || from == FROM_TTTT_POST_FEEDBACK_VIEW) {
			etCusCode.setOnTouchListener(this);
			etCusCode.setIsHandleDefault(false);
		} else {
			etCusCode.setImageClearVisibile(false);
			etCusCode.setEnabled(false);
			GlobalUtil.setEnableEditTextClear(etCusCode, false);
			etCusCode.setPadding(GlobalUtil.dip2Pixel(5), 0, GlobalUtil.dip2Pixel(5), 0);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				etDate.setText(DateUtils.getCurrentDate());
				setValueCustomer();
				etContent.setText(Constants.STR_BLANK);
				etCusCode.setText(Constants.STR_BLANK);
				if (spinnerType != null && typeList.size() > 0
						&& adapterLine != null) {
					spinnerType.setAdapter(adapterLine);
					spinnerType.setSelection(0);
				}
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	@Override
	protected int getTabIndex() {
		return TAB_INDEX;
	}

	@Override
	protected String getTitleString() {
		String title = Constants.STR_BLANK;
		int role=GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		if(role== UserDTO.TYPE_PRESALES ||role == UserDTO.TYPE_VALSALES){
			if (from == PostFeedbackView.FROM_NOTE_LIST_VIEW) {
				title = StringUtil.getString(R.string.TITLE_VIEW_POST_FEEDBACK_DEFAULT);
			} else {
				title = StringUtil.getString(R.string.TITLE_POST_FEEDBACK_EX);
			}
		}else if(role==UserDTO.TYPE_TNPG) {
			if(from== PostFeedbackView.FROM_TTTT_POST_FEEDBACK_VIEW){
				title = StringUtil.getString(R.string.TITLE_VIEW_POST_FEEDBACK_DEFAULT);
			}else {
				title = StringUtil.getString(R.string.TITLE_POST_FEEDBACK);
			}
		}
		return title;
	}
}
