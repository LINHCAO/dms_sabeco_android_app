/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.dto.view.NewCustomerItem;
import com.viettel.dms.sqllite.db.CUSTOMER_TABLE;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.sabeco.R;

/**
 * Row for New Customer List
 * NewCustomerRow
 * @author: duongdt3
 * @since:  10:50:03 03/01/2014
 * @update: 10:50:03 03/01/2014
 */
public class NewCustomerRow extends DMSTableRow  implements OnClickListener {

	private int ACTION_CUSTOMER_NAME = 0;

	private int ACTION_DELETE = 1;
	
	private NewCustomerItem info;
	// so thu tu
	private TextView tvSTT;
	// khach hang
	private TextView tvTKH;
	// dia chi
	private TextView tvDiaChi;
	// so dien thoai
	private TextView tvDienThoai;
	// trang thai
	private TextView tvTrangThai;
	//icon xoa
	private ImageView ivXoa;


	public NewCustomerRow(Context context, NewCustomerListView listener, int actionCustomerName, int actionDelete) {
		super(context, R.layout.list_new_customer_row);
		setListener(listener);
		this.listener = listener;
		this.ACTION_CUSTOMER_NAME = actionCustomerName;
		this.ACTION_DELETE = actionDelete;
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvTKH = (TextView) this.findViewById(R.id.tvTKH);
		tvDiaChi = (TextView) this.findViewById(R.id.tvDiaChi);
		tvDienThoai = (TextView) this.findViewById(R.id.tvDienThoai);
		tvTrangThai = (TextView) this.findViewById(R.id.tvTrangThai);
		ivXoa = (ImageView) this.findViewById(R.id.ivXoa);
		//set onclick event
		tvTKH.setOnClickListener(this);


	}

	/**
	 * render row view from data
	 * @author: duongdt3
	 * @since:  10:50:03 03/01/2014
	 * @update: 10:50:03 03/01/2014
	 * @return: void
	 */	
	public void render(NewCustomerItem info){
		this.info = info;
		//render
		
		tvSTT.setText(String.valueOf(this.info.tvSTT));
		tvTKH.setText(this.info.tvTKH);
		tvDiaChi.setText(this.info.tvDiaChi);
		tvDienThoai.setText(this.info.tvDienThoai);
		tvTrangThai.setText(this.info.tvTrangThai);
		
		//check action customer name
		if (this.info.isEdit) {
			ivXoa.setOnClickListener(this);
		}else{
			//neu ko được sửa thì ẩn nút xóa
			ivXoa.setImageDrawable(null);
		}
		
		//set màu theo trạng thái
		if (this.info.trangThai == CUSTOMER_TABLE.STATE_CUSTOMER_ERROR) {
			int colorError = ImageUtil.getColor(R.color.RED);
			this.setBackgroundRowByColor(colorError);
		}else if (this.info.trangThai == CUSTOMER_TABLE.STATE_CUSTOMER_NOT_APPROVED) {
			int colorNotApproved = ImageUtil.getColor(R.color.OGRANGE);
			this.setBackgroundRowByColor(colorNotApproved);
		}
		
	}

	@Override
	public void onClick(View v) {
		//click on ten Khach Hang
		if (listener != null) {
    		if (v == tvTKH) {
    			listener.handleVinamilkTableRowEvent(ACTION_CUSTOMER_NAME, v, info);
    		}else if (v == ivXoa) {
    			listener.handleVinamilkTableRowEvent(ACTION_DELETE, v, info);
    		}
		}
		super.onClick(v);
	}
}
