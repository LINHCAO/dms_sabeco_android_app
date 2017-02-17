/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.debit;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.sale.debit.PaymentVoucherDTO.PaymentVoucherItem;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class PaymentVoucherView extends BaseFragment {
	public static final String TAG = PaymentVoucherView.class.getName();

	private GlobalBaseActivity parent;
	private CustomerDebtDTO debitDetailDto;
	private VinamilkTableView tbCusOrder;
	private PaymentVoucherDTO dto;
	private long returnTotal;
	private TextView tvReturnTotal;

	private Button btOutput;

	public static PaymentVoucherView newInstance(Bundle data) {
		PaymentVoucherView f = new PaymentVoucherView();
		f.setArguments(data);
		return f;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		parent = (GlobalBaseActivity) activity;
		debitDetailDto = (CustomerDebtDTO) getArguments().getSerializable(IntentConstants.INTENT_DTO);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_payment_voucher_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);

		tvReturnTotal = (TextView) view.findViewById(R.id.tvTotalDebt);
		btOutput = (Button) view.findViewById(R.id.btPaidDebt);
		btOutput.setOnClickListener(this);
		tbCusOrder = (VinamilkTableView) v.findViewById(R.id.tbCusList);

		SpannableObject obj = new SpannableObject();
		obj.addSpan(StringUtil.getString(R.string.TITLE_TBHV_HEADER_TITLE_PAYMENT_VOUCHER_VIEW) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		if(!StringUtil.isNullOrEmpty(debitDetailDto.customer.customerCode)) {
			obj.addSpan(debitDetailDto.customer.customerCode.substring(0, 3) + "-" + debitDetailDto.customer.customerName,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		}else {
			obj.addSpan(debitDetailDto.customer.customerName,
					ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);
		}

		int[] CUSTOMER_LIST_TABLE_WIDTHS = { 50, 140, 100, 110, 110, 110, 140, 100, 60 };
		String[] CUSTOMER_LIST_TABLE_TITLES = { StringUtil.getString(R.string.TEXT_STT),
				StringUtil.getString(R.string.TEXT_INVOICE_CODE), StringUtil.getString(R.string.TEXT_INVOICE_DATE),
				StringUtil.getString(R.string.TEXT_TOTAL_DEBT), StringUtil.getString(R.string.TEXT_PAID),
				StringUtil.getString(R.string.TEXT_REMAIN_AMOUNT), StringUtil.getString(R.string.TEXT_RECEIPS),
				StringUtil.getString(R.string.TEXT_PAID_DATE), "" };
		tbCusOrder.getHeaderView().addColumns(CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
				ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG));

		setTitleHeaderView(obj);

		getPaymentVoucher();

		return v;
	}

	/**
	 * get Payment Voucher
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void getPaymentVoucher() {
		parent.showLoadingDialog();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_DEBIT_ID, debitDetailDto.debitDetail.debitID);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.PAYMENT_VOUCHER;
		e.sender = this;
		e.viewData = b;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.PAYMENT_VOUCHER: {
			dto = (PaymentVoucherDTO) (modelEvent.getModelData());
			for (int i = 0; i < dto.arrList.size(); i++) {
				returnTotal += (dto.arrList.get(i).totalReturnAmount - dto.arrList.get(i).cusPaidAmount);
			}
			tvReturnTotal.setText(StringUtil.parseAmountMoney("" + returnTotal));
			renderLayout();
		}
		}
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void renderLayout() {
		tbCusOrder.clearAllData();
		if (dto.arrList.size() > 0) {
			tbCusOrder.hideNoContentRow();
			int pos = 1;
			for (PaymentVoucherItem paymentVoucherItem: dto.arrList) {
				PaymentVoucherRow row = new PaymentVoucherRow(parent, this);
				row.renderLayout(pos, paymentVoucherItem);
				pos++;
				tbCusOrder.addRow(row);
			}

		} else {
			tbCusOrder.showNoContentRow();
		}
	}
}
