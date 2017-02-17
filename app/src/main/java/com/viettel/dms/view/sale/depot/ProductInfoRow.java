/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.depot;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.db.ProductDTO;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.control.SpannableObject;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.dms.view.supervisor.SupervisorProductListView;
import com.viettel.sabeco.R;

/**
 * 
 * row danh sach san pham
 * role NVBH, GSBH
 * @author: YenNTH
 * @version: 1.0
 * @since: 1.0
 */
@SuppressLint("ViewConstructor")
public class ProductInfoRow extends DMSTableRow implements OnClickListener {
//	private final Context context;
//	private VinamilkTableListener listener;
	ProductDTO currentDTO; // dto san pham
//	private final View view;
	// so thu tu
	TextView tvSTT;
	// ma san pham
	public TextView tvProductCode;
	// ten san pham
	TextView tvProductName;
	// ton kho
	TextView tvQuantity;
	// don vi tinh
	private TextView tvUOM;
	// thong bao khong co du lieu
	TextView tvNoResultInfo;

	public ProductInfoRow(Context context, ProductListView listen) {
		super(context, R.layout.layout_product_info_row);
		setListener(listen);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvUOM = (TextView) this.findViewById(R.id.tvUOM);
		tvQuantity = (TextView) this.findViewById(R.id.tvQuantity);
//		tvNoResultInfo = (TextView) this.findViewById(R.id.tvNoResultInfo);
	}
	
	public ProductInfoRow(Context context, SupervisorProductListView listen) {
		super(context, R.layout.layout_product_info_row);
		setListener(listen);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvUOM = (TextView) this.findViewById(R.id.tvUOM);
		tvQuantity = (TextView) this.findViewById(R.id.tvQuantity);
//		tvNoResultInfo = (TextView) this.findViewById(R.id.tvNoResultInfo);
	}

	/**
	 * 
	 * render layout for product row
	 * 
	 * @author: YenNTH
	 * @param position
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int position, ProductDTO item) {
		this.currentDTO = item;
		tvSTT.setText(Constants.STR_BLANK + position);
		tvProductCode.setText(item.productCode);
		if (item.isHasFocus) {
			SpannableObject spanObj = new SpannableObject();
			spanObj.addSpan("*", ImageUtil.getColor(R.color.RED),android.graphics.Typeface.BOLD);
			tvProductCode.append(spanObj.getSpan());
		}
		tvProductName.setText(item.productName);
		tvUOM.setText(item.uom2);
		String text= StringUtil.parseAmountMoney(item.inventory);
		tvQuantity.setText(text);

		if (item.inventory<=0) {
			tvSTT.setTextColor(ImageUtil.getColor(R.color.RED));
			tvProductCode.setTextColor(ImageUtil.getColor(R.color.RED));
			tvProductName.setTextColor(ImageUtil.getColor(R.color.RED));
			tvUOM.setTextColor(ImageUtil.getColor(R.color.RED));
			tvQuantity.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}
}
