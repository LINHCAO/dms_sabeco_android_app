/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.VisitDTO.VisitItem;
import com.viettel.dms.view.control.HeaderTableInfo;
import com.viettel.sabeco.R;

/**
 * Row cho table danh sach ghe tham
 * Man hinh Khong PDSD 
 */
public class VisitRow extends TableRow implements OnClickListener {
 
	private static HeaderTableInfo[] TABLE_HEADER = null;

	/**
	 * Lay header cua table
	 * 
	 * @param con
	 * @return HeaderTableInfo[]
	 */
	public static HeaderTableInfo[] getTableHeader(Context con) {
		if (TABLE_HEADER == null) {
			VisitRow row = new VisitRow(con);
			TABLE_HEADER = new HeaderTableInfo[] {
					new HeaderTableInfo(row.tvStt.getText().toString(), row.tvStt.getLayoutParams().width),
					new HeaderTableInfo(row.tvThoiGian.getText().toString(), row.tvThoiGian.getLayoutParams().width),
					new HeaderTableInfo(row.tvTrangThai.getText().toString(), row.tvTrangThai.getLayoutParams().width) };
		}
		return TABLE_HEADER;
	}

	public TextView tvStt;		// column STT
	public TextView tvThoiGian;	// column Thoi gian
	public TextView tvTrangThai;// column Trang thai

	/**
	 * Khoi tao doi tuong
	 * 
	 * @param context
	 */
	public VisitRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = vi.inflate(R.layout.layout_visit_row, this, false);
		TableRow row = (TableRow) view.findViewById(R.id.row);
		tvStt = (TextView) view.findViewById(R.id.tvStt);
		tvThoiGian = (TextView) view.findViewById(R.id.tvThoiGian);
		tvTrangThai = (TextView) view.findViewById(R.id.tvTrangThai);

		// remove in row
		row.removeAllViews();

		// add into this
		this.addView(tvStt);
		this.addView(tvThoiGian);
		this.addView(tvTrangThai);
		this.setOnClickListener(this);
	}
	
	/**
	 * Render view
	 * 
	 * @param visitItem
	 */
	public void render(VisitItem visitItem) {
		tvStt.setText("" + visitItem.stt);
		tvThoiGian.setText(visitItem.thoiGian);
		tvTrangThai.setText(visitItem.trangThai);
	}

	@Override
	public void onClick(View v) {

	}
}
