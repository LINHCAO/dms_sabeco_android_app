/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO.GSNPPTrainingResultReportDayItem;
import com.viettel.dms.dto.view.GSNPPTrainingResultDayReportDTO.VISIT_STATUS;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.map.dto.LatLng;
import com.viettel.sabeco.R;

/**
 * row cua man hinh bao cao ket qua huan luyen nhan vien ban hang
 * 
 * @author hieunq1
 */
@SuppressLint("ViewConstructor")
public class GSNPPTrainingResultReportDayRow extends TableRow implements OnClickListener {
	private View view;
	private GSNPPTrainingResultReportDayItem data;
	protected VinamilkTableListener listener;
	private TextView tvMaKH;
	private TextView tvSTT;
	private TextView tvDcKH;
	private TextView tvTenKH;
	private TextView tvPlanAmount;
	private TextView tvAmount;
	private TextView tvPoint;
	private TextView tvMoi;
	private TextView tvON;
	private TextView tvNT;
	private ImageView ivCheck;
	private boolean isValid = false;
	protected VinamilkTableView parentTable;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public GSNPPTrainingResultReportDayRow(Context context, boolean sum, VinamilkTableView parentTable) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi.inflate(R.layout.layout_staff_train_result_report_row_sum, this);
		} else {
			view = vi.inflate(R.layout.layout_staff_train_result_report_row, this);
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
		ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
		if (!sum) {
			ivCheck.setOnClickListener(this);
		}
	}

	/**
	 * render row
	 * 
	 * @param bd
	 * @param accSaleProgReportItem
	 */
	public void render(GSNPPTrainingResultReportDayItem item, double dis, double lat, double lng, boolean bd) {
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

		LatLng myLatLng = new LatLng(lat, lng);
		LatLng staffLatLng = new LatLng(item.lat, item.lng);

		if (bd) {
			if (item.isOr == 0) {
				if (item.visit == VISIT_STATUS.VISITED || item.visit == VISIT_STATUS.VISITING) {
					ivCheck.setImageResource(R.drawable.icon_check);
					isValid = true;
				} else {
					if (myLatLng.lat > 0 && myLatLng.lng > 0 && staffLatLng.lat > 0 && staffLatLng.lng > 0) {
						double distance = GlobalUtil.getDistanceBetween(myLatLng, staffLatLng);
						if (distance <= dis) {
							ivCheck.setImageResource(R.drawable.icon_checkpoint);
							isValid = true;
						} else {
							ivCheck.setImageBitmap(null);
							isValid = false;
						}

					} else {
						ivCheck.setImageBitmap(null);
						isValid = false;
					}
				}
			} else {
				ivCheck.setImageResource(R.drawable.icon_checkpoint);
				isValid = true;
			}
		} else {
			ivCheck.setImageBitmap(null);
			isValid = false;
		}
	}

	/**
	 * render sum row
	 * 
	 * @param staffScore
	 * 
	 * @param planTotal
	 * @param soldTotal
	 * @param remainTotal
	 */
	public void renderSum(GSNPPTrainingResultDayReportDTO dto, double staffScore) {
		tvPlanAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(dto.amountMonth / 1000.0))));
		tvAmount.setText(StringUtil.parseAmountMoney(String.valueOf(Math.round(dto.amount / 1000.0))));
		tvMoi.setText(String.valueOf(dto.isNew));
		tvON.setText(String.valueOf(dto.isOn));
		tvNT.setText(String.valueOf(dto.isOr));
		// tvPoint.setText(String.valueOf(dto.score));
		tvPoint.setText(String.valueOf(staffScore));
	}

	@Override
	public void onClick(View v) {
		if (v == ivCheck) {
			if (isValid && listener != null) {
				listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_REVIEWS_STAFF_VIEW, parentTable, data);
			}
		}
	}
}
