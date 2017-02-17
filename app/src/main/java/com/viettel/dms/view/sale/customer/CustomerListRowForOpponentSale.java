/**
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.viettel.dms.view.sale.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.CustomerListItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.HeaderTableInfo;
import com.viettel.dms.view.control.VinamilkTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * CustomerListRowForOpponentSale.java
 * 
 * @author: dungdq3
 * @version: 1.0
 * @since: 10:38:48 AM Mar 7, 2014
 */
public class CustomerListRowForOpponentSale extends VinamilkTableRow {

	// Views
	private final Context con;
	// so thu tu
	private TextView tvSTT;
	// thong tin khach hang
	private TextView tvCusInfo;
	// dia chi
	private TextView tvAddress;
	private VinamilkTableListener listener;
	private TableRow row;
	// parameters
	private CustomerListItem customerListItem;
	private View[] arrView;

	private CustomerListRowForOpponentSale(Context context) {
		super(context);
		con = context;
	}

	/**
	 * @param context
	 */
	public CustomerListRowForOpponentSale(Context context,
			VinamilkTableListener listen) {
		this(context);
		// TODO Auto-generated constructor stub
		listener = listen;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = vi.inflate(R.layout.layout_customer_list_row_for_opponent, this);
		// khoi tao View
		initView(v);
		setOnClickListener(this);
	}

	/**
	 * Khoi tao cac Views
	 * 
	 * @author: dungdq3
	 * @since: 10:48:48 AM Mar 7, 2014
	 * @return: void
	 * @throws:
	 * @param v
	 */
	private void initView(View v) {
		// TODO Auto-generated method stub
		row = (TableRow) v.findViewById(R.id.row);
		tvSTT = (TextView) v.findViewById(R.id.tvPosition);
		tvCusInfo = (TextView) v.findViewById(R.id.tvCusNameCusCode);
		tvAddress = (TextView) v.findViewById(R.id.tvAddressCus);
		row.removeAllViews();
		arrView = new View[] { tvSTT, tvCusInfo, tvAddress };

		for (View vi : arrView) {
			addView(vi);
		}
	}

	/**
	 * render layout
	 * 
	 * @author: dungdq3
	 * @param item
	 * @param pos
	 * @since: 10:52:40 AM Mar 7, 2014
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(int pos, CustomerListItem item) {
		// TODO Auto-generated method stub
		customerListItem = item;
		tvSTT.setText(String.valueOf(pos));
		StringBuilder sb = new StringBuilder(item.aCustomer.customerCode);
		sb.append(" - ");
		sb.append(item.aCustomer.getCustomerName());
		tvCusInfo.setText(sb.toString());
		tvAddress.setText(item.aCustomer.getStreet());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);
		if (listener != null) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.UPDATE_CUSTOMER, this, customerListItem);
		}
	}

	/**
	 * tao header cho table
	 * 
	 * @author: dungdq3
	 * @since: 11:31:18 AM Mar 7, 2014
	 * @return: View
	 * @throws:
	 * @return
	 */
	public View getHeaderForTable() {
		// TODO Auto-generated method stub
		LinearLayout header = new LinearLayout(con);
		LinearLayout.LayoutParams lp = new TableRow.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		header.setLayoutParams(lp);

		for (View v : arrView) {
			HeaderTableInfo h = new HeaderTableInfo(v.getTag().toString(), v.getLayoutParams().width);
			header.addView(h.render(con, ImageUtil.getColor(R.color.BLACK), ImageUtil.getColor(R.color.TABLE_HEADER_BG)));
		}
		return header;
	}

}
