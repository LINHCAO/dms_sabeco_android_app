/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.training;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * thong tin sku can bo xung vao danh gia huan luyen NVBH
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class ProductNeedAddToReviewStaffRow extends TableRow implements
		OnClickListener {

	Context _context;
	View view;
	// STT
	TextView tvSTT;
	// code
	TextView tvProductCode;
	// text view hight light gp / *
	TextView tvGSNPP;
	// linnerlayout product code
	LinearLayout llProductCode;
	// Name
	public TextView tvProductName;
	// price
	TextView tvPrice;
	// Image view CTKM
	ImageView ivPromotion;
	// linner layout CTKM
	LinearLayout llCTKM;
	// check box select product
	CheckBox cbSelected;
	// linerlayout CTKM
	LinearLayout llCheckBoxSelect;
	// text view notify null
	TextView tvNotifyNull;

	// listener
	protected VinamilkTableListener listener;
	// table parent
	private View tableParent;
	// row object
	private TableRow row;
	// data to render layout for row
	FindProductSaleOrderDetailViewDTO myData;

	/**
	 * constructor for class
	 * 
	 * @param context
	 * @param aRow
	 */
	public ProductNeedAddToReviewStaffRow(Context context, View aRow) {
		super(context);
		tableParent = aRow;
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_select_product_need_add_sale_row,
				this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
		tvGSNPP = (TextView) view.findViewById(R.id.tvGSNPP);
		llProductCode = (LinearLayout) view.findViewById(R.id.llProductCode);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvPrice = (TextView) view.findViewById(R.id.tvPrice);
		ivPromotion = (ImageView) view.findViewById(R.id.ivPromotion);
		ivPromotion.setOnClickListener(this);
		llCTKM = (LinearLayout) view.findViewById(R.id.llCTKM);
		llCheckBoxSelect = (LinearLayout) view
				.findViewById(R.id.llCheckBoxSelect);
		cbSelected = (CheckBox) view.findViewById(R.id.cbSelected);
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);
		myData = new FindProductSaleOrderDetailViewDTO();
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	 * display notify list empty
	 * 
	 * @author: HaiTC3
	 * @param strNotifyContent
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutNotify(String strNotifyContent) {
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setText(strNotifyContent);
		llProductCode.setVisibility(View.GONE);
		tvSTT.setVisibility(View.GONE);
		tvProductCode.setVisibility(View.GONE);
		tvProductName.setVisibility(View.GONE);
		tvPrice.setVisibility(View.GONE);
		ivPromotion.setVisibility(View.GONE);
		llCTKM.setVisibility(View.GONE);
		llCheckBoxSelect.setVisibility(View.GONE);
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
	public void renderLayout(int position,
			FindProductSaleOrderDetailViewDTO item) {
		this.myData = item;
		tvSTT.setText(String.valueOf(position));
		llProductCode.setVisibility(View.VISIBLE);
		tvProductCode.setText(item.productCode);
		String messageOrder = "";
		if (item.mhTT == 1 && item.gsnppRequest == 1) {
			messageOrder = "*,GS";
		} else if (item.mhTT == 1) {
			messageOrder = "*";
		} else if (item.gsnppRequest == 1) {
			messageOrder = "GS";
		}

		if (!StringUtil.isNullOrEmpty(messageOrder)) {
			tvGSNPP.setVisibility(View.VISIBLE);
			tvGSNPP.setText(messageOrder);
		} else {
			tvGSNPP.setVisibility(View.GONE);
		}

		tvProductName.setText(item.productName);

		tvPrice.setText(StringUtil.parseAmountMoney(String
				.valueOf(item.saleOrderDetail.price)));

		if (item.hasPromotionProgrameCode) {
			ivPromotion.setVisibility(View.VISIBLE);
		} else {
			ivPromotion.setVisibility(View.INVISIBLE);
		}

		// thiet lap trang thai checked or unchecked
		if (item.isSelected) {
			cbSelected.setChecked(true);
		} else {
			cbSelected.setChecked(false);
		}

		if (item.available_quantity <= 0) {
			tvSTT.setTextColor(getResources().getColor(R.color.RED));
			tvProductCode.setTextColor(getResources().getColor(R.color.RED));
			tvProductName.setTextColor(getResources().getColor(R.color.RED));
			tvPrice.setTextColor(getResources().getColor(R.color.RED));
		}
	}

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == ivPromotion) {
			listener.handleVinamilkTableRowEvent(
					ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL,
					tableParent, myData);
		}
	}
}
