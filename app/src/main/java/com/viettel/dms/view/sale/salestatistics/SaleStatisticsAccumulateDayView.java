/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.salestatistics;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.SaleStatisticsAccumulateDayDTO;
import com.viettel.dms.dto.view.SaleStatisticsAccumulateDayDTO.SaleStatisticsAccumulateDayItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * man hinh thong ke don tong luy ke ngay
 * 
 * @author: HieuNH
 * @version: 1.0
 * @since: 1.1
 */
public class SaleStatisticsAccumulateDayView extends BaseFragment implements
		OnItemSelectedListener, VinamilkTableListener {
	public static final String TAG = SaleStatisticsAccumulateDayView.class
			.getName();
	private static int[] CUSTOMER_NOT_PSDS_IN_MONTH_SALE_TABLE_WIDTHS = { 45,
			90, 210, 55, 140, 140, 140, 127 };
	private static String[] CUSTOMER_NOT_PSDS_IN_MONTH_SALE_TABLE_TITLES = {
			StringUtil.getString(R.string.TEXT_STT),
			StringUtil.getString(R.string.TEXT_HEADER_PRODUCT_CODE),
			StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
			StringUtil.getString(R.string.TEXT_HEADER_PRODUCT_INDUSTRY),
			StringUtil.getString(R.string.TEXT_HEADER_TABLE_PLAN) + " (x1000)",
			StringUtil.getString(R.string.TEXT_HEADER_TABLE_DONE) + " (x1000)",
			StringUtil.getString(R.string.TEXT_HEADER_TABLE_REMAIN)
					+ " (x1000)",
			StringUtil.getString(R.string.TEXT_HEADER_TABLE_PROGRESS) };
	// show report sale statistics in day
	private static final int ACTION_MENU_SALE_STATISTICS_IN_DAY = 2;
	// show report sale statistics in month
	private static final int ACTION_MENU_SALE_STATISTICS_IN_MONTH = 1;
	// list product industry
	Spinner spIndustryProduct;
	// edit text product code
	VNMEditTextClearable etInputProductCode;
	// edit text product name
	VNMEditTextClearable etInputProductName;
	// image button search
	Button ibtSearch;
	// table list product
	VinamilkTableView tbProductList;
	// sold data
	SaleStatisticsAccumulateDayDTO saleData = new SaleStatisticsAccumulateDayDTO();
	private SalesPersonActivity parent;
	private int currentSelected;
	private boolean isCount = false;// co goi request count product hay kg
	private int countCustomer;

	/**
	 * 
	 * get instance for object
	 * 
	 * @param data
	 * @return
	 * @return: SaleStatisticsAccumulateDayView
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 19, 2012
	 */
	public static SaleStatisticsAccumulateDayView getInstance(Bundle data) {
		SaleStatisticsAccumulateDayView instance = new SaleStatisticsAccumulateDayView();
		instance.setArguments(data);
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_sale_statistics_accumulate_day_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.initView(v);
		this.setTitleHeaderView(StringUtil
				.getString(R.string.TITLE_SALE_STATISTICS_ACCUMULATE_DAY_VIEW)
				+ new SimpleDateFormat(" dd/MM/yyyy").format(new Date()));
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH),
				R.drawable.icon_accumulated,
				ACTION_MENU_SALE_STATISTICS_IN_MONTH);
		addMenuItem(
				StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE),
				R.drawable.icon_calendar, ACTION_MENU_SALE_STATISTICS_IN_DAY);
		setMenuItemFocus(ACTION_MENU_SALE_STATISTICS_IN_MONTH);
		this.requestGetAccumulateDay();
		return v;
	}

	/**
	 * 
	 * init controls for view
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 19, 2012
	 */
	public void initView(View v) {
		spIndustryProduct = (Spinner) v.findViewById(R.id.spIndustryProduct);
		spIndustryProduct.setOnItemSelectedListener(this);
		etInputProductCode = (VNMEditTextClearable) v
				.findViewById(R.id.etInputProductCode);
		etInputProductName = (VNMEditTextClearable) v
				.findViewById(R.id.etInputProductName);
		ibtSearch = (Button) v.findViewById(R.id.ibtSearch);
		ibtSearch.setOnClickListener(this);
		tbProductList = (VinamilkTableView) v.findViewById(R.id.tbProductList);
		tbProductList.getHeaderView().addColumns(
				CUSTOMER_NOT_PSDS_IN_MONTH_SALE_TABLE_WIDTHS,
				CUSTOMER_NOT_PSDS_IN_MONTH_SALE_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		tbProductList.setListener(this);
	}

	/**
	 * 
	 * get list product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 20, 2012
	 */
	public void requestGetAccumulateDay() {
		if (!parent.isShowProgressDialog()) {
			parent.showProgressDialog(getString(R.string.loading));
		}
		isCount = false;
		// send request get list vote product
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);
		data.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		String page = " limit " + (0) + "," + Constants.NUM_ITEM_PER_PAGE;
		data.putString(IntentConstants.INTENT_PAGE, page);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_PRODUCT_SALE_STATISTICS_ACCUMULATE_DAY;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * get count list product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 20, 2012
	 */
	public void requestCountListProductSold() {
		if (!parent.isShowProgressDialog()) {
			parent.showProgressDialog(getString(R.string.loading));
		}
		isCount = true;
		// send request get list vote product
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String productCode = StringUtil
				.getEngStringFromUnicodeString(etInputProductCode.getText()
						.toString());
		String productName = StringUtil
				.getEngStringFromUnicodeString(etInputProductName.getText()
						.toString());
		String industryProduct = "";

		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);

		if (this.saleData != null && this.saleData.listIndustry != null
				&& this.saleData.listIndustry.size() > this.currentSelected) {
			industryProduct = this.saleData.listIndustry
					.get(this.currentSelected);
		}

		data.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		if (!StringUtil.isNullOrEmpty(productCode)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE,
					productCode.trim());
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME,
					productName.trim());
		}
		if (!StringUtil.isNullOrEmpty(industryProduct)
				&& !"Tất cả".equals(industryProduct)) {
			data.putString(IntentConstants.INTENT_INDUSTRY,
					industryProduct.trim());
		}
		// String page;
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_COUNT_SALE_STATISTICS_ACCUMULATE_DAY_LIST_PRODUCT;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * get list product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 20, 2012
	 */
	public void requestListProductSold() {
		if (!parent.isShowProgressDialog()) {
			parent.showProgressDialog(getString(R.string.loading));
		}
		// send request get list vote product
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String productCode = StringUtil
				.getEngStringFromUnicodeString(etInputProductCode.getText()
						.toString());
		String productName = StringUtil
				.getEngStringFromUnicodeString(etInputProductName.getText()
						.toString());
		String industryProduct = "";

		data.putString(IntentConstants.INTENT_SHOP_ID, GlobalInfo.getInstance()
				.getProfile().getUserData().shopId);

		if (this.saleData != null && this.saleData.listIndustry != null
				&& this.saleData.listIndustry.size() > this.currentSelected) {
			industryProduct = this.saleData.listIndustry
					.get(this.currentSelected);
		}

		data.putString(
				IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile()
						.getUserData().id));
		if (!StringUtil.isNullOrEmpty(productCode)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE,
					productCode.trim());
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME,
					productName.trim());
		}
		if (!StringUtil.isNullOrEmpty(industryProduct)
				&& !"Tất cả".equals(industryProduct)) {
			data.putString(IntentConstants.INTENT_INDUSTRY,
					industryProduct.trim());
		}
		String page;
		if (isCount) {
			page = " limit " + (0) + "," + Constants.NUM_ITEM_PER_PAGE;
		} else {
			page = " limit "
					+ ((tbProductList.getPagingControl().getCurrentPage() - 1) * Constants.NUM_ITEM_PER_PAGE)
					+ "," + Constants.NUM_ITEM_PER_PAGE;
		}
		data.putString(IntentConstants.INTENT_PAGE, page);
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_SALE_STATISTICS_ACCUMULATE_DAY_LIST_PRODUCT;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * init list industry product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 20, 2012
	 */
	public void renderListIndustryProduct() {
		if (saleData != null && saleData.arrList != null) {
			if (this.saleData.listIndustry.size() > 1) {
				this.saleData.listIndustry.add(0, "Tất cả");
			}
			SpinnerAdapter adapterLine = new SpinnerAdapter(parent,
					R.layout.simple_spinner_item,
					this.saleData.listIndustry
							.toArray(new String[this.saleData.listIndustry
									.size()]));
			// this.isFirstLoadProduct = true;
			this.spIndustryProduct.setAdapter(adapterLine);
		}
	}

	/**
	 * 
	 * render layout for screen
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 19, 2012
	 */
	public void renderLayout() {
		if (isCount) {
			isCount = false;
			tbProductList.getPagingControl().setCurrentPage(1);
			tbProductList.getPagingControl().setTotalPage(-1);
		}
		int pos = 1 + Constants.NUM_ITEM_PER_PAGE
				* (tbProductList.getPagingControl().getCurrentPage() - 1);
		if (this.saleData.arrList.size() > 0) {
			tbProductList.clearAllData();
			for (int i = 0, size = this.saleData.arrList.size(); i < size; i++) {
				SaleStatisticsAccumulateDayItem data = this.saleData.arrList
						.get(i);
				SaleStatisticsAccumulateDayRow row = new SaleStatisticsAccumulateDayRow(
						parent);
				row.render(pos, data);
				pos++;
				tbProductList.addRow(row);
			}
		} else {
			tbProductList.showNoContentRow();
		}

		if (tbProductList.getPagingControl().totalPage < 0) {
			tbProductList.setTotalSize(this.saleData.totalList);
		}
		parent.closeProgressDialog();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == ibtSearch) {
			this.requestCountListProductSold();
		}
		super.onClick(v);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onAttach(android.app.Activity
	 * )
	 */
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_PRODUCT_SALE_STATISTICS_ACCUMULATE_DAY:
			this.saleData.listIndustry = (ArrayList<String>) modelEvent
					.getModelData();
			renderListIndustryProduct();
			requestCountListProductSold();
			break;
		case ActionEventConstant.GET_COUNT_SALE_STATISTICS_ACCUMULATE_DAY_LIST_PRODUCT:
			countCustomer = (Integer) modelEvent.getModelData();
			requestListProductSold();
			break;
		case ActionEventConstant.GET_SALE_STATISTICS_ACCUMULATE_DAY_LIST_PRODUCT:
			this.saleData.arrList = ((SaleStatisticsAccumulateDayDTO) modelEvent
					.getModelData()).arrList;
			this.saleData.totalList = countCustomer;
			renderLayout();
			break;
		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleErrorModelViewEvent
	 * (com.viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelViewEvent(modelEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onDestroyView()
	 */
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onItemSelected(android
	 * .widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		if (currentSelected != arg2) {
			currentSelected = arg2;
			this.requestCountListProductSold();
		}
	}

	/**
	 * 
	 * clear all data in input text
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 20, 2012
	 */
	public void clearAllData() {
		this.etInputProductCode.setText(Constants.STR_BLANK);
		this.etInputProductName.setText(Constants.STR_BLANK);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.widget.AdapterView.OnItemSelectedListener#onNothingSelected(android
	 * .widget.AdapterView)
	 */
	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onEvent(int,
	 * android.view.View, java.lang.Object)
	 */
	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case ACTION_MENU_SALE_STATISTICS_IN_DAY:
			this.gotoSaleStatisticsProductListInDay();
			break;

		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	/**
	 * 
	 * display sale statistics product list in day
	 * 
	 * @return: void
	 * @throws:
	 * @author: HieuNH
	 * @date: Oct 22, 2012
	 */
	private void gotoSaleStatisticsProductListInDay() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_SALE_STATISTICS_PRODUCT_VIEW_IN_DAY;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				etInputProductCode.setText("");
				etInputProductName.setText("");
				currentSelected = 0;
				spIndustryProduct.setSelection(currentSelected);
				this.requestGetAccumulateDay();
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableloadMore(android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		// TODO Auto-generated method stub
		// load more data for table product list
		if (control == tbProductList) {
			requestListProductSold();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.listener.VinamilkTableListener#
	 * handleVinamilkTableRowEvent(int, android.view.View, java.lang.Object)
	 */
	@Override
	public void handleVinamilkTableRowEvent(int action, View control,
			Object data) {
		// TODO Auto-generated method stub

	}
}
