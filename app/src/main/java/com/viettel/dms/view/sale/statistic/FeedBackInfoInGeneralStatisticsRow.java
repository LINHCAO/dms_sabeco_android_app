/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.sale.statistic;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viettel.dms.dto.db.FeedBackDTO;
import com.viettel.dms.dto.view.NoteInfoDTO;
import com.viettel.dms.util.StringUtil;
import com.viettel.dms.view.main.BaseFragment;
import com.viettel.sabeco.R;

/**
 * thong tin mot dong thong bao nam trong man hinh thong ke chung
 * 
 * @author: HaiTC3
 * @version: 1.0
 * @since: 1.0
 */
public class FeedBackInfoInGeneralStatisticsRow extends RelativeLayout
		implements OnClickListener {

	// content
	Context context;
	// check box feed back status
	public CheckBox cbFBStatus;
	// display customer name
	public TextView tvCustomerName;
	// customer code
	public TextView tvCustomerCode;
	// feed back type
	public TextView tvFBType;
	// feed back info
	public TextView tvFBInfo;
	// feed back date
	public TextView tvFBDate;
	BaseFragment bfParentListener;

	public FeedBackInfoInGeneralStatisticsRow(Context context,
			AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public FeedBackInfoInGeneralStatisticsRow(Context context) {
		super(context);
		initView(context);
	}

	public FeedBackInfoInGeneralStatisticsRow(Context context,
			AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 
	 * init view for row
	 * 
	 * @author: HaiTC3
	 * @param context
	 * @return: void
	 * @throws:
	 */
	private void initView(Context context) {
		// TODO Auto-generated method stub
		this.context = context;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View row = inflater.inflate(
				R.layout.layout_feed_back_general_statistics_row, this);
		cbFBStatus = (CheckBox) row.findViewById(R.id.cbFBStatus);
		cbFBStatus.setOnClickListener(this);
		tvCustomerName = (TextView) row.findViewById(R.id.tvCustomerName);
		tvCustomerCode = (TextView) row.findViewById(R.id.tvCustomerCode);
		tvFBType = (TextView) row.findViewById(R.id.tvFBType);
		tvFBInfo = (TextView) row.findViewById(R.id.tvFBInfo);
		tvFBDate = (TextView) row.findViewById(R.id.tvFBDate);
	}

	/**
	 * 
	 * set parent listener
	 * 
	 * @author: HaiTC3
	 * @param lis
	 * @return: void
	 * @throws:
	 */
	public void setListener(BaseFragment lis) {
		this.bfParentListener = lis;
	}

	/**
	 * 
	 * update layout for row
	 * 
	 * @author: HaiTC3
	 * @param noteInfo
	 * @return: void
	 * @throws:
	 */
	public void updateLayout(NoteInfoDTO noteInfo) {
		tvFBInfo.setText(noteInfo.feedBack.content);
		
		tvFBDate.setText(noteInfo.feedBack.createDate);
		tvCustomerName.setText(noteInfo.customerDTO.getCustomerName());
		if (noteInfo.customerDTO.getCustomerCode() != null
				&& noteInfo.customerDTO.getCustomerCode().length() > 3) {
			tvCustomerCode.setText(noteInfo.customerDTO.getCustomerCode()
					.substring(0, 3));
		} else {
			tvCustomerCode.setText(noteInfo.customerDTO.getCustomerCode());
		}

		tvFBType.setText(noteInfo.feedBack.getTypeTitle(noteInfo.feedBack.type));
		if (noteInfo.feedBack.status == FeedBackDTO.FEEDBACK_STATUS_STAFF_DONE
				&& !StringUtil.isNullOrEmpty(noteInfo.feedBack.doneDate)) {
			cbFBStatus.setChecked(true);
			cbFBStatus.setEnabled(false);
		} else {
			cbFBStatus.setChecked(false);
			cbFBStatus.setEnabled(true);
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
		if (v == cbFBStatus && bfParentListener != null) {
			this.bfParentListener.onClick(v);
		}
	}
}
