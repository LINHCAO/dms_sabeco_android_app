/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

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

import com.viettel.dms.dto.view.GsnppLessThan2MinsDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.SupervisorActivity;
import com.viettel.sabeco.R;

/**
 * Danh sach KH ghe tham < 2 phut
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class GsnppLessThan2MinView extends ScrollView implements OnClickListener {

	private SupervisorActivity parent;
	private GSBHRouteSupervisionView fragParent;
	public View viewLayout;
	private VinamilkTableView tbList;
	private Button btClose;
	private GsnppLessThan2MinsDTO dto;
	private TextView title;
	private List<TableRow> listRows = new ArrayList<TableRow>();

	/**
	 * @param context
	 */
	public GsnppLessThan2MinView(Context context, GsnppLessThan2MinsDTO dto) {
		super(context);
		parent = (SupervisorActivity) context;
		fragParent = (GSBHRouteSupervisionView) parent.getFragmentManager().findFragmentByTag(
				GSBHRouteSupervisionView.TAG);
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(R.layout.layout_wrong_cus_list_view, null);
		tbList = (VinamilkTableView) viewLayout.findViewById(R.id.tbCusFeedBack);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		title = (TextView) viewLayout.findViewById(R.id.tvTitle);
		btClose.setOnClickListener(this);
		this.dto = dto;
		if (dto.isLessThan2Min) {
			title.setText(StringUtil.getString(R.string.TEXT_LESS_THAN_2_MIN));
		} else {
			title.setText(StringUtil.getString(R.string.TEXT_MORE_THAN_30_MIN));
		}
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
	private void renderLayout(GsnppLessThan2MinsDTO dto2) {
		if (dto.arrList.size() > 0) {
			for (int i = 0, s = dto.arrList.size(); i < s; i++) {
				GsnppLessThan2MinRow row = new GsnppLessThan2MinRow(parent);
				row.renderLayout(dto.arrList.get(i));
				listRows.add(row);
			}
			// customer list table
			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 170, 220, 110, 110, 110 };
			String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_CUSTOMER),
					StringUtil.getString(R.string.TEXT_ADDRESS), StringUtil.getString(R.string.TEXT_BEGIN_TIME),
					StringUtil.getString(R.string.TEXT_END_TIME), StringUtil.getString(R.string.TEXT_SALES_2) };

			tbList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbList.addContent(listRows);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btClose) {
			fragParent.onClick(v);
		}
	}

}
