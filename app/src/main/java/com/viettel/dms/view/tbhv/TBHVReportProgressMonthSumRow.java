/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.ReportProgressMonthCellDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * dong bao cao tien do luy ke thang trong tablet row total, su dung cho
 * TBHVReportProgressMonthView
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressMonthSumRow extends TableRow {
	// // table parent
	// private View tableParent;
	// this context
	Context _context;
	// view for row
	View view;
	// row control
	TableRow row;
	// text veiw display sum
	TextView tvSum;
	// display amount plan
	TextView tvAmountPlan;
	// display amount done
	TextView tvAmountDone;
	// display amount progress
	TextView tvAmountProgress;
	// display amount remain
	TextView tvAmountRemain;
	// display sku plan
	TextView tvSKUPlan;
	// display sku done
	TextView tvSKUDone;

	public TBHVReportProgressMonthSumRow(Context context, View aRow) {
		super(context);
		_context = context;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_report_progress_month_row_sum, this);
		row = (TableRow) view.findViewById(R.id.row);
		tvSum = (TextView) view.findViewById(R.id.tvNameNPP);
		tvAmountPlan = (TextView) view.findViewById(R.id.tvAmountPLAN);
		tvAmountDone = (TextView) view.findViewById(R.id.tvAmountDONE);
		tvAmountProgress = (TextView) view.findViewById(R.id.tvAmountProgress);
		tvAmountRemain = (TextView) view.findViewById(R.id.tvAmountRemain);
		tvSKUPlan = (TextView) view.findViewById(R.id.tvSKUPlan);
		tvSKUDone = (TextView) view.findViewById(R.id.tvSKUDone);

	}

	/**
	 * 
	 * render layout for row
	 * 
	 * @author: HaiTC3
	 * @param object
	 * @return: void
	 * @throws:
	 */
	public void renderLayoutForRow(ReportProgressMonthCellDTO object, int parentPercent) {
		tvAmountPlan.setText(StringUtil.parseAmountMoney(object.amountPlan));
		tvAmountDone.setText(StringUtil.parseAmountMoney(object.amountDone));
		tvAmountProgress.setText(object.progressAmountDone + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (object.progressAmountDone < parentPercent) {
			tvAmountProgress.setTextColor(ImageUtil.getColor(R.color.RED));
		}
		tvAmountRemain.setText(StringUtil.parseAmountMoney(object.amountRemain));
		tvSKUPlan.setText(Constants.STR_BLANK);
		tvSKUDone.setText(Constants.STR_BLANK);
	}
}
