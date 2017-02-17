/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.indebtedness;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.viettel.dms.constants.TableDefineContanst;
import com.viettel.dms.dto.view.ListFindProductOrderListDTO;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * 
 * indebtedness list view
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class IndebtednessListView extends BaseFragment implements
		OnClickListener {

	public static final String TAG = IndebtednessListView.class.getName();

	// edit text input customer code
	EditText etInputCustomerCode;
	// edit text input customer name
	EditText etInputCustomerName;
	// spinner select status
	Spinner spSelectStatus;
	// button search
	Button btSearch;

	// vinamilk table product list
	VinamilkTableView tbIndebtednessList;

	private SalesPersonActivity parent;

	// list product
	ListFindProductOrderListDTO listProduct;

	/**
	 * 
	 * init instance for indebtedness list view
	 * 
	 * @author: HaiTC3
	 * @return
	 * @return: IndebtednessListView
	 * @throws:
	 */
	public static IndebtednessListView getInstance() {

		IndebtednessListView instance = new IndebtednessListView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		instance.setArguments(args);

		return instance;
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
	 * com.viettel.vinamilk.view.main.BaseFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_indebtedness_list_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initView(v);
		setTitleHeaderView(getString(R.string.TEXT_HEADER_TITLE_INDEBTEDNESS));
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
		tbIndebtednessList = (VinamilkTableView) v
				.findViewById(R.id.tbIndebtednessList);
		// text view number product info
		this.etInputCustomerCode = (EditText) v
				.findViewById(R.id.etInputCustomerCode);
		this.etInputCustomerName = (EditText) v
				.findViewById(R.id.etInputCustomerName);
		this.spSelectStatus = (Spinner) v.findViewById(R.id.spSelectStatus);
		// button search
		btSearch = (Button) v.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		renderLayout();

	}

	/**
	 * 
	 * init layout for screen
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	private void renderLayout() {
		int pos = 1;
		for (int i = 0; i < 10; i++) {
			IndebtednessInfoRow row = new IndebtednessInfoRow(parent, null);
			row.setClickable(true);
			row.setTag(pos);
			row.renderLayout(i, null);
			row.setListener(parent);
			row.setOnClickListener(this);
			tbIndebtednessList.addRow(row);
		}

		tbIndebtednessList.getHeaderView().addColumns(
				TableDefineContanst.INDEBTEDNESS_TABLE_WIDTHS,
				TableDefineContanst.INDEBTEDNESS_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));

		tbIndebtednessList.setTotalSize(50);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleModelViewEvent(com.
	 * viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleModelViewEvent(modelEvent);
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
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#enableMenuBar(com.viettel
	 * .vinamilk.view.listener.OnEventControlListener)
	 */
	@Override
	public void enableMenuBar(OnEventControlListener listener) {
		// TODO Auto-generated method stub
		super.enableMenuBar(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btSearch) {
			searchIndebtedness();
		}
	}

	/**
	 * 
	 * search indebtedness
	 * 
	 * @author: HaiTC3
	 * @return: void
	 * @throws:
	 */
	public void searchIndebtedness() {
		// to do search
	}

}
