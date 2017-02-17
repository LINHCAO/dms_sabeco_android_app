/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO.InfoProgressEmployeeDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO.ReportProductFocusItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
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
 * 01-02. Báo cáo tiến độ bán MHTT đến ngày
 * 
 * @author: HoanPD1
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint("SimpleDateFormat")
public class ProgressReportSalesFocusView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener {

	public static final String TAG = ProgressReportSalesFocusView.class
			.getName();
	// show customer not PSDS
	public static final int MENU_PSDS = 1;
	// show report CTTB screen
	public static final int MENU_CTTB = 2;
	// show report MHTT screen
	public static final int MENU_MHTT = 3;
	// show report sale in month screen
	public static final int MENU_ACCUMULATE = 4;
	// show report sale to day screen
	public static final int MENU_DATE = 5;

	TextView tvPlanDate;// so ngay ban hang ke hoach
	TextView tvPastDate;// so ngay ban hang da qua
	TextView tvProgress;// Tien do

	// bang bao cao tien do ban hang trong tam
	private VinamilkTableView tbProgReportsSalesFocus;
	GlobalBaseActivity parent;

	ProgressReportSalesFocusDTO progReportSalesFocusDTO;
	private View viewGroup;
	private View vItemHeader;
	LinearLayout vMHTT;
	Vector<View> vTmp = new Vector<View>();
	// check load the first done
	public boolean isDoneLoadFirst = false;

