/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableRow;

import com.viettel.dms.dto.view.WrongPlanCustomerDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * Wrong Customer List View
 * WrongCustomerListView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:31:42 14 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class WrongCustomerListView extends ScrollView implements OnClickListener {

	private SupervisorActivity parent;
	public View viewLayout;
	private Button btClose;
	//private WrongPlanCustomerDTO dto;
	private GSBHRouteSupervisionView fragParent;
	List<TableRow> listRows = new ArrayList<TableRow>();
	private VinamilkTableView tbWrongList;

	public WrongCustomerListView(Context context, WrongPlanCustomerDTO dto) {
		super(context);
		parent = (SupervisorActivity) context;
		fragParent = (GSBHRouteSupervisionView) parent.getFragmentManager().findFragmentByTag(
				GSBHRouteSupervisionView.TAG);
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_wrong_cus_list_view, null);

		tbWrongList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCusFeedBack);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		//this.dto = dto;
		renderLayout(dto);
	}

	private void renderLayout(WrongPlanCustomerDTO dto) {
		if (dto.arrWrong.size() > 0) {
			for (int i = 0, s = dto.arrWrong.size(); i < s; i++) {
				WrongCustomerListRow row = new WrongCustomerListRow(parent);
				row.renderLayout(dto.arrWrong.get(i));
				listRows.add(row);
			}
			// customer list table
			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 170, 220, 140, 80, 110 };
			String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_CUSTOMER),
					StringUtil.getString(R.string.TEXT_ADDRESS), StringUtil.getString(R.string.LINE1),
					StringUtil.getString(R.string.TEXT_TIME), StringUtil.getString(R.string.TEXT_SALES_2) };

			tbWrongList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbWrongList.addContent(listRows);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btClose) {
			fragParent.onClick(v);
		}
	}

}
