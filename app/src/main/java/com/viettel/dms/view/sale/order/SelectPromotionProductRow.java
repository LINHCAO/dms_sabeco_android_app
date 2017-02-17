/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 *  row display for select promotion product view
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class SelectPromotionProductRow extends TableRow implements OnClickListener{
	Context _context;
	View view;
	// STT
	TextView tvSTT;
	// code
	TextView tvProductCode;
	// Name
	TextView tvProductName;
	// number stock
	TextView tvNumStock;
//	// number tvPrice
//	TextView tvPrice;
	// number tvNumberPromotion
	TextView tvNumberPromotion;
	
	
	TextView tvNotifyNull;
	
	// listener
	protected VinamilkTableListener listener;
	// table parent
	private View tableParent;
	private TableRow row;
	// data to render layout for row
	OrderDetailViewDTO myData;

	/**
	 * constructor for class
	 * @param context
	 * @param aRow
	 */
	public SelectPromotionProductRow(Context context, View aRow) {
		super(context);
		tableParent = aRow;
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_select_product_promotion_row, this);
		row = (TableRow)view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
//		tvProductCode.setOnClickListener(this);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvNumStock = (TextView) view.findViewById(R.id.tvNumStock);
//		tvPrice = (TextView) view.findViewById(R.id.tvPrice);
		tvNumberPromotion = (TextView) view.findViewById(R.id.tvNumberPromotion);
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);
		myData = new OrderDetailViewDTO();
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	*  display notify null for table
	*  @author: HaiTC3
	*  @param strNotifyContent
	*  @return: void
	*  @throws:
	 */
	public void renderLayoutNotify(String strNotifyContent){
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setText(strNotifyContent);
		tvSTT.setVisibility(View.GONE);
		tvProductCode.setVisibility(View.GONE);
		tvProductName.setVisibility(View.GONE);
		tvNumStock.setVisibility(View.GONE);
//		tvPrice.setVisibility(View.GONE);
		tvNumberPromotion.setVisibility(View.GONE);
	}
	/**
	 * 
	*  init layout for row
	*  @author: HaiTC3
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, OrderDetailViewDTO item) {
		this.myData = item;
		tvSTT.setText(String.valueOf(position));
		tvProductCode.setText(item.productCode);
		tvProductName.setText(item.productName);
		long stock1 = item.stock/item.convfact;
		long stock2 = item.stock % item.convfact;
		String stock = String.valueOf(stock1) + "/" + String.valueOf(stock2);
		tvNumStock.setText(stock);
//		tvPrice.setText(StringUtil.parseAmountMoney(String.valueOf(item.orderDetailDTO.price)));
		tvNumberPromotion.setText(String.valueOf(item.orderDetailDTO.quantity));
		if(item.stock <= 0){
			this.updateColorForRow(ImageUtil.getColor(R.color.RED));
		}
		else if(item.stock < item.orderDetailDTO.quantity){
			this.updateColorForRow(ImageUtil.getColor(R.color.OGRANGE));
		}
	}
	
	/**
	 * 
	*  update color for row
	*  @param color
	*  @return: void
	*  @throws:
	*  @author: HaiTC3
	*  @date: Oct 24, 2012
	 */
	public void updateColorForRow(int color){
		tvSTT.setTextColor(color);
		tvProductCode.setTextColor(color);
		tvProductName.setTextColor(color);
		tvNumStock.setTextColor(color);
//		tvPrice.setTextColor(color);
		tvNumberPromotion.setTextColor(color);
	}
	
	/**
	 * 
	*  cap nhat layout khi dong nay da duoc selected
	*  @author: HaiTC3
	*  @return: void
	*  @throws:
	 */
	public void updateLayoutSelected(){
		tvSTT.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
		tvProductCode.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
		tvProductName.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
		tvNumStock.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
//		tvPrice.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
		tvNumberPromotion.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
	}

	public View getView() {
		return view;
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == row) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.BROADCAST_CHANGE_PROMOTION_PRODUCT, tableParent, this.myData);
		}
	}
}
