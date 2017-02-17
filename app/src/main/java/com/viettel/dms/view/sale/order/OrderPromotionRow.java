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

import com.viettel.dms.constants.Constants;
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
 * Row mat hang ban
 * 
 * @author: TruongHN
 * @version: 1.0
 * @since: 1.0
 */
public class OrderPromotionRow extends TableRow implements OnClickListener,
		TextWatcher {
	Context _context;
	int TYPE_ACTION_DELETE = 0;
	int TYPE_ACTION_CHANGE_PROMOTION_PRODUCT = 1;
	int TYPE_ACTION_CHANGE_PROMOTION_ORDER = 2;
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
	// so luong ton kho dang format thung/ hop
	TextView tvRemaindStockFormat;
	// tong
	EditText etTotal;
	// chiet khau
	TextView tvDiscount;
	// khuyen mai
	TextView tvPromo;
	// image view ivActionRow
	ImageView ivActionRow;
	// linerlayout action row
	LinearLayout llActionRow;
	// listener
	protected OnEventControlListener listener;
	// dto row
	private OrderDetailViewDTO rowDTO;
	// row
	private TableRow row;
	// textview max quantity
	private TextView tvMaxQuantityFree;
	// linear layout total
	private LinearLayout llTotal;
	int typeActionRow = -1;
	// check can change promotion type promotion for order
	boolean isAlowChange = false;
	// check when input money > allow value -> settext -> call afterTextChange again
	boolean hasResetAmount = false;
	

	public OrderPromotionRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.order_promotion_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode);
		tvProductCode.setOnClickListener(this);
		llMHTT = (LinearLayout) view.findViewById(R.id.llMHTT);
		tvMHTT = (TextView) view.findViewById(R.id.tvMHTT);
		tvMaxQuantityFree = (TextView) view
				.findViewById(R.id.tvMaxQuantityFree);
		tvRemaindStockFormat = (TextView) view
				.findViewById(R.id.tvRemaindStockFormat);
		tvProductName = (TextView) view.findViewById(R.id.tvProductName);
		etTotal = (EditText) view.findViewById(R.id.etTotal);
		etTotal.addTextChangedListener(this);
		etTotal.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus) {
					if(rowDTO != null && rowDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER) {
						etTotal.setText(StringUtil.parseAmountMoney(etTotal.getText().toString().trim().replace(",", "")));
					}
				}
			}
		});
		tvDiscount = (TextView) view.findViewById(R.id.tvDiscount);
		tvPromo = (TextView) view.findViewById(R.id.tvPromo);
		tvPromo.setOnClickListener(this);
		ivActionRow = (ImageView) view.findViewById(R.id.ivActionRow);
		ivActionRow.setOnClickListener(this);
		llActionRow = (LinearLayout) view.findViewById(R.id.llActionRow);
		llTotal = (LinearLayout) view.findViewById(R.id.llTotal);
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
	public void updateData(String STT, OrderDetailViewDTO dto) {
		updateData(STT, dto, true);
	}

	/**
	 * 
	 * Cap nhat data co kiem tra stock total
	 * @author: Nguyen Thanh Dung
	 * @param STT
	 * @param dto
	 * @param isCheckStockTotal
	 * @return: void
	 * @throws:
	 */
	public void updateData(String STT, OrderDetailViewDTO dto,
			boolean isCheckStockTotal) {
		rowDTO = dto;
		if (dto.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
			updateOrderForProduct(STT, dto, isCheckStockTotal);
		} else if (dto.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER
				|| dto.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
			updatePromotionForOrder(dto);
		} else if (dto.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER_TYPE_PRODUCT) {
			updatePromotionProductZV21(dto, isCheckStockTotal);
		}
	}

	/**
	* Mo ta muc dich ham
	* @author: TruongHN
	* @param dto
	* @param isCheckStockTotal
	* @return: void
	* @throws:
	*/
	private void updatePromotionProductZV21(OrderDetailViewDTO dto,
			boolean isCheckStockTotal) {
		tvSTT.setVisibility(View.GONE);

		tvProductCode.setText(dto.productCode);
		tvProductName.setText(dto.productName);
		if (StringUtil.isNullOrEmpty(dto.productName)) {
			tvProductName.setText(dto.typeName);
		}
		// etTotal.setText(StringUtil.parseAmountMoney(String.valueOf(dto.orderDetailDTO.quantity)));
		etTotal.setText(String.valueOf(dto.orderDetailDTO.quantity));
		tvMaxQuantityFree.setText(String.valueOf(dto.orderDetailDTO.maxQuantityFree));
		tvMaxQuantityFree.setVisibility(View.VISIBLE);
		if(GlobalInfo.getInstance().getAllowEditPromotion() == 1) {
			tvMaxQuantityFree.setText("(" + tvMaxQuantityFree.getText().toString() + ")");
			llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);	
		} else {
			etTotal.setVisibility(View.GONE);
			llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		}
		tvDiscount.setText("");
		tvPromo.setText(Constants.STR_BLANK);
		// HaiTC: hien thi so luong ton kho dang format thung / hop
		tvRemaindStockFormat.setText(dto.remaindStockFormat);
		
		//Cho phep doi sp KM hay ko?
		if (dto.changeProduct == 1) {
			typeActionRow = TYPE_ACTION_CHANGE_PROMOTION_PRODUCT;
			ivActionRow.setVisibility(View.VISIBLE);
			ivActionRow.setImageResource(R.drawable.icon_change_promotion);
			// ivAction.setImageBitmap(null);
		} else {
			ivActionRow.setVisibility(View.GONE);
		}
		// Cap nhat mau khi kiem tra ton kho
		if (isCheckStockTotal) {
			checkStockTotal(dto);
		}
	}

	/**
	* Render layout cho sp KM sp
	* @author: TruongHN
	* @param STT
	* @param dto
	* @param isCheckStockTotal
	* @return: void
	* @throws:
	*/
	private void updateOrderForProduct(String STT, OrderDetailViewDTO dto,
			boolean isCheckStockTotal) {
		tvSTT.setText(STT);
		tvProductCode.setText(dto.productCode);
		tvProductName.setText(dto.productName);
		if (StringUtil.isNullOrEmpty(dto.productName)) {
			tvProductName.setText(dto.typeName);
		}
		// etTotal.setText(StringUtil.parseAmountMoney(String.valueOf(dto.orderDetailDTO.quantity)));
		
		if (dto.orderDetailDTO.programeType == 0) {
			if(dto.type == OrderDetailViewDTO.FREE_PRODUCT) {
				etTotal.setText(String.valueOf(dto.orderDetailDTO.quantity));
				tvMaxQuantityFree.setText(String.valueOf(dto.orderDetailDTO.maxQuantityFree));
				// HaiTC: hien thi so luong ton kho dang format thung / hop
				tvRemaindStockFormat.setText(dto.remaindStockFormat);
			} else if(dto.type == OrderDetailViewDTO.FREE_PRICE) {
				etTotal.setText(StringUtil.parseAmountMoney(dto.orderDetailDTO.discountAmount));
				tvMaxQuantityFree.setText(StringUtil.parseAmountMoney(dto.orderDetailDTO.maxAmountFree));
				// HaiTC: hien thi so luong ton kho dang format thung / hop
				tvRemaindStockFormat.setText(Constants.STR_BLANK);
			} else if(dto.type == OrderDetailViewDTO.FREE_PERCENT) {
				etTotal.setText(StringUtil.parseAmountMoney(dto.orderDetailDTO.discountAmount));
				tvMaxQuantityFree.setText(StringUtil.parseAmountMoney(dto.orderDetailDTO.maxAmountFree));
				// HaiTC: hien thi so luong ton kho dang format thung / hop
				tvRemaindStockFormat.setText(Constants.STR_BLANK);

				if((int)dto.orderDetailDTO.discountPercentage == dto.orderDetailDTO.discountPercentage) {
					tvDiscount.setText((int)dto.orderDetailDTO.discountPercentage + "%");
				} else {
					tvDiscount.setText(dto.orderDetailDTO.discountPercentage + "%");	
				}
				
			}
			// neu la CTKM tu dong va cau hinh cho phep chinh sua
			tvMaxQuantityFree.setVisibility(View.VISIBLE);
			if(GlobalInfo.getInstance().getAllowEditPromotion() == 1) {
				tvMaxQuantityFree.setText("(" + tvMaxQuantityFree.getText().toString() + ")");
				llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);	
			} else {
				etTotal.setVisibility(View.GONE);
				llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			}
		} else {
			tvMaxQuantityFree.setText(String.valueOf(dto.orderDetailDTO.quantity));
			etTotal.setVisibility(View.GONE);
			llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		}
		
		tvPromo.setText(dto.orderDetailDTO.programeCode);
		// HaiTC: hien thi so luong ton kho dang format thung / hop
		tvRemaindStockFormat.setText(dto.remaindStockFormat);

		if (dto.orderDetailDTO.programeType == 2) {
			SpannableObject objCk = new SpannableObject();
			objCk.addSpan(dto.orderDetailDTO.programeCode,
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			tvPromo.setText(objCk.getSpan());

			tvPromo.setOnClickListener(null);
		}
		// neu la CTKM tu dong thi ko duoc xoa
		if (dto.orderDetailDTO.programeType != 0) {
			typeActionRow = TYPE_ACTION_DELETE;
			ivActionRow.setVisibility(View.VISIBLE);
			ivActionRow.setImageResource(R.drawable.icon_delete);
			// ivActionDelete.setImageBitmap(null);
		} else {
			if (dto.changeProduct == 1) {
				typeActionRow = TYPE_ACTION_CHANGE_PROMOTION_PRODUCT;
				ivActionRow.setVisibility(View.VISIBLE);
				ivActionRow.setImageResource(R.drawable.icon_change_promotion);
				// ivAction.setImageBitmap(null);
			} else {
				ivActionRow.setVisibility(View.GONE);
			}
		}
		

		// Cap nhat mau khi kiem tra ton kho
		if (isCheckStockTotal) {
			checkStockTotal(dto);
		}
	}

	/**
	* Mo ta muc dich ham
	* @author: TruongHN
	* @param dto
	* @return: void
	* @throws:
	*/
	private void updatePromotionForOrder(OrderDetailViewDTO dto) {
		tvSTT.setVisibility(View.GONE);
		// an di so luong ton kho
		tvRemaindStockFormat.setVisibility(View.GONE);
		// an di ma san pham va thong tin MHTT
		llMHTT.setVisibility(View.GONE);

		// hien thi title khuyen mai don hang
		LayoutParams currentParam = (LayoutParams) tvProductName.getLayoutParams();
		currentParam.width = GlobalUtil.dip2Pixel(337);
		tvProductName.setLayoutParams(currentParam);
		tvProductName.setText(StringUtil.getString(R.string.TEXT_TITLE_PROMOTIOIN_ORDER));
		tvProductName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);

		// cap nhat lai layout cho thong tin so tien khuyen mai
		LayoutParams lllayoutParamUpdate = new LayoutParams(
				GlobalUtil.dip2Pixel(312),
				LinearLayout.LayoutParams.WRAP_CONTENT);
		lllayoutParamUpdate.gravity = Gravity.CENTER;
		lllayoutParamUpdate.setMargins(GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1));
		llTotal.setLayoutParams(lllayoutParamUpdate);	

		// hien thi title khuyen mai don hang
		tvProductName.setText(StringUtil
				.getString(R.string.TEXT_TITLE_PROMOTIOIN_ORDER));

		// hien thi phan tram chiet khau
		if(dto.orderDetailDTO.discountPercentage > 0) {
			if((int)dto.orderDetailDTO.discountPercentage == dto.orderDetailDTO.discountPercentage) {
				tvDiscount.setText(String.valueOf((int)dto.orderDetailDTO.discountPercentage) + " %");
			} else {
				tvDiscount.setText(String.valueOf(dto.orderDetailDTO.discountPercentage) + " %");	
			} 
		} else {
			tvDiscount.setText(Constants.STR_BLANK);
		}
		
		if (dto.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ZV21) {
			etTotal.setVisibility(View.GONE);
			tvMaxQuantityFree.setVisibility(View.GONE);
		}else{
			// hien thi so tien chiet khau
			tvMaxQuantityFree.setText(StringUtil.parseAmountMoney(String.valueOf(dto.orderDetailDTO.maxAmountFree)));
			// hien thi so tien khuyen mai, mac dinh so tien khuyen mai = so tien chiet khau
			etTotal.setText(StringUtil.parseAmountMoney(String.valueOf(dto.orderDetailDTO.discountAmount)));
			GlobalUtil.setFilterInputMoney(etTotal,StringUtil.parseAmountMoney(String.valueOf(dto.orderDetailDTO.discountAmount)).length());
			
			if(GlobalInfo.getInstance().getAllowEditPromotion() == 1) {
				tvMaxQuantityFree.setText("(" + tvMaxQuantityFree.getText().toString() + ")");
				llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);	
			} else {
				etTotal.setVisibility(View.GONE);
				llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
			}
		}
		
		

		// hien thi CT HTTM
		tvPromo.setText(dto.orderDetailDTO.programeCode);
		if (dto.orderDetailDTO.programeType == 2) {
			SpannableObject objCk = new SpannableObject();
			objCk.addSpan(dto.orderDetailDTO.programeCode,
					ImageUtil.getColor(R.color.BLACK),
					android.graphics.Typeface.NORMAL);
			tvPromo.setText(objCk.getSpan());

			tvPromo.setOnClickListener(null);
		}
		if (isAlowChange) {
			ivActionRow.setImageResource(R.drawable.icon_refresh);
			ivActionRow.setVisibility(View.VISIBLE);
			typeActionRow = TYPE_ACTION_CHANGE_PROMOTION_ORDER;
		} else {
			ivActionRow.setVisibility(View.VISIBLE);
			ivActionRow.setImageDrawable(null);
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
		if (dto.orderDetailDTO.quantity > 0) {// San pham KM hang, SP KM tien
												// thi quantity = 0
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

				etTotal.setTextColor(ImageUtil
						.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
				tvDiscount.setTextColor(ImageUtil
						.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
				tvRemaindStockFormat.setTextColor(ImageUtil
						.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
				tvPromo.setTextColor(ImageUtil
						.getColor(R.color.COLOR_LOCATION_NAME));
				tvMaxQuantityFree.setTextColor(ImageUtil
						.getColor(R.color.TITLE_LIGHT_BLACK_COLOR));
			}
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

		etTotal.setTextColor(color);
		tvDiscount.setTextColor(color);
		tvRemaindStockFormat.setTextColor(color);
		tvPromo.setTextColor(color);
		tvMaxQuantityFree.setTextColor(color);
	}

	/**
	 * Tao row tong mat hang khuyen mai
	 * 
	 * @author: TruongHN
	 * @param total
	 * @param ck
	 * @return: void
	 * @throws:
	 */
	public void updateTotalPromotionRow(String total, String ck) {
		TableRow.LayoutParams param = new TableRow.LayoutParams();
		param.width = GlobalUtil.dip2Pixel(40);
		param.height = GlobalUtil.dip2Pixel(40);
		param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
				GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
		tvSTT.setLayoutParams(param);
		tvSTT.setText(" ");

		tvProductCode.setText(" ");
		SpannableObject obj = new SpannableObject();
		obj.addSpan("Tổng", ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvProductName.setText(obj.getSpan());
		LayoutParams currentParam = (LayoutParams) tvProductName
				.getLayoutParams();
		currentParam.width = GlobalUtil.dip2Pixel(317);
		tvProductName.setLayoutParams(currentParam);

		SpannableObject objTotal = new SpannableObject();
		objTotal.addSpan(StringUtil.parseAmountMoney(total),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvMaxQuantityFree.setText(objTotal.getSpan());
		llTotal.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
		SpannableObject objCk = new SpannableObject();
		objCk.addSpan(StringUtil.parseAmountMoney(ck),
				ImageUtil.getColor(R.color.BLACK),
				android.graphics.Typeface.BOLD);
		tvDiscount.setText(objCk.getSpan());
		tvPromo.setText(" ");
		ivActionRow.setImageBitmap(null);
		etTotal.setVisibility(View.GONE);
		tvRemaindStockFormat.setVisibility(View.GONE);
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
		} else if (arg0 == tvPromo && listener != null) {
			listener.onEvent(OrderView.ACTION_VIEW_PROMOTION, this, rowDTO);
		} else if (arg0 == ivActionRow && listener != null
				&& typeActionRow == TYPE_ACTION_CHANGE_PROMOTION_PRODUCT) {
			listener.onEvent(OrderView.ACTION_CHANGE_PROMOTION, this, rowDTO);
		} else if (arg0 == ivActionRow && listener != null
				&& typeActionRow == TYPE_ACTION_DELETE) {
			listener.onEvent(OrderView.ACTION_DELETE_PROMOTION, this, rowDTO);
		} else if (arg0 == row && _context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) _context);
		} else if (arg0 == ivActionRow && listener != null
				&& typeActionRow == TYPE_ACTION_CHANGE_PROMOTION_ORDER) {
			listener.onEvent(
					OrderView.ACTION_CHANGE_PROMOTION_FOR_PROMOTIOIN_ORDER,
					this, rowDTO);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable arg0) {
		//KM cho don hang: ZV19, ZV20 hoac KM cho sp: KM %, KM amount
		if (rowDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_ORDER || 
				(rowDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT && (rowDTO.type == OrderDetailViewDTO.FREE_PRICE || rowDTO.type == OrderDetailViewDTO.FREE_PERCENT))) {
			// chi khac khi day la khuyen mai loai khuyen mai theo don hang
			long moneyKM = 0;
			if (etTotal.getText().toString().length() > 0) {
				moneyKM = Long.valueOf(etTotal.getText().toString().replace(",", ""));
			}
			if (rowDTO != null) {
				if(hasResetAmount == true) {
					hasResetAmount = false;
				} else {
					rowDTO.oldDiscountAmount = rowDTO.orderDetailDTO.discountAmount;
					if (moneyKM > rowDTO.orderDetailDTO.maxAmountFree) {
						hasResetAmount = true;
						etTotal.setText(StringUtil.parseAmountMoney(String
								.valueOf(rowDTO.orderDetailDTO.maxAmountFree)));
						rowDTO.orderDetailDTO.discountAmount = rowDTO.orderDetailDTO.maxAmountFree;
					} else {
						rowDTO.orderDetailDTO.discountAmount = moneyKM;
					}
					
					listener.onEvent(OrderView.ACTION_CHANGE_REAL_MONEY_PROMOTION, this, rowDTO);
				}
			}
		} else {
			int realOrder = 0;
			if (etTotal.getText().toString().length() > 0) {
				realOrder = Integer.valueOf(etTotal.getText().toString());
			}
			if (rowDTO != null && rowDTO.orderDetailDTO != null
					&& realOrder != rowDTO.orderDetailDTO.quantity) {
				if (realOrder > rowDTO.orderDetailDTO.maxQuantityFree) {
					etTotal.setText(String
							.valueOf(rowDTO.orderDetailDTO.maxQuantityFree));
				} else {
					ArrayList<Integer> param = new ArrayList<Integer>();
					param.add(rowDTO.numberOrderView);
					param.add(realOrder);
					// update row total
					if (rowDTO.promotionType == OrderDetailViewDTO.PROMOTION_FOR_PRODUCT) {
						listener.onEvent(OrderView.ACTION_CHANGE_REAL_PROMOTION, this, param);
					} else {
						listener.onEvent(OrderView.ACTION_CHANGE_REAL_PROMOTION_ORDER, this, param);
					}
				}
			}
		}
	}
//	public String formatAmount(long num) 
//	{
//	    DecimalFormat decimalFormat = new DecimalFormat();
//	    DecimalFormatSymbols decimalFormateSymbol = new DecimalFormatSymbols();
//	    decimalFormateSymbol.setGroupingSeparator(',');
//	    decimalFormat.setDecimalFormatSymbols(decimalFormateSymbol);
//	    return decimalFormat.format(num);
//	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence,
	 * int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int,
	 * int, int)
	 */
	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		// tinh toan lai so luong gia

	}
}
