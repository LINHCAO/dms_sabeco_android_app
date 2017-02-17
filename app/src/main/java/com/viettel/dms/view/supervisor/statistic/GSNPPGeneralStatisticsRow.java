/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor.statistic;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.dto.view.GeneralStatisticsViewDTO.GeneralStatisticsInfo;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.sabeco.R;

/**
 * Row for GSBH report
 * GSBHGeneralStatisticsRow.java
 * @author: duongdt3
 * @version: 1.0 
 * @since:  14:51:32 12 Dec 2013
 */
public class GSNPPGeneralStatisticsRow extends DMSTableRow  implements OnClickListener {
	private int ACTION_STAFF_CODE = 1;
	private int ACTION_SHOW_DETAIL_INFO = 2;
	
	private boolean isTotalRow = false;
	private TextView tvStaffCode;
	private TextView tvStaffName;
	private TextView tvKeHoach;
	private TextView tvThucHien;
	private TextView tvKeHoachThang;
	private TextView tvThucHienThang;
	private TextView tvTienDoThang;
	private TextView tvConLaiThang;
	private ImageView imgInfo;

	private GeneralStatisticsInfo info;
		
	public GSNPPGeneralStatisticsRow(Context context, GSNPPGeneralStatisticsView lis, 
			boolean isTotalRow, int actionStaffCode, int actionDetailStaffInfo) {
		super(context, R.layout.layout_gsbh_general_report_row);
		this.isTotalRow = isTotalRow;
		this.ACTION_STAFF_CODE = actionStaffCode;
		this.ACTION_SHOW_DETAIL_INFO = actionDetailStaffInfo;
		setListener(lis);
		tvStaffCode = (TextView) this.findViewById(R.id.tvStaffCode);
		tvStaffName = (TextView) this.findViewById(R.id.tvStaffName);
		tvKeHoach = (TextView) this.findViewById(R.id.tvKeHoach);
		tvThucHien = (TextView) this.findViewById(R.id.tvThucHien);
		tvKeHoachThang = (TextView) this.findViewById(R.id.tvKeHoachThang);
		tvThucHienThang = (TextView) this.findViewById(R.id.tvThucHienThang);
		tvConLaiThang = (TextView) this.findViewById(R.id.tvConLaiThang);
		tvTienDoThang = (TextView) this.findViewById(R.id.tvTienDoThang);
		imgInfo = (ImageView) this.findViewById(R.id.imgInfo);

		//set onclick event
		this.setOnClickListener(this);
		//click in staff code
		tvStaffCode.setOnClickListener(this);
		//click in image search
		imgInfo.setOnClickListener(this);
	}
	
	public void render(GeneralStatisticsInfo info){
		this.info = info;
		if (isTotalRow) {
			showRowSum("Tổng", tvStaffCode, tvStaffName);
			imgInfo.setImageDrawable(null);
			imgInfo.setOnClickListener(null);
		} else {
			// case normal row
			tvStaffCode.setText(info.tvObjectCode);
			tvStaffName.setText(info.tvObjectName);
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
		// case other views
		if (v == tvStaffCode) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ACTION_STAFF_CODE, v, info);
			}
		} else if (v == imgInfo) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ACTION_SHOW_DETAIL_INFO, v, info);
			}
		}
	}
}
