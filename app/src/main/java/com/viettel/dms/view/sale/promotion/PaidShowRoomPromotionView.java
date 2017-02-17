/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.PaidShowRoomPromotionDto;
import com.viettel.dms.dto.view.PaidShowRoomPromotionDto.PaidPromotionItem;
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
 * Paid Show Room Promotion View
 * PaidShowRoomPromotionView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:19:26 14 Jan 2014
 */
public class PaidShowRoomPromotionView extends BaseFragment implements
		OnEventControlListener, OnClickListener {

	private GlobalBaseActivity parent; // GlobalBaseActivity
//	private TextView tvProgCode; // tvProgCode
//	private TextView tvProgName; // tvProgName
//	private TextView tvCusCode; // tvCusCode
//	private TextView tvCusName; // tvCusCode
//	private TextView tvFromDate; // tvFromDate
//	private TextView tvToDate; // tvToDate
//	private TextView tvLevel; // tvLevel
	private VinamilkTableView tbPaidPromotion;
	ArrayList<PaidShowRoomPromotionDto.PaidPromotionItem> list = new ArrayList<PaidShowRoomPromotionDto.PaidPromotionItem>();

	private static final int ACTION_ADD_CUSTOMER = 0;

	public static final String TAG = PaidShowRoomPromotionView.class
			.getName();

	public static PaidShowRoomPromotionView getInstance() {
		PaidShowRoomPromotionView f = new PaidShowRoomPromotionView();
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
				R.layout.layout_paid_showroom_promotion_fragment, container,
				false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_PAID_SHOWROOM_PROMOTION));

//		tvProgCode = (TextView) view.findViewById(R.id.tvProgCode);
//		tvProgName = (TextView) view.findViewById(R.id.tvProgName);
//		tvCusCode = (TextView) view.findViewById(R.id.tvCusCode);
//		tvCusName = (TextView) view.findViewById(R.id.tvCusName);
//		tvFromDate = (TextView) view.findViewById(R.id.tvFromDate);
//		tvToDate = (TextView) view.findViewById(R.id.tvToDate);
//		tvLevel = (TextView) view.findViewById(R.id.tvLevel);
		tbPaidPromotion = (VinamilkTableView) view
				.findViewById(R.id.tbPaidPromotion);

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(R.drawable.icon_menu_payment_homephone, ACTION_ADD_CUSTOMER);

		getPromotionList();
		return v;
	}

	/**
	 * getToolList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getPromotionList() {
		ActionEvent e = new ActionEvent();
		Vector<String> v = new Vector<String>();
		e.viewData = v;
		e.action = ActionEventConstant.PAID_PROMOTION_LIST;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.PAID_PROMOTION_LIST:
			PaidShowRoomPromotionDto dto = new PaidShowRoomPromotionDto();
			list.add(dto.item);
			list.add(dto.item);
			list.add(dto.item);
			list.add(dto.item);
			list.add(dto.item);
			list.add(dto.item);

			renderLayout();
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
	private void renderLayout() {
		
		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 60, 120, 360, 120, 120, 160, 100 };
		String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT), StringUtil.getString(R.string.TEXT_COLUM_ORDER_CODE),
				StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME), StringUtil.getString(R.string.TEXT_COLUM_PRICE), "Thực đặt", 
				StringUtil.getString(R.string.TEXT_PROMOTION), Constants.STR_SPACE };
		tbPaidPromotion.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS,
				CUSTOMER_LIST_TABLE_TITLES, ImageUtil.getColor(R.color.BLACK),
				ImageUtil.getColor(R.color.TABLE_HEADER_BG));
		
		int pos = 1;
		for (PaidPromotionItem paidPromotionItem: list) {
			PaidShowRoomPromotionRow row = new PaidShowRoomPromotionRow(parent);
			row.tvNumber.setOnClickListener(this);
			row.setTag(Integer.valueOf(pos));
			row.render(pos, paidPromotionItem);
			pos++;
			tbPaidPromotion.addRow(row);
		}

		TableRow sumRow = new TableRow(parent);
		LayoutInflater vi = (LayoutInflater) parent
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(R.layout.layout_paid_showroom_promotion_sum_row, sumRow);	
		tbPaidPromotion.addRow(sumRow);
		tbPaidPromotion.setTotalSize(9);

	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onEvent(int eventType, View control, Object data) {

	}
}
