/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.depot;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.dto.view.ListFindProductOrderListDTO;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * 
 * statistics total proudcts view
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class StatisticsTotalProductsView extends BaseFragment implements
		OnClickListener {

	public static final String TAG = StatisticsTotalProductsView.class
			.getName();

	// input product code
	EditText etInputProductCode;

	// input product name
	EditText etInputProductName;

	// button select industry to find product
	Spinner spSelectIndustry;

	// button select industry children to find product
	Spinner spSelectIndustryChildren;

	// button search product
	ImageButton ibtSearch;

	// text view display number product above table list product
	TextView tvNumberProduct;

	// vinamilk table product list
	VinamilkTableView tbStatisticsTotalProducts;

	private SalesPersonActivity parent;

	// list product
	ListFindProductOrderListDTO listProduct;

	/**
	 * 
	 * init instance for class
	 * 
	 * @author: HaiTC3
	 * @since: 10:38:05 AM | Jun 14, 2012
	 * @return
	 * @return: StatisticsTotalProductsView
	 * @throws:
	 */
	public static StatisticsTotalProductsView getInstance() {

		StatisticsTotalProductsView instance = new StatisticsTotalProductsView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onActivityCreated(android
	 * .os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
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
		
		super.onAttach(activity);
		try {
			parent = (SalesPersonActivity) activity;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		
		super.onDestroy();
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
		
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_statistics_total_products_view, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initView(v);
		setTitleHeaderView(getString(R.string.TEXT_HEADER_TITLE_STATISTICS_TOTAL_PRODUCTS));
		return v;
	}

	/**
	 * 
	 * init control for screen
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		// table list product
		tbStatisticsTotalProducts = (VinamilkTableView) v
				.findViewById(R.id.tbStatisticsTotalProducts);
		// text view number product info
		tvNumberProduct = (TextView) v.findViewById(R.id.tvNumberProduct);
		// button search
		ibtSearch = (ImageButton) v.findViewById(R.id.ibtSearch);
		ibtSearch.setOnClickListener(this);
		// button select industry
		spSelectIndustry = (Spinner) v.findViewById(R.id.spSelectIndustry);
		// button seleect industry children
		spSelectIndustryChildren = (Spinner) v
				.findViewById(R.id.spSelectIndustryChildren);
		renderLayout();

	}

	/**
	 * 
	 * render layout for screen when we have data
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		int pos = 1;
		tbStatisticsTotalProducts.clearAllData();
		for (int i = 0; i < 5; i++) {
			StatisticsTotalProductsRow row = new StatisticsTotalProductsRow(
					parent, null);
			row.setClickable(true);
			row.setTag(Integer.valueOf(pos));
			row.renderLayout(i, null);
			row.setListener(parent);
			row.setOnClickListener(this);
			tbStatisticsTotalProducts.addRow(row);
		}

		// add end row
		StatisticsTotalProductsEndRow row = new StatisticsTotalProductsEndRow(
				parent, null);
		row.setClickable(true);
		row.setTag(Integer.valueOf(pos));
		row.renderLayout(1, null);
		row.setListener(parent);
		row.setOnClickListener(this);
		tbStatisticsTotalProducts.addRow(row);

		tbStatisticsTotalProducts.getHeaderView().addColumns(
				TableDefineContanst.STATISTICS_TOTAL_PRODUCTS_TABLE_WIDTHS,
				TableDefineContanst.STATISTICS_TOTAL_PRODUCTS_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));

		tbStatisticsTotalProducts.setTotalSize(50);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		super.handleModelViewEvent(modelEvent);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == ibtSearch) {
			searchStatistics();
		}
	}

	/**
	 * 
	 * search statistics
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void searchStatistics() {
		// to do search
	}
}
