/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tnpg.statistic;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.sabeco.R;


/**
 * Row for PG report
 * PGGeneralStatisticsRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  19:29:37 5 Dec 2013
 */
public class TNPGGeneralStatisticsRow extends DMSTableRow  implements OnClickListener {
	private TextView tvProductCode;
	private TextView tvProductName;
	private TextView tvDVT;
	private TextView tvKeHoach;
	private TextView tvThucHien;
	private TextView tvKeHoachThang;
	private TextView tvThucHienThang;
	private TextView tvConLaiThang;
	private TextView tvTienDoThang;

	
	public TNPGGeneralStatisticsRow(Context context, TNPGGeneralStatisticView lis) {
		super(context, R.layout.layout_tttt_general_report_row);
		setListener(lis);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvDVT = (TextView) this.findViewById(R.id.tvDVT);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvKeHoachThang = (TextView) this.findViewById(R.id.tvKeHoachThang);
		tvThucHienThang = (TextView) this.findViewById(R.id.tvThucHienThang);
		tvConLaiThang = (TextView) this.findViewById(R.id.tvConLaiThang);
		tvTienDoThang = (TextView) this.findViewById(R.id.tvTienDoThang);

		int color = ImageUtil.getColor(R.color.COLOR_REMAIN);
		tvConLaiThang.setBackgroundColor(color);
		
		//set onclick event
		this.setOnClickListener(this);
	}

	public void render(GeneralStatisticsInfo info){
		//info of 2 case
		tvProductCode.setText(info.tvObjectCode);
		tvProductName.setText(info.tvObjectName);
		tvDVT.setText(info.tvDVT);
		
		// đối với PG là tính số lẻ
		//kế hoạch là thùng/két
		String strKeHoach =  StringUtil.getQuantityAndConvfactConvert(info.tvKeHoach, info.tvConvfact);
		String strThucHien = StringUtil.getQuantityAndConvfactConvert(info.tvThucHien, info.tvConvfact);
		//kế hoạch là thùng/két
		String strKeHoachThang = StringUtil.getQuantityAndConvfactConvert(info.tvKeHoachThang, info.tvConvfact);
		String strThucHienThang = StringUtil.getQuantityAndConvfactConvert(info.tvThucHienThang, info.tvConvfact);
		String strConLaiThang = StringUtil.getQuantityAndConvfactConvert(info.tvConLaiThang, info.tvConvfact);
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
