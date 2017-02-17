/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.FindProductSaleOrderDetailViewDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * display row product when research
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class FindProductAddOrderListRow extends TableRow implements
		OnClickListener, TextWatcher {
	public static final int MAX_LENGTH = 9;
	public static final int MAX_LENGTH_WITH_COMMA = 11;
	
	private final Context _context;
	private final View view;
	// STT
	TextView tvSTT;
	// code
	TextView tvMMH;
	// text view hight light gp / *
	TextView tvGSNPP;
	// linnerlayout product code
	LinearLayout llProductCode;
	// Name
	public TextView tvMHName;
	// price
//	TextView tvPrice;
	// gia
	EditText etPrice;
	// inventory
	TextView tvStockTotal;
	// convfact
	TextView tvConvfactName;
	// Image view CTKM
	ImageView ivCTKM;
	// number
	EditText etNumber;
	// text view notify null
	LinearLayout llPrice;
	LinearLayout llEditText;
	// listener
	protected VinamilkTableListener listener;
	// linerlayout CTKM
	LinearLayout llCTKM;
	// linner layout detail CT
	LinearLayout llCTDetail;
	// image button CT
	ImageButton imbtCTDetail;
	// text view CT code
	TextView tvCTDetail;
	// data to render layout for row
	FindProductSaleOrderDetailViewDTO myData;

	public FindProductAddOrderListRow(Context con){
		super(con);
		_context = con;
		LayoutInflater vi = (LayoutInflater) _context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_find_product_add_orderlist_row, this);
	}
	
	/**
	 * constructor for class
	 * 
	 * @param context
	 * @param aRow
	 */
	public FindProductAddOrderListRow(Context context, VinamilkTableListener listen) {
		this(context);
		listener=listen;
		setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvMMH = (TextView) view.findViewById(R.id.tvMMH);
		tvGSNPP = (TextView) view.findViewById(R.id.tvGSNPP);
		llProductCode = (LinearLayout) view.findViewById(R.id.llProductCode);
		tvMHName = (TextView) view.findViewById(R.id.tvMHName);
		tvCTDetail = (TextView) view.findViewById(R.id.tvCTDetail);
		tvCTDetail.setOnClickListener(this);
		tvConvfactName = (TextView) view.findViewById(R.id.tvConvfactName);
		tvStockTotal = (TextView) view.findViewById(R.id.tvStockTotal);
		ivCTKM = (ImageView) view.findViewById(R.id.ivCTKM);
		ivCTKM.setOnClickListener(this);

		etPrice = (EditText) view.findViewById(R.id.etPrice);
		etPrice.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					etPrice.setSelection(etPrice.getText().length());
					GlobalUtil.setFilterInputPrice(etPrice, MAX_LENGTH);
				} else {
					GlobalUtil.setFilterInputPrice(etPrice, MAX_LENGTH_WITH_COMMA);
					String suggestedPrice = etPrice.getText().toString().trim().replace(",", "");
					if(!StringUtil.isNullOrEmpty(suggestedPrice)) {
						long price = Long.parseLong(suggestedPrice);
						myData.suggestedPrice = String.valueOf(price);
						etPrice.setText(StringUtil.parseAmountMoney(price));
					} else {
						myData.suggestedPrice = suggestedPrice;
						etPrice.setText(myData.suggestedPrice);
					}
				}
			}
		});
		etNumber = (EditText) view.findViewById(R.id.etNumber);
		etNumber.addTextChangedListener(this);
		llPrice = (LinearLayout) view.findViewById(R.id.llPrice);
		llEditText = (LinearLayout) view.findViewById(R.id.llEditText);
		llCTKM = (LinearLayout) view.findViewById(R.id.llCTKM);
		llCTDetail = (LinearLayout) view.findViewById(R.id.llCTDetail);
		myData = new FindProductSaleOrderDetailViewDTO();
		imbtCTDetail = (ImageButton) view.findViewById(R.id.imbtCTDetail);
		imbtCTDetail.setOnClickListener(this);
		
		//Khong hien thi gia
		if (GlobalInfo.getInstance().getIsShowPrice() == 0) {
			llPrice.setVisibility(View.GONE);
			TableRow.LayoutParams param = new TableRow.LayoutParams();
			param.width = GlobalUtil.dip2Pixel(289 + 95);
			param.height = GlobalUtil.dip2Pixel(42);
			param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
			tvMHName.setLayoutParams(param);
		}
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
			FindProductSaleOrderDetailViewDTO item, boolean isRequestFocus) {
		this.myData = item;
		tvSTT.setText(String.valueOf(position));
		llProductCode.setVisibility(View.VISIBLE);
		tvMMH.setText(item.productCode);
		String messageOrder = Constants.STR_BLANK;
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

		tvMHName.setText(item.productName);

		// hien thi ten quy cach
		tvConvfactName.setText(String.valueOf(item.uom2.trim()));

		// hien thi so luong ton kho co the dat hang da format
		tvStockTotal.setText(StringUtil.parseAmountMoney(String.valueOf(item.available_quantity)));

		if (item.hasPromotionProgrameCode) {
			ivCTKM.setVisibility(View.VISIBLE);
		} else {
			ivCTKM.setVisibility(View.INVISIBLE);
		}

		if (item.hasSelectPrograme) {
			imbtCTDetail.setVisibility(View.GONE);
			tvCTDetail.setVisibility(View.VISIBLE);
			tvCTDetail.setText(item.saleOrderDetail.programeCode);
		} else {
			imbtCTDetail.setVisibility(View.VISIBLE);
			tvCTDetail.setVisibility(View.GONE);
		}

		etPrice.setText(StringUtil.parseAmountMoney(item.suggestedPrice));
		if (!StringUtil.isNullOrEmpty(item.numProduct)
				&& !item.numProduct.equals("0")) {
			etNumber.setText(String.valueOf(item.numProduct));
		}

		if (item.quantity <= 0 || item.available_quantity <= 0) {
			this.updateColorForTextRow(ImageUtil.getColor(R.color.RED));
		}
		if (isRequestFocus) {
			 etNumber.requestFocus();
		}
	}

	/**
	 * 
	 * set color for all text in row
	 * 
	 * @param color
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Oct 23, 2012
	 */
	public void updateColorForTextRow(int color) {
		tvSTT.setTextColor(color);
		tvMMH.setTextColor(color);
		tvMHName.setTextColor(color);
		etPrice.setTextColor(color);
		tvStockTotal.setTextColor(color);
		tvConvfactName.setTextColor(color);
		etNumber.setTextColor(color);
		tvCTDetail.setTextColor(color);
		tvGSNPP.setTextColor(color);
	}

	/**
	 * 
	 * update colum programe after select programe for product
	 * 
	 * @author: HaiTC3
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void updateLayout(FindProductSaleOrderDetailViewDTO item) {
		if (item.hasSelectPrograme) {
			imbtCTDetail.setVisibility(View.GONE);
			tvCTDetail.setVisibility(View.VISIBLE);
			tvCTDetail.setText(item.saleOrderDetail.programeCode);
		} else {
			imbtCTDetail.setVisibility(View.VISIBLE);
			tvCTDetail.setVisibility(View.GONE);
		}
	}

	public View getView() {
		return view;
	}

	@Override
	public void onClick(View v) {
		
		if (v == ivCTKM) {
			listener.handleVinamilkTableRowEvent(
					ActionEventConstant.GO_TO_PROMOTION_PROGRAME_DETAIL,
					ivCTKM, myData);
		} else if (v == imbtCTDetail || v == tvCTDetail) {
			listener.handleVinamilkTableRowEvent(
					ActionEventConstant.GO_TO_CT_DETAIL, this, myData);
		} else if (v == this && _context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) _context);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
		
		int numProductRealOrder = GlobalUtil.calRealOrder(etNumber.getText()
				.toString().trim(), this.myData.convfact);
		if (this.myData.quantity > 0 && this.myData.available_quantity > 0) {
			if (numProductRealOrder > this.myData.available_quantity) {
				this.updateColorForTextRow(ImageUtil.getColor(R.color.OGRANGE));
			} else {
				this.updateColorForTextRow(ImageUtil.getColor(R.color.BLACK));
				tvGSNPP.setTextColor(ImageUtil.getColor(R.color.RED));
			}
		}
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		

	}

}
