/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

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
import com.viettel.dms.view.control.VinamilkTableView;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

 
/**
 * Hien thi thong tin 1 dong bao cao lich su ghe tham khach hang cua NVBH
 * 
 * GSBHReportVisitCustomerOnPlanOfDayRow.java
 * @author: QuangVT
 * @version: 1.0 
 * @since:  5:50:58 PM Dec 4, 2013
 */
public class GSNPPReportVisitCustomerOnPlanOfDayRow extends TableRow implements OnClickListener {
	/*--------------- DEFAULT ---------------*/
	// --- Context
	//private Context mContext;
	// --- View of row
	private final View mView;
	
	/*--------------- COLUMNS ---------------*/
	private TextView tvStaffCode;		// Ma nhan vien
	private TextView tvStaffName;		// Ten nhan vien 
	private TextView tvCustomerInfo;	// Thong tin KH
	private TextView tvMiddleInfo;		// Hien thi thoi gian giua
	private TextView tvEndInfo;			// Hien thi thoi gian cuoi
	private TextView tvCurrentInfo;		// Hien thi thoi gian hien tai
	private ImageView ivVisitCus;		// Icon ghe tham
	private ImageView ivChecked;		// Icon co ghe tham KH truoc tg bat dau
	private ImageView ivDeleted;		// Icon khong co KH dc ghe tham truoc tg bat dau
	
	/*--------------- DTO ---------------*/
	// DTO for render
	private ReportNVBHVisitCustomerDTO myData;

	/*--------------- LISTENER ---------------*/ 
	// Listener for click row
	protected VinamilkTableListener listener;
	
	/*--------------- TABLE ---------------*/
	// table parent
	protected VinamilkTableView mTable;
	// Root

	public GSNPPReportVisitCustomerOnPlanOfDayRow(Context con){
		super(con);
		LayoutInflater vi = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = vi.inflate(R.layout.layout_report_visit_customer_on_plan_row, this);
	}
	
	/**
	 * Ham khoi tao
	 * 
	 * @author QuangVT
	 * @param context
	 * @param aRow
	 */
	public GSNPPReportVisitCustomerOnPlanOfDayRow(Context context, VinamilkTableListener listen) {
		this(context);
		listener=listen;
		setOnClickListener(this);
		// Binding columns
		tvStaffCode 	= (TextView) mView.findViewById(R.id.tvStaffCode);
		tvStaffName 	= (TextView) mView.findViewById(R.id.tvStaffName);
		tvCustomerInfo 	= (TextView) mView.findViewById(R.id.tvCustomerInfo);
		tvMiddleInfo 	= (TextView) mView.findViewById(R.id.tvMiddleInfo);
		tvEndInfo 		= (TextView) mView.findViewById(R.id.tvEndInfo);
		tvCurrentInfo 	= (TextView) mView.findViewById(R.id.tvCurrentInfo);
		ivDeleted  = (ImageView) mView.findViewById(R.id.ivDeleted);
		ivChecked  = (ImageView) mView.findViewById(R.id.ivChecked);
		ivVisitCus = (ImageView) mView.findViewById(R.id.ivVisitCus);
		ivVisitCus.setOnClickListener(this);
	}

	/**
	 * Render layout for row with a GeneralStatisticsViewDTO
	 * 
	 * @author: QuangVT
	 * @since: 5:54:37 PM Dec 4, 2013
	 * @return: void
	 * @throws:  
	 * @param dto
	 */
	public void renderLayout(ReportNVBHVisitCustomerDTO dto) {
		// Save data
		myData = dto;
		
		//------------Render data ------------ //
		//-- Staff Code
		tvStaffCode.setText(dto.staffCode);
		// -- Staff Name
		tvStaffName.setText(dto.staffName);
		// -- Column Start
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

		// --- Column Middle
		final String strNumOrderMid    = String.valueOf(dto.numCusOrderMiddle);
		final String strNumVisitedMid  = String.valueOf(dto.numCustomerVisitedMiddle);
		final String strNumVisitInPlan = String.valueOf(dto.maxNumCustomerVisitMiddle);
		final String strColumnMidInfo  = strNumOrderMid + "/" + strNumVisitedMid + "/" + strNumVisitInPlan;
		tvMiddleInfo.setText(strColumnMidInfo);

		// --- Column End
		final String strNumOrderEnd       = String.valueOf(dto.numCusOrderEnd);
		final String strNumVisitedEnd     = String.valueOf(dto.numCustomerVisitedEnd);
		final String strNumVisitInPlanEnd = String.valueOf(dto.maxNumCustomerVisitEnd);
		final String strColumnEndInfo     = strNumOrderEnd + "/" + strNumVisitedEnd + "/" + strNumVisitInPlanEnd;
		tvEndInfo.setText(strColumnEndInfo);

		// --- Column Current 
		String strColumnCurrInfo = Constants.STR_BLANK;
		if(dto.viewCurrenFollow == ReportNVBHVisitCustomerDTO.FOLLOW_MIDDLE){
			strColumnCurrInfo = strColumnMidInfo;
		}else if(dto.viewCurrenFollow == ReportNVBHVisitCustomerDTO.FOLLOW_END){
			strColumnCurrInfo = strColumnEndInfo;
		}
		tvCurrentInfo.setText(strColumnCurrInfo);

		// Neu khong dinh vi duoc thi khong hien icon visit
		//if (dto.latPosition > 0 && dto.lngPosition > 0) {
		//	ivVisitCus.setVisibility(View.VISIBLE);
		//} else {
		//	ivVisitCus.setVisibility(View.INVISIBLE);
		//}

		//--------- HighLight ---------//
		if (dto.numTotalCustomerVisit > 0) {
			//--HighLight Current
			if (dto.isHighLightCurrent) {
				tvCurrentInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}else{
				tvCurrentInfo.setTextColor(ImageUtil.getColor(R.color.BLACK));
			}
			//--HighLight End
			if (dto.isHighLightEnd) {
				tvEndInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}else{
				tvEndInfo.setTextColor(ImageUtil.getColor(R.color.BLACK));
			}
			//--HighLight Middle
			if (dto.isHighLightMiddle) {
				tvMiddleInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}else{
				tvMiddleInfo.setTextColor(ImageUtil.getColor(R.color.BLACK));
			}
			//--HighLight NVBH
			if (dto.isHighLightNVBH) {
				tvStaffCode.setTextColor(ImageUtil.getColor(R.color.RED));
				tvStaffName.setTextColor(ImageUtil.getColor(R.color.RED));
			}else{
				tvStaffCode.setTextColor(ImageUtil.getColor(R.color.BLACK));
				tvStaffName.setTextColor(ImageUtil.getColor(R.color.BLACK));
			}
			//--HighLight Customer Info
			if (dto.isOr == ReportNVBHVisitCustomerDTO.CUSTOMER_OUT_PLAN) {
				tvCustomerInfo.setTextColor(ImageUtil.getColor(R.color.RED));
			}else{
				tvCustomerInfo.setTextColor(ImageUtil.getColor(R.color.BLACK));
			}
		}
		else{
			ivDeleted.setVisibility(View.INVISIBLE);
			ivChecked.setVisibility(View.INVISIBLE);
		}
	} 
 
	@Override
	public void onClick(View v) {
		if (v == ivVisitCus) {
			final int ACTION = ActionEventConstant.GO_TO_MAP_FROM_REPORT_VISIT_CUSTOMER;
			listener.handleVinamilkTableRowEvent(ACTION, mTable, myData);
		}
	}
}
