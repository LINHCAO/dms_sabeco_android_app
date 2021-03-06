/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.tnpg.statistic.TNPGGeneralStatisticView;
import com.viettel.sabeco.R;

/**
 * General Statistics Month Detail Row for NVBH
 * NVBHGeneralStatisticsMonthDetailRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  11:02:47 16 Dec 2013
 */
public class NVBHGeneralStatisticsMonthDetailRow extends DMSTableRow  implements OnClickListener {

	private TextView tvProductCode;
	private TextView tvProductName;
	private TextView tvDVT;
	private TextView tvKeHoach;
	private TextView tvThucHien;
	private TextView tvTienDo;
	private TextView tvConLai;

	public NVBHGeneralStatisticsMonthDetailRow(Context context, NVBHGeneralStatisticsView lis) {
		super(context, R.layout.layout_nvbh_general_month_report_row);
		setListener(lis);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvDVT = (TextView) this.findViewById(R.id.tvDVT);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) this.findViewById(R.id.tvConLai);
		tvTienDo = (TextView) this.findViewById(R.id.tvTienDo);
		int color = ImageUtil.getColor(R.color.COLOR_REMAIN);
		tvConLai.setBackgroundColor(color);
		
		//set onclick event
		this.setOnClickListener(this);
	}
	
	public NVBHGeneralStatisticsMonthDetailRow(Context context, TNPGGeneralStatisticView lis) {
		super(context, R.layout.layout_nvbh_general_month_report_row);
		setListener(lis);
		tvProductCode = (TextView) this.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) this.findViewById(R.id.tvProductName);
		tvDVT = (TextView) this.findViewById(R.id.tvDVT);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvConLai = (TextView) this.findViewById(R.id.tvConLai);
		tvTienDo = (TextView) this.findViewById(R.id.tvTienDo);
		int color = ImageUtil.getColor(R.color.COLOR_REMAIN);
		tvConLai.setBackgroundColor(color);
		
		//set onclick event
		this.setOnClickListener(this);
	}
	
	/**
	 * Chung cho 2 truong hop
	 * @author: duongdt3
	 * @since: 10:18:03 3 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	private void renderData(GeneralStatisticsInfo info){
		tvProductCode.setText(info.tvObjectCode);
		tvProductName.setText(info.tvObjectName);
		tvDVT.setText(info.tvDVT);
		
		// check hightlight
		if (info.isHightLight) {
			tvTienDo.setTextColor(Color.RED);
		}
	}
	
	/**
	 * Render rieng cho NVBH
	 * @author: duongdt3
	 * @since: 10:18:12 3 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	public void render(GeneralStatisticsInfo info){
		renderData(info);
		
		// info of 2 case
		String strKeHoach = StringUtil.parseAmountMoney(info.tvKeHoachThang);
		String strThucHien = StringUtil.parseAmountMoney(info.tvThucHienThang);
		String strConLai = StringUtil.parseAmountMoney(info.tvConLaiThang);
		String strTienDo = StringUtil.parsePercent(info.tvTienDoThang);
		tvKeHoach.setText(strKeHoach);
		tvThucHien.setText(strThucHien);
		tvConLai.setText(strConLai);
		tvTienDo.setText(strTienDo);
	}
	
	/**
	 * Render rieng cho  PG
	 * @author: duongdt3
	 * @since: 10:18:29 3 Dec 2013
	 * @return: void
	 * @throws:  
	 * @param info
	 */
	public void renderForPG(GeneralStatisticsInfo info){
		renderData(info);
		
		String strKeHoach =  StringUtil.getQuantityAndConvfactConvert(info.tvKeHoachThang, info.tvConvfact);
		String strThucHien =StringUtil.getQuantityAndConvfactConvert(info.tvThucHienThang, info.tvConvfact);
		String strConLai = StringUtil.getQuantityAndConvfactConvert(info.tvConLaiThang, info.tvConvfact);
		String strTienDo = StringUtil.parsePercent(info.tvTienDoThang);
		tvKeHoach.setText(strKeHoach);
		tvThucHien.setText(strThucHien);
		tvConLai.setText(strConLai);
		tvTienDo.setText(strTienDo);
	}

	@Override
	public void onClick(View v) {

	}
}
