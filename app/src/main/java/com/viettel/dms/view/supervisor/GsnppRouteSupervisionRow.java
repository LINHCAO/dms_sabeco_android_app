/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.viettel.dms.dto.view.GsnppRouteSupervisionItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Gsnpp Route Supervision Row
 * GsnppRouteSupervisionRow.java
 * @version: 1.0 
 * @since:  08:30:16 20 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class GsnppRouteSupervisionRow extends DMSTableRow implements OnClickListener {

	public TextView tvMaNVBH;
	public TextView tvNVBH;
	public TextView tvSodiemban;
	public TextView tvDungtuyen;
	public TextView tvSaituyen;
	public TextView tv2phut;
	public TextView tv230phut;
	public TextView tv30phut;
	public ImageView visitCus;
	GsnppRouteSupervisionItem item;

	public GsnppRouteSupervisionRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_sale_road_map_monitor_row);
		setListener(lis);
		this.setOnClickListener(this);
		tvMaNVBH = (TextView) this.findViewById(R.id.tvMaNVBH);
		tvNVBH = (TextView) this.findViewById(R.id.tvNVBH);
		tvSodiemban = (TextView) this.findViewById(R.id.tvSodiemban);
		tvSaituyen = (TextView) this.findViewById(R.id.tvSaituyen);
		tvDungtuyen = (TextView) this.findViewById(R.id.tvDungtuyen);
		tv230phut = (TextView) this.findViewById(R.id.tv230phut);
		tv30phut = (TextView) this.findViewById(R.id.tv30phut);
		tv2phut = (TextView) this.findViewById(R.id.tv2phut);
		visitCus = (ImageView) this.findViewById(R.id.visitCus);
		visitCus.setOnClickListener(this);
		tvMaNVBH.setOnClickListener(this);
		tvSaituyen.setOnClickListener(this);
		tv2phut.setOnClickListener(this);
		tv30phut.setOnClickListener(this);
	}


//	//header table of this row
//	private static HeaderTableInfo[] TABLE_HEADER = null;
//
//	public static HeaderTableInfo[] getTableHeader(Context con) {
//		if (TABLE_HEADER == null) {
//			GsnppRouteSupervisionRow row = new GsnppRouteSupervisionRow(con);
//			TABLE_HEADER = new HeaderTableInfo[] {
//					new HeaderTableInfo(row.tvMaNVBH.getText().toString(), row.tvMaNVBH.getLayoutParams().width),
//					new HeaderTableInfo(row.tvNVBH.getText().toString(), row.tvNVBH.getLayoutParams().width),
//					new HeaderTableInfo(row.tvSodiemban.getText().toString(), row.tvSodiemban.getLayoutParams().width),
//					new HeaderTableInfo(StringUtil.getString(R.string.TEXT_STORE), 0, new HeaderTableInfo[] {
//                					new HeaderTableInfo(row.tvDungtuyen.getText().toString(), row.tvDungtuyen.getLayoutParams().width),
//                					new HeaderTableInfo(row.tvSaituyen.getText().toString(), row.tvSaituyen.getLayoutParams().width)}),
//                	new HeaderTableInfo(StringUtil.getString(R.string.TEXT_VISIT_TIME), 0, new HeaderTableInfo[] {
//                					new HeaderTableInfo(row.tv2phut.getText().toString(), row.tv2phut.getLayoutParams().width),
//                					new HeaderTableInfo(row.tv230phut.getText().toString(), row.tv230phut.getLayoutParams().width),
//                					new HeaderTableInfo(row.tv30phut.getText().toString(), row.tv30phut.getLayoutParams().width)}),
//					new HeaderTableInfo("", row.visitCus.getLayoutParams().width) };
//		}
//		return TABLE_HEADER;
//	}

	public void render(GsnppRouteSupervisionItem item) {
		this.item = item;
		tvMaNVBH.setText(item.aStaff.staffCode);
		tvNVBH.setText(item.aStaff.name);
		tvSodiemban.setText("" + item.numVisited + "/" + item.numTotalCus);
		tvDungtuyen.setText("" + item.numRightPlan);
		tv230phut.setText("" + item.numOnTime);
		tv30phut.setText("" + item.numMoreThan30Min);
		tv2phut.setText("" + item.numLessThan2Min);
		tvSaituyen.setText("" + item.numWrongPlan);
		
		if (item.numWrongPlan > 0) {
			tvSaituyen.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			tvSaituyen.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}
		if (item.numLessThan2Min > 0) {
			tv2phut.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			tv2phut.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}
		if (item.numMoreThan30Min > 0) {
			tv30phut.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			tv30phut.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}

		if (item.numWrongPlan > 0 || item.numMoreThan30Min > 0 || item.numLessThan2Min > 0) {
			int color = ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED);
			tvMaNVBH.setBackgroundColor(color);
			tvNVBH.setBackgroundColor(color);
			tvSodiemban.setBackgroundColor(color);
			tvDungtuyen.setBackgroundColor(color);
			tvSaituyen.setBackgroundColor(color);
			tv230phut.setBackgroundColor(color);
			tv30phut.setBackgroundColor(color);
			tv2phut.setBackgroundColor(color);
			visitCus.setBackgroundColor(color);
		} else {
			int background = R.drawable.style_row_default;
			ImageUtil.setBackgroundBySelectorDrawable(tvMaNVBH,background);
			ImageUtil.setBackgroundBySelectorDrawable(tvNVBH, background);
			ImageUtil.setBackgroundBySelectorDrawable(tvSodiemban, background);
			ImageUtil.setBackgroundBySelectorDrawable(tvDungtuyen, background);
			ImageUtil.setBackgroundBySelectorDrawable(tvSaituyen, background);
			ImageUtil.setBackgroundBySelectorDrawable(tv230phut, background);
			ImageUtil.setBackgroundBySelectorDrawable(tv30phut, background);
			ImageUtil.setBackgroundBySelectorDrawable(tv2phut, background);
			ImageUtil.setBackgroundBySelectorDrawable(visitCus, background);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
		if (v == visitCus) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW, visitCus, item);
		} else if (v == tvMaNVBH) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.STAFF_INFORMATION, tvMaNVBH, item);
		} else if (v == tvSaituyen) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GET_WRONG_PLAN_CUSTOMER, tvSaituyen, item);
		} else if (v == tv2phut) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GET_LESS_THAN_2_MINS, tv2phut, item);
		} else if (v == tv30phut) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GET_MORE_THAN_30_MINS, tv30phut, item);
		}
	}
}
