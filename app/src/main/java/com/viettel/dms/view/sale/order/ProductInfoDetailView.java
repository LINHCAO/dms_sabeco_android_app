/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * 
 * Popup thong tin chi tiet san pham
 * 
 * @author: ThanhNN8
 * @version: 1.0
 * @since: 1.0
 */
public class ProductInfoDetailView extends LinearLayout implements
		OnClickListener {

	public static final int CLOSE_POPUP_DETAIL_PROMOTION = 1000;
	// ma san pham
	TextView tvProductCode;
	// ten san pham
	TextView tvProductName;
	// quy cach
	TextView tvProductType;
	// nganh hang
	TextView tvProductPart;
	// button close
	public Button btCloseProductInfoDetail;
	// parent
	// public SalesPersonActivity parent;

	// BaseFragment
	public BaseFragment listener;
	public View viewLayout;
	ProductDTO dtoData;

	/**
	 * @param context
	 * @param attrs
	 */
	public ProductInfoDetailView(Context context, BaseFragment listener) {
		super(context);
		// this.parent = (SalesPersonActivity) context;

		this.listener = listener;
		LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();
		viewLayout = inflater
				.inflate(R.layout.layout_product_info_detail, null);

		btCloseProductInfoDetail = (Button) viewLayout
				.findViewById(R.id.btCloseProductInfoDetail);

		btCloseProductInfoDetail.setOnClickListener(this);

		tvProductCode = (TextView) viewLayout.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) viewLayout.findViewById(R.id.tvProductName);
		tvProductType = (TextView) viewLayout.findViewById(R.id.tvProductType);
		tvProductPart = (TextView) viewLayout.findViewById(R.id.tvProductPart);
	}

	/**
	 * 
	 * update layout
	 * 
	 * @author: ThanhNN
	 * @return: void
	 * @throws:
	 */
	public void updateLayout(ProductDTO dto) {
		this.dtoData = dto;
		tvProductCode.setText(dto.productCode);
		tvProductName.setText(dto.productName);
		// tvProductType.setText(dto.convfact + " " + dto.uom1 + "/" +
		// dto.uom2);
		tvProductType.setText(dto.convfact + Constants.STR_SPACE + dto.uom1
				+ " / " + dto.uom2);
		tvProductPart.setText(dto.categoryCode);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (view == btCloseProductInfoDetail) {
			listener.onClick(view);
		}
	}
}
