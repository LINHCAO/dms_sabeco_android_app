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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO;
import com.viettel.dms.dto.view.TBHVTrainingPlanDayResultReportDTO.TBHVTrainingPlanDayResultReportItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Giam sat huan luyen ngay
 * 
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
@SuppressLint("ViewConstructor")
public class TBHVDayTrainingSupervisionRow extends TableRow implements OnClickListener {
	private View view;// view
	private TBHVTrainingPlanDayResultReportItem data;// data
	protected VinamilkTableListener listener;// listener
	private TextView tvMaKH;// textview tvMaKH
	private TextView tvSTT;// textview tvSTT
	private TextView tvDcKH;// textview tvDcKH
	private TextView tvTenKH;// textview tvTenKH
	private TextView tvPlanAmount;// textview tvPlanAmount
	private TextView tvAmount;// textview tvAmount
	private TextView tvPoint;// textview tvPoint
	private TextView tvMoi;// textview tvMoi
	private TextView tvON;// textview tvONtvNT
	private TextView tvNT;// textview tvNT
	private ImageView visitCus;
	LinearLayout llvisitCus;
	TextView tvNoResultInfo;
	protected VinamilkTableView parentTable;// textview parentTable
	private boolean isValid;

	public TBHVDayTrainingSupervisionRow(Context context, boolean sum, VinamilkTableView parentTable) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi.inflate(R.layout.layout_day_training_supervision_row_sum, this);
		} else {
			view = vi.inflate(R.layout.layout_day_training_supervision_row, this);
			visitCus = (ImageView) view.findViewById(R.id.visitCus);
			visitCus.setOnClickListener(this);
		}
		TableRow row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		this.parentTable = parentTable;
		tvSTT = (TextView) view.findViewById(R.id.tvSTT);
		tvMaKH = (TextView) view.findViewById(R.id.tvMaKH);
		tvTenKH = (TextView) view.findViewById(R.id.tvTenKH);
		tvDcKH = (TextView) view.findViewById(R.id.tvDcKH);
		tvPlanAmount = (TextView) view.findViewById(R.id.tvPlanAmount);
		tvAmount = (TextView) view.findViewById(R.id.tvAmount);
		tvPoint = (TextView) view.findViewById(R.id.tvPoint);
		tvMoi = (TextView) view.findViewById(R.id.tvMoi);
		tvON = (TextView) view.findViewById(R.id.tvON);
		tvNT = (TextView) view.findViewById(R.id.tvNT);
		llvisitCus = (LinearLayout) view.findViewById(R.id.llvisitCus);
		tvNoResultInfo = (TextView) view.findViewById(R.id.tvNoResultInfo);

	}

	/**
	 * render row
	 * 
	 * @param bd
	 * @param accSaleProgReportItem
	 */
	public void render(TBHVTrainingPlanDayResultReportItem item, boolean isGsnppTrainedToday, boolean bd) {
		this.data = item;
		tvSTT.setText("" + data.stt);
		tvMaKH.setText(data.custCode != null && data.custCode.length() > 3 ? data.custCode.substring(0, 3)
				: data.custCode);
		tvTenKH.setText(data.custName);
		tvDcKH.setText(data.custAddr);
		tvPlanAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(data.amountMonth / 1000.0))));
		tvAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(data.amount / 1000.0))));
		tvMoi.setText(String.valueOf(data.isNew));
		tvON.setText(String.valueOf(data.isOn));
		tvNT.setText(String.valueOf(data.isOr));
		tvPoint.setText(String.valueOf(data.score));

		if (isGsnppTrainedToday && bd) {
			visitCus.setImageResource(R.drawable.icon_checkpoint);
			isValid = true;
		} else {
			visitCus.setImageBitmap(null);
			isValid = false;
		}
	}

	/**
	 * render sum row
	 * 
	 * @param planTotal
	 * @param soldTotal
	 * @param remainTotal
	 */
	public void renderSum(TBHVTrainingPlanDayResultReportDTO dto) {
		tvPlanAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(dto.amountMonth / 1000.0))));
		tvAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(dto.amount / 1000.0))));
		tvMoi.setText(String.valueOf(dto.isNew));
		tvON.setText(String.valueOf(dto.isOn));
		tvNT.setText(String.valueOf(dto.isOr));
		tvPoint.setText(String.valueOf(dto.score));
	}

	/**
	 * Mo ta muc dich cua ham
	 * 
	 * @author: TamPQ
	 * @return: voidvoid
	 * @throws:
	 */
	public void renderNoResult() {
		tvSTT.setVisibility(View.GONE);
		tvMaKH.setVisibility(View.GONE);
		tvTenKH.setVisibility(View.GONE);
		tvDcKH.setVisibility(View.GONE);
		tvPlanAmount.setVisibility(View.GONE);
		tvAmount.setVisibility(View.GONE);
		tvMoi.setVisibility(View.GONE);
		tvON.setVisibility(View.GONE);
		tvNT.setVisibility(View.GONE);
		tvPoint.setVisibility(View.GONE);
		visitCus.setVisibility(View.GONE);
		llvisitCus.setVisibility(View.GONE);
		tvNoResultInfo.setText(StringUtil.getString(R.string.MESSAGE_NO_INFOMATION));
		tvNoResultInfo.setVisibility(View.VISIBLE);
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View v) {
		if (v == visitCus && listener != null && isValid) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.ACTION_TBHV_GO_TO_ADD_REQUIREMENT, parentTable,
					data);
		}
	}

}
