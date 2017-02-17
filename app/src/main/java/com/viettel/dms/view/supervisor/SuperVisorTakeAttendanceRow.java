/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.AttendanceDTO;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.util.VNMTraceUnexceptionLog;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;
import com.viettel.utils.VTLog;

/**
 * SuperVisor Take Attendance Row
 * SuperVisorTakeAttendanceRow.java
 * @version: 1.0 
 * @since:  08:30:23 20 Jan 2014
 */
@SuppressLint("ViewConstructor")
public class SuperVisorTakeAttendanceRow extends DMSTableRow implements OnClickListener{
	public static final int ACTION_CLICK_CODE_PROGRAM = 1;
	//so thu tu
	TextView tvSTT;
	//ma chuong trinh
	TextView tvCode;
	//ten chuong trinh
	TextView tvName;
	//tu ngay - den ngay
	TextView tvDate;
	ImageView ivVisitCus;
	LinearLayout llvisitCus;
	AttendanceDTO currentDTO;

	public SuperVisorTakeAttendanceRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_supervisor_attendance_row);
		setListener(lis);
		this.setOnClickListener(this);
		tvSTT = (TextView) this.findViewById(R.id.tvPromotionSTT);
		tvCode = (TextView) this.findViewById(R.id.tvPromotionCode);
		tvName = (TextView) this.findViewById(R.id.tvPromotionName);
		tvDate = (TextView) this.findViewById(R.id.tvPromotionDate);
		ivVisitCus = (ImageView) this.findViewById(R.id.ivVisitCus);
		ivVisitCus.setOnClickListener(this);
		llvisitCus = (LinearLayout) this.findViewById(R.id.llvisitCus);
	}
	
	
	/**
	 * 
	*  render du lieu cho row
	*  @author: ThanhNN8
	*  @param position
	*  @param item
	 * @param distance 
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(AttendanceDTO item, String distance) {
		this.currentDTO = item;
		double distance1=Double.parseDouble(distance);
		tvSTT.setText(item.staffCode);
		tvCode.setText(item.name);
		if (!StringUtil.isNullOrEmpty(item.time1)) {
			StringBuilder str = new StringBuilder();
			str.append(DateUtils.convertDateOneFromFormatToAnotherFormat(item.time1, DateUtils.DATE_FORMAT_ATTENDANCE, DateUtils.DATE_FORMAT_HOUR_MINUTE));
			if (item.distance1 >= 1000) {
				float tempDistance = (float) item.distance1 / 1000;
				DecimalFormat df = new DecimalFormat("0.00");
				String formater = df.format(tempDistance);
				str.append(" - (" + formater + " km)");
			} else if (item.distance1 >= 0) {
				str.append(" - (" + item.distance1 + " m)");
			} else {
			}
			
			//duongdt to do ca dong
			if(item.distance1>distance1){
				renderWithColor(ImageUtil.getColor(R.color.RED));
				//tvName.setTextColor(ImageUtil.getColor(R.color.RED));
			}
			
			tvName.setText(str.toString());
		} 
		
		if(!item.onTime && item.time2 != null && !StringUtil.isNullOrEmpty(item.time2)) {
			tvDate.setTextColor(ImageUtil.getColor(R.color.RED));
			StringBuilder str = new StringBuilder();
			str.append(DateUtils.convertDateOneFromFormatToAnotherFormat(item.time2, DateUtils.DATE_FORMAT_ATTENDANCE, DateUtils.DATE_FORMAT_HOUR_MINUTE));
			if (item.distance2 >= 1000) {
				float tempDistance = (float) item.distance2 / 1000;
				DecimalFormat df = new DecimalFormat("0.00");
				String formater = df.format(tempDistance);
				str.append(" - (" + formater + " km)");
			} else if (item.distance2 >= 0) {
				str.append(" - (" + item.distance2 + " m)");
			} else {
				
			}
			
			if(item.distance2>distance1){
				//tvDate.setTextColor(ImageUtil.getColor(R.color.RED));
			}
			renderWithColor(ImageUtil.getColor(R.color.RED));
			tvDate.setText(str.toString());
		}
		
//		if(!item.onTime || item.distance1 < 0) {
//			//Chua xac dinh duoc vi tri nhung trong thoi gian cho phep thi chua hight light
//			renderWithColor(ImageUtil.getColor(R.color.BLACK));
//		}
		
		if(StringUtil.isNullOrEmpty(item.time1) && StringUtil.isNullOrEmpty(item.time2)) {
			ivVisitCus.setVisibility(View.INVISIBLE);
			renderWithColor(ImageUtil.getColor(R.color.RED));
		}
//		tvDate.setText(item.FROM_DATE + " - " + item.TO_DATE);
	}
	
	
	/**
	 * 
	*  render du lieu cho row
	*  @author: ThanhNN8
	*  @param position
	*  @param item
	*  @return: void
	*  @throws:
	 */
	public void renderLayout(int position, AttendanceDTO item, String endHourMinute) {
		this.currentDTO = item;	
//		tvSTT.setText("" + position);
		tvSTT.setText(item.staffCode);
		tvCode.setText(item.name);
		if (!StringUtil.isNullOrEmpty(item.time1)) {
			StringBuilder str = new StringBuilder();
			str.append(DateUtils.convertDateOneFromFormatToAnotherFormat(item.time1, DateUtils.DATE_FORMAT_ATTENDANCE, DateUtils.DATE_FORMAT_HOUR_MINUTE));
			
			if (item.distance1 >= 1000) {
				float tempDistance = (float) item.distance1 / 1000;
				DecimalFormat df = new DecimalFormat("0.00");
				String formater = df.format(tempDistance);
				str.append(" - (" + formater + " km)");
			} else if (item.distance1 >= 0) {
				str.append(" - (" + item.distance1 + " m)");
			} else {
			}
			
			tvName.setText(str.toString());
		} 
		
		if(!item.onTime && item.time2 != null && !StringUtil.isNullOrEmpty(item.time2)) {
			StringBuilder str = new StringBuilder();
			str.append(DateUtils.convertDateOneFromFormatToAnotherFormat(item.time2, DateUtils.DATE_FORMAT_ATTENDANCE, DateUtils.DATE_FORMAT_HOUR_MINUTE));
			if (item.distance2 >= 1000) {
				float tempDistance = (float) item.distance2 / 1000;
				DecimalFormat df = new DecimalFormat("0.00");
				String formater = df.format(tempDistance);
				str.append(" - (" + formater + " km)");
			} else if (item.distance2 >= 0) {
				str.append(" - (" + item.distance2 + " m)");
			} else {
				
			}
			
			tvDate.setText(str.toString());
		}
		
		if(!item.onTime || item.distance1 < 0) {
			//Chua xac dinh duoc vi tri nhung trong thoi gian cho phep thi chua hight light
			if(StringUtil.isNullOrEmpty(item.time1) && StringUtil.isNullOrEmpty(item.time2)) {
				SimpleDateFormat formatter = new SimpleDateFormat(DateUtils.DATE_FORMAT_ATTENDANCE);
				String strCurrentDate = formatter.format(new Date());
				Date currentDate = new Date();
				Date endDate = new Date();
				
				String endTime = strCurrentDate.split(" ")[0] + " " + endHourMinute;
				
				try {
					currentDate = formatter.parse(strCurrentDate);
					endDate = formatter.parse(endTime);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					VTLog.e("UnexceptionLog", VNMTraceUnexceptionLog.getReportFromThrowable(e));
				}
				
				if(currentDate.compareTo(endDate) > 0) {
					renderWithColor(ImageUtil.getColor(R.color.RED));					
				}
			} else {
				renderWithColor(ImageUtil.getColor(R.color.RED));
			}
		}
		
		if(StringUtil.isNullOrEmpty(item.time1) && StringUtil.isNullOrEmpty(item.time2)) {
			ivVisitCus.setVisibility(View.INVISIBLE);
		}
	}
	/**
	 * @author: yennth16
	 * @since: 16:39:09 23-04-2015
	 * @return: void
	 * @throws:  
	 */
	public void renderHeader(String text1, String text2) {
			tvName.setText(text1);
			tvDate.setText(text2);
	}
	public void renderWithColor(int color) {
		tvSTT.setTextColor(color);
		tvCode.setTextColor(color);
		tvName.setTextColor(color);
		tvDate.setTextColor(color);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == ivVisitCus) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.GO_TO_MAP_FROM_REPORT_VISIT_CUSTOMER, this,
					currentDTO);
		}
	}
}
