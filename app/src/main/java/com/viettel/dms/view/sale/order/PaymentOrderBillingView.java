/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.viettel.dms.global.ModelEvent;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * 
 * payment order screen
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class PaymentOrderBillingView extends BaseFragment implements
		OnEventControlListener, OnClickListener {

	// tag for this fragment
	public static final String TAG = PaymentOrderBillingView.class.getName();

	// action back to pre view
	public final static int BACK_ACTION = 0;

	// order item code
	TextView tvOrderItemCode;
	// order customer
	TextView tvOrderCustomer;
	// order item date
	TextView tvOrderItemDate;
	// order item status
	TextView tvOrderItemStatus;
	// order total money
	TextView tvOrderTotalMoney;
	// display payment money
	EditText etPaymentMoney;
	// button payment
	Button btPayment;
	// button pass don't send payment
	Button btPass;

	/**
	 * 
	 * method get instance
	 * 
	 * @author: HaiTC3
	 * @param index
	 * @return
	 * @return: PaymentOrderBillingView
	 * @throws:
	 */
	public static PaymentOrderBillingView getInstance(int index) {

		PaymentOrderBillingView instance = new PaymentOrderBillingView();
		// Supply index input as an argument.
		Bundle args = new Bundle();
		args.putInt("index", index);
		instance.setArguments(args);
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onActivityCreated(android
	 * .os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.viettel.vinamilk.view.main.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#onCreateView(android.view
	 * .LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		ViewGroup view = (ViewGroup) inflater.inflate(
				R.layout.layout_payment_order_billing_view, container, false);
		View v = super.onCreateView(inflater, view, savedInstanceState);
		initView(v);
		enableMenuBar(this);
		addMenuItem(R.drawable.icon_menu_payment_homephone, BACK_ACTION);
		setTitleHeaderView(getString(R.string.TEXT_HEADER_TITLE_PAYMENT_ORDER_BILLING));
		return v;
	}

	/**
	 * 
	 * init control for screen
	 * 
	 * @author: HaiTC3
	 * @param v
	 * @return: void
	 * @throws:
	 */
	public void initView(View v) {
		tvOrderCustomer = (TextView) v.findViewById(R.id.tvOrderCustomer);
		tvOrderItemCode = (TextView) v.findViewById(R.id.tvOrderItemCode);
		tvOrderItemDate = (TextView) v.findViewById(R.id.tvOrderItemDate);
		tvOrderItemStatus = (TextView) v.findViewById(R.id.tvOrderItemStatus);
		tvOrderTotalMoney = (TextView) v.findViewById(R.id.tvOrderTotalMoney);
		etPaymentMoney = (EditText) v.findViewById(R.id.etPaymentMoney);
		btPass = (Button) v.findViewById(R.id.btPass);
		btPass.setOnClickListener(this);
		btPayment = (Button) v.findViewById(R.id.btPayment);
		btPayment.setOnClickListener(this);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleModelViewEvent(com.
	 * viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleModelViewEvent(modelEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.main.BaseFragment#handleErrorModelViewEvent
	 * (com.viettel.vinamilk.global.ModelEvent)
	 */
	@Override
	public void handleErrorModelViewEvent(ModelEvent modelEvent) {
		// TODO Auto-generated method stub
		super.handleErrorModelViewEvent(modelEvent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.viettel.vinamilk.view.listener.OnEventControlListener#onEvent(int,
	 * android.view.View, java.lang.Object)
	 */
	@Override
	public void onEvent(int eventType, View control, Object data) {
		// TODO Auto-generated method stub
		switch (eventType) {
		case BACK_ACTION:

			break;

		default:
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btPass) {

		} else if (v == btPayment) {

		}
	}

}
