/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.salestatistics;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.dto.view.SaleProductInfoDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * hien thi 1 row trong man hinh thong ke don tong trong ngay cua PreSales
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class SaleStatisticsInDayPreSalesRow extends TableRow implements OnClickListener {
	// current context
	private final Context _context;
	// parent view
	private final View view;
	// stt
	TextView tvSTT;
	// product code
	TextView tvProductCode;
	// product name
	TextView tvProductName;
	// industry product
	TextView tvIndustryProduct;
	// // convfact
	TextView tvConvfact;
	// price
	TextView tvPrice;
	// number product
	TextView tvNumberProduct;
	// total amount
	TextView tvTotalAmount;
	// data to render layout for row
	FindProductSaleOrderDetailViewDTO myData;
	
	public SaleStatisticsInDayPreSalesRow(Context con){
		super(con);
		_context = con;
		LayoutInflater vi = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_sale_statistics_in_day_pre_sales_row, this);
	}
			
	/**
	 * constructor for class
	 * 
	 * @param context
	 * @param aRow
	 */
	public SaleStatisticsInDayPreSalesRow(Context context, View aRow) {
		this(context);
		setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvIndustryProduct = (TextView) view.findViewById(R.id.tvIndustryProduct);
		tvConvfact = (TextView) view.findViewById(R.id.tvConvfact);
		tvPrice = (TextView) view.findViewById(R.id.tvPrice);
		tvNumberProduct = (TextView) view.findViewById(R.id.tvNumberProduct);
		tvTotalAmount = (TextView) view.findViewById(R.id.tvTotalAmount);
	}

	/**
	 * 
	 * init layout for row
	 * 
	 * @author: HaiTC3
	 * @param position
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int position, SaleProductInfoDTO item) {
		tvSTT.setText(String.valueOf(position));
		tvProductCode.setText(item.productInfo.productCode);
		tvProductName.setText(item.productInfo.productName);
		tvIndustryProduct.setText(item.productInfo.categoryCode);
		tvConvfact.setText(String.valueOf(item.productInfo.convfact));
		tvPrice.setText(StringUtil.parseAmountMoney(String.valueOf(item.productInfo.unitPrice)));
		tvNumberProduct.setText(item.numberProductFormat);
		tvTotalAmount.setText(StringUtil.parseAmountMoney(String.valueOf(item.totalAmountSold)));
	}

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
