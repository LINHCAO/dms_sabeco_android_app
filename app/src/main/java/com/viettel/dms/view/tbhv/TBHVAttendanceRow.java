/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.TBHVAttendanceDTO.TVBHAttendanceItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * hien thi thong tin 1 dong bao cao cham cong ngay cua GSNPP
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TBHVAttendanceRow extends DMSTableRow implements OnClickListener {
	TextView tvNPP;
	TextView tvGSNPP;
	TextView tvTotalNV;
	TextView tvMiddleInfo;
	TextView tvAfterMiddleInfo;
	TVBHAttendanceItem item;

	public TBHVAttendanceRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_tbhv_report_take_attend_od_day_row);
		setListener(lis);
		setOnClickListener(this);
		tvNPP = (TextView) this.findViewById(R.id.tvNPP);
		tvNPP.setOnClickListener(this);
		tvGSNPP = (TextView) this.findViewById(R.id.tvGSNPP);
		tvGSNPP.setOnClickListener(this);
		tvTotalNV = (TextView) this.findViewById(R.id.tvTotalNV);
		tvMiddleInfo = (TextView) this.findViewById(R.id.tvMiddleInfo);
		tvAfterMiddleInfo = (TextView) this.findViewById(R.id.tvAfterMiddleInfo);
	}

	/**
	 * 
	 * render layout for row with a
	 * 
	 * @author: HaiTC3
	 * @since: 4:51:47 PM | Jun 11, 2012
	 * @param position
	 * @param GeneralStatisticsViewDTO
	 *            item
	 * @return: void
	 * @throws:
	 */
	public void render(TVBHAttendanceItem item) {
		this.item = item;
		tvNPP.setText(item.nvbhShopCode);
		tvGSNPP.setText(item.gsnppStaffName);
		tvTotalNV.setText("" + item.numCus);
		if(item.hasPosition){
			if(item.notArrivedNPP <0){
				tvMiddleInfo.setText(Constants.STR_BLANK);
			}else{
				tvMiddleInfo.setText("" + item.notArrivedNPP);
			}
			if(item.lateArrivedNPP <0){
				tvAfterMiddleInfo.setText(Constants.STR_BLANK);
			}else{
				tvAfterMiddleInfo.setText("" + item.lateArrivedNPP);
			}
			if(item.notArrivedNPP > 0){
				tvMiddleInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}
			if(item.lateArrivedNPP > 0){
				tvAfterMiddleInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}
		}else{
			tvMiddleInfo.setText(Constants.STR_BLANK);
			tvAfterMiddleInfo.setText(Constants.STR_BLANK);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == tvNPP || v == tvGSNPP) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.TBHV_ATTENDANCE_DETAIL, v, item);
		}
	}
}
