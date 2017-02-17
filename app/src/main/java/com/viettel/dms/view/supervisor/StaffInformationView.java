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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.dto.view.SaleInMonthItemDto;
import com.viettel.dms.dto.view.StaffInfoDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.HashMapKPI;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.DMSTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Hiển thị thông tin nhân viên 
 * StaffInformationView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  16:05:57 19 Dec 2013
 */
public class StaffInformationView extends BaseFragment implements OnEventControlListener, OnClickListener,
		VinamilkTableListener {

	private GlobalBaseActivity parent; // parent
	TextView tvStaffCode;
	TextView tvStaffName;
	TextView tvStaffPhone;
	DMSTableView tbThreeMonth;
	DMSTableView tbLostCus;
	String staffCode;
	String staffName;
	String staffPhone;
	String staffId;
	StaffInfoDTO staffInfoDto;
	public int curPage = -1;
	private boolean isUpdateData = false;;
	private AlertDialog alertRemindDialog;
	private SaleInMonthItemDto curSaleInMonthItemDto;
	private StaffInfoCustomerNotPSDSInMonthsView cusNoPSDSView;

	public static final String TAG = StaffInformationView.class.getName();

	public static StaffInformationView getInstance(Bundle b) {
		StaffInformationView f = new StaffInformationView();
		f.setArguments(b);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		parent = (GlobalBaseActivity) activity;
		staffCode = getArguments().getString(IntentConstants.INTENT_STAFF_CODE);
		staffName = getArguments().getString(IntentConstants.INTENT_STAFF_NAME);
		staffPhone = getArguments().getString(IntentConstants.INTENT_STAFF_PHONE,"");
		staffId = getArguments().getString(IntentConstants.INTENT_STAFF_ID);
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_staff_info_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		this.tvStaffCode = (TextView) v.findViewById(R.id.tvStaffCode);
		this.tvStaffName = (TextView) v.findViewById(R.id.tvStaffName);
		this.tvStaffPhone = (TextView) v.findViewById(R.id.tvPhone);
		tbThreeMonth = (DMSTableView) v.findViewById(R.id.tbThreeMonth);
		tbThreeMonth.setListener(this);
		tbLostCus = (DMSTableView) v.findViewById(R.id.tbLostCus);
		tbLostCus.setListener(this);

		// update info:
		tvStaffCode.setText(staffCode);
		tvStaffName.setText(staffName);
		tvStaffPhone.setText(staffPhone);

		layoutTableHeader();

		getStaffInfo(1, 1, 1);

		setTitleHeaderView(getString(R.string.TITLE_HEADER_STAFF_INFO_VIEW));
		return v;
	}

	public void layoutTableHeader() {
		initHeaderTable(tbThreeMonth, new StaffInfoSaleProgressRow(parent, this));
		initHeaderTable(tbLostCus, new StaffInfoCusCanLose(parent, this));
	}

	/**
	 * getStaffInfo
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getStaffInfo(int isLoadSaleInMonth, int isGetTotalPage, int page) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		bundle.putInt(IntentConstants.INTENT_PAGE, page);
		bundle.putInt(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		bundle.putInt(IntentConstants.INTENT_LOAD_SALE_IN_MONTH, isLoadSaleInMonth);
		e.viewData = bundle;
		e.action = ActionEventConstant.STAFF_INFORMATION;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.STAFF_INFORMATION:
			StaffInfoDTO tempDto = (StaffInfoDTO) modelEvent.getModelData();
			if (staffInfoDto != null) {
				staffInfoDto.arrCusCanLose = tempDto.arrCusCanLose;
			} else {
				staffInfoDto = tempDto;
			}
			if (isUpdateData) {
				isUpdateData = false;
				curPage = -1;
			}
			renderLayout();
			parent.closeProgressDialog();
			requestInsertLogKPI(HashMapKPI.GSNPP_THONGTINNHANVIEN, modelEvent.getActionEvent().startTimeFromBoot);
			break;
		case ActionEventConstant.GET_CUSTOMER_NO_PSDS:
			CustomerListDTO cusDto = (CustomerListDTO) modelEvent.getModelData();
			if (alertRemindDialog != null && alertRemindDialog.isShowing()) {
				Bundle b = (Bundle) modelEvent.getActionEvent().viewData;
				cusDto.curPage = b.getInt(IntentConstants.INTENT_PAGE);
				cusNoPSDSView.renderLayout(cusDto);
			} else {
				showPopUpCusNoPSDS(cusDto, curSaleInMonthItemDto.month);
			}
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
	private void showPopUpCusNoPSDS(CustomerListDTO cusDto, String month) {
		Builder build = new AlertDialog.Builder(parent, R.style.CustomDialogTheme);

		cusNoPSDSView = new StaffInfoCustomerNotPSDSInMonthsView(parent, cusDto, month);
		build.setView(cusNoPSDSView.viewLayout);
		alertRemindDialog = build.create();
		Window window = alertRemindDialog.getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.argb(0, 255, 255, 255)));
		window.setLayout(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER);
		alertRemindDialog.show();
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		tbThreeMonth.clearAllData();
		tbLostCus.clearAllData();

		tbThreeMonth.setTotalSize(staffInfoDto.arrSaleProgress.size(),tbLostCus.getPagingControl().getCurrentPage());
		List<DMSTableRow> listSaleProgressRows = new ArrayList<DMSTableRow>();
		for (int i = 0, s = staffInfoDto.arrSaleProgress.size(); i < s; i++) {
			StaffInfoSaleProgressRow row = new StaffInfoSaleProgressRow(parent, this);
			row.render(staffInfoDto.arrSaleProgress.get(i));
			listSaleProgressRows.add(row);
			tbThreeMonth.addRow(row);
		}
//		tbThreeMonth.ad(listSaleProgressRows);

		if (curPage > 0) {
			tbLostCus.getPagingControl().setCurrentPage(curPage);
		} else {
			tbLostCus.setTotalSize(staffInfoDto.totalCusCanLose,1);
		}
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE * (tbLostCus.getPagingControl().getCurrentPage() - 1);
		List<DMSTableRow> listCanLoseRows = new ArrayList<DMSTableRow>();
		for (int i = 0, s = staffInfoDto.arrCusCanLose.size(); i < s; i++) {
			StaffInfoCusCanLose row = new StaffInfoCusCanLose(parent, this);
			row.render(pos, staffInfoDto.arrCusCanLose.get(i));
			pos++;
			listCanLoseRows.add(row);
			tbLostCus.addRow(row);
		}
//		tbLostCus.addContent(listCanLoseRows);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbLostCus) {
			curPage = tbLostCus.getPagingControl().getCurrentPage();
			getStaffInfo(0, 0, tbLostCus.getPagingControl().getCurrentPage());
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case ActionEventConstant.GET_CUSTOMER_NO_PSDS:
			SaleInMonthItemDto dto = (SaleInMonthItemDto) data;
			if (dto.noPSDS > 0) {
				curSaleInMonthItemDto = dto;
				getCusNotPSDS(dto, 1, true);
			}
			break;

		default:
			break;
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param dto
	 * @return: voidvoid
	 * @throws:
	 */
	private void getCusNotPSDS(SaleInMonthItemDto dto, int page, boolean isGetTotalPage) {
		Bundle b = new Bundle();
		// b.putString(IntentConstants.INTENT_STAFF_ID, staffId);
		// b.putString(IntentConstants.INTENT_DAY, dto.month);
		b.putInt(IntentConstants.INTENT_ID, dto.rptSaleHisId);
		b.putBoolean(IntentConstants.INTENT_GET_TOTAL_PAGE, isGetTotalPage);
		b.putInt(IntentConstants.INTENT_PAGE, page);
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = b;
		e.action = ActionEventConstant.GET_CUSTOMER_NO_PSDS;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	public void closePopup() {
		if (alertRemindDialog != null && alertRemindDialog.isShowing()) {
			alertRemindDialog.dismiss();
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				// cau request du lieu man hinh
				isUpdateData = true;
				getStaffInfo(1, 1, 1);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
