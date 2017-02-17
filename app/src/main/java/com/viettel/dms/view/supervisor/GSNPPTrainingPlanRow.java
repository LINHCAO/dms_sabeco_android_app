/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.supervisor;

import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * row cua man hinh ke hoach huan luyen
 * 
 * @author hieunq1
 */
public class GSNPPTrainingPlanRow extends LinearLayout implements OnClickListener {
	private View view;

	protected VinamilkTableListener listener;

	private TextView tvDayT2;

	private TextView tvDayT3;

	private TextView tvDayT4;

	private TextView tvDayT5;

	private TextView tvDayT6;

	private TextView tvDayCN;

	private TextView tvNameT2;

	private TextView tvNameT3;

	private TextView tvNameT4;

	private TextView tvNameT6;

	private TextView tvNameT5;

	private TextView tvNameCN;

	private TextView tvDayT7;

	private TextView tvNameT7;

	private LinearLayout llT2;

	private LinearLayout llT3;

	private LinearLayout llT4;

	private LinearLayout llT5;

	private LinearLayout llT6;

	private LinearLayout llT7;

	private LinearLayout llCN;

	private GSNPPTrainingPlanIem[] data;

	public VinamilkTableListener getListener() {
		return listener;
	}

	public void setListener(VinamilkTableListener listener) {
		this.listener = listener;
	}

	public GSNPPTrainingPlanRow(Context context) {
		super(context);
		LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = vi.inflate(R.layout.layout_plan_train_result_report_row, this);

		llT2 = (LinearLayout) view.findViewById(R.id.llT2);
		llT3 = (LinearLayout) view.findViewById(R.id.llT3);
		llT4 = (LinearLayout) view.findViewById(R.id.llT4);
		llT5 = (LinearLayout) view.findViewById(R.id.llT5);
		llT6 = (LinearLayout) view.findViewById(R.id.llT6);
		llT7 = (LinearLayout) view.findViewById(R.id.llT7);
		llCN = (LinearLayout) view.findViewById(R.id.llCN);

		llT2.setOnClickListener(this);
		llT3.setOnClickListener(this);
		llT4.setOnClickListener(this);
		llT5.setOnClickListener(this);
		llT6.setOnClickListener(this);
		llT7.setOnClickListener(this);
		llCN.setOnClickListener(this);

		tvDayT2 = (TextView) view.findViewById(R.id.tvDayT2);
		tvDayT3 = (TextView) view.findViewById(R.id.tvDayT3);
		tvDayT4 = (TextView) view.findViewById(R.id.tvDayT4);
		tvDayT5 = (TextView) view.findViewById(R.id.tvDayT5);
		tvDayT6 = (TextView) view.findViewById(R.id.tvDayT6);
		tvDayT7 = (TextView) view.findViewById(R.id.tvDayT7);
		tvDayCN = (TextView) view.findViewById(R.id.tvDayCN);

		tvNameT2 = (TextView) view.findViewById(R.id.tvNameT2);
		tvNameT3 = (TextView) view.findViewById(R.id.tvNameT3);
		tvNameT4 = (TextView) view.findViewById(R.id.tvNameT4);
		tvNameT5 = (TextView) view.findViewById(R.id.tvNameT5);
		tvNameT6 = (TextView) view.findViewById(R.id.tvNameT6);
		tvNameT7 = (TextView) view.findViewById(R.id.tvNameT7);
		tvNameCN = (TextView) view.findViewById(R.id.tvNameCN);

		data = new GSNPPTrainingPlanIem[7];
	}

	public void render(boolean isInMonth, boolean isInDay, int dayOfWeek, int dayOfMonth, GSNPPTrainingPlanIem item) {
		String name = "";
		Date now = DateUtils.getStartTimeOfDay(new Date());
		if (item != null) {
			name = item.staffName;
		}
		if (isInMonth) {
			switch (dayOfWeek) {
			case Calendar.MONDAY: {
				tvDayT2.setText(String.valueOf(dayOfMonth));
				tvNameT2.setText(name);
				if (isInDay) {
					llT2.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llT2.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[0] = item;
				break;
			}
			case Calendar.TUESDAY: {
				tvDayT3.setText(String.valueOf(dayOfMonth));
				tvNameT3.setText(name);
				if (isInDay) {
					llT3.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llT3.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[1] = item;
				break;
			}
			case Calendar.WEDNESDAY: {
				tvDayT4.setText(String.valueOf(dayOfMonth));
				tvNameT4.setText(name);
				if (isInDay) {
					llT4.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llT4.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[2] = item;
				break;
			}
			case Calendar.THURSDAY: {
				tvDayT5.setText(String.valueOf(dayOfMonth));
				tvNameT5.setText(name);
				if (isInDay) {
					llT5.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llT5.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[3] = item;
				break;
			}
			case Calendar.FRIDAY: {
				tvDayT6.setText(String.valueOf(dayOfMonth));
				tvNameT6.setText(name);
				if (isInDay) {
					llT6.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llT6.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[4] = item;
				break;
			}
			case Calendar.SATURDAY: {
				tvDayT7.setText(String.valueOf(dayOfMonth));
				tvNameT7.setText(name);
				if (isInDay) {
					llT7.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llT7.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[5] = item;
				break;
			}
			case Calendar.SUNDAY: {
				tvDayCN.setText(String.valueOf(dayOfMonth));
				tvNameCN.setText(name);
				if (isInDay) {
					llCN.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
				} else if (dayOfMonth < DateUtils.getDay(now)) {
					llCN.setBackgroundResource(R.color.LIGHT_GRAY_BG);
				}
				data[6] = item;
				break;
			}
			default:
				break;
			}
		} else {
			switch (dayOfWeek) {
			case Calendar.MONDAY: {
				tvDayT2.setText("");
				tvNameT2.setText("");
				break;
			}
			case Calendar.TUESDAY: {
				tvDayT3.setText("");
				tvNameT3.setText("");
				break;
			}
			case Calendar.WEDNESDAY: {
				tvDayT4.setText("");
				tvNameT4.setText("");
				break;
			}
			case Calendar.THURSDAY: {
				tvDayT5.setText("");
				tvNameT5.setText("");
				break;
			}
			case Calendar.FRIDAY: {
				tvDayT6.setText("");
				tvNameT6.setText("");
				break;
			}
			case Calendar.SATURDAY: {
				tvDayT7.setText("");
				tvNameT7.setText("");
				break;
			}
			case Calendar.SUNDAY: {
				tvDayCN.setText("");
				tvNameCN.setText("");
				break;
			}
			default:
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v == llT2) {
			if (data[0] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[0]);
			}
		}
		if (v == llT3) {
			if (data[1] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[1]);
			}
		}
		if (v == llT4) {
			if (data[2] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[2]);
			}
		}
		if (v == llT5) {
			if (data[3] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[3]);
			}
		}
		if (v == llT6) {
			if (data[4] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[4]);
			}
		}
		if (v == llT7) {
			if (data[5] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[5]);
			}
		}
		if (v == llCN) {
			if (data[6] != null) {
				listener.handleVinamilkTableRowEvent(0, v, data[6]);
			}
		}

	}

}
