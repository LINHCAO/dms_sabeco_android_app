/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

/**
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import java.util.ArrayList;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.OrderDetailViewDTO;
import com.viettel.dms.global.GlobalInfo;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Row mat hang khuyen mai
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class OrderProductRow extends TableRow implements OnClickListener {
	public static final int MAX_LENGTH = 9;
	public static final int MAX_LENGTH_WITH_COMMA = 11;
	Context _context;
	View view;
	// so thu tu
	TextView tvSTT;
	// ma mat hang
	TextView tvProductCode;
	// Linear Layout chua product code & dau *
	LinearLayout llMHTT;
	// Dau * cho MHTT (Mat hang trong tam)
	TextView tvMHTT;
	// ten mat hang
	TextView tvProductName;
	TextView tvUOM;
	// remaind product
	TextView tvRemainProduct;
	// gia
	EditText etPrice;
	// thuc dat
	EditText etRealOrder;
	// text thuc dat
	TextView tvRealOrder;
	// thanh tien
	TextView tvAmount;
	// khuyen mai
	ImageView ivPromo;
	// linearLayout promo
	LinearLayout llPromo;
	// action
	ImageView ivAction;
	// linearLayout delete
	LinearLayout llActionDelete;
	// listener
	protected OnEventControlListener listener;
	// linear thuc dat
	LinearLayout llRealOrder;
	LinearLayout llPrice;
	// dto row
	OrderDetailViewDTO rowDTO;
	// row
	private TableRow row;
	
	TextWatcher twPrice = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable arg0) {
//			long price = 0;
			String suggestedPrice = "";
			if (etPrice.getText().toString().trim().length() > 0) {
//				price = Long.valueOf(etPrice.getText().toString().trim().replace(",", ""));
				suggestedPrice = etPrice.getText().toString().trim().replace(",", "");
			}
			if (rowDTO != null && rowDTO.orderDetailDTO != null && !suggestedPrice.equals(rowDTO.suggestedPrice)) {
				ArrayList<String> param = new ArrayList<String>();
				param.add("" + rowDTO.numberOrderView);
				param.add(suggestedPrice);
				listener.onEvent(OrderView.ACTION_CHANGE_REAL_PRICE, OrderProductRow.this, param);
			}			
		}
	};
	
	TextWatcher twRealOrder = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			String realOrder = "";
			if (etRealOrder.getText().toString().trim().length() > 0) {
				realOrder = etRealOrder.getText().toString().trim();
			}
			if (rowDTO != null && rowDTO.orderDetailDTO != null && !realOrder.equals(rowDTO.quantity)) {
				rowDTO.quantity = realOrder;
				// update row total
				ArrayList<String> param = new ArrayList<String>();
				param.add("" + rowDTO.numberOrderView);
				param.add(realOrder);
				listener.onEvent(OrderView.ACTION_CHANGE_REAL_ORDER, OrderProductRow.this, param);
			}			
		}
	};
	
	public OrderProductRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.order_product_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);

		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
		tvProductCode.setOnClickListener(this);

		tvMHTT = (TextView) view.findViewById(R.id.tvMHTT);
		llMHTT = (LinearLayout) view.findViewById(R.id.llMHTT);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		tvUOM = (TextView) view.findViewById(R.id.tvUOM);
		tvRemainProduct = (TextView) view.findViewById(R.id.tvRemainProduct);
		etPrice = (EditText) view.findViewById(R.id.etPrice);
		etPrice.addTextChangedListener(twPrice);
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
						rowDTO.suggestedPrice = String.valueOf(price);
						etPrice.setText(StringUtil.parseAmountMoney(price));
					} else {
						rowDTO.suggestedPrice = suggestedPrice;
						etPrice.setText(rowDTO.suggestedPrice);
					}
				}
			}
		});
		etRealOrder = (EditText) view.findViewById(R.id.etRealOrder1);
		tvRealOrder = (TextView) view.findViewById(R.id.tvRealOrder);
//		GlobalUtil.setFilterInputConvfact(etRealOrder,
//				Constants.MAX_LENGHT_QUANTITY);
		// etRealOrder.setFreezesText(true);
		// tvRealOrder.setVisibility(View.GONE);
		etRealOrder.addTextChangedListener(twRealOrder);
		etRealOrder.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					etRealOrder.setSelection(etRealOrder.getText().length());
				}
			}
		});
		tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		ivPromo = (ImageView) view.findViewById(R.id.ivPromo);
		ivPromo.setOnClickListener(this);
		llPromo = (LinearLayout) view.findViewById(R.id.llPromo);
		ivAction = (ImageView) view.findViewById(R.id.ivAction);
		llActionDelete = (LinearLayout) view.findViewById(R.id.llActionDelete);
		ivAction.setOnClickListener(this);
		// ivAction.setVisibility(View.GONE);
		llRealOrder = (LinearLayout) view.findViewById(R.id.llRealOrder);
		llPrice = (LinearLayout) view.findViewById(R.id.llPrice);
		
		//Khong hien thi gia
		if (GlobalInfo.getInstance().getIsShowPrice() == 0) {
			llPrice.setVisibility(View.GONE);
			tvAmount.setVisibility(View.GONE);
			TableRow.LayoutParams param = new TableRow.LayoutParams();
			param.width = GlobalUtil.dip2Pixel(193 + 102 + 131);
			param.height = GlobalUtil.dip2Pixel(42);
			param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
			tvProductName.setLayoutParams(param);
		}
	}

	public void setListner(OnEventControlListener listener) {
		this.listener = listener;
	}

	/**
	 * Cap nhat data
	 * 
	 * @author: TruongHN
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	public void updateData(OrderDetailViewDTO dto) {
		updateData(dto, true);
	}

	/**
	 * 
	 * Cap nhat da ta co kiem tra stock total
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @param isCheckStockTotal
	 * @return: void
	 * @throws:
	 */
	public void updateData(OrderDetailViewDTO dto, boolean isCheckStockTotal) {
		rowDTO = dto;
		tvSTT.setText(String.valueOf(rowDTO.numberOrderView));
		tvProductCode.setText(rowDTO.productCode);
		tvProductName.setText(rowDTO.productName);
		tvUOM.setText(rowDTO.uom2);

		//HaiTC: display remain product
		tvRemainProduct.setText(dto.remaindStockFormat);

		etPrice.setText(StringUtil.parseAmountMoney(rowDTO.suggestedPrice));
		if (StringUtil.isNullOrEmpty(rowDTO.orderDetailDTO.programeCode)) {
			ivPromo.setImageBitmap(null);
		}
		
		
		if (rowDTO.orderDetailDTO.amount >= 0) {
			tvAmount.setText(StringUtil.parseAmountMoney(rowDTO.orderDetailDTO.amount));
		}

		if (!StringUtil.isNullOrEmpty(rowDTO.quantity)) {
			etRealOrder.setTextKeepState(String.valueOf(rowDTO.quantity));
		} else {
			etRealOrder.setText("0");
		}

		if (dto.isFocus > 0) {
			tvMHTT.setVisibility(View.VISIBLE);
			tvMHTT.setText("*");
		}

		// Cap nhat mau khi kiem tra ton kho
		if (isCheckStockTotal) {
			checkStockTotal(dto);
		}
	}

	/**
	 * Kiem tra ton kho
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param dto
	 * @return: void
	 * @throws:
	 */

	public void checkStockTotal(OrderDetailViewDTO dto) {
		if (dto.stock <= 0) {
			updateRowWithColor(ImageUtil.getColor(R.color.RED));
		} else if (dto.totalOrderQuantity > dto.stock) {
			updateRowWithColor(ImageUtil.getColor(R.color.OGRANGE));
		} else {
			tvSTT.setTextColor(ImageUtil
					.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
			tvProductCode.setTextColor(ImageUtil
					.getColor(R.color.COLOR_LOCATION_NAME));
			tvProductName.setTextColor(ImageUtil
					.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
			tvUOM.setTextColor(ImageUtil
					.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
			etRealOrder.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvMHTT.setTextColor(ImageUtil.getColor(R.color.RED));

			etPrice.setTextColor(ImageUtil
					.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
			tvRemainProduct.setTextColor(ImageUtil
					.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
			tvAmount.setTextColor(ImageUtil
					.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
		}
	}

	/**
	 * 
	 * Cap nhat full mau cho 1 row
	 * 
	 * @author: Nguyen Thanh Dung
	 * @param color
	 * @return: void
	 * @throws:
	 */
	private void updateRowWithColor(int color) {
		tvSTT.setTextColor(color);
		tvProductCode.setTextColor(color);
		tvProductName.setTextColor(color);
		tvUOM.setTextColor(color);
		etRealOrder.setTextColor(color);
		tvMHTT.setTextColor(color);

		etPrice.setTextColor(color);
		tvRemainProduct.setTextColor(color);
		tvAmount.setTextColor(color);
	}

	/**
	 * Cap nhat lai STT
	 * 
	 * @author: TruongHN
	 * @param numberRow
	 * @return: void
	 * @throws:
	 */
	public void updateNumberRow(int numberRow) {
		if (rowDTO != null) {
			rowDTO.numberOrderView = numberRow;
		}
		tvSTT.setText(String.valueOf(numberRow));
	}

	/**
	 * Tao row tong - table mat hang ban
	 * 
	 * @author: TruongHN
	 * @param price
	 * @param realOrder
	 * @return: void
	 * @throws:
	 */
	public void updateTotalRow(String total, String sku) {
		// column empty
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(748);
		param.height = GlobalUtil.dip2Pixel(40);
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvSTT.setLayoutParams(param);
		SpannableObject obj = new SpannableObject();
		obj.addSpan(StringUtil.getString(R.string.TOTAL),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvSTT.setText(obj.getSpan());
		// tvProductCode.setVisibility(View.GONE);
		tvProductName.setVisibility(View.GONE);
		tvUOM.setVisibility(View.GONE);
		llMHTT.setVisibility(View.GONE);
		etPrice.setVisibility(View.GONE);
		tvRemainProduct.setVisibility(View.GONE);

		// column sku
		llPrice.setVisibility(View.GONE);
		llRealOrder.setVisibility(View.GONE);
//		tvRealOrder.setVisibility(View.GONE);
		SpannableObject objSku = new SpannableObject();
		objSku.addSpan(StringUtil.parseAmountMoney(sku),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvRealOrder.setText(objSku.getSpan());
		// tvRealOrder.setText(sku);
		etRealOrder.setVisibility(View.GONE);

		TableRow.LayoutParams param2 = new TableRow.LayoutParams();
		param2.width = GlobalUtil.dip2Pixel(211);
		param2.height = LayoutParams.MATCH_PARENT;
		param2.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvAmount.setLayoutParams(param2);
		tvAmount.setGravity(Gravity.CENTER);
		// column thanh tien
		SpannableObject objAmount = new SpannableObject();
		objAmount.addSpan(StringUtil.parseAmountMoney(total),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvAmount.setText(objAmount.getSpan());

		// tvAmount.setText(StringUtil.parseAmountMoney(total));
		// column empty
		llActionDelete.setVisibility(View.GONE);
		llPromo.setVisibility(View.GONE);

	}

	/**
	 * cap nhat row tong
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void updateTotalValue(String total, String sku) {
		// column thanh tien
		SpannableObject objTotal = new SpannableObject();
		objTotal.addSpan(StringUtil.parseAmountMoney(total),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);

		tvAmount.setText(objTotal.getSpan());
		// column sku
		SpannableObject objSku = new SpannableObject();
		objSku.addSpan(StringUtil.parseAmountMoney(sku),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvRealOrder.setText(objSku.getSpan());
	}

	/**
	 * Cap nhat thanh tien
	 * 
	 * @author: TruongHN
	 * @param amount
	 * @return: void
	 * @throws:
	 */
	public void updateAmount(String amount) {
		// column thanh tien
		tvAmount.setText(StringUtil.parseAmountMoney(amount));

		// Cap nhat mau khi kiem tra ton kho
		checkStockTotal(rowDTO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if (arg0 == tvProductCode && listener != null) {
			listener.onEvent(OrderView.ACTION_VIEW_PRODUCT, this, rowDTO);
		} else if (arg0 == ivPromo && listener != null) {
			listener.onEvent(OrderView.ACTION_VIEW_PROMOTION, this, rowDTO);
		} else if (arg0 == ivAction && listener != null) {
			if (rowDTO != null) {
				listener.onEvent(OrderView.ACTION_DELETE, this, rowDTO);
			}
		} else if (arg0 == row && _context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) _context);
		}
	}
}
