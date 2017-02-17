/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.salestatistics;

import java.util.ArrayList;

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
import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.controller.UserController;
import com.viettel.dms.dto.view.SaleProductInfoDTO;
import com.viettel.dms.dto.view.SaleStatisticsProductInDayInfoViewDTO;
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
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * man hinh thong ke don tong trong ngay Pre Sales
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class SaleStatisticsInDayPreSalesView extends BaseFragment implements OnItemSelectedListener {
	public static final String TAG = SaleStatisticsInDayPreSalesView.class.getName();
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
	// table list product
	VinamilkTableView tbProductList;
	// flag the first load data
	boolean isDoneLoadFirst = false;
	// image button search
	Button btSearch;
	// sold data
	SaleStatisticsProductInDayInfoViewDTO saleData = new SaleStatisticsProductInDayInfoViewDTO();
	// parent activity
	private SalesPersonActivity parent;
	// index selected
	private int currentSelected = -1;

	/**
	 * 
	 * get instance for object
	 * 
	 * @param data
	 * @return
	 * @return: SaleStatisticsInDayPreSalesView
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 19, 2012
	 */
	public static SaleStatisticsInDayPreSalesView getInstance(Bundle data) {
		SaleStatisticsInDayPreSalesView instance = new SaleStatisticsInDayPreSalesView();
		instance.setArguments(data);
		return instance;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_sale_statistics_in_day_pre_sales_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		this.initView(v);
		this.setTitleHeaderView(StringUtil.getString(R.string.TITLE_SALE_STATISTICS_IN_DAY_VIEW));
		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_MONTH), R.drawable.icon_accumulated,
				ACTION_MENU_SALE_STATISTICS_IN_MONTH);
		addMenuItem(StringUtil.getString(R.string.TEXT_HEADER_MENU_REPORT_DATE), R.drawable.icon_calendar,
				ACTION_MENU_SALE_STATISTICS_IN_DAY);
		setMenuItemFocus(ACTION_MENU_SALE_STATISTICS_IN_DAY);
		if (!this.isDoneLoadFirst) {
			this.requestGetSaleStatisticsInfo();
		}
		return v;
	}

	/**
	 * 
	 * init controls for view
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 19, 2012
	 */
	public void initView(View v) {
		spIndustryProduct = (Spinner) v.findViewById(R.id.spIndustryProduct);
		spIndustryProduct.setOnItemSelectedListener(this);
		etInputProductCode = (VNMEditTextClearable) v.findViewById(R.id.etInputProductCode);
		etInputProductName = (VNMEditTextClearable) v.findViewById(R.id.etInputProductName);
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		tbProductList = (VinamilkTableView) v.findViewById(R.id.tbProductList);
	}

	/**
	 * 
	 * get all sku sale in day
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 19, 2012
	 */
	public void requestGetSaleStatisticsInfo() {
		parent.showProgressDialog(getString(R.string.loading));

		// send request get list vote product
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		data.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		data.putString(IntentConstants.INTENT_STAFF_TYPE,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().chanelObjectType));
		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_INDUSTRY_PRODUCT_AND_LIST_PRODUCT;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * get list product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public void requestListProductSold() {
		parent.showProgressDialog(getString(R.string.loading));

		// send request get list vote product
		ActionEvent e = new ActionEvent();
		Bundle data = new Bundle();
		String productCode = etInputProductCode.getText().toString();
		String productName = etInputProductName.getText().toString();
		String industryProduct = Constants.STR_BLANK;
		if (this.saleData != null && this.saleData.listIndustry != null
				&& this.saleData.listIndustry.size() >= this.currentSelected) {
			if (currentSelected > 0) {
				industryProduct = this.saleData.listIndustry.get(this.currentSelected);

			}
		}

		data.putString(IntentConstants.INTENT_STAFF_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		data.putString(IntentConstants.INTENT_SHOP_ID,
				String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().shopId));
		if (!StringUtil.isNullOrEmpty(productCode)) {
			data.putString(IntentConstants.INTENT_PRODUCT_CODE, productCode.trim());
		}
		if (!StringUtil.isNullOrEmpty(productName)) {
			data.putString(IntentConstants.INTENT_PRODUCT_NAME,
					StringUtil.getEngStringFromUnicodeString(productName.trim()));
		}
		if (!StringUtil.isNullOrEmpty(industryProduct)) {
			data.putString(IntentConstants.INTENT_INDUSTRY, industryProduct.trim());
		}

		e.viewData = data;
		e.sender = this;
		e.action = ActionEventConstant.GET_LIST_PRODUCT_PRE_SALE_SOLD;
		e.isNeedCheckTimeServer = false;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * 
	 * init list industry product
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public void renderListIndustryProduct() {
		if (saleData != null && saleData.listIndustry != null) {
			String[] arrPromotion = new String[this.saleData.listIndustry.size()];
			for (int i = 0, size = this.saleData.listIndustry.size(); i < size; i++) {
				arrPromotion[i] = this.saleData.listIndustry.get(i);
			}
			SpinnerAdapter adapterLine = new SpinnerAdapter(parent, R.layout.simple_spinner_item, arrPromotion);
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
	 * @author: HaiTC3
	 * @date: Oct 19, 2012
	 */
	public void renderLayout() {
		int pos = 1;
		if (this.saleData.listProduct.size() > 0) {
			tbProductList.clearAllData();
			for (int i = 0, size = this.saleData.listProduct.size(); i < size; i++) {
				SaleProductInfoDTO data = this.saleData.listProduct.get(i);
				SaleStatisticsInDayPreSalesRow row = new SaleStatisticsInDayPreSalesRow(parent, tbProductList);
				row.setClickable(true);
				row.renderLayout(pos, data);
				pos++;
				tbProductList.addRow(row);
			}
		} else {
			tbProductList.showNoContentRow();
		}

		if (!isDoneLoadFirst) {

			tbProductList.getHeaderView().addColumns(TableDefineContanst.SALE_STATISTICS_IN_DAY_TABLE_WIDTHS,
					TableDefineContanst.SALE_STATISTICS_IN_DAY_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		}
	}

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
		case ActionEventConstant.GET_LIST_INDUSTRY_PRODUCT_AND_LIST_PRODUCT:
			this.saleData = (SaleStatisticsProductInDayInfoViewDTO) modelEvent.getModelData();
			if (saleData != null && saleData.listIndustry != null) {
				// add item "tat ca" len top
				if (saleData.listIndustry.size() > 0) {
					this.saleData.listIndustry.add(0, StringUtil.getString(R.string.ALL));
				} else {
					this.saleData.listIndustry.add(StringUtil.getString(R.string.ALL));
				}
				this.renderListIndustryProduct();
				this.renderLayout();
				this.currentSelected = 0;
				this.isDoneLoadFirst = true;
			}
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_LIST_PRODUCT_PRE_SALE_SOLD:
			this.saleData.listProduct = (ArrayList<SaleProductInfoDTO>) modelEvent.getModelData();
			if (this.saleData.listProduct != null) {
				this.renderLayout();
			}
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
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_LIST_PRODUCT_PRE_SALE_SOLD:
			this.parent.closeProgressDialog();
			break;
		case ActionEventConstant.GET_LIST_INDUSTRY_PRODUCT_AND_LIST_PRODUCT:
			this.parent.closeProgressDialog();
			break;
		default:
			super.handleErrorModelViewEvent(modelEvent);
			break;
		}
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case ACTION_MENU_SALE_STATISTICS_IN_MONTH:
			this.gotoSaleStatisticsProductListInMonth();
			break;

		default:
			super.onEvent(eventType, control, data);
			break;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		if (currentSelected != arg2 && this.isDoneLoadFirst) {
			currentSelected = arg2;
			etInputProductCode.setText(Constants.STR_BLANK);
			etInputProductName.setText(Constants.STR_BLANK);
			this.requestListProductSold();
		}
	}

	/**
	 * 
	 * clear all data in input text
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 20, 2012
	 */
	public void clearAllData() {
		this.etInputProductCode.setText(Constants.STR_BLANK);
		this.etInputProductName.setText(Constants.STR_BLANK);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btSearch) {
			this.requestListProductSold();
		} else {
			super.onClick(v);
		}
	}

	/**
	 * 
	 * display sale statistics in month
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 22, 2012
	 */
	private void gotoSaleStatisticsProductListInMonth() {
		ActionEvent e = new ActionEvent();
		e.sender = this;
		e.viewData = new Bundle();
		e.action = ActionEventConstant.GO_TO_SALE_STATISTICS_PRODUCT_VIEW_IN_MONTH;
		UserController.getInstance().handleSwitchFragment(e);
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		// TODO Auto-generated method stub
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				if (etInputProductCode != null) {
					etInputProductCode.setText(Constants.STR_BLANK);
				}
				if (etInputProductName != null) {
					etInputProductName.setText(Constants.STR_BLANK);
				}
				if (saleData != null && saleData.listIndustry != null && saleData.listIndustry.size() > 0) {
					this.spIndustryProduct.setSelection(0);
				}
				this.currentSelected = 0;
				this.requestListProductSold();
			}
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
