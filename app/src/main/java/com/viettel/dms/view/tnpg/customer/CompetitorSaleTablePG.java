/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.dto.db.ProductCompetitorDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Competitor Sale Table PG
 * CompetitorSaleTablePG.java
 * @version: 1.0 
 * @since:  08:31:06 20 Jan 2014
 */
public class CompetitorSaleTablePG extends LinearLayout implements
		View.OnClickListener {

	private final Context con;
	private final View v;
	private TextView tvCompetitorName;
	private VinamilkTableView tbProductCompetList;
	private ProductCompetitorDTO productCompetitorDTO;
	private int type;

	public CompetitorSaleTablePG(Context context) {
		super(context);
		con = context;
		LayoutInflater vi = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.activity_competitor_sale_table_pg, this);
		setDrawingCacheEnabled(true);
		buildDrawingCache();
	}

	/**
	 * @param context
	 */
	public CompetitorSaleTablePG(Context context, ProductCompetitorDTO dto,
			int type1) {
		this(context);
		productCompetitorDTO = dto;
		type = type1;
		initView(v);
		renderLayout();
	}

	/**
	 * render layout
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	private void renderLayout() {
		tbProductCompetList.setDrawingCacheEnabled(true);
		tbProductCompetList.buildDrawingCache();
		tvCompetitorName.setText(productCompetitorDTO.getNameCompetitor());
		int pos = 1;
		if(productCompetitorDTO.getArrProduct().size()==0){
			tbProductCompetList.showNoContentRow();
		}else{
			tbProductCompetList.clearAllData();
			for (OpProductDTO productDTO : productCompetitorDTO.getArrProduct()) {
				CompetitorSaleRowPG row = new CompetitorSaleRowPG(con, type);
				row.renderLayout(pos, productDTO);
				row.setClickable(true);
				row.setTag(pos);
				pos++;
				tbProductCompetList.addRow(row);
			}
		}
	}

	/**
	 * Khởi tạo màn hình
	 * 
	 * @author: dungdq3
	 * @param: View v
	 * @return: void
	 */
	private void initView(View v) {
		
		tvCompetitorName = (TextView) v.findViewById(R.id.tvCompetitorName);
		tbProductCompetList = (VinamilkTableView) v
				.findViewById(R.id.tbProductCompetList);
		int[] COMPET_LIST_TABLE_WIDTHS = { 45, 130, 330, 139, 240 };
		String titleLastColumn = "";
		if (type == 0) { // kiem hang ton doi thu
			titleLastColumn = StringUtil
					.getString(R.string.TEXT_TITLE_REMAIN_COMPETITOR);
		} else {
			titleLastColumn = StringUtil
					.getString(R.string.TEXT_TITLE_SALE_COMPETITOR);
		}
		setOnClickListener(this);
		String[] COMPET_LIST_TABLE_TITLES = {
				StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
				StringUtil.getString(R.string.TEXT_LABLE_PRODUCT_UOM),
				titleLastColumn };
		tbProductCompetList.getHeaderView().addColumns(
				COMPET_LIST_TABLE_WIDTHS, COMPET_LIST_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));

	}

	@Override
	public void onClick(View v) {
		if (v == this) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) con);
		}
	}
}
