/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanHistoryAccDTO.TBHVHistoryPlanTrainingItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * TBHVHistoryPlanTrainingRow
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
@SuppressLint("ViewConstructor")
public class TBHVHistoryPlanTrainingRow extends TableRow implements OnClickListener {
	private View view;

	private TBHVHistoryPlanTrainingItem data; // dto item

	protected VinamilkTableListener listener; // activity

	private TextView tvMaNPP; // textview MaNVBH

	private TextView tvTenNPP;// textview tvNVBH

	private TextView tvDate;// textview tvDate

	private TextView tvPlanAmount;// textview tvPlanAmount

	private TextView tvAmount;// textview tvAmount

	private TextView tvKH;// textview tvKH

	private TextView tvPSDS;// textview tvPSDS

	private TextView tvMoi;// textview tvMoi

	private TextView tvON;// textview tvON

	private TextView tvPoint;// textview tvPoint

	private TextView tvNT;// textview tvNT

	private TextView tvTenNVBH;// textview tvTenNVBH
	public TextView tvNoResultInfo;

	public TBHVHistoryPlanTrainingRow(Context context, boolean sum) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// if (sum) {
		// view = vi.inflate(R.layout.layout_history_plan_training_row_sum,
		// this);
		//
		// } else {
		view = vi.inflate(R.layout.layout_tbhv_history_plan_training_row, this);
		// }
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);

		tvMaNPP = (TextView) view.findViewById(R.id.tvMaNPP);
		tvTenNPP = (TextView) view.findViewById(R.id.tvTenNPP);
		tvTenNPP.setOnClickListener(this);
		tvTenNVBH = (TextView) view.findViewById(R.id.tvTenNVBH);
		tvDate = (TextView) view.findViewById(R.id.tvDate);
		tvPlanAmount = (TextView) view.findViewById(R.id.tvPlanAmount);
		tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		tvKH = (TextView) view.findViewById(R.id.tvKH);
		tvPSDS = (TextView) view.findViewById(R.id.tvPSDS);
		tvMoi = (TextView) view.findViewById(R.id.tvMoi);
		tvON = (TextView) view.findViewById(R.id.tvON);
		tvNT = (TextView) view.findViewById(R.id.tvNT);
		tvPoint = (TextView) view.findViewById(R.id.tvPoint);
		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);
	}

	/**
	 * render
	 * 
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void render(TBHVHistoryPlanTrainingItem item, TBHVTrainingPlanHistoryAccDTO trainedDto) {
		this.data = item;
		tvMaNPP.setText(data.nvbhShopCode);
		tvTenNPP.setText(data.staffName);
		tvTenNVBH.setText(data.nvbhStaffName);
		tvDate.setText(data.date);
		tvPlanAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(data.amountMonth / 1000.0))));
		tvAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(data.amount / 1000.0))));
		tvKH.setText(String.valueOf(data.numCustomerPlan));
		tvPSDS.setText(String.valueOf(data.numCustomerOrder));
		tvMoi.setText(String.valueOf(data.numCustomerNew));
		tvON.setText(String.valueOf(data.numCustomerIr));
		tvNT.setText(String.valueOf(data.numCustomerOr));
		tvPoint.setText(String.valueOf((float) Math.round(data.score * 100) / 100.0));

		if (trainedDto != null && item.isTrainedByTBHV(trainedDto)) {
			tvMaNPP.setTextColor(ImageUtil.getColor(R.color.RED));
			tvTenNPP.setTextColor(ImageUtil.getColor(R.color.RED));
			tvTenNVBH.setTextColor(ImageUtil.getColor(R.color.RED));
			tvDate.setTextColor(ImageUtil.getColor(R.color.RED));
			tvPlanAmount.setTextColor(ImageUtil.getColor(R.color.RED));
			tvAmount.setTextColor(ImageUtil.getColor(R.color.RED));
			tvKH.setTextColor(ImageUtil.getColor(R.color.RED));
			tvPSDS.setTextColor(ImageUtil.getColor(R.color.RED));
			tvMoi.setTextColor(ImageUtil.getColor(R.color.RED));
			tvON.setTextColor(ImageUtil.getColor(R.color.RED));
			tvNT.setTextColor(ImageUtil.getColor(R.color.RED));
			tvPoint.setTextColor(ImageUtil.getColor(R.color.RED));
		} else {
			tvMaNPP.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvTenNPP.setTextColor(ImageUtil.getColor(R.color.COLOR_USER_NAME));
			tvTenNVBH.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvDate.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvPlanAmount.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvAmount.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvKH.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvPSDS.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvMoi.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvON.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvNT.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvPoint.setTextColor(ImageUtil.getColor(R.color.BLACK));
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
		tvMaNPP.setVisibility(View.GONE);
		tvTenNPP.setVisibility(View.GONE);
		tvTenNVBH.setVisibility(View.GONE);
		tvDate.setVisibility(View.GONE);
		tvPlanAmount.setVisibility(View.GONE);
		tvAmount.setVisibility(View.GONE);
		tvKH.setVisibility(View.GONE);
		tvPSDS.setVisibility(View.GONE);
		tvMoi.setVisibility(View.GONE);
		tvON.setVisibility(View.GONE);
		tvNT.setVisibility(View.GONE);
		tvPoint.setVisibility(View.GONE);
		tvNoResultInfo.setVisibility(View.VISIBLE);
		tvNoResultInfo.setText(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public VinamilkTableListener getListener() {
		return listener;
	}

	@Override
	public void onClick(View v) {
		if (v == tvTenNPP) {
			if (listener != null)
				listener.handleVinamilkTableRowEvent(ActionEventConstant.GSNPP_TRAINING_PLAN_FOR_TBHV, v, data);
		}
	}

}
