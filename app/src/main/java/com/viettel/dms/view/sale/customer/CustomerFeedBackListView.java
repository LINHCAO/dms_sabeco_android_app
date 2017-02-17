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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.controller.TNPGController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.me.UserDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.GSNPPCustomerFeedBackDetailView;
import com.viettel.dms.view.supervisor.GSNPPCustomerFeedBackRow;
import com.viettel.dms.view.supervisor.training.TrainingListView;
import com.viettel.dms.view.tnpg.customer.CustomerListViewPG;
import com.viettel.sabeco.R;

import java.util.Vector;

/**
 * Danh sach gop y
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerFeedBackListView extends BaseFragment implements VinamilkTableListener, OnEventControlListener {

	public static final String TAG = CustomerFeedBackListView.class.getName();
	Button btFeedBack;// btFeedBack
	TextView tvCusCode;// tvCusCode
	TextView tvCusName;// tvCusName
	DMSTableView tbCusFeedBack;// tbCusFeedBack
	private GlobalBaseActivity parent;
	private CustomerDTO customer;
	private CustomerFeedBackDto dto = new CustomerFeedBackDto();
	AlertDialog alertFollowProblemDetail;	// dialog product detail view
	GSNPPCustomerFeedBackDetailView feedBackDetailView;// feedback dto
	// action menu
	private static final int MENU_LIST_FEEDBACKS = 11;
	private static final int MENU_MAP = 12;
	private static final int MENU_IMAGE = 13;
	private static final int MENU_INFO_DETAIL = 14;
	// action xu ly van de
	public static final int ACTION_OK = 0;
	public static final int ACTION_CANCEL = 1;
	public static final int ACTION_GET_CUSTOMER_LIST = 3;
	public static final int ACTION_CHOOSE_CUSTOMER = 4;
	private static final int ACTION_SAVE_FEEDBACK = 6;
	private static final int ACTION_CANCEL_FEEDBACK = 7;
	private static final int TAB_INDEX = 2;
	private boolean isGetTotal=false;
	private int role;
	private String sender;


	public static CustomerFeedBackListView newInstance(Bundle b) {
		CustomerFeedBackListView f = new CustomerFeedBackListView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) getActivity();
		customer = (CustomerDTO) getArguments().getSerializable(IntentConstants.INTENT_CUSTOMER);
		if(!StringUtil.isNullOrEmpty(getArguments().getString(IntentConstants.INTENT_SENDER))){
			sender = getArguments().getString(IntentConstants.INTENT_SENDER);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_customer_feed_back_list_fragment, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState); 
	    role=GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType; 
		setTitleHeaderView(getFullTitleString());

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_POSITION), R.drawable.icon_map, MENU_MAP);
		addMenuItem(StringUtil.getString(R.string.TEXT_PHOTOS), R.drawable.menu_picture_icon, MENU_IMAGE);
		addMenuItem(StringUtil.getString(R.string.TEXT_FEEDBACK), R.drawable.icon_reminders, MENU_LIST_FEEDBACKS);
		addMenuItem(StringUtil.getString(R.string.TEXT_INFO), R.drawable.icon_detail, MENU_INFO_DETAIL, View.INVISIBLE);
		setMenuItemFocus(3);

		btFeedBack = (Button) v.findViewById(R.id.btFeedBack);
		btFeedBack.setOnClickListener(this);
		
		tvCusCode = (TextView) v.findViewById(R.id.tvCusCode);
		tvCusName = (TextView) v.findViewById(R.id.tvCusName);
		tbCusFeedBack = (DMSTableView) v.findViewById(R.id.tbCusFeedBack);
		tbCusFeedBack.setListener(this);
		
		if(role == UserDTO.TYPE_GSNPP){
//			btFeedBack.setVisibility(View.INVISIBLE);
			initHeaderTable(tbCusFeedBack, new GSNPPCustomerFeedBackRow(parent, this));
		}else {
//			btFeedBack.setVisibility(View.VISIBLE);
			initHeaderTable(tbCusFeedBack, new CustomerFeedBackRow(parent, this));
		}
		if (customer != null) {
			if(!StringUtil.isNullOrEmpty(customer.customerCode)) {
				tvCusCode.setText(customer.customerCode.substring(0, 3));
			}else{
				tvCusCode.setText("");
			}
			tvCusName.setText(customer.customerName);
		}
		if(dto.arrItem.size()==0|| dto.arrItem==null){
			
				getCustomerFeedBack(1, true);
		}else {
			renderLayout();
		}

		return v;
	}

	/**
	 * lay danh sách các góp ý của khách hàng.
	 * @author: dungdq3
	 * @return: void
	 */
	private void getCustomerFeedBack(int page, boolean isGetTotalPage) {
		isGetTotal=isGetTotalPage;
		parent.showProgressDialog(getString(R.string.loading));
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_STATE, "");// tat ca
		bundle.putString(IntentConstants.INTENT_TYPE, "");
		bundle.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		bundle.putString(IntentConstants.INTENT_CUSTOMER_ID, customer.getCustomerId());
		bundle.putString(IntentConstants.INTENT_DONE_DATE, "");
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		bundle.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		if(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType== UserDTO.TYPE_TNPG){
			bundle.putInt(IntentConstants.INTENT_FROM,PostFeedbackView.FROM_TTTT_POST_FEEDBACK_VIEW);
		}
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_LIST_CUS_FEED_BACK;
		e.sender = this;
		e.viewData = bundle;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_CUS_FEED_BACK:
			CustomerFeedBackDto temDto = (CustomerFeedBackDto) (modelEvent.getModelData());
			dto.arrItem = temDto.arrItem;
			if (dto.currentPage <= 0) {
				if(isGetTotal){
					dto.totalFeedBack = temDto.totalFeedBack;
					dto.typeList = temDto.typeList;
				}
			}
			renderLayout();
			if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_VALSALES
					|| GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_PRESALES) {
				requestInsertLogKPI(HashMapKPI.NVBH_GOPY, modelEvent.getActionEvent().startTimeFromBoot);
			} else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_GSNPP) {
				requestInsertLogKPI(HashMapKPI.GSNPP_GOPY, modelEvent.getActionEvent().startTimeFromBoot);
			}else if (GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType == UserDTO.TYPE_TNPG) {
				requestInsertLogKPI(HashMapKPI.TTTT_GOPY, modelEvent.getActionEvent().startTimeFromBoot);
			}
			break;
		case ActionEventConstant.UPDATE_FEEDBACK:
			parent.showDialog(StringUtil.getString(R.string.TEXT_UPDATE_FEEDBACK_SUCC));
			dto.currentPage = -1;
			getCustomerFeedBack(1, false);
			break;
		case ActionEventConstant.DELETE_FEEDBACK:
			parent.showDialog(StringUtil.getString(R.string.TEXT_DELETE_FEEDBACK_SUCC));
			dto.currentPage = -1;
			getCustomerFeedBack(1, true);
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}

	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);

	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void renderLayout() {
//		tbCusFeedBack.getListRowView().clear();
		// paging
		if (dto.currentPage <= 0) {
			tbCusFeedBack.setTotalSize(dto.totalFeedBack,1);
		} else {
			tbCusFeedBack.getPagingControl().setCurrentPage(dto.currentPage);
		}
		tbCusFeedBack.clearAllData();
		if (dto.arrItem.size() > 0) {
//			tbCusFeedBack.hideNoContentRow();
			int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbCusFeedBack.getPagingControl().getCurrentPage() - 1);
			for (FeedBackDTO feedBackDTO: dto.arrItem) {
				if(role == UserDTO.TYPE_GSNPP){
					GSNPPCustomerFeedBackRow row = new GSNPPCustomerFeedBackRow(parent, this);
					row.renderLayout(pos, feedBackDTO);
					pos++;
					tbCusFeedBack.addRow(row);
				}else {
					CustomerFeedBackRow row = new CustomerFeedBackRow(parent, this);
					row.renderLayout(pos, feedBackDTO);
					pos++;
					tbCusFeedBack.addRow(row);
				}
				
			}
		}else{
			tbCusFeedBack.showNoContentRow();
		}

	}
	/**
	 * 
	 * Hien thi pop up chi tiet gop y
	 * 
	 * @author: YenNTH
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	private void showFollowProblemDetail(FeedBackDTO dto) {
		if (alertFollowProblemDetail == null) {
			Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);
			feedBackDetailView = new GSNPPCustomerFeedBackDetailView(parent, this);
			build.setView(feedBackDetailView.viewLayout);
			alertFollowProblemDetail = build.create();
			Window window = alertFollowProblemDetail.getWindow();
			window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
			window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			window.setGravity(Gravity.CENTER);
		}
		feedBackDetailView.updateLayout(dto);
		alertFollowProblemDetail.show();
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbCusFeedBack) {
			dto.currentPage = tbCusFeedBack.getPagingControl().getCurrentPage();
			getCustomerFeedBack(dto.currentPage, false);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case ActionEventConstant.UPDATE_FEEDBACK:
			CheckBox cb = (CheckBox) control;
			Vector<Object> vt1 = new Vector<Object>();
			vt1.add(data);
			vt1.add(cb);
			//if (cb.isChecked()) {
				GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, this.parent, StringUtil.getString(R.string.TEXT_POST_FEEDBACK), StringUtil.getString(R.string.TEXT_AGREE),
						ACTION_SAVE_FEEDBACK, StringUtil.getString(R.string.TEXT_DENY),
						ACTION_CANCEL_FEEDBACK, vt1, false, false);
			//}
			break;
		case ActionEventConstant.DELETE_FEEDBACK:
			control.setEnabled(false);
			FeedBackDTO item = (FeedBackDTO) data;
			Vector<Object> vt = new Vector<Object>();
			vt.add(item);
			vt.add(control);
			GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent,
					StringUtil.getString(R.string.TEXT_QUESTION_DELETE), StringUtil.getString(R.string.TEXT_AGREE),
					ACTION_OK, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CANCEL, vt, false, false);
			break;
		case ActionEventConstant.GO_TO_FOLLOW_PROBLEM_DETAIL: {
			// show detail problem
			FeedBackDTO feedBackDto= (FeedBackDTO) data;
				showFollowProblemDetail(feedBackDto);
			break;
		}
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (feedBackDetailView != null && feedBackDetailView.btCloseFollowProblemDetail == v) {
			if (alertFollowProblemDetail != null && alertFollowProblemDetail.isShowing()) {
				alertFollowProblemDetail.dismiss();
			}
		}
		if (v == btFeedBack) {
			Bundle b = new Bundle();
			b.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getMenuIndexString());
			b.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
			b.putInt(IntentConstants.INTENT_FROM, PostFeedbackView.FROM_FEEDBACK_LIST_VIEW);
			ActionEvent e = new ActionEvent();
			e.action = ActionEventConstant.POST_FEEDBACK;
			e.sender = this;
			e.viewData = b;
			SaleController.getInstance().handleSwitchFragment(e);
		} else {
			super.onClick(v);
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case MENU_INFO_DETAIL: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerInfo();
		}
			break;
		case MENU_MAP: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoCustomerLocation();
		}
			break;
		case MENU_IMAGE: {
			CustomerListView frag = (CustomerListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListView.TAG);
			if (frag != null) {
				frag.isBackFromPopStack = true;
			}
			CustomerListViewPG fragPG = (CustomerListViewPG) this.getActivity().getFragmentManager()
					.findFragmentByTag(CustomerListViewPG.TAG);
			if (fragPG != null) {
				fragPG.isBackFromPopStack = true;
			}
			TrainingListView fragTrain = (TrainingListView) this.getActivity().getFragmentManager()
					.findFragmentByTag(TrainingListView.TAG);
			if (fragTrain != null) {
				fragTrain.isBackFromPopStack = true;
				fragTrain.isBackFromPopStackNot = true;
			}
			GlobalUtil.popBackStack(this.getActivity());
			gotoListAlbumUser(customer);
		}
			break;
		case ACTION_OK:
			Vector<Object> vt;
			View v;
			vt = (Vector<Object>) data;
			FeedBackDTO item = (FeedBackDTO) vt.elementAt(0);
			v = (View) vt.elementAt(1);
			item.status = 0;
			item.updateDate = DateUtils.getCurrentDateTimeWithFormat(DateUtils.DATE_FORMAT_NOW);
			item.updateUser = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
			parent.showLoadingDialog();
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.action = ActionEventConstant.DELETE_FEEDBACK;
			e.viewData = item;
			e.userData = v;
			e.isNeedCheckTimeServer = false;
			SaleController.getInstance().handleViewEvent(e);
			break;
		case ACTION_CANCEL:
			vt = (Vector<Object>) data;
			v = (View) vt.elementAt(1);
			v.setEnabled(true);
			break;
		case ACTION_SAVE_FEEDBACK:
			Vector<Object> vt1;
			CheckBox cb;
			vt1 = (Vector<Object>) data;
			FeedBackDTO item1 = (FeedBackDTO) vt1.elementAt(0);
			cb = (CheckBox) vt1.elementAt(1);
			cb.setEnabled(false);
			item1.doneDate = DateUtils.now();
			item1.updateDate = DateUtils.now();
			item1.updateUser = String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id);
			item1.status = FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE;
			updateFeedbackRow(item1);
			break;
		case ACTION_CANCEL_FEEDBACK:
			Vector<Object> vt2;
			CheckBox cb2;
			vt2 = (Vector<Object>) data;
			cb2 = (CheckBox) vt2.elementAt(1);
			cb2.setChecked(false);
		default:
			break;
		}
		super.onEvent(eventType, control, data);
	}

	/**
	 * di toi man hinh cap nhat vi tri khach hang
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void gotoCustomerLocation() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		bundle.putString(IntentConstants.INTENT_SENDER, sender);
		e.viewData = bundle;
		e.action = ActionEventConstant.GOTO_CUSTOMER_LOCATION;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * 
	 * Qua man hinh chi tiet co ban khach hang
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param
	 * @return: void
	 * @throws:
	 */
	public void gotoCustomerInfo() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		Bundle bunde = new Bundle();
		bunde.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bunde.putString(IntentConstants.INTENT_CUSTOMER_ID, String.valueOf(customer.customerId));
		bunde.putString(IntentConstants.INTENT_SENDER, sender);
		e.viewData = bunde;
		e.action = ActionEventConstant.GO_TO_CUSTOMER_INFO;
		UserController.getInstance().handleSwitchFragment(e);
	}

	/**
	 * updateFeedbackRow
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void updateFeedbackRow(FeedBackDTO item) {
		parent.showProgressDialog(getString(R.string.loading), false);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.action = ActionEventConstant.UPDATE_FEEDBACK;
		e.viewData = item;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Toi man hinh ds album cua kh
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param customer
	 * @return: void
	 * @throws:
	 */

	private void gotoListAlbumUser(CustomerDTO customer) {
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_PARENT_PREFIX_TITLE, getParentPrefixTitle());
		bundle.putSerializable(IntentConstants.INTENT_CUSTOMER, customer);
		bundle.putString(IntentConstants.INTENT_SENDER, sender);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = bundle;
		e.action = ActionEventConstant.GO_TO_LIST_ALBUM_USER;
		
		int typeUser = GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType;
		if(typeUser == UserDTO.TYPE_VALSALES || typeUser == UserDTO.TYPE_PRESALES){
			SaleController.getInstance().handleSwitchFragment(e); 
		}else if(typeUser == UserDTO.TYPE_TNPG){
			TNPGController.getInstance().handleSwitchFragment(e);
		}else if(typeUser == UserDTO.TYPE_GSNPP){
			SuperviorController.getInstance().handleSwitchFragment(e); 
		}else{
			SaleController.getInstance().handleSwitchFragment(e); 
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				dto.currentPage= 0;
				getCustomerFeedBack(1, true);
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
		return StringUtil.getString(R.string.TITLE_VIEW_FEEDBACK_DEFAULT);
	}
}
