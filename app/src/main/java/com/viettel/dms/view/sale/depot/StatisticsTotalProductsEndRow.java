/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.depot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 *  Mo ta muc dich cua lop (interface)
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 10:48:21 AM | Jun 14, 2012
 */
public class StatisticsTotalProductsEndRow extends TableRow{
	Context _context;
	View view;
	// STT info
	TextView tvTotal;
	// product code
	TextView tvPromotion;
	// product name
	TextView tvNumberProduct;
	// product in stock
	TextView tvSold;
	// total product 
	TextView tvStock;

	// listener
	protected OnEventControlListener listener;

	public StatisticsTotalProductsEndRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_statistics_product_info_end_row, this);
		tvTotal = (TextView) view.findViewById(R.id.tvTotal);
		tvPromotion = (TextView) view.findViewById(R.id.tvPromotion);
		tvNumberProduct = (TextView) view.findViewById(R.id.tvNumberProduct);
		tvSold = (TextView) view.findViewById(R.id.tvSold);
		tvStock = (TextView) view.findViewById(R.id.tvStock);
	}

	/**
	 * 
	*  set listener for row
	*  @author: HaiTC3
	*  @since: 10:46:40 AM | Jun 14, 2012
	*  @param listener
	*  @return: void
	*  @throws:
	 */
	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	
	/**
	 * 
	*  render layout for row
	*  @author: HaiTC3
	*  @since: 10:46:26 AM | Jun 14, 2012
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, ProductDTO item) {
		tvTotal.setText("tong");
		tvPromotion.setText("Ma khuyen mai");
		tvNumberProduct.setText("so luong");
		tvSold.setText("so hang da ban");
		tvStock.setText("so hang ton kho");
	}

	public View getView() {
		return view;
	}
}
