/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.util.Date;
import java.util.Vector;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.dto.view.GSNPPTrainingPlanDTO.GSNPPTrainingPlanIem;
import com.viettel.dms.dto.view.TBHVTrainingPlanDTO.TBHVTrainingPlanItem;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.listener.OnEventControlListener;
import com.viettel.sabeco.R;

/**
 * 1 column thu trong huan luyen TBHV
 * 
 * @author: TruongHN3
 * @version: 1.1
 * @since: 1.0 com.viettel.vinamilk.view.tbhv.TBHVTrainingDateColumn
 */
public class TBHVTrainingDateColumn extends RelativeLayout implements OnClickListener {
	// click tren ten NVBH
	public static final int SALE_PERSON_NAME_CLICK = 1;
	// click tren ten GSNPP
	public static final int SUPERVISOR_NAME_CLICK = 2;
	// click tren icon GSNPP
	public static final int SUPERVISOR_ICON_CLICK = 3;
	// click tren column
	public static final int COLUMN_CLICK = 4;

	// icon GSNPP
	private ImageView ivSupervisor;
	// text ngay
	private TextView tvDay;
	// text ho ten
	private TextView tvName;
	// text tip ho ten GSNPP
	private TextView tvTipName;

	// view
	private RelativeLayout rlTrainingDate;

	// thong tin cua 1 colunmn
	private TBHVTrainingPlanItem item;

	// thong tin GSNPP cua 1 colunmn
	private GSNPPTrainingPlanIem planGsnppItem;

	// listener
	protected OnEventControlListener listener;

	public TBHVTrainingDateColumn(Context context) {
		super(context);
		initView(context);
	}

	public TBHVTrainingDateColumn(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	private void initView(Context mContext) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.layout_tbhv_training_report_column_row, this, true);
		tvDay = (TextView) view.findViewById(R.id.tvDay);
		tvName = (TextView) view.findViewById(R.id.tvName);
		tvName.setOnClickListener(this);
		tvTipName = (TextView) view.findViewById(R.id.tvTipName);
		tvTipName.setOnClickListener(this);
		ivSupervisor = (ImageView) view.findViewById(R.id.ivSupervisor);
		ivSupervisor.setOnClickListener(this);
		rlTrainingDate = (RelativeLayout) view.findViewById(R.id.rlTrainingDate);
		rlTrainingDate.setOnClickListener(this);
	}

	public void setInfoView() {

	}

	public void setColorDay() {
		tvDay.setTextColor(ImageUtil.getColor(R.color.RED));
	}

	/**
	 * Hien thi tip ten GSNPP
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void showTipGSNPP() {
		tvName.setVisibility(View.GONE);
		String name = "";
		if (this.item != null) {
			name = this.item.staffName;
		}
		if (!StringUtil.isNullOrEmpty(name)) {
			tvTipName.setText(name);
			tvTipName.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * Hien thi ten NVBH
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	public void showNameSalePerson() {
		tvTipName.setVisibility(View.GONE);
		tvName.setVisibility(View.VISIBLE);
		String name = "";
		if (this.planGsnppItem != null) {
			name = this.planGsnppItem.staffName;
		}
		if (!StringUtil.isNullOrEmpty(name)) {
			tvName.setText(name);
		}
	}

	/**
	 * Neu la ngay khong phai thang hien tai thi clear het text
	 * 
	 * @author: TruongHN
	 * @return: void
	 * @throws:
	 */
	private void clearText() {
		tvDay.setText("");
		tvName.setText("");
		tvName.setVisibility(View.GONE);
		tvTipName.setVisibility(View.GONE);
		ivSupervisor.setVisibility(View.GONE);
	}

	public void render(boolean isInMonth, boolean isInDay, int dayOfWeek, int dayOfMonth,
			TBHVTrainingPlanItem item, GSNPPTrainingPlanIem planGsnppItem) {
		this.item = item;
		this.planGsnppItem = planGsnppItem;
		Date now = DateUtils.getStartTimeOfDay(new Date());
		if (isInMonth) {
			tvDay.setText(String.valueOf(dayOfMonth));
			if (item != null && item.staffId != 0) {
				ivSupervisor.setVisibility(View.VISIBLE);
			} else {
				ivSupervisor.setVisibility(View.GONE);
			}
			if (planGsnppItem != null) {
				tvName.setText(planGsnppItem.staffName);
			} else {
				tvName.setText("");
			}
			if (isInDay) {
				rlTrainingDate.setBackgroundResource(R.color.CALENDAR_CURRENT_DATE);
			} else if (dayOfMonth < DateUtils.getDay(now)) {
				rlTrainingDate.setBackgroundResource(R.color.LIGHT_GRAY_BG);
			}
		} else {
			clearText();
		}
	}

	public void setListener(OnEventControlListener listener) {
		this.listener = listener;
	}

	@Override
	public void onClick(View arg0) {
		if (arg0 == tvName) {
			if (listener != null) {
				Vector<Object> vt = new Vector<Object>();
				vt.add(this.planGsnppItem);
				if (item != null) {
					vt.add(this.item);
				}
				listener.onEvent(SALE_PERSON_NAME_CLICK, this, vt);
			}
		} else if (arg0 == tvTipName) {
			if (listener != null) {
				listener.onEvent(SUPERVISOR_NAME_CLICK, this, this.item);
			}
		} else if (arg0 == rlTrainingDate) {
			if (listener != null) {
				listener.onEvent(COLUMN_CLICK, this, this.item);
			}
		} else if (arg0 == ivSupervisor) {
			if (listener != null) {
				listener.onEvent(SUPERVISOR_ICON_CLICK, this, this.item);
			}
		}

	}
}