	public static ProgressReportSalesFocusView newInstance(Bundle data) {
		ProgressReportSalesFocusView instance = new ProgressReportSalesFocusView();

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
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_progress_report_sales_focus, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_VIEW_PROGRESS_REPORT_SALES_FOCUS)
				+ new SimpleDateFormat(" dd/MM/yyyy").format(new Date()));
		viewGroup = v;
		initViewControl(v);
		initMenuActionBar();
		if (!this.isDoneLoadFirst) {
			getAccList();
		}
		return v;
	}

	/**
	 * 
	 * init view control for screen
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 * @since: Jan 29, 2013
	 */
	public void initViewControl(View v) {
		tvPlanDate = (TextView) v.findViewById(R.id.tvPlanDate);
		tvPastDate = (TextView) v.findViewById(R.id.tvSoldDate);
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
		tbProgReportsSalesFocus = (VinamilkTableView) v
				.findViewById(R.id.tbProgReportsSalesFocus);
		tbProgReportsSalesFocus.setListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if (this.isDoneLoadFirst) {
			renderData();
		}
		super.onResume();
	}

	/**
	 * 
	 * get danh sach nhan vien bao cao tien do
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void getAccList() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		// TODO Auto-generated method stub
		ActionEvent e = new ActionEvent();
		// Vector<String> data = new Vector();
		Bundle data = new Bundle();
		data.putLong(IntentConstants.INTENT_STAFF_ID, GlobalInfo.getInstance().getProfile().getUserData().id);
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance().getProfile().getUserData().shopId);
		e.viewData = data;
		e.action = ActionEventConstant.PROG_REPOST_SALE_FOCUS;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 */
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.PROG_REPOST_SALE_FOCUS:
			progReportSalesFocusDTO = (ProgressReportSalesFocusDTO) modelEvent
					.getModelData();
			if (progReportSalesFocusDTO != null
					&& progReportSalesFocusDTO.arrRptFocusItemTotal != null) {
				for (int i = 0, sizeTotal = progReportSalesFocusDTO.arrRptFocusItemTotal
						.size(); i < sizeTotal; i++) {
					ReportProductFocusItem item = progReportSalesFocusDTO.arrRptFocusItemTotal
							.get(i);
					item.leftMoney = item.planMoney > item.soldMoney ? (item.planMoney - item.soldMoney)
							: 0;

					item.soldPercent = (int) item.planMoney > 0 ? item.soldMoney
							* 100 / item.planMoney
							: 100;
				}
			}
			renderData();
			this.isDoneLoadFirst = true;
			parent.closeProgressDialog();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.PROG_REPOST_SALE_FOCUS:
			parent.closeProgressDialog();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * thiet lap menu bar
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void initMenuActionBar() {
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_PSDS),
				R.drawable.icon_order, MENU_PSDS);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_CTTB),
				R.drawable.icon_reminders, MENU_CTTB);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT),
				R.drawable.icon_list_star, MENU_MHTT);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated, MENU_ACCUMULATE);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, MENU_DATE);
		setMenuItemFocus(3);
	}

	/**
	 * 
	 * Render lauout
	 * 
	 * @author: HoanPD1
	 * @return: void
	 * @throws:
	 */
	private void renderData() {
		vMHTT = (LinearLayout) viewGroup.findViewById(R.id.layMHTT);
		LayoutInflater vi = (LayoutInflater) parent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (View v : vTmp) {
			vMHTT.removeView(v);
		}
		vTmp.removeAllElements();
		for (int i = 0; i < progReportSalesFocusDTO.arrMMTTText.size(); i++) {
			String number = progReportSalesFocusDTO.arrMMTTText.get(i);
			vItemHeader = vi.inflate(
					R.layout.layout_progress_report_sales_focus_header_item,
					null);
			TextView tv = (TextView) vItemHeader.findViewById(R.id.tvMHTT);
			tv.setText(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT)
					+ Constants.STR_SPACE + number + Constants.STR_SPACE
					+ StringUtil.getString(R.string.TEXT_DVT_VND));
			vMHTT.addView(vItemHeader);
			vTmp.add(vItemHeader);
		}

		tvPlanDate.setText(String.valueOf(progReportSalesFocusDTO.PlanDay));
		tvPastDate.setText(String.valueOf(progReportSalesFocusDTO.PastDay));
		tvProgress.setText(String
				.valueOf((int) progReportSalesFocusDTO.Progress) + "%");
		tbProgReportsSalesFocus.clearAllData();
		for (InfoProgressEmployeeDTO employeeDTO : progReportSalesFocusDTO.listProgressSalesStaff) {
			ProgressReportSalesFocusRow row = new ProgressReportSalesFocusRow(
				parent, false);
			row.setDataRow(progReportSalesFocusDTO, employeeDTO);
			row.setListener(this);
			tbProgReportsSalesFocus.addRow(row);
		}

		ProgressReportSalesFocusRow row = new ProgressReportSalesFocusRow(
				parent, true);
		// row tong
		row.renderSum(progReportSalesFocusDTO);
		tbProgReportsSalesFocus.addRow(row);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub
		switch (action) {
		case ProgressReportSalesFocusRow.STAFF_INFORMATION: {
			InfoProgressEmployeeDTO dto = (InfoProgressEmployeeDTO) data;
			Bundle bundle = new Bundle();
			bundle.putString(IntentConstants.INTENT_STAFF_ID,String.valueOf(dto.staffId));
			bundle.putString(IntentConstants.INTENT_STAFF_CODE, dto.staffCode);
			bundle.putString(IntentConstants.INTENT_STAFF_NAME, dto.staffName);
			if (!StringUtil.isNullOrEmpty(dto.staffMobile)) {
				bundle.putString(IntentConstants.INTENT_STAFF_PHONE,dto.staffMobile);
			} else if (!StringUtil.isNullOrEmpty(dto.staffPhone)) {
				bundle.putString(IntentConstants.INTENT_STAFF_PHONE,dto.staffPhone);
			}
			ActionEvent e = new ActionEvent();
			e.viewData = bundle;
			e.action = ActionEventConstant.STAFF_INFORMATION;
			e.sender = this;
			SuperviorController.getInstance().handleSwitchFragment(e);
			break;
		}
		default:
			break;
		}
	}

	/**
	 * xu ly khi chon menubar
	 */
	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		if (control instanceof MenuItem) {
			switch (eventType) {
			case MENU_PSDS: {
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
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
			case MENU_MHTT:
				break;
			case MENU_ACCUMULATE: {
				ActionEvent e = new ActionEvent();
				e.action = ActionEventConstant.ACC_SALE_PROG_REPORT;
				e.sender = this;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			}
			case MENU_DATE:
				ActionEvent e = new ActionEvent();
				e.sender = this;
				e.viewData = new Bundle();
				e.action = ActionEventConstant.ACTION_REPORT_PROGRESS_DATE;
				SuperviorController.getInstance().handleSwitchFragment(e);
				break;
			default:
				break;
			}
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				getAccList();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
