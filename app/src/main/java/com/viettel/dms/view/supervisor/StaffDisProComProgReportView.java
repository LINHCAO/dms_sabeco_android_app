/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.DisProComProgReportDTO;
import com.viettel.dms.dto.view.DisProComProgReportDTO.DisProComProgReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * man hinh bao cao chuong trinh trung bay cua nhan vien ban hang
 * 
 * @author hieunq1
 * 
 */
public class StaffDisProComProgReportView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener {

	private static final int ACTION_DATE = 1;
	private static final int ACTION_ACC = 2;
	private static final int ACTION_MHTT = 3;
	private static final int ACTION_CTTB = 4;
	private static final int ACTION_PSDS = 5;

	private GlobalBaseActivity parent;
	private VinamilkTableView tbList;
	private DisProComProgReportDTO dto;
	public String disProId;
	public String disProCode;
	public String disProName;
	private LinearLayout llLevel;
	private TextView tvJoin;

	public static final String TAG = StaffDisProComProgReportView.class
			.getName();

	public static StaffDisProComProgReportView getInstance() {
		StaffDisProComProgReportView f = new StaffDisProComProgReportView();
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater
				.inflate(R.layout.layout_staff_dis_pro_com_prog_report,
						container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TEXT_REPORT_PROGRESS_CTTB_NVBH_DATE)
				+ DateUtils.defaultDateFormat.format(new Date()));
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),
				R.drawable.icon_order, ACTION_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB),
				R.drawable.icon_reminders, ACTION_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT),
				R.drawable.icon_list_star, ACTION_MHTT);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, ACTION_ACC);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, ACTION_DATE);
		setMenuItemFocus(2);

		TextView tvMaCTTB = (TextView) view.findViewById(R.id.tvMaCTTB);
		tvMaCTTB.setText(disProCode);
		TextView tvTenCTTB = (TextView) view.findViewById(R.id.tvTenCTTB);
		tvTenCTTB.setText(disProName);
		llLevel = (LinearLayout) view.findViewById(R.id.layoutLevel);
		tvJoin = (TextView) view.findViewById(R.id.tvJoin);
		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
		getList();
		return v;
	}

	/**
	 * get data
	 */
	private void getList() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));

		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();

		b.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		b.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_ID, disProId);
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		e.viewData = b;
		e.action = ActionEventConstant.STAFF_DIS_PRO_COM_PROG_REPORT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.STAFF_DIS_PRO_COM_PROG_REPORT:
			dto = (DisProComProgReportDTO) modelEvent.getModelData();
			renderLayout();
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
	 * render layout
	 */
	private void renderLayout() {
		llLevel.removeAllViews();
		// tao d/s title colum cua level
		String[] listCharTitle = new String[] { "A", "B", "C", "D", "E", "F",
				"G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "W",
				"V", "Y", "Z" };

		int widthTVLevel = dto.maxLevelDisPlay <= 5 ? 476 / (dto.maxLevelDisPlay + 1)
				: 476 / 6;
		for (int i = 0; i < dto.maxLevelDisPlay; i++) {
			DisProComProgReportLevelTextView tv = new DisProComProgReportLevelTextView(
					parent);
			if (i == 0) {
				tv.setWithForTextViewLevel(widthTVLevel, true, false);
			} else if (i == dto.maxLevelDisPlay - 1) {
				tv.setWithForTextViewLevel(widthTVLevel, false, true);
			} else {
				tv.setWithForTextViewLevel(widthTVLevel, false, false);
			}
			tv.render(listCharTitle[i]);
			llLevel.addView(tv);
		}
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(widthTVLevel + 10);
		param.height = LayoutParams.FILL_PARENT;
		param.setMargins(GlobalUtil.dip2Pixel(0), GlobalUtil.dip2Pixel(2),
				GlobalUtil.dip2Pixel(0), GlobalUtil.dip2Pixel(1));
		tvJoin.setLayoutParams(param);

		List<TableRow> listRows = new ArrayList<TableRow>();
		for (int i = 0, s = dto.arrList.size(); i < s; i++) {
			StaffDisProComProgReportRow row = new StaffDisProComProgReportRow(
					parent, false);
			row.render(dto.arrList.get(i), dto.maxLevelDisPlay, widthTVLevel);
			row.setListener(this);
			listRows.add(row);
		}

		StaffDisProComProgReportRow row = new StaffDisProComProgReportRow(
				parent, true);
		row.renderSum(dto, widthTVLevel);
		listRows.add(row);
		tbList.addContent(listRows);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (control instanceof MenuItem) {
			switch (eventType) {
			case ACTION_DATE: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_REPORT_PROGRESS_DATE;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_ACC: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.ACC_SALE_PROG_REPORT;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_MHTT: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.PROG_REPOST_SALE_FOCUS;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_CTTB: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.DIS_PRO_COM_PROG_REPORT;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case ACTION_PSDS: {
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

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		DisProComProgReportItem item = (DisProComProgReportItem) data;
		Bundle bundle = new Bundle();
		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_ID, disProId);
		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_CODE,disProCode);
		bundle.putString(IntentConstants.INTENT_DISPLAY_PROGRAM_NAME,disProName);
		bundle.putLong(IntentConstants.INTENT_STAFF_ID,Long.parseLong(item.staffId));
		bundle.putString(IntentConstants.INTENT_STAFF_ID_PARA, item.staffCode);
		bundle.putString(IntentConstants.INTENT_STAFF_NAME, item.staffName);
		ActionEvent actionEvent = new ActionEvent();
		actionEvent.viewData = bundle;
		actionEvent.action = ActionEventConstant.PROG_REPOST_PRO_DISP_DETAIL_SALE;
		actionEvent.sender = this;
		SuperviorController.getInstance().handleSwitchFragment(actionEvent);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getList();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
