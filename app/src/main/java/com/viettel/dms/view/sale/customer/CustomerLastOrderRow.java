/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.SaleOrderCustomerDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.sabeco.R;

/**
 * Dong thong tin don hang gan nhat cua khach hang
 * @author : BangHN
 * since : 2:15:44 PM
 * version :
 */
public class CustomerLastOrderRow extends DMSTableRow implements OnClickListener{
//	private final Context context;
//	private final View view;
	private TextView tvNum;// so thu tu
	private TextView tvOrderCode;//so don hang
	private TextView tvDate;//ngay
	private TextView tvSKU;//sku
	private TextView tvMoney;//thanh tien
//	private VinamilkTableListener listener;
	private SaleOrderCustomerDTO saleOrderCustomerDTO;
	
//	public CustomerLastOrderRow(Context con){
//		super(con);
//		context = con;
//		LayoutInflater vi = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		view = vi.inflate(R.layout.layout_customer_last_order_row, this);
//	}
	
	public CustomerLastOrderRow(Context context, CustomerInfoView lis) {
		super(context, R.layout.layout_customer_last_order_row);
		setListener(lis);
//		listener=listen;
//		setOnClickListener(this);
		tvNum = (TextView) this.findViewById(R.id.tvNum);
		tvOrderCode = (TextView) this.findViewById(R.id.tvOrderCode);
		tvOrderCode.setOnClickListener(this);
		tvDate = (TextView) this.findViewById(R.id.tvDate);
		tvSKU = (TextView) this.findViewById(R.id.tvSKU);
		tvMoney = (TextView) this.findViewById(R.id.tvMoney);
		
		//Neu khong cho hien thi gia thi khong hien thi thanh tien
//		if(GlobalInfo.getInstance().getIsShowPrice() == 0) {
//			tvMoney.setVisibility(View.GONE);
//			TableRow.LayoutParams param = new TableRow.LayoutParams();
//			param.width = GlobalUtil.dip2Pixel(250 + 195);
//			param.height = GlobalUtil.dip2Pixel(42);
//			param.setMargins(GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1),
//					GlobalUtil.dip2Pixel(1), GlobalUtil.dip2Pixel(1));
//			tvOrderCode.setLayoutParams(param);
//		}
	}
	
	public void setDataRow(SaleOrderCustomerDTO dto, int index){
		saleOrderCustomerDTO=dto;
		tvNum.setText("" + index);
		tvOrderCode.setText(saleOrderCustomerDTO.getSaleOder().orderNumber);
		tvDate.setText(saleOrderCustomerDTO.getSaleOder().orderDate);
		tvMoney.setText(StringUtil.parseAmountMoney(saleOrderCustomerDTO.getSaleOder().total));
		tvSKU.setText("" + saleOrderCustomerDTO.getSKU());
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==tvOrderCode){
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_ORDER_VIEW, tvOrderCode, saleOrderCustomerDTO.getSaleOder());
		}
	}
}
