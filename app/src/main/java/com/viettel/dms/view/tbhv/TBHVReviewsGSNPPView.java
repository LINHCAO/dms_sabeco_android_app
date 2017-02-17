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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.TBHVController;
import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.db.FeedBackTBHVDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.TBHVActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * danh gia nhan xet GSNPP tren tuyen huan luyen
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReviewsGSNPPView extends BaseFragment implements
		OnTouchListener {
	// tag for fragment
	public static final String TAG = TBHVReviewsGSNPPView.class.getName();
	private static final int DONE_DATE_CONTROL = 1;
	// text view display code npp, name npp
	TextView tvNPPCodeName;
	// text view display code gsnpp, name gsnpp
	TextView tvGSNPPCodeName;
	// text view display NVBH name
	TextView tvNVBHName;
	// button save
	Button btSave;
	// input reviews general
	EditText etInputReviewsGeneral;
	// input reviews distribution and display
	EditText etInputReviewsDistributionDisplay;
	// input reveiws services customer
	EditText etInputReviewsServicesCustomer;
	// input reviews relationship customer
	EditText etInputReviewsRelationshipCustomer;
	// input reviews skill nvbh
	EditText etInputReviewsSkillNVBH;
	// input reviews skill gsnpp
	EditText etInputReviewsSkillGSNPP;
	// input reveiws other problems
	EditText etInputReviewsOtherProblems;
	// flag done load data the first
	boolean isDoneLoadFirst = false;
	// id npp
	long shopId;
	// code npp
	String shopCode;
	// name npp
	String shopName;
	// id gsnpp
	String gsnppId;
	// code gsnpp
	String gsnppCode;
	// name gsnpp
	String gsnppName;
	// name NVBH
	String nvbhName;
	// // training plan detail id
	// int trainingPlanDetailId;
	// list reviews
	List<FeedBackTBHVDTO> listReviews = new ArrayList<FeedBackTBHVDTO>();
	// parent activity
	private TBHVActivity parent;
	// input done date
	VNMEditTextClearable etInputDoneDate;
	// current calendar
	int currentCalendar;

	public static TBHVReviewsGSNPPView getInstance(Bundle data) {
		TBHVReviewsGSNPPView instance = new TBHVReviewsGSNPPView();
		instance.setArguments(data);
		return instance;
	}

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_tbhv_reviews_gsnpp_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		if (!StringUtil.isNullOrEmpty(String.valueOf(getArguments().getLong(
				IntentConstants.INTENT_SHOP_ID)))) {
			this.shopId = getArguments().getLong(IntentConstants.INTENT_SHOP_ID);
		}
		// if (!StringUtil.isNullOrEmpty(String.valueOf(getArguments().getLong(
		// IntentConstants.INTENT_TRAINING_DETAIL_ID)))) {
		// this.trainingPlanDetailId = getArguments().getInt(
		// IntentConstants.INTENT_TRAINING_DETAIL_ID);
		// }
		if (!StringUtil.isNullOrEmpty(getArguments().getString(
				IntentConstants.INTENT_SHOP_CODE))) {
			this.shopCode = getArguments().getString(
					IntentConstants.INTENT_SHOP_CODE);
		}
		if (!StringUtil.isNullOrEmpty(getArguments().getString(
				IntentConstants.INTENT_SHOP_NAME))) {
			this.shopName = getArguments().getString(
					IntentConstants.INTENT_SHOP_NAME);
		}
		if (!StringUtil.isNullOrEmpty(getArguments().getString(
				IntentConstants.INTENT_STAFF_OWNER_ID))) {
			this.gsnppId = getArguments().getString(
					IntentConstants.INTENT_STAFF_OWNER_ID);
		}
		if (!StringUtil.isNullOrEmpty(getArguments().getString(
				IntentConstants.INTENT_STAFF_OWNER_CODE))) {
			this.gsnppCode = getArguments().getString(
					IntentConstants.INTENT_STAFF_OWNER_CODE);
		}
		if (!StringUtil.isNullOrEmpty(getArguments().getString(
				IntentConstants.INTENT_STAFF_OWNER_NAME))) {
			this.gsnppName = getArguments().getString(
					IntentConstants.INTENT_STAFF_OWNER_NAME);
		}
		if (!StringUtil.isNullOrEmpty(getArguments().getString(
				IntentConstants.INTENT_STAFF_NAME))) {
			this.nvbhName = getArguments().getString(
					IntentConstants.INTENT_STAFF_NAME);
		}

		this.setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_VIEW_TBHV_REVIEWS_VIEW));
		this.initView(v);
		if (!this.isDoneLoadFirst) {
			this.requestGetReviewsInfo();
		}
		return v;
	}

	/**
	 * kiem tra ngay thuc hien hop le
	 * @return
	 * @return: boolean
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
	 */
	public boolean checkDoneDateCorrect() {
		boolean result = true;
		String doneDate = etInputDoneDate.getText().toString().trim();
		if (StringUtil.isNullOrEmpty(doneDate)) {
			parent.showDialog(StringUtil
					.getString(R.string.TEXT_NOTIFY_DONE_DATE_NOT_NULL));
			result = false;
		} else {
			if (DateUtils.isCompareWithToDate(doneDate) < 0) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_LABLE_TBHV_INFO_REMIND_DATE_ERROR));
				result = false;
			}
		}
		return result;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (this.isDoneLoadFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * init control for screen
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		tvNPPCodeName = (TextView) v.findViewById(R.id.tvNPPCodeName);
		tvNPPCodeName.setText(this.shopCode );
		tvGSNPPCodeName = (TextView) v.findViewById(R.id.tvGSNPPCodeName);
		tvGSNPPCodeName.setText(this.gsnppName);
		tvNVBHName = (TextView) v.findViewById(R.id.tvNVBHName);
		tvNVBHName.setText(this.nvbhName);

		btSave = (Button) v.findViewById(R.id.btSave);
		btSave.setOnClickListener(this);
		etInputReviewsDistributionDisplay = (EditText) v
				.findViewById(R.id.etInputReviewsDistributionDisplay);
		etInputReviewsGeneral = (EditText) v
				.findViewById(R.id.etInputReviewsGeneral);
		etInputReviewsOtherProblems = (EditText) v
				.findViewById(R.id.etInputReviewsOtherProblems);
		etInputReviewsRelationshipCustomer = (EditText) v
				.findViewById(R.id.etInputReviewsRelationshipCustomer);
		etInputReviewsServicesCustomer = (EditText) v
				.findViewById(R.id.etInputReviewsServicesCustomer);
		etInputReviewsSkillGSNPP = (EditText) v
				.findViewById(R.id.etInputReviewsSkillGSNPP);
		etInputReviewsSkillNVBH = (EditText) v
				.findViewById(R.id.etInputReviewsSkillNVBH);
		etInputDoneDate = (VNMEditTextClearable) v
				.findViewById(R.id.etInputDoneDate);
		this.etInputDoneDate.setIsHandleDefault(false);
		etInputDoneDate.setOnTouchListener(this);
		etInputDoneDate.setText(DateUtils.getCurrentDate());
	}

	/**
	 * render layout for screen
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void renderLayout() {
		for (int i = 0, size = this.listReviews.size(); i < size; i++) {
			FeedBackTBHVDTO item = this.listReviews.get(i);
			switch (item.feedBackBasic.type) {
			case FeedBackDTO.FEEDBACK_TYPE_GENERAL:
				etInputReviewsGeneral.setText(item.feedBackBasic.content);
				break;
			case FeedBackDTO.FEEDBACK_TYPE_DISTRIBUTION_DISPLAY:
				etInputReviewsDistributionDisplay
						.setText(item.feedBackBasic.content);
				break;
			case FeedBackDTO.FEEDBACK_TYPE_OTHER_PROBLEM:
				etInputReviewsOtherProblems.setText(item.feedBackBasic.content);
				break;
			case FeedBackDTO.FEEDBACK_TYPE_RELATIONSHIP_CUSTOMER:
				etInputReviewsRelationshipCustomer
						.setText(item.feedBackBasic.content);
				break;
			case FeedBackDTO.FEEDBACK_TYPE_SERVICE_CUSTOMER:
				etInputReviewsServicesCustomer
						.setText(item.feedBackBasic.content);
				break;
			case FeedBackDTO.FEEDBACK_TYPE_SKILL_GSNPP:
				etInputReviewsSkillGSNPP.setText(item.feedBackBasic.content);
				break;
			case FeedBackDTO.FEEDBACK_TYPE_SKILL_NVBH:
				etInputReviewsSkillNVBH.setText(item.feedBackBasic.content);
				break;
			default:
				break;
			}

			etInputDoneDate.setText(item.feedBackBasic.remindDate);
		}
	}

	/**
	 * get past reviews info for screen
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void requestGetReviewsInfo() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		// data.putString(IntentConstants.INTENT_TRAINING_DETAIL_ID,
		// String.valueOf(this.trainingPlanDetailId));
		data.putString(IntentConstants.INTENT_STAFF_ID, this.gsnppId);
		data.putString(
				IntentConstants.INTENT_CREATE_USER_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_TBHV_REVIEWS_INFO;
		e.isNeedCheckTimeServer = false;
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * update training reveiws to db local and to server
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void requestUpdateReviewsToDataBase() {
		this.parent.showProgressDialog(getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putSerializable(
				IntentConstants.INTENT_TBHV_LIST_TRAINING_SHOP_OBJECT,
				(Serializable) this.listReviews);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.TBHV_UPDATE_TRAINING_REVIEWS_INFO_TO_DB;
		e.isNeedCheckTimeServer = false;
		this.btSave.setEnabled(false);
		TBHVController.getInstance().handleViewEvent(e);
	}

	/**
	 * check object is exist in list
	 * @author: HaiTC3
	 * @param objectType
	 * @return
	 * @return: boolean
	 * @throws:
	 */
	public int isExistObjectInList(int objectType) {
		int index = -1;
		for (int i = 0, size = this.listReviews.size(); i < size; i++) {
			if (objectType == this.listReviews.get(i).feedBackBasic.type) {
				index = i;
			}
		}
		return index;
	}

	/**
	 * general data to save to DB or delete from db or update to DB
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void generalDataSaveToDB() {
		// reviews general about flow sale
		String text = this.etInputReviewsGeneral.getText().toString().trim();
		String remindDate = this.getRemindDate();
		int index = this.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_GENERAL);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu nhap vao textbox
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_GENERAL,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}

		// reviews distribution and display
		text = this.etInputReviewsDistributionDisplay.getText().toString()
				.trim();
		index = this
				.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_DISTRIBUTION_DISPLAY);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_DISTRIBUTION_DISPLAY,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}
		// reviews customer service
		text = this.etInputReviewsServicesCustomer.getText().toString().trim();
		index = this
				.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_SERVICE_CUSTOMER);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_SERVICE_CUSTOMER,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}
		// reviews relationship customer
		text = this.etInputReviewsRelationshipCustomer.getText().toString()
				.trim();
		index = this
				.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_RELATIONSHIP_CUSTOMER);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_RELATIONSHIP_CUSTOMER,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}
		// reviews skill NVBH
		text = this.etInputReviewsSkillNVBH.getText().toString().trim();
		index = this.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_SKILL_NVBH);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_SKILL_NVBH,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}
		// reviews skill GSNPP
		text = this.etInputReviewsSkillGSNPP.getText().toString().trim();
		index = this.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_SKILL_GSNPP);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_SKILL_GSNPP,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}
		// reviews skill other problem
		text = this.etInputReviewsOtherProblems.getText().toString().trim();
		index = this
				.isExistObjectInList(FeedBackDTO.FEEDBACK_TYPE_OTHER_PROBLEM);
		if (StringUtil.isNullOrEmpty(text)) { // rong
			if (index != -1) { // ton tai
				this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_DELETED;
			}
		} else { // co du lieu
			if (index != -1) { // ton tai
				// co thay doi noi dung
				if (!text
						.equals(this.listReviews.get(index).feedBackBasic.content)
						|| (StringUtil.isNullOrEmpty(remindDate))
						|| (!remindDate
								.equals(this.listReviews.get(index).feedBackBasic.remindDate))) {
					this.listReviews.get(index).feedBackBasic.content = text;
					this.listReviews.get(index).feedBackBasic.updateDate = DateUtils
							.now();
					this.listReviews.get(index).feedBackBasic.remindDate = this
							.getRemindDate();
					this.listReviews.get(index).feedBackBasic.updateUser = String
							.valueOf(GlobalInfo.getInstance().getProfile()
									.getUserData().id);
					this.listReviews.get(index).currentState = FeedBackTBHVDTO.STATE_NEW_UPDATE;
				}
			} else { // chua ton tai
				FeedBackTBHVDTO newObject = new FeedBackTBHVDTO(
						FeedBackDTO.FEEDBACK_TYPE_OTHER_PROBLEM,
						FeedBackTBHVDTO.STATE_NEW_INSERT);
				this.updateFeedbackInfo(newObject, text);
				this.listReviews.add(newObject);

			}
		}
	}

	/**
	 * update feedback info
	 * @param newObject
	 * @param text
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
	 */
	public void updateFeedbackInfo(FeedBackTBHVDTO newObject, String text) {
		newObject.updateFeedbackInf(
				text,
				this.gsnppId,
				FeedBackDTO.FEEDBACK_STATUS_CREATE,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id),
				DateUtils.now(),
				this.getRemindDate(),
				0,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
	}

	/**
	 * get remind date when save
	 * @return
	 * @return: String
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
	 */
	public String getRemindDate() {
		String result = Constants.STR_BLANK;
		try {
			String strRemindDate = etInputDoneDate.getText().toString().trim();
			if (!StringUtil.isNullOrEmpty(strRemindDate)) {
				Date tn = StringUtil.stringToDate(strRemindDate,
						Constants.STR_BLANK);
				result = StringUtil.dateToString(tn, "yyyy-MM-dd");
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.GET_TBHV_REVIEWS_INFO:
			if (modelEvent.getModelData() != null) {
				this.isDoneLoadFirst = true;
				this.listReviews = (List<FeedBackTBHVDTO>) modelEvent
						.getModelData();
				this.renderLayout();
			}
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.TBHV_UPDATE_TRAINING_REVIEWS_INFO_TO_DB:
			this.btSave.setEnabled(true);
			GlobalUtil.popBackStack(this.getActivity());
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
		case ActionEventConstant.GET_TBHV_REVIEWS_INFO:
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.TBHV_UPDATE_TRAINING_REVIEWS_INFO_TO_DB:
			// remove object new insert out of list
			for (int i = 0, size = this.listReviews.size(); i < size; i++) {
				if (this.listReviews.get(i).currentState == FeedBackTBHVDTO.STATE_NEW_INSERT) {
					this.listReviews.remove(i);
					size = this.listReviews.size();
				}
			}
			this.parent.closeProgressDialog();
			this.btSave.setEnabled(true);
			break;
		default:
			break;
		}
	}

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btSave) {
			if (this.checkDoneDateCorrect()) {
				this.generalDataSaveToDB();
				this.requestUpdateReviewsToDataBase();
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
			 this.requestGetReviewsInfo();
			 }
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if (v == etInputDoneDate) {
			if (!etInputDoneDate.onTouchEvent(event)) {
				currentCalendar = DONE_DATE_CONTROL;
				parent.fragmentTag = TBHVReviewsGSNPPView.TAG;
				parent.showDatePickerWithDate(GlobalBaseActivity.DATE_DIALOG_ID, etInputDoneDate.getText().toString().trim(), true);
			}
		}
		return true;
	}

	/**
	 * 
	 * update edittext done date
	 * 
	 * @param dayOfMonth
	 * @param monthOfYear
	 * @param year
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 8, 2012
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
		String dates = Constants.STR_BLANK;
		if (currentCalendar == DONE_DATE_CONTROL) {
			dates = new StringBuilder().append(sDay).append("/").append(sMonth)
					.append("/").append(year).append(Constants.STR_SPACE)
					.toString();
			int a = DateUtils.isCompareWithToDate(dates);
			if (a < 0) {
				parent.showDialog(StringUtil
						.getString(R.string.TEXT_LABLE_TBHV_INFO_REMIND_DATE_ERROR));
			} else {
				etInputDoneDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
				etInputDoneDate.setText(new StringBuilder().append(sDay)
						.append("/").append(sMonth).append("/").append(year)
						.append(Constants.STR_SPACE));
			}

		}
	}
}
