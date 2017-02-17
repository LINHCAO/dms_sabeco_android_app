/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.STATUS;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem;
import com.viettel.dms.dto.view.TBHVRouteSupervisionDTO.THBVRouteSupervisionItem.Customer;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * Giam sat lo trinh Row
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVRouteSupervisionRow extends TableRow implements OnClickListener {

	private View view;// view
	private TableRow row;// row
	public TextView tvCodeNpp;// textview Name Npp
	public TextView tvNameGsnpp;// textview Name Gsnpp
	public TextView tvNameTraining;// textview Name Gsnpp
	public TextView tvNameNVBH;// textview Name NVBH
	public TextView tvNumSaleStore;// textview Num Sale Store
	public TextView tvNumRightPlan;// textview Num Right Plan
	public TextView tvNumWrongPlan;// textview Num Wrong Plan
	public ImageView ivMap;// Map
	public LinearLayout llvisitCus;
	public TextView tvNoResultInfo;
	private VinamilkTableListener listener;// listener
	THBVRouteSupervisionItem item;//
	Context context;// activity

	public TBHVRouteSupervisionRow(Context context, TBHVRouteSupervisionView frag) {
		super(context);
		this.context = context;
		this.listener = (VinamilkTableListener) frag;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_route_supervision_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvCodeNpp = (TextView) view.findViewById(R.id.tvNameNpp);
		tvNameNVBH = (TextView) view.findViewById(R.id.tvNameNVBH);
		tvNameGsnpp = (TextView) view.findViewById(R.id.tvNameGsnpp);
		tvNameTraining = (TextView) view.findViewById(R.id.tvTraining);
		tvNameTraining.setOnClickListener(this);
		tvNumSaleStore = (TextView) view.findViewById(R.id.tvNumSaleStore);
		tvNumSaleStore.setOnClickListener(this);
		tvNumRightPlan = (TextView) view.findViewById(R.id.tvNumRightPlan);
		tvNumWrongPlan = (TextView) view.findViewById(R.id.tvNumWrongPlan);
		tvNumWrongPlan.setOnClickListener(this);
		ivMap = (ImageView) view.findViewById(R.id.ivMap);
		ivMap.setOnClickListener(this);
		llvisitCus = (LinearLayout) view.findViewById(R.id.llvisitCus);
		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
	}

	/**
	 * render
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void render(TBHVRouteSupervisionDTO.THBVRouteSupervisionItem item) {
		this.item = item;
		tvCodeNpp.setText(item.shopCode);
		tvNameGsnpp.setText(item.staffNameGS);
		tvNameNVBH.setText(item.staffNameNVBH);
		if (item.gsnppRealSeq.size() > 0) {
			Customer cus = item.gsnppRealSeq.get(item.gsnppRealSeq.size() - 1);
			if (cus.isVisting) {
				tvNameTraining.setEnabled(true);
				tvNameTraining.setText(cus.codeName);
				// KH dang ghe tham sai tuyen hay dung tuyen
				if (cus.status == STATUS.RIGHT_PLAN) {
					tvNameTraining.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
				} else {
					tvNameTraining.setTextColor(ImageUtil.getColor(R.color.RED));
				}
			} else {
				tvNameTraining.setEnabled(false);
				tvNameTraining.setText("");
			}
		} else {
			tvNameTraining.setEnabled(false);
			tvNameTraining.setText("");
		}

		tvNumSaleStore.setText("" + item.numGsnppVisitedStore + "/" + item.numNvbhVisitedStore + "/"
				+ item.numSaleStore);
		tvNumRightPlan.setText("" + item.numRightPlan);
		tvNumWrongPlan.setText("" + item.numWrongPlan);

		// set mau text
		if (item.numSaleStore > 0) {
			tvNumSaleStore.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			tvNumSaleStore.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}
		if (item.numWrongPlan > 0) {
			tvNumWrongPlan.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
		} else {
			tvNumWrongPlan.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}

		// set background cho tung row
		if (item.numWrongPlan > 0) {
			tvCodeNpp.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvCodeNpp.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNameGsnpp.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvNameGsnpp.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNameTraining.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvNameTraining.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNameNVBH.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvNameNVBH.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNumSaleStore.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvNumRightPlan.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			tvNumWrongPlan.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
			ivMap.setBackgroundColor(ImageUtil.getColor(R.color.COLOR_VISIT_STORE_CLOSED));
		} else {
			tvCodeNpp.setBackgroundResource(R.drawable.style_row_default);
			tvCodeNpp.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNameGsnpp.setBackgroundResource(R.drawable.style_row_default);
			tvNameGsnpp.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNameTraining.setBackgroundResource(R.drawable.style_row_default);
			tvNameTraining.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNameNVBH.setBackgroundResource(R.drawable.style_row_default);
			tvNameNVBH.setPadding(GlobalUtil.dip2Pixel(15), 0, GlobalUtil.dip2Pixel(15), 0);
			tvNumSaleStore.setBackgroundResource(R.drawable.style_row_default);
			tvNumRightPlan.setBackgroundResource(R.drawable.style_row_default);
			tvNumWrongPlan.setBackgroundResource(R.drawable.style_row_default);
			ivMap.setBackgroundResource(R.drawable.style_row_default);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == row && listener != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
		if (v == ivMap) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ActionEventConstant.TBHV_ROUTE_SUPERVISION_MAP, null, item);
			}
		} else if (v == tvNameTraining) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ActionEventConstant.TBHV_ROUTE_SUPERVISION_TRAINING_CUS, null,
						item);
			}
		} else if (v == tvNumSaleStore) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ActionEventConstant.TBHV_ROUTE_SUPERVISION_VISITED_CUS, null, item);
			}
		} else if (v == tvNumWrongPlan) {
			if (listener != null) {
				listener.handleVinamilkTableRowEvent(ActionEventConstant.TBHV_ROUTE_SUPERVISION_WRONG_CUS, null, item);
			}
		}
	}
	
	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	public void renderNoResult() {
		tvCodeNpp.setVisibility(View.GONE);
		tvNameGsnpp.setVisibility(View.GONE);
		tvNameTraining.setVisibility(View.GONE);
		tvNameNVBH.setVisibility(View.GONE);
		tvNumSaleStore.setVisibility(View.GONE);
		tvNumRightPlan.setVisibility(View.GONE);
		tvNumWrongPlan.setVisibility(View.GONE);
		ivMap.setVisibility(View.GONE);
		llvisitCus.setVisibility(View.GONE);
		tvNoResultInfo.setVisibility(View.VISIBLE);
		tvNoResultInfo.setText(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
	}

}
