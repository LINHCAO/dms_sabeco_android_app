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
import android.widget.TableRow;
import android.widget.TextView;

import com.viettel.dms.constants.Constants;
import com.viettel.dms.dto.view.ReportNVBHVisitCustomerDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.sabeco.R;

/**
 * TBHV hien thi thong tin 1 dong bao cao lich su ghe tham khach hang cua NVBH
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.1
 */
public class TBHVReportVisitCustomerOnPlanOfDayRow extends TableRow implements OnClickListener {
	private final Context context;
	private final View view;
	private TextView tvStaffCode;
	private TextView tvStaffName;
	private TextView tvCustomerInfo;
	private TextView tvMiddleInfo;
	private TextView tvEndInfo;
	private TextView tvCurrentInfo;
	private ImageView ivChecked;
	private ImageView ivDeleted;
	private ImageView imgMapTBHV;
	ReportNVBHVisitCustomerDTO myData;

	// table parent
	private TBHVReportVisitCustomerDayOfDetailNPP customerDayOfDetailNPP;

	public TBHVReportVisitCustomerOnPlanOfDayRow(Context con){
		super(con);
		context=con;
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_tbhv_report_visit_customer_on_plan_row, this);
	}
	
	public TBHVReportVisitCustomerOnPlanOfDayRow(Context context, TBHVReportVisitCustomerDayOfDetailNPP listen) {
		this(context);
		customerDayOfDetailNPP=listen;
		setOnClickListener(this);
		tvStaffCode = (TextView) view.findViewById(R.id.tvStaffCode);
		tvStaffName = (TextView) view.findViewById(R.id.tvStaffName);
		tvCustomerInfo = (TextView) view.findViewById(R.id.tvCustomerInfo);
		tvMiddleInfo = (TextView) view.findViewById(R.id.tvMiddleInfo);
		tvEndInfo = (TextView) view.findViewById(R.id.tvEndInfo);
		tvCurrentInfo = (TextView) view.findViewById(R.id.tvCurrentInfo);
		ivDeleted = (ImageView) view.findViewById(R.id.ivDeleted);
		ivChecked = (ImageView) view.findViewById(R.id.ivChecked);
		imgMapTBHV=(ImageView) view.findViewById(R.id.ivMapTBHV);
		imgMapTBHV.setOnClickListener(this);
	}

	/**
	 * 
	 * render layout for row with a GeneralStatisticsViewDTO
	 * 
	 * @author: HaiTC3
	 * @since: 4:51:47 PM | Jun 11, 2012
	 * @param position
	 * @param GeneralStatisticsViewDTO
	 *            item
	 * @return: void
	 * @throws:
	 */
	public void renderLayout(ReportNVBHVisitCustomerDTO dto) {
		myData = dto;
		tvStaffCode.setText(dto.staffCode);
		tvStaffName.setText(dto.staffName);
		String cusInfo = Constants.STR_BLANK;
		if (!StringUtil.isNullOrEmpty(dto.customerCodeStartVisit)) {
			if (dto.customerCodeStartVisit.length() > 3) {
				cusInfo += dto.customerCodeStartVisit.substring(0, 3);
			} else {
				cusInfo += dto.customerCodeStartVisit;
			}

			cusInfo += " - " + dto.customerNameStartVisit + " - (" + dto.timeStartVisit + ")";
		}
		if (!StringUtil.isNullOrEmpty(dto.timeStartVisit)) {
			ivChecked.setVisibility(View.VISIBLE);
			ivDeleted.setVisibility(View.GONE);
			tvCustomerInfo.setText(cusInfo);
		} else {
			ivDeleted.setVisibility(View.VISIBLE);
			ivChecked.setVisibility(View.GONE);
			tvCustomerInfo.setVisibility(View.INVISIBLE);
		}

		tvMiddleInfo.setText(String.valueOf(dto.numCusOrderMiddle) + "/" + String.valueOf(dto.numCustomerVisitedMiddle)
				+ "/" + String.valueOf(dto.maxNumCustomerVisitMiddle));

		tvEndInfo.setText(String.valueOf(dto.numCusOrderEnd) + "/" + String.valueOf(dto.numCustomerVisitedEnd) + "/"
				+ String.valueOf(dto.maxNumCustomerVisitEnd));

		tvCurrentInfo
				.setText(String.valueOf(dto.numCusOrderCurrent) + "/"
						+ String.valueOf(dto.numCustomerCurrentVisitedInPlan) + "/"
						+ String.valueOf(dto.numTotalCustomerVisit));
		boolean highLight=false;
		if (dto.numTotalCustomerVisit > 0) {
			if (dto.isHighLightCurrent) {
				tvCurrentInfo.setTextColor(ImageUtil.getColor(R.color.RED));
				highLight=true;
			}

			if (dto.isHighLightEnd) {
				tvEndInfo.setTextColor(ImageUtil.getColor(R.color.RED));
				highLight=true;
			}
			if (dto.isHighLightMiddle) {
				tvMiddleInfo.setTextColor(ImageUtil.getColor(R.color.RED));
				highLight=true;
			}

			if (dto.isOr == ReportNVBHVisitCustomerDTO.CUSTOMER_OUT_PLAN) {
				tvCustomerInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}
		} else {
			ivDeleted.setVisibility(View.INVISIBLE);
			ivChecked.setVisibility(View.INVISIBLE);
		}
		if(highLight){
			tvStaffCode.setTextColor(ImageUtil.getColor(R.color.RED));
			tvStaffName.setTextColor(ImageUtil.getColor(R.color.RED));
		}
	}

//	/**
//	 * Mo ta chuc nang cua ham
//	 * 
//	 * @return: void
//	 * @throws:
//	 * @author: HaiTC3
//	 * @date: Jan 25, 2013
//	 */
//	public void renderNoResultLayout() {
//		tvStaffCode.setVisibility(View.GONE);
//		tvStaffName.setVisibility(View.GONE);
//		tvCustomerInfo.setVisibility(View.GONE);
//		tvMiddleInfo.setVisibility(View.GONE);
//		tvEndInfo.setVisibility(View.GONE);
//		tvCurrentInfo.setVisibility(View.GONE);
//		ivDeleted.setVisibility(View.GONE);
//		ivChecked.setVisibility(View.GONE);
//		llLayout.setVisibility(View.GONE);
//		tvNoResultInfo.setVisibility(View.VISIBLE);
//		tvNoResultInfo.setText(StringUtil.getString(R.string.TEXT_HEADER_TAKE_ATTENDANCE_NO_RESULT));
//	}

	public View getView() {
		return view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==imgMapTBHV){
			customerDayOfDetailNPP.handleVinamilkTableRowEvent(ActionEventConstant.GSNPP_ROUTE_SUPERVISION_MAP_VIEW, this, myData);
		}
	}
}
