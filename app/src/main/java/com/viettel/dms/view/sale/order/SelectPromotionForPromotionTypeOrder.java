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
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * hien thi 1 dong thong tin CTKM cho CTKM loai don hang
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class SelectPromotionForPromotionTypeOrder extends TableRow implements
		OnClickListener {
	Context _context;
	View view;
	// STT
	TextView tvSTT;
	// code
	TextView tvPromotionCode;
	// description
	TextView tvPromotionDes;

	// promotion content
	TextView tvPromotionContent;

	// text view notify end row
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
	 * 
	 * @param context
	 * @param aRow
	 */
	public SelectPromotionForPromotionTypeOrder(Context context, View aRow) {
		super(context);
		tableParent = aRow;
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(
				R.layout.layout_select_promotion_for_promotion_type_order_row,
				this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvPromotionCode = (TextView) view.findViewById(R.id.tvPromotionCode);
		tvPromotionDes = (TextView) view.findViewById(R.id.tvPromotionDes);
		tvPromotionContent = (TextView) view
				.findViewById(R.id.tvPromotionContent);
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);

		myData = new OrderDetailViewDTO();
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
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
	public void renderLayout(int position, OrderDetailViewDTO item) {
		this.myData = item;
		tvNotifyNull.setVisibility(View.GONE);
		tvSTT.setText(String.valueOf(position));
		tvPromotionCode.setText(item.orderDetailDTO.programeCode);
		tvPromotionDes.setText(item.orderDetailDTO.programeName);
//		if(item.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER){
//			if(item.orderDetailDTO.discountPercentage > 0) {
//				tvPromotionContent.setText(String.valueOf(item.orderDetailDTO.discountPercentage + "%"));
//			} else {
//				tvPromotionContent.setText(StringUtil.parseAmountMoney(String.valueOf(item.orderDetailDTO.maxAmountFree)));
//			}
//		}
//		else if(item.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21){
//			tvPromotionContent.setText(item.promotionDescription);
//			
//		}
		tvPromotionContent.setText(item.promotionDescription);
	}

	/**
	 * 
	 * hien thi dong cuoi cung cua bang neu co du lieu, hoac hien thi thong bao
	 * null neu khong co du lieu nao
	 * 
	 * @author: HaiTC3
	 * @param message
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutEndRowNotSelectProgrameOrNull(String message) {
		this.myData = null;
		tvSTT.setVisibility(View.GONE);
		tvPromotionCode.setVisibility(View.GONE);
		tvPromotionDes.setVisibility(View.GONE);
		tvPromotionContent.setVisibility(View.GONE);
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setText(message);
	}

	public View getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == row && listener != null) {
			listener.handleVinamilkTableRowEvent(
					ActionEventConstant.ACTION_SELECT_PROMOTION_TYPE_PROMOTION_ORDER,
					tableParent, this.myData);
		}
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
		tvPromotionCode.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
		tvPromotionDes.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
		tvPromotionContent.setBackgroundColor(getResources().getColor(R.color.BG_ITEM_SELECT));
	}
}
