/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.PaidShowRoomPromotionDto.PaidPromotionItem;
import com.viettel.sabeco.R;

/**
 * Paid Show Room Promotion Row
 * PaidShowRoomPromotionRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:19:02 14 Jan 2014
 */
public class PaidShowRoomPromotionRow extends TableRow {

	private View view;
	TextView tvNumber;
	public TextView tvProductCode;
	TextView tvProductName;
	TextView tvProductPrice;
	TextView tvPromotion;
	EditText etRealNumber;

	public PaidShowRoomPromotionRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_paid_showroom_promotion_row, this);
		tvNumber = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvProductPrice = (TextView) view.findViewById(R.id.tvProducPrice);
		tvPromotion = (TextView) view.findViewById(R.id.tvPromotion);
		etRealNumber = (EditText) view.findViewById(R.id.etRealNumber);
	}

	public void render(int pos, PaidPromotionItem paidPromotionItem) {
		tvNumber.setText("" + pos);
		tvProductCode.setText(paidPromotionItem.productCode);
		tvProductName.setText(paidPromotionItem.productName);
		tvProductPrice.setText(paidPromotionItem.productPrice);
		tvPromotion.setText(paidPromotionItem.promotionNumber);
	}
	
	

}
