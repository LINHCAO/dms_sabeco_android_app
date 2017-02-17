/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ReportFocusProductItem;
import com.viettel.dms.dto.view.ReportSalesFocusEmployeeInfo;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * display report progress sales focus row. use for display report progress
 * sales focus view, report progress sales focus view detail
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressSalesFocusDetailRow extends TableRow implements OnClickListener {

	// action when click textview NPP
	public static final int ACTION_SHOW_REPORT_PROGRESS_SALE_FOCUS_DETAIL_CLICK_NPP = 0;
	// action when click textview gsnpp
	public static final int ACTION_SHOW_REPORT_PROGRESS_SALE_FOCUS_DETAIL_CLICK_GSNPP = 1;
	// table parent
	private View tableParent;
	// context
	Context _context;
	// view for row
	View view;
	// row control
	TableRow row;
	// display staff name
	TextView tvStaffName;
	// display staff owner name
	TextView tvStaffOwnerName;
	// display total
	TextView tvTotal;
	// linnerlayout for products forcus
	private LinearLayout llMHTT;
	// dto to display on row
	ReportSalesFocusEmployeeInfo currentDTO;
	// listener
	protected VinamilkTableListener listener;
	// flag check row sum ?
	boolean isRowSum = false;
	// check detail screen
	boolean isDetailScreen = false;

	/**
	 * @param context
	 */
	public TBHVReportProgressSalesFocusDetailRow(Context context, View aRow, boolean isRowSum, boolean isDetailScreen) {
		super(context);
		tableParent = aRow;
		this.isRowSum = isRowSum;
		this.isDetailScreen = isDetailScreen;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_progress_report_sales_focus_detail_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		tvStaffName = (TextView) view.findViewById(R.id.tvNVBH);
		tvStaffOwnerName = (TextView) view.findViewById(R.id.tvMaNVBH);
		if (isDetailScreen) {
			tvStaffName.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvStaffOwnerName.setTextColor(ImageUtil.getColor(R.color.BLACK));
			tvStaffOwnerName.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		} else {
			tvStaffOwnerName.setGravity(Gravity.CENTER);
			tvStaffName.setOnClickListener(this);
			tvStaffOwnerName.setOnClickListener(this);
		}

		llMHTT = (LinearLayout) view.findViewById(R.id.layMHTT);
		tvTotal = (TextView) view.findViewById(R.id.tvTotal);
		if (!isRowSum) {
			row.setOnClickListener(this);
			tvTotal.setVisibility(View.GONE);
			tvStaffName.setVisibility(View.VISIBLE);
			tvStaffOwnerName.setVisibility(View.VISIBLE);
		} else {
			tvTotal.setVisibility(View.VISIBLE);
			tvStaffName.setVisibility(View.GONE);
			tvStaffOwnerName.setVisibility(View.GONE);
		}
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	/**
	 * 
	 * render layout for row
	 * 
	 * @author: HaiTC3
	 * @param parentProgress
	 * @param item
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutForTBHVReportFocus(int parentProgress, ReportSalesFocusEmployeeInfo item) {
		currentDTO = item;
		if (!this.isRowSum) {
			tvStaffName.setText(item.staffName);
			if (this.isDetailScreen) {
				tvStaffOwnerName.setText(item.staffOwnerName);
			} else {
				tvStaffOwnerName.setText(item.staffOwnerCode);
			}
		}

		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LayoutParams param = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT);
		for (int i = 0, size = item.listFocusProductItem.size(); i < size; i++) {
			ReportFocusProductItem itemChildren = item.listFocusProductItem.get(i);

			View child = vi.inflate(R.layout.layout_tbhv_progress_report_sales_focus_row_item, null);

			TextView tvPlanMoney = (TextView) child.findViewById(R.id.tvPlanMoney);
			TextView tvSoldMoney = (TextView) child.findViewById(R.id.tvSoldMoney);
			TextView tvLeftMoney = (TextView) child.findViewById(R.id.tvLeftMoney);
			TextView tvSoldPercent = (TextView) child.findViewById(R.id.tvSoldPercent);
			if (this.isRowSum) {
				tvPlanMoney.setTypeface(null, Typeface.BOLD);
				tvSoldMoney.setTypeface(null, Typeface.BOLD);
				tvLeftMoney.setTypeface(null, Typeface.BOLD);
				tvSoldPercent.setTypeface(null, Typeface.BOLD);
			}

			tvPlanMoney.setText(StringUtil.parseAmountMoney(itemChildren.amountPlan));
			tvSoldMoney.setText(StringUtil.parseAmountMoney(itemChildren.amount));
			tvLeftMoney.setText(StringUtil.parseAmountMoney(itemChildren.remain));
			tvSoldPercent.setText(itemChildren.progress + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
			if (this.isRowSum) {
				tvPlanMoney.setTypeface(null, Typeface.BOLD);
				tvSoldMoney.setTypeface(null, Typeface.BOLD);
				tvLeftMoney.setTypeface(null, Typeface.BOLD);
				tvSoldPercent.setTypeface(null, Typeface.BOLD);
			}

			if (itemChildren.amountPlan > 0) {
				if (itemChildren.amount > 0 && itemChildren.progress < parentProgress) {
					tvSoldPercent.setTextColor(getContext().getResources().getColor(R.color.RED));
				}
				if (itemChildren.amount > 0 && itemChildren.progress >= parentProgress) {
					tvSoldPercent.setTextColor(ImageUtil.getColor(R.color.BLACK));
				}
				if (itemChildren.amount == 0) {
					tvSoldPercent.setTextColor(ImageUtil.getColor(R.color.RED));
				}
			}
			if (itemChildren.amountPlan <= 0) {
				if (itemChildren.amount > 0) {
					tvSoldPercent.setText("100%");
					tvSoldPercent.setTextColor(ImageUtil.getColor(R.color.BLACK));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
				if (itemChildren.amount == 0 && parentProgress == 0) {
					tvSoldPercent.setText("0%");
					tvSoldPercent.setTextColor(ImageUtil.getColor(R.color.BLACK));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
				if (itemChildren.amount == 0) {
					tvSoldPercent.setText("0%");
					tvSoldPercent.setTextColor(ImageUtil.getColor(R.color.RED));
					tvLeftMoney.setText(StringUtil.parseAmountMoney(0));
				}
			}
			llMHTT.addView(child, param);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvStaffOwnerName && this.listener != null) {
			this.listener.handleVinamilkTableRowEvent(ACTION_SHOW_REPORT_PROGRESS_SALE_FOCUS_DETAIL_CLICK_NPP,
					tableParent, currentDTO);
		} else if (v == tvStaffName && this.listener != null) {
			this.listener.handleVinamilkTableRowEvent(ACTION_SHOW_REPORT_PROGRESS_SALE_FOCUS_DETAIL_CLICK_GSNPP,
					tableParent, currentDTO);
		}
	}

}
