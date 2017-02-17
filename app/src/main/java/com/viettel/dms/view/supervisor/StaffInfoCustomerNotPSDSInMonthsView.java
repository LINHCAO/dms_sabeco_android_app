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
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerListDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint("ViewConstructor")
public class StaffInfoCustomerNotPSDSInMonthsView extends ScrollView implements OnClickListener, VinamilkTableListener {
	public SupervisorActivity parent;
	public StaffInformationView fragParent;
	public View viewLayout;
	public VinamilkTableView tbList;
	public Button btClose;
	public TextView tvTitle;
	public CustomerListDTO dto;

	/**
	 * @param context
	 */
	public StaffInfoCustomerNotPSDSInMonthsView(Context context, CustomerListDTO dto, String month) {
		super(context);
		parent = (SupervisorActivity) context;
		fragParent = (StaffInformationView) parent.getFragmentManager().findFragmentByTag(StaffInformationView.TAG);
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_cus_not_psds_in_3_months, null);
		tbList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCus);
		tbList.setListener(this);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		tvTitle = (TextView) viewLayout.findViewById(R.id.title);
		tvTitle.setText(StringUtil.getString(R.string.TEXT_CUS_NOT_PSDS_IN_MONTHS) + " " + month);
		btClose.setOnClickListener(this);
		// customer list table
		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 60, 80, 240, 300 };
		String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
				StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME),
				StringUtil.getString(R.string.TEXT_ADDRESS) };

		tbList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		this.dto = dto;
		renderLayout(dto);
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @param dto2
	 * @return: voidvoid
	 * @throws:
	 */
	public void renderLayout(CustomerListDTO dto) {
		if (dto.cusList.size() > 0) {
			if (dto.curPage <= 0) {
				tbList.setNumItemsPage(5);
				tbList.setTotalSize(dto.totalCustomer);
				dto.curPage = tbList.getPagingControl().getCurrentPage();
			} else {
				tbList.getPagingControl().setCurrentPage(dto.curPage);
			}
			int pos = 1 + 5 * (tbList.getPagingControl().getCurrentPage() - 1);
			List<TableRow> listRows = new ArrayList<TableRow>();
			for (int i = 0, s = dto.cusList.size(); i < s; i++) {
				StaffInfoCusNotPSDSInMonthRow row = new StaffInfoCusNotPSDSInMonthRow(parent);
				row.renderLayout(dto.cusList.get(i), pos);
				pos++;
				listRows.add(row);
			}
			tbList.addContent(listRows);
		}
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == btClose) {
			fragParent.closePopup();
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
		if (control == tbList) {
			dto.curPage = tbList.getPagingControl().getCurrentPage();
			fragParent.onEvent(ActionEventConstant.GET_CUSTOMER_NO_PSDS, control, dto.curPage);
		}
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {

	}

}
