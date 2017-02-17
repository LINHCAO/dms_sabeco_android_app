/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.ProgReportProDispDetailSaleDTO;
import com.viettel.dms.dto.view.ProgReportProDispDetailSaleRowDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * 
 * Man hinh hien thi bao cao chi tiet chuong trinh trung bay - khach hang tham
 * gia cua nhan vien ban hang
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class ProgReportProDispDetailSaleView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener, OnClickListener {

	public static final String TAG = ProgReportProDispDetailSaleView.class
			.getName();
	public static final int MENU_PSDS = 1;
	public static final int MENU_CTTB = 2;
	public static final int MENU_MHTT = 3;
	public static final int MENU_ACCUMULATE = 4;
	public static final int MENU_DATE = 5;

	TextView tvCTTBCode;// ma CTTB
	TextView tvCTTBName;// ten CTTB
	TextView tvNVBHCode;// ma nhan vien ban hang
	TextView tvViewAll;// hien thi tat ca

	private VinamilkTableView tbList;
	GlobalBaseActivity parent;
	ProgReportProDispDetailSaleDTO dto;
	private String displayProgrameCode; // ma CTTB
	private String displayProgrameName; // ten chuong trinh trung bay
	//private String staffCode; // ma nhan vien ban hang
	private String staffName; // ten nhan vien ban hang
	private long staffId; // id nhan vien ban hang
	private boolean isFirstTime;// kiem tra lan dau tien vao man hinh
	private boolean checkAll; // kiem tra xem tat ca

	public static ProgReportProDispDetailSaleView newInstance(Bundle data) {
		ProgReportProDispDetailSaleView instance = new ProgReportProDispDetailSaleView();
		// Supply index input as an argument.

		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		isFirstTime = true;
		checkAll = false;
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_prog_report_pro_disp_detail_sale, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_VIEW_PROG_REPOST_PRO_DISP_DETAIL_SALE)
				+ new SimpleDateFormat(" dd/MM/yyyy").format(new Date()));

		tvCTTBCode = (TextView) v.findViewById(R.id.tvCTTBCode);
		tvCTTBName = (TextView) v.findViewById(R.id.tvCTTBName);
		tvNVBHCode = (TextView) v.findViewById(R.id.tvNVBHCode);
		tvViewAll = (TextView) v.findViewById(R.id.tvViewAll);
		tvViewAll.setOnClickListener(this);
		tbList = (VinamilkTableView) v.findViewById(R.id.tbList);
		tbList.setListener(this);
		Bundle bundle = getArguments();
		displayProgrameCode = bundle.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE);
		displayProgrameName = bundle.getString(IntentConstants.INTENT_DISPLAY_PROGRAM_NAME);
		//staffCode = bundle.getString(IntentConstants.INTENT_STAFF_ID_PARA);
		staffName = bundle.getString(IntentConstants.INTENT_STAFF_NAME);
		staffId = bundle.getLong(IntentConstants.INTENT_STAFF_ID);
		tvCTTBCode.setText(displayProgrameCode);
		tvCTTBName.setText(displayProgrameName);
		tvNVBHCode.setText(staffName);
		initMenuActionBar();
		getAccList(0, 0);
		return v;
	}

	/**
	 * 
	 * thiet lap list row
	 * 
	 * @author: ThanhNN8
	 * @param checkAll
	 *            //kiem tra co phai request hien thi tat ca hay khong?
	 * @return: void
	 * @throws:
	 */
	// thiet lap list row
	private void getAccList(int pageNumber, int checkAll) {
		// TODO Auto-generated method stub
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		String page = " limit " + (pageNumber * Constants.NUM_ITEM_PER_PAGE)
				+ "," + Constants.NUM_ITEM_PER_PAGE;
		bundle.putString(IntentConstants.INTENT_PAGE, page);
		bundle.putLong(IntentConstants.INTENT_STAFF_ID, staffId);
		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE,
				displayProgrameCode);
		bundle.putInt(IntentConstants.INTENT_ID, checkAll);
		e.viewData = bundle;
		e.action = ActionEventConstant.PROG_REPOST_PRO_DISP_DETAIL_SALE;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	// thiet lap MenuActionBar
	private void initMenuActionBar() {
		// TODO Auto-generated method stub
		enableMenuBar(this);
		addMenuItem("Chưa PSDS", R.drawable.icon_order, MENU_PSDS);
		addMenuItem("CTTB", R.drawable.icon_reminders, MENU_CTTB);
		addMenuItem("MHTT", R.drawable.icon_list_star, MENU_MHTT);
		addMenuItem("Lũy kế", R.drawable.icon_accumulated, MENU_ACCUMULATE);
		addMenuItem("Ngày", R.drawable.icon_calendar, MENU_DATE);
		setMenuItemFocus(2);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.PROG_REPOST_PRO_DISP_DETAIL_SALE:
			dto = (ProgReportProDispDetailSaleDTO) modelEvent.getModelData();
			if (dto != null && dto.listItem.size() == 0 && checkAll == false) {
				checkAll = true;
				getAccList(0, 1);
				break;
			}
			renderData();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
		parent.closeProgressDialog();
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
		parent.closeProgressDialog();
	}

	/**
	 * 
	 * Layout cho man hinh
	 * 
	 * @author: ThanhNN8
	 * @return: void
	 * @throws:
	 */
	private void renderData() {
		// TODO Auto-generated method stub
		// tinh so trang trong truong hop hien thi tat ca cac loai khach hang
		if (checkAll) {
			if (dto.listItem.size() > 0 && dto.totalItem > Constants.NUM_ITEM_PER_PAGE) {
				tbList.getPagingControl().setVisibility(View.VISIBLE);
				if (tbList.getPagingControl().totalPage < 0) {
					tbList.setTotalSize(dto.totalItem);
					tbList.getPagingControl().setCurrentPage(1);
				}
			} else {
				tbList.getPagingControl().setVisibility(View.GONE);
			}
		} else {
			tbList.getPagingControl().setVisibility(View.GONE);
		}

		tbList.clearAllData();
		for (ProgReportProDispDetailSaleRowDTO detailSaleRowDTO : dto.listItem) {
			ProgReportProDispDetailSaleRow row = new ProgReportProDispDetailSaleRow(
					parent, false);
			row.render(detailSaleRowDTO);
			row.setListener(this);
			tbList.addRow(row);
		}
		if (checkAll == false) {
			ProgReportProDispDetailSaleRow row = new ProgReportProDispDetailSaleRow(
					parent, true);
			row.renderSum(dto.RemainSaleTotal);
			tbList.addRow(row);
			tvViewAll.setVisibility(View.VISIBLE);
		} else {
			tvViewAll.setVisibility(View.GONE);
		}
		if (isFirstTime) {
			tbList.getHeaderView().addColumns(
					TableDefineContanst.REPORT_DISPLAY_PROGRAME_DETAIL_WIDTHS,
					TableDefineContanst.REPORT_DISPLAY_PROGRAME_DETAIL_TITLES,
					ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			isFirstTime = false;
		}
	}

	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (control instanceof MenuItem) {
			switch (eventType) {
			case MENU_DATE: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_REPORT_PROGRESS_DATE;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case MENU_ACCUMULATE: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.ACC_SALE_PROG_REPORT;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case MENU_MHTT: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.PROG_REPOST_SALE_FOCUS;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case MENU_CTTB: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.DIS_PRO_COM_PROG_REPORT;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case MENU_PSDS: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			default:
				break;
			}
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		if (control == tbList && checkAll) {
			getAccList((tbList.getPagingControl().getCurrentPage() - 1), 1);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvViewAll) {
			checkAll = true;
			getAccList(0, 1);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				checkAll = false;
				getAccList(0, 0);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

}
