/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.dto.view.ReportProgressMonthCellDTO;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * dong bao cao tien do luy ke thang trong tablet, su dung cho
 * TBHVReportProgressMonthView
 * 
 * @author: HaiTC3
 * @version: 1.1
 * @since: 1.0
 */
public class TBHVReportProgressMonthDetailRow extends TableRow implements OnClickListener {
	// table parent
	private View tableParent;
	// this context for row
	Context _context;
	// view of row
	View view;
	// row control
	TableRow row;
	// display npp
	TextView tvNameNPP;
	// display gsnpp
	TextView tvNameGSNPP;
	// display amount plan
	TextView tvAmountPLAN;
	// display done amount
	TextView tvAmountDONE;
	// display amount progress
	TextView tvAmountProgress;
	// display amount remain
	TextView tvAmountRemain;
	// sku plan
	TextView tvSKUPlan;
	// done sku
	TextView tvSKUDone;
	protected VinamilkTableListener listener;
	// current data
	ReportProgressMonthCellDTO currentObject;
	// check detail screen
	boolean isDetailScreen = false;

	public TBHVReportProgressMonthDetailRow(Context context, View aRow, boolean isDetailScreen) {
		super(context);
		tableParent = aRow;
		_context = context;
		this.isDetailScreen = isDetailScreen;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_report_progress_month_detail_row, this);
		row = (TableRow) view.findViewById(R.id.row);
		row.setOnClickListener(this);
		tvNameNPP = (TextView) view.findViewById(R.id.tvNameNPP);
		if (!isDetailScreen) {
			tvNameNPP.setGravity(Gravity.CENTER);
			tvNameNPP.setOnClickListener(this);
		} else {
			tvNameNPP.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
			tvNameNPP.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}
		tvNameGSNPP = (TextView) view.findViewById(R.id.tvNameGSNPP);
		if (!isDetailScreen) {
			tvNameGSNPP.setOnClickListener(this);
		} else {
			tvNameGSNPP.setTextColor(ImageUtil.getColor(R.color.BLACK));
		}
		tvAmountPLAN = (TextView) view.findViewById(R.id.tvAmountPLAN);
		tvAmountDONE = (TextView) view.findViewById(R.id.tvAmountDONE);
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
	public void renderLayoutForRow(ReportProgressMonthCellDTO object, int pecentStandar) {
		currentObject = object;
		if (this.isDetailScreen) {
			tvNameNPP.setText(object.staffOwnerName);
		} else {
			tvNameNPP.setText(object.staffOwnerCode);
		}
		tvNameGSNPP.setText(object.staffName);
		tvAmountPLAN.setText(StringUtil.parseAmountMoney(object.amountPlan));
		tvAmountDONE.setText(StringUtil.parseAmountMoney(object.amountDone));
		tvAmountProgress.setText(object.progressAmountDone + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (object.progressAmountDone < pecentStandar) {
			tvAmountProgress.setTextColor(ImageUtil.getColor(R.color.RED));
		}
		tvAmountRemain.setText(StringUtil.parseAmountMoney(object.amountRemain));
		tvSKUPlan.setText(StringUtil.parseTwoDigitsDecimalCharacter(object.numSKUPlan));
		tvSKUDone.setText(StringUtil.parseTwoDigitsDecimalCharacter(object.numSKUDone));

	}
	/**
	 * 
	*  render layout for row with report detail NVBH
	*  @param object
	*  @param pecentStandar
	*  @return: void
	*  @throws:
	*  @author: HaiTC3
	*  @date: Jan 10, 2013
	 */
	public void renderLayoutForRowDetail(ReportProgressMonthCellDTO object, int pecentStandar) {
		currentObject = object;
		tvNameNPP.setText(object.staffOwnerName);
		tvNameGSNPP.setText(object.staffName);
		tvAmountPLAN.setText(StringUtil.parseAmountMoney(object.amountPlan));
		tvAmountDONE.setText(StringUtil.parseAmountMoney(object.amountDone));
		tvAmountProgress.setText(object.progressAmountDone + StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (object.progressAmountDone < pecentStandar) {
			tvAmountProgress.setTextColor(ImageUtil.getColor(R.color.RED));
		}
		tvAmountRemain.setText(StringUtil.parseAmountMoney(object.amountRemain));
		tvSKUPlan.setText(StringUtil.parseTwoDigitsDecimalCharacter(object.numSKUPlan));
		tvSKUDone.setText(StringUtil.parseTwoDigitsDecimalCharacter(object.numSKUDone));
		
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == tvNameNPP && listener != null) {
			this.listener.handleVinamilkTableRowEvent(
					TBHVReportProgressMonthView.ACTION_SHOW_REPORT_PROGRESS_MONT_NPP_DETAIL_CLICK_NPP, tableParent,
					currentObject);
		} else if (v == tvNameGSNPP && listener != null) {
			this.listener.handleVinamilkTableRowEvent(
					TBHVReportProgressMonthView.ACTION_SHOW_REPORT_PROGRESS_MONT_NPP_DETAIL_CLICK_GSNPP, tableParent,
					currentObject);
		}
	}
}
