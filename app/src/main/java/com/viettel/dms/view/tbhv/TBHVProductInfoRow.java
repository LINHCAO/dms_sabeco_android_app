/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * layout for product info row
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVProductInfoRow extends DMSTableRow implements OnClickListener {
	private TextView tvSTT;
	private TextView tvProductCode;
	private TextView tvProductName;
	private TextView tvPrice;
	private TextView tvCategory;
	private TextView tvConvfact;
	ProductDTO currentDTO;
	
	public TBHVProductInfoRow(Context context, VinamilkTableListener listen) {
		super(context, R.layout.layout_tbhv_product_info_row);
		setListener(listen);
		this.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductCode.setOnClickListener(this);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvCategory = (TextView) this.findViewById(R.id.tvCategory);
		tvConvfact = (TextView) this.findViewById(R.id.tvConvfact);
		tvPrice = (TextView) this.findViewById(R.id.tvPrice);
	}

	/**
	 * 
	 * render layout for product row
	 * 
	 * @author: HaiTC3
	 * @param position
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int position, ProductDTO item) {
		currentDTO = item;
		tvSTT.setText("" + position);
		tvProductCode.setText(item.productCode);
		tvProductName.setText(item.productName);
		tvCategory.setText(String.valueOf(item.categoryCode));
		tvConvfact.setText(String.valueOf(item.convfact));
		tvPrice.setText(StringUtil.parseAmountMoney(item.unitPrice));
	}
	
	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}else if(v==tvProductCode){
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GOTO_PRODUCT_INTRODUCTION, tvProductCode, currentDTO);
		}
	}
}
