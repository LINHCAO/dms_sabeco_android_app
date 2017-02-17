/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVProgressDateReportDTO.TBHVProgressDateReportItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;
/**
 * 
 *  row tat ca, dung trong man hinh bao cao ngay cua TBHV
 *  @author: Nguyen Huu Hieu
 *  @version: 1.1
 *  @since: 1.0
 */
public class TBHVProgressDateReportAllRow extends TableRow {
	private View view;
		
	public TextView tvTongCong;	
	public TextView tvKeHoach;
	public TextView tvThucHien;
	public TextView tvConLai;
	public TextView tvSKUKeHoach;
	public TextView tvSKUThucHien;
	TBHVProgressDateReportItem item;

	public TBHVProgressDateReportAllRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_progress_date_report_all_row, this);
		tvTongCong = (TextView) view.findViewById(R.id.tvTongCong);
		tvSKUKeHoach = (TextView) view.findViewById(R.id.tvSKUKeHoach);
		tvSKUThucHien = (TextView) view.findViewById(R.id.tvSKUThucHien);
		tvKeHoach = (TextView) view.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) view.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) view.findViewById(R.id.tvConLai);		
	}
	
	public void render(String nvbh, Double keHoach, Double thucHien, Double conLai) {		
		tvTongCong.setText(nvbh);		
		tvKeHoach.setText(StringUtil.parseAmountMoney(keHoach));
		tvThucHien.setText(StringUtil.parseAmountMoney(thucHien));
		tvConLai.setText(StringUtil.parseAmountMoney(conLai));		
		tvTongCong.setTypeface(null, Typeface.BOLD);
		tvSKUKeHoach.setTypeface(null, Typeface.BOLD);
		tvSKUThucHien.setTypeface(null, Typeface.BOLD);
		tvKeHoach.setTypeface(null, Typeface.BOLD);
		tvThucHien.setTypeface(null, Typeface.BOLD);
		tvConLai.setTypeface(null, Typeface.BOLD);		
	}	
}
