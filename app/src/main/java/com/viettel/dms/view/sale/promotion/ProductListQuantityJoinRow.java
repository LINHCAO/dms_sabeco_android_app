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
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ListProductQuantityJoin.ProductQuantityJoin;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * ProductListQuantityJoinRow.java
 * @author: quangvt1
 * @version: 1.0 
 * @since:  17:44:16 20-05-2014
 */
public class ProductListQuantityJoinRow extends TableRow implements TextWatcher {

	public static final int ACTION_CLICK_CODE_PROGRAM = 1;
	GlobalBaseActivity _context;
	View view;
	//so thu tu
	TextView tvSTT;
	//ma san pham
	TextView tvProductCode;
	//ten san pham
	TextView tvProductName;
	// So luong dang ki tham gia
	EditText edQuantity; 
	// Cho phep thay doi san luong hay khong
	boolean isAllowEdit;
	// san pham
	ProductQuantityJoin item;
	
	/**
	 * khoi tao row
	 * @param context
	 * @param aRow
	 */
	public ProductListQuantityJoinRow(GlobalBaseActivity context, boolean isAllowEdit) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.isAllowEdit = isAllowEdit;
		view = vi.inflate(R.layout.layout_product_quantity_join_row, this); 
		tvSTT = (TextView) view.findViewById(R.id.tvSTT); 
		tvProductCode = (TextView) view.findViewById(R.id.tvProductCode); 
		tvProductName = (TextView) view.findViewById(R.id.tvProductName); 
		edQuantity = (EditText) view.findViewById(R.id.etQuantity); 
		edQuantity.setEnabled(isAllowEdit);
		edQuantity.addTextChangedListener(this);
	} 
	 
	public void renderLayout(int position, ProductQuantityJoin item) { 
		this.item = item;
		
		tvSTT.setText("" + position); 
		tvProductCode.setText(item.product_code);
		tvProductName.setText(item.product_name);
		
		if(item.oldQuantity > 0){
			edQuantity.setText(item.oldQuantity + "");
			//edQuantity.setSelection(edQuantity.getText().length());
		}
	}  
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { 
		
		if (!StringUtil.isNullOrEmpty(edQuantity.getText().toString())) {
			long quantity = Long.parseLong(edQuantity.getText().toString()); 
				if (quantity <= 0) {
					_context.showDialog(StringUtil.getString(R.string.TEXT_THAN_ZERO));
					edQuantity.setText("");
					item.newQuantity = 0;
				} else {
					item.newQuantity = quantity;
				} 
		}else if(StringUtil.isNullOrEmpty(edQuantity.getText().toString())){
			item.newQuantity = 0;
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
