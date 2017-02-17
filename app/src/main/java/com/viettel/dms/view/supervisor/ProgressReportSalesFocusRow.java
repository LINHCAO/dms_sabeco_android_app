/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO.InfoProgressEmployeeDTO;
import com.viettel.dms.dto.view.ProgressReportSalesFocusDTO.ReportProductFocusItem;
import com.viettel.dms.util.GlobalUtil;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.dms.view.main.GlobalBaseActivity;
import com.viettel.sabeco.R;

/**
 * 
 * display row for report sales focus product
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class ProgressReportSalesFocusRow extends TableRow implements OnClickListener {
	public static final int STAFF_INFORMATION = 0;
	private final Context context;
	private final View view;
	// Ma NVBH
	TextView tvMaNVBH;
	// Nhan vien BH
	TextView tvNVBH;
	// text notify when null
	TextView tvNotifyNull;

	protected VinamilkTableListener listener;
	private InfoProgressEmployeeDTO dto;
	private LinearLayout layMMTT;

	public VinamilkTableListener getListener() {
		return listener;
	}

	/**
	 * 
	 * set listener for row
	 * 
	 * @param _listener
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	public void setListener(VinamilkTableListener _listener) {
		this.listener = _listener;
	}
	
	public ProgressReportSalesFocusRow(Context con){
		super(con);
		context = con;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_progress_report_sales_focus_sum_row, this);
	}

	public ProgressReportSalesFocusRow(Context context, boolean sum) {
		// TODO Auto-generated constructor stubư
		this(context);
		setOnClickListener(this);

		tvMaNVBH = (TextView) view.findViewById(R.id.tvMaNVBH);
		tvMaNVBH.setOnClickListener(this);
		tvNVBH = (TextView) view.findViewById(R.id.tvNVBH);
		if (sum) {
			tvNVBH.setTypeface(null, Typeface.BOLD);
		}
		layMMTT = (LinearLayout) view.findViewById(R.id.layMHTT);
		tvNotifyNull = (TextView) view.findViewById(R.id.tvNotifyNull);
	}

	/**
	 * 
	 * update layout with data
	 * 
	 * @param progReportSalesFocusDTO
	 * @param dto
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	public void setDataRow(ProgressReportSalesFocusDTO progReportSalesFocusDTO, InfoProgressEmployeeDTO dto) {
		// TODO Auto-generated method stub
		this.dto = dto;
		tvMaNVBH.setText(dto.staffCode);
		tvNVBH.setText(dto.staffName);
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < dto.arrRptFocusItem.size(); i++) {
			ReportProductFocusItem item = dto.arrRptFocusItem.get(i);

			View child = vi.inflate(R.layout.layout_progress_report_sales_focus_row_item, null);

			TextView tvPlanMoney = (TextView) child.findViewById(R.id.tvPlanMoney);
			TextView tvSoldMoney = (TextView) child.findViewById(R.id.tvSoldMoney);
			TextView tvLeftMoney = (TextView) child.findViewById(R.id.tvLeftMoney);
			TextView tvSoldPercent = (TextView) child.findViewById(R.id.tvSoldPercent);

			// tvPlanMoney.setText(StringUtil.parseAmountMoney("" +
			// Math.round(item.planMoney / 1000.0)));
			// tvSoldMoney.setText(StringUtil.parseAmountMoney("" +
			// Math.round(item.soldMoney / 1000.0)));
			// tvLeftMoney.setText(StringUtil.parseAmountMoney("" +
			// Math.round(item.leftMoney / 1000.0)));
			tvPlanMoney.setText(StringUtil.parseAmountMoney("" + Math.round(item.planMoney)));
			tvSoldMoney.setText(StringUtil.parseAmountMoney("" + Math.round(item.soldMoney)));
			tvLeftMoney.setText(StringUtil.parseAmountMoney("" + Math.round(item.leftMoney)));
			String strPercent = StringUtil
					.formatDoubleString(StringUtil.decimalFormatSymbols("#.##", item.soldPercent));
			tvSoldPercent.setText(strPercent + "%");

			if (item.planMoney > 0) {
				if (item.soldMoney > 0 && item.soldPercent < progReportSalesFocusDTO.Progress) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.RED));
				}
				if (item.soldMoney > 0 && item.soldPercent >= progReportSalesFocusDTO.Progress) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.BLACK));
				}
				if (item.soldMoney == 0) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.RED));
				}
			}
			if (item.planMoney <= 0) {
				if (item.soldMoney > 0) {
					tvSoldPercent.setText("100%");
					tvSoldPercent.setTextColor(this.getContext().getResources().getColor(R.color.BLACK));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
				if (item.soldMoney == 0 && progReportSalesFocusDTO.Progress == 0) {
					tvSoldPercent.setText("0%");
					tvSoldPercent.setTextColor(this.getContext().getResources().getColor(R.color.BLACK));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
				if (item.soldMoney == 0) {
					tvSoldPercent.setText("0%");
					tvSoldPercent.setTextColor(this.getContext().getResources().getColor(R.color.RED));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
			}
			layMMTT.addView(child);

		}

	}

	/**
	 * 
	 * display row sum
	 * 
	 * @param progReportSalesFocusDTO
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 10, 2013
	 */
	public void renderSum(ProgressReportSalesFocusDTO progReportSalesFocusDTO) {
		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (ReportProductFocusItem focusItem: progReportSalesFocusDTO.arrRptFocusItemTotal) {
			View child = vi.inflate(R.layout.layout_progress_report_sales_focus_sum_row_item, null);
			TextView tvPlanMoney = (TextView) child.findViewById(R.id.tvPlanMoney);
			TextView tvSoldMoney = (TextView) child.findViewById(R.id.tvSoldMoney);
			TextView tvLeftMoney = (TextView) child.findViewById(R.id.tvLeftMoney);
			TextView tvSoldPercent = (TextView) child.findViewById(R.id.tvSoldPercent);

			tvPlanMoney.setText(StringUtil.parseAmountMoney("" + Math.round(focusItem.planMoney)));
			tvSoldMoney.setText(StringUtil.parseAmountMoney("" + Math.round(focusItem.soldMoney)));
			// tvLeftMoney.setText(StringUtil.parseAmountMoney(""
			// + Math.round(focusItem.leftMoney )));
			tvLeftMoney.setText(StringUtil
					.parseAmountMoney(focusItem.planMoney >= focusItem.soldMoney ? (focusItem.planMoney - focusItem.soldMoney) : 0));

			// <HaiTC>: update show percent
			String strPercent = StringUtil
			.formatDoubleString(StringUtil.decimalFormatSymbols("#.##", focusItem.soldPercent));
			tvSoldPercent.setText(strPercent + "%");

			if (focusItem.planMoney > 0) {
				if (focusItem.soldMoney > 0 && focusItem.soldPercent < progReportSalesFocusDTO.Progress) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.RED));
				}
				if (focusItem.soldMoney > 0 && focusItem.soldPercent >= progReportSalesFocusDTO.Progress) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.BLACK));
				}
				if (focusItem.soldMoney == 0) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.RED));
				}
			}
			if (focusItem.planMoney <= 0) {
				if (focusItem.soldMoney > 0) {
					tvSoldPercent.setText("100%");
					tvSoldPercent.setTextColor(this.getContext().getResources().getColor(R.color.BLACK));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
				if (focusItem.soldMoney == 0 && progReportSalesFocusDTO.Progress == 0) {
					tvSoldPercent.setText("0%");
					tvSoldPercent.setTextColor(this.getContext().getResources().getColor(R.color.BLACK));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
				if (focusItem.soldMoney == 0) {
					tvSoldPercent.setText("0%");
					tvSoldPercent.setTextColor(this.getContext().getResources().getColor(R.color.RED));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
			}
			// </HaiTC> end

			layMMTT.addView(child);

		}
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
		layMMTT.setVisibility(View.GONE);
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setBackgroundColor(ImageUtil.getColor(R.color.WHITE));
		tvNotifyNull.setText(contentNotify);
	}

	public void onClick(View arg0) {
		if (arg0 == tvMaNVBH) {
			listener.handleVinamilkTableRowEvent(STAFF_INFORMATION, this, dto);
		} else if (arg0 == this && context != null) {
			GlobalUtil.forceHideKeyboard((GlobalBaseActivity) context);
		}
	}

}
