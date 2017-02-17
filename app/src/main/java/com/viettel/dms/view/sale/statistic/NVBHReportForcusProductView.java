/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.ForcusProductOfNVBHDTO;
import com.viettel.dms.dto.view.NVBHReportForcusProductInfoViewDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * Bao cao mat hang trong tam trong module thong ke chung cua NVBH
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class NVBHReportForcusProductView extends BaseFragment {
	// tag for fragment
	public static final String TAG = NVBHReportForcusProductView.class.getName();

	// khach hang chua phat sinh doanh so
	private static final int ACTION_MENU_NEED_DONE = 3;
	// mat hang trong tam
	private static final int ACTION_MENU_MHTT = 2;
	// thong ke chung
	private static final int ACTION_MENU_GENERAL_STATISTICS = 1;

	// current activity
	private SalesPersonActivity parent;
	// number day sale plan
	private TextView tvNumDaySalePlan;
	// num day sold
	private TextView tvNumDaySold;
	// percent sold
	private TextView tvSoldPercent;
	// layout header of table
	private LinearLayout llHeader;
	// table list product
	private VinamilkTableView tbProductList;
	// flag check load data the first
	public boolean isDoneLoadFirst = false;
	// data for screen
	public NVBHReportForcusProductInfoViewDTO screenData = new NVBHReportForcusProductInfoViewDTO();

	/**
	 * 
	 * get instance
	 * 
	 * @param data
	 * @return
	 * @return: ReportForcusProductView
	 * @throws:
	 * @author: HaiTC3
	 * @date: Dec 28, 2012
	 */
	public static NVBHReportForcusProductView getInstance(Bundle data) {
		NVBHReportForcusProductView instance = new NVBHReportForcusProductView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onAttach(Activity activity) {
		
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) {
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_report_forcus_product_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initView(v);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_REPORT_FORCUS_PRODUCT));
		this.initHeadermenu();
		if (!this.isDoneLoadFirst) {
			this.getReportForcusProductInfo();
		}
		return v;
	}

	/**
	 * 
	 * init control of view
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	private void initView(View v) {
		tvNumDaySalePlan = (TextView) v.findViewById(R.id.tvNumDaySalePlan);
		tvNumDaySold = (TextView) v.findViewById(R.id.tvNumDaySold);
		tvSoldPercent = (TextView) v.findViewById(R.id.tvSoldPercent);
		llHeader = (LinearLayout) v.findViewById(R.id.llHeader);
		llHeader.setVisibility(View.INVISIBLE);
		tbProductList = (VinamilkTableView) v.findViewById(R.id.tbProductList);
		tbProductList.setVisibility(View.INVISIBLE);
	}

	@Override
	public void onResume() {
		if (this.isDoneLoadFirst) {
			renderLayout();
		}
		super.onResume();
	}

	/**
	 * 
	 * get report forcus product info
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void getReportForcusProductInfo() {
		parent.showProgressDialog(StringUtil.getString(R.string.loading));
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		data.putString(IntentConstants.INTENT_SALE_TYPE_CODE,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().saleTypeCode));
		e.sender = this;
		e.action = ActionEventConstant.NVBH_GET_REPORT_FORCUS_PRODUCT_INFO_VIEW;
		e.viewData = data;
		UserController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * render layout for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void renderLayout() {
		if (screenData != null) {
			llHeader.setVisibility(View.VISIBLE);
			tbProductList.setVisibility(View.VISIBLE);
			tvNumDaySalePlan.setText(String.valueOf(screenData.numberDayPlan));
			tvNumDaySold.setText(String.valueOf(screenData.numberDaySold));
			tvSoldPercent.setText(StringUtil.decimalFormatSymbols("#.##", screenData.progressSold)
					+ StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));

			// render table
			if (this.screenData.listForcusProduct != null && this.screenData.listForcusProduct.size() > 0) {
				tbProductList.clearAllData();
				tbProductList.hideNoContentRow();
				// add list row nomal
				int pos=1;
				for (ForcusProductOfNVBHDTO forcusProductOfNVBHDTO : screenData.listForcusProduct) {
					NVBHReportForcusProductRow row = new NVBHReportForcusProductRow(parent, tbProductList, false);
					row.setClickable(true);
					// update number order for product on view
					row.renderLayoutForRowProductInfo(forcusProductOfNVBHDTO, pos, screenData.progressSold);
					pos++;
					tbProductList.addRow(row);
				}

				// add list row total
				for (ForcusProductOfNVBHDTO productOfNVBHDTO: screenData.listTotalForcusProduct) {
					NVBHReportForcusProductRow row = new NVBHReportForcusProductRow(parent, tbProductList, true);
					row.setClickable(true);
					// update number order for product on view
					row.renderLayoutForRowTotal(productOfNVBHDTO, screenData.progressSold);
					tbProductList.addRow(row);
				}
			} else {
				tbProductList.showNoContentRow();
			}
		} else {
			renderHeaderTable();
		}
	}

	/**
	 * 
	 * hien thi header khi chuong trinh xay ra loi xu ly SQL
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 * @since: Feb 4, 2013
	 */
	public void renderHeaderTable() {
		llHeader.setVisibility(View.VISIBLE);
		tbProductList.setVisibility(View.VISIBLE);
		tbProductList.showNoContentRow();
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				this.getReportForcusProductInfo();
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/**
	 * 
	 * init header for menu
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	private void initHeadermenu() {
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_NEED_DONE), R.drawable.icon_order,
				ACTION_MENU_NEED_DONE);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_MHTT), R.drawable.icon_list_star, ACTION_MENU_MHTT);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_GENERAL_STATISTICS), R.drawable.icon_reminders,
				ACTION_MENU_GENERAL_STATISTICS);
		setMenuItemFocus(2);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		
		ActionEvent e = new ActionEvent();
		switch (eventType) {
		case ACTION_MENU_GENERAL_STATISTICS:
			e.action = ActionEventConstant.GO_TO_GENERAL_STATISTICS;
			e.sender = this;
			e.viewData = new Bundle();
			UserController.getInstance().handleSwitchFragment(e);
			break;
		case ACTION_MENU_NEED_DONE:
			e.action = ActionEventConstant.GO_TO_NVBH_NEED_DONE_VIEW;
			e.viewData = new Bundle();
			e.sender = this;
			UserController.getInstance().handleSwitchFragment(e);
			break;
		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.NVBH_GET_REPORT_FORCUS_PRODUCT_INFO_VIEW:
			this.screenData = (NVBHReportForcusProductInfoViewDTO) modelEvent.getModelData();
			this.isDoneLoadFirst = true;
			this.renderLayout();
			parent.closeProgressDialog();
			break;

		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		
		ActionEvent event = modelEvent.getActionEvent();
		switch (event.action) {
		case ActionEventConstant.NVBH_GET_REPORT_FORCUS_PRODUCT_INFO_VIEW:
			renderHeaderTable();
			parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}
}
