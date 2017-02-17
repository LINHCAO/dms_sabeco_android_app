/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import java.util.ArrayList;
import java.util.Vector;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.ReviewShowRoomDto;
import com.viettel.dms.dto.view.ShowRoomGoodsDto;
import com.viettel.dms.dto.view.ShowRoomToolDto;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Review Show Room Fragment
 * ReviewShowRoomFragment.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:20:00 14 Jan 2014
 */
public class ReviewShowRoomFragment extends BaseFragment implements
		OnEventControlListener, OnClickListener {
	private GlobalBaseActivity parent;
	private boolean isLoading;
	ReviewShowRoomDto dto = new ReviewShowRoomDto();;
	ArrayList<ShowRoomGoodsDto> goodsList = new ArrayList<ShowRoomGoodsDto>();
	ArrayList<ShowRoomToolDto> toolList = new ArrayList<ShowRoomToolDto>();
	TextView tvProgCode;
	TextView tvProgName;
	TextView tvCusCode;
	TextView tvCusName;
	TextView tvFromDate;
	TextView tvToDate;
	TextView tvLevel;
	VinamilkTableView tbTool;
	VinamilkTableView tbGoods;

	private static final int ACTION_ADD_CUSTOMER = 0;
	public static final String TAG = ReviewShowRoomFragment.class.getName();

	public static ReviewShowRoomFragment getInstance() {
		ReviewShowRoomFragment f = new ReviewShowRoomFragment();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		f.setArguments(args);
		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (GlobalBaseActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_review_showroomf_fragment, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_PROMOTION_PROGRAM));

		tvProgCode = (TextView) view.findViewById(R.id.tvProgCode);
		tvProgName = (TextView) view.findViewById(R.id.tvProgName);
		tvCusCode = (TextView) view.findViewById(R.id.tvCusCode);
		tvCusName = (TextView) view.findViewById(R.id.tvCusName);
		tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
		tvToDate = (TextView) view.findViewById(R.id.tvToDate);
		tvLevel = (TextView) view.findViewById(R.id.tvLevel);
		tbTool = (VinamilkTableView) view.findViewById(R.id.tbToolToShow);
		tbGoods = (VinamilkTableView) view.findViewById(R.id.tbGoodsToShow);

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(R.drawable.icon_menu_payment_homephone, ACTION_ADD_CUSTOMER);

		getToolList();
		getGoodsList();
		return v;
	}

	/**
	 * getGoodsList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getGoodsList() {
		ActionEvent e = new ActionEvent();
		Vector<String> v = new Vector<String>();

		e.viewData = v;
		e.action = ActionEventConstant.REVIEW_SHOWROOM_GOODS_LIST;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * getToolList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getToolList() {
		if (isLoading)
			return;
		isLoading = true;
		ActionEvent e = new ActionEvent();
		Vector<String> v = new Vector<String>();

		e.viewData = v;
		e.action = ActionEventConstant.REVIEW_SHOWROOM_TOOL_LIST;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.REVIEW_SHOWROOM_TOOL_LIST:
			dto.toolDto = new ShowRoomToolDto();
			toolList.add(dto.toolDto);
			toolList.add(dto.toolDto);
			toolList.add(dto.toolDto);
			toolList.add(dto.toolDto);
			toolList.add(dto.toolDto);

			renderLayout_ToolList();

			break;
		case ActionEventConstant.REVIEW_SHOWROOM_GOODS_LIST:
			dto.goodsDto = new ShowRoomGoodsDto();
			goodsList.add(dto.goodsDto);
			goodsList.add(dto.goodsDto);
			goodsList.add(dto.goodsDto);
			goodsList.add(dto.goodsDto);
			goodsList.add(dto.goodsDto);
			
			renderLayout_GoodList();
			break;

		default:
			super.handleModelViewEvent(modelEvent);
			break;
		}
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout_GoodList() {
		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 80, 120, 540, 150, 150 };
		String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT), StringUtil.getString(R.string.TEXT_NH_CODE), 
				StringUtil.getString(R.string.TEXT_CATEGORY_NAME), StringUtil.getString(R.string.ATTAIN), StringUtil.getString(R.string.UN_ATTAIN)};

		tbGoods.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS,
				CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		if(goodsList.size()>0){
			tbGoods.clearAllData();
			tbGoods.hideNoContentRow();
			int pos = 1;
			for (ShowRoomGoodsDto showRoomGoodsDto: goodsList) {
				ShowRoomGoodsRow row = new ShowRoomGoodsRow(parent);
				row.tvGoodsCode.setOnClickListener(this);
				row.setTag(Integer.valueOf(pos));
				row.render(pos, showRoomGoodsDto);
				pos++;
				tbGoods.addRow(row);
			}
			tbGoods.setTotalSize(9);
		}else{
			tbGoods.showNoContentRow();
		}
		
	}

	/**
	 * renderLayout
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void renderLayout_ToolList() {
		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 80, 120, 540, 150, 150 };
		String[] CUSTOMER_LIST_TABLE_TITLES = {StringUtil.getString(R.string.TEXT_STT), StringUtil.getString(R.string.TEXT_CC_CODE), 
				StringUtil.getString(R.string.TEXT_CC_NAME),StringUtil.getString(R.string.ATTAIN), StringUtil.getString(R.string.UN_ATTAIN)};

		tbTool.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS,
				CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		int pos = 1;
		if(toolList.size()>0){
			tbTool.clearAllData();
			tbTool.hideNoContentRow();
			for (ShowRoomToolDto showRoomToolDto: toolList) {
				ShowRoomToolRow row = new ShowRoomToolRow(parent);
				row.tvToolCode.setOnClickListener(this);
				row.setTag(Integer.valueOf(pos));
				row.render(pos, showRoomToolDto);
				pos++;
				tbTool.addRow(row);
			}
		}else{
			tbTool.showNoContentRow();
		}
		tbTool.setTotalSize(9);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onEvent(int eventType, View control, Object data) {

	}
}
