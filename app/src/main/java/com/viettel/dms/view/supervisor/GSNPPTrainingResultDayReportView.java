/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.db.ActionLogDTO;
import com.viettel.dms.dto.db.CustomerDTO;
import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO.GSNPPTrainingResultReportDayItem;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO.VISIT_STATUS;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.locating.PositionManager;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * Man hinh bao cao ket qua huan luyen nhan vien ban hang
 * 
 * @author hieunq1
 * 
 */
public class GSNPPTrainingResultDayReportView extends BaseFragment implements OnEventControlListener,
		VinamilkTableListener {

	private GlobalBaseActivity parent;
	private VinamilkTableView tbList;

	public long staffId;
	public long trainDetailId;
	public String staffName;
	public String saleTypeCode;
	public double staffScore;
	public long shopId;
	public GSNPPTrainingResultDayReportDTO dto;
	public String trainDate;
	private double lat = -1;
	private double lng = -1;
	private CountDownTimer timer;
	TextView tvScore;

	public static final String TAG = GSNPPTrainingResultDayReportView.class.getName();
	private static final int ACTION_POS_OK = 0;

	public static GSNPPTrainingResultDayReportView newInstance(Bundle viewData) {
		GSNPPTrainingResultDayReportView f = new GSNPPTrainingResultDayReportView();
		// Supply index input as an argument.
		if (viewData != null) {
			f.setArguments(viewData);
		}
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
		lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		long staffId = getArguments().getLong(IntentConstants.INTENT_STAFF_ID);
		long shopId = getArguments().getLong(IntentConstants.INTENT_SHOP_ID);
		String staffName = getArguments().getString(IntentConstants.INTENT_STAFF_NAME);
		double score = getArguments().getDouble(IntentConstants.INTENT_STAFF_TRAIN_SCORE);
		String date = getArguments().getString(IntentConstants.INTENT_STAFF_TRAIN_DATE);
		long traiDetailId = getArguments().getLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID);
		saleTypeCode = getArguments().getString(IntentConstants.INTENT_SALE_TYPE_CODE);

		this.staffId = staffId;
		this.staffName = staffName;
		this.staffScore = score;
		this.trainDate = date;
		this.trainDetailId = traiDetailId;
		this.shopId = shopId;

		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_staff_train_result_report, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_GSNPP_04_03_VIEW));

		TextView tvTrainDays = (TextView) view.findViewById(R.id.tvTrainDays);
		try {
			Date date1 = DateUtils.defaultDateFormat.parse(trainDate);
			tvTrainDays.setText(DateUtils.getDayOfWeek(date1) + ", " + DateUtils.defaultDateFormat.format(date1));
		} catch (ParseException e) {
		}
		TextView tvTenNVBH = (TextView) view.findViewById(R.id.tvTenNVBH);
		tvTenNVBH.setText(staffName);
		tvScore = (TextView) view.findViewById(R.id.tvScore);
		tvScore.setText(String.valueOf(staffScore));

		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		if (dto == null) {
			if (trainDate.equals(DateUtils.getCurrentDateTimeWithFormat("dd/MM/yyyy"))) {
				restartLocatingUpdate();
			}
			getReport();
		} else {
			renderLayout();
		}
		return v;
	}

	/**
	 * getCustomerList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getReport() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_STAFF_TRAIN_DETAIL_ID, trainDetailId);
		b.putLong(IntentConstants.INTENT_SHOP_ID, shopId);
		e.viewData = b;
		e.action = ActionEventConstant.GSNPP_TRAINING_PLAN_DAY_REPORT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GSNPP_TRAINING_PLAN_DAY_REPORT:
			dto = (GSNPPTrainingResultDayReportDTO) modelEvent.getModelData();
			renderLayout();
			parent.closeProgressDialog();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	private void renderLayout() {
		tvScore.setText("" + dto.score);
		boolean bd = trainDate.equalsIgnoreCase(DateUtils.defaultDateFormat.format(new Date()));
		List<TableRow> listRows = new ArrayList<TableRow>();
		for (GSNPPTrainingResultReportDayItem e : dto.listResult) {
			GSNPPTrainingResultReportDayRow child = new GSNPPTrainingResultReportDayRow(parent, false, tbList);
			child.render(e, dto.distance, lat, lng, bd);
			child.setListener(this);
			listRows.add(child);
		}
		GSNPPTrainingResultReportDayRow child = new GSNPPTrainingResultReportDayRow(parent, true, tbList);
		child.renderSum(dto, dto.score);
		listRows.add(child);
		tbList.addContent(listRows);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW:
			GSNPPTrainingResultReportDayItem item = (GSNPPTrainingResultReportDayItem) data;

			CustomerListItem cusItem = new CustomerListItem();
			cusItem.aCustomer.customerId = item.custId;
			cusItem.aCustomer.customerName = item.custName;
			cusItem.aCustomer.customerCode = item.custCode;
			cusItem.isOr = item.isOr;
			cusItem.visitStartTime = item.startTime;
			cusItem.visitEndTime = item.endTime;
			cusItem.visitActLogId = item.visitActLogId;

			ActionLogDTO actionLog = GlobalInfo.getInstance().getProfile().getActionLogVisitCustomer();
			if (actionLog != null && actionLog.aCustomer.customerId != item.custId
					&& StringUtil.isNullOrEmpty(actionLog.endTime) && actionLog.id > 0) {
				// ket thuc ghe tham
				parent.requestUpdateActionLog("0", null, cusItem, this);
			}

			if (item.isOr == 0) {
				if (item.visit == VISIT_STATUS.NONE_VISIT) {
					parent.initMenuVisitCustomer(item.custCode, item.custName);
					parent.removeMenuCloseCustomer();

					item.visit = VISIT_STATUS.VISITING;
					parent.requestStartInsertVisitActionLog(cusItem);
				} else if (item.visit == VISIT_STATUS.VISITING) {
					parent.initMenuVisitCustomer(item.custCode, item.custName);
					parent.removeMenuCloseCustomer();

					saveLastVisitedToActionLogProfile(cusItem);

				} else if (item.visit == VISIT_STATUS.VISITED) {
					parent.initCustomerNameOnMenuVisit(item.custCode, item.custName);
					saveLastVisitedToActionLogProfile(cusItem);
				}
			} else {
				parent.initCustomerNameOnMenuVisit(item.custCode, item.custName);
				saveLastVisitedToActionLogProfile(cusItem);
			}

			this.goToReviewStaffView(item);

			break;

		default:
			break;
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void saveLastVisitedToActionLogProfile(CustomerListItem item) {
		ActionLogDTO action = new ActionLogDTO();
		// chu y ko co id cua action_log
		action.aCustomer.customerId = item.aCustomer.customerId;
		action.aCustomer.customerName = item.aCustomer.customerName;
		action.aCustomer.customerCode = item.aCustomer.customerCode;
		action.startTime = item.visitStartTime;
		action.endTime = item.visitEndTime;
		action.isOr = item.isOr;
		action.createUser = GlobalInfo.getInstance().getProfile().getUserData().userCode;
		action.staffId = GlobalInfo.getInstance().getProfile().getUserData().id;
		action.lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
		action.lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
		// action.objectId = item.visit;
		action.objectType = "0";
		action.id = item.visitActLogId;
		GlobalInfo.getInstance().getProfile().setActionLogVisitCustomer(action);

	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @throws:
	 */
	public void goToReviewStaffView(GSNPPTrainingResultReportDayItem item) {
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID, String.valueOf(this.staffId));
		data.putString(IntentConstants.INTENT_STAFF_NAME, this.staffName);
		data.putString(IntentConstants.INTENT_SALE_TYPE_CODE, this.saleTypeCode);
		data.putString(IntentConstants.INTENT_SHOP_ID, "" + this.shopId);
		data.putString(IntentConstants.INTENT_TRAINING_DETAIL_ID, String.valueOf(this.trainDetailId));

		CustomerDTO cus = new CustomerDTO();
		cus.customerId = item.custId;
		cus.customerName = item.custName;
		cus.customerCode = item.custCode;
		cus.lat = item.lat;
		cus.lng = item.lng;

		data.putSerializable(IntentConstants.INTENT_CUSTOMER_OBJECT, cus);

		((SupervisorActivity) parent).setTrainingReviewStaffBundle(data);

		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = data;
		e.action = ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getReport();
			}
			break;
		case ActionEventConstant.ACTION_UPDATE_POSITION:
			// tranh truong hop notify nhieu lan
			if (this.lat > 0 && this.lng > 0) {
				return;
			}
			double lat = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLatitude();
			double lng = GlobalInfo.getInstance().getProfile().getMyGPSInfo().getLongtitude();
			if (lat < 0 || lng < 0) {
				parent.closeProgressDialog();
				//parent.showDialog(StringUtil.getString(R.string.TEXT_ALERT_CANT_LOCATE_YOUR_POSITION));
				return;
			} else {
				this.lat = lat;
				this.lng = lng;
				if (dto != null && dto.listResult != null && dto.listResult.size() > 0) {
					renderLayout();
				}
				parent.closeProgressDialog();
			}

			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
