/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.OpProductDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.HeaderTableInfo;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Row cap nhat san luong bia doi thu/bia sai gon
 * 
 * @author: DungNT19
 * @version: 1.0 
 * @since:  14:30:59 29-05-2014
 */
public class CompetitorSaleRow extends TableRow implements TextWatcher,
		OnClickListener {

	private final Context con;
	private final View v;
	private int type;
	private final GlobalBaseActivity parent;
	private View[] arrView;
	//So thu tu
	private TextView tvSTTCompet;
	// ma mat hang
	private TextView tvMMHCompet;
	//ten mat hang
	private TextView tvMHNameCompet;
	// don vi tinh
	private TextView tvConvfactNameCompet;
	// input san luong ban
	private EditText etNumberSaleCompet;
	// input gia
	private EditText etPriceCompet;
	// row
	private TableRow rowCompetitor;
	// layout parent input san luong ban
	private LinearLayout llEditTextCompet;
	// layout input gia
	private LinearLayout llEditTextPriceCompet;
	// DTO row
	private OpProductDTO opProductDTO;
	private boolean allowChangeQuantity;

	public static final int MAX_LENGTH = 18;
	public static final int MAX_LENGTH_WITH_COMMA = 20;

//	private CompetitorSaleRow(Context context) {
//		super(context);
//		con = context;
//		parent = (GlobalBaseActivity) con;
//
//		LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		v = vi.inflate(R.layout.activity_competitor_sale_row, this, false);
//	}

	public CompetitorSaleRow(Context context, int type, boolean allowChangeQuantity) {
		super(context);
		con = context;
		parent = (GlobalBaseActivity) con;
		LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		v = type == 1 ? vi.inflate(R.layout.activity_competitor_sale_price_row, this, false) : vi.inflate(R.layout.activity_competitor_sale_row, this, false);

		this.type = type;
		this.allowChangeQuantity = allowChangeQuantity;
		initView(v);
	}

	/**
	 * render layout
	 * 
	 * @author: dungdq3
	 * @param:
	 * @return: void
	 */
	public void renderLayout(int pos, OpProductDTO opProductDTO) {

		this.opProductDTO = opProductDTO;
		tvSTTCompet.setText("" + pos);
		tvMMHCompet.setText(opProductDTO.getProductCode());
		tvMHNameCompet.setText(opProductDTO.getProductName());
		if (!StringUtil.isNullOrEmpty(opProductDTO.getUOM2())) {
			tvConvfactNameCompet.setText(opProductDTO.getUOM2().trim());
		} else {
			tvConvfactNameCompet.setText("");
		}
		
		if(opProductDTO.isInserted()){
			etNumberSaleCompet.setText(opProductDTO.getOldQuantity() + "");
		}else{
			etNumberSaleCompet.setText(Constants.STR_BLANK);
		}

		if(type == 1) {
			if (opProductDTO.isInsertedPrice()) {
				etPriceCompet.setText(StringUtil.parseAmountMoney(opProductDTO.getOldPrice()));
			} else {
				etPriceCompet.setText("");
			}
		}
		if(opProductDTO.actionType==1){
			etNumberSaleCompet.setEnabled(false);
			if(type == 1) {
				etPriceCompet.setEnabled(false);
			}
		}
		 
		// etNumberSaleCompet.requestFocus();
	}

	/**
	 * Khởi tạo màn hình
	 * 
	 * @author: dungdq3
	 * @param: View v
	 * @return: void
	 */
	private void initView(View v) {
		setDrawingCacheEnabled(true);
		buildDrawingCache();
		rowCompetitor = (TableRow) v.findViewById(R.id.rowCompetitor);
		llEditTextCompet = (LinearLayout) v.findViewById(R.id.llEditTextCompet);
		tvSTTCompet = (TextView) v.findViewById(R.id.tvSTTCompet);
		tvMMHCompet = (TextView) v.findViewById(R.id.tvMMHCompet);
		tvMHNameCompet = (TextView) v.findViewById(R.id.tvMHNameCompet);
		tvConvfactNameCompet = (TextView) v.findViewById(R.id.tvConvfactNameCompet);
		etNumberSaleCompet = (EditText) v.findViewById(R.id.etNumberSaleCompet);
		etNumberSaleCompet.addTextChangedListener(this);
		if(type == 1) {
			llEditTextPriceCompet = (LinearLayout) v.findViewById(R.id.llEditTextPriceCompet);
			etPriceCompet = (EditText) v.findViewById(R.id.etPriceCompet);
			etPriceCompet.addTextChangedListener(this);
			etPriceCompet.setOnFocusChangeListener(new OnFocusChangeListener() {

				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						etPriceCompet.setSelection(etPriceCompet.getText().length());
						GlobalUtil.setFilterInputPrice(etPriceCompet, MAX_LENGTH);
					} else {
						GlobalUtil.setFilterInputPrice(etPriceCompet, MAX_LENGTH_WITH_COMMA);
						String suggestedPrice = etPriceCompet.getText().toString().trim().replace(",", "");
						if (!StringUtil.isNullOrEmpty(suggestedPrice)) {
							long price = Long.parseLong(suggestedPrice);
//							myData.suggestedPrice = String.valueOf(price);
							etPriceCompet.setText(StringUtil.parseAmountMoney(price));
						} else {
//							myData.suggestedPrice = suggestedPrice;
							etPriceCompet.setText(suggestedPrice);
						}
					}
				}
			});
		}
		setOnClickListener(this);

		rowCompetitor.removeAllViews();
		arrView = type == 1 ? new View[] { tvSTTCompet, tvMMHCompet, tvMHNameCompet,
				tvConvfactNameCompet, llEditTextCompet, llEditTextPriceCompet } : new View[] { tvSTTCompet, tvMMHCompet, tvMHNameCompet,
				tvConvfactNameCompet, llEditTextCompet };

		for (View vi : arrView) {
			addView(vi);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if(!StringUtil.isNullOrEmpty(etNumberSaleCompet.getText().toString()) && !allowChangeQuantity){
			etNumberSaleCompet.setText("");
			parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_CUSTOMER));
			return;
		}
		if(type == 1 && !StringUtil.isNullOrEmpty(etPriceCompet.getText().toString()) && !allowChangeQuantity){
			etPriceCompet.setText("");
			parent.showDialog(StringUtil.getString(R.string.TEXT_CHOOSE_CUSTOMER));
			return;
		}
		
		if (!StringUtil.isNullOrEmpty(etNumberSaleCompet.getText().toString())) {
			long quantity = Long.parseLong(etNumberSaleCompet.getText().toString());
			if (type == 0) { // Kiem ton hoac kiem ban
				opProductDTO.setNewQuantity(quantity);
			} else {
				if (quantity <= 0) {
					//parent.showDialog(StringUtil.getString(R.string.TEXT_BIGGER_THAN_ZERO));
					etNumberSaleCompet.setText("");
				} else {
					opProductDTO.setNewQuantity(quantity);
				}
			}
		}else if(StringUtil.isNullOrEmpty(etNumberSaleCompet.getText().toString()) && opProductDTO.getNewQuantity()>0){
			opProductDTO.setNewQuantity(0);
		}

		if(type == 1) {
			if (!StringUtil.isNullOrEmpty(etPriceCompet.getText().toString())) {
				long price = Long.parseLong(etPriceCompet.getText().toString().replace(",", ""));
				if (type == 0) { // Kiem ton hoac kiem ban
					opProductDTO.setNewPrice(price);
				} else {
					if (price <= 0) {
						//parent.showDialog(StringUtil.getString(R.string.PRICE_BIGGER_THAN_ZERO));
						etPriceCompet.setText("");
					} else {
						opProductDTO.setNewPrice(price);
					}
				}
			} else if (StringUtil.isNullOrEmpty(etPriceCompet.getText().toString()) && opProductDTO.getNewPrice() > 0) {
				opProductDTO.setNewPrice(0);
			}
			opProductDTO.setPrice(opProductDTO.getNewPrice());
		}

		opProductDTO.setQuantity(opProductDTO.getNewQuantity());
	}

	@Override
	public void onClick(View v) {

		if (v == this && con != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) con);
		}
	}
	
	/**
	 * Lay header cho table
	 * @author: dungdq3
	 * @since: 4:08:07 PM Feb 7, 2014
	 * @return: void
	 * @throws:  
	 */
	public View getHeaderForTable(){
		LinearLayout header = new LinearLayout(con);
		LinearLayout.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		header.setLayoutParams(lp);
		
		for (View v : arrView) {
			HeaderTableInfo h = new HeaderTableInfo(v.getTag().toString(), v.getLayoutParams().width);
			header.addView(h.render(con,  ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG)));
		}
		return header;
	}
}
