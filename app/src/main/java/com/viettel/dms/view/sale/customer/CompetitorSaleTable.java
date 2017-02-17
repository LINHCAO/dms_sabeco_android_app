/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

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
public class CompetitorSaleTable extends LinearLayout implements
		View.OnClickListener {

	private final Context con;
	private final View v;
	// ten doi thu
	private TextView tvCompetitorName;
	//list row
	private VinamilkTableView tbProductCompetList;
	// DTO mang hinh
	private ProductCompetitorDTO productCompetitorDTO;
	private int type;
	private boolean allowChangeQuantity = false;

	public CompetitorSaleTable(Context context) {
		super(context);
		con = context;
		LayoutInflater vi = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(R.layout.activity_competitor_sale_table_pg, this);
		setDrawingCacheEnabled(true);
		buildDrawingCache();
	}
	
	/**
	 * Ham khoi tao
	 * @param context
	 * @param type : 0: bsg, 1- bia doi thu
	 */
	public CompetitorSaleTable(Context context, int type) {
		super(context);
		con = context;
		
		int idLayout  = 0;
		// BSG
		if(type == 0){
			idLayout = R.layout.activity_bsg_sale_table_pg;
		}
		// Bia doi thu
		else{
			idLayout = R.layout.activity_competitor_sale_table_pg;
		}
		LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = vi.inflate(idLayout, this);
		setDrawingCacheEnabled(true);
		buildDrawingCache();
	}

	/**
	 * @param context
	 */
	public CompetitorSaleTable(Context context, ProductCompetitorDTO dto, int type1) {
		this(context, dto, type1, true); 
	}
	
	/**
	 * @param context
	 */
	public CompetitorSaleTable(Context context, ProductCompetitorDTO dto, int type1, boolean allowChangeQuantity) {
		this(context);
		productCompetitorDTO = dto;
		this.allowChangeQuantity = allowChangeQuantity;
		type = type1;
		initView(v);
		renderLayout();
	}
	
	/**
	 * Ham khoi tao cho BSG
	 * @param context
	 */
	public CompetitorSaleTable(Context context, ProductCompetitorDTO dto, int type1, int unuse, boolean allowChangeQuantity) {
		this(context, 0);
		productCompetitorDTO = dto;
		this.allowChangeQuantity = allowChangeQuantity;
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
				CompetitorSaleRow row = new CompetitorSaleRow(con, type, allowChangeQuantity);
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
		int[] COMPET_LIST_TABLE_WIDTHS;
		if(type == 1) {
			COMPET_LIST_TABLE_WIDTHS = new int[] { 45, 130, 250, 110, 180, 180 };
		} else {
			COMPET_LIST_TABLE_WIDTHS = new int[] { 45, 130, 330, 139, 240 };
		}
		String titleLastColumn = "";
//		if (type == 0) { // kiem hang ton doi thu
//			titleLastColumn = StringUtil
//					.getString(R.string.TEXT_TITLE_REMAIN_COMPETITOR);
//		} else {
//			titleLastColumn = StringUtil
//					.getString(R.string.TEXT_TITLE_SALE_COMPETITOR);
//		}
		titleLastColumn = StringUtil
				.getString(R.string.TEXT_TITLE_SALE_COMPETITOR);
		setOnClickListener(this);
		String[] COMPET_LIST_TABLE_TITLES;
		if(type == 1) {
			COMPET_LIST_TABLE_TITLES = new String[] {
					StringUtil.getString(R.string.TEXT_STT),
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
					StringUtil.getString(R.string.TEXT_LABLE_PRODUCT_UOM),
					titleLastColumn,
					StringUtil.getString(R.string.TEXT_TITLE_PRICE_COMPETITOR)};
		} else {
			COMPET_LIST_TABLE_TITLES = new String[] {
					StringUtil.getString(R.string.TEXT_STT),
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME),
					StringUtil.getString(R.string.TEXT_LABLE_PRODUCT_UOM),
					titleLastColumn };
		}
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
