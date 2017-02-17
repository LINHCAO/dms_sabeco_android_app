/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.depot;

import java.util.Vector;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.TotalStatisticsDto;
import com.viettel.dms.dto.view.TotalStatisticsItem;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpinnerAdapter;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * TotalStatisticsFragment
 * 
 * @author : TamPQ since : 9:48:52 AM version :
 */
public class TotalStatisticsView extends BaseFragment implements
		OnEventControlListener, OnClickListener {
	private GlobalBaseActivity parent; // parent
	private EditText tvProductCode;// tvProductCode
	private EditText tvProductName;// tvProductName
	private Spinner spProductType;// spProductType
	private Spinner spProductSubType;// spProductSubType
	private VinamilkTableView tbStatisticsList;// tbStatisticsList
	private TotalStatisticsDto dto = new TotalStatisticsDto();
	private ImageButton btSearch;
	private boolean isLoaded = false;

	public static final String TAG = TotalStatisticsView.class.getName();
	private static final int ACTION_PRODUCT = 0;
	private static final int ACTION_TOTAL_STATISTICS = 1;

	private String[] arrCategory = new String[] { "Tất cả", "Ngành 1",
			"Ngành 2", "Ngành 3", "Ngành 4" };
	private String[] arrSubCategory = new String[] { "Tất cả", "Ngành con 1",
			"Ngành con 2", "Ngành con 3", "Ngành con 4" };

	public static TotalStatisticsView getInstance() {
		TotalStatisticsView f = new TotalStatisticsView();
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
				R.layout.layout_total_statistics_fragment, container, false);

		View v = super.onCreateView(inflater, view, savedInstanceState);
		setTitleHeaderView(StringUtil.getString(R.string.TITLE_VIEW_TOTAL_STATISTICS));

		// menu
		enableMenuBar(this);
		addMenuItem(R.drawable.menu_promotion_icon, ACTION_PRODUCT);
		addMenuItem(R.drawable.menu_report_icon, ACTION_TOTAL_STATISTICS);
		setMenuItemFocus(2);

		tvProductCode = (EditText) view.findViewById(R.id.etProductCode);
		tvProductName = (EditText) view.findViewById(R.id.etProductName);
		btSearch = (ImageButton) view.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		spProductType = (Spinner) view.findViewById(R.id.spProductType);
		spProductSubType = (Spinner) view.findViewById(R.id.spProductSubType);
		SpinnerAdapter adapterLine = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, arrCategory);
		spProductType.setAdapter(adapterLine);
		SpinnerAdapter adapterState = new SpinnerAdapter(parent,
				R.layout.simple_spinner_item, arrSubCategory);
		spProductSubType.setAdapter(adapterState);

		tbStatisticsList = (VinamilkTableView) view
				.findViewById(R.id.tbStatisticsList);

		getStatisticsList();
		return v;
	}

	/**
	 * getToolList
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private void getStatisticsList() {
		ActionEvent e = new ActionEvent();
		Vector<String> v = new Vector<String>();

		v.add(IntentConstants.INTENT_STAFF_ID);
		v.add(String.valueOf(GlobalInfo.getInstance().getProfile()
				.getUserData().id));
		v.add(IntentConstants.INTENT_SHOP_ID);
		if (!StringUtil.isNullOrEmpty(GlobalInfo.getInstance().getProfile()
				.getUserData().shopId)) {
			v.add(GlobalInfo.getInstance().getProfile().getUserData().shopId);
		} else {
			v.add("1");
		}
		v.add(IntentConstants.INTENT_USER_ID);
		// v.add(String.valueOf(GlobalInfo.getInstance().getProfile().getUserData().id));
		v.add("255");

		// check data search
		v.add(IntentConstants.INTENT_PRODUCT_CODE);
		v.add(tvProductCode.getText().toString());
		v.add(IntentConstants.INTENT_PRODUCT_NAME);
		v.add(tvProductName.getText().toString());
		v.add(IntentConstants.INTENT_PRODUCT_CAT);
		v.add(getProducCat(arrCategory[spProductType.getSelectedItemPosition()]));
		v.add(IntentConstants.INTENT_PRODUCT_SUB_CAT);
		v.add(getProducSubCat(arrSubCategory[spProductSubType
				.getSelectedItemPosition()]));

		e.viewData = v;
		e.action = ActionEventConstant.TOTAL_STATISTICS;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * getProducCat
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private String getProducCat(String cat) {
		String d = "";
		if (cat.equals("Thứ 2")) {
			d = "T2";
		} else if (cat.equals("Thứ 3")) {
			d = "T3";
		} else if (cat.equals("Thứ 4")) {
			d = "T4";
		} else if (cat.equals("Thứ 5")) {
			d = "T5";
		} else if (cat.equals("Thứ 6")) {
			d = "T6";
		} else if (cat.equals("Thứ 7")) {
			d = "T7";
		} else if (cat.equals("Chủ nhật")) {
			d = "CN";
		} else if (cat.equals("Tất cả")) {
			d = "";
		}
		return d;

	}

	/**
	 * getProducSubCat
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	private String getProducSubCat(String cat) {
		String d = "";
		if (cat.equals("Thứ 2")) {
			d = "T2";
		} else if (cat.equals("Thứ 3")) {
			d = "T3";
		} else if (cat.equals("Thứ 4")) {
			d = "T4";
		} else if (cat.equals("Thứ 5")) {
			d = "T5";
		} else if (cat.equals("Thứ 6")) {
			d = "T6";
		} else if (cat.equals("Thứ 7")) {
			d = "T7";
		} else if (cat.equals("Chủ nhật")) {
			d = "CN";
		} else if (cat.equals("Tất cả")) {
			d = "";
		}
		return d;

	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.TOTAL_STATISTICS:
			dto = (TotalStatisticsDto) modelEvent.getModelData();
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
		tbStatisticsList.clearAllData();
		int pos = 1;
		for (TotalStatisticsItem totalStatisticsItem : dto.arrList) {
			TotalStatisticsRow row = new TotalStatisticsRow(parent);
			row.tvNumber.setOnClickListener(this);
			row.tvPromotion.setOnClickListener(this);
			row.setTag(Integer.valueOf(pos));
			row.render(pos, totalStatisticsItem);
			pos++;
			tbStatisticsList.addRow(row);
		}

		if(tbStatisticsList.getListRowView().size() != 0){
			TableRow sumRow = new TableRow(parent);
			LayoutInflater vi = (LayoutInflater) parent
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			vi.inflate(R.layout.layout_total_statistics_sum_row, sumRow);

			TextView totalAvailable = (TextView) sumRow
					.findViewById(R.id.tvTotalQuantity);
			TextView soldOut = (TextView) sumRow.findViewById(R.id.tvTotalSoldOut);
			TextView totalRemain = (TextView) sumRow
					.findViewById(R.id.tvTotalRemain);

			totalAvailable.setText("" + dto.available_quantity);
			int sold = dto.quantity - dto.available_quantity;
			soldOut.setText("" + sold);
			totalRemain.setText("" + dto.available_quantity);
			tbStatisticsList.addRow(sumRow);
		}

		if (!isLoaded) {
			isLoaded = true;
			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 60, 120, 382, 120, 120, 120,
					120 };
			String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT), "Mã MH",
					StringUtil.getString(R.string.TEXT_COLUM_ORDER_NAME), StringUtil.getString(R.string.TEXT_PROMOTION)
					, StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT), StringUtil.getString(R.string.TEXT_NUMBER_PRODUCT_SOLD)
					, StringUtil.getString(R.string.TEXT_HEADER_TABLE_REMAIN) };

			tbStatisticsList.getHeaderView().addColumns(
					CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));

		}
		tbStatisticsList.disablePagingControl();

	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSearch:
			getStatisticsList();
			break;

		default:
			break;
		}

	}
}
