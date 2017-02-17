/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO.TBHVTrainingPlanItem;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * Ke hoach huan luyen GSNPP Row
 * @author : TamPQ
 * @version: 1.1
 * @since : 1.0
 */
public class TBHVTrainigPlanRow extends LinearLayout implements
		OnClickListener, OnEventControlListener {

	protected OnEventControlListener listener;// listener

	private TextView tvDayCN;// tvDayCN
	private TextView tvNameCN;// tvNameCN
	private LinearLayout llCN;// llCN
	
	// column T2
	private TBHVTrainingDateColumn rlColumnMonday;
	// column T3
	private TBHVTrainingDateColumn rlColumnTuesday;
	// column T4
	private TBHVTrainingDateColumn rlColumnWed;
	// column T5
	private TBHVTrainingDateColumn rlColumnThur;
	// column T6
	private TBHVTrainingDateColumn rlColumnFriday;
	// column T7
	private TBHVTrainingDateColumn rlColumnSat;
	
	private TBHVTrainingPlanItem[] data;// data

	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	public TBHVTrainigPlanRow(Context context) {
		super(context);
		initView(context);
	}
	
	public TBHVTrainigPlanRow(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);

	}
	private void initView(Context mContext){
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_tbhv_gsnpp_trainning_result_report_row, this, true);
		// column T2
		rlColumnMonday = (TBHVTrainingDateColumn) view.findViewById(R.id.rlColumnMonday);
		rlColumnMonday.setListener(this);
		// column T3
		rlColumnTuesday = (TBHVTrainingDateColumn) view.findViewById(R.id.rlColumnTuesday);
		rlColumnTuesday.setListener(this);
		// column T4
		rlColumnWed = (TBHVTrainingDateColumn) view.findViewById(R.id.rlColumnWed);
		rlColumnWed.setListener(this);
		// column T5
		rlColumnThur = (TBHVTrainingDateColumn) view.findViewById(R.id.rlColumnThur);
		rlColumnThur.setListener(this);
		// column T6
		rlColumnFriday = (TBHVTrainingDateColumn) view.findViewById(R.id.rlColumnFriday);
		rlColumnFriday.setListener(this);
		// column T7
		rlColumnSat = (TBHVTrainingDateColumn) findViewById(R.id.rlColumnSat);
		rlColumnSat.setListener(this);
		rlColumnSat.setColorDay();
		// column CN
		llCN = (LinearLayout) view.findViewById(R.id.rlCN);
		llCN.setOnClickListener(this);
		tvDayCN = (TextView) view.findViewById(R.id.tvDayCN);
		tvNameCN = (TextView) view.findViewById(R.id.tvNameCN);
		
		data = new TBHVTrainingPlanItem[7];
	}

	/**
	*  Hien thi tip ten GSNPP
	*  @author: TruongHN
	*  @return: void
	*  @throws:
	 */
	public void showTipGSNPP(){
		rlColumnMonday.showTipGSNPP();
		rlColumnTuesday.showTipGSNPP();
		rlColumnWed.showTipGSNPP();
		rlColumnThur.showTipGSNPP();
		rlColumnFriday.showTipGSNPP();
		rlColumnSat.showTipGSNPP();
	}
	/**
	 *  Hien thi ten NVBH
	 *  @author: TruongHN
	 *  @return: void
	 *  @throws:
	 */
	public void showNameSalePerson(){
		rlColumnMonday.showNameSalePerson();
		rlColumnTuesday.showNameSalePerson();
		rlColumnWed.showNameSalePerson();
		rlColumnThur.showNameSalePerson();
		rlColumnFriday.showNameSalePerson();
		rlColumnSat.showNameSalePerson();
	}
	
	/**
	 * render
	 * @author: TamPQ
	 * @return: void
	 * @throws:
	 */
	public void render(boolean isInMonth, boolean isInDay, int dayOfWeek,
			int dayOfMonth, TBHVTrainingPlanItem item, GSNPPTrainingPlanIem planGsnppItem) {
		switch (dayOfWeek) {
			case Calendar.MONDAY: {
				rlColumnMonday.render(isInMonth, isInDay, dayOfWeek, dayOfMonth, item, planGsnppItem);
				data[0] = item;
				break;
			}
			case Calendar.TUESDAY: {
				rlColumnTuesday.render(isInMonth, isInDay, dayOfWeek, dayOfMonth, item, planGsnppItem);	
				data[1] = item;
				break;
			}
			case Calendar.WEDNESDAY: {
				rlColumnWed.render(isInMonth, isInDay, dayOfWeek, dayOfMonth, item, planGsnppItem);
				data[2] = item;
				break;
			}
			case Calendar.THURSDAY: {
				rlColumnThur.render(isInMonth, isInDay, dayOfWeek, dayOfMonth, item, planGsnppItem);
				data[3] = item;
				break;
			}
			case Calendar.FRIDAY: {
				rlColumnFriday.render(isInMonth, isInDay, dayOfWeek, dayOfMonth, item, planGsnppItem);
				data[4] = item;
				break;
			}
			case Calendar.SATURDAY: {
				rlColumnSat.render(isInMonth, isInDay, dayOfWeek, dayOfMonth, item, planGsnppItem);
				data[5] = item;
				break;
			}
			case Calendar.SUNDAY: {
				String name = "";
				data[6] = item;
				if (isInMonth) {
					Date now = DateUtils.getStartTimeOfDay(new Date());
					if (item != null) {
						name = item.staffName;
					}
					tvDayCN.setText(String.valueOf(dayOfMonth));
					tvNameCN.setText(name);
					if (isInDay) {
						llCN.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
					}else if (dayOfMonth < DateUtils.getDay(now)){
						llCN.setBackgroundResource(R.color.LIGHT_GRAY_BG);
					}
				}else{
					tvDayCN.setText("");
					tvNameCN.setText("");
				}
				break;
			}
		default:
			break;
		}
	}


	@Override
	public void onEvent(int eventType, View control, Object data) {
		if (control == rlColumnMonday || control == rlColumnTuesday ||
				control == rlColumnWed || control == rlColumnThur ||
				control == rlColumnFriday || control == rlColumnSat){
			if (eventType == TBHVTrainingDateColumn.SALE_PERSON_NAME_CLICK){
				if (listener != null){
					listener.onEvent(TBHVTrainingDateColumn.SALE_PERSON_NAME_CLICK, this, data);
				}
			}else if (eventType == TBHVTrainingDateColumn.SUPERVISOR_NAME_CLICK){
				if (listener != null){
					listener.onEvent(TBHVTrainingDateColumn.SUPERVISOR_NAME_CLICK, this, data);
				}
			}else if (eventType == TBHVTrainingDateColumn.SUPERVISOR_ICON_CLICK){
				if (listener != null){
					listener.onEvent(TBHVTrainingDateColumn.SUPERVISOR_ICON_CLICK, this, data);
				}
			}else if (eventType == TBHVTrainingDateColumn.COLUMN_CLICK){
				if (listener != null){
					listener.onEvent(TBHVTrainingDateColumn.COLUMN_CLICK, this, data);
				}
			}
		}
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
