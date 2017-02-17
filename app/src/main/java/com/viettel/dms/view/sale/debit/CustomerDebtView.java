/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.debit;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Danh sach cong no khach hang
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */

public class CustomerDebtView extends BaseFragment implements OnClickListener, VinamilkTableListener {
	public static final String TAG = CustomerDebtView.class.getName();
	private GlobalBaseActivity parent; // parent
	// nhap ma khach hang
	VNMEditTextClearable edCustomerCode;
	// nhap ten khach hang
	VNMEditTextClearable edCustomerName;
	// button tim kiem
	Button btSearch;
	// bang du lieu
	VinamilkTableView tbCusList;
	// ma, ten KH de search
	private String textCusCode = "";
	private String textCusNameAddress = "";
	// du lieu cong no khach hang
	ArrayList<CustomerDebtDTO> listCustomerDebt;

	public static CustomerDebtView newInstance() {
		CustomerDebtView f = new CustomerDebtView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putBoolean("isReload", true);
		f.setArguments(args);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_cust_debt_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		edCustomerCode = (VNMEditTextClearable) view.findViewById(R.id.edCustomerCode);
		edCustomerName = (VNMEditTextClearable) view.findViewById(R.id.edCustomerName);
		btSearch = (Button) view.findViewById(R.id.btSearch);
		btSearch.setOnClickListener(this);
		tbCusList = (VinamilkTableView) v.findViewById(R.id.tbCusList);

		setTitleHeaderView(StringUtil.getString(R.string.TITLE_CUSTOMER_DEBT_VIEW));
		layoutTableHeader();
		getListCustomerDebt(1);
		return v;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btSearch:
			textCusCode = edCustomerCode.getText().toString();
			textCusNameAddress = edCustomerName.getText().toString();
			getListCustomerDebt(1);
			break;
		default:
			super.onClick(v);
			break;
		}
	}

	/**
	 * Layout header table view
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void layoutTableHeader() {
		// customer list table
		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 60, 100, 200, 300, 120, 90, 70 };
		String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_TB_CUSTOMER_CODE),
				StringUtil.getString(R.string.TEXT_TB_CUSTOMER_NAME), StringUtil.getString(R.string.TEXT_ADDRESS),
				StringUtil.getString(R.string.TEXT_CUS_DEBT), StringUtil.getString(R.string.TEXT_DAY_NUMBER), "" };
		tbCusList.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		switch (modelEvent.getActionEvent().action) {
		case ActionEventConstant.ACTION_GET_CUSTOMER_DEBT:
			listCustomerDebt = (ArrayList<CustomerDebtDTO>) modelEvent.getModelData();
			renderDataLayout();
			parent.closeProgressDialog();
			break;
		default:
			break;
		}
		super.handleModelViewEvent(modelEvent);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	/**
	 * Get danh sach cong no khach hang
	 * 
	 * @author: BangHN
	 * @param page
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void getListCustomerDebt(int page) {
		parent.showLoadingDialog();
		ActionEvent e = new ActionEvent();
		Bundle bundle = new Bundle();
		bundle.putInt(IntentConstants.INTENT_PAGE, page);

		// check data search
		bundle.putString(IntentConstants.INTENT_CUSTOMER_CODE, textCusCode);
		bundle.putString(IntentConstants.INTENT_CUSTOMER_NAME,
				StringUtil.getEngStringFromUnicodeString(textCusNameAddress));
		bundle.putString(IntentConstants.INTENT_USER_ID, "" + GlobalInfo.getInstance().getProfile().getUserData().id);

		e.viewData = bundle;
		e.action = ActionEventConstant.ACTION_GET_CUSTOMER_DEBT;
		e.sender = this;
		SaleController.getInstance().handleViewEvent(e);
	}

	/**
	 * render data du lieu danh sach cong no
	 * 
	 * @author: BangHN
	 * @return: void
	 * @throws: Ngoại lệ do hàm đưa ra (nếu có)
	 */
	private void renderDataLayout() {
		if (listCustomerDebt != null && listCustomerDebt.size() > 0) {
			tbCusList.clearAllData();
			int pos=1;
			for (CustomerDebtDTO customerDebtDTO: listCustomerDebt) {
				CustomerDebtlRow row = new CustomerDebtlRow(parent, this);
				row.renderLayout(pos, customerDebtDTO);
				tbCusList.addRow(row);
				pos++;
			}
		} else {
			tbCusList.showNoContentRow();
		}
	}

	@Override
	public void handleVinamilkTableloadMore(View control, Object data) {
	}

	@Override
	public void handleVinamilkTableRowEvent(int action, View control, Object data) {
		switch (action) {
		case ActionEventConstant.GET_CUS_DEBIT_DETAIL:
			ActionEvent e = new ActionEvent();
			e.viewData = data;
			e.action = ActionEventConstant.GET_CUS_DEBIT_DETAIL;
			e.sender = this;
			SaleController.getInstance().handleSwitchFragment(e);
			break;

		default:
			break;
		}
	}
	
	
	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			if (this.isVisible()) {
				parent.showLoadingDialog();
				textCusCode = edCustomerCode.getText().toString();
				textCusNameAddress = edCustomerName.getText().toString();
				getListCustomerDebt(1);
			}
			break;
		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
