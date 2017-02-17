/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.VoteDisplayProductDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 *  display row vote display present product promotion
 *  @author: HaiTC3
 *  @version: 1.0
 *  @since: 1.0
 */
public class VoteDisplayPresentProductRow extends DMSTableRow implements OnClickListener, TextWatcher{
	GlobalBaseActivity parent;
	// STT
	TextView tvSTT;
	// code
	TextView tvProductCode;
	// Name
	TextView tvProductName;
	// price
	TextView tvNumberProduct;
	// number 
	EditText etVote;
	// Luu lai de khi thay nhap gia tri se set vao numvote
	VoteDisplayProductDTO item;
	
	LinearLayout llEditText;
	

	/**
	 * constructor for class
	 * @param context
	 * @param aRow
	 */
	public VoteDisplayPresentProductRow(Context context, VoteDisplayPresentProductView lis) {
		super(context, R.layout.layout_vote_display_present_product_row);
		setListener( lis);
		this.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvNumberProduct = (TextView) this.findViewById(R.id.tvNumberProduct);
		etVote = (EditText) this.findViewById(R.id.etVote);
		etVote.addTextChangedListener(this);
		llEditText = (LinearLayout) this.findViewById(R.id.llEditText);
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
	public void renderLayout(int position, VoteDisplayProductDTO item) { 
		this.item = item;
		if (item.productID != -10) {// nếu productId =-10 tuc là dong tong 
			tvSTT.setText(String.valueOf(position));
			tvProductCode.setText(item.productCode);
			tvProductName.setText(item.productName);
			tvNumberProduct.setText(item.numberProduct);
		}else {
			renderTotalRow(item.totalName, item.numberProduct);
		}
	}

	/**
	 * render dong tong mà khach hang phai thuc hien trung bay duoc 
	 * @author: hoanpd1
	 * @since: 15:53:58 22-08-2014
	 * @return: void
	 * @throws:  
	 * @param item
	 */
	public void renderTotalRow(String totalName, String numberProduct){
		showRowSum(item.totalName, tvSTT, tvProductCode, tvProductName);
		etVote.setVisibility(View.INVISIBLE);
		tvNumberProduct.setText(numberProduct);
		tvNumberProduct.setTypeface(tvNumberProduct.getTypeface(), Typeface.BOLD);
	}
	
	@Override
	public void onClick(View v) {
		if (v == this && parent != null){
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity)parent);
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) { 
		
	}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) { 
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) { 
		String strNum = etVote.getText().toString();
		if (!StringUtil.isNullOrEmpty(strNum)) {
			int numVote = Integer.parseInt(strNum);
			if (numVote <= 0) {
				parent.showDialog(StringUtil.getString(R.string.TEXT_VOTE_DISPLAY_VALUE_MUST_BIGGER_THAN_ZERO));
				etVote.setText("");
				item.voteNumber = 0;
			} else {
				item.voteNumber = numVote;
			}
		} else {
			item.voteNumber = 0;
		}
	}
}
