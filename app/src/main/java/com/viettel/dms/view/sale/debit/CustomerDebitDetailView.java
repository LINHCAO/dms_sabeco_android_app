/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.debit;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.constants.IntentConstants;
import com.viettel.dms.controller.SaleController;
import com.viettel.dms.dto.view.CusDebitDetailDTO;
import com.viettel.dms.dto.view.CusDebitDetailDTO.CusDebitDetailItem;
import com.viettel.dms.dto.view.CustomerDebtDTO;
import com.viettel.dms.global.ActionEvent;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.control.VNMEditTextClearable;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Chi tiet cong no cua KH
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerDebitDetailView extends BaseFragment implements OnClickListener {

	public static final String TAG = CustomerDebitDetailView.class.getName();
	private static final int ACTION_OK = 0;
	private static final int ACTION_CANCEL = 1;
	private static final int ACTION_RECEIPS = 2;
	private static final int ACTION_PAYMENT_VOUCHER = 3;
	private GlobalBaseActivity parent; // parent
	private TextView tvTotalDebt;
	private Button btPaidDebt;
	public VNMEditTextClearable edPaidAmount;
	CusDebitDetailDTO dto;
	private VinamilkTableView tbCusOrder;
	CustomerDebtDTO debitDetailDto;
	long debitTotal;
	long payTotal;

	public static CustomerDebitDetailView newInstance(CustomerDebtDTO dto) {
		CustomerDebitDetailView f = new CustomerDebitDetailView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putSerializable(IntentConstants.INTENT_DTO, dto);
		f.setArguments(args);
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
		ViewGroup view = (ViewGroup) inflater.inflate(R.layout.layout_cust_debt_detail_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		tvTotalDebt = (TextView) view.findViewById(R.id.tvTotalDebt);
		edPaidAmount = (VNMEditTextClearable) view.findViewById(R.id.edPaidAmount);
		edPaidAmount.setKeyListener(new KeyListener() {

			@Override
			public boolean onKeyUp(View view, Editable text, int keyCode, KeyEvent event) {
				return false;
			}

			@Override
			public boolean onKeyOther(View view, Editable text, KeyEvent event) {
				return false;
			}

			@Override
			public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_0) {
					edPaidAmount.setText(StringUtil.parseAmountMoney(edPaidAmount.getText().toString()));
					return true;
				}
				return false;
			}

			@Override
			public int getInputType() {
				return 0;
			}

			@Override
			public void clearMetaKeyState(View view, Editable content, int states) {

			}
		});
		btPaidDebt = (Button) view.findViewById(R.id.btPaidDebt);
		btPaidDebt.setOnClickListener(this);
		tbCusOrder = (VinamilkTableView) v.findViewById(R.id.tbCusList);

		// enable menu bar
		enableMenuBar(this);
		addMenuItem(StringUtil.getString(R.string.TEXT_PAYMENT_VOUCHER), R.drawable.icon_order, ACTION_PAYMENT_VOUCHER);
		addMenuItem(StringUtil.getString(R.string.TEXT_RECEIPS), R.drawable.icon_map, ACTION_RECEIPS);
		setMenuItemFocus(2);

		SpannableObject obj = new SpannableObject();
		obj.addSpan(StringUtil.getString(R.string.TITLE_TBHV_HEADER_TITLE_DEBIT_DETAIL_VIEW) + Constants.STR_SPACE,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.NORMAL);
		if(!StringUtil.isNullOrEmpty(debitDetailDto.customer.customerCode)) {
			obj.addSpan(debitDetailDto.customer.customerCode.substring(0, 3) + "-" + debitDetailDto.customer.customerName,
				ImageUtil.getColor(R.color.BLACK), android.graphics.Typeface.BOLD);

		}else{
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

		getCusDebtDetail();
		return v;
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void getCusDebtDetail() {
		parent.showLoadingDialog();
		Bundle b = new Bundle();
		b.putLong(IntentConstants.INTENT_DEBIT_ID, debitDetailDto.debitDetail.debitID);
		ActionEvent e = new ActionEvent();
		e.action = ActionEventConstant.GET_CUS_DEBIT_DETAIL;
		e.sender = this;
		e.viewData = b;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		ActionEvent e = modelEvent.getActionEvent();
		switch (e.action) {
		case ActionEventConstant.GET_CUS_DEBIT_DETAIL: {
			dto = (CusDebitDetailDTO) (modelEvent.getModelData());
			dto.customerId = debitDetailDto.customer.customerId;
			// dto.debitId = debitDetailDto.debitDetail.debitID;
			// dto.totalDebit = debitDetailDto.debitDetail.remain;
			// dto.totalPay = debitDetailDto.debitDetail.totalPay;
			for (int i = 0; i < dto.arrList.size(); i++) {
				debitTotal += dto.arrList.get(i).remainingAmount;
			}
			// tvTotalDebt.setText(StringUtil.parseAmountMoney("" +
			// Math.round((double) debitTotal / 1000.0)));
			tvTotalDebt.setText(StringUtil.parseAmountMoney("" + debitTotal));
			renderLayout();
			break;
		}
		case ActionEventConstant.PAY_DEBT: {
			dto = null;
			debitTotal = 0;
			edPaidAmount.setText(StringUtil.parseAmountMoney(""));
			edPaidAmount.setEnabled(true);
			edPaidAmount.setHint(StringUtil.getString(R.string.TEXT_PAID_AMOUNT));
			edPaidAmount.setImageClearVisibile(true);
			getCusDebtDetail();
		}

		default:
			break;
		}
		super.handleModelViewEvent(modelEvent);
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	private void renderLayout() {
		if (dto.arrList.size() > 0) {
			tbCusOrder.clearAllData();
			tbCusOrder.hideNoContentRow();
			int pos = 1;
			int i=0;
			for (CusDebitDetailItem cusDebitDetailItem: dto.arrList) {
				CustomerDebitDetailRow row = new CustomerDebitDetailRow(parent, this);
				cusDebitDetailItem.index = i;
				row.renderLayout(pos, cusDebitDetailItem);
				pos++;
				i++;
				tbCusOrder.addRow(row);
			}

		} else {
			tbCusOrder.showNoContentRow();
		}
	}

	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		parent.closeProgressDialog();
		super.handleErrorModelViewEvent(modelEvent);
	}

	@Override
	public void onEvent(int eventType, View control, Object data) {
		switch (eventType) {
		case ActionEventConstant.PLUS_PAY_DEBT: {
			CusDebitDetailItem item = (CusDebitDetailItem) data;
			payTotal += item.remainingAmount;
			edPaidAmount.setText(StringUtil.parseAmountMoney("" + payTotal));
			edPaidAmount.setEnabled(false);
			edPaidAmount.setImageClearVisibile(false);
			dto.isAllUncheck = false;
			break;
		}

		case ActionEventConstant.SUBTRACT_PAY_DEBT: {
			CusDebitDetailItem item = (CusDebitDetailItem) data;
			payTotal -= item.remainingAmount;
			dto.isAllUncheck = true;
			for (int i = 0; i < dto.arrList.size(); i++) {
				if (dto.arrList.get(i).isCheck) {
					dto.isAllUncheck = false;
					break;
				}
			}
			if (dto.isAllUncheck) {
				edPaidAmount.setText(StringUtil.parseAmountMoney(""));
				edPaidAmount.setEnabled(true);
				edPaidAmount.setHint(StringUtil.getString(R.string.TEXT_PAID_AMOUNT));
				edPaidAmount.setImageClearVisibile(true);
			} else {
				edPaidAmount.setText(StringUtil.parseAmountMoney("" + payTotal));
				edPaidAmount.setEnabled(false);
				edPaidAmount.setImageClearVisibile(false);
			}
			break;
		}
		case ACTION_OK: {
			CusDebitDetailDTO dto = (CusDebitDetailDTO) data;
			processPayDebt(dto);
			break;
		}
		case ACTION_CANCEL: {
			break;
		}
		case ACTION_PAYMENT_VOUCHER:
			Bundle b = new Bundle();
			b.putSerializable(IntentConstants.INTENT_DTO, debitDetailDto);
			ActionEvent e = new ActionEvent();
			e.sender = this;
			e.action = ActionEventConstant.PAYMENT_VOUCHER;
			e.viewData = b;
			SaleController.getInstance().handleSwitchFragment(e);
			break;

		default:
			super.onEvent(eventType, control, data);
			break;
		}

	}

	/**
	 * Thanh toan cong no
	 * 
	 * @author: TamPQ
	 * @param dto2
	 * @return: voidvoid
	 * @throws:
	 */
	private void processPayDebt(CusDebitDetailDTO dto) {
		dto.payNowTotal = payTotal;
		if (dto.isAllUncheck && !StringUtil.isNullOrEmpty(edPaidAmount.getText().toString())) {
			for (int i = dto.arrList.size() - 1; i >= 0; i--) {
				CusDebitDetailItem item = dto.arrList.get(i);
				if (item.remainingAmount > 0) {
					if (payTotal >= item.remainingAmount) {
						item.paidAmount = item.totalDebit;
						item.isWouldPay = true;
						payTotal -= item.remainingAmount;
						item.remainingAmount = 0;
					} else {
						item.paidAmount += payTotal;
						item.remainingAmount -= payTotal;
						item.isWouldPay = true;
						payTotal = 0;
						break;
					}
				}
			}
		} else if (!dto.isAllUncheck && !StringUtil.isNullOrEmpty(edPaidAmount.getText().toString())) {
			payTotal = 0;
			for (int i = 0; i < dto.arrList.size(); i++) {
				CusDebitDetailItem item = dto.arrList.get(i);
				if (item.isCheck) {
					item.isWouldPay = true;
					item.paidAmount = item.totalDebit;
					item.remainingAmount = 0;
				}
			}
		}

		dto.totalPay += dto.payNowTotal;
		dto.totalDebit -= dto.payNowTotal;

		ActionEvent e = new ActionEvent();
		e.viewData = dto;
		e.sender = this;
		e.action = ActionEventConstant.PAY_DEBT;
		SaleController.getInstance().handleViewEvent(e);
	}

	@Override
	public void onClick(View v) {
		if (v == btPaidDebt) {
			String inputStr = edPaidAmount.getText().toString();
			if (StringUtil.isNullOrEmpty(inputStr)) {
				String mess = StringUtil.getString(R.string.TEXT_INPUT_PAY_MONEY);
				GlobalUtil.showDialogConfirm(this, parent, mess, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), -1,
						null, false);
			} else {
				if (dto.isAllUncheck) {
					payTotal = Long.parseLong(edPaidAmount.getText().toString());
					if (Long.parseLong(inputStr) > debitTotal) {

					}
				}
				if (dto.isAllUncheck && Long.parseLong(inputStr) > debitTotal) {
					String mess = StringUtil.getString(R.string.TEXT_EXCEED_MONEY);
					GlobalUtil.showDialogConfirm(this, parent, mess, StringUtil.getString(R.string.TEXT_BUTTON_CLOSE),
							-1, null, false);
				} else {
					String textConfirm = StringUtil.getString(R.string.TEXT_PAY_DEBT) + " "
							+ debitDetailDto.customer.customerName + " "
							+ StringUtil.getString(R.string.TEXT_PAY_DEBT_2) + " "
							+ StringUtil.parseAmountMoney("" + payTotal) + " "
							+ StringUtil.getString(R.string.TEXT_PAY_DEBT_3);
					GlobalUtil.showDialogConfirmCanBackAndTouchOutSide(this, parent, textConfirm,
							StringUtil.getString(R.string.TEXT_AGREE), ACTION_OK,
							StringUtil.getString(R.string.TEXT_BUTTON_CLOSE), ACTION_CANCEL, dto, true, true);
				}
			}
		} else {
			super.onClick(v);
		}
	}

	@Override
	protected void receiveBroadcast(int action, Bundle extras) {
		switch (action) {
		case ActionEventConstant.NOTIFY_REFRESH_VIEW:
			parent.showLoadingDialog();
			dto = null;
			debitTotal = 0;
			payTotal = 0;
			edPaidAmount.setText("");
			edPaidAmount.setEnabled(true);
			edPaidAmount.setHint(StringUtil.getString(R.string.TEXT_PAID_AMOUNT));
			edPaidAmount.setImageClearVisibile(true);
			getCusDebtDetail();
			break;

		default:
			super.receiveBroadcast(action, extras);
			break;
		}
	}
}
