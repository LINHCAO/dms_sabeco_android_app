/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.Date;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SuperviorController;
import com.viettel.dms.dto.view.AccSaleProgReportDTO;
import com.viettel.dms.dto.view.AccSaleProgReportDTO.AccSaleProgReportItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.MenuItem;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Man hinh bao cao luy ke den ngay
 * 
 * @author : HieuNQ since : 9:48:52 AM version :
 */
public class AccSaleProgReportView extends BaseFragment implements
		OnEventControlListener, VinamilkTableListener {

	public static final String TAG = AccSaleProgReportView.class.getName();
	// bao cao ngay
	private static final int ACTION_DATE = 1;
	// bao cao luy ke den ngay hien tai
	private static final int ACTION_ACC = 2;
	// bao cao MHTT
	private static final int ACTION_MHTT = 3;
	// bao cao CTTB
	private static final int ACTION_CTTB = 4;
	// bao cao PSDS
	private static final int ACTION_PSDS = 5;

	private GlobalBaseActivity parent;
	// ngay ban hang theo ke hoach
	public TextView tvPlanDate;
	// ngay ban hang da qua
	public TextView tvSoldDate;
	// tien do ban hang
	public TextView tvProgress;
	// table d/s bao cao
	private VinamilkTableView tbList;
	// data for table
	public AccSaleProgReportDTO dto;
	// check done load data the first
	public boolean isDoneLoadFirst = false;

	public static AccSaleProgReportView getInstance() {
		AccSaleProgReportView f = new AccSaleProgReportView();
		// Supply index input as an argument.
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
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_acc_sale_prog_report, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil
				.getString(R.string.TEXT_REPORT_PROGRESS_TO_DATE)
				+ Constants.STR_SPACE
				+ DateUtils.defaultDateFormat.format(new Date()));
		// enable menu bar
		initMenuHeader();

		// init view for screen
		initView(v);
		if (!this.isDoneLoadFirst) {
			getAccList();
		}

		return v;
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
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * 
	 * init header menu
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	public void initMenuHeader() {
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
		setMenuItemFocus(4);
	}

	/**
	 * 
	 * init view for screen
	 * 
	 * @param view
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	public void initView(View view) {
		tvPlanDate = (TextView) view.findViewById(R.id.tvPlanDate);
		tvProgress = (TextView) view.findViewById(R.id.tvProgress);
		tvSoldDate = (TextView) view.findViewById(R.id.tvSoldDate);
		tbList = (VinamilkTableView) view.findViewById(R.id.tbCusList);
		tbList.setListener(this);
	}

	/**
	 * getCustomerList
	 * 
	 * @author: HieuNQ
	 * @return: void
	 * @throws:
	 */
	private void getAccList() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));

		ActionEvent e = new ActionEvent();
		Bundle b = new Bundle();
		b.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		b.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		e.viewData = b;
		e.action = ActionEventConstant.ACC_SALE_PROG_REPORT;
		e.sender = this;
		SuperviorController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.ACC_SALE_PROG_REPORT:
			dto = (AccSaleProgReportDTO) modelEvent.getModelData();
			// <HaiTC> calculated remain total and perSoldTotal
			if (dto != null) {
				dto.remainTotal = dto.planTotal > dto.soldTotal ? (dto.planTotal - dto.soldTotal)
						: 0;
				dto.perSoldTotal = dto.planTotal > 0 ? dto.soldTotal * 100
						/ dto.planTotal : 100.0;
				if (dto.planTotal <= 0 && dto.soldTotal == 0) {
					dto.perSoldTotal = 0.00;
				}
			}
			// </HaiTC>
			renderLayout();
			parent.closeProgressDialog();
			this.isDoneLoadFirst = true;
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.ACC_SALE_PROG_REPORT:
			parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * 
	 * render layout
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	private void renderLayout() {
		tvPlanDate.setText(String.valueOf(dto.monthSalePlan));
		tvSoldDate.setText(String.valueOf(dto.soldSalePlan));
		tvProgress.setText(String.valueOf((int) dto.perSalePlan) + "%");
		tbList.clearAllData();
		for (AccSaleProgReportItem item : dto.arrList) {
			AccSaleProgReportRow row = new AccSaleProgReportRow(parent, false);
			row.render(item, (int) dto.perSalePlan > (int) item.percent);
			row.setListener(this);
			tbList.addRow(row);
		}
		AccSaleProgReportRow row = new AccSaleProgReportRow(parent, true);
		// HaiTC update perSoldTotal, boolean check
		row.renderSum(dto.planTotal, dto.soldTotal, dto.remainTotal,
				dto.perSoldTotal,
				(int) dto.perSalePlan > (int) dto.perSoldTotal);
		tbList.addRow(row);
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
			case ACTION_ACC:

				break;
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
			case ACTION_PSDS:
				gotoReportCustomerNotPSDSInMonth();
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 
	 * go to report customer not PSDS in month
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	private void gotoReportCustomerNotPSDSInMonth() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.ACTION_REPORT_CUSTOMER_NOT_PSDS_IN_MONTH;
		SuperviorController.getInstance().handleSwitchFragment(e);
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {

	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		AccSaleProgReportItem item = (AccSaleProgReportItem) data;

		Bundle b = new Bundle();
		b.putString(IntentConstants.INTENT_STAFF_CODE, item.staffCode);
		b.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(item.staffId));
		b.putString(IntentConstants.INTENT_STAFF_PHONE, item.mobile);
		b.putString(IntentConstants.INTENT_STAFF_NAME,
				String.valueOf(item.staffName));
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.STAFF_INFORMATION;
		e.sender = this;
		e.viewData = b;
		SuperviorController.getInstance().handleSwitchFragment(e);
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
