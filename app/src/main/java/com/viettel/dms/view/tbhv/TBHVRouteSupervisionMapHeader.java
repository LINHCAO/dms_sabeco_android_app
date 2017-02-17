/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * Header map THBV
 * 
 * @author : YenNTH
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVRouteSupervisionMapHeader extends LinearLayout {

	private TextView tvShopCode;// ma nha phan phoi
	private TextView tvStaffCodeNVBH;// thong tin gsbh
	private TextView tvStaffNameGS;// thong tin nhan vien 
	private TextView lbShopCode;// title ma nha phan phoi
	private TextView lbStaffCodeGS;// title thong tin gsbh
	private TextView lbStaffCodeNVBH;// title thong tin nv

	public TBHVRouteSupervisionMapHeader(Context context) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(
				R.layout.layout_tbhv_map_route_supervision_header, this);
		tvShopCode = (TextView) view.findViewById(R.id.tvMaNPP);
		tvStaffCodeNVBH = (TextView) view.findViewById(R.id.tvGSNPP);
		tvStaffNameGS = (TextView) view.findViewById(R.id.tvNVBH);
		lbShopCode = (TextView) view.findViewById(R.id.lbShopCode);
		lbStaffCodeGS = (TextView) view.findViewById(R.id.lbStaffCodeGS);
		lbStaffCodeNVBH = (TextView) view.findViewById(R.id.lbStaffCodeNVBH);
	}

	/**
	 * updateView
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void updateView(THBVRouteSupervisionItem routeSupervisionItem, String from) {
		if(from.equalsIgnoreCase(Constants.TYPE_TT)){
			tvShopCode.setVisibility(View.GONE);
			tvStaffCodeNVBH.setVisibility(View.GONE);
			lbShopCode.setVisibility(View.GONE);
			lbStaffCodeGS.setVisibility(View.GONE);
			lbStaffCodeNVBH.setText(StringUtil.getString(R.string.TEXT_LABEL_TTTT));
		}else {
			lbShopCode.setVisibility(View.VISIBLE);
			lbStaffCodeGS.setVisibility(View.VISIBLE);
			tvShopCode.setVisibility(View.VISIBLE);
			tvStaffCodeNVBH.setVisibility(View.VISIBLE);
			if (!StringUtil.isNullOrEmpty(routeSupervisionItem.shopCode)) {
				tvShopCode.setText(routeSupervisionItem.shopCode);
			} else {
				tvShopCode.setText(Constants.STR_BLANK);
			}
			if (!StringUtil.isNullOrEmpty(routeSupervisionItem.staffCodeNVBH)) {
				tvStaffCodeNVBH.setText(routeSupervisionItem.staffCodeNVBH + "-" + routeSupervisionItem.staffNameNVBH);
			} else {
				tvStaffCodeNVBH.setText(Constants.STR_BLANK);
			}
		}
		
		if (!StringUtil.isNullOrEmpty(routeSupervisionItem.staffCodeGS)) {
			tvStaffNameGS.setText(routeSupervisionItem.staffCodeGS + "-" + routeSupervisionItem.staffNameGS);
		} else {
			tvStaffNameGS.setText(Constants.STR_BLANK);
		}

	}

}
