/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Dòng hiển thị báo cáo doanh số ngày, tháng của NVBH
 * GeneralStatisticsSaleRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  10:35:28 22 Nov 2013
 */
@SuppressLint("ViewConstructor")
public class NVBHGeneralStatisticsRow extends DMSTableRow implements OnClickListener {

	private TextView tvSTT;
	private TextView tvProductCode;
	private TextView tvProductName;
	private TextView tvDVT;	
	private TextView tvKeHoach;
	private TextView tvThucHien;
	//private TextView tvConLai;
	private TextView tvKeHoachThang;
	private TextView tvThucHienThang;
	private TextView tvConLaiThang;
	private TextView tvTienDoThang;

	boolean isTotalRow = false;
	
	public static final int TYPE_ROW_SALEMAN = 1;
	public static final int TYPE_ROW_PG = 2;
	public static final int TYPE_ROW_SUPERVISOR = 3;

	public NVBHGeneralStatisticsRow(Context context, VinamilkTableListener lis,
			boolean isColorRemain, boolean isTotalRow) {
		super(context, R.layout.layout_nvbh_general_report_row);
		this.isTotalRow = isTotalRow;
		setListener(lis);
		tvSTT = (TextView) this.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvDVT = (TextView) this.findViewById(R.id.tvDVT);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvKeHoachThang = (TextView) this.findViewById(R.id.tvKeHoachThang);
		tvThucHienThang = (TextView) this.findViewById(R.id.tvThucHienThang);
		tvConLaiThang = (TextView) this.findViewById(R.id.tvConLaiThang);
		tvTienDoThang = (TextView) this.findViewById(R.id.tvTienDoThang);
	}

	public void render(GeneralStatisticsInfo info){
		if (isTotalRow) {
			showRowSum("Tổng", tvSTT, tvProductCode, tvProductName, tvDVT);
			//bold row
//			GlobalUtil.setBoldArrayViewInRow(arrView);
			
		} else {
			// case normal row
			String stt = String.valueOf(info.tvSTT);
			tvSTT.setText(stt);
			tvProductCode.setText(info.tvObjectCode);
			tvProductName.setText(info.tvObjectName);
			tvDVT.setText(info.tvDVT);
		}
		// info of 2 case
		String strKeHoach = StringUtil.parseAmountMoney(info.tvKeHoach);
		String strThucHien = StringUtil.parseAmountMoney(info.tvThucHien);
		String strKeHoachThang = StringUtil.parseAmountMoney(info.tvKeHoachThang);
		String strThucHienThang = StringUtil.parseAmountMoney(info.tvThucHienThang);
		String strConLaiThang = StringUtil.parseAmountMoney(info.tvConLaiThang);
		String strTienDoThang = StringUtil.parsePercent(info.tvTienDoThang);

		tvKeHoach.setText(strKeHoach);
		tvThucHien.setText(strThucHien);
		tvKeHoachThang.setText(strKeHoachThang);
		tvThucHienThang.setText(strThucHienThang);
		tvConLaiThang.setText(strConLaiThang);
		tvTienDoThang.setText(strTienDoThang);
		
		// check hightlight
		if (info.isHightLight) {
			tvTienDoThang.setTextColor(Color.RED);
		}
	}
	
	@Override
	public void onClick(View v) {

	}

}
