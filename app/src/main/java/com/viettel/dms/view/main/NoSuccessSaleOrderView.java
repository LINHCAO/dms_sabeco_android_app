/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.NoSuccessSaleOrderDto;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.sabeco.R;

/**
 *  Popup canh bao don hang loi
 *  @author: HieuNH
 *  @version: 1.0
 *  @since: 1.0
 */
public class NoSuccessSaleOrderView extends ScrollView implements
		OnClickListener {

	private GlobalBaseActivity parent;
	public View viewLayout;
	private VinamilkTableView tbList;
	private TextView tvTitle;
	private Button btClose;
	private NoSuccessSaleOrderDto dto;
	List<TableRow> listRows = new ArrayList<TableRow>();

	public NoSuccessSaleOrderView(Context context, NoSuccessSaleOrderDto dto) {
		super(context);
		parent = (GlobalBaseActivity) context;
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_no_success_order_list, null);
		tbList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCusFeedBack);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		tvTitle = (TextView) viewLayout.findViewById(R.id.tvTitle);
		btClose.setOnClickListener(this);
		this.dto = dto;
		renderLayout(dto);
	}

	private void renderLayout(NoSuccessSaleOrderDto dto2) {
		tvTitle.setText(StringUtil.getString(R.string.TEXT_WARNING));
		int pos = 1;
		if (dto.itemList.size() > 0) {
			for (int i = 0, s = dto.itemList.size(); i < s; i++) {
				NoSuccessSaleOrderRow row = new NoSuccessSaleOrderRow(parent);
				row.renderLayout(pos, dto.itemList.get(i));
				pos++;
				listRows.add(row);
			}
			// customer list table
			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 40, 140, 60, 170, 160, 150 };
			String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
					StringUtil.getString(R.string.TEXT_ORDER_NUMBER),
					StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
					StringUtil.getString(R.string.TEXT_CUSTOMER_NAME_), StringUtil.getString(R.string.TEXT_TOTAL),
					StringUtil.getString(R.string.TEXT_DESCRIPTION) };

			tbList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbList.addContent(listRows);
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btClose) {
			parent.onClick(v);
		}
	}

}
