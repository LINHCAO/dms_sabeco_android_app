/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.promotion;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.db.CustomerInputQuantityDetailDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * ProductListQuantityDoneRow.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  17:44:16 20-05-2014
 */
public class ProductListQuantityDoneRow extends TableRow implements TextWatcher { 
	GlobalBaseActivity _context;
	View view;
	//so thu tu
	TextView tvSTT;
	//ma san pham
	TextView tvProductCode;
	//ten san pham
	TextView tvProductName;
	// So lieu mua tu NPP
	TextView tvOrderFromNPP;
	// So luong thuc hien nhap
	EditText edQuantity; 
	// So luong thuc hien
	TextView tvQuantity;
	// Cho phep thay doi san luong hay khong
	boolean isAllowEdit;
	// llEditQuantity
	LinearLayout llEditQuantity;
	// san pham
	CustomerInputQuantityDetailDTO detail;
	// type : 1-NPP, 2-PG
	int type; 
	
	/**
	 * khoi tao row
	 * @param context
	 * @param aRow
	 */
	public ProductListQuantityDoneRow(GlobalBaseActivity context, boolean isAllowEdit, int type) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isAllowEdit = isAllowEdit;
		this.type = type;
		view = vi.inflate(R.layout.layout_product_quantity_done_row, this); 
		tvSTT = (TextView) view.findViewById(R.id.tvSTT); 
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode); 
		tvProductName = (TextView) view.findViewById(R.id.tvProductName); 
		tvOrderFromNPP = (TextView) view.findViewById(R.id.tvOrderFromNPP);
		edQuantity = (EditText) view.findViewById(R.id.etQuantity);  
		edQuantity.addTextChangedListener(this);
		tvQuantity = (TextView) view.findViewById(R.id.tvQuantity);
		llEditQuantity = (LinearLayout) view.findViewById(R.id.llEditQuantity);
		
		if(isAllowEdit){
			llEditQuantity.setVisibility(View.VISIBLE);
			tvQuantity.setVisibility(View.GONE);
		}else{
			llEditQuantity.setVisibility(View.GONE);
			tvQuantity.setVisibility(View.VISIBLE);
		}
	} 
	 
	public void renderLayout(int position, CustomerInputQuantityDetailDTO detail) { 
		this.detail = detail;
		
		tvSTT.setText("" + position); 
		tvProductCode.setText(detail.PRODUCT_CODE);
		tvProductName.setText(detail.PRODUCT_NAME);
		tvOrderFromNPP.setText(detail.QUANTITY_SHOP + "");
		
		if(isAllowEdit){
			if(!StringUtil.isNullOrEmpty(detail.UPDATE_USER) || detail.OLDQUANTITY > 0){
				edQuantity.setText(detail.OLDQUANTITY + ""); 
				this.detail.isInputNewQuantity = true;
			}else{
				this.detail.isInputNewQuantity = false;
			}
		}else{
			if(type == 1){
				tvQuantity.setText(detail.OLDQUANTITY + "");
			}else{
				tvQuantity.setText(detail.QUANTITY_PG + "");
			}
		}  
	}  
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {  
		if (!StringUtil.isNullOrEmpty(edQuantity.getText().toString())) {
			long quantity = Long.parseLong(edQuantity.getText().toString());  
			detail.NEWQUANTITY = quantity;
			this.detail.isInputNewQuantity = true;
		}else{
			detail.NEWQUANTITY = 0;
			this.detail.isInputNewQuantity = false;
		}
	}

	@Override
	public void afterTextChanged(Editable s) { 
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) { 
	}
}
