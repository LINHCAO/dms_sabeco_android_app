/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.order;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.TableRow;

import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.main.SalesPersonActivity;
import com.viettel.dms.view.sale.customer.CustomerFeedBackDto;
import com.viettel.sabeco.R;

/**
 * Update Remind From Call Sale View
 * UpdateRemindFromCallSaleView.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:17:34 14 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class UpdateRemindFromCallSaleView extends ScrollView implements OnClickListener {
	public SalesPersonActivity parent;
	public OrderView fragParent;
	VinamilkTableView tbCusFeedBack;// tbCusFeedBack
	Button btClose;
	public View viewLayout;
	CustomerFeedBackDto dto;
	List<TableRow> listRows = new ArrayList<TableRow>();

	public UpdateRemindFromCallSaleView(Context context, CustomerFeedBackDto dto) {
		super(context);
		parent = (SalesPersonActivity) context;
		fragParent = (OrderView) parent.getFragmentManager().findFragmentByTag(OrderView.TAG);
		LayoutInflater inflater = this.parent.getLayoutInflater();
		viewLayout = inflater.inflate(
				R.layout.layout_update_remind_callsale_view, null);

		tbCusFeedBack = (VinamilkTableView) viewLayout
				.findViewById(R.id.tbCusFeedBack);
		btClose = (Button) viewLayout.findViewById(R.id.btClose);
		btClose.setOnClickListener(this);
		this.dto = dto;
		
		renderLayout(dto);
		
	}

	public void renderLayout(CustomerFeedBackDto dto) {
		if (dto.arrItem.size() > 0) {
			int pos = 1;
			for (int i = 0, s = dto.arrItem.size(); i < s; i++) {
				UpdateRemindFromCallSaleRow row = new UpdateRemindFromCallSaleRow(parent);
				row.cbDone.setOnClickListener(this);
				row.cbDone.setTag(i);
				row.renderLayout(pos, dto.arrItem.get(i));
				pos++;
				listRows.add(row);
			}
			// customer list table
			int[] CUSTOMER_LIST_TABLE_WIDTHS = { 40, 300, 110, 110, 110, 50};
			String[] CUSTOMER_LIST_TABLE_TITLES = { "STT", "Nội dung", "Loại",
					"Ngày nhắc nhở", "Ngày TH", " "};

			tbCusFeedBack.getHeaderView().addColumns(
					CUSTOMER_LIST_TABLE_WIDTHS, CUSTOMER_LIST_TABLE_TITLES,
					ImageUtil.getColor(R.color.BLACK),
					ImageUtil.getColor(R.color.TABLE_HEADER_BG));
			tbCusFeedBack.addContent(listRows);
		}
	}

	@Override
	public void onClick(View v) {
		if(v == btClose){
			fragParent.onClick(v);
		}else if(v.getId() == R.id.cbNote){
			CheckBox cb = (CheckBox) v;
			if (cb.isChecked()) {
				Calendar currentDate = Calendar.getInstance(TimeZone
						.getTimeZone("GMT+7"));
				SimpleDateFormat formatter2 = new SimpleDateFormat(
				"dd/MM/yyyy");

				int index = (Integer) cb.getTag();
				FeedBackDTO item = dto.arrItem.get(index);
				item.doneDate = DateUtils.now();
				item.status = FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE;
				
				UpdateRemindFromCallSaleRow row = (UpdateRemindFromCallSaleRow) listRows.get(index);
				row.tvDoneDate.setText(formatter2.format(currentDate.getTime()));
				fragParent.updateFeedbackRow(item);
			}
		}
	}

}
