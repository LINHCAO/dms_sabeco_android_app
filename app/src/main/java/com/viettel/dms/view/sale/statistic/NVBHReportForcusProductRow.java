/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.ForcusProductOfNVBHDTO;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * display product forcus infor on row
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class NVBHReportForcusProductRow extends TableRow implements
		OnClickListener {
	private final Context context;
	View view;
	// stt
	private TextView tvSTT;
	// product code
	private TextView tvProductCode;
	// product name
	private TextView tvProductName;
	// product industry
	private TextView tvProductIndustry;
	// forcus product type
	private TextView tvForcusProductType;
	// plan amount
	private TextView tvPlanAmount;
	// done amount
	private TextView tvDoneAmount;
	// remain amount
	private TextView tvRemainAmount;
	// progress sale
	private TextView tvProgress;
	// text notify null
	private TextView tvNotifyNull;

	public NVBHReportForcusProductRow(Context con){
		super(con);
		context = con;
	}
	
	public NVBHReportForcusProductRow(Context context, View aRow,
			boolean isTotalRow) {
		this(context);
		LayoutInflater vi = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (isTotalRow) {
			view = vi.inflate(
					R.layout.layout_nvbh_report_forcus_product_row_total, this);
			this.initViewControlTotalRow(view);
		} else {
			view = vi.inflate(R.layout.layout_nvbh_report_forcus_product_row,
					this);
			this.initViewControlNomalRow(view);
		}
		setOnClickListener(this);

	}

	/**
	 * 
	 * init view control for nomal row (not total row)
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void initViewControlNomalRow(View v) {
		tvSTT = (TextView) v.findViewById(R.id.tvSTT);
		tvProductCode = (TextView) v.findViewById(R.id.tvProductCode);
		tvProductName = (TextView) v.findViewById(R.id.tvProductName);
		tvProductIndustry = (TextView) v.findViewById(R.id.tvProductIndustry);
		tvForcusProductType = (TextView) v
				.findViewById(R.id.tvTypeForcusProduct);
		tvPlanAmount = (TextView) v.findViewById(R.id.tvPlanAmount);
		tvDoneAmount = (TextView) v.findViewById(R.id.tvDoneAmount);
		tvRemainAmount = (TextView) v.findViewById(R.id.tvRemainAmount);
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
		tvNotifyNull = (TextView) v.findViewById(R.id.tvNotifyNull);
	}

	/**
	 * 
	 * init view control for total row
	 * 
	 * @param v
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void initViewControlTotalRow(View v) {
		tvForcusProductType = (TextView) v
				.findViewById(R.id.tvTypeForcusProduct);
		tvPlanAmount = (TextView) v.findViewById(R.id.tvPlanAmount);
		tvDoneAmount = (TextView) v.findViewById(R.id.tvDoneAmount);
		tvRemainAmount = (TextView) v.findViewById(R.id.tvRemainAmount);
		tvProgress = (TextView) v.findViewById(R.id.tvProgress);
	}

	/**
	 * 
	 * render layout for row product info
	 * 
	 * @param productInfo
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void renderLayoutForRowProductInfo(
			ForcusProductOfNVBHDTO productInfo, int stt, float pecentStandar) {
		tvSTT.setText(String.valueOf(stt));
		tvProductCode.setText(productInfo.productCode);
		tvProductName.setText(productInfo.productName);
		tvProductIndustry.setText(productInfo.productIndustry);
		tvForcusProductType.setText(productInfo.typeProductFocus);
		tvPlanAmount.setText(StringUtil
				.parseAmountMoney(productInfo.planAmount));
		tvDoneAmount.setText(StringUtil
				.parseAmountMoney(productInfo.doneAmount));
		tvRemainAmount.setText(StringUtil
				.parseAmountMoney(productInfo.remainAmount));
		tvProgress.setText(productInfo.progress
				+ StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (productInfo.progress < pecentStandar) {
			tvProgress.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

	/**
	 * 
	 * render layout for row total
	 * 
	 * @param totalTypeForcus
	 * @param pecentStandar
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void renderLayoutForRowTotal(ForcusProductOfNVBHDTO totalTypeForcus,
			float pecentStandar) {
		if (!StringUtil.isNullOrEmpty(totalTypeForcus.typeProductFocus)
				&& totalTypeForcus.typeProductFocus.indexOf(StringUtil
						.getString(R.string.TEXT_HEADER_MENU_MHTT)) >= 0) {
			tvForcusProductType.setText(totalTypeForcus.typeProductFocus);
		} else {
			tvForcusProductType.setText(StringUtil
					.getString(R.string.TEXT_HEADER_MENU_MHTT)
					+ totalTypeForcus.typeProductFocus);
		}
		tvPlanAmount.setText(StringUtil
				.parseAmountMoney(totalTypeForcus.planAmount));
		tvDoneAmount.setText(StringUtil
				.parseAmountMoney(totalTypeForcus.doneAmount));
		tvRemainAmount.setText(StringUtil
				.parseAmountMoney(totalTypeForcus.remainAmount));
		tvProgress.setText(totalTypeForcus.progress
				+ StringUtil.getString(R.string.TEXT_SYMBOL_PERCENT));
		if (totalTypeForcus.progress < pecentStandar) {
			tvProgress.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

	/**
	 * 
	 * render layout for row notify null
	 * 
	 * @return: void
	 * @throws:
	 * @author: HaiTC3
	 * @date: Jan 2, 2013
	 */
	public void renderLayoutForNotifyNull() {
		tvSTT.setVisibility(View.GONE);
		tvProductCode.setVisibility(View.GONE);
		tvProductName.setVisibility(View.GONE);
		tvProductIndustry.setVisibility(View.GONE);
		tvForcusProductType.setVisibility(View.GONE);
		tvPlanAmount.setVisibility(View.GONE);
		tvDoneAmount.setVisibility(View.GONE);
		tvRemainAmount.setVisibility(View.GONE);
		tvProgress.setVisibility(View.GONE);
		tvNotifyNull.setVisibility(View.VISIBLE);
		tvNotifyNull.setText(StringUtil
				.getString(R.string.TEXT_NOTIFY_REPORT_MHTT_NULL)
				+ Constants.STR_SPACE + DateUtils.getCurrentDate());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
