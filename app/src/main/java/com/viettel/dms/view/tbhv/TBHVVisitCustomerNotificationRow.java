/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.dms.view.tbhv;

import java.text.DateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.viettel.dms.dto.view.TBHVVisitCustomerNotificationDTO.TBHVVisitCustomerNotificationItem;
import com.viettel.dms.global.ActionEventConstant;
import com.viettel.dms.util.DateUtils;
import com.viettel.dms.util.ImageUtil;
import com.viettel.dms.view.control.DMSTableRow;
import com.viettel.dms.view.listener.VinamilkTableListener;
import com.viettel.sabeco.R;

/**
 * Mo ta muc dich cua class
 * 
 * @author: TamPQ
 * @version: 1.0
 * @since: 1.0
 */
public class TBHVVisitCustomerNotificationRow extends DMSTableRow implements OnClickListener {
	TextView tvNPPCode;
	TextView tvGSNPP;
	TextView tvNumCus;
	TextView tv930;
	TextView tv1200;
	TextView tv1600;
	TextView tvNow;
	private TBHVVisitCustomerNotificationItem item;

	/**
	 * @param context
	 */
	public TBHVVisitCustomerNotificationRow(Context context, VinamilkTableListener lis) {
		super(context, R.layout.layout_visit_cus_noti_row);
		setListener(lis);
		this.setOnClickListener(this);
		tvNPPCode = (TextView) this.findViewById(R.id.tvNPP);
		tvNPPCode.setOnClickListener(this);
		tvGSNPP = (TextView) this.findViewById(R.id.tvGSNPP);
		tvGSNPP.setOnClickListener(this);
		tvNumCus = (TextView) this.findViewById(R.id.tvNumCus);
		tv930 = (TextView) this.findViewById(R.id.tv930);
		tv1200 = (TextView) this.findViewById(R.id.tv1200);
		tv1600 = (TextView) this.findViewById(R.id.tv1600);
		tvNow = (TextView) this.findViewById(R.id.tvNow);
	}

	@Override
	public void onClick(View v) {
		if (v == tvNPPCode || v == tvGSNPP) {
			listener.handleVinamilkTableRowEvent(ActionEventConstant.TBHV_SHOW_REPORT_VISIT_CUSTOMER_DETAIL_NPP, this,
					item);
		}
	}

	/**
	 * render du lieu
	 * 
	 * @author: TamPQ
	 * @param dt_end
	 * @param dt_middle
	 * @param dt_start
	 * @param tbhvVisitCustomerNotificationItem
	 * @return: voidvoid
	 * @throws:
	 */
	public void render(TBHVVisitCustomerNotificationItem item, String dt_start, String dt_middle, String dt_end) {
		this.item = item;
		tvNPPCode.setText(item.nvbhShopCode);
		tvGSNPP.setText(item.gsnppStaffName);
		tvNumCus.setText("" + item.numNvbh);
		DateFormat formatter = DateUtils.defaultHourMinute;
		try {
			Date time930 = DateUtils.parseHourMinute(dt_start);
			Date time1200 = DateUtils.parseHourMinute(dt_middle);
			Date time1600 = DateUtils.parseHourMinute(dt_end);
			Date timeNow = DateUtils.parseHourMinute(formatter.format(new Date()));

			if (timeNow.before(time930)) {
				tv930.setText("0");
				tv1200.setText("0");
				tv1600.setText("0");
				tvNow.setText("0");
			} else if (timeNow.after(time930) && timeNow.before(time1200)) {
				tv930.setText("" + (item.numNvbh - item.numNvbh_right_930));
				if ((item.numNvbh - item.numNvbh_right_930) > 0) {
					tv930.setTextColor(ImageUtil.getColor(R.color.RED));
				}
				tv1200.setText("0");
				tv1600.setText("0");
				tvNow.setText("" + (item.numNvbh - item.numNvbh_right_now));
				if ((item.numNvbh - item.numNvbh_right_now) > 0) {
					tvNow.setTextColor(ImageUtil.getColor(R.color.RED));
				}
			} else if (timeNow.after(time1200) && timeNow.before(time1600)) {
				tv930.setText("" + (item.numNvbh - item.numNvbh_right_930));
				if ((item.numNvbh - item.numNvbh_right_930) > 0) {
					tv930.setTextColor(ImageUtil.getColor(R.color.RED));
				}

				tv1200.setText("" + (item.numNvbh - item.numNvbh_right_1200));
				if ((item.numNvbh - item.numNvbh_right_1200) > 0) {
					tv1200.setTextColor(ImageUtil.getColor(R.color.RED));
				}
				tv1600.setText("0");
				tvNow.setText("" + (item.numNvbh - item.numNvbh_right_now));
				if ((item.numNvbh - item.numNvbh_right_now) > 0) {
					tvNow.setTextColor(ImageUtil.getColor(R.color.RED));
				}
			} else {
				tv930.setText("" + (item.numNvbh - item.numNvbh_right_930));
				if ((item.numNvbh - item.numNvbh_right_930) > 0) {
					tv930.setTextColor(ImageUtil.getColor(R.color.RED));
				}

				tv1200.setText("" + (item.numNvbh - item.numNvbh_right_1200));
				if ((item.numNvbh - item.numNvbh_right_1200) > 0) {
					tv1200.setTextColor(ImageUtil.getColor(R.color.RED));
				}
				tv1600.setText("" + (item.numNvbh - item.numNvbh_right_1600));
				if ((item.numNvbh - item.numNvbh_right_1600) > 0) {
					tv1600.setTextColor(ImageUtil.getColor(R.color.RED));
				}
				tvNow.setText("" + (item.numNvbh - item.numNvbh_right_now));
				if ((item.numNvbh - item.numNvbh_right_now) > 0) {
					tvNow.setTextColor(ImageUtil.getColor(R.color.RED));
				}
			}

		} catch (Exception e) {
		}

	}
}
