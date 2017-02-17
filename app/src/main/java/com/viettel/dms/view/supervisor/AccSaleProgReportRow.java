/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.AccSaleProgReportDTO.AccSaleProgReportItem;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * row cua man hinh bao cao luy ke den ngay
 * 
 * @author hieunq1
 */
public class AccSaleProgReportRow extends TableRow implements OnClickListener {
	private View view;
	// tv ma nvbh
	public TextView tvMaNVBH;
	// tv nvbh
	public TextView tvNVBH;
	// tv doanh so ke hoach
	public TextView tvMoneyPlan;
	// tv doanh so thuc hien
	public TextView tvDMoneySold;
	// phan tram
	public TextView tvPercent;
	// doanh so con lai
	public TextView tvMoneyRemain;
	// sku ke hoach
	public TextView tvSKU_KH;
	// aku thuc hien
	public TextView tvSKU_target;
	// diem
	public TextView tvScore;
	// text notify when null
	TextView tvNotifyNull;
	private AccSaleProgReportItem data;

	protected VinamilkTableListener listener;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public AccSaleProgReportRow(Context context, boolean sum) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (sum) {
			view = vi.inflate(R.layout.layout_acc_sale_prog_report_sum_row, this);
			TableRow row = (TableRow) view.findViewById(R.id.row);
			row.setOnClickListener(this);
			tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
			tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
			tvMoneyPlan = (TextView) view.findViewById(R.id.tvMoneyPlan);
			tvDMoneySold = (TextView) view.findViewById(R.id.tvDMoneySold);
			tvMoneyRemain = (TextView) view.findViewById(R.id.tvMoneyRemain);
			tvPercent = (TextView) view.findViewById(R.id.tvPercent);
			tvSKU_KH = (TextView) view.findViewById(R.id.tvSKU_KH);
			tvSKU_target = (TextView) view.findViewById(R.id.tvSKU_target);
			tvScore = (TextView) view.findViewById(R.id.tvScore);
			tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);
		} else {
			view = vi.inflate(R.layout.layout_acc_sale_prog_report_row, this);
			TableRow row = (TableRow) view.findViewById(R.id.row);
			row.setOnClickListener(this);
			tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
			tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
			tvMoneyPlan = (TextView) view.findViewById(R.id.tvMoneyPlan);
			tvDMoneySold = (TextView) view.findViewById(R.id.tvDMoneySold);
			tvMoneyRemain = (TextView) view.findViewById(R.id.tvMoneyRemain);
			tvPercent = (TextView) view.findViewById(R.id.tvPercent);
			tvSKU_KH = (TextView) view.findViewById(R.id.tvSKU_KH);
			tvSKU_target = (TextView) view.findViewById(R.id.tvSKU_target);
			tvScore = (TextView) view.findViewById(R.id.tvScore);
		}
		tvMaNVBH.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (listener != null) {
					listener.handleVinamilkTableRowEvent(0, v, data);
				}
			}
		});
	}

	/**
	 * render row
	 * 
	 * @param accSaleProgReportItem
	 * @param warm
	 */
	public void render(AccSaleProgReportItem accSaleProgReportItem, boolean warm) {
		this.data = accSaleProgReportItem;
		tvMaNVBH.setText(accSaleProgReportItem.staffCode);
		tvNVBH.setText(accSaleProgReportItem.staffName);
		tvMoneyPlan.setText(StringUtil.parseAmountMoney(Constants.STR_BLANK
				+ Math.round((double) accSaleProgReportItem.moneyPlan / 1000.0)));
		tvDMoneySold.setText(StringUtil.parseAmountMoney(Constants.STR_BLANK
				+ Math.round((double) accSaleProgReportItem.moneySold / 1000.0)));
		tvMoneyRemain.setText(StringUtil.parseAmountMoney(Constants.STR_BLANK
				+ Math.round((double) accSaleProgReportItem.moneyRemain / 1000.0)));
		String strPercent = StringUtil.formatDoubleString(StringUtil.decimalFormatSymbols("#.##",
				accSaleProgReportItem.percent));
		tvPercent.setText(strPercent + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (warm) {
			tvPercent.setTextColor(this.getContext().getResources().getColor(R.color.RED));
		}
		tvSKU_KH.setText(Constants.STR_BLANK + accSaleProgReportItem.sku_kh);
		tvSKU_target.setText(Constants.STR_BLANK + accSaleProgReportItem.sku_target);
		tvScore.setText(Constants.STR_BLANK + accSaleProgReportItem.score);
	}

	/**
	 * render sum row
	 * 
	 * @param planTotal
	 * @param soldTotal
	 * @param remainTotal
	 */
	public void renderSum(long planTotal, long soldTotal, long remainTotal, double perSoldTotal, boolean warm) {
		tvMoneyPlan.setText(StringUtil.parseAmountMoney(Constants.STR_BLANK + Math.round((double) planTotal / 1000.0)));
		tvDMoneySold
				.setText(StringUtil.parseAmountMoney(Constants.STR_BLANK + Math.round((double) soldTotal / 1000.0)));
		tvMoneyRemain.setText(StringUtil.parseAmountMoney(Constants.STR_BLANK
				+ Math.round((double) remainTotal / 1000.0)));
		// <HaiTC> update display percent
		String strPercent = StringUtil.formatDoubleString(StringUtil.decimalFormatSymbols("#.##", perSoldTotal));
		tvPercent.setText(strPercent + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (warm) {
			tvPercent.setTextColor(this.getContext().getResources().getColor(R.color.RED));
		}
		// </HaiTC>
	}

	/**
	 * 
	 * show row notify null
	 * 
	 * @param contentNotify
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Nov 6, 2012
	 */
	public void showNotifyNull(String contentNotify) {
		tvMaNVBH.setVisibility(View.GONE);
		tvNVBH.setVisibility(View.GONE);
		tvMoneyPlan.setVisibility(View.GONE);
		tvDMoneySold.setVisibility(View.GONE);
		tvMoneyRemain.setVisibility(View.GONE);
		tvPercent.setVisibility(View.GONE);
		tvSKU_KH.setVisibility(View.GONE);
		tvSKU_target.setVisibility(View.GONE);
		tvScore.setVisibility(View.GONE);
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setBackgroundColor(ImageUtil.getColor(R.color.WHITE));
		tvNotifyNull.setText(contentNotify);
	}

	@Override
	public void onClick(View paramView) {
		// TODO Auto-generated method stub

	}

}
