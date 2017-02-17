/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.customer;

/**
 * Copyright 2012 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerProgrameDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Row chuong trinh trung bay
 * 
 * @author: BangHN
 * @version: 1.0
 * @since: 1.0
 */
public class CustomerProgrameDisplayRow extends DMSTableRow {
	public static final int ACTION_VIEW_PRODUCT = 1;
	public static final int ACTION_VIEW_PROMOTION = 2;
	public static final int ACTION_DELETE = 3;

//	Context mContext;
//	View view;
	TextView tvCode;// ma ct
	TextView tvName;// ten ct
	TextView tvDepart;// nganh hang
	TextView tvLevel;// muc thamg gia
	TextView tvQuota;// chi tieu
	TextView tvRemain;// ds con lai

	protected OnEventControlListener listener;
	// linear ds san pham
	LinearLayout llProducts;

	public CustomerProgrameDisplayRow(Context context, CustomerInfoView lis) {
		super(context, R.layout.customer_programe_display_row);
		setListener(lis);
//		mContext = context;
//		LayoutInflater vi = (LayoutInflater) context
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		view = vi.inflate(R.layout.customer_programe_display_row, this);

		tvCode = (TextView) this.findViewById(R.id.tvCode);
		tvName = (TextView) this.findViewById(R.id.tvName);
		tvLevel = (TextView) this.findViewById(R.id.tvLevel);
		tvDepart = (TextView) this.findViewById(R.id.tvDepart);
		tvQuota = (TextView) this.findViewById(R.id.tvQuota);
		tvRemain = (TextView) this.findViewById(R.id.tvRemain);

	}

	/**
	 * Cap nhat data
	 * 
	 * @author: BangHN
	 * @param dto
	 * @return: void
	 * @throws:
	 */
	public void updateData(CustomerProgrameDTO dto) {
		tvCode.setText(dto.displayProgrameCode);
		tvName.setText(dto.displayProgrameName);
		tvLevel.setText(dto.levelCode);
		tvDepart.setText(dto.cat);
//		tvQuota.setText(StringUtil.parseAmountMoney(dto.amount));
		tvQuota.setText(StringUtil.parseAmountMoney(dto.amountPlan));
		tvRemain.setText(StringUtil.parseAmountMoney(dto.amountRemain));
		// if ((dto.amount - dto.amountPlan) > 0) {
		// tvRemain.setText(StringUtil.parseAmountMoney(dto.amount -
		// dto.amountPlan));
		// } else {
		// tvRemain.setText("0");
		// }
	}
}
